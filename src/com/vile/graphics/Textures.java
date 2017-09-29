package com.vile.graphics;

import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.imageio.ImageIO;

import com.vile.graphics.Render;
import com.vile.Display;

/**
 * Title: Textures
 * @author Alex Byrd
 * Date Updated: 7/26/2016
 *
 * Descriptions:
 * Loads the textures into the game from the textures file, and makes them
 * into Render objects with given rgb values and width and heights so that
 * they are rendered correctly on screen.
 */
public class Textures 
{
	public static Render[] floors;
	
	public static Render enemy1   = loadBitMap
			("resources/textures/enemy1.png");
	public static Render enemy2   = loadBitMap
			("resources/textures/enemy2.png");
	public static Render enemy3   = loadBitMap
			("resources/textures/enemy3.png");
	public static Render enemy4   = loadBitMap
			("resources/textures/enemy4.png");
	public static Render wall1 = loadBitMap
			("resources/textures/theme"+(Display.themeNum + 1)
					+"/walls/wall1.png");
	public static Render wall2 = loadBitMap
			("resources/textures/theme"+(Display.themeNum + 1)
					+"/walls/wall2.png");
	public static Render wall3 = loadBitMap
			("resources/textures/theme"+(Display.themeNum + 1)
					+"/walls/wall3.png");
	public static Render wall4 = loadBitMap
			("resources/textures/theme"+(Display.themeNum + 1)
					+"/walls/wall4.png");
	public static Render wall4damaged1 = loadBitMap
			("resources/textures/theme"+3
					+"/walls/wall4damage1.png");
	public static Render wall4damaged2 = loadBitMap
			("resources/textures/theme"+3
					+"/walls/wall4damage2.png");
	public static Render wall4damaged3 = loadBitMap
			("resources/textures/theme"+3
					+"/walls/wall4damage3.png");
	public static Render wall5 = loadBitMap
			("resources/textures/theme"+(Display.themeNum + 1)
					+"/walls/wall5.png");
	public static Render wall6 = loadBitMap
			("resources/textures/theme"+(Display.themeNum + 1)
					+"/walls/wall6.png");
	public static Render wall7 = loadBitMap
			("resources/textures/theme"+(Display.themeNum + 1)
					+"/walls/wall7.png");
	public static Render wall8 = loadBitMap
			("resources/textures/theme"+(Display.themeNum + 1)
					+"/walls/wall8.png");
	public static Render wall9 = loadBitMap
			("resources/textures/theme"+(Display.themeNum + 1)
					+"/walls/wall9.png");
	public static Render wall10 = loadBitMap
			("resources/textures/theme"+(Display.themeNum + 1)
					+"/walls/wall10.png");
	public static Render wall11 = loadBitMap
			("resources/textures/theme"+(Display.themeNum + 1)
					+"/walls/wall11.png");
	public static Render wall12 = loadBitMap
			("resources/textures/theme"+(Display.themeNum + 1)
					+"/walls/wall12.png");
	public static Render wall13Phase1 = loadBitMap
			("resources/textures/theme"+(Display.themeNum + 1)
					+"/walls/wall13phase1.png");
	public static Render wall13Phase2 = loadBitMap
			("resources/textures/theme"+(Display.themeNum + 1)
					+"/walls/wall13phase2.png");
	public static Render wall14 = loadBitMap
			("resources/textures/theme"+(Display.themeNum + 1)
					+"/walls/wall14.png");
	public static Render teleportEnter = loadBitMap
			("resources/textures/teleporterEnter.png");
	public static Render teleportExit = loadBitMap
			("resources/textures/teleporterExit.png");
	public static Render tutorialWall = loadBitMap
			("resources/textures/tutorialWall.png");
	public static Render tutorialWall2 = loadBitMap
			("resources/textures/tutorialWall2.png");
	public static Render tutorialWall3 = loadBitMap
			("resources/textures/tutorialWall3.png");
	public static Render tutorialWall4 = loadBitMap
			("resources/textures/tutorialWall4.png");
	public static Render tutorialWall5 = loadBitMap
			("resources/textures/tutorialWall5.png");
	public static Render wall15Phase5 = loadBitMap
			("resources/textures/theme"+(Display.themeNum + 1)
					+"/walls/wall15phase5.png");
	public static Render wall15Phase4 = loadBitMap
			("resources/textures/theme"+(Display.themeNum + 1)
					+"/walls/wall15phase4.png");
	public static Render wall15Phase3 = loadBitMap
			("resources/textures/theme"+(Display.themeNum + 1)
					+"/walls/wall15phase3.png");
	public static Render wall15Phase2 = loadBitMap
			("resources/textures/theme"+(Display.themeNum + 1)
					+"/walls/wall15phase2.png");
	public static Render wall15Phase1 = loadBitMap
			("resources/textures/theme"+(Display.themeNum + 1)
					+"/walls/wall15phase1.png");
	
	//Toxic Waste/Lava. Has many phases
	public static Render toxic1 = loadBitMap
			("resources/textures/theme"+(Display.themeNum + 1)
					+"/walls/toxicWaste16.png");
	public static Render toxic2 = loadBitMap
			("resources/textures/theme"+(Display.themeNum + 1)
					+"/walls/toxicWaste15.png");
	public static Render toxic3 = loadBitMap
			("resources/textures/theme"+(Display.themeNum + 1)
					+"/walls/toxicWaste14.png");
	public static Render toxic4 = loadBitMap
			("resources/textures/theme"+(Display.themeNum + 1)
					+"/walls/toxicWaste13.png");
	public static Render toxic5 = loadBitMap
			("resources/textures/theme"+(Display.themeNum + 1)
					+"/walls/toxicWaste12.png");
	public static Render toxic6 = loadBitMap
			("resources/textures/theme"+(Display.themeNum + 1)
					+"/walls/toxicWaste11.png");
	public static Render toxic7 = loadBitMap
			("resources/textures/theme"+(Display.themeNum + 1)
					+"/walls/toxicWaste10.png");
	public static Render toxic8 = loadBitMap
			("resources/textures/theme"+(Display.themeNum + 1)
					+"/walls/toxicWaste9.png");
	public static Render toxic9 = loadBitMap
			("resources/textures/theme"+(Display.themeNum + 1)
					+"/walls/toxicWaste8.png");
	public static Render toxic10 = loadBitMap
			("resources/textures/theme"+(Display.themeNum + 1)
					+"/walls/toxicWaste7.png");
	public static Render toxic11 = loadBitMap
			("resources/textures/theme"+(Display.themeNum + 1)
					+"/walls/toxicWaste6.png");
	public static Render toxic12 = loadBitMap
			("resources/textures/theme"+(Display.themeNum + 1)
					+"/walls/toxicWaste5.png");
	public static Render toxic13 = loadBitMap
			("resources/textures/theme"+(Display.themeNum + 1)
					+"/walls/toxicWaste4.png");
	public static Render toxic14 = loadBitMap
			("resources/textures/theme"+(Display.themeNum + 1)
					+"/walls/toxicWaste3.png");
	public static Render toxic15 = loadBitMap
			("resources/textures/theme"+(Display.themeNum + 1)
					+"/walls/toxicWaste2.png");
	public static Render toxic16 = loadBitMap
			("resources/textures/theme"+(Display.themeNum + 1)
					+"/walls/toxicWaste1.png");
	////////////////////////////////////////////////////////

	public static Render health  = loadBitMap
			("resources/textures/health.png");
	public static Render shell   = loadBitMap
			("resources/textures/shell.png");
	public static Render megaPhase1   = loadBitMap
			("resources/textures/megahealthphase1.png");
	public static Render megaPhase2   = loadBitMap
			("resources/textures/megahealthphase2.png");
	public static Render megaPhase3   = loadBitMap
			("resources/textures/megahealthphase3.png");
	public static Render megaPhase4   = loadBitMap
			("resources/textures/megahealthphase4.png");
	public static Render redKey = loadBitMap
			("resources/textures/redKey.png");
	public static Render blueKey = loadBitMap
			("resources/textures/blueKey.png");
	public static Render greenKey = loadBitMap
			("resources/textures/greenKey.png");
	public static Render yellowKey = loadBitMap
			("resources/textures/yellowKey.png");
	public static Render bullet  = loadBitMap
			("resources/textures/bullet.png");
	public static Render shotgun  = loadBitMap
			("resources/textures/shotgun.png");
	public static Render resurrect1  = loadBitMap
			("resources/textures/resurrectSkull1.png");
	public static Render resurrect2  = loadBitMap
			("resources/textures/resurrectSkull2.png");
	public static Render resurrect3  = loadBitMap
			("resources/textures/resurrectSkull3.png");
	public static Render environSuit  = loadBitMap
			("resources/textures/environSuit.png");
	public static Render goblet1  = loadBitMap
			("resources/textures/goblet1.png");
	public static Render goblet2  = loadBitMap
			("resources/textures/goblet2.png");
	public static Render goblet3  = loadBitMap
			("resources/textures/goblet3.png");
	public static Render adrenaline  = loadBitMap
			("resources/textures/adrenaline.png");
	public static Render glasses  = loadBitMap
			("resources/textures/glasses.png");
	public static Render torch1   = loadBitMap
			("resources/textures/torch1.png");
	public static Render torch2   = loadBitMap
			("resources/textures/torch2.png");
	public static Render torch3   = loadBitMap
			("resources/textures/torch3.png");
	public static Render torch4   = loadBitMap
			("resources/textures/torch4.png");
	public static Render lamp1   = loadBitMap
			("resources/textures/lamp1.png");
	public static Render lamp2   = loadBitMap
			("resources/textures/lamp2.png");
	public static Render lamp3   = loadBitMap
			("resources/textures/lamp3.png");
	public static Render tree   = loadBitMap
			("resources/textures/tree.png");
	public static Render canister   = loadBitMap
			("resources/textures/canister.png");
	public static Render canExplode1   = loadBitMap
			("resources/textures/canExplode1.png");
	public static Render canExplode2   = loadBitMap
			("resources/textures/canExplode2.png");
	public static Render canExplode3   = loadBitMap
			("resources/textures/canExplode3.png");
	public static Render canExplode4   = loadBitMap
			("resources/textures/canExplode4.png");
	public static Render chainmeal   = loadBitMap
			("resources/textures/chainmealArmor.png");
	public static Render combat   = loadBitMap
			("resources/textures/combatArmor.png");
	public static Render argent   = loadBitMap
			("resources/textures/argentArmor.png");
	public static Render shard   = loadBitMap
			("resources/textures/armorShard.png");
	public static Render vial   = loadBitMap
			("resources/textures/healthVial.png");
	public static Render upgrade1   = loadBitMap
			("resources/textures/weaponUpgrade1.png");
	public static Render upgrade2   = loadBitMap
			("resources/textures/weaponUpgrade2.png");
	public static Render upgrade3   = loadBitMap
			("resources/textures/weaponUpgrade3.png");
	public static Render upgrade4   = loadBitMap
			("resources/textures/weaponUpgrade4.png");
	public static Render holyWater1   = loadBitMap
			("resources/textures/holyWater1.png");
	public static Render holyWater2   = loadBitMap
			("resources/textures/holyWater2.png");
	public static Render table   = loadBitMap
			("resources/textures/table.png");
	public static Render lampTable = loadBitMap
			("resources/textures/lampTable.png");
	public static Render scepter   = loadBitMap
			("resources/textures/decietScepter.png");
	public static Render invisEmerald   = loadBitMap
			("resources/textures/invisibilityEmerald.png");
	public static Render invisEmerald2   = loadBitMap
			("resources/textures/invisibilityEmerald2.png");
	public static Render invisEmerald3   = loadBitMap
			("resources/textures/invisibilityEmerald3.png");
	public static Render invisEmerald4   = loadBitMap
			("resources/textures/invisibilityEmerald4.png");
	public static Render morgoth   = loadBitMap
			("resources/textures/morgoth.png");
	public static Render enemy5   = loadBitMap
			("resources/textures/enemy5.png");
	public static Render shellBox = loadBitMap
			("resources/textures/shellBox.png");
	
