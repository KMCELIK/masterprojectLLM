package privategpt.executor;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import privategpt.dto.DocumentEvaluation;
import privategpt.dto.DocumentSoll;
import privategpt.dto.Order;
import privategpt.process.InvoiceMapper;
import privategpt.process.PrivateGptRequestingProcess;
import privategpt.process.SimilarityCalculationProcess;
import privategpt.reader.OcrReader;

public class PrivateGptRequestExecutor {
    // Datenbankverbindung
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/whs";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "kaan1234"; // password und user auf sein eigenes anpassen

    public static void main(String[] args) {
        System.out.println("Prozessstart: " + LocalDateTime.now());

        List<String> invoicePaths = getInvoicePaths("C:\\Eclipse\\workspace\\Masterprojekt-SS\\src\\main\\resources\\");

        if (invoicePaths.isEmpty()) {
            System.out.println("Keine Rechnungen gefunden.");
            return;
        }

        Map<String, DocumentSoll> documentSollMap = getDocumentSollList();
        List<DocumentEvaluation> evaluations = new ArrayList<>();

        processInvoices(invoicePaths, documentSollMap, evaluations);

        System.out.println("Prozessende: " + LocalDateTime.now());
    }

    private static List<String> getInvoicePaths(String directoryPath) {
        List<String> invoicePaths = new ArrayList<>();
        File directory = new File(directoryPath);

        if (directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile()) {
                        invoicePaths.add(file.getAbsolutePath());
                    }
                }
            }
        }
        return invoicePaths;
    }

    private static void processInvoices(List<String> invoicePaths, Map<String, DocumentSoll> documentSollMap, List<DocumentEvaluation> evaluations) {
        List<Order> orders = new ArrayList<>();

        for (String path : invoicePaths) {
            String ocrFile = OcrReader.readPdf(path);

            if (ocrFile == null) {
                continue;
            }

            Order order = InvoiceMapper.mapJsonToInvoice(
                PrivateGptRequestingProcess.getPrivateGptResponseFromPdfRequest(
                    ocrFile.replaceAll("\\r?\\n|\\r|\\t", "").replaceAll("[\\p{Cntrl}&&[^\r\n\t]]", "")
                )
            );

            try {
                TimeUnit.SECONDS.sleep(15); // die Responses waren besser wenn nicht zuviele Requests aufeinmal oder nacheinander geschickt worden sind.
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            orders.add(order);
            DocumentEvaluation eval = new DocumentEvaluation();
            eval.setPath(path);
            eval.setOrder(order);
            evaluations.add(eval);
        }

        System.out.println(orders.toString());
        evaluateDocuments(evaluations, documentSollMap);
    }

    private static void evaluateDocuments(List<DocumentEvaluation> evaluations, Map<String, DocumentSoll> documentSollMap) {
        final int TOTAL_POINTS = 10;
        final double MINIMUM_SIMILARITY = 0.6;

        for (DocumentEvaluation eval : evaluations) {
            int correctPoints = 0;
            DocumentSoll soll = documentSollMap.get(eval.getPath());

            if (soll == null) {
                continue;
            }

            Order sollOrder = InvoiceMapper.mapJsonToInvoice(soll.getSollJson().replaceAll("\\n", ""));
            Order order = eval.getOrder();

            if (order == null || sollOrder == null) {
                handleEvaluationError(eval, sollOrder, TOTAL_POINTS);
                continue;
            }

            if (compareItems(sollOrder, order, MINIMUM_SIMILARITY)) correctPoints++;
            if (compareFields(sollOrder.getSeller(), order.getSeller(), MINIMUM_SIMILARITY)) correctPoints++;
            if (compareFields(sollOrder.getBillingAddress().toString(), order.getBillingAddress().toString(), MINIMUM_SIMILARITY)) correctPoints++;
            if (compareFields(sollOrder.getDeliveryAddress().toString(), order.getDeliveryAddress().toString(), MINIMUM_SIMILARITY)) correctPoints++;
            if (compareFields(sollOrder.getHandler(), order.getHandler(), MINIMUM_SIMILARITY)) correctPoints++;
            if (compareFields(sollOrder.getPaymentTerms(), order.getPaymentTerms(), MINIMUM_SIMILARITY)) correctPoints++;
            if (compareFields(sollOrder.getOrderNumber(), order.getOrderNumber(), MINIMUM_SIMILARITY)) correctPoints++;
            if (compareFields(sollOrder.getOrderDate(), order.getOrderDate(), MINIMUM_SIMILARITY)) correctPoints++;
            if (compareFields(sollOrder.getNetAmount(), order.getNetAmount(), MINIMUM_SIMILARITY)) correctPoints++;
            if (compareFields(sollOrder.getTotalAmount(), order.getTotalAmount(), MINIMUM_SIMILARITY)) correctPoints++;

            double correctnessPercentage = ((double) correctPoints / TOTAL_POINTS) * 100;
            eval.setMistakes(TOTAL_POINTS - correctPoints);
            eval.setReader("ITEXTPDF");
            eval.setSollJson(InvoiceMapper.writeOrderToJson(sollOrder));
            eval.setIstJson(InvoiceMapper.writeOrderToJson(order));
            eval.setCorrectness((int) correctnessPercentage);

            insertEvaluation(eval);
        }
    }

    private static boolean compareItems(Order sollOrder, Order order, double minimumSimilarity) {
        return sollOrder.getItems() != null && !sollOrder.getItems().isEmpty() &&
               SimilarityCalculationProcess.calculateSimilarity(sollOrder.getItems().toString(), order.getItems().toString()) > minimumSimilarity;
    }

    private static boolean compareFields(String sollField, String istField, double minimumSimilarity) {
        return sollField != null && SimilarityCalculationProcess.calculateSimilarity(sollField, istField) > minimumSimilarity;
    }

    private static void handleEvaluationError(DocumentEvaluation eval, Order sollOrder, int totalPoints) {
        eval.setCorrectness(0);
        eval.setReader("ITEXTPDF");
        eval.setIstJson("Fehler ist aufgetreten");
        eval.setSollJson(sollOrder != null ? InvoiceMapper.writeOrderToJson(sollOrder) : "Fehler bei Path: " + eval.getPath());
        eval.setMistakes(totalPoints);

        insertEvaluation(eval);
    }

    private static Map<String, DocumentSoll> getDocumentSollList() {
        Map<String, DocumentSoll> documentSollMap = new HashMap<>();

        String sql = "SELECT * FROM Document_Soll";

        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                String path = resultSet.getString("Path");
                String sollJson = resultSet.getString("sollJson");
                String art = resultSet.getString("Art");

                DocumentSoll documentSoll = new DocumentSoll(path, sollJson, art);
                documentSollMap.put(path, documentSoll);
            }
        } catch (Exception e) {
            e.printStackTrace();  // bei weiterführung, hier logging einbauen 
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
            e.printStackTrace();  // bei weiterführung, hier logging einbauen 
        }
    }
}
