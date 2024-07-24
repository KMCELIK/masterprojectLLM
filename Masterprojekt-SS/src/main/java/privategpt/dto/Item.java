package privategpt.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Item {
	@JsonProperty("ArtNr")
	private String itemNumber;

	@JsonProperty("Beschreibung")
	private String description;

	@JsonProperty("Menge")
	private String quantity;

	@JsonProperty("Stückpreis")
	private Double unitPrice;

	@JsonProperty("Rabatt")
	private Double discount;

	@JsonProperty("Betrag")
	private Double amount;

	public String getItemNumber() {
		return itemNumber;
	}

	public void setItemNumber(String itemNumber) {
		this.itemNumber = itemNumber;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(double unitPrice) {
		this.unitPrice = unitPrice;
	}

	public double getDiscount() {
		return discount;
	}

	public void setDiscount(double discount) {
		this.discount = discount;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	@Override
	public String toString() {
		return "Item [itemNumber=" + itemNumber + ", description=" + description + ", quantity=" + quantity
				+ ", unitPrice=" + unitPrice + ", discount=" + discount + ", amount=" + amount + "]";
	}

	public Item(String itemNumber, String description, String quantity, double unitPrice, double discount,
			double amount) {
		super();
		this.itemNumber = itemNumber;
		this.description = description;
		this.quantity = quantity;
		this.unitPrice = unitPrice;
		this.discount = discount;
		this.amount = amount;
	}

	public Item() {

	}
}
