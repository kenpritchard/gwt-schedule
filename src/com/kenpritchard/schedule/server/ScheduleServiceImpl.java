package com.kenpritchard.schedule.server;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.kenpritchard.schedule.client.Event;
import com.kenpritchard.schedule.client.ScheduleService;

public class ScheduleServiceImpl extends RemoteServiceServlet implements ScheduleService {
	private static final long serialVersionUID = 1L;
	private List<Event> events = null;

	@Override
	public List<Event> getEvents() {
		if(this.events == null) {
			synchronized(this) {
				if(this.events == null) {
					this.readEvents();
				}
			}
		}
		return this.events;
	}
	
	private void readEvents() {
		this.events = new ArrayList<Event>();
		InputStream in = null; 
		BufferedReader reader = null;
		try {
			in = this.getClass().getClassLoader().getResourceAsStream("events.txt");
			reader = new BufferedReader(new InputStreamReader(in));
			String line = "";
			while ((line = reader.readLine()) != null) {
				String [] tokens = line.split(",");
				Event e = new Event();
				Calendar c = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
				c.set(Integer.parseInt(tokens[0].trim()), Integer.parseInt(tokens[1].trim()), Integer.parseInt(tokens[2].trim()), Integer.parseInt(tokens[3].trim()), Integer.parseInt(tokens[4].trim()));
				e.setStartTime(c.getTime());
				e.setFirstEventName(tokens[5]);
				e.setSecondEventName(tokens[6]);
				this.events.add(e);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
