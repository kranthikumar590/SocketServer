package com.client.data;

public class Rfid {

	private String rfid_number;
	private String type;
	public String getRfid_number() {
		return rfid_number;
	}
	public void setRfid_number(String rfid_number) {
		this.rfid_number = rfid_number;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	@Override
	public String toString() {
		return "Rfid [rfid_number=" + rfid_number + ", type=" + type + "]";
	}
	
	
	
	
}
