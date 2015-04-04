package simulation;

import truck.comdata;

public class simulation {
	public simulation(){
		
	}
	public comdata simmove(double x, double v, long a, long truckstate, long truckrank){
		comdata data=new comdata();
		data.x=x+v*0.1+1/2*a*0.1*0.1;
		data.v=v+a*0.1;
		data.a=a;
		data.truckstate=truckstate;
		data.truckrank=truckrank;
		return(data);
	}
}