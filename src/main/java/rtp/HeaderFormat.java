package  rtp;

import java.net.DatagramPacket;
import java.util.ArrayList;
import java.util.List;

import  start.RTPServerLog;
import  rules.RTPHeader;

public abstract class HeaderFormat {

	protected byte[] header;
	
	protected int headerLen = RTPHeader.headerminlength;
	
	protected boolean extensionHeader = false;
	
	protected short version;
	
	protected short padding;
	
	protected short extentionX;
	
	protected short cc;
	
	protected short m;
	
	protected short paylType;
	
	protected short lowSequenceNum;
	
	protected short highSequenceNum;
	
	protected int timestamp;
	
	protected int syncSrc;
	
	protected List<Integer> contribSrc;
	
	protected short extHeaderID = -1;
	
	protected short extLength = -1;
	
	protected List<Integer> extensions;
	
	public byte[] getHeader(){
		return this.header;
	}
	
	public HeaderFormat(){
		this.header = new byte[this.headerLen];

		RTPServerLog.log("Create new header for empty");
 	}
	
	public HeaderFormat(int headerlen){
		this.header = new byte[headerlen];

		RTPServerLog.log("Create new header for  empty");
 }
	
	public HeaderFormat(byte[] header) throws Exception{
		if (header.length < RTPHeader.headerminlength) {

			RTPServerLog.log("Cannot create new header. Header rtp less than minimun for this protocol.");

			throw new Exception("Header rtp less than minimun for this protocol");
		}
		
		this.header = header;
		this.headerLen = this.header.length;

		RTPServerLog.log("Create new full header ");

	}
	
	public void setHeaderLen(int l){
		this.headerLen = l;

		RTPServerLog.log("Set new length for  header : " + l);

	}
	
	
	public void setHeader(byte[] header) {
		this.header = header;
	}

	public abstract void setAllFromHeader()  throws RTPHeaderException;
	
	public abstract void setPacket(DatagramPacket packet) throws RTPHeaderException;
	
	protected void genericReadAllFromHeader() throws RTPHeaderException {
		try {
			if (this.header != null && this.header.length >= RTPHeader.headerminlength) {

				this.readVersion();
				this.readPadding();
				this.readExtention();
				this.readCSRCCount();


				this.readMarker();
				this.readPayloadType();
				this.readSequenceNumber();
				this.readTimestamp();
				this.readSynchronizzationSrc();
				this.readContributingSrc();
				this.readExtensions();
				RTPServerLog.log("Set new header All fields for " + this.getClass().getName() + ".");
			} else {
				throw new RTPHeaderException("Header null or not right length");
			}
		} catch (Exception e) {
			throw new RTPHeaderException(e);
		}
	}
	/**
	* Ver.
	* Indicates the version of the protocol. Current version is 2
	*/
	protected void readVersion() throws RTPHeaderException{
		if (this.header != null && this.header.length >= 1) {
			this.version = (short)(this.header[0] & 0x03);
		} else {
			throw new RTPHeaderException("version : Header null or not right length 1 idx.");
		}
		RTPServerLog.log("Protocol version is " + this.version);
	}	
	/**
	* P
	* Used to indicate if there are extra padding bytes at the end of the RTP packet. 
	* A padding might be used to fill up a block of certain size, for example as required by an encryption algorithm. 
	* The last byte of the padding contains the number of padding bytes that were added (including itself)
	*/
	protected void readPadding() throws RTPHeaderException{
		if (this.header != null && this.header.length >= 1) {
			this.padding = (short)((this.header[0] >> 2) & 0x01);
		} 
		else {
			throw new RTPHeaderException("padding :Header null or not right length 1 idx.");
		}
		RTPServerLog.log("Padding is " + this.padding);
	}
	/**
	* X
	* Indicates presence of an Extension header between standard header and payload data. 
	* This is application or profile specific
	*/
	protected void readExtention() throws RTPHeaderException{
		if (this.header != null && this.header.length >= 1) {
			this.extentionX = (short)((this.header[0] >> 3) & 0x01);
		} 
		else {
			throw new RTPHeaderException("x :Header null or not right length 1 idx.");
		}
		RTPServerLog.log("X (extension) is " + this.extentionX);
	}

	/**
	* CC
	* Contains the number of CSRC identifiers (defined below) that follow the fixed header
	*/
	protected void readCSRCCount() throws RTPHeaderException{
		if (this.header != null && this.header.length >= 1) {

			this.cc = (short)(this.header[0] & 0xF0);

		} 
		else {
			throw new RTPHeaderException("cc :Header null or not right length 1 idx.");
		}
		RTPServerLog.log("CC is " + this.cc);	
	}

	/**
	* M
	* Used at the application level and defined by a profile. 
	* If it is set, it means that the current data has some special relevance for the application
	*/
	protected void readMarker() throws RTPHeaderException{
		if (this.header != null && this.header.length >= 2) {
			this.m = (short)(this.header[1] & 0x01);
		} 
		else {
			throw new RTPHeaderException("m :Header null or not right length 2 idx.");
		}
		RTPServerLog.log("Marker is " + this.cc);				
	}	
	
