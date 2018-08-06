package springsCKK;

import java.util.ArrayList;

public class Spring {

	double mass;
	double length;
	double k;
	int parts;
	double lengthbetween;
	double amplitude;
	double period;

	
	public Spring (double mass, double length, double k, int parts){
		this.mass = mass;
		this.length = length;
		this.k = k;
		this.parts = parts;
		this.lengthbetween = length/ (2*parts) ;
	}
	
	public Spring (){
		
	}
	
	
}
