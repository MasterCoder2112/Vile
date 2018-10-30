package com.vile.graphics;

import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.imageio.ImageIO;

import com.vile.launcher.FPSLauncher;

/**
 * Title: Textures
 * 
 * @author Alex Byrd Date Updated: 3/07/2016
 *
 *         Descriptions: Loads the textures into the game from the textures
 *         file, and makes them into Render objects with given rgb values and
 *         width and heights so that they are rendered correctly on screen.
 */
public class Textures {
	public static Render[] floors;

	//////////////////////////////////////// WALLS
	public static Render wall1 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/walls/wall1.png");
	public static Render wall2 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/walls/wall2.png");
	public static Render wall3 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/walls/wall3.png");
	public static Render wall4 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/walls/wall4.png");
	public static Render wall4damaged1 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/walls/wall4damage1.png");
	public static Render wall4damaged2 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/walls/wall4damage2.png");
	public static Render wall4damaged3 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/walls/wall4damage3.png");
	public static Render wall5 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/walls/wall5.png");
	public static Render wall6 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/walls/wall6.png");
	public static Render wall7 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/walls/wall7.png");
	public static Render wall8 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/walls/wall8.png");
	public static Render wall9 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/walls/wall9.png");
	public static Render wall10 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/walls/wall10.png");
	public static Render wall11 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/walls/wall11.png");
	public static Render wall12 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/walls/wall12.png");
	public static Render wall13Phase1 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/walls/wall13phase1.png");
	public static Render wall13Phase2 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/walls/wall13phase2.png");
	public static Render wall14 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/walls/wall14.png");
	public static Render teleportEnter = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/walls/teleporterEnter.png");
	public static Render teleportExit = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/walls/teleporterExit.png");
	public static Render tutorialWall = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/walls/tutorialWall.png");
	public static Render tutorialWall2 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/walls/tutorialWall2.png");
	public static Render tutorialWall3 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/walls/tutorialWall3.png");
	public static Render tutorialWall4 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/walls/tutorialWall4.png");
	public static Render tutorialWall5 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/walls/tutorialWall5.png");
	public static Render wall15Phase5 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/walls/wall15phase5.png");
	public static Render wall15Phase4 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/walls/wall15phase4.png");
	public static Render wall15Phase3 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/walls/wall15phase3.png");
	public static Render wall15Phase2 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/walls/wall15phase2.png");
	public static Render wall15Phase1 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/walls/wall15phase1.png");

	// Toxic Waste/Lava. Has many phases
	public static Render toxic1 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/walls/toxicWaste16.png");
	public static Render toxic2 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/walls/toxicWaste15.png");
	public static Render toxic3 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/walls/toxicWaste14.png");
	public static Render toxic4 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/walls/toxicWaste13.png");
	public static Render toxic5 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/walls/toxicWaste12.png");
	public static Render toxic6 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/walls/toxicWaste11.png");
	public static Render toxic7 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/walls/toxicWaste10.png");
	public static Render toxic8 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/walls/toxicWaste9.png");
	public static Render toxic9 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/walls/toxicWaste8.png");
	public static Render toxic10 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/walls/toxicWaste7.png");
	public static Render toxic11 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/walls/toxicWaste6.png");
	public static Render toxic12 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/walls/toxicWaste5.png");
	public static Render toxic13 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/walls/toxicWaste4.png");
	public static Render toxic14 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/walls/toxicWaste3.png");
	public static Render toxic15 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/walls/toxicWaste2.png");
	public static Render toxic16 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/walls/toxicWaste1.png");
	public static Render coolWall = loadBitMap("resources" + FPSLauncher.themeName + "/textures/walls/coolWall.png");
	public static Render spine1 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/walls/spineWall1.png");
	public static Render spine2 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/walls/spineWall2.png");
	public static Render spine3 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/walls/spineWall3.png");
	public static Render spine4 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/walls/spineWall4.png");
	public static Render spine5 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/walls/spineWall5.png");
	public static Render spine6 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/walls/spineWall6.png");
	public static Render spine7 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/walls/spineWall7.png");
	public static Render spine8 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/walls/spineWall8.png");
	public static Render mlg = loadBitMap("resources" + FPSLauncher.themeName + "/textures/walls/mlg.png");
	public static Render box = loadBitMap("resources" + FPSLauncher.themeName + "/textures/walls/boxWall.png");
	public static Render woodenWall = loadBitMap("resources" + FPSLauncher.themeName + "/textures/walls/woodWall.png");
	public static Render bloodWall = loadBitMap("resources" + FPSLauncher.themeName + "/textures/walls/bloodWall.png");
	public static Render marble = loadBitMap("resources" + FPSLauncher.themeName + "/textures/walls/marble.png");
	public static Render normButton = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/walls/normButton.png");
	public static Render normButtonOn = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/walls/normButtonOn.png");
	public static Render wall35 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/walls/wall35.png");
	public static Render wall36 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/walls/wall36.png");
	public static Render wall37 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/walls/wall37.png");
	public static Render wall38 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/walls/wall38.png");
	public static Render wall39 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/walls/wall39.png");
	public static Render wall40 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/walls/wall40.png");
	public static Render wall41 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/walls/wall41.png");
	public static Render wall42a = loadBitMap("resources" + FPSLauncher.themeName + "/textures/walls/wall42-1.png");
	public static Render wall42b = loadBitMap("resources" + FPSLauncher.themeName + "/textures/walls/wall42-2.png");
	public static Render wall42c = loadBitMap("resources" + FPSLauncher.themeName + "/textures/walls/wall42-3.png");
	public static Render wall42d = loadBitMap("resources" + FPSLauncher.themeName + "/textures/walls/wall42-4.png");

	//////////////////////////////////////////////// ITEMS
	public static Render health = loadBitMap("resources" + FPSLauncher.themeName + "/textures/items/health.png");
	public static Render shell = loadBitMap("resources" + FPSLauncher.themeName + "/textures/items/shell.png");
	public static Render megaPhase1 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/items/megahealthphase1.png");
	public static Render megaPhase2 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/items/megahealthphase2.png");
	public static Render megaPhase3 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/items/megahealthphase3.png");
	public static Render megaPhase4 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/items/megahealthphase4.png");
	public static Render redKey = loadBitMap("resources" + FPSLauncher.themeName + "/textures/items/redKey.png");
	public static Render blueKey = loadBitMap("resources" + FPSLauncher.themeName + "/textures/items/blueKey.png");
	public static Render greenKey = loadBitMap("resources" + FPSLauncher.themeName + "/textures/items/greenKey.png");
	public static Render yellowKey = loadBitMap("resources" + FPSLauncher.themeName + "/textures/items/yellowKey.png");
	public static Render shotgun = loadBitMap("resources" + FPSLauncher.themeName + "/textures/items/shotgun.png");
	public static Render resurrect1 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/items/resurrectSkull1.png");
	public static Render resurrect2 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/items/resurrectSkull2.png");
	public static Render resurrect3 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/items/resurrectSkull3.png");
	public static Render environSuit = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/items/environSuit.png");
	public static Render goblet1 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/items/goblet1.png");
	public static Render goblet2 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/items/goblet2.png");
	public static Render goblet3 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/items/goblet3.png");
	public static Render adrenaline = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/items/adrenaline.png");
	public static Render glasses = loadBitMap("resources" + FPSLauncher.themeName + "/textures/items/glasses.png");
	public static Render torch1 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/items/torch1.png");
	public static Render torch2 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/items/torch2.png");
	public static Render torch3 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/items/torch3.png");
	public static Render torch4 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/items/torch4.png");
	public static Render lamp1 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/items/lamp1.png");
	public static Render lamp2 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/items/lamp2.png");
	public static Render lamp3 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/items/lamp3.png");
	public static Render tree = loadBitMap("resources" + FPSLauncher.themeName + "/textures/items/tree.png");
	public static Render canister = loadBitMap("resources" + FPSLauncher.themeName + "/textures/items/canister.png");
	public static Render chainmeal = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/items/chainmealArmor.png");
	public static Render combat = loadBitMap("resources" + FPSLauncher.themeName + "/textures/items/combatArmor.png");
	public static Render argent = loadBitMap("resources" + FPSLauncher.themeName + "/textures/items/argentArmor.png");
	public static Render shard = loadBitMap("resources" + FPSLauncher.themeName + "/textures/items/armorShard.png");
	public static Render vial = loadBitMap("resources" + FPSLauncher.themeName + "/textures/items/healthVial.png");
	public static Render upgrade1 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/items/weaponUpgrade1.png");
	public static Render upgrade2 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/items/weaponUpgrade2.png");
	public static Render upgrade3 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/items/weaponUpgrade3.png");
	public static Render upgrade4 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/items/weaponUpgrade4.png");
	public static Render holyWater1 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/items/holyWater1.png");
	public static Render holyWater2 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/items/holyWater2.png");
	public static Render table = loadBitMap("resources" + FPSLauncher.themeName + "/textures/items/table.png");
	public static Render lampTable = loadBitMap("resources" + FPSLauncher.themeName + "/textures/items/lampTable.png");
	public static Render scepter = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/items/decietScepter.png");
	public static Render invisEmerald = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/items/invisibilityEmerald.png");
	public static Render invisEmerald2 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/items/invisibilityEmerald2.png");
	public static Render invisEmerald3 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/items/invisibilityEmerald3.png");
	public static Render invisEmerald4 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/items/invisibilityEmerald4.png");
	public static Render shellBox = loadBitMap("resources" + FPSLauncher.themeName + "/textures/items/shellBox.png");
	public static Render chargePack = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/items/chargePack.png");
	public static Render largeChargePack = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/items/chargePackLarge.png");
	public static Render phaseCannon = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/items/phaseCannonWeapon.png");
	public static Render sat1 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/items/SatDish1.png");
	public static Render sat2 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/items/SatDish2.png");
	public static Render pistol = loadBitMap("resources" + FPSLauncher.themeName + "/textures/items/pistol.png");
	public static Render clip = loadBitMap("resources" + FPSLauncher.themeName + "/textures/items/clip.png");
	public static Render bullets = loadBitMap("resources" + FPSLauncher.themeName + "/textures/items/boxOfBullets.png");
	public static Render rockets = loadBitMap("resources" + FPSLauncher.themeName + "/textures/items/rockets.png");
	public static Render rocketLaucher = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/items/rocketLauncher.png");
	public static Render rocketCrate = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/items/rocketCrate.png");
	public static Render bonePile = loadBitMap("resources" + FPSLauncher.themeName + "/textures/items/bonePile.png");
	public static Render tEnter1 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/items/tEnter1.png");
	public static Render tEnter2 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/items/tEnter2.png");
	public static Render tEnter3 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/items/tEnter3.png");
	public static Render tEnter4 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/items/tEnter4.png");
	public static Render tExit1 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/items/tExit1.png");
	public static Render tExit2 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/items/tExit2.png");
	public static Render tExit3 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/items/tExit3.png");
	public static Render tExit4 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/items/tExit4.png");
	public static Render darkBook = loadBitMap("resources" + FPSLauncher.themeName + "/textures/items/darkBook.png");
	public static Render turret = loadBitMap("resources" + FPSLauncher.themeName + "/textures/items/turret.png");
	public static Render marine1 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/items/marine1.png");
	public static Render marine2 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/items/marine2.png");
	public static Render marine3 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/items/marine3.png");
	public static Render marine4 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/items/marine4.png");
	public static Render marine5 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/items/marine5.png");
	public static Render burningCorpse = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/items/burningCorpse.png");
	public static Render carcass = loadBitMap("resources" + FPSLauncher.themeName + "/textures/items/carcass.png");
	public static Render wizardCorpse = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/items/wizardCorpse.png");
	public static Render eyePile = loadBitMap("resources" + FPSLauncher.themeName + "/textures/items/eyePile.png");
	public static Render bone = loadBitMap("resources" + FPSLauncher.themeName + "/textures/items/bone.png");
	public static Render remains = loadBitMap("resources" + FPSLauncher.themeName + "/textures/items/remains.png");
	public static Render remains2 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/items/remains2.png");
	public static Render corpsePile = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/items/corpsePile.png");
	public static Render redPillar = loadBitMap("resources" + FPSLauncher.themeName + "/textures/items/redPillar.png");
	public static Render greenPillar = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/items/greenPillar.png");
	public static Render bluePillar = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/items/bluePillar.png");
	public static Render beer = loadBitMap("resources" + FPSLauncher.themeName + "/textures/items/beer.png");
	public static Render bible = loadBitMap("resources" + FPSLauncher.themeName + "/textures/items/bible.png");
	public static Render blueBanner = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/items/blueBanner.png");
	public static Render burger = loadBitMap("resources" + FPSLauncher.themeName + "/textures/items/burger.png");
	public static Render ceilingLamp = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/items/ceilingLamp.png");
	public static Render bush1 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/items/bush1.png");
	public static Render bush2 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/items/bush2.png");
	public static Render chair = loadBitMap("resources" + FPSLauncher.themeName + "/textures/items/chair.png");
	public static Render placeHolder = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/items/comingSoon.png");
	public static Render electroPod = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/items/electroPod.png");
	public static Render evilCandels1 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/items/evilCandels1.png");
	public static Render evilCandels2 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/items/evilCandels2.png");
	public static Render evilCandels3 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/items/evilCandels3.png");
	public static Render evilCandels4 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/items/evilCandels4.png");
	public static Render groundLamp1 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/items/groundLamp.png");
	public static Render groundLamp2 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/items/groundLamp2.png");
	public static Render groundLamp3 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/items/groundLamp3.png");
	public static Render hellroot = loadBitMap("resources" + FPSLauncher.themeName + "/textures/items/hellroot.png");
	public static Render pustule = loadBitMap("resources" + FPSLauncher.themeName + "/textures/items/pustule.png");
	public static Render redBanner = loadBitMap("resources" + FPSLauncher.themeName + "/textures/items/redBanner.png");
	public static Render rock1 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/items/rock.png");
	public static Render rock2 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/items/rock2.png");
	public static Render rock3 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/items/rock3.png");
	public static Render stalagmite1 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/items/stalagmite.png");
	public static Render stalagmite2 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/items/stalagmite2.png");
	public static Render techBarrel = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/items/techBarrel.png");
	public static Render techPillar1 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/items/techPillar1.png");
	public static Render techPillar2 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/items/techPillar2.png");
	public static Render techPillar3 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/items/techPillar3.png");
	public static Render toilet = loadBitMap("resources" + FPSLauncher.themeName + "/textures/items/toilet.png");
	public static Render trash1 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/items/trashcan.png");
	public static Render trash2 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/items/trashcan2.png");
	public static Render trash3 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/items/trashcan3.png");
	public static Render treeAlive = loadBitMap("resources" + FPSLauncher.themeName + "/textures/items/treeAlive.png");

