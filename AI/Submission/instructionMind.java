
// a Mind for ImageWorld


import java.util.*;
import java.awt.Point;
import org.w2mind.net.*;

public class PacManDBZmind  implements Mind 
{


//====== Mind must respond to these methods: ==========================================================
//  newrun(), endrun()
//  getaction()
//======================================================================================================



	public void newrun()  throws RunError 
	{
	}


	public void endrun()  throws RunError
	{
	}



	public Action getaction ( State state )
	{ 
		//split the string up so we can access the elements
		String s = state.toString();		 
		String[] x = s.split(",");			   

		//create variables to hold information from the state
		Point friezaPos = new Point();
		Point gokuPos = new Point();
		Point dbPos = new Point();
		Point ginyuPos = new Point();
		Point cellPos = new Point();
		int dbCollected,friezaHealth,ginyuHealth,cellHealth;
		
		//these integers will hold the return values we desire
		int i,j,k,m;

		//read values from the state into associated variables
		// use these variables to decide which way you would like 
		// the characters to move
		friezaPos.x = Integer.parseInt(x[0]);
		friezaPos.y = Integer.parseInt(x[1]);
		gokuPos.x = Integer.parseInt(x[2]);
		gokuPos.y = Integer.parseInt(x[3]);
		dbPos.x = Integer.parseInt(x[4]);
		dbPos.y = Integer.parseInt(x[5]);
		ginyuPos.x = Integer.parseInt(x[6]);
		ginyuPos.y = Integer.parseInt(x[7]);
		dbCollected = Integer.parseInt(x[8]);
		friezaHealth = Integer.parseInt(x[9]);
		ginyuHealth = Integer.parseInt(x[10]);
		cellPos.x = Integer.parseInt(x[11]);
		cellPos.y = Integer.parseInt(x[12]);
		cellHealth = Integer.parseInt(x[13]);

		

		//add the return values
		// i represents frieza's move
		// j represents goku's move
		// k represents ginyu's move
		// m represents cell's move

		//a return value can be any of these:
		//ACTION_RIGHT
	        //ACTION_UP	
	        //ACTION_DOWN	
	        //STAY_STILL	
	        //NO_ACTIONS	
	        //ACTION_RAND	

		//Note: if the move you try and make isn't valid the world will choose a random move.

		String a = String.format ("%d,%d,%d,%d", i,j,k,m);

		return new Action (a);	
		 
	}
	
	



}




