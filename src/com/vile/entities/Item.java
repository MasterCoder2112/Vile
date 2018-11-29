package com.vile.entities;

import com.vile.Display;
import com.vile.Game;
import com.vile.PopUp;
import com.vile.Sound;
import com.vile.SoundController;
import com.vile.graphics.Render;
import com.vile.graphics.Render3D;
import com.vile.launcher.FPSLauncher;
import com.vile.levelGenerator.Block;
import com.vile.levelGenerator.Level;

/**
 * Title: Item
 * 
 * @author Alex Byrd Date updated: 5/12/2017
 *
 *
 *         Description: Creates an Item with a given itemValue, itemID, and
 *         x,y,z values. Items can also be activated when the player picks them
 *         up, doing certain things depending on the items id.
 * 
 *         Phase time keeps track of the texture phase the item is on right now.
 *         Used for animated items.
 */
public class Item {
	// Normal values
	public int itemID = 0;
	public int itemValue = 0;
	public int itemActivationID = 0;
	public int rotation = 0;
	public double x;
	public double y;
	public double z;
	public double height = 8;

	// For floating items
	public double floatHeight = 0;
	public double floatAmount = 0.001;
	public double floated = 0;

	// Distance from player
	public double distanceFromPlayer = 0;

	// To be added when physics is added to items
	public double movementSpeedX = 0;
	public double movementSpeedZ = 0;

	public int phaseTime = 0;
	public int size = 160;

	// Flags for items
	public boolean isSeeable = true;
	public boolean hasAmmo = false;
	public boolean activated = false;
	public boolean isSolid = false;
	public boolean aboveBlock = true;

	/*
	 * The Audio clip after activating this object will be this. Notice this is
	 * completely different than the default sound this item will play when picked
	 * up. This is for announcments, or special story line queues and such.
	 */
	public String audioQueue = "";

	// Items image to render
	public Render itemImage = null;

	// Items will tick so that they respawn if respawn is turned on.
	public int tickCount = 0;
	public boolean pickedUp = false;

	// What pixels does this render on the screen
	// public ArrayList<Integer> pixelsOnScreen = new ArrayList<Integer>();

	/**
	 * Instantiates an item object
	 * 
	 * @param value
	 * @param x
	 * @param y
	 * @param z
	 * @param ID
	 */
	public Item(int value, double x, double y, double z, int ID, int rotation, int itemActID, String audioQueue) {
		itemValue = value;
		itemID = ID;
		this.x = x;
		this.y = y;
		this.z = z;
		this.rotation = rotation;
		this.itemActivationID = itemActID;
		this.audioQueue = audioQueue;

		// If the audioQueue is -1 then that means it is a null
		// audioQueue, meaning there is none, so make it a blank
		// string.
		if (audioQueue.equals("-1")) {
			this.audioQueue = "";
		}

		// If it is a shell item, it has a default amount of 4
		if (itemID == ItemNames.SHELLS.getID()) {
			itemValue = ItemNames.SHELLS.getValue();
			hasAmmo = true;
		}
		// If the item is a shotgun, it carries slightly
		// more shells than A normal shell casing.
		else if (itemID == ItemNames.SHOTGUN.getID()) {
			itemValue = ItemNames.SHELLS.getValue();
			hasAmmo = true;
		}
		// If a Phase chargePack
		else if (itemID == ItemNames.SMALLCHARGE.getID()) {
			itemValue = ItemNames.SMALLCHARGE.getValue();
			hasAmmo = true;
		}
		// If a large Phase charge pack
		else if (itemID == ItemNames.LARGECHARGE.getID()) {
			itemValue = ItemNames.LARGECHARGE.getValue();
			hasAmmo = true;
		}
		// If box of shells
		else if (itemID == ItemNames.SHELLBOX.getID()) {
			itemValue = ItemNames.SHELLBOX.getValue();
			hasAmmo = true;
		}
		// Pistol
		else if (itemID == ItemNames.PISTOL.getID()) {
			itemValue = ItemNames.PISTOL.getValue();
			hasAmmo = true;
		}
		// If clip of bullets
		else if (itemID == ItemNames.BULLETS.getID()) {
			itemValue = ItemNames.BULLETS.getValue();
			hasAmmo = true;
		}
		// If box of bullets
		else if (itemID == ItemNames.BOXOFBULLETS.getID()) {
			itemValue = ItemNames.BOXOFBULLETS.getValue();
			hasAmmo = true;
		}

		// If Rocket Launcher
		else if (itemID == ItemNames.ROCKETLAUNCHER.getID()) {
			itemValue = ItemNames.ROCKETLAUNCHER.getValue();
			hasAmmo = true;
		}

		// Rocket ammo
		else if (itemID == ItemNames.ROCKETS.getID()) {
			itemValue = ItemNames.ROCKETS.getValue();
			hasAmmo = true;
		}

		// Rocket Crate
		else if (itemID == ItemNames.ROCKETCRATE.getID()) {
			itemValue = ItemNames.ROCKETCRATE.getValue();
			hasAmmo = true;
		}

		// If Phase Cannon Weapon
		else if (itemID == ItemNames.PHASECANNON.getID()) {
			itemValue = ItemNames.PHASECANNON.getValue();
			hasAmmo = true;
		}

		/*
		 * Any solid objects treat differently
		 */
		if (itemID >= ItemNames.TORCH.getID() && itemID <= ItemNames.CANISTER.getID()
				|| itemID == ItemNames.HOLYWATER.getID() || itemID == ItemNames.TABLE.getID()
				|| itemID == ItemNames.LAMPTABLE.getID() || itemID == ItemNames.RADAR.getID()
				|| itemID == ItemNames.DARKBOOK.getID()
				|| itemID >= ItemNames.TECHBARREL.getID() && itemID <= ItemNames.ROCK3.getID()
				|| itemID == ItemNames.REDBANNER.getID() || itemID == ItemNames.REDPILLAR.getID()
				|| itemID == ItemNames.BLUEPILLAR.getID() || itemID == ItemNames.BLUEBANNER.getID()
				|| itemID >= ItemNames.BURNINGMAN.getID() && itemID < ItemNames.STRIPCORPSE.getID()) {
			// Have a lower height so player can jump on them
			if (itemID == ItemNames.HOLYWATER.getID() || itemID == ItemNames.TABLE.getID()
					|| itemID == ItemNames.LAMPTABLE.getID() || itemID == ItemNames.DARKBOOK.getID()
					|| itemID == ItemNames.TOILET.getID() || itemID == ItemNames.TRASH1.getID()
					|| itemID == ItemNames.TRASH2.getID() || itemID == ItemNames.TRASH3.getID()
					|| itemID == ItemNames.ROCK1.getID() || itemID == ItemNames.ROCK2.getID()
					|| itemID == ItemNames.ROCK3.getID() || itemID == ItemNames.CHAIR.getID()
					|| itemID == ItemNames.TECHCANISTER.getID() || itemID == ItemNames.TECHBARREL.getID()) {
				height = 5;
			}

			// They are solid, so add them to the solid items list
			isSolid = true;
			Game.solidItems.add(this);
		}

		// If in smileMode, add this to the happySavers arraylist
		if (itemID == ItemNames.HOLYWATER.getID() && Display.smileMode) {
			Game.happySavers.add(this);
		}

		/*
		 * Depending on skillmode, change the value of the objects you pick up to make
		 * it easier and or harder to obtain said item.
		 */
		if (FPSLauncher.modeChoice <= 1) {
			itemValue *= 1.5;
		} else if (FPSLauncher.modeChoice >= 3) {
			itemValue *= 0.75;
		}

		// If secret object, add to secrets in map
		if (itemID == ItemNames.SECRET.getID()) {
			Game.secretsInMap++;
		}

		// Secrets, Hurting blocks, doors, etc...
		// Unseeable items that activate things
		if (itemID >= ItemNames.SPAWN.getID() && itemID <= ItemNames.YELLOWDOOR.getID()
				|| itemID == ItemNames.TOXICWASTE.getID() || itemID == ItemNames.LAVA.getID()
				|| itemID == ItemNames.SECRET.getID() || itemID == ItemNames.LINEDEF.getID()
				|| itemID == ItemNames.ENEMYSPAWN.getID() || itemID == ItemNames.WALLBEGONE.getID()
				|| itemID == ItemNames.ACTIVATEEXP.getID() || itemID == ItemNames.WATER.getID()) {
			isSeeable = false;
		}

		// Always add item to general item list
		Game.items.add(this);

		/*
		 * Some items are bigger or smaller than others so fix that here.
		 */
		if (ID == ItemNames.MEGAHEALTH.getID() || ID == ItemNames.SKULLOFRES.getID() || ID == ItemNames.GOBLET.getID()
				|| ID >= 33) {
			size = 240;
		}

		if (ID == ItemNames.HEALTHPACK.getID()) {
			size = 80;
		}

		if (ID >= 71) {
			size = 420;
		}

		if (ID == ItemNames.SHOTGUN.getID() || ID == ItemNames.BIOSUIT.getID()) {
			size = 480;
		}

		if (ID >= 29 && ID < 33 || ID == 39 || ID == 42 || ID == 43 || ID == ItemNames.BONEPILE.getID()
				|| ID == ItemNames.TELEPORTERENTER.getID() || ID == ItemNames.TELEPORTEREXIT.getID()
				|| ID == ItemNames.REDBANNER.getID() || ID == ItemNames.BLUEBANNER.getID()
				|| ID == ItemNames.BURNINGMAN.getID() || ID == ItemNames.TECHLAMP1.getID()
				|| ID == ItemNames.TECHLAMP2.getID() || ID == ItemNames.BUSH1.getID()
				|| ID == ItemNames.BUSH2.getID()) {
			size = 600;
		}

		// If Satillite
		if (ID == ItemNames.RADAR.getID() || ID == ItemNames.GREENPILLAR.getID() || ID == ItemNames.BLUEPILLAR.getID()
				|| ID == ItemNames.REDPILLAR.getID() || ID == ItemNames.TREEALIVE.getID()
				|| ID == ItemNames.STALAG1.getID() || ID == ItemNames.STALAG2.getID()
				|| ID == ItemNames.TECHPILLAR1.getID() || ID == ItemNames.TECHPILLAR2.getID()
				|| ID == ItemNames.TECHPILLAR3.getID()) {
			size = 2048;
		}

		/*
		 * Figures out if item should float or not
		 */
		if (!isSolid && (ID == ItemNames.VIAL.getID() || ID == ItemNames.SHARD.getID()
				|| (ID >= ItemNames.REDKEY.getID() && ID <= ItemNames.YELLOWKEY.getID())
				|| ID == ItemNames.PISTOL.getID() || ID == ItemNames.SHOTGUN.getID()
				|| ID == ItemNames.PHASECANNON.getID() || ID == ItemNames.ROCKETLAUNCHER.getID()
				|| ID == ItemNames.UPGRADE.getID() || ID == ItemNames.GOBLET.getID()
				|| ID == ItemNames.ADRENALINE.getID() || ID == ItemNames.VISIONGLASSES.getID()
				|| ID == ItemNames.INVISEMERALD.getID() || ID == ItemNames.DECIETSCEPTER.getID())) {
			floatHeight = 0.2;
			floatAmount = 0.01;
		}
	}

