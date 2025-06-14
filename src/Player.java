import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;


public class Player {
    Rectangle playerHitBox;
    Point2D leftBottom;
    Point2D leftTop;
    Point2D rightBottom;
    Point2D rightTop;
    private Image image;
    private int xPos;
    private int yPos;
    private int ySpeed = 0;
    private final int width;
    private final int height;
    private int moveSeq = 0;
    private int moveSeqSleep = 0;
    private SpriteSheet sprites;
    private Control control;
    private ArrayList<Tile> neighbours;
    private int lifePoints = 100;
    boolean grounded;
    private int jumpCounter = 0;
    private int jumpTop;
    private boolean isJumping = false;
    int direction;
    int dashCounter = 0;
    int dashCooldown = 0;
    boolean dashAquired=true;
    private String facing = "straight";
    int cooldown = 0;
    private int grav = 0;
    private int attackAnim = 0;
    private String attackDir = "right";
    public int hp;
    private int gravSlower = 0;


    public Player(int pXPos, int pYPos, int pWidth, int pHeight, SpriteSheet pSpriteSheet, Control pControl) {
        sprites = pSpriteSheet;
        image = sprites.getSpriteElement(0, 1);
        xPos = pXPos;
        yPos = pYPos;
        width = pWidth;
        height = pHeight;
        control = pControl;
        leftBottom = new Point2D.Double(xPos+24, yPos+63);
        leftTop = new Point2D.Double(xPos+24, yPos+21);
        rightBottom = new Point2D.Double(xPos+40, yPos+63);
        rightTop = new Point2D.Double(xPos+40, yPos+21);
        playerHitBox = new Rectangle(xPos+23, yPos+20, 18, 44);
    }

    public Point getLocation() {
        return new Point(xPos, yPos);
    }

    public void setMove(Point pMove) {
        Control.camera.centerOnPlayer(this);
        int oldX = xPos;
        int walkMultiplier=5;
        if (!grounded){
            walkMultiplier = 8;
        }
        if (dashAquired && (Control.keyManager.dash||dashCounter!=0) && dashCooldown==0){
            walkMultiplier=15;
            if (direction==1){
                xPos+=direction*walkMultiplier;
            } else {
                xPos+=-1*walkMultiplier;
            }
            dashCounter++;
            if (dashCounter == 11){
                dashCounter=0;
                dashCooldown=21;
            }
        } else {
            xPos += pMove.getX()*walkMultiplier;
        }
        if (dashCooldown>0){
            dashCooldown--;
        }
        if (!isJumping){
            gravitation();
        }
        if(Control.keyManager.jump&&grounded){
            isJumping=true;
        } else if (!Control.keyManager.jump){
            isJumping=false;
        }

        if (grounded){
            jumpCounter=0;
        }

        if (isJumping){
            jump();
        }
        if (moveSeqSleep++ == 7) {
            if (moveSeq < 2) {
                moveSeq++;
            } else {
                moveSeq = 0;
            }
            moveSeqSleep = 0;
        }
        setCurrentImage((int) pMove.getX(), (int) pMove.getY(), moveSeq);

        if (wallCheck()){
            xPos=oldX;
        }

        if (cooldown != 0){
            cooldown--;
        }
    }

    public void hit(int x){
        hp-=x;
    }

    public void jump(){
        if (!ceilingCheck() && jumpTop < 1){
            grounded=false;
            if (jumpCounter < 20) {
                yPos -= (20-jumpCounter);
                if (gravSlower == 0) {
                    jumpCounter++;
                    gravSlower++;
                } else {
                    gravSlower--;
                }
            } else if (jumpTop == 0) {
                System.out.println("the fuck is going on");
                jumpTop++;
            }
        } else {
            isJumping=false;
            jumpCounter = 0;
            jumpTop=0;
        }
    }

