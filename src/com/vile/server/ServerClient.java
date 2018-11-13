package com.vile.server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

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

	// Starts new client up which connects to the host
	public ServerClient(String hostInfo) {
		String[] hostData = hostInfo.split(":");

		try {
			Socket hostSocket = new Socket(hostData[0], Integer.parseInt(hostData[1]));
			PrintWriter out = new PrintWriter(hostSocket.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(hostSocket.getInputStream()));

			// TODO Somehow start a game from here, and output new information every tick to
			// the server
			// and wait for a response to update the local game information.

			// Make sure to get information from the host first so that it sets up a game as
			// the game
			// is on the hosts side currently.

		} catch (Exception e) {
			// TODO Send client back to main menu saying connection failed.
		}
	}

}
