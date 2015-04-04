package truck;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;

import network.check;
import network.listening;

public class Truck {
	public static void main(String[] args){
		threadhelloperiod th_helloperiod=new threadhelloperiod();
		Thread Th_helloperiod=new Thread(th_helloperiod);
		Th_helloperiod.start();
		
		threadupdateperiod th_updateperiod=new threadupdateperiod();
		Thread Th_updateperiod=new Thread(th_updateperiod);
		Th_updateperiod.start();
		
		listening listen=new listening();
		try {
			listen.listeningprocess();
		} catch (IOException e) {
			System.out.println("start listening error" + e);
			e.printStackTrace();
		}
	}
}
//send hello
class threadhelloperiod implements Runnable{
	threadhelloperiod(){
	}

	public void run() {
		while(true){
			try {
				Thread.sleep(Global.sendhelloperiodflag);
			} catch (InterruptedException e) {
				System.out.println("thread1 sleep error" + e);
				e.printStackTrace();
			}
			threadhello th_hello=new threadhello();
			Thread Th_hello=new Thread(th_hello);
			Th_hello.start();
		}
	}
}

class threadhello implements Runnable{
	threadhello(){
	}
	public void run(){
		Forward.sendhello();
	}
}
//updata data
class threadupdateperiod implements Runnable{
	threadupdateperiod(){
	}
	
	public void run() {
		Forward.initializtion();
		System.out.println("initializtion finished");
		activemonitor th_monitor=new activemonitor();
		Thread Th_monitor=new Thread(th_monitor);
		Th_monitor.start();
		while(true){
			try {
				Thread.sleep(Global.selfdataperiodflag);
			} catch (InterruptedException e) {
				System.out.println("thread1 sleep error" + e);
				e.printStackTrace();
			}
			threadupdate th_update=new threadupdate();
			Thread Th_update=new Thread(th_update);
			Th_update.start();
		}
	}
}

class threadupdate implements Runnable{
	threadupdate(){
	}
	
	public void run() {
		Forward.updatedata();
		Forward.updateconfigure();
		Forward.printsecond();
		
	}
}

class activemonitor implements Runnable{
	public activemonitor(){
		
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
		if(command.equals("q")){
			System.exit(0);
		}
		else
			System.out.println("wrong command, try again");
	}
}