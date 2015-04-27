package com.kenpritchard.schedule.client;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Schedule implements EntryPoint {
	private static final int REFRESH_INTERVAL_IN_MS = 300000;
	private static final long HALF_HOUR_IN_MS = 1800000L; 
	private VerticalPanel mainPanel = new VerticalPanel();
	private FlexTable eventsFlexTable = new FlexTable();
	private ScheduleServiceAsync scheduleSvc = GWT.create(ScheduleService.class);

	/**
	 * Entry point method.
	 */
	public void onModuleLoad() {
		this.refreshEventList();
		mainPanel.add(eventsFlexTable);
		RootPanel.get("eventList").add(mainPanel);
		this.refreshEventList();

		// Setup timer to refresh list automatically.
		Timer refreshTimer = new Timer() {
			@Override
			public void run() {
				refreshEventList();
			}
		};
		refreshTimer.scheduleRepeating(REFRESH_INTERVAL_IN_MS);

	}
	
	/**
	 * Make sure events are fresh.
	 */
	private void refreshEventList() {
		// Initialize the service proxy.
		if (scheduleSvc == null) {
			scheduleSvc = GWT.create(ScheduleService.class);
		}

		// Set up the callback object.
		AsyncCallback<List<Event>> callback = new AsyncCallback<List<Event>>() {
			public void onFailure(Throwable caught) {
		        GWT.log("Failed to retrieve event list");
			}

			public void onSuccess(List<Event> events) {
				updateTable(events);
			}
		};

		scheduleSvc.getEvents(callback);
	}

	/**
	 * Update the Price and Change fields all the rows in the stock table.
	 * 
	 * @param prices
	 *            Stock data for all rows.
	 */
	private void updateTable(List<Event> events) {
		final DateTimeFormat dateFormat = DateTimeFormat.getFormat("EEEE");
		final DateTimeFormat timeFormat = DateTimeFormat.getFormat("h:mm a");
		final int startingRow = 2;
		final Set<String> dateLabels = new HashSet<String>();
		Collections.sort(events, Event.EventDateComparator);
		eventsFlexTable.removeAllRows();
		eventsFlexTable.addStyleName("eventList");
		eventsFlexTable.setHTML(0, 0, "<h1>Event Schedule<h1>");
		eventsFlexTable.getFlexCellFormatter().setColSpan(0, 0, 3);
		
		eventsFlexTable.setCellPadding(15);
		
		eventsFlexTable.getRowFormatter().addStyleName(0, "eventListTitle");
		eventsFlexTable.getColumnFormatter().addStyleName(0, "eventListTimeColumn");
		eventsFlexTable.getColumnFormatter().addStyleName(1, "eventListDescColumn");
		eventsFlexTable.getColumnFormatter().addStyleName(2, "eventListDescColumn");
		int row = startingRow;
		for(Event event : events) {
			String day = dateFormat.format(event.getStartTime());
			if(!dateLabels.contains(day)) {
				dateLabels.add(day);
				eventsFlexTable.setText(row, 0, dateFormat.format(event.getStartTime()));
				eventsFlexTable.getFlexCellFormatter().setColSpan(row, 0, 3);
				eventsFlexTable.getRowFormatter().addStyleName(row, "eventListDaySubHeader");
				row++;
				eventsFlexTable.setText(row, 0, "Time");
				eventsFlexTable.setText(row, 1, "San Francisco Live Stream");
				eventsFlexTable.setText(row, 2, "Local Presentation");
				eventsFlexTable.getRowFormatter().addStyleName(row, "eventListHeader");
				row++;
			}
			eventsFlexTable.setText(row, 0, timeFormat.format(event.getStartTime()));
			if(event.getFirstEventName().equalsIgnoreCase(event.getSecondEventName())) {
				eventsFlexTable.setText(row, 1, event.getFirstEventName());
				eventsFlexTable.getFlexCellFormatter().setColSpan(row, 1, 2);
				eventsFlexTable.getCellFormatter().setStyleName(row, 1, "eventListCombinedEvents");
			}
			else {
				eventsFlexTable.setText(row, 1, event.getFirstEventName());
				eventsFlexTable.setText(row, 2, event.getSecondEventName());
			}
			row++;
		}
	}
}
