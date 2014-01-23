package com.client.data;

public class EveningSession {

	private String evening_in_bound;
	private String evening_out_bound;
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
		return "EveningSession [evening_in_bound=" + evening_in_bound
				+ ", evening_out_bound=" + evening_out_bound + "]";
	}
	
}
