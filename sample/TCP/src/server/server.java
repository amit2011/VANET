package server;

import java.io.*; 
import java.net.*;

public class server {
	public static void main(String[] args) throws IOException{
		ServerSocket serversocket=null;
		try{
		serversocket=new ServerSocket(4000);
		}catch (IOException e) {
		    System.out.println("Could not listen on port: 4000" + e);
		    System.exit(-1);
		} 
		Socket socket=null;
		InputStream indata=socket.getInputStream();
		ByteArrayOutputStream bos=new ByteArrayOutputStream ();
		while(true){
		socket = serversocket.accept();
		int len=0;
		byte[] buffer=new byte[1024];
		while((len=indata.read(buffer))!=-1){
			bos.write(buffer,0,len);
		}
		
		String msg = bos.toString();
		System.out.println("one time");
		System.out.println(msg);
		}
	}

}
