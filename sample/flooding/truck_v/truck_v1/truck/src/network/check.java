package network;

import java.net.*;

import truck.Global;

public class check {
	public check(){
		
	}
	public int chechresult(String seqnum, String srcadd){
		InetAddress selfhost = null;
		String selfipadd=null;
		try {
			selfhost = InetAddress.getLocalHost();
			selfipadd=selfhost.getHostAddress();
		} catch (UnknownHostException e) {
			System.out.println("getselfIP error" +e);
			e.printStackTrace();
		}
		if(srcadd==selfipadd){
			return -1;
		}
		int rank=checkrank(srcadd);
		if(rank>-1 && (Global.configurefile[rank][0] == null || Long.parseLong(seqnum)<Long.parseLong(Global.configurefile[rank][4]))){
			return rank;
		}//extra (Global.configurefile[rank][0] == null) for initialization
		else
			return -1;
	}
	public int checkrank(String srcadd){
		int i=0;
		for(i=0;i<9;i++){
			if((Global.configurefile[i][0] == null) || (Global.configurefile[i][0]).equals(srcadd))
				return i;
		}//i<9 since configurefile is String[10][5]
		return -1;
	}

}