	//Spine wall phases
	public static Render spine1 = loadBitMap
			("resources/textures/theme"+(Display.themeNum + 1)
					+"/walls/spineWall1.png");
	public static Render spine2 = loadBitMap
			("resources/textures/theme"+(Display.themeNum + 1)
					+"/walls/spineWall2.png");
	public static Render spine3 = loadBitMap
			("resources/textures/theme"+(Display.themeNum + 1)
					+"/walls/spineWall3.png");
	public static Render spine4 = loadBitMap
			("resources/textures/theme"+(Display.themeNum + 1)
					+"/walls/spineWall4.png");
	public static Render spine5 = loadBitMap
			("resources/textures/theme"+(Display.themeNum + 1)
					+"/walls/spineWall5.png");
	public static Render spine6 = loadBitMap
			("resources/textures/theme"+(Display.themeNum + 1)
					+"/walls/spineWall6.png");
	public static Render spine7 = loadBitMap
			("resources/textures/theme"+(Display.themeNum + 1)
					+"/walls/spineWall7.png");
	public static Render spine8 = loadBitMap
			("resources/textures/theme"+(Display.themeNum + 1)
					+"/walls/spineWall8.png");
	
	public static Render mlg = loadBitMap
			("resources/textures/mlg.png");
	public static Render chargePack = loadBitMap
			("resources/textures/chargePack.png");
	public static Render largeChargePack = loadBitMap
			("resources/textures/chargePackLarge.png");
	public static Render phaseCannon = loadBitMap
			("resources/textures/phaseCannonWeapon.png");
	public static Render phaser = loadBitMap
			("resources/textures/phaser.png");
	public static Render box = loadBitMap
			("resources/textures/boxWall.png");
	public static Render woodenWall = loadBitMap
			("resources/textures/woodWall.png");
	public static Render bloodWall = loadBitMap
			("resources/textures/bloodWall.png");
	public static Render marble = loadBitMap
			("resources/textures/marble.png");
	public static Render sat1 = loadBitMap
			("resources/textures/SatDish1.png");
	public static Render sat2 = loadBitMap
			("resources/textures/SatDish2.png");
	public static Render normButton = loadBitMap
			("resources/textures/normButton.png");
	public static Render pistol = loadBitMap
			("resources/textures/pistol.png");
	public static Render clip = loadBitMap
			("resources/textures/clip.png");
	public static Render bullets = loadBitMap
			("resources/textures/boxOfBullets.png");
	public static Render vileCiv1 = loadBitMap
			("resources/textures/vileCivilian.png");
	public static Render vileCiv2 = loadBitMap
			("resources/textures/vileCivilian2.png");
	public static Render vileCiv3 = loadBitMap
			("resources/textures/vileCivilian3.png");
	public static Render vileCiv4 = loadBitMap
			("resources/textures/vileCivilian4.png");
	public static Render vileCivAttack1 = loadBitMap
			("resources/textures/vileCivilianAttack1.png");
	public static Render vileCivAttack2 = loadBitMap
			("resources/textures/vileCivilianAttack2.png");
	public static Render vileCivHurt = loadBitMap
			("resources/textures/vileCivilianHurt.png");
	public static Render belegoth = loadBitMap
			("resources/textures/belegoth.png");
	public static Render corpse1 = loadBitMap
			("resources/textures/corpse1.png");
	public static Render corpse2 = loadBitMap
			("resources/textures/corpse2.png");
	public static Render corpse3 = loadBitMap
			("resources/textures/corpse3.png");
	public static Render corpse4 = loadBitMap
			("resources/textures/corpse4.png");
	public static Render corpse5 = loadBitMap
			("resources/textures/corpse5.png");
	public static Render corpse6 = loadBitMap
			("resources/textures/corpse6.png");
	public static Render corpse7 = loadBitMap
			("resources/textures/corpse7.png");
	public static Render corpse8 = loadBitMap
			("resources/textures/corpse8.png");
	public static Render corpseType2 = loadBitMap
			("resources/textures/corpseType2.png");
	public static Render enemy4b = loadBitMap
			("resources/textures/enemy4-2.png");
	public static Render enemy3b = loadBitMap
			("resources/textures/enemy3-2.png");
	public static Render enemy3c = loadBitMap
			("resources/textures/enemy3-3.png");
	public static Render enemy3d = loadBitMap
			("resources/textures/enemy3-4.png");
	public static Render enemy3e = loadBitMap
			("resources/textures/enemy3-5.png");
	public static Render enemy3f = loadBitMap
			("resources/textures/enemy3-6.png");
	public static Render defaultFireball = loadBitMap
			("resources/textures/fireball.png");
	public static Render giantFireball = loadBitMap
			("resources/textures/giantFireball.png");
	public static Render electricShock = loadBitMap
			("resources/textures/electricShock.png");
	public static Render electroBall = loadBitMap
			("resources/textures/electroBall.png");
	public static Render enemy1b   = loadBitMap
			("resources/textures/enemy1-2.png");
	public static Render enemy1corpse1   = loadBitMap
			("resources/textures/enemy1corpse1.png");
	public static Render enemy1corpse2   = loadBitMap
			("resources/textures/enemy1corpse2.png");
	public static Render enemy1corpse3   = loadBitMap
			("resources/textures/enemy1corpse3.png");
	public static Render enemy1corpse   = loadBitMap
			("resources/textures/enemy1corpse.png");
	public static Render enemy1hurt   = loadBitMap
			("resources/textures/enemy1hurt.png");
	public static Render enemy1fire1   = loadBitMap
			("resources/textures/enemy1fire1.png");
	public static Render enemy1fire2   = loadBitMap
			("resources/textures/enemy1fire2.png");
	public static Render enemy1fire3   = loadBitMap
			("resources/textures/enemy1fire3.png");
	public static Render enemy1fire4   = loadBitMap
			("resources/textures/enemy1fire4.png");
	public static Render enemy5b   = loadBitMap
			("resources/textures/enemy5-2.png");
	public static Render enemy5corpse1   = loadBitMap
			("resources/textures/enemy5corpse1.png");
	public static Render enemy5corpse2   = loadBitMap
			("resources/textures/enemy5corpse2.png");
	public static Render enemy5corpse3   = loadBitMap
			("resources/textures/enemy5corpse3.png");
	public static Render enemy5corpse4   = loadBitMap
			("resources/textures/enemy5corpse4.png");
	public static Render enemy5corpse5   = loadBitMap
			("resources/textures/enemy5corpse5.png");
	public static Render enemy5corpse6   = loadBitMap
			("resources/textures/enemy5corpse6.png");
	public static Render enemy5corpse7   = loadBitMap
			("resources/textures/enemy5corpse7.png");
	public static Render enemy5corpse8   = loadBitMap
			("resources/textures/enemy5corpse8.png");
	public static Render enemy5corpse9   = loadBitMap
			("resources/textures/enemy5corpse9.png");
	public static Render enemy5corpse10   = loadBitMap
			("resources/textures/enemy5corpse10.png");
	public static Render enemy5corpse11   = loadBitMap
			("resources/textures/enemy5corpse11.png");
	public static Render enemy5corpse12   = loadBitMap
			("resources/textures/enemy5corpse12.png");
	public static Render enemy5corpse13   = loadBitMap
			("resources/textures/enemy5corpse13.png");
	public static Render enemy5corpse14   = loadBitMap
			("resources/textures/enemy5corpse14.png");
	public static Render enemy5corpse15   = loadBitMap
			("resources/textures/enemy5corpse15.png");
	public static Render enemy5corpse16   = loadBitMap
			("resources/textures/enemy5corpse16.png");
	public static Render enemy5corpse17   = loadBitMap
			("resources/textures/enemy5corpse17.png");
	public static Render enemy4corpse   = loadBitMap
			("resources/textures/enemy4corpse.png");
	public static Render enemy5c   = loadBitMap
			("resources/textures/enemy5-3.png");
	public static Render enemy5d   = loadBitMap
			("resources/textures/enemy5-4.png");
	public static Render enemy5corpse   = loadBitMap
			("resources/textures/enemy5corpse.png");
	public static Render enemy5fire1   = loadBitMap
			("resources/textures/enemy5fire1.png");
	public static Render enemy5fire2   = loadBitMap
			("resources/textures/enemy5fire2.png");
	public static Render enemy3corpse   = loadBitMap
			("resources/textures/enemy3corpse.png");
	public static Render enemy3fire1   = loadBitMap
			("resources/textures/enemy3fire1.png");
	public static Render enemy3fire2   = loadBitMap
			("resources/textures/enemy3fire2.png");
	public static Render enemy3fire3   = loadBitMap
			("resources/textures/enemy3fire3.png");
	public static Render enemy3hurt   = loadBitMap
			("resources/textures/enemy3hurt.png");
	public static Render enemy2hurt   = loadBitMap
			("resources/textures/enemy2hurt.png");
	public static Render enemy2corpse1   = loadBitMap
			("resources/textures/enemy2corpse1.png");
	public static Render enemy2corpse2   = loadBitMap
			("resources/textures/enemy2corpse2.png");
	public static Render enemy2corpse3   = loadBitMap
			("resources/textures/enemy2corpse3.png");
	public static Render enemy2corpse4   = loadBitMap
			("resources/textures/enemy2corpse4.png");
	public static Render enemy2corpse5   = loadBitMap
			("resources/textures/enemy2corpse5.png");
	public static Render enemy2corpse6   = loadBitMap
			("resources/textures/enemy2corpse6.png");
	public static Render enemy2corpse   = loadBitMap
			("resources/textures/enemy2corpse.png");
	public static Render enemy2fire1   = loadBitMap
			("resources/textures/enemy2fire.png");
	public static Render belegothCorpse1   = loadBitMap
			("resources/textures/belegothCorpse1.png");
	public static Render belegothCorpse2   = loadBitMap
			("resources/textures/belegothCorpse2.png");
	public static Render belegothCorpse3   = loadBitMap
			("resources/textures/belegothCorpse3.png");
	public static Render belegothCorpse4   = loadBitMap
			("resources/textures/belegothCorpse4.png");
	public static Render belegothCorpse5   = loadBitMap
			("resources/textures/belegothCorpse5.png");
	public static Render belegothCorpse6   = loadBitMap
			("resources/textures/belegothCorpse6.png");
	public static Render belegothCorpse7   = loadBitMap
			("resources/textures/belegothCorpse7.png");
	public static Render belegothCorpse8   = loadBitMap
			("resources/textures/belegothCorpse8.png");
	public static Render belegothCorpse9   = loadBitMap
			("resources/textures/belegothCorpse9.png");
	public static Render belegothCorpse10   = loadBitMap
			("resources/textures/belegothCorpse10.png");
	public static Render belegothCorpse11   = loadBitMap
			("resources/textures/belegothCorpse11.png");
	public static Render belegothCorpse12   = loadBitMap
			("resources/textures/belegothCorpse12.png");
	public static Render belegothCorpse13   = loadBitMap
			("resources/textures/belegothCorpse13.png");
	public static Render belegothCorpse   = loadBitMap
			("resources/textures/belegothCorpse14.png");
	public static Render belegothHurt   = loadBitMap
			("resources/textures/belegothHurt.png");
	public static Render belegothMelee   = loadBitMap
			("resources/textures/belegothMelee.png");
	public static Render belegoth2   = loadBitMap
			("resources/textures/belegoth2.png");
	public static Render belegoth3   = loadBitMap
			("resources/textures/belegoth3.png");
	public static Render belegoth4   = loadBitMap
			("resources/textures/belegoth4.png");
	public static Render belegoth5   = loadBitMap
			("resources/textures/belegoth5.png");
	public static Render belegoth6   = loadBitMap
			("resources/textures/belegoth6.png");
	public static Render belegothAttack1   = loadBitMap
			("resources/textures/belegothAttack1.png");
	public static Render belegothAttack2   = loadBitMap
			("resources/textures/belegothAttack2.png");
	public static Render belegothAttack3   = loadBitMap
			("resources/textures/belegothAttack3.png");
	public static Render belegothAttack4   = loadBitMap
			("resources/textures/belegothAttack4.png");
	public static Render morgothFire1   = loadBitMap
			("resources/textures/morgothFire1.png");
	public static Render morgothFire2   = loadBitMap
			("resources/textures/morgothFire2.png");
	public static Render morgothMelee   = loadBitMap
			("resources/textures/morgothMelee.png");
	public static Render morgoth2   = loadBitMap
			("resources/textures/morgoth2.png");
	public static Render morgoth3   = loadBitMap
			("resources/textures/morgoth3.png");
	public static Render morgoth4   = loadBitMap
			("resources/textures/morgoth4.png");
	public static Render morgothHurt   = loadBitMap
			("resources/textures/morgothHurt.png");
	public static Render enemy2left45   = loadBitMap
			("resources/textures/enemy2(45 left).png");
	public static Render enemy2left   = loadBitMap
			("resources/textures/enemy2(left).png");
	public static Render enemy2left135   = loadBitMap
			("resources/textures/enemy2(135 left).png");
	public static Render enemy2back   = loadBitMap
			("resources/textures/enemy2back.png");
	public static Render enemy2right45   = loadBitMap
			("resources/textures/enemy2(45 right).png");
	public static Render enemy2right   = loadBitMap
			("resources/textures/enemy2(right).png");
	public static Render enemy2right135   = loadBitMap
			("resources/textures/enemy2(135 right).png");
	public static Render enemy2left45fire   = loadBitMap
			("resources/textures/enemy2fire(45 left).png");
	public static Render enemy2left135fire   = loadBitMap
			("resources/textures/enemy2fire(135 left).png");
	public static Render enemy2backfire   = loadBitMap
			("resources/textures/enemy2fire(back).png");
	public static Render enemy2right45fire   = loadBitMap
			("resources/textures/enemy2fire(45 right).png");
	public static Render enemy2rightfire   = loadBitMap
			("resources/textures/enemy2fire(right).png");
	public static Render enemy2leftfire   = loadBitMap
			("resources/textures/enemy2fire(left).png");
	public static Render enemy2right135fire   = loadBitMap
			("resources/textures/enemy2fire(135 right).png");
	public static Render enemy1left45   = loadBitMap
			("resources/textures/enemy1(45 left).png");
	public static Render enemy1left   = loadBitMap
			("resources/textures/enemy1(left).png");
	public static Render enemy1left135   = loadBitMap
			("resources/textures/enemy1(135 left).png");
	public static Render enemy1back   = loadBitMap
			("resources/textures/enemy1(back).png");
	public static Render enemy1right45   = loadBitMap
			("resources/textures/enemy1(45 right).png");
	public static Render enemy1right   = loadBitMap
			("resources/textures/enemy1(right).png");
	public static Render enemy1right135   = loadBitMap
			("resources/textures/enemy1(135 right).png");
	public static Render enemy1left45fire1   = loadBitMap
			("resources/textures/enemy1fire1(45 left).png");
	public static Render enemy1left45fire2   = loadBitMap
			("resources/textures/enemy1fire2(45 left).png");
	public static Render enemy1left45fire3   = loadBitMap
			("resources/textures/enemy1fire3(45 left).png");
	public static Render enemy1left45fire4   = loadBitMap
			("resources/textures/enemy1fire4(45 left).png");
	public static Render enemy1leftfire   = loadBitMap
			("resources/textures/enemy1fire(left).png");
	public static Render enemy1right45fire1   = loadBitMap
			("resources/textures/enemy1fire1(45 right).png");
	public static Render enemy1right45fire2   = loadBitMap
			("resources/textures/enemy1fire2(45 right).png");
	public static Render enemy1right45fire3   = loadBitMap
			("resources/textures/enemy1fire3(45 right).png");
	public static Render enemy1right45fire4   = loadBitMap
			("resources/textures/enemy1fire4(45 right).png");
	public static Render enemy1rightfire   = loadBitMap
			("resources/textures/enemy1fire(right).png");
	public static Render enemy1right45hurt   = loadBitMap
			("resources/textures/enemy1hurt(45 right).png");
	public static Render enemy1righthurt   = loadBitMap
			("resources/textures/enemy1hurt(right).png");
	public static Render enemy1left45hurt   = loadBitMap
			("resources/textures/enemy1hurt(45 left).png");
	public static Render enemy1lefthurt   = loadBitMap
			("resources/textures/enemy1hurt(left).png");
	public static Render vileCivilian145left   = loadBitMap
			("resources/textures/vileCivilian1(45 left).png");
	public static Render vileCivilian245left   = loadBitMap
			("resources/textures/vileCivilian2(45 left).png");
	public static Render vileCivilian345left   = loadBitMap
			("resources/textures/vileCivilian3(45 left).png");
	public static Render vileCivilian445left   = loadBitMap
			("resources/textures/vileCivilian4(45 left).png");
	public static Render vileCivilian1left   = loadBitMap
			("resources/textures/vileCivilian1(left).png");
	public static Render vileCivilian2left   = loadBitMap
			("resources/textures/vileCivilian2(left).png");
	public static Render vileCivilian3left   = loadBitMap
			("resources/textures/vileCivilian3(left).png");
	public static Render vileCivilian4left   = loadBitMap
			("resources/textures/vileCivilian4(left).png");
	public static Render vileCivilian1135left   = loadBitMap
			("resources/textures/vileCivilian1(135 left).png");
	public static Render vileCivilian2135left   = loadBitMap
			("resources/textures/vileCivilian2(135 left).png");
	public static Render vileCivilian3135left   = loadBitMap
			("resources/textures/vileCivilian3(135 left).png");
	public static Render vileCivilian4135left   = loadBitMap
			("resources/textures/vileCivilian4(135 left).png");
	public static Render vileCivilian1back   = loadBitMap
			("resources/textures/vileCivilian1(back).png");
	public static Render vileCivilian2back   = loadBitMap
			("resources/textures/vileCivilian2(back).png");
	public static Render vileCivilian3back   = loadBitMap
			("resources/textures/vileCivilian3(back).png");
	public static Render vileCivilian4back   = loadBitMap
			("resources/textures/vileCivilian4(back).png");
	public static Render vileCivilian145right   = loadBitMap
			("resources/textures/vileCivilian1(45 right).png");
	public static Render vileCivilian245right   = loadBitMap
			("resources/textures/vileCivilian2(45 right).png");
	public static Render vileCivilian345right   = loadBitMap
			("resources/textures/vileCivilian3(45 right).png");
	public static Render vileCivilian445right   = loadBitMap
			("resources/textures/vileCivilian4(45 right).png");
	public static Render vileCivilian1right   = loadBitMap
			("resources/textures/vileCivilian1(right).png");
	public static Render vileCivilian2right   = loadBitMap
			("resources/textures/vileCivilian2(right).png");
	public static Render vileCivilian3right   = loadBitMap
			("resources/textures/vileCivilian3(right).png");
	public static Render vileCivilian4right   = loadBitMap
			("resources/textures/vileCivilian4(right).png");
	public static Render vileCivilian1135right   = loadBitMap
			("resources/textures/vileCivilian1(135 right).png");
	public static Render vileCivilian2135right   = loadBitMap
			("resources/textures/vileCivilian2(135 right).png");
	public static Render vileCivilian3135right   = loadBitMap
			("resources/textures/vileCivilian3(135 right).png");
	public static Render vileCivilian4135right   = loadBitMap
			("resources/textures/vileCivilian4(135 right).png");
	public static Render vileCivilianright45hurt   = loadBitMap
			("resources/textures/vileCivilianhurt(45 right).png");
	public static Render vileCivilianrighthurt   = loadBitMap
			("resources/textures/vileCivilianhurt(right).png");
	public static Render vileCivilianleft45hurt   = loadBitMap
			("resources/textures/vileCivilianhurt(45 left).png");
	public static Render vileCivilianlefthurt   = loadBitMap
			("resources/textures/vileCivilianhurt(left).png");
	public static Render vileCivilianright135hurt   = loadBitMap
			("resources/textures/vileCivilianhurt(135 right).png");
	public static Render vileCivilianleft135hurt   = loadBitMap
			("resources/textures/vileCivilianhurt(135 left).png");
	public static Render vileCivilianbackhurt   = loadBitMap
			("resources/textures/vileCivilianhurt(back).png");
	public static Render vileCivilianAttack145left   = loadBitMap
			("resources/textures/vileCivilianAttack1(45 left).png");
	public static Render vileCivilianAttack245left   = loadBitMap
			("resources/textures/vileCivilianAttack2(45 left).png");
	public static Render vileCivilianAttack1left   = loadBitMap
			("resources/textures/vileCivilianAttack1(left).png");
	public static Render vileCivilianAttack2left   = loadBitMap
			("resources/textures/vileCivilianAttack2(left).png");
	public static Render vileCivilianAttack1135left   = loadBitMap
			("resources/textures/vileCivilianAttack1(135 left).png");
	public static Render vileCivilianAttack2135left   = loadBitMap
			("resources/textures/vileCivilianAttack2(135 left).png");
	public static Render vileCivilianAttack1back   = loadBitMap
			("resources/textures/vileCivilianAttack1(back).png");
	public static Render vileCivilianAttack2back   = loadBitMap
			("resources/textures/vileCivilianAttack2(back).png");
	public static Render vileCivilianAttack145right   = loadBitMap
			("resources/textures/vileCivilianAttack1(45 right).png");
	public static Render vileCivilianAttack245right   = loadBitMap
			("resources/textures/vileCivilianAttack2(45 right).png");
	public static Render vileCivilianAttack1right   = loadBitMap
			("resources/textures/vileCivilianAttack1(right).png");
	public static Render vileCivilianAttack2right   = loadBitMap
			("resources/textures/vileCivilianAttack2(right).png");
	public static Render vileCivilianAttack1135right   = loadBitMap
			("resources/textures/vileCivilianAttack1(135 right).png");
	public static Render vileCivilianAttack2135right   = loadBitMap
			("resources/textures/vileCivilianAttack2(135 right).png");
	public static Render enemy4corpse1   = loadBitMap
			("resources/textures/enemy4corpse1.png");
	public static Render enemy4corpse2   = loadBitMap
			("resources/textures/enemy4corpse2.png");
	public static Render enemy4corpse3   = loadBitMap
			("resources/textures/enemy4corpse3.png");
	public static Render enemy5hurt   = loadBitMap
			("resources/textures/enemy5hurt.png");
	public static Render enemy5a45left = loadBitMap
			("resources/textures/enemy5-1(45 left).png");
	public static Render enemy5b45left = loadBitMap
			("resources/textures/enemy5-2(45 left).png");
	public static Render enemy5c45left = loadBitMap
			("resources/textures/enemy5-3(45 left).png");
	public static Render enemy5d45left = loadBitMap
			("resources/textures/enemy5-4(45 left).png");
	public static Render enemy5firea45left = loadBitMap
			("resources/textures/enemy5fire1(45 left).png");
	public static Render enemy5fireb45left = loadBitMap
			("resources/textures/enemy5fire2(45 left).png");
	public static Render enemy5hurt45left = loadBitMap
			("resources/textures/enemy5hurt(45 left).png");
	public static Render enemy5aleft = loadBitMap
			("resources/textures/enemy5-1(left).png");
	public static Render enemy5bleft = loadBitMap
			("resources/textures/enemy5-2(left).png");
	public static Render enemy5cleft = loadBitMap
			("resources/textures/enemy5-3(left).png");
	public static Render enemy5dleft = loadBitMap
			("resources/textures/enemy5-4(left).png");
	public static Render enemy5firealeft = loadBitMap
			("resources/textures/enemy5fire1(left).png");
	public static Render enemy5firebleft = loadBitMap
			("resources/textures/enemy5fire2(left).png");
	public static Render enemy5hurtleft = loadBitMap
			("resources/textures/enemy5hurt(left).png");
	public static Render enemy5a135left = loadBitMap
			("resources/textures/enemy5-1(135 left).png");
	public static Render enemy5b135left = loadBitMap
			("resources/textures/enemy5-2(135 left).png");
	public static Render enemy5c135left = loadBitMap
			("resources/textures/enemy5-3(135 left).png");
	public static Render enemy5d135left = loadBitMap
			("resources/textures/enemy5-4(135 left).png");
	public static Render enemy5firea135left = loadBitMap
			("resources/textures/enemy5fire1(135 left).png");
	public static Render enemy5fireb135left = loadBitMap
			("resources/textures/enemy5fire2(135 left).png");
	public static Render enemy5hurt135left = loadBitMap
			("resources/textures/enemy5hurt(135 left).png");
	public static Render enemy5aback = loadBitMap
			("resources/textures/enemy5-1(back).png");
	public static Render enemy5bback = loadBitMap
			("resources/textures/enemy5-2(back).png");
	public static Render enemy5cback = loadBitMap
			("resources/textures/enemy5-3(back).png");
	public static Render enemy5dback = loadBitMap
			("resources/textures/enemy5-4(back).png");
	public static Render enemy5fireaback = loadBitMap
			("resources/textures/enemy5fire1(back).png");
	public static Render enemy5firebback = loadBitMap
			("resources/textures/enemy5fire2(back).png");
	public static Render enemy5hurtback = loadBitMap
			("resources/textures/enemy5hurt(back).png");
	public static Render enemy5a45right = loadBitMap
			("resources/textures/enemy5-1(45 right).png");
	public static Render enemy5b45right = loadBitMap
			("resources/textures/enemy5-2(45 right).png");
	public static Render enemy5c45right = loadBitMap
			("resources/textures/enemy5-3(45 right).png");
	public static Render enemy5d45right = loadBitMap
			("resources/textures/enemy5-4(45 right).png");
	public static Render enemy5firea45right = loadBitMap
			("resources/textures/enemy5fire1(45 right).png");
	public static Render enemy5fireb45right = loadBitMap
			("resources/textures/enemy5fire2(45 right).png");
	public static Render enemy5hurt45right = loadBitMap
			("resources/textures/enemy5hurt(45 right).png");
	public static Render enemy5aright = loadBitMap
			("resources/textures/enemy5-1(right).png");
	public static Render enemy5bright = loadBitMap
			("resources/textures/enemy5-2(right).png");
	public static Render enemy5cright = loadBitMap
			("resources/textures/enemy5-3(right).png");
	public static Render enemy5dright = loadBitMap
			("resources/textures/enemy5-4(right).png");
	public static Render enemy5firearight = loadBitMap
			("resources/textures/enemy5fire1(right).png");
	public static Render enemy5firebright = loadBitMap
			("resources/textures/enemy5fire2(right).png");
	public static Render enemy5hurtright = loadBitMap
			("resources/textures/enemy5hurt(right).png");
	public static Render enemy5a135right = loadBitMap
			("resources/textures/enemy5-1(135 right).png");
	public static Render enemy5b135right = loadBitMap
			("resources/textures/enemy5-2(135 right).png");
	public static Render enemy5c135right = loadBitMap
			("resources/textures/enemy5-3(135 right).png");
	public static Render enemy5d135right = loadBitMap
			("resources/textures/enemy5-4(135 right).png");
	public static Render enemy5firea135right = loadBitMap
			("resources/textures/enemy5fire1(135 right).png");
	public static Render enemy5fireb135right = loadBitMap
			("resources/textures/enemy5fire2(135 right).png");
	public static Render enemy5hurt135right = loadBitMap
			("resources/textures/enemy5hurt(135 right).png");
	public static Render enemy3corpse1   = loadBitMap
			("resources/textures/enemy3corpse1.png");
	public static Render enemy3corpse2   = loadBitMap
			("resources/textures/enemy3corpse2.png");
	public static Render enemy3corpse3   = loadBitMap
			("resources/textures/enemy3corpse3.png");
	public static Render enemy3corpse4   = loadBitMap
			("resources/textures/enemy3corpse4.png");
	public static Render enemy3corpse5   = loadBitMap
			("resources/textures/enemy3corpse5.png");
	public static Render enemy3corpse6   = loadBitMap
			("resources/textures/enemy3corpse6.png");
	public static Render enemy3corpse7   = loadBitMap
			("resources/textures/enemy3corpse7.png");
	public static Render enemy3corpse8   = loadBitMap
			("resources/textures/enemy3corpse8.png");
	public static Render enemy3corpse9   = loadBitMap
			("resources/textures/enemy3corpse9.png");
	public static Render enemy3a45left = loadBitMap
			("resources/textures/enemy3-1(45 left).png");
	public static Render enemy3b45left = loadBitMap
			("resources/textures/enemy3-2(45 left).png");
	public static Render enemy3c45left = loadBitMap
			("resources/textures/enemy3-3(45 left).png");
	public static Render enemy3d45left = loadBitMap
			("resources/textures/enemy3-4(45 left).png");
	public static Render enemy3e45left = loadBitMap
			("resources/textures/enemy3-5(45 left).png");
	public static Render enemy3f45left = loadBitMap
			("resources/textures/enemy3-6(45 left).png");
	public static Render enemy3fire1left45 = loadBitMap
			("resources/textures/enemy3fire1(45 left).png");
	public static Render enemy3fire2left45 = loadBitMap
			("resources/textures/enemy3fire2(45 left).png");
	public static Render enemy3fire3left45 = loadBitMap
			("resources/textures/enemy3fire3(45 left).png");
	public static Render enemy3hurt45left = loadBitMap
			("resources/textures/enemy3hurt(45 left).png");
	public static Render enemy3a45right = loadBitMap
			("resources/textures/enemy3-1(45 right).png");
	public static Render enemy3b45right = loadBitMap
			("resources/textures/enemy3-2(45 right).png");
	public static Render enemy3c45right = loadBitMap
			("resources/textures/enemy3-3(45 right).png");
	public static Render enemy3d45right = loadBitMap
			("resources/textures/enemy3-4(45 right).png");
	public static Render enemy3e45right = loadBitMap
			("resources/textures/enemy3-5(45 right).png");
	public static Render enemy3f45right = loadBitMap
			("resources/textures/enemy3-6(45 right).png");
	public static Render enemy3fire1right45 = loadBitMap
			("resources/textures/enemy3fire1(45 right).png");
	public static Render enemy3fire2right45 = loadBitMap
			("resources/textures/enemy3fire2(45 right).png");
	public static Render enemy3fire3right45 = loadBitMap
			("resources/textures/enemy3fire3(45 right).png");
	public static Render enemy3hurt45right = loadBitMap
			("resources/textures/enemy3hurt(45 right).png");
	public static Render enemy3aleft = loadBitMap
			("resources/textures/enemy3-1(left).png");
	public static Render enemy3bleft = loadBitMap
			("resources/textures/enemy3-2(left).png");
	public static Render enemy3cleft = loadBitMap
			("resources/textures/enemy3-3(left).png");
	public static Render enemy3dleft = loadBitMap
			("resources/textures/enemy3-4(left).png");
	public static Render enemy3eleft = loadBitMap
			("resources/textures/enemy3-5(left).png");
	public static Render enemy3fleft = loadBitMap
			("resources/textures/enemy3-6(left).png");
	public static Render enemy3fire1left= loadBitMap
			("resources/textures/enemy3fire1(left).png");
	public static Render enemy3fire2left= loadBitMap
			("resources/textures/enemy3fire2(left).png");
	public static Render enemy3fire3left= loadBitMap
			("resources/textures/enemy3fire3(left).png");
	public static Render enemy3hurtleft = loadBitMap
			("resources/textures/enemy3hurt(left).png");
	public static Render enemy3aright = loadBitMap
			("resources/textures/enemy3-1(right).png");
	public static Render enemy3bright = loadBitMap
			("resources/textures/enemy3-2(right).png");
	public static Render enemy3cright = loadBitMap
			("resources/textures/enemy3-3(right).png");
	public static Render enemy3dright = loadBitMap
			("resources/textures/enemy3-4(right).png");
	public static Render enemy3eright = loadBitMap
			("resources/textures/enemy3-5(right).png");
	public static Render enemy3fright = loadBitMap
			("resources/textures/enemy3-6(right).png");
	public static Render enemy3fire1right= loadBitMap
			("resources/textures/enemy3fire1(right).png");
	public static Render enemy3fire2right= loadBitMap
			("resources/textures/enemy3fire2(right).png");
	public static Render enemy3fire3right= loadBitMap
			("resources/textures/enemy3fire3(right).png");
	public static Render enemy3hurtright = loadBitMap
			("resources/textures/enemy3hurt(right).png");
	public static Render enemy3a135right = loadBitMap
			("resources/textures/enemy3-1(135 right).png");
	public static Render enemy3b135right = loadBitMap
			("resources/textures/enemy3-2(135 right).png");
	public static Render enemy3c135right = loadBitMap
			("resources/textures/enemy3-3(135 right).png");
	public static Render enemy3d135right = loadBitMap
			("resources/textures/enemy3-4(135 right).png");
	public static Render enemy3e135right = loadBitMap
			("resources/textures/enemy3-5(135 right).png");
	public static Render enemy3f135right = loadBitMap
			("resources/textures/enemy3-6(135 right).png");
	public static Render enemy3fire1right135 = loadBitMap
			("resources/textures/enemy3fire1(135 right).png");
	public static Render enemy3fire2right135 = loadBitMap
			("resources/textures/enemy3fire2(135 right).png");
	public static Render enemy3fire3right135 = loadBitMap
			("resources/textures/enemy3fire3(135 right).png");
	public static Render enemy3hurt135right = loadBitMap
			("resources/textures/enemy3hurt(135 right).png");
	public static Render enemy3a135left = loadBitMap
			("resources/textures/enemy3-1(135 left).png");
	public static Render enemy3b135left = loadBitMap
			("resources/textures/enemy3-2(135 left).png");
	public static Render enemy3c135left = loadBitMap
			("resources/textures/enemy3-3(135 left).png");
	public static Render enemy3d135left = loadBitMap
			("resources/textures/enemy3-4(135 left).png");
	public static Render enemy3e135left = loadBitMap
			("resources/textures/enemy3-5(135 left).png");
	public static Render enemy3f135left = loadBitMap
			("resources/textures/enemy3-6(135 left).png");
	public static Render enemy3fire1left135 = loadBitMap
			("resources/textures/enemy3fire1(135 left).png");
	public static Render enemy3fire2left135 = loadBitMap
			("resources/textures/enemy3fire2(135 left).png");
	public static Render enemy3fire3left135 = loadBitMap
			("resources/textures/enemy3fire3(135 left).png");
	public static Render enemy3hurt135left = loadBitMap
			("resources/textures/enemy3hurt(135 left).png");
	public static Render enemy3aback = loadBitMap
			("resources/textures/enemy3-1(back).png");
	public static Render enemy3bback = loadBitMap
			("resources/textures/enemy3-2(back).png");
	public static Render enemy3cback = loadBitMap
			("resources/textures/enemy3-3(back).png");
	public static Render enemy3dback = loadBitMap
			("resources/textures/enemy3-4(back).png");
	public static Render enemy3eback = loadBitMap
			("resources/textures/enemy3-5(back).png");
	public static Render enemy3fback = loadBitMap
			("resources/textures/enemy3-6(back).png");
	public static Render enemy3fire1back= loadBitMap
			("resources/textures/enemy3fire1(back).png");
	public static Render enemy3fire2back= loadBitMap
			("resources/textures/enemy3fire2(back).png");
	public static Render enemy3fire3back= loadBitMap
			("resources/textures/enemy3fire3(back).png");
	public static Render enemy3hurtback = loadBitMap
			("resources/textures/enemy3hurt(back).png");
	public static Render rockets = loadBitMap
			("resources/textures/rockets.png");
	public static Render rocketLaucher = loadBitMap
			("resources/textures/rocketLauncher.png");
	public static Render rocketCrate = loadBitMap
			("resources/textures/rocketCrate.png");
	public static Render rocket = loadBitMap
			("resources/textures/rocket.png");
	public static Render explosion1 = loadBitMap
			("resources/textures/explosion1.png");
	public static Render explosion2 = loadBitMap
			("resources/textures/explosion2.png");
	public static Render explosion3 = loadBitMap
			("resources/textures/explosion3.png");
	public static Render explosion4 = loadBitMap
			("resources/textures/explosion4.png");
	public static Render explosion5 = loadBitMap
			("resources/textures/explosion5.png");
	public static Render explosion6 = loadBitMap
			("resources/textures/explosion6.png");
	public static Render explosion7 = loadBitMap
			("resources/textures/explosion7.png");
	public static Render explosion8 = loadBitMap
			("resources/textures/explosion8.png");
	public static Render defaultCorpse1 = loadBitMap
			("resources/textures/defaultCorpse1.png");
	public static Render defaultCorpse2 = loadBitMap
			("resources/textures/defaultCorpse2.png");
	public static Render coolWall = loadBitMap
			("resources/textures/theme"+(Display.themeNum + 1)
					+"/walls/coolWall.png");
	public static Render phaseHit1 = loadBitMap
			("resources/textures/phaseHit1.png");
	public static Render phaseHit2 = loadBitMap
			("resources/textures/phaseHit2.png");
	public static Render phaseHit3 = loadBitMap
			("resources/textures/phaseHit3.png");
	public static Render phaseHit4 = loadBitMap
			("resources/textures/phaseHit4.png");
	public static Render phaseHit5 = loadBitMap
			("resources/textures/phaseHit5.png");
	public static Render fireHit1 = loadBitMap
			("resources/textures/fireHit1.png");
	public static Render fireHit2 = loadBitMap
			("resources/textures/fireHit2.png");
	public static Render fireHit3 = loadBitMap
			("resources/textures/fireHit3.png");
	public static Render fireHit4 = loadBitMap
			("resources/textures/fireHit4.png");
	public static Render bulletHit1 = loadBitMap
			("resources/textures/bulletHit1.png");
	public static Render bulletHit2 = loadBitMap
			("resources/textures/bulletHit2.png");
	public static Render bulletHit3 = loadBitMap
			("resources/textures/bulletHit3.png");
	public static Render bulletHit4 = loadBitMap
			("resources/textures/bulletHit4.png");
	public static Render bloodSpray1 = loadBitMap
			("resources/textures/bloodSpray1.png");
	public static Render bloodSpray2 = loadBitMap
			("resources/textures/bloodSpray2.png");
	public static Render bloodSpray3 = loadBitMap
			("resources/textures/bloodSpray3.png");
	public static Render bloodSpray4 = loadBitMap
			("resources/textures/bloodSpray4.png");
	
