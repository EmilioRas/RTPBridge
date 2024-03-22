package start;

import java.io.File;
import java.io.FileOutputStream;

import java.net.InetAddress;
import java.net.InetSocketAddress;

import rtp.server.RTPServer;
import rtp.server.RTPServerReceiver;
import rtp.server.RTPServerTransmitter;


public class RTP{
	
	public static int receivedNum = 0;

	public static void main(String[] args){

		if (args.length >= 5){


			try {
				File logFile = new File(args[4] + File.separatorChar + "RTPLog.log");
				RTPServerLog.setLogFile(logFile);
				if (args.length >= 6)
					RTPServerLog.setEnable(Boolean.parseBoolean(args[5]));
				RTPServerLog.setInlogFile(new FileOutputStream(logFile));
				RTPServerLog.logAnyway(args[0]);
				RTPServerLog.logAnyway(args[1]);
				RTPServerLog.logAnyway(args[2]);
				RTPServerLog.logAnyway(args[3]);
				RTPServerLog.logAnyway(args[4]);
				if (args.length >= 6)
					RTPServerLog.logAnyway(args[5]);

				RTPServerReceiver receiver = null;
				RTPServerTransmitter sender = null;
				//receiver


				if (args[0] != null && args[0].equals("local")) {
					receiver = new RTPServerReceiver(Integer.parseInt(args[2]));
				} else {
					receiver = new RTPServerReceiver(Integer.parseInt(args[2]),
							RTPServerReceiver.getInet(args[0], false));

				}

				receiver.setByClient(false);
				if (args[1].equals("none") && !args[3].equals("none")) {
					receiver.setByClient(true);
					receiver.setDest(new InetSocketAddress(RTPServer.getInet(args[0], false), Integer.parseInt(args[3])));
				} else if (!args[1].equals("none")  && !(args[3].equals("none")))
					receiver.setDest(new InetSocketAddress(RTPServer.getInet(args[1], false), Integer.parseInt(args[3])));
				else {
					RTPServerLog.logAnyway("Cannot set Destination packet ...");
					System.exit(0);
				}
				//loop bridge
					RTPServerLog.logAnyway("Start RTP Receiver ...");
					InetAddress dataAddress = null;
					int dataPort = -1;


				RTPServerLog.logAnyway("Receiver send buffer size :" + receiver.getSendBufferSize());
				RTPServerLog.logAnyway("Receiver receive buffer size :" + receiver.getReceiveBufferSize());
				RTPServerLog.logAnyway("Receiver so timeout :" + receiver.getSoTimeout());
				RTPServerLog.logAnyway("Receiver traffic class :" + receiver.getTrafficClass());
				RTPServerLog.logAnyway("Receiver broadcast :" + receiver.getBroadcast());

				while (RTP.receivedNum >= 0) {


					Thread tRec = new Thread((Runnable) receiver);
					synchronized (receiver.getDataServer()) {
						tRec.start();
						receiver.getDataServer().wait();
						//sender.setTmpPacket(receiver.getTmpPacket());
					}


					RTP.receivedNum++;


					RTPServerLog.log("... waiting ...");

					//end receiver


				}
				//end sender

					//end loop bridge
				
					System.out.println("Num. Loops :" + RTP.receivedNum);
			} catch (Exception e){
				RTPServerLog.log(e.getMessage());
				System.exit(1);
			}
		} else {
			System.out.println("Not arguments. RTP Exit!!!");
			System.out.println("1. IP for (receiver/sender 1) . LX");
			System.out.println("2. IP for (multicast 2) . RX");
			System.out.println("3. PORT for (1)");
			System.out.println("4. PORT for (2)");
			System.out.println("5. LOG folder");
			System.out.println("6. LOG Enable (false/true)");
			System.exit(0);
		}
	}
}