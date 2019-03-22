package rtp.client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class RTPClient extends DatagramSocket{

	private int port;

	private InetAddress laddr;
	
	public static InetAddress getInet(String ip, boolean type){
		//TODO
		return null;
	}

	public RTPClient(int port,InetAddress laddr) throws SocketException{
		super(port,laddr);
		this.port = port;
		this.laddr = laddr;
	}
	
	@Override
	public void send(DatagramPacket p) throws IOException{
		super.send(p);
	}
	
	@Override
	public void receive(DatagramPacket p) throws IOException{
		super.receive(p);
	}
}
