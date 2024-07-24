package privategpt.dto;

import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Order {

	@JsonProperty("Auftragsnummer")
	private String orderNumber;

	@JsonProperty("Bestelldatum")
	private String orderDate;

	@JsonProperty("Verkäufer")
	private String seller;

	@JsonProperty("Bearbeiter")
	private String handler;

	@JsonProperty("Lieferadresse")
	private Address deliveryAddress;

	@JsonProperty("Rechnungsadresse")
	private Address billingAddress;

	@JsonProperty("Liefertermin")
	private String deliveryDate;

	@JsonProperty("Artikel")
	private List<Item> items;

	@JsonProperty("Nettobetrag")
	private Double netAmount;

	@JsonProperty("USt19")
	private Double vat19;

	@JsonProperty("Gesamt")
	private Double totalAmount;

	@JsonProperty("Zahlungsbedingungen")
	private String paymentTerms;

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public String getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}

	public String getSeller() {
		return seller;
	}

	public void setSeller(String seller) {
		this.seller = seller;
	}

	public String getHandler() {
		return handler;
	}

	public void setHandler(String handler) {
		this.handler = handler;
	}

	public Address getDeliveryAddress() {
		return deliveryAddress;
	}

	public void setDeliveryAddress(Address deliveryAddress) {
		this.deliveryAddress = deliveryAddress;
	}

	public Address getBillingAddress() {
		return billingAddress;
	}

	public void setBillingAddress(Address billingAddress) {
		this.billingAddress = billingAddress;
	}

	public String getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(String deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}

	public double getNetAmount() {
		return netAmount;
	}

	public void setNetAmount(double netAmount) {
		this.netAmount = netAmount;
	}

	public double getVat19() {
		return vat19;
	}

	public void setVat19(double vat19) {
		this.vat19 = vat19;
	}

	public double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getPaymentTerms() {
		return paymentTerms;
	}

	public void setPaymentTerms(String paymentTerms) {
		this.paymentTerms = paymentTerms;
	}

	@Override
	public String toString() {
		return "Order [orderNumber=" + orderNumber + ", orderDate=" + orderDate + ", seller=" + seller + ", handler="
				+ handler + ", deliveryAddress=" + deliveryAddress + ", billingAddress=" + billingAddress
				+ ", deliveryDate=" + deliveryDate + ", items=" + items + ", netAmount=" + netAmount + ", vat19="
				+ vat19 + ", totalAmount=" + totalAmount + ", paymentTerms=" + paymentTerms + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(billingAddress, deliveryAddress, deliveryDate, handler, items, netAmount, orderDate,
				orderNumber, paymentTerms, seller, totalAmount, vat19);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Order other = (Order) obj;
		return Objects.equals(billingAddress, other.billingAddress)
				&& Objects.equals(deliveryAddress, other.deliveryAddress)
				&& Objects.equals(deliveryDate, other.deliveryDate) && Objects.equals(handler, other.handler)
				&& Objects.equals(items, other.items)
				&& Double.doubleToLongBits(netAmount) == Double.doubleToLongBits(other.netAmount)
				&& Objects.equals(orderDate, other.orderDate) && Objects.equals(orderNumber, other.orderNumber)
				&& Objects.equals(paymentTerms, other.paymentTerms) && Objects.equals(seller, other.seller)
				&& Double.doubleToLongBits(totalAmount) == Double.doubleToLongBits(other.totalAmount)
				&& Double.doubleToLongBits(vat19) == Double.doubleToLongBits(other.vat19);
	}

	public Order(String orderNumber, String orderDate, String seller, String handler, Address deliveryAddress,
			Address billingAddress, String deliveryDate, List<Item> items, double netAmount, double vat19,
			double totalAmount, String paymentTerms) {
		super();
		this.orderNumber = orderNumber;
		this.orderDate = orderDate;
		this.seller = seller;
		this.handler = handler;
		this.deliveryAddress = deliveryAddress;
		this.billingAddress = billingAddress;
		this.deliveryDate = deliveryDate;
		this.items = items;
		this.netAmount = netAmount;
		this.vat19 = vat19;
		this.totalAmount = totalAmount;
		this.paymentTerms = paymentTerms;
	}

	public Order() {

	}
}
