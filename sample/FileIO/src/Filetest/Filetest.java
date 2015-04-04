package Filetest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class Filetest {
	public static void main(String[] args){
		//input from screen, write into txt, if input is "print", read from txt, print on screen
		BufferedReader indata = new BufferedReader(new InputStreamReader(System.in));
		String input=null;
		try {
			input=indata.readLine();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		File file = new File("d:/addfile.txt");  
        try {  
            file.createNewFile();
        } catch (IOException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        }
        
        byte writebyte[] = new byte[1024];
        writebyte=input.getBytes();
        try{
        	FileOutputStream in = new FileOutputStream(file);
        	try{
        		in.write(writebyte, 0, writebyte.length);  
                in.close();
        	} catch (IOException e) {  
                // TODO Auto-generated catch block  
                e.printStackTrace();  
            }  
        } catch (FileNotFoundException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        }
        
        if(input.equals("print")){
        	try{
        		FileInputStream out = new FileInputStream(file);  
        		InputStreamReader outreader = new InputStreamReader(out);
        		int ch = 0;
        		while((ch=outreader.read())!=-1){
        			System.out.print((char) ch);
        		}
        		outreader.close();
        	}catch (Exception e) {  
        		// TODO: handle exception  
        	} 
        }
	}

}
