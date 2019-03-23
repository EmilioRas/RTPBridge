package  rtp.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;

import  start.RTPServerLog;

public class RTPServerReceive extends RTPServer implements Runnable{

	protected RTPDataServer dataServer;

	public RTPServerReceive(int port) throws IOException{
		super(port);
//		super.setReceiveBufferSize(256);
		RTPServerLog.log("Receiver datagram listen to : "+ this.getLocalAddress() +" | port : "+ this.getLocalPort());
		this.dataServer = new RTPDataServer();		
	}
	
	public RTPServerReceive(int port,InetAddress laddr) throws IOException{
		super(port,laddr);
//		super.setReceiveBufferSize(256);
		RTPServerLog.log("Receiver datagram listen to : " + this.getLocalAddress() +" | port : "+ this.getLocalPort());
		this.dataServer = new RTPDataServer();		
	}
	
	
	@Override
	public void run() {
		try {
			RTPServerLog.log("Listen ...");
			
			byte[] buf = new byte[RTPServer.bufSize];
			int length = RTPServer.bufSize;
			DatagramPacket data = new DatagramPacket(buf, length);
			synchronized (this.dataServer) {
				this.receive(data);
				RTPServerLog.log("\t We have a new packet...");
				if (data != null) {
					RTPServerLog.log("\t\t Packets received!!! from address:"+this.dataServer.getPacket().getAddress().getHostAddress()+
							" | port: "+this.dataServer.getPacket().getPort());
					RTPServerLog.log("\t\t New packets data... ");
					RTPServerLog.log("\t\t data length:" + this.dataServer.getPacket().getLength());
					this.dataServer.setPacket(data);
					RTPServerLog.log("\t\t Packet in data rtp server...");
					this.dataServer.notify();
				} else {
					RTPServerLog.log("\t data packet received is null");
				}
			}
			
		} catch (Exception e){
			RTPServerLog.log(e.getMessage());
		} finally {
			this.close();
			RTPServerLog.log("Return to ...");
		}
	}

	public RTPDataServer getDataServer() {
		return dataServer;
	}

	public void setDataServer(RTPDataServer dataServer) {
		this.dataServer = dataServer;
	}
	
	
}
