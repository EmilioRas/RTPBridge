package  rtp.server;

import java.io.IOException;
import java.net.*;

import  start.RTPServerLog;

public class RTPServerReceiver extends RTPServer implements Runnable{

	protected RTPDataServer dataServer;

	protected InetSocketAddress dest;

	protected boolean byClient;

	public void setByClient(boolean byClient) {
		this.byClient = byClient;
	}

	public InetSocketAddress getDest() {
		return dest;
	}

	public void setDest(InetSocketAddress dest) {
		this.dest = dest;
	}

	public RTPServerReceiver(int port) throws IOException{
		super(port);

		RTPServerLog.log("Receiver datagram listen to : "+ this.getLocalAddress() +" | port : "+ this.getLocalPort());
		this.dataServer = new RTPDataServer();

	}
	
	public RTPServerReceiver(int port, InetAddress laddr) throws IOException{
		super(port,laddr);

		RTPServerLog.log("Receiver datagram listen to : " + this.getLocalAddress() +" | port : "+ this.getLocalPort());
		this.dataServer = new RTPDataServer();


	}


	@Override
	public void run() {

		synchronized (this.getDataServer()) {
			try {
				RTPServerLog.log("Listen left...");
				byte[] buffer = new byte[10000];
				DatagramPacket data = new DatagramPacket(buffer, 10000);


				this.receive(data);


				RTPServerLog.log("\t We have a new packet (lx)...");
				if (data != null) {
					RTPServerLog.log("\t\t New packets data... ");

					/*
					this.getDataServer().setPacket(data);

					if (this.dataServer.getPacket() != null) {
						RTPServerLog.log("\t\t data length:" + this.dataServer.getPacket().getData().length);
					} else {
						this.getDataServer().notify();
						return;
					}

					if (this.byClient) {
						//this.setDest(new InetSocketAddress(this.dataServer.getPacket().getAddress(),  this.dataServer.getPacket().getPort()));
					}


					RTPServerLog.log("\t\t Packet in data rtp lx server... start to send");
					RTPServerLog.log("\t\t Destinat Address is : " + this.getDest().getAddress());

					DatagramPacket hi = new DatagramPacket(this.dataServer.getPacket().getData(), this.dataServer.getPacket().getLength(), this.getDest());


					 */

					//this.setTmpPacket(hi);
					DatagramPacket hi = new DatagramPacket(data.getData(), data.getData().length, this.getDest());

					this.send(hi);
					RTPServerLog.log("\t\t END");
				} else {
					RTPServerLog.log("\t data packet received is null");

				}
				this.getDataServer().notify();


			} catch(Exception e){
				RTPServerLog.log(e.getMessage());
				this.getDataServer().notify();
			} finally{
				RTPServerLog.log("Return to ...");
			}
		}
	}

	public RTPDataServer getDataServer() {
		return dataServer;
	}

}
