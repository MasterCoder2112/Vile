package com.vile.entities;

/**
 * Title: ServerPlayer
 * 
 * @author Alexander Byrd
 * 
 *         Date Created: 11/17/2018
 * 
 *         Description: Holds position values for any other players on the
 *         server so the game can render them.
 *
 */
public class ServerPlayer {

	public double x = 0;
	public double y = 0;
	public double z = 0;

	public ServerPlayer(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

}
