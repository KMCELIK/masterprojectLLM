package privategpt.dto;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

public class PaymentTermsDeserializer extends JsonDeserializer<String> {
	@Override
	public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		JsonNode node = p.getCodec().readTree(p);
		if (node.isArray()) {
			if (node.size() > 0) {
				JsonNode firstElement = node.get(0);
				return firstElement.toString(); // Konvertiert das erste Element in einen String
			}
		} else if (node.isObject()) {
			return node.toString(); // Konvertiert das Objekt in einen String
		} else if (node.isTextual()) {
			return node.asText(); // Für den Fall, dass es bereits ein String ist
		}
		return null; // Oder ein anderer Standardwert
	}
}