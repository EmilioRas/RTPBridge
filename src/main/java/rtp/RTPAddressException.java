package rtp;

public class RTPAddressException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5539541452843990067L;

	public RTPAddressException(){
		super("net Address exception");
	}
	
	public RTPAddressException(String addrError){
		super(addrError);
	}
}
