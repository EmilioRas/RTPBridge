package start;

import java.io.File;
import java.io.FileOutputStream;

import rtp.client.RTPClientSend;
import rtp.server.RTPServerReceive;

public class RTP{
	
	public static int receivedNum = 0;

	public static void main(String[] args){
		if (args.length >= 4){
			try {
				File logFile = new File(args[3]+File.separatorChar+"RTPLog.log");
				RTPServerLog.setLogFile(logFile);
				RTPServerLog.setInlogFile(new FileOutputStream(logFile));
				RTPServerReceive receiver = null;
				RTPClientSend sender = null;		
				if (args[0].equals("r")){
					int myRecSize = 1;
					try {
						myRecSize = Integer.parseInt(args[4]);
					} catch (Exception e) {
						RTPServerLog.log("Not set max Life time!!!");
					}
					
					RTPServerLog.log("Start RTP Receiver ...");
					while (RTP.receivedNum < myRecSize) {
						if (args[1] != null && args[1].equals("local")) {
							receiver = new RTPServerReceive(Integer.parseInt(args[2]));
						} else {
							receiver = new RTPServerReceive(Integer.parseInt(args[2]), 
									RTPServerReceive.getInet(args[1], false));
						}
						if (RTP.receivedNum == 0) {
							RTPServerLog.log("Receiver send buffer size :" + receiver.getSendBufferSize());
							RTPServerLog.log("Receiver receive buffer size :" + receiver.getReceiveBufferSize());
							RTPServerLog.log("Receiver so timeout :" + receiver.getSoTimeout());
							RTPServerLog.log("Receiver traffic class :" + receiver.getTrafficClass());
							RTPServerLog.log("Receiver broadcast :" + receiver.getBroadcast());
						}
						
						Thread tRec = new Thread((Runnable)receiver);
						synchronized (receiver.getDataServer()) {
							tRec.start();
							receiver.getDataServer().wait();
						}
						
						if (myRecSize > 1) {
							RTP.receivedNum++;
						}
						
						RTPServerLog.log("... waiting ...");
					}
				} else if (args[0].equals("s")){	
					if (args[1] != null && args[1].equals("local")) {
						sender = new RTPClientSend(Integer.parseInt(args[2]));
					} else {
						sender = new RTPClientSend(Integer.parseInt(args[2]), 
								RTPClientSend.getInet(args[1], false));
					}
					Thread tPlay = new Thread(sender);	
					RTPServerLog.log("Start RTP Sender ...");
					synchronized (sender.getDataClient()) {
						tPlay.start();
						sender.getDataClient().wait();
					}
				}
				
				

			} catch (Exception e){
				RTPServerLog.log(e.getMessage());
				System.exit(0);
			}
		} else {
			System.out.println("Not arguments. RTP Exit!!!");
			System.out.println("1. s/r (sender/receiver)");
			System.out.println("2. IP for (sender/receiver) . <local> to local");
			System.out.println("3. PORT for (sender/receiver)");
			System.out.println("4. LOG folder");
			System.out.println("5. Life time. in sec");
			System.exit(0);
		}
	}
}