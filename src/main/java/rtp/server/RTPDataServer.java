package rtp.server;

import java.net.DatagramPacket;

import  start.RTPServerLog;
import  rtp.HeaderFormat;
import  rtp.RTPHeaderException;
import rules.RTPHeader;

public class RTPDataServer extends HeaderFormat implements RTPHeader{

	public RTPDataServer() {
		super();
		RTPServerLog.log("New data server for rtp protocol...");
	}
	
	protected DatagramPacket packet;
	
	protected byte[] content;


	public void setAndByPassPacket(DatagramPacket packet){
		this.packet = packet;
	}

	@Override
	public void setPacket(DatagramPacket packet) throws RTPHeaderException {

		synchronized (this) {
			if (packet != null) {
				this.header = new byte[packet.getLength()];
				int d = 0;
				do {
					this.header[d] =  packet.getData()[d];
					d++;
				} while (d < packet.getLength());

				RTPServerLog.log("\t Packet is not null. Setting this on server...");
				RTPServerLog.log("\t Datagram packet length is :" + packet.getLength());
				try {
					RTPServerLog.log("\t Start parsing header format for data Packet");
					RTPServerLog.log("\t Not setting header length yet");
					RTPServerLog.log("\t but setting data in header...");
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

						byte[] content = (packet.getLength() - this.headerLen > 0 ? new byte[packet.getLength() - this.headerLen] : null);

						for (int i = 0; packet.getLength() >= this.headerLen && i < packet.getLength(); i++) {
							RTPServerLog.logNoN(". > :");
							if (i < this.headerLen) {
								this.header[i] = packet.getData()[i];
								RTPServerLog.logNoN(new String(new byte[]{this.header[i]}));
							} else if (i >= this.headerLen && content != null) {
								content[i - this.headerLen] = packet.getData()[i];
								RTPServerLog.logNoN(new String(new byte[]{content[i - this.headerLen]}));
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
					byte[] buf = new byte[this.header.length];
					int length = this.header.length;
					DatagramPacket data = new DatagramPacket(buf, length);
					data.setData(header);
					this.packet = data;
				} catch (Exception e) {
					RTPServerLog.log("An error occurred in set header properties on server:" + e.getMessage());
					throw new RTPHeaderException(e);
				}
			} else {
				RTPServerLog.log("Sorry! Packet is null");
			}

		}
	}

	@Override
	public void setAllFromHeader() throws RTPHeaderException {
		try {
			this.genericReadAllFromHeader();
		} catch (Exception e){
			throw new RTPHeaderException(e);
		}
	}

	public DatagramPacket getPacket() {

		return this.packet;

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
