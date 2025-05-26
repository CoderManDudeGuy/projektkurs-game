import java.awt.*;
import java.util.ArrayList;

import static java.lang.Thread.sleep;


public class Control {
    public static GUI gui;
    public static Map map;
    public static final int FPS = 60; //(Bilder pro Sekunde)
    public static final long maxLoopTime = 1000 / FPS;
    public static String resFolder = "res/";
    public static Player player;
    public static Boss testEnemy;
    public static KeyManager keyManager;
    public static Camera camera;
    public ArrayList<Boss> enemies = new ArrayList<Boss>();

    public Control() {
        keyManager = new KeyManager();
        createGui();
        createGame();
        start();
    }

    public void createGui() {
        gui = new GUI(this);
    }

    private void createGame() {
        map = new Map("res/BossTest.txt");
        SpriteSheet sheet = new SpriteSheet("res/playerSheet.png", 4, 3, 64, 64);
        camera = new Camera(map.getMapWidth(), map.getMapHeight(), gui.gamePanel.getWidth(), gui.gamePanel.getHeight());
        player  = new Player(117, 1000, 64, 64, sheet, this);
        testEnemy = new Boss(768, 115, 64, 64, sheet, this);
        enemies.add(testEnemy);
        gui.mainFrame.requestFocus();
    }

    private void start() {
        Loop loop = new Loop();
        Thread t = new Thread(loop);
        t.start();
    }

    public void loadNewMap() {
//        map = new Map("res/Aktuelle Demo-Map_2.txt");
    }

    public void update() {
        keyManager.update();
        Point p = keyInputToMove();
        player.setMove(p);
        for (int i = 0; i < enemies.size(); i++) {
            enemies.get(i).fight();
        }
        player.setFacing(keyManager.staring);
        if (keyManager.action) player.attack();
        gui.gamePanel.repaint();
    }

    private Point keyInputToMove() {
        int xMove = 0;
        int yMove = 0;
        if (keyManager.jump) yMove = -5;
        if (keyManager.left) xMove = -1;
        if (keyManager.right) xMove = 1;
        return new Point(xMove, yMove);
    }

    public static void main(String[] args) {
        new Control();
    }

    class Loop implements Runnable {
        long timestamp = 0;
        long oldTimestamp = 0;

        public void run() {
            while (true) {
                oldTimestamp = System.currentTimeMillis();
                update();
                timestamp = System.currentTimeMillis();

                if ((timestamp - oldTimestamp) > maxLoopTime) {
                    System.out.println("Zu langsam");
                    continue;
                }

                try {
                    sleep(maxLoopTime - (timestamp - oldTimestamp));
                } catch (Exception e) {
                    System.out.println("Loopfehler: " + maxLoopTime);
                }
            }
        }
    }
}
