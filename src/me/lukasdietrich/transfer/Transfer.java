package me.lukasdietrich.transfer;

import java.io.Serializable;

public abstract class Transfer implements Serializable {
	private static final long serialVersionUID = 9144634849347146798L;

	private int portSender;
	private int[] portReceiver;
	
	public Transfer(int portSender, int... portReceiver) {
		this.portSender = portSender;
		this.portReceiver = portReceiver;
	}
	
	public int getPortSender() {
		return portSender;
	}

	public int[] getPortReceiver() {
		return portReceiver;
	}
	
}
