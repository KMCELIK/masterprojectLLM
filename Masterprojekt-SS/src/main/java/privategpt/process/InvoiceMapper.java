package privategpt.process;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;

import privategpt.dto.Order;
import privategpt.dto.OrderWrapper;

public class InvoiceMapper {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.configure(Feature.ALLOW_COMMENTS, true);
        objectMapper.configure(Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
    }

    public static Order mapJsonToInvoice(String json) {
        if (json == null) {
            return null;
        }

        json = correctJson(json); //.replaceAll("(,\\s*\\])|(,\\s*\\})", "$1"));

        try {
            JsonNode rootNode = objectMapper.readTree(json);
            if (rootNode == null || !rootNode.isObject()) {
                throw new IOException("Invalid JSON structure");
            }

            if (rootNode.has("Auftrag")) {
                return objectMapper.treeToValue(rootNode, OrderWrapper.class).getOrder();
            } else {
                return objectMapper.treeToValue(rootNode, Order.class);
            }
        } catch (JsonProcessingException e) {
            logJsonError("JSON processing error", e, json);
        } catch (IOException e) {
            logJsonError("Error parsing JSON", e, json);
        }

        return null;
    }

    public static String writeOrderToJson(Order order) {
        if (order == null) {
            return null;
        }

        try {
            return objectMapper.writeValueAsString(order);
        } catch (JsonProcessingException e) {
           // bei weiterführung, hier logging einbauen  e.printStackTrace();
        }

        return null;
    }

    private static String correctJson(String json) {
        json = fixInvalidCharacters(json);

        try {
            JsonNode rootNode = objectMapper.readTree(json);
            if (rootNode.has("Auftrag")) {
                correctOrderFields((ObjectNode) rootNode.get("Auftrag"));
            }

            return objectMapper.writeValueAsString(rootNode);
        } catch (IOException e) {
        	 // bei weiterführung, hier logging einbauen  e.printStackTrace();
        }

        return json;
    }

    private static void correctOrderFields(ObjectNode auftragNode) {
        correctDateField(auftragNode, "Bestelldatum");
        correctArticleFields(auftragNode.get("Artikel"));
        correctDecimal(auftragNode, "Nettobetrag");
        correctDecimal(auftragNode, "USt19");
        correctDecimal(auftragNode, "Gesamt");
    }

    private static void correctDateField(ObjectNode node, String fieldName) {
        if (node.has(fieldName)) {
            String dateValue = node.get(fieldName).asText().split(" ")[0];
            node.put(fieldName, dateValue);
        }
    }

    private static void correctArticleFields(JsonNode artikelArray) {
        if (artikelArray != null && artikelArray.isArray()) {
            for (JsonNode artikelNode : artikelArray) {
                correctDecimal(artikelNode, "Rabatt");
                correctDecimal(artikelNode, "Betrag");
                correctDecimal(artikelNode, "Stückpreis");
                correctDecimal(artikelNode, "Menge");
                correctDescription(artikelNode, "Beschreibung");
            }
        }
    }

    private static void correctDecimal(JsonNode node, String fieldName) {
        if (node.has(fieldName)) {
            String value = node.get(fieldName).asText().replace(",", ".").replaceAll("[^\\d.]", "");
            try {
                Double.parseDouble(value);
                ((ObjectNode) node).put(fieldName, value);
            } catch (NumberFormatException e) {
                ((ObjectNode) node).put(fieldName, "0");
            }
        }
    }

    private static void correctDescription(JsonNode node, String fieldName) {
        if (node.has(fieldName)) {
            String value = node.get(fieldName).asText();
            if (value.contains("\"") && !value.endsWith("\"")) {
                value = value + "\"";
            }
            ((ObjectNode) node).put(fieldName, value);
        }
    }

    private static String fixInvalidCharacters(String jsonString) {
        jsonString = jsonString.replaceAll(",(\\s*[}\\]])", "$1");
        Pattern pattern = Pattern.compile("(?<=\\{\\s*)(\\w+)(\\s*:\\s*)");
        Matcher matcher = pattern.matcher(jsonString);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, "\"" + matcher.group(1) + "\"" + matcher.group(2));
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    private static void logJsonError(String message, Exception e, String json) {
    	 // bei weiterführung, hier logging einbauen  e.printStackTrace();
    	 System.err.println(message + ": " + e.getMessage());
    	 // bei weiterführung, hier logging einbauen System.out.println("Error JSON: " + json);
    }
}
