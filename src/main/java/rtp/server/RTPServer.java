package rtp.server;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import rtp.RTPAddressException;

public class RTPServer extends DatagramSocket{

	public static int bufSize = 1024;
	
	public RTPServer(int port, InetAddress laddr) throws SocketException {
		super(port, laddr);
		this.setSendBufferSize(RTPServer.bufSize);
		this.setReceiveBufferSize(RTPServer.bufSize);
	}
	
	public RTPServer(int port) throws SocketException {
		super(port);
		this.setSendBufferSize(RTPServer.bufSize);
		this.setReceiveBufferSize(RTPServer.bufSize);
	}

	/**
	 * 
	 * @param ip
	 * @param type at now olny false for ipv4
	 * @return
	 */
	public static InetAddress getInet(String ip, boolean type) throws RTPAddressException {
		if (!type && ip.split("\\.").length == 4){
			String[] ip4 = ip.split("\\.");
			try {
				long asLong = 0;
				byte[] bip4 = new byte[4];
				for (int i = 0; i < bip4.length; i++){
					asLong = (asLong << 8) | Integer.parseInt(ip4[i]);
					bip4[i] = (byte) asLong;
				}
				return InetAddress.getByAddress(bip4);
			} catch (Exception e){
				throw new RTPAddressException("Error in raw ipv4 address");
			}
		} else throw new RTPAddressException("Not input ipv4 address");
	}
	
	
}