	/////////////////////////////////////////////// ENTITIES
	public static Render enemy1 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy1.png");
	public static Render enemy2 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy2.png");
	public static Render enemy3 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy3.png");
	public static Render enemy4 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy4.png");
	public static Render morgoth = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/morgoth.png");
	public static Render enemy5 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy5.png");
	public static Render vileCiv1 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/vileCivilian.png");
	public static Render vileCiv2 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/vileCivilian2.png");
	public static Render vileCiv3 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/vileCivilian3.png");
	public static Render vileCiv4 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/vileCivilian4.png");
	public static Render vileCivAttack1 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/vileCivilianAttack1.png");
	public static Render vileCivAttack2 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/vileCivilianAttack2.png");
	public static Render vileCivHurt = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/vileCivilianHurt.png");
	public static Render belegoth = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/belegoth.png");
	public static Render corpse1 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/corpse1.png");
	public static Render corpse2 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/corpse2.png");
	public static Render corpse3 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/corpse3.png");
	public static Render corpse4 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/corpse4.png");
	public static Render corpse5 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/corpse5.png");
	public static Render corpse6 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/corpse6.png");
	public static Render corpse7 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/corpse7.png");
	public static Render corpse8 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/corpse8.png");
	public static Render corpseType2 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/corpseType2.png");
	public static Render enemy4b = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy4-2.png");
	public static Render enemy3b = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy3-2.png");
	public static Render enemy3c = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy3-3.png");
	public static Render enemy3d = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy3-4.png");
	public static Render enemy3e = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy3-5.png");
	public static Render enemy3f = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy3-6.png");
	public static Render enemy1b = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy1-2.png");
	public static Render enemy1corpse1 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy1corpse1.png");
	public static Render enemy1corpse2 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy1corpse2.png");
	public static Render enemy1corpse3 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy1corpse3.png");
	public static Render enemy1corpse = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy1corpse.png");
	public static Render enemy1hurt = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy1hurt.png");
	public static Render enemy1fire1 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy1fire1.png");
	public static Render enemy1fire2 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy1fire2.png");
	public static Render enemy1fire3 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy1fire3.png");
	public static Render enemy1fire4 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy1fire4.png");
	public static Render enemy5b = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy5-2.png");
	public static Render enemy5corpse1 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy5corpse1.png");
	public static Render enemy5corpse2 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy5corpse2.png");
	public static Render enemy5corpse3 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy5corpse3.png");
	public static Render enemy5corpse4 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy5corpse4.png");
	public static Render enemy5corpse5 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy5corpse5.png");
	public static Render enemy5corpse6 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy5corpse6.png");
	public static Render enemy5corpse7 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy5corpse7.png");
	public static Render enemy5corpse8 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy5corpse8.png");
	public static Render enemy5corpse9 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy5corpse9.png");
	public static Render enemy5corpse10 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy5corpse10.png");
	public static Render enemy5corpse11 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy5corpse11.png");
	public static Render enemy5corpse12 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy5corpse12.png");
	public static Render enemy5corpse13 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy5corpse13.png");
	public static Render enemy5corpse14 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy5corpse14.png");
	public static Render enemy5corpse15 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy5corpse15.png");
	public static Render enemy5corpse16 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy5corpse16.png");
	public static Render enemy5corpse17 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy5corpse17.png");
	public static Render enemy4corpse = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy4corpse.png");
	public static Render enemy5c = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy5-3.png");
	public static Render enemy5d = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy5-4.png");
	public static Render enemy5corpse = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy5corpse.png");
	public static Render enemy5fire1 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy5fire1.png");
	public static Render enemy5fire2 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy5fire2.png");
	public static Render enemy3corpse = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy3corpse.png");
	public static Render enemy3fire1 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy3fire1.png");
	public static Render enemy3fire2 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy3fire2.png");
	public static Render enemy3fire3 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy3fire3.png");
	public static Render enemy3hurt = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy3hurt.png");
	public static Render enemy2hurt = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy2hurt.png");
	public static Render enemy2corpse1 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy2corpse1.png");
	public static Render enemy2corpse2 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy2corpse2.png");
	public static Render enemy2corpse3 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy2corpse3.png");
	public static Render enemy2corpse4 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy2corpse4.png");
	public static Render enemy2corpse5 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy2corpse5.png");
	public static Render enemy2corpse6 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy2corpse6.png");
	public static Render enemy2corpse = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy2corpse.png");
	public static Render enemy2fire1 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy2fire.png");
	public static Render belegothCorpse1 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/belegothCorpse1.png");
	public static Render belegothCorpse2 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/belegothCorpse2.png");
	public static Render belegothCorpse3 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/belegothCorpse3.png");
	public static Render belegothCorpse4 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/belegothCorpse4.png");
	public static Render belegothCorpse5 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/belegothCorpse5.png");
	public static Render belegothCorpse6 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/belegothCorpse6.png");
	public static Render belegothCorpse7 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/belegothCorpse7.png");
	public static Render belegothCorpse8 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/belegothCorpse8.png");
	public static Render belegothCorpse9 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/belegothCorpse9.png");
	public static Render belegothCorpse10 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/belegothCorpse10.png");
	public static Render belegothCorpse11 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/belegothCorpse11.png");
	public static Render belegothCorpse12 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/belegothCorpse12.png");
	public static Render belegothCorpse13 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/belegothCorpse13.png");
	public static Render belegothCorpse = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/belegothCorpse14.png");
	public static Render belegothHurt = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/belegothHurt.png");
	public static Render belegothMelee = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/belegothMelee.png");
	public static Render belegoth2 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/belegoth2.png");
	public static Render belegoth3 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/belegoth3.png");
	public static Render belegoth4 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/belegoth4.png");
	public static Render belegoth5 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/belegoth5.png");
	public static Render belegoth6 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/belegoth6.png");
	public static Render belegothAttack1 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/belegothAttack1.png");
	public static Render belegothAttack2 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/belegothAttack2.png");
	public static Render belegothAttack3 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/belegothAttack3.png");
	public static Render belegothAttack4 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/belegothAttack4.png");
	public static Render morgothFire1 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/morgothFire1.png");
	public static Render morgothFire2 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/morgothFire2.png");
	public static Render morgothMelee = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/morgothMelee.png");
	public static Render morgoth2 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/morgoth2.png");
	public static Render morgoth3 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/morgoth3.png");
	public static Render morgoth4 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/morgoth4.png");
	public static Render morgothHurt = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/morgothHurt.png");
	public static Render enemy2left45 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy2(45 left).png");
	public static Render enemy2left = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy2(left).png");
	public static Render enemy2left135 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy2(135 left).png");
	public static Render enemy2back = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy2back.png");
	public static Render enemy2right45 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy2(45 right).png");
	public static Render enemy2right = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy2(right).png");
	public static Render enemy2right135 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy2(135 right).png");
	public static Render enemy2left45fire = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy2fire(45 left).png");
	public static Render enemy2left135fire = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy2fire(135 left).png");
	public static Render enemy2backfire = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy2fire(back).png");
	public static Render enemy2right45fire = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy2fire(45 right).png");
	public static Render enemy2rightfire = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy2fire(right).png");
	public static Render enemy2leftfire = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy2fire(left).png");
	public static Render enemy2right135fire = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy2fire(135 right).png");
	public static Render enemy1left45 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy1(45 left).png");
	public static Render enemy1left = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy1(left).png");
	public static Render enemy1left135 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy1(135 left).png");
	public static Render enemy1back = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy1(back).png");
	public static Render enemy1right45 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy1(45 right).png");
	public static Render enemy1right = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy1(right).png");
	public static Render enemy1right135 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy1(135 right).png");
	public static Render enemy1left45fire1 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy1fire1(45 left).png");
	public static Render enemy1left45fire2 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy1fire2(45 left).png");
	public static Render enemy1left45fire3 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy1fire3(45 left).png");
	public static Render enemy1left45fire4 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy1fire4(45 left).png");
	public static Render enemy1leftfire = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy1fire(left).png");
	public static Render enemy1right45fire1 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy1fire1(45 right).png");
	public static Render enemy1right45fire2 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy1fire2(45 right).png");
	public static Render enemy1right45fire3 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy1fire3(45 right).png");
	public static Render enemy1right45fire4 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy1fire4(45 right).png");
	public static Render enemy1rightfire = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy1fire(right).png");
	public static Render enemy1right45hurt = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy1hurt(45 right).png");
	public static Render enemy1righthurt = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy1hurt(right).png");
	public static Render enemy1left45hurt = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy1hurt(45 left).png");
	public static Render enemy1lefthurt = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy1hurt(left).png");
	public static Render vileCivilian145left = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/vileCivilian1(45 left).png");
	public static Render vileCivilian245left = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/vileCivilian2(45 left).png");
	public static Render vileCivilian345left = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/vileCivilian3(45 left).png");
	public static Render vileCivilian445left = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/vileCivilian4(45 left).png");
	public static Render vileCivilian1left = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/vileCivilian1(left).png");
	public static Render vileCivilian2left = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/vileCivilian2(left).png");
	public static Render vileCivilian3left = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/vileCivilian3(left).png");
	public static Render vileCivilian4left = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/vileCivilian4(left).png");
	public static Render vileCivilian1135left = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/vileCivilian1(135 left).png");
	public static Render vileCivilian2135left = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/vileCivilian2(135 left).png");
	public static Render vileCivilian3135left = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/vileCivilian3(135 left).png");
	public static Render vileCivilian4135left = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/vileCivilian4(135 left).png");
	public static Render vileCivilian1back = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/vileCivilian1(back).png");
	public static Render vileCivilian2back = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/vileCivilian2(back).png");
	public static Render vileCivilian3back = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/vileCivilian3(back).png");
	public static Render vileCivilian4back = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/vileCivilian4(back).png");
	public static Render vileCivilian145right = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/vileCivilian1(45 right).png");
	public static Render vileCivilian245right = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/vileCivilian2(45 right).png");
	public static Render vileCivilian345right = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/vileCivilian3(45 right).png");
	public static Render vileCivilian445right = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/vileCivilian4(45 right).png");
	public static Render vileCivilian1right = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/vileCivilian1(right).png");
	public static Render vileCivilian2right = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/vileCivilian2(right).png");
	public static Render vileCivilian3right = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/vileCivilian3(right).png");
	public static Render vileCivilian4right = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/vileCivilian4(right).png");
	public static Render vileCivilian1135right = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/vileCivilian1(135 right).png");
	public static Render vileCivilian2135right = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/vileCivilian2(135 right).png");
	public static Render vileCivilian3135right = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/vileCivilian3(135 right).png");
	public static Render vileCivilian4135right = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/vileCivilian4(135 right).png");
	public static Render vileCivilianright45hurt = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/vileCivilianhurt(45 right).png");
	public static Render vileCivilianrighthurt = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/vileCivilianhurt(right).png");
	public static Render vileCivilianleft45hurt = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/vileCivilianhurt(45 left).png");
	public static Render vileCivilianlefthurt = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/vileCivilianhurt(left).png");
	public static Render vileCivilianright135hurt = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/vileCivilianhurt(135 right).png");
	public static Render vileCivilianleft135hurt = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/vileCivilianhurt(135 left).png");
	public static Render vileCivilianbackhurt = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/vileCivilianhurt(back).png");
	public static Render vileCivilianAttack145left = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/vileCivilianAttack1(45 left).png");
	public static Render vileCivilianAttack245left = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/vileCivilianAttack2(45 left).png");
	public static Render vileCivilianAttack1left = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/vileCivilianAttack1(left).png");
	public static Render vileCivilianAttack2left = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/vileCivilianAttack2(left).png");
	public static Render vileCivilianAttack1135left = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/vileCivilianAttack1(135 left).png");
	public static Render vileCivilianAttack2135left = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/vileCivilianAttack2(135 left).png");
	public static Render vileCivilianAttack1back = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/vileCivilianAttack1(back).png");
	public static Render vileCivilianAttack2back = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/vileCivilianAttack2(back).png");
	public static Render vileCivilianAttack145right = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/vileCivilianAttack1(45 right).png");
	public static Render vileCivilianAttack245right = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/vileCivilianAttack2(45 right).png");
	public static Render vileCivilianAttack1right = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/vileCivilianAttack1(right).png");
	public static Render vileCivilianAttack2right = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/vileCivilianAttack2(right).png");
	public static Render vileCivilianAttack1135right = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/vileCivilianAttack1(135 right).png");
	public static Render vileCivilianAttack2135right = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/vileCivilianAttack2(135 right).png");
	public static Render enemy4corpse1 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy4corpse1.png");
	public static Render enemy4corpse2 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy4corpse2.png");
	public static Render enemy4corpse3 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy4corpse3.png");
	public static Render enemy5hurt = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy5hurt.png");
	public static Render enemy5a45left = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy5-1(45 left).png");
	public static Render enemy5b45left = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy5-2(45 left).png");
	public static Render enemy5c45left = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy5-3(45 left).png");
	public static Render enemy5d45left = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy5-4(45 left).png");
	public static Render enemy5firea45left = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy5fire1(45 left).png");
	public static Render enemy5fireb45left = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy5fire2(45 left).png");
	public static Render enemy5hurt45left = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy5hurt(45 left).png");
	public static Render enemy5aleft = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy5-1(left).png");
	public static Render enemy5bleft = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy5-2(left).png");
	public static Render enemy5cleft = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy5-3(left).png");
	public static Render enemy5dleft = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy5-4(left).png");
	public static Render enemy5firealeft = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy5fire1(left).png");
	public static Render enemy5firebleft = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy5fire2(left).png");
	public static Render enemy5hurtleft = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy5hurt(left).png");
	public static Render enemy5a135left = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy5-1(135 left).png");
	public static Render enemy5b135left = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy5-2(135 left).png");
	public static Render enemy5c135left = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy5-3(135 left).png");
	public static Render enemy5d135left = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy5-4(135 left).png");
	public static Render enemy5firea135left = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy5fire1(135 left).png");
	public static Render enemy5fireb135left = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy5fire2(135 left).png");
	public static Render enemy5hurt135left = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy5hurt(135 left).png");
	public static Render enemy5aback = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy5-1(back).png");
	public static Render enemy5bback = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy5-2(back).png");
	public static Render enemy5cback = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy5-3(back).png");
	public static Render enemy5dback = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy5-4(back).png");
	public static Render enemy5fireaback = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy5fire1(back).png");
	public static Render enemy5firebback = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy5fire2(back).png");
	public static Render enemy5hurtback = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy5hurt(back).png");
	public static Render enemy5a45right = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy5-1(45 right).png");
	public static Render enemy5b45right = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy5-2(45 right).png");
	public static Render enemy5c45right = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy5-3(45 right).png");
	public static Render enemy5d45right = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy5-4(45 right).png");
	public static Render enemy5firea45right = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy5fire1(45 right).png");
	public static Render enemy5fireb45right = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy5fire2(45 right).png");
	public static Render enemy5hurt45right = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy5hurt(45 right).png");
	public static Render enemy5aright = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy5-1(right).png");
	public static Render enemy5bright = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy5-2(right).png");
	public static Render enemy5cright = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy5-3(right).png");
	public static Render enemy5dright = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy5-4(right).png");
	public static Render enemy5firearight = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy5fire1(right).png");
	public static Render enemy5firebright = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy5fire2(right).png");
	public static Render enemy5hurtright = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy5hurt(right).png");
	public static Render enemy5a135right = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy5-1(135 right).png");
	public static Render enemy5b135right = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy5-2(135 right).png");
	public static Render enemy5c135right = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy5-3(135 right).png");
	public static Render enemy5d135right = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy5-4(135 right).png");
	public static Render enemy5firea135right = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy5fire1(135 right).png");
	public static Render enemy5fireb135right = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy5fire2(135 right).png");
	public static Render enemy5hurt135right = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy5hurt(135 right).png");
	public static Render enemy3corpse1 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy3corpse1.png");
	public static Render enemy3corpse2 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy3corpse2.png");
	public static Render enemy3corpse3 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy3corpse3.png");
	public static Render enemy3corpse4 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy3corpse4.png");
	public static Render enemy3corpse5 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy3corpse5.png");
	public static Render enemy3corpse6 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy3corpse6.png");
	public static Render enemy3corpse7 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy3corpse7.png");
	public static Render enemy3corpse8 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy3corpse8.png");
	public static Render enemy3corpse9 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy3corpse9.png");
	public static Render enemy3a45left = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy3-1(45 left).png");
	public static Render enemy3b45left = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy3-2(45 left).png");
	public static Render enemy3c45left = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy3-3(45 left).png");
	public static Render enemy3d45left = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy3-4(45 left).png");
	public static Render enemy3e45left = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy3-5(45 left).png");
	public static Render enemy3f45left = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy3-6(45 left).png");
	public static Render enemy3fire1left45 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy3fire1(45 left).png");
	public static Render enemy3fire2left45 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy3fire2(45 left).png");
	public static Render enemy3fire3left45 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy3fire3(45 left).png");
	public static Render enemy3hurt45left = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy3hurt(45 left).png");
	public static Render enemy3a45right = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy3-1(45 right).png");
	public static Render enemy3b45right = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy3-2(45 right).png");
	public static Render enemy3c45right = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy3-3(45 right).png");
	public static Render enemy3d45right = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy3-4(45 right).png");
	public static Render enemy3e45right = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy3-5(45 right).png");
	public static Render enemy3f45right = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy3-6(45 right).png");
	public static Render enemy3fire1right45 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy3fire1(45 right).png");
	public static Render enemy3fire2right45 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy3fire2(45 right).png");
	public static Render enemy3fire3right45 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy3fire3(45 right).png");
	public static Render enemy3hurt45right = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy3hurt(45 right).png");
	public static Render enemy3aleft = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy3-1(left).png");
	public static Render enemy3bleft = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy3-2(left).png");
	public static Render enemy3cleft = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy3-3(left).png");
	public static Render enemy3dleft = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy3-4(left).png");
	public static Render enemy3eleft = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy3-5(left).png");
	public static Render enemy3fleft = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy3-6(left).png");
	public static Render enemy3fire1left = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy3fire1(left).png");
	public static Render enemy3fire2left = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy3fire2(left).png");
	public static Render enemy3fire3left = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy3fire3(left).png");
	public static Render enemy3hurtleft = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy3hurt(left).png");
	public static Render enemy3aright = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy3-1(right).png");
	public static Render enemy3bright = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy3-2(right).png");
	public static Render enemy3cright = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy3-3(right).png");
	public static Render enemy3dright = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy3-4(right).png");
	public static Render enemy3eright = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy3-5(right).png");
	public static Render enemy3fright = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy3-6(right).png");
	public static Render enemy3fire1right = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy3fire1(right).png");
	public static Render enemy3fire2right = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy3fire2(right).png");
	public static Render enemy3fire3right = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy3fire3(right).png");
	public static Render enemy3hurtright = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy3hurt(right).png");
	public static Render enemy3a135right = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy3-1(135 right).png");
	public static Render enemy3b135right = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy3-2(135 right).png");
	public static Render enemy3c135right = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy3-3(135 right).png");
	public static Render enemy3d135right = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy3-4(135 right).png");
	public static Render enemy3e135right = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy3-5(135 right).png");
	public static Render enemy3f135right = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy3-6(135 right).png");
	public static Render enemy3fire1right135 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy3fire1(135 right).png");
	public static Render enemy3fire2right135 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy3fire2(135 right).png");
	public static Render enemy3fire3right135 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy3fire3(135 right).png");
	public static Render enemy3hurt135right = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy3hurt(135 right).png");
	public static Render enemy3a135left = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy3-1(135 left).png");
	public static Render enemy3b135left = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy3-2(135 left).png");
	public static Render enemy3c135left = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy3-3(135 left).png");
	public static Render enemy3d135left = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy3-4(135 left).png");
	public static Render enemy3e135left = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy3-5(135 left).png");
	public static Render enemy3f135left = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy3-6(135 left).png");
	public static Render enemy3fire1left135 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy3fire1(135 left).png");
	public static Render enemy3fire2left135 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy3fire2(135 left).png");
	public static Render enemy3fire3left135 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy3fire3(135 left).png");
	public static Render enemy3hurt135left = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy3hurt(135 left).png");
	public static Render enemy3aback = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy3-1(back).png");
	public static Render enemy3bback = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy3-2(back).png");
	public static Render enemy3cback = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy3-3(back).png");
	public static Render enemy3dback = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy3-4(back).png");
	public static Render enemy3eback = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy3-5(back).png");
	public static Render enemy3fback = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy3-6(back).png");
	public static Render enemy3fire1back = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy3fire1(back).png");
	public static Render enemy3fire2back = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy3fire2(back).png");
	public static Render enemy3fire3back = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy3fire3(back).png");
	public static Render enemy3hurtback = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/enemy3hurt(back).png");
	public static Render defaultCorpse1 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/defaultCorpse1.png");
	public static Render defaultCorpse2 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/defaultCorpse2.png");
	public static Render defaultCorpse3 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/defaultCorpse3.png");
	public static Render defaultCorpse4 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/defaultCorpse4.png");
	public static Render defaultCorpse5 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/defaultCorpse5.png");
	public static Render defaultCorpse6 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/entities/defaultCorpse6.png");

