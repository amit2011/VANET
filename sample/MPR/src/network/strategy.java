package network;

import truck.Global;

public class strategy {
	public strategy(){
	}
	
	public int createneitable(String srcadd,String[] bidirection, String[] undirection){
		int ii=0, jj=0, flag=0;
		//check src
		for(ii=0;Global.neighbortable[ii][0]!=null;ii++){
			if(srcadd.equals(Global.neighbortable[ii][0])){
				flag=1;
				break;
			}
		}
		if(flag==0){
			Global.neighbortable[ii][0]=srcadd;
//			System.out.println("strategy"+"+srcadd+"+Global.neighbortable[ii][0]);
			Global.neighbortable[ii][1]="1";
			Global.neighbortable[ii][3]=null;
			Global.neighbortable[ii][4]=Long.toString(System.currentTimeMillis());
		}
		else
			flag=0;
		//check one hop
		for(jj=0;bidirection[jj]!=null;jj++){
			for(ii=0;Global.neighbortable[ii][0]!=null;ii++){
/*				if(ii>3){
					System.out.println(ii);
					System.out.println("strategy"+"+neighbortable+"+Global.neighbortable[ii][0]);
				}
*/				
				if(System.currentTimeMillis()-Long.parseLong(Global.neighbortable[ii][4])<Global.neighborholdtimeflag){
					if(bidirection[jj].equals(Global.neighbortable[ii][0])){
						Global.neighbortable[ii][1]="1";
						Global.neighbortable[ii][3]=null;
						Global.neighbortable[ii][4]=Long.toString(System.currentTimeMillis());
						flag=1;
					}
				}
				else if(bidirection[jj].equals(Global.selfipadd)){
					flag=1;
				}
			}
			if(flag==0){
				Global.neighbortable[ii][0]=bidirection[jj];
//				System.out.println("strategy"+"+bidirection+"+Global.neighbortable[ii][0]);
				Global.neighbortable[ii][1]="2";
				Global.neighbortable[ii][3]=null;
				Global.neighbortable[ii][4]=Long.toString(System.currentTimeMillis());
			}
			else
				flag=0;
		}
		
		setstatus();
		return 0;
	}
	
	public static void setstatus(){
		int ii=0, jj=0, kk=0, ll=0;
		int proflag=-1, backflag=-1;
		double selflocation=0;
		for(kk=0; Global.configurefile[kk][0]!=null;kk++){
			if(Global.selfipadd.equals(Global.configurefile[kk][0])){
				selflocation=Double.parseDouble(Global.configurefile[kk][1]);
				break;
			}
		}
		double prolocation=selflocation, backlocation=selflocation;
//		String[] bidirection=new String[10];
		for(ii=0;Global.neighbortable[ii][0]!=null;ii++){
			if(Global.neighbortable[ii][1].equals("2")){
				Global.neighbortable[ii][2]="0";
			}
			else if(Global.neighbortable[ii][1].equals("1")){
				if(System.currentTimeMillis()-Long.parseLong(Global.neighbortable[ii][4])>Global.neighborholdtimeflag){
					Global.neighbortable[ii][2]="0";
				}
				//MPR select
				else{
					Global.neighbortable[ii][2]="1";
					if(selflocation==0){
//						break;
					}//change configurefile[jj][0] to wirelessconnecttable[jj]
					for(jj=0; Global.wirelessconnecttable[jj]!=null;jj++){
						if(Global.neighbortable[ii][0].equals(Global.wirelessconnecttable[jj])){
							for(ll=0;Global.wirelessconnecttable[jj].equals(Global.configurefile[ll][0])==false;ll++){
								;
							}
							if(selflocation<Double.parseDouble(Global.configurefile[ll][1])){
								if(prolocation<Double.parseDouble(Global.configurefile[ll][1])){
									prolocation=Double.parseDouble(Global.configurefile[ll][1]);
									proflag=ii;
//									break;
								}
							}
							else if(selflocation>Double.parseDouble(Global.configurefile[ll][1])){
								if(backlocation>Double.parseDouble(Global.configurefile[ll][1])){
									backlocation=Double.parseDouble(Global.configurefile[ll][1]);
									backflag=ii;
//									break;
								}
							}
						}
					}
				}
			}
		}
		if(proflag>-1 && backflag>-1){
			Global.neighbortable[proflag][2]="2";
			Global.MPRselect[0]=Global.neighbortable[proflag][0];
			Global.neighbortable[backflag][2]="2";
			Global.MPRselect[1]=Global.neighbortable[backflag][0];
		}
		else if(proflag>-1 && backflag==-1){
			Global.neighbortable[proflag][2]="2";
			Global.MPRselect[0]=Global.neighbortable[proflag][0];
		}
		else if(proflag==-1 && backflag>-1){
			Global.neighbortable[backflag][2]="2";
			Global.MPRselect[0]=Global.neighbortable[backflag][0];
		}
	}

}
