package springsCKK;

import javax.sound.midi.*;
import java.awt.Color;
import java.util.ArrayList;
import org.opensourcephysics.controls.AbstractSimulation;
import org.opensourcephysics.controls.SimulationControl;
import org.opensourcephysics.display.TextBox;
import org.opensourcephysics.frames.PlotFrame;

public class OscillatingSpring extends AbstractSimulation {

	PlotFrame frame = new PlotFrame("X", "Y", "Oscillating Spring");
	ArrayList<Particle> masses = new ArrayList<Particle>(); //array of masses that is displayed on the screen
	ArrayList<Particle> pastMasses = new ArrayList<Particle>(); //duplicate array of masses used to calculate forces 
	Spring cord = new Spring(); //saves values about the bungee cord
	double timestep;
	double time_ellapsed = 0;
	TextBox text = new TextBox("Frequency");

	//main
	public static void main(String[] args) {
		SimulationControl.createApp(new OscillatingSpring());
	}

	/**
	 * Runs the simulation of a spring bouncing
	 */
	protected void doStep() {


		time_ellapsed+=timestep;

		frame.addDrawable(text);

		for (int i = 0; i < masses.size(); i++) {
			pastMasses.get(i).x = masses.get(i).x;
			pastMasses.get(i).y = masses.get(i).y; //copies the past string

		}

		//oscillating y
		masses.get(0).y = cord.amplitude * Math.sin( ((2*Math.PI)/ cord.period) * time_ellapsed );
		masses.get(0).setY(masses.get(0).y);

		//rest of the string
		for (int i = 1; i < cord.parts-1; i++) {	
			masses.get(i).newAy(forcesY(i)); //changes acceleration based on sum of forces
			masses.get(i).newVY(masses.get(i).Ay, timestep); //changes velocity
			masses.get(i).newPositionY(masses.get(i).Vy, timestep); //changes position
			//Normal
			masses.get(i).newAx(forcesX(i)); //changes acceleration based on sum of forces
			masses.get(i).newVX(masses.get(i).Ax, timestep); //changes velocity
			masses.get(i).newPositionX(masses.get(i).Vx, timestep); //changes position
			//			//No x movement
			//			masses.get(i).newAx(forcesX(i)); //changes acceleration based on sum of forces
			//			masses.get(i).newVX(masses.get(i).Ax, timestep); //changes velocity
			//			masses.get(i).newPositionX(masses.get(i).Vx, 0); //changes position
		}
	}




	/**
	 * Finds the spring constant of each individual spring based on the spring constant of the cord as a whole
	 * @return little k
	 */
	public double findK(){
		return cord.k*cord.parts; //little k = big k * number of parts
	}

	/**
	 * Distance formula
	 * @param mass1
	 * @param mass2
	 * @return
	 */
	public double distance(Particle mass1, Particle mass2){

		double x1 = mass1.x;
		double y1 = mass1.y;
		double x2 = mass2.x;
		double y2 = mass2.y;

		//distance formula
		double distance = Math.sqrt( Math.pow( (x2-x1) , 2) + Math.pow( (y2-y1) , 2) );

		return distance;
	}

	/**
	 * Angle 
	 * @param mass1
	 * @param mass2
	 * @return
	 */
	public double angle(Particle mass1, Particle mass2){

		double x1 = mass1.x;
		double y1 = mass1.y;
		double x2 = mass2.x;
		double y2 = mass2.y;

		double opposite = Math.abs(y2-y1);
		double adjacent = Math.abs(x2-x1);

		double angle = Math.atan(opposite/adjacent);





		return angle;
	}