	///////////////////////////////////////////// PROJECTILES
	public static Render phaser = loadBitMap("resources" + FPSLauncher.themeName + "/textures/projectiles/phaser.png");
	public static Render bullet = loadBitMap("resources" + FPSLauncher.themeName + "/textures/projectiles/bullet.png");
	public static Render canExplode1 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/projectiles/canExplode1.png");
	public static Render canExplode2 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/projectiles/canExplode2.png");
	public static Render canExplode3 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/projectiles/canExplode3.png");
	public static Render canExplode4 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/projectiles/canExplode4.png");
	public static Render defaultFireball = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/projectiles/fireball.png");
	public static Render giantFireball = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/projectiles/giantFireball.png");
	public static Render electricShock = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/projectiles/electricShock.png");
	public static Render electroBall = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/projectiles/electroBall.png");
	public static Render rocket = loadBitMap("resources" + FPSLauncher.themeName + "/textures/projectiles/rocket.png");
	public static Render explosion1 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/projectiles/explosion1.png");
	public static Render explosion2 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/projectiles/explosion2.png");
	public static Render explosion3 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/projectiles/explosion3.png");
	public static Render explosion4 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/projectiles/explosion4.png");
	public static Render explosion5 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/projectiles/explosion5.png");
	public static Render explosion6 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/projectiles/explosion6.png");
	public static Render explosion7 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/projectiles/explosion7.png");
	public static Render explosion8 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/projectiles/explosion8.png");
	public static Render phaseHit1 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/projectiles/phaseHit1.png");
	public static Render phaseHit2 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/projectiles/phaseHit2.png");
	public static Render phaseHit3 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/projectiles/phaseHit3.png");
	public static Render phaseHit4 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/projectiles/phaseHit4.png");
	public static Render phaseHit5 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/projectiles/phaseHit5.png");
	public static Render fireHit1 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/projectiles/fireHit1.png");
	public static Render fireHit2 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/projectiles/fireHit2.png");
	public static Render fireHit3 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/projectiles/fireHit3.png");
	public static Render fireHit4 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/projectiles/fireHit4.png");
	public static Render bulletHit1 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/projectiles/bulletHit1.png");
	public static Render bulletHit2 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/projectiles/bulletHit2.png");
	public static Render bulletHit3 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/projectiles/bulletHit3.png");
	public static Render bulletHit4 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/projectiles/bulletHit4.png");
	public static Render bloodSpray1 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/projectiles/bloodSpray1.png");
	public static Render bloodSpray2 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/projectiles/bloodSpray2.png");
	public static Render bloodSpray3 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/projectiles/bloodSpray3.png");
	public static Render bloodSpray4 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/projectiles/bloodSpray4.png");
	public static Render criticalHit1 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/projectiles/criticalSpray1.png");
	public static Render criticalHit2 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/projectiles/criticalSpray2.png");
	public static Render criticalHit3 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/projectiles/criticalSpray3.png");
	public static Render criticalHit4 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/projectiles/criticalSpray4.png");
	public static Render criticalHit5 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/projectiles/criticalSpray5.png");
	public static Render criticalHit6 = loadBitMap(
			"resources" + FPSLauncher.themeName + "/textures/projectiles/criticalSpray6.png");

