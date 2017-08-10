package com.vile.graphics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import com.vile.Display;
import com.vile.Game;
import com.vile.SoundController;
import com.vile.entities.Bullet;
import com.vile.entities.Corpse;
import com.vile.entities.Door;
import com.vile.entities.Enemy;
import com.vile.entities.EnemyFire;
import com.vile.entities.Entity;
import com.vile.entities.Explosion;
import com.vile.entities.Item;
import com.vile.entities.ItemNames;
import com.vile.entities.Player;
import com.vile.entities.Projectile;
import com.vile.input.Controller;
import com.vile.input.InputHandler;
import com.vile.levelGenerator.*;

/**
 * Title: Render3D
 * @author Alex Byrd
 * Date Updated: 5/2/2017
 *
 * Renders the graphics on the screen for all the entities, walls,
 * etc... It renders all these things in a raycasting environment that
 * simulates a 3D environment without using "actual" 3D.
 */
public class Render3D extends Render
{
    //Checks the fps in all graphical checks throughout all the classes
    //so that images don't ever move too fast.
    public static double fpsCheck = 0;

    /*
    * Basically an array of values used for the pixels rendered on the
    * screen to get darker as they fade into the distance. The lower the
    * value, the darker that pixel is. The pixel stays the same color, but
    * its brightness value is set by these arrays. zBufferWall is for walls
    * and zBuffer is for everything else.
    */
    private double[] zBuffer;
    private double[] zBufferWall;

    /*
    * Sets how many pixels away in the z direction the render distance
    * limiter begins.
    */
    public static double renderDistanceDefault = 100000.0;
    private double renderDistance = renderDistanceDefault;

    /*
    * Corrects the movement of walls to the players movement. If the
    * player moves, the walls look as if they are staying in place though
    * they are truly not, they are just moving away at enough speed to
    * simulate that they are staying in place with respect to the player.
    */
    private double forwardCorrect = 3;
    private double rightCorrect   = 3;

    //Speeds and turning values
    private double fowardSpeed, rightSpeed, cosine, sine;

    //Default ceiling height
    public static double ceilingDefaultHeight = 200;

    //Stores the id of the current wall being rendered to keep track
    public static int currentWallID = 0;

    //Is low res graphic settings on?
    public static boolean lowRes = false;

    ArrayList<Block> allBlocks;

    /**
     * Sets up new Render3D object with a width and height. Also creates a
     * ZBufferWall object which holds the current position of the nearest
     * wall to the player. This is so the player can not see things
     * that are behind walls. ZBuffer is the darkness basically of each
     * pixel, mainly used for determining render distance stuff
     * @param width
     * @param height
     */
    public Render3D(int width, int height)
    {
        super(width, height);
        zBuffer     = new double[HEIGHT * WIDTH];
        zBufferWall = new double[HEIGHT * WIDTH];
    }

