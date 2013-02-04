package me.lukasdietrich.transfer;

public class KickTransfer extends Transfer {
	private static final long serialVersionUID = -1424284629997952261L;

	private String message;
	
	public KickTransfer(int portSender, String message, int portReceiver) {
		super(portSender, portReceiver);
		this.message = message;
	}
	
	public String getMessage() {
		return this.message;
	}

}
