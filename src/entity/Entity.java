package entity;

import main.GamePanel;
import main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Entity {

    GamePanel gp;

    // Location and speed information about the character
    public int worldX, worldY;
    public int speed;

    // A BufferedImage describes an Image with an accessible buffer of image data
    // We will use this to call our stored image files (player walking)
    public BufferedImage up1, up2, down1, down2, left1, left2, right1, right2;
    public String direction;

    // This is to make the sprite look as if he is walking (move from up1 to up2 etc.)
    // Therefore, we make a counter and record of which to use
    public int spriteCounter = 0;
    public int spriteNum = 1;

    // Setting the solid area of the sprite that actually does collision with tiles/objects
    public Rectangle solidArea = new Rectangle(0, 0, 48, 48);
    public int solidAreaDefaultX, solidAreaDefaultY;
    public boolean collisionOn = false;

    // Integer to slow down the direction change of the NPC
    public int actionLockCounter = 0;

    // Array for string dialogues
    String[] dialogues = new String[20];

    int dialogueIndex = 0;

    public Entity(GamePanel gp){
        // This is an abstract class, received from the Player class
        this.gp = gp;
    }

    // Carried via inheritance in Java
    public void setAction(){}

    public void speak(){
        // Loop through a dialogue string list
        if(dialogues[dialogueIndex] == null) {
            dialogueIndex = 0;
        }
        gp.ui.currentDialogue = dialogues[dialogueIndex];
        dialogueIndex++;

        // I have to make sure the NPC is always looking at you when in dialogue
        switch(gp.player.direction){
            case "up":
                direction = "down";
                break;
            case "down":
                direction = "up";
                break;
            case "left":
                direction = "right";
                break;
            case "right":
                direction = "left";
                break;
        }
    }

    public void update(){
        setAction();
        collisionOn = false;
        gp.cChecker.checkTile(this);
        gp.cChecker.checkObject(this, false);
        gp.cChecker.checkPlayer(this);

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

    public void draw(Graphics2D g2){

        BufferedImage image = null;

        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        if(worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
                worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
                worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
                worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {

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

            g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
        }
    }

    public BufferedImage setup(String imagePath){
        UtilityTool uTool = new UtilityTool();
        BufferedImage image = null;

        try{
            image = ImageIO.read(getClass().getResourceAsStream(imagePath + ".png"));
            image = uTool.scaleImage(image, gp.tileSize, gp.tileSize);
        } catch (IOException e){
            e.printStackTrace();
        }
        return image;
    }
}
