package rtp.server;

import start.RTPServerLog;

import java.io.IOException;
import java.net.*;

public class RTPServerTransmitter implements Runnable{

    protected RTPDataServer dataServer;

    protected DatagramSocket dataSocket;


    public RTPServerTransmitter(int port) throws IOException {
        RTPServerLog.log("Transmitter datagram listen to local | port : "+ port);
        this.dataServer = new RTPDataServer();
        this.dest = new InetSocketAddress("127.0.0.1",port);
        this.dataSocket = new DatagramSocket(port);
        this.dataSocket.setReuseAddress(true);
    }

    public RTPServerTransmitter(int port, InetAddress laddr) throws IOException{

        RTPServerLog.log("Transmitter datagram listen to : " + laddr.getHostAddress() +" | port : "+ port);
        this.dataServer = new RTPDataServer();
        this.dest = new InetSocketAddress(laddr,port);
        this.dataSocket = new DatagramSocket(port,laddr);
        this.dataSocket.setReuseAddress(true);
    }

    protected InetSocketAddress dest;
    protected InetSocketAddress getDest() {
        return dest;
    }
    public void setDest(InetSocketAddress dest) {
        this.dest = dest;
    }

    protected DatagramPacket tmpPacket;

    public DatagramPacket getTmpPacket() {
        return tmpPacket;
    }

    public void setTmpPacket(DatagramPacket tmpPacket) {
        this.tmpPacket = tmpPacket;
    }

    @Override
    public void run() {
        try {
            RTPServerLog.log("Sender right...");


            synchronized (this.getDataServer()) {

                RTPServerLog.log("\t We have a new packet (rx)...");
                if (this.getTmpPacket() != null) {
                    RTPServerLog.log("\t\t New packets data... ");

                    RTPServerLog.log("\t\t Packet in data rtp rx server... start to send");

                    RTPServerLog.log("\t\t Packet in data rtp rx server... Port : " + this.getTmpPacket().getPort());
                    if (this.getTmpPacket().getAddress() != null && this.getTmpPacket().getPort() > 0) {
                        this.dataSocket.send(this.getTmpPacket());
                        RTPServerLog.log("\t\t END send");
                    }
                }
                this.getDataServer().notify();

            }
        } catch (Exception e){
            RTPServerLog.log(e.getMessage());
        } finally {
            RTPServerLog.log("Return to ...");
        }
    }

    public RTPDataServer getDataServer() {
        return this.dataServer;
    }
}
