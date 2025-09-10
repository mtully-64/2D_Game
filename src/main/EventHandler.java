package main;

import java.awt.*;

public class EventHandler {

    GamePanel gp;
    Rectangle eventRect;
    int eventRectDefaultX, eventRectDefaultY;

    public EventHandler(GamePanel gp){
        this.gp = gp;

        // The event trigger point is small in the middle of the tile
        eventRect = new Rectangle();
        eventRect.x = 23;
        eventRect.y = 23;
        eventRect.width = 2;
        eventRect.height = 2;
        eventRectDefaultX = eventRect.x;
        eventRectDefaultY = eventRect.y;
    }

    public void checkEvent(){
        if(hit(27, 16, "right") == true){
            // Event will happen
            damagePit(gp.dialogueState);
        }

        if(hit(23, 12, "up") == true){
            healingPool(gp.dialogueState);
        }
    }

    // Method to handling the player hitting an object/character
    public boolean hit(int eventCol, int eventRow, String reqDirection){
        boolean hit = false;

        gp.player.solidArea.x = gp.player.worldX + gp.player.solidArea.x;
        gp.player.solidArea.y = gp.player.worldY + gp.player.solidArea.y;
        eventRect.x = eventCol*gp.tileSize + eventRect.x;
        eventRect.y = eventRow*gp.tileSize + eventRect.y;

        if(gp.player.solidArea.intersects(eventRect)){
            if(gp.player.direction.contentEquals(reqDirection) || reqDirection.contentEquals("any")){
                hit = true;
            }
        }

        gp.player.solidArea.x = gp.player.solidAreaDefaultX;
        gp.player.solidArea.y = gp.player.solidAreaDefaultY;
        eventRect.x = eventRectDefaultX;
        eventRect.y = eventRectDefaultY;

        return hit;
    }

    // When the player falls into a 'pit', they will lose 1 life
    public void damagePit(int gameState){
        gp.gameState = gameState;

        gp.ui.currentDialogue = "You have fallen into a pit!";
        gp.player.life -= 1;
    }

    // When the player hits enter at a water body, they will heal in life
    public void healingPool(int gameState){
        if(gp.keyH.enterPressed == true){
            gp.gameState = gameState;
            gp.ui.currentDialogue = "You drank water, your life has been re-healed";
            gp.player.life = gp.player.maxLife;
        }
    }
}
