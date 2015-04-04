package network;

import java.io.*;
import java.net.*;

import truck.comdata;

public class broadcast {
	private String seqnum;
	private String srcadd;
	private String prehop;
	
	public broadcast(String seqnum, String srcadd, String prehop){
		this.seqnum=seqnum;
		this.srcadd=srcadd;
		this.prehop=prehop;
	}
	
	public void broadcastmsg(comdata xva) throws IOException{
		DatagramSocket UDPSocket = new DatagramSocket();
		InetAddress broadcastIPAddress = InetAddress.getByName("192.168.1.255");
		
		byte[] outdata = new byte[1024];
		byte[] seqnumbyte=new byte[5];
		byte[] srcaddbyte=new byte[32];
		byte[] prehopbyte=new byte[32];
		byte[] xlocbyte = new byte[10];
		byte[] volbyte = new byte[10];
		byte[] accbyte = new byte[5];
		byte[] tstatebyte = new byte[5];
		byte[] trankbyte = new byte[5];
		
		seqnumbyte = seqnum.getBytes();
		System.arraycopy(seqnumbyte,0,outdata,0,seqnumbyte.length);
		srcaddbyte = srcadd.getBytes();
		System.arraycopy(srcaddbyte,0,outdata,5,srcaddbyte.length);
		prehopbyte = prehop.getBytes();
		System.arraycopy(prehopbyte,0,outdata,37,prehopbyte.length);
		String xloc=String.format("%.2f", xva.x);
		xlocbyte = xloc.getBytes();
		System.arraycopy(xlocbyte,0,outdata,69,xlocbyte.length);
		String vol=String.format("%.2f", xva.v);
		volbyte = vol.getBytes();
		System.arraycopy(volbyte,0,outdata,79,volbyte.length);
		String acc=Long.toString(xva.a);
		accbyte = acc.getBytes();
		System.arraycopy(accbyte,0,outdata,89,accbyte.length);
		String tstate=Long.toString(xva.truckstate);
		tstatebyte = tstate.getBytes();
		System.arraycopy(tstatebyte,0,outdata,94,tstatebyte.length);
		String trank=Long.toString(xva.truckrank);
		trankbyte = trank.getBytes();
		System.arraycopy(trankbyte,0,outdata,99,trankbyte.length);

		DatagramPacket outPacket = new DatagramPacket(outdata, outdata.length, broadcastIPAddress, 10130);
		try{
			UDPSocket.send(outPacket);
		}catch (IOException e) {
			System.out.println("send error" + e);
		}
		UDPSocket.close(); 
	}
}
