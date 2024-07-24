package privategpt.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OrderWrapper {

	@JsonProperty("Auftrag")
	private Order order;

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public OrderWrapper() {

	}
	
	public OrderWrapper(Order order) {
		this.order = order;
	}

}
