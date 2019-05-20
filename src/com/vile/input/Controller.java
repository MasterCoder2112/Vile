package com.vile.input;

import java.util.ArrayList;

import com.vile.Display;
import com.vile.Game;
import com.vile.PopUp;
import com.vile.RunGame;
import com.vile.SoundController;
import com.vile.entities.Button;
import com.vile.entities.Door;
import com.vile.entities.Elevator;
import com.vile.entities.EntityParent;
import com.vile.entities.Explosion;
import com.vile.entities.HurtingBlock;
import com.vile.entities.Item;
import com.vile.entities.ItemNames;
import com.vile.entities.Player;
import com.vile.graphics.Render3D;
import com.vile.launcher.FPSLauncher;
import com.vile.levelGenerator.Block;
import com.vile.levelGenerator.Level;

/**
 * Title: Controller
 * 
 * @author Alex Byrd Date Updated: 5/01/2017
 *
 *         Takes all the input from the controls sent from game, and performs
 *         all the actions of the game and helps set the variables used by
 *         Render3D to render the 3D environment of the game correctly as the
 *         Player moves.
 * 
 *         Also it updates movement, and detects collision.
 */
public class Controller {
	/*
	 * Position and movement variables.
	 */
	private double xa;
	private double za;
	private double upRotationa;
	private double rotationa;

	/*
	 * Variables used to check certain cases, so that some actions do not activate
	 * if another one is activated. For instance, if your in a jump you cannot jump
	 * again, and etc...
	 */
	public static boolean inJump = false;
	private boolean crouching = false;

	// Whether certain weapons are to be switched to or not
	// They are static so that switching through the mouse
	// wheel also works
	public static boolean switch0 = false;
	public static boolean switch1 = false;
	public static boolean switch2 = false;
	public static boolean switch3 = false;
	public static boolean switch4 = false;
	public static boolean switch5 = false;

	/*
	 * Handles time between when you can activate certain events
	 */
	public static int time;
	private int time2;

	// How much time have you been drunk
	private int drunkTime = 0;

	/*
	 * Variables used in falling acceleration (gravity)
	 */
	public static double fallSpeed = 0.4;
	public static double fallAmount = 0;

	// Public and static because it can be changed when accessing a
	// new custom resource pack. (In case on the moon or other planet)
	public static double acceleration = 0.03;

	/*
	 * Other variables
	 */
	public static boolean mouseLeft = false;
	public static boolean mouseRight = false;
	public static boolean mouseUp = false;
	public static boolean mouseDown = false;
	public static boolean showFPS = false;
	public static boolean quitGame = false;
	public static double moveSpeed = 1.0;
	public static double rotationUpSpeed = 0.01;
	public static int timeAfterShot;
	public static boolean shot = false;

	// Whether players starting height has been checked yet
	private boolean firstCheck = false;

	// How long has it been since the player has last tried
	// to activate something
	private int useTime = 0;

