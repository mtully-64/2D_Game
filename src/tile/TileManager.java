package tile;

import main.GamePanel;
import main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TileManager {

    GamePanel gp;
    public Tile[] tile;
    // We have to load in the map through a txt file
    public int[][] mapTileNum;

    public TileManager(GamePanel gp){
        this.gp = gp;

        // We create 10 times of tile for the game (this number is up to the 'texture pack' size
        tile = new Tile[10];
        // We initialise the map of 16x12 that will hold the tiles, through a 2D array
        mapTileNum = new int[gp.maxWorldCol][gp.maxWorldRow];

        // Load in the tile images
        getTileImage();
        loadMap("/maps/world01.txt");
    }

    // This method is to load in the tile images
    public void getTileImage(){
        setup(0, "grass", false);
        setup(1, "wall", true);
        setup(2, "water", true);
        setup(3, "earth", false);
        setup(4, "tree", true);
        setup(5, "sand", false);
    }

    // This loads in a tile of a buffered image, improving rendering performance of the game
    public void setup(int index, String imagePath, boolean collision){
        UtilityTool uTool = new UtilityTool();

        try{
            tile[index] = new Tile();
            tile[index].image = ImageIO.read(getClass().getResourceAsStream("/tiles/" + imagePath + ".png"));
            tile[index].image = uTool.scaleImage(tile[index].image, gp.tileSize, gp.tileSize);
            tile[index].collision = collision;
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    // Method to load in the txt map information
    public void loadMap(String filePath){
        try{
            // Reading the content of the txt file
            InputStream is = getClass().getResourceAsStream(filePath);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            int col = 0;
            int row = 0;

            while(col < gp.maxWorldCol && row < gp.maxWorldRow){
                String line = br.readLine(); // This does a whole line of the txt at once

                while(col<gp.maxWorldCol){
                    String[] numbers = line.split(" ");

                    int num = Integer.parseInt(numbers[col]);

                    mapTileNum[col][row] = num;
                    col++;
                }
                if(col == gp.maxWorldCol){
                    col = 0;
                    row++;
                }
            }

            // Remember to close that buffer reader
            br.close();

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    // Draw the tiles onto the window
    public void draw(Graphics2D g2){
        // We draw each tile
        int worldCol = 0;
        int worldRow = 0;

        while(worldCol <gp.maxWorldCol && worldRow < gp.maxWorldRow){

            // Get each row/col in the map and source its innate tile based on the map.txt file
            int tileNum = mapTileNum[worldCol][worldRow];

            int worldX = worldCol * gp.tileSize;
            int worldY = worldRow * gp.tileSize;
            int screenX = worldX - gp.player.worldX + gp.player.screenX;
            int screenY = worldY - gp.player.worldY + gp.player.screenY;

            if(worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
                    worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
                    worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
                    worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {
                g2.drawImage(tile[tileNum].image, screenX, screenY, gp.tileSize, gp.tileSize, null);
            }

            worldCol++;

            if(worldCol == gp.maxWorldCol){
                worldCol = 0;
                worldRow++;
            }
        }
    }
}

