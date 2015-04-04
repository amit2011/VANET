package network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

import truck.Comdata;
import truck.Global;

public class listening {
	public void listeningprocess() throws IOException{
		DatagramSocket UDPserverSocket = null;
		boolean listening = true;
		try {
			UDPserverSocket = new DatagramSocket(10133);
		}catch (IOException e) {
			System.out.println("Could not listen on the port" + e);
			e.printStackTrace();
//			System.exit(-1);
		}
		byte[] inData = new byte[256];
		
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
	
	public static void listeninghello(DatagramPacket inPacket){
		String srcadd = new String(inPacket.getData(), 1, 16).trim();
		Integer unnum=Integer.parseInt(new String(inPacket.getData(), 17, 2).trim());
		Integer binum=Integer.parseInt(new String(inPacket.getData(), 19, 2).trim());
		
		if(srcadd.equals(Global.selfipadd)==false){
//			System.out.println("listening"+"+selfipadd+"+Global.selfipadd);
//			System.out.println("listening"+"+srcadd+"+srcadd);
			String [] undirection=new String[Global.size];
			String [] bidirection=new String[Global.size];
			int position=21;
//			System.out.println("listening"+"+binum+"+binum);
			for(;binum>0;binum--){
/*				if(binum>9){
					System.out.println("---------------------------");
				}
*/
//				bidirection[binum-1] = new String(inPacket.getData(), position, 16).trim();
				String receive=new String(inPacket.getData(), position, 16).trim();
				if((receive.startsWith("131.204.14"))&&(receive.length()==14)){
					bidirection[binum-1] =receive;
				}
//				System.out.println("listening"+bidirection[binum-1]);
				position=position+16;
			}
			for(;unnum>0;unnum--){
/*				if(unnum>9){
					System.out.println("+++++++++++++++++++++++++++");
				}
*/
//				undirection[unnum-1] = new String(inPacket.getData(), position, 16).trim();
				String receive=new String(inPacket.getData(), position, 16).trim();
				if((receive.startsWith("131.204.14"))&&(receive.length()==14)){
					undirection[unnum-1] =receive;
				}
				position=position+16;
			}
			strategy stragy=new strategy();
			int neitableresult=stragy.createneitable(srcadd,bidirection,undirection);
		}
	}
	
	public static void listeningmsg(DatagramPacket inPacket){
		String seqnum = new String(inPacket.getData(), 1, 5).trim();
		String srcadd = new String(inPacket.getData(), 6, 16).trim();
		String prehop = new String(inPacket.getData(), 22, 16).trim();
		String xloc = new String(inPacket.getData(),38, 8).trim();
		String vol = new String(inPacket.getData(), 46, 8).trim();
		String acc = new String(inPacket.getData(), 54, 2).trim();
		String tstate = new String(inPacket.getData(), 56, 2).trim();
		String trank = new String(inPacket.getData(), 58, 4).trim();
		String delaytime= new String(inPacket.getData(), 62, 2).trim();
		String nprnum= new String(inPacket.getData(), 64, 2).trim();
		String mpr0 = new String(inPacket.getData(), 66, 16).trim();
		String mpr1 = new String(inPacket.getData(), 82, 16).trim();
		
		//check package to updata file
		check ck=new check();
		int ckresultline=ck.checkresultseq(seqnum, srcadd);
		int ckresultstate=0;
		if(tstate.equals("9")){
			ck.checkresultstate(srcadd,Global.selfipadd);
			ckresultstate=-1;
		}
		int ipformatresult=-1;
		if((srcadd.startsWith("131.204.14"))&&(srcadd.length()==14)){
			ipformatresult=0;
		}
		
		//update 
		if((ckresultline>-1)&&(ckresultstate>-1)&&(ipformatresult>-1)){
			//writefile
			Global.configurefile[ckresultline][0]=srcadd;
			Global.configurefile[ckresultline][1]=xloc;
			Global.configurefile[ckresultline][2]=vol;
			Global.configurefile[ckresultline][3]=acc;
			Global.configurefile[ckresultline][4]=seqnum;
			Global.configurefile[ckresultline][5]=tstate;
			Global.configurefile[ckresultline][6]=trank;
			
			//broadcast
			if(Global.selfipadd.equals(mpr0)||Global.selfipadd.equals(mpr1)){
/*				
				System.out.println("listening"+"+srcadd+"+srcadd);
				System.out.println("listening"+"+MPRselect[0]+"+mpr0);
				System.out.println("listening"+"+MPRselect[1]+"+mpr1);
*/				
				Comdata xva=new Comdata();
				try{
					xva.x=Double.parseDouble(xloc);
					xva.v=Double.parseDouble(vol);
					xva.a=Long.parseLong(acc);
					xva.truckstate=Long.parseLong(tstate);
					xva.truckrank=Long.parseLong(trank);
					xva.delaytime=delaytime;
//				xva.x=Long.valueOf(xloc).longValue();
//				xva.x=Long.valueOf(vol).longValue();
//				xva.x=Long.valueOf(acc).longValue();
				}catch (NullPointerException e){
					System.out.println("parseDouble is null" + e);
					e.printStackTrace();
				}
				broadcast brocast=new broadcast(seqnum, srcadd, Global.selfipadd);
				try {
					brocast.broadcastmsg(xva);
				} catch (IOException e) {
					System.out.println("broadcast error" + e);
					e.printStackTrace();
				}
			}
			//delaydetect
			int selfline=ck.checkrank(Global.selfipadd);
			if(selfline<0||Global.configurefile[selfline][6]==null){
			
			}
			else if(Global.configurefile[selfline][6].equals("1")){
				if(delaytime.equals("2")||delaytime.equals("3")||delaytime.equals("4")||delaytime.equals("5")){
					long nowtime=System.currentTimeMillis();
					Global.truckdelaytime[Integer.parseInt(delaytime)-1]=nowtime-Global.truckdelaytime[0];
//					System.out.println("truck " + delaytime +" delaytime: " + (Global.truckdelaytime[Integer.parseInt(delaytime)-1])/2 +" ms");
				}
			}
			else{
				if(delaytime.equals("1")){
					Global.truckdelayflag=Global.configurefile[selfline][6];
				}
			}
		}
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
		String pktflag = new String(inPacket.getData(), 0, 1).trim();
		if(pktflag.equals("0")){
			listening.listeninghello(inPacket);
		}
		else if(pktflag.equals("*")){
			listening.listeningmsg(inPacket);
		}
	}
}
