package me.lukasdietrich.transfer;

public class JoinResponseTransfer extends Transfer {
	private static final long serialVersionUID = -1400885727654975834L;

	private String name;
	
	public JoinResponseTransfer(int portSender, int portReceiver, String name) {
		super(portSender, portReceiver);
		
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}

}
