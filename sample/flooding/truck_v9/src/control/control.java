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

				for(i=0;i<9 && (Global.configurefile[i][0] != null);i++){
					if ((Long.parseLong(Global.configurefile[i][6]))==1 && (Long.parseLong(Global.configurefile[i][5]))==3 ){
					    Global.configurefile[j][5]="3";
					    a=0;
					    return a;
					}
				}// when the first car meets, other cars set 3;
				//System.out.println(min);

				if (max==1){
					a=0;
					Global.configurefile[j][5]="3";
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
						//System.out.println(m);
						//System.out.println(i);
						double d=Double.parseDouble(Global.configurefile[i][1])-Double.parseDouble(Global.configurefile[j][1]);
						//System.out.println(d);
						if(d<45) d=45;
						double det1= (double) 2*Math.sqrt((d-25-20)/2)/0.1;
						double det=det1;
						double detx=det/2;
						if (Global.configurefile[j][7]=="-1"){
						    Global.configurefile[j][7]=String.format("%.2f", detx);//use this method
						    a=0;
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
						long n=Long.parseLong(Global.configurefile[j][6])+1;
						for(i=0;i<9 && (Global.configurefile[i][0] != null);i++){
							if(Long.parseLong(Global.configurefile[i][6])==n ){
								break;
							}
						}// get the i to the back of j
						
						if (Global.configurefile[i][5]=="2"){
						    a=0;
						    Global.configurefile[j][5]="3";
						    return a;
					    }
						else if (Global.configurefile[j][5]=="1"){
							a=0;
						    Global.configurefile[j][5]="2";
						    return a;
						}
				    } 
				}
				
                        // greater than 2 cars					
				else if (max>2){
					if (Long.parseLong(Global.configurefile[j][6])==max){
						if (Long.parseLong(Global.configurefile[j][5])==1){
							long m=Long.parseLong(Global.configurefile[j][6])-1;
							for(i=0;i<9 && (Global.configurefile[i][0] != null);i++){
								if(Long.parseLong(Global.configurefile[i][6])==m ){
									break;
								}
							}// get the i to the front of j
							//System.out.println(m);
							//System.out.println(i);
							double d=Double.parseDouble(Global.configurefile[i][1])-Double.parseDouble(Global.configurefile[j][1]);
							//System.out.println(d);
							if(d<45) d=45;
							double det1= (double) 2*Math.sqrt((d-25-20)/2)/0.1;
							double det=det1;
							double detx=det/2;
							if (Global.configurefile[j][7]=="-1"){
							    Global.configurefile[j][7]=String.format("%.2f", detx);//use this method
							    a=0;
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
									else if((Double.parseDouble(Global.configurefile[j][2])<=30)){
										Global.configurefile[j][7]="-1";
										Global.configurefile[j][5]="2";
										a=0;
									    return a;
									}
							     }
						      }
						
					       } //end state==1
						else if (Long.parseLong(Global.configurefile[j][5])==2){
							int q,p;
							long n= Long.parseLong(Global.configurefile[j][6])-1;
							int n1 = (int) n;
							int flag=0;
							for(i=n1;i>=2;i--){
								for(q=0;q<9 && (Global.configurefile[q][0] != null);q++){
									if(Long.parseLong(Global.configurefile[q][6])==i ){
										break;
									}
								}// get the i to the front of j
								for(p=0;p<9 && (Global.configurefile[p][0] != null);p++){
									if(Long.parseLong(Global.configurefile[p][6])==i-1 ){
										break;
									}
								}// get the i to the front of j
							    if(Long.parseLong(Global.configurefile[q][5])==1 &&Long.parseLong(Global.configurefile[p][5])==1 ){
							    	double d=Double.parseDouble(Global.configurefile[p][1])-Double.parseDouble(Global.configurefile[q][1]);
									//System.out.println(d);
									if(d<45) d=45;
									double det1= (double) 2*Math.sqrt((d-25-20)/2)/0.1;
									double det=det1;
									double detx=det/2;
									if (Global.configurefile[j][7]=="-1"){
									    Global.configurefile[j][7]=String.format("%.2f", detx);//use this method
									    a=0;
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
											else if((Double.parseDouble(Global.configurefile[j][2])<=30)){
												Global.configurefile[j][7]="-1";
												Global.configurefile[j][5]="2";
												a=0;
											    return a;
											}
									     }
								      }
							    }
							}
							if(flag==0){
								a=0;
								return a;
							}
								
						}
						
					  } //end==max
					else if (Long.parseLong(Global.configurefile[j][6])==1){
						if(Long.parseLong(Global.configurefile[j][5])==1){
						   long m=Long.parseLong(Global.configurefile[j][6])+1;
						   for(i=0;i<9 && (Global.configurefile[i][0] != null);i++){
							 if(Long.parseLong(Global.configurefile[i][6])==m ){
								  break;
							 }
						   }// get the i to the front of j
						   if(Long.parseLong(Global.configurefile[i][5])==1){
							   a=0;
							   return a;
						   }
						   else if(Long.parseLong(Global.configurefile[i][5])==2){
							   a=0;
							   Global.configurefile[j][5]="3";
							   return a;
						   }
						}
						
					}//end ==1
					else {
						if(Long.parseLong(Global.configurefile[j][5])==1){
							long m=Long.parseLong(Global.configurefile[j][6])+1;
							   for(i=0;i<9 && (Global.configurefile[i][0] != null);i++){
								 if(Long.parseLong(Global.configurefile[i][6])==m ){
									  break;
								 }
							   }// get the i to the front of j
							   if(Long.parseLong(Global.configurefile[i][5])==1){
								   a=0;
								   return a;
							   }
							   else if(Long.parseLong(Global.configurefile[i][5])==2){
								   long n=Long.parseLong(Global.configurefile[j][6])-1;
									for(i=0;i<9 && (Global.configurefile[i][0] != null);i++){
										if(Long.parseLong(Global.configurefile[i][6])==n ){
											break;
										}
									}// get the i to the front of j
									double d=Double.parseDouble(Global.configurefile[i][1])-Double.parseDouble(Global.configurefile[j][1]);
									//System.out.println(d);
									if(d<45) d=45;
									double det1= (double) 2*Math.sqrt((d-25-20)/2)/0.1;
									double det=det1;
									double detx=det/2;
									if (Global.configurefile[j][7]=="-1"){
									    Global.configurefile[j][7]=String.format("%.2f", detx);//use this method
									    a=0;
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
											else if((Double.parseDouble(Global.configurefile[j][2])<=30)){
												Global.configurefile[j][7]="-1";
												Global.configurefile[j][5]="2";
												a=0;
											    return a;
											}
									     }
								      }									
							   }//end ==2							
							
						}//end==1
						else if (Long.parseLong(Global.configurefile[j][5])==2){
							int q,p;
							long n= Long.parseLong(Global.configurefile[j][6])-1;
							int n1 = (int) n;
							int flag=0;
							for(i=n1;i>=2;i--){
								for(q=0;q<9 && (Global.configurefile[q][0] != null);q++){
									if(Long.parseLong(Global.configurefile[q][6])==i ){
										break;
									}
								}// get the i to the front of j
								for(p=0;p<9 && (Global.configurefile[p][0] != null);p++){
									if(Long.parseLong(Global.configurefile[p][6])==i-1 ){
										break;
									}
								}// get the i to the front of j
							    if(Long.parseLong(Global.configurefile[q][5])==1 &&Long.parseLong(Global.configurefile[p][5])==1 ){
							    	double d=Double.parseDouble(Global.configurefile[p][1])-Double.parseDouble(Global.configurefile[q][1]);
									//System.out.println(d);
									if(d<45) d=45;
									double det1= (double) 2*Math.sqrt((d-25-20)/2)/0.1;
									double det=det1;
									double detx=det/2;
									if (Global.configurefile[j][7]=="-1"){
									    Global.configurefile[j][7]=String.format("%.2f", detx);//use this method
									    a=0;
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
											else if((Double.parseDouble(Global.configurefile[j][2])<=30)){
												Global.configurefile[j][7]="-1";
												Global.configurefile[j][5]="2";
												a=0;
											    return a;
											}
									     }
								      }
							    }
							}
							if(flag==0){
								a=0;
								return a;
							}
								
						} //end ==2
							
						
					} //end 1<j<max
					
				  }//end max>2
				   

				}
			 }// state 5==1 or 2
		
        else if(Long.parseLong(Global.configurefile[j][5])==3){
        	if (Double.parseDouble(Global.configurefile[j][2])!=30){
        	 if (Double.parseDouble(Global.configurefile[j][2])>30){
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
        	else {
        	  long rankb=1;
        	  for(i=0;i<9&& (Global.configurefile[i][0] != null);i++){
				if(Long.parseLong(Global.configurefile[i][5])==4){
					for (int k=0;k<9 && (Global.configurefile[k][0] != null);k++){
						if (Double.parseDouble(Global.configurefile[j][1])<Double.parseDouble(Global.configurefile[k][1])){
							rankb++;
						}	
					}
					Global.configurefile[j][6]=Long.toString(rankb);
					a=0;
					return a;
				}
				
			  }//end for

			  for(i=0;i<9 && (Global.configurefile[i][0] != null);i++){
					if(Long.parseLong(Global.configurefile[i][5])==5){
						if (Long.parseLong(Global.configurefile[j][6])<Long.parseLong(Global.configurefile[i][6])){
							a=0;
							return a;
						}
						else {
							if (Long.parseLong(Global.configurefile[i][6])==1){
									long rankb1=0;
									for (int k2=0;k2<9 && (Global.configurefile[k2][0] != null);k2++){
										if (Double.parseDouble(Global.configurefile[j][1])<Double.parseDouble(Global.configurefile[k2][1])){
											rankb1++;
										}	
									}
									Global.configurefile[j][6]=Long.toString(rankb1);
									a=0;
									return a;		
							}
							
						    long m=Long.parseLong(Global.configurefile[i][6])-1;
						    int k=0;
							   for(k=0;k<9 && (Global.configurefile[k][0] != null);k++){
								 if(Long.parseLong(Global.configurefile[k][6])==m ){
									  break;
								 }
							   }// get the i to the front of j
							long m1=Long.parseLong(Global.configurefile[i][6])+1;	
							int k1=0;
							   for(k1=0;k1<9 && (Global.configurefile[k1][0] != null);k1++){
								 if(Long.parseLong(Global.configurefile[k1][6])==m1 ){
									  break;
								 }
							   }// get the i to the back of j
							double d=Double.parseDouble(Global.configurefile[k][1])-Double.parseDouble(Global.configurefile[k1][1]);	 
							double det1=d/2;
							long rankb1=0;
							if (det1<=45){
								if (j==k1){
									Global.configurefile[j][6]=Global.configurefile[i][6];
									a=0;
									return a;
								}
								for (int k2=0;k2<9 && (Global.configurefile[k2][0] != null);k2++){
									if (Double.parseDouble(Global.configurefile[j][1])<Double.parseDouble(Global.configurefile[k2][1])){
										rankb1++;
									}	
								}
								Global.configurefile[j][6]=Long.toString(rankb1);
								a=0;
								return a;		
							} // 
							double det=2*Math.sqrt(det1)/0.1;
	
							double detx=det/2;
							if (Global.configurefile[j][7]=="-1"){
							    Global.configurefile[j][7]=String.format("%.2f", detx);//use this method
							    a=0;
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
									else if((Double.parseDouble(Global.configurefile[j][2])<=30)){
										Global.configurefile[j][7]="-1";
										Global.configurefile[j][5]="3";
										a=0;
									    return a;
									}
							     }
						      }
						}//end else
					}
				}//for
			  a=0;
			  return a; //there is no car that wants to join the system
			  
        	}//else

        }//state==3
        else if(Long.parseLong(Global.configurefile[j][5])==4){
        	if (Double.parseDouble(Global.configurefile[j][2])>30){
				if (Global.configurefile[j][7]=="-1"){
				    double det=(double) ((Double.parseDouble(Global.configurefile[j][2])-30)/2/0.1); // timer
				    if (det==0){
				     a=0;
				     Global.configurefile[j][7]="-1";
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
						Global.configurefile[j][5]="8";
						a=0;
					    return a;
					    }
					}
			} //reduce speed
			else if(Double.parseDouble(Global.configurefile[j][2])<30){
				if (Global.configurefile[j][7]=="-1"){
				    double det=(double) ((30-Double.parseDouble(Global.configurefile[j][2]))/2/0.1);
				    if (det==0){
				     a=0;
				     Global.configurefile[j][7]="-1";
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
						Global.configurefile[j][5]="8";
						a=0;
					    return a;
					    }
					}// increase speed
			}//become the same speed 30m/s
			else if (Double.parseDouble(Global.configurefile[j][2])==30){
				Global.configurefile[j][7]="-1";
				Global.configurefile[j][5]="8";
				a=0;
			    return a;
			}
				
        }
        else if(Long.parseLong(Global.configurefile[j][5])==5){
			System.out.println("leave process begin");
			double det1=15;
			double detxv=det1;
			if (Global.configurefile[j][7]=="-1"){
			    Global.configurefile[j][7]=String.format("%.2f", detxv);//use this method
			    a=0;
			    return a;
			    }// the timer
			else{
				if (Double.parseDouble(Global.configurefile[j][7])>1){
				    Global.configurefile[j][7]=Double.toString(Double.parseDouble(Global.configurefile[j][7])-1);
				    a=0;
				    return a;
				    }// 
				else {
						Global.configurefile[j][7]="-1";
						Global.configurefile[j][5]="9";
						a=0;
					    return a;
					}
			    }     	
        }
        else if(Long.parseLong(Global.configurefile[j][5])==8){
        	for(i=0;i<9 && (Global.configurefile[i][0] != null);i++){
				if(Long.parseLong(Global.configurefile[i][6])==1){
					break;
				}	
			}//	
        	double d=Double.parseDouble(Global.configurefile[i][1])-Double.parseDouble(Global.configurefile[j][1])+20+25;
			//System.out.println(d);
			double det1= 2*Math.sqrt(d/2)/0.1;
			double det=det1;
			double detx=det/2;
			if (Global.configurefile[j][7]=="-1"){
			    Global.configurefile[j][7]=String.format("%.2f", detx);//use this method
			    a=0;
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
					else if((Double.parseDouble(Global.configurefile[j][2])<=30)){
						Global.configurefile[j][7]="-1";
						Global.configurefile[j][5]="3";
						Global.configurefile[j][6]="1";
						a=0;
					    return a;
					}
			     }
		      }
        }
		
		a=0;
    	return 0;
	}
}
        	
