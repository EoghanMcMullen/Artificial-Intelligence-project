import java.awt.image.*;
import java.io.*;
import java.util.*;
import javax.imageio.*; 
import java.awt.Point;
import org.w2mind.net.*;
import java.io.Serializable;

public class myWorld extends AbstractWorld 
{
	//this will be 16*16
	//yields 256 squares
	public static final int GRID_SIZE = 16;
	protected Vector<Point> freeCells;
	protected Vector<Point> walledCells = new Vector<Point>();	

	Point coppos, robpos;// positions of cop and robber, squares numbered from 0 to GRID_SIZE - 1
	int MAX_STEPS = 20;		// number of steps in a run
	int nocaught;			// primary score field = number of times robber has been caught in this run
	int nocaughtbyme;		// secondary score field = number of times robber caught due to my action
	List <String> scoreCols;	// Headers for the score fields

	int timestep;		


	// actions (public static since used by Mind):
	// custom for 2d
	public  static final int ACTION_LEFT	= 0;
	public  static final int ACTION_RIGHT	= 1;
	public  static final int ACTION_UP	= 2;
	public  static final int ACTION_DOWN	= 3;
	public  static final int STAY_STILL	= 4;
	public  static final int NO_ACTIONS	= 5;
	public  static final int ACTION_RAND    = 6;


	//images used

 	String SUPPORT_DIR 	= "images";// support files 
	String IMG_COP 		= SUPPORT_DIR + "/frieza.png";
 	String IMG_ROB 		= SUPPORT_DIR + "/gokuSmall.png";
 	String IMG_CAUGHT 	= SUPPORT_DIR + "/cloud2.png";
	String IMG_BG 		= SUPPORT_DIR + "/bg.jpg";

	
	// transient - don't serialise these:

	private transient      ArrayList <BufferedImage> buf;

	private transient InputStream copStream = null, robStream = null, caughtStream = null,bgStream = null;		
    	private transient BufferedImage copImg, robImg, caughtImg,bgImg; 

	//stores images width + height

	int imgwidth, imgheight;

	//---------------------------------------
	//Return a random position on the grid
	//--------------------------------------
	protected Point randomPosition()
	{
		Random r = new Random();
		return new Point(r.nextInt(GRID_SIZE),r.nextInt(GRID_SIZE));
	}
	//-----------------------------------------
	//Give all characters random board positions
	//-----------------------------------------

	private void initPos()	
	{
		coppos = randomPosition();  						
		do 
		{ 
			robpos = randomPosition(); 
		} 
		// repeat until different position
		while(coppos == robpos);
	}

	//----------------------------------------------
	//return with a random move from 0 - (NO_ACTIONS -1)
	//----------------------------------------------
	private int randomAction()				 
	{
	 	Random r = new Random(); 
	 	return ( r.nextInt( NO_ACTIONS ) );
	}


	
	// Move in direction supplied
	private void move(Point startPos, int direction)
	{
		if(direction == ACTION_LEFT)	startPos.x=((startPos.x - 1 + GRID_SIZE) % GRID_SIZE);		 
		if(direction == ACTION_RIGHT)	startPos.x=((startPos.x + 1 + GRID_SIZE) % GRID_SIZE);		 
		if(direction == ACTION_UP)	startPos.y=((startPos.y - 1 + GRID_SIZE) % GRID_SIZE);		 
		if(direction == ACTION_DOWN)	startPos.y=((startPos.y + 1 + GRID_SIZE) % GRID_SIZE);	
		if(direction == ACTION_RAND)    move(startPos, randomAction());	 
	}

	//-------------------------------
	//check if the game has ended
	//-------------------------------
	private boolean runFinished()
	{ 
		return ( timestep >= MAX_STEPS ); 
	}

//==========================================================
//add the points where walls will be, add to vector
//==========================================================

