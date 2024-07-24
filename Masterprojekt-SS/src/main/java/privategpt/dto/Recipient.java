package privategpt.dto;

public class Recipient {
	@Override
	public String toString() {
		return "Recipient [name=" + name + ", address=" + address + "]";
	}

	private String name;
	private String address;

	// Getters and Setters
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
}
