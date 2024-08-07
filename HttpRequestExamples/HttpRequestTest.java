package privategpt.examples;

import java.io.BufferedReader;
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

public class HttpRequestTest {
	public static void main(String[] args) {
		try {
			String urlString = "http://85.10.204.111:8002/v1/chat/completions";
			URL url = new URL(urlString);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();

			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setDoOutput(true);

			Map<String, Object> messageContent = new HashMap<>();
			messageContent.put("content", "Hello, how much is 1+1?");

			Map<String, Object> requestBody = new HashMap<>();
			requestBody.put("messages", List.of(messageContent));
			requestBody.put("prompt", "string");
			requestBody.put("stream", false);
			requestBody.put("use_context", true);
			requestBody.put("include_sources", false);

			ObjectMapper objectMapper = new ObjectMapper();
			String jsonBody = objectMapper.writeValueAsString(requestBody);
			System.out.println("JSON Body: " + jsonBody); // Debug-Ausgabe des JSON-Bodys

			try (OutputStream os = connection.getOutputStream()) {
				byte[] input = jsonBody.getBytes(StandardCharsets.UTF_8);
				os.write(input, 0, input.length);
			}

			int responseCode = connection.getResponseCode();
			System.out.println("Response Code: " + responseCode);

			BufferedReader in = new BufferedReader(
					new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
			String inputLine;
			StringBuilder response = new StringBuilder();
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			String responseBody = response.toString();
			System.out.println("Response Body: " + responseBody);
			CompletionResponse completionResponse = objectMapper.readValue(responseBody, CompletionResponse.class);

			String messageContentOutput = completionResponse.getChoices().get(0).getMessage().getContent();
			System.out.println("Message Content: " + messageContentOutput);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
