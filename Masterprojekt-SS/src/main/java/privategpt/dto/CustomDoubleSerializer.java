package privategpt.dto;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class CustomDoubleSerializer extends JsonDeserializer<Double> {

    @Override
    public Double deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
    	String text = p.getText();
        text = text.replace(",", ".");
        
        if (text.contains(".")) {
            String[] parts = text.split("\\.");
            if (parts.length == 2 && parts[1].isEmpty()) {
                // Entfernen des Punktes, wenn kein Wert nach dem Punkt ist
                text = parts[0];
            }
        }
        
        return Double.valueOf(text);
    }
}