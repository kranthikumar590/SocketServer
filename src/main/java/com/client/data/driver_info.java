package com.client.data;

public class driver_info {

	private morning morning;
	private evening evening;
	public morning getMorning() {
		return morning;
	}
	public void setMorning(morning morning) {
		this.morning = morning;
	}
	public evening getEvening() {
		return evening;
	}
	public void setEvening(evening evening) {
		this.evening = evening;
	}
	@Override
	public String toString() {
		return "driver_info [morning=" + morning + ", evening=" + evening + "]";
	}
	
}
