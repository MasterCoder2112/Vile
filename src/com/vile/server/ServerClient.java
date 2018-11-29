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

	// Starts new client up which connects to the host
	public ServerClient(String hostInfo) {
		String[] hostData = hostInfo.split(":");

		try {
			hostSocket = new Socket(hostData[0], Integer.parseInt(hostData[1]));

			Display.itemsRespawn = true;
			Display.gameType = 2;

			new RunGame();

			System.out.println("Connected");
		} catch (Exception e) {
			// TODO Send client back to main menu saying connection failed.
			new FPSLauncher(0);
		}
	}

}
