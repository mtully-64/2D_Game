package entity;

import main.GamePanel;
import main.KeyHandler;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Player extends Entity{
    GamePanel gp;
    KeyHandler  keyH;

    // These indicate where we draw the player on the screen
    //  Remember, that the map moves not the player, when wasd is pressed
    public final int screenX;
    public final int screenY;

    public Player(GamePanel gp, KeyHandler keyH){
        this.gp = gp;
        this.keyH = keyH;

        // Return the halfway point of the screen/map
        screenX = gp.screenWidth/2 - (gp.tileSize/2);
        screenY = gp.screenHeight/2 - (gp.tileSize/2);

        // Instantiate the collision rectangle on the player
        solidArea = new Rectangle();
        solidArea.x = 8;
        solidArea.y = 16;
        solidArea.width = 32;
        solidArea.height = 32;

        setDefaultValues();
        getPlayerImage();
    }

    // Method to set the player's default values
    public void setDefaultValues(){
        worldX = gp.tileSize * 23; // Player's x position on the world map
        worldY = gp.tileSize * 21; // Player's y position on the world map
        speed = 4;
        direction = "down";
    }

    // Method to load in images of player walking (the sprite)
    public void getPlayerImage(){
        try{
            up1 = ImageIO.read(getClass().getResourceAsStream("/player/boy_up_1.png"));
            up2 = ImageIO.read(getClass().getResourceAsStream("/player/boy_up_2.png"));
            down1 = ImageIO.read(getClass().getResourceAsStream("/player/boy_down_1.png"));
            down2 = ImageIO.read(getClass().getResourceAsStream("/player/boy_down_2.png"));
            left1 = ImageIO.read(getClass().getResourceAsStream("/player/boy_left_1.png"));
            left2 = ImageIO.read(getClass().getResourceAsStream("/player/boy_left_2.png"));
            right1 = ImageIO.read(getClass().getResourceAsStream("/player/boy_right_1.png"));
            right2 = ImageIO.read(getClass().getResourceAsStream("/player/boy_right_2.png"));
        }catch (IOException e){
            e.printStackTrace();
        }
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

        // Draw the image to the screen
        g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null); // The image observer is null
    }
}
