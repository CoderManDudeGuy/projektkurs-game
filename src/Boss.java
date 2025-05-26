import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Random;

public class Boss{
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
    int jumpTop;
    int jumpCounter;
    boolean isJumping;
    int attackCooldown = 0;
    boolean isAttacking = false;
    int walkingCooldown = 0;
    int rngNumber;
    boolean busy = false;
    private int grav = 0;
    private int gravSlower = 0;
    public int hp = 500;
    public boolean hitboxActive = true;
    public boolean isVisible = true;
    private boolean digMove = true;
    private boolean zoomMove = true ;
    private boolean basicMove = false;
    private boolean hitWall;

    public Boss(int pXPos, int pYPos, int pWidth, int pHeight, SpriteSheet pSpriteSheet, Control pControl) {
        sprites = pSpriteSheet;
        image = sprites.getSpriteElement(0, 1);
        xPos = pXPos;
        yPos = pYPos;
        width = pWidth;
        height = pHeight;
        control = pControl;
        neighbours = Control.map.getLayerList().get(0).getNeighbours(xPos+32, yPos+42, 1, 1);
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
        setCurrentImage((int) (oldX -  pMove.getX()), (int) pMove.getY(), moveSeq);

        if (wallCheck()){
            xPos=oldX;
            movement=-movement;
            hitWall = true;
        } else {
            hitWall = false;
        }

    }

    public void gravitation(){

            if (floorCheck() || !hitboxActive) {
                grounded = true;
                grav = 0;
            } else {
                if (gravSlower == 0) {
                    if (grav != 10) {
                        grav++;
                    }
                    gravSlower++;
                } else {
                    gravSlower--;
                }
                yPos += grav;
            }
    }

    public void setCurrentImage(int pXMove, int pYMove, int pMoveSeq) {
        System.out.println(pXMove);
        if (pXMove > 0 && grounded) {
            image = sprites.getSpriteElement(1, pMoveSeq);
            direction=1;
        } else if (pXMove > 0){
            image = sprites.getSpriteElement(0, 2);
            direction=1;
        }

        if (pXMove < 0 && grounded) {
            image = sprites.getSpriteElement(2, pMoveSeq);
            direction=2;
        } else  if (pXMove < 0){
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
        if (hitboxActive) {
            ArrayList<Layer> layerList = Control.map.getLayerList();
            leftTop = new Point2D.Double(xPos + 23, yPos + 23);
            Point2D leftMiddle = new Point2D.Double(xPos + 23, yPos + 42);
            leftBottom = new Point2D.Double(xPos + 23, yPos + 63);
            rightTop = new Point2D.Double(xPos + 41, yPos + 23);
            Point2D rightMiddle = new Point2D.Double(xPos + 41, yPos + 42);
            rightBottom = new Point2D.Double(xPos + 41, yPos + 63);
            for (int i = 0; i < layerList.size(); i++) {
                Tile temp = layerList.get(0).tiles[(int) leftTop.getX() / 64][(int) leftTop.getY() / 64];
                Tile temp2 = layerList.get(0).tiles[(int) leftMiddle.getX() / 64][(int) leftMiddle.getY() / 64];
                Tile temp3 = layerList.get(0).tiles[(int) leftBottom.getX() / 64][(int) leftBottom.getY() / 64];
                Tile temp4 = layerList.get(0).tiles[(int) rightTop.getX() / 64][(int) rightTop.getY() / 64];
                Tile temp5 = layerList.get(0).tiles[(int) rightMiddle.getX() / 64][(int) rightMiddle.getY() / 64];
                Tile temp6 = layerList.get(0).tiles[(int) rightBottom.getX() / 64][(int) rightBottom.getY() / 64];
                if (grounded) {
                    if (temp.isBlocked() || temp2.isBlocked() || temp4.isBlocked() || temp5.isBlocked()) {
                        return true;
                    }
                } else {
                    if (temp.isBlocked() || temp2.isBlocked() || temp3.isBlocked() || temp4.isBlocked() || temp5.isBlocked() || temp6.isBlocked()) {
                        return true;
                    }
                }

            }
        }
        return false;
    }

    private boolean floorCheck(){
        if (hitboxActive){
            ArrayList<Layer> layerList = Control.map.getLayerList();
            leftBottom = new Point2D.Double(xPos+26, yPos+64);
            rightBottom = new Point2D.Double(xPos+37, yPos+64);
            Tile temp = layerList.get(0).tiles[(int) leftBottom.getX() / 64][(int) leftBottom.getY() / 64];
            Tile temp2 = layerList.get(0).tiles[(int) rightBottom.getX() / 64][(int) rightBottom.getY() / 64];
            if(temp.isBlocked()||temp2.isBlocked()){
                return true;
            }
        }
        return false;
    }

    private boolean ceilingCheck(){
        if (hitboxActive){
            ArrayList<Layer> layerList = Control.map.getLayerList();
            leftTop = new Point2D.Double(xPos+26, yPos+20);
            rightTop = new Point2D.Double(xPos+37, yPos+20);
            Tile temp = layerList.get(0).tiles[(int) leftTop.getX() / 64][(int) leftTop.getY() / 64];
            Tile temp2 = layerList.get(0).tiles[(int) rightTop.getX() / 64][(int) rightTop.getY() / 64];
            if(temp.isBlocked()||temp2.isBlocked()){
                return true;
            }
        }
        return false;
    }

    public void paintMe(Graphics2D g2d) {
        g2d.drawImage(image, xPos - Control.camera.getXOffset(), yPos - Control.camera.getYOffset(), width, height, null);
        enemyHitBox = new Rectangle(xPos - Control.camera.getXOffset() + 23, yPos - Control.camera.getYOffset() - 36,  300, 100);
        g2d.setColor(Color.blue);
        g2d.fill(enemyHitBox);
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
            System.out.println("hit the ceiling");
            isJumping=false;
            jumpCounter = 0;
            jumpTop=0;
        }
    }