	/**
	 * Activates a particular item, meaning depending on its ID it performs the task
	 * it is designated to such as healing the player a certain amount for happiness
	 * boosts, or giving the player ammo if shells, etc...
	 * 
	 * It returns whether the item could actually be used or not. If the player
	 * could be healed for instance it returns true to remove the item from the map,
	 * otherwise if the player is at full health it will return false as the item
	 * was not used.
	 * 
	 * @return
	 */
	public boolean activate() {

		// If item was pickedUp then don't activate it again until respawned
		if (pickedUp) {
			return false;
		}

		// Was item used?
		boolean itemUsed = false;

		distanceFromPlayer = Math.sqrt(((Math.abs(x - Player.x)) * (Math.abs(x - Player.x)))
				+ ((Math.abs(z - Player.z)) * (Math.abs(z - Player.z))));

		/*
		 * If MegaHealth, then heal the player completely.
		 */
		if (itemID == ItemNames.MEGAHEALTH.getID()) {
			if (Player.health < Player.maxHealth) {
				Player.health = Player.maxHealth;

				// Displays that the player picked up the megahealth
				if (Display.smileMode) {
					Display.messages.add(new PopUp("Picked up the MEGAHAPPINESS!"));
				} else {
					Display.messages.add(new PopUp("Picked up the MEGAHEALTH!"));
				}

				// Mega Item pick up sound
				SoundController.megaPickUp.playAudioFile(distanceFromPlayer);

				itemUsed = true;
			}
		}
		/*
		 * If a Health pack, heal the player accordingly
		 */
		else if (itemID == ItemNames.HEALTHPACK.getID()) {
			if (Player.health >= 100) {
				return false;
			}

			Player.health += ItemNames.HEALTHPACK.getValue();

			if (Player.health >= 100) {
				Player.health = 100;
			}

			// Displays that the player picked up a Health Pack
			if (Display.smileMode) {
				Display.messages.add(new PopUp("It probably contained puppies and stuff"));
				Display.messages.add(new PopUp("A happy pack has made you more happy!"));
			} else {
				Display.messages.add(new PopUp("You heal yourself with a healthpack!"));
			}

			// Play correlating sound effect
			SoundController.healthBig.playAudioFile(distanceFromPlayer);

			itemUsed = true;
		}

		/*
		 * With anything that gives a weapon ammo, check to see if the ammo can be added
		 * or not, and then if it is, pick up the item and display what you picked up.
		 * Add the ammo to its corresponding weapon as well.
		 */
		else if (hasAmmo) {
			if (itemID == ItemNames.PISTOL.getID() || itemID == ItemNames.BULLETS.getID()
					|| itemID == ItemNames.BOXOFBULLETS.getID()) {
				itemUsed = Player.weapons[0].addAmmo(itemValue);
			} else if (itemID == ItemNames.SHELLS.getID() || itemID == ItemNames.SHOTGUN.getID()
					|| itemID == ItemNames.SHELLBOX.getID()) {
				itemUsed = Player.weapons[1].addAmmo(itemValue);
			} else if (itemID == ItemNames.PHASECANNON.getID() || itemID == ItemNames.SMALLCHARGE.getID()
					|| itemID == ItemNames.LARGECHARGE.getID()) {
				itemUsed = Player.weapons[2].addAmmo(itemValue);
			}
			// Rocket Launcher stuff
			else {
				itemUsed = Player.weapons[3].addAmmo(itemValue);
			}

			/*
			 * Return if ammo couldn't be added for any reason and the Player does not have
			 * the weapon. If the player doesn't have the weapon, it will not be equipped
			 * but ammo won't be added.
			 */
			if (!itemUsed) {
				if (itemID == ItemNames.PHASECANNON.getID() && !Player.weapons[2].canBeEquipped
						|| itemID == ItemNames.SHOTGUN.getID() && !Player.weapons[1].canBeEquipped
						|| itemID == ItemNames.PISTOL.getID() && !Player.weapons[0].dualWield
						|| itemID == ItemNames.ROCKETLAUNCHER.getID() && !Player.weapons[3].canBeEquipped) {

				} else {
					return false;
				}
			}

			if (itemID == ItemNames.SHELLS.getID()) {
				if (Display.smileMode) {
					Display.messages.add(new PopUp("Man this stuff is powerful"));
					Display.messages.add(new PopUp("You picked up some joy!"));
				} else {
					Display.messages.add(new PopUp("You picked up some shotgun shells!"));
				}

				try {
					// Play ammo pick up sound effect
					SoundController.clip.playAudioFile(distanceFromPlayer);
				} catch (Exception e) {

				}
			} else if (itemID == ItemNames.SHOTGUN.getID()) {
				// Displays that the player picked up the joy Spreader
				if (Display.smileMode) {
					Display.messages.add(new PopUp("Better make like Jesus and spread that joy"));
					Display.messages.add(new PopUp("You found the joy spreader!"));
				} else {
					Display.messages.add(new PopUp("You found the shotgun!"));
				}

				// Weapon Pick up sound
				SoundController.weaponPickUp.playAudioFile(distanceFromPlayer);

				// If weapon can't already be equipped, make it so that
				// it can be, and equip it
				if (!Player.weapons[1].canBeEquipped) {
					Player.weapons[1].canBeEquipped = true;
					Player.weaponEquipped = 1;
				}
			}

			else if (itemID == ItemNames.SHELLBOX.getID()) {
				if (Display.smileMode) {
					Display.messages.add(new PopUp("More Joy than you can handle"));
					Display.messages.add(new PopUp("You found a box of joy!"));
				} else {
					Display.messages.add(new PopUp("You pick up a shellbox!"));
				}

				// Play ammo pick up sound effect
				SoundController.clip.playAudioFile(distanceFromPlayer);
			} else if (itemID == ItemNames.PHASECANNON.getID()) {
				if (Display.smileMode) {
					Display.messages.add(new PopUp("It doesn't sound peaceful but trust me it is"));
					Display.messages.add(new PopUp("You found a Peacecannon!"));
				} else {
					Display.messages.add(new PopUp("You found the Phasecannon!"));
				}

				// Weapon Pick up sound
				SoundController.weaponPickUp.playAudioFile(distanceFromPlayer);

				// If weapon can't already be equipped, make it so that
				// it can be, and equip it
				if (!Player.weapons[2].canBeEquipped) {
					Player.weapons[2].canBeEquipped = true;
					Player.weaponEquipped = 2;
				}
			} else if (itemID == ItemNames.SMALLCHARGE.getID()) {
				if (Display.smileMode) {
					Display.messages.add(new PopUp("A Little bit of peace please?"));
					Display.messages.add(new PopUp("You found a small peace charge!"));
				} else {
					Display.messages.add(new PopUp("You found a small phase charge!"));
				}

				// Play ammo pick up sound effect
				SoundController.clip.playAudioFile(distanceFromPlayer);
			} else if (itemID == ItemNames.LARGECHARGE.getID()) {
				if (Display.smileMode) {
					Display.messages.add(new PopUp("We charge in peace"));
					Display.messages.add(new PopUp("You found a large Peace charge!"));
				} else {
					Display.messages.add(new PopUp("You found a large Phase charge!"));
				}

				// Play ammo pick up sound effect
				SoundController.clip.playAudioFile(distanceFromPlayer);
			} else if (itemID == ItemNames.PISTOL.getID()) {
				// Weapon Pick up sound
				SoundController.weaponPickUp.playAudioFile(distanceFromPlayer);

				// If weapon cant be dual wielded yet, make it so that it
				// is
				if (!Player.weapons[0].dualWield) {
					Player.weapons[0].dualWield = true;
					Player.weaponEquipped = 0;
					Display.messages.add(new PopUp("Hell yeah! Dual wielding!"));

					if (Display.smileMode) {
						Display.messages
								.add(new PopUp("Never can make too many people fall in love. (Except for you...)"));
						Display.messages.add(new PopUp("Heck yeah! Making Cupid proud!"));
					} else {
						Display.messages.add(new PopUp("Hell yeah! Dual wielding!"));
					}
				} else {
					if (Display.smileMode) {
						Display.messages.add(new PopUp("All the more to love you my dear"));
						Display.messages.add(new PopUp("You pick up a cupids bow!"));
					} else {
						Display.messages.add(new PopUp("You pick up a pistol!"));
					}
				}
			} else if (itemID == ItemNames.BULLETS.getID()) {
				if (Display.smileMode) {
					Display.messages.add(new PopUp("I need your love"));
					Display.messages.add(new PopUp("You pick up some love arrows!"));
				} else {
					Display.messages.add(new PopUp("You pick up a bullet clip!"));
				}

				// Play ammo pick up sound effect
				SoundController.clip.playAudioFile(distanceFromPlayer);
			} else if (itemID == ItemNames.BOXOFBULLETS.getID()) {
				if (Display.smileMode) {
					Display.messages.add(new PopUp("Give me all your love! ALL OF IT"));
					Display.messages.add(new PopUp("You found a quiver of love arrows!"));
				} else {
					Display.messages.add(new PopUp("You found a box of bullets!"));
				}

				// Play ammo pick up sound effect
				SoundController.clip.playAudioFile(distanceFromPlayer);
			}

			else if (itemID == ItemNames.ROCKETLAUNCHER.getID()) {
				if (Display.smileMode) {
					Display.messages.add(new PopUp("Kill them with cuteness"));
					Display.messages.add(new PopUp("No way... The Teddy Bear Launcher!"));
				} else {
					Display.messages.add(new PopUp("OH YEAH! You got the Rocket Launcher"));
				}

				// Weapon Pick up sound
				SoundController.weaponPickUp.playAudioFile(distanceFromPlayer);

				// If weapon can't already be equipped, make it so that
				// it can be, and equip it
				if (!Player.weapons[3].canBeEquipped) {
					Player.weapons[3].canBeEquipped = true;
					Player.weaponEquipped = 3;
				}
			} else if (itemID == ItemNames.ROCKETS.getID()) {
				if (Display.smileMode) {
					Display.messages.add(new PopUp("Take a Teddy Bear to the face!"));
					Display.messages.add(new PopUp("You found some Teddy Bears!"));
				} else {
					Display.messages.add(new PopUp("You found some rockets!"));
				}

				// Play ammo pick up sound effect
				SoundController.clip.playAudioFile(distanceFromPlayer);
			} else if (itemID == ItemNames.ROCKETCRATE.getID()) {
				if (Display.smileMode) {
					Display.messages.add(new PopUp("Contains lots of them Teddy Bears"));
					Display.messages.add(new PopUp("You found a toy box!"));
				} else {
					Display.messages.add(new PopUp("You open a rocket crate!"));
				}

				// Play ammo pick up sound effect
				SoundController.clip.playAudioFile(distanceFromPlayer);
			}
		}

		/*
		 * Keys
		 */
		else if (itemID == ItemNames.REDKEY.getID()) {
			Player.hasRedKey = true;
			itemUsed = true;

			Display.messages.add(new PopUp("You picked up the red keycard!"));

			// Key pick up sound
			SoundController.keyPickUp.playAudioFile(distanceFromPlayer);
		} else if (itemID == ItemNames.BLUEKEY.getID()) {
			Player.hasBlueKey = true;
			itemUsed = true;

			// Displays that the player picked up the Blue key
			Display.messages.add(new PopUp("You picked up the blue keycard!"));

			// Key pick up sound
			SoundController.keyPickUp.playAudioFile(distanceFromPlayer);
		} else if (itemID == ItemNames.GREENKEY.getID()) {
			Player.hasGreenKey = true;
			itemUsed = true;

			// Displays that the player picked up the green key
			Display.messages.add(new PopUp("You picked up the green keycard!"));

			// Key pick up sound
			SoundController.keyPickUp.playAudioFile(distanceFromPlayer);
		} else if (itemID == ItemNames.YELLOWKEY.getID()) {
			Player.hasYellowKey = true;
			itemUsed = true;

			// Displays that the player picked up the yellow key
			Display.messages.add(new PopUp("You picked up the yellow keycard!"));

			// Key pick up sound
			SoundController.keyPickUp.playAudioFile(distanceFromPlayer);
		}
		// If a secret
		else if (itemID == ItemNames.SECRET.getID()) {
			Game.secretsFound++;
			itemUsed = true;

			// Display that a secret was found
			Display.messages.add(new PopUp("A SECRET is discovered!"));
			SoundController.secret.playAudioFile(distanceFromPlayer);
		}
		// Skull of Resurrection
		else if (itemID == ItemNames.SKULLOFRES.getID()) {
			Player.resurrections++;
			itemUsed = true;

			if (Display.smileMode) {
				Display.messages
						.add(new PopUp("If you get sad... I'm always here to make you happy again... temporarily."));
				Display.messages.add(new PopUp("Your happiness has enfused with the Happy Sphere!"));
			} else {
				Display.messages.add(new PopUp("Your soul has binded with the skull of resurrection!"));
			}

			// Creepy sound is played
			SoundController.creepySound.playAudioFile(distanceFromPlayer);
		}
		// Environment Suit
		else if (itemID == ItemNames.BIOSUIT.getID()) {
			Player.environProtectionTime = 1100;
			itemUsed = true;

			Display.messages.add(new PopUp("You adorn the environmental suit!"));

			// Play special pick up sound for this item
			SoundController.specialPickup.playAudioFile(distanceFromPlayer);
		}
		// Goblet of invurnuralbility
		else if (itemID == ItemNames.GOBLET.getID()) {
			Player.immortality = 1100 * (int) Render3D.fpsCheck;
			Player.vision = 1100 * (int) Render3D.fpsCheck;
			itemUsed = true;

			if (Display.smileMode) {
				Display.messages.add(new PopUp("It's Inconcievable!"));
				Display.messages.add(new PopUp("A Goblet of Inconcievable Joy!"));
			} else {
				Display.messages.add(new PopUp("You drink from the Goblet, and play God!"));
			}

			// Play correlating sound effect
			SoundController.healthBig.playAudioFile(distanceFromPlayer);
		}
		// Adrenaline
		else if (itemID == ItemNames.ADRENALINE.getID()) {
			Player.maxHealth++;
			Player.health++;

			itemUsed = true;

			if (Display.smileMode) {
				Display.messages.add(new PopUp("Mmmmm... Donuts..."));
				Display.messages.add(new PopUp("You eat the donut!"));
			} else {
				Display.messages.add(new PopUp("You inject the adrenaline!"));
			}

			// Play special pick up sound for this item
			SoundController.specialPickup.playAudioFile(distanceFromPlayer);
		}
		// Glasses of vision
		else if (itemID == ItemNames.VISIONGLASSES.getID()) {
			Player.vision = 1100 * (int) Render3D.fpsCheck;

			itemUsed = true;
			Display.messages.add(new PopUp("You're vision has been enhanced!"));

			// Mega Item pick up sound
			SoundController.megaPickUp.playAudioFile(distanceFromPlayer);
		}
		// Chainmeal armor
		else if (itemID == ItemNames.CHAINMEAL.getID()) {
			if (Player.armor < 50) {
				Player.armor = ItemNames.CHAINMEAL.getValue();

				itemUsed = true;

				if (Display.smileMode) {
					Display.messages.add(new PopUp("This will help you! I'm Positive!"));
					Display.messages.add(new PopUp("50 Positivity Points!"));
				} else {
					Display.messages.add(new PopUp("You adorn the chainmeal armor!"));
				}

				// Play Armor pick up sound
				SoundController.armorPickup.playAudioFile(distanceFromPlayer);
			} else {
				itemUsed = false;
			}
		}
		// Combat Armor
		else if (itemID == ItemNames.COMBAT.getID()) {
			if (Player.armor < 100) {
				Player.armor = ItemNames.COMBAT.getValue();

				itemUsed = true;

				if (Display.smileMode) {
					Display.messages.add(new PopUp("Full of positive thoughts you are."));
					Display.messages.add(new PopUp("100 Positivity Points!"));
				} else {
					Display.messages.add(new PopUp("You adorn the combat armor!"));
				}

				// Play Armor pick up sound
				SoundController.armorPickup.playAudioFile(distanceFromPlayer);
			} else {
				itemUsed = false;
			}
		}
		// Argent Armor
		else if (itemID == ItemNames.ARGENT.getID()) {
			if (Player.armor < 200) {
				Player.armor = ItemNames.ARGENT.getValue();

				itemUsed = true;

				if (Display.smileMode) {
					Display.messages.add(new PopUp("Positivily Outstanding!"));
					Display.messages.add(new PopUp("200 Positivity Points!"));
				} else {
					Display.messages.add(new PopUp("You adorn the powerful Argent Armor!"));
				}

				// Play Armor pick up sound
				SoundController.armorPickup.playAudioFile(distanceFromPlayer);
			} else {
				itemUsed = false;
			}
		}
		// Armor Shard
		else if (itemID == ItemNames.SHARD.getID()) {
			if (Player.armor < 200) {
				Player.armor++;
			}

			itemUsed = true;

			if (Display.smileMode) {
				Display.messages.add(new PopUp("Keep your chin up! You're doing fine! Be positive."));
				Display.messages.add(new PopUp("1 Positivity Point!"));
			} else {
				Display.messages.add(new PopUp("You pick up an armor shard!"));
			}

			// Play Armor pick up sound
			SoundController.armorPickup.playAudioFile(distanceFromPlayer);
		}
		// Health vial
		else if (itemID == ItemNames.VIAL.getID()) {
			Player.health += ItemNames.VIAL.value;

			itemUsed = true;
			if (Display.smileMode) {
				Display.messages.add(new PopUp("You take your medicine..."));
				Display.messages.add(new PopUp("Hey you're happier now! You're fine!"));
			} else {
				Display.messages.add(new PopUp("You consume the health vial!"));
			}

			// Play correlating sound effect
			SoundController.health.playAudioFile(distanceFromPlayer);
		}
		// Beer
		else if (itemID == ItemNames.BEER.getID()) {
			Player.health += ItemNames.BEER.value;

			itemUsed = true;
			if (Display.smileMode) {
				Display.messages.add(new PopUp("Just taste that carbonation!"));
				Display.messages.add(new PopUp("You consume the Soda!"));
			} else {
				Display.messages.add(new PopUp("You consume the Beer!"));
			}

			// Play correlating sound effect
			SoundController.health.playAudioFile(distanceFromPlayer);
		}
		// Health vial
		else if (itemID == ItemNames.BURGER.getID()) {
			Player.health += ItemNames.BURGER.value;

			itemUsed = true;
			if (Display.smileMode) {
				Display.messages.add(new PopUp("Tastes like American Goodness!"));
				Display.messages.add(new PopUp("You consume the Burger!"));
			} else {
				Display.messages.add(new PopUp("You consume the Burger!"));
			}

			// Play correlating sound effect
			SoundController.health.playAudioFile(distanceFromPlayer);
		}
		// Upgrade point
		else if (itemID == ItemNames.UPGRADE.getID()) {
			itemUsed = true;
			Display.messages.add(new PopUp("No use yet though..."));
			Display.messages.add(new PopUp("You collect an upgrade point!"));

			// Add to upgradePoints player has
			Player.upgradePoints += 1;

			// Play special pick up sound for this item
			SoundController.specialPickup.playAudioFile(distanceFromPlayer);
		}
		// Holy Water
		else if (itemID == ItemNames.HOLYWATER.getID()) {
			itemUsed = false;
			if (Display.smileMode) {
				Display.messages.add(new PopUp("You're already happy, no need to be saved..."));
				return false;
			} else {
				Display.messages.add(new PopUp("You collect the holy water!"));
			}

			// Play correlating sound effect
			SoundController.health.playAudioFile(distanceFromPlayer);

			// Becomes just a normal table
			itemID = 42;
		}
		// Scepter of Deciet
		else if (itemID == ItemNames.DECIETSCEPTER.getID()) {
			itemUsed = true;
			if (Display.smileMode) {
				Display.messages.add(new PopUp("Make those sad faces love for you!"));
				Display.messages.add(new PopUp("You hold the Scepter of Love!"));
			} else {
				Display.messages.add(new PopUp("Power surges from the Scepter of Deciet!"));
			}

			// Play creepy pick up sound for this item
			SoundController.creepySound.playAudioFile(distanceFromPlayer);
		}
		// Invisibility Emerald
		else if (itemID == ItemNames.INVISEMERALD.getID()) {
			itemUsed = true;
			Display.messages.add(new PopUp("You are now invisible!"));

			// Player is now invisible for a certain amount of ticks
			Player.invisibility = 1100 * (int) Render3D.fpsCheck;

			// Play correlating sound effect
			SoundController.health.playAudioFile(distanceFromPlayer);
		}

		return itemUsed;
	}

