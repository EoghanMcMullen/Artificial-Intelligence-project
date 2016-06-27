
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
		Point dbPos = new Point();
		Point ginyuPos = new Point();
		int dbCollected,friezaHealth,ginyuHealth;
		

		int i;

		ginyuPos.x = Integer.parseInt(x[0]);
		ginyuPos.y = Integer.parseInt(x[1]);
		gokuPos.x = Integer.parseInt(x[2]);
		gokuPos.y = Integer.parseInt(x[3]);
		dbPos.x = Integer.parseInt(x[4]);
		dbPos.y = Integer.parseInt(x[5]);
		ginyuPos.x = Integer.parseInt(x[6]);
		ginyuPos.y = Integer.parseInt(x[7]);
		dbCollected = Integer.parseInt(x[8]);
		friezaHealth = Integer.parseInt(x[9]);
		ginyuHealth = Integer.parseInt(x[10]);

		

		//========================================
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
		
		//===========================================
		//Make Goku head for the dragonball
		//===========================================
		int j;
		if(dbCollected == 0)
		{
			int disDbX = gokuPos.x - dbPos.x;
			int disDbY = gokuPos.y - dbPos.y;

			int disDb = Math.abs(disDbX) + Math.abs(disDbY);
		
			//check horizontal
			if(Math.abs(disDbX) > Math.abs(disDbY))
			{
				//then move in x direction
				if(disDbX < 0)
				{
					j = myWorld.ACTION_RIGHT;
				}
				else j = myWorld.ACTION_LEFT;
			}
			//else vertical movement
			else
			{
				if(disDbY < 0)
				{
					j = myWorld.ACTION_DOWN;
				}
				else
				{
					j = myWorld.ACTION_UP;
				}
			}
		}
		//when Goku collects the 3 dragonballs 
		//Goku will chase the bad guys
		else
		{
			
			//get distance from goku to frieza			
			int disFX = gokuPos.x - friezaPos.x;
			int disFY = gokuPos.y - friezaPos.y;
			int disF = Math.abs(disFX) + Math.abs(disFY);

			//get distance from goku to Ginyu
			int disGX = gokuPos.x - ginyuPos.x;
			int disGY = gokuPos.y - ginyuPos.y;
			int disG = Math.abs(disGX) + Math.abs(disGY);

			//check which bad guy is closest
			//If frieza is closer go for him
			//if ginyu is dead go for frieza
			if(disF <= disG || ginyuHealth < 1)
			{
				//check horizontal
				if(Math.abs(disFX) > Math.abs(disFY))
				{
					//then move in x direction
					if(disFX < 0)
					{
						j = myWorld.ACTION_RIGHT;
					}
					else j = myWorld.ACTION_LEFT;
				}
				//else vertical movement
				else
				{
					if(disFY < 0)
					{
						j = myWorld.ACTION_DOWN;
					}
					else
					{
						j = myWorld.ACTION_UP;
					}
				}
			}
			//else go for ginyu
			else
			{
				//check horizontal
				if(Math.abs(disGX) > Math.abs(disGY))
				{
					//then move in x direction
					if(disGX < 0)
					{
						j = myWorld.ACTION_RIGHT;
					}
					else j = myWorld.ACTION_LEFT;
				}
				//else vertical movement
				else
				{
					if(disGY < 0)
					{
						j = myWorld.ACTION_DOWN;
					}
					else
					{
						j = myWorld.ACTION_UP;
					}
				}

			}
			


		}
		//========================================
		//Make captain Ginyu chase goku
		//========================================
		int k;		

		//get distance from ginyu to goku
		int disGokuX2 = ginyuPos.x - gokuPos.x;
		int disGokuY2 = ginyuPos.y - gokuPos.y;

		int disGoku2 = Math.abs(disGokuX2) + Math.abs(disGokuY2);
	
		//check horizontal
		if(Math.abs(disGokuX2) > Math.abs(disGokuY2))
		{
			//then move in x direction
			if(disGokuX2 < 0)
			{
				k = myWorld.ACTION_RIGHT;
			}
			else k = myWorld.ACTION_LEFT;
		}
		//else vertical movement
		else
		{
			if(disGokuY2 < 0)
			{
				k = myWorld.ACTION_DOWN;
			}
			else
			{
				k = myWorld.ACTION_UP;
			}
		}
		
		String a = String.format ("%d,%d,%d", i,j,k);

		return new Action (a);		 
	}
	
	



}




