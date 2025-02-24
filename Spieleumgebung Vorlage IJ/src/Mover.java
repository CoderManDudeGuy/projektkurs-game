import java.awt.*;
import java.util.ArrayList;

public class Mover {
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
    Point check1 = new Point();
    Point check2 = new Point();
    Point check3 = new Point();
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
        System.out.println(ySpeed);

        if (moveSeqSleep++ == 7) {
            if (moveSeq < 2) {
                moveSeq++;
            } else {
                moveSeq = 0;
                if (!grounded) {
                    ySpeed += 2;
                }

            }
            moveSeqSleep = 0;
        }
        setCurrentImage((int) pMove.getX(), (int) ySpeed, moveSeq);
        setCheckPoints(new Point((int) pMove.getX(), 1));

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
        if(grounded){
            ySpeed += pMove.getY();
        }

        itemCheck(control.keyManager.action);
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
    }

    public void setCheckPoints(Point pMove) {
        if (pMove.getX() == 1 && pMove.getY() == 0) { //rechts
            check1.setLocation(xPos + (width - 10), yPos + 5);
            check2.setLocation(xPos + (width - 10), yPos + (height / 2));
            check3.setLocation(xPos + (width - 10), yPos + height - 5);
        }

        if (pMove.getX() == -1 && pMove.getY() == 0) { //links
            check1.setLocation(xPos + 10, yPos + 5);
            check2.setLocation(xPos + 10, yPos + (height / 2));
            check3.setLocation(xPos + 10, yPos + height - 5);
        }

        if (pMove.getX() == 0 && pMove.getY() == -1) { //oben
            check1.setLocation(xPos + 10, yPos + 5);
            check2.setLocation(xPos + (width / 2), yPos + 5);
            check3.setLocation(xPos + (width / 2) + 10, yPos + 5);
        }

        if (pMove.getX() == 0 && pMove.getY() == 1) { //unten
            check1.setLocation(xPos + 10, yPos + (height - 5));
            check2.setLocation(xPos + (width / 2), yPos + (height - 5));
            check3.setLocation(xPos + (width - 10), yPos + (height - 5));
        }
    }

    private boolean collisionCheck() {
        ArrayList<Layer> layerList = Control.map.getLayerList();
        for (int i = 0; i < layerList.size(); i++) {
            Tile temp1 = layerList.get(i).tiles[(int) check1.getX() / layerList.get(i).getTileWidth()][(int) check1.getY() / layerList.get(i).getTileHeight()];
            Tile temp2 = layerList.get(i).tiles[(int) check2.getX() / layerList.get(i).getTileWidth()][(int) check2.getY() / layerList.get(i).getTileHeight()];
            Tile temp3 = layerList.get(i).tiles[(int) check3.getX() / layerList.get(i).getTileWidth()][(int) check3.getY() / layerList.get(i).getTileHeight()];
            if (temp1.isBlocked() || temp2.isBlocked() || temp3.isBlocked()) {
                ySpeed = 0;
                grounded = true;
                return true;
            }
        }
        return false;
    }

    public void itemCheck(boolean action) {
        ArrayList<Layer> layerList = control.map.getLayerList();
        for (int i = 0; i < layerList.size(); i++) {
            if (action) {
                Tile temp1 = layerList.get(i).tiles[(int) check1.getX() / layerList.get(i).getTileWidth()][(int) check1.getY() / layerList.get(i).getTileHeight()];
                Tile temp2 = layerList.get(i).tiles[(int) check2.getX() / layerList.get(i).getTileWidth()][(int) check2.getY() / layerList.get(i).getTileHeight()];
                Tile temp3 = layerList.get(i).tiles[(int) check3.getX() / layerList.get(i).getTileWidth()][(int) check3.getY() / layerList.get(i).getTileHeight()];

                if (temp1.getFlagI().equals("I")) {
                    temp1.itemCatched();
                    Control.keyManager.releaseAll();
                    return;
                }
                if (temp2.getFlagI().equals("I")) {
                    temp2.itemCatched();
                    Control.keyManager.releaseAll();
                    return;
                }

                if (temp3.getFlagI().equals("I")) {
                    temp3.itemCatched();
                    Control.keyManager.releaseAll();
                    return;
                }

                if (temp1.getFlagI().equals("K")) {
                    control.loadNewMap();
                    Control.keyManager.releaseAll();
                    return;
                }
                if (temp2.getFlagI().equals("K")) {
//                    control.loadNewMap();
                    Control.keyManager.releaseAll();
                    return;
                }
            }
        }
    }

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
    }
}
