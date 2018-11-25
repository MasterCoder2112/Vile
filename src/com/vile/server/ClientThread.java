package com.vile.server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

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

	public ClientThread(Socket clientSocket) {
		this.clientSocket = clientSocket;
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

			System.out.println("Has client and is waiting...");

			// TODO Read next commented area
			/*
			 * Below is from the tutorial on the web. Modify it in such a way so that this
			 * thread waits for a response from the client (client will send positional data
			 * and any bullets that were added within the current tick to the server here.
			 * It will also send any items that it picked up so the server can remove those
			 * as well.) and then add that data to the server. Data is added to the server
			 * through the methods found in the ServerHost class. After the server hosts
			 * data has been updated, then the server will send the completely updated data
			 * back to all the clients so all new data is seen on all the clients games.
			 * Then this thread will loop and wait for the next response from the client.
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
