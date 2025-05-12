package main;

import entity.Player;
import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable{
    // Screen Settings
    final int originalTileSize = 16; //this is 16x16 tile

    //now I have to scale the 16x16 tile to fit today's monitor/screen size (16 is based on SEGA architecture)
    final int scale = 3; //set the scale to be 3 times bigger... 3x16 = 48 (a common for game dev)
    public final int tileSize = originalTileSize * scale; //48x48 tile

    //now we have to how many tiles can be displayed on a single screen
    final int maxScreenCol = 16; //max titles on x-axis is 16
    final int maxScreenRow = 12; //max titles on y-axis is 12

    final int screenWidth = tileSize * maxScreenCol; // 16x48 = 768 pixels
    final int screenHeight = tileSize * maxScreenRow; //12x48 = 576 pixels

    // FPS setting to 60 FPS
    int FPS = 60;

    //we need to instantiate the key handler
    KeyHandler keyH = new KeyHandler();

    //we need time for the game to be run, allowing us to make a refresh rate etc.
    //hence, we use a 'Thread'
    Thread gameThread; //a thread that you can start and stop

    // Instantiate the player
    Player player = new Player(this, keyH);

    public GamePanel(){
        //set the size of the class 'JPanel'
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        //set background color to black
        this.setBackground(Color.black);
        //set the buffer to true, all the drawing from this component will be done in an offscreen painting buffer
        // i.e. the rendering performance of the game is enhanced
        this.setDoubleBuffered(true);

        //add key handler to game panel
        this.addKeyListener(keyH);
        this.setFocusable(true);
    }

    //the 'run' method is overwritten from the interface 'Runnable'
    //when we start the game's thread, this run method is automatically called

    public void startGameThread(){
        gameThread = new Thread(this); //calling 'this' refers to the class 'GamePanel', i.e. we are passing the class into this constructor
        gameThread.start();
    }

    //this run method will contain the core running of the game, it will hold the game loop
    @Override
    public void run(){

        // This is for the draw interval: 1 billion "nanoseconds" divided by 60 FPS -> 0.01667 seconds
        double drawInterval = (double) 1000000000/FPS;
        double nextDrawTime = System.nanoTime() + drawInterval;

        //as long as the gameThread exists, this process is repeated forever
        while(gameThread != null){

            //1. We have to update the information, such as character positioning
            update();
            //2. Redraw the screen
            //redrawing the screen 60 times per second, is 60 FPS
            repaint(); //this is how you call the 'paintComponent' method

            // Check to see the remaining time when finished redraw
            // Then for this remaining time we will sleep
            // The Thread.sleep() requires a try-catch
            try {
                // Needs to be converted from nano to milli
                double remainingTime = nextDrawTime - System.nanoTime();
                remainingTime = remainingTime/1000000;

                // This shouldn't happen but just in case we take longer to paint then the period
                if (remainingTime < 0){
                    remainingTime = 0;
                }

                Thread.sleep((long) remainingTime); //sleep only accepts long data type

                // Now we refresh the next draw time
                nextDrawTime += drawInterval;

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void update(){
        player.update();
    }

    //method used to redraw onto the screen
    public void paintComponent(Graphics g){
        super.paintComponent(g);

        //now we call the class 'Graphics2D' class, extending the Graphics class
        /*This provides a more sophisticated control over geometry, coordinate transformations, color management and text layout*/
        Graphics2D g2 = (Graphics2D) g; //here we do type conversion

        player.draw(g2);

        //the 'dispose' method is used to dispose of this graphics context and release any system resources that it is using
        g2.dispose();
    }
}