	/**
	 * Activates an item but it is a little bit different than the method above.
	 * This one activates it only for a particular server player because this method
	 * will only be called for clients connected to a server.
	 * 
	 * @return
	 */
	public boolean activateServer(ServerPlayer sP) {

		// If item was pickedUp then don't activate it again until respawned
		if (pickedUp) {
			return false;
		}

		// Was item used?
		boolean itemUsed = false;

		distanceFromPlayer = Math
				.sqrt(((Math.abs(x - sP.x)) * (Math.abs(x - sP.x))) + ((Math.abs(z - sP.z)) * (Math.abs(z - sP.z))));

		/*
		 * If MegaHealth, then heal the player completely.
		 */
		if (itemID == ItemNames.MEGAHEALTH.getID()) {
			if (sP.health < sP.maxHealth) {
				sP.health = sP.maxHealth;

				// Displays that the player picked up the megahealth
				if (Display.smileMode) {
					sP.clientMessages.add(new PopUp("Picked up the MEGAHAPPINESS!"));
				} else {
					sP.clientMessages.add(new PopUp("Picked up the MEGAHEALTH!"));
				}

				// Add sound and distance from sound to server player
				sP.audioToPlay.add(SoundController.megaPickUp.audioName);
				sP.audioDistances.add((int) (distanceFromPlayer));

				itemUsed = true;
			}
		}
		/*
		 * If a Health pack, heal the player accordingly
		 */
		else if (itemID == ItemNames.HEALTHPACK.getID()) {
			if (sP.health >= 100) {
				return false;
			}

			sP.health += ItemNames.HEALTHPACK.getValue();

			if (sP.health >= 100) {
				sP.health = 100;
			}

			// Displays that the player picked up a Health Pack
			if (Display.smileMode) {
				sP.clientMessages.add(new PopUp("It probably contained puppies and stuff"));
				sP.clientMessages.add(new PopUp("A happy pack has made you more happy!"));
			} else {
				sP.clientMessages.add(new PopUp("You heal yourself with a healthpack!"));
			}

			// Play correlating sound effect
			// Add sound and distance from sound to server player
			sP.audioToPlay.add(SoundController.healthBig.audioName);
			sP.audioDistances.add((int) (distanceFromPlayer));

			itemUsed = true;
		}

		/*
		 * With anything that gives a weapon ammo, check to see if the ammo can be added
		 * or not, and then if it is, pick up the item and display what you picked up.
		 * Add the ammo to its corresponding weapon as well.
		 */
		else if (hasAmmo) {
			if (itemID == ItemNames.PISTOL.getID() || itemID == ItemNames.BULLETS.getID()
					|| itemID == ItemNames.BOXOFBULLETS.getID()) {
				itemUsed = sP.weapons[0].addAmmo(itemValue);
			} else if (itemID == ItemNames.SHELLS.getID() || itemID == ItemNames.SHOTGUN.getID()
					|| itemID == ItemNames.SHELLBOX.getID()) {
				itemUsed = sP.weapons[1].addAmmo(itemValue);
			} else if (itemID == ItemNames.PHASECANNON.getID() || itemID == ItemNames.SMALLCHARGE.getID()
					|| itemID == ItemNames.LARGECHARGE.getID()) {
				itemUsed = sP.weapons[2].addAmmo(itemValue);
			}
			// Rocket Launcher stuff
			else {
				itemUsed = sP.weapons[3].addAmmo(itemValue);
			}

			/*
			 * Return if ammo couldn't be added for any reason and the Player does not have
			 * the weapon. If the player doesn't have the weapon, it will not be equipped
			 * but ammo won't be added.
			 */
			if (!itemUsed) {
				if (itemID == ItemNames.PHASECANNON.getID() && !sP.weapons[2].canBeEquipped
						|| itemID == ItemNames.SHOTGUN.getID() && !sP.weapons[1].canBeEquipped
						|| itemID == ItemNames.PISTOL.getID() && !sP.weapons[0].dualWield
						|| itemID == ItemNames.ROCKETLAUNCHER.getID() && !sP.weapons[3].canBeEquipped) {

				} else {
					return false;
				}
			}

			if (itemID == ItemNames.SHELLS.getID()) {
				if (Display.smileMode) {
					sP.clientMessages.add(new PopUp("Man this stuff is powerful"));
					sP.clientMessages.add(new PopUp("You picked up some joy!"));
				} else {
					sP.clientMessages.add(new PopUp("You picked up some shotgun shells!"));
				}

				try {
					// Play ammo pick up sound effect
					// Add sound and distance from sound to server player
					sP.audioToPlay.add(SoundController.clip.audioName);
					sP.audioDistances.add((int) (distanceFromPlayer));
				} catch (Exception e) {

				}
			} else if (itemID == ItemNames.SHOTGUN.getID()) {
				// Displays that the player picked up the joy Spreader
				if (Display.smileMode) {
					sP.clientMessages.add(new PopUp("Better make like Jesus and spread that joy"));
					sP.clientMessages.add(new PopUp("You found the joy spreader!"));
				} else {
					sP.clientMessages.add(new PopUp("You found the shotgun!"));
				}

				// Weapon Pick up sound
				// Add sound and distance from sound to server player
				sP.audioToPlay.add(SoundController.weaponPickUp.audioName);
				sP.audioDistances.add((int) (distanceFromPlayer));

				// If weapon can't already be equipped, make it so that
				// it can be, and equip it
				if (!sP.weapons[1].canBeEquipped) {
					sP.weapons[1].canBeEquipped = true;
					sP.weaponEquipped = 1;
				}
			}

			else if (itemID == ItemNames.SHELLBOX.getID()) {
				if (Display.smileMode) {
					sP.clientMessages.add(new PopUp("More Joy than you can handle"));
					sP.clientMessages.add(new PopUp("You found a box of joy!"));
				} else {
					sP.clientMessages.add(new PopUp("You pick up a shellbox!"));
				}

				// Play ammo pick up sound effect
				// Add sound and distance from sound to server player
				sP.audioToPlay.add(SoundController.clip.audioName);
				sP.audioDistances.add((int) (distanceFromPlayer));

			} else if (itemID == ItemNames.PHASECANNON.getID()) {
				if (Display.smileMode) {
					sP.clientMessages.add(new PopUp("It doesn't sound peaceful but trust me it is"));
					sP.clientMessages.add(new PopUp("You found a Peacecannon!"));
				} else {
					sP.clientMessages.add(new PopUp("You found the Phasecannon!"));
				}

				// Weapon Pick up sound
				// Add sound and distance from sound to server player
				sP.audioToPlay.add(SoundController.weaponPickUp.audioName);
				sP.audioDistances.add((int) (distanceFromPlayer));

				// If weapon can't already be equipped, make it so that
				// it can be, and equip it
				if (!sP.weapons[2].canBeEquipped) {
					sP.weapons[2].canBeEquipped = true;
					sP.weaponEquipped = 2;
				}
			} else if (itemID == ItemNames.SMALLCHARGE.getID()) {
				if (Display.smileMode) {
					sP.clientMessages.add(new PopUp("A Little bit of peace please?"));
					sP.clientMessages.add(new PopUp("You found a small peace charge!"));
				} else {
					sP.clientMessages.add(new PopUp("You found a small phase charge!"));
				}

				// Play ammo pick up sound effect
				// Add sound and distance from sound to server player
				sP.audioToPlay.add(SoundController.clip.audioName);
				sP.audioDistances.add((int) (distanceFromPlayer));

			} else if (itemID == ItemNames.LARGECHARGE.getID()) {
				if (Display.smileMode) {
					sP.clientMessages.add(new PopUp("We charge in peace"));
					sP.clientMessages.add(new PopUp("You found a large Peace charge!"));
				} else {
					sP.clientMessages.add(new PopUp("You found a large Phase charge!"));
				}

				// Play ammo pick up sound effect
				// Add sound and distance from sound to server player
				sP.audioToPlay.add(SoundController.clip.audioName);
				sP.audioDistances.add((int) (distanceFromPlayer));

			} else if (itemID == ItemNames.PISTOL.getID()) {
				// Weapon Pick up sound
				// Add sound and distance from sound to server player
				sP.audioToPlay.add(SoundController.weaponPickUp.audioName);
				sP.audioDistances.add((int) (distanceFromPlayer));

				// If weapon cant be dual wielded yet, make it so that it
				// is
				if (!sP.weapons[0].dualWield) {
					sP.weapons[0].dualWield = true;
					sP.weaponEquipped = 0;
					sP.clientMessages.add(new PopUp("Hell yeah! Dual wielding!"));

					if (Display.smileMode) {
						sP.clientMessages
								.add(new PopUp("Never can make too many people fall in love. (Except for you...)"));
						sP.clientMessages.add(new PopUp("Heck yeah! Making Cupid proud!"));
					} else {
						sP.clientMessages.add(new PopUp("Hell yeah! Dual wielding!"));
					}
				} else {
					if (Display.smileMode) {
						sP.clientMessages.add(new PopUp("All the more to love you my dear"));
						sP.clientMessages.add(new PopUp("You pick up a cupids bow!"));
					} else {
						sP.clientMessages.add(new PopUp("You pick up a pistol!"));
					}
				}
			} else if (itemID == ItemNames.BULLETS.getID()) {
				if (Display.smileMode) {
					sP.clientMessages.add(new PopUp("I need your love"));
					sP.clientMessages.add(new PopUp("You pick up some love arrows!"));
				} else {
					sP.clientMessages.add(new PopUp("You pick up a bullet clip!"));
				}

				// Play ammo pick up sound effect
				// Add sound and distance from sound to server player
				sP.audioToPlay.add(SoundController.clip.audioName);
				sP.audioDistances.add((int) (distanceFromPlayer));

			} else if (itemID == ItemNames.BOXOFBULLETS.getID()) {
				if (Display.smileMode) {
					sP.clientMessages.add(new PopUp("Give me all your love! ALL OF IT"));
					sP.clientMessages.add(new PopUp("You found a quiver of love arrows!"));
				} else {
					sP.clientMessages.add(new PopUp("You found a box of bullets!"));
				}

				// Play ammo pick up sound effect
				// Add sound and distance from sound to server player
				sP.audioToPlay.add(SoundController.clip.audioName);
				sP.audioDistances.add((int) (distanceFromPlayer));
			}

			else if (itemID == ItemNames.ROCKETLAUNCHER.getID()) {
				if (Display.smileMode) {
					sP.clientMessages.add(new PopUp("Kill them with cuteness"));
					sP.clientMessages.add(new PopUp("No way... The Teddy Bear Launcher!"));
				} else {
					sP.clientMessages.add(new PopUp("OH YEAH! You got the Rocket Launcher"));
				}

				// Weapon Pick up sound
				// Add sound and distance from sound to server player
				sP.audioToPlay.add(SoundController.weaponPickUp.audioName);
				sP.audioDistances.add((int) (distanceFromPlayer));

				// If weapon can't already be equipped, make it so that
				// it can be, and equip it
				if (!sP.weapons[3].canBeEquipped) {
					sP.weapons[3].canBeEquipped = true;
					sP.weaponEquipped = 3;
				}
			} else if (itemID == ItemNames.ROCKETS.getID()) {
				if (Display.smileMode) {
					sP.clientMessages.add(new PopUp("Take a Teddy Bear to the face!"));
					sP.clientMessages.add(new PopUp("You found some Teddy Bears!"));
				} else {
					sP.clientMessages.add(new PopUp("You found some rockets!"));
				}

				// Play ammo pick up sound effect
				// Add sound and distance from sound to server player
				sP.audioToPlay.add(SoundController.clip.audioName);
				sP.audioDistances.add((int) (distanceFromPlayer));

			} else if (itemID == ItemNames.ROCKETCRATE.getID()) {
				if (Display.smileMode) {
					sP.clientMessages.add(new PopUp("Contains lots of them Teddy Bears"));
					sP.clientMessages.add(new PopUp("You found a toy box!"));
				} else {
					sP.clientMessages.add(new PopUp("You open a rocket crate!"));
				}

				// Play ammo pick up sound effect
				// Add sound and distance from sound to server player
				sP.audioToPlay.add(SoundController.clip.audioName);
				sP.audioDistances.add((int) (distanceFromPlayer));
			}
		}

		/*
		 * Keys
		 */
		else if (itemID == ItemNames.REDKEY.getID()) {
			sP.hasRedKey = true;
			itemUsed = true;

			sP.clientMessages.add(new PopUp("You picked up the red keycard!"));

			// Key pick up sound
			// Add sound and distance from sound to server player
			sP.audioToPlay.add(SoundController.keyPickUp.audioName);
			sP.audioDistances.add((int) (distanceFromPlayer));
		} else if (itemID == ItemNames.BLUEKEY.getID()) {
			sP.hasBlueKey = true;
			itemUsed = true;

			// Displays that the player picked up the Blue key
			sP.clientMessages.add(new PopUp("You picked up the blue keycard!"));

			// Key pick up sound
			// Add sound and distance from sound to server player
			sP.audioToPlay.add(SoundController.keyPickUp.audioName);
			sP.audioDistances.add((int) (distanceFromPlayer));
		} else if (itemID == ItemNames.GREENKEY.getID()) {
			sP.hasGreenKey = true;
			itemUsed = true;

			// Displays that the player picked up the green key
			sP.clientMessages.add(new PopUp("You picked up the green keycard!"));

			// Key pick up sound
			// Add sound and distance from sound to server player
			sP.audioToPlay.add(SoundController.keyPickUp.audioName);
			sP.audioDistances.add((int) (distanceFromPlayer));
		} else if (itemID == ItemNames.YELLOWKEY.getID()) {
			sP.hasYellowKey = true;
			itemUsed = true;

			// Displays that the player picked up the yellow key
			sP.clientMessages.add(new PopUp("You picked up the yellow keycard!"));

			// Key pick up sound
			// Add sound and distance from sound to server player
			sP.audioToPlay.add(SoundController.keyPickUp.audioName);
			sP.audioDistances.add((int) (distanceFromPlayer));
		}
		// If a secret
		else if (itemID == ItemNames.SECRET.getID()) {
			Game.secretsFound++;
			itemUsed = true;

			// Display that a secret was found
			sP.clientMessages.add(new PopUp("A SECRET is discovered!"));
			// Add sound and distance from sound to server player
			sP.audioToPlay.add(SoundController.secret.audioName);
			sP.audioDistances.add((int) (distanceFromPlayer));
		}
		// Skull of Resurrection
		else if (itemID == ItemNames.SKULLOFRES.getID()) {
			sP.resurrections++;
			itemUsed = true;

			if (Display.smileMode) {
				sP.clientMessages
						.add(new PopUp("If you get sad... I'm always here to make you happy again... temporarily."));
				sP.clientMessages.add(new PopUp("Your happiness has enfused with the Happy Sphere!"));
			} else {
				sP.clientMessages.add(new PopUp("Your soul has binded with the skull of resurrection!"));
			}

			// Creepy sound is played
			// Add sound and distance from sound to server player
			sP.audioToPlay.add(SoundController.creepySound.audioName);
			sP.audioDistances.add((int) (distanceFromPlayer));
		}
		// Environment Suit
		else if (itemID == ItemNames.BIOSUIT.getID()) {
			sP.environProtectionTime = 1100;
			itemUsed = true;

			sP.clientMessages.add(new PopUp("You adorn the environmental suit!"));

			// Play special pick up sound for this item
			// Add sound and distance from sound to server player
			sP.audioToPlay.add(SoundController.specialPickup.audioName);
			sP.audioDistances.add((int) (distanceFromPlayer));
		}
		// Goblet of invurnuralbility
		else if (itemID == ItemNames.GOBLET.getID()) {
			sP.immortality = 1100 * (int) Render3D.fpsCheck;
			sP.vision = 1100 * (int) Render3D.fpsCheck;
			itemUsed = true;

			if (Display.smileMode) {
				sP.clientMessages.add(new PopUp("It's Inconcievable!"));
				sP.clientMessages.add(new PopUp("A Goblet of Inconcievable Joy!"));
			} else {
				sP.clientMessages.add(new PopUp("You drink from the Goblet, and play God!"));
			}

			// Play correlating sound effect
			// Add sound and distance from sound to server player
			sP.audioToPlay.add(SoundController.healthBig.audioName);
			sP.audioDistances.add((int) (distanceFromPlayer));
		}
		// Adrenaline
		else if (itemID == ItemNames.ADRENALINE.getID()) {
			sP.maxHealth++;
			sP.health++;

			itemUsed = true;

			if (Display.smileMode) {
				sP.clientMessages.add(new PopUp("Mmmmm... Donuts..."));
				sP.clientMessages.add(new PopUp("You eat the donut!"));
			} else {
				sP.clientMessages.add(new PopUp("You inject the adrenaline!"));
			}

			// Play special pick up sound for this item
			// Add sound and distance from sound to server player
			sP.audioToPlay.add(SoundController.specialPickup.audioName);
			sP.audioDistances.add((int) (distanceFromPlayer));
		}
		// Glasses of vision
		else if (itemID == ItemNames.VISIONGLASSES.getID()) {
			sP.vision = 1100 * (int) Render3D.fpsCheck;

			itemUsed = true;
			sP.clientMessages.add(new PopUp("You're vision has been enhanced!"));

			// Mega Item pick up sound
			// Add sound and distance from sound to server player
			sP.audioToPlay.add(SoundController.megaPickUp.audioName);
			sP.audioDistances.add((int) (distanceFromPlayer));
		}
		// Chainmeal armor
		else if (itemID == ItemNames.CHAINMEAL.getID()) {
			if (sP.armor < 50) {
				sP.armor = ItemNames.CHAINMEAL.getValue();

				itemUsed = true;

				if (Display.smileMode) {
					sP.clientMessages.add(new PopUp("This will help you! I'm Positive!"));
					sP.clientMessages.add(new PopUp("50 Positivity Points!"));
				} else {
					sP.clientMessages.add(new PopUp("You adorn the chainmeal armor!"));
				}

				// Play Armor pick up sound
				// Add sound and distance from sound to server player
				sP.audioToPlay.add(SoundController.armorPickup.audioName);
				sP.audioDistances.add((int) (distanceFromPlayer));
			} else {
				itemUsed = false;
			}
		}
		// Combat Armor
		else if (itemID == ItemNames.COMBAT.getID()) {
			if (sP.armor < 100) {
				sP.armor = ItemNames.COMBAT.getValue();

				itemUsed = true;

				if (Display.smileMode) {
					sP.clientMessages.add(new PopUp("Full of positive thoughts you are."));
					sP.clientMessages.add(new PopUp("100 Positivity Points!"));
				} else {
					sP.clientMessages.add(new PopUp("You adorn the combat armor!"));
				}

				// Play Armor pick up sound
				// Add sound and distance from sound to server player
				sP.audioToPlay.add(SoundController.armorPickup.audioName);
				sP.audioDistances.add((int) (distanceFromPlayer));
			} else {
				itemUsed = false;
			}
		}
		// Argent Armor
		else if (itemID == ItemNames.ARGENT.getID()) {
			if (sP.armor < 200) {
				sP.armor = ItemNames.ARGENT.getValue();

				itemUsed = true;

				if (Display.smileMode) {
					sP.clientMessages.add(new PopUp("Positivily Outstanding!"));
					sP.clientMessages.add(new PopUp("200 Positivity Points!"));
				} else {
					sP.clientMessages.add(new PopUp("You adorn the powerful Argent Armor!"));
				}

				// Play Armor pick up sound
				// Add sound and distance from sound to server player
				sP.audioToPlay.add(SoundController.armorPickup.audioName);
				sP.audioDistances.add((int) (distanceFromPlayer));
			} else {
				itemUsed = false;
			}
		}
		// Armor Shard
		else if (itemID == ItemNames.SHARD.getID()) {
			if (sP.armor < 200) {
				sP.armor++;
			}

			itemUsed = true;

			if (Display.smileMode) {
				sP.clientMessages.add(new PopUp("Keep your chin up! You're doing fine! Be positive."));
				sP.clientMessages.add(new PopUp("1 Positivity Point!"));
			} else {
				sP.clientMessages.add(new PopUp("You pick up an armor shard!"));
			}

			// Play Armor pick up sound
			// Add sound and distance from sound to server player
			sP.audioToPlay.add(SoundController.armorPickup.audioName);
			sP.audioDistances.add((int) (distanceFromPlayer));
		}
		// Health vial
		else if (itemID == ItemNames.VIAL.getID()) {
			sP.health += ItemNames.VIAL.value;

			itemUsed = true;
			if (Display.smileMode) {
				sP.clientMessages.add(new PopUp("You take your medicine..."));
				sP.clientMessages.add(new PopUp("Hey you're happier now! You're fine!"));
			} else {
				sP.clientMessages.add(new PopUp("You consume the health vial!"));
			}

			// Play correlating sound effect
			// Add sound and distance from sound to server player
			sP.audioToPlay.add(SoundController.health.audioName);
			sP.audioDistances.add((int) (distanceFromPlayer));
		}
		// Beer
		else if (itemID == ItemNames.BEER.getID()) {
			sP.health += ItemNames.BEER.value;

			itemUsed = true;
			if (Display.smileMode) {
				sP.clientMessages.add(new PopUp("Just taste that carbonation!"));
				sP.clientMessages.add(new PopUp("You consume the Soda!"));
			} else {
				sP.clientMessages.add(new PopUp("You consume the Beer!"));
			}

			// Play correlating sound effect
			// Add sound and distance from sound to server player
			sP.audioToPlay.add(SoundController.health.audioName);
			sP.audioDistances.add((int) (distanceFromPlayer));
		}
		// Burger
		else if (itemID == ItemNames.BURGER.getID()) {
			sP.health += ItemNames.BURGER.value;

			itemUsed = true;
			if (Display.smileMode) {
				sP.clientMessages.add(new PopUp("Tastes like American Goodness!"));
				sP.clientMessages.add(new PopUp("You consume the Burger!"));
			} else {
				sP.clientMessages.add(new PopUp("You consume the Burger!"));
			}

			// Play correlating sound effect
			// Add sound and distance from sound to server player
			sP.audioToPlay.add(SoundController.health.audioName);
			sP.audioDistances.add((int) (distanceFromPlayer));
		}
		// Upgrade point
		else if (itemID == ItemNames.UPGRADE.getID()) {
			itemUsed = true;
			sP.clientMessages.add(new PopUp("No use yet though..."));
			sP.clientMessages.add(new PopUp("You collect an upgrade point!"));

			// Add to upgradePoints player has
			sP.upgradePoints += 1;

			// Play special pick up sound for this item
			// Add sound and distance from sound to server player
			sP.audioToPlay.add(SoundController.specialPickup.audioName);
			sP.audioDistances.add((int) (distanceFromPlayer));
		}
		// Holy Water
		else if (itemID == ItemNames.HOLYWATER.getID()) {
			itemUsed = false;
			if (Display.smileMode) {
				sP.clientMessages.add(new PopUp("You're already happy, no need to be saved..."));
				return false;
			} else {
				sP.clientMessages.add(new PopUp("You collect the holy water!"));
			}

			// Play correlating sound effect
			// Add sound and distance from sound to server player
			sP.audioToPlay.add(SoundController.health.audioName);
			sP.audioDistances.add((int) (distanceFromPlayer));

			// Becomes just a normal table
			itemID = 42;
		}
		// Scepter of Deciet
		else if (itemID == ItemNames.DECIETSCEPTER.getID()) {
			itemUsed = true;
			if (Display.smileMode) {
				sP.clientMessages.add(new PopUp("Make those sad faces love for you!"));
				sP.clientMessages.add(new PopUp("You hold the Scepter of Love!"));
			} else {
				sP.clientMessages.add(new PopUp("Power surges from the Scepter of Deciet!"));
			}

			// Play creepy pick up sound for this item
			// Add sound and distance from sound to server player
			sP.audioToPlay.add(SoundController.creepySound.audioName);
			sP.audioDistances.add((int) (distanceFromPlayer));
		}
		// Invisibility Emerald
		else if (itemID == ItemNames.INVISEMERALD.getID()) {
			itemUsed = true;
			sP.clientMessages.add(new PopUp("You are now invisible!"));

			// Player is now invisible for a certain amount of ticks
			sP.invisibility = 1100 * (int) Render3D.fpsCheck;

			// Play correlating sound effect
			// Add sound and distance from sound to server player
			sP.audioToPlay.add(SoundController.health.audioName);
			sP.audioDistances.add((int) (distanceFromPlayer));
		}

		return itemUsed;
	}