    public void fight(){
        if(digMove){
            if (wait==0){
                hitboxActive = false;
                isVisible = false;
                xPos = control.player.getPos().x;
                yPos = 1472;
                wait++;
            } else if(wait>0 && wait<20){
                wait++;
            } else if (wait == 20){
                if (!(ceilingCheck()) && jumpTop < 1){
                    System.out.println(ceilingCheck());
                    grounded=false;
                    if (jumpCounter < 25) {
                        yPos -= (25-jumpCounter);
                        if (gravSlower == 0) {
                            jumpCounter++;
                            gravSlower++;
                        } else {
                            gravSlower--;
                        }

                        if (yPos<=1415){
                            hitboxActive = true;
                            isVisible = true;
                        }
                    } else if (jumpTop == 0) {
                        jumpTop++;
                    }
                } else {
                    isJumping=false;
                    jumpCounter = 0;
                    jumpTop=0;
                }
            }
        } else if(zoomMove){
            if (wait == 0){
                if((control.player.getPos().x-xPos)>0){
                    setMove(new Point(xPos+1,yPos));
                } else {
                    setMove(new Point(xPos-1,yPos));
                    System.out.println("Ok?");
                }

                wait++;
            } else if(wait<30){
                wait++;
            } else if(wait==30){
                if (direction == 1){
                    setMove(new Point(xPos-63,yPos));
                } else {
                    setMove(new Point(xPos+63,yPos));
                    System.out.println("shot");
                }
                System.out.println(hitWall);
                if (hitWall){
                    zoomMove = false;
                    wait = 0;
                }
            }
        } else if (basicMove) {
            if ((xPos-control.player.getPos().x)<125 && (xPos-control.player.getPos().x)>-125) {
                setMove(new Point(xPos-((xPos-control.player.getPos().x)/5), yPos));
            }
        } else if (hp>0){attack();}

        gravitation();

    }

    public void chomp(Rectangle pPlayer){
        if (direction == 1){
            int[] xValues = new int[]{xPos - Control.camera.getXOffset() + 23 + 300, xPos - Control.camera.getXOffset() + 23+300, xPos - Control.camera.getXOffset() + 23 + 300 + 75};
            int[] yValues = new int[]{yPos - Control.camera.getYOffset() - 36, yPos - Control.camera.getYOffset() - 36 + 100, yPos - Control.camera.getYOffset() - 36 + 50};
        } else {
            int[] xValues = new int[]{xPos - Control.camera.getXOffset() + 23, xPos - Control.camera.getXOffset() + 23, xPos - Control.camera.getXOffset() + 23 + 300 + 75};
            int[] yValues = new int[]{yPos - Control.camera.getYOffset() - 36, yPos - Control.camera.getYOffset() - 36 + 100, yPos - Control.camera.getYOffset() - 36 + 50};
        }


//        enemyHitBox = new Rectangle(xPos - Control.camera.getXOffset() + 23, yPos - Control.camera.getYOffset() - 36,  300, 100);

    }

    public void damage(){
        lifePoints-=10;
    }

    public void attack (){

        if (grounded){
            rngNumber = rng.nextInt(6);
            if(rngNumber > 1){
                basicMove = true;
            } else if (rngNumber == 1) {
                digMove = true;
            } else {
                zoomMove = true;
            }
        }
    }

    public void takeDmg(int x){
        hp-=x;
        if (hp<0){
            hp=0;
        }

    }

    public Point getPos(){
        return new Point(xPos,yPos);
    }
}
