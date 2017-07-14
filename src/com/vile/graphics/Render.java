package com.vile.graphics;

import com.vile.Display;

/**
 * Title: Render
 * @author Alex Byrd
 * Date Updated: Long ago
 * 
 * Description:
 * Renders the pixels in 2D on the screen.
 */
public class Render
{
	public final int WIDTH;
	public final int HEIGHT;
	public int[] PIXELS;
	
	public Render (int width, int height)
	{
		this.WIDTH  = width;
		this.HEIGHT = height;
		
		//How many pixels are in a given frame at a time
		PIXELS = new int[width * height];
	}
	
   /**
    * Uses a given Render sent in, with OffSets (Usually 0) that is used
    * to draw onto the screen. This basically sets up this Render Objects
    * Pixels to be equal to the Render objects pixels sent in, with the
    * offsets applied. 
    * 
    * @param render
    * @param xOffSet
    * @param yOffSet
    */
	public static void draw(Render render, int xOffSet, int yOffSet)
	{	
		int[] temp = new int[render.PIXELS.length];
		
		for(int y = 0; y < render.HEIGHT; y++)
		{
			int yPix = y + yOffSet;
		    
		   /*
		    * NOTE: Can use Display.HEIGHT because HEIGHT is static and therefore
		    * is part of the object Display, therefore you do not need to create
		    * a display object to use it.
		    */
			if(yPix < 0 || yPix >= Display.HEIGHT)
			{
				continue;
			}
			
			for(int x = 0; x < render.WIDTH; x++)
			{
				int xPix = x + xOffSet;
				
				if(xPix < 0 || xPix >= Display.WIDTH)
				{
					continue;
				}
				
				int alpha = render.PIXELS[x + (y * render.WIDTH)];
				
				//If there is something to render, then render it
				//if(alpha > 0)
				//{
				temp[xPix + (yPix * render.WIDTH)] = alpha;
				//}
			}
		}
		
		render.PIXELS = new int[temp.length];
		
		for(int i = 0; i < temp.length; i++)
		{
			render.PIXELS[i] = temp[i];
		}
	}

}
