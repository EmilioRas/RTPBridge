package rtp.client;

import java.net.DatagramPacket;

import main.RTPServerLog;
import rtp.HeaderFormat;
import rtp.RTPHeaderException;
import rules.RTPHeader;

public class RTPDataClient extends HeaderFormat implements RTPHeader{

	public RTPDataClient() {
		super();
	}
	
	protected DatagramPacket packet;
	
	protected byte[] content;

	@Override
	public void setHeader(byte[] header) {
		this.setHeaderLen(this.packet != null ? this.packet.getLength() : RTPHeader.headerminlength);
		if (this.packet != null) {
			RTPServerLog.log("Packet is not null. Setting this...");
			this.setHeader(this.packet.getData());
			try {
				this.setAllFromHeader();
			} catch (Exception e) {
				RTPServerLog.log("An error occurred in set header properties :" + e.getMessage());
			}
		}
	}

	@Override
	public void setAllFromHeader() throws RTPHeaderException {
		this.genericReadAllFromHeader();
	}

	public DatagramPacket getPacket() {
		return packet;
	}

	public void setPacket(DatagramPacket packet) {
		this.packet = packet;
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
