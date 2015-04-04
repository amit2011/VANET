package network;

import java.io.IOException;
import java.net.*;

import truck.comdata;
import truck.Global;

public class listening {
	public void listeningmsg() throws IOException{
		DatagramSocket UDPserverSocket = null;
		boolean listening = true;
		try {
			UDPserverSocket = new DatagramSocket(2000);
		}catch (IOException e) {
			System.out.println("Could not listen on the port" + e);
			e.printStackTrace();
//			System.exit(-1);
		}
		byte[] inData = new byte[1024];
		
		while (listening) {
			DatagramPacket inPacket = new DatagramPacket(inData, inData.length);
			try{
				UDPserverSocket.receive(inPacket);
			}catch(IOException e){
				System.out.println("receive error" + e);
			}
			new UDPMultipleServerThread(inPacket, UDPserverSocket).start();
		}
		UDPserverSocket.close();
	}
}

class UDPMultipleServerThread extends Thread {
	private DatagramPacket inPacket=null;
	private DatagramSocket UDPserverSocket=null;
	public UDPMultipleServerThread(DatagramPacket inPacket, DatagramSocket UDPserverSocket) {
		this.inPacket = inPacket;
		this.UDPserverSocket = UDPserverSocket;
	}
	public void run(){
		String seqnum = new String(inPacket.getData(), 0, 5).trim();
		String srcadd = new String(inPacket.getData(), 5, 32).trim();
		String prehop = new String(inPacket.getData(), 37, 32).trim();
		String xloc = new String(inPacket.getData(),69, 10).trim();
		String vol = new String(inPacket.getData(), 79, 5).trim();
		String acc = new String(inPacket.getData(), 84, 2).trim();
		String tstate = new String(inPacket.getData(), 86, 2).trim();
		String trank = new String(inPacket.getData(), 88, 2).trim();
		//check package to updata file
		check ck=new check();
		int ckresultline=ck.chechresult(seqnum, srcadd);
//System.out.println("TAG:listening received");
//System.out.println(srcadd+xloc+seqnum);
		if(ckresultline>-1){
//System.out.println("TAG:file update, new not self-to-self");
			//writefile
			Global.configurefile[ckresultline][0]=srcadd;
			Global.configurefile[ckresultline][1]=xloc;
			Global.configurefile[ckresultline][2]=vol;
			Global.configurefile[ckresultline][3]=acc;
			Global.configurefile[ckresultline][4]=seqnum;
			Global.configurefile[ckresultline][5]=tstate;
			Global.configurefile[ckresultline][6]=trank;
			//broadcast
			InetAddress clientIPAddress = inPacket.getAddress();
			String clientIP=clientIPAddress.getHostAddress();
			broadcast brocast=new broadcast(seqnum, srcadd, clientIP);
			comdata xva=new comdata();
			try{
				xva.x=Double.parseDouble(xloc);
				xva.v=Double.parseDouble(vol);
				xva.a=Long.parseLong(acc);
				xva.truckstate=Long.parseLong(tstate);
				xva.truckrank=Long.parseLong(trank);
//				xva.x=Long.valueOf(xloc).longValue();
//				xva.x=Long.valueOf(vol).longValue();
//				xva.x=Long.valueOf(acc).longValue();
			}catch (NullPointerException e){
				System.out.println("parseDouble is null" + e);
				e.printStackTrace();
			}
			try {
				brocast.broadcastmsg(xva);
			} catch (IOException e) {
				System.out.println("broadcast error" + e);
				e.printStackTrace();
			}
		}
	}
}
