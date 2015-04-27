package com.kenpritchard.schedule.client;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;

public class Event implements Serializable {
	private static final long serialVersionUID = 1L;
	private Date startTime;
	private int durationInMinutes;
	private String firstEventName;
	private String secondEventName;
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public int getDurationInMinutes() {
		return durationInMinutes;
	}
	public void setDurationInMinutes(int durationInMinutes) {
		this.durationInMinutes = durationInMinutes;
	}
	public String getFirstEventName() {
		return firstEventName;
	}
	public void setFirstEventName(String firstEventName) {
		this.firstEventName = firstEventName;
	}
	public String getSecondEventName() {
		return secondEventName;
	}
	public void setSecondEventName(String secondEventName) {
		this.secondEventName = secondEventName;
	}
	
	public static Comparator<Event> EventDateComparator = new Comparator<Event>() {

		@Override
		public int compare(Event e1, Event e2) {
			return e1.getStartTime().compareTo(e2.getStartTime());
		}
		
	};
}
