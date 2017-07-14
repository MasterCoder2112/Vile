package com.vile.entities;

/**
 * Title: Button
 * @author Alex Byrd
 * Date Updated: 8/15/2016
 * 
 * Description:
 * The Button Entity which can not be seen, but has a position that 
 * correlates to where a button is. Right now there is only one type
 * of button, and that is a button that ends the level and moves on to 
 * the next one (if there is one). 
 */
public class Button extends Entity 
{	
	public boolean pressed = false;
	
	public Button(double x, double y, double z, int itemID) 
	{
		super(0, 0, 0, 0, 0, x, y, z, 0, 0);
		
		//If itemID is 9, its the end button
		if(itemID == 9)
		{
			ID = 0;
		}
		//If itemID is 53, the button activates either enemy spawnpoints
		//and or the Communication Satillite.
		else if(itemID == 53)
		{
			ID = 1;
		}
	}

}
