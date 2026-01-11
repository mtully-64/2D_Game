package main;

import entity.Entity;
import entity.Player;
import tile.TileManager;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class GamePanel extends JPanel implements Runnable{
    /*
    ********************************
    * SCREEN SETTINGS
    ********************************
     */

    // Tile size settings
    final int originalTileSize = 16; //this is 16x16 tile

    // Now I have to scale the 16x16 tile to fit today's monitor/screen size (16 is based on SEGA architecture)
    final int scale = 3; //set the scale to be 3 times bigger... 3x16 = 48 (a common for game dev)
    public final int tileSize = originalTileSize * scale; //48x48 tile

    // Now we have to how many tiles can be displayed on a single screen
    public final int maxScreenCol = 16; //max titles on x-axis is 16
    public final int maxScreenRow = 12; //max titles on y-axis is 12

    public final int screenWidth = tileSize * maxScreenCol; // 16x48 = 768 pixels
    public final int screenHeight = tileSize * maxScreenRow; //12x48 = 576 pixels

    /*
     ********************************
     * WORLD SETTINGS
     ********************************
     */
    public final int maxWorldCol = 50;
    public final int maxWorldRow = 50;

    /*
     ********************************
     * FPS SETTINGS
     ********************************
     */
    // FPS setting to 60 FPS
    int FPS = 60;

    /*
     ********************************
     * SYSTEM
     ********************************
     */

    // We need to instantiate the tile manager
    TileManager tileM = new TileManager(this);

    // We need to instantiate the key handler
    public KeyHandler keyH = new KeyHandler(this);

    // Instantiate sounds into the game
    Sound music = new Sound();
    Sound sound_effects = new Sound();

    // Instantiate the idea of collision of objects with NPCs, Characters etc
    public CollisionChecker cChecker = new CollisionChecker(this);

    // Include the assets/objects
    public AssetSetter aSetter = new AssetSetter(this);

    // Instantiate in the UI of the game
    public UI ui = new UI(this);

    public EventHandler eHandler = new EventHandler(this);

    // We need time for the game to be run, allowing us to make a refresh rate etc.
    // Hence, we use a 'Thread'
    Thread gameThread; //a thread that you can start and stop

    /*
     ********************************
     * ENTITY AND OBJECT
     ********************************
     */
    // Instantiate the player
    public Player player = new Player(this, keyH);

    // Instantiate objects like keys etc., too many objects can slow down the game
    public Entity obj[] = new Entity[10];

    // Now we will instantiate the image array of the NPCs for the game
    public Entity[] npc = new Entity[10];

    // Instantiating the monsters in the game - max is 20
    public Entity monster[] = new Entity[20];

    // Controlling of render order
    // Array will be sorted by, the entity with the lowest worldY comes in index 0
    // Then it will draw entities in order of their worldY value
    ArrayList<Entity> entityList = new ArrayList<>();

    /*
     ********************************
     * GAME STATE
     ********************************
     */
    public int gameState;
    public final int titleState = 0;
    public final int playState = 1;
    public final int pauseState = 2;
    public final int dialogueState = 3;

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

    public void setupGame(){
        aSetter.setObject();
        aSetter.setNPC();
        aSetter.setMonster();
        gameState = titleState;
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
        if(gameState == playState) {
            // Player
            player.update();
            // NPC
            for(int i = 0; i < npc.length; i++){
                if(npc[i] != null){
                    npc[i].update();
                }
            }
            // Monster
            for(int i = 0; i < monster.length; i++){
                if(monster[i] != null){
                    if (monster[i].alive == true && monster[i].dying == false){
                        monster[i].update();
                    }
                    if (monster[i].alive == false){
                        monster[i] = null;
                    }
                }
            }
        }
        if(gameState == pauseState){
            // in progress atm!
        }
    }

    // Method used to redraw onto the screen
    public void paintComponent(Graphics g){
        super.paintComponent(g);

        //now we call the class 'Graphics2D' class, extending the Graphics class
        /*This provides a more sophisticated control over geometry, coordinate transformations, color management and text layout*/
        Graphics2D g2 = (Graphics2D) g; //here we do type conversion

        // Showcase nerd stats just like Minecraft, when hit the F3 button
        long drawStart = 0 ;
        if(keyH.checkNerdStats){
            drawStart = System.nanoTime();
        }

        // Title Screen
        if(gameState == titleState){
            ui.draw(g2);
        } // Others
        else{

            // Tile draw
            tileM.draw(g2); // Always draw the tile first, then the player

            // All entities are put into list
            entityList.add(player);
            for(int i = 0; i < npc.length; i++){
                if (npc[i] != null){
                    entityList.add(npc[i]);
                }
            }

            for(int i = 0; i < obj.length; i++){
                if(obj[i] != null){
                    entityList.add(obj[i]);
                }
            }

            for(int i = 0; i < monster.length; i++){
                if(monster[i] != null){
                    entityList.add(monster[i]);
                }
            }

            // Now the array list is sorted
            Collections.sort(entityList, new Comparator<Entity>() {
                @Override
                public int compare(Entity e1, Entity e2) {
                    int result = Integer.compare(e1.worldY, e2.worldY);
                    return result;
                }
            });

            // Draw entities
            for(int i = 0; i < entityList.size(); i++){
                entityList.get(i).draw(g2);
            }

            // Empty the entity list
            entityList.clear();

            // UI draw
            ui.draw(g2);

        }


        // Used for nerd stats
        if(keyH.checkNerdStats) {
            long drawEnd = System.nanoTime();
            long passed = drawEnd - drawStart;
            g2.setColor(Color.white);
            g2.drawString("Draw Time: " + passed, 10, 400);
        }
        //the 'dispose' method is used to dispose of this graphics context and release any system resources that it is using
        g2.dispose();
    }

    /*
     ********************************
     * MUSIC
     ********************************
     */

    public void playMusic(int i){
        music.setFile(i);
        music.play();
        music.loop();
    }

    public void stopMusic(){
        music.stop();
    }

    public void playSE(int i){
        sound_effects.setFile(i);
        sound_effects.play();
    }
}
