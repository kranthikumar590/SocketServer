package com.student.data;

public class StudentSessions {

	private rfid_info rfid_info;

	public rfid_info getRfid_info() {
		return rfid_info;
	}

	public void setRfid_info(rfid_info rfid_info) {
		this.rfid_info = rfid_info;
	}

	@Override
	public String toString() {
		return "StudentSessions [rfid_info=" + rfid_info + "]";
	}
	
}