    public void attack() {

        int diffX = control.enemies.get(0).getPos().x - xPos;
        int diffY = control.enemies.get(0).getPos().y - yPos;

        if (cooldown == 0 || cooldown > 50) {

            cooldown = 60;

            if (facing.equals("straight")) {
                if (direction == 1) {
                    int[] xValues = new int[]{xPos - Control.camera.getXOffset() + 41, xPos - Control.camera.getXOffset() + 41, xPos - Control.camera.getXOffset() + 41 + 50};
                    int[] yValues = new int[]{yPos - Control.camera.getYOffset() + 42 - 50, yPos - Control.camera.getYOffset() + 42 + 50, yPos - Control.camera.getYOffset() + 42};

                    Polygon p = new Polygon(xValues, yValues, 3);

                    if (p.intersects(control.enemies.get(0).enemyHitBox)) {
                        control.enemies.get(0).takeDmg(10);
                    }

                    if (cooldown == 0){
                        attackDir = "right";
                    }

                } else if (direction == 2) {

                    int[] xValues = new int[]{xPos - Control.camera.getXOffset()+23, xPos - Control.camera.getXOffset()+23, xPos - Control.camera.getXOffset()+23-50};
                    int[] yValues = new int[]{yPos - Control.camera.getYOffset()+42-50, yPos - Control.camera.getYOffset()+42+50, yPos - Control.camera.getYOffset()+42};

                    Polygon p = new Polygon(xValues, yValues, 3);

                    if (p.intersects(control.enemies.get(0).enemyHitBox)) {
                        control.enemies.get(0).takeDmg(10);
                    }

                    if (cooldown == 0) {
                        attackDir = "left";
                    }
                }

            } else if (facing.equals("up")) {
                int[] xValues = new int[]{xPos - Control.camera.getXOffset()+32+50, xPos - Control.camera.getXOffset()+32-50, xPos - Control.camera.getXOffset()+32};
                int[] yValues = new int[]{yPos - Control.camera.getYOffset()+20, yPos - Control.camera.getYOffset()+20, yPos - Control.camera.getYOffset()+20-50};

                Polygon p = new Polygon(xValues, yValues, 3);

                if (p.intersects(control.enemies.get(0).enemyHitBox)) {
                    control.enemies.get(0).takeDmg(10);
                }

                if (cooldown == 0){
                    attackDir = "up";
                }

            } else if (facing.equals("down")) {
                int[] xValues = new int[]{xPos - Control.camera.getXOffset()+32+50, xPos - Control.camera.getXOffset()+32-50, xPos - Control.camera.getXOffset()+32};
                int[] yValues = new int[]{yPos - Control.camera.getYOffset()+64, yPos - Control.camera.getYOffset()+64, yPos - Control.camera.getYOffset()+64+50};

                Polygon p = new Polygon(xValues, yValues, 3);

                if (p.intersects(control.enemies.get(0).enemyHitBox)) {
                    control.enemies.get(0).takeDmg(10);
                }

                if (cooldown==0){
                    attackDir = "down";
                }

            }

        }
    }


    public void gravitation(){
        if (!isJumping) {
            if (floorCheck()) {
                grounded = true;
                grav = 0;
            } else {
                if (gravSlower == 0) {
                    if (grav != 20) {
                        grav++;
                    }
                    gravSlower++;
                } else {
                    gravSlower--;
                }
                yPos += grav;
            }
        }
    }
    public void setFacing(String x){
        facing = x;
    }

    public void setCurrentImage(int pXMove, int pYMove, int pMoveSeq) {
        if (pXMove == 1 && grounded) {
            image = sprites.getSpriteElement(1, pMoveSeq);
            direction=1;
        } else if (pXMove == 1 && isJumping) {
            image = sprites.getSpriteElement(2, 0);
            direction=1;
        } else if (pXMove == 1){
            image = sprites.getSpriteElement(2, 2);
            direction=1;
        }

        if (pXMove == -1 && grounded) {
            image = sprites.getSpriteElement(0, pMoveSeq);
            direction=2;
        }  else if (pXMove == -1 && isJumping) {
            image = sprites.getSpriteElement(3, 0);
            direction=2;
        } else  if (pXMove == -1){
            image = sprites.getSpriteElement(3, 2);
            direction=2;
        }

        if (pXMove == 0 && grounded){
            image = sprites.getSpriteElement(direction, 0);
        } else if (pXMove == 0 && isJumping) {
            if (direction == 1){
                image = sprites.getSpriteElement(0, 0);
            } else {
                image = sprites.getSpriteElement(3, 2);
            }
        } else if (pXMove == 0) {
            if (direction == 1){
                image = sprites.getSpriteElement(0, 2);
            } else {
                image = sprites.getSpriteElement(3, 0);
            }
        }
    }

