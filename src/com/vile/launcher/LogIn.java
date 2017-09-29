package com.vile.launcher;

public class LogIn extends FPSLauncher
{
	private static final long serialVersionUID = 1L;

	public LogIn() {
		super(3);
		setTitle("Log In");
		super.logInScreen();
		super.drawBackground();
	}
}
