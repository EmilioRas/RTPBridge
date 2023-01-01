package rtp.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import rtp.RTPAddressException;

public class RTPServer extends DatagramSocket{

	protected static String nInterface;

	public static void setnInterface(String nInterface) {
		RTPServer.nInterface = nInterface;
	}

	public static String getnInterface() {
		return nInterface;
	}




	
	public RTPServer(int port, InetAddress laddr) throws SocketException {
		super(port, laddr);

		this.setBroadcast(true);

	}
	
	public RTPServer(int port) throws SocketException {
		super(port);

	}


	@Override
	public void receive(DatagramPacket data) throws IOException {
		super.receive(data);
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