	// If image fails to load twice, then have a default image
	private static int failCounter = 0;

	/**
	 * Constructs floor textures since they require a bit more optimized version of
	 * instantiation
	 */
	public static void Textures() {
		try {
			floors = null;
			// Array of floor textures
			floors = new Render[(int) Files.list(Paths.get("resources" + FPSLauncher.themeName + "/textures/floors"))
					.count()];

			// Adds all floor textures in order in the array
			for (int i = 0; i < floors.length; i++) {
				floors[i] = loadBitMap(
						"resources" + FPSLauncher.themeName + "/textures/floors/floor" + (i + 1) + ".png");
			}
		} catch (Exception e) {
			try {
				// Array of floor textures
				floors = new Render[(int) Files.list(Paths.get("resources/default/textures/floors")).count()];

				// Adds all floor textures in order in the array
				for (int i = 0; i < floors.length; i++) {
					floors[i] = loadBitMap("resources/default/textures/floors/floor" + (i + 1) + ".png");
				}
			} catch (Exception ex) {
				// Nothing worked.
			}
		}
	}

	/**
	 * Finds the texture called for in the file, then creates a Render object out of
	 * it so that it renders on the screen.
	 * 
	 * @param fileName
	 * @return
	 */
	public static Render loadBitMap(String fileName) {
		try {
			BufferedImage image = ImageIO.read(new File(fileName));

			failCounter = 0;

			int width = image.getWidth();
			int height = image.getHeight();

			Render result = new Render(width, height);

			image.getRGB(0, 0, width, height, result.PIXELS, 0, width);

			return result;
		} catch (Exception e) {
			failCounter++;

			if (failCounter == 1) {
				// Splits directories into parts
				String[] parts = fileName.split("/");

				// Reset the resource pack to default
				parts[1] = "/default";

				fileName = "";

				// Add new file path as the new fileName
				for (int i = 0; i < parts.length; i++) {
					if (i > 1) {
						fileName += ("/" + parts[i]);
					} else {
						fileName += parts[i];
					}
				}

				// Try to load again.
				return loadBitMap(fileName);
			} else {
				failCounter = 0;
				return new Render(256, 256);
			}
		}
	}

