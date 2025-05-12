//belongs to the package 'main' in src
package main;

//import for JFrame
import javax.swing.JFrame;

public class Main {
    public static void main(String[] args) {
        //Create a window first for the game
        JFrame window = new JFrame();
        //window setting configurations
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("2D Adventure");

        //Game panel is now to be included
        GamePanel gamePanel = new GamePanel();
        window.add(gamePanel);
        window.pack();

        //set the window to the centre of the screen
        window.setLocationRelativeTo(null);
        window.setVisible(true);

        // Even though I have called 'setFocusable(true)', it is safe to include this
        gamePanel.requestFocusInWindow();

        // I have to "kick off the loop"
        gamePanel.startGameThread();
    }
}
