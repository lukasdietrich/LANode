package me.lukasdietrich.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;

import me.lukasdietrich.assets.WavSound;
import me.lukasdietrich.transfer.ChatTransfer;
import me.lukasdietrich.transfer.FileTransfer;
import me.lukasdietrich.transfer.JoinResponseTransfer;
import me.lukasdietrich.transfer.JoinTransfer;
import me.lukasdietrich.transfer.LeaveTransfer;
import me.lukasdietrich.transfer.Transfer;

public class Client extends Thread {

	private Socket socket;
	
	private ObjectInputStream in;
	private ObjectOutputStream out;
	
	private ClientActivity activity;
	
	private String name;
	
	public Client(String host, int port, String name, ClientActivity activity) throws UnknownHostException, IOException {
		this.socket = new Socket(host, port);
		
		this.name = name;
		
		this.activity = activity;
		this.activity.setPort(getPort());
		this.activity.setClient(this);
		
		this.out = new ObjectOutputStream(socket.getOutputStream());
		this.in = new ObjectInputStream(socket.getInputStream());
		
		this.start();
	}
	 
	public String getNetworkName() {
		return this.name;
	}
	
	public void sendObject(Transfer content) throws IOException {
		this.out.writeObject(content);
	}
	
	private boolean closed = false;
	
	public void close() {
		try {
			closed = true;
			this.in.close();
			this.out.close();
			this.socket.close();
		} catch (IOException e) {
			// pfft. What should go wrong here? :D
		}
	}
	
	public int getPort() {
		return socket.getLocalPort();
	}
	
	@Override
	public void run() {
		while(true) {
			try {
				Object o = in.readObject();
				if(o != null) {
					// new client connected
					// add to list with name and port
					if(o instanceof JoinTransfer) {
						JoinTransfer trans = (JoinTransfer) o;
						LogicalClientsContainer.getInstance().add(new LogicalClient(trans.getName(), trans.getPortSender()));
						
						activity.addPortToChat(trans.getPortSender());
						sendObject(new JoinResponseTransfer(this.getPort(), trans.getPortSender(), this.name));
						
						activity.setConnectedCount(LogicalClientsContainer.getInstance().getAvailableCount());
					
					// client left session
					// remove from list
					} else if(o instanceof LeaveTransfer) {
						LeaveTransfer trans = (LeaveTransfer) o;
						LogicalClientsContainer.getInstance().remove(trans.getPortSender());
						
						activity.removePortFromChat(trans.getPortSender());

						activity.setConnectedCount(LogicalClientsContainer.getInstance().getAvailableCount());
						
					// this client joined
					// existing clients respond with name and port
					} else if(o instanceof JoinResponseTransfer) {
						JoinResponseTransfer trans = (JoinResponseTransfer) o;
						LogicalClientsContainer.getInstance().add(new LogicalClient(trans.getName(), trans.getPortSender()));
						
						activity.addPortToChat(trans.getPortSender());
						
						activity.setConnectedCount(LogicalClientsContainer.getInstance().getAvailableCount());
					} else if(o instanceof FileTransfer) {
						FileTransfer trans = (FileTransfer) o;

						WavSound.play("notif");
						
						trans.store();
					} else if(o instanceof ChatTransfer) {
						ChatTransfer trans = (ChatTransfer) o;

						WavSound.play("notif");
						activity.setVisible(true);
						
						activity.updateChat(trans);
					}
				}
			} catch (Exception e) {
				System.out.println(e.getLocalizedMessage());
				break;
			}
		}
		
		if(!closed) {
			JOptionPane.showMessageDialog(activity, "Server antwortet nicht mehr! :(");
		}
		
		activity.dispose();
		System.exit(1);
	}
	
}
