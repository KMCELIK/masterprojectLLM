package privategpt.process;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import privategpt.dto.Order;
import privategpt.dto.OrderWrapper;

public class InvoiceMapper {
	public static Order mapJsonToInvoice(String json) {
		ObjectMapper objectMapper = new ObjectMapper();
		Order order = null;
		JsonNode rootNode = null;
		try {
			rootNode = objectMapper.readTree(json);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		if (rootNode.has("Auftrag")) {
			// Directly map to Order if "Auftrag" is the root node
			try {
				order = objectMapper.readValue(json, OrderWrapper.class).getOrder();
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
		} else {
			// Map using OrderWrapper if "Auftrag" is not the root node
			try {
				order = objectMapper.readValue(json, Order.class);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
		}
		return order;
	}

	public static String writeOrderToJson(Order order) {
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = null;
		try {
			json = ow.writeValueAsString(order);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return json;
	}
}
