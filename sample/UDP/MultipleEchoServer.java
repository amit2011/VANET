import java.net.*;
import java.io.*;
public class MultipleEchoServer {
public static void main(String[] args) throws IOException {
DatagramSocket serverSocket = null;
boolean listening = true;
try {
serverSocket = new DatagramSocket(2000);
}
catch (IOException e) {
System.out.println("Could not listen on port: 2000" + e);
System.exit(-1);
}
byte[] inData = new byte[1400];
while (listening) {
DatagramPacket inPacket = new DatagramPacket(inData, inData.length);
serverSocket.receive(inPacket);
System.out.println("inData length " + inData.length);
new MultipleEchoServerThread(inPacket, serverSocket).start();
}
serverSocket.close();
}
}