	/**
	 * Takes all the values in from reading the keyboard, and activates certain
	 * things depending on what values are true or not. This method also the x, z,
	 * and rotation speeds so that they can be used by the Render3D method to move
	 * the player the corresponding ways.
	 * 
	 * Also takes care of shooting, flying, and other such events.
	 */
	public void performActions(Game game) {
		/*
		 * Reset these variables each time it runs through.
		 */
		double rotationSpeed = 0.03;
		double horizontalMouseSpeed = 0.005 * Display.mouseSpeedHorizontal;
		double verticalMouseSpeed = 0.001 * Display.mouseSpeedVertical;

		// Amount your moving in x or z direction each time it loops
		double moveX = 0;
		double moveZ = 0;

		/*
		 * Only allow these actions if the player is alive
		 */
		if (Player.alive) {
			/*
			 * If you shoot, then set shot to true
			 */
			if (Game.shoot) {
				shot = true;
			}

			// If moving forward
			if (Game.foward) {
				moveZ += 1 / 21.3;
			}

			// If moving back
			if (Game.back) {
				moveZ -= 1 / 21.3;
			}

			// If strafing left
			if (Game.left) {
				moveX -= 1 / 21.3;
			}

			// If strafing right
			if (Game.right) {
				moveX += 1 / 21.3;
			}

			// If turning left
			if (Game.turnLeft && Player.frozen == 0) {
				rotationa -= rotationSpeed;
			}

			// If turning right
			if (Game.turnRight && Player.frozen == 0) {
				rotationa += rotationSpeed;
			}

			// If looking up
			if (Game.turnUp && Player.frozen == 0) {
				upRotationa -= rotationUpSpeed;
			}

			// If looking down
			if (Game.turnDown && Player.frozen == 0) {
				upRotationa += rotationUpSpeed;
			}

			// If mouse goes left
			if (mouseLeft && Player.frozen == 0) {
				rotationa += horizontalMouseSpeed;

				// if(rotationa < -0.6)
				// {
				// rotationa = 0;
				// }

				mouseLeft = false;
			}

			// If mouse moves right
			if (mouseRight && Player.frozen == 0) {
				rotationa += horizontalMouseSpeed;

				// if(rotationa > 0.6)
				// {
				// rotationa = 0;
				// }

				mouseRight = false;
			}

			// If mouse moves up
			if (mouseUp && Player.frozen == 0) {
				upRotationa += verticalMouseSpeed;

				// if(upRotationa < -0.1)
				// {
				// upRotationa = 0;
				// }

				mouseUp = false;
			}

			// If mouse moves down
			if (mouseDown && Player.frozen == 0) {
				upRotationa += verticalMouseSpeed;

				// if(upRotationa > 0.1)
				// {
				// upRotationa = 0;
				// }

				mouseDown = false;
			}

			// If running, set moveSpeed to 1.5 times the value it was
			if (Game.run) {
				moveSpeed *= 1.5;
			}

			/*
			 * If you are pressing crouch, and not going up because of jumping. Halve the
			 * moveSpeed, set crouching = true, and keep performing a given operation while
			 * you are crouching.
			 */
			if ((Game.crouch || Player.forceCrouch) && !inJump && Player.frozen == 0) {
				// If still not as low as you can go
				if (Player.yCorrect > -6.0 + Player.y) {
					// If flying, just fly down until the player reaches the ground
					if (Player.flyOn && Player.y >= Player.maxHeight + Player.extraHeight) {
						Player.y -= 0.8;
					}
					// TODO Fix this eventually
					// If falling from getting out of flyMode.
					// else if (fallAmount > 0) {
					/*
					 * If you are falling, your not crouching, but you are increasing your speed
					 * downward due to becoming smaller, and so your speed increases.
					 */
					// crouching = false;
					// fallSpeed *= 1.005;

					/*
					 * Only increase the acceleration once when falling and hunkering down while
					 * falling
					 */
					/*
					 * if (!once) { fallAmount *= 1.1; once = true; } }
					 */
					// If not falling, just crouch at the normal speed
					// Also you have to be at your normal height or
					// below it already.
					else {
						moveSpeed = 0.5;
						crouching = true;

						Player.yCorrect -= 0.5;
						Player.height -= 0.17;
					}
				}

				// If there is some decimal error, round to -6.0
				if (Player.yCorrect <= -6.0 + Player.y) {
					Player.yCorrect = -6.0 + Player.y;
					Player.height = 0;
				}
			}
			/*
			 * If coming up from a crouch and you have not reached your normal height yet
			 * and there is not a block right above you that you can't raise through.
			 */
			else if (Player.yCorrect < Player.y && Player.height < 2
					&& (Player.yCorrect + 4 < Player.blockOn.y && Player.blockOn.y > Player.y
							|| Player.blockOn.y <= Player.y || Player.y + 2 < Player.blockOn.y)) {
				crouching = false;
				Player.forceCrouch = false;

				// Slowly raise up
				if (Player.yCorrect < Player.y) {
					Player.yCorrect += 0.5;
				}

				// Also raises player up
				if (Player.height < 2) {
					Player.height += 0.17;
				}

				if (Player.height >= 2) {
					Player.height = 2;
				}

				if (Player.yCorrect > Player.y) {
					Player.yCorrect = Player.y;
				}
			}
			// If no longer crouching
			else {
				crouching = false;
				Player.forceCrouch = false;
			}

			/*
			 * If fly mode is not on, jump like normal
			 */
			if (!Player.flyOn) {
				/*
				 * If the Player is trying to jump, and the player is on the ground, then jump.
				 * Also player can't be frozen.
				 */
				if (Game.jump && Player.y - (Player.maxHeight + Player.extraHeight) <= 0.2 && Player.frozen == 0) {
					inJump = true;
					Player.jumpHeight = Player.totalJump + Player.maxHeight + Player.extraHeight;
				}

				/*
				 * If you haven't reached your max jumpHeight yet then keep going up, but if you
				 * have, then stop going up.
				 */
				if (inJump && Player.y < Player.jumpHeight
						&& (Player.y + 0.4 < (Player.blockOn.y) && Player.blockOn.y > 0
								|| Player.y >= Player.blockOn.y)) {
					Player.y += 0.4;
				} else {
					inJump = false;
				}

				// If you are above the ceiling, put Player back to the maxHeight
				// he or she can be at.
				if (Player.y + Player.height >= Render3D.ceilingDefaultHeight) {
					inJump = false;
					Player.y = Render3D.ceilingDefaultHeight - 0.3 - Player.height;
				}

				// If you're falling, accelerate down
				if (Player.y > 0 + Player.maxHeight + Player.extraHeight && !inJump) {
					Player.y -= fallSpeed;
					fallSpeed = fallSpeed + (fallSpeed * acceleration);
				}
			} else {
				// If in flymode just go upward
				if (Game.jump) {
					Player.y += 0.8;
				}

				// If you are above the ceiling, get back to the ceiling
				if (Player.y >= Render3D.ceilingDefaultHeight) {
					Player.y = Render3D.ceilingDefaultHeight;
				}
			}

			// If Player.y is negligibly close to his/her max height
			// then just set the player at being their maxHeight
			if (Player.y < 0.4 + Player.maxHeight + Player.extraHeight
					&& Player.y > -0.4 + Player.maxHeight + Player.extraHeight) {
				Player.y = 0 + Player.maxHeight + Player.extraHeight;
				inJump = false;
				fallSpeed = 0.4;
			}

			/*
			 * If you fell so hard you went below the normal Player.y for crouching, then
			 * set the Player.y equal to -7 to not go below the map.
			 */
			if (Player.y <= -7 + Player.maxHeight + Player.extraHeight && !crouching) {
				Player.y = -7 + Player.maxHeight;
				inJump = false;
				fallSpeed = 0.4;
			}

			// Reset fallAmount each tick so that falling off of things works as well.
			if (fallAmount < Player.y - Player.maxHeight && !firstCheck) {
				fallAmount = Player.y - Player.maxHeight;
			}

			/*
			 * If you are on the ground, calculate whether you are getting fall damage or
			 * not. If you havent fallen, then you recieve no damage.
			 */
			if (fallAmount > Player.jumpHeight * 2 && Math.abs(Player.y - Player.maxHeight) <= 0) {
				if (!Player.godModeOn && Player.immortality == 0 && !Player.flyOn) {
					Player.hurtPlayer((int) (10 * (fallAmount / 25)));
					SoundController.crushed.playAudioFile(0);

					if (Player.health <= 0) {
						Display.messages.add(new PopUp("Player fell to a clumsy death!"));
					}
				}

				fallAmount = 0;
			}
			// If gun shot
			if (shot) {
				// If shot by using the keyboard, shut it off here
				if (Game.shoot) {
					shot = false;
				}

				// See if weapon can fire
				if (!Player.weapons[Player.weaponEquipped].shoot()) {
					// Only play sound if weapon can't fire and its out of ammo
					if (Player.weapons[Player.weaponEquipped].ammo <= 0
							&& !Player.weapons[Player.weaponEquipped].melee) {
						SoundController.ammoOut.playAudioFile(0);
					}
				}
			}

			/*
			 * If weapon can be equipped, and another weapon isn't being fired, then switch
			 * to this weapon
			 */
			if (Game.weaponSlot0 && Player.weapons[Player.weaponEquipped].weaponShootTime == 0
					&& Player.weapons[Player.weaponEquipped].weaponShootTime2 == 0) {
				if (Player.weapons[0].canBeEquipped) {
					Player.weaponEquipped = 0;
				} else {
					PopUp temp = new PopUp("You don't have this weapon yet!");

					// Only display the message if its not on screen yet
					boolean exists = false;

					for (PopUp p : Display.messages) {
						if (temp.text == p.text) {
							exists = true;
							break;
						}
					}

					// If Message does not exist yet
					if (!exists) {
						Display.messages.add(temp);
					}
				}
			}
			// Switch to this weapon once current weapon is done firing
			else if (Game.weaponSlot0) {
				if (Player.weapons[0].canBeEquipped) {
					switch0 = true;
					switch3 = false;
					switch1 = false;
					switch2 = false;
					switch4 = false;
					switch5 = false;
				} else {
					PopUp temp = new PopUp("You don't have this weapon yet!");

					// Only display the message if its not on screen yet
					boolean exists = false;

					for (PopUp p : Display.messages) {
						if (temp.text == p.text) {
							exists = true;
							break;
						}
					}

					// If Message does not exist yet
					if (!exists) {
						Display.messages.add(temp);
					}
				}
			}

			/*
			 * If weapon can be equipped, and another weapon isn't being fired, then switch
			 * to this weapon
			 */
			if (Game.weaponSlot1 && Player.weapons[Player.weaponEquipped].weaponShootTime == 0
					&& Player.weapons[Player.weaponEquipped].weaponShootTime2 == 0) {
				if (Player.weapons[1].canBeEquipped) {
					Player.weaponEquipped = 1;
				} else {
					PopUp temp = new PopUp("You don't have this weapon yet!");

					// Only display the message if its not on screen yet
					boolean exists = false;

					for (PopUp p : Display.messages) {
						if (temp.text == p.text) {
							exists = true;
							break;
						}
					}

					// If Message does not exist yet
					if (!exists) {
						Display.messages.add(temp);
					}
				}
			}
			// Switch to this weapon once current weapon is done firing
			else if (Game.weaponSlot1) {
				if (Player.weapons[1].canBeEquipped) {
					switch1 = true;
					switch0 = false;
					switch3 = false;
					switch2 = false;
					switch4 = false;
					switch5 = false;
				} else {
					PopUp temp = new PopUp("You don't have this weapon yet!");

					// Only display the message if its not on screen yet
					boolean exists = false;

					for (PopUp p : Display.messages) {
						if (temp.text == p.text) {
							exists = true;
							break;
						}
					}

					// If Message does not exist yet
					if (!exists) {
						Display.messages.add(temp);
					}
				}
			}

			/*
			 * If weapon can be equipped, and another weapon isn't being fired, then switch
			 * to this weapon
			 */
			if (Game.weaponSlot2 && Player.weapons[Player.weaponEquipped].weaponShootTime == 0
					&& Player.weapons[Player.weaponEquipped].weaponShootTime2 == 0) {
				if (Player.weapons[2].canBeEquipped) {
					Player.weaponEquipped = 2;
				} else {
					PopUp temp = new PopUp("You don't have this weapon yet!");

					// Only display the message if its not on screen yet
					boolean exists = false;

					for (PopUp p : Display.messages) {
						if (temp.text == p.text) {
							exists = true;
							break;
						}
					}

					// If Message does not exist yet
					if (!exists) {
						Display.messages.add(temp);
					}
				}
			}
			// Switch to this weapon once current weapon is done firing
			else if (Game.weaponSlot2) {
				if (Player.weapons[2].canBeEquipped) {
					switch2 = true;
					switch0 = false;
					switch1 = false;
					switch3 = false;
					switch4 = false;
					switch5 = false;
				} else {
					PopUp temp = new PopUp("You don't have this weapon yet!");

					// Only display the message if its not on screen yet
					boolean exists = false;

					for (PopUp p : Display.messages) {
						if (temp.text == p.text) {
							exists = true;
							break;
						}
					}

					// If Message does not exist yet
					if (!exists) {
						Display.messages.add(temp);
					}
				}
			}

			/*
			 * If weapon can be equipped, and another weapon isn't being fired, then switch
			 * to this weapon
			 */
			if (Game.weaponSlot3 && Player.weapons[Player.weaponEquipped].weaponShootTime == 0
					&& Player.weapons[Player.weaponEquipped].weaponShootTime2 == 0) {
				if (Player.weapons[3].canBeEquipped) {
					Player.weaponEquipped = 3;
				} else {
					PopUp temp = new PopUp("You don't have this weapon yet!");

					// Only display the message if its not on screen yet
					boolean exists = false;

					for (PopUp p : Display.messages) {
						if (temp.text == p.text) {
							exists = true;
							break;
						}
					}

					// If Message does not exist yet
					if (!exists) {
						Display.messages.add(temp);
					}
				}
			}
			// Switch to this weapon once current weapon is done firing
			else if (Game.weaponSlot3) {
				if (Player.weapons[3].canBeEquipped) {
					switch3 = true;
					switch0 = false;
					switch1 = false;
					switch2 = false;
					switch4 = false;
					switch5 = false;
				} else {
					PopUp temp = new PopUp("You don't have this weapon yet!");

					// Only display the message if its not on screen yet
					boolean exists = false;

					for (PopUp p : Display.messages) {
						if (temp.text == p.text) {
							exists = true;
							break;
						}
					}

					// If Message does not exist yet
					if (!exists) {
						Display.messages.add(temp);
					}
				}
			}

			/*
			 * If weapon can be equipped, and another weapon isn't being fired, then switch
			 * to this weapon
			 */
			if (Game.weaponSlot4 && Player.weapons[Player.weaponEquipped].weaponShootTime == 0
					&& Player.weapons[Player.weaponEquipped].weaponShootTime2 == 0) {
				if (Player.weapons[4].canBeEquipped) {
					Player.weaponEquipped = 4;
				} else {
					PopUp temp = new PopUp("You don't have this weapon yet!");

					// Only display the message if its not on screen yet
					boolean exists = false;

					for (PopUp p : Display.messages) {
						if (temp.text == p.text) {
							exists = true;
							break;
						}
					}

					// If Message does not exist yet
					if (!exists) {
						Display.messages.add(temp);
					}
				}
			}
			// Switch to this weapon once current weapon is done firing
			else if (Game.weaponSlot4) {
				if (Player.weapons[4].canBeEquipped) {
					switch3 = false;
					switch0 = false;
					switch1 = false;
					switch2 = false;
					switch4 = true;
					switch5 = false;
				} else {
					PopUp temp = new PopUp("You don't have this weapon yet!");

					// Only display the message if its not on screen yet
					boolean exists = false;

					for (PopUp p : Display.messages) {
						if (temp.text == p.text) {
							exists = true;
							break;
						}
					}

					// If Message does not exist yet
					if (!exists) {
						Display.messages.add(temp);
					}
				}
			}

			/*
			 * If weapon can be equipped, and another weapon isn't being fired, then switch
			 * to this weapon
			 */
			if (Game.weaponSlot5 && Player.weapons[Player.weaponEquipped].weaponShootTime == 0
					&& Player.weapons[Player.weaponEquipped].weaponShootTime2 == 0) {
				if (Player.weapons[5].canBeEquipped) {
					Player.weaponEquipped = 5;
				} else {
					PopUp temp = new PopUp("You don't have this weapon yet!");

					// Only display the message if its not on screen yet
					boolean exists = false;

					for (PopUp p : Display.messages) {
						if (temp.text == p.text) {
							exists = true;
							break;
						}
					}

					// If Message does not exist yet
					if (!exists) {
						Display.messages.add(temp);
					}
				}
			}
			// Switch to this weapon once current weapon is done firing
			else if (Game.weaponSlot5) {
				if (Player.weapons[5].canBeEquipped) {
					switch3 = false;
					switch0 = false;
					switch1 = false;
					switch2 = false;
					switch4 = false;
					switch5 = true;
				} else {
					PopUp temp = new PopUp("You don't have this weapon yet!");

					// Only display the message if its not on screen yet
					boolean exists = false;

					for (PopUp p : Display.messages) {
						if (temp.text == p.text) {
							exists = true;
							break;
						}
					}

					// If Message does not exist yet
					if (!exists) {
						Display.messages.add(temp);
					}
				}
			}

			/*
			 * If current weapon is finally able to be switched out, then switch to weapon
			 * you wanted to switch to
			 */
			if (Player.weapons[Player.weaponEquipped].weaponShootTime == 0
					&& Player.weapons[Player.weaponEquipped].weaponShootTime2 == 0) {
				if (switch0) {
					Player.weaponEquipped = 0;
					switch0 = false;
				} else if (switch1) {
					Player.weaponEquipped = 1;
					switch1 = false;
				} else if (switch2) {
					Player.weaponEquipped = 2;
					switch2 = false;
				} else if (switch3) {
					Player.weaponEquipped = 3;
					switch3 = false;
				} else if (switch4) {
					Player.weaponEquipped = 4;
					switch4 = false;
				} else if (switch5) {
					Player.weaponEquipped = 5;
					switch5 = false;
				}
			}

			/*
			 * If you try to reload, and there are still cartridges with ammo in them, and
			 * some time has passed since the last reload, then reload. Also cannot be a
			 * melee weapon
			 */
			if (Game.reloading && time == 0) {
				if (Player.weapons[Player.weaponEquipped].reload() && !Player.weapons[Player.weaponEquipped].melee) {
					time++;

					SoundController.reload.playAudioFile(0);
				}
			}

			// If Player is upgrading weapon, make sure he/she can, then upgrade it and
			// update values. Otherwise tell the player they can't do it right now.
			if (Game.upgradeWeapon && time == 0) {
				time++;

				if (Player.weapons[Player.weaponEquipped].upgradePointsNeeded != -1) {
					if (Player.weapons[Player.weaponEquipped].upgradePointsNeeded <= Player.upgradePoints) {
						Player.upgradePoints -= Player.weapons[Player.weaponEquipped].upgradePointsNeeded;
						Player.weapons[Player.weaponEquipped].upgradePointsNeeded *= 2;
						Player.weapons[Player.weaponEquipped].baseDamage *= 1.25;
						Player.weapons[Player.weaponEquipped].damage *= 1.25;

						// Makes it more likely hit will be critical
						Player.weapons[Player.weaponEquipped].criticalHitChances--;

						SoundController.phaseCannonHit.playAudioFile(0);
					} else {
						Display.messages.add(new PopUp("You do not have enough points to upgrade this weapon!"));
					}
				} else {
					Display.messages.add(new PopUp("This weapon cannot be upgraded!"));
				}
			}
		} else {
			// If dead the player just sits and rotates while also
			// being near the ground to simulate a fallen body
			rotationa += 0.001;
			Player.y = -6.0;
		}

		// If time is not 0, add to it
		if (time != 0) {
			time++;
		}

		// If 25 ticks have passed, set time to 0
		if (time == 25) {
			time = 0;
		}

		// Do the same thing with time2 for shooting
		if (time2 != 0) {
			time2++;
		}

		// Allow shooting to be faster though, so 11 instead of 25 ticks
		if (time2 == 11) {
			time2 = 0;
		}

		/*
		 * If you want to show or not show fps, and some time has passed since you last
		 * performed the action, then flip the status of showFPS, and then start the
		 * ticks again since this action was last performed.
		 */
		if (Game.fpsShow && time == 0) {
			if (showFPS) {
				showFPS = false;
			} else {
				showFPS = true;
			}

			time++;
		}

		// If you want to quit the game, set quitGame to true
		if (Game.pause && time == 0) {
			// Game is paused
			Display.pauseGame = true;

			// It will quit the "game" in terms of all the events the game
			// ticks through, not the app itself.
			quitGame = true;

			time++;
		}

		/*
		 * If the player is trying to use an entity such as a button, elevator or door,
		 * if the player is within a small distance of that particular entity then
		 * activate that entity.
		 * 
		 * Each time check all of these entities in the game to see if the player is
		 * within range of them.
		 */
		if (Game.use && Player.alive) {
			// If nothing is activated, this will activate the oomf sound
			boolean anyActivated = false;

			// Activate button is button pressed
			if (Game.buttons.size() > 0) {
				for (int i = 0; i < Game.buttons.size(); i++) {
					Button button = Game.buttons.get(i);

					// Has to be in range of button
					if (Math.abs(button.z - Player.z) <= 0.95 && Math.abs(button.x - Player.x) <= 0.95
							&& !button.pressed) {

						// If client, then add this to activated things to add to the server
						if (Display.gameType == 1) {
							Game.activatedButtons.add(button);
						}
						button.pressed = true;
						button.activated = true;
						anyActivated = true;

						SoundController.activated.playAudioFile(0);
						// Play button press sound
						SoundController.buttonPress.playAudioFile(0);

						// Reset useTime and start it again
						useTime = 0;
						useTime++;
					}
				}
			}

			/*
			 * See if player activated a door. Player has to be within range of door, and
			 * have the appropriate key to activate a door. Otherwise show on screen that
			 * you need a certain key to activate.
			 */
			for (int i = 0; i < Game.doors.size(); i++) {
				Door door = Game.doors.get(i);

				if (Math.abs(door.getZ() - Player.z) <= 0.95 && Math.abs(door.getX() - Player.x) <= 0.95) {
					if (door.doorType == 0 || door.doorType == 1 && Player.hasRedKey
							|| door.doorType == 2 && Player.hasBlueKey || door.doorType == 3 && Player.hasGreenKey
							|| door.doorType == 4 && Player.hasYellowKey) {
						// If door doesn't have to be activated by a button
						if (door.itemActivationID == 0 && !door.activated) {
							if (!SoundController.keyUse.isStillActive()) {
								SoundController.keyUse.playAudioFile(door.distanceFromPlayer + 10);
							}

							// If client, then add this to activated things to add to the server
							if (Display.gameType == 1) {
								Game.activatedDoors.add(door);
							}

							door.activated = true;
							anyActivated = true;
						} else if (door.itemActivationID > 0) {
							if (!SoundController.keyTry.isStillActive()) {
								SoundController.keyTry.playAudioFile(door.distanceFromPlayer + 10);
							}

							PopUp temp = new PopUp("This door is opened elsewhere!");

							// Only display the message if its not on screen yet
							boolean exists = false;

							for (PopUp p : Display.messages) {
								if (temp.text == p.text) {
									exists = true;
									break;
								}
							}

							// If Message does not exist yet
							if (!exists) {
								Display.messages.add(temp);
							}
						}
					} else {
						if (door.doorType == 1 && !Player.hasRedKey) {
							/*
							 * Displays that the player cannot open the door without the red key.
							 */
							PopUp temp = new PopUp("You need the Red Keycard!");

							// Only display the message if its not on screen yet
							boolean exists = false;

							for (PopUp p : Display.messages) {
								if (temp.text == p.text) {
									exists = true;
									break;
								}
							}

							// If Message does not exist yet
							if (!exists) {
								Display.messages.add(temp);
							}

							if (!SoundController.keyTry.isStillActive()) {
								SoundController.keyTry.playAudioFile(door.distanceFromPlayer + 10);
							}
						} else if (door.doorType == 2 && !Player.hasBlueKey) {
							/*
							 * Displays that the player cannot open the door without the blue key.
							 */
							PopUp temp = new PopUp("You need the Blue Keycard!");

							boolean exists = false;

							for (PopUp p : Display.messages) {
								if (temp.text == p.text) {
									exists = true;
									break;
								}
							}

							// If Message does not exist yet
							if (!exists) {
								Display.messages.add(temp);
							}

							if (!SoundController.keyTry.isStillActive()) {
								SoundController.keyTry.playAudioFile(door.distanceFromPlayer + 10);
							}
						} else if (door.doorType == 3 && !Player.hasGreenKey) {
							/*
							 * Displays that the player cannot open the door without the green key.
							 */
							PopUp temp = new PopUp("You need the Green Keycard!");

							boolean exists = false;

							for (PopUp p : Display.messages) {
								if (temp.text == p.text) {
									exists = true;
									break;
								}
							}

							// If Message does not exist yet
							if (!exists) {
								Display.messages.add(temp);
							}

							if (!SoundController.keyTry.isStillActive()) {
								SoundController.keyTry.playAudioFile(door.distanceFromPlayer + 10);
							}
						} else {
							/*
							 * Displays that the player cannot open the door without the yellow key.
							 */
							PopUp temp = new PopUp("You need the Yellow Keycard!");

							boolean exists = false;

							for (PopUp p : Display.messages) {
								if (temp.text == p.text) {
									exists = true;
									break;
								}
							}

							// If Message does not exist yet
							if (!exists) {
								Display.messages.add(temp);
							}

							if (!SoundController.keyTry.isStillActive()) {
								SoundController.keyTry.playAudioFile(door.distanceFromPlayer + 10);
							}
						}
					}
				}
			}

			/*
			 * See if player is in range to activate an elevator
			 */
			for (int i = 0; i < Game.elevators.size(); i++) {
				Elevator elevator = Game.elevators.get(i);

				if (Math.abs(elevator.getZ() - Player.z) <= 0.95 && Math.abs(elevator.getX() - Player.x) <= 0.95) {
					// If door doesn't have to be activated by a button
					if (elevator.itemActivationID == 0) {

						// If client, then add this to activated things to add to the server
						if (Display.gameType == 1) {
							Game.activatedElevators.add(elevator);
						}

						elevator.activated = true;
						anyActivated = true;

						// Reset useTime and start it again
						useTime = 0;
						useTime++;
					} else {
						PopUp temp = new PopUp("This elevator is activated elsewhere!");

						boolean exists = false;

						for (PopUp p : Display.messages) {
							if (temp.text == p.text) {
								exists = true;
								break;
							}
						}

						// If Message does not exist yet
						if (!exists) {
							Display.messages.add(temp);
						}
					}
				}
			}

			// If nothing was activated play oomf sound effect
			if (!anyActivated && useTime == 0) {
				SoundController.tryToUse.playAudioFile(0);

				// Begin the next waiting cycle to play sound again
				useTime++;
			}

			// Keep ticking the wait time until it reaches 21
			if (useTime < 21 && useTime > 0) {
				useTime++;
			}
			// At 21 reset it to 0
			else {
				useTime = 0;
			}
		}
		/*
		 * If player is dead, allow player to respawn at beginning of the map so he/she
		 * does not have to replay through the whole game, but start them out with their
		 * default values, kind of like in DOOM when you die. Usually if your skilled
		 * enough you can survive and continue on going.
		 */
		else if (Game.use && !Player.alive) {
			new Player();

			// If not survival, reload map
			if (FPSLauncher.gameMode == 0) {
				Display.messages = new ArrayList<PopUp>();

				// Clients do not start a new game, they just reset their positions back to
				// their original place but everything else about them is reset.
				if (Display.gameType == 1) {
					Player.x = Player.startX;
					Player.y = Player.startY;
					Player.z = Player.startZ;
				} else {
					if (Display.nonDefaultMap) {
						game = new Game(RunGame.game, Display.nonDefaultMap, Display.newMapName);
					} else {
						game.loadNextMap(false, "");
					}
				}
			} else {
				Display.messages = new ArrayList<PopUp>();
				// If survival mode then restart survival
				game.display.restartSurvival();
			}
		} else {
			// If the player is not trying to use a wall, reset useTime
			useTime = 0;
		}

		/*
		 * If you want to noclip, and some time has passed since you last performed the
		 * action, then flip the status of noClip, and then start the ticks again since
		 * this action was last performed.
		 */
		if (Game.consoleOpen && time == 0) {
			time++;

			if (!Display.consoleOpen) {
				Display.consoleOpen = true;
			} else {
				Display.consoleOpen = false;
			}
		}

		// Multiply players speed by certain amount
		moveSpeed *= Player.speedMultiplier;

		// Recall's friendly entities and tells them to stop trying to pursue an
		// enemy
		if (Game.recallFriendlies && time == 0) {
			for (int i = 0; i < Game.entities.size(); i++) {
				EntityParent temp = Game.entities.get(i);

				SoundController.rockAndRoll.playAudioFile(0);

				if (temp.isFriendly) {
					temp.targetEnemy = null;
				}
			}
		}

		/*
		 * Calculates the change of x and z depending on which direction the player is
		 * moving, and the players rotation. The players rotation is used to determine
		 * how the graphics are rotated and drawn around the player using sin and cos to
		 * make a complete circle around the player.
		 */
		xa += ((moveX * Math.cos(Player.rotation)) + (moveZ * Math.sin(Player.rotation))) * moveSpeed;
		za += ((moveZ * Math.cos(Player.rotation)) - (moveX * Math.sin(Player.rotation))) * moveSpeed;

		int speedMultiplier = 1;

		if (Game.run) {
			speedMultiplier = 2;
		}

		if (Math.abs(za) + Math.abs(xa) >= (1 / 21.3)) {
			if (Player.moveDirect == 1) {
				Player.movementTick += 2 * speedMultiplier;

				if (Player.movementTick >= 45) {
					Player.moveDirect = -1;
				}
			} else {
				Player.movementTick -= 2 * speedMultiplier;

				if (Player.movementTick <= -45) {
					Player.moveDirect = 1;
				}
			}
		} else {
			Player.movementTick = 0;
		}

		/*
		 * If player is drunk enough he/she will sway in random directions more and
		 * more.
		 */
		if (Player.drunkLevels >= 1500) {
			double swayEffect = 0.5;

			if (Player.drunkLevels > 3500) {
				swayEffect = 4;
			} else if (Player.drunkLevels > 3000) {
				swayEffect = 3;
			} else if (Player.drunkLevels > 2500) {
				swayEffect = 2;
			} else if (Player.drunkLevels > 2000) {
				swayEffect = 1;
			}

			boolean left = false;
			boolean back = false;

			if (drunkTime > 38) {
				left = true;
				back = true;
			} else if (drunkTime > 25) {
				left = false;
				back = true;
			} else if (drunkTime > 12) {
				left = true;
				back = false;
			}

			if (drunkTime > 50) {
				drunkTime = 0;
			}

			// TODO add crap

			if (left) {
				xa -= (swayEffect / 21.3);
			} else {
				xa += (swayEffect / 21.3);
			}

			if (back) {
				za -= (swayEffect / 21.3);
			} else {
				za += (swayEffect / 21.3);
			}

			drunkTime++;
		}

		double xEffects = 0;
		double zEffects = 0;
		double yEffects = 0;

		if (Player.xEffects > 0) {
			xEffects = 0.2;
		} else if (Player.xEffects < 0) {
			xEffects = -0.2;
		}

		if (Player.zEffects > 0) {
			zEffects = 0.2;
		} else if (Player.zEffects < 0) {
			zEffects = -0.2;
		}

		if (Player.yEffects > 0) {
			yEffects = 2;
		} else if (Player.yEffects < 0) {
			yEffects = -2;
		}

		Player.y += (yEffects);

		// Only if player is not frozen.
		if (Player.frozen == 0) {
			/*
			 * These determine if the space to the side of the player is a solid block or
			 * not. If its not then move the player, if it is then don't execute movement in
			 * that direction.
			 * 
			 * If noclip is on, you can clip through walls, so you can do this anyway.
			 */
			if (isFree(Player.x + xa + (xEffects), Player.z) || Player.noClipOn) {
				Player.x += xa + (xEffects);
			}

			/*
			 * These determine if the space in front or back of the player is a solid block
			 * or not. If its not then move the player, if it is then don't execute movement
			 * in that direction.
			 * 
			 * If noclip is on, you can clip through walls, so you can do this anyway.
			 */
			if (isFree(Player.x, Player.z + za + (zEffects)) || Player.noClipOn) {
				Player.z += za + (zEffects);
			}
		}

		// Update player buffs (invincibility, etc...)
		Player.updateBuffs();

		// System.out.println("HERE");

		try {
			for (Item e : Player.blockOn.wallEntities) {
				// If it contains a Toxic Waste Block or Lava Block
				if (Game.hurtingBlocks.contains(e)) {
					/*
					 * Hurt the player if the ticks have reset. Meaning player can only be hurt
					 * every so many ticks while on the block.
					 */
					e.activate();

					// Update HurtingBlock time only when the player is on the
					// block
					HurtingBlock.time++;

					if (HurtingBlock.time > 21 * Render3D.fpsCheck) {
						HurtingBlock.time = 0;
					}
				}

				// If a Teleporter enterance
				if (e.itemID == ItemNames.TELEPORTERENTER.getID()) {
					// Check all teleporters for a matching exit
					for (int i = 0; i < Game.teleporters.size(); i++) {
						// Teleporter Object
						Item tel = Game.teleporters.get(i);

						// If there is a teleporter exit with the same exact
						// activation ID, teleport the player to that location
						if (tel.itemActivationID == e.itemActivationID
								&& tel.itemID == ItemNames.TELEPORTEREXIT.getID()) {
							Player.x = tel.x;
							Player.z = tel.z;

							// Block teleporter exit is on
							Block teleporterExit = Level.getBlock((int) tel.x, (int) tel.z);

							// Set players y value to that new blocks height
							Player.y = teleporterExit.height + teleporterExit.y;

							// Play teleportation sound
							SoundController.teleportation.playAudioFile(0);
						}
					}
				}
			}
		} catch (Exception e) {

		}

		try {
			for (int i = 0; i < Player.blockOn.wallItems.size(); i++) {
				Item item = Player.blockOn.wallItems.get(i);

				// If it is a line def
				if (item.itemID == ItemNames.LINEDEF.itemID) {
					// If End Level Line Def
					if (item.itemActivationID == 0) {
						Game.mapNum++;
						time++;
						Display.clearedLevel = true;
					} else {

						// TODO change in future
						if (item.itemActivationID == -1) {
							Player.frozen = 1000;
						} else if (item.itemActivationID == -2) {
							Player.frozen = 100;
						} else {

							// Remove other linedefs of the same activation ids
							int size = Game.items.size();
							for (int s = 0; s < size; s++) {
								Item it = Game.items.get(s);
								if (it.itemID == ItemNames.LINEDEF.itemID
										&& it.itemActivationID == item.itemActivationID) {
									size--;
									s--;
									it.removeItem();
								}
							}
						}

						// Search through all the doors
						for (int k = 0; k < Game.doors.size(); k++) {
							Door door = Game.doors.get(k);

							// If door has the same activation ID as the
							// button then activate it.
							if (door.itemActivationID == item.itemActivationID) {
								/*
								 * If the itemActivationID is the special ID, then just stop the door from
								 * automatically opening and closing. Otherwise activate the door as normal.
								 */
								if (door.itemActivationID == 2112) {
									// Hoping no one uses this id, but
									// this stops the door from automatically
									// opening and closing continuously.
									door.itemActivationID = 1221;
								}
								// If this special ID, activate it to continue to
								// move
								else if (door.itemActivationID == 1221) {
									door.itemActivationID = 2112;
									door.activated = true;
									door.stayOpen = false;
								} else {
									door.activated = true;
								}

								// If client, then add this to activated things to add to the server
								if (Display.gameType == 1) {
									Game.activatedDoors.add(door);
								}
							}
						}

						// Search through all the elevators
						for (int k = 0; k < Game.elevators.size(); k++) {
							Elevator e = Game.elevators.get(k);

							// If elevator has the same activation ID as the
							// button then activate it.
							if (e.itemActivationID == item.itemActivationID) {
								/*
								 * If the itemActivationID is the special ID, then just stop the elevator from
								 * automatically moving. Otherwise activate the elevator as normal.
								 */
								if (e.itemActivationID == 2112) {
									// Hoping no one uses this id, but
									// this stops the elevator from automatically
									// moving continuously.
									e.itemActivationID = 1221;
								}
								// If this special ID, activate it to continue to
								// move
								else if (e.itemActivationID == 1221) {
									e.itemActivationID = 2112;
									e.activated = true;
								} else {
									e.activated = true;
								}

								// If client, then add this to activated things to add to the server
								if (Display.gameType == 1) {
									Game.activatedElevators.add(e);
								}
							}
						}

						// Stores Items to be deleted
						ArrayList<Item> tempItems2 = new ArrayList<Item>();

						// Scan all activatable items
						for (int j = 0; j < Game.activatable.size(); j++) {
							Item itemAct = Game.activatable.get(j);

							// If Item is a Happiness Tower, activate it and
							// state that it is activated
							if (itemAct.itemID == ItemNames.RADAR.getID() && !itemAct.activated
									&& item.itemActivationID == itemAct.itemActivationID) {
								itemAct.activated = true;
								Display.messages.add(new PopUp("COM SYSTEM ACTIVATED"));
								SoundController.uplink.playAudioFile(0);
							} else {
								// If item is enemy spawnpoint, then spawn the
								// enemy, and add the item to the arraylist of
								// items to be deleted
								if (itemAct.itemID == ItemNames.ENEMYSPAWN.getID()
										&& itemAct.itemActivationID == item.itemActivationID) {
									Game.enemiesInMap++;
									game.addEnemy(itemAct.x, itemAct.z, itemAct.rotation);
									tempItems2.add(itemAct);
								}
								// If Explosion has same activation ID of the button
								// then activate it
								else if (itemAct.itemID == ItemNames.ACTIVATEEXP.getID()
										&& itemAct.itemActivationID == item.itemActivationID) {
									new Explosion(itemAct.x, itemAct.y, itemAct.z, 0, 0);
									tempItems2.add(itemAct);
								}
								// If it gets rid of a wall, delete the wall and create an
								// air wall in its place.
								else if (itemAct.itemID == ItemNames.WALLBEGONE.getID()
										&& itemAct.itemActivationID == item.itemActivationID) {
									Block block2 = Level.getBlock((int) itemAct.x, (int) itemAct.z);

									// Block is effectively no longer there
									block2.height = 0;

									tempItems2.add(itemAct);
								}
							}
						}

						// Remove all the items that need to be deleted now
						for (int j = 0; j < tempItems2.size(); j++) {
							Item temp2 = tempItems2.get(j);

							temp2.removeItem();
						}
					}

					// Play audio queue if there is one
					item.activateAudioQueue();

					// Remove linedef from game
					item.removeItem();
				}

				// If a Secret block
				if (item.itemID == ItemNames.SECRET.getID()) {
					/*
					 * Activate secret
					 */
					boolean activated = item.activate();

					/*
					 * If the item was activated remove it from the block, but not from the map so
					 * that it can still keep track of how many secrets you have found.
					 */
					if (activated) {
						Player.blockOn.wallItems.remove(item);
					}
				}
			}
		} catch (Exception e) {

		}

		/*
		 * If the players y value + 4 is greater than the y of the block * 4(since the y
		 * of the block is corrected for rendering), then the player is either inside or
		 * on top of the block. The + 4 is there for stairs
		 */
		if (Player.y + 4 > (Player.blockOn.y * 4) && Player.y + 2 >= (Player.blockOn.y * 4) + Player.blockOn.height) {
			Player.maxHeight = Player.blockOn.height + (Player.blockOn.y * 4) + Player.blockOn.baseCorrect;
		} else {
			Player.maxHeight = 0;
		}

		/*
		 * If player is on an item, add that items height to the current maxHeight
		 */
		if (Player.extraHeight > 0) {
			Player.maxHeight += Player.extraHeight;
			Player.extraHeight = 0;
		}

		// If not crouching or jumping or flying, and the player is
		// not dead then reset the players y value. Also the player
		// must not be partially crouched under a block (height has to
		// be two if fully standing up).
		if (!inJump && Player.y < Player.maxHeight && Player.alive) {
			Player.y = Player.maxHeight;
		}

		/*
		 * Only reset the players yCorrect if the player is effectively not crouching
		 */
		if (Player.height >= 2 || Player.yCorrect >= Player.y) {
			Player.yCorrect = Player.y;
		}

		/*
		 * In case player is crouched and moving up stairs, this will make sure the
		 * crouched y value also changes.
		 */
		else if (Player.yCorrect + 6.0 <= Player.y) {
			Player.yCorrect = Player.y - 6.0;
		}

		// Don't allow players y value to go below 0
		if (Player.y < 0) {
			Player.y = 0;
		}

		// TODO here

		/*
		 * Causes the players movement to quickly (but not instantaniously) move to 0 if
		 * there is not reset in the players movement.
		 */
		xa *= 0.1;
		za *= 0.1;

		// Increase the players rotation based on change in rotation
		Player.rotation += rotationa;

		// Decreases rotation steadily if you stop rotating
		rotationa = 0.0d;

		// Does the same things with upRotation
		Player.upRotate += upRotationa;

		upRotationa = 0.0d;

		// Sets lower bound on upRotation
		if (Player.upRotate >= 2.8) {
			Player.upRotate = 2.8;
		}

		// Sets upper bound on upRotation
		if (Player.upRotate <= 0.3) {
			Player.upRotate = 0.3;
		}
	}

