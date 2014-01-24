package com.gps.data;

public class Map {

	private String arrived_time;
	private String lat;
	private String Long;
	public String getArrived_time() {
		return arrived_time;
	}
	public void setArrived_time(String arrived_time) {
		this.arrived_time = arrived_time;
	}
	public String getLat() {
		return lat;
	}
	public void setLat(String lat) {
		this.lat = lat;
	}
	public String getLong() {
		return Long;
	}
	public void setLong(String l) {
		Long = l;
	}
	@Override
	public String toString() {
		return "Map [arrived_time=" + arrived_time + ", lat=" + lat + ", Long="
				+ Long + "]";
	}
	
}
