import java.net.*;
import java.io.*; 
public class MultipleEchoServerThread extends Thread {
private DatagramPacket inPacket = null;
private DatagramSocket serverSocket = null;
public MultipleEchoServerThread(DatagramPacket inPacket, DatagramSocket
serverSocket) {
this.inPacket = inPacket;
this.serverSocket = serverSocket;
}
public void run(){
int inPacketLength;
String inputLine;
	String clientIP;
	int clientPortnumber;
	String clientPort;
inPacketLength = inPacket.getLength();
System.out.println("inPacket length: " + inPacketLength);
inputLine = new String(inPacket.getData(), 0, inPacketLength);
System.out.println("inputLine length " + inputLine.length());

/*	clientIP=serverSocket.getInetAddress().getHostAddress();
	clientPortnumber=serverSocket.getLocalPort();
	clientPort=Integer.toString(clientPortnumber);
*/
	InetAddress clientIPAddress = inPacket.getAddress();
	clientIP=clientIPAddress.getHostAddress();
	int port = inPacket.getPort();
	clientPort=Integer.toString(port);

output(inputLine);
output(clientIP);
output(clientPort);
}

public void output(String stringdata)
{
byte[] outData = new byte[1400];
InetAddress clientIPAddress = inPacket.getAddress();
int port = inPacket.getPort();

outData = stringdata.getBytes();
DatagramPacket outPacket = new DatagramPacket(outData, outData.length, clientIPAddress, port);
try {
serverSocket.send(outPacket);
} catch (IOException e) {
System.out.println("send error" + e);
}
System.out.println("Echo to UDP Client: " + stringdata);
}
}