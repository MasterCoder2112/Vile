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

			// TODO Only issue I could see maybe happening is that unlike C I don't know if
			// it will wait
			// for client input before doing these things. I'm thinking it will though.

			loadDataFromClient(in);

			sendDataToClient(out);

		} catch (Exception e) {

		}
	}

	/**
	 * Send all the new and updated game data to the client so that their game will
	 * update with the new values and it renders for them correctly.
	 * 
	 * @param out
	 */
	public void sendDataToClient(PrintWriter out) {
		/*
		 * TODO below is the code used by the FPSLauncher to save data needed for a
		 * savefile to be created. In a similar manner, we will take all the game data
		 * (along with the Game.otherPlayers arraylist which is not seen in the code
		 * below) and write it to a string which will be sent through the PrintWriter to
		 * the clients socket.
		 */

		/*
		 * Tries to write game data into a save file within the current users directory
		 */
		/*
		 * try { rewrite = new BufferedWriter(new FileWriter("Users/" + currentUserName
		 * + "/" + fileName + ".txt"));
		 * 
		 * rewrite.write(Game.mapNum + ":" + gameMode + ":" + Game.secretsFound + ":" +
		 * Game.enemiesInMap + ":" + Player.kills + ":" + themeName);
		 * 
		 * rewrite.newLine();
		 * 
		 * // Player and level stuff rewrite.write(Player.health + ":" +
		 * Player.maxHealth + ":" + Player.armor + ":" + Player.x + ":" + Player.y + ":"
		 * + Player.z + ":" + Player.rotation + ":" + Player.maxHeight + ":" +
		 * Player.hasRedKey + ":" + Player.hasBlueKey + ":" + Player.hasGreenKey + ":" +
		 * Player.hasYellowKey + ":" + Player.upRotate + ":" + Player.extraHeight + ":"
		 * + Player.resurrections + ":" + Player.environProtectionTime + ":" +
		 * Player.immortality + ":" + Player.vision + ":" + Player.invisibility + ":" +
		 * Player.weaponEquipped + ":" + Player.godModeOn + ":" + Player.noClipOn + ":"
		 * + Player.flyOn + ":" + Player.superSpeedOn + ":" + Player.unlimitedAmmoOn +
		 * ":" + Player.upgradePoints + ":" + Level.width + ":" + Level.height + ":" +
		 * Game.mapNum + ":" + Game.mapAudio + ":" + Game.mapFloor + ":" +
		 * Game.mapCeiling + ":" + Render3D.ceilingDefaultHeight + ":" +
		 * Level.renderDistance + ":" + Game.mapName + ",");
		 * 
		 * // Weapons for (int i = 0; i < Player.weapons.length; i++) { Weapon w =
		 * Player.weapons[i]; int size = w.cartridges.size();
		 * 
		 * rewrite.write(w.weaponID + ":" + w.canBeEquipped + ":" + w.dualWield + ":" +
		 * w.ammo);
		 * 
		 * for (int j = 0; j < size; j++) { int cartSize = w.cartridges.get(j).ammo;
		 * rewrite.write(":" + cartSize); }
		 * 
		 * rewrite.write(";"); }
		 * 
		 * rewrite.newLine(); rewrite.write("Walls:"); rewrite.newLine();
		 * 
		 * for (int i = 0; i < Level.blocks.length; i++) { Block b = Level.blocks[i];
		 * rewrite.write(b.health + ":" + b.x + ":" + b.y + ":" + b.z + ":" + b.wallID +
		 * ":" + b.wallPhase + ":" + b.height + ":" + b.isSolid + ":" + b.seeThrough +
		 * ";"); }
		 * 
		 * rewrite.newLine(); rewrite.write("Enemies:"); rewrite.newLine();
		 * 
		 * for (int i = 0; i < Game.enemies.size(); i++) { Enemy en =
		 * Game.enemies.get(i); rewrite.write(en.health + ":" + en.xPos + ":" + en.yPos
		 * + ":" + en.zPos + ":" + en.ID + ":" + en.itemActivationID + ":" +
		 * en.maxHeight + ":" + en.newTarget + ":" + en.targetX + ":" + en.targetY + ":"
		 * + en.targetZ + ":" + en.activated + ":" + en.rotation + ":" + en.isAttacking
		 * + ":" + en.isFiring + ":" + en.isABoss + ":" + en.xEffects + ":" +
		 * en.yEffects + ":" + en.zEffects + ":" + en.tick + ":" + en.tickRound + ":" +
		 * en.tickAmount + ";"); }
		 * 
		 * rewrite.newLine(); rewrite.write("Bosses:"); rewrite.newLine();
		 * 
		 * for (int i = 0; i < Game.bosses.size(); i++) { Enemy en = Game.bosses.get(i);
		 * rewrite.write(en.health + ":" + en.xPos + ":" + en.yPos + ":" + en.zPos + ":"
		 * + en.ID + ":" + en.itemActivationID + ":" + en.maxHeight + ":" + en.newTarget
		 * + ":" + en.targetX + ":" + en.targetY + ":" + en.targetZ + ":" + en.activated
		 * + ":" + en.rotation + ":" + en.isAttacking + ":" + en.isFiring + ":" +
		 * en.isABoss + ":" + en.xEffects + ":" + en.yEffects + ":" + en.zEffects + ":"
		 * + en.tick + ":" + en.tickRound + ":" + en.tickAmount + ";"); }
		 * 
		 * rewrite.newLine(); rewrite.write("Items:"); rewrite.newLine();
		 * 
		 * for (int i = 0; i < Game.items.size(); i++) { Item item = Game.items.get(i);
		 * String audioQueue = item.audioQueue;
		 * 
		 * // So any null audio queues will be set as -1 // to be ignored if
		 * (audioQueue.equals("")) { audioQueue = "-1"; }
		 * 
		 * rewrite.write(item.itemActivationID + ":" + item.itemID + ":" + item.x + ":"
		 * + item.y + ":" + item.z + ":" + item.rotation + ":" + audioQueue + ";"); }
		 * 
		 * rewrite.newLine(); rewrite.write("Bullets:"); rewrite.newLine();
		 * 
		 * for (int i = 0; i < Game.bullets.size(); i++) { Bullet b =
		 * Game.bullets.get(i); rewrite.write(b.ID + ":" + b.damage + ":" + b.speed +
		 * ":" + b.x + ":" + b.y + ":" + b.z + ":" + b.xa + ":" + b.za + ":" +
		 * b.initialSpeed + ";"); }
		 * 
		 * rewrite.newLine(); rewrite.write("Enemy Projectiles:"); rewrite.newLine();
		 * 
		 * for (int i = 0; i < Game.enemyProjectiles.size(); i++) { EnemyFire b =
		 * Game.enemyProjectiles.get(i); rewrite.write(b.ID + ":" + b.damage + ":" +
		 * b.speed + ":" + b.x + ":" + b.y + ":" + b.z + ":" + b.xa + ":" + b.za + ":" +
		 * b.initialSpeed + ";"); }
		 * 
		 * rewrite.newLine(); rewrite.write("Explosions:"); rewrite.newLine();
		 * 
		 * for (int i = 0; i < Game.explosions.size(); i++) { Explosion exp =
		 * Game.explosions.get(i); rewrite.write(exp.ID + ":" + exp.phaseTime + ":" +
		 * exp.x + ":" + exp.y + ":" + exp.z + ":" + exp.exploded + ":" +
		 * exp.itemActivationID + ";"); }
		 * 
		 * rewrite.newLine(); rewrite.write("Buttons:"); rewrite.newLine();
		 * 
		 * for (int i = 0; i < Game.buttons.size(); i++) { Button b =
		 * Game.buttons.get(i); rewrite.write(b.itemID + ":" + b.itemActivationID + ":"
		 * + b.x + ":" + b.y + ":" + b.z + ":" + b.pressed + ":" + b.audioQueue + ";");
		 * }
		 * 
		 * rewrite.newLine(); rewrite.write("Doors:"); rewrite.newLine();
		 * 
		 * for (int i = 0; i < Game.doors.size(); i++) { Door d = Game.doors.get(i);
		 * rewrite.write(d.ID + ":" + d.itemActivationID + ":" + d.xPos + ":" + d.yPos +
		 * ":" + d.zPos + ":" + d.doorX + ":" + d.doorZ + ":" + d.time + ":" +
		 * d.soundTime + ":" + d.doorType + ":" + d.doorY + ":" + d.maxHeight + ";"); }
		 * 
		 * rewrite.newLine(); rewrite.write("Elevators:"); rewrite.newLine();
		 * 
		 * for (int i = 0; i < Game.elevators.size(); i++) { Elevator ele =
		 * Game.elevators.get(i); rewrite.write(ele.ID + ":" + ele.itemActivationID +
		 * ":" + ele.xPos + ":" + ele.yPos + ":" + ele.zPos + ":" + ele.elevatorX + ":"
		 * + ele.elevatorZ + ":" + ele.height + ":" + ele.soundTime + ":" + ele.movingUp
		 * + ":" + ele.movingDown + ":" + ele.waitTime + ":" + ele.upHeight + ":" +
		 * ele.activated + ":" + ele.maxHeight + ";"); }
		 * 
		 * rewrite.newLine(); rewrite.write("Corpses:"); rewrite.newLine();
		 * 
		 * for (int i = 0; i < Game.corpses.size(); i++) { Corpse cor =
		 * Game.corpses.get(i); rewrite.write(cor.enemyID + ":" + cor.phaseTime + ":" +
		 * cor.xPos + ":" + cor.yPos + ":" + cor.zPos + ":" + cor.time + ":" +
		 * cor.xEffects + ":" + cor.yEffects + ":" + cor.zEffects + ";"); }
		 * 
		 * rewrite.close(); } catch (IOException ex) { System.out.println(ex); //
		 * exception handling left as an exercise for the reader }
		 */
	}

	/**
	 * Loads data that the client sends in, into the host game and updates those
	 * values.
	 * 
	 * @param in
	 */
	public void loadDataFromClient(BufferedReader in) {
		// TODO Below is the code from when I loaded information from a file for the
		// game.
		// Use this code to do a similar thing in parsing data that the client sends in.
		// The client will be sending in data about themselves (Will be set to the
		// Game.otherPlayers.get(index of client) data), data from activatedButtons,
		// activatedDoors, activatedElevators, itemsAdded, and addedBullets.

		/*
		 * String currentLine = ""; while(currentLine = in.readLine()){ TODO put code
		 * within this for loop. }
		 */

		// Elements in the line
		/*
		 * String[] elements = currentLine.split(":");
		 * 
		 * // Was it survival or campaign mode int gM = Integer.parseInt(elements[1]);
		 * 
		 * FPSLauncher.gameMode = gM;
		 * 
		 * // Resets all the lists resetLists();
		 * 
		 * secretsFound = Integer.parseInt(elements[2]); enemiesInMap =
		 * Integer.parseInt(elements[3]); Player.kills = Integer.parseInt(elements[4]);
		 * FPSLauncher.themeName = (elements[5]);
		 * 
		 * // Length of elements to read in int length = 0;
		 * 
		 * ////////////////////////// Player stuff now currentLine = sc.nextLine();
		 * 
		 * String weaponStuff = ""; String otherStuff = "";
		 * 
		 * // Split between weapon equipped and weapon attributes elements =
		 * currentLine.split(",");
		 * 
		 * weaponStuff = elements[1]; otherStuff = elements[0];
		 * 
		 * elements = otherStuff.split(":");
		 * 
		 * Player.health = Integer.parseInt(elements[0]); Player.maxHealth =
		 * Integer.parseInt(elements[1]); Player.armor = Integer.parseInt(elements[2]);
		 * Player.x = Double.parseDouble(elements[3]); Player.y =
		 * Double.parseDouble(elements[4]); Player.z = Double.parseDouble(elements[5]);
		 * Player.rotation = Double.parseDouble(elements[6]); Player.maxHeight =
		 * Double.parseDouble(elements[7]); Player.hasRedKey =
		 * Boolean.parseBoolean(elements[8]); Player.hasBlueKey =
		 * Boolean.parseBoolean(elements[9]); Player.hasGreenKey =
		 * Boolean.parseBoolean(elements[10]); Player.hasYellowKey =
		 * Boolean.parseBoolean(elements[11]); Player.upRotate =
		 * Double.parseDouble(elements[12]); Player.extraHeight =
		 * Double.parseDouble(elements[13]); Player.resurrections =
		 * Integer.parseInt(elements[14]); Player.environProtectionTime =
		 * Integer.parseInt(elements[15]); Player.immortality =
		 * Integer.parseInt(elements[16]); Player.vision =
		 * Integer.parseInt(elements[17]); Player.invisibility =
		 * Integer.parseInt(elements[18]); Player.weaponEquipped =
		 * Integer.parseInt(elements[19]); Player.godModeOn =
		 * Boolean.parseBoolean(elements[20]); Player.noClipOn =
		 * Boolean.parseBoolean(elements[21]); Player.flyOn =
		 * Boolean.parseBoolean(elements[22]); Player.superSpeedOn =
		 * Boolean.parseBoolean(elements[23]); Player.unlimitedAmmoOn =
		 * Boolean.parseBoolean(elements[24]); Player.upgradePoints =
		 * Integer.parseInt(elements[25]); Level.width = Integer.parseInt(elements[26]);
		 * Level.height = Integer.parseInt(elements[27]); mapNum =
		 * Integer.parseInt(elements[28]); mapAudio = elements[29]; mapFloor =
		 * Integer.parseInt(elements[30]); mapCeiling = Integer.parseInt(elements[31]);
		 * Render3D.ceilingDefaultHeight = Double.parseDouble(elements[32]);
		 * Render3D.renderDistanceDefault = Double.parseDouble(elements[33]);
		 * 
		 * // Because map names are normally named as Map#: Name. // The colon causes
		 * issues so this fixes that. And adds // the colon back in because its split
		 * out try { mapName = elements[34] + ":" + elements[35]; } catch (Exception ex)
		 * { // If there's an issue, its probably because the map name // does not
		 * include the colon mapName = elements[34]; }
		 * 
		 * // Set up new level with this size level = new Level(Level.width,
		 * Level.height);
		 * 
		 * // Split up weapon Attributes elements = weaponStuff.split(";");
		 * 
		 * // Length is set to the amount of elements length = elements.length;
		 * 
		 * /* If there is one element, and it is just blank space, then set the array to
		 * being null again.
		 */
		/*
		 * if (elements.length == 1 && elements[0].trim().equals("")) { elements = null;
		 * length = 0; }
		 * 
		 * /* For each weapon, load in its attributes depending on what they were when
		 * the game was saved.
		 */
		/*
		 * for (int i = 0; i < length; i++) { Weapon w = Player.weapons[i];
		 * 
		 * String[] weaponStats = elements[i].split(":");
		 * 
		 * int size = weaponStats.length - 4;
		 * 
		 * w.weaponID = Integer.parseInt(weaponStats[0]); w.canBeEquipped =
		 * Boolean.parseBoolean(weaponStats[1]); w.dualWield =
		 * Boolean.parseBoolean(weaponStats[2]); w.ammo =
		 * Integer.parseInt(weaponStats[3]);
		 * 
		 * for (int j = 0; j < size; j++) { w.cartridges.add(new
		 * Cartridge(Integer.parseInt(weaponStats[4 + j]))); } }
		 * 
		 * ////////////////// Walls sc.nextLine();
		 * 
		 * String thisLine = "";
		 * 
		 * currentLine = sc.nextLine();
		 * 
		 * // Stop reading when it reaches where the next element of the // game is
		 * being loaded in. while (!thisLine.equals("Enemies:")) { thisLine =
		 * sc.nextLine();
		 * 
		 * if (thisLine.equals("Enemies:")) { break; }
		 * 
		 * currentLine += thisLine; }
		 * 
		 * elements = currentLine.split(";");
		 * 
		 * // Length is set to the amount of elements length = elements.length;
		 * 
		 * /* If there is one element, and it is just blank space, then set the array to
		 * being null again.
		 */
		/*
		 * if (elements.length == 1 && elements[0].trim().equals("")) { elements = null;
		 * length = 0; }
		 * 
		 * for (int i = 0; i < length; i++) { otherStuff = elements[i]; String[] bAt =
		 * otherStuff.split(":");
		 * 
		 * // Create enemy with its needed values Block b = new
		 * Block(Double.parseDouble(bAt[6]), Integer.parseInt(bAt[4]),
		 * Double.parseDouble(bAt[2]) * 4, Integer.parseInt(bAt[1]),
		 * Integer.parseInt(bAt[3]));
		 * 
		 * b.health = Integer.parseInt(bAt[0]); b.wallPhase = Integer.parseInt(bAt[5]);
		 * b.isSolid = Boolean.parseBoolean(bAt[7]); b.seeThrough =
		 * Boolean.parseBoolean(bAt[8]);
		 * 
		 * Level.blocks[b.x + b.z * Level.width] = b; }
		 * 
		 * ////////////////// Enemies thisLine = "";
		 * 
		 * currentLine = sc.nextLine();
		 * 
		 * while (!thisLine.equals("Bosses:")) { thisLine = sc.nextLine();
		 * 
		 * if (thisLine.equals("Bosses:")) { break; }
		 * 
		 * currentLine += thisLine; }
		 * 
		 * elements = currentLine.split(";");
		 * 
		 * // Length is set to the amount of elements length = elements.length;
		 * 
		 * /* If there is one element, and it is just blank space, then set the array to
		 * being null again.
		 */
		/*
		 * if (elements.length == 1 && elements[0].trim().equals("")) { elements = null;
		 * length = 0; }
		 * 
		 * for (int i = 0; i < length; i++) { otherStuff = elements[i]; String[] enAt =
		 * otherStuff.split(":");
		 * 
		 * // Create enemy with its needed values Enemy en = new
		 * Enemy(Double.parseDouble(enAt[1]), Double.parseDouble(enAt[2]),
		 * Double.parseDouble(enAt[3]), Integer.parseInt(enAt[4]),
		 * Double.parseDouble(enAt[12]), Integer.parseInt(enAt[5]));
		 * 
		 * en.maxHeight = Double.parseDouble(enAt[6]); en.newTarget =
		 * Boolean.parseBoolean(enAt[7]); en.targetX = Double.parseDouble(enAt[8]);
		 * en.targetY = Double.parseDouble(enAt[9]); en.targetZ =
		 * Double.parseDouble(enAt[10]); en.activated = Boolean.parseBoolean(enAt[11]);
		 * en.isAttacking = Boolean.parseBoolean(enAt[13]); en.isFiring =
		 * Boolean.parseBoolean(enAt[14]); en.isABoss = Boolean.parseBoolean(enAt[15]);
		 * en.xEffects = Double.parseDouble(enAt[16]); en.yEffects =
		 * Double.parseDouble(enAt[17]); en.zEffects = Double.parseDouble(enAt[18]);
		 * en.tick = Integer.parseInt(enAt[19]); en.tickRound =
		 * Integer.parseInt(enAt[20]); en.tickAmount = Integer.parseInt(enAt[21]);
		 * 
		 * Game.enemies.add(en);
		 * 
		 * Block blockOn = Level.getBlock((int) en.xPos, (int) en.zPos);
		 * 
		 * // Only if in campaign mode, survival mode acts weird here if (gM == 0) {
		 * blockOn.entitiesOnBlock.add(en); } }
		 * 
		 * thisLine = "";
		 * 
		 * currentLine = sc.nextLine();
		 * 
		 * while (!thisLine.equals("Items:")) { thisLine = sc.nextLine();
		 * 
		 * if (thisLine.equals("Items:")) { break; }
		 * 
		 * currentLine += thisLine; }
		 * 
		 * elements = currentLine.split(";");
		 * 
		 * // Length is set to the amount of elements length = elements.length;
		 * 
		 * /* If there is one element, and it is just blank space, then set the array to
		 * being null again.
		 */
		/*
		 * if (elements.length == 1 && elements[0].trim().equals("")) { elements = null;
		 * length = 0; }
		 * 
		 * for (int i = 0; i < length; i++) { otherStuff = elements[i]; String[] enAt =
		 * otherStuff.split(":");
		 * 
		 * // Create enemy with its needed values Enemy en = new
		 * Enemy(Double.parseDouble(enAt[1]), Double.parseDouble(enAt[2]),
		 * Double.parseDouble(enAt[3]), Integer.parseInt(enAt[4]),
		 * Double.parseDouble(enAt[12]), Integer.parseInt(enAt[5]));
		 * 
		 * en.maxHeight = Double.parseDouble(enAt[6]); en.newTarget =
		 * Boolean.parseBoolean(enAt[7]); en.targetX = Double.parseDouble(enAt[8]);
		 * en.targetY = Double.parseDouble(enAt[9]); en.targetZ =
		 * Double.parseDouble(enAt[10]); en.activated = Boolean.parseBoolean(enAt[11]);
		 * en.isAttacking = Boolean.parseBoolean(enAt[13]); en.isFiring =
		 * Boolean.parseBoolean(enAt[14]); en.isABoss = Boolean.parseBoolean(enAt[15]);
		 * en.xEffects = Double.parseDouble(enAt[16]); en.yEffects =
		 * Double.parseDouble(enAt[17]); en.zEffects = Double.parseDouble(enAt[18]);
		 * en.tick = Integer.parseInt(enAt[19]); en.tickRound =
		 * Integer.parseInt(enAt[20]); en.tickAmount = Integer.parseInt(enAt[21]);
		 * 
		 * Game.bosses.add(en); }
		 * 
		 * thisLine = "";
		 * 
		 * currentLine = sc.nextLine();
		 * 
		 * while (!thisLine.equals("Bullets:")) { thisLine = sc.nextLine();
		 * 
		 * if (thisLine.equals("Bullets:")) { break; }
		 * 
		 * currentLine += thisLine; }
		 * 
		 * elements = currentLine.split(";");
		 * 
		 * // Length is set to the amount of elements length = elements.length;
		 * 
		 * /* If there is one element, and it is just blank space, then set the array to
		 * being null again.
		 */
		/*
		 * if (elements.length == 1 && elements[0].trim().equals("")) { elements = null;
		 * length = 0; }
		 * 
		 * for (int i = 0; i < length; i++) { otherStuff = elements[i]; String[] itemAtt
		 * = otherStuff.split(":");
		 * 
		 * int itemID = Integer.parseInt(itemAtt[1]);
		 * 
		 * Item temp = null;
		 * 
		 * // TODO update item loading stuff maybe
		 * 
		 * /* If its not an explosive canister, add it as a normal item. Otherwise add
		 * it as an explosive canister
		 */
		/*
		 * if (itemID != ItemNames.CANISTER.getID()) { temp = new Item(10,
		 * Double.parseDouble(itemAtt[2]), Double.parseDouble(itemAtt[3]),
		 * Double.parseDouble(itemAtt[4]), itemID, Integer.parseInt(itemAtt[5]),
		 * Integer.parseInt(itemAtt[0]), itemAtt[6]); } else { temp = new
		 * ExplosiveCanister(10, Double.parseDouble(itemAtt[2]),
		 * Double.parseDouble(itemAtt[3]), Double.parseDouble(itemAtt[4]), itemID,
		 * Integer.parseInt(itemAtt[5]), Integer.parseInt(itemAtt[0])); }
		 * 
		 * Block itemBlock = Level.getBlock((int) temp.x, (int) temp.z);
		 * 
		 * // If the item gives the block a specific quality, or if the // item can not
		 * be removed from the block (if its solid) if (temp.isSolid || itemID ==
		 * ItemNames.BREAKABLEWALL.getID() || itemID == ItemNames.SECRET.getID() ||
		 * itemID == ItemNames.LINEDEF.getID()) { // Set item to being the item that is
		 * within this // block only if it is solid itemBlock.wallItems.add(temp); }
		 * 
		 * // If satellite dish, add to activatable list as well if (itemID ==
		 * ItemNames.RADAR.getID()) { Game.activatable.add(temp); } // If item supposed
		 * to be activated by button else if (itemID == ItemNames.ACTIVATEEXP.getID() ||
		 * itemID == ItemNames.ENEMYSPAWN.getID() || itemID ==
		 * ItemNames.WALLBEGONE.getID()) { Game.activatable.add(temp); } else if (itemID
		 * == ItemNames.TELEPORTEREXIT.getID() || itemID ==
		 * ItemNames.TELEPORTERENTER.getID()) { Game.teleporters.add(temp);
		 * 
		 * itemBlock.wallEntities.add(temp); } }
		 * 
		 * //////////////////////// Bullets thisLine = "";
		 * 
		 * currentLine = sc.nextLine();
		 * 
		 * while (!thisLine.equals("Enemy Projectiles:")) { thisLine = sc.nextLine();
		 * 
		 * if (thisLine.equals("Enemy Projectiles:")) { break; }
		 * 
		 * currentLine += thisLine; }
		 * 
		 * elements = currentLine.split(";");
		 * 
		 * // Length is set to the amount of elements length = elements.length;
		 * 
		 * /* If there is one element, and it is just blank space, then set the array to
		 * being null again.
		 */
		/*
		 * if (elements.length == 1 && elements[0].trim().equals("")) { elements = null;
		 * length = 0; }
		 * 
		 * for (int i = 0; i < length; i++) { Bullet b = null;
		 * 
		 * String[] bAtt = elements[i].split(":");
		 * 
		 * b = new Bullet(Integer.parseInt(bAtt[1]), Double.parseDouble(bAtt[2]),
		 * Double.parseDouble(bAtt[3]), Double.parseDouble(bAtt[4]),
		 * Double.parseDouble(bAtt[5]), Integer.parseInt(bAtt[0]), 0, false);
		 * 
		 * b.xa = Double.parseDouble(bAtt[6]); b.za = Double.parseDouble(bAtt[7]);
		 * b.initialSpeed = Double.parseDouble(bAtt[8]);
		 * 
		 * Game.bullets.add(b); }
		 * 
		 * /////////////////////////// Enemy Fire thisLine = "";
		 * 
		 * currentLine = sc.nextLine();
		 * 
		 * while (!thisLine.equals("Explosions:")) { thisLine = sc.nextLine();
		 * 
		 * if (thisLine.equals("Explosions:")) { break; }
		 * 
		 * currentLine += thisLine; }
		 * 
		 * elements = currentLine.split(";");
		 * 
		 * // Length is set to the amount of elements length = elements.length;
		 * 
		 * /* If there is one element, and it is just blank space, then set the array to
		 * being null again.
		 */
		/*
		 * if (elements.length == 1 && elements[0].trim().equals("")) { elements = null;
		 * length = 0; }
		 * 
		 * for (int i = 0; i < length; i++) { EnemyFire b = null;
		 * 
		 * String[] bAtt = elements[i].split(":");
		 * 
		 * // Given all the information we have, construct // the enemy projectile the
		 * best we can after a save b = new EnemyFire(Integer.parseInt(bAtt[1]),
		 * Double.parseDouble(bAtt[2]), Double.parseDouble(bAtt[3]),
		 * Double.parseDouble(bAtt[4]), Double.parseDouble(bAtt[5]),
		 * Integer.parseInt(bAtt[0]), 0, 0, 0, 0, null, false);
		 * 
		 * b.xa = Double.parseDouble(bAtt[6]); b.za = Double.parseDouble(bAtt[7]);
		 * b.initialSpeed = Double.parseDouble(bAtt[8]);
		 * 
		 * Game.enemyProjectiles.add(b); }
		 * 
		 * /////////////////////////////////// Explosions thisLine = "";
		 * 
		 * currentLine = sc.nextLine();
		 * 
		 * while (!thisLine.equals("Buttons:")) { thisLine = sc.nextLine();
		 * 
		 * if (thisLine.equals("Buttons:")) { break; }
		 * 
		 * currentLine += thisLine; }
		 * 
		 * elements = currentLine.split(";");
		 * 
		 * // Length is set to the amount of elements length = elements.length;
		 * 
		 * /* If there is one element, and it is just blank space, then set the array to
		 * being null again.
		 */
		/*
		 * if (elements.length == 1 && elements[0].trim().equals("")) { elements = null;
		 * length = 0; }
		 * 
		 * for (int i = 0; i < length; i++) { Explosion exp = null;
		 * 
		 * String[] expAtt = elements[i].split(":");
		 * 
		 * exp = new Explosion(Double.parseDouble(expAtt[2]),
		 * Double.parseDouble(expAtt[3]), Double.parseDouble(expAtt[4]),
		 * Integer.parseInt(expAtt[0]), Integer.parseInt(expAtt[6]));
		 * 
		 * exp.exploded = Boolean.parseBoolean(expAtt[5]); exp.phaseTime =
		 * Integer.parseInt(expAtt[1]);
		 * 
		 * Game.explosions.add(exp); }
		 * 
		 * //////////////////////////// Buttons thisLine = "";
		 * 
		 * currentLine = sc.nextLine();
		 * 
		 * while (!thisLine.equals("Doors:")) { thisLine = sc.nextLine();
		 * 
		 * if (thisLine.equals("Doors:")) { break; }
		 * 
		 * currentLine += thisLine; }
		 * 
		 * elements = currentLine.split(";");
		 * 
		 * // Length is set to the amount of elements length = elements.length;
		 * 
		 * /* If there is one element, and it is just blank space, then set the array to
		 * being null again.
		 */
		/*
		 * if (elements.length == 1 && elements[0].trim().equals("")) { elements = null;
		 * length = 0; }
		 * 
		 * for (int i = 0; i < length; i++) { Button b = null;
		 * 
		 * String[] bAtt = elements[i].split(":");
		 * 
		 * b = new Button(Double.parseDouble(bAtt[2]), Double.parseDouble(bAtt[3]),
		 * Double.parseDouble(bAtt[4]), Integer.parseInt(bAtt[0]),
		 * Integer.parseInt(bAtt[1]), bAtt[5]);
		 * 
		 * b.pressed = Boolean.parseBoolean(bAtt[5]);
		 * 
		 * Game.buttons.add(b); }
		 * 
		 * //////////////////////////// Doors thisLine = "";
		 * 
		 * currentLine = sc.nextLine();
		 * 
		 * while (!thisLine.equals("Elevators:")) { thisLine = sc.nextLine();
		 * 
		 * if (thisLine.equals("Elevators:")) { break; }
		 * 
		 * currentLine += thisLine; }
		 * 
		 * elements = currentLine.split(";");
		 * 
		 * // Length is set to the amount of elements length = elements.length;
		 * 
		 * /* If there is one element, and it is just blank space, then set the array to
		 * being null again.
		 */
		/*
		 * if (elements.length == 1 && elements[0].trim().equals("")) { elements = null;
		 * length = 0; }
		 * 
		 * for (int i = 0; i < length; i++) { Door d = null;
		 * 
		 * String[] dAtt = elements[i].split(":");
		 * 
		 * d = new Door(Double.parseDouble(dAtt[2]), Double.parseDouble(dAtt[3]),
		 * Double.parseDouble(dAtt[4]), Integer.parseInt(dAtt[5]),
		 * Integer.parseInt(dAtt[6]), Integer.parseInt(dAtt[9]),
		 * Integer.parseInt(dAtt[1]), Double.parseDouble(dAtt[11]) * 4);
		 * 
		 * d.time = Integer.parseInt(dAtt[7]); d.soundTime = Integer.parseInt(dAtt[8]);
		 * d.ID = Integer.parseInt(dAtt[0]); d.doorY = Double.parseDouble(dAtt[10]);
		 * 
		 * Block thisBlock = Level.getBlock(d.doorX, d.doorZ);
		 * 
		 * thisBlock.y = d.doorY;
		 * 
		 * if (thisBlock.y > 0) { thisBlock.isMoving = true; }
		 * 
		 * Game.doors.add(d); }
		 * 
		 * //////////////////////////// Elevators thisLine = "";
		 * 
		 * currentLine = sc.nextLine();
		 * 
		 * while (!thisLine.equals("Corpses:")) { thisLine = sc.nextLine();
		 * 
		 * if (thisLine.equals("Corpses:")) { break; }
		 * 
		 * currentLine += thisLine; }
		 * 
		 * elements = currentLine.split(";");
		 * 
		 * // Length is set to the amount of elements length = elements.length;
		 * 
		 * /* If there is one element, and it is just blank space, then set the array to
		 * being null again.
		 */
		/*
		 * if (elements.length == 1 && elements[0].trim().equals("")) { elements = null;
		 * length = 0; }
		 * 
		 * for (int i = 0; i < length; i++) { Elevator e = null;
		 * 
		 * String[] eAtt = elements[i].split(":");
		 * 
		 * e = new Elevator(Double.parseDouble(eAtt[2]), Double.parseDouble(eAtt[3]),
		 * Double.parseDouble(eAtt[4]), Integer.parseInt(eAtt[5]),
		 * Integer.parseInt(eAtt[6]), Integer.parseInt(eAtt[1]),
		 * Double.parseDouble(eAtt[14]));
		 * 
		 * e.height = Integer.parseInt(eAtt[7]); e.soundTime =
		 * Integer.parseInt(eAtt[8]); e.ID = Integer.parseInt(eAtt[0]); e.waitTime =
		 * Integer.parseInt(eAtt[11]); e.upHeight = Double.parseDouble(eAtt[12]);
		 * e.movingUp = Boolean.parseBoolean(eAtt[9]); e.movingDown =
		 * Boolean.parseBoolean(eAtt[10]); e.activated = Boolean.parseBoolean(eAtt[13]);
		 * 
		 * Game.elevators.add(e);
		 * 
		 * Block thisBlock = Level.getBlock(e.elevatorX, e.elevatorZ);
		 * 
		 * thisBlock.height = e.height; }
		 * 
		 * /////////////////////////////// Corpses thisLine = "";
		 * 
		 * // Sometimes theres not a next line try { currentLine = sc.nextLine(); }
		 * catch (Exception e) {
		 * 
		 * }
		 * 
		 * while (sc.hasNextLine()) { thisLine = sc.nextLine();
		 * 
		 * currentLine += thisLine; }
		 * 
		 * elements = currentLine.split(";");
		 * 
		 * // Length is set to the amount of elements length = elements.length;
		 * 
		 * /* If there is one element, and it is just blank space, then set the array to
		 * being null again.
		 */
		/*
		 * if (elements.length == 1 && elements[0].trim().equals("")) { elements = null;
		 * length = 0; }
		 * 
		 * for (int i = 0; i < length; i++) { Corpse c = null;
		 * 
		 * String[] cAtt = elements[i].split(":");
		 * 
		 * c = new Corpse(Double.parseDouble(cAtt[2]), Double.parseDouble(cAtt[4]),
		 * Double.parseDouble(cAtt[3]), Integer.parseInt(cAtt[0]),
		 * Double.parseDouble(cAtt[6]), Double.parseDouble(cAtt[8]),
		 * Double.parseDouble(cAtt[7]));
		 * 
		 * c.time = Integer.parseInt(cAtt[5]); c.phaseTime = Integer.parseInt(cAtt[1]);
		 * 
		 * Game.corpses.add(c); }
		 */
	}

}
