package me.lukasdietrich.transfer;

public class JoinTransfer extends Transfer {
	private static final long serialVersionUID = 4731838957366960365L;

	private String name;
	
	public JoinTransfer(int portSender, String name) {
		super(portSender);
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}

}
