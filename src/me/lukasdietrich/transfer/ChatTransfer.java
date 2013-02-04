package me.lukasdietrich.transfer;

import me.lukasdietrich.commons.DateTime;

public class ChatTransfer extends Transfer {
	private static final long serialVersionUID = 1194845062362368232L;

	private String dateTime;
	private String message;
	
	public ChatTransfer(int portSender, String message, int... portReceiver) {
		super(portSender, portReceiver);
		
		this.message = message;
		this.dateTime = DateTime.format("%d.%M %h:%m");
	}
	
	public String getMessage() {
		return this.message;
	}
	
	public String getDateTime() {
		return this.dateTime;
	}

}
