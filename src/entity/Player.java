package entity;

import main.GamePanel;
import main.KeyHandler;
import main.UtilityTool;
import object.OBJ_Shield_Wood;
import object.OBJ_Sword_Normal;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.Buffer;

public class Player extends Entity{
    GamePanel gp;
    KeyHandler  keyH;

    // These indicate where we draw the player on the screen
    // Remember, that the map moves not the player, when wasd is pressed
    public final int screenX;
    public final int screenY;

    // Fixing the character standing bug
    int standCounter = 0;

    // Boolean to prevent player interaction and sword swing at the same time
    public boolean attackCanceled = false;

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

        attackArea.width = 36;
        attackArea.height = 36;

        setDefaultValues();
        getPlayerImage();
        getPlayerAttackImage();
    }

    // Method to set the player's default values
    public void setDefaultValues(){
        worldX = gp.tileSize * 23; // Player's x position on the world map
        worldY = gp.tileSize * 21; // Player's y position on the world map
        speed = 4;
        direction = "down";

        // Player status
        level = 1;
        maxLife = 6; // 3 hearts is 6 lives, 1 life is half a heart
        life = maxLife;
        strength = 1;
        dexterity = 1;
        exp = 1;
        nextLevelExp = 5;
        coin = 0;

        // Weapons that you start with
        currentWeapon = new OBJ_Sword_Normal(gp);
        currentShield = new OBJ_Shield_Wood(gp);

        attack = getAttack(); // the total attack value
        defence = getDefence(); // the total defence value
    }

    // Method to return attack
    public int getAttack(){
        return attack = strength * currentWeapon.attackValue;
    }

    // Method to return defence
    public int getDefence(){
        return defence = dexterity * currentShield.defenceValue;
    }

    // Method to load in images of player walking (the sprite)
    public void getPlayerImage(){
        up1 = setup("/player/boy_up_1", gp.tileSize, gp.tileSize);
        up2 = setup("/player/boy_up_2", gp.tileSize, gp.tileSize);
        down1 = setup("/player/boy_down_1", gp.tileSize, gp.tileSize);
        down2 = setup("/player/boy_down_2", gp.tileSize, gp.tileSize);
        left1 = setup("/player/boy_left_1", gp.tileSize, gp.tileSize);
        left2 = setup("/player/boy_left_2", gp.tileSize, gp.tileSize);
        right1 = setup("/player/boy_right_1", gp.tileSize, gp.tileSize);
        right2 = setup("/player/boy_right_2", gp.tileSize, gp.tileSize);
    }

    // Method to load and scale the attack images
    public void getPlayerAttackImage(){
        attackUp1 = setup("/player/boy_attack_up_1", gp.tileSize, gp.tileSize*2);
        attackUp2 = setup("/player/boy_attack_up_2", gp.tileSize, gp.tileSize*2);
        attackDown1 = setup("/player/boy_attack_down_1", gp.tileSize, gp.tileSize*2);
        attackDown2 = setup("/player/boy_attack_down_2", gp.tileSize, gp.tileSize*2);
        attackLeft1 = setup("/player/boy_attack_left_1", gp.tileSize*2, gp.tileSize);
        attackLeft2 = setup("/player/boy_attack_left_2", gp.tileSize*2, gp.tileSize);
        attackRight1 = setup("/player/boy_attack_right_1", gp.tileSize*2, gp.tileSize);
        attackRight2 = setup("/player/boy_attack_right_2", gp.tileSize*2, gp.tileSize);
    }

    public void update(){

        if (attacking == true){
            attacking();
        }

        // Statement to stop the sprite moving between png 1 and 2, when not moving
        else if(keyH.upPressed || keyH.downPressed || keyH.leftPressed || keyH.rightPressed || keyH.enterPressed){

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

            // If the collision is false, the player can move
            if(!collisionOn && !keyH.enterPressed) {
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

            if(keyH.enterPressed == true && !attackCanceled){
                gp.playSE(7);
                attacking = true;
                spriteCounter = 0;
            }

            attackCanceled = false;
            gp.keyH.enterPressed = false;

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

    // Method for player attack animation
    public void attacking(){
        spriteCounter++;

        // Show attacking image 1 in the first 5 frames
        if(spriteCounter <= 5){
            spriteNum = 1;
        }

        // Show attacking image 2 in the first 5 frames
        if(spriteCounter > 5 && spriteCounter <= 25){
            spriteNum = 2;

            // Check did attack land on this monster
            // The solid area must be checked in the area of the attacking sword and not the character

            // Save the current worldX, worldY and solidArea
            int currentWorldX = worldX;
            int currentWorldY = worldY;
            int solidAreaWidth = solidArea.height;
            int solidAreaHeight = solidArea.width;

            // Adjust the player's worldX and worldY for the attack
            switch (direction){
                case "up":
                    worldY -= attackArea.height;
                    break;
                case "down":
                    worldY += attackArea.height;
                    break;
                case "left":
                    worldX -= attackArea.width;
                    break;
                case "right":
                    worldX += attackArea.width;
                    break;
            }

            // Attack area becomes solidArea
            solidArea.width = attackArea.width;
            solidArea.height = attackArea.height;

            // Check monster collision with updated worldX, worldY and solidArea
            int monsterIndex = gp.cChecker.checkEntity(this, gp.monster);
            damageMonster(monsterIndex);

            // After checking collison, restore the original information
            worldX = currentWorldX;
            worldY = currentWorldY;
            solidArea.width = solidAreaWidth;
            solidArea.height = solidAreaHeight;
        }

        // Finish the attacking animation
        if(spriteCounter > 25){
            spriteNum = 1;
            spriteCounter = 0;
            attacking = false;
        }
    }

    // This is the method to pick up an object (object interaction)
    public void pickUpObject(int i){
        if(i != 999){ // If an object is not touched, 999 is a random number thats outside of the index

        }
    }

    // Method to handle the interaction of the player with an NPC or Monster (like to start dialog etc.)
    public void interactNPC(int i){
        // Only open the dialogue window when you press the Enter key
        if(gp.keyH.enterPressed == true){
            if(i != 999){
                attackCanceled = true;
                gp.gameState = gp.dialogueState;
                gp.npc[i].speak();
            }
        }
    }

    // Method to decrease player's health upon contact with a monster
    public void contactMonster(int i){
        if(i != 999){
            if(invincible == false){
                gp.playSE(6);
                life -= 1;
                invincible = true;
            }
        }
    }

    // Method for hit detection
    public void damageMonster(int i){
        if(i != 999){
            if(gp.monster[i].invincible == false){
                gp.playSE(5);
                gp.monster[i].life -= 1;
                gp.monster[i].invincible = true;
                gp.monster[i].damageReaction();

                // Monster dies
                if(gp.monster[i].life <= 0){
                    gp.monster[i].dying = true;
                }
            }
        }
    }

    // This method will be used to draw the player's sprite on the screen
    public void draw(Graphics2D g2){
        BufferedImage image = null;

        // Correcting the sprite image position when attacking (it can be a 32x32 or 32x16 instead of 16x16)
        int tempScreenX = screenX;
        int tempScreenY = screenY;

        // Show the player's image based on the intended direction that they are facing
        switch(direction){
            case "up":
                if(attacking == false) {
                    if(spriteNum == 1) {
                        image = up1;
                    } else if (spriteNum == 2) {
                        image = up2;
                    }
                } else if (attacking == true) {
                    tempScreenY = screenY - gp.tileSize;

                    if(spriteNum == 1) {
                        image = attackUp1;
                    } else if (spriteNum == 2) {
                        image = attackUp2;
                    }
                }
                break;

            case "down":
                if(attacking == false) {
                    if (spriteNum == 1) {
                        image = down1;
                    } else if (spriteNum == 2) {
                        image = down2;
                    }
                } else if (attacking == true) {
                    if (spriteNum == 1) {
                        image = attackDown1;
                    } else if (spriteNum == 2) {
                        image = attackDown2;
                    }
                }
                break;

            case "left":
                if(attacking == false){
                    if(spriteNum == 1){
                        image = left1;
                    } else if (spriteNum == 2) {
                        image = left2;
                    }
                } else if (attacking == true) {
                    tempScreenX = screenX - gp.tileSize;

                    if(spriteNum == 1){
                        image = attackLeft1;
                    } else if (spriteNum == 2) {
                        image = attackLeft2;
                    }
                }
                break;

            case "right":
                if(attacking == false) {
                    if (spriteNum == 1) {
                        image = right1;
                    } else if (spriteNum == 2) {
                        image = right2;
                    }
                } else if (attacking == true) {
                    if (spriteNum == 1) {
                        image = attackRight1;
                    } else if (spriteNum == 2) {
                        image = attackRight2;
                    }
                }
                break;
        }

        if(invincible == true){
            // Make a player half transparent when he is "invincible" or just been hit
            changeAlpha(g2, 0.3f);
        }

        // Draw the image to the screen
        g2.drawImage(image, tempScreenX, tempScreenY, null); // The image observer is null

        // Now you have to reset the transparency
        changeAlpha(g2, 1f); // Without it fucked my UI text
    }
}
