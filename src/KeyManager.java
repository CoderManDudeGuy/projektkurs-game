import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyManager implements KeyListener {

    private boolean[] keys;
    public boolean up, down, left, right, action, jump, dash;
    public String staring = "straight";

    public boolean isTurning = false;
    //public double degreeOfTankRotation = 0.0;
    //public double degreeOfTowerRotation = 0.0;

    public KeyManager(){
        keys = new boolean[256];
    }

    public void update(){
        up = keys[KeyEvent.VK_W];
        down = keys[KeyEvent.VK_S];
        left = keys[KeyEvent.VK_A];
        right = keys[KeyEvent.VK_D];
        action = keys[KeyEvent.VK_M];
        jump = keys[KeyEvent.VK_SPACE];
        dash = keys[KeyEvent.VK_SHIFT];

        if (up){
            staring = "up";
        } else if(down){
            staring = "down";
        } else {
            staring = "straight";
        }
    }

    public void keyPressed(KeyEvent e) {
        keys[e.getKeyCode()] = true;

        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
        }

        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
        }

        if (e.getKeyCode() == KeyEvent.VK_UP) {
        }

        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
        }
    }

    public void keyReleased(KeyEvent e) {
        keys[e.getKeyCode()] = false;

        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
        }

        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
        }

        if (e.getKeyCode() == KeyEvent.VK_UP) {
        }

        if (e.getKeyCode() == KeyEvent.VK_DOWN) {;
        }
    }

    public void releaseAll(){
        keys[KeyEvent.VK_W] = false;
        keys[KeyEvent.VK_SPACE] = false;
        keys[KeyEvent.VK_A] = false;
        keys[KeyEvent.VK_D] = false;
        keys[KeyEvent.VK_E] = false;
        keys[KeyEvent.VK_M] = false;
        update();
    }


    public void keyTyped(KeyEvent e) {
    }
}
