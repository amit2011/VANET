package truck;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import network.broadcast;
import network.check;
import network.listening;
import simulation.simulation;
import truck.comdata;
import control.control;

public class Forward {
	public static void main(String[] args){
		thread1 th1=new thread1("newth1");
		Thread Th1=new Thread(th1);
		Th1.start();
		
		listening listen=new listening();
		try {
			listen.listeningmsg();
		} catch (IOException e) {
			System.out.println("start listening error" + e);
			e.printStackTrace();
		}
	}
}

class thread1 implements Runnable{
	private String name;
	public thread1(){
	}
	
	public thread1(String name){
		this.name = name;
	}
	
	public void run(){
		while(true){
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				System.out.println("thread1 sleep error" + e);
				e.printStackTrace();
			}
			thread2 th2=new thread2("newth2");
			Thread Th2=new Thread(th2);
			Th2.start();
			}
		}
}

class thread2 implements Runnable{
	private String name;
	public thread2(){
	}
	public thread2(String name){
		this.name = name;
	}
	public void run(){
//		long time=System.currentTimeMillis();
//		System.out.println(time);
		control getacc=new control();
		long x=1,v=1;
		long a=getacc.controlmethod(x,v);
		
		simulation sim=new simulation();
		comdata xva=sim.simmove(x, v, a);

		InetAddress selfhost = null;
		String selfipadd=null;
		try {
			selfhost = InetAddress.getLocalHost();
			selfipadd=selfhost.getHostAddress();
		} catch (UnknownHostException e) {
			System.out.println("getselfIP error" + e);
			e.printStackTrace();
		}
		//checkrank with selfipadd
		check ck=new check();
		int rank=ck.checkrank(selfipadd);
		if(rank>-1 && (Global.configurefile[rank][4]==null || Global.configurefile[rank][4]=="0")){
			Global.configurefile[rank][4]="9999";
		}//extra (Global.configurefile[rank][0] == null) for initialization (=="0") for loop 0-9999
		else{
			Global.configurefile[rank][4]=Long.toString(Long.parseLong(Global.configurefile[rank][4])-1);
		}
			//writefile
			Global.configurefile[rank][0]=selfipadd;
			Global.configurefile[rank][1]=Long.toString(xva.x);
			Global.configurefile[rank][2]=Long.toString(xva.v);
			Global.configurefile[rank][3]=Long.toString(xva.a);
			//broadcast
			String seqnum=Global.configurefile[rank][4];
			broadcast brocast=new broadcast(seqnum, selfipadd, selfipadd);
			try {
				brocast.broadcastmsg(xva);
			} catch (IOException e) {
				System.out.println("broadcast error" + e);
				e.printStackTrace();
			}
			//print display
System.out.println("TAG:thread2 begin, self broadcast");
System.out.println(selfipadd+xva.x+seqnum);
	}
}