	/**
	 * If item can be moved and has a force on it, then move that object until it is
	 * stopped. Speed will decrease by half until it reaches a negligible distance
	 * from 0. This will be the first test of simple game physics.
	 */
	public void moveItem() {
		// TODO implement me next update
	}

	/**
	 * Removes item from everything it may be referenced in over the course of the
	 * game.
	 */
	public void removeItem() {
		// If items are set to respawn, then set pickedUp to true and return instead
		// of removing the item.
		if (Display.itemsRespawn) {
			pickedUp = true;
			return;
		}
		// Block the item is on
		Block block = Level.getBlock((int) this.x, (int) this.z);

		// Remove from total list of items if it contains this item
		if (Game.items.contains(this)) {
			Game.items.remove(this);
		}

		// If item is an activatable item, remove it from this too
		if (Game.activatable.contains(this)) {
			Game.activatable.remove(this);
		}

		// If a solid item, then remove it from that list too
		if (Game.solidItems.contains(this)) {
			Game.solidItems.remove(this);
		}

		// If the blocks solid item is this item, then set that wall
		// item is deleted
		try {
			for (int i = 0; i < block.wallItems.size(); i++) {
				Item item = block.wallItems.get(i);

				if (item.equals(this)) {
					block.wallItems.remove(item);
				}
			}
		} catch (Exception e) {

		}
	}

	/**
	 * Play a given audio clip for this item based on what audioQueue this item has
	 * for when it is activated.
	 */
	public void activateAudioQueue() {
		// If there is an audioQueue to be played
		if (audioQueue != "") {
			// Search all the available audio files
			for (Sound s : SoundController.allSounds) {
				// If the audio clip is found with the same name
				// then play it.
				if (audioQueue.equals(s.audioName)) {
					// Stop all other sounds so this queue can be heard.
					// This is the most important
					for (Sound s2 : SoundController.allSounds) {
						s2.stopAll();
					}

					s.playAudioFile(0);
					break;
				}
			}
		}
	}

	/**
	 * Tick the time the item has been gone, and wait to reach a certain amount.
	 */
	public void tick() {
		tickCount++;
	}
}
