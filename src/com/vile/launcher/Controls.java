package com.vile.launcher;

/**
 * Title: Controls
 * @author Alex Byrd
 * Date updated: 7/26/2016
 *
 * Description:
 * Sets up a new menu, but of ID of 2 so that it will be the controls
 * menu, not the normal menu.
 */
public class Controls extends FPSLauncher
{
	private static final long serialVersionUID = 1L;

   /**
    * Instantiates this new menu
    */
	public Controls() 
	{
		super(2);
		setTitle("Controls:");
		super.drawControlsButtons();
		super.drawBackground();
	}

}
