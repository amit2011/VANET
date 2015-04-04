package control;
import java.lang.Math;
import java.net.InetAddress;
import java.net.UnknownHostException;

import truck.Global;

public class control {
	public void control(){
	}
	
	public long controlmethod(){
		long a=0;
		InetAddress selfhost = null;
		String selfipadd=null;
		try {
			selfhost = InetAddress.getLocalHost();
			selfipadd=selfhost.getHostAddress();
		} catch (UnknownHostException e) {
			System.out.println("getselfIP error" +e);
			e.printStackTrace();
		}
		
		int i=0,j=-1;
		for(i=0;i<9;i++){
			if((Global.configurefile[i][0] != null) && (Global.configurefile[i][0]).equals(selfipadd))
				j=i;
		}//get the IP position
		if (j==-1)
				return a;
		if (Long.parseLong(Global.configurefile[j][5])==0){
			if (Double.parseDouble(Global.configurefile[j][2])>=30){
				if (Global.configurefile[j][7]=="-1"){
				    double det=(double) ((Double.parseDouble(Global.configurefile[j][2])-30)/2/0.1); // timer
				    if (det==0){
				     a=0;
				     Global.configurefile[j][7]="-1";
					 Global.configurefile[j][5]="1";
				     return a;
				    }
				    else{
				    Global.configurefile[j][7]=Double.toString(det);
				    a=-2;
				    return a;
				    }
				}
				else{
					if (Double.parseDouble(Global.configurefile[j][7])>1){
					    Global.configurefile[j][7]=Double.toString(Double.parseDouble(Global.configurefile[j][7])-1);
					    a=-2;
					    return a;
					    }
					else{
						Global.configurefile[j][7]="-1";
						Global.configurefile[j][5]="1";
						a=0;
					    return a;
					    }
					}
			} //reduce speed
			else {
				if (Global.configurefile[j][7]=="-1"){
				    double det=(double) ((30-Double.parseDouble(Global.configurefile[j][2]))/2/0.1);
				    if (det==0){
				     a=0;
				     Global.configurefile[j][7]="-1";
					 Global.configurefile[j][5]="1";
				     return a;
				    }
				    else{
				    Global.configurefile[j][7]=Double.toString(det);//use this method
				    a=2;
				    return a;
				    }
				}
				else{
					if (Double.parseDouble(Global.configurefile[j][7])>1){
					    Global.configurefile[j][7]=Double.toString(Double.parseDouble(Global.configurefile[j][7])-1);
					    a=2;
					    return a;
					    }
					else{
						Global.configurefile[j][7]="-1";
						Global.configurefile[j][5]="1";
						a=0;
					    return a;
					    }
					}// increase speed
			}//become the same speed 30m/s
			
		}
		else if(Long.parseLong(Global.configurefile[j][5])==1 || Long.parseLong(Global.configurefile[j][5])==2){
			int k=1;
			for(i=0;i<9 && (Global.configurefile[i][0] != null);i++){
				if(Long.parseLong(Global.configurefile[i][5])==0){
					k=0;
					break;
				}
			}//
			if (k==0){
				a=0;
				return a;
			} // waiting for other cars
			else{
				long max=0;
				for(i=0;i<9 && (Global.configurefile[i][0] != null);i++){
					max=(Long.parseLong(Global.configurefile[i][6]));
					break;
				}//
				for(i=0;i<9 && (Global.configurefile[i][0] != null);i++){
					if (max<(Long.parseLong(Global.configurefile[i][6])))
					max=(Long.parseLong(Global.configurefile[i][6]));
				}// get the max rank;
				//System.out.println(max);
				//System.out.println(j);
				
				if (max==1){
					a=0;
					Global.configurefile[j][5]="2";
					return a;
				}
				else if (max==2){
					if (Long.parseLong(Global.configurefile[j][6])==max){
						long m=Long.parseLong(Global.configurefile[j][6])-1;
						for(i=0;i<9 && (Global.configurefile[i][0] != null);i++){
							if(Long.parseLong(Global.configurefile[i][6])==m ){
								break;
							}
						}// get the i to the front of j
						System.out.println(m);
						System.out.println(i);
						double d=Double.parseDouble(Global.configurefile[i][1])-Double.parseDouble(Global.configurefile[j][1]);
						//System.out.println(d);
						if(d<=45) d=45;
						double det1= (double) 2*Math.sqrt((d-25-20)/2)/0.1;
						double det=det1;
						double detx=det/2+1;
						if (Global.configurefile[j][7]=="-1" && Global.configurefile[5][1]!="2"){
						    Global.configurefile[j][7]=String.format("%.2f", detx);//use this method
						    a=2;
						    return a;
						    }// the timer
						else{
							if (Double.parseDouble(Global.configurefile[j][7])>1){
							    Global.configurefile[j][7]=Double.toString(Double.parseDouble(Global.configurefile[j][7])-1);
							    a=2;
							    return a;
							    }// increase speed with +a
							else if (Double.parseDouble(Global.configurefile[j][7])<=1){
								if ((Double.parseDouble(Global.configurefile[j][2])>30)){
							    a=-2;
							    return a;
							    }// decrease speed with -a
								else if((Double.parseDouble(Global.configurefile[j][2])<=30))
								{
									Global.configurefile[j][7]="-1";
									Global.configurefile[j][5]="2";
									a=0;
								    return a;
								}
								}
							}
						
						
					}
					else if (Long.parseLong(Global.configurefile[j][6])==max-1) {
						a=0;
						Global.configurefile[j][5]="2";
						return a;
					}
				}
				else if (max>2){
					
				}
					
					
				a=0;
				return a;
			    
				/*
				if (Double.parseDouble(Global.configurefile[j][6])==1){
					Global.configurefile[j][7]="-1";
					Global.configurefile[j][5]="2";
					a=0;
					return a;
				}// the first car
				else{
					long m=Long.parseLong(Global.configurefile[j][6])-1;
					for(i=0;i<9 && (Global.configurefile[i][0] != null);i++){
						if(Long.parseLong(Global.configurefile[i][6])==m ){
							break;
						}
					}// get the i to the front of j
					double d=Double.parseDouble(Global.configurefile[i][1])-Double.parseDouble(Global.configurefile[j][1]);
					double det1=2*Math.sqrt((d-25-20)/2);
					double det=det1;
					double detx=det/2;
					if (Global.configurefile[j][7]=="-1"){
					    Global.configurefile[j][7]=String.format("%.2f", det);;//use this method
					    a=2;
					    return a;
					    }// the timer
					else{
						if (Double.parseDouble(Global.configurefile[j][7])>=detx){
						    Global.configurefile[j][7]=Long.toString(Long.parseLong(Global.configurefile[j][7])-1);
						    a=2;
						    return a;
						    }// increase speed with +a
						else if (Double.parseDouble(Global.configurefile[j][7])>1){
						    Global.configurefile[j][7]=Long.toString(Long.parseLong(Global.configurefile[j][7])-1);
						    a=-2;
						    return a;
						    }// decrease speed with -a
						else{
							Global.configurefile[j][7]="-1";
							Global.configurefile[j][5]="2";
							a=0;
						    return a;
						    }
						}
					}*/       
				}
		}
        else if(Long.parseLong(Global.configurefile[j][5])==2){
			for(i=0;i<9&& (Global.configurefile[i][0] != null);i++){
				if(Long.parseLong(Global.configurefile[i][5])==3){
					Global.configurefile[j][6]=Global.configurefile[j][6]+Long.toString(1);
					a=0;
					return a;
				}
				
			}//
			for(i=0;i<9 && (Global.configurefile[i][0] != null);i++){
				if(Long.parseLong(Global.configurefile[i][5])==4){
					if (j<i){
						a=0;
						return a;
					}
					else {
						long d=20;
						double det1=2*Math.sqrt((d)/2);
						double det=(double)det1;
						double detx=det/2;
						if (Global.configurefile[j][7]=="-1"){
						    Global.configurefile[j][7]=String.format("%.2f", det);//use this method
						    a=2;
						    return a;
						    }// the timer
						else{
							if (Double.parseDouble(Global.configurefile[j][7])>=detx){
							    Global.configurefile[j][7]=Double.toString(Double.parseDouble(Global.configurefile[j][7])-1);
							    a=2;
							    return a;
							    }// increase speed with +a
							else if (Double.parseDouble(Global.configurefile[j][7])>1){
							    Global.configurefile[j][7]=Double.toString(Double.parseDouble(Global.configurefile[j][7])-1);
							    a=-2;
							    return a;
							    }// decrease speed with -a
							else{
								Global.configurefile[j][7]="-1";
								Global.configurefile[j][5]="2";
								a=0;
							    return a;
							    }
							}
					}
				}
				
			}//for
        	
		}
        else if(Long.parseLong(Global.configurefile[j][5])==3){
        	for(i=0;i<9 && (Global.configurefile[i][0] != null);i++){
				if(Long.parseLong(Global.configurefile[i][6])==1){
					break;
				}	
			}//	
        	Double d=Double.parseDouble(Global.configurefile[i][1])-Double.parseDouble(Global.configurefile[j][1])+20+25;
			double det1=2*Math.sqrt((d)/2);
			double det=(double)det1;
			double detx=det/2;
			if (Global.configurefile[j][7]=="-1"){
			    Global.configurefile[j][7]=String.format("%.2f", det);//use this method
			    a=2;
			    return a;
			    }// the timer
			else{
				if (Double.parseDouble(Global.configurefile[j][7])>=detx){
				    Global.configurefile[j][7]=Double.toString(Double.parseDouble(Global.configurefile[j][7])-1);
				    a=2;
				    return a;
				    }// increase speed with +a
				else if (Double.parseDouble(Global.configurefile[j][7])>1){
				    Global.configurefile[j][7]=Double.toString(Double.parseDouble(Global.configurefile[j][7])-1);
				    a=-2;
				    return a;
				    }// decrease speed with -a
				else{
					Global.configurefile[j][7]="-1";
					Global.configurefile[j][5]="2";
					a=0;
				    return a;
				    }
				}
		}
        else if(Long.parseLong(Global.configurefile[j][5])==4){
			
		}

		return(a);
	}
}
