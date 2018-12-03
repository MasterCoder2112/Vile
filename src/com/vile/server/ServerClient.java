package com.vile.server;

import java.net.Socket;

import com.vile.Display;
import com.vile.RunGame;
import com.vile.launcher.FPSLauncher;

/**
 * Title: ServerClient
 * 
 * @author Alexander Byrd
 * 
 *         Data Created: 11/13/2018
 * 
 *         Description: Starts up a new server client that will connect to a
 *         host given the information sent in that the user has input. It will
 *         then start up a new local game given the information from the host,
 *         and send any updated information every tick to the host.
 *
 */
public class ServerClient {

	public static Socket hostSocket;
	public static String[] hostData;

	// Starts new client up which connects to the host
	public ServerClient(String hostInfo) {
		hostData = hostInfo.split(":");

		try {
			Display.gameType = 1;
			// BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));

			// Display clientGame = new Display();

			// TODO Change if there is an issue or in the future something changes
			// Host game does not render a screen.
			// if (Display.gameType != 0) {
			/*
			 * Sets up image of cursor, its width and height, and it's type of
			 * BufferedImage.
			 */
			// BufferedImage cursor = new BufferedImage(16, 16,
			// BufferedImage.TYPE_INT_ARGB);

			/*
			 * Sets up a new cursor of custom type to be used. This cursor is blank, meaning
			 * it is completely see through and won't be seen in the way of the graphics
			 * when playing the game.
			 */
			// Cursor blank = Toolkit.getDefaultToolkit().createCustomCursor(cursor, new
			// Point(0, 0), "blank");

			/*
			 * Self explanitory stuff
			 */
			/*
			 * frame = new JFrame(Display.TITLE); frame.add(clientGame);
			 * frame.setSize(Display.WIDTH, Display.HEIGHT);
			 * frame.getContentPane().setCursor(blank);
			 * frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			 * frame.setResizable(false); frame.setLocationRelativeTo(null);
			 * 
			 * // If full screen if (FPSLauncher.resolutionChoice >= 4) {
			 * frame.setExtendedState(JFrame.MAXIMIZED_BOTH); frame.setUndecorated(true); }
			 * 
			 * ImageIcon titleIcon = new ImageIcon( "resources" + FPSLauncher.themeName +
			 * "/textures/hud/titleIcon.png"); frame.setIconImage(titleIcon.getImage());
			 * 
			 * frame.setVisible(true); frame.setAlwaysOnTop(true); }
			 * 
			 * Display.isRunning = true;
			 * 
			 * RunGame.frame = frame;
			 */

			new RunGame();

			// hostSocket.setKeepAlive(true);
			// hostSocket.setReuseAddress(true);
			// hostSocket.setSoLinger(true, 10000000);

			/*
			 * String fromServer = ""; String fromUser = "";
			 * 
			 * while ((fromServer = in.readLine()) != null) { System.out.println("Server: "
			 * + fromServer); if (fromServer.equals("Bye.")) break;
			 * 
			 * // clientGame.multiRun();
			 * 
			 * fromUser = "keep going"; if (fromUser != null) {
			 * System.out.println("Client: " + fromUser); out.println(fromUser); } }
			 */

			/*
			 * Display.itemsRespawn = true; Display.gameType = 2;
			 * 
			 * new RunGame();
			 * 
			 * System.out.println("Connected");
			 */
		} catch (Exception e) {
			// TODO Send client back to main menu saying connection failed.
			System.out.println(e);
			new FPSLauncher(0);
		}
	}

}
