package simulation;

import truck.Comdata;

public class simulation {
	public simulation(){
		
	}
	public Comdata simmove(Comdata xva){
		Comdata data=new Comdata();
		data=xva;
		data.x=xva.x+xva.v*control.control.timeperiodflag+1/2*xva.a*control.control.timeperiodflag*control.control.timeperiodflag;
		data.v=xva.v+xva.a*control.control.timeperiodflag;
		return(data);
	}
}