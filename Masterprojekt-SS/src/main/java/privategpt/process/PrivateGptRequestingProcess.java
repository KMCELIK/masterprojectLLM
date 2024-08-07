package privategpt.process;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

import privategpt.dto.CompletionResponse;

public class PrivateGptRequestingProcess {
	  public static String getPrivateGptResponseFromPdfRequest(String PdfFile) {
	        String jsonResponse = null;
	        try {
	            String urlString = "http://85.10.204.111:8002/v1/chat/completions"; // privateGPT
	            URL url = new URL(urlString);
	            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

	            connection.setRequestMethod("POST");
	            connection.setRequestProperty("Content-Type", "application/json");
	            connection.setDoOutput(true);

	            String request = "Gegeben ist folgender String" + PdfFile + "Bitte erstelle eine JSON-Darstellung des Strings gemäß der TypeScript-Definition im folgenden Code interface Lieferadresse { Name: string; Strasse: string; Stadt: string; Land: string; Telefon: string;} interface Rechnungsadresse { Name: string; Strasse: string; Stadt: string; Land: string; Telefon: string; } interface Artikel { ArtNr: string; Beschreibung: string; Menge: string; Stückpreis: string; Rabatt: string; Betrag: string; } interface Auftrag { Auftragsnummer: string; Bestelldatum: string; Verkäufer: string; Bearbeiter: string; Lieferadresse: Lieferadresse; Rechnungsadresse: Rechnungsadresse; Liefertermin: string; Artikel: Artikel[]; Nettobetrag: string; USt19: string; Gesamt: string; Zahlungsbedingungen: string; } Bitte beschränke dich nur auf die TypeScript-Definition und erstelle keine Werte, die dort nicht definiert sind.";
				Map<String, Object> messageContent = new HashMap<>();
	            messageContent.put("content", request);

	            Map<String, Object> requestBody = new HashMap<>();
	            requestBody.put("messages", List.of(messageContent));
	            requestBody.put("prompt", "string");
	            requestBody.put("stream", false);
	            requestBody.put("use_context", true);
	            requestBody.put("include_sources", false);

	            ObjectMapper objectMapper = new ObjectMapper();
	            String jsonBody = objectMapper.writeValueAsString(requestBody);

	            try (OutputStream os = connection.getOutputStream()) {
	                byte[] input = jsonBody.getBytes(StandardCharsets.UTF_8);
	                os.write(input, 0, input.length);
	            }

	            int responseCode = connection.getResponseCode();
	            if (responseCode != HttpURLConnection.HTTP_OK) {
	                throw new IOException("HTTP error code: " + responseCode);
	            }

	            try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
	                String inputLine;
	                StringBuilder response = new StringBuilder();
	                while ((inputLine = in.readLine()) != null) {
	                    response.append(inputLine);
	                }
	                jsonResponse = response.toString();
	            }

	            CompletionResponse completionResponse = objectMapper.readValue(jsonResponse, CompletionResponse.class);

	            String messageContentOutput = completionResponse.getChoices().get(0).getMessage().getContent();
	            int startIndex = messageContentOutput.indexOf('{');
	            int endIndex = messageContentOutput.lastIndexOf('}');

	            if (startIndex != -1 && endIndex != -1 && startIndex < endIndex) {
	                jsonResponse = messageContentOutput.substring(startIndex, endIndex + 1);
	            }
	        } catch (Exception e) {
	            e.printStackTrace();  // bei weiterführung, hier logging einbauen 
	        }

	        if (jsonResponse != null) {
	            jsonResponse = jsonResponse.replaceAll("\\r?\\n|\\r|\\t", "");
	        }

	        return jsonResponse;
	    }
}
