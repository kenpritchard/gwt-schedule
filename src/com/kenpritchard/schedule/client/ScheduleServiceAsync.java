package com.kenpritchard.schedule.client;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ScheduleServiceAsync {

	void getEvents(AsyncCallback<List<Event>> callback);

}
