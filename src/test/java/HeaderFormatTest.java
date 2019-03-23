

import org.junit.Assert;
import org.junit.Test;

public class HeaderFormatTest {

	private byte[] header;
	
	private int headerLen = 12;
	
	public byte[] getHeader(){
		return this.header;
	}
	
	public HeaderFormatTest(){
		this.header = new byte[this.headerLen];
	}
	
	

	@Test
	public void shortToByte(){
		HeaderFormatTest f = new HeaderFormatTest();
		f.header[0] = (byte) 255;
		f.header[0] = (byte)((f.header[0] & 0x00FF) >> 1);
		Assert.assertArrayEquals(new byte[]{127,0,0,0,0,0,0,0,0,0,0,0},f.header);
		System.out.println(f.header[0]);
	}
}
