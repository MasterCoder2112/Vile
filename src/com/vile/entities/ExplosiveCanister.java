package com.vile.entities;

import com.vile.Game;

/**
 * Title: ExplosiveCanister
 * @author Alexander Byrd
 * Date Created: 11/11/2016
 * Date Updated: 4/21/2017
 * MY BIRTHDAY! Celebrated with explosions. Lol
 * 
 * Description:
 * An extension of Item that has a certain amount of health, and a flag
 * that states whether the canister is exploding or not at the moment.
 *
 */
public class ExplosiveCanister extends Item
{
	public int health = 10;
	public double heightCorrect = 8;
	
	public boolean performedAction = false;
	public boolean playedSound = false;
	
   /**
    * Creates Canister Item with values sent in, then adds it to the
    * Canister arraylist in game.
    * 
    * @param value
    * @param x
    * @param y
    * @param z
    * @param ID
    */
	public ExplosiveCanister(int value, double x, double y, double z, int ID, int rotation) 
	{
		super(value, x, y, z, ID, rotation);
		
		//Add the canister to the game
		Game.canisters.add(this);
	}
	
   /**
    * Removes this canister from everywhere in the game it may be
    * included in. This is called after the canister explodes.
    */
	public void removeCanister()
	{	
		Game.items.remove(this);
		
		Game.solidItems.remove(this);	
		
		Game.canisters.remove(this);
	}

}