	/**
	 * Resets the textures for each game so that textures do not stay the same from
	 * theme to theme.
	 */
	public static void resetTextures() {
		//////////////////////////////////////// WALLS
		wall1 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/walls/wall1.png");
		wall2 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/walls/wall2.png");
		wall3 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/walls/wall3.png");
		wall4 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/walls/wall4.png");
		wall4damaged1 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/walls/wall4damage1.png");
		wall4damaged2 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/walls/wall4damage2.png");
		wall4damaged3 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/walls/wall4damage3.png");
		wall5 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/walls/wall5.png");
		wall6 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/walls/wall6.png");
		wall7 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/walls/wall7.png");
		wall8 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/walls/wall8.png");
		wall9 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/walls/wall9.png");
		wall10 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/walls/wall10.png");
		wall11 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/walls/wall11.png");
		wall12 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/walls/wall12.png");
		wall13Phase1 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/walls/wall13phase1.png");
		wall13Phase2 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/walls/wall13phase2.png");
		wall14 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/walls/wall14.png");
		teleportEnter = loadBitMap("resources" + FPSLauncher.themeName + "/textures/walls/teleporterEnter.png");
		teleportExit = loadBitMap("resources" + FPSLauncher.themeName + "/textures/walls/teleporterExit.png");
		tutorialWall = loadBitMap("resources" + FPSLauncher.themeName + "/textures/walls/tutorialWall.png");
		tutorialWall2 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/walls/tutorialWall2.png");
		tutorialWall3 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/walls/tutorialWall3.png");
		tutorialWall4 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/walls/tutorialWall4.png");
		tutorialWall5 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/walls/tutorialWall5.png");
		wall15Phase5 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/walls/wall15phase5.png");
		wall15Phase4 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/walls/wall15phase4.png");
		wall15Phase3 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/walls/wall15phase3.png");
		wall15Phase2 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/walls/wall15phase2.png");
		wall15Phase1 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/walls/wall15phase1.png");

		// Toxic Waste/Lava. Has many phases
		toxic1 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/walls/toxicWaste16.png");
		toxic2 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/walls/toxicWaste15.png");
		toxic3 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/walls/toxicWaste14.png");
		toxic4 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/walls/toxicWaste13.png");
		toxic5 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/walls/toxicWaste12.png");
		toxic6 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/walls/toxicWaste11.png");
		toxic7 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/walls/toxicWaste10.png");
		toxic8 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/walls/toxicWaste9.png");
		toxic9 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/walls/toxicWaste8.png");
		toxic10 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/walls/toxicWaste7.png");
		toxic11 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/walls/toxicWaste6.png");
		toxic12 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/walls/toxicWaste5.png");
		toxic13 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/walls/toxicWaste4.png");
		toxic14 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/walls/toxicWaste3.png");
		toxic15 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/walls/toxicWaste2.png");
		toxic16 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/walls/toxicWaste1.png");
		coolWall = loadBitMap("resources" + FPSLauncher.themeName + "/textures/walls/coolWall.png");
		spine1 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/walls/spineWall1.png");
		spine2 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/walls/spineWall2.png");
		spine3 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/walls/spineWall3.png");
		spine4 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/walls/spineWall4.png");
		spine5 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/walls/spineWall5.png");
		spine6 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/walls/spineWall6.png");
		spine7 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/walls/spineWall7.png");
		spine8 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/walls/spineWall8.png");
		mlg = loadBitMap("resources" + FPSLauncher.themeName + "/textures/walls/mlg.png");
		box = loadBitMap("resources" + FPSLauncher.themeName + "/textures/walls/boxWall.png");
		woodenWall = loadBitMap("resources" + FPSLauncher.themeName + "/textures/walls/woodWall.png");
		bloodWall = loadBitMap("resources" + FPSLauncher.themeName + "/textures/walls/bloodWall.png");
		marble = loadBitMap("resources" + FPSLauncher.themeName + "/textures/walls/marble.png");
		normButton = loadBitMap("resources" + FPSLauncher.themeName + "/textures/walls/normButton.png");
		normButtonOn = loadBitMap("resources" + FPSLauncher.themeName + "/textures/walls/normButtonOn.png");
		wall35 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/walls/wall35.png");
		wall36 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/walls/wall36.png");
		wall37 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/walls/wall37.png");
		wall38 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/walls/wall38.png");
		wall39 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/walls/wall39.png");
		wall40 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/walls/wall40.png");
		wall41 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/walls/wall41.png");
		wall42a = loadBitMap("resources" + FPSLauncher.themeName + "/textures/walls/wall42-1.png");
		wall42b = loadBitMap("resources" + FPSLauncher.themeName + "/textures/walls/wall42-2.png");
		wall42c = loadBitMap("resources" + FPSLauncher.themeName + "/textures/walls/wall42-3.png");
		wall42d = loadBitMap("resources" + FPSLauncher.themeName + "/textures/walls/wall42-4.png");

		//////////////////////////////////////////////// ITEMS
		health = loadBitMap("resources" + FPSLauncher.themeName + "/textures/items/health.png");
		shell = loadBitMap("resources" + FPSLauncher.themeName + "/textures/items/shell.png");
		megaPhase1 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/items/megahealthphase1.png");
		megaPhase2 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/items/megahealthphase2.png");
		megaPhase3 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/items/megahealthphase3.png");
		megaPhase4 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/items/megahealthphase4.png");
		redKey = loadBitMap("resources" + FPSLauncher.themeName + "/textures/items/redKey.png");
		blueKey = loadBitMap("resources" + FPSLauncher.themeName + "/textures/items/blueKey.png");
		greenKey = loadBitMap("resources" + FPSLauncher.themeName + "/textures/items/greenKey.png");
		yellowKey = loadBitMap("resources" + FPSLauncher.themeName + "/textures/items/yellowKey.png");
		shotgun = loadBitMap("resources" + FPSLauncher.themeName + "/textures/items/shotgun.png");
		resurrect1 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/items/resurrectSkull1.png");
		resurrect2 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/items/resurrectSkull2.png");
		resurrect3 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/items/resurrectSkull3.png");
		environSuit = loadBitMap("resources" + FPSLauncher.themeName + "/textures/items/environSuit.png");
		goblet1 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/items/goblet1.png");
		goblet2 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/items/goblet2.png");
		goblet3 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/items/goblet3.png");
		adrenaline = loadBitMap("resources" + FPSLauncher.themeName + "/textures/items/adrenaline.png");
		glasses = loadBitMap("resources" + FPSLauncher.themeName + "/textures/items/glasses.png");
		torch1 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/items/torch1.png");
		torch2 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/items/torch2.png");
		torch3 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/items/torch3.png");
		torch4 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/items/torch4.png");
		lamp1 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/items/lamp1.png");
		lamp2 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/items/lamp2.png");
		lamp3 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/items/lamp3.png");
		tree = loadBitMap("resources" + FPSLauncher.themeName + "/textures/items/tree.png");
		canister = loadBitMap("resources" + FPSLauncher.themeName + "/textures/items/canister.png");
		chainmeal = loadBitMap("resources" + FPSLauncher.themeName + "/textures/items/chainmealArmor.png");
		combat = loadBitMap("resources" + FPSLauncher.themeName + "/textures/items/combatArmor.png");
		argent = loadBitMap("resources" + FPSLauncher.themeName + "/textures/items/argentArmor.png");
		shard = loadBitMap("resources" + FPSLauncher.themeName + "/textures/items/armorShard.png");
		vial = loadBitMap("resources" + FPSLauncher.themeName + "/textures/items/healthVial.png");
		upgrade1 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/items/weaponUpgrade1.png");
		upgrade2 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/items/weaponUpgrade2.png");
		upgrade3 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/items/weaponUpgrade3.png");
		upgrade4 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/items/weaponUpgrade4.png");
		holyWater1 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/items/holyWater1.png");
		holyWater2 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/items/holyWater2.png");
		table = loadBitMap("resources" + FPSLauncher.themeName + "/textures/items/table.png");
		lampTable = loadBitMap("resources" + FPSLauncher.themeName + "/textures/items/lampTable.png");
		scepter = loadBitMap("resources" + FPSLauncher.themeName + "/textures/items/decietScepter.png");
		invisEmerald = loadBitMap("resources" + FPSLauncher.themeName + "/textures/items/invisibilityEmerald.png");
		invisEmerald2 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/items/invisibilityEmerald2.png");
		invisEmerald3 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/items/invisibilityEmerald3.png");
		invisEmerald4 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/items/invisibilityEmerald4.png");
		shellBox = loadBitMap("resources" + FPSLauncher.themeName + "/textures/items/shellBox.png");
		chargePack = loadBitMap("resources" + FPSLauncher.themeName + "/textures/items/chargePack.png");
		largeChargePack = loadBitMap("resources" + FPSLauncher.themeName + "/textures/items/chargePackLarge.png");
		phaseCannon = loadBitMap("resources" + FPSLauncher.themeName + "/textures/items/phaseCannonWeapon.png");
		sat1 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/items/SatDish1.png");
		sat2 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/items/SatDish2.png");
		pistol = loadBitMap("resources" + FPSLauncher.themeName + "/textures/items/pistol.png");
		clip = loadBitMap("resources" + FPSLauncher.themeName + "/textures/items/clip.png");
		bullets = loadBitMap("resources" + FPSLauncher.themeName + "/textures/items/boxOfBullets.png");
		rockets = loadBitMap("resources" + FPSLauncher.themeName + "/textures/items/rockets.png");
		rocketLaucher = loadBitMap("resources" + FPSLauncher.themeName + "/textures/items/rocketLauncher.png");
		rocketCrate = loadBitMap("resources" + FPSLauncher.themeName + "/textures/items/rocketCrate.png");
		bonePile = loadBitMap("resources" + FPSLauncher.themeName + "/textures/items/bonePile.png");
		tEnter1 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/items/tEnter1.png");
		tEnter2 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/items/tEnter2.png");
		tEnter3 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/items/tEnter3.png");
		tEnter4 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/items/tEnter4.png");
		tExit1 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/items/tExit1.png");
		tExit2 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/items/tExit2.png");
		tExit3 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/items/tExit3.png");
		tExit4 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/items/tExit4.png");
		darkBook = loadBitMap("resources" + FPSLauncher.themeName + "/textures/items/darkBook.png");
		turret = loadBitMap("resources" + FPSLauncher.themeName + "/textures/items/turret.png");
		marine1 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/items/marine1.png");
		marine2 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/items/marine2.png");
		marine3 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/items/marine3.png");
		marine4 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/items/marine4.png");
		marine5 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/items/marine5.png");

		/////////////////////////////////////////////// ENTITIES
		enemy1 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy1.png");
		enemy2 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy2.png");
		enemy3 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy3.png");
		enemy4 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy4.png");
		morgoth = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/morgoth.png");
		enemy5 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy5.png");
		vileCiv1 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/vileCivilian.png");
		vileCiv2 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/vileCivilian2.png");
		vileCiv3 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/vileCivilian3.png");
		vileCiv4 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/vileCivilian4.png");
		vileCivAttack1 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/vileCivilianAttack1.png");
		vileCivAttack2 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/vileCivilianAttack2.png");
		vileCivHurt = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/vileCivilianHurt.png");
		belegoth = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/belegoth.png");
		corpse1 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/corpse1.png");
		corpse2 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/corpse2.png");
		corpse3 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/corpse3.png");
		corpse4 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/corpse4.png");
		corpse5 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/corpse5.png");
		corpse6 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/corpse6.png");
		corpse7 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/corpse7.png");
		corpse8 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/corpse8.png");
		corpseType2 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/corpseType2.png");
		enemy4b = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy4-2.png");
		enemy3b = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy3-2.png");
		enemy3c = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy3-3.png");
		enemy3d = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy3-4.png");
		enemy3e = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy3-5.png");
		enemy3f = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy3-6.png");
		enemy1b = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy1-2.png");
		enemy1corpse1 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy1corpse1.png");
		enemy1corpse2 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy1corpse2.png");
		enemy1corpse3 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy1corpse3.png");
		enemy1corpse = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy1corpse.png");
		enemy1hurt = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy1hurt.png");
		enemy1fire1 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy1fire1.png");
		enemy1fire2 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy1fire2.png");
		enemy1fire3 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy1fire3.png");
		enemy1fire4 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy1fire4.png");
		enemy5b = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy5-2.png");
		enemy5corpse1 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy5corpse1.png");
		enemy5corpse2 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy5corpse2.png");
		enemy5corpse3 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy5corpse3.png");
		enemy5corpse4 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy5corpse4.png");
		enemy5corpse5 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy5corpse5.png");
		enemy5corpse6 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy5corpse6.png");
		enemy5corpse7 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy5corpse7.png");
		enemy5corpse8 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy5corpse8.png");
		enemy5corpse9 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy5corpse9.png");
		enemy5corpse10 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy5corpse10.png");
		enemy5corpse11 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy5corpse11.png");
		enemy5corpse12 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy5corpse12.png");
		enemy5corpse13 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy5corpse13.png");
		enemy5corpse14 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy5corpse14.png");
		enemy5corpse15 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy5corpse15.png");
		enemy5corpse16 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy5corpse16.png");
		enemy5corpse17 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy5corpse17.png");
		enemy4corpse = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy4corpse.png");
		enemy5c = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy5-3.png");
		enemy5d = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy5-4.png");
		enemy5corpse = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy5corpse.png");
		enemy5fire1 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy5fire1.png");
		enemy5fire2 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy5fire2.png");
		enemy3corpse = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy3corpse.png");
		enemy3fire1 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy3fire1.png");
		enemy3fire2 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy3fire2.png");
		enemy3fire3 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy3fire3.png");
		enemy3hurt = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy3hurt.png");
		enemy2hurt = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy2hurt.png");
		enemy2corpse1 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy2corpse1.png");
		enemy2corpse2 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy2corpse2.png");
		enemy2corpse3 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy2corpse3.png");
		enemy2corpse4 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy2corpse4.png");
		enemy2corpse5 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy2corpse5.png");
		enemy2corpse6 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy2corpse6.png");
		enemy2corpse = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy2corpse.png");
		enemy2fire1 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy2fire.png");
		belegothCorpse1 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/belegothCorpse1.png");
		belegothCorpse2 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/belegothCorpse2.png");
		belegothCorpse3 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/belegothCorpse3.png");
		belegothCorpse4 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/belegothCorpse4.png");
		belegothCorpse5 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/belegothCorpse5.png");
		belegothCorpse6 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/belegothCorpse6.png");
		belegothCorpse7 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/belegothCorpse7.png");
		belegothCorpse8 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/belegothCorpse8.png");
		belegothCorpse9 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/belegothCorpse9.png");
		belegothCorpse10 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/belegothCorpse10.png");
		belegothCorpse11 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/belegothCorpse11.png");
		belegothCorpse12 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/belegothCorpse12.png");
		belegothCorpse13 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/belegothCorpse13.png");
		belegothCorpse = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/belegothCorpse14.png");
		belegothHurt = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/belegothHurt.png");
		belegothMelee = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/belegothMelee.png");
		belegoth2 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/belegoth2.png");
		belegoth3 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/belegoth3.png");
		belegoth4 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/belegoth4.png");
		belegoth5 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/belegoth5.png");
		belegoth6 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/belegoth6.png");
		belegothAttack1 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/belegothAttack1.png");
		belegothAttack2 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/belegothAttack2.png");
		belegothAttack3 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/belegothAttack3.png");
		belegothAttack4 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/belegothAttack4.png");
		morgothFire1 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/morgothFire1.png");
		morgothFire2 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/morgothFire2.png");
		morgothMelee = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/morgothMelee.png");
		morgoth2 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/morgoth2.png");
		morgoth3 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/morgoth3.png");
		morgoth4 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/morgoth4.png");
		morgothHurt = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/morgothHurt.png");
		enemy2left45 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy2(45 left).png");
		enemy2left = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy2(left).png");
		enemy2left135 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy2(135 left).png");
		enemy2back = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy2back.png");
		enemy2right45 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy2(45 right).png");
		enemy2right = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy2(right).png");
		enemy2right135 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy2(135 right).png");
		enemy2left45fire = loadBitMap(
				"resources" + FPSLauncher.themeName + "/textures/entities/enemy2fire(45 left).png");
		enemy2left135fire = loadBitMap(
				"resources" + FPSLauncher.themeName + "/textures/entities/enemy2fire(135 left).png");
		enemy2backfire = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy2fire(back).png");
		enemy2right45fire = loadBitMap(
				"resources" + FPSLauncher.themeName + "/textures/entities/enemy2fire(45 right).png");
		enemy2rightfire = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy2fire(right).png");
		enemy2leftfire = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy2fire(left).png");
		enemy2right135fire = loadBitMap(
				"resources" + FPSLauncher.themeName + "/textures/entities/enemy2fire(135 right).png");
		enemy1left45 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy1(45 left).png");
		enemy1left = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy1(left).png");
		enemy1left135 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy1(135 left).png");
		enemy1back = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy1(back).png");
		enemy1right45 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy1(45 right).png");
		enemy1right = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy1(right).png");
		enemy1right135 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy1(135 right).png");
		enemy1left45fire1 = loadBitMap(
				"resources" + FPSLauncher.themeName + "/textures/entities/enemy1fire1(45 left).png");
		enemy1left45fire2 = loadBitMap(
				"resources" + FPSLauncher.themeName + "/textures/entities/enemy1fire2(45 left).png");
		enemy1left45fire3 = loadBitMap(
				"resources" + FPSLauncher.themeName + "/textures/entities/enemy1fire3(45 left).png");
		enemy1left45fire4 = loadBitMap(
				"resources" + FPSLauncher.themeName + "/textures/entities/enemy1fire4(45 left).png");
		enemy1leftfire = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy1fire(left).png");
		enemy1right45fire1 = loadBitMap(
				"resources" + FPSLauncher.themeName + "/textures/entities/enemy1fire1(45 right).png");
		enemy1right45fire2 = loadBitMap(
				"resources" + FPSLauncher.themeName + "/textures/entities/enemy1fire2(45 right).png");
		enemy1right45fire3 = loadBitMap(
				"resources" + FPSLauncher.themeName + "/textures/entities/enemy1fire3(45 right).png");
		enemy1right45fire4 = loadBitMap(
				"resources" + FPSLauncher.themeName + "/textures/entities/enemy1fire4(45 right).png");
		enemy1rightfire = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy1fire(right).png");
		enemy1right45hurt = loadBitMap(
				"resources" + FPSLauncher.themeName + "/textures/entities/enemy1hurt(45 right).png");
		enemy1righthurt = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy1hurt(right).png");
		enemy1left45hurt = loadBitMap(
				"resources" + FPSLauncher.themeName + "/textures/entities/enemy1hurt(45 left).png");
		enemy1lefthurt = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy1hurt(left).png");
		vileCivilian145left = loadBitMap(
				"resources" + FPSLauncher.themeName + "/textures/entities/vileCivilian1(45 left).png");
		vileCivilian245left = loadBitMap(
				"resources" + FPSLauncher.themeName + "/textures/entities/vileCivilian2(45 left).png");
		vileCivilian345left = loadBitMap(
				"resources" + FPSLauncher.themeName + "/textures/entities/vileCivilian3(45 left).png");
		vileCivilian445left = loadBitMap(
				"resources" + FPSLauncher.themeName + "/textures/entities/vileCivilian4(45 left).png");
		vileCivilian1left = loadBitMap(
				"resources" + FPSLauncher.themeName + "/textures/entities/vileCivilian1(left).png");
		vileCivilian2left = loadBitMap(
				"resources" + FPSLauncher.themeName + "/textures/entities/vileCivilian2(left).png");
		vileCivilian3left = loadBitMap(
				"resources" + FPSLauncher.themeName + "/textures/entities/vileCivilian3(left).png");
		vileCivilian4left = loadBitMap(
				"resources" + FPSLauncher.themeName + "/textures/entities/vileCivilian4(left).png");
		vileCivilian1135left = loadBitMap(
				"resources" + FPSLauncher.themeName + "/textures/entities/vileCivilian1(135 left).png");
		vileCivilian2135left = loadBitMap(
				"resources" + FPSLauncher.themeName + "/textures/entities/vileCivilian2(135 left).png");
		vileCivilian3135left = loadBitMap(
				"resources" + FPSLauncher.themeName + "/textures/entities/vileCivilian3(135 left).png");
		vileCivilian4135left = loadBitMap(
				"resources" + FPSLauncher.themeName + "/textures/entities/vileCivilian4(135 left).png");
		vileCivilian1back = loadBitMap(
				"resources" + FPSLauncher.themeName + "/textures/entities/vileCivilian1(back).png");
		vileCivilian2back = loadBitMap(
				"resources" + FPSLauncher.themeName + "/textures/entities/vileCivilian2(back).png");
		vileCivilian3back = loadBitMap(
				"resources" + FPSLauncher.themeName + "/textures/entities/vileCivilian3(back).png");
		vileCivilian4back = loadBitMap(
				"resources" + FPSLauncher.themeName + "/textures/entities/vileCivilian4(back).png");
		vileCivilian145right = loadBitMap(
				"resources" + FPSLauncher.themeName + "/textures/entities/vileCivilian1(45 right).png");
		vileCivilian245right = loadBitMap(
				"resources" + FPSLauncher.themeName + "/textures/entities/vileCivilian2(45 right).png");
		vileCivilian345right = loadBitMap(
				"resources" + FPSLauncher.themeName + "/textures/entities/vileCivilian3(45 right).png");
		vileCivilian445right = loadBitMap(
				"resources" + FPSLauncher.themeName + "/textures/entities/vileCivilian4(45 right).png");
		vileCivilian1right = loadBitMap(
				"resources" + FPSLauncher.themeName + "/textures/entities/vileCivilian1(right).png");
		vileCivilian2right = loadBitMap(
				"resources" + FPSLauncher.themeName + "/textures/entities/vileCivilian2(right).png");
		vileCivilian3right = loadBitMap(
				"resources" + FPSLauncher.themeName + "/textures/entities/vileCivilian3(right).png");
		vileCivilian4right = loadBitMap(
				"resources" + FPSLauncher.themeName + "/textures/entities/vileCivilian4(right).png");
		vileCivilian1135right = loadBitMap(
				"resources" + FPSLauncher.themeName + "/textures/entities/vileCivilian1(135 right).png");
		vileCivilian2135right = loadBitMap(
				"resources" + FPSLauncher.themeName + "/textures/entities/vileCivilian2(135 right).png");
		vileCivilian3135right = loadBitMap(
				"resources" + FPSLauncher.themeName + "/textures/entities/vileCivilian3(135 right).png");
		vileCivilian4135right = loadBitMap(
				"resources" + FPSLauncher.themeName + "/textures/entities/vileCivilian4(135 right).png");
		vileCivilianright45hurt = loadBitMap(
				"resources" + FPSLauncher.themeName + "/textures/entities/vileCivilianhurt(45 right).png");
		vileCivilianrighthurt = loadBitMap(
				"resources" + FPSLauncher.themeName + "/textures/entities/vileCivilianhurt(right).png");
		vileCivilianleft45hurt = loadBitMap(
				"resources" + FPSLauncher.themeName + "/textures/entities/vileCivilianhurt(45 left).png");
		vileCivilianlefthurt = loadBitMap(
				"resources" + FPSLauncher.themeName + "/textures/entities/vileCivilianhurt(left).png");
		vileCivilianright135hurt = loadBitMap(
				"resources" + FPSLauncher.themeName + "/textures/entities/vileCivilianhurt(135 right).png");
		vileCivilianleft135hurt = loadBitMap(
				"resources" + FPSLauncher.themeName + "/textures/entities/vileCivilianhurt(135 left).png");
		vileCivilianbackhurt = loadBitMap(
				"resources" + FPSLauncher.themeName + "/textures/entities/vileCivilianhurt(back).png");
		vileCivilianAttack145left = loadBitMap(
				"resources" + FPSLauncher.themeName + "/textures/entities/vileCivilianAttack1(45 left).png");
		vileCivilianAttack245left = loadBitMap(
				"resources" + FPSLauncher.themeName + "/textures/entities/vileCivilianAttack2(45 left).png");
		vileCivilianAttack1left = loadBitMap(
				"resources" + FPSLauncher.themeName + "/textures/entities/vileCivilianAttack1(left).png");
		vileCivilianAttack2left = loadBitMap(
				"resources" + FPSLauncher.themeName + "/textures/entities/vileCivilianAttack2(left).png");
		vileCivilianAttack1135left = loadBitMap(
				"resources" + FPSLauncher.themeName + "/textures/entities/vileCivilianAttack1(135 left).png");
		vileCivilianAttack2135left = loadBitMap(
				"resources" + FPSLauncher.themeName + "/textures/entities/vileCivilianAttack2(135 left).png");
		vileCivilianAttack1back = loadBitMap(
				"resources" + FPSLauncher.themeName + "/textures/entities/vileCivilianAttack1(back).png");
		vileCivilianAttack2back = loadBitMap(
				"resources" + FPSLauncher.themeName + "/textures/entities/vileCivilianAttack2(back).png");
		vileCivilianAttack145right = loadBitMap(
				"resources" + FPSLauncher.themeName + "/textures/entities/vileCivilianAttack1(45 right).png");
		vileCivilianAttack245right = loadBitMap(
				"resources" + FPSLauncher.themeName + "/textures/entities/vileCivilianAttack2(45 right).png");
		vileCivilianAttack1right = loadBitMap(
				"resources" + FPSLauncher.themeName + "/textures/entities/vileCivilianAttack1(right).png");
		vileCivilianAttack2right = loadBitMap(
				"resources" + FPSLauncher.themeName + "/textures/entities/vileCivilianAttack2(right).png");
		vileCivilianAttack1135right = loadBitMap(
				"resources" + FPSLauncher.themeName + "/textures/entities/vileCivilianAttack1(135 right).png");
		vileCivilianAttack2135right = loadBitMap(
				"resources" + FPSLauncher.themeName + "/textures/entities/vileCivilianAttack2(135 right).png");
		enemy4corpse1 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy4corpse1.png");
		enemy4corpse2 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy4corpse2.png");
		enemy4corpse3 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy4corpse3.png");
		enemy5hurt = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy5hurt.png");
		enemy5a45left = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy5-1(45 left).png");
		enemy5b45left = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy5-2(45 left).png");
		enemy5c45left = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy5-3(45 left).png");
		enemy5d45left = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy5-4(45 left).png");
		enemy5firea45left = loadBitMap(
				"resources" + FPSLauncher.themeName + "/textures/entities/enemy5fire1(45 left).png");
		enemy5fireb45left = loadBitMap(
				"resources" + FPSLauncher.themeName + "/textures/entities/enemy5fire2(45 left).png");
		enemy5hurt45left = loadBitMap(
				"resources" + FPSLauncher.themeName + "/textures/entities/enemy5hurt(45 left).png");
		enemy5aleft = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy5-1(left).png");
		enemy5bleft = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy5-2(left).png");
		enemy5cleft = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy5-3(left).png");
		enemy5dleft = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy5-4(left).png");
		enemy5firealeft = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy5fire1(left).png");
		enemy5firebleft = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy5fire2(left).png");
		enemy5hurtleft = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy5hurt(left).png");
		enemy5a135left = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy5-1(135 left).png");
		enemy5b135left = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy5-2(135 left).png");
		enemy5c135left = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy5-3(135 left).png");
		enemy5d135left = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy5-4(135 left).png");
		enemy5firea135left = loadBitMap(
				"resources" + FPSLauncher.themeName + "/textures/entities/enemy5fire1(135 left).png");
		enemy5fireb135left = loadBitMap(
				"resources" + FPSLauncher.themeName + "/textures/entities/enemy5fire2(135 left).png");
		enemy5hurt135left = loadBitMap(
				"resources" + FPSLauncher.themeName + "/textures/entities/enemy5hurt(135 left).png");
		enemy5aback = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy5-1(back).png");
		enemy5bback = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy5-2(back).png");
		enemy5cback = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy5-3(back).png");
		enemy5dback = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy5-4(back).png");
		enemy5fireaback = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy5fire1(back).png");
		enemy5firebback = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy5fire2(back).png");
		enemy5hurtback = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy5hurt(back).png");
		enemy5a45right = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy5-1(45 right).png");
		enemy5b45right = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy5-2(45 right).png");
		enemy5c45right = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy5-3(45 right).png");
		enemy5d45right = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy5-4(45 right).png");
		enemy5firea45right = loadBitMap(
				"resources" + FPSLauncher.themeName + "/textures/entities/enemy5fire1(45 right).png");
		enemy5fireb45right = loadBitMap(
				"resources" + FPSLauncher.themeName + "/textures/entities/enemy5fire2(45 right).png");
		enemy5hurt45right = loadBitMap(
				"resources" + FPSLauncher.themeName + "/textures/entities/enemy5hurt(45 right).png");
		enemy5aright = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy5-1(right).png");
		enemy5bright = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy5-2(right).png");
		enemy5cright = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy5-3(right).png");
		enemy5dright = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy5-4(right).png");
		enemy5firearight = loadBitMap(
				"resources" + FPSLauncher.themeName + "/textures/entities/enemy5fire1(right).png");
		enemy5firebright = loadBitMap(
				"resources" + FPSLauncher.themeName + "/textures/entities/enemy5fire2(right).png");
		enemy5hurtright = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy5hurt(right).png");
		enemy5a135right = loadBitMap(
				"resources" + FPSLauncher.themeName + "/textures/entities/enemy5-1(135 right).png");
		enemy5b135right = loadBitMap(
				"resources" + FPSLauncher.themeName + "/textures/entities/enemy5-2(135 right).png");
		enemy5c135right = loadBitMap(
				"resources" + FPSLauncher.themeName + "/textures/entities/enemy5-3(135 right).png");
		enemy5d135right = loadBitMap(
				"resources" + FPSLauncher.themeName + "/textures/entities/enemy5-4(135 right).png");
		enemy5firea135right = loadBitMap(
				"resources" + FPSLauncher.themeName + "/textures/entities/enemy5fire1(135 right).png");
		enemy5fireb135right = loadBitMap(
				"resources" + FPSLauncher.themeName + "/textures/entities/enemy5fire2(135 right).png");
		enemy5hurt135right = loadBitMap(
				"resources" + FPSLauncher.themeName + "/textures/entities/enemy5hurt(135 right).png");
		enemy3corpse1 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy3corpse1.png");
		enemy3corpse2 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy3corpse2.png");
		enemy3corpse3 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy3corpse3.png");
		enemy3corpse4 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy3corpse4.png");
		enemy3corpse5 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy3corpse5.png");
		enemy3corpse6 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy3corpse6.png");
		enemy3corpse7 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy3corpse7.png");
		enemy3corpse8 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy3corpse8.png");
		enemy3corpse9 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy3corpse9.png");
		enemy3a45left = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy3-1(45 left).png");
		enemy3b45left = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy3-2(45 left).png");
		enemy3c45left = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy3-3(45 left).png");
		enemy3d45left = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy3-4(45 left).png");
		enemy3e45left = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy3-5(45 left).png");
		enemy3f45left = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy3-6(45 left).png");
		enemy3fire1left45 = loadBitMap(
				"resources" + FPSLauncher.themeName + "/textures/entities/enemy3fire1(45 left).png");
		enemy3fire2left45 = loadBitMap(
				"resources" + FPSLauncher.themeName + "/textures/entities/enemy3fire2(45 left).png");
		enemy3fire3left45 = loadBitMap(
				"resources" + FPSLauncher.themeName + "/textures/entities/enemy3fire3(45 left).png");
		enemy3hurt45left = loadBitMap(
				"resources" + FPSLauncher.themeName + "/textures/entities/enemy3hurt(45 left).png");
		enemy3a45right = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy3-1(45 right).png");
		enemy3b45right = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy3-2(45 right).png");
		enemy3c45right = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy3-3(45 right).png");
		enemy3d45right = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy3-4(45 right).png");
		enemy3e45right = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy3-5(45 right).png");
		enemy3f45right = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy3-6(45 right).png");
		enemy3fire1right45 = loadBitMap(
				"resources" + FPSLauncher.themeName + "/textures/entities/enemy3fire1(45 right).png");
		enemy3fire2right45 = loadBitMap(
				"resources" + FPSLauncher.themeName + "/textures/entities/enemy3fire2(45 right).png");
		enemy3fire3right45 = loadBitMap(
				"resources" + FPSLauncher.themeName + "/textures/entities/enemy3fire3(45 right).png");
		enemy3hurt45right = loadBitMap(
				"resources" + FPSLauncher.themeName + "/textures/entities/enemy3hurt(45 right).png");
		enemy3aleft = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy3-1(left).png");
		enemy3bleft = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy3-2(left).png");
		enemy3cleft = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy3-3(left).png");
		enemy3dleft = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy3-4(left).png");
		enemy3eleft = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy3-5(left).png");
		enemy3fleft = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy3-6(left).png");
		enemy3fire1left = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy3fire1(left).png");
		enemy3fire2left = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy3fire2(left).png");
		enemy3fire3left = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy3fire3(left).png");
		enemy3hurtleft = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy3hurt(left).png");
		enemy3aright = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy3-1(right).png");
		enemy3bright = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy3-2(right).png");
		enemy3cright = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy3-3(right).png");
		enemy3dright = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy3-4(right).png");
		enemy3eright = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy3-5(right).png");
		enemy3fright = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy3-6(right).png");
		enemy3fire1right = loadBitMap(
				"resources" + FPSLauncher.themeName + "/textures/entities/enemy3fire1(right).png");
		enemy3fire2right = loadBitMap(
				"resources" + FPSLauncher.themeName + "/textures/entities/enemy3fire2(right).png");
		enemy3fire3right = loadBitMap(
				"resources" + FPSLauncher.themeName + "/textures/entities/enemy3fire3(right).png");
		enemy3hurtright = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy3hurt(right).png");
		enemy3a135right = loadBitMap(
				"resources" + FPSLauncher.themeName + "/textures/entities/enemy3-1(135 right).png");
		enemy3b135right = loadBitMap(
				"resources" + FPSLauncher.themeName + "/textures/entities/enemy3-2(135 right).png");
		enemy3c135right = loadBitMap(
				"resources" + FPSLauncher.themeName + "/textures/entities/enemy3-3(135 right).png");
		enemy3d135right = loadBitMap(
				"resources" + FPSLauncher.themeName + "/textures/entities/enemy3-4(135 right).png");
		enemy3e135right = loadBitMap(
				"resources" + FPSLauncher.themeName + "/textures/entities/enemy3-5(135 right).png");
		enemy3f135right = loadBitMap(
				"resources" + FPSLauncher.themeName + "/textures/entities/enemy3-6(135 right).png");
		enemy3fire1right135 = loadBitMap(
				"resources" + FPSLauncher.themeName + "/textures/entities/enemy3fire1(135 right).png");
		enemy3fire2right135 = loadBitMap(
				"resources" + FPSLauncher.themeName + "/textures/entities/enemy3fire2(135 right).png");
		enemy3fire3right135 = loadBitMap(
				"resources" + FPSLauncher.themeName + "/textures/entities/enemy3fire3(135 right).png");
		enemy3hurt135right = loadBitMap(
				"resources" + FPSLauncher.themeName + "/textures/entities/enemy3hurt(135 right).png");
		enemy3a135left = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy3-1(135 left).png");
		enemy3b135left = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy3-2(135 left).png");
		enemy3c135left = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy3-3(135 left).png");
		enemy3d135left = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy3-4(135 left).png");
		enemy3e135left = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy3-5(135 left).png");
		enemy3f135left = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy3-6(135 left).png");
		enemy3fire1left135 = loadBitMap(
				"resources" + FPSLauncher.themeName + "/textures/entities/enemy3fire1(135 left).png");
		enemy3fire2left135 = loadBitMap(
				"resources" + FPSLauncher.themeName + "/textures/entities/enemy3fire2(135 left).png");
		enemy3fire3left135 = loadBitMap(
				"resources" + FPSLauncher.themeName + "/textures/entities/enemy3fire3(135 left).png");
		enemy3hurt135left = loadBitMap(
				"resources" + FPSLauncher.themeName + "/textures/entities/enemy3hurt(135 left).png");
		enemy3aback = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy3-1(back).png");
		enemy3bback = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy3-2(back).png");
		enemy3cback = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy3-3(back).png");
		enemy3dback = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy3-4(back).png");
		enemy3eback = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy3-5(back).png");
		enemy3fback = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy3-6(back).png");
		enemy3fire1back = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy3fire1(back).png");
		enemy3fire2back = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy3fire2(back).png");
		enemy3fire3back = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy3fire3(back).png");
		enemy3hurtback = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/enemy3hurt(back).png");
		defaultCorpse1 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/defaultCorpse1.png");
		defaultCorpse2 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/defaultCorpse2.png");
		defaultCorpse3 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/defaultCorpse3.png");
		defaultCorpse4 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/defaultCorpse4.png");
		defaultCorpse5 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/defaultCorpse5.png");
		defaultCorpse6 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/entities/defaultCorpse6.png");

		///////////////////////////////////////////// PROJECTILES
		phaser = loadBitMap("resources" + FPSLauncher.themeName + "/textures/projectiles/phaser.png");
		bullet = loadBitMap("resources" + FPSLauncher.themeName + "/textures/projectiles/bullet.png");
		canExplode1 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/projectiles/canExplode1.png");
		canExplode2 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/projectiles/canExplode2.png");
		canExplode3 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/projectiles/canExplode3.png");
		canExplode4 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/projectiles/canExplode4.png");
		defaultFireball = loadBitMap("resources" + FPSLauncher.themeName + "/textures/projectiles/fireball.png");
		giantFireball = loadBitMap("resources" + FPSLauncher.themeName + "/textures/projectiles/giantFireball.png");
		electricShock = loadBitMap("resources" + FPSLauncher.themeName + "/textures/projectiles/electricShock.png");
		electroBall = loadBitMap("resources" + FPSLauncher.themeName + "/textures/projectiles/electroBall.png");
		rocket = loadBitMap("resources" + FPSLauncher.themeName + "/textures/projectiles/rocket.png");
		explosion1 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/projectiles/explosion1.png");
		explosion2 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/projectiles/explosion2.png");
		explosion3 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/projectiles/explosion3.png");
		explosion4 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/projectiles/explosion4.png");
		explosion5 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/projectiles/explosion5.png");
		explosion6 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/projectiles/explosion6.png");
		explosion7 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/projectiles/explosion7.png");
		explosion8 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/projectiles/explosion8.png");
		phaseHit1 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/projectiles/phaseHit1.png");
		phaseHit2 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/projectiles/phaseHit2.png");
		phaseHit3 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/projectiles/phaseHit3.png");
		phaseHit4 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/projectiles/phaseHit4.png");
		phaseHit5 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/projectiles/phaseHit5.png");
		fireHit1 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/projectiles/fireHit1.png");
		fireHit2 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/projectiles/fireHit2.png");
		fireHit3 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/projectiles/fireHit3.png");
		fireHit4 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/projectiles/fireHit4.png");
		bulletHit1 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/projectiles/bulletHit1.png");
		bulletHit2 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/projectiles/bulletHit2.png");
		bulletHit3 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/projectiles/bulletHit3.png");
		bulletHit4 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/projectiles/bulletHit4.png");
		bloodSpray1 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/projectiles/bloodSpray1.png");
		bloodSpray2 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/projectiles/bloodSpray2.png");
		bloodSpray3 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/projectiles/bloodSpray3.png");
		bloodSpray4 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/projectiles/bloodSpray4.png");
		criticalHit1 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/projectiles/criticalSpray1.png");
		criticalHit2 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/projectiles/criticalSpray2.png");
		criticalHit3 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/projectiles/criticalSpray3.png");
		criticalHit4 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/projectiles/criticalSpray4.png");
		criticalHit5 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/projectiles/criticalSpray5.png");
		criticalHit6 = loadBitMap("resources" + FPSLauncher.themeName + "/textures/projectiles/criticalSpray6.png");
	}

