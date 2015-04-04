package client;

import java.io.*; 
import java.net.*;

public class client {
	public static void main(String[] args) throws IOException {
		Socket socket=new Socket("192.168.1.14", 4000);
		OutputStream outdata=socket.getOutputStream();
		
		BufferedReader systemin1 = new BufferedReader(new InputStreamReader(System.in));
		String msg1=systemin1.readLine();
//		BufferedReader systemin2 = new BufferedReader(new InputStreamReader(System.in));
//		String msg2=systemin2.readLine();
		
		byte[] bytes=new byte[100];
		byte[] msg2byte = new byte[10];
		msg2byte = msg1.getBytes();
		System.arraycopy(msg2byte,1,bytes,10,2);
		
		
		outdata.write(bytes);
//		outdata.write(msg2.getBytes());
		outdata.close();
		socket.close();
	}

}
