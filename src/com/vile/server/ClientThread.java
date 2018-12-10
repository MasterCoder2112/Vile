package com.vile.server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import com.vile.Display;
import com.vile.Game;
import com.vile.PopUp;
import com.vile.entities.Bullet;
import com.vile.entities.Cartridge;
import com.vile.entities.Explosion;
import com.vile.entities.Item;
import com.vile.entities.Position;
import com.vile.entities.ServerPlayer;
import com.vile.entities.Weapon;

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

	public Socket clientSocket;
	public int clientID = 0;
	public PrintWriter out;
	public BufferedReader in;

	// public Thread clientThread;

	public ClientThread(Socket clientSocket) {
		this.clientSocket = clientSocket;
		ServerPlayer newPlayer = new ServerPlayer();
		Game.otherPlayers.add(newPlayer);
		clientID = newPlayer.ID;

		try {
			out = new PrintWriter(this.clientSocket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
		} catch (Exception e) {

		}

		// clientThread = new Thread(this);
		// clientThread.start();
	}

	/**
	 * Runs continuously. Accepting input from the client when it comes in to update
	 * the host game, and then all the data from the host game is sent back to each
	 * of the clients through this method, after all has been updated.
	 */
	@Override
	public void run() {
		try {

			System.out.println("Has client and is waiting... Client ID: " + clientID);

			// TODO Only issue I could see maybe happening is that unlike C I don't know if
			// it will wait
			// for client input before doing these things. I'm thinking it will though.
			String inputLine = "";
			String outputLine = "";

			for (int i = 0; i < Game.spawnPoints.size(); i++) {
				Position p = Game.spawnPoints.get(i);

				// Sends client starting data for their starting positions as well as normal
				// data as well.
				if (p.spawnID == clientID) {
					outputLine += p.x + ":" + p.y + ":" + p.z + ":" + Game.mapName + "?";
				}
			}

			outputLine = sendDataToClient(outputLine);

			out.println(outputLine);

			while ((inputLine = in.readLine()) != null) {

				// If client ends connection
				if (inputLine.equals("bye")) {
					// All other clients ID's go down by 1
					for (int i = clientID + 1; i < ServerHost.clients.size(); i++) {
						ClientThread c = ServerHost.clients.get(i);
						c.clientID -= 1;
					}

					// Make the ID's of the actually Server Players go down too to adjust
					for (int i = clientID + 1; i < Game.otherPlayers.size(); i++) {
						ServerPlayer sP = Game.otherPlayers.get(i);

						sP.ID--;
					}

					// Remove this player from the server
					Game.otherPlayers.remove(clientID);

					// Remove clientThread class
					ServerHost.clients.remove(clientID);

					// Client count goes down.
					ServerHost.clientCount--;

					System.out.println("Client " + clientID + " left the server");

					try {
						ServerHost.clientThreads.get(clientID).join();
					} catch (Exception ex) {
						System.out.println(ex);
					}

					break;
				} else {
					// TODO uncomment this
					loadDataFromClient(inputLine);
					outputLine = "";
					outputLine = sendDataToClient(outputLine);
					out.println(outputLine);
				}
			}

			// String firstLine = in.readLine().trim();
			// in.reset();

			// System.out.println(firstLine);

			/*
			 * if (firstLine == "awake") { for (int i = 0; i < Game.spawnPoints.size(); i++)
			 * { Position p = Game.spawnPoints.get(i);
			 * 
			 * // Sends client starting data for their starting positions as well as normal
			 * // data as well. if (p.spawnID == clientID) { dataString += p.x + " : " + p.y
			 * + " : " + p.z + "?"; } } } else if (firstLine == "bye") { // All other
			 * clients ID's go down by 1 for (int i = clientID + 1; i <
			 * ServerHost.clients.size(); i++) { ClientThread c = ServerHost.clients.get(i);
			 * c.clientID -= 1; }
			 * 
			 * // Make the ID's of the actually Server Players go down too to adjust for
			 * (int i = clientID + 1; i < Game.otherPlayers.size(); i++) { ServerPlayer sP =
			 * Game.otherPlayers.get(i);
			 * 
			 * sP.ID--; }
			 * 
			 * // Remove this player from the server Game.otherPlayers.remove(clientID);
			 * 
			 * // Remove clientThread class ServerHost.clients.remove(clientID);
			 * 
			 * // Client count goes down. ServerHost.clientCount--;
			 * 
			 * System.out.println("Client " + clientID + " left the server");
			 * 
			 * // Join this thread clientThread.join();
			 * 
			 * return; } else { System.out.println(firstLine); // loadDataFromClient(in); }
			 * 
			 * System.out.println("In is: " + firstLine);
			 * 
			 * // sendDataToClient(out, dataString);
			 */

		} catch (Exception e) {
			e.printStackTrace();

			// All other clients ID's go down by 1
			for (int i = clientID + 1; i < ServerHost.clients.size(); i++) {
				ClientThread c = ServerHost.clients.get(i);
				c.clientID -= 1;
			}

			// Make the ID's of the actually Server Players go down too to adjust
			for (int i = clientID + 1; i < Game.otherPlayers.size(); i++) {
				ServerPlayer sP = Game.otherPlayers.get(i);

				sP.ID--;
			}

			// Remove this player from the server
			Game.otherPlayers.remove(clientID);

			// Remove clientThread class
			ServerHost.clients.remove(clientID);

			// Client count goes down.
			ServerHost.clientCount--;

			System.out.println("Client " + clientID + " left the server by error");

			try {
				ServerHost.clientThreads.get(clientID).join();
			} catch (Exception ex) {
				System.out.println(ex);
			}
		}
	}

	/**
	 * Send all the new and updated game data to the client so that their game will
	 * update with the new values and it renders for them correctly.
	 * 
	 * @param out
	 */
	public String sendDataToClient(String dataString) {
		/*
		 * TODO below is the code used by the FPSLauncher to save data needed for a
		 * savefile to be created. In a similar manner, we will take all the game data
		 * (along with the Game.otherPlayers arraylist which is not seen in the code
		 * below) and write it to a string which will be sent through the PrintWriter to
		 * the clients socket.
		 */
		for (int i = 0; i < Game.otherPlayers.size(); i++) {
			ServerPlayer sP = Game.otherPlayers.get(i);

			if (sP.ID != clientID) {
				dataString += "Other:" + sP.x + ":" + sP.y + ":" + sP.z + ":" + sP.ID + ":" + sP.health + ":"
						+ sP.maxHealth + ":" + sP.armor + ":" + sP.environProtectionTime + ":" + sP.immortality + ":"
						+ sP.vision + ":" + sP.invisibility + ":" + sP.height + ":" + sP.rotation + ":" + sP.xEffects
						+ ":" + sP.yEffects + ":" + sP.zEffects + ":" + sP.alive + ":" + sP.kills + ":" + sP.deaths
						+ ":" + sP.forceCrouch + ":" + sP.hasBlueKey + ":" + sP.hasRedKey + ":" + sP.hasGreenKey + ":"
						+ sP.hasYellowKey + ":" + sP.resurrections + ":" + sP.weaponEquipped + ":";

				// Weapons this player should have
				for (int j = 0; j < sP.weapons.length; j++) {
					Weapon w = sP.weapons[j];
					int size = w.cartridges.size();

					dataString += w.weaponID + "," + w.canBeEquipped + "," + w.dualWield + "," + w.ammo;

					if (w.canBeEquipped) {
						// System.out.println(w.weaponID + ":" + w.dualWield);
					}

					for (int k = 0; k < size; k++) {
						int cartSize = w.cartridges.get(k).ammo;
						dataString += "," + cartSize;
					}

					if (j < sP.weapons.length - 1) {
						dataString += "-";
					}
				}

				dataString += ":";

				// All messages that should have been displayed this tick for the client.
				for (int j = 0; j < sP.clientMessages.size(); j++) {
					PopUp p = sP.clientMessages.get(j);

					if (j == sP.clientMessages.size() - 2) {
						dataString += p.getText();
					} else {
						dataString += p.getText() + "-";
					}
				}

				if (sP.clientMessages.size() == 0) {
					dataString += "-1";
				}

				dataString += ":";
				sP.clientMessages = new ArrayList<PopUp>();

				// Any audio files that were supposed to be activated this tick for the client.
				for (int j = 0; j < sP.audioToPlay.size(); j++) {
					String s = sP.audioToPlay.get(j);

					if (j == sP.audioToPlay.size() - 2) {
						dataString += s;
					} else {
						dataString += s + "-";
					}
				}

				if (sP.audioToPlay.size() == 0) {
					dataString += "-1";
				}

				dataString += ":";
				sP.audioToPlay = new ArrayList<String>();

				// Any audio files that were supposed to be activated have a distance from the
				// player
				// that they were activated from to seem realistic.s
				for (int j = 0; j < sP.audioDistances.size(); j++) {
					int dist = sP.audioDistances.get(j).intValue();

					if (j == sP.audioDistances.size() - 2) {
						dataString += dist;
					} else {
						dataString += dist + "-";
					}
				}

				if (sP.audioDistances.size() == 0) {
					dataString += "-1";
				}

				dataString += ";";
				sP.audioDistances = new ArrayList<Integer>();
			} else {

				dataString += "Client:" + sP.x + ":" + sP.y + ":" + sP.z + ":" + clientID + ":" + sP.health + ":"
						+ sP.maxHealth + ":" + sP.armor + ":" + sP.environProtectionTime + ":" + sP.immortality + ":"
						+ sP.vision + ":" + sP.invisibility + ":" + sP.height + ":" + sP.rotation + ":" + sP.xEffects
						+ ":" + sP.yEffects + ":" + sP.zEffects + ":" + sP.alive + ":" + sP.kills + ":" + sP.deaths
						+ ":" + sP.forceCrouch + ":" + sP.hasBlueKey + ":" + sP.hasRedKey + ":" + sP.hasGreenKey + ":"
						+ sP.hasYellowKey + ":" + sP.resurrections + ":" + sP.weaponEquipped + ":";

				// Weapons this player should have
				for (int j = 0; j < sP.weapons.length; j++) {
					Weapon w = sP.weapons[j];
					int size = w.cartridges.size();

					dataString += w.weaponID + "," + w.canBeEquipped + "," + w.dualWield + "," + w.ammo;

					if (w.canBeEquipped) {
						// System.out.println(w.weaponID + ":" + w.dualWield);
					}

					for (int k = 0; k < size; k++) {
						int cartSize = w.cartridges.get(k).ammo;
						dataString += "," + cartSize;
					}

					if (j < sP.weapons.length - 1) {
						dataString += "-";
					}
				}

				dataString += ":";

				// All messages that should have been displayed this tick for the client.
				for (int j = 0; j < sP.clientMessages.size(); j++) {
					PopUp p = sP.clientMessages.get(j);

					if (j == sP.clientMessages.size() - 2) {
						dataString += p.getText();
					} else {
						dataString += p.getText() + "-";
					}
				}

				if (sP.clientMessages.size() == 0) {
					dataString += "-1";
				}

				dataString += ":";
				sP.clientMessages = new ArrayList<PopUp>();

				// Any audio files that were supposed to be activated this tick for the client.
				for (int j = 0; j < sP.audioToPlay.size(); j++) {
					String s = sP.audioToPlay.get(j);

					if (j == sP.audioToPlay.size() - 2) {
						dataString += s;
					} else {
						dataString += s + "-";
					}
				}

				if (sP.audioToPlay.size() == 0) {
					dataString += "-1";
				}

				dataString += ":";
				sP.audioToPlay = new ArrayList<String>();

				// Any audio files that were supposed to be activated have a distance from the
				// player
				// that they were activated from to seem realistic.s
				for (int j = 0; j < sP.audioDistances.size(); j++) {
					int dist = sP.audioDistances.get(j).intValue();

					if (j == sP.audioDistances.size() - 2) {
						dataString += dist;
					} else {
						dataString += dist + "-";
					}
				}

				if (sP.audioDistances.size() == 0) {
					dataString += "-1";
				}

				dataString += ";";
				sP.audioDistances = new ArrayList<Integer>();
			}

		}

		/*
		 * if (firstTime) { dataString += "?"; dataString += "Walls: " + Level.height +
		 * " : " + Level.width + " ; ";
		 * 
		 * for (int i = 0; i < Level.blocks.length; i++) { Block b = Level.blocks[i];
		 * dataString += (b.health + " : " + b.x + " : " + b.y + " : " + b.z + " : " +
		 * b.wallID + " : " + b.wallPhase + " : " + b.height + " : " + b.isSolid + " : "
		 * + b.seeThrough + ";"); }
		 * 
		 * firstTime = false; }
		 */

		dataString += "?";
		dataString += "Items;";

		for (int i = 0; i < Game.items.size(); i++) {
			Item item = Game.items.get(i);
			dataString += (item.itemID + ":" + item.x + ":" + item.y + ":" + item.z + ":" + item.rotation + ":"
					+ item.itemActivationID + ":" + item.pickedUp + ":" + item.tickCount + ":" + item.phaseTime + ";");
		}

		dataString += "?";
		dataString += "Bullets;";
		for (int i = 0; i < Game.bullets.size(); i++) {
			Bullet b = Game.bullets.get(i);
			dataString += (b.ID + ":" + b.x + ":" + b.y + ":" + b.z + ";");
		}

		dataString += "?";
		dataString += "Explosions;";

		for (int i = 0; i < Game.explosions.size(); i++) {
			Explosion exp = Game.explosions.get(i);
			dataString += (exp.ID + ":" + exp.phaseTime + ":" + exp.x + ":" + exp.y + ":" + exp.z + ";");
		}

		/*
		 * dataString += "?"; dataString += "Buttons:";
		 * 
		 * for (int i = 0; i < Game.buttons.size(); i++) { Button b =
		 * Game.buttons.get(i); dataString += (b.itemID + " : " + b.itemActivationID +
		 * " : " + b.x + " : " + b.y + " : " + b.z + " : " + b.pressed + ";"); }
		 * 
		 * dataString += "?"; dataString += "Doors:";
		 * 
		 * for (int i = 0; i < Game.doors.size(); i++) { Door d = Game.doors.get(i);
		 * dataString += (d.ID + " : " + d.itemActivationID + " : " + d.xPos + " : " +
		 * d.yPos + " : " + d.zPos + " : " + d.doorX + " : " + d.doorZ + " : " + d.time
		 * + " : " + d.soundTime + " : " + d.doorType + " : " + d.doorY + " : " +
		 * d.maxHeight + ";"); }
		 * 
		 * dataString += "?"; dataString += "Elevators:";
		 * 
		 * for (int i = 0; i < Game.elevators.size(); i++) { Elevator ele =
		 * Game.elevators.get(i); dataString += (ele.ID + " : " + ele.itemActivationID +
		 * " : " + ele.xPos + " : " + ele.yPos + " : " + ele.zPos + " : " +
		 * ele.elevatorX + " : " + ele.elevatorZ + " : " + ele.height + " : " +
		 * ele.soundTime + " : " + ele.movingUp + " : " + ele.movingDown + " : " +
		 * ele.waitTime + " : " + ele.upHeight + " : " + ele.activated + " : " +
		 * ele.maxHeight + ";"); }
		 */
		/*
		 * dataString += "?"; dataString += "HitSprites:";
		 * 
		 * for (int i = 0; i < Game.sprites.size(); i++) { HitSprite hS =
		 * Game.sprites.get(i); dataString += (hS.ID + " : " + hS.x + " : " + hS.y +
		 * " : " + hS.z + " : " + hS.phaseTime + ";"); }
		 */

		return dataString;
	}

	/**
	 * Loads data that the client sends in, into the host game and updates those
	 * values.
	 * 
	 * @param in
	 */
	public void loadDataFromClient(String inputLine) {

		String[] elements = inputLine.split("\\?");

		// take the first element ServerPlayer and break it down
		String[] playerList = elements[0].split(":");

		// assign each piece of the element to the ServerPlayer object
		ServerPlayer sP = Game.otherPlayers.get(clientID);

		sP.x = Double.parseDouble(playerList[0]);
		sP.y = Double.parseDouble(playerList[1]);
		sP.z = Double.parseDouble(playerList[2]);
		sP.ID = clientID;
		sP.rotation = Double.parseDouble(playerList[3]);
		sP.health = Integer.parseInt(playerList[4]);
		sP.maxHealth = Integer.parseInt(playerList[5]);
		sP.armor = Integer.parseInt(playerList[6]);
		sP.immortality = Integer.parseInt(playerList[7]);
		sP.godModeOn = Boolean.parseBoolean(playerList[8]);
//		sP.forceCrouch = Boolean.parseBoolean(playerList[23]);
		sP.hasGreenKey = Boolean.parseBoolean(playerList[14]);
		sP.hasRedKey = Boolean.parseBoolean(playerList[13]);
		sP.hasYellowKey = Boolean.parseBoolean(playerList[16]);
		sP.hasBlueKey = Boolean.parseBoolean(playerList[15]);
		sP.alive = Boolean.parseBoolean(playerList[9]);
		sP.kills = Integer.parseInt(playerList[10]);
		sP.deaths = Integer.parseInt(playerList[11]);
		sP.weaponEquipped = Integer.parseInt(playerList[12]);
		sP.xEffects = Double.parseDouble(playerList[17]);
		sP.yEffects = Double.parseDouble(playerList[18]);
		sP.zEffects = Double.parseDouble(playerList[19]);

		if (sP.ID == 0) {
			Display.hostTick = 1;
		}

		// split weaponList into an array
		String[] weapons = playerList[20].split("-");
		/*
		 * For each weapon, load in its attributes depending on what they were when the
		 * game was saved.
		 */
		for (int i = 0; i < weapons.length; i++) {
			Weapon w = sP.weapons[i];
			w.cartridges = new ArrayList<Cartridge>();

			String[] weaponStats = weapons[i].split(",");

			int size = weaponStats.length - 4;

			w.weaponID = Integer.parseInt(weaponStats[0]);
			w.canBeEquipped = Boolean.parseBoolean(weaponStats[1]);
			w.dualWield = Boolean.parseBoolean(weaponStats[2]);
			w.ammo = Integer.parseInt(weaponStats[3]);

			for (int j = 0; j < size; j++) {
				w.cartridges.add(new Cartridge(Integer.parseInt(weaponStats[4 + j])));
			}
		}

		// sP.clientMessages = new ArrayList<PopUp>();
		// sP.audioToPlay = new ArrayList<String>();
		// sP.audioDistances = new ArrayList<Integer>();

		String[] bulletStrings = elements[1].split(";");

		if (bulletStrings.length > 1 && bulletStrings[0].trim().equals("Bullets")) {
			for (int i = 1; i < bulletStrings.length; i++) {
				String[] boolets = bulletStrings[i].split(":"); // array is delimited by ,
				Bullet b = new Bullet(Integer.parseInt(boolets[0]), // damage
						Double.parseDouble(boolets[1]), // speed
						Double.parseDouble(boolets[2]), // x
						Double.parseDouble(boolets[3]), // y
						Double.parseDouble(boolets[4]), // z
						Integer.parseInt(boolets[5]), // ID
						Double.parseDouble(boolets[6]), // rotation
						false); // criticalHit
				Game.bullets.add(b);

				b.upRotation = Double.parseDouble(boolets[7]); // uprotation
				b.clientID = clientID;
			}
		}

		String currentSection = elements[2];
		playerList = currentSection.split(";");

		for (String player : playerList) {
			String[] attributes = player.split(":");
			ServerPlayer serverP = Game.otherPlayers.get(Integer.parseInt(attributes[4]));

			serverP.x = Double.parseDouble(attributes[1]);
			serverP.y = Double.parseDouble(attributes[2]);
			serverP.z = Double.parseDouble(attributes[3]);
			serverP.ID = Integer.parseInt(attributes[4]);
			serverP.health = Integer.parseInt(attributes[5]);
			serverP.maxHealth = Integer.parseInt(attributes[6]);
			serverP.armor = Integer.parseInt(attributes[7]);
			serverP.environProtectionTime = Integer.parseInt(attributes[8]);
			serverP.immortality = Integer.parseInt(attributes[9]);
			serverP.vision = Integer.parseInt(attributes[10]);
			serverP.invisibility = Integer.parseInt(attributes[11]);
			serverP.height = Double.parseDouble(attributes[12]);
			serverP.rotation = Double.parseDouble(attributes[13]);
			serverP.xEffects = Double.parseDouble(attributes[14]);
			serverP.yEffects = Double.parseDouble(attributes[15]);
			serverP.zEffects = Double.parseDouble(attributes[16]);
			serverP.alive = Boolean.parseBoolean(attributes[17]);
			serverP.kills = Integer.parseInt(attributes[18]);
			serverP.deaths = Integer.parseInt(attributes[19]);
			serverP.forceCrouch = Boolean.parseBoolean(attributes[20]);
			serverP.hasBlueKey = Boolean.parseBoolean(attributes[21]);
			serverP.hasRedKey = Boolean.parseBoolean(attributes[22]);
			serverP.hasGreenKey = Boolean.parseBoolean(attributes[23]);
			serverP.hasYellowKey = Boolean.parseBoolean(attributes[24]);
			serverP.resurrections = Integer.parseInt(attributes[25]);
			serverP.weaponEquipped = Integer.parseInt(attributes[26]);

			weapons = attributes[27].split("-");
			/*
			 * For each weapon, load in its attributes depending on what they were when the
			 * game was saved.
			 */
			for (int i = 0; i < weapons.length; i++) {
				Weapon w = serverP.weapons[i];

				String[] weaponStats = weapons[i].split(",");

				int size = weaponStats.length - 4;

				w.weaponID = Integer.parseInt(weaponStats[0]);
				w.canBeEquipped = Boolean.parseBoolean(weaponStats[1]);
				w.dualWield = Boolean.parseBoolean(weaponStats[2]);
				w.ammo = Integer.parseInt(weaponStats[3]);

				for (int j = 0; j < size; j++) {
					w.cartridges.add(new Cartridge(Integer.parseInt(weaponStats[4 + j])));
				}
			}

			// Pop up messages are added immediately
			String[] messages = attributes[28].split("-");

			if (!messages[0].trim().equals("-1")) {
				for (String message : messages) {
					if (!message.trim().equals("1") && !message.trim().equals("")) {
						serverP.clientMessages.add(new PopUp(message));
					}
				}
			}

			// Audio stuff that should also be played immediately every tick
			String[] audioNames = attributes[29].split("-");
			String[] distances = attributes[20].split("-");

			if (!audioNames[0].trim().equals("-1") && !distances[0].trim().equals("-1")
					&& audioNames.length == distances.length) {
				for (int i = 0; i < audioNames.length; i++) {
					serverP.audioToPlay.add(audioNames[i]);
					serverP.audioDistances.add(new Integer(distances[i]));
				}
			}
			Game.otherPlayers.set(serverP.ID, serverP);
		}

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