	/**
	 * A test method to stop the heap space running out bug
	 */
	public static void setToNull() {
		//////////////////////////////////////// WALLS
		wall1 = null;
		wall2 = null;
		wall3 = null;
		wall4 = null;
		wall4damaged1 = null;
		wall4damaged2 = null;
		wall4damaged3 = null;
		wall5 = null;
		wall6 = null;
		wall7 = null;
		wall8 = null;
		wall9 = null;
		wall10 = null;
		wall11 = null;
		wall12 = null;
		wall13Phase1 = null;
		wall13Phase2 = null;
		wall14 = null;
		teleportEnter = null;
		teleportExit = null;
		tutorialWall = null;
		tutorialWall2 = null;
		tutorialWall3 = null;
		tutorialWall4 = null;
		tutorialWall5 = null;
		wall15Phase5 = null;
		wall15Phase4 = null;
		wall15Phase3 = null;
		wall15Phase2 = null;
		wall15Phase1 = null;

		// Toxic Waste/Lava. Has many phases
		toxic1 = null;
		toxic2 = null;
		toxic3 = null;
		toxic4 = null;
		toxic5 = null;
		toxic6 = null;
		toxic7 = null;
		toxic8 = null;
		toxic9 = null;
		toxic10 = null;
		toxic11 = null;
		toxic12 = null;
		toxic13 = null;
		toxic14 = null;
		toxic15 = null;
		toxic16 = null;
		coolWall = null;
		spine1 = null;
		spine2 = null;
		spine3 = null;
		spine4 = null;
		spine5 = null;
		spine6 = null;
		spine7 = null;
		spine8 = null;
		mlg = null;
		box = null;
		woodenWall = null;
		bloodWall = null;
		marble = null;
		normButton = null;
		normButtonOn = null;
		wall35 = null;
		wall36 = null;
		wall37 = null;
		wall38 = null;
		wall39 = null;
		wall40 = null;
		wall41 = null;
		wall42a = null;
		wall42b = null;
		wall42c = null;
		wall42d = null;

		//////////////////////////////////////////////// ITEMS
		health = null;
		shell = null;
		megaPhase1 = null;
		megaPhase2 = null;
		megaPhase3 = null;
		megaPhase4 = null;
		redKey = null;
		blueKey = null;
		greenKey = null;
		yellowKey = null;
		shotgun = null;
		resurrect1 = null;
		resurrect2 = null;
		resurrect3 = null;
		environSuit = null;
		goblet1 = null;
		goblet2 = null;
		goblet3 = null;
		adrenaline = null;
		glasses = null;
		torch1 = null;
		torch2 = null;
		torch3 = null;
		torch4 = null;
		lamp1 = null;
		lamp2 = null;
		lamp3 = null;
		tree = null;
		canister = null;
		chainmeal = null;
		combat = null;
		argent = null;
		shard = null;
		vial = null;
		upgrade1 = null;
		upgrade2 = null;
		upgrade3 = null;
		upgrade4 = null;
		holyWater1 = null;
		holyWater2 = null;
		table = null;
		lampTable = null;
		scepter = null;
		invisEmerald = null;
		invisEmerald2 = null;
		invisEmerald3 = null;
		invisEmerald4 = null;
		shellBox = null;
		chargePack = null;
		largeChargePack = null;
		phaseCannon = null;
		sat1 = null;
		sat2 = null;
		pistol = null;
		clip = null;
		bullets = null;
		rockets = null;
		rocketLaucher = null;
		rocketCrate = null;
		bonePile = null;
		tEnter1 = null;
		tEnter2 = null;
		tEnter3 = null;
		tEnter4 = null;
		tExit1 = null;
		tExit2 = null;
		tExit3 = null;
		tExit4 = null;
		darkBook = null;
		turret = null;
		marine1 = null;
		marine2 = null;
		marine3 = null;
		marine4 = null;
		marine5 = null;

		/////////////////////////////////////////////// ENTITIES
		enemy1 = null;
		enemy2 = null;
		enemy3 = null;
		enemy4 = null;
		morgoth = null;
		enemy5 = null;
		vileCiv1 = null;
		vileCiv2 = null;
		vileCiv3 = null;
		vileCiv4 = null;
		vileCivAttack1 = null;
		vileCivAttack2 = null;
		vileCivHurt = null;
		belegoth = null;
		corpse1 = null;
		corpse2 = null;
		corpse3 = null;
		corpse4 = null;
		corpse5 = null;
		corpse6 = null;
		corpse7 = null;
		corpse8 = null;
		corpseType2 = null;
		enemy4b = null;
		enemy3b = null;
		enemy3c = null;
		enemy3d = null;
		enemy3e = null;
		enemy3f = null;
		enemy1b = null;
		enemy1corpse1 = null;
		enemy1corpse2 = null;
		enemy1corpse3 = null;
		enemy1corpse = null;
		enemy1hurt = null;
		enemy1fire1 = null;
		enemy1fire2 = null;
		enemy1fire3 = null;
		enemy1fire4 = null;
		enemy5b = null;
		enemy5corpse1 = null;
		enemy5corpse2 = null;
		enemy5corpse3 = null;
		enemy5corpse4 = null;
		enemy5corpse5 = null;
		enemy5corpse6 = null;
		enemy5corpse7 = null;
		enemy5corpse8 = null;
		enemy5corpse9 = null;
		enemy5corpse10 = null;
		enemy5corpse11 = null;
		enemy5corpse12 = null;
		enemy5corpse13 = null;
		enemy5corpse14 = null;
		enemy5corpse15 = null;
		enemy5corpse16 = null;
		enemy5corpse17 = null;
		enemy4corpse = null;
		enemy5c = null;
		enemy5d = null;
		enemy5corpse = null;
		enemy5fire1 = null;
		enemy5fire2 = null;
		enemy3corpse = null;
		enemy3fire1 = null;
		enemy3fire2 = null;
		enemy3fire3 = null;
		enemy3hurt = null;
		enemy2hurt = null;
		enemy2corpse1 = null;
		enemy2corpse2 = null;
		enemy2corpse3 = null;
		enemy2corpse4 = null;
		enemy2corpse5 = null;
		enemy2corpse6 = null;
		enemy2corpse = null;
		enemy2fire1 = null;
		belegothCorpse1 = null;
		belegothCorpse2 = null;
		belegothCorpse3 = null;
		belegothCorpse4 = null;
		belegothCorpse5 = null;
		belegothCorpse6 = null;
		belegothCorpse7 = null;
		belegothCorpse8 = null;
		belegothCorpse9 = null;
		belegothCorpse10 = null;
		belegothCorpse11 = null;
		belegothCorpse12 = null;
		belegothCorpse13 = null;
		belegothCorpse = null;
		belegothHurt = null;
		belegothMelee = null;
		belegoth2 = null;
		belegoth3 = null;
		belegoth4 = null;
		belegoth5 = null;
		belegoth6 = null;
		belegothAttack1 = null;
		belegothAttack2 = null;
		belegothAttack3 = null;
		belegothAttack4 = null;
		morgothFire1 = null;
		morgothFire2 = null;
		morgothMelee = null;
		morgoth2 = null;
		morgoth3 = null;
		morgoth4 = null;
		morgothHurt = null;
		enemy2left45 = null;
		enemy2left = null;
		enemy2left135 = null;
		enemy2back = null;
		enemy2right45 = null;
		enemy2right = null;
		enemy2right135 = null;
		enemy2left45fire = null;
		enemy2left135fire = null;
		enemy2backfire = null;
		enemy2right45fire = null;
		enemy2rightfire = null;
		enemy2leftfire = null;
		enemy2right135fire = null;
		enemy1left45 = null;
		enemy1left = null;
		enemy1left135 = null;
		enemy1back = null;
		enemy1right45 = null;
		enemy1right = null;
		enemy1right135 = null;
		enemy1left45fire1 = null;
		enemy1left45fire2 = null;
		enemy1left45fire3 = null;
		enemy1left45fire4 = null;
		enemy1leftfire = null;
		enemy1right45fire1 = null;
		enemy1right45fire2 = null;
		enemy1right45fire3 = null;
		enemy1right45fire4 = null;
		enemy1rightfire = null;
		enemy1right45hurt = null;
		enemy1righthurt = null;
		enemy1left45hurt = null;
		enemy1lefthurt = null;
		vileCivilian145left = null;
		vileCivilian245left = null;
		vileCivilian345left = null;
		vileCivilian445left = null;
		vileCivilian1left = null;
		vileCivilian2left = null;
		vileCivilian3left = null;
		vileCivilian4left = null;
		vileCivilian1135left = null;
		vileCivilian2135left = null;
		vileCivilian3135left = null;
		vileCivilian4135left = null;
		vileCivilian1back = null;
		vileCivilian2back = null;
		vileCivilian3back = null;
		vileCivilian4back = null;
		vileCivilian145right = null;
		vileCivilian245right = null;
		vileCivilian345right = null;
		vileCivilian445right = null;
		vileCivilian1right = null;
		vileCivilian2right = null;
		vileCivilian3right = null;
		vileCivilian4right = null;
		vileCivilian1135right = null;
		vileCivilian2135right = null;
		vileCivilian3135right = null;
		vileCivilian4135right = null;
		vileCivilianright45hurt = null;
		vileCivilianrighthurt = null;
		vileCivilianleft45hurt = null;
		vileCivilianlefthurt = null;
		vileCivilianright135hurt = null;
		vileCivilianleft135hurt = null;
		vileCivilianbackhurt = null;
		vileCivilianAttack145left = null;
		vileCivilianAttack245left = null;
		vileCivilianAttack1left = null;
		vileCivilianAttack2left = null;
		vileCivilianAttack1135left = null;
		vileCivilianAttack2135left = null;
		vileCivilianAttack1back = null;
		vileCivilianAttack2back = null;
		vileCivilianAttack145right = null;
		vileCivilianAttack245right = null;
		vileCivilianAttack1right = null;
		vileCivilianAttack2right = null;
		vileCivilianAttack1135right = null;
		vileCivilianAttack2135right = null;
		enemy4corpse1 = null;
		enemy4corpse2 = null;
		enemy4corpse3 = null;
		enemy5hurt = null;
		enemy5a45left = null;
		enemy5b45left = null;
		enemy5c45left = null;
		enemy5d45left = null;
		enemy5firea45left = null;
		enemy5fireb45left = null;
		enemy5hurt45left = null;
		enemy5aleft = null;
		enemy5bleft = null;
		enemy5cleft = null;
		enemy5dleft = null;
		enemy5firealeft = null;
		enemy5firebleft = null;
		enemy5hurtleft = null;
		enemy5a135left = null;
		enemy5b135left = null;
		enemy5c135left = null;
		enemy5d135left = null;
		enemy5firea135left = null;
		enemy5fireb135left = null;
		enemy5hurt135left = null;
		enemy5aback = null;
		enemy5bback = null;
		enemy5cback = null;
		enemy5dback = null;
		enemy5fireaback = null;
		enemy5firebback = null;
		enemy5hurtback = null;
		enemy5a45right = null;
		enemy5b45right = null;
		enemy5c45right = null;
		enemy5d45right = null;
		enemy5firea45right = null;
		enemy5fireb45right = null;
		enemy5hurt45right = null;
		enemy5aright = null;
		enemy5bright = null;
		enemy5cright = null;
		enemy5dright = null;
		enemy5firearight = null;
		enemy5firebright = null;
		enemy5hurtright = null;
		enemy5a135right = null;
		enemy5b135right = null;
		enemy5c135right = null;
		enemy5d135right = null;
		enemy5firea135right = null;
		enemy5fireb135right = null;
		enemy5hurt135right = null;
		enemy3corpse1 = null;
		enemy3corpse2 = null;
		enemy3corpse3 = null;
		enemy3corpse4 = null;
		enemy3corpse5 = null;
		enemy3corpse6 = null;
		enemy3corpse7 = null;
		enemy3corpse8 = null;
		enemy3corpse9 = null;
		enemy3a45left = null;
		enemy3b45left = null;
		enemy3c45left = null;
		enemy3d45left = null;
		enemy3e45left = null;
		enemy3f45left = null;
		enemy3fire1left45 = null;
		enemy3fire2left45 = null;
		enemy3fire3left45 = null;
		enemy3hurt45left = null;
		enemy3a45right = null;
		enemy3b45right = null;
		enemy3c45right = null;
		enemy3d45right = null;
		enemy3e45right = null;
		enemy3f45right = null;
		enemy3fire1right45 = null;
		enemy3fire2right45 = null;
		enemy3fire3right45 = null;
		enemy3hurt45right = null;
		enemy3aleft = null;
		enemy3bleft = null;
		enemy3cleft = null;
		enemy3dleft = null;
		enemy3eleft = null;
		enemy3fleft = null;
		enemy3fire1left = null;
		enemy3fire2left = null;
		enemy3fire3left = null;
		enemy3hurtleft = null;
		enemy3aright = null;
		enemy3bright = null;
		enemy3cright = null;
		enemy3dright = null;
		enemy3eright = null;
		enemy3fright = null;
		enemy3fire1right = null;
		enemy3fire2right = null;
		enemy3fire3right = null;
		enemy3hurtright = null;
		enemy3a135right = null;
		enemy3b135right = null;
		enemy3c135right = null;
		enemy3d135right = null;
		enemy3e135right = null;
		enemy3f135right = null;
		enemy3fire1right135 = null;
		enemy3fire2right135 = null;
		enemy3fire3right135 = null;
		enemy3hurt135right = null;
		enemy3a135left = null;
		enemy3b135left = null;
		enemy3c135left = null;
		enemy3d135left = null;
		enemy3e135left = null;
		enemy3f135left = null;
		enemy3fire1left135 = null;
		enemy3fire2left135 = null;
		enemy3fire3left135 = null;
		enemy3hurt135left = null;
		enemy3aback = null;
		enemy3bback = null;
		enemy3cback = null;
		enemy3dback = null;
		enemy3eback = null;
		enemy3fback = null;
		enemy3fire1back = null;
		enemy3fire2back = null;
		enemy3fire3back = null;
		enemy3hurtback = null;
		defaultCorpse1 = null;
		defaultCorpse2 = null;
		defaultCorpse3 = null;
		defaultCorpse4 = null;
		defaultCorpse5 = null;
		defaultCorpse6 = null;

		///////////////////////////////////////////// PROJECTILES
		phaser = null;
		bullet = null;
		canExplode1 = null;
		canExplode2 = null;
		canExplode3 = null;
		canExplode4 = null;
		defaultFireball = null;
		giantFireball = null;
		electricShock = null;
		electroBall = null;
		rocket = null;
		explosion1 = null;
		explosion2 = null;
		explosion3 = null;
		explosion4 = null;
		explosion5 = null;
		explosion6 = null;
		explosion7 = null;
		explosion8 = null;
		phaseHit1 = null;
		phaseHit2 = null;
		phaseHit3 = null;
		phaseHit4 = null;
		phaseHit5 = null;
		fireHit1 = null;
		fireHit2 = null;
		fireHit3 = null;
		fireHit4 = null;
		bulletHit1 = null;
		bulletHit2 = null;
		bulletHit3 = null;
		bulletHit4 = null;
		bloodSpray1 = null;
		bloodSpray2 = null;
		bloodSpray3 = null;
		bloodSpray4 = null;
		criticalHit1 = null;
		criticalHit2 = null;
		criticalHit3 = null;
		criticalHit4 = null;
		criticalHit5 = null;
		criticalHit6 = null;
	}
}
