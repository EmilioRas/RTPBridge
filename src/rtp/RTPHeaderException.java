package rtp;

public class RTPHeaderException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8049089343408061859L;

	public RTPHeaderException(Exception e){
		super(e);
	}
	
	public RTPHeaderException(){
		super("You should be describe header rpt error!");
	}
	
	public RTPHeaderException(String errormsg){
		super(errormsg);
	}
}
