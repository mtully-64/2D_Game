package entity;

import main.GamePanel;

import java.util.Random;

public class NPC_OldMan extends Entity{

    public NPC_OldMan(GamePanel gp){
        super(gp);

        direction = "down";
        speed = 1;

        getImage();
    }

    // Method to load in images of player walking (the sprite)
    public void getImage(){
        up1 = setup("/npc/oldman_up_1");
        up2 = setup("/npc/oldman_up_2");
        down1 = setup("/npc/oldman_down_1");
        down2 = setup("/npc/oldman_down_2");
        left1 = setup("/npc/oldman_left_1");
        left2 = setup("/npc/oldman_left_2");
        right1 = setup("/npc/oldman_right_1");
        right2 = setup("/npc/oldman_right_2");
    }

    // We set the characters behaviour (like AI), where each characters behaviour can be over-ridden
    public void setAction(){

        // Direction change counter so that it does not change direction too fast (120 frames or 2s)
        actionLockCounter++;
        if(actionLockCounter == 120) {

            Random random = new Random();
            int i = random.nextInt(100) + 1; // dumb way of saying 1 to 100

            // 25% chance of going a direction
            if (i <= 25) {
                direction = "up";
            }
            if (i > 25 && i <= 50) {
                direction = "down";
            }
            if (i > 50 && i < 75) {
                direction = "left";
            }
            if (i > 75 && i <= 100) {
                direction = "right";
            }

            actionLockCounter = 0;
        }
    }
}
