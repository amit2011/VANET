package truck;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;

import network.broadcast;
import network.check;
import simulation.simulation;
import control.control;

public class Forward {
	public static void initializtion(){
		Global.wirelessconnecttable[0]="131.204.14.255";
		System.out.println("begin or join");
		BufferedReader indata1 = new BufferedReader(new InputStreamReader(System.in));
		String input1=null;
		try {
			input1=indata1.readLine();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if(input1.equals("b")){
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
			Comdata xva=new Comdata();
			xva.x=(int) ((10-rank)*80+Math.random()*20);
			xva.v=(int) (25+Math.random()*10);
			
			Global.configurefile[0][0]=Global.selfipadd;
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
			broadcast brocast=new broadcast("9999", Global.selfipadd, Global.selfipadd);
			boolean breakflag=true;
			while(breakflag){
				try {
					Thread.sleep(100);
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
			
			Global.configurefile[0][0]=Global.selfipadd;
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
	}

	public static void updatedata() {
		long a=control.controlmethod();
		Comdata xva=new Comdata();
		int ii=0;
		for(ii=0;ii<Global.size-1;ii++){
			if((Global.configurefile[ii][0] != null) && (Global.configurefile[ii][0]).equals(Global.selfipadd))
            break;
		}//get the IP position
		xva.x=Double.parseDouble(Global.configurefile[ii][1]);
		xva.v=Double.parseDouble(Global.configurefile[ii][2]);
		xva.a=a;
		xva.truckstate=Long.parseLong(Global.configurefile[ii][5]);
		xva.truckrank=Long.parseLong(Global.configurefile[ii][6]);
		
		simulation sim=new simulation();
		xva=sim.simmove(xva);
		check ck=new check();
		int rank=ck.checkrank(Global.selfipadd);
		if(rank>-1 && (Global.configurefile[rank][4]==null || Global.configurefile[rank][4]=="0")){
			Global.configurefile[rank][4]="9999";
		}//extra (Global.configurefile[rank][0] == null) for initialization (=="0") for loop 0-9999
		else{
			Global.configurefile[rank][4]=Long.toString(Long.parseLong(Global.configurefile[rank][4])-1);
		}
		//write file
		Global.configurefile[rank][0]=Global.selfipadd;
		Global.configurefile[rank][1]=String.format("%.2f", xva.x);
		Global.configurefile[rank][2]=String.format("%.2f", xva.v);
		Global.configurefile[rank][3]=Long.toString(xva.a);
		Global.configurefile[rank][5]=Long.toString(xva.truckstate);
		Global.configurefile[rank][6]=Long.toString(xva.truckrank);
		{
			//delay detect
			xva.delaytime=Global.truckdelayflag;
			Global.truckdelayflag=null;
			if(xva.delaytime!=null){
			}
			if(Long.parseLong(Global.configurefile[rank][4])%100==0){
				xva.delaytime="1";
				Global.truckdelaytime[0]=System.currentTimeMillis();
			}
		}
		//broadcast
		String seqnum=Global.configurefile[rank][4];
		broadcast brocast=new broadcast(seqnum, Global.selfipadd, Global.selfipadd);
		try {
			brocast.broadcastmsg(xva);
		} catch (IOException e) {
			System.out.println("broadcast error" + e);
			e.printStackTrace();
		}
	}

	public static void sendhello() {
		Integer unnum=0,binum=0;
		int ii=0;
		String [] undirection=new String[10];
		String [] bidirection=new String[10];
		for(ii=0;Global.neighbortable[ii][0]!=null;ii++){
/*
			System.out.println("0+"+Global.neighbortable[ii][0]);
			System.out.println("1+"+Global.neighbortable[ii][1]);
			System.out.println("2+"+Global.neighbortable[ii][2]);
*/
			if(Global.neighbortable[ii][2].equals("0")){
				if((Global.neighbortable[ii][0].startsWith("131.204.14"))&&(Global.neighbortable[ii][0].length()==14)){
					undirection[unnum]=Global.neighbortable[ii][0];
					unnum++;
				}
			}
			else if(Global.neighbortable[ii][2].equals("1") || Global.neighbortable[ii][2].equals("2")){
				if((Global.neighbortable[ii][0].startsWith("131.204.14"))&&(Global.neighbortable[ii][0].length()==14)){
					bidirection[binum]=Global.neighbortable[ii][0];
					binum++;
//				System.out.println(Global.neighbortable[ii][1]);
				}
			}
		}
		broadcast brocast=new broadcast(Global.selfipadd);
		try {
			brocast.broadcasthello(binum, unnum, bidirection, undirection);
		} catch (IOException e) {
			System.out.println("broadcast error" + e);
			e.printStackTrace();
		}
	}
	
	public static void updateconfigure(){
		File file = new File(Global.filepath); 
		try {  
            file.createNewFile();
        } catch (IOException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        }
     
/*        FileInputStream out = null;
		try {
			out = new FileInputStream(file);
			BufferedReader readout = new BufferedReader(new InputStreamReader(out));
*/
		RandomAccessFile randomFile = null;
		try{
			randomFile = new RandomAccessFile(file, "rws");
			byte[] readlinebyte=new byte[41];
			String readline=new String(readlinebyte);
			try {
				int ii=0, lineresult=-1;
				while((readline=randomFile.readLine())!=null){
//					System.out.println("Forward"+"+readline+"+readline);
					lineresult=processline(readline, ii);
					if(lineresult!=-1){
						Global.selflineconfigure=lineresult;
					}
					ii++;
				}
				randomFile.close();
				if(Global.selflineconfigure==-1){
					Global.selflineconfigure=ii;
				}
				//file line change method
				check ck=new check();
				int rank=ck.checkrank(Global.selfipadd);
				if(rank>-1){
					Global.selflineconfigure=Integer.parseInt(Global.configurefile[rank][6])-1;
//				System.out.println("Forward"+"+selflineconfigure+"+Global.selflineconfigure);
					Configurebuffer.createconfigure();
					Configurebuffer.writeconfigure();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	static void printsecond(){
		//print display
/*			for(int i=0; i<10 && Global.configurefile[i][0]!=null; i++){
				for(int j=0;j<8;j++){
					System.out.println(Global.configurefile[i][j]);
				}
			}
*/		
			
		Global.printflag++;
		if(Global.printflag==1000/Global.selfdataperiodflag)
		{
			System.out.println("print"+"+MPRselect[0]+"+Global.MPRselect[0]);
			System.out.println("print"+"+MPRselect[1]+"+Global.MPRselect[1]);
			Global.printflag=0;
			int[] rankmatrix=new int[Global.size];
			for(int i=0;i<Global.size;i++)
				rankmatrix[i]=9;
			
			for(int i=0;i<Global.size && Global.configurefile[i][0]!=null; i++){
				int rankflag=0;
				for(int j=0;j<Global.size && Global.configurefile[j][0]!=null; j++){
					if(Double.parseDouble(Global.configurefile[i][1]) < Double.parseDouble(Global.configurefile[j][1]))
						rankflag++;
				}
				rankmatrix[rankflag]=i;
			}
			
			for(int i=0; i<Global.size && Global.configurefile[rankmatrix[i]][0]!=null; i++){
				System.out.print("ip"+"="+Global.configurefile[rankmatrix[i]][0].substring(11,14)+"\t\t");
			}
			System.out.print("\n");
			for(int i=0; i<Global.size && Global.configurefile[rankmatrix[i]][0]!=null; i++){
				System.out.print("x"+"="+Global.configurefile[rankmatrix[i]][1]+"\t");
			}
			System.out.print("\n");
			for(int i=0; i<Global.size && Global.configurefile[rankmatrix[i]][0]!=null; i++){
				if(i==0)
					System.out.print("Dx"+"="+"0"+"\t\t");
				else{
					double Dx=Double.parseDouble(Global.configurefile[rankmatrix[i-1]][1])-Double.parseDouble(Global.configurefile[rankmatrix[i]][1]);
					System.out.print("Dx"+"="+Math.round(Dx)+"\t\t");
				}
			}
			System.out.print("\n");
			for(int i=0; i<Global.size && Global.configurefile[rankmatrix[i]][0]!=null; i++){
				System.out.print("v"+"="+Global.configurefile[rankmatrix[i]][2]+"\t\t");
			}
			System.out.print("\n");
			for(int i=0; i<Global.size && Global.configurefile[rankmatrix[i]][0]!=null; i++){
				System.out.print("S"+"="+Global.configurefile[rankmatrix[i]][5]+"\t\t");
			}
			System.out.print("\n");
			for(int i=0; i<Global.size && Global.configurefile[rankmatrix[i]][0]!=null; i++){
				System.out.print("R"+"="+Global.configurefile[rankmatrix[i]][6]+"\t\t");
			}
			System.out.print("\n");
			System.out.print("\n");
		}
	}
	
/*	public static void processline(String readline, int ii){
		String ip=readline.substring(0,15);
		Integer port=Integer.parseInt(readline.substring(16,23));
		Integer x=Integer.parseInt(readline.substring(24,31));
		switch(ii)
		{
			case 0: Configurebuffer node0 =new Configurebuffer(ip, port, x); break;
			case 1: Configurebuffer node1 =new Configurebuffer(ip, port, x); break;
			case 2: Configurebuffer node2 =new Configurebuffer(ip, port, x); break;
			case 3: Configurebuffer node3 =new Configurebuffer(ip, port, x); break;
			case 4: Configurebuffer node4 =new Configurebuffer(ip, port, x); break;
			case 5: Configurebuffer node5 =new Configurebuffer(ip, port, x); break;
			case 6: Configurebuffer node6 =new Configurebuffer(ip, po
			System.out.println("Forward"+"+selflineconfigure+"+Global.selflineconfigure);rt, x); break;
			case 7: Configurebuffer node7 =new Configurebuffer(ip, port, x); break;
			case 8: Configurebuffer node8 =new Configurebuffer(ip, port, x); break;
			case 9: Configurebuffer node9 =new Configurebuffer(ip, port, x); break;
			default: System.out.println("nodes is over 10");
		}
*/
	public static int processline(String readlinepart, int ii){
		byte[] readlinebyte=new byte[41];
		readlinebyte=readlinepart.getBytes();
		String readline=new String(readlinebyte);
//		readline=readline.concat(readlinepart);
		Global.configurebuffer[ii][0]=readline.substring(0,15).trim();		//ip
		Global.configurebuffer[ii][1]=readline.substring(16,23).trim();	//port
		Global.configurebuffer[ii][2]=readline.substring(24,31).trim();	//x
		Global.configurebuffer[ii][4]=readline.substring(32,39).trim();	//links
//		System.out.println("Forward"+"+selfipadd+"+Global.selfipadd);
//		System.out.println("Forward"+"+substring+"+Global.configurebuffer[ii][0]);
		if(Global.selfipadd.equals(readline.substring(0,15).trim())){
//			System.out.println("Forward"+"+lineip+"+readline.substring(0,15));
//			System.out.println("Forward"+"+selfipadd+"+Global.selfipadd);
//			System.out.println("Forward"+"+lineflag+");
			return(ii);
		}
		return -1;
	}

}
