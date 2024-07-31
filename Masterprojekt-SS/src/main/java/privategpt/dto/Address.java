package privategpt.dto;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Address {
	@JsonProperty("Name")
	private String name;

	@JsonProperty("Strasse")
	private String street;

	@JsonProperty("Stadt")
	private String city;

	@JsonProperty("Land")
	private String country;

	@JsonProperty("Telefon")
	private String phone;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Override
	public String toString() {
		return "Address [name=" + name + ", street=" + street + ", city=" + city + ", country=" + country + ", phone="
				+ phone + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(city, country, name, phone, street);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Address other = (Address) obj;
		return Objects.equals(city, other.city) && Objects.equals(country, other.country)
				&& Objects.equals(name, other.name) && Objects.equals(phone, other.phone)
				&& Objects.equals(street, other.street);
	}

	public Address() {

	}

	public Address(String name, String street, String city, String country, String phone) {
		super();
		this.name = name;
		this.street = street;
		this.city = city;
		this.country = country;
		this.phone = phone;
	}

}
