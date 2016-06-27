
// a Mind for ImageWorld


import java.util.*;
import java.awt.Point;
import org.w2mind.net.*;

public class myMind  implements Mind 
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
		String s = state.toString();		 
		String[] x = s.split(",");			   

		Point friezaPos = new Point();
		Point gokuPos = new Point();

		int i;

		friezaPos.x = Integer.parseInt(x[0]);
		friezaPos.y = Integer.parseInt(x[1]);
		gokuPos.x = Integer.parseInt(x[2]);
		gokuPos.y = Integer.parseInt(x[3]);

		//get distance from frieza to goku
		int disGokuX = friezaPos.x - gokuPos.x;
		int disGokuY = friezaPos.y - gokuPos.y;

		int disGoku = Math.abs(disGokuX) + Math.abs(disGokuY);
		
		//check horizontal
		if(Math.abs(disGokuX) > Math.abs(disGokuY))
		{
			//then move in x direction
			if(disGokuX < 0)
			{
				i = myWorld.ACTION_RIGHT;
			}
			else i = myWorld.ACTION_LEFT;
		}
		//else vertical movement
		else
		{
			if(disGokuY < 0)
			{
				i = myWorld.ACTION_DOWN;
			}
			else
			{
				i = myWorld.ACTION_UP;
			}
		}



	

		//if (gokuPos.x < friezaPos.x) 
		//i = myWorld.ACTION_UP;		
	
		//else i = myWorld.ACTION_LEFT;

		String a = String.format ("%d", i);

		return new Action (a);		 
	}



}




