package main;

import java.awt.*;
import java.text.DecimalFormat;

// Handles all on-screen User Interface
public class UI {

    GamePanel gp;
    Graphics2D g2;
    Font arial_30, arial_80B;
    public boolean messageOn = false;
    public String message = "";
    int messageCounter = 0;
    public boolean gameFinished = false;
    public String currentDialogue = "";

    // Show the length of time to find the treasure
    double playTime;
    DecimalFormat dFormat = new DecimalFormat("#0.00");

    public UI(GamePanel gp){
        this.gp = gp;

        arial_30 = new Font("Arial", Font.PLAIN, 30);
        arial_80B = new Font("Arial", Font.BOLD, 80);
    }

    public void showMessage(String text){
        message = text;
        messageOn = true;
    }

    public void draw(Graphics2D g2){
        this.g2 = g2;

        g2.setFont(arial_30);
        g2.setColor(Color.white);

        // Play state
        if(gp.gameState == gp.playState){

        }

        // Pause state
        if(gp.gameState == gp.pauseState){
            drawPauseScreen();
        }

        // Dialogue state
        if(gp.gameState == gp.dialogueState){
            drawDialogueScreen();
        }
    }
    public void drawPauseScreen(){

        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 80F));
        String text = "PAUSED";
        int x = getXforCenteredText(text);
        int y = gp.screenHeight/2;

        g2.drawString(text, x, y);
    }

    public void drawDialogueScreen(){
        // Window for dialogue
        int x = gp.tileSize*2; // Two tiles from the left edge
        int y = gp.tileSize/2;
        int width = gp.screenWidth - (gp.tileSize*4);
        int height = gp.tileSize*4;

        drawSubWindow(x, y, width, height);

        // Where to display the text
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 26F));
        x += gp.tileSize;
        y += gp.tileSize;

        for(String line : currentDialogue.split("\n")){
            // Split the text inside of the dialogue at the "\n" symbol
            g2.drawString(line, x, y);
            // Then increase the Y to display the next line below
            y += 40;
        }
    }

    public void drawSubWindow(int x, int y, int width, int height){
        Color c = new Color(0,0,0, 210);
        g2.setColor(c);
        g2.fillRoundRect(x, y, width, height, 35, 35);

        c = new Color(255,255,255);
        g2.setColor(c);
        g2.setStroke(new BasicStroke(5)); // White outer border/stroke
        g2.drawRoundRect(x+5, y+5, width-10, height-10, 25, 25);
    }

    public int getXforCenteredText(String text){
        int length = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        return gp.screenWidth/2 - length/2;
    }
}
