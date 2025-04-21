import java.awt.*;
import static java.lang.Thread.sleep;


public class Control {
    public static GUI gui;
    public static Map map;
    public static final int FPS = 60; //(Bilder pro Sekunde)
    public static final long maxLoopTime = 1000 / FPS;
    public static String resFolder = "res/";
    public static Mover mover;
    public static Enemy enemy;
    public static KeyManager keyManager;
    public static Camera camera;


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
        map = new Map("res/BetterTest.txt");
        SpriteSheet sheet = new SpriteSheet("res/playertest.png", 4, 3, 64, 64);
        mover  = new Mover(117, 115, 64, 64, sheet, this);
        enemy  = new Enemy(405, 323, 64, 64, sheet, this);
        camera = new Camera(map.getMapWidth(), map.getMapHeight(), gui.gamePanel.getWidth(), gui.gamePanel.getHeight());
        gui.mainFrame.requestFocus();
    }

    private void start() {
        Loop loop = new Loop();
        Thread t = new Thread(loop);
        t.start();
    }

    public void loadNewMap() {
        map = new Map("res/fall.txt");
        mover.setPos(115, 115);
    }

    public void update() {
        keyManager.update();
        Point p = keyInputToMove();
        mover.setMove(p);
        gui.gamePanel.repaint();
    }

    private Point keyInputToMove() {
        int xMove = 0;
        int yMove = 0;
        if (keyManager.up) yMove = -7;
//        if (keyManager.down) yMove = 1;
        if (keyManager.left) xMove = -4;
        if (keyManager.right) xMove = 4;
        if (keyManager.down) mover.shoot();
        if (keyManager.action) loadNewMap();
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
