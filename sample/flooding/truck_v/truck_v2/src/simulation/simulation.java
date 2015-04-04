package simulation;

import truck.comdata;

public class simulation {
	public simulation(){
		
	}
	public comdata simmove(){
		long x=1,v=2,a=1;
		comdata data=new comdata();
		data.x=(long)(x+v*0.1+1/2*a*0.1*0.1);
		data.v=(long)(v+a*0.1);
		data.a=a;
		return(data);
	}
}
