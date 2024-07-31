package privategpt.process;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import privategpt.dto.Order;
import privategpt.dto.OrderWrapper;

public class InvoiceMapper {
	private static final ObjectMapper objectMapper = new ObjectMapper();

	static {
		objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
	}

	public static Order mapJsonToInvoice(String json) {
		Order order = null;
		JsonNode rootNode = null;

		if (json != null) {

			try {
				rootNode = objectMapper.readTree(json);
	            if (rootNode == null || !rootNode.isObject()) {
	                throw new IOException("Invalid JSON structure");
	            }
	            
				if (rootNode.has("Auftrag")) {
					// Directly map to Order if "Auftrag" is the root node
					try {
						order = objectMapper.treeToValue(rootNode, OrderWrapper.class).getOrder();
					} catch (JsonProcessingException e) {
						e.printStackTrace();
						throw new IOException("Error in reading: Objekt Mapper: objectMapper.readValue(json, OrderWrapper.class).getOrder()");
					}
				} else {
					// Map using OrderWrapper if "Auftrag" is not the root node
					try {
						order = objectMapper.treeToValue(rootNode, Order.class);
					} catch (JsonProcessingException e) {
						e.printStackTrace();
						throw new IOException("Error in reading: Objekt Mapper: objectMapper.readValue(json, Order.class)");
					}
				}
	            return order;
			} catch (JsonProcessingException e) {
				e.printStackTrace();
				System.out.print("Error through JSON: " + json);
				return null;
			} catch (IOException e) {
	            e.printStackTrace();
	            System.err.println("Error parsing JSON: " + e.getMessage());
	            System.out.print("Error through JSON: " + json);
	            return null;
			}

		}
		return order;
	}

	public static String writeOrderToJson(Order order) {
		String json = null;
		try {
			json = objectMapper.writeValueAsString(order);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return json;
	}
}
