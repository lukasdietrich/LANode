package me.lukasdietrich.client;

public class LogicalClient {

	private String name;
	private int port;
	
	public LogicalClient(String name, int port) {
		this.name = name;
		this.port = port;
	}
	
	public String getName() {
		return this.name;
	}
	
	public int getPort() {
		return this.port;
	}
	
}
