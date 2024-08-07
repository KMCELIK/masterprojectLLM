package privategpt.dto;

import java.io.IOException;
import java.util.regex.Pattern;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

public class PaymentTermsDeserializer extends JsonDeserializer<String> {

	private static final Pattern DATE_PATTERN = Pattern.compile("\\d{2}\\.\\d{2}\\.\\d{4}" // dd.mm.yyyy format
	);

	@Override
	public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		JsonNode node = p.getCodec().readTree(p);
		if (node.isArray() && node.size() > 0) {

			String lastDateLikeString = null;
			for (JsonNode element : node) {
				if (element.isTextual() && DATE_PATTERN.matcher(element.asText()).matches()) {
					lastDateLikeString = element.asText();
				}
			}
			return lastDateLikeString != null ? lastDateLikeString : node.get(node.size() - 1).toString(); 
		} else if (node.isObject()) {
			return node.toString(); 
		} else if (node.isTextual()) {
			return node.asText(); 
		}
		return null;
	}
}