	private void genWalls()
	{

		//-------------------------------
		//Outer walls
		
		//left wall
		for(int i = 0;i < GRID_SIZE;i++)
		{
			//leaving opening where appropriate
			if(i == 7 || i == 8)
			{
				//do nothing
			}
			else
			{
				Point p = new Point(0,i);
				walledCells.addElement(p);
			}
		}
		//top wall
		for(int i = 0;i < GRID_SIZE;i++)
		{
			Point p = new Point(i,0);
			walledCells.addElement(p);
		}
		
		//right wall
		for(int i = 0;i < GRID_SIZE;i++)
		{
			//leave appropriate openings
			if(i == 7 || i == 8)
			{
				//do nothing
			}
			else
			{
				Point p = new Point(GRID_SIZE - 1,i);
				walledCells.addElement(p);
			}
		}
		// bottom wall
		for(int i = 0;i < GRID_SIZE;i++)
		{
			Point p = new Point(i,GRID_SIZE -1);
			walledCells.addElement(p);
		}
		//--------------------------------
		
		
		//==================================
		//Left Outer Grid
		//==================================

		//left top single
		Point p1 = new Point(2,2);
		walledCells.addElement(p1);
	
		//left top square
		for(int i = 1; i<=3; i++)
		{
			for(int j = 4;j <= 6;j++)
			{
				Point p = new Point(i,j);
				walledCells.addElement(p);
			}
		}

		//left bottom square
		for(int i = 1; i<=3; i++)
		{
			for(int j = 9;j <= 11;j++)
			{
				Point p = new Point(i,j);
				walledCells.addElement(p);
			}
		}
		
		//left bottom single
		Point p3 = new Point(2,13);
		walledCells.addElement(p3);

		//================================
		//Left inner Grid
		//================================
		
		for(int i = 2;i <= 13;i++)
		{
			if(i == 7||i == 8||i == 12)
			{
				//do nothing
			}
			else
			{	
				Point p = new Point(5,i);
				walledCells.addElement(p);
			}
			
		}
		
		for(int i = 9; i<= 11;i++)
		{
			Point p = new Point(6,i);
			walledCells.addElement(p);
		}
		
		for(int i = 2;i<= 6; i++)
		{
			if(i == 4)
			{
				//do nothing
			}
			else
			{
				Point p = new Point(7,i);
				walledCells.addElement(p);
			}
		}

		//=====================================
		// Right inner grid
		//=====================================
		
		for(int i = 8;i <= 10; i = i + 2)
		{
			for(int j = 2; j <= 13; j++)
			{
				if(j == 4 || j == 7 || j == 8 || (j == 10 && i == 10)|| (j == 12 && i == 10))
				{
					//do nothing
				}
				else
				{
					Point p = new Point(i,j);
					walledCells.addElement(p);
				}
			}
		}
		//=====================================
		//Right outer grid
		//=====================================
		
		//top right single
		Point p4 = new Point(13,2);
		walledCells.addElement(p4);
		
		//top right square
		
		for(int i = 12; i <= 14; i++)
		{
			for(int j = 4 ; j <= 6; j++)
			{
				
				
				Point p = new Point(i,j);
				walledCells.addElement(p);	
				
			}		

		}
		
		//Bottom Right Square
		for(int i = 12; i <= 14; i++)
		{
			for(int j = 9 ; j <= 11; j++)
			{
			
				
				Point p = new Point(i,j);
				walledCells.addElement(p);	
				
			}		

		}
		//Bottom right single
		Point p5 = new Point(13,13);
		walledCells.addElement(p5);
		
	}









	//----------------------------------
	//Initialize the images
	//---------------------------------

	// sets up new buffer to hold images  
	private void initImages()
	{
		if(imagesDesired) 
		{
			// buffer is cleared for each timestep, multiple images per timestep
	  		buf = new ArrayList <BufferedImage> ();			

			// block is only executed once (only read from disk once)
	  		if(copStream == null) 					
	  		{
	   			try
	   			{	// use memory, not disk, for temporary images
					ImageIO.setUseCache(false);		
					// read from disk
					copStream = getClass().getResourceAsStream(IMG_COP);		 
					robStream = getClass().getResourceAsStream(IMG_ROB);
					caughtStream = getClass().getResourceAsStream (IMG_CAUGHT);
					bgStream = getClass().getResourceAsStream(IMG_BG);
					
		
				    	copImg = javax.imageio.ImageIO.read(copStream);
				    	robImg = javax.imageio.ImageIO.read(robStream);
					caughtImg = javax.imageio.ImageIO.read(caughtStream);
					bgImg = javax.imageio.ImageIO.read(bgStream);

					// dimensions of jpg covering one square of the grid
					imgwidth  = copImg.getWidth();		
					imgheight = copImg.getHeight();
	   			}
	   			catch(IOException e){}
	  		}
	 	}
	}

