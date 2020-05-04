package start;

import java.io.File;
import java.io.FileOutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;

import rtp.client.RTPClientSend;

import rtp.server.RTPServer;
import rtp.server.RTPServerReceiver;

public class RTP{
	
	public static int receivedNum = 0;

	public static void main(String[] args){
		if (args.length >= 6){


			try {
				File logFile = new File(args[4]+File.separatorChar+"RTPLog.log");
				RTPServerLog.setLogFile(logFile);
				RTPServerLog.setInlogFile(new FileOutputStream(logFile));
				RTPServerLog.log(args[0]);
				RTPServerLog.log(args[1]);
				RTPServerLog.log(args[2]);
				RTPServerLog.log(args[3]);
				RTPServerLog.log(args[4]);
				RTPServerLog.log(args[5]);
				RTPServerReceiver receiver = null;
				RTPClientSend sender = null;		
				//receiver
					int myRecSize = 1;
					try {
						myRecSize = Integer.parseInt(args[5]);
					} catch (Exception e) {
						RTPServerLog.log("Not set max Life time!!!");
					}

					if (args[0] != null && args[0].equals("local")) {
						receiver = new RTPServerReceiver(Integer.parseInt(args[2]));
					} else {
						receiver = new RTPServerReceiver(Integer.parseInt(args[2]),
								RTPServerReceiver.getInet(args[0], false));
					}

					if (Integer.parseInt(args[3]) > -1) {

						if (args[1] != null && args[1].equals("local")) {
							sender = new RTPClientSend(Integer.parseInt(args[3]));
						} else {
							sender = new RTPClientSend(Integer.parseInt(args[3]),
									RTPClientSend.getInet(args[1], false));
						}

					}


					//loop bridge
					RTPServerLog.log("Start RTP Receiver ...");
					InetAddress dataAddress = null;
					int dataPort = -1;



				while (myRecSize < 0 || RTP.receivedNum < myRecSize) {

						RTPServerLog.log("Receiver send buffer size :" + receiver.getSendBufferSize());
						RTPServerLog.log("Receiver receive buffer size :" + receiver.getReceiveBufferSize());
						RTPServerLog.log("Receiver so timeout :" + receiver.getSoTimeout());
						RTPServerLog.log("Receiver traffic class :" + receiver.getTrafficClass());
						RTPServerLog.log("Receiver broadcast :" + receiver.getBroadcast());

						
						Thread tRec = new Thread((Runnable)receiver);
						synchronized (receiver.getDataServer()) {
							tRec.start();
							receiver.getDataServer().wait();
						}
						

						RTP.receivedNum++;

						
						RTPServerLog.log("... waiting ...");

					//end receiver

					//sender

					if (Integer.parseInt(args[3]) > -1) {
						Thread tPlay = new Thread(sender);
						RTPServerLog.log("Start RTP Sender ...");
						synchronized (sender.getDataClient()) {

							sender.setAddress(dataAddress);
							sender.setPort(dataPort);

							sender.getDataClient().setPacket(receiver.getDataServer().getPacket());

							tPlay.start();
							sender.getDataClient().wait();


						}
					}
				//end sender
					}
					//end loop bridge
				

			} catch (Exception e){
				RTPServerLog.log(e.getMessage());
				System.exit(1);
			}
		} else {
			System.out.println("Not arguments. RTP Exit!!!");
			System.out.println("1. IP for (receiver) . <local> to local");
			System.out.println("2. IP for (sender) . <local> to local");
			System.out.println("3. PORT for (receiver)");
			System.out.println("4. PORT for (sender)");
			System.out.println("5. LOG folder");
			System.out.println("6. Times to life. It is the number of datapackage that this run can be able to route (-1 run without times to life)");
			System.exit(0);
		}
	}
}