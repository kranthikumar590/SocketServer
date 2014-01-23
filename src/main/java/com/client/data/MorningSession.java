package com.client.data;

public class MorningSession {

	
	private String in_bound;
	private String out_bound;
	public String getIn_bound() {
		return in_bound;
	}
	public void setIn_bound(String in_bound) {
		this.in_bound = in_bound;
	}
	public String getOut_bound() {
		return out_bound;
	}
	public void setOut_bound(String out_bound) {
		this.out_bound = out_bound;
	}
	@Override
	public String toString() {
		return "MorningSession [in_bound=" + in_bound + ", out_bound="
				+ out_bound + "]";
	}
	
	
}
