package me.lukasdietrich.commons;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class Network {

	private static String adress;
	
	/**
	 * returns LAN ip if accessible.
	 * else 127.0.0.1 is returned.
	 * 
	 * @return
	 * @throws SocketException
	 */
	public static String getAddress() {
		if(adress != null)
			return adress;
		
		try {
			Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
			while (interfaces.hasMoreElements()) {
				
			    NetworkInterface current = interfaces.nextElement();
			    Enumeration<InetAddress> addresses = current.getInetAddresses();
			    
			    while (addresses.hasMoreElements()){
			        InetAddress current_addr = addresses.nextElement();
			        
			        if(current_addr.getHostAddress().split("\\.").length == 4 && !current_addr.getHostAddress().equals("127.0.0.1"))
			        	return adress = current_addr.getHostAddress();
			    }
			}
		} catch (SocketException e) {
			Logger.get().err(e, null);
		}
		
		return adress = "127.0.0.1";
	}
	
	public static void searchHosts(final int port, final ProgressListener listener) {
		new Thread() {
			
			@Override
			public void run() {
				
				boolean cancel = false;
				
				try {
					String baseHost = Network.getAddress().substring(0, Network.getAddress().lastIndexOf(".")+1);
					
					for(int i = 1; i < 254 && !cancel; i++) {
						
						int p = (int)((i / 254.0) * 100);
						
						if(InetAddress.getByName(baseHost + i).isReachable(1500)) {
							if(!listener.percentChanged(p, baseHost + i))
								cancel = true;
						} else {
							if(!listener.percentChanged(p))
								cancel = true;
						}
						
						
						
					}
					
				} catch(Exception e) {
					Logger.get().err(e, this);
				} finally {
					listener.progressDone(!cancel, this);
				}
				
			}
			
		}.start();
	}
	
}