    private boolean wallCheck(){
        ArrayList<Layer> layerList = Control.map.getLayerList();
        leftTop = new Point2D.Double(xPos+23, yPos+23);
        Point2D leftMiddle = new Point2D.Double(xPos+23, yPos+42);
        leftBottom = new Point2D.Double(xPos+23, yPos+63);
        rightTop = new Point2D.Double(xPos+41, yPos+23);
        Point2D rightMiddle = new Point2D.Double(xPos+41, yPos+42);
        rightBottom = new Point2D.Double(xPos+41, yPos+63);
        for (int i = 0; i < layerList.size(); i++) {
            Tile temp = layerList.get(0).tiles[(int) leftTop.getX() / 64][(int) leftTop.getY() / 64];
            Tile temp2 = layerList.get(0).tiles[(int) leftMiddle.getX() / 64][(int) leftMiddle.getY() / 64];
            Tile temp3 = layerList.get(0).tiles[(int) leftBottom.getX() / 64][(int) leftBottom.getY() / 64];
            Tile temp4 = layerList.get(0).tiles[(int) rightTop.getX() / 64][(int) rightTop.getY() / 64];
            Tile temp5 = layerList.get(0).tiles[(int) rightMiddle.getX() / 64][(int) rightMiddle.getY() / 64];
            Tile temp6 = layerList.get(0).tiles[(int) rightBottom.getX() / 64][(int) rightBottom.getY() / 64];
            if (grounded){
                if (temp.isBlocked() || temp2.isBlocked() || temp4.isBlocked() || temp5.isBlocked()) {
                    return true;
                }
            } else {
                if (temp.isBlocked() || temp2.isBlocked() || temp3.isBlocked() || temp4.isBlocked() || temp5.isBlocked() || temp6.isBlocked()) {
                    return true;
                }
            }

        }
        return false;
    }

    private boolean floorCheck(){
        ArrayList<Layer> layerList = Control.map.getLayerList();
        leftBottom = new Point2D.Double(xPos+26, yPos+64);
        rightBottom = new Point2D.Double(xPos+37, yPos+64);
        Tile temp = layerList.get(0).tiles[(int) leftBottom.getX() / 64][(int) leftBottom.getY() / 64];
        Tile temp2 = layerList.get(0).tiles[(int) rightBottom.getX() / 64][(int) rightBottom.getY() / 64];
        if(temp.isBlocked()||temp2.isBlocked()){
            return true;
        }
        return false;
    }

    private boolean ceilingCheck(){
        ArrayList<Layer> layerList = Control.map.getLayerList();
        leftTop = new Point2D.Double(xPos+26, yPos+20);
        rightTop = new Point2D.Double(xPos+37, yPos+20);
        Tile temp = layerList.get(0).tiles[(int) leftTop.getX() / 64][(int) leftTop.getY() / 64];
        Tile temp2 = layerList.get(0).tiles[(int) rightTop.getX() / 64][(int) rightTop.getY() / 64];
        if(temp.isBlocked()||temp2.isBlocked()){
            return true;
        }
        return false;
    }

//    public void itemCheck(boolean action) {
//        ArrayList<Layer> layerList = control.map.getLayerList();
//        for (int i = 0; i < layerList.size(); i++) {
//            if (action) {
//                Tile temp1 = layerList.get(i).tiles[(int) check1.getX() / layerList.get(i).getTileWidth()][(int) check1.getY() / layerList.get(i).getTileHeight()];
//                Tile temp2 = layerList.get(i).tiles[(int) check2.getX() / layerList.get(i).getTileWidth()][(int) check2.getY() / layerList.get(i).getTileHeight()];
//                Tile temp3 = layerList.get(i).tiles[(int) check3.getX() / layerList.get(i).getTileWidth()][(int) check3.getY() / layerList.get(i).getTileHeight()];
//
//                if (temp1.getFlagI().equals("I")) {
//                    temp1.itemCatched();
//                    Control.keyManager.releaseAll();
//                    return;
//                }
//                if (temp2.getFlagI().equals("I")) {
//                    temp2.itemCatched();
//                    Control.keyManager.releaseAll();
//                    return;
//                }
//
//                if (temp3.getFlagI().equals("I")) {
//                    temp3.itemCatched();
//                    Control.keyManager.releaseAll();
//                    return;
//                }
//
//                if (temp1.getFlagI().equals("K")) {
//                    control.loadNewMap();
//                    Control.keyManager.releaseAll();
//                    return;
//                }
//                if (temp2.getFlagI().equals("K")) {
//                    control.loadNewMap();
//                    Control.keyManager.releaseAll();
//                    return;
//                }
//            }
//        }
//    }

