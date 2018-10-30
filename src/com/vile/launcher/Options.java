package com.vile.launcher;

/**
 * Title: Options
 * @author Alex Byrd
 * Date Updated: 7/26/2016
 * 
 * Description:
 * Creates a new menu, but with ID of 1 so that it creates the options
 * menu instead of the normal main menu.
 *
 */
public class Options extends FPSLauncher
{
	private static final long serialVersionUID = 1L;

   /**
    * Instantiates the new menu
    */
	public Options() 
	{
		super(1);
		setTitle("Options:");
		super.drawOptionsMenu();
		super.drawBackground();
		
		//Fixes bug where sometimes this loses focus
		super.newMapName.setEditable(true);
	}
}
