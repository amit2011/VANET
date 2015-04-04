package printfile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class printfile {
	public static void main(String[] args){
		threadprintfile th_printfile=new threadprintfile();
		Thread Th_printfile=new Thread(th_printfile);
		Th_printfile.start();
	}
	
	public static void getfile(){
		File file = new File("/home/new-ece/lzg0014/Desktop/file.txt");
		RandomAccessFile randomFile = null;
		try{
			randomFile = new RandomAccessFile(file, "r");
			String readline;
			try {
				while((readline=randomFile.readLine())!=null){
					System.out.println(readline);
				}
				randomFile.close();
				for(int ii=3;ii>0;ii--){
					System.out.println("\n");
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
}

class threadprintfile implements Runnable{
	threadprintfile(){
	}

	public void run() {
		while(true){
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				System.out.println("thread1 sleep error" + e);
				e.printStackTrace();
			}
			printfile.getfile();
		}
	}
}
