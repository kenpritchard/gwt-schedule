package com.kenpritchard.schedule.client;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("events")
public interface ScheduleService extends RemoteService {
	List<Event> getEvents();
}
