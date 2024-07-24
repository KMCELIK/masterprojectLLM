package privategpt.dto;

import java.util.List;
import java.util.Objects;

public class Invoice {
	
	private String invoiceNumber;
	private String date;
	private String dueDate;
	private Recipient recipient;
	private List<Item> items;
	private double totalAmount;
	private String currency;
	@Override
	public int hashCode() {
		return Objects.hash(currency, date, dueDate, invoiceNumber, items, recipient, totalAmount);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Invoice other = (Invoice) obj;
		return Objects.equals(currency, other.currency) && Objects.equals(date, other.date)
				&& Objects.equals(dueDate, other.dueDate) && Objects.equals(invoiceNumber, other.invoiceNumber)
				&& Objects.equals(items, other.items) && Objects.equals(recipient, other.recipient)
				&& Double.doubleToLongBits(totalAmount) == Double.doubleToLongBits(other.totalAmount);
	}

	@Override
	public String toString() {
		return "Invoice [invoiceNumber=" + invoiceNumber + ", date=" + date + ", dueDate=" + dueDate + ", recipient="
				+ recipient + ", items=" + items + ", totalAmount=" + totalAmount + ", currency=" + currency + "]";
	}



	// Getters and Setters
	public String getInvoiceNumber() {
		return invoiceNumber;
	}

	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getDueDate() {
		return dueDate;
	}

	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}

	public Recipient getRecipient() {
		return recipient;
	}

	public void setRecipient(Recipient recipient) {
		this.recipient = recipient;
	}

	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}

	public double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}
}
