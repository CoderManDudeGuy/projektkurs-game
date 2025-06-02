import java.awt.*;
import java.util.ArrayList;

import static java.lang.Thread.sleep;


public class Control {
    public static GUI gui;
    public static Map map;
    public static String currentMap = "res/Room 1 (1).txt";
    public static final int FPS = 60; //(Bilder pro Sekunde)
    public static final long maxLoopTime = 1000 / FPS;
    public static String resFolder = "res/";
    public static Player player;
    public static Boss testBoss;
    public static KeyManager keyManager;
    public static Camera camera;
    public static ArrayList<Enemy> enemies = new ArrayList<>();
    public Point spawn;
    SpriteSheet bossSheet = new SpriteSheet("res/crabSheet.png", 6, 2, 128, 64);

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
        camera = new Camera(map.getMapWidth(), map.getMapHeight(), gui.gamePanel.getWidth(), gui.gamePanel.getHeight());
        loadNewMap();
        camera = new Camera(map.getMapWidth(), map.getMapHeight(), gui.gamePanel.getWidth(), gui.gamePanel.getHeight());
        SpriteSheet sheet = new SpriteSheet("res/hotPlayerSheet.png", 8, 4, 64, 64);
        SpriteSheet bossSheet = new SpriteSheet("res/crabSheet.png", 6, 2, 128, 64);
        player = new Player(100, 1000, 64, 64, sheet, this);
        spawn = new Point(100,1000);
//        testBoss = new Boss(500, 115, 300, 100, bossSheet, this);
        gui.mainFrame.requestFocus();

    }

    private void start() {
        Loop loop = new Loop();
        Thread t = new Thread(loop);
        t.start();
    }

    public void loadNewMap() {
        if (player!=null){
            player.wallA = false;
            player.floorA = false;
            player.ceilingA = false;

            if (!player.alive){
                player.alive = true;
                player.hp = 100;
            }
        }

        map = new Map(currentMap);
        camera = new Camera(map.getMapWidth(), map.getMapHeight(), gui.gamePanel.getWidth(), gui.gamePanel.getHeight());



        enemies.clear();


        for(int i = 0;i<map.getLayerList().get(0).enemiesPos.size();i+=3){
            enemies.add(new Enemy(map.getLayerList().get(0).enemiesPos.get(i+1), map.getLayerList().get(0).enemiesPos.get(i+2), 64, 64, bossSheet, this));
        }
    }

    public void update() {
        keyManager.update();
        if (player.hp>0){
            Point p = keyInputToMove();
            player.setMove(p);
            for (int i = 0; i < enemies.size(); i++) {
                enemies.get(i).roam(player.playerHitBox);
            }
            if (testBoss!=null){
                testBoss.fight();
            }
            player.setFacing(keyManager.staring);
            if (keyManager.action) player.attack();

            if (player.inTransZone){
                if (currentMap.equals("res/Room 1 (1).txt")){
                    System.out.println("loaded new");
                    player.setPos(new Point(-11, 450));
                    spawn = new Point(-11,450);
                    currentMap = "res/Room 2 (1).txt";

                } else if (currentMap.equals("res/Room 2 (1).txt")){
                    if (player.getPos().y>1400){
                        currentMap = "res/Room 3 left.txt";
                        player.setPos(new Point(3555, 33));
                        spawn = new Point(3555,33);
                    } else {
                        System.out.println("loaded new");
                        currentMap = "res/Room 3 new.txt";
                        player.setPos(new Point(0,0));
                        spawn = new Point(0,0);
                    }
                } else if (currentMap.equals("res/Room 3 left.txt")){
                    currentMap = "res/Boss room 1.txt";
                    player.setPos(new Point(0,0));
                    spawn = new Point(0,0);
                } else if (currentMap.equals("res/Room 3 new.txt")){
                    currentMap = "res/Room 4 new.txt";
                    player.setPos(new Point(-20,256));
                    spawn = new Point(-20,256);
                } else if (currentMap.equals("res/Room 4 new.txt")){
                    currentMap = "res/Room 5.txt";
                    player.setPos(new Point(1854,194));
                    spawn = new Point(1854,194);
                } else if (currentMap.equals("res/Room 5.txt")){
                    currentMap = "res/Room 7.txt";
                    player.setPos(new Point(1882,43));
                    spawn = new Point(1882,43);
                } else if (currentMap.equals("res/Boss room 1.txt")){
                    currentMap = "res/BossTest.txt";
                    player.setPos(new Point(0,132));
                    spawn = new Point(0,132);

                }else if (currentMap.equals("res/Room 7.txt")){
                    currentMap = "res/BossTest.txt";
                    player.setPos(new Point(1461,132));
                    spawn = new Point(1461,132);

                }

                loadNewMap();
            }

        } else {
            player.alive = false;
            if (keyManager.restart){
                loadNewMap();
                player.setPos(spawn);
            } else if (keyManager.quit){
                System.exit(0);
            }
        }

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