    public void renderLifePointBar(Graphics2D g2d){
        BasicStroke stroke = new BasicStroke(3 , BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND );
        g2d.setStroke(stroke);
        g2d.drawRect(xPos - Control.camera.getXOffset()-15, yPos - Control.camera.getYOffset()-25, 104, 20);

        if (lifePoints >60){
            g2d.setColor(Color.green);
        }

        if (lifePoints > 30 && lifePoints < 60){
            g2d.setColor(Color.yellow);
        }

        if (lifePoints < 30){
            g2d.setColor(Color.red);
        }

        g2d.fillRect(xPos - Control.camera.getXOffset()-13, yPos - Control.camera.getYOffset()-23, lifePoints, 16);
    }
    
        public void paintMe(Graphics2D g2d) {
        g2d.drawImage(image, xPos - Control.camera.getXOffset(), yPos - Control.camera.getYOffset(), width, height, null);
//        renderLifePointBar(g2d);
//            playerHitBox = new Rectangle(xPos - Control.camera.getXOffset()+23, yPos - Control.camera.getYOffset()+20,  18, 44);
//            g2d.fill(playerHitBox);

            if (cooldown > 50){
//              int[] xValues = {xPos - Control.camera.getXOffset(), xPos - Control.camera.getXOffset(), xPos - Control.camera.getXOffset()+50};
//              int[] yValues = {xPos - Control.camera.getXOffset()-50, xPos - Control.camera.getXOffset()+50, xPos - Control.camera.getXOffset()};

                int[] xValues;
                int[] yValues;
                if (attackDir.equals("right")){
                    xValues = new int[]{xPos - Control.camera.getXOffset() + 41, xPos - Control.camera.getXOffset() + 41, xPos - Control.camera.getXOffset() + 41 + 50};
                    yValues = new int[]{yPos - Control.camera.getYOffset()+42-50, yPos - Control.camera.getYOffset()+42+50, yPos - Control.camera.getYOffset()+42};

                } else if(attackDir.equals("left")){
                    xValues = new int[]{xPos - Control.camera.getXOffset()+23, xPos - Control.camera.getXOffset()+23, xPos - Control.camera.getXOffset()+23-50};
                    yValues = new int[]{yPos - Control.camera.getYOffset()+42-50, yPos - Control.camera.getYOffset()+42+50, yPos - Control.camera.getYOffset()+42};


                } else if (attackDir.equals("up")){
                    xValues = new int[]{xPos - Control.camera.getXOffset()+32+50, xPos - Control.camera.getXOffset()+32-50, xPos - Control.camera.getXOffset()+32};
                    yValues = new int[]{yPos - Control.camera.getYOffset()+20, yPos - Control.camera.getYOffset()+20, yPos - Control.camera.getYOffset()+20-50};

                } else {
                    xValues = new int[]{xPos - Control.camera.getXOffset()+32+50, xPos - Control.camera.getXOffset()+32-50, xPos - Control.camera.getXOffset()+32};
                    yValues = new int[]{yPos - Control.camera.getYOffset()+64, yPos - Control.camera.getYOffset()+64, yPos - Control.camera.getYOffset()+64+50};

                }

                g2d.setColor(Color.black);
                g2d.drawPolygon(xValues, yValues, 3);
                g2d.setColor(Color.red);
                g2d.fillPolygon(xValues, yValues, 3);
                cooldown--;
            }
    }

    public Point getPos(){
        return new Point(xPos,yPos);
    }
}
