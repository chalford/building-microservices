package uk.co.bbc.vivo.poster.integration;

import java.util.Date;

public class Event {

	private final String type;
	private final String body;
	private final Date dateTime;
	
	
	public Event(String type, String body, Date dateTime) {
		this.type = type;
		this.body = body;
		this.dateTime = dateTime;
	}
	
	public String getType() {
		return type;
	}
	
	public String getBody() {
		return body;
	}
	
	public Date getDateTime() {
		return dateTime;
	}
}