    /**
     * Initially called floor because it just rendered the floor, and the
     * ceiling which was just an extension of the floor. Now it is called
     * to render everything and call all the other rendering methods.
     * @param game
     */
    public void floor(Game game)
    {
        //Play announcement that correlates to the map loaded in
        if((Game.mapNum == 1 || Game.mapNum == 9) && !game.firstSound
                && Game.setMap)
        {
            SoundController.level1Anouncement.playAudioFile();
            game.firstSound = true;
        }
        else if((Game.mapNum == 2 || Game.mapNum == 6 ||
                Game.mapNum == 8 || Game.mapNum == 10)
                && !game.firstSound && Game.setMap)
        {
            SoundController.creepySound.playAudioFile();
            game.firstSound = true;
        }

        //If low res graphics settings, set low res to true
        if(Display.graphicsSelection == 2
                || Display.graphicsSelection == 0
                || Display.graphicsSelection == 4)
        {
            lowRes = true;
        }
        else
        {
            lowRes = false;
        }

	   /*
	    * Determines how textures should change due to the players rotation,
	    * and movement speed.
	    */
        cosine        = Math.cos(Player.rotation);
        sine          = Math.sin(Player.rotation);
        fowardSpeed   = Player.z * 21.3;
        rightSpeed    = Player.x * 21.3;

        //Ceiling Height and floor depth in accordance to where the
        //Player is in the y direction
        double ceilingHeight = ceilingDefaultHeight -
                Player.y;
        double floorDepth    = 8 +
                Player.y;

        //If night time, then be the darkest map theme
        if(Display.themeNum == 1 && !Game.setMap)
        {
            renderDistanceDefault = 50000;
        }
        //This is the default survival render distance
        else
        {
            if(!Game.setMap)
            {
                renderDistanceDefault = 100000;
            }
        }

        //If weapon isn't being fired, keep the brightness as it normally
        //is, else make it slightly brighter to simulate a shot.
        if(Player.weapons[Player.weaponEquipped].weaponShootTime == 0
                && Player.weapons[Player.weaponEquipped]
                .weaponShootTime2 == 0)
        {
            renderDistance = renderDistanceDefault;
        }
        else
        {
            renderDistance = renderDistanceDefault + 5000;
        }

        //Increase by 1 pixel at a time
        int correction = 1;

        //If low graphics, then increase by 2 pixels at a time
        if(lowRes)
        {
            correction = 2;
        }

        zBufferWall = new double[HEIGHT * WIDTH];
        zBuffer = new double[HEIGHT * WIDTH];

        //Corrects it so that at a higher fps rate, the animations
        //and movements in the game are not incredibly faster than
        //they should be.
        fpsCheck = ((Display.fps / 40) + 1);

        //Things were going too fast under 40fps
        //So keep correction as being 2
        if(Display.fps <= 40)
        {
            fpsCheck = 2;
        }

        //Go through all pixels in y direction
        for (int y = 0; y < HEIGHT; y+=correction)
        {
		   /*
		    * Sets the ceiling and the floor basically, ceiling is both
		    * of them. they are horizontal lines drawn at certain y coordinates
		    * that basically decrease in distance between by a factor of 2.0 each
		    * time they are drawn, then they are divided by height to get the squares
		    * to look right.
		    */
            double ceiling = (y - HEIGHT
                    / Math.sin(Player.upRotate)
                    * Math.cos(Player.upRotate)) /
                    HEIGHT;



		   /*
			* Determines where the floor and ceiling should meet. This
			* is basically the depth of field. Pixels seem to get smaller
			* and more out of focus the "farther" they seem to be from
			* the player.
			*/
            double z       = floorDepth / ceiling;

		   /*
		    * Makes the actual ceiling of the frame move the same direction as the
		    * floor instead of being the inverse of the of the floor. Also sets the
		    * distance the ceiling is above the player.
		    */
            if (ceiling < 0)
            {
                z       = ceilingHeight / -ceiling;
            }

            //Go through all pixels on the x
            for (int x = 0; x < WIDTH; x+=correction)
            {
			   /*
			    * Sets the bars along the x that go up in y. As
			    * the bars go into the distance, the distance between them
			    * decreases by a factor of 2.0. Then the distance between
			    * them is / HEIGHT to set them equal to the distance between
			    * the y ones, to make squares.
			    */
                double depth = (x - WIDTH
                        / 2.0) / HEIGHT;

                //Test depth. Works similarly but has issues
                //double depth = 2 * x / (double)WIDTH - 1;

                //Sets the depth relative to z direction
                depth *= z;

			   /*
			    * Positions of floor and ceiling pixels on screen in
			    * accordance to how much the player has moved already and
			    * in which direction he/she is looking. This is very hard
			    * to explain what is going on, but as seen here with the
			    * cosine and sine formulas, the map is a x and y grid
			    * in a way, and when you rotate, the pixels around you
			    * act sort of like a circle, and you rotate, the textures
			    * rotate with you in a way to make it seem as if you are
			    * turning. And if you go foward or backwards they will
			    * move in accordance to that as well.
			    */
                double xx = ((depth * (cosine)) + (z * (sine))) + rightSpeed;
                double yy = ((z * cosine) - (depth * sine)) + fowardSpeed;

			   /*
			    * This makes the squares the floor and ceiling are made up
			    * of smaller the bigger the number is, and bigger the smaller
			    * the number is.
			    */
                double textureCorrect = 16;

                //If moon theme, textures are bigger
                if(Display.themeNum == 4)
                {
                    textureCorrect = 2;
                }

			   /*
			    * I still don't understand this part. It multiplies
			    * the integer bits by 255 so that it can get the
			    * particular pixel from the 256 by 256 pixel image
			    * file, but otherwise it confuses me how this works.
			    *
			    * I guess textureCorrect is something that determines
			    * how much the pixels are stretched on screen
			    */
                int xPix = (int) ((xx) * textureCorrect) & 255;
                int yPix = (int) ((yy) * textureCorrect) & 255;

			   /*
			    * Ceiling Height changes depending on theme of the
			    * game.
			    */
                if(Display.themeNum == 0)
                {
                    ceilingDefaultHeight  = 1000.0;
                }
                else if(Display.themeNum == 1)
                {
                    ceilingDefaultHeight  = 100.0;
                }
                else if(Display.themeNum == 4)
                {
                    ceilingDefaultHeight  = 100.0;
                }

                zBuffer[(x) + (y) * WIDTH] = z;

                //Deal with 4 pixels in one loop if low res mode
                if(lowRes)
                {
                    zBuffer[(x+1) + (y+1) * WIDTH] = z;
                    zBuffer[(x+1) + (y) * WIDTH] = z;
                    zBuffer[(x) + (y+1) * WIDTH] = z;
                }

                //If not outdoors mode
                if(Display.themeNum != 0)
                {
                    //For the sky
                    if(ceiling < 0)
                    {
					   /*
					    * Depending on the ID of the ceiling texture, then
					    * render that texture that is chosen by picking it
					    * out from the floors array from the textures
					    * class
					    */
                        PIXELS[x + y * WIDTH] = //((xPix) << 8 | (yPix) << 16);
                                Textures.floors[Game.mapCeiling].PIXELS
                                        [(xPix & 255) + (yPix & 255) * 256];

                        //Here I show that it can be shown in
                        //hexidecimals as well as normal decimals
                        if(lowRes)
                        {
                            PIXELS[(x+1) + (y) * WIDTH] =
                                    Textures.floors[Game.mapCeiling].PIXELS
                                            [(xPix & 255) + (yPix & 255) * 256];;
                            PIXELS[(x+1) + (y+1) * WIDTH] =
                                    Textures.floors[Game.mapCeiling].PIXELS
                                            [(xPix & 255) + (yPix & 255) * 256];;
                            PIXELS[(x) + (y+1) * WIDTH] =
                                    Textures.floors[Game.mapCeiling].PIXELS
                                            [(xPix & 255) + (yPix & 255) * 256];;
                        }
                    }
                    else
                    {
                        PIXELS[x + y * WIDTH] = //((xPix) << 8 | (yPix) << 16);
                                Textures.floors[Game.mapFloor].PIXELS
                                        [(xPix & 255) + (yPix & 255) * 256];

                        if(lowRes)
                        {
                            PIXELS[(x+1) + (y) * WIDTH] = //((xPix) << 8 | (yPix) << 16);
                                    Textures.floors[Game.mapFloor].PIXELS
                                            [(xPix & 255) + (yPix & 255) * 256];
                            PIXELS[(x+1) + (y+1) * WIDTH] = //((xPix) << 8 | (yPix) << 16);
                                    Textures.floors[Game.mapFloor].PIXELS
                                            [(xPix & 255) + (yPix & 255) * 256];
                            PIXELS[(x) + (y+1) * WIDTH] = //((xPix) << 8 | (yPix) << 16);
                                    Textures.floors[Game.mapFloor].PIXELS
                                            [(xPix & 255) + (yPix & 255) * 256];
                        }
                    }
                }
                //If Outdoors theme mode
                else
                {
                    if(ceiling < 0)
                    {
					   /*
					    * Makes the blue sky color using bit shifting for each
					    * pixel. These are split up as RGB values. The |
					    * basically merges these values together in binary so
					    * that the colors blend in a way.
					    */
                        PIXELS[x + y * WIDTH] = 0 | 255 << 8 | 255;

                        //Here I show that it can be shown in
                        //hexidecimals as well as normal decimals
                        if(lowRes)
                        {
                            PIXELS[(x+1) + (y) * WIDTH] =
                                    0 | 0x00FF00 | 0x0000FF;
                            PIXELS[(x+1) + (y+1) * WIDTH] =
                                    0 | 65280 | 255;
                            PIXELS[(x) + (y+1) * WIDTH] =
                                    0x00FFFF;
                        }
                    }
                    else
                    {
                        PIXELS[x + y * WIDTH] = //((xPix) << 8 | (yPix) << 16);
                                Textures.floors[9].PIXELS
                                        [(xPix & 255) + (yPix & 255) * 256];

                        if(lowRes)
                        {
                            PIXELS[(x+1) + (y) * WIDTH] = //((xPix) << 8 | (yPix) << 16);
                                    Textures.floors[9].PIXELS
                                            [(xPix & 255) + (yPix & 255) * 256];
                            PIXELS[(x+1) + (y+1) * WIDTH] = //((xPix) << 8 | (yPix) << 16);
                                    Textures.floors[9].PIXELS
                                            [(xPix & 255) + (yPix & 255) * 256];
                            PIXELS[(x) + (y+1) * WIDTH] = //((xPix) << 8 | (yPix) << 16);
                                    Textures.floors[9].PIXELS
                                            [(xPix & 255) + (yPix & 255) * 256];
                        }
                    }
                }
            }
        }

        allBlocks = new ArrayList<Block>();

        renderBlocks();

	   /*
	    * Used to correct the players y in case the player is
	    * crouching and the y goes below the maxHeight the player
	    * can stand on.
	    */
        double playerYCorrect = Player.y;

        if(playerYCorrect < Player.maxHeight)
        {
            playerYCorrect = Player.maxHeight;
        }

	   /*
	    * For every enemy on the map, render each, and also update all
	    * of its values and such as well. These type of things used to
	    * be updated in the Game class but because the game already
	    * goes through all the enemies here anyway, instead of going
	    * through all of them twice, this only calls all of them
	    * once, increasing the fps and optimizing the game even more,
	    * especially on very slow computers and with huge amounts of
	    * enemies.
	    */
        for(int i = 0; i < Game.enemies.size(); ++i)
        {
            Enemy enemy = Game.enemies.get(i);

		   /*
		    * If the player is within 1 block of the enemy, or if
		    * the player is playing survival mode, then activate the
		    * enemy to start moving and acting.
		    *
		    * Player has to be alive, and the enemy cannot already
		    * be activated for this to occur. Also the gamemode cannot
		    * be peaceful otherwise the sound just gets annoying.
		    *
		    * Enemy can also be activated if seen by the player
		    */
            if((enemy.distance <= 1 &&
                    enemy.distance >= 0
                    || !Game.setMap) && !enemy.activated
                    && Player.alive && Game.skillMode > 0)
            {
                //Activate enemy
                enemy.activated = true;

			   /*
			    * Play the audio file that correlates to each enemies
			    * activation sound.
			    */
                if(enemy.ID == 6)
                {
                    SoundController.bossActivate.playAudioFile();
                }
                else if(enemy.ID == 8)
                {
                    SoundController.belegothActivate.playAudioFile();
                }
                else if(enemy.ID == 1)
                {
                    SoundController.enemyActivate.playAudioFile();
                }
                else if(enemy.ID == 2)
                {
                    SoundController.enemy2Activate.playAudioFile();
                }
                else if(enemy.ID == 3)
                {
                    SoundController.enemy3Activate.playAudioFile();
                }
                else if(enemy.ID == 4)
                {
                    SoundController.enemy4Activate.playAudioFile();
                }
                else if(enemy.ID == 5)
                {
                    SoundController.enemy5Activate.playAudioFile();
                }
                else if(enemy.ID == 7)
                {
                    SoundController.enemy7Activate.playAudioFile();
                }
            }

            //Deactivate the enemies if the player is dead and they have
            //no other target
            if(Player.health <= 0 && enemy.targetEnemy == null)
            {
                enemy.activated = false;
            }

            //tick the enemy, updating its values and such
            enemy.tick(enemy);

            //Update the enemies movements
            enemy.move();

		   /*
		    * If the enemy is within range of the door, open the
		    * door because enemies are smarter than just letting a
		    * door stop them from killing you.
		    *
		    * Also the enemy has to be activated to open doors.
		    *
		    * Flying enemies can't open doors because they can
		    * just fly over them
		    */
            if(!enemy.canFly && enemy.activated)
            {
                for(int a = 0; a < Game.doors.size(); a++)
                {
                    Door door = Game.doors.get(a);

                    //If door is in range of enemy, have it open as long
                    //as it is not a door requiring a key
                    if(Math.abs(door.getZ() - enemy.zPos) <= 1
                            && Math.abs(door.getX() - enemy.xPos) <= 1
                            && door.itemActivationID == 0)
                    {
                        if(door.doorType == 0)
                        {
                            door.activated = true;
                        }
                    }
                }
            }

		   /*
	        * Attempt to attack the player as long as the player is
	        * alive. The method itself will determine what attack if
	        * any the enemy will use though.
	        */
            enemy.attack(playerYCorrect);

            //Correct the enemies y so it appears correct graphically
            double yCorrect = enemy.getY();

		   /*
		    * Because the boss is so gosh darn big, correct his graphical
		    * height of the ground so it looks like he is touching the
		    * ground.
		    */
            if(enemy.isABoss)
            {
                yCorrect -= 35;
            }

            //How high the enemy should be raised as it goes into the
            //distance so it doesn't go below the ground graphically
            double yCorrection = 0.04;
            double change = 2;

            //Changes depending on the graphics settings
            if(Display.graphicsSelection >= 2
                    && Display.graphicsSelection < 4)
            {
                yCorrection = 0.02;
            }
            else if(Display.graphicsSelection >= 4)
            {
                yCorrection = 0;
            }

            //Enemy is raised farther up the farther it is away from
            //the player
            yCorrect -= (enemy.distanceFromPlayer / change) * yCorrection;

            //Call method to render Enemy
            renderEnemy(enemy.getX(), yCorrect, enemy.getZ(), 0.2,
                    enemy.ID, enemy);
        }

        //All entities are done checking if player is in their sight
        Entity.checkSight = false;

        //Items to delete
        ArrayList<Item> tempItems = new ArrayList<Item>();

	   /*
	    * For each item, render correctly and update its values, as
	    * well as see if it can be picked up by the player to optimize the
	    * game since it already has to call this method anyway.
	    */
        for(int i = 0; i < Game.items.size(); ++i)
        {
            Item item = Game.items.get(i);

            //Block item is over or on
            Block temp = Level.getBlock((int)item.x, (int)item.z);

		   /*
		    * If the items y is greater than the blocks height plus
		    * its y value, then decrease the items y until it reaches
		    * the ground.
		    */
            if(item.y > temp.height + temp.y && !temp.isaDoor)
            {
                item.y -= 1;
            }
            else
            {
                //Have item stay on top of the block if pretty much
                //fallen as far as it can.
                if(!temp.isaDoor && item.y >= temp.height + temp.y)
                {
                    item.y = temp.height + temp.y;
                }
                else
                {
                    //If landed on flat ground
                    item.y = 0;
                }
            }

            double distance =
                    Math.sqrt(((Math.abs(Player.x - item.x))
                            * (Math.abs(Player.x - item.x)))
                            + ((Math.abs(Player.z - item.z))
                            * (Math.abs(Player.z - item.z))));

            //If it is a line def
            if(item.itemID == ItemNames.LINEDEF.itemID &&
                    distance <= 0.5)
            {
                //If End Level Line Def
                if(item.itemActivationID == 0)
                {
                    Game.mapNum++;
                    game.loadNextMap(false, "");
                }
                else
                {
                    //Search through all the doors
                    for(int k = 0; k < Game.doors.size(); k++)
                    {
                        Door door = Game.doors.get(k);

                        //If door has the same activation ID as the
                        //button then activate it.
                        if(door.itemActivationID
                                == item.itemActivationID)
                        {
                            door.activated = true;
                        }
                    }

                    //Stores Items to be deleted
                    ArrayList<Item> tempItems2 = new ArrayList<Item>();

                    //Scan all activatable items
                    for(int j = 0; j < Game.activatable.size(); j++)
                    {
                        Item itemAct = Game.activatable.get(j);

                        //If Item is a Happiness Tower, activate it and
                        //state that it is activated
                        if(itemAct.itemID == ItemNames.RADAR.getID()
                                && !itemAct.activated &&
                                item.itemActivationID ==
                                        itemAct.itemActivationID)
                        {
                            itemAct.activated = true;
                            Display.itemPickup = "COM SYSTEM ACTIVATED!";
                            Display.itemPickupTime = 1;
                        }
                        else
                        {
                            //If item is enemy spawnpoint, then spawn the
                            //enemy, and add the item to the arraylist of
                            //items to be deleted
                            if(itemAct.itemID == ItemNames.ENEMYSPAWN.getID()
                                    && itemAct.itemActivationID
                                    == item.itemActivationID)
                            {
                                Game.enemiesInMap++;
                                game.addEnemy(itemAct.x, itemAct.z,
                                        itemAct.rotation);
                                tempItems2.add(itemAct);
                            }
                            //If Explosion has same activation ID of the button
                            //then activate it
                            else if(itemAct.itemID ==
                                    ItemNames.ACTIVATEEXP.getID()
                                    && itemAct.itemActivationID
                                    == item.itemActivationID)
                            {
                                new Explosion(itemAct.x, itemAct.y,
                                        itemAct.z, 0, 0);
                                tempItems2.add(itemAct);
                            }
                            //If it gets rid of a wall, delete the wall and create an
                            //air wall in its place.
                            else if(itemAct.itemID
                                    == ItemNames.WALLBEGONE.getID()
                                    && itemAct.itemActivationID ==
                                    item.itemActivationID)
                            {
                                Block block2 = Level.getBlock
                                        ((int)itemAct.x, (int)itemAct.z);

                                //Block is effectively no longer there
                                block2.height = 0;

                                tempItems2.add(itemAct);
                            }
                        }
                    }

                    //Remove all the items that need to be deleted now
                    for(int j = 0; j < tempItems2.size(); j++)
                    {
                        Item temp2 =  tempItems2.get(j);

                        temp2.removeItem();
                    }
                }
            }

            //If the item is at least 0.7 units away, and its not
            //a secret, and the player is not in noClip mode
            if (distance <= 0.7
                    && Math.abs(item.y - playerYCorrect) <= 3
                    && item.itemID != ItemNames.SECRET.itemID
                    && !Controller.noClipOn)
            {
                //Was the object activated?
                boolean activated = item.activate();

			   /*
			    * If the item was activated remove it. Otherwise keep
			    * it in the map.
			    */
                if(activated)
                {
                    tempItems.add(item);
                }
            }

		   /*
		    * Again because the units visually are different from the
		    * actual units, this corrects the height so that it matches
		    * the height that the players can visually pick it up.
		    *
		    * For instance if an item is at height 12. It is going to
		    * look like it is at like a height of 36. The player can
		    * still pick it up, but it will look like its not
		    * supposed to be picked up. This corrects that.
		    */
            double yCorrect = 0 - ((item.y/10)) -(item.y/12);

            if(item.y >= 12)
            {
                yCorrect = 0.2 - (item.y/11.7) - 0.6;
            }

		   /*
		    * The bigger the sprite needs to be for the item being
		    * displayed, then lift it higher off the ground so that
		    * it does not seem to clip through the ground.
		    */
            if(item.itemID == ItemNames.MEGAHEALTH.getID()
                    || item.itemID == ItemNames.SHOTGUN.getID()
                    || item.itemID == ItemNames.SKULLOFRES.getID()
                    || item.itemID == ItemNames.BIOSUIT.getID()
                    || item.itemID >= ItemNames.CHAINMEAL.getID())
            {
                yCorrect -= 0.5;
            }

            //Smaller sprites
            if(item.itemID == ItemNames.HOLYWATER.getID()
                    || (item.itemID >= ItemNames.HEALTHPACK.getID()
                    && item.itemID <= ItemNames.YELLOWKEY.getID())
                    || item.itemID == ItemNames.TABLE.getID()
                    || item.itemID == ItemNames.LAMPTABLE.getID()
                    || item.itemID == ItemNames.GOBLET.getID())
            {
                yCorrect -= 0.3;
            }
            //Larger sprites
            else if(item.itemID >= ItemNames.TORCH.getID()
                    && item.itemID < ItemNames.CHAINMEAL.getID())
            {
                yCorrect -= 0.8;
            }

            //If Satillite Dish
            else if(item.itemID == ItemNames.RADAR.getID())
            {
                yCorrect -= 2.5;
            }

            //Same as above loops.
            double yCorrection = 0.04;
            double change = 2;

            if(Display.graphicsSelection >= 2
                    && Display.graphicsSelection < 4)
            {
                yCorrection = 0.02;
            }
            else if(Display.graphicsSelection >= 4)
            {
                yCorrection = 0;
            }

            //Corrects y graphics visually so item looks right to
            //the player
            yCorrect -= (distance / change) * yCorrection;

            //Only render item if it is seeable (can be seen by player).
            if(item.isSeeable)
            {
                renderItems(item.x, yCorrect + 0.77, item.z,
                        0, item.itemID, item);
            }
        }

        //Remove all items within list
        for(int j = 0; j < tempItems.size(); ++j)
        {
            Item temp = tempItems.get(j);

            //Removes all references of item in game
            temp.removeItem();
        }

	   /*
	    * Renders all the corpses in the game, as well as updating their
	    * values for optimization purposes so the game doesn't have to
	    * run through all the corpses twice per tick.
	    */
        for(int i = 0; i < Game.corpses.size(); i++)
        {
            Corpse corpse = Game.corpses.get(i);

            //Distance from player corpse is
            double distance =
                    Math.sqrt(((Math.abs(Player.x - corpse.x))
                            * (Math.abs(Player.x - corpse.x)))
                            + ((Math.abs(Player.z - corpse.z))
                            * (Math.abs(Player.z - corpse.z))));

            //Block corpse is on
            Block temp = Level.getBlock((int)corpse.x, (int)corpse.z);

		   /*
		    * If the items y is greater than the blocks height plus
		    * its y value, then decrease the items y until it reaches
		    * the ground.
		    */
            if(corpse.y > temp.height + temp.y + 3 && !temp.isaDoor)
            {
                corpse.y -= 1;
            }
            else
            {
                //If not a door, and negligable landed on the block
                //set finalize the corpses height on the block now.
                if(!temp.isaDoor && corpse.y >= temp.height + temp.y)
                {
                    corpse.y = temp.height + temp.y + 3;
                }
                else
                {
                    //If on flat ground, the height is 3. This is
                    //so the textures don't go through the ground.
                    corpse.y = 3;
                }
            }

            corpse.tick();

		   /*
		    * If in Death cannot hurt me mode, the corpses will resurrect
		    * on their own after 10000 ticks.
		    */
            if(Display.skillMode == 4)
            {
                //If 10000 ticks have passed with correction for the fps
                //and the corpse is not just a default corpse
                if(corpse.time > (10000 / ((Display.fps / 30) + 1))
                        && corpse.enemyID != 0)
                {
				   /*
				    * Reconstruct enemy depending on the ID the corpse
				    * was before it died. Also activate the new resurrected enemy.
				    */
                    Enemy newEnemy = new Enemy(corpse.x, 0, corpse.z,
                            corpse.enemyID, 0, 0);

                    //Activate this new enemy
                    newEnemy.activated = true;

                    //Add enemy to the game
                    Game.enemies.add(newEnemy);

                    //Remove the corpse
                    Game.corpses.remove(corpse);

                    //Add to enemies in the map
                    Game.enemiesInMap++;

                    //Play teleportation/respawn sound effect
                    SoundController.teleportation.playAudioFile();
                }
            }

		   /*
		    * Determine correction it needs to look right at the
		    * height level it is at.
		    */
            double totalHeight = temp.height + temp.y;
            double heightCorrect = 0;

            if(totalHeight >= 0 && totalHeight < 18)
            {
                heightCorrect = 8;
            }
            else if(totalHeight >= 18 && totalHeight < 30)
            {
                heightCorrect = 9;
            }
            else if(totalHeight >= 30 && totalHeight <= 36)
            {
                heightCorrect = 9;
            }
            else if(totalHeight > 36 && totalHeight <= 48)
            {
                heightCorrect = 10;
            }
            else if(totalHeight <= 79)
            {
                heightCorrect = 10;
            }
            else
            {
                double addCorrect;
                totalHeight -= 60;

                addCorrect = totalHeight / 20;
                heightCorrect = 10 + (0.5 * addCorrect);

            }

            //The correct y it will graphically be displayed as on
            //the screen
            double yCorrect = (-corpse.y / heightCorrect) + 0.5;

            //Same as loops above
            double yCorrection = 0.04;
            double change = 2;

            if(Display.graphicsSelection >= 2
                    && Display.graphicsSelection < 4)
            {
                yCorrection = 0.02;
            }
            else if(Display.graphicsSelection >= 4)
            {
                yCorrection = 0;
            }

            yCorrect -= (distance / change) * yCorrection;

            //Render corpse
            renderCorpse(corpse.x, yCorrect, corpse.z,
                    0, corpse);

        }

	   /*
	    * Ticks here since this will be called anyway, and makes it
	    * so that it doesn't have to run through all the explosions
	    * twice since this runs through them anyway.
	    */
        for(int i = 0; i < Game.explosions.size(); i++)
        {
            Explosion explosion = Game.explosions.get(i);

            explosion.tick();

            renderExplosion(explosion.x, explosion.y, explosion.z,
                    0, explosion);
        }

	   /*
	    * Mainly for shotgun bullets. Since they are in a spread shot, and
	    * could potentially hit the enemy at the same time, then make it so
	    * that the sound of hitting the enemy doesn't play several times in
	    * the same tick otherwise it'll be a super loud sound, and it's
	    * annoying. This checks for that and prevents it.
	    *
	    * These reset the values to false each tick.
	    */
        Projectile.bossHit = false;
        Projectile.enemyHit = false;

	   /*
	    * Renders all the bullet objects and moves the
	    * bullets as well. It goes through this anyway so
	    * adding movement to this only seemed right in
	    * optimizing the game more.
	    */
        for(int i = 0; i < Game.bullets.size(); i++)
        {
            Bullet bullet = Game.bullets.get(i);

            bullet.move();

            renderProjectiles(bullet.x, bullet.y, bullet.z,
                    0.2, bullet.ID);
        }

	   /*
	    * Renders all the enemy projectiles and moves them here for the
	    * same reasons as stated above for bullets.
	    */
        for(int i = 0; i < Game.enemyProjectiles.size(); i++)
        {
            EnemyFire temp = Game.enemyProjectiles.get(i);

            temp.move();

            renderProjectiles(temp.x, temp.y, temp.z,
                    0.2, temp.ID);
        }
    }

