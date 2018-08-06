package springsCKK;

import java.awt.Color;
import java.util.ArrayList;
import org.opensourcephysics.controls.AbstractSimulation;
import org.opensourcephysics.controls.SimulationControl;
import org.opensourcephysics.frames.PlotFrame;

/**
 * 
 * This program simulates the motion of a person falling with a bungee cord from 100m.
 * This program uses the physics of spring motion and gravity to model the motion of the person and the cord in one dimension (the y-dimension)
 * The bungee cord in this program is made up of a variable number of smaller parts. 
 * 
 * @authors Claire Keenan-Kurgan and Lily Steinman
 *
 */

public class Bungee extends AbstractSimulation {

	PlotFrame frame = new PlotFrame("X", "Y", "Bungee Cord");
	ArrayList<Particle> masses = new ArrayList<Particle>(); //array of masses that is displayed on the screen
	ArrayList<Particle> pastMasses = new ArrayList<Particle>(); //duplicate array of masses used to calculate forces 
	Spring cord = new Spring(); //saves values about the bungee cord
	double timestep;

	//main
	public static void main(String[] args) {
		SimulationControl.createApp(new Bungee());
	}

	/**
	 * Runs the simulation of a spring bouncing
	 */
	protected void doStep() {

		for (int i = 0; i < masses.size(); i++) {
			pastMasses.get(i).y = masses.get(i).y; //copies the past string
		}

		for (int i = cord.parts - 1; i >= 1; i--) {
			masses.get(i).newAy(forcesY(i)); //changes acceleration based on sum of forces
			masses.get(i).newVY(masses.get(i).Ay, timestep); //changes velocity
			masses.get(i).newPositionY(masses.get(i).Vy, timestep); //changes position
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
	 * Calculates the total force on each individual particle in the spring
	 * @param spot -- which particle in the ArrayList that we are calculating the forces for
	 * @return net	the net force including upforce, downforce, and gravity
	 */
	public double forcesY(int spot){
		double net = 0;
		double deltaX1 = 0;
		double deltaX2 = 0;
		double spring_up = 0;
		double spring_down = 0;
		double grav =  9.8 * masses.get(spot).mass; // Fg = mg

		//if bottom mass, only spring force is upwards
		if(spot == masses.size()-1){
			deltaX1 = ((pastMasses.get(spot-1).y - pastMasses.get(spot).y) - cord.lengthbetween); //change in x
			spring_up = findK()*deltaX1; // Fsp = spring constant*change in x
		}
		
		//if top mass, only spring force is downwards
		else if(spot==0){
			deltaX2 = ((pastMasses.get(spot).y - pastMasses.get(spot+1).y) - cord.lengthbetween); //change in x
			spring_down = findK()*deltaX2; // Fsp = spring constant*change in x
		}
		
		//if in the middle, there is spring force in both directions
		else{
			deltaX1 = ((pastMasses.get(spot-1).y - pastMasses.get(spot).y) - cord.lengthbetween); //change in x above
			deltaX2 = ((pastMasses.get(spot).y - pastMasses.get(spot+1).y) - cord.lengthbetween); //change in x below
			spring_up = findK()*deltaX1; // Fsp = spring constant*change in x
			spring_down = findK()*deltaX2; // Fsp = spring constant*change in x
		}

		net = spring_up - (spring_down + grav); // net force
		return net;
	}

	/**
	 * Sets the initial state of the simulation
	 * 	Currently everything starts at height 100 and x-coordinate 0
	 */
	public void initialize(){ 
		
		this.delayTime = 0; //speeds up the dostep
		frame.setPreferredMinMax(-25, 25, 0, 110);
		
		timestep = control.getDouble("Timestep");//timestep
		
		//initialize cord using parameters from the control, purpose of cords is to save these values
		cord = new Spring(control.getDouble("Cord mass"), control.getDouble("Cord length"), control.getDouble("Cord K"), control.getInt("Number of parts in cord"));

		for (int i = 0; i < cord.parts; i++) {
			//masses.add(new Particle(0.0, 50.0 - i*cord.lengthbetween, cord.mass/cord.parts));
			masses.add(new Particle(0.0, 100.0, cord.mass/cord.parts)); //initialize each mass
			pastMasses.add(new Particle()); //add an empty particle in the duplicate pastMasses array
			masses.get(i).pixRadius = 3; //size
			frame.addDrawable(masses.get(i));

			masses.get(i).color = new Color (0+5*i, 0+5*i, 200);
			//masses.get(i).color = Color.gray; //color
		}

		//sets the particle that is a person (last particle)
		masses.get(masses.size()-1).color = Color.blue;
		masses.get(masses.size()-1).pixRadius = 5;
		masses.get(masses.size()-1).mass = control.getDouble("Person mass");

		frame.setVisible(true);

	}

	/**
	 * Resets values 
	 * Opens control panel 
	 */
	public void reset(){ 
		control.setValue("Cord length", 30);
		control.setValue("Cord mass", 15);
		control.setValue("Cord K", 300);
		control.setValue("Person mass", 50);
		control.setValue("Number of parts in cord", 50);
		control.setValue("Timestep", 0.004);
	}

}
