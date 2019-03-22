package rtp.client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;

import main.RTPServerLog;
import rtp.server.RTPServer;
import rtp.server.RTPDataServer;

public class RTPClientSend extends RTPServer implements Runnable{

	protected RTPDataClient dataClient;
	
	protected InetAddress address;
	
	protected int port;
	
	public RTPClientSend(int port) throws IOException{
		super(port);
//		super.setSendBufferSize(256);
		this.dataClient = new RTPDataClient();
	}
	
	public RTPClientSend(int port,InetAddress laddr) throws IOException{
		super(port,laddr);
//		super.setSendBufferSize(256);
		this.dataClient = new RTPDataClient();
	}
	
	
	@Override
	public void run() {
		try{
			synchronized (this.dataClient) {
				this.dataClient.getPacket().setAddress(address);
				this.dataClient.getPacket().setPort(port);
				if (this.dataClient.getPacket() != null) {
					this.send(this.dataClient.getPacket());
					RTPServerLog.log("\t New packets ,address to:"+address.getHostName()+" | port: "+port+" ...");
				} else {
					RTPServerLog.log("\t Cannot send new packets null to: "+address.getHostName()+" | port: "+port+"!!!");
				}
				this.dataClient.notify();
			}		
		} catch (Exception e){
			RTPServerLog.log(e.getMessage());
		} finally {
			this.close();
		}
	}

	public RTPDataClient getDataClient() {
		return dataClient;
	}

	public void setDataClient(RTPDataClient dataClient) {
		this.dataClient = dataClient;
	}

	public InetAddress getAddress() {
		return address;
	}

	public void setAddress(InetAddress address) {
		this.address = address;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	
}