   /**
    * Constructs floor textures since they require a bit more optimized
    * version of instantiation
    */
	public static void Textures()
	{
		try
		{
			//Array of floor textures
			floors = new Render[(int)Files.list(Paths.get
					("resources/textures/floors")).count()];
			
			//Adds all floor textures in order in the array
			for(int i = 0; i < floors.length; i++)
			{
				floors[i] = loadBitMap
						("resources/textures/floors/floor"
								+(i + 1)+".png");
			}
		}
		catch(Exception e)
		{
			//blah
		}
	}
	
   /**
    * Finds the texture called for in the file, then creates a Render 
    * object out of it so that it renders on the screen.
    * @param fileName
    * @return
    */
	public static Render loadBitMap(String fileName)
	{
		try
		{
			BufferedImage image = ImageIO.read(new File(fileName));
			
			int width = image.getWidth();
			int height = image.getHeight();
		
			Render result = new Render(width, height);
		
			image.getRGB(0, 0, width, height, result.PIXELS, 0, width);
		
			return result;
		}
		catch (Exception e)
		{
			System.out.println(fileName);
			throw new RuntimeException(e);
		}
	}
	
   /**
    * Resets the textures for each game so that textures do not stay
    * the same from theme to theme.
    */
	public static void resetTextures()
	{
		enemy1   = loadBitMap
				("resources/textures/enemy1.png");
		enemy2   = loadBitMap
				("resources/textures/enemy2.png");
		enemy3   = loadBitMap
				("resources/textures/enemy3.png");
		enemy4   = loadBitMap
				("resources/textures/enemy4.png");
		wall1 = loadBitMap
				("resources/textures/theme"+(Display.themeNum + 1)
						+"/walls/wall1.png");
		wall2 = loadBitMap
				("resources/textures/theme"+(Display.themeNum + 1)
						+"/walls/wall2.png");
		wall3 = loadBitMap
				("resources/textures/theme"+(Display.themeNum + 1)
						+"/walls/wall3.png");
		wall4 = loadBitMap
				("resources/textures/theme"+(Display.themeNum + 1)
						+"/walls/wall4.png");
		wall4damaged1 = loadBitMap
				("resources/textures/theme"+3
						+"/walls/wall4damage1.png");
		wall4damaged2 = loadBitMap
				("resources/textures/theme"+3
						+"/walls/wall4damage2.png");
		wall4damaged3 = loadBitMap
				("resources/textures/theme"+3
						+"/walls/wall4damage3.png");
		wall5 = loadBitMap
				("resources/textures/theme"+(Display.themeNum + 1)
						+"/walls/wall5.png");
		wall6 = loadBitMap
				("resources/textures/theme"+(Display.themeNum + 1)
						+"/walls/wall6.png");
		wall7 = loadBitMap
				("resources/textures/theme"+(Display.themeNum + 1)
						+"/walls/wall7.png");
		wall8 = loadBitMap
				("resources/textures/theme"+(Display.themeNum + 1)
						+"/walls/wall8.png");
		wall9 = loadBitMap
				("resources/textures/theme"+(Display.themeNum + 1)
						+"/walls/wall9.png");
		wall10 = loadBitMap
				("resources/textures/theme"+(Display.themeNum + 1)
						+"/walls/wall10.png");
		wall11 = loadBitMap
				("resources/textures/theme"+(Display.themeNum + 1)
						+"/walls/wall11.png");
		wall12 = loadBitMap
				("resources/textures/theme"+(Display.themeNum + 1)
						+"/walls/wall12.png");
		wall13Phase1 = loadBitMap
				("resources/textures/theme"+(Display.themeNum + 1)
						+"/walls/wall13phase1.png");
		wall13Phase2 = loadBitMap
				("resources/textures/theme"+(Display.themeNum + 1)
						+"/walls/wall13phase2.png");
		wall14 = loadBitMap
				("resources/textures/theme"+(Display.themeNum + 1)
						+"/walls/wall14.png");
		teleportEnter = loadBitMap
				("resources/textures/teleporterEnter.png");
		teleportExit = loadBitMap
				("resources/textures/teleporterExit.png");
		tutorialWall = loadBitMap
				("resources/textures/tutorialWall.png");
		tutorialWall2 = loadBitMap
				("resources/textures/tutorialWall2.png");
		tutorialWall3 = loadBitMap
				("resources/textures/tutorialWall3.png");
		tutorialWall4 = loadBitMap
				("resources/textures/tutorialWall4.png");
		tutorialWall5 = loadBitMap
				("resources/textures/tutorialWall5.png");
		wall15Phase5 = loadBitMap
				("resources/textures/theme"+(Display.themeNum + 1)
						+"/walls/wall15phase5.png");
		wall15Phase4 = loadBitMap
				("resources/textures/theme"+(Display.themeNum + 1)
						+"/walls/wall15phase4.png");
		wall15Phase3 = loadBitMap
				("resources/textures/theme"+(Display.themeNum + 1)
						+"/walls/wall15phase3.png");
		wall15Phase2 = loadBitMap
				("resources/textures/theme"+(Display.themeNum + 1)
						+"/walls/wall15phase2.png");
		wall15Phase1 = loadBitMap
				("resources/textures/theme"+(Display.themeNum + 1)
						+"/walls/wall15phase1.png");
		health  = loadBitMap
				("resources/textures/health.png");
		shell   = loadBitMap
				("resources/textures/shell.png");
		bullet  = loadBitMap
				("resources/textures/bullet.png");
		megaPhase1   = loadBitMap
				("resources/textures/megahealthphase1.png");
		megaPhase2   = loadBitMap
				("resources/textures/megahealthphase2.png");
		megaPhase3   = loadBitMap
				("resources/textures/megahealthphase3.png");
		megaPhase4   = loadBitMap
				("resources/textures/megahealthphase4.png");
		redKey = loadBitMap
				("resources/textures/redKey.png");
		blueKey = loadBitMap
				("resources/textures/blueKey.png");
		greenKey = loadBitMap
				("resources/textures/greenKey.png");
		yellowKey = loadBitMap
				("resources/textures/yellowKey.png");
		shotgun  = loadBitMap
				("resources/textures/shotgun.png");
		
		toxic1 = loadBitMap
				("resources/textures/theme"+(Display.themeNum + 1)
						+"/walls/toxicWaste16.png");
		toxic2 = loadBitMap
				("resources/textures/theme"+(Display.themeNum + 1)
						+"/walls/toxicWaste15.png");
		toxic3 = loadBitMap
				("resources/textures/theme"+(Display.themeNum + 1)
						+"/walls/toxicWaste14.png");
		toxic4 = loadBitMap
				("resources/textures/theme"+(Display.themeNum + 1)
						+"/walls/toxicWaste13.png");
		toxic5 = loadBitMap
				("resources/textures/theme"+(Display.themeNum + 1)
						+"/walls/toxicWaste12.png");
		toxic6 = loadBitMap
				("resources/textures/theme"+(Display.themeNum + 1)
						+"/walls/toxicWaste11.png");
		toxic7 = loadBitMap
				("resources/textures/theme"+(Display.themeNum + 1)
						+"/walls/toxicWaste10.png");
		toxic8 = loadBitMap
				("resources/textures/theme"+(Display.themeNum + 1)
						+"/walls/toxicWaste9.png");
		toxic9 = loadBitMap
				("resources/textures/theme"+(Display.themeNum + 1)
						+"/walls/toxicWaste8.png");
		toxic10 = loadBitMap
				("resources/textures/theme"+(Display.themeNum + 1)
						+"/walls/toxicWaste7.png");
		toxic11 = loadBitMap
				("resources/textures/theme"+(Display.themeNum + 1)
						+"/walls/toxicWaste6.png");
		toxic12 = loadBitMap
				("resources/textures/theme"+(Display.themeNum + 1)
						+"/walls/toxicWaste5.png");
		toxic13 = loadBitMap
				("resources/textures/theme"+(Display.themeNum + 1)
						+"/walls/toxicWaste4.png");
		toxic14 = loadBitMap
				("resources/textures/theme"+(Display.themeNum + 1)
						+"/walls/toxicWaste3.png");
		toxic15 = loadBitMap
				("resources/textures/theme"+(Display.themeNum + 1)
						+"/walls/toxicWaste2.png");
		toxic16 = loadBitMap
				("resources/textures/theme"+(Display.themeNum + 1)
						+"/walls/toxicWaste1.png");
		
		resurrect1  = loadBitMap
				("resources/textures/resurrectSkull1.png");
		resurrect2  = loadBitMap
				("resources/textures/resurrectSkull2.png");
		resurrect3  = loadBitMap
				("resources/textures/resurrectSkull3.png");
		environSuit  = loadBitMap
			("resources/textures/environSuit.png");
		goblet1  = loadBitMap
				("resources/textures/goblet1.png");
		goblet2  = loadBitMap
				("resources/textures/goblet2.png");
		goblet3  = loadBitMap
				("resources/textures/goblet3.png");
		adrenaline  = loadBitMap
				("resources/textures/adrenaline.png");
		glasses  = loadBitMap
				("resources/textures/glasses.png");
		torch1   = loadBitMap
				("resources/textures/torch1.png");
		torch2   = loadBitMap
				("resources/textures/torch2.png");
		torch3   = loadBitMap
				("resources/textures/torch3.png");
		torch4   = loadBitMap
				("resources/textures/torch4.png");
		lamp1   = loadBitMap
				("resources/textures/lamp1.png");
		lamp2   = loadBitMap
				("resources/textures/lamp2.png");
		lamp3   = loadBitMap
				("resources/textures/lamp3.png");
		tree   = loadBitMap
				("resources/textures/tree.png");
		canister   = loadBitMap
				("resources/textures/canister.png");
		canExplode1   = loadBitMap
				("resources/textures/canExplode1.png");
		canExplode2   = loadBitMap
				("resources/textures/canExplode2.png");
		canExplode3   = loadBitMap
				("resources/textures/canExplode3.png");
		canExplode4   = loadBitMap
				("resources/textures/canExplode4.png");
		combat   = loadBitMap
				("resources/textures/combatArmor.png");
		argent   = loadBitMap
				("resources/textures/argentArmor.png");
		shard   = loadBitMap
				("resources/textures/armorShard.png");
		vial   = loadBitMap
				("resources/textures/healthVial.png");
		upgrade1   = loadBitMap
				("resources/textures/weaponUpgrade1.png");
		upgrade2   = loadBitMap
				("resources/textures/weaponUpgrade2.png");
		upgrade3   = loadBitMap
				("resources/textures/weaponUpgrade3.png");
		upgrade4   = loadBitMap
				("resources/textures/weaponUpgrade4.png");
		holyWater1   = loadBitMap
				("resources/textures/holyWater1.png");
		holyWater2   = loadBitMap
				("resources/textures/holyWater2.png");
		scepter   = loadBitMap
				("resources/textures/decietScepter.png");
		invisEmerald   = loadBitMap
				("resources/textures/invisibilityEmerald.png");
		invisEmerald2   = loadBitMap
				("resources/textures/invisibilityEmerald2.png");
		invisEmerald3   = loadBitMap
				("resources/textures/invisibilityEmerald3.png");
		invisEmerald4   = loadBitMap
				("resources/textures/invisibilityEmerald4.png");
		table   = loadBitMap
				("resources/textures/table.png");
		lampTable = loadBitMap
				("resources/textures/lampTable.png");
		enemy5   = loadBitMap
				("resources/textures/enemy5.png");
		morgoth   = loadBitMap
				("resources/textures/morgoth.png");
		shellBox = loadBitMap
				("resources/textures/shellBox.png");
		spine1 = loadBitMap
				("resources/textures/theme"+(Display.themeNum + 1)
						+"/walls/spineWall1.png");
		spine2 = loadBitMap
				("resources/textures/theme"+(Display.themeNum + 1)
						+"/walls/spineWall2.png");
		spine3 = loadBitMap
				("resources/textures/theme"+(Display.themeNum + 1)
						+"/walls/spineWall3.png");
		spine4 = loadBitMap
				("resources/textures/theme"+(Display.themeNum + 1)
						+"/walls/spineWall4.png");
		spine5 = loadBitMap
				("resources/textures/theme"+(Display.themeNum + 1)
						+"/walls/spineWall5.png");
		spine6 = loadBitMap
				("resources/textures/theme"+(Display.themeNum + 1)
						+"/walls/spineWall6.png");
		spine7 = loadBitMap
				("resources/textures/theme"+(Display.themeNum + 1)
						+"/walls/spineWall7.png");
		spine8 = loadBitMap
				("resources/textures/theme"+(Display.themeNum + 1)
						+"/walls/spineWall8.png");
		
		mlg = loadBitMap
				("resources/textures/mlg.png");
		chargePack = loadBitMap
				("resources/textures/chargePack.png");
		largeChargePack = loadBitMap
				("resources/textures/chargePackLarge.png");
		phaseCannon = loadBitMap
				("resources/textures/phaseCannonWeapon.png");
		phaser = loadBitMap
				("resources/textures/phaser.png");
		box = loadBitMap
				("resources/textures/boxWall.png");
		woodenWall = loadBitMap
				("resources/textures/woodWall.png");
		bloodWall = loadBitMap
				("resources/textures/bloodWall.png");
		marble = loadBitMap
				("resources/textures/marble.png");
		sat1 = loadBitMap
				("resources/textures/SatDish1.png");
		sat2 = loadBitMap
				("resources/textures/SatDish2.png");
		normButton = loadBitMap
				("resources/textures/normButton.png");
		pistol = loadBitMap
				("resources/textures/pistol.png");
		clip = loadBitMap
				("resources/textures/clip.png");
		bullets = loadBitMap
				("resources/textures/boxOfBullets.png");
		vileCiv1 = loadBitMap
				("resources/textures/vileCivilian.png");
		vileCiv2 = loadBitMap
				("resources/textures/vileCivilian2.png");
		vileCiv3 = loadBitMap
				("resources/textures/vileCivilian3.png");
		vileCiv4 = loadBitMap
				("resources/textures/vileCivilian4.png");
		vileCivAttack1 = loadBitMap
				("resources/textures/vileCivilianAttack1.png");
		vileCivAttack2 = loadBitMap
				("resources/textures/vileCivilianAttack2.png");
		vileCivHurt = loadBitMap
				("resources/textures/vileCivilianHurt.png");
		belegoth = loadBitMap
				("resources/textures/belegoth.png");
		corpse1 = loadBitMap
				("resources/textures/corpse1.png");
		corpse2 = loadBitMap
				("resources/textures/corpse2.png");
		corpse3 = loadBitMap
				("resources/textures/corpse3.png");
		corpse4 = loadBitMap
				("resources/textures/corpse4.png");
		corpse5 = loadBitMap
				("resources/textures/corpse5.png");
		corpse6 = loadBitMap
				("resources/textures/corpse6.png");
		corpse7 = loadBitMap
				("resources/textures/corpse7.png");
		corpse8 = loadBitMap
				("resources/textures/corpse8.png");
		corpseType2 = loadBitMap
				("resources/textures/corpseType2.png");
		enemy4b = loadBitMap
				("resources/textures/enemy4-2.png");
		enemy3b = loadBitMap
				("resources/textures/enemy3-2.png");
		enemy3c = loadBitMap
				("resources/textures/enemy3-3.png");
		enemy3d = loadBitMap
				("resources/textures/enemy3-4.png");
		enemy3e = loadBitMap
				("resources/textures/enemy3-5.png");
		enemy3f = loadBitMap
				("resources/textures/enemy3-6.png");
		defaultFireball = loadBitMap
				("resources/textures/fireball.png");
		giantFireball = loadBitMap
				("resources/textures/giantFireball.png");
		electricShock = loadBitMap
				("resources/textures/electricShock.png");
		electroBall = loadBitMap
				("resources/textures/electroBall.png");
		enemy1b   = loadBitMap
				("resources/textures/enemy1-2.png");
		enemy1corpse1   = loadBitMap
				("resources/textures/enemy1corpse1.png");
		enemy1corpse2   = loadBitMap
				("resources/textures/enemy1corpse2.png");
		enemy1corpse3   = loadBitMap
				("resources/textures/enemy1corpse3.png");
		enemy1corpse   = loadBitMap
				("resources/textures/enemy1corpse.png");
		enemy1hurt   = loadBitMap
				("resources/textures/enemy1hurt.png");
		enemy1fire1   = loadBitMap
				("resources/textures/enemy1fire1.png");
		enemy1fire2   = loadBitMap
				("resources/textures/enemy1fire2.png");
		enemy1fire3   = loadBitMap
				("resources/textures/enemy1fire3.png");
		enemy1fire4   = loadBitMap
				("resources/textures/enemy1fire4.png");
		enemy5b   = loadBitMap
				("resources/textures/enemy5-2.png");
		enemy5corpse1   = loadBitMap
				("resources/textures/enemy5corpse1.png");
		enemy5corpse2   = loadBitMap
				("resources/textures/enemy5corpse2.png");
		enemy5corpse3   = loadBitMap
				("resources/textures/enemy5corpse3.png");
		enemy5corpse4   = loadBitMap
				("resources/textures/enemy5corpse4.png");
		enemy5corpse5   = loadBitMap
				("resources/textures/enemy5corpse5.png");
		enemy5corpse6   = loadBitMap
				("resources/textures/enemy5corpse6.png");
		enemy5corpse7   = loadBitMap
				("resources/textures/enemy5corpse7.png");
		enemy5corpse   = loadBitMap
				("resources/textures/enemy5corpse.png");
		enemy5fire1   = loadBitMap
				("resources/textures/enemy5fire1.png");
		enemy5fire2   = loadBitMap
				("resources/textures/enemy5fire2.png");
		enemy5corpse8   = loadBitMap
				("resources/textures/enemy5corpse8.png");
		enemy5corpse9   = loadBitMap
				("resources/textures/enemy5corpse9.png");
		enemy5corpse10   = loadBitMap
				("resources/textures/enemy5corpse10.png");
		enemy5corpse11   = loadBitMap
				("resources/textures/enemy5corpse11.png");
		enemy5corpse12   = loadBitMap
				("resources/textures/enemy5corpse12.png");
		enemy5corpse13   = loadBitMap
				("resources/textures/enemy5corpse13.png");
		enemy5corpse14   = loadBitMap
				("resources/textures/enemy5corpse14.png");
		enemy5corpse15   = loadBitMap
				("resources/textures/enemy5corpse15.png");
		enemy5corpse16   = loadBitMap
				("resources/textures/enemy5corpse16.png");
		enemy5corpse17   = loadBitMap
				("resources/textures/enemy5corpse17.png");
		enemy4corpse   = loadBitMap
				("resources/textures/enemy4corpse.png");
		enemy5c   = loadBitMap
				("resources/textures/enemy5-3.png");
		enemy5d   = loadBitMap
				("resources/textures/enemy5-4.png");
		enemy3corpse   = loadBitMap
				("resources/textures/enemy3corpse.png");
		enemy3fire1   = loadBitMap
				("resources/textures/enemy3fire1.png");
		enemy3fire2   = loadBitMap
				("resources/textures/enemy3fire2.png");
		enemy3fire3   = loadBitMap
				("resources/textures/enemy3fire3.png");
		enemy3hurt   = loadBitMap
				("resources/textures/enemy3hurt.png");
		enemy2hurt   = loadBitMap
				("resources/textures/enemy2hurt.png");
		enemy2corpse1   = loadBitMap
				("resources/textures/enemy2corpse1.png");
		enemy2corpse2   = loadBitMap
				("resources/textures/enemy2corpse2.png");
		enemy2corpse3   = loadBitMap
				("resources/textures/enemy2corpse3.png");
		enemy2corpse4   = loadBitMap
				("resources/textures/enemy2corpse4.png");
		enemy2corpse5   = loadBitMap
				("resources/textures/enemy2corpse5.png");
		enemy2corpse6   = loadBitMap
				("resources/textures/enemy2corpse6.png");
		enemy2corpse   = loadBitMap
				("resources/textures/enemy2corpse.png");
		enemy2fire1   = loadBitMap
				("resources/textures/enemy2fire.png");
		belegothCorpse1   = loadBitMap
				("resources/textures/belegothCorpse1.png");
		belegothCorpse2   = loadBitMap
				("resources/textures/belegothCorpse2.png");
		belegothCorpse3   = loadBitMap
				("resources/textures/belegothCorpse3.png");
		belegothCorpse4   = loadBitMap
				("resources/textures/belegothCorpse4.png");
		belegothCorpse5   = loadBitMap
				("resources/textures/belegothCorpse5.png");
		belegothCorpse6   = loadBitMap
				("resources/textures/belegothCorpse6.png");
		belegothCorpse7   = loadBitMap
				("resources/textures/belegothCorpse7.png");
		belegothCorpse8   = loadBitMap
				("resources/textures/belegothCorpse8.png");
		belegothCorpse9   = loadBitMap
				("resources/textures/belegothCorpse9.png");
		belegothCorpse10   = loadBitMap
				("resources/textures/belegothCorpse10.png");
		belegothCorpse11   = loadBitMap
				("resources/textures/belegothCorpse11.png");
		belegothCorpse12   = loadBitMap
				("resources/textures/belegothCorpse12.png");
		belegothCorpse13   = loadBitMap
				("resources/textures/belegothCorpse13.png");
		belegothCorpse   = loadBitMap
				("resources/textures/belegothCorpse14.png");
		belegothHurt   = loadBitMap
				("resources/textures/belegothHurt.png");
		belegothMelee   = loadBitMap
				("resources/textures/belegothMelee.png");
		belegoth2   = loadBitMap
				("resources/textures/belegoth2.png");
		belegoth3   = loadBitMap
				("resources/textures/belegoth3.png");
		belegoth4   = loadBitMap
				("resources/textures/belegoth4.png");
		belegoth5   = loadBitMap
				("resources/textures/belegoth5.png");
		belegoth6   = loadBitMap
				("resources/textures/belegoth6.png");
		belegothAttack1   = loadBitMap
				("resources/textures/belegothAttack1.png");
		belegothAttack2   = loadBitMap
				("resources/textures/belegothAttack2.png");
		belegothAttack3   = loadBitMap
				("resources/textures/belegothAttack3.png");
		belegothAttack4   = loadBitMap
				("resources/textures/belegothAttack4.png");
		morgothFire1   = loadBitMap
				("resources/textures/morgothFire1.png");
		morgothFire2   = loadBitMap
				("resources/textures/morgothFire2.png");
		morgothMelee   = loadBitMap
				("resources/textures/morgothMelee.png");
		morgoth2   = loadBitMap
				("resources/textures/morgoth2.png");
		morgoth3   = loadBitMap
				("resources/textures/morgoth3.png");
		morgoth4   = loadBitMap
				("resources/textures/morgoth4.png");
		morgothHurt   = loadBitMap
				("resources/textures/morgothHurt.png");
		enemy2left45   = loadBitMap
				("resources/textures/enemy2(45 left).png");
		enemy2left   = loadBitMap
				("resources/textures/enemy2(left).png");
		enemy2left135   = loadBitMap
				("resources/textures/enemy2(135 left).png");
		enemy2back   = loadBitMap
				("resources/textures/enemy2back.png");
		enemy2right45   = loadBitMap
				("resources/textures/enemy2(45 right).png");
		enemy2right   = loadBitMap
				("resources/textures/enemy2(right).png");
		enemy2right135   = loadBitMap
				("resources/textures/enemy2(135 right).png");
		enemy2left45fire   = loadBitMap
				("resources/textures/enemy2fire(45 left).png");
		enemy2left135fire   = loadBitMap
				("resources/textures/enemy2fire(135 left).png");
		enemy2backfire   = loadBitMap
				("resources/textures/enemy2fire(back).png");
		enemy2right45fire   = loadBitMap
				("resources/textures/enemy2fire(45 right).png");
		enemy2rightfire   = loadBitMap
				("resources/textures/enemy2fire(right).png");
		enemy2leftfire   = loadBitMap
				("resources/textures/enemy2fire(left).png");
		enemy2right135fire   = loadBitMap
				("resources/textures/enemy2fire(135 right).png");
		enemy1left45   = loadBitMap
				("resources/textures/enemy1(45 left).png");
		enemy1left   = loadBitMap
				("resources/textures/enemy1(left).png");
		enemy1left135   = loadBitMap
				("resources/textures/enemy1(135 left).png");
		enemy1back   = loadBitMap
				("resources/textures/enemy1(back).png");
		enemy1right45   = loadBitMap
				("resources/textures/enemy1(45 right).png");
		enemy1right   = loadBitMap
				("resources/textures/enemy1(right).png");
		enemy1right135   = loadBitMap
				("resources/textures/enemy1(135 right).png");
		enemy1left45fire1   = loadBitMap
				("resources/textures/enemy1fire1(45 left).png");
		enemy1left45fire2   = loadBitMap
				("resources/textures/enemy1fire2(45 left).png");
		enemy1left45fire3   = loadBitMap
				("resources/textures/enemy1fire3(45 left).png");
		enemy1left45fire4   = loadBitMap
				("resources/textures/enemy1fire4(45 left).png");
		enemy1leftfire   = loadBitMap
				("resources/textures/enemy1fire(left).png");
		enemy1right45fire1   = loadBitMap
				("resources/textures/enemy1fire1(45 right).png");
		enemy1right45fire2   = loadBitMap
				("resources/textures/enemy1fire2(45 right).png");
		enemy1right45fire3   = loadBitMap
				("resources/textures/enemy1fire3(45 right).png");
		enemy1right45fire4   = loadBitMap
				("resources/textures/enemy1fire4(45 right).png");
		enemy1rightfire   = loadBitMap
				("resources/textures/enemy1fire(right).png");
		enemy1right45hurt   = loadBitMap
				("resources/textures/enemy1hurt(45 right).png");
		enemy1righthurt   = loadBitMap
				("resources/textures/enemy1hurt(right).png");
		enemy1left45hurt   = loadBitMap
				("resources/textures/enemy1hurt(45 left).png");
		enemy1lefthurt   = loadBitMap
				("resources/textures/enemy1hurt(left).png");
		vileCivilian145left   = loadBitMap
				("resources/textures/vileCivilian1(45 left).png");
		vileCivilian245left   = loadBitMap
				("resources/textures/vileCivilian2(45 left).png");
		vileCivilian345left   = loadBitMap
				("resources/textures/vileCivilian3(45 left).png");
		vileCivilian445left   = loadBitMap
				("resources/textures/vileCivilian4(45 left).png");
		vileCivilian1left   = loadBitMap
				("resources/textures/vileCivilian1(left).png");
		vileCivilian2left   = loadBitMap
				("resources/textures/vileCivilian2(left).png");
		vileCivilian3left   = loadBitMap
				("resources/textures/vileCivilian3(left).png");
		vileCivilian4left   = loadBitMap
				("resources/textures/vileCivilian4(left).png");
		vileCivilian1135left   = loadBitMap
				("resources/textures/vileCivilian1(135 left).png");
		vileCivilian2135left   = loadBitMap
				("resources/textures/vileCivilian2(135 left).png");
		vileCivilian3135left   = loadBitMap
				("resources/textures/vileCivilian3(135 left).png");
		vileCivilian4135left   = loadBitMap
				("resources/textures/vileCivilian4(135 left).png");
		vileCivilian1back   = loadBitMap
				("resources/textures/vileCivilian1(back).png");
		vileCivilian2back   = loadBitMap
				("resources/textures/vileCivilian2(back).png");
		vileCivilian3back   = loadBitMap
				("resources/textures/vileCivilian3(back).png");
		vileCivilian4back   = loadBitMap
				("resources/textures/vileCivilian4(back).png");
		vileCivilian145right   = loadBitMap
				("resources/textures/vileCivilian1(45 right).png");
		vileCivilian245right   = loadBitMap
				("resources/textures/vileCivilian2(45 right).png");
		vileCivilian345right   = loadBitMap
				("resources/textures/vileCivilian3(45 right).png");
		vileCivilian445right   = loadBitMap
				("resources/textures/vileCivilian4(45 right).png");
		vileCivilian1right   = loadBitMap
				("resources/textures/vileCivilian1(right).png");
		vileCivilian2right   = loadBitMap
				("resources/textures/vileCivilian2(right).png");
		vileCivilian3right   = loadBitMap
				("resources/textures/vileCivilian3(right).png");
		vileCivilian4right   = loadBitMap
				("resources/textures/vileCivilian4(right).png");
		vileCivilian1135right   = loadBitMap
				("resources/textures/vileCivilian1(135 right).png");
		vileCivilian2135right   = loadBitMap
				("resources/textures/vileCivilian2(135 right).png");
		vileCivilian3135right   = loadBitMap
				("resources/textures/vileCivilian3(135 right).png");
		vileCivilian4135right   = loadBitMap
				("resources/textures/vileCivilian4(135 right).png");
		vileCivilianright45hurt   = loadBitMap
				("resources/textures/vileCivilianhurt(45 right).png");
		vileCivilianrighthurt   = loadBitMap
				("resources/textures/vileCivilianhurt(right).png");
		vileCivilianleft45hurt   = loadBitMap
				("resources/textures/vileCivilianhurt(45 left).png");
		vileCivilianlefthurt   = loadBitMap
				("resources/textures/vileCivilianhurt(left).png");
		vileCivilianright135hurt   = loadBitMap
				("resources/textures/vileCivilianhurt(135 right).png");
		vileCivilianleft135hurt   = loadBitMap
				("resources/textures/vileCivilianhurt(135 left).png");
		vileCivilianbackhurt   = loadBitMap
				("resources/textures/vileCivilianhurt(back).png");
		vileCivilianAttack145left   = loadBitMap
				("resources/textures/vileCivilianAttack1(45 left).png");
		vileCivilianAttack245left   = loadBitMap
				("resources/textures/vileCivilianAttack2(45 left).png");
		vileCivilianAttack1left   = loadBitMap
				("resources/textures/vileCivilianAttack1(left).png");
		vileCivilianAttack2left   = loadBitMap
				("resources/textures/vileCivilianAttack2(left).png");
		vileCivilianAttack1135left   = loadBitMap
				("resources/textures/vileCivilianAttack1(135 left).png");
		vileCivilianAttack2135left   = loadBitMap
				("resources/textures/vileCivilianAttack2(135 left).png");
		vileCivilianAttack1back   = loadBitMap
				("resources/textures/vileCivilianAttack1(back).png");
		vileCivilianAttack2back   = loadBitMap
				("resources/textures/vileCivilianAttack2(back).png");
		vileCivilianAttack145right   = loadBitMap
				("resources/textures/vileCivilianAttack1(45 right).png");
		vileCivilianAttack245right   = loadBitMap
				("resources/textures/vileCivilianAttack2(45 right).png");
		vileCivilianAttack1right   = loadBitMap
				("resources/textures/vileCivilianAttack1(right).png");
		vileCivilianAttack2right   = loadBitMap
				("resources/textures/vileCivilianAttack2(right).png");
		vileCivilianAttack1135right   = loadBitMap
				("resources/textures/vileCivilianAttack1(135 right).png");
		vileCivilianAttack2135right   = loadBitMap
				("resources/textures/vileCivilianAttack2(135 right).png");
		enemy4corpse1   = loadBitMap
				("resources/textures/enemy4corpse1.png");
		enemy4corpse2   = loadBitMap
				("resources/textures/enemy4corpse2.png");
		enemy4corpse3   = loadBitMap
				("resources/textures/enemy4corpse3.png");
		enemy5hurt   = loadBitMap
				("resources/textures/enemy5hurt.png");
		enemy5a45left = loadBitMap
				("resources/textures/enemy5-1(45 left).png");
		enemy5b45left = loadBitMap
				("resources/textures/enemy5-2(45 left).png");
		enemy5c45left = loadBitMap
				("resources/textures/enemy5-3(45 left).png");
		enemy5d45left = loadBitMap
				("resources/textures/enemy5-4(45 left).png");
		enemy5firea45left = loadBitMap
				("resources/textures/enemy5fire1(45 left).png");
		enemy5fireb45left = loadBitMap
				("resources/textures/enemy5fire2(45 left).png");
		enemy5hurt45left = loadBitMap
				("resources/textures/enemy5hurt(45 left).png");
		enemy5aleft = loadBitMap
				("resources/textures/enemy5-1(left).png");
		enemy5bleft = loadBitMap
				("resources/textures/enemy5-2(left).png");
		enemy5cleft = loadBitMap
				("resources/textures/enemy5-3(left).png");
		enemy5dleft = loadBitMap
				("resources/textures/enemy5-4(left).png");
		enemy5firealeft = loadBitMap
				("resources/textures/enemy5fire1(left).png");
		enemy5firebleft = loadBitMap
				("resources/textures/enemy5fire2(left).png");
		enemy5hurtleft = loadBitMap
				("resources/textures/enemy5hurt(left).png");
		enemy5a135left = loadBitMap
				("resources/textures/enemy5-1(135 left).png");
		enemy5b135left = loadBitMap
				("resources/textures/enemy5-2(135 left).png");
		enemy5c135left = loadBitMap
				("resources/textures/enemy5-3(135 left).png");
		enemy5d135left = loadBitMap
				("resources/textures/enemy5-4(135 left).png");
		enemy5firea135left = loadBitMap
				("resources/textures/enemy5fire1(135 left).png");
		enemy5fireb135left = loadBitMap
				("resources/textures/enemy5fire2(135 left).png");
		enemy5hurt135left = loadBitMap
				("resources/textures/enemy5hurt(135 left).png");
		enemy5aback = loadBitMap
				("resources/textures/enemy5-1(back).png");
		enemy5bback = loadBitMap
				("resources/textures/enemy5-2(back).png");
		enemy5cback = loadBitMap
				("resources/textures/enemy5-3(back).png");
		enemy5dback = loadBitMap
				("resources/textures/enemy5-4(back).png");
		enemy5fireaback = loadBitMap
				("resources/textures/enemy5fire1(back).png");
		enemy5firebback = loadBitMap
				("resources/textures/enemy5fire2(back).png");
		enemy5hurtback = loadBitMap
				("resources/textures/enemy5hurt(back).png");
		enemy5a45right = loadBitMap
				("resources/textures/enemy5-1(45 right).png");
		enemy5b45right = loadBitMap
				("resources/textures/enemy5-2(45 right).png");
		enemy5c45right = loadBitMap
				("resources/textures/enemy5-3(45 right).png");
		enemy5d45right = loadBitMap
				("resources/textures/enemy5-4(45 right).png");
		enemy5firea45right = loadBitMap
				("resources/textures/enemy5fire1(45 right).png");
		enemy5fireb45right = loadBitMap
				("resources/textures/enemy5fire2(45 right).png");
		enemy5hurt45right = loadBitMap
				("resources/textures/enemy5hurt(45 right).png");
		enemy5aright = loadBitMap
				("resources/textures/enemy5-1(right).png");
		enemy5bright = loadBitMap
				("resources/textures/enemy5-2(right).png");
		enemy5cright = loadBitMap
				("resources/textures/enemy5-3(right).png");
		enemy5dright = loadBitMap
				("resources/textures/enemy5-4(right).png");
		enemy5firearight = loadBitMap
				("resources/textures/enemy5fire1(right).png");
		enemy5firebright = loadBitMap
				("resources/textures/enemy5fire2(right).png");
		enemy5hurtright = loadBitMap
				("resources/textures/enemy5hurt(right).png");
		enemy5a135right = loadBitMap
				("resources/textures/enemy5-1(135 right).png");
		enemy5b135right = loadBitMap
				("resources/textures/enemy5-2(135 right).png");
		enemy5c135right = loadBitMap
				("resources/textures/enemy5-3(135 right).png");
		enemy5d135right = loadBitMap
				("resources/textures/enemy5-4(135 right).png");
		enemy5firea135right = loadBitMap
				("resources/textures/enemy5fire1(135 right).png");
		enemy5fireb135right = loadBitMap
				("resources/textures/enemy5fire2(135 right).png");
		enemy5hurt135right = loadBitMap
				("resources/textures/enemy5hurt(135 right).png");
		enemy3corpse1   = loadBitMap
				("resources/textures/enemy3corpse1.png");
		enemy3corpse2   = loadBitMap
				("resources/textures/enemy3corpse2.png");
		enemy3corpse3   = loadBitMap
				("resources/textures/enemy3corpse3.png");
		enemy3corpse4   = loadBitMap
				("resources/textures/enemy3corpse4.png");
		enemy3corpse5   = loadBitMap
				("resources/textures/enemy3corpse5.png");
		enemy3corpse6   = loadBitMap
				("resources/textures/enemy3corpse6.png");
		enemy3corpse7   = loadBitMap
				("resources/textures/enemy3corpse7.png");
		enemy3corpse8   = loadBitMap
				("resources/textures/enemy3corpse8.png");
		enemy3corpse9   = loadBitMap
				("resources/textures/enemy3corpse9.png");
		 enemy3a45left = loadBitMap
				("resources/textures/enemy3-1(45 left).png");
		 enemy3b45left = loadBitMap
				("resources/textures/enemy3-2(45 left).png");
		 enemy3c45left = loadBitMap
				("resources/textures/enemy3-3(45 left).png");
		 enemy3d45left = loadBitMap
				("resources/textures/enemy3-4(45 left).png");
		 enemy3e45left = loadBitMap
				("resources/textures/enemy3-5(45 left).png");
		 enemy3f45left = loadBitMap
				("resources/textures/enemy3-6(45 left).png");
		 enemy3fire1left45 = loadBitMap
				("resources/textures/enemy3fire1(45 left).png");
		 enemy3fire2left45 = loadBitMap
				("resources/textures/enemy3fire2(45 left).png");
		 enemy3fire3left45 = loadBitMap
				("resources/textures/enemy3fire3(45 left).png");
		 enemy3hurt45left = loadBitMap
				("resources/textures/enemy3hurt(45 left).png");
		 enemy3a45right = loadBitMap
				("resources/textures/enemy3-1(45 right).png");
		 enemy3b45right = loadBitMap
				("resources/textures/enemy3-2(45 right).png");
		 enemy3c45right = loadBitMap
				("resources/textures/enemy3-3(45 right).png");
		 enemy3d45right = loadBitMap
				("resources/textures/enemy3-4(45 right).png");
		 enemy3e45right = loadBitMap
				("resources/textures/enemy3-5(45 right).png");
		 enemy3f45right = loadBitMap
				("resources/textures/enemy3-6(45 right).png");
		 enemy3fire1right45 = loadBitMap
				("resources/textures/enemy3fire1(45 right).png");
		 enemy3fire2right45 = loadBitMap
				("resources/textures/enemy3fire2(45 right).png");
		 enemy3fire3right45 = loadBitMap
				("resources/textures/enemy3fire3(45 right).png");
		 enemy3hurt45right = loadBitMap
				("resources/textures/enemy3hurt(45 right).png");
		 enemy3aleft = loadBitMap
				("resources/textures/enemy3-1(left).png");
		 enemy3bleft = loadBitMap
				("resources/textures/enemy3-2(left).png");
		 enemy3cleft = loadBitMap
				("resources/textures/enemy3-3(left).png");
		 enemy3dleft = loadBitMap
				("resources/textures/enemy3-4(left).png");
		 enemy3eleft = loadBitMap
				("resources/textures/enemy3-5(left).png");
		 enemy3fleft = loadBitMap
				("resources/textures/enemy3-6(left).png");
		 enemy3fire1left= loadBitMap
				("resources/textures/enemy3fire1(left).png");
		 enemy3fire2left= loadBitMap
				("resources/textures/enemy3fire2(left).png");
		 enemy3fire3left= loadBitMap
				("resources/textures/enemy3fire3(left).png");
		 enemy3hurtleft = loadBitMap
				("resources/textures/enemy3hurt(left).png");
		 enemy3aright = loadBitMap
				("resources/textures/enemy3-1(right).png");
		 enemy3bright = loadBitMap
				("resources/textures/enemy3-2(right).png");
		 enemy3cright = loadBitMap
				("resources/textures/enemy3-3(right).png");
		 enemy3dright = loadBitMap
				("resources/textures/enemy3-4(right).png");
		 enemy3eright = loadBitMap
				("resources/textures/enemy3-5(right).png");
		 enemy3fright = loadBitMap
				("resources/textures/enemy3-6(right).png");
		 enemy3fire1right= loadBitMap
				("resources/textures/enemy3fire1(right).png");
		 enemy3fire2right= loadBitMap
				("resources/textures/enemy3fire2(right).png");
		 enemy3fire3right= loadBitMap
				("resources/textures/enemy3fire3(right).png");
		 enemy3hurtright = loadBitMap
				("resources/textures/enemy3hurt(right).png");
		 enemy3a135right = loadBitMap
				("resources/textures/enemy3-1(135 right).png");
		 enemy3b135right = loadBitMap
				("resources/textures/enemy3-2(135 right).png");
		 enemy3c135right = loadBitMap
				("resources/textures/enemy3-3(135 right).png");
		 enemy3d135right = loadBitMap
				("resources/textures/enemy3-4(135 right).png");
		 enemy3e135right = loadBitMap
				("resources/textures/enemy3-5(135 right).png");
		 enemy3f135right = loadBitMap
				("resources/textures/enemy3-6(135 right).png");
		 enemy3fire1right135 = loadBitMap
				("resources/textures/enemy3fire1(135 right).png");
		 enemy3fire2right135 = loadBitMap
				("resources/textures/enemy3fire2(135 right).png");
		 enemy3fire3right135 = loadBitMap
				("resources/textures/enemy3fire3(135 right).png");
		 enemy3hurt135right = loadBitMap
				("resources/textures/enemy3hurt(135 right).png");
		 enemy3a135left = loadBitMap
				("resources/textures/enemy3-1(135 left).png");
		 enemy3b135left = loadBitMap
				("resources/textures/enemy3-2(135 left).png");
		 enemy3c135left = loadBitMap
				("resources/textures/enemy3-3(135 left).png");
		 enemy3d135left = loadBitMap
				("resources/textures/enemy3-4(135 left).png");
		 enemy3e135left = loadBitMap
				("resources/textures/enemy3-5(135 left).png");
		 enemy3f135left = loadBitMap
				("resources/textures/enemy3-6(135 left).png");
		 enemy3fire1left135 = loadBitMap
				("resources/textures/enemy3fire1(135 left).png");
		 enemy3fire2left135 = loadBitMap
				("resources/textures/enemy3fire2(135 left).png");
		 enemy3fire3left135 = loadBitMap
				("resources/textures/enemy3fire3(135 left).png");
		 enemy3hurt135left = loadBitMap
				("resources/textures/enemy3hurt(135 left).png");
		 enemy3aback = loadBitMap
				("resources/textures/enemy3-1(back).png");
		 enemy3bback = loadBitMap
				("resources/textures/enemy3-2(back).png");
		 enemy3cback = loadBitMap
				("resources/textures/enemy3-3(back).png");
		 enemy3dback = loadBitMap
				("resources/textures/enemy3-4(back).png");
		 enemy3eback = loadBitMap
				("resources/textures/enemy3-5(back).png");
		 enemy3fback = loadBitMap
				("resources/textures/enemy3-6(back).png");
		 enemy3fire1back= loadBitMap
				("resources/textures/enemy3fire1(back).png");
		 enemy3fire2back= loadBitMap
				("resources/textures/enemy3fire2(back).png");
		 enemy3fire3back= loadBitMap
				("resources/textures/enemy3fire3(back).png");
		 enemy3hurtback = loadBitMap
				("resources/textures/enemy3hurt(back).png");
	     rockets = loadBitMap
				("resources/textures/rockets.png");
		 rocketLaucher = loadBitMap
				("resources/textures/rocketLauncher.png");
		 rocketCrate = loadBitMap
				("resources/textures/rocketCrate.png");
		 rocket = loadBitMap
				("resources/textures/rocket.png");
		 explosion1 = loadBitMap
				("resources/textures/explosion1.png");
		 explosion2 = loadBitMap
				("resources/textures/explosion2.png");
		 explosion3 = loadBitMap
				("resources/textures/explosion3.png");
		 explosion4 = loadBitMap
				("resources/textures/explosion4.png");
		 explosion5 = loadBitMap
				("resources/textures/explosion5.png");
		 explosion6 = loadBitMap
				("resources/textures/explosion6.png");
		 explosion7 = loadBitMap
				("resources/textures/explosion7.png");
		 explosion8 = loadBitMap
				("resources/textures/explosion8.png");
		 defaultCorpse1 = loadBitMap
					("resources/textures/defaultCorpse1.png");
		 defaultCorpse2 = loadBitMap
					("resources/textures/defaultCorpse2.png");
		coolWall = loadBitMap
				("resources/textures/theme"+(Display.themeNum + 1)
						+"/walls/coolWall.png");
		phaseHit1 = loadBitMap
				("resources/textures/phaseHit1.png");
		phaseHit2 = loadBitMap
				("resources/textures/phaseHit2.png");
		phaseHit3 = loadBitMap
				("resources/textures/phaseHit3.png");
		phaseHit4 = loadBitMap
				("resources/textures/phaseHit4.png");
		phaseHit5 = loadBitMap
				("resources/textures/phaseHit5.png");
		fireHit1 = loadBitMap
				("resources/textures/fireHit1.png");
		fireHit2 = loadBitMap
				("resources/textures/fireHit2.png");
		fireHit3 = loadBitMap
				("resources/textures/fireHit3.png");
		fireHit4 = loadBitMap
				("resources/textures/fireHit4.png");
		bulletHit1 = loadBitMap
				("resources/textures/bulletHit1.png");
		bulletHit2 = loadBitMap
				("resources/textures/bulletHit2.png");
		bulletHit3 = loadBitMap
				("resources/textures/bulletHit3.png");
		bulletHit4 = loadBitMap
				("resources/textures/bulletHit4.png");
		bloodSpray1 = loadBitMap
				("resources/textures/bloodSpray1.png");
		bloodSpray2 = loadBitMap
				("resources/textures/bloodSpray2.png");
		bloodSpray3 = loadBitMap
				("resources/textures/bloodSpray3.png");
		bloodSpray4 = loadBitMap
				("resources/textures/bloodSpray4.png");
	}
}
