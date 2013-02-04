package me.lukasdietrich.client;

public interface LogicalClientsContainerListener {

	public void clientAdded(int port);
	public void clientRemoved(int port);
	
}
