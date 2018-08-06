package springsCKK;

import org.opensourcephysics.display.Circle;

public class Particle extends Circle{

	public double x; //x-position
	public double y; //y-position
	public double Vx;  //Vx velocity vector
	public double Vy; //Vy velocity vector
	public double Ax;
	public double Ay;
	public double mass;
	
	/**
	 * Constructor with x, y, mass
	 */
	public Particle(double x, double y, double mass) {
		super(); //parent class
		this.x = x;
		this.y = y;
		this.mass = mass;
		this.setXY(x,y);
	}
	
	public Particle(){
		
	}
	
	/**
	 * Sets this planet's acceleration in the x-direction for a given sum of the forces in the x-direction 
	 * @param sumofforces	sum of the forces in the x-direction
	 */
	public void newAx(double sumforces){
		this.Ax = sumforces / this.mass; // f = ma, a = f/m
	}

	/**
	 * Sets this planet's acceleration in the y-direction for a given sum of the forces in the x-direction 
	 * @param sumofforces	sum of the forces in the y-direction
	 */
	public void newAy(double sumforces){
		this.Ay = sumforces / this.mass; // f = ma, a = f/m
	}
	
	
	/**
	 * Sets a new velocity in the X direction
	 * @param ax		acceleration in the x direction
	 * @param timestep	timestep
	 */
	public void newVX(double ax, double timestep){
		this.Vx = this.Vx + (ax*timestep); // v = vo + at
	}


	/**
	 * Sets a new velocity in the Y direction
	 * @param ax		acceleration in the y direction
	 * @param timestep	timestep
	 */
	public void newVY(double ay, double timestep){
		this.Vy = this.Vy + (ay*timestep); // v = vo + at
	}

	/**
	 * Sets a new x-position
	 * @param vx		velocity in the x-direction
	 * @param timestep	timestep
	 */
	public void newPositionX(double vx, double timestep){
		this.x = this.x + (vx*timestep); //x = xo + vt
		this.setXY(this.x, this.y);
	}

	/**
	 * Sets a new y-position
	 * @param vy		velocity in the y-direction
	 * @param timestep	timestep
	 */
	public void newPositionY(double vy, double timestep){
		this.y = this.y + vy*timestep; //y = yo + vt
		this.setXY(this.x, this.y);
	}	
	
}
