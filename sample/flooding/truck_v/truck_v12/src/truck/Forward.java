package truck;
import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;

import network.broadcast;
import network.check;
import network.listening;
import simulation.simulation;
import truck.comdata;
import control.control;

public class Forward {
	static final long testperiodflag=100;	//msecond
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
	
	public static void initializtion(){
		System.out.println("begin or join");
		BufferedReader indata1 = new BufferedReader(new InputStreamReader(System.in));
		String input1=null;
		try {
			input1=indata1.readLine();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if(input1.equals("begin")){
			System.out.println("please enter total number of initial truck");
			BufferedReader indata2 = new BufferedReader(new InputStreamReader(System.in));
			String input2=null;
			try {
				input2=indata2.readLine();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			int totalnumber=Integer.parseInt(input2);
			System.out.println("please enter rank for this truck");
			BufferedReader indata3 = new BufferedReader(new InputStreamReader(System.in));
			String input3=null;
			try {
				input3=indata3.readLine();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			int rank=Integer.parseInt(input3);
			comdata xva=new comdata();
			xva.x=((5-rank)*82+Math.random()*23);
			xva.v=(int) (25+Math.random()*10);
			InetAddress selfhost = null;
			String selfipadd=null;
			try {
				selfhost = InetAddress.getLocalHost();
				selfipadd=selfhost.getHostAddress();
			} catch (UnknownHostException e) {
				System.out.println("getselfIP error" + e);
				e.printStackTrace();
			}
			Global.configurefile[0][0]=selfipadd;
			Global.configurefile[0][1]=String.format("%.2f", xva.x);
			Global.configurefile[0][2]=String.format("%.2f", xva.v);
			Global.configurefile[0][3]="0";
			Global.configurefile[0][4]="9999";
			Global.configurefile[0][5]="0";
			Global.configurefile[0][6]=Integer.toString(rank);
			Global.configurefile[0][7]="-1";
			
			xva.a=0;
			xva.truckstate=0;
			xva.truckrank=rank;
			broadcast brocast=new broadcast("9999", selfipadd, selfipadd);
			boolean breakflag=true;
			while(breakflag){
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					System.out.println("thread1 sleep error" + e);
					e.printStackTrace();
				}
				try {
					brocast.broadcastmsg(xva);
				} catch (IOException e) {
					System.out.println("broadcast error" + e);
					e.printStackTrace();
				}
				int i=0;
				for(;i<totalnumber;i++){
					if(Global.configurefile[i][0]==null)
						break;
				}
				if(i==(totalnumber)){
					breakflag=false;
				}
			}
		}
		else if(input1.equals("join")){
			System.out.println("please enter location for this truck");
			BufferedReader indata2 = new BufferedReader(new InputStreamReader(System.in));
			String input2=null;
			try {
				input2=indata2.readLine();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			double v=(int) (25+Math.random()*10);
			InetAddress selfhost = null;
			String selfipadd=null;
			try {
				selfhost = InetAddress.getLocalHost();
				selfipadd=selfhost.getHostAddress();
			} catch (UnknownHostException e) {
				System.out.println("getselfIP error" + e);
				e.printStackTrace();
			}
			Global.configurefile[0][0]=selfipadd;
//			Global.configurefile[0][1]=String.format("%.2f", input2);
			Global.configurefile[0][1]=input2;
			Global.configurefile[0][2]=String.format("%.2f", v);
			Global.configurefile[0][3]="0";
			Global.configurefile[0][4]="9999";
			Global.configurefile[0][5]="4";
			Global.configurefile[0][6]="0";
			Global.configurefile[0][7]="-1";
		}
		else{
			System.out.println("input unmatched");
			initializtion();
		}
		
		thread3 th3=new thread3("newth3");
		Thread Th3=new Thread(th3);
		Th3.start();
	}
	
	public void run(){
		thread1.initializtion();
		while(true){
			try {
				Thread.sleep(Forward.testperiodflag);
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
		long a=getacc.controlmethod();

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
        double x;
		double v;
		int ii=0;
		for(ii=0;ii<9;ii++){
			if((Global.configurefile[ii][0] != null) && (Global.configurefile[ii][0]).equals(selfipadd))
            break;
		}//get the IP position
		x=Double.parseDouble(Global.configurefile[ii][1]);
		v=Double.parseDouble(Global.configurefile[ii][2]);
		long truckstate=Long.parseLong(Global.configurefile[ii][5]);
		long truckrank=Long.parseLong(Global.configurefile[ii][6]);
		simulation sim=new simulation();
		comdata xva=sim.simmove(x, v, a, truckstate, truckrank);
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
			Global.configurefile[rank][1]=String.format("%.2f", xva.x);
			Global.configurefile[rank][2]=String.format("%.2f", xva.v);
			Global.configurefile[rank][3]=Long.toString(xva.a);
			Global.configurefile[rank][5]=Long.toString(xva.truckstate);
			Global.configurefile[rank][6]=Long.toString(xva.truckrank);
			//broadcast
			String seqnum=Global.configurefile[rank][4];
			broadcast brocast=new broadcast(seqnum, selfipadd, selfipadd);
			try {
				brocast.broadcastmsg(xva);
			} catch (IOException e) {
				System.out.println("broadcast error" + e);
				e.printStackTrace();
			}
//System.out.println("TAG:thread2 begin, self broadcast");
//System.out.println(selfipadd+xva.x+seqnum);
			print.printsecond();
	}
}

class thread3 implements Runnable{
	private String name;
	public thread3(){
	}
	public thread3(String name){
		this.name = name;
	}
	public void run(){
		while(true){
			System.out.println("give commnand to this truck");
			BufferedReader indata = new BufferedReader(new InputStreamReader(System.in));
			String command=null;
			try {
				command=indata.readLine();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			monitor(command);
		}
	}
	private static void monitor(String command){
		if(command.equals("l")){
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
			Global.configurefile[rank][5]="5";
		}
		else
			System.out.println("wrong command, try again");
	}
}

class print{
	static void printsecond(){
		//print display
/*			for(int i=0; i<10 && Global.configurefile[i][0]!=null; i++){
				for(int j=0;j<8;j++){
					System.out.println(Global.configurefile[i][j]);
				}
			}
*/		
			
		Global.printflag++;
		if(Global.printflag==1000/Forward.testperiodflag)
		{
			Global.printflag=0;
			int[] rankmatrix=new int[10];
			for(int i=0;i<10;i++)
				rankmatrix[i]=9;
			
			for(int i=0;i<10 && Global.configurefile[i][0]!=null; i++){
				int rankflag=0;
				for(int j=0;j<10 && Global.configurefile[j][0]!=null; j++){
					if(Double.parseDouble(Global.configurefile[i][1]) < Double.parseDouble(Global.configurefile[j][1]))
						rankflag++;
				}
				rankmatrix[rankflag]=i;
			}
			
			for(int i=0; i<10 && Global.configurefile[rankmatrix[i]][0]!=null; i++){
				System.out.print("ip"+"="+Global.configurefile[rankmatrix[i]][0].substring(11,14)+"\t\t");
			}
			System.out.print("\n");
			for(int i=0; i<10 && Global.configurefile[rankmatrix[i]][0]!=null; i++){
				System.out.print("x"+"="+Global.configurefile[rankmatrix[i]][1]+"\t");
			}
			System.out.print("\n");
			for(int i=0; i<10 && Global.configurefile[rankmatrix[i]][0]!=null; i++){
				if(i==0)
					System.out.print("Dx"+"="+"0"+"\t\t");
				else{
					double Dx=Double.parseDouble(Global.configurefile[rankmatrix[i-1]][1])-Double.parseDouble(Global.configurefile[rankmatrix[i]][1]);
					System.out.print("Dx"+"="+Math.round(Dx)+"\t\t");
				}
			}
			System.out.print("\n");
			for(int i=0; i<10 && Global.configurefile[rankmatrix[i]][0]!=null; i++){
				System.out.print("v"+"="+Global.configurefile[rankmatrix[i]][2]+"\t\t");
			}
			System.out.print("\n");
			for(int i=0; i<10 && Global.configurefile[rankmatrix[i]][0]!=null; i++){
				System.out.print("S"+"="+Global.configurefile[rankmatrix[i]][5]+"\t\t");
			}
			System.out.print("\n");
			for(int i=0; i<10 && Global.configurefile[rankmatrix[i]][0]!=null; i++){
				System.out.print("R"+"="+Global.configurefile[rankmatrix[i]][6]+"\t\t");
			}
			System.out.print("\n");
			System.out.print("\n");
		}
	}
}
