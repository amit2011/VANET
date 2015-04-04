import java.io.*; import java.net.*;public class EchoClient { 	public static void main(String[] args) throws IOException { 			BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in)); 		//System.in这个输入流指的是用户从屏幕交互，输入client的字符
		DatagramSocket echoSocket = new DatagramSocket(); 		//创建UDP的socket	    InetAddress serverIPAddress = InetAddress.getByName("192.168.1.20");		//将String类型的IP转换为InetAddress类型
	    byte[] outData = new byte[1400]; 	    byte[] inData = new byte[1400];   		String userInput;
		int inPacketLength;
		while ((userInput = stdIn.readLine()) != null) { 			outData = userInput.getBytes();			//将字符放入输出buffer
		  	DatagramPacket outPacket = new DatagramPacket(outData, outData.length, serverIPAddress, 2000); 			//为输出buffer里的字符outdata，建立数据报outpacket			echoSocket.send(outPacket);			//将数据报送出去，用DatagramSocket的send方法
			System.out.println("OutData length:" + outData.length);			DatagramPacket inPacket = new DatagramPacket(inData, inData.length); 			//为输入buffer字符indata，建立数据报inPacket	  		echoSocket.receive(inPacket);			//将接收字符连接数据报inPacket，并送入输入buffer
	  		inPacketLength = inPacket.getLength();			//测量数据报inPacket长度	  		String echoString = new String(inPacket.getData(), 0, inPacketLength); 			//将数据报inPacket的字符或输出buffer的字符，输出并从package类型转换为string
	  		System.out.println("Echo from UDP Server: " + echoString); 			System.out.println("inPacket length:" + inPacketLength);	    	if (userInput.equals("Bye."))	        	break;        		 } 			stdIn.close(); 		echoSocket.close(); 	} } 