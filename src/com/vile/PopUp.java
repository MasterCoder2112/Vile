package com.vile;

/**
 * Title: PopUp
 * @author Alexander Byrd
 * Date Created: September 28, 2017
 * 
 * Description:
 * Creates a PopUp text object which keeps track of it's y on the screen
 * and the time it has been up so it will disappear after it has been shown
 * long enough. And also floats down the screen as it ticks on.
 * 
 * Only yOnScreen has a setter to keep popups from bundling up
 */
public class PopUp 
{
	private int tick = 0;
	private int yOnScreen = 50;
	private int timeToStay = 250;
	private int yChange = 1;
	public String text = "";
	
	//Constructs the popup with a set time to stay up
	//and whether it moves or not
	public PopUp(String textToDisplay, int timeToStay, int yChange) 
	{
		this.text = textToDisplay;
		this.timeToStay = timeToStay;
		this.yChange = yChange;
	}
	
	public PopUp(String textToDisplay) 
	{
		this.text = textToDisplay;
		
	}
	
   /**
    * Tick the events of this object
    */
	public void tick()
	{
		tick++;
		yOnScreen += yChange;
		
		//Remove from screen after a variable amount of ticks.
		//If timeToStay is -1 it stays on screen.
		if((tick > timeToStay || yOnScreen > 300) && timeToStay != -1)
		{
			Display.messages.remove(this);
		}
	}
	
   /**
    * Returns y on screen
    * @return
    */
	public int getYOnScreen()
	{
		return yOnScreen;
	}
	
   /**
    * Returns y on screen
    * @return
    */
	public void setYOnScreen(int newY)
	{
		this.yOnScreen = newY; 
	}
	
   /**
    * Returns tick Count it is on
    * @return
    */
	public int getTick()
	{
		return tick;
	}
	
   /**
    * Returns text inside the popup
    * @return
    */
	public String getText()
	{
		return text;
	}
}
