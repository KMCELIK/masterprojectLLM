package privategpt.examples;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.entity.mime.FileBody;
import org.apache.hc.client5.http.entity.mime.MultipartEntityBuilder;
import org.apache.hc.client5.http.entity.mime.StringBody;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

public class HttpRequestTest2 {
    public static void main(String[] args) throws ParseException {
        String url = "http://85.10.204.111:8002/v1/chat/completions";
        String pdfFilePath = "src/main/resources/Beispielrechnung.pdf";  // Update the path if needed

        // Check if the file exists
        File pdfFile = new File(pdfFilePath);
        if (!pdfFile.exists()) {
            System.err.println("File not found: " + pdfFilePath);
            return;
        }

        // Prepare JSON part
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonBody = "";
        try {
            Map<String, Object> messages = new HashMap<>();
            messages.put("content", "Can you read the following PDF-File and tell me what is this PDF-File about?");
            
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("messages", List.of(messages));
            requestBody.put("prompt", "string");
            requestBody.put("stream", false);
            requestBody.put("use_context", true);
            requestBody.put("include_sources", false);
            jsonBody = objectMapper.writeValueAsString(requestBody);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Send HTTP request
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(url);
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.addPart("request", new StringBody(jsonBody, ContentType.APPLICATION_JSON));
            builder.addPart("file", new FileBody(pdfFile, ContentType.APPLICATION_OCTET_STREAM));
            
            httpPost.setEntity(builder.build());

            try (CloseableHttpResponse response = client.execute(httpPost)) {
                String responseBody = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
                System.out.println("Response Code: " + response.getCode());
                System.out.println("Response Body: " + responseBody);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
