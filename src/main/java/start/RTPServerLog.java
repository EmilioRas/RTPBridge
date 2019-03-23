package start;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.GregorianCalendar;


public class RTPServerLog {

	private static Calendar cal = GregorianCalendar.getInstance();
	
	private static File logFile;

	private static OutputStream inlogFile;
	
	public static void logFileClose() throws IOException{
		RTPServerLog.inlogFile.close();
	}
	
	public static OutputStream getInlogFile() {
		return RTPServerLog.inlogFile;
	}



	public static void setInlogFile(OutputStream inlogFile) {
		RTPServerLog.inlogFile = inlogFile;
	}



	public static File getLogFile() {
		return RTPServerLog.logFile;
	}



	public static void setLogFile(File logFile) {
		RTPServerLog.logFile = logFile;
	}


	public static void log(String log){
		System.out.println(log);
		log = RTPServerLog.cal.getTime().toString()+ " - "+log+"\n";
		try {
			RTPServerLog.inlogFile.write(log.getBytes());
			RTPServerLog.inlogFile.flush();
			
		} catch (IOException io){
			System.err.println("unable to write logs");
		}
	}
	
	public static void logNoN(String log){
		try {
			RTPServerLog.inlogFile.write(log.getBytes());
			RTPServerLog.inlogFile.flush();
			System.out.print(log);
		} catch (IOException io){
			System.err.println("unable to write logs");
		}
	}

}
