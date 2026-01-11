package entity;

import main.GamePanel;
import main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Entity {

    GamePanel gp;

    // A BufferedImage describes an Image with an accessible buffer of image data
    // We will use this to call our stored image files
    public BufferedImage up1, up2, down1, down2, left1, left2, right1, right2;
    public BufferedImage attackUp1, attackUp2, attackDown1, attackDown2, attackLeft1, attackLeft2, attackRight1, attackRight2;

    // Entity parameters
    public BufferedImage image, image2, image3;

    // Setting the solid area of the sprite that actually does collision with tiles/objects
    public Rectangle solidArea = new Rectangle(0, 0, 48, 48);
    public Rectangle attackArea = new Rectangle(0, 0, 0, 0);
    public int solidAreaDefaultX, solidAreaDefaultY;
    public boolean collision = false;

    // Array for string dialogues
    String[] dialogues = new String[20];

    //**** STATE ****

    // Location information about the character
    public int worldX, worldY;
    public String direction = "down";
    // This is to make the sprite look as if he is walking (move from up1 to up2 etc.)
    // Therefore, we make a counter and record of which to use
    public int spriteNum = 1;
    int dialogueIndex = 0;
    public boolean collisionOn = false;

    // I have to make invincible time, once hit by a monster
    // This time means the player can not be injured in the time period after being initially hit by a monster
    // This is because the update is called at 60 times per second
    // So when the player touches the monster they will lose the intended health value but at a rate of every frame (too fast so he is dead instantly)
    public boolean invincible = false;

    // Boolean to trigger if character is in attacking animation via Enter key
    public boolean attacking = false;

    // Death effect on death, instead of just vanishing
    public boolean alive = true;
    public boolean dying = false;

    // Visibility of entity health bar only when interacted with
    public boolean hpBarOn = false;

    //**** COUNTERS ****

    // Used alongside spriteNum
    public int spriteCounter = 0;
    // Integer to slow down the direction change of the NPC
    public int actionLockCounter = 0;
    public int invincibleCounter = 0;
    // Used for death effect
    int dyingCounter = 0;
    // Used for health bar visibility duration
    int hpBarCounter = 0;

    //**** CHARACTER ATTRIBUTES ****

    // I cannot have the NPC giving damage to player
    public int type; // This would be like {player: 0, npc: 1, monster: 2}

    public String name;
    public int speed;

    // Character status - shared by both player and monsters
    public int maxLife;
    public int life;

    public Entity(GamePanel gp){
        // This is an abstract class, received from the Player class
        this.gp = gp;
    }

    // Carried via inheritance in Java
    public void setAction(){}

    // Monsters react to damage, carried via inheritance
    public void damageReaction(){}

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
        gp.cChecker.checkEntity(this, gp.npc);
        gp.cChecker.checkEntity(this, gp.monster); // Collisions can happen between Monsters and NPCs
        boolean contactPlayer = gp.cChecker.checkPlayer(this);

        // Entity of type 2 means that it can cause damage to Player, unlike an NPC
        if(this.type == 2 && contactPlayer == true){
            if(gp.player.invincible == false){
                // Give damage
                gp.playSE(6);
                gp.player.life -= 1;
                gp.player.invincible = true;
            }
        }

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

        if(invincible == true){
            invincibleCounter++;
            if(invincibleCounter > 40){
                invincible = false;
                invincibleCounter = 0;
            }
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

            // Monster health bar
            if (type == 2 && hpBarOn == true) {
                double oneScale = (double)gp.tileSize/maxLife;
                double hpBarValue = oneScale*life;

                g2.setColor(new Color(35, 35, 35));
                g2.fillRect(screenX - 1, screenY - 16, gp.tileSize + 2, 12);

                g2.setColor(new Color(255, 0, 30));
                g2.fillRect(screenX, screenY - 15, (int)hpBarValue, 10);

                hpBarCounter++;

                // Disappear after 10 seconds
                if(hpBarCounter > 600){
                    hpBarCounter = 0;
                    hpBarOn = false;
                }
            }

            if(invincible == true){
                // Display the health bar
                hpBarOn = true;
                hpBarCounter = 0;

                // Make a player half transparent when he is "invincible" or just been hit
                changeAlpha(g2, 0.4f);
            }

            if(dying == true){
                dyingAnimation(g2);
            }

            g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);

            changeAlpha(g2, 1f);
        }
    }

    // Method for an animation of the entity dying - blinking effect
    public void dyingAnimation(Graphics2D g2){
        dyingCounter++;

        // Constant for animation timing
        int i = 5;

        if(dyingCounter <= i){
            changeAlpha(g2, 0f);
        }
        if(dyingCounter > i && dyingCounter >= i*2){
            changeAlpha(g2, 1f);
        }
        if(dyingCounter > i*2 && dyingCounter >= i*3){
            changeAlpha(g2, 0f);
        }
        if(dyingCounter > i*3 && dyingCounter >= i*4){
            changeAlpha(g2, 1f);
        }
        if(dyingCounter > i*4 && dyingCounter >= i*5){
            changeAlpha(g2, 0f);
        }
        if(dyingCounter > i*5 && dyingCounter >= i*6){
            changeAlpha(g2, 1f);
        }
        if(dyingCounter > i*6 && dyingCounter >= i*7){
            changeAlpha(g2, 0f);
        }
        if(dyingCounter > i*7 && dyingCounter >= i*8){
            changeAlpha(g2, 1f);
        }
        if(dyingCounter > i*8){
            dying = false;
            alive = false;
        }
    }

    public void changeAlpha(Graphics2D g2, float alphaValue){
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alphaValue));
    }

    public BufferedImage setup(String imagePath, int width, int height){
        UtilityTool uTool = new UtilityTool();
        BufferedImage image = null;

        try{
            image = ImageIO.read(getClass().getResourceAsStream(imagePath + ".png"));
            image = uTool.scaleImage(image, width, height);
        } catch (IOException e){
            e.printStackTrace();
        }
        return image;
    }
}
