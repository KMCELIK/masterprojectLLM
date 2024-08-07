package privategpt.examples;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ChatCompletionExample {

    public static void chatCompletion(String documentId) {
        String url = "http://85.10.204.111:8002/v1/chat/completions";

        // Prepare JSON part
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonBody = "";
        try {
            Map<String, Object> messages = new HashMap<>();
            messages.put("content", "Ich habe den Auftrag Beispielrechnung beigefügt, ich möchte nun, dass du mir nun deine Antwort nur als "
            		+ "JSON ausgeben und die jeweiligen Attribute aus der Beispielrechnung rausfiltern. Bitte gib mir eine JSON in folgenden Format aus: "
            		+ "{auftrag: auftragsnummer, positionen: [artikelnummer, artikelanzahl, artikelpreis, artikelbeschreibung], Auftraggeber, Gesamtsumme}, Gib den Response nur als JSON aus also "
            		+ "es ist kein Antwortsatz oder etwas derartiges nötig. ");
            
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("messages", List.of(messages));
            requestBody.put("prompt", "string");
            requestBody.put("stream", false);
            requestBody.put("use_context", true);
            requestBody.put("include_sources", true);
            requestBody.put("context_filter", Map.of("docs_ids", List.of(documentId)));

            jsonBody = objectMapper.writeValueAsString(requestBody);
            System.out.println("Request JSON: " + jsonBody);  // Debug output
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Send HTTP request
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(url);
            httpPost.setHeader("Content-Type", "application/json");
            httpPost.setEntity(new StringEntity(jsonBody, ContentType.APPLICATION_JSON));

            try (CloseableHttpResponse response = client.execute(httpPost)) {
                String responseBody = null;
				try {
					responseBody = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
				} catch (ParseException e) {
					e.printStackTrace();
				}
                System.out.println("Response Code: " + response.getCode());
                System.out.println("Response Body: " + responseBody);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