    /*
    * Renders each enemy on the map as a sprite. Each sprites position is
    * corrected for the players movement so that it does not move (other
    * than its own movement) in correlation to the players movement.
    */
    public void renderEnemy(double x, double y, double z, double hOffSet, int ID, Enemy enemy)
    {
	   /*
	    * The change in x, y, and z coordinates in correlation to the player to
	    * correct them in accordance to the players position so that they don't
	    * seem to move. Casts a vector between the item and Player to
	    * figure out the difference in distance.
	    */
        double xC 		        = (x - Player.x) * 1.9;
        double yC 			    = (y / enemy.heightCorrect) + (Player.y * 0.1);
        double zC 			    = (z - Player.z) * 1.9;

        //Size of sprite in terms of pixels. 512 x 512 is typical
        int spriteSize          = 512;

        //If a boss, have a bigger sprite size
        if(enemy.isABoss)
        {
            spriteSize = 4096;
        }

	   /*
	    * Corrects for rotation of the player. Meaning as the player
	    * rotates around the object, the object is always turned
	    * towards the player no matter where the player is facing.
	    *
	    * xC is basically a change in x vector, and zC is a change
	    * in z vector, and using the players rotation values it figures
	    * out how the textures should rotate to always face towards the
	    * player.
	    */
        double rotX      =
                xC * cosine - zC * sine;
        double rotY      =
                yC;
        double rotZ      =
                zC * cosine + xC * sine;

	   /*
	    * Centers the way the sprites are rendered towards the center
	    * of the screen as that is the center of your view
	    */
        double xCenter   = WIDTH / 2;
        //double yCenter   = HEIGHT / 2;

        //How wide in terms of pixels the sprite is on screen
        double xPixel    = ((rotX / rotZ) * HEIGHT) + xCenter;

        //How high in terms of pixels the texture is on screen.
        //Has to be slightly corrected when player looks up
        //or down.
        double yPixel    = ((rotY / rotZ) * HEIGHT) +
                (HEIGHT / Math.sin(Player.upRotate)
                        * Math.cos(Player.upRotate));

	   /*
	    * Figures out the sides of the object being rendered by drawing
	    * vectors to them. These sides will be used in int form below
	    * to loop through the image rendering all of its pixels to the
	    * screen.
	    */
        double xPixelL   = xPixel - (spriteSize / rotZ);
        double xPixelR   = xPixel + (spriteSize / rotZ);
        double yPixelL   = yPixel - (spriteSize / rotZ);
        double yPixelR   = yPixel + (spriteSize / rotZ);

        //Converts them into ints
        int xPixL = (int)(xPixelL);
        int xPixR = (int)(xPixelR);
        int yPixL = (int)(yPixelL);
        int yPixR = (int)(yPixelR);

        //I used this because it didn't work without this.
        rotZ *= 8;

        //Dimensions of image being loaded in
        int imageDimensions = 256;

        //Updates enemy phase
        enemy.enemyPhase++;

        //Is enemy in the players sight
        enemy.inSight = false;

	   /*
	    * Make sure it doesn't try to render textures off the screen
	    * because they can't be
	    */
        if(yPixR > HEIGHT)
        {
            yPixR = HEIGHT;
        }
        else if(yPixR < 0)
        {
            yPixR = 0;
        }

        if(xPixR > WIDTH)
        {
            xPixR = WIDTH;
        }
        else if(xPixR < 0)
        {
            xPixR = 0;
        }

        if(yPixL > HEIGHT)
        {
            yPixL = HEIGHT;
        }
        else if(yPixL < 0)
        {
            yPixL = 0;
        }

        if(xPixL > WIDTH)
        {
            xPixL = WIDTH;
        }
        else if(xPixL < 0)
        {
            xPixL = 0;
        }

        try
        {
		   /*
		    * Performs different operations when looping through the pixels
		    * depending on whether low resolution is turned on or not. This
		    * makes the game faster than having if statements within the
		    * double for loops.
		    */
            if(lowRes)
            {
			   /*
			    * Render each pixel in the sprite correctly, and with the
			    * (zBuffer) as well so that sprites behind other sprites will
			    * not clip through sprites in front of them.
			    *
			    * For each sprite also texture them correctly
			    * and correct the textures for rotation and movement.
			    */
                for(int yy = yPixL; yy < yPixR; yy+=2)
                {
                    //How the sprite rotates up or down based on players up and
                    //down rotation
                    double pixelRotationY = -((yy - yPixelR) / (yPixelL - yPixelR));

                    //Sets the current pixel to an int, and multiplies it by
                    //256 since the image is 256 by 256
                    int yTexture = (int)(pixelRotationY * imageDimensions);

                    //Go through width of image now
                    for(int xx = xPixL; xx < xPixR; xx+=2)
                    {
                        //Corrects the way the pixel faces in the x direction
                        //depending on the players rotation
                        double pixelRotationX = -(xx - xPixelR) / (xPixelL - xPixelR);

                        //Sets the current pixel to an int and multiplies it by
                        //256 as that is the size of the image.
                        int xTexture = (int)(pixelRotationX * imageDimensions);

                        //Can the textures be seen
                        boolean seen = false;

					   /*
					    * In case a rare bug continues to occur.
					    */
                        if(zBuffer[(xx) + (yy) * WIDTH] < 0)
                        {
                            zBuffer[(xx) + (yy) * WIDTH] = 0;
                        }

                        try
                        {
                            //If not behind something else, and still has pixels
                            //to be rendered, the enemy can be seen
                            if(zBuffer[(xx) + (yy) * WIDTH] > rotZ
                                    && zBuffer[(xx) + (yy+1) * WIDTH] > rotZ
                                    && zBuffer[(xx+1) + (yy+1) * WIDTH] > rotZ
                                    && zBuffer[(xx+1) + (yy) * WIDTH] > rotZ)
                            {
                                seen = true;
                            }
                        }
                        catch(Exception e)
                        {
                            continue;
                        }

                        //If pixel is seen and can be rendered
                        if(seen)
                        {
                            int color = 0;

                            //Catch any weird errors and just continue
                            try
                            {
                                //Set the color of the pixel to be generated.
                                color = enemy.currentPhase.PIXELS
                                        [(xTexture & 255) + (yTexture & 255) * 256];
                            }
                            catch (Exception e)
                            {
                                continue;
                            }

						   /*
						    * If color is not white, render it, otherwise don't to
						    * have transparency around your image. First two ff's
						    * are the images alpha, 2nd two are red, 3rd two are
						    * green, 4th two are blue. ff is 255.
						    */
                            if(color != 0xffffffff)
                            {
                                try
                                {
                                    //Sets pixel in 2D array to that color
                                    PIXELS[xx + yy * WIDTH] = color;

                                    //This is now the nearest pixel to the
                                    //player, at this coordinate so create
                                    //the new buffer here.
                                    zBuffer[(xx) + (yy) * WIDTH] = rotZ;

                                    PIXELS[(xx + 1) + (yy) * WIDTH] = color;
                                    zBuffer[(xx+1) + (yy) * WIDTH] = rotZ;
                                    PIXELS[(xx + 1) + (yy + 1) * WIDTH] = color;
                                    zBuffer[(xx+1) + (yy+1) * WIDTH] = rotZ;
                                    PIXELS[(xx) + (yy + 1) * WIDTH] = color;
                                    zBuffer[(xx) + (yy+1) * WIDTH] = rotZ;
                                }
                                catch(Exception e)
                                {

                                }

                                //Reapers are translucent because theyre ghost
                                //technically
                                if(ID == 4)
                                {
                                    xx+=2;
                                }
                            }
                        }
                    }
                }
            }
            else
            {
			   /*
			    * Render each pixel in the sprite correctly, and with the
			    * (zBuffer) as well so that sprites behind other sprites will
			    * not clip through sprites in front of them.
			    *
			    * For each sprite also texture them correctly
			    * and correct the textures for rotation and movement.
			    */
                for(int yy = yPixL; yy < yPixR; yy++)
                {
                    //How the sprite rotates up or down based on players up and
                    //down rotation
                    double pixelRotationY = -((yy - yPixelR) / (yPixelL - yPixelR));

                    //Sets the current pixel to an int, and multiplies it by
                    //256 since the image is 256 by 256
                    int yTexture = (int)(pixelRotationY * imageDimensions);

                    //Go through width of image now
                    for(int xx = xPixL; xx < xPixR; xx++)
                    {
                        //Corrects the way the pixel faces in the x direction
                        //depending on the players rotation
                        double pixelRotationX = -(xx - xPixelR) / (xPixelL - xPixelR);

                        //Sets the current pixel to an int and multiplies it by
                        //256 as that is the size of the image.
                        int xTexture = (int)(pixelRotationX * imageDimensions);

                        //Can the textures be seen
                        boolean seen = false;

					   /*
					    * In case a rare bug continues to occur.
					    */
                        if(zBuffer[(xx) + (yy) * WIDTH] < 0)
                        {
                            zBuffer[(xx) + (yy) * WIDTH] = 0;
                        }

                        try
                        {
                            //If not behind something else, and still has pixels
                            //to be rendered, the enemy can be seen
                            if(zBuffer[(xx) + (yy) * WIDTH] > rotZ)
                            {
                                seen = true;
                            }
                        }
                        catch(Exception e)
                        {
                            continue;
                        }

                        //If pixel is seen and can be rendered
                        if(seen)
                        {
                            int color = 0;

                            //Catch any weird errors and just continue
                            try
                            {
                                //Set the color of the pixel to be generated.
                                color = enemy.currentPhase.PIXELS
                                        [(xTexture & 255) + (yTexture & 255) * 256];
                            }
                            catch (Exception e)
                            {
                                continue;
                            }

						   /*
						    * If color is not white, render it, otherwise don't to
						    * have transparency around your image. First two ff's
						    * are the images alpha, 2nd two are red, 3rd two are
						    * green, 4th two are blue. ff is 255.
						    */
                            if(color != 0xffffffff)
                            {
                                try
                                {
                                    //Sets pixel in 2D array to that color
                                    PIXELS[xx + yy * WIDTH] = color;

                                    //This is now the nearest pixel to the
                                    //player, at this coordinate so create
                                    //the new buffer here.
                                    zBuffer[(xx) + (yy) * WIDTH] = rotZ;
                                }
                                catch(Exception e)
                                {

                                }

                                //Reapers are translucent because theyre ghost
                                //technically
                                if(ID == 4)
                                {
                                    xx++;
                                }
                            }
                        }
                    }
                }
            }
        }
        catch(Exception e)
        {
            return;
        }
    }

