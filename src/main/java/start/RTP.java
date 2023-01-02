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
				RTPServerLog.setInlogFile(new FileOutputStream(logFile));
				RTPServerLog.log(args[0]);
				RTPServerLog.log(args[1]);
				RTPServerLog.log(args[2]);
				RTPServerLog.log(args[3]);
				RTPServerLog.log(args[4]);


				RTPServerReceiver receiver = null;
				RTPServerTransmitter sender = null;
				//receiver




				if (args[0] != null && args[0].equals("local")) {
					receiver = new RTPServerReceiver(Integer.parseInt(args[2]));
				} else {
					receiver = new RTPServerReceiver(Integer.parseInt(args[2]),
							RTPServerReceiver.getInet(args[0], false));

				}

				if (args[1].equals("none"))
					receiver.setDest(new InetSocketAddress(RTPServer.getInet(args[0], false), Integer.parseInt(args[3])));
				else
					receiver.setDest(new InetSocketAddress(RTPServer.getInet(args[1], false), Integer.parseInt(args[3])));
					//loop bridge
					RTPServerLog.log("Start RTP Receiver ...");
					InetAddress dataAddress = null;
					int dataPort = -1;


				RTPServerLog.log("Receiver send buffer size :" + receiver.getSendBufferSize());
				RTPServerLog.log("Receiver receive buffer size :" + receiver.getReceiveBufferSize());
				RTPServerLog.log("Receiver so timeout :" + receiver.getSoTimeout());
				RTPServerLog.log("Receiver traffic class :" + receiver.getTrafficClass());
				RTPServerLog.log("Receiver broadcast :" + receiver.getBroadcast());

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

			System.exit(0);
		}
	}
}