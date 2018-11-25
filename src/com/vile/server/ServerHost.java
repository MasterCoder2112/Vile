package com.vile.server;

import java.net.ServerSocket;
import java.net.Socket;

import com.vile.Display;
import com.vile.Game;
import com.vile.RunGame;
import com.vile.entities.Bullet;
import com.vile.launcher.FPSLauncher;

/**
 * Title: ServerHost
 * 
 * @author Alexander Byrd
 * 
 *         Date Created: 11/13/2018
 * 
 *         Description: This class allows for a server to be started using the
 *         current hosts current ip address and a port number that the host
 *         chooses, and then accepts up to 4 clients (as of now) and then allows
 *         clients to recieve game information from the server and also allows
 *         clients to send new information to be added back to the server.
 *
 */
public class ServerHost {

	public int maxClients = 3;
	public int clientCount = 0;
	public Thread[] clientThreads = new Thread[maxClients];
	public ServerSocket serverSocket;

	public ServerHost(String portNum) {

		try {
			int portNumber = Integer.parseInt(portNum);

			serverSocket = new ServerSocket(portNumber);

			Display.itemsRespawn = true;
			Display.clientGame = false;

			new RunGame();

			// Don't use unless seeing if this fixes the issue
			// Thread host = new Thread(this);
			// host.run();

			while (clientCount < maxClients) {
				Socket clientSocket = serverSocket.accept();
				ClientThread client = new ClientThread(clientSocket);
				Thread newThread = new Thread(client);
				clientThreads[clientCount] = newThread;
				newThread.start();
				clientCount++;
			}

			// TODO Figure out how to start the game somewhere in here for the host?
		} catch (Exception e) {
			// TODO Have something that says to the host the server could not be started
			new FPSLauncher(0);
		}
	}

	/**
	 * Ends the server by closing all client threads and then closing the server
	 * socket for the host.
	 */
	public void endServer() {

		// End all client threads first
		for (Thread t : clientThreads) {
			try {
				t.join();
			} catch (Exception e) {

			}
		}

		// Close server socket
		try {
			serverSocket.close();
		} catch (Exception e) {

		}
	}

	/**
	 * Adds bullet shot by client to the hosts game. All bullet related info is
	 * found within the bulletData string sent in as a parameter.
	 * 
	 * @param bulletData
	 */
	public void addBullet(String bulletData) {
		Bullet b = null;

		String[] bAtt = bulletData.split(":");

		b = new Bullet(Integer.parseInt(bAtt[1]), Double.parseDouble(bAtt[2]), Double.parseDouble(bAtt[3]),
				Double.parseDouble(bAtt[4]), Double.parseDouble(bAtt[5]), Integer.parseInt(bAtt[0]), 0, false);

		b.xa = Double.parseDouble(bAtt[6]);
		b.za = Double.parseDouble(bAtt[7]);
		b.initialSpeed = Double.parseDouble(bAtt[8]);

		Game.bullets.add(b);
	}

	/**
	 * Updates a particular players positional data on the hosts game so that all
	 * the clients will know where to render the other players.
	 * 
	 * @param playerIndex
	 * @param positionData
	 */
	public void updatePlayerPosition(int playerIndex, String positionData) {

		String[] coordinates = positionData.split(":");
		Game.otherPlayers.get(playerIndex).x = Double.parseDouble(coordinates[0]);
		Game.otherPlayers.get(playerIndex).y = Double.parseDouble(coordinates[1]);
		Game.otherPlayers.get(playerIndex).z = Double.parseDouble(coordinates[2]);
	}

}
