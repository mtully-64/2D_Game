package entity;

import main.GamePanel;
import main.KeyHandler;
import main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.Buffer;

public class Player extends Entity{
    GamePanel gp;
    KeyHandler  keyH;

    // These indicate where we draw the player on the screen
    //  Remember, that the map moves not the player, when wasd is pressed
    public final int screenX;
    public final int screenY;

    // Fixing the character standing bug
    int standCounter = 0;

    public Player(GamePanel gp, KeyHandler keyH){
        // Call the constructor of the super class into the class
        super(gp);

        this.gp = gp;
        this.keyH = keyH;

        // Return the halfway point of the screen/map
        screenX = gp.screenWidth/2 - (gp.tileSize/2);
        screenY = gp.screenHeight/2 - (gp.tileSize/2);

        // Instantiate the collision rectangle on the player
        solidArea = new Rectangle();
        solidArea.x = 8;
        solidArea.y = 16;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        solidArea.width = 30;
        solidArea.height = 30;

        setDefaultValues();
        getPlayerImage();
    }

    // Method to set the player's default values
    public void setDefaultValues(){
        worldX = gp.tileSize * 23; // Player's x position on the world map
        worldY = gp.tileSize * 21; // Player's y position on the world map
        speed = 4;
        direction = "down";

        // Player status
        maxLife = 6; // 3 hearts is 6 lives, 1 life is half a heart
        life = maxLife;
    }

    // Method to load in images of player walking (the sprite)
    public void getPlayerImage(){
        up1 = setup("/player/boy_up_1");
        up2 = setup("/player/boy_up_2");
        down1 = setup("/player/boy_down_1");
        down2 = setup("/player/boy_down_2");
        left1 = setup("/player/boy_left_1");
        left2 = setup("/player/boy_left_2");
        right1 = setup("/player/boy_right_1");
        right2 = setup("/player/boy_right_2");
    }

    public void update(){

        // Statement to stop the sprite moving between png 1 and 2, when not moving
        if(keyH.upPressed || keyH.downPressed || keyH.leftPressed || keyH.rightPressed){

            // Update player position
            if(keyH.upPressed){
                direction = "up";
            }
            if(keyH.downPressed){
                direction = "down";
            }
            if(keyH.leftPressed){
                direction = "left";
            }
            if(keyH.rightPressed){
                direction = "right";
            }

            // Check tile collision
            collisionOn = false;
            gp.cChecker.checkTile(this);

            // Check object collision (pass the player and pass the bool of true)
            int objIndex = gp.cChecker.checkObject(this, true);
            pickUpObject(objIndex);

            // Checking NPC collision
            int npcIndex = gp.cChecker.checkEntity(this, gp.npc);
            interactNPC(npcIndex);

            // Check Monster collision
            int monsterIndex = gp.cChecker.checkEntity(this, gp.monster);
            // Call a method to receive player damage when touch monster
            contactMonster(monsterIndex);


            // Check the event
            gp.eHandler.checkEvent();

            gp.keyH.enterPressed = false;

            // If the collision is false, the player can move
            if(!collisionOn) {
                switch(direction){
                    case "up":
                        worldY -= speed;
                        break;

                    case "down":
                        worldY += speed;
                        break;

                    case "left":
                        worldX -= speed;
                        break;

                    case "right":
                        worldX += speed;
                        break;
                }
            }

            // This is to alternate the walking of the player
            spriteCounter++;
            if(spriteCounter > 12){ // This means that the player image changes in every 12 frames (remember we do 60 Frames Per Second)
                if(spriteNum == 1){
                    spriteNum = 2;
                } else if (spriteNum == 2) {
                    spriteNum = 1;
                }
                spriteCounter = 0;
            }
        }
        else{
            standCounter++;

            // Making sure there is always a 20 frames time buffer
            if(standCounter == 20){
                spriteNum = 1;
                standCounter = 0;
            }
        }

        if(invincible == true){
            invincibleCounter++;
            if(invincibleCounter > 60){
                invincible = false;
                invincibleCounter = 0;
            }
        }
    }

    // This is the method to pick up an object (object interaction)
    public void pickUpObject(int i){
        if(i != 999){ // If an object is not touched, 999 is a random number thats outside of the index

        }
    }

    // Method to handle the interaction of the player with an NPC or Monster (like to start dialog etc.)
    public void interactNPC(int i){
        if(i != 999){
            // Only open the dialogue window when you press the Enter key
            if(gp.keyH.enterPressed == true){
                gp.gameState = gp.dialogueState;
                gp.npc[i].speak();
            }
        }
    }

    // Method to decrease player's health upon contact with a monster
    public void contactMonster(int i){
        if(i != 999){
            if(invincible == false){
                life -= 1;
                invincible = true;
            }
        }
    }

    // This method will be used to draw the player's sprite on the screen
    public void draw(Graphics2D g2){
        BufferedImage image = null;

        // Show the player's image based on the intended direction that they are facing
        switch(direction){
            case "up":
                if(spriteNum == 1){
                    image = up1;
                } else if (spriteNum == 2) {
                    image = up2;
                }
                break;

            case "down":
                if(spriteNum == 1){
                    image = down1;
                } else if (spriteNum == 2) {
                    image = down2;
                }
                break;

            case "left":
                if(spriteNum == 1){
                    image = left1;
                } else if (spriteNum == 2) {
                    image = left2;
                }
                break;

            case "right":
                if(spriteNum == 1){
                    image = right1;
                } else if (spriteNum == 2) {
                    image = right2;
                }
                break;
        }

        if(invincible == true){
            // Make a player half transparent when he is "invincible" or just been hit
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
        }

        // Draw the image to the screen
        g2.drawImage(image, screenX, screenY, null); // The image observer is null

        // Now you have to reset the transparency
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f)); // Without it fucked my UI text
    }
}
