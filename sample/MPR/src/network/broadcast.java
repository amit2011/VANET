package network;

import java.io.*;
import java.net.*;

import truck.*;

public class broadcast {
	private String seqnum;
	private String srcadd;
	private String prehop;
	
	public broadcast(String srcadd){
		this.srcadd=srcadd;
	}
	
	public broadcast(String seqnum, String srcadd, String prehop){
		this.seqnum=seqnum;
		this.srcadd=srcadd;
		this.prehop=prehop;
	}
	
	public void broadcastmsg(Comdata xva) throws IOException{
		
		byte[] outdata = new byte[256];
		byte[] pktflagbyte=new byte[1];
		byte[] seqnumbyte=new byte[5];
		byte[] srcaddbyte=new byte[16];
		byte[] prehopbyte=new byte[16];
		byte[] xlocbyte = new byte[8];
		byte[] volbyte = new byte[8];
		byte[] accbyte = new byte[2];
		byte[] tstatebyte = new byte[2];
		byte[] trankbyte = new byte[4];
		byte[] delaytimebyte = new byte[2];
		byte[] mprnumbyte = new byte[2];
		byte[] mpr0byte = new byte[16];
		byte[] mpr1byte = new byte[16];
		
		String pktflag="*";
		pktflagbyte=pktflag.getBytes();
		System.arraycopy(pktflagbyte,0,outdata,0,pktflagbyte.length);
		seqnumbyte = seqnum.getBytes();
		System.arraycopy(seqnumbyte,0,outdata,1,seqnumbyte.length);
		srcaddbyte = srcadd.getBytes();
		System.arraycopy(srcaddbyte,0,outdata,6,srcaddbyte.length);
		prehopbyte = prehop.getBytes();
		System.arraycopy(prehopbyte,0,outdata,22,prehopbyte.length);
		String xloc=String.format("%.2f", xva.x);
		xlocbyte = xloc.getBytes();
		System.arraycopy(xlocbyte,0,outdata,38,xlocbyte.length);
		String vol=String.format("%.2f", xva.v);
		volbyte = vol.getBytes();
		System.arraycopy(volbyte,0,outdata,46,volbyte.length);
		String acc=Long.toString(xva.a);
		accbyte = acc.getBytes();
		System.arraycopy(accbyte,0,outdata,54,accbyte.length);
		String tstate=Long.toString(xva.truckstate);
		tstatebyte = tstate.getBytes();
		System.arraycopy(tstatebyte,0,outdata,56,tstatebyte.length);
		String trank=Long.toString(xva.truckrank);
		trankbyte = trank.getBytes();
		System.arraycopy(trankbyte,0,outdata,58,trankbyte.length);
		if(xva.delaytime!=null){
			delaytimebyte = xva.delaytime.getBytes();
			System.arraycopy(delaytimebyte,0,outdata,62,delaytimebyte.length);
		}
		String nprnum="0";
		if(Global.MPRselect[0]!=null && (Global.MPRselect[0].equals(srcadd)==false)){
			mpr0byte=Global.MPRselect[0].getBytes();
			System.arraycopy(mpr0byte,0,outdata,66,mpr0byte.length);
			nprnum="1";
		}
		if(Global.MPRselect[1]!=null && (Global.MPRselect[1].equals(srcadd)==false)){
			if(Global.MPRselect[0]==null){
				mpr0byte=Global.MPRselect[0].getBytes();
				System.arraycopy(mpr0byte,0,outdata,82,mpr0byte.length);
				nprnum="2";
			}
			else{
				mpr1byte=Global.MPRselect[1].getBytes();
				System.arraycopy(mpr1byte,0,outdata,82,mpr1byte.length);
				nprnum="2";
			}
		}
/*
			System.out.println("broadcast"+"+srcadd+"+srcadd);
			System.out.println("broadcast"+"+MPRselect[0]+"+Global.MPRselect[0]);
			System.out.println("broadcast"+"+MPRselect[1]+"+Global.MPRselect[1]);
*/		
		mprnumbyte=nprnum.getBytes();
		System.arraycopy(mprnumbyte,0,outdata,64,mprnumbyte.length);
		
		broadcastprocess(outdata);
	}
	
	public void broadcasthello(Integer binum, Integer unnum, String[] bidirection, String[] undirection) throws IOException{
		
		byte[] outdata = new byte[256];
		byte[] pktflagbyte=new byte[1];
		byte[] numbyte=new byte[2];
		byte[] addbyte = new byte[16];
		
		String pktflag="0";
		pktflagbyte=pktflag.getBytes();
		System.arraycopy(pktflagbyte,0,outdata,0,pktflagbyte.length);
//		addbyte=srcadd.getBytes();
		addbyte=Global.selfipadd.getBytes();
		System.arraycopy(addbyte,0,outdata,1,addbyte.length);
		numbyte=Integer.toString(unnum).getBytes();
		System.arraycopy(numbyte,0,outdata,17,numbyte.length);
//		System.out.println("broadcast"+"+unnum+"+unnum);
		numbyte=Integer.toString(binum).getBytes();
		System.arraycopy(numbyte,0,outdata,19,numbyte.length);
//		System.out.println("broadcast"+"+binum+"+binum);
		int position=21;
		for(;binum>0;binum--){
			addbyte = new byte[16];
			addbyte=bidirection[binum-1].getBytes();
			System.arraycopy(addbyte,0,outdata,position,addbyte.length);
//			System.out.println("broadcast"+bidirection[binum-1]);
			position=position+16;
		}	
		for(;unnum>0;unnum--){
			addbyte = new byte[16];
			addbyte=undirection[unnum-1].getBytes();
			System.arraycopy(addbyte,0,outdata,position,addbyte.length);
			position=position+16;
		}
		
		broadcastprocess(outdata);
	}
	
	private static void broadcastprocess(byte[] outdata) throws IOException{
		int ii=0;
		for(ii=0;Global.wirelessconnecttable[ii]!=null;ii++){
//		System.out.println("broadcast"+Global.wirelessconnecttable[ii]);
			DatagramSocket UDPSocket = new DatagramSocket();
			InetAddress broadcastIPAddress = InetAddress.getByName(Global.wirelessconnecttable[ii]);
			
			DatagramPacket outPacket = new DatagramPacket(outdata, outdata.length, broadcastIPAddress, 10133);
			try{
				UDPSocket.send(outPacket);
			}catch (IOException e) {
				System.out.println("send error" + e);
			}
			UDPSocket.close();
		}
		
		throughput();
	}
	
	private static void throughput(){
		Global.throughputnum++;
		if(Global.throughputnum==1){
			Global.inittime=System.currentTimeMillis();
		}
		long nowtime=System.currentTimeMillis();
		if(nowtime>(Global.inittime+100000)){
			Global.throughput=Global.throughputnum/100;
			System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"+"throughput: " + Global.throughput*104+" bps");
			Global.throughputnum=0;
		}
	}
}
