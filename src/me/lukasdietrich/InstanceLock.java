package me.lukasdietrich;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class InstanceLock {
	
	private static ServerSocket lockserver;
	private static JFrame window;
	
	public static void lockInstance(int port) {
		if(lockserver == null) {
			try {
				// no instance running
				lockserver = new ServerSocket(port);
				
				new Thread() {
					
					@Override
					public void run() {
						while(true) {
							try {
								
								Socket socket = lockserver.accept();
								ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
								
								if(window != null && in.readBoolean()) {
									window.setVisible(true);
									SwingUtilities.invokeLater(new Runnable() {
										
										@Override
										public void run() {
											window.setVisible(true);
											window.toFront();
											window.requestFocus();
										}
										
									});
								}
								
								in.close();
								socket.close();
								
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
					
				}.start();
				
			} catch (IOException e) {
				// instance already running
				
				try {
					Socket socket = new Socket(Strings.getString(InstanceLock.class, "0"), port);
					
					ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
					out.writeBoolean(true);
					
					out.close();
					
					socket.close();
				} catch (Exception e1) {
					// failed to bring instance to front
				}
				
				System.exit(0);
			}
		}
	}
	
	public static void setWindow(JFrame window) {
		InstanceLock.window = window;
	}
	
}
