package me.lukasdietrich.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Map.Entry;

import me.lukasdietrich.Strings;
import me.lukasdietrich.commons.Logger;
import me.lukasdietrich.commons.Network;
import me.lukasdietrich.transfer.BanTransfer;
import me.lukasdietrich.transfer.ChatTransfer;
import me.lukasdietrich.transfer.FileTransfer;
import me.lukasdietrich.transfer.JoinResponseTransfer;
import me.lukasdietrich.transfer.JoinTransfer;
import me.lukasdietrich.transfer.KickTransfer;
import me.lukasdietrich.transfer.LeaveTransfer;
import me.lukasdietrich.transfer.Transfer;

public class Server extends Thread {

	private ServerSocket host;
	private Hashtable<Integer, SocketHandler> clients;
	
	private LinkedList<InetAddress> banned;
	
	public Server(int port) throws IOException {
		this.clients = new Hashtable<Integer, SocketHandler>();
		this.banned  = new LinkedList<InetAddress>();
		
		this.host = new ServerSocket(port);
		
		this.start();
	}
	
	public String getAddress() {
		return Network.getAddress();
	}
	
	public void handleTransfer(Transfer transfer) {
		sendToPort(transfer);
		
		if(transfer instanceof KickTransfer) {
			clients.remove(transfer.getPortReceiver()[0]).close();
		} else if(transfer instanceof BanTransfer) {
			SocketHandler handler = clients.remove(transfer.getPortReceiver()[0]);
			banned.add(handler.getAddress());
			handler.close();
		}
	}
	
	public void sendToPort(Transfer trans) {
		
		if(trans.getPortReceiver().length == 0) {
			// @ all
			for(Entry<Integer, SocketHandler> entry : clients.entrySet()) {
				if(entry.getKey() != trans.getPortSender()) {
					try {
						entry.getValue().sendObject(trans);
					} catch (IOException e) {
						System.out.println(Strings.getString(Server.class, "0"));
					}
				}
			}
		} else {
			// @ list of ports
			for(int p : trans.getPortReceiver()) {
				try {
					clients.get(p).sendObject(trans);
				} catch (IOException e) {
					System.out.println(Strings.getString(Server.class, "1"));
				}
			}
		}
	}
	
	public void connectionClosed(SocketHandler handler) {
		clients.remove(handler);
		Logger.get().log(Strings.getString(Server.class, "2")+ handler.getPort() +Strings.getString(Server.class, "3"), this); //$NON-NLS-2$
		
		sendToPort(new LeaveTransfer(handler.getPort()));
	}
	
	@Override
	public void run() {
		Logger.get().log(Strings.getString(Server.class, "4") +" @ "+ getAddress(), this);
		
		while(true) {
			Logger.get().log(Strings.getString(Server.class, "5"), this);
			
			try {
				Socket newSocket = this.host.accept();
				
				if(banned.contains(newSocket.getInetAddress())) {
					newSocket.close();
					
					Logger.get().log(Strings.getString(Server.class, "7")+ newSocket.getPort(), this);
				} else {
					SocketHandler newHandler = new SocketHandler(newSocket, this);
					clients.put(newHandler.getPort(), newHandler);
					
					Logger.get().log(Strings.getString(Server.class, "6")+ newHandler.getPort(), this);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
}
