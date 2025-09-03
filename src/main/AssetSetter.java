package main;

import entity.NPC_OldMan;

public class AssetSetter {

    // Pass the game panel into the AssetSetter
    GamePanel gp;
    public AssetSetter(GamePanel gp){
        this.gp = gp;
    }

    public void setObject(){

    }

    public void setNPC(){
        // I will instantiate the NPC old man and put him into the specific location
        gp.npc[0] = new NPC_OldMan(gp);
        gp.npc[0].worldX = gp.tileSize*21;
        gp.npc[0].worldY = gp.tileSize*21;
    }
}
