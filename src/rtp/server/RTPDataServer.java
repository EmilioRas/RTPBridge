package rtp.server;

import java.net.DatagramPacket;

import main.RTPServerLog;
import rtp.HeaderFormat;
import rtp.RTPHeaderException;
import rules.RTPHeader;

public class RTPDataServer extends HeaderFormat implements RTPHeader{

	public RTPDataServer() {
		super();
		RTPServerLog.log("New data server for rtp protocol...");
	}
	
	protected DatagramPacket packet;
	
	protected byte[] content;

	@Override
	public void setPacket(DatagramPacket packet) throws RTPHeaderException{
		this.packet = packet;
		if (this.packet != null) {
			RTPServerLog.log("\t Packet is not null. Setting this on server...");
			RTPServerLog.log("\t Datagram packet length is :" +packet.getLength());
			try {
				RTPServerLog.log("\t Start parsing header format for data Packet");
				RTPServerLog.log("\t Not setting header length yet");
				RTPServerLog.log("\t but setting data in header...");
				this.header = packet.getData();
				this.setAllFromHeader();
				if (this.cc > 0) {
					this.headerLen = this.headerLen + this.cc * 4;
					RTPServerLog.log("\t\t New Header Length : " + this.headerLen);
				}
				
				if (this.extentionX == 1) {
					this.headerLen = this.headerLen + this.extLength * 4;
					RTPServerLog.log("\t\t New Header Length : " + this.headerLen);
				}
				
				RTPServerLog.log("\t Setting Header Length ...");
				this.setHeaderLen(this.headerLen);
				
				if (packet.getLength() > this.headerLen) {
					RTPServerLog.log("\t\t Separe header from content ...");
					
					this.header = new byte[this.headerLen];
					
					byte[] content = new byte[packet.getLength() - this.headerLen];
					
					for (int i = 0; i < packet.getLength(); i++) {
						if (i < this.headerLen) {
							this.header[i] = packet.getData()[i];
						} else if (i >= this.headerLen){
							content[i - this.headerLen] = packet.getData()[i];
						}
						RTPServerLog.logNoN(".");
					}
					RTPServerLog.log("\t\t Setting data rtp");
					this.setData(content);
					RTPServerLog.log("\t\t New parsing header format for new header and header length. Security check...");
					this.setAllFromHeader();
					
				} else {
					RTPServerLog.log("\t\t Only Header in packet");
				}
				
				RTPServerLog.log("\t End set packet!");
			} catch (Exception e) {
				RTPServerLog.log("An error occurred in set header properties on server:" + e.getMessage());
				throw new RTPHeaderException(e);
			}
		} else {
			RTPServerLog.log("Sorry! Packet is null");
		}
	}

	@Override
	public void setAllFromHeader() throws RTPHeaderException {
		this.genericReadAllFromHeader();
	}

	public DatagramPacket getPacket() {
		return packet;
	}

	@Override
	public void setData(byte[] data) {
		this.content = data;
	}

	@Override
	public byte[] getData() {
		return this.content;
	}

	

}
