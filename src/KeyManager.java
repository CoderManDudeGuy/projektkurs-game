import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyManager implements KeyListener {

    private boolean[] keys;
    public boolean up, down, left, right, action, shot;

    public boolean isTurning = false;
    public double degreeOfTankRotation = 0.0;
    public double degreeOfTowerRotation = 0.0;

    public KeyManager(){
        keys = new boolean[256];
    }

    public void update(){
        up = keys[KeyEvent.VK_W];
        down = keys[KeyEvent.VK_S];
        left = keys[KeyEvent.VK_A];
        right = keys[KeyEvent.VK_D];
        action = keys[KeyEvent.VK_M];
        shot = keys[KeyEvent.VK_SPACE];
    }

    public void keyPressed(KeyEvent e) {
        keys[e.getKeyCode()] = true;

        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            degreeOfTankRotation = 0.03;
        }

        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            degreeOfTankRotation = -0.03;
        }

        if (e.getKeyCode() == KeyEvent.VK_UP) {
            degreeOfTowerRotation = 0.03;
            isTurning = true;
        }

        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            degreeOfTowerRotation = -0.03;
            isTurning = true;
        }
    }

    public void keyReleased(KeyEvent e) {
        keys[e.getKeyCode()] = false;

        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            degreeOfTankRotation = 0;
        }

        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            degreeOfTankRotation = 0;
        }

        if (e.getKeyCode() == KeyEvent.VK_UP) {
            degreeOfTowerRotation = 0.0;
            isTurning = false;
        }

        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            degreeOfTowerRotation = -0.00;
            isTurning = false;
        }
    }

    public void releaseAll(){
        keys[KeyEvent.VK_W] = false;
        keys[KeyEvent.VK_S] = false;
        keys[KeyEvent.VK_A] = false;
        keys[KeyEvent.VK_D] = false;
        keys[KeyEvent.VK_E] = false;
        keys[KeyEvent.VK_M] = false;
        update();
    }


    public void keyTyped(KeyEvent e) {
    }
}
