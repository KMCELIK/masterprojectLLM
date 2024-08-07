package privategpt.examples;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.entity.mime.FileBody;
import org.apache.hc.client5.http.entity.mime.MultipartEntityBuilder;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class IngestFileExample {

    public static String ingestFile(String pdfFilePath) {
        String url = "http://85.10.204.111:8002/v1/ingest/file";

        // Check if the file exists
        File pdfFile = new File(pdfFilePath);
        if (!pdfFile.exists()) {
            System.err.println("File not found: " + pdfFilePath);
            return null;
        }

        // Send HTTP request
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(url);
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.addPart("file", new FileBody(pdfFile, ContentType.APPLICATION_OCTET_STREAM));
            
            httpPost.setEntity(builder.build());

            try (CloseableHttpResponse response = client.execute(httpPost)) {
                String responseBody = null;
				try {
					responseBody = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
				} catch (ParseException e) {
					e.printStackTrace();
				}
                System.out.println("Response Code: " + response.getCode());
                System.out.println("Response Body: " + responseBody);

                // Extract document ID from the response
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode responseJson = objectMapper.readTree(responseBody);
                JsonNode data = responseJson.path("data").get(0);
                String documentId = data.path("doc_id").asText();
                return documentId;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        String pdfFilePath = "src/main/resources/Beispielrechnung.pdf";  // Update the path if needed
        String documentId = ingestFile(pdfFilePath);
        if (documentId != null) {
            ChatCompletionExample.chatCompletion(documentId);
        }
    }
}
