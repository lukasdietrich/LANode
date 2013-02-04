package me.lukasdietrich.commons;

public class LogEvent {

	private String message;
	private boolean isError;
	
	public LogEvent(String message, boolean isError) {
		this.message = message;
		this.isError = isError;
	}
	
	public String getMessage() {
		return this.message;
	}
	
	public boolean isError() {
		return this.isError;
	}
	
}