    /**
     * Renders items the same way as the enemies
     * @param x
     * @param y
     * @param z
     * @param hOffSet
     * @param ID
     */
    public void renderItems(double x, double y, double z, double hOffSet,
                            int ID, Item item)
    {
        double yCorrect = Player.y;

	   /*
	    * If the player is crouching, this corrects the item graphics so
	    * that you can still see them as if you were actually crawling
	    * and not below the map as it would be if it used the negative
	    * Player.y as the actually y.
	    */
        if(Player.y < Player.maxHeight - 2)
        {
            yCorrect = Player.y - 1.77;
        }

        double xC 		        = ((x) - Player.x) * 1.9;
        double yC 			    = y + (yCorrect / 10);
        double zC 			    = ((z) - Player.z) * 1.9;

        double rotX      =
                xC * cosine - zC * sine;
        double rotY      =
                yC;
        double rotZ      =
                zC * cosine + xC * sine;

        double xCenter   = WIDTH / 2;

        double xPixel    = ((rotX / rotZ) * HEIGHT) + xCenter;
        double yPixel    = ((rotY / rotZ) * HEIGHT) +
                (HEIGHT / Math.sin(Player.upRotate)
                        * Math.cos(Player.upRotate));

        double xPixelL   = xPixel - (item.size / rotZ);
        double xPixelR   = xPixel + (item.size / rotZ);

        double yPixelL   = yPixel - (item.size / rotZ);
        double yPixelR   = yPixel + (item.size / rotZ);

        int xPixL = (int)(xPixelL);
        int xPixR = (int)(xPixelR);
        int yPixL = (int)(yPixelL);
        int yPixR = (int)(yPixelR);

        if(xPixL < 0)
        {
            xPixL = 0;
        }

        if(xPixR > WIDTH)
        {
            xPixR = WIDTH;
        }

        if(yPixL < 0)
        {
            yPixL = 0;
        }

        if(yPixR > HEIGHT)
        {
            yPixR = HEIGHT;
        }

        rotZ *= 8;

	   /*
	    * Because the rendering loop is longer the farther you are
	    * from the item, this updates the item no matter how far you
	    * are from the item, making the phase speed the same for the
	    * item
	    */
        item.phaseTime++;

        int imageDimensions = 256;

        //Brightness of image
        int brightness = 255;

        //Does the image change brightness?
        boolean changesBrightness = false;

	   /*
	    * Figures out item image to load
	    */
        switch (ID)
        {
            //If Megahappiness. Continue to switch phases
            //For some reason enums don't work in case statements which
            //is stupid.
            case 1:
                if(item.phaseTime <= 20 * fpsCheck)
                {
                    item.itemImage = Textures.megaPhase1;
                }
                else if(item.phaseTime <= 40 * fpsCheck)
                {
                    item.itemImage = Textures.megaPhase2;
                }
                else if(item.phaseTime <= 60 * fpsCheck)
                {
                    item.itemImage = Textures.megaPhase3;
                }
                else
                {
                    item.itemImage = Textures.megaPhase4;
                }

                if(item.phaseTime > 80 * fpsCheck)
                {
                    item.phaseTime = 0;
                }

                break;

            //If happiness pack
            case 2:
                item.phaseTime = 0;

                item.itemImage = Textures.health;

                break;

            //Joy ammo
            case 3:
                item.phaseTime = 0;

                item.itemImage = Textures.shell;

                break;

            //Red KeyCard
            case 4:
                item.phaseTime = 0;

                item.itemImage = Textures.redKey;

                break;

            //Blue keycard
            case 5:
                item.phaseTime = 0;

                item.itemImage = Textures.blueKey;
                break;

            //Green keycard
            case 6:
                item.phaseTime = 0;

                item.itemImage = Textures.greenKey;
                break;

            //Yellow keycard
            case 7:
                item.phaseTime = 0;
                item.itemImage = Textures.yellowKey;
                break;

            //Joy Spreader
            case 21:
                item.phaseTime = 0;
                item.itemImage = Textures.shotgun;
                break;

            //Happiness Restock
            case 24:
                brightness = 255;

                if(item.phaseTime <= 20 * fpsCheck)
                {
                    brightness = 150;

                    item.itemImage = Textures.resurrect1;
                }
                else if(item.phaseTime <= 40 * fpsCheck)
                {
                    brightness = 200;

                    item.itemImage = Textures.resurrect2;
                }
                else if(item.phaseTime <= 60 * fpsCheck)
                {
                    item.itemImage = Textures.resurrect3;
                }

                if(item.phaseTime > 60 * fpsCheck)
                {
                    item.phaseTime = 0;
                }

                changesBrightness = true;

                break;

            //Environmental Protection Suit
            case 25:
                item.phaseTime = 0;
                item.itemImage = Textures.environSuit;
                break;

            //Goblet of Joy
            case 26:
                if(item.phaseTime <= 20 * fpsCheck)
                {
                    brightness = 255;
                    item.itemImage = Textures.goblet1;
                }
                else if(item.phaseTime <= 40 * fpsCheck)
                {
                    brightness = 150;
                    item.itemImage = Textures.goblet2;
                }
                else if(item.phaseTime <= 60 * fpsCheck)
                {
                    brightness = 255;
                    item.itemImage = Textures.goblet3;
                }

                if(item.phaseTime > 60 * fpsCheck)
                {
                    item.phaseTime = 0;
                }

                changesBrightness = true;

                break;

            //Donut
            case 27:
                item.phaseTime = 0;
                item.itemImage = Textures.adrenaline;
                break;

            //Glasses of vision
            case 28:
                brightness = 0;

                if(item.phaseTime <= 20 * fpsCheck)
                {
                    brightness = 255;
                    item.itemImage = Textures.glasses;
                }
                else if(item.phaseTime <= 40 * fpsCheck)
                {
                    brightness = 150;
                    item.itemImage = Textures.glasses;
                }

                if(item.phaseTime > 40 * fpsCheck)
                {
                    item.phaseTime = 0;
                }

                changesBrightness = true;

                break;

            //Love Torch
            case 29:
                brightness = 255;

                if(item.phaseTime <= 5 * fpsCheck)
                {
                    brightness = 200;
                    item.itemImage = Textures.torch1;
                }
                else if(item.phaseTime <= 10 * fpsCheck)
                {
                    item.itemImage = Textures.torch2;
                }
                else if(item.phaseTime <= 15 * fpsCheck)
                {
                    brightness = 200;
                    item.itemImage = Textures.torch4;
                }
                else if(item.phaseTime <= 20 * fpsCheck)
                {
                    item.itemImage = Textures.torch3;
                }

                if(item.phaseTime > 20 * fpsCheck)
                {
                    item.phaseTime = 0;
                }

                changesBrightness = true;

                break;

            //Lamp
            case 30:
                brightness = 255;

                if(item.phaseTime <= 5 * fpsCheck)
                {
                    brightness = 175;
                    item.itemImage = Textures.lamp3;
                }
                else if(item.phaseTime <= 10 * fpsCheck)
                {
                    brightness = 200;
                    item.itemImage = Textures.lamp2;
                }
                else if(item.phaseTime <= 15 * fpsCheck)
                {
                    brightness = 255;
                    item.itemImage = Textures.lamp1;
                }
                else if(item.phaseTime <= 20 * fpsCheck)
                {
                    brightness = 200;
                    item.itemImage = Textures.lamp2;
                }

                if(item.phaseTime > 20 * fpsCheck)
                {
                    item.phaseTime = 0;
                }

                changesBrightness = true;

                break;

            //Tree
            case 31:
                item.itemImage = Textures.tree;
                item.phaseTime = 0;
                break;

            //Explosive Canister
            case 32:
                item.itemImage = Textures.canister;
                item.phaseTime = 0;
                break;

            //50 Positivity
            case 33:
                item.itemImage = Textures.chainmeal;
                item.phaseTime = 0;
                break;

            //100 Positivity
            case 34:
                item.itemImage = Textures.combat;
                item.phaseTime = 0;
                break;

            //200 Positivity
            case 35:
                item.itemImage = Textures.argent;
                item.phaseTime = 0;
                break;

            //1 Positivity
            case 36:
                item.itemImage = Textures.shard;
                item.phaseTime = 0;
                break;

            //Happiness vial
            case 37:
                item.itemImage = Textures.vial;
                item.phaseTime = 0;
                break;

            //Weapon upgrade point
            case 38:
                brightness = 255;

                if(item.phaseTime <= 7 * fpsCheck)
                {
                    item.itemImage = Textures.upgrade1;
                }
                else if(item.phaseTime <= 14 * fpsCheck)
                {
                    item.itemImage = Textures.upgrade2;
                }
                else if(item.phaseTime <= 21 * fpsCheck)
                {
                    item.itemImage = Textures.upgrade3;
                }
                else if(item.phaseTime <= 28 * fpsCheck)
                {
                    item.itemImage = Textures.upgrade4;
                }

                if(item.phaseTime > 28 * fpsCheck)
                {
                    item.phaseTime = 0;
                }

                break;

            //Water of no use
            case 39:
                brightness = 255;

                if(item.phaseTime <= 7 * fpsCheck)
                {
                    brightness = 200;

                    item.itemImage = Textures.holyWater1;
                }
                else if(item.phaseTime <= 14 * fpsCheck)
                {
                    item.itemImage = Textures.holyWater2;
                }

                if(item.phaseTime > 14 * fpsCheck)
                {
                    item.phaseTime = 0;
                }

                changesBrightness = true;

                break;

            //Scepter of Love
            case 40:
                item.itemImage = Textures.scepter;
                item.phaseTime = 0;
                break;

            //Invisibility Emerald
            case 41:

                if(item.phaseTime <= 2 * fpsCheck)
                {
                    item.itemImage = Textures.invisEmerald;
                }
                else if(item.phaseTime <= 4 * fpsCheck)
                {
                    item.itemImage = Textures.invisEmerald2;
                }
                else if(item.phaseTime <= 6 * fpsCheck)
                {
                    item.itemImage = Textures.invisEmerald3;
                }
                else if(item.phaseTime <= 8 * fpsCheck)
                {
                    item.itemImage = Textures.invisEmerald4;
                }

                if(item.phaseTime > 8 * fpsCheck)
                {
                    item.phaseTime = 0;
                }

                break;

            //Table (empty)
            case 42:
                item.itemImage = Textures.table;
                item.phaseTime = 0;
                break;

            //Lamp table
            case 43:
                item.itemImage = Textures.lampTable;
                item.phaseTime = 0;
                break;

            //Joy Box
            case 47:
                item.itemImage = Textures.shellBox;
                item.phaseTime = 0;
                break;

            //Peace Cannon
            case 49:
                item.itemImage = Textures.phaseCannon;
                item.phaseTime = 0;
                break;

            //Peace Pack
            case 50:
                item.itemImage = Textures.chargePack;
                item.phaseTime = 0;
                break;

            //Large Peace Pack
            case 51:
                item.itemImage = Textures.largeChargePack;
                item.phaseTime = 0;
                break;

            //Love System
            case 52:
                brightness = 100;

                if(item.activated)
                {
                    brightness = 255;

                    if(item.phaseTime <= 7 * fpsCheck)
                    {
                        brightness = 200;

                        item.itemImage = Textures.sat1;
                    }
                    else if(item.phaseTime <= 14 * fpsCheck)
                    {
                        item.itemImage = Textures.sat2;
                    }

                    if(item.phaseTime > 14 * fpsCheck)
                    {
                        item.phaseTime = 0;
                    }
                }
                else
                {
                    item.phaseTime = 0;

                    item.itemImage = Textures.sat1;
                }

                changesBrightness = true;

                break;

            //Cupids bow
            case 55:
                item.itemImage = Textures.pistol;
                item.phaseTime = 0;
                break;

            //Love Arrows
            case 56:
                item.itemImage = Textures.clip;
                item.phaseTime = 0;
                break;

            //Quiver of love arrows
            case 57:
                item.itemImage = Textures.bullets;
                item.phaseTime = 0;
                break;

            //Teddy Bear Launcher
            case 60:
                item.phaseTime = 0;
                item.itemImage = Textures.rocketLaucher;
                break;

            //Teddy bears
            case 61:
                item.phaseTime = 0;
                item.itemImage = Textures.rockets;
                break;

            //Toy box
            case 62:
                item.phaseTime = 0;
                item.itemImage = Textures.rocketCrate;
                break;
        }

        //If brightness is not the default of 255 then the brightness
        //must be changed below
        if(brightness != 255)
        {
            changesBrightness = true;
        }

        //Corrects for low res setting more pixels every loop
        int correction = 1;

        if(lowRes)
        {
            correction = 2;
        }

        //Don't let textures be rendered off screen
        if(yPixR > HEIGHT)
        {
            yPixR = HEIGHT;
        }
        else if(yPixR < 0)
        {
            yPixR = 0;
        }

        if(xPixR > WIDTH)
        {
            xPixR = WIDTH;
        }
        else if(xPixR < 0)
        {
            xPixR = 0;
        }

	   /*
	    * Performs different operations when looping through the pixels
	    * depending on whether low resolution is turned on or not. This
	    * makes the game faster than having if statements within the
	    * double for loops.
	    */
        if(lowRes)
        {
            for(int yy = yPixL; yy < yPixR; yy+=correction)
            {
                double pixelRotationY = -(yy - yPixelR) / (yPixelL - yPixelR);
                int yTexture = (int)(pixelRotationY * imageDimensions);

                for(int xx = xPixL; xx < xPixR; xx+=correction)
                {
                    double pixelRotationX = -(xx - xPixelR) / (xPixelL - xPixelR);
                    int xTexture = (int)(pixelRotationX * imageDimensions);
                    boolean seen = false;

				   /*
				    * Try to see if the graphics can be drawn on screen or not
				    * and if there is a problem with temp being out of the
				    * array index, catch it and continue.
				    */
                    try
                    {
                        if(zBuffer[(xx) + (yy) * WIDTH] > rotZ
                                && zBuffer[(xx+1) + (yy) * WIDTH] > rotZ
                                && zBuffer[(xx+1) + (yy+1) * WIDTH] > rotZ
                                && zBuffer[(xx) + (yy+1) * WIDTH] > rotZ)
                        {
                            seen = true;
                        }
                    }
                    catch(Exception e)
                    {
                        continue;
                    }

                    //If within field of view
                    if(seen)
                    {
                        int color = 0;

                        try
                        {
                            color = item.itemImage.PIXELS
                                    [(xTexture & 255) + (yTexture & 255) * 256];
                        }
                        catch(Exception e)
                        {
                            continue;
                        }

					   /*
					    * If color is not white, render it, otherwise don't to
					    * have transparency around your image. First two ff's
					    * are the images alpha, 2nd two are red, 3rd two are
					    * green, 4th two are blue. ff is 255.
					    */
                        if(color != 0xffffffff)
                        {
                            //Adjust brightness if pixel needs to have its
                            //brightness changed from 255 to something else
                            if(changesBrightness && brightness != 255)
                            {
                                color = adjustBrightness(brightness, color, 0);
                            }

                            //Try to render
                            try
                            {
                                PIXELS[xx + yy * WIDTH] = color;
                                zBuffer[(xx) + (yy) * WIDTH] = rotZ;
                                PIXELS[(xx + 1) + (yy) * WIDTH] = color;
                                zBuffer[(xx+1) + (yy+1) * WIDTH] = rotZ;
                                PIXELS[(xx + 1) + (yy + 1) * WIDTH] = color;
                                zBuffer[(xx+1) + (yy) * WIDTH] = rotZ;
                                PIXELS[(xx) + (yy + 1) * WIDTH] = color;
                                zBuffer[(xx) + (yy+1) * WIDTH] = rotZ;
                            }
                            catch(Exception e)
                            {
                                continue;
                            }
                        }
                    }
                }
            }
        }
        else
        {
            for(int yy = yPixL; yy < yPixR; yy++)
            {
                double pixelRotationY = -(yy - yPixelR) / (yPixelL - yPixelR);
                int yTexture = (int)(pixelRotationY * imageDimensions);

                for(int xx = xPixL; xx < xPixR; xx++)
                {
                    double pixelRotationX = -(xx - xPixelR) / (xPixelL - xPixelR);
                    int xTexture = (int)(pixelRotationX * imageDimensions);
                    boolean seen = false;

				   /*
				    * Try to see if the graphics can be drawn on screen or not
				    * and if there is a problem with temp being out of the
				    * array index, catch it and continue.
				    */
                    try
                    {
                        if(zBuffer[(xx) + (yy) * WIDTH] > rotZ)
                        {
                            seen = true;
                        }
                    }
                    catch(Exception e)
                    {
                        continue;
                    }

                    //If within field of view
                    if(seen)
                    {
                        int color = 0;

                        try
                        {
                            color = item.itemImage.PIXELS
                                    [(xTexture & 255) + (yTexture & 255) * 256];
                        }
                        catch(Exception e)
                        {
                            continue;
                        }

					   /*
					    * If color is not white, render it, otherwise don't to
					    * have transparency around your image. First two ff's
					    * are the images alpha, 2nd two are red, 3rd two are
					    * green, 4th two are blue. ff is 255.
					    */
                        if(color != 0xffffffff)
                        {
                            //Adjust brightness if pixel needs to have its
                            //brightness changed from 255 to something else
                            if(changesBrightness && brightness != 255)
                            {
                                color = adjustBrightness(brightness, color, 0);
                            }

                            //Try to render
                            try
                            {
                                PIXELS[xx + yy * WIDTH] = color;
                                zBuffer[(xx) + (yy) * WIDTH] = rotZ;
                            }
                            catch(Exception e)
                            {
                                continue;
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Renders explosion like methods above. It takes too long to comment
     * this stuff so just look at the renderEnemies method to know what
     * all this stuff does.
     * @param x
     * @param y
     * @param z
     * @param hOffSet
     * @param explosion
     */
    public void renderExplosion(double x, double y, double z,
                                double hOffSet, Explosion explosion)
    {
        double yCorrect = Player.y;

	   /*
	    * If the player is crouching, this corrects the item graphics so
	    * that you can still see them as if you were actually crawling
	    * and not below the map as it would be if it used the negative
	    * Player.y as the actually y.
	    */
        if(Player.y < Player.maxHeight - 2)
        {
            yCorrect = Player.y - 1.77;
        }

        double xC 		        = ((x) - Player.x) * 1.9;
        double yC 			    = y + (yCorrect / 10);
        double zC 			    = ((z) - Player.z) * 1.9;

        int spriteSize          = 600;

        double rotX      =
                xC * cosine - zC * sine;
        double rotY      =
                yC;
        double rotZ      =
                zC * cosine + xC * sine;

        double xCenter   = WIDTH / 2;

        double xPixel    = ((rotX / rotZ) * HEIGHT) + xCenter;
        double yPixel    = ((rotY / rotZ) * HEIGHT) +
                (HEIGHT / Math.sin(Player.upRotate)
                        * Math.cos(Player.upRotate));

        double xPixelL   = xPixel - (spriteSize / rotZ);
        double xPixelR   = xPixel + (spriteSize / rotZ);

        double yPixelL   = yPixel - (spriteSize / rotZ);
        double yPixelR   = yPixel + (spriteSize / rotZ);

        int xPixL = (int)(xPixelL);
        int xPixR = (int)(xPixelR);
        int yPixL = (int)(yPixelL);
        int yPixR = (int)(yPixelR);

        if(xPixL < 0)
        {
            xPixL = 0;
        }

        if(xPixR > WIDTH)
        {
            xPixR = WIDTH;
        }

        if(yPixL < 0)
        {
            yPixL = 0;
        }

        if(yPixR > HEIGHT)
        {
            yPixR = HEIGHT;
        }

        rotZ *= 8;

        int imageDimensions = 256;

        int correction = 1;

        if(lowRes)
        {
            correction = 2;
        }

        //The image of the explosion
        Render image = null;

        double phaseTime = explosion.phaseTime;

        if(explosion.ID == 1)
        {
            if(phaseTime <= 4 * fpsCheck)
            {
                image = Textures.canExplode1;
            }
            else if(phaseTime <= 8 * fpsCheck)
            {
                image = Textures.canExplode2;
            }
            else if(phaseTime <= 12 * fpsCheck)
            {
                image = Textures.canExplode3;
            }
            else if(phaseTime > 12 * fpsCheck)
            {
                image = Textures.canExplode4;
            }
        }
        else
        {
            if(phaseTime <= 2 * fpsCheck)
            {
                image = Textures.explosion1;
            }
            else if(phaseTime <= 4 * fpsCheck)
            {
                image = Textures.explosion2;
            }
            else if(phaseTime <= 6 * fpsCheck)
            {
                image = Textures.explosion3;
            }
            else if(phaseTime <= 8 * fpsCheck)
            {
                image = Textures.explosion4;
            }
            else if(phaseTime <= 10 * fpsCheck)
            {
                image = Textures.explosion5;
            }
            else if(phaseTime <= 12 * fpsCheck)
            {
                image = Textures.explosion6;
            }
            else if(phaseTime <= 14 * fpsCheck)
            {
                image = Textures.explosion7;
            }
            else if(phaseTime > 14 * fpsCheck)
            {
                image = Textures.explosion8;
            }
        }

        if(yPixR > HEIGHT)
        {
            yPixR = HEIGHT;
        }
        else if(yPixR < 0)
        {
            yPixR = 0;
        }

        if(xPixR > WIDTH)
        {
            xPixR = WIDTH;
        }
        else if(xPixR < 0)
        {
            xPixR = 0;
        }

	   /*
	    * Performs different operations when looping through the pixels
	    * depending on whether low resolution is turned on or not. This
	    * makes the game faster than having if statements within the
	    * double for loops.
	    */
        if(lowRes)
        {
            for(int yy = yPixL; yy < yPixR; yy+=correction)
            {
                double pixelRotationY = -(yy - yPixelR) / (yPixelL - yPixelR);
                int yTexture = (int)(pixelRotationY * imageDimensions);

                for(int xx = xPixL; xx < xPixR; xx+=correction)
                {
                    double pixelRotationX = -(xx - xPixelR) / (xPixelL - xPixelR);
                    int xTexture = (int)(pixelRotationX * imageDimensions);
                    boolean seen = false;

				   /*
				    * Try to see if the graphics can be drawn on screen or not
				    * and if there is a problem with temp being out of the
				    * array index, catch it and continue.
				    */
                    try
                    {
                        if(zBuffer[(xx) + (yy) * WIDTH] > rotZ
                                && zBuffer[(xx+1) + (yy) * WIDTH] > rotZ
                                && zBuffer[(xx+1) + (yy+1) * WIDTH] > rotZ
                                && zBuffer[(xx) + (yy+1) * WIDTH] > rotZ)
                        {
                            seen = true;
                        }
                    }
                    catch(Exception e)
                    {
                        continue;
                    }

                    //If within field of view
                    if(seen)
                    {
                        int color = 0;

                        try
                        {
                            color = image.PIXELS
                                    [(xTexture & 255) + (yTexture & 255) * 256];
                        }
                        catch(Exception e)
                        {
                            continue;
                        }

					   /*
					    * If color is not white, render it, otherwise don't to
					    * have transparency around your image. First two ff's
					    * are the images alpha, 2nd two are red, 3rd two are
					    * green, 4th two are blue. ff is 255.
					    */
                        if(color != 0xffffffff)
                        {
                            //Try to render
                            try
                            {
                                PIXELS[xx + yy * WIDTH] = color;
                                zBuffer[(xx) + (yy) * WIDTH] = rotZ;
                                PIXELS[(xx + 1) + (yy) * WIDTH] = color;
                                zBuffer[(xx+1) + (yy+1) * WIDTH] = rotZ;
                                PIXELS[(xx + 1) + (yy + 1) * WIDTH] = color;
                                zBuffer[(xx+1) + (yy) * WIDTH] = rotZ;
                                PIXELS[(xx) + (yy + 1) * WIDTH] = color;
                                zBuffer[(xx) + (yy+1) * WIDTH] = rotZ;
                            }
                            catch(Exception e)
                            {
                                continue;
                            }
                        }
                    }
                }
            }
        }
        else
        {
            for(int yy = yPixL; yy < yPixR; yy++)
            {
                double pixelRotationY = -(yy - yPixelR) / (yPixelL - yPixelR);
                int yTexture = (int)(pixelRotationY * imageDimensions);

                for(int xx = xPixL; xx < xPixR; xx++)
                {
                    double pixelRotationX = -(xx - xPixelR) / (xPixelL - xPixelR);
                    int xTexture = (int)(pixelRotationX * imageDimensions);
                    boolean seen = false;

				   /*
				    * Try to see if the graphics can be drawn on screen or not
				    * and if there is a problem with temp being out of the
				    * array index, catch it and continue.
				    */
                    try
                    {
                        if(zBuffer[(xx) + (yy) * WIDTH] > rotZ)
                        {
                            seen = true;
                        }
                    }
                    catch(Exception e)
                    {
                        continue;
                    }

                    //If within field of view
                    if(seen)
                    {
                        int color = 0;

                        try
                        {
                            color = image.PIXELS
                                    [(xTexture & 255) + (yTexture & 255) * 256];
                        }
                        catch(Exception e)
                        {
                            continue;
                        }

					   /*
					    * If color is not white, render it, otherwise don't to
					    * have transparency around your image. First two ff's
					    * are the images alpha, 2nd two are red, 3rd two are
					    * green, 4th two are blue. ff is 255.
					    */
                        if(color != 0xffffffff)
                        {
                            //Try to render
                            try
                            {
                                PIXELS[xx + yy * WIDTH] = color;
                                zBuffer[(xx) + (yy) * WIDTH] = rotZ;
                            }
                            catch(Exception e)
                            {
                                continue;
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Render all the corpses and their different phases as they fall
     * to the ground after being killed. Most of this rendering stuff
     * is the same as renderEnemies method so just look at the comments
     * there for reference
     * @param x
     * @param y
     * @param z
     * @param hOffSet
     * @param corpse
     */
    public void renderCorpse(double x, double y, double z,
                             double hOffSet, Corpse corpse)
    {
        double yCorrect = Player.y;

        //Default corpse size
        int spriteSize          = 600;

        //If it is the corpse of a boss
        if(corpse.enemyID == 6 || corpse.enemyID == 8)
        {
            spriteSize = 4096;
            y -= 3;
        }

	   /*
	    * If the player is crouching, this corrects the item graphics so
	    * that you can still see them as if you were actually crawling
	    * and not below the map as it would be if it used the negative
	    * Player.y as the actually y.
	    */
        if(Player.y < Player.maxHeight - 2)
        {
            yCorrect = Player.y - 1.77;
        }

        double xC 		        = ((x) - Player.x) * 1.9;
        double yC 			    = y + (yCorrect / 10);
        double zC 			    = ((z) - Player.z) * 1.9;

        double rotX      =
                xC * cosine - zC * sine;
        double rotY      =
                yC;
        double rotZ      =
                zC * cosine + xC * sine;

        double xCenter   = WIDTH / 2;

        double xPixel    = ((rotX / rotZ) * HEIGHT) + xCenter;
        double yPixel    = ((rotY / rotZ) * HEIGHT) +
                (HEIGHT / Math.sin(Player.upRotate)
                        * Math.cos(Player.upRotate));

        double xPixelL   = xPixel - (spriteSize / rotZ);
        double xPixelR   = xPixel + (spriteSize / rotZ);

        double yPixelL   = yPixel - (spriteSize / rotZ);
        double yPixelR   = yPixel + (spriteSize / rotZ);

        int xPixL = (int)(xPixelL);
        int xPixR = (int)(xPixelR);
        int yPixL = (int)(yPixelL);
        int yPixR = (int)(yPixelR);

        if(xPixL < 0)
        {
            xPixL = 0;
        }

        if(xPixR > WIDTH)
        {
            xPixR = WIDTH;
        }

        if(yPixL < 0)
        {
            yPixL = 0;
        }

        if(yPixR > HEIGHT)
        {
            yPixR = HEIGHT;
        }

        rotZ *= 8;

        if(corpse.phaseTime > 0)
        {
            corpse.phaseTime--;
        }

        int imageDimensions = 256;

        int correction = 1;

        //Low res setting
        if(lowRes)
        {
            correction = 2;
        }

        //Graphics of the happyFace to be rendered
        Render corpseGraphics = null;

        //Depending on corpse, have a different sprite
        //depending on the enemy killed.
        if(corpse.phaseTime == 0)
        {
            if(corpse.enemyID == 7)
            {
                corpseGraphics= Textures.corpseType2;
            }
            else if(corpse.enemyID == 1)
            {
                corpseGraphics= Textures.enemy1corpse;
            }
            else if(corpse.enemyID == 2)
            {
                corpseGraphics= Textures.enemy2corpse;
            }
            else if(corpse.enemyID == 3)
            {
                corpseGraphics= Textures.enemy3corpse;
            }
            else if(corpse.enemyID == 4)
            {
                corpseGraphics= Textures.enemy4corpse;
            }
            else if(corpse.enemyID == 5)
            {
                corpseGraphics= Textures.enemy5corpse;
            }
            else if(corpse.enemyID == 8)
            {
                corpseGraphics= Textures.belegothCorpse;
            }
            else
            {
                corpseGraphics = corpse.corpseImage;
            }
        }
        //As corpse falls, make its animation look realistic
        else
        {
            if(corpse.enemyID == 1)
            {
                if(corpse.phaseTime >= 12)
                {
                    corpseGraphics= Textures.enemy1corpse1;
                }
                else if(corpse.phaseTime >= 6)
                {
                    corpseGraphics= Textures.enemy1corpse2;
                }
                else if(corpse.phaseTime >= 0)
                {
                    corpseGraphics= Textures.enemy1corpse3;
                }
            }
            else if(corpse.enemyID == 2)
            {
                if(corpse.phaseTime >= 21)
                {
                    corpseGraphics= Textures.enemy2corpse1;
                }
                else if(corpse.phaseTime >= 18)
                {
                    corpseGraphics= Textures.enemy2corpse2;
                }
                else if(corpse.phaseTime >= 15)
                {
                    corpseGraphics= Textures.enemy2corpse3;
                }
                else if(corpse.phaseTime >= 12)
                {
                    corpseGraphics= Textures.enemy2corpse4;
                }
                else if(corpse.phaseTime >= 9)
                {
                    corpseGraphics= Textures.enemy2corpse5;
                }
                else if(corpse.phaseTime >= 6)
                {
                    corpseGraphics= Textures.enemy2corpse6;
                }
                else if(corpse.phaseTime >= 3)
                {
                    corpseGraphics= Textures.enemy2corpse;
                }
                else if(corpse.phaseTime >= 0)
                {
                    corpseGraphics= Textures.enemy2corpse;
                }
            }
            else if(corpse.enemyID == 3)
            {
                if(corpse.phaseTime >= 21)
                {
                    corpseGraphics= Textures.enemy3corpse1;
                }
                else if(corpse.phaseTime >= 18)
                {
                    corpseGraphics= Textures.enemy3corpse2;
                }
                else if(corpse.phaseTime >= 15)
                {
                    corpseGraphics= Textures.enemy3corpse3;
                }
                else if(corpse.phaseTime >= 12)
                {
                    corpseGraphics= Textures.enemy3corpse4;
                }
                else if(corpse.phaseTime >= 9)
                {
                    corpseGraphics= Textures.enemy3corpse5;
                }
                else if(corpse.phaseTime >= 6)
                {
                    corpseGraphics= Textures.enemy3corpse6;
                }
                else if(corpse.phaseTime >= 3)
                {
                    corpseGraphics= Textures.enemy3corpse7;
                }
                else if(corpse.phaseTime >= 2)
                {
                    corpseGraphics= Textures.enemy3corpse8;
                }
                else if(corpse.phaseTime >= 0)
                {
                    corpseGraphics= Textures.enemy3corpse9;
                }
            }
            else if(corpse.enemyID == 4)
            {
                if(corpse.phaseTime >= 12)
                {
                    corpseGraphics= Textures.enemy4corpse1;
                }
                else if(corpse.phaseTime >= 6)
                {
                    corpseGraphics= Textures.enemy4corpse2;
                }
                else if(corpse.phaseTime >= 0)
                {
                    corpseGraphics= Textures.enemy4corpse3;
                }
            }
            else if(corpse.enemyID == 5)
            {
                if(corpse.phaseTime >= 22)
                {
                    corpseGraphics= Textures.enemy5corpse1;
                }
                else if(corpse.phaseTime >= 20)
                {
                    corpseGraphics= Textures.enemy5corpse2;
                }
                else if(corpse.phaseTime >= 18)
                {
                    corpseGraphics= Textures.enemy5corpse3;
                }
                else if(corpse.phaseTime >= 18)
                {
                    corpseGraphics= Textures.enemy5corpse4;
                }
                else if(corpse.phaseTime >= 16)
                {
                    corpseGraphics= Textures.enemy5corpse5;
                }
                else if(corpse.phaseTime >= 14)
                {
                    corpseGraphics= Textures.enemy5corpse6;
                }
                else if(corpse.phaseTime >= 12)
                {
                    corpseGraphics= Textures.enemy5corpse7;
                }
                else if(corpse.phaseTime >= 10)
                {
                    corpseGraphics= Textures.enemy5corpse8;
                }
                else if(corpse.phaseTime >= 8)
                {
                    corpseGraphics= Textures.enemy5corpse9;
                }
                else if(corpse.phaseTime >= 7)
                {
                    corpseGraphics= Textures.enemy5corpse10;
                }
                else if(corpse.phaseTime >= 6)
                {
                    corpseGraphics= Textures.enemy5corpse11;
                }
                else if(corpse.phaseTime >= 5)
                {
                    corpseGraphics= Textures.enemy5corpse12;
                }
                else if(corpse.phaseTime >= 4)
                {
                    corpseGraphics= Textures.enemy5corpse13;
                }
                else if(corpse.phaseTime >= 3)
                {
                    corpseGraphics= Textures.enemy5corpse14;
                }
                else if(corpse.phaseTime >= 2)
                {
                    corpseGraphics= Textures.enemy5corpse15;
                }
                else if(corpse.phaseTime >= 1)
                {
                    corpseGraphics= Textures.enemy5corpse16;
                }
                else if(corpse.phaseTime >= 0)
                {
                    corpseGraphics= Textures.enemy5corpse17;
                }
            }
            else if(corpse.enemyID == 8)
            {
                if(corpse.phaseTime >= 41)
                {
                    corpseGraphics= Textures.belegothCorpse1;
                }
                else if(corpse.phaseTime >= 37)
                {
                    corpseGraphics= Textures.belegothCorpse2;
                }
                else if(corpse.phaseTime >= 33)
                {
                    corpseGraphics= Textures.belegothCorpse3;
                }
                else if(corpse.phaseTime >= 29)
                {
                    corpseGraphics= Textures.belegothCorpse4;
                }
                else if(corpse.phaseTime >= 25)
                {
                    corpseGraphics= Textures.belegothCorpse5;
                }
                else if(corpse.phaseTime >= 21)
                {
                    corpseGraphics= Textures.belegothCorpse6;
                }
                else if(corpse.phaseTime >= 18)
                {
                    corpseGraphics= Textures.belegothCorpse7;
                }
                else if(corpse.phaseTime >= 15)
                {
                    corpseGraphics= Textures.belegothCorpse8;
                }
                else if(corpse.phaseTime >= 12)
                {
                    corpseGraphics= Textures.belegothCorpse9;
                }
                else if(corpse.phaseTime >= 9)
                {
                    corpseGraphics= Textures.belegothCorpse10;
                }
                else if(corpse.phaseTime >= 6)
                {
                    corpseGraphics= Textures.belegothCorpse11;
                }
                else if(corpse.phaseTime >= 3)
                {
                    corpseGraphics= Textures.belegothCorpse12;
                }
                else if(corpse.phaseTime >= 0)
                {
                    corpseGraphics= Textures.belegothCorpse13;
                }
            }
            else
            {
                if(corpse.phaseTime >= 21)
                {
                    corpseGraphics= Textures.corpse1;
                }
                else if(corpse.phaseTime >= 18)
                {
                    corpseGraphics= Textures.corpse2;
                }
                else if(corpse.phaseTime >= 15)
                {
                    corpseGraphics= Textures.corpse3;
                }
                else if(corpse.phaseTime >= 12)
                {
                    corpseGraphics= Textures.corpse4;
                }
                else if(corpse.phaseTime >= 9)
                {
                    corpseGraphics= Textures.corpse5;
                }
                else if(corpse.phaseTime >= 6)
                {
                    corpseGraphics= Textures.corpse6;
                }
                else if(corpse.phaseTime >= 3)
                {
                    corpseGraphics= Textures.corpse7;
                }
                else if(corpse.phaseTime >= 0)
                {
                    corpseGraphics= Textures.corpse8;
                }
            }
        }

        if(yPixR > HEIGHT)
        {
            yPixR = HEIGHT;
        }
        else if(yPixR < 0)
        {
            yPixR = 0;
        }

        if(xPixR > WIDTH)
        {
            xPixR = WIDTH;
        }
        else if(xPixR < 0)
        {
            xPixR = 0;
        }

	   /*
	    * Performs different operations when looping through the pixels
	    * depending on whether low resolution is turned on or not. This
	    * makes the game faster than having if statements within the
	    * double for loops.
	    */
        if(lowRes)
        {
            for(int yy = yPixL; yy < yPixR; yy+=correction)
            {
                double pixelRotationY = -(yy - yPixelR) / (yPixelL - yPixelR);
                int yTexture = (int)(pixelRotationY * imageDimensions);

                for(int xx = xPixL; xx < xPixR; xx+=correction)
                {
                    double pixelRotationX = -(xx - xPixelR) / (xPixelL - xPixelR);
                    int xTexture = (int)(pixelRotationX * imageDimensions);
                    boolean seen = false;

				   /*
				    * Try to see if the graphics can be drawn on screen or not
				    * and if there is a problem with temp being out of the
				    * array index, catch it and continue.
				    */
                    try
                    {
                        if(zBuffer[(xx) + (yy) * WIDTH] > rotZ
                                && zBuffer[(xx+1) + (yy) * WIDTH] > rotZ
                                && zBuffer[(xx+1) + (yy+1) * WIDTH] > rotZ
                                && zBuffer[(xx) + (yy+1) * WIDTH] > rotZ)
                        {
                            seen = true;
                        }
                    }
                    catch(Exception e)
                    {
                        continue;
                    }

                    //If within field of view
                    if(seen)
                    {
                        int color = 0;

                        try
                        {
                            color = corpseGraphics.PIXELS
                                    [(xTexture & 255) + (yTexture & 255) * 256];
                        }
                        catch(Exception e)
                        {
                            continue;
                        }

					   /*
					    * If color is not white, render it, otherwise don't to
					    * have transparency around your image. First two ff's
					    * are the images alpha, 2nd two are red, 3rd two are
					    * green, 4th two are blue. ff is 255.
					    */
                        if(color != 0xffffffff)
                        {
                            //Try to render
                            try
                            {
                                PIXELS[xx + yy * WIDTH] = color;
                                zBuffer[(xx) + (yy) * WIDTH] = rotZ;
                                PIXELS[(xx + 1) + (yy) * WIDTH] = color;
                                zBuffer[(xx+1) + (yy+1) * WIDTH] = rotZ;
                                PIXELS[(xx + 1) + (yy + 1) * WIDTH] = color;
                                zBuffer[(xx+1) + (yy) * WIDTH] = rotZ;
                                PIXELS[(xx) + (yy + 1) * WIDTH] = color;
                                zBuffer[(xx) + (yy+1) * WIDTH] = rotZ;
                            }
                            catch(Exception e)
                            {
                                continue;
                            }
                        }
                    }
                }
            }
        }
        else
        {
            for(int yy = yPixL; yy < yPixR; yy++)
            {
                double pixelRotationY = -(yy - yPixelR) / (yPixelL - yPixelR);
                int yTexture = (int)(pixelRotationY * imageDimensions);

                for(int xx = xPixL; xx < xPixR; xx++)
                {
                    double pixelRotationX = -(xx - xPixelR) / (xPixelL - xPixelR);
                    int xTexture = (int)(pixelRotationX * imageDimensions);
                    boolean seen = false;

				   /*
				    * Try to see if the graphics can be drawn on screen or not
				    * and if there is a problem with temp being out of the
				    * array index, catch it and continue.
				    */
                    try
                    {
                        if(zBuffer[(xx) + (yy) * WIDTH] > rotZ)
                        {
                            seen = true;
                        }
                    }
                    catch(Exception e)
                    {
                        continue;
                    }

                    //If within field of view
                    if(seen)
                    {
                        int color = 0;

                        try
                        {
                            color = corpseGraphics.PIXELS
                                    [(xTexture & 255) + (yTexture & 255) * 256];
                        }
                        catch(Exception e)
                        {
                            continue;
                        }

					   /*
					    * If color is not white, render it, otherwise don't to
					    * have transparency around your image. First two ff's
					    * are the images alpha, 2nd two are red, 3rd two are
					    * green, 4th two are blue. ff is 255.
					    */
                        if(color != 0xffffffff)
                        {
                            //Try to render
                            try
                            {
                                PIXELS[xx + yy * WIDTH] = color;
                                zBuffer[(xx) + (yy) * WIDTH] = rotZ;
                            }
                            catch(Exception e)
                            {
                                continue;
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Renders bullets the same way as the enemies so look up there for
     * comments on how this all works.
     * @param x
     * @param y
     * @param z
     * @param hOffSet
     */
    public void renderProjectiles(double x, double y, double z, double hOffSet
            ,int ID)
    {
        double xC 		        = (x - Player.x) * 1.9;
        double yC 			    = y + (Player.y * 0.085);
        double zC 			    = (z - Player.z) * 1.9;

        int spriteSize          = 16;

        if(ID >= 3)
        {
            spriteSize = 128;
        }

        double rotX      =
                xC * cosine - zC * sine;
        double rotY      =
                yC;
        double rotZ      =
                zC * cosine + xC * sine;

        double xCenter   = WIDTH / 2;

        double xPixel    = ((rotX / rotZ) * HEIGHT) + xCenter;
        double yPixel    = ((rotY / rotZ) * HEIGHT) +
                (HEIGHT / Math.sin(Player.upRotate)
                        * Math.cos(Player.upRotate));

        double xPixelL   = xPixel - (spriteSize / rotZ);
        double xPixelR   = xPixel + (spriteSize / rotZ);

        double yPixelL   = yPixel - (spriteSize / rotZ);
        double yPixelR   = yPixel + (spriteSize / rotZ);

        int xPixL = (int)(xPixelL);
        int xPixR = (int)(xPixelR);
        int yPixL = (int)(yPixelL);
        int yPixR = (int)(yPixelR);

        if(xPixL < 0)
        {
            xPixL = 0;
        }

        if(xPixR > WIDTH)
        {
            xPixR = WIDTH;
        }

        if(yPixL < 0)
        {
            yPixL = 0;
        }

        if(yPixR > HEIGHT)
        {
            yPixR = HEIGHT;
        }

        rotZ *= 8;

        int correction = 1;

        //If low res setting
        if(lowRes)
        {
            correction = 2;
        }

        //The image being rendered in the form of a render object
        Render type;

        //Different image for different projectile types
        if(ID == 0)
        {
            type = Textures.bullet;
        }
        else if(ID == 1)
        {
            type = Textures.bullet;
        }
        else if(ID == 2)
        {
            type = Textures.phaser;
        }
        else if(ID == 3)
        {
            type = Textures.rocket;
        }
        else if(ID == 4)
        {
            type = Textures.defaultFireball;
        }
        else if(ID == 5)
        {
            type = Textures.electroBall;
        }
        else if(ID == 6)
        {
            type = Textures.giantFireball;
        }
        else
        {
            type = Textures.electricShock;
        }

        if(yPixR > HEIGHT)
        {
            yPixR = HEIGHT;
        }
        else if(yPixR < 0)
        {
            yPixR = 0;
        }

        if(xPixR > WIDTH)
        {
            xPixR = WIDTH;
        }
        else if(xPixR < 0)
        {
            xPixR = 0;
        }

        for(int yy = yPixL; yy < yPixR; yy+=correction)
        {
            double pixelRotationY = (yy - yPixelR) / (yPixelL - yPixelR);
            int yTexture = (int)(pixelRotationY * 256);

            for(int xx = xPixL; xx < xPixR; xx+=correction)
            {
                double pixelRotationX = (xx - xPixelR) / (xPixelL - xPixelR);
                int xTexture = (int)(pixelRotationX * 256);
                boolean seen = false;

			   /*
			    * Try to see if the graphics can be drawn on screen or not
			    * and if there is a problem with temp being out of the
			    * array index, catch it and continue.
			    */
                try
                {
                    if(zBuffer[(xx) + (yy) * WIDTH] > rotZ)
                    {
                        seen = true;
                    }

                    if(lowRes)
                    {
                        if(seen && zBuffer[(xx+1) + (yy) * WIDTH] > rotZ
                                && zBuffer[(xx+1) + (yy+1) * WIDTH] > rotZ
                                && zBuffer[(xx) + (yy+1) * WIDTH] > rotZ)
                        {
                            seen = true;
                        }
                        else
                        {
                            seen = false;
                        }
                    }
                }
                catch(Exception e)
                {
                    continue;
                }

                if(seen)
                {
                    int color = 0;

                    try
                    {
                        color = type.PIXELS
                                [(xTexture & 255) + (yTexture & 255) * 256];
                    }
                    catch(Exception e)
                    {
                        continue;
                    }


				   /*
				    * If color is not white, render it, otherwise don't to
				    * have transparency around your image. First two ff's
				    * are the images alpha, 2nd two are red, 3rd two are
				    * green, 4th two are blue. ff is 255.
				    */
                    if(color != 0xffffffff)
                    {
                        //Try to render
                        try
                        {
                            PIXELS[xx + yy * WIDTH] = color;
                            zBuffer[(xx) + (yy) * WIDTH] = rotZ;

                            //If low res setting on
                            if(lowRes)
                            {
                                PIXELS[(xx + 1) + (yy) * WIDTH] = color;
                                zBuffer[(xx+1) + (yy) * WIDTH] = rotZ;
                                PIXELS[(xx + 1) + (yy + 1) * WIDTH] = color;
                                zBuffer[(xx+1) + (yy+1) * WIDTH] = rotZ;
                                PIXELS[(xx) + (yy + 1) * WIDTH] = color;
                                zBuffer[(xx) + (yy+1) * WIDTH] = rotZ;
                            }
                        }
                        catch(Exception e)
                        {
                            continue;
                        }
                    }
                }
            }
        }
    }




    /**
     * Renders the walls depending on where the blocks in the map are, and
     * the players movements
     * @param xLeft
     * @param xRight
     * @param yHeight
     * @param zDepthLeft
     * @param zDepthRight
     * @param wallHeight
     */
    public void render3DWalls(double xLeft, double xRight, double yHeight,
                              double zDepthLeft, double zDepthRight, double wallHeight
            , int wallID, Block block)
    {
        //It was too much work to indent everything, so please forgive the
        //non indentation of everything in this try block.
        try
        {
		   /*
		    * Corrects it so that the left side of the wall does not look like
		    * it is moving as the player moves. It is supposed to look as if
		    * it is staying put in the same place. You should test this. Make
		    * those 2's ones and you'll see just how much a little change will
		    * screw everything up.
		    */
            double xCLeft 		     =
                    ((xLeft)- (rightSpeed * rightCorrect)) * 2;
            double zCLeft 			 =
                    ((zDepthLeft) - (fowardSpeed * forwardCorrect)) * 2;

		   /*
			* Rotate the Left side x and z (up to a full circle if needed)
			* using both cosine and sine in opposite ways for both
			* equations to get the effect of a circle. This allows for the
			* wall to rotate with the player yet look like its staying in the
			* same place. You can test this effect if you want. If you set
			* xCLeft to 0 here or anything really, the wall well allow
			* the right side to rotate correctly but the left side of every
			* wall will do weird crap like stretch out to infinity and
			* stuff. This basically just makes it so that it will turn
			* towards the player the more the player turns away from that
			* side to make it seem like it is staying in the same place.
			*/
            double rotLeftSideX      =  (xCLeft * cosine - zCLeft * sine);
            double rotLeftSideZ      =  (zCLeft * cosine + xCLeft * sine);

		   /*
		    * Moves corners of wall (or box I guess you could say) in
		    * correlation to your y direction (If you jump or crouch).
		    * So that they seemingly stay in the same positions as well.
		    */
            double yCornerTopLeft    =
                    ((-yHeight - ((0.5 / 12) * wallHeight))
                            + (Player.y * 0.06)) * 100;
            double yCornerBottomLeft =
                    ((0.5 - yHeight) + (Player.y * 0.06)) * 100;

		   /*
		    * Corrects it so that the right side of the wall does not look like
		    * it is moving as the player moves. It is supposed to look as if
		    * it is staying put in the same place. You should test this. Make
		    * those 2's ones and you'll see just how much a little change will
		    * screw everything up.
		    */
            double xCRight 		     =
                    ((xRight)- (rightSpeed * rightCorrect)) * 2;
            double zCRight 			 =
                    ((zDepthRight) - (fowardSpeed * forwardCorrect)) * 2;

		   /*
			* Rotate the Right side x and z (up to a full circle if needed)
			* using both cosine and sine in opposite ways for both
			* equations to get the effect of a circle. This allows for the
			* wall to rotate with the player yet look like its staying in the
			* same place. You can test this effect if you want. If you set
			* xCRight to xCLeft here or anything really, the wall well allow
			* the left side to rotate correctly but the right side of every
			* wall will do weird crap like strech out to infinity and
			* stuff. This basically just makes it so that it will turn
			* towards the player the more the player turns away from that
			* side to make it seem like it is staying in the same place.
			*/
            double rotRightSideX      =
                    (xCRight * cosine - zCRight * sine);
            double rotRightSideZ      =
                    (zCRight * cosine + xCRight * sine);

		   /*
			* Moves corners of wall (or box I guess you could say) in
			* correlation to your y direction (If you jump or crouch).
			* So that they seemingly stay in the same positions as well.
			*/
            double yCornerTopRight    =
                    ((-yHeight - ((0.5 / 12) * wallHeight)) + (Player.y * 0.06)) * 100;
            double yCornerBottomRight =
                    ((0.5 - yHeight) + (Player.y * 0.06)) * 100;

            //All i know is it helps with making the pixels seem like they
            //rotate when the wall rotates. They rotate along with the wall
            //basically.
            double text4Modifier = 256;

            //The radius the wall will clip out of
            double clip = 13;

		   /*
		    * Uses cohen sutherland theroem to clip off any textures outside
		    * of the box created by the walls. If this wasn't used the
		    * wall textures may stretch out to infinity.
		    */
            if(rotLeftSideZ <= clip)
            {
                double temp = (clip - rotLeftSideZ)
                        / (rotRightSideZ - rotLeftSideZ);
                rotLeftSideZ = rotLeftSideZ +
                        (rotRightSideZ - rotLeftSideZ) * temp;
                rotLeftSideX = rotLeftSideX +
                        (rotRightSideX - rotLeftSideX) * temp;
            }

            //Used for both sides
            if(rotRightSideZ <= clip)
            {
                double temp = (clip - rotLeftSideZ)
                        / (rotRightSideZ - rotLeftSideZ);
                rotRightSideZ = rotLeftSideZ +
                        (rotRightSideZ - rotLeftSideZ) * temp;
                rotRightSideX = rotLeftSideX +
                        (rotRightSideX - rotLeftSideX) * temp;
            }

			/*
			 * Calculates the vectors from the center (The players position
			 * in respect to the wall) and places them at each end of the wall
			 * to define the range in which the textures will be rendered. The
			 * width of the wall per say.
			 */
            double xPixelLeft  		  =
                    ((rotLeftSideX / rotLeftSideZ) * HEIGHT + (WIDTH / 2));
            double xPixelRight		  =
                    ((rotRightSideX / rotRightSideZ) * HEIGHT + (WIDTH / 2));

            //Make those into ints for the for loops
            int xPixelLeftInt         = (int) (xPixelLeft);
            int xPixelRightInt        = (int) (xPixelRight);

		   /*
		    * Don't render things off the screen
		    */
            if(xPixelLeftInt < 0)
            {
                xPixelLeftInt = 0;
            }

		   /*
		    * Dont render things off the screen
		    */
            if(xPixelRightInt > WIDTH)
            {
                xPixelRightInt = WIDTH;
            }

		   /*
		    * Sets up vectors to all 4 corners of the wall to define the edges
		    * of the wall to be rendered no matter where the player is looking
		    * up or down. For instance if yPixelTop left was lower, the wall
		    * would have the left side be lower and tilt upward towards the
		    * right side which wouldn't look right, so this makes the textures
		    * stay as a square and stretch as far as looks realistic in the
		    * y direction
		    */
            double yPixelTopLeft 	  =
                    (yCornerTopLeft
                            / rotLeftSideZ * HEIGHT + (HEIGHT
                            / Math.sin(Player.upRotate)
                            * Math.cos(Player.upRotate)));
            double yPixelBottomLeft   =
                    (yCornerBottomLeft
                            / rotLeftSideZ * HEIGHT + (HEIGHT
                            / Math.sin(Player.upRotate)
                            * Math.cos(Player.upRotate)));
            double yPixelTopRight 	  =
                    (yCornerTopRight
                            / rotRightSideZ * HEIGHT + (HEIGHT
                            / Math.sin(Player.upRotate)
                            * Math.cos(Player.upRotate)));
            double yPixelBottomRight  =
                    (yCornerBottomRight
                            / rotRightSideZ * HEIGHT + (HEIGHT
                            / Math.sin(Player.upRotate)
                            * Math.cos(Player.upRotate)));

            //Allows the textures to rotate with the wall.
            double texture1 = 1 / rotLeftSideZ;
            double texture2 = 1 / rotRightSideZ;
            double texture4 = text4Modifier / rotRightSideZ;

            //Walls phase is always updated.
            block.wallPhase++;

            //Brightness of each wall
            int brightness = 255;

            //Does wall stay at full brightness or does it change
            boolean changesBrightness = false;

            //Figures out what wall texture to render beforehand
            switch (wallID)
            {
                //Dungeon Bricks
                case 1:
                    block.wallImage = Textures.wall1;

                    block.wallPhase = 0;
                    break;

                //Futuristic Wall
                case 2:
                    block.wallImage = Textures.wall2;

                    block.wallPhase = 0;
                    break;

                //Dark bricks
                case 3:
                    block.wallImage = Textures.wall3;

                    block.wallPhase = 0;
                    break;

                //Glass
                case 4:
                    block.wallImage = Textures.wall4;

                    //If glass is damaged, it has different textures
                    if(block.health <= 10)
                    {
                        block.wallImage = Textures.wall4damaged3;
                    }
                    else if(block.health <= 30)
                    {
                        block.wallImage = Textures.wall4damaged2;
                    }
                    else if(block.health <= 50)
                    {
                        block.wallImage = Textures.wall4damaged1;
                    }

                    block.wallPhase = 0;
                    break;

                //Computer Wall
                case 5:
                    block.wallImage = Textures.wall5;

                    block.wallPhase = 0;
                    break;

                //End Button
                case 6:
                    block.wallImage = Textures.wall6;

                    block.wallPhase = 0;
                    break;

                //Normal door
                case 7:
                    block.wallImage = Textures.wall7;

                    block.wallPhase = 0;
                    break;

                //Elevator
                case 8:
                    block.wallImage = Textures.wall8;

                    block.wallPhase = 0;
                    break;

                //Red key door
                case 9:
                    block.wallImage = Textures.wall9;

                    block.wallPhase = 0;
                    break;

                //Blue key door
                case 10:
                    block.wallImage = Textures.wall10;

                    block.wallPhase = 0;
                    break;

                //Green key door
                case 11:
                    block.wallImage = Textures.wall11;

                    block.wallPhase = 0;
                    break;

                //Yellow key door
                case 12:
                    block.wallImage = Textures.wall12;

                    block.wallPhase = 0;
                    break;

                //Moving computer graphic wall
                case 13:
                    if(block.wallPhase < 50 * fpsCheck)
                    {
                        block.wallImage = Textures.wall13Phase1;
                    }
                    else
                    {
                        block.wallImage = Textures.wall13Phase2;
                    }

                    if(block.wallPhase > 100 * fpsCheck)
                    {
                        block.wallPhase = 0;
                    }

                    break;

                //Secret found wall
                case 14:
                    block.wallImage = Textures.wall14;

                    block.wallPhase = 0;
                    break;

                //Electric wall
                case 15:
                    brightness = 0;

                    if(block.wallPhase < 10 * fpsCheck)
                    {
                        block.wallImage = Textures.wall15Phase1;

                        brightness = 25;
                    }
                    else if(block.wallPhase < 20 * fpsCheck)
                    {
                        block.wallImage = Textures.wall15Phase2;

                        brightness = 75;
                    }
                    else if(block.wallPhase < 30 * fpsCheck)
                    {
                        block.wallImage = Textures.wall15Phase3;

                        brightness = 150;
                    }
                    else if(block.wallPhase < 40 * fpsCheck)
                    {
                        block.wallImage = Textures.wall15Phase4;

                        brightness = 200;
                    }
                    else if(block.wallPhase < 50 * fpsCheck)
                    {
                        block.wallImage = Textures.wall15Phase5;

                        brightness = 255;
                    }
                    else if(block.wallPhase < 60 * fpsCheck)
                    {
                        block.wallImage = Textures.wall15Phase4;

                        brightness = 200;
                    }
                    else if(block.wallPhase < 70 * fpsCheck)
                    {
                        block.wallImage = Textures.wall15Phase3;

                        brightness = 150;
                    }
                    else if(block.wallPhase < 80 * fpsCheck)
                    {
                        block.wallImage = Textures.wall15Phase2;

                        brightness = 75;
                    }
                    else if(block.wallPhase <= 90 * fpsCheck)
                    {
                        block.wallImage = Textures.wall15Phase1;

                        brightness = 25;
                    }

                    if(block.wallPhase > 90 * fpsCheck)
                    {
                        block.wallPhase = 0;
                    }

                    changesBrightness = true;

                    break;

                //Various liquid walls
                case 16:
                case 17:
                case 25:
                    brightness = 0;

                    if(block.wallPhase < 10 * fpsCheck)
                    {
                        block.wallImage = Textures.toxic1;

                        brightness = 150;
                    }
                    else if(block.wallPhase < 20 * fpsCheck)
                    {
                        block.wallImage = Textures.toxic2;

                        brightness = 175;
                    }
                    else if(block.wallPhase < 30 * fpsCheck)
                    {
                        block.wallImage = Textures.toxic3;

                        brightness = 200;
                    }
                    else if(block.wallPhase < 40 * fpsCheck)
                    {
                        block.wallImage = Textures.toxic4;

                        brightness = 225;
                    }
                    else if(block.wallPhase < 50 * fpsCheck)
                    {
                        block.wallImage = Textures.toxic5;

                        brightness = 255;
                    }
                    else if(block.wallPhase < 60 * fpsCheck)
                    {
                        block.wallImage = Textures.toxic6;

                        brightness = 225;
                    }
                    else if(block.wallPhase < 70 * fpsCheck)
                    {
                        block.wallImage = Textures.toxic7;

                        brightness = 200;
                    }
                    else if(block.wallPhase < 80 * fpsCheck)
                    {
                        block.wallImage = Textures.toxic8;

                        brightness = 175;
                    }
                    else if(block.wallPhase < 90 * fpsCheck)
                    {
                        block.wallImage = Textures.toxic9;

                        brightness = 150;
                    }
                    else if(block.wallPhase < 100 * fpsCheck)
                    {
                        block.wallImage = Textures.toxic10;

                        brightness = 175;
                    }
                    else if(block.wallPhase < 110 * fpsCheck)
                    {
                        block.wallImage = Textures.toxic11;

                        brightness = 200;
                    }
                    else if(block.wallPhase < 120 * fpsCheck)
                    {
                        block.wallImage = Textures.toxic12;

                        brightness = 225;
                    }
                    else if(block.wallPhase < 130 * fpsCheck)
                    {
                        block.wallImage = Textures.toxic13;

                        brightness = 255;
                    }
                    else if(block.wallPhase < 140 * fpsCheck)
                    {
                        block.wallImage = Textures.toxic14;

                        brightness = 225;
                    }
                    else if(block.wallPhase < 150 * fpsCheck)
                    {
                        block.wallImage = Textures.toxic15;

                        brightness = 200;
                    }
                    else if(block.wallPhase < 161 * fpsCheck)
                    {
                        block.wallImage = Textures.toxic16;

                        brightness = 175;
                    }
                    else
                    {
                        block.wallPhase = 0;
                    }

                    changesBrightness = true;

                    break;

                //Spine Wall
                case 18:
                    if(block.wallPhase < 10 * fpsCheck)
                    {
                        block.wallImage = Textures.spine1;
                    }
                    else if(block.wallPhase < 20 * fpsCheck)
                    {
                        block.wallImage = Textures.spine2;
                    }
                    else if(block.wallPhase < 30 * fpsCheck)
                    {
                        block.wallImage = Textures.spine3;
                    }
                    else if(block.wallPhase < 40 * fpsCheck)
                    {
                        block.wallImage = Textures.spine4;
                    }
                    else if(block.wallPhase < 50 * fpsCheck)
                    {
                        block.wallImage = Textures.spine5;
                    }
                    else if(block.wallPhase < 60 * fpsCheck)
                    {
                        block.wallImage = Textures.spine6;
                    }
                    else if(block.wallPhase < 70 * fpsCheck)
                    {
                        block.wallImage = Textures.spine7;
                    }
                    else if(block.wallPhase < 80 * fpsCheck)
                    {
                        block.wallImage = Textures.spine8;
                    }
                    else
                    {
                        block.wallPhase = 0;
                    }

                    break;

                //Dead electric wall
                case 19:
                    brightness = 25;
                    block.wallImage = Textures.wall15Phase1;

                    break;

                //MLG wall
                case 20:
                    block.wallImage = Textures.mlg;
                    block.wallPhase = 0;
                    break;

                //Box wall
                case 21:
                    block.wallImage = Textures.box;
                    block.wallPhase = 0;
                    break;

                //Wood wall
                case 22:
                    block.wallImage = Textures.woodenWall;
                    block.wallPhase = 0;
                    break;

                //Wall with picture on it
                case 23:
                    block.wallImage = Textures.bloodWall;
                    block.wallPhase = 0;
                    break;

                //Marble wall
                case 24:
                    block.wallImage = Textures.marble;
                    block.wallPhase = 0;
                    break;

                //Normal Button
                case 26:
                    block.wallImage = Textures.normButton;
                    block.wallPhase = 0;
                    break;

                //Cool molten rock texture
                case 27:
                    block.wallImage = Textures.coolWall;

                    block.wallPhase = 0;

                    break;

                //Teleporter entrance
                case 28:
                    block.wallImage = Textures.teleportEnter;

                    block.wallPhase = 0;

                    break;

                //Teleporter exit
                case 29:
                    block.wallImage = Textures.teleportExit;

                    block.wallPhase = 0;

                    break;

                //Tutorial Wall
                case 30:
                    block.wallImage = Textures.tutorialWall;

                    block.wallPhase = 0;

                    break;

                //Tutorial Wall 2
                case 31:
                    block.wallImage = Textures.tutorialWall2;

                    block.wallPhase = 0;

                    break;

                //Tutorial Wall 3
                case 32:
                    block.wallImage = Textures.tutorialWall3;

                    block.wallPhase = 0;

                    break;

                //Tutorial Wall 4
                case 33:
                    block.wallImage = Textures.tutorialWall4;

                    block.wallPhase = 0;

                    break;

                //Tutorial Wall 5
                case 34:
                    block.wallImage = Textures.tutorialWall5;

                    block.wallPhase = 0;

                    break;

                //Default texture
                default:
                    block.wallImage = Textures.coolWall;

                    block.wallPhase = 0;

                    break;
            }

            //If not a brightness of 255, it'll need to be changed
            if(brightness != 255)
            {
                changesBrightness = true;
            }

            //How it iterates through the for loop. By 1s or 2s
            int correction = 1;

            //Color of current picture
            int color = 0;

            //Does something with textures. I don't yet understand this
            double zWall = (texture1 + (texture2 - texture1));

            if(lowRes)
            {
                correction = 2;

			   /*
			    * While the pixels being rendered are still within the bounds
			    * of the wall (The width of the wall)
			    */
                for(int x = xPixelLeftInt; x < xPixelRightInt; x+=correction)
                {
				   /*
				    * How much the pixels are to rotate depending on your movement
				    * . Your movement is tracked by the change in xPixelLeft and
				    * xPixelRight from frame to frame.
				    */
                    double pixelRotation = (x - xPixelLeft) /
                            (xPixelLeft - xPixelRight);

                    //Figures out the texture that needs to be rendered
                    int xTexture = (int) (((texture4) * (pixelRotation)) /
                            zWall);

				   /*
				    * Figures out where the top pixel and bottom pixel are located
				    * on the screen.
				    */
                    double yPixelTop     = yPixelTopLeft +
                            (yPixelTopLeft - yPixelTopRight)
                                    * pixelRotation;

                    double yPixelBottom  = yPixelBottomLeft +
                            (yPixelBottomLeft - yPixelBottomRight)
                                    * pixelRotation;

				   /*
				    * Casts them into ints to be drawn to the screen
				    */
                    int yPixelTopInt     = (int) (yPixelTop);
                    int yPixelBottomInt  = (int) (yPixelBottom);

				   /*
				    * If the wall goes out of the top of the frame, reduce it so
				    * that it stays in the frame.
				    */
                    if(yPixelTopInt < 0)
                    {
                        yPixelTopInt = 0;
                    }

				   /*
				    * If the wall goes below the frame, make it so that it still
				    * stays in the frame.
				    */
                    if(yPixelBottomInt > HEIGHT)
                    {
                        yPixelBottomInt = HEIGHT;
                    }

				   /*
				    * If the top is farther down than the bottom of the wall,
				    * then don't render that maddness
				    */
                    if(yPixelTopInt > yPixelBottomInt)
                    {
                        return;
                    }

				   /*
				    * For each y pixel from the top of the wall to the bottom,
				    * render the pixels correctly depending on how you rotate
				    * upward (looking up). Also depending on the walls ID, change
				    * the texture of the wall to be so.
				    */
                    for(int y = yPixelTopInt; y < yPixelBottomInt; y+=correction)
                    {
                        //Figures out how the pixel should be stretched or look like
                        //in the y direction
                        double pixelRotationY = (y - yPixelTop) /
                                (yPixelBottom - yPixelTop);

                        int yTexture = (int) (256 * pixelRotationY);

                        try
                        {
                            //If wall is behind another wall, break out of the
                            //loops here because it shouldn't be able to be seen
                            //behind another wall.
                            if(zBufferWall[(x) + (y) * WIDTH] > zWall
                                    || zBufferWall[(x) + (y+1) * WIDTH] > zWall
                                    || zBufferWall[(x+1) + (y+1) * WIDTH] > zWall
                                    || zBufferWall[(x+1) + (y) * WIDTH] > zWall)
                            {
                                continue;
                            }

                            //Set color of pixel to draw to the color of the pixel
                            //it correlates to in the image file
                            color = block.wallImage.PIXELS
                                    [(xTexture & 255) + (yTexture & 255) * 256];

                        }
                        catch(Exception e)
                        {
                            continue;
                        }

					   /*
					    * If color is not white, render it, otherwise don't to
					    * have transparency around your image. First two ff's
					    * are the images alpha, 2nd two are red, 3rd two are
					    * green, 4th two are blue. ff is 255.
					    */
                        if(color != 0xffffffff || Display.themeNum == 3
                                || Display.themeNum == 5)
                        {
                            //Change brightness of pixel if needed
                            if(changesBrightness)
                            {
                                color = adjustBrightness(brightness, color, wallID);
                            }

                            double rotZ =
                                    (0.4 / (texture1 -
                                            (texture2 - texture1)
                                                    * pixelRotation)) * 0.3;

                            //Try to render
                            try
                            {
                                PIXELS[x + y * WIDTH] = color;
                                zBuffer[(x) + (y) * WIDTH] = rotZ;
                                PIXELS[(x + 1) + (y) * WIDTH] = color;
                                zBuffer[(x+1) + (y) * WIDTH] = rotZ;
                                PIXELS[(x + 1) + (y + 1) * WIDTH] = color;
                                zBuffer[(x+1) + (y+1) * WIDTH] = rotZ;
                                PIXELS[(x) + (y + 1) * WIDTH] = color;
                                zBuffer[(x) + (y+1) * WIDTH] = rotZ;
                            }
                            catch(Exception e)
                            {
                                continue;
                            }

                            //Set this pixel to being the front most wall pixel
                            zBufferWall[(x) + (y) * WIDTH] =  zWall;

                            try
                            {
                                zBufferWall[(x) + (y+1) * WIDTH] =  zWall;
                                zBufferWall[(x+1) + (y+1) * WIDTH] = zWall;
                                zBufferWall[(x+1) + (y) * WIDTH] = zWall;
                            }
                            catch(Exception e)
                            {
                                continue;
                            }
                        }

					   /*
					    * Always render the last couple of pixels to ensure that
					    * the edges of the walls are always rendered no matter
					    * what because if they aren't... weird things occur.
					    */
                        if(x >= xPixelRightInt - 6)
                        {
                            correction = 1;
                        }
                    }
                }
            }
            else
            {
			   /*
			    * While the pixels being rendered are still within the bounds
			    * of the wall (The width of the wall)
			    */
                for(int x = xPixelLeftInt; x < xPixelRightInt; x++)
                {
				   /*
				    * How much the pixels are to rotate depending on your movement
				    * . Your movement is tracked by the change in xPixelLeft and
				    * xPixelRight from frame to frame.
				    */
                    double pixelRotation = (x - xPixelLeft) /
                            (xPixelLeft - xPixelRight);

                    //Figures out the texture that needs to be rendered
                    int xTexture = (int) (((texture4) * (pixelRotation)) /
                            zWall);

				   /*
				    * Figures out where the top pixel and bottom pixel are located
				    * on the screen.
				    */
                    double yPixelTop     = yPixelTopLeft +
                            (yPixelTopLeft - yPixelTopRight)
                                    * pixelRotation;
                    double yPixelBottom  = yPixelBottomLeft +
                            (yPixelBottomLeft - yPixelBottomRight)
                                    * pixelRotation;

				   /*
				    * Casts them into ints to be drawn to the screen
				    */
                    int yPixelTopInt     = (int) (yPixelTop);
                    int yPixelBottomInt  = (int) (yPixelBottom);

				   /*
				    * If the wall goes out of the top of the frame, reduce it so
				    * that it stays in the frame.
				    */
                    if(yPixelTopInt < 0)
                    {
                        yPixelTopInt = 0;
                    }

				   /*
				    * If the wall goes below the frame, make it so that it still
				    * stays in the frame.
				    */
                    if(yPixelBottomInt > HEIGHT)
                    {
                        yPixelBottomInt = HEIGHT;
                    }

				   /*
				    * If the top is farther down than the bottom of the wall,
				    * then don't render that maddness
				    */
                    if(yPixelTopInt > yPixelBottomInt)
                    {
                        return;
                    }

				   /*
				    * For each y pixel from the top of the wall to the bottom,
				    * render the pixels correctly depending on how you rotate
				    * upward (looking up). Also depending on the walls ID, change
				    * the texture of the wall to be so.
				    */
                    for(int y = yPixelTopInt; y < yPixelBottomInt; y++)
                    {
                        //Figures out how the pixel should be stretched or look like
                        //in the y direction
                        double pixelRotationY = (y - yPixelTop) /
                                (yPixelBottom - yPixelTop);
                        int yTexture = (int) (256 * pixelRotationY);

                        try
                        {
                            //If wall is behind another wall, break out of the
                            //loops here because it shouldn't be able to be seen
                            //behind another wall.
                            if(zBufferWall[(x) + (y) * WIDTH] > zWall)
                            {
                                continue;
                            }

                            //Set color of pixel to draw to the color of the pixel
                            //it correlates to in the image file
                            color = block.wallImage.PIXELS
                                    [(xTexture & 255) + (yTexture & 255) * 256];
                        }
                        catch(Exception e)
                        {
                            continue;
                        }

					   /*
					    * If color is not white, render it, otherwise don't to
					    * have transparency around your image. First two ff's
					    * are the images alpha, 2nd two are red, 3rd two are
					    * green, 4th two are blue. ff is 255.
					    */
                        if(color != 0xffffffff || Display.themeNum == 3
                                || Display.themeNum == 5)
                        {
                            //Change brightness of pixel if needed
                            if(changesBrightness)
                            {
                                color = adjustBrightness(brightness, color, wallID);
                            }

                            double rotZ =
                                    (0.4 / (texture1 -
                                            (texture2 - texture1)
                                                    * pixelRotation)) * 0.3;
                            //Try to render
                            try
                            {
                                PIXELS[x + y * WIDTH] = color;
                                zBuffer[(x) + (y) * WIDTH] = rotZ;
                            }
                            catch(Exception e)
                            {
                                continue;
                            }

                            //Set this pixel to being the front most wall pixel
                            zBufferWall[(x) + (y) * WIDTH] =  zWall;
                        }
                    }
                }
            }
        }
        catch(Exception e)
        {
        }
    }

    /**
     * Corrects the wall textures so they don't cause each other to clip
     * into each other and disappear, and also renders them correctly so
     * all 4 corners of each wall are seen correctly and with the correct
     * width and height of textures.
     * @param block
     */
    public void renderWallsCorrect(Block block)
    {
        currentWallID = block.wallID;

        //Corrects wall rendering for player so size looks normal.
        //And so the player doesn't clip through the textures.
        double test = 63.92;

        Block eastBlock  = Level.getBlock(block.x + 1, block.z);
        Block southBlock = Level.getBlock(block.x, block.z + 1);
        Block westBlock  = Level.getBlock(block.x - 1, block.z);
        Block northBlock = Level.getBlock(block.x, block.z - 1);


        double renderHeight = block.height;

	   /*
	    * Corrects the height of the wall that is rendered if the blocks
	    * height is less than 12. This is only because when the height
	    * is less than 12, the wall does not render the correct height
	    * in accordance to the player. For example when the wall
	    * height is 0, for some reason it will render as if it was at
	    * height 6 and etc...
	    */
        if(block.height < 12)
        {
            renderHeight = 12 - ((12 - block.height) * 2);
        }
        else if(block.height >= 48)
        {
            renderHeight += 18;
        }
        else if(block.height >= 24)
        {
            renderHeight += 6;
        }
        else if(block.height > 12)
        {
            renderHeight += 0;
        }

        block.hCorrect = renderHeight;

        //IF the block being checked is solid
        if(block.isSolid)
        {
		   /*
		    * If the block on the east side is not the same or greater
		    * height or same y value as the block currently being
		    * rendered.
		    *
		    * Also if the current block is not seeThrough, but the
		    * east block is, render this block still.
		    */
            if(eastBlock.height < block.height
                    || eastBlock.y != block.y
                    || eastBlock.seeThrough && !block.seeThrough)
            {
                if(Math.sin(Player.rotation) > 0)
                {
                    render3DWalls((block.x + 1) * test,
                            (block.x + 1) * test, block.y / 3,
                            block.z * test, (block.z + 0.99999)
                                    * test, renderHeight, block.wallID, block);
                }
                else
                {
                    render3DWalls((block.x + 1.00001) * test,
                            (block.x + 1.00001) * test, block.y / 3,
                            block.z * test, (block.z + 0.99999)
                                    * test, renderHeight, block.wallID, block);
                }
            }

		   /*
		    * Same as the east block but on the south side (positive z)
		    */
            if(southBlock.height < block.height
                    || southBlock.y != block.y
                    || southBlock.seeThrough && !block.seeThrough)
            {
                if(Math.cos(Player.rotation) < 0)
                {
                    render3DWalls((block.x + 1) * test,
                            (block.x + 0.00002) * test, block.y / 3, (block.z + 1)
                                    * test, (block.z + 1)
                                    * test, renderHeight, block.wallID, block);
                }
                else
                {
                    render3DWalls((block.x + 1) * test,
                            (block.x + 0.00002) * test, block.y / 3, (block.z + 1)
                                    * test, (block.z + 1)
                                    * test, renderHeight, block.wallID, block);
                }
            }

		   /*
		    * Same as others but in the north direction (negative z)
		    */
            if(northBlock.height < block.height
                    || northBlock.y != block.y
                    || northBlock.seeThrough && !block.seeThrough)
            {
			   /*
			    * Fixes a weird graphical glitch that happens for some
			    * reason.
			    */
                if(Math.sin(Player.rotation) > 0)
                {
                    render3DWalls(block.x * test,
                            (block.x + 0.99999) * test, block.y / 3,
                            (block.z) * test,
                            (block.z) * test,
                            renderHeight, block.wallID, block);
                }
                else
                {
                    render3DWalls(block.x * test,
                            (block.x + 1) * test, block.y / 3,
                            (block.z) * test,
                            (block.z) * test,
                            renderHeight, block.wallID, block);
                }
            }

		   /*
		    * Same as others but in west (Negative x) direction.
		    */
            if(westBlock.height < block.height
                    || westBlock.y != block.y
                    || westBlock.seeThrough && !block.seeThrough)
            {
                render3DWalls((block.x) * test,
                        (block.x) * test, block.y / 3,
                        (block.z + 1) * test, (block.z + 0.00001) * test,
                        renderHeight, block.wallID, block);

            }
        }
    }

    /**
     * Adjusts the brightness of a texture based on the brightness sent
     * to this method, and the color (in integer form) of the texture.
     *
     * WallID is only used for walls so that lava can be the toxic
     * waste texture shifted to red. If an item calls this method, it
     * will send in 0 for wallID.
     *
     * @param brightness
     * @param color
     * @param wallID
     * @return
     */
    public int adjustBrightness(int brightness, int color, int wallID)
    {
        //Certain walls change brightness, but if they are at
        //full brightness, don't go through this whole method
        if(brightness == 255 && wallID != 17 && wallID != 25)
        {
            return color;
        }

	   /*
	    * Or you can use 0xff. It goes from 0 - 255, and 255 = 0xff.
	    * The 255 is not technically needed as it just causes the
	    * number to stay the same, but it does matter for the int b
	    * for some reason. I think because it causes the render distance
	    * to fade to blue if not. The shifting of the ints causes the color
	    * to change.
	    *
	    * Converts the bit value of the color into an int so that it can
	    * be easily tampered with
	    */
        int r = (color >> 16) & 255;
        int g = (color >> 8)  & 255;
        int b =  color        & 255;

	   /*
	    * Divides that value by 255, then multiplies it by the
	    * brightness level of the pixel to determine how bright
	    * the reds, greens, and blues in each pixel will get.
	    */
        r     = (r * brightness) / 255;
        g     = (g * brightness) / 255;
        b     = (b * brightness) / 255;

        if (wallID == 17)
        {
            color = r << 16 | g << 16 | b << 16;
        }
        else if(wallID == 25)
        {
            color = r | g | b;
        }
        //Set colors back to their original components
        else
        {
            //Set colors back to their original components
            //If not white
            if(color != 0xffffffff)
            {
                color = r << 16 | g << 8 | b;
            }
        }

        //Return new color with new brightness
        return color;
    }

    /**
     * Creates the effect of objects getting darker as you look into the
     * distance of the map.
     */
    public void renderDistanceLimiter()
    {
        int skip = 6;

        if(lowRes)
        {
            //Go through all the pixels on the screen in series of 6
            for (int i = 0; i < (WIDTH * HEIGHT); i+=skip)
            {
                //Color value of this pixel in integer form
                int color       = PIXELS[i];
                int color1 = 0;
                int color2 = 0;
                int color3 = 0;
                int color4 = 0;
                int color5 = 0;

                //Brightness of color. 255 is Full brightness
                int brightness = 255;
                int brightness1 = 255;
                int brightness2 = 255;
                int brightness3 = 255;
                int brightness4 = 255;
                int brightness5 = 255;

                int j = Player.vision;

			   /*
			    * If player is nearing the end of immortality, or is not
			    * immortal.
			    */
                if(j < 100 * fpsCheck && j % 5 == 0)
                {
				   /*
				    * The brightness of each pixel depending on its distance
				    * from the player, and the render Distance
				    */
                    brightness = (int) (renderDistance / (zBuffer[i]));
                    brightness1 = (int) (renderDistance / (zBuffer[i+1]));
                    brightness2 = (int) (renderDistance / (zBuffer[i+2]));
                    brightness3 = (int) (renderDistance / (zBuffer[i+3]));
                    brightness4 = (int) (renderDistance / (zBuffer[i+4]));
                    brightness5 = (int) (renderDistance / (zBuffer[i+5]));
                }
                //If the player is immortal
                else
                {
                    brightness = 255;
                    brightness1 = 255;
                    brightness2 = 255;
                    brightness3 = 255;
                    brightness4 = 255;
                    brightness5 = 255;
                }

                //Never can be less than 0 brightness
                if(brightness < 0)
                {
                    brightness = 0;
                }

                color1      = PIXELS[i+1];
                color2      = PIXELS[i+2];
                color3      = PIXELS[i+3];
                color4      = PIXELS[i+4];
                color5      = PIXELS[i+5];

                if(brightness1 < 0)
                {
                    brightness1 = 0;
                }
                if(brightness2 < 0)
                {
                    brightness2 = 0;
                }
                if(brightness3 < 0)
                {
                    brightness3 = 0;
                }
                if(brightness4 < 0)
                {
                    brightness4 = 0;
                }
                if(brightness5 < 0)
                {
                    brightness5 = 0;
                }

                //Can never be brighter than 255
                if(brightness1 > 255)
                {
                    brightness1 = 255;
                }
                //Can never be brighter than 255
                if(brightness2 > 255)
                {
                    brightness2 = 255;
                }
                //Can never be brighter than 255
                if(brightness3 > 255)
                {
                    brightness3 = 255;
                }
                //Can never be brighter than 255
                if(brightness4 > 255)
                {
                    brightness4 = 255;
                }
                //Can never be brighter than 255
                if(brightness5 > 255)
                {
                    brightness5 = 255;
                }

                //Can never be brighter than 255
                if(brightness > 255)
                {
                    brightness = 255;
                }

			   /*
			    * Or you can use 0xff. It goes from 0 - 255, and 255 = 0xff.
			    * The 255 is not technically needed as it just causes the
			    * number to stay the same, but it does matter for the int b
			    * for some reason. I think because it causes the render distance
			    * to fade to blue if not. The shifting of the ints causes the color
			    * to change.
			    *
			    * Converts the bit value of the color into an int so that it can
			    * be easily tampered with
			    */
                int r = (color >> 16) & 255;
                int g = (color >> 8)  & 255;
                int b =  color        & 255;
                int r1 = 0;
                int g1 = 0;
                int b1 = 0;
                int r2 = 0;
                int g2 = 0;
                int b2 = 0;
                int r3 = 0;
                int g3 = 0;
                int b3 = 0;
                int r4 = 0;
                int g4 = 0;
                int b4 = 0;
                int r5 = 0;
                int g5 = 0;
                int b5 = 0;

			   /*
			    * Divides that value by 255, then multiplies it by the
			    * brightness level of the pixel to determine how bright
			    * the reds, greens, and blues in each pixel will get.
			    */
                r     = (r * brightness) / 255;
                g     = (g * brightness) / 255;
                b     = (b * brightness) / 255;
                r1 = (color1 >> 16) & 255;
                g1 = (color1 >> 8)  & 255;
                b1 =  color1        & 255;
                r2 = (color2 >> 16) & 255;
                g2 = (color2 >> 8)  & 255;
                b2 =  color2        & 255;
                r3 = (color3 >> 16) & 255;
                g3 = (color3 >> 8)  & 255;
                b3 =  color3        & 255;
                r4 = (color4 >> 16) & 255;
                g4 = (color4 >> 8)  & 255;
                b4 =  color4        & 255;
                r5 = (color5 >> 16) & 255;
                g5 = (color5 >> 8)  & 255;
                b5 =  color5        & 255;
                r1     = (r1 * brightness1) / 255;
                g1     = (g1 * brightness1) / 255;
                b1     = (b1 * brightness1) / 255;
                r2     = (r2 * brightness2) / 255;
                g2     = (g2 * brightness2) / 255;
                b2     = (b2 * brightness2) / 255;
                r3     = (r3 * brightness3) / 255;
                g3     = (g3 * brightness3) / 255;
                b3     = (b3 * brightness3) / 255;
                r4     = (r4 * brightness4) / 255;
                g4     = (g4 * brightness4) / 255;
                b4     = (b4 * brightness4) / 255;
                r5     = (r5 * brightness5) / 255;
                g5     = (g5 * brightness5) / 255;
                b5     = (b5 * brightness5) / 255;

                //Reset the bits of that particular pixel
                if(Player.alive && Player.playerHurt == 0)
                {
                    int ePT = Player.environProtectionTime;

                    if((ePT < 100 * fpsCheck && ePT % 5 == 0))
                    {
                        PIXELS[i] = r << 16 | g << 8 | b;
                        PIXELS[i+1] = r1 << 16 | g1 << 8 | b1;
                        PIXELS[i+2] = r2 << 16 | g2 << 8 | b2;
                        PIXELS[i+3] = r3 << 16 | g3 << 8 | b3;
                        PIXELS[i+4] = r4 << 16 | g4 << 8 | b4;
                        PIXELS[i+5] = r5 << 16 | g5 << 8 | b5;
                    }
                    else
                    {
                        PIXELS[i] = r << 8 | g << 8 | b << 8;
                        PIXELS[i+1] = r1 << 8 | g1 << 8 | b1 << 8;
                        PIXELS[i+2] = r2 << 8 | g2 << 8 | b2 << 8;
                        PIXELS[i+3] = r3 << 8 | g3 << 8 | b3 << 8;
                        PIXELS[i+4] = r4 << 8 | g4 << 8 | b4 << 8;
                        PIXELS[i+5] = r5 << 8 | g5 << 8 | b5 << 8;
                    }
                }
                else
                {
                    PIXELS[i] = r << 16 | g << 16 | b << 16;
                    PIXELS[i+1] = r1 << 16 | g1 << 16 | b1 << 16;
                    PIXELS[i+2] = r2 << 16 | g2 << 16 | b2 << 16;
                    PIXELS[i+3] = r3 << 16 | g3 << 16 | b3 << 16;
                    PIXELS[i+4] = r4 << 16 | g4 << 16 | b4 << 16;
                    PIXELS[i+5] = r5 << 16 | g5 << 16 | b5 << 16;
                }

                if(i + 6 > WIDTH * HEIGHT)
                {
                    skip = 1;
                }
            }
        }
        else
        {
            //Go through all the pixels on the screen
            for (int i = 0; i < (WIDTH * HEIGHT); i++)
            {
                //Color value of this pixel in integer form
                int color       = PIXELS[i];

                //Brightness of color. 255 is Full brightness
                int brightness = 255;

                //Whether the player has enhanced vision or not
                int j = Player.vision;

			   /*
			    * If player is nearing end of enhanced vision
			    */
                if(j < 100 * fpsCheck && j % 5 == 0)
                {
				   /*
				    * The brightness of each pixel depending on its distance
				    * from the player, and the render Distance
				    *
				    * zBuffer is the distance from the player in terms of z
				    * the pixel is, and the renderDistance is how far the
				    * player can see. If the pixel is farther than the
				    * renderDistance then just make it black.
				    */
                    brightness = (int) (renderDistance / (zBuffer[i]));
                }
                //If the player has enhanced vision
                else
                {
                    brightness = 255;
                }

                //Never can be less than 0 brightness
                if(brightness < 0)
                {
                    brightness = 0;
                }

                //Can never be brighter than 255
                if(brightness > 255)
                {
                    brightness = 255;
                }

			   /*
			    * Or you can use 0xff. It goes from 0 - 255, and 255 = 0xff.
			    * The 255 is not technically needed as it just causes the
			    * number to stay the same, but it does matter for the int b
			    * for some reason. I think because it causes the render distance
			    * to fade to blue if not. The shifting of the ints causes the color
			    * to change.
			    *
			    * Converts the bit value of the color into an int so that it can
			    * be easily tampered with
			    */
                int r = (color >> 16) & 255;
                int g = (color >> 8)  & 255;
                int b =  color        & 255;

			   /*
			    * Takes the brightness value and divides it by 255 to put
			    * it into the range of 0 to 255 so it can be rendered as
			    * the correct brightness on screen.
			    */
                r     = (r * brightness) / 255;
                g     = (g * brightness) / 255;
                b     = (b * brightness) / 255;

                //If Player is not being hurt and is alive
                if(Player.alive && Player.playerHurt == 0)
                {
                    int ePT = Player.environProtectionTime;

                    //If the environmental protection suit is wearing off
                    if((ePT < 100 * fpsCheck && ePT % 5 == 0))
                    {
                        PIXELS[i] = r << 16 | g << 8 | b << 0;
                    }
                    else
                    {
                        PIXELS[i] = r << 8 | g << 8 | b << 8;
                    }
                }
                //If dead or hurt
                else
                {
                    PIXELS[i] = r << 16 | g << 16 | b << 16;
                }
            }
        }
    }

    /**
     * May use a special render method in the future, but for now just
     * render all the blocks.
     * @param index
     */
    public void renderBlocks()
    {
        for(Block block: Level.blocks)
        {
            renderWallsCorrect(block);
        }
    }
}