package me.lukasdietrich;

import java.io.IOException;
import java.net.UnknownHostException;

import javax.swing.UIManager;

import me.lukasdietrich.client.Client;
import me.lukasdietrich.client.ClientActivity;
import me.lukasdietrich.client.InitActivity;
import me.lukasdietrich.commons.Logger;
import me.lukasdietrich.server.Server;
import me.lukasdietrich.transfer.JoinTransfer;

public class LANode {

	public final static int PORT = 25569;
	
	/**
	 * @param args
	 * @throws IOException 
	 * @throws UnknownHostException 
	 */
	public static void main(String[] args) {
		
		if(args.length > 0 && args[0].equalsIgnoreCase("nogui")) {
			try {
				@SuppressWarnings("unused")
				Server server = new Server(PORT);
			} catch (IOException e1) {
				Logger.get().log(Strings.getString(LANode.class, "0"), null);
			}
		} else {
			// just one LANode per user
			/**
			 * TODO
			 * - uncomment
			 */
			InstanceLock.lockInstance(PORT - 1);
			
			try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} catch (Exception e) {
				// no worries.
			}
			
			InitActivity init = new InitActivity();
			init.setVisible(true);
			
			String host = init.getAdress();
			boolean ishost = init.isHost();
			
			if(ishost) {
				try {
					Server server = new Server(PORT);
					host = server.getAddress();
				} catch (IOException e1) {
					host = "localhost"; //$NON-NLS-1$
					Logger.get().log(Strings.getString(LANode.class, "0"), null);
					ishost = false;
				}
			}
			
			ClientActivity gui;
			Client client;
			
			try {
				gui = new ClientActivity(ishost);
				client = new Client(host, PORT, init.getUser(), gui);
				client.sendObject(new JoinTransfer(client.getPort(), init.getUser()));
				gui.setTitle(gui.getTitle() +" @ "+ host);
				gui.setVisible(true);
				
				InstanceLock.setWindow(gui);
			} catch (UnknownHostException e) {
				Logger.get().err(e, null);
			} catch (IOException e) {
				Logger.get().err(e, null);
			}
		}
		
	}

}
