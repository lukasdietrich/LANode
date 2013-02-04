package me.lukasdietrich.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.Socket;

import me.lukasdietrich.transfer.Transfer;

public class SocketHandler extends Thread {

	private Server server;
	private Socket socket;
	
	private ObjectInputStream in;
	private ObjectOutputStream out;
	
	public SocketHandler(Socket socket, Server server) throws IOException {
		this.socket = socket;
		this.server = server;
		
		this.out = new ObjectOutputStream(socket.getOutputStream());
		
		this.start();
	}
	
	public void close() {
		try {
			in.close();
			out.close();
			socket.close();
		} catch (IOException e) {}
	}
	
	public InetAddress getAddress() {
		return socket.getInetAddress();
	}
	
	public int getPort() {
		return socket.getPort();
	}
	
	public void sendObject(Serializable content) throws IOException {
		this.out.writeObject(content);
	}
	
	@Override
	public void run() {

		try {
			this.in = new ObjectInputStream(socket.getInputStream());
		} catch (IOException e1) {
			server.connectionClosed(this);
			return;
		}
		
		while(true) {
			try {
				Object o = in.readObject();
				if(o != null)
					server.handleTransfer((Transfer) o);
			} catch (Exception e) {
				break;
			}
		}
		
		server.connectionClosed(this);
		
	}
	
}