	/**
	 * Is called with a given x and z value to determine if the block the player is
	 * about to move to is free to move to or not. If it is determined to be solid,
	 * you cannot move into it, and it stops your movement, if not obviously you can
	 * move through it.
	 * 
	 * In terms of recent updates, if the block is higher than the players position
	 * and height in the y direction, then the player can also move under it. This
	 * is mainly used for doors, but will also come in handy later for multi floored
	 * maps.
	 * 
	 * Since the 1.4 Update there are two methods, each is called individually
	 * depending on the direction the player is moving so that at any height the
	 * walls will detect your collision correctly no matter what the situation is.
	 * 
	 * @param nextX
	 * @param nextZ
	 * @return
	 */
	public boolean isFree(double nextX, double nextZ) {
		// Dont let player exit the map if not in noclip
		if (nextX < 0 || nextX > Level.width || nextZ < 0 || nextZ > Level.height) {
			return false;
		}

		// Number used for how far away from block, block detects
		double z = 0.3;

		/*
		 * Determine the block the Player is about to move into given the direction that
		 * it is going. Then set this block as the block to check the collision of. It
		 * actually does this to two blocks because it checks the distance on both sides
		 * of the player as he/she moves in a certain direction, because otherwise it'll
		 * only be checking to see if the player is "z" units from the block on only one
		 * side, allowing the player to go through certain corners of blocks. This
		 * checks both sides to make sure the player is not hitting a block.
		 */
		Block block = Level.getBlock((int) (nextX - z), (int) (nextZ - z));
		Block block2 = Level.getBlock((int) (nextX - z), (int) (nextZ + z));

		if (nextX < Player.x && nextZ == Player.z) {
			block = Level.getBlock((int) (nextX - z), (int) (nextZ - z));
			block2 = Level.getBlock((int) (nextX - z), (int) (nextZ + z));
		} else if (nextX >= Player.x && nextZ == Player.z) {
			block = Level.getBlock((int) (nextX + z), (int) (nextZ - z));
			block2 = Level.getBlock((int) (nextX + z), (int) (nextZ + z));
		} else if (nextX == Player.x && nextZ >= Player.z) {
			block = Level.getBlock((int) (nextX - z), (int) (nextZ + z));
			block2 = Level.getBlock((int) (nextX + z), (int) (nextZ + z));
		} else // (xx == Player.x && zz < Player.z)
		{
			block = Level.getBlock((int) (nextX - z), (int) (nextZ - z));
			block2 = Level.getBlock((int) (nextX + z), (int) (nextZ - z));
		}

		/*
		 * Fixes bug where you could fall behind items in corners. Now it basically says
		 * if you are on top of the item, then keep your position on top of the item as
		 * long as you are still on the block that the item is on, and you are above the
		 * item.
		 */
		try {
			for (int i = 0; i < Player.blockOn.wallItems.size(); i++) {
				Item temp = Player.blockOn.wallItems.get(i);

				// If walking into block with a solid object on it
				if (temp.isSolid) {

					// Difference in y
					double yDifference = Math.abs(Player.y - Math.abs(temp.y));

					if (yDifference > temp.height + Player.height && temp.y >= Player.blockOn.y && !temp.hanging) {
						Player.extraHeight = temp.height + 5;
					}
				}
			}
		} catch (Exception e) {

		}

		try {
			for (int i = 0; i < block.wallItems.size(); i++) {
				Item temp = block.wallItems.get(i);

				// If walking into block with a solid object on it
				if (temp.isSolid) {
					// Distance between item and player
					double distance = Math.sqrt(((Math.abs(temp.x - nextX)) * (Math.abs(temp.x - nextX)))
							+ ((Math.abs(temp.z - nextZ)) * (Math.abs(temp.z - nextZ))));

					// Difference in y
					double yDifference = Math.abs(Player.y - Math.abs(temp.y));

					// If right next to item
					if (distance <= 0.3) {
						// If not above or below it enough, don't let the player
						// move into it
						if ((yDifference <= temp.height + Player.height)) {
							return false;
						}
						// Otherwise reset the players height to being the items
						// height
						else {
							// Only if on or above the block the player is on and it is not hanging
							if (temp.y >= Player.blockOn.y && !temp.hanging) {
								Player.extraHeight = temp.height + 5;
							}
						}
					}

				}
			}
		} catch (Exception e) {

		}

		// Go through all the entities in the game to make sure they are not
		// too close to you
		// TODO fix this up a bit for chairs and stuff
		for (int i = 0; i < Game.entities.size(); i++) {
			EntityParent temp = Game.entities.get(i);

			// Find the distance between the entity and player
			double distanceFromPlayer = Math.sqrt(((Math.abs(nextX - temp.xPos)) * (Math.abs(nextX - temp.xPos)))
					+ ((Math.abs(nextZ - temp.zPos)) * (Math.abs(nextZ - temp.zPos))));

			// Difference between enemy and player
			double yDifference = Math.abs(Player.y - Math.abs(temp.yPos * 11));

			// If close enough, don't allow the player to move into it unless its friendly.
			if (distanceFromPlayer <= 0.5 && (temp.isSolid || !temp.isFriendly) && !temp.isABoss) {
				// If not above or below it enough, don't let the player
				// move into it
				if ((yDifference <= temp.height + Player.height)) {
					if (temp.moveable) {
						double xMove = nextX - Player.x;
						double zMove = nextZ - Player.z;

						Block blockOn = Level.getBlock((int) (temp.xPos), (int) (temp.zPos));

						if (temp.isFree(temp.xPos + xMove, temp.zPos)) {
							temp.xPos += xMove;
						}

						if (temp.isFree(temp.xPos, temp.zPos + zMove)) {
							temp.zPos += zMove;
						}

						// Block entity is now on
						Block blockOnNew = Level.getBlock((int) (temp.xPos), (int) (temp.zPos));

						/*
						 * If the entity is on a new block, then remove the entity from the last block
						 * it was on and add it to the new block its on.
						 */
						if (!blockOnNew.equals(blockOn)) {
							blockOn.entitiesOnBlock.remove(temp);
							blockOnNew.entitiesOnBlock.add(temp);

							temp.setHeight();
						}

						// Allows entities to ride lifts/elevators
						// Basically set entities position to the position of the current
						// block.
						if (!temp.canFly && Math.abs(blockOnNew.height + temp.yPos) <= 2) {
							temp.yPos = -(blockOnNew.height + (blockOnNew.y * 4) + blockOnNew.baseCorrect) / 11;
						}

					}

					return false;
				}
				// Otherwise reset the players height to being the items
				// height
				else {
					// Only if on or above the block the player is on.
					if (temp.yPos >= Player.blockOn.y) {
						Player.extraHeight = temp.height + 5;
					}
				}

			}
			// If a boss, have the distance be farther the hitbox expands
			// out of
			else if (distanceFromPlayer <= 3 && temp.isABoss) {
				return false;
			}
		}

		// Adds the plus 4 to go up steps
		double yCorrect = Player.y + 4;

		/*
		 * After first check, no longer set the players height as being the block that
		 * he/she is in. This is just to set the player up initially.
		 */
		if (!firstCheck) {
			// Player.y = block5.height + (block5.y * 4) + block5.baseCorrect;
			Player.maxHeight = Player.y;
			firstCheck = true;
		}

		/*
		 * Because the blocks Y in correlation to the players Y is 4 times less than
		 * what the players y would be at that height visually. (Ex. When a wall is at a
		 * y of 3, to the player it looks like it is at a y of 12, though it is not)
		 * This corrects it so the wall height being checked is the same height the
		 * player thinks it is. Do this for block2 as well
		 */
		double blockY = block.y * 4;
		double block2Y = block2.y * 4;

		// If either block the player is moving into is solid
		if (block.isSolid || block2.isSolid) {
			// Make sure the player is not hitting either of the
			// blocks
			return (checkCollision(block, blockY, yCorrect) && checkCollision(block2, block2Y, yCorrect));
		}

		return true;
	}

	/**
	 * A private helper method that helps check to see if the Player can move into
	 * the block that is sent in, and resets the Players maximum standing height if
	 * he/she can to the blocks height.
	 * 
	 * @param block
	 * @param blockY
	 * @param yCorrect
	 * @return
	 */
	private boolean checkCollision(Block block, double blockY, double yCorrect) {
		/*
		 * If player is between blocks bottom and top, don't let the player get on to,
		 * or go through the block. If crouched correct this calculation a little bit so
		 * that you can go under blocks.
		 */
		if (block.height + (block.baseCorrect) + blockY > yCorrect
				&& ((Player.y == Player.yCorrect && yCorrect + 4 >= blockY) || (Player.y != Player.yCorrect
						&& (yCorrect + 4) - Math.abs(Player.y - Player.yCorrect) >= blockY))) {
			return false;
		}

		return true;
	}
}
