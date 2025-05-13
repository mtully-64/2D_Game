package tile;

import main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TileManager {

    GamePanel gp;
    Tile[] tile;
    // We have to load in the map through a txt file
    int[][] mapTileNum;

    public TileManager(GamePanel gp){
        this.gp = gp;

        // We create 10 times of tile for the game (this number is up to the 'texture pack' size
        tile = new Tile[10];
        // We initialise the map of 16x12 that will hold the tiles, through a 2D array
        mapTileNum = new int[gp.maxScreenCol][gp.maxScreenRow];

        // Load in the tile images
        getTileImage();
        loadMap("/maps/map.txt");
    }

    // This method is to load in the tile images
    public void getTileImage(){
        try{
            // Load in the first tile as grass
            tile[0] = new Tile();
            tile[0].image = ImageIO.read(getClass().getResourceAsStream("/tiles/grass.png"));

            // Load in the second tile as wall
            tile[1] = new Tile();
            tile[1].image = ImageIO.read(getClass().getResourceAsStream("/tiles/wall.png"));

            // Load in the first tile as water
            tile[2] = new Tile();
            tile[2].image = ImageIO.read(getClass().getResourceAsStream("/tiles/water.png"));

        }catch(IOException e){
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

            while(col < gp.maxScreenCol && row < gp.maxScreenRow){
                String line = br.readLine(); // This does a whole line of the txt at once

                while(col<gp.maxScreenCol){
                    String numbers[] = line.split(" ");

                    int num = Integer.parseInt(numbers[col]);

                    mapTileNum[col][row] = num;
                    col++;
                }
                if(col == gp.maxScreenCol){
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
        int col = 0;
        int row = 0;
        int x = 0;
        int y = 0;

        while(col <gp.maxScreenCol && row < gp.maxScreenRow){

            // Get each row/col in the map and source its innate tile based on the map.txt file
            int tileNum = mapTileNum[col][row];

            g2.drawImage(tile[tileNum].image, x, y, gp.tileSize, gp.tileSize, null);
            col++;
            x += gp.tileSize;

            if(col == gp.maxScreenCol){
                col = 0;
                x = 0;
                row++;
                y += gp.tileSize;
            }
        }
    }
}

