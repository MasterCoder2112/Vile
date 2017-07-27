package com.vile;

import java.applet.Applet;
import java.awt.*;

/**
 * Title: FPSTestApplet
 *
 * @author Alex Byrd
 *         Date Updated: 4/7/2016
 *         <p>
 *         Description:
 *         This runs the game in a web browser created by an html file.
 *         It makes no alterations to the game itself, it is just now runnable
 *         from the web browser.
 */
public class FPSTestApplet extends Applet {
    //Usually java just wants this. Idk really what it is
    private static final long serialVersionUID = 1L;

    //Declares and instantiates the display
    private Display display = new Display();

    /**
     * Initiates the applet and sets it up
     */
    public void init() {
        //Sets the screen layout to the default layout
        setLayout(new BorderLayout());

        //Adds the display to the screen
        add(display);
    }

    /**
     * Starts the game
     */
    public void start() {
        display.start();
    }

    /**
     * Stops the game
     */
    public void stop() {
        display.stop();
    }

}
