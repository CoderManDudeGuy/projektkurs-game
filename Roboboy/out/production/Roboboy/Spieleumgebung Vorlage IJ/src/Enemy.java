import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.awt.geom.Ellipse2D;
import java.util.Random;

public class Enemy {

    Rectangle enemyHitBox;
    Ellipse2D viewRadius;
    Ellipse2D attackRadius;
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
    private Control control;
    private int moveSeq = 0;
    private int moveSeqSleep = 0;
    private SpriteSheet sprites;
    private ArrayList<Tile> neighbours;
    private int lifePoints = 30;
    boolean grounded;
    int direction;
    Random rng = new Random();
    boolean active = false;
    int wait =  0;
    int movement = 2;


    public Enemy(int pXPos, int pYPos, int pWidth, int pHeight, SpriteSheet pSpriteSheet, Control pControl) {
        sprites = pSpriteSheet;
        image = sprites.getSpriteElement(0, 1);
        xPos = pXPos;
        yPos = pYPos;
        width = pWidth;
        height = pHeight;
        control = pControl;
        neighbours = Control.map.getLayerList().getFirst().getNeighbours(xPos+32, yPos+42, 1, 1);
        leftBottom = new Point2D.Double(xPos+24, yPos+63);
        leftTop = new Point2D.Double(xPos+24, yPos+21);
        rightBottom = new Point2D.Double(xPos+40, yPos+63);
        rightTop = new Point2D.Double(xPos+40, yPos+21);
        enemyHitBox = new Rectangle(xPos+23, yPos+20, 18, 44);
        viewRadius = new Ellipse2D.Double(xPos-240, yPos-240, 480, 480);
        attackRadius = new Ellipse2D.Double(xPos-96, yPos-96, 192, 192);
    }


    public Point getLocation() {
        return new Point(xPos, yPos);
    }

    public void setMove(Point pMove) {
        int oldX = xPos;
        xPos = (int) pMove.getX();
        gravitation();
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
            movement=-movement;
        }
    }

    public void gravitation(){
        if (floorCheck()){
            grounded = true;
        } else {
            ySpeed = 8;
            yPos += ySpeed;
        }
    }

    public void setCurrentImage(int pXMove, int pYMove, int pMoveSeq) {
        if (pXMove == 1 && grounded) {
            image = sprites.getSpriteElement(1, pMoveSeq);
            direction=1;
        } else if (pXMove == 1){
            image = sprites.getSpriteElement(0, 2);
            direction=1;
        }

        if (pXMove == -1 && grounded) {
            image = sprites.getSpriteElement(2, pMoveSeq);
            direction=2;
        } else  if (pXMove == -1){
            image = sprites.getSpriteElement(3, 0);
            direction=2;
        }

        if (pXMove == 0 && grounded){
            image = sprites.getSpriteElement(direction, 0);
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

    public void paintMe(Graphics2D g2d) {
        g2d.drawImage(image, xPos - Control.camera.getXOffset(), yPos - Control.camera.getYOffset(), width, height, null);
        viewRadius = new Ellipse2D.Double(xPos - Control.camera.getXOffset()-208, yPos - Control.camera.getYOffset()-202, 480, 480);
        g2d.setColor(Color.red);
        g2d.fill(viewRadius);
        attackRadius = new Ellipse2D.Double(xPos - Control.camera.getXOffset()-64, yPos - Control.camera.getYOffset()-61, 192, 192);
        g2d.setColor(Color.black);
        g2d.fill(attackRadius);
        enemyHitBox = new Rectangle(xPos - Control.camera.getXOffset()+23, yPos - Control.camera.getYOffset()+20,  18, 44);
        g2d.setColor(Color.blue);
        g2d.fill(enemyHitBox);
    }

    public void damage(){
        lifePoints-=10;
    }

    public void roam(Rectangle pPlayer) {
        if (viewRadius.intersects(pPlayer)) {
                if (enemyHitBox.getX() < pPlayer.getX() && !wallCheck()) {
                    movement = 3;
                } else if (enemyHitBox.getX() > pPlayer.getX() && !wallCheck()) {
                    movement = -3;
                }
                wait=0;
            } else {
            if (wait<60+rng.nextInt(90)){
                wait++;
                setMove(new Point(xPos, yPos));
                movement = 2;
                return;
            } else if (wait<450+rng.nextInt(350)) {
                wait++;
            } else {
                wait=0;
            }
        }
            setMove(new Point(xPos + movement, yPos));
        }

        public void attack (Rectangle pPlayer){
            if (attackRadius.intersects(pPlayer)) {

            }
        }
    }

