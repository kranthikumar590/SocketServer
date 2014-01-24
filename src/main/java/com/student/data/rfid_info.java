package com.student.data;

public class rfid_info {

	private String rfid;
	private String morning_in_bound;
	private String morning_out_bound;
	private String evening_in_bound;
	private String evening_out_bound;
	public String getRfid() {
		return rfid;
	}
	public void setRfid(String rfid) {
		this.rfid = rfid;
	}
	public String getMorning_in_bound() {
		return morning_in_bound;
	}
	public void setMorning_in_bound(String morning_in_bound) {
		this.morning_in_bound = morning_in_bound;
	}
	public String getMorning_out_bound() {
		return morning_out_bound;
	}
	public void setMorning_out_bound(String morning_out_bound) {
		this.morning_out_bound = morning_out_bound;
	}
	public String getEvening_in_bound() {
		return evening_in_bound;
	}
	public void setEvening_in_bound(String evening_in_bound) {
		this.evening_in_bound = evening_in_bound;
	}
	public String getEvening_out_bound() {
		return evening_out_bound;
	}
	public void setEvening_out_bound(String evening_out_bound) {
		this.evening_out_bound = evening_out_bound;
	}
	@Override
	public String toString() {
		return "rfid_info [rfid=" + rfid + ", morning_in_bound="
				+ morning_in_bound + ", morning_out_bound=" + morning_out_bound
				+ ", evening_in_bound=" + evening_in_bound
				+ ", evening_out_bound=" + evening_out_bound + "]";
	}
	
}
