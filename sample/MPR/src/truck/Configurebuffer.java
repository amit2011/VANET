package truck;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;

import network.check;

public class Configurebuffer {
	String ip;
	Integer port;
	Integer x;
	Integer[] links;
//	Configurebuffer[] links;
	public Configurebuffer(String ip, Integer port, Integer x, Integer[] links){
		this.ip=ip;
		this.port=port;
		this.x=x;
		this.links=links;
	}
	public Configurebuffer(String ip, Integer port, Integer x){
		this.ip=ip;
		this.port=port;
		this.x=x;
	}
	
	public static void createconfigure(){
		int ii=0, jj=0;
		String links=null;
		check ck=new check();
		int rank=ck.checkrank(Global.selfipadd);
		if(rank>-1 && Global.selflineconfigure>-1){
			for(int kk=0;kk<Global.size;kk++){
				Global.wirelessconnecttable[kk]=null;
			}
			Global.configurebuffer[Global.selflineconfigure][0]=Global.selfipadd;
			Global.configurebuffer[Global.selflineconfigure][1]="10130";
			Global.configurebuffer[Global.selflineconfigure][2]=Global.configurefile[rank][1];
			for(ii=0;(Global.configurebuffer[ii][0]!=null);ii++){
//				System.out.println("configurebuffer"+"+rank+"+Global.configurefile[rank][1]);
//				System.out.println("configurebuffer"+"+ii+"+Global.configurebuffer[ii][0]);
				if((ii!=Global.selflineconfigure)){
					if(Math.abs(Double.parseDouble(Global.configurefile[rank][1])-Double.parseDouble(Global.configurebuffer[ii][2]))<Global.wirelessdistance){
						Global.wirelessconnecttable[jj]=Global.configurebuffer[ii][0];
						jj++;
						if(links==null){
							links=Integer.toString(ii);
						}
						else
							links=links.concat(Integer.toString(ii));
					}
				}
				Global.configurebuffer[Global.selflineconfigure][4]=links;
			}
		}
	}
	
	public static void writeconfigure(){
		File filew = new File(Global.filepath); 
		try {  
            filew.createNewFile();
        } catch (IOException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        }
		byte writebyte[] = new byte[41];
		byte ipbyte[] = new byte[16];
		byte portbyte[] = new byte[8];
		byte xbyte[] = new byte[8];
		byte linkbyte[] = new byte[8];
		byte enterbyte[]=new byte[1];
        ipbyte=Global.configurebuffer[Global.selflineconfigure][0].getBytes();
		System.arraycopy(ipbyte,0,writebyte,0,ipbyte.length);
		portbyte=Global.configurebuffer[Global.selflineconfigure][1].getBytes();
		System.arraycopy(portbyte,0,writebyte,16,portbyte.length);
		xbyte=Global.configurebuffer[Global.selflineconfigure][2].getBytes();
		System.arraycopy(xbyte,0,writebyte,24,xbyte.length);
		if(Global.configurebuffer[Global.selflineconfigure][4]!=null){
			linkbyte=Global.configurebuffer[Global.selflineconfigure][4].getBytes();
			System.arraycopy(linkbyte,0,writebyte,32,linkbyte.length);
		}
		enterbyte="\n".getBytes();
		System.arraycopy(enterbyte,0,writebyte,40,enterbyte.length);
/*		
		try{
			FileOutputStream in=new FileOutputStream(file);
			try{
				in.write("aaaaaaaaaaaaa".getBytes(), 0, "aaaaaaaaaaaaa".getBytes().length);  
                in.close();
			} catch (IOException e) {  
                // TODO Auto-generated catch block  
                e.printStackTrace();  
            } 
		} catch (FileNotFoundException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        }
*/
		RandomAccessFile randomFile = null;
		try{
			randomFile = new RandomAccessFile(filew, "rws");
			try {
				if(Global.selflineconfigure!=0){
					randomFile.seek(Global.selflineconfigure*41);
				}
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try{
				randomFile.write(writebyte,0,writebyte.length);
//				System.out.println("Configurebuffer"+"+write+"+writebyte);
				randomFile.close();
			} catch (IOException e) {  
                // TODO Auto-generated catch block  
                e.printStackTrace();  
            } 
		} catch (FileNotFoundException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        }
		
	}

}