	/**
	 * 
	 * @param mass1
	 * @param mass2
	 * @return
	 */
	public double forcesX(int spot){
		double mag_right;
		double angle_right;
		double component_right;
		if(spot != cord.parts-1){
			mag_right = findK() * (distance(pastMasses.get(spot), pastMasses.get(spot+1)) - cord.lengthbetween) ;
			angle_right = angle(pastMasses.get(spot), pastMasses.get(spot+1));
			component_right = mag_right * Math.cos(angle_right) * direction(pastMasses.get(spot).x, pastMasses.get(spot+1).x);
		}
		else {
			component_right = 0;
		}
		//
		//		double mag_right = findK() * (distance(pastMasses.get(spot), pastMasses.get(spot+1)) - cord.lengthbetween) ;
		//		double angle_right = angle(pastMasses.get(spot), pastMasses.get(spot+1));
		//		double component_right = mag_right * Math.cos(angle_right) * direction(pastMasses.get(spot).x, pastMasses.get(spot+1).x);


		double mag_left = findK() * (distance(pastMasses.get(spot-1), pastMasses.get(spot)) - cord.lengthbetween) ;
		double angle_left = angle(pastMasses.get(spot-1), pastMasses.get(spot));
		double component_left = mag_left * Math.cos(angle_left) * direction(pastMasses.get(spot-1).x, pastMasses.get(spot).x);

		return component_right - component_left;
	}

	/**
	 * 
	 * @param mass1
	 * @param mass2
	 * @return
	 */
	public double forcesY(int spot){

		double mag_right;
		double angle_right;
		double component_right;
		if(spot != cord.parts-1){
			mag_right = findK() * (distance(pastMasses.get(spot), pastMasses.get(spot+1)) - cord.lengthbetween) ;
			angle_right = angle(pastMasses.get(spot), pastMasses.get(spot+1));
			component_right = mag_right * Math.sin(angle_right) * direction(pastMasses.get(spot).y, pastMasses.get(spot+1).y);
		}
		else {
			component_right = 0;
		}

		//		double mag_right = findK() * (distance(pastMasses.get(spot), pastMasses.get(spot+1)) - cord.lengthbetween) ;
		//		double angle_right = angle(pastMasses.get(spot), pastMasses.get(spot+1));
		//		double component_right = mag_right * Math.sin(angle_right) * direction(pastMasses.get(spot).y, pastMasses.get(spot+1).y);

		double mag_left = findK() * (distance(pastMasses.get(spot-1), pastMasses.get(spot)) - cord.lengthbetween); 
		double angle_left = angle(pastMasses.get(spot-1), pastMasses.get(spot)) ;
		double component_left = mag_left * Math.sin(angle_left) * direction(pastMasses.get(spot-1).y, pastMasses.get(spot).y);

		return component_right - component_left;
	}

	/**
	 * 
	 * @param coor1
	 * @param coor2
	 * @return
	 */
	public double direction(double coor1, double coor2){

		if(coor2 < coor1){
			return -1;
		}
		else{
			return 1;
		}
	}


	/**
	 * Sets the initial state of the simulation
	 * 	Currently everything starts at height 100 and x-coordinate 0
	 */
	public void initialize(){ 

		this.delayTime = 0; //speeds up the dostep
		frame.setPreferredMinMax(-1, 2, -1, 1);

		timestep = control.getDouble("Timestep");//timestep

		//initialize cord using parameters from the control, purpose of cords is to save these values
		cord = new Spring(control.getDouble("Cord mass"), control.getDouble("Cord length"), control.getDouble("Cord K"), control.getInt("Number of parts in cord"));
		cord.amplitude = control.getDouble("Amplitude");
		cord.period = control.getDouble("Period");

		for (int i = 0; i < cord.parts; i++) {
			//masses.add(new Particle(0.0, 50.0 - i*cord.lengthbetween, cord.mass/cord.parts));
			masses.add(new Particle(i*(2*cord.lengthbetween), 0, cord.mass/cord.parts)); //initialize each mass
			pastMasses.add(new Particle()); //add an empty particle in the duplicate pastMasses array
			masses.get(i).pixRadius = 3; //size
			frame.addDrawable(masses.get(i));

			masses.get(i).color = new Color (0+9*i, 0+9*i, 200);
			//masses.get(i).color = Color.blue; //color
		}

		masses.get(12).color = Color.black;

		text.setXY(-.6, .6);
		text.setText("Frequency: " + 1 / cord.period);

		frame.setVisible(true);
	}

	/**
	 * Resets values 
	 * Opens control panel 
	 */
	public void reset(){ 

		control.setValue("Cord length", 1.5);
		control.setValue("Cord mass", 1.5);
		control.setValue("Cord K", 100);
		control.setValue("Number of parts in cord", 25);
		control.setValue("Timestep", 0.002);
		control.setValue("Amplitude",  .1);
		//control.setValue("Period", .3); //1 hump
		control.setValue("Period", .14);		
	}

}
