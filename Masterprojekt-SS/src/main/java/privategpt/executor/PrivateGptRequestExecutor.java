package privategpt.executor;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import privategpt.dto.DocumentEvaluation;
import privategpt.dto.DocumentSoll;
import privategpt.dto.Order;
import privategpt.process.InvoiceMapper;
import privategpt.process.PrivateGptRequestingProcess;
import privategpt.reader.OcrReader;

public class PrivateGptRequestExecutor {
	// hier eigene DB-Connection
	private static final String JDBC_URL = "jdbc:mysql://localhost:3306/whs";
	private static final String USERNAME = "root";
	private static final String PASSWORD = "kaan1234";

	public static void main(String[] args) {
		List<String> invoicePaths = new ArrayList<String>();

		File directory = new File("C:\\Eclipse\\workspace\\Masterprojekt-SS\\src\\main\\resources\\");
		if (directory.exists() && directory.isDirectory()) {
			File[] files = directory.listFiles();
			if (files != null) {
				for (File file : files) {
					if (file.isFile()) {
						invoicePaths.add(file.getAbsolutePath());
					}
				}
			}

			Map<String, DocumentSoll> documentSollMap = getDocumentSollList();
			List<DocumentEvaluation> evaluations = new ArrayList<DocumentEvaluation>();
			List<Order> orders = new ArrayList<Order>();

			invoicePaths.forEach(i -> {
				String ocrFile = OcrReader.readPdf(i);
				Order order = InvoiceMapper
						.mapJsonToInvoice(PrivateGptRequestingProcess.getPrivateGptResponseFromPdfRequest(ocrFile));

				orders.add(order);
				DocumentEvaluation eval = new DocumentEvaluation();
				eval.setPath(i);
				eval.setOrder(order);
				evaluations.add(eval);
			});

			System.out.println(orders.toString());

			evaluations.forEach(eval -> {
				/*
				 * Auftragsnummer Bestelldatum Verkäufer Bearbeiter Lieferadresse - Name,v
				 * Strassse, Stadt, Land Rechnungsadresse - Name, Strassse, Stadt, Land Artikel-
				 * count list size artikel - Menge equals jeweils Artikel - Gesamtpreis jeweils
				 * Nettobetrag Gesamtpreis Zahlungsbedingung
				 */
				int gesamt = 10; // es gibt 10 Vergleichspunkte:
				int evalCorrect = 0;
				DocumentSoll soll = documentSollMap.get(eval.getPath());
				
				if (soll == null || soll.equals(null)) {
					return; 
				}
				
				Order sollOrder = InvoiceMapper.mapJsonToInvoice(soll.getSollJson());

				// evaluation
				if (eval.getOrder() != null) {

					if (sollOrder.getItems().equals(eval.getOrder().getItems()))
						evalCorrect += 1;

					if (sollOrder.getOrderNumber().equals(eval.getOrder().getOrderNumber()))
						evalCorrect += 1;

					if (sollOrder.getOrderDate().equals(eval.getOrder().getOrderDate()))
						evalCorrect += 1;

					if (sollOrder.getNetAmount() == eval.getOrder().getNetAmount())
						evalCorrect += 1;

					if (sollOrder.getSeller().equals(eval.getOrder().getSeller()))
						evalCorrect += 1;

					if (sollOrder.getHandler().equals(eval.getOrder().getHandler()))
						evalCorrect += 1;

					if (sollOrder.getItems().equals(eval.getOrder().getItems()))
						evalCorrect += 1;

					if (sollOrder.getNetAmount() == eval.getOrder().getNetAmount())
						evalCorrect += 1;

					if (sollOrder.getTotalAmount() == eval.getOrder().getTotalAmount())
						evalCorrect += 1;

					if (sollOrder.getPaymentTerms().equals(eval.getOrder().getPaymentTerms()))
						evalCorrect += 1;
				
				//
				double percentage = (double) ((double) evalCorrect / (double) gesamt) * 100;
				eval.setMistakes(gesamt - evalCorrect);
				eval.setReader("ITEXTPDF");
				eval.setSollJson(InvoiceMapper.writeOrderToJson(sollOrder));
				eval.setIstJson(InvoiceMapper.writeOrderToJson(eval.getOrder()));
				eval.setCorrectness(Double.valueOf(percentage).intValue());

				insertEvaluation(eval);
				}
			});

		}
	}

	private static Map<String, DocumentSoll> getDocumentSollList() {
		Map<String, DocumentSoll> documentSollMap = new HashMap<String, DocumentSoll>();

		try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
				Statement statement = connection.createStatement();) {

			String sql = "SELECT * FROM Document_Soll";
			ResultSet resultSet = statement.executeQuery(sql);

			// Process the result set
			while (resultSet.next()) {
				String path = resultSet.getString("Path");
				String sollJson = resultSet.getString("sollJson");
				String art = resultSet.getString("Art");

				DocumentSoll documentSoll = new DocumentSoll(path, sollJson, art);
				documentSollMap.put(path, documentSoll);
			}

			// Close the resources
			resultSet.close();
			statement.close();
			connection.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return documentSollMap;
	}

	private static void insertEvaluation(DocumentEvaluation evaluation) {
		String sql = "INSERT INTO Document_evaluation (Path, sollJson, istJson, Reader, mistakes, correctness) VALUES (?, ?, ?, ?, ?, ?)";

		try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
				PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

			preparedStatement.setString(1, evaluation.getPath());
			preparedStatement.setString(2, evaluation.getSollJson());
			preparedStatement.setString(3, evaluation.getIstJson());
			preparedStatement.setString(4, evaluation.getReader());
			preparedStatement.setInt(5, evaluation.getMistakes());
			preparedStatement.setInt(6, evaluation.getCorrectness());

			preparedStatement.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}