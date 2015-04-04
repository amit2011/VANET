import java.io.*; import java.net.*;public class EchoClient { 	public static void main(String[] args) throws IOException { 			BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in)); 
		DatagramSocket echoSocket = new DatagramSocket(); 	    InetAddress serverIPAddress = InetAddress.getByName("192.168.1.255");
	    byte[] outData = new byte[1400]; 	    byte[] inData = new byte[1400];   		String userInput;
		int inPacketLength;
		while ((userInput = stdIn.readLine()) != null) { 			outData = userInput.getBytes(); 
		  	DatagramPacket outPacket = new DatagramPacket(outData, outData.length, serverIPAddress, 2000); 			echoSocket.send(outPacket);
			System.out.println("OutData length:" + outData.length);			DatagramPacket inPacket = new DatagramPacket(inData, inData.length); 			for(int i=3;i>0;i--){	  		echoSocket.receive(inPacket);
	  		inPacketLength = inPacket.getLength(); 	  		String echoString = new String(inPacket.getData(), 0, inPacketLength); 
	  		System.out.println("Echo from UDP Server: " + echoString); 			System.out.println("inPacket length:" + inPacketLength);			}	    	if (userInput.equals("Bye."))	        	break;        		 } 			stdIn.close(); 		echoSocket.close(); 	} } 