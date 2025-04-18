import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.awt.geom.Ellipse2D;
import java.util.Vector;

public class Mover {
    Rectangle playerHitBox;
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


    public Mover(int pXPos, int pYPos, int pWidth, int pHeight, SpriteSheet pSpriteSheet, Control pControl) {
        sprites = pSpriteSheet;
        image = sprites.getSpriteElement(0, 1);
        xPos = pXPos;
        yPos = pYPos;
        width = pWidth;
        height = pHeight;
        control = pControl;
        playerHitBox = new Rectangle(xPos+23, yPos+20, 18, 44);
    }

    public Point getLocation() {
        return new Point(xPos, yPos);
    }

    public void setMove(Point pMove) {
        Control.camera.centerOnMover(this);
        int oldX = xPos;
        int oldY = yPos;
        xPos += pMove.getX();
        if (ySpeed!=0){
            grounded = false;
        }
        yPos += ySpeed;

        if (moveSeqSleep++ == 7) {
            if (moveSeq < 2) {
                moveSeq++;
            } else {
                moveSeq = 0;
                if (!grounded) {
                    ySpeed += 1;
                }

            }
            moveSeqSleep = 0;
        }
        setCurrentImage((int) pMove.getX(), (int) ySpeed, moveSeq);

        if (collisionCheck()) {
            xPos = oldX;
            yPos = oldY;
            lifePoints--;
            if (lifePoints < 0) {
                lifePoints = 100;
            }
        } else {
            grounded = false;
        }
//        itemCheck(control.keyManager.action);
    }

    public void setCurrentImage(int pXMove, int pYMove, int pMoveSeq) {
        if (pXMove == -1) {
            image = sprites.getSpriteElement(1, pMoveSeq);
        }
        if (pXMove == 1) {

            image = sprites.getSpriteElement(2, pMoveSeq);
        }
        if (pYMove == -1) {
            image = sprites.getSpriteElement(3, pMoveSeq);
        }
        if (pYMove == 1) {
            image = sprites.getSpriteElement(0, pMoveSeq);
        }
        playerHitBox = new Rectangle(xPos+20, yPos+16,  24, 48);
    }

    private boolean collisionCheck() {
        ArrayList<Layer> layerList = Control.map.getLayerList();
        neighbours = Control.map.getNeighbours(xPos +32, yPos +32, 18, 44);
        for (int i = 0; i < neighbours.size(); i++) {
            if (playerHitBox.getBounds().intersects(neighbours.get(i).xPos() + Control.camera.getXOffset(), neighbours.get(i).yPos() + Control.camera.getYOffset(), 64, 64)) {
                if (neighbours.get(i).isBlocked()) {
                    if (i==5||i==6||i==7){
                        grounded = true;
                        ySpeed = 0;
                    }
                    if (i==0||i==1||i==2){
                        ySpeed = 0;
                    }
                    return true;
                }
            }
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
////                    control.loadNewMap();
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
            playerHitBox = new Rectangle(xPos - Control.camera.getXOffset()+23, yPos - Control.camera.getYOffset()+20,  18, 44);
            g2d.fill(playerHitBox);
    }
}