	/**
	* PT
	* Indicates the format of the payload and determines its interpretation by the application. 
	* This is specified by an RTP profile. 
	* For example, see RTP Profile for audio and video conferences with minimal control (RFC 3551)
	*/
	protected void readPayloadType() throws RTPHeaderException{
		if (this.header != null && this.header.length >= 2) {

			this.paylType = (short)((this.header[1] >> 1 ) & 0xFF);

		} 
		else {
			throw new RTPHeaderException("PT :Header null or not right length 2 idx.");
		}
		RTPServerLog.log("PT is " + this.paylType);
	}

	/**
	* Sequence Number
	* The sequence number is incremented by one for each RTP data packet sent and is to be used by the receiver to detect packet loss and to restore packet sequence. 
	* The RTP does not specify any action on packet loss; it is left to the application to take appropriate action. 
	* For example, video applications may play the last known frame in place of the missing frame. According to RFC 3550, 
	* the initial value of the sequence number should be random to make known-plaintext attacks on encryption more difficult. RTP provides no guarantee of delivery, 
	* but the presence of sequence numbers makes it possible to detect missing packets
	*/
	protected void readSequenceNumber() throws RTPHeaderException{
		if (this.header != null && this.header.length >= 3) {
			this.lowSequenceNum = (short)(this.header[2]  & 0xFF);
		} 
		else {
			throw new RTPHeaderException("L Sequ Num :Header null or not right length 3 idx.");
		}
		RTPServerLog.log("L Sequence Number is " + this.lowSequenceNum);
		
		if (this.header != null && this.header.length >= 4) {
			this.highSequenceNum = (short)(this.header[3]  & 0xFF);
		} 
		else {
			throw new RTPHeaderException("H Sequ Num :Header null or not right length 3 idx.");
		}
		RTPServerLog.log("H Sequence Number is " + this.highSequenceNum);
	}	
	
	/**
	* TimeStamp
	* Used by the receiver to play back the received samples at appropriate time and interval.
	* When several media streams are present, the timestamps may be independent in each stream.
	* The granularity of the timing is application specific.
	* For example, an audio application that samples data once every 125 �s (8 kHz, a common sample rate in digital telephony) would use that value as its clock resolution. 
	* Video streams typically use a 90 kHz clock. 
	* The clock granularity is one of the details that is specified in the RTP profile for an application
	*/
	protected void readTimestamp() throws RTPHeaderException{
		if (this.header != null && this.header.length >= 7) {
			this.timestamp = (((int)(this.header[7]  & 0xFF)) * 256^4) + 
					(((int)(this.header[6]  & 0xFF)) * 256^3) + 
					(((int)(this.header[5]  & 0xFF)) * 256^2) + 
					((int)(this.header[4]  & 0xFF));
			RTPServerLog.log("Time stamp is " + this.timestamp);
		} 
		else {
			throw new RTPHeaderException("Timestamp :Header null or not right length 7 idx.");
		}
	}
	
	/**
	* SSRC
	* Synchronization source identifier uniquely identifies the source of a stream. The synchronization sources within the same RTP session will be unique
	*/
	protected void readSynchronizzationSrc() throws RTPHeaderException{
		if (this.header != null && this.header.length >= 11) {
			this.syncSrc = (((int)(this.header[11]  & 0xFF)) * 256^4) + 
					(((int)(this.header[10]  & 0xFF)) * 256^3) + 
					(((int)(this.header[9]  & 0xFF)) * 256^2) + 
					((int)(this.header[8]  & 0xFF));
			RTPServerLog.log("SynchronizzationSrc is " + this.syncSrc);
		} 
		else {
			throw new RTPHeaderException("Synchronizzation Src :Header null or not right length 11 idx.");
		}
	}
	
	/**
	* CSRC
	* 32 bits each, number indicated by CSRC count field) Contributing source IDs enumerate contributing sources to a stream which has been generated from multiple sources
	*/
	protected void readContributingSrc() throws RTPHeaderException{
		this.contribSrc = new ArrayList<Integer>(this.cc);
		for (int c = 0; c < this.cc ; c++) {
			if (this.header != null && this.header.length > (15 + c * 4)) {

				this.contribSrc.add(Integer.MAX_VALUE);

				this.contribSrc.set(c,(((int)(this.header[15 + c * 4]  & 0xFF)) * 256^4) + 
						(((int)(this.header[14 + c * 4]  & 0xFF)) * 256^3) + 
						(((int)(this.header[13 + c * 4]  & 0xFF)) * 256^2) + 
						((int)(this.header[12 + c * 4]  & 0xFF)));
				

			} 
			else {

				//throw new RTPHeaderException("Contributing Src :Header null or not right length "+(15 + c * 4)+" idx.");
				return;

			}
		}
		
		
	}
	
