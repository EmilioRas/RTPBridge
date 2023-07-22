package rtp.client;

import java.io.IOException;

import java.net.DatagramPacket;

import java.net.InetAddress;
import rtp.server.RTPServer;
import start.RTPServerLog;


public class RTPClientSend extends RTPServer implements Runnable{

	protected DatagramPacket fromClient;

	public DatagramPacket getFromClient() {
		return fromClient;
	}

	public void setFromClient(DatagramPacket fromClient) {
		this.fromClient = fromClient;
	}

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
				this.receive(this.getFromClient());
				if (this.dataClient.getPacket() != null) {

					 	RTPServerLog.log("\t New packets ,address to:"+address.getHostAddress()+" | port: "+port+" ...");

							this.send(this.dataClient.getPacket());

				} else {
					if (address != null && address.getHostAddress() != null)
						RTPServerLog.log("\t Cannot send new packets null to: "+address.getHostName()+" | port: "+port+"!!!");

				}
				this.dataClient.notify();
			}		
		} catch (Exception e){
			RTPServerLog.log(e.getMessage());
		} finally {

			RTPServerLog.log("End to send...");

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
