package me.lukasdietrich.client;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;

public class LogicalClientsContainer implements Iterable<LogicalClient> {

	private static LogicalClientsContainer instance;
	public static LogicalClientsContainer getInstance() {
		if(instance == null)
			instance = new LogicalClientsContainer();
		return instance;
	}
	
	private Hashtable<Integer, LogicalClient> table;
	private LinkedList<LogicalClientsContainerListener> listeners;
	
	private LogicalClientsContainer() {
		this.listeners = new LinkedList<LogicalClientsContainerListener>();
		this.table = new Hashtable<Integer, LogicalClient>();
	}
	
	public int getAvailableCount() {
		return table.size();
	}
	
	public void add(LogicalClient client) {
		if(!table.containsValue(client)) {
			table.put(client.getPort(), client);
			
			for(LogicalClientsContainerListener listener : listeners) {
				listener.clientAdded(client.getPort());
			}
		}
	}
	
	public LogicalClient get(int port) {
		return this.table.get(port);
	}
	
	public void remove(int port) {
		table.remove(port);
		
		for(LogicalClientsContainerListener listener : listeners) {
			listener.clientRemoved(port);
		}
	}
	
	public String[] listForNames() {
		String[] names = new String[table.size()];
		
		int i = 0;
		
		for(Enumeration<LogicalClient> clients = table.elements(); clients.hasMoreElements(); i++) {
			names[i] = clients.nextElement().getName();
		}
		
		return names;
	}
	
	public LogicalClient getClientForName(String name) {
		for(Enumeration<LogicalClient> clients = table.elements(); clients.hasMoreElements();) {
			
			LogicalClient client;
			if((client = clients.nextElement()).getName().equals(name)) {
				return client;
			}
		}
		
		return null;
		
	}

	public boolean addListener(LogicalClientsContainerListener listener) {
		return this.listeners.add(listener);
	}
	
	public boolean removeListener(LogicalClientsContainerListener listener) {
		return this.listeners.remove(listener);
	}
	
	@Override
	public Iterator<LogicalClient> iterator() {
		return new LogicalClientIterator();
	}
	
	public class LogicalClientIterator implements Iterator<LogicalClient> {

		private Enumeration<LogicalClient> enu;
		
		public LogicalClientIterator() {
			enu = table.elements();
		}
		
		@Override
		public boolean hasNext() {
			return enu.hasMoreElements();
		}

		@Override
		public LogicalClient next() {
			return enu.nextElement();
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}
		
	}
	
}
