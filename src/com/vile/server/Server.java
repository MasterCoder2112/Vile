package com.vile.server;

import com.vile.Game;

public class Server {

	public Server() {
		// TODO All Server setup stuff will go here, including accepting clients etc...
	}

	/**
	 * Adds bullet shot by client to the hosts game.
	 * 
	 * @param bulletData
	 */
	public void addBullet(String bulletData) {
		// TODO Take in information from client about bullet that was shot, and add it
		// here to the
		// hosts game.
		// Bullet bullet = new Bullet();

		// Game.bullets.add(bullet);
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
