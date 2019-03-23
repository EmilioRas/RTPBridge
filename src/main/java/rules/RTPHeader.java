package rules;

import java.util.List;

public interface RTPHeader {
	static final int headerminlength = 12;
	static final int sequenceNumberlength =2;
	static final int timestamplength = 4;
	static final int ssrclength = 4;
	static final int csrcObjlength = 4;
	static final int extensionHeaderlength = 4;
	
	public byte[] getHeader();
	public void setData(byte[] data);
	public byte[] getData();
	public void setHeaderLen(int headerLen);
	public int getHeaderLen();
	public boolean isExtensionHeader();
	public short getVersion();
	public short getPadding();
	public short getExtentionX();
	public short getCc();
	public short getM();
	public short getPaylType();
	public short getLowSequenceNum();
	public short getHighSequenceNum();
	public int getTimestamp();
	public int getSyncSrc();
	public List<Integer> getContribSrc();
	public List<Integer> getExtensions();
	public short getExtLength();
	public short getExtHeaderID();
}
