package com.vile.server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import com.vile.Game;
import com.vile.entities.ServerPlayer;

/**
 * Title: ClientThread
 * 
 * @author Alexander Byrd
 * 
 *         Date Created: 11/13/2018
 * 
 *         Description: Runs a client thread which accepts client responses, and
 *         then sends back a response of its own so that both server and client
 *         information are updated each tick.
 *
 */
public class ClientThread implements Runnable {

	Socket clientSocket;
	int clientID = 0;

	public ClientThread(Socket clientSocket) {
		this.clientSocket = clientSocket;
		ServerPlayer newPlayer = new ServerPlayer();
		Game.otherPlayers.add(newPlayer);
		clientID = newPlayer.ID;
	}

	/**
	 * Runs continuously. Accepting input from the client when it comes in to update
	 * the host game, and then all the data from the host game is sent back to each
	 * of the clients through this method, after all has been updated.
	 */
	@Override
	public void run() {
		try {

			PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

			System.out.println("Has client and is waiting... Client ID: " + clientID);

			// TODO Read next commented area
			/*
			 * For data transfer, make sure the client receives all the servers game
			 * information right off the bat so the map and everything can be loaded
			 * correctly. Also send the clients ID so that the player object can set it's
			 * ID. After having done that then have code that waits for the client to
			 * respond after he/she ticks and when the client does respond update the
			 * servers game and then send the game data back to the client. If the client is
			 * lagging, it will be like any other game where the client will just have to
			 * catch up with the server. Nothing we can do about this currently. Look at the
			 * Game classes loadGame() method and FPSLauncher classes saveGame code.
			 */

			// String inputLine, outputLine;

			// Initiate conversation with client
			// KnockKnockProtocol kkp = new KnockKnockProtocol();
			// outputLine = kkp.processInput(null);
			// out.println(outputLine);

			// while ((inputLine = in.readLine()) != null) {
			// outputLine = kkp.processInput(inputLine);
			// out.println(outputLine);
			// if (outputLine.equals("Bye."))
			// break;
			// }
		} catch (Exception e) {

		}
	}

}
