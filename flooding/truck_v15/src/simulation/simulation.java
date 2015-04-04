package simulation;

import truck.comdata;

public class simulation {
	public simulation(){
		
	}
	public comdata simmove(double x, double v, long a, long truckstate, long truckrank){
		comdata data=new comdata();
		data.x=x+v*control.control.timeperiodflag+1/2*a*control.control.timeperiodflag*control.control.timeperiodflag;
		data.v=v+a*control.control.timeperiodflag;
		data.a=a;
		data.truckstate=truckstate;
		data.truckrank=truckrank;
		return(data);
	}
}