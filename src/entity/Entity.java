package entity;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Entity {

    // Location and speed information about the character
    public int worldX, worldY;
    public int speed;

    // A BufferedImage describes an Image with an accessible buffer of image data
    // We will use this to call our stored image files (player walking)
    public BufferedImage up1, up2, down1, down2, left1, left2, right1, right2;
    public String direction;

    // This is to make the sprite look as if he is walking (move from up1 to up2 etc.)
    // Therefore, we make a counter and record of which to use
    public int spriteCounter = 0;
    public int spriteNum = 1;

    // Setting the solid area of the sprite that actually does collision with tiles/objects
    public Rectangle solidArea;
    public int solidAreaDefaultX, solidAreaDefaultY;
    public boolean collisionOn = false;

}
