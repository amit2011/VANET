package simulation;

import truck.comdata;

public class simulation {
	public simulation(){
		
	}
	public comdata simmove(long x, long v, long a){
		comdata data=new comdata();
		data.x=x+5;
		data.v=v+1;
		data.a=a;
		return(data);
	}
}
