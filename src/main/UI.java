package main;

import object.OBJ_Key;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;

// Handles all on-screen User Interface
public class UI {

    GamePanel gp;
    Font arial_30, arial_80B;
    BufferedImage keyImage;
    public boolean messageOn = false;
    public String message = "";
    int messageCounter = 0;
    public boolean gameFinished = false;

    // Show the length of time to find the treasure
    double playTime;
    DecimalFormat dFormat = new DecimalFormat("#0.00");

    public UI(GamePanel gp){
        this.gp = gp;

        arial_30 = new Font("Arial", Font.PLAIN, 30);
        arial_80B = new Font("Arial", Font.BOLD, 80);

        OBJ_Key key = new OBJ_Key();
        keyImage = key.image;
    }

    public void showMessage(String text){
        message = text;
        messageOn = true;
    }

    public void draw(Graphics2D g2){

        if(gameFinished){
            // Display a congrats message at the center of the screen
            g2.setFont(arial_30);
            g2.setColor(Color.WHITE);

            String text;
            int textLength;
            int x;
            int y;

            // Hit the treasure chest message
            text = "You found the treasure";
            textLength = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth(); // Return len of text

            x = gp.screenWidth/2 - textLength/2;
            y = gp.screenHeight/2 - (gp.tileSize * 3);
            g2.drawString(text, x, y);

            // Display the overall time of game taken
            text = "Your Time is: "+ dFormat.format(playTime)+ "!";
            textLength = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth(); // Return len of text

            x = gp.screenWidth/2 - textLength/2;
            y = gp.screenHeight/2 + (gp.tileSize * 4);
            g2.drawString(text, x, y);

            // Congrats message
            g2.setFont(arial_80B);
            g2.setColor(Color.yellow);

            text = "Congratulations!";
            textLength = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth(); // Return len of text

            x = gp.screenWidth/2 - textLength/2;
            y = gp.screenHeight/2 + (gp.tileSize * 2);
            g2.drawString(text, x, y);

            // Now stop the game
            gp.gameThread = null;

        } else {
            g2.setFont(arial_30);
            g2.setColor(Color.WHITE);
            g2.drawImage(keyImage, gp.tileSize/2, gp.tileSize/2, gp.tileSize, gp.tileSize, null);
            g2.drawString("x = " + gp.player.hasKey, 74, 65);

            // Time
            playTime += (double)1/60;
            g2.drawString("Time: "+ dFormat.format(playTime), gp.tileSize*11, 65);

            // Message
            if(messageOn){
                g2.setFont(g2.getFont().deriveFont(20F));
                g2.drawString(message, gp.tileSize/2,gp.tileSize*5); // Put the message five tiles from top right corner

                messageCounter++;

                // Make the text disappear after 2 seconds
                if(messageCounter > 120){
                    messageCounter = 0;
                    messageOn = false;
                }
            }
        }


    }
}
