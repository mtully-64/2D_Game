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
        // Done as a shorthand to not retype numbers of monsters each time!
        int i = 0;

        // Slime 1
        gp.monster[i] = new MON_GreenSlime(gp);
        gp.monster[i].worldX = gp.tileSize*23;
        gp.monster[i].worldY = gp.tileSize*36;

        // Increase, due to addition of another monster
        i++;

        // Slime 2
        gp.monster[i] = new MON_GreenSlime(gp);
        gp.monster[i].worldX = gp.tileSize*23;
        gp.monster[i].worldY = gp.tileSize*37;

        i++;
        gp.monster[i] = new MON_GreenSlime(gp);
        gp.monster[i].worldX = gp.tileSize*24;
        gp.monster[i].worldY = gp.tileSize*37;

        i++;
        gp.monster[i] = new MON_GreenSlime(gp);
        gp.monster[i].worldX = gp.tileSize*34;
        gp.monster[i].worldY = gp.tileSize*42;

        i++;
        gp.monster[i] = new MON_GreenSlime(gp);
        gp.monster[i].worldX = gp.tileSize*38;
        gp.monster[i].worldY = gp.tileSize*42;
    }
}
