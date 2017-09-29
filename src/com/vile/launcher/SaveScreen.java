package com.vile.launcher;

public class SaveScreen extends FPSLauncher
{
	private static final long serialVersionUID = 1L;

	public SaveScreen() 
	{
		super(4);
		setTitle("Save and Load Game");
		super.saveLoadMenuSetup();
		super.drawBackground();
	}

}
