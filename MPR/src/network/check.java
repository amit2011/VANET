package network;

import truck.Global;

public class check {
	public check(){
	}
	public int checkresultseq(String seqnum, String srcadd){
		if(srcadd==Global.selfipadd){
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
		for(i=0;i<Global.size-1;i++){
			if((Global.configurefile[i][0] == null) || (Global.configurefile[i][0]).equals(srcadd))
				return i;
		}//i<9 since configurefile is String[10][5]
		return -1;
	}
	
	public void checkresultstate(String srcadd, String clientIP){
		if(srcadd.equals(clientIP)){
			System.exit(0);
		}
		int i=0;
		for(i=0;i<Global.size-1;i++){
			if((Global.configurefile[i][0]!=null) && (Global.configurefile[i][0]).equals(srcadd)){
				for(int j=i;j<Global.size-1;j++){
					if(Global.configurefile[j][0] == null){
						for(int k=0;k<8;k++){
							Global.configurefile[i][k]=Global.configurefile[j-1][k];
							Global.configurefile[j-1][k]=null;
						}
						j=Global.size-1;
					}
				}
				i=Global.size-1;
			}
		}
	}
}
