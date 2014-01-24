package com.student.data;

import java.util.Arrays;

public class MainRfidInfo {
	
	private rfid_info rfid_info[];

	public rfid_info[] getRfid_info() {
		return rfid_info;
	}

	public void setRfid_info(rfid_info[] rfid_info) {
		this.rfid_info = rfid_info;
	}

	@Override
	public String toString() {
		return "MainRfidInfo [rfid_info=" + Arrays.toString(rfid_info) + "]";
	}
	
}
