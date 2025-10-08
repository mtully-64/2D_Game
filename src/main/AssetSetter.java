package main;

import entity.NPC_OldMan;
import monster.MON_GreenSlime;

public class AssetSetter {

    // Pass the game panel into the AssetSetter
    GamePanel gp;
    public AssetSetter(GamePanel gp){
        this.gp = gp;
    }

    // Method used to call in all wanted Objects
    public void setObject(){

    }

    // Method used to call in all wanted NPCs
    public void setNPC(){
        // I will instantiate the NPC old man and put him into the specific location
        gp.npc[0] = new NPC_OldMan(gp);
        gp.npc[0].worldX = gp.tileSize*21;
        gp.npc[0].worldY = gp.tileSize*21;
    }

    // Method used to call in all wanted Monsters
    public void setMonster(){
        // Slime 1
        gp.monster[0] = new MON_GreenSlime(gp);
        gp.monster[0].worldX = gp.tileSize*23;
        gp.monster[0].worldY = gp.tileSize*36;

        // Slime 2
        gp.monster[1] = new MON_GreenSlime(gp);
        gp.monster[1].worldX = gp.tileSize*23;
        gp.monster[1].worldY = gp.tileSize*37;
    }
}