	//-----------------------------------
	//add images to the buffer
	//------------------------------------
	private void addImage()	
	{
		//add walls to the wall vector
		//genWalls();

		if (imagesDesired) 
		{
			BufferedImage img = new BufferedImage ((imgwidth*GRID_SIZE),(imgheight*GRID_SIZE),BufferedImage.TYPE_INT_RGB);

			// Draws background image and doesn't change it
			img.createGraphics().drawImage(bgImg,0,0,null);

			//=============================================
			//Add the maze
			//=============================================
			for(int i = 0; i < GRID_SIZE;i++)
			{
				for(int j = 0; j < GRID_SIZE;j++)
				{
					Point p = new Point(i,j);
					if(walledCells.contains(p))
					{
						
						img.createGraphics().drawImage ( caughtImg, (imgwidth * i), (imgheight * j), null );
					}
				}
			}
	
		
			//=============================================
			if (coppos == robpos)
				img.createGraphics().drawImage ( caughtImg, (imgwidth * coppos.x), (imgheight * coppos.y), null );
			else
			{
				img.createGraphics().drawImage ( copImg, (imgwidth * coppos.x), (imgheight * coppos.y), null );
				img.createGraphics().drawImage ( robImg, (imgwidth * robpos.x),(imgheight * robpos.y), null );
			}
			buf.add(img);	
		}
	}	
	  
	//====== World must respond to these methods: ==========================================================
	//  newrun(), endrun()
	//  getstate(), takeaction()
	//  getscore() 
	//======================================================================================================

	//--------------------------------
	//Initializes a new run of the world
	//--------------------------------

	public void newrun() throws RunError
	{
		
		//Create Points to store position of cops + robbers
		coppos=new Point();
		robpos=new Point();

		//Reset all values
		timestep = 0;
		nocaught = 0;
		nocaughtbyme = 0;
	
		//use initializer
		initPos();
		genWalls();

		//Headers for score fields
		scoreCols = new LinkedList<String>();		
		scoreCols.add("Caught");
		scoreCols.add("Caught_by_me");
	}



	public void endrun() throws RunError
	{
	}





	//====== Definition of state: ===========================================================================
	// Constructs a string to describe the curent world state
	//======================================================================================================



	public State getstate() throws RunError
	{

	  String x = String.format ("%d,%d,%d,%d", coppos.x,coppos.y,robpos.x,robpos.y);

	  return new State (x);
	}
	
	//=========================================================================================================
	//Mind.takeaction() constructs a string to describe the action. 
	//=========================================================================================================
	public State takeaction (Action action) throws RunError 
	{ 
		// If run with images off, imagesDesired = false and this does nothing.
		initImages();	
		addImage();

		//parse the action
		String s = action.toString();
		// parsed into a[0], a[1], ...
		String[] a = s.split(",");			  

		// ignore any other fields
		int i = Integer.parseInt(a[0]);		

		// take the action
		move(coppos,i);

		//intermidiate image
		addImage();			

		if(coppos == robpos)  
		{
			nocaught++;
			// caught due to our action, not robber's action
			nocaughtbyme++;	
			initPos();		 
		}
		// move the robber 
		else			 
		{
			move(robpos,randomAction());

			if (coppos == robpos) 
			{
				addImage();
				// caught due to robber's action
				nocaught++;		
				initPos();
			}
		}

		timestep++;

		//there will be no loop around if run is finished so print image
		if(runFinished())
			addImage();

		return getstate();
	}
	



	//==========================================================================================================================
	// get game score
	//==========================================================================================================================


	 
	public Score getscore() throws RunError
	{

		String s = String.format ("%d,%d", nocaught, nocaughtbyme);

		// Setting finished = true will end the run.
		// N.B. This is the only way the World has to tell the underlying w2m system to stop the run.

		boolean finished = ( timestep >= MAX_STEPS );
		 
		List <Comparable> values = new LinkedList <Comparable> ();
		values.add(nocaught);
		values.add(nocaughtbyme);

	 	return new Score(s,finished,scoreCols,values);
	}

	//return the images of the world	
	public ArrayList<BufferedImage> getimage() throws RunError
	{
		return buf;
	}
}