	/**
	 * Extensions if X == 1
	 * (optional, presence indicated by Extension field) 
	 * The first 32-bit word contains a profile-specific identifier (16 bits) and a length specifier (16 bits) that indicates the length of the extension 
	 * (EHL = extension header length) in 32-bit units, excluding the 32 bits of the extension header
	 * @throws RTPHeaderException
	 */
	protected void readExtensions() throws RTPHeaderException{
		if (this.extentionX == 1) {
			this.extHeaderID = (short) ((this.header[(1+RTPHeader.headerminlength+this.cc)] * 256^2)+ ((this.header[(RTPHeader.headerminlength+this.cc)])));
			RTPServerLog.log("Extension header ID in idx "+(1+RTPHeader.headerminlength+this.cc)+ ","+(RTPHeader.headerminlength+this.cc)+" is " + this.extHeaderID);
			this.extLength = (short)((this.header[(3+RTPHeader.headerminlength+this.cc)] * 256^2)+ ((this.header[(2+RTPHeader.headerminlength+this.cc)])));
			RTPServerLog.log("Extension Length in idx "+(3+RTPHeader.headerminlength+this.cc)+ ","+(2+RTPHeader.headerminlength + this.cc)+" is " + this.extLength);
			
			this.extensions = new ArrayList<Integer>();


			for (int c = 0; c < extLength ; c++) {
				if (this.header != null && this.header.length >= (7+RTPHeader.headerminlength+this.cc+c*4)) {
					this.extensions.add(-1);
					this.extensions.set(c,(((int)(this.header[7+RTPHeader.headerminlength+this.cc+c*4]  & 0xFF)) * 256^4) + 
							(((int)(this.header[6+RTPHeader.headerminlength+this.cc+c*4]  & 0xFF)) * 256^3) + 
							(((int)(this.header[5+RTPHeader.headerminlength+this.cc+c*4]  & 0xFF)) * 256^2) + 
							(((int)(this.header[4+RTPHeader.headerminlength+this.cc+c*4]  & 0xFF)) * 256));
					
					RTPServerLog.log("Extension in idx "+c+ " is " + this.extensions.get(c));
				} 
				else {
					throw new RTPHeaderException("Extension :Header null or not right length "+(7+RTPHeader.headerminlength+this.cc+this.extLength)+" idx.");
				}
			}
		} else {
			RTPServerLog.log("There is not extensions .");
		}
	}

	/**
	* indica la lunghezza corrente dell'header
	*/
	public int getHeaderLen() {
		return headerLen;
	}

	/**

	* indica se  presente un extension header

	*/
	public boolean isExtensionHeader() {
		return extensionHeader;
	}

	/**
	* Ver.
	* indica la versione del protocollo
	*/
	public short getVersion() {
		return version;
	}

	/**
	* P
	* indica se e presente un byte di padding alla fine del pacchetto
	*/
	public short getPadding() {
		return padding;
	}

	/**
	* X
	* indica la presenza di un extentionHeader tra l header standard e il payload
	*/
	public short getExtentionX() {
		return extentionX;
	}

	/**
	* CC
	* contiene il numero di CSRC che seguono l header minimo
	*/
	public short getCc() {
		return cc;
	}

	/**
	* M
	* se settato il pacchetto ha una speciale rilevanza per il livello applicativo
	*/
	public short getM() {
		return m;
	}

	/**
	* PT
	* indica il formato del payload. determina la sua interpr dall applicazione 
	*/
	public short getPaylType() {
		return paylType;
	}

	/**
	* Sequence Number Low
	* viene incrementato di 1 per ogni pacchetto RTP inviato. il valore iniziale deve essere casuale
	*/
	public short getLowSequenceNum() {
		return lowSequenceNum;
	}

	/**
	* Sequence Number High
	* viene incrementato di 1 per ogni pacchetto RTP inviato. il valore iniziale deve essere casuale
	*/
	public short getHighSequenceNum() {
		return highSequenceNum;
	}

	/**
	* TimeStamp
	* permette al ricevente di riprodurre il media all intervallo appropiato
	*/
	public int getTimestamp() {
		return timestamp;
	}

	/**
	* SSRC
	* indetifica in modo univoco la fonte dello stream in una sessione RTP
	*/
	public int getSyncSrc() {
		return syncSrc;
	}

	/**
	* CSRC
	* enumerano le fonti di uno stream generato da piu fonti
	*/
	public List<Integer> getContribSrc() {
		return contribSrc;
	}

	/**
	* Extension ID
	*/
	public short getExtHeaderID() {
		return extHeaderID;
	}

	/**
	* Extension length
	*/
	public short getExtLength() {
		return extLength;
	}

	/**
	* Extension
	* enumerano le extension
	*/
	public List<Integer> getExtensions() {
		return extensions;
	}
	
	
	
	
	
		
	
	

	
	
	
		
	
			
	
	
	
		
	
	
	
	
		
	
	
}