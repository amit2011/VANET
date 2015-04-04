package truck;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Global {
	
	
	public static int printflag=0;
	public static int throughputnum=0;
	public static long inittime=0;
	public static long throughput=0;
	public static String truckdelayflag=null;
	public static final int size=15;
	public static final long wirelessdistance=150;	//meter
	public static final long selfdataperiodflag=100;	//msecond
	public static final long sendhelloperiodflag=1000;	//msecond
	public static final long neighborholdtimeflag=10000;	//msecond
	public static final String filepath="/home/new-ece/lzg0014/Desktop/file.txt";	//filepath
	public static int selflineconfigure=-1;
	public static long[] truckdelaytime=new long[5];
	public static String[][] configurefile=new String[size][8];
	public static String[][] configurebuffer=new String[size][5];
	public static String[] MPRselect=new String[2];
	public static String[][] neighbortable=new String[size][5];
	public static String[] wirelessconnecttable=new String[size];
	
	public static String selfipadd=getselfip();
	
	private static String getselfip() {
		InetAddress selfhost = null;
		String selfipadd=null;
		try {
			selfhost = InetAddress.getLocalHost();
			selfipadd=selfhost.getHostAddress();
		} catch (UnknownHostException e) {
			System.out.println("getselfIP error" + e);
			e.printStackTrace();
		}
		return selfipadd;
	}
	
	private static String[] init_wirelessconnecttable(){
		String[] wirelessconnecttable=new String[size];
		wirelessconnecttable[0]="131.204.14.255";
		return wirelessconnecttable;
	}

}
