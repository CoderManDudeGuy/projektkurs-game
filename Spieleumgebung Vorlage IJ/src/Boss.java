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
    private boolean digMove = false;
    private boolean zoomMove = false ;
    private boolean basicMove = false;
    private boolean hitWall;
    private int chomping;
    private String chompDir = "right";
    private boolean pop = true;
    private boolean dashBasic = false;
    private boolean jumpBasic = false;
    private int targetX = 0;
    private int startX = 0;
    private int deficitX = 0;
    private int deficitY = 0;
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
        if (moveSeqSleep++ == 7) {
            if (moveSeq < 2) {
                moveSeq++;
            } else {
                moveSeq = 0;
            }
            moveSeqSleep = 0;
        }


        if (wallCheck()){
            if (oldX-pMove.getX()>0){
                xPos += 64-(xPos%64);
                System.out.println("HEÖP");
            } else {
                xPos -= (xPos+300)%64;
                System.out.println("HMMM");
            }
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

    public void setCurrentImage(int pXMove, int pYMove, int pMoveSeq) {

        if (!grounded){
            image = sprites.getSpriteElement(4,0);
            if (pXMove>0){
                direction=2;
            }
            if (pXMove<0){
                direction=1;
            }
        } else {
            if (pXMove>0){
                image = sprites.getSpriteElement(1, 1);
                direction=2;
            }

            if (pXMove<0){
                image = sprites.getSpriteElement(1, 0);
                direction=1;
            }

            if (pXMove==0){
                image = sprites.getSpriteElement(0, 0);
            }
        }

        if (digMove){
            if (wait==0){
                image = sprites.getSpriteElement(2,0);
            } else if (wait<20){
                image = sprites.getSpriteElement(2,1);
            } else if(wait<50){
                if (pXMove>0){
                    image=sprites.getSpriteElement(3,1);
                } else {
                    image=sprites.getSpriteElement(3,0);
                }
            }

        }

        if (chomping!=0){
            if (chompDir.equals("left")){
                image=sprites.getSpriteElement(5,1);
            } else {
                image=sprites.getSpriteElement(5,0);
            }
        }
    }

    private boolean wallCheck(){
        if (hitboxActive) {
            ArrayList<Layer> layerList = Control.map.getLayerList();
            leftTop = new Point2D.Double(xPos , yPos);
            Point2D leftMiddle = new Point2D.Double(xPos, yPos + (height/2));
            leftBottom = new Point2D.Double(xPos, yPos + height);
            rightTop = new Point2D.Double(xPos + width, yPos);
            Point2D rightMiddle = new Point2D.Double(xPos + width, yPos + (height/2));
            rightBottom = new Point2D.Double(xPos + width, yPos + height);
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
            leftBottom = new Point2D.Double(xPos+10, yPos+100);
            rightBottom = new Point2D.Double(xPos+290, yPos+100);
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
            leftTop = new Point2D.Double(xPos, yPos);
            rightTop = new Point2D.Double(xPos+300, yPos);
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
        enemyHitBox = new Rectangle(xPos - Control.camera.getXOffset(), yPos - Control.camera.getYOffset(),  300, 100);
//        g2d.setColor(Color.blue);
//        g2d.fill(enemyHitBox);

        g2d.setColor(Color.red);
        if (chomping>0){
            int[] xValues;
            int[] yValues;

            if (chompDir.equals("right")){
                xValues = new int[]{xPos - Control.camera.getXOffset() + 23 + 300, xPos - Control.camera.getXOffset() + 23+300, xPos - Control.camera.getXOffset() + 23 + 300 + 250};
                yValues = new int[]{yPos - Control.camera.getYOffset() - 36, yPos - Control.camera.getYOffset() - 36 + 100, yPos - Control.camera.getYOffset() - 36 + 50};
            } else {
                xValues = new int[]{xPos - Control.camera.getXOffset() + 23, xPos - Control.camera.getXOffset() + 23, xPos - Control.camera.getXOffset() + 23 - 250};
                yValues = new int[]{yPos - Control.camera.getYOffset() - 36, yPos - Control.camera.getYOffset() - 36 + 100, yPos - Control.camera.getYOffset() - 36 + 50};
            }

            g2d.fillPolygon(xValues,yValues,3);
        }

    }

    public void jump(){

        if (grounded){
            isJumping = true;
        }

        if (isJumping) {

            if (!ceilingCheck() && jumpTop < 1) {
                grounded = false;
                if (jumpCounter < 20) {
                    yPos -= (20 - jumpCounter);
                    if (gravSlower == 0) {
                        jumpCounter++;
                        gravSlower++;
                    } else {
                        gravSlower--;
                    }
                } else if (jumpTop == 0) {
                    jumpTop++;
                }
            } else {
                isJumping = false;
                jumpCounter = 0;
                jumpTop = 0;
            }
        }
    }

    public void fight(){
        int oldX = xPos;
        int oldY = yPos;
        if(digMove){
            if (wait==0){
                hitboxActive = false;
                isVisible = false;
                wait++;
            }else if(wait<20){
                wait++;
            } else if(wait<50){
                if (wait==20){
                    targetX = control.player.getPos().x;
                }

                if (xPos-(((wait-20)+1)*((xPos-targetX+118)/30))<=1172 && xPos-(((wait-20)+1)*((xPos-targetX+118)/30))>=65){
                    setMove(new Point(xPos-(((wait-20)+1)*((xPos-targetX+118)/30)), yPos));
                }


                wait++;
            } else if (wait == 50){
                if (!(ceilingCheck()) && jumpTop < 1){
                    grounded=false;
                    isJumping = true;

                    if (jumpCounter < 30) {
                        yPos -= (30-jumpCounter);
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
                    wait++;
                }
            } else if (wait== 51){
                wait = 0;
                digMove = false;
            }
        } else if(zoomMove){
            if (wait == 0){
                System.out.println("Ready for zoom");
                if((control.player.getPos().x-xPos)>0){
                    setMove(new Point(xPos+1,yPos));
                } else {
                    setMove(new Point(xPos-1,yPos));
                }

                wait++;
            } else if(wait<30){
                wait++;
            } else if(wait==30){
                if (direction == 1){
                    setMove(new Point(xPos-63,yPos));
                } else {
                    setMove(new Point(xPos+63,yPos));

                }

                if (hitWall){
                    wait++;
                }
            } else if(wait<60){
                wait++;
            } if(wait == 60){
                zoomMove = false;
                wait=0;
            }
        } else if (basicMove) {

            if (wait == 0) {
                if ((xPos-control.player.getPos().x)<300 && (xPos-control.player.getPos().x)>-600){
                    dashBasic = true;
                } else if ((xPos-control.player.getPos().x)<600 && (xPos-control.player.getPos().x)>-900){
                    jumpBasic = true;
                } else{
                    rngNumber = rng.nextInt(2);
                    if (rngNumber == 0){
                        digMove = true;
                        basicMove = false;
                    } else {
                        zoomMove = true;
                        basicMove = false;
                    }
                }
            }

            if (dashBasic) {

                if (wait < 30) {
                    if ((xPos-control.player.getPos().x)>0){
                        if ((xPos-control.player.getPos().x)<100){
                            wait = 30;
                        } else {
                            setMove(new Point(xPos-16, yPos));
                        }
                    } else {
                        if ((xPos-control.player.getPos().x)>-400){
                            wait = 30;
                        } else {
                            setMove(new Point(xPos+16, yPos));
                        }
                    }
                    wait++;
                } else if (wait<40){
                    wait++;
                } else if (wait < 60) {
                    if (wait == 30){
                        chomping = 0;
                    }
                    chomp(control.player.playerHitBox);
                    wait++;
                    chomping++;
                } else if (wait < 120) {
                    chomping = 0;
                    wait++;
                }
                if (wait == 120) {
                    wait = 0;
                    basicMove = false;
                    dashBasic = false;
                }
            } else if(jumpBasic){
                if (wait <81){
                    if (wait == 0){
                        targetX = control.player.getPos().x-130;
                        startX  = xPos;
                    }
                    jump();
                    setMove(new Point(startX-((wait)*((startX-targetX)/80)), yPos));

                    if (hitWall){
                        wait = 80;
                        isJumping = false;
                        jumpCounter = 0;
                        jumpTop = 0;
                    }

                    System.out.println("Jumping towards: " + targetX);
                    wait++;
                } else if(wait<100){
                    wait++;
                } else if(wait == 100){
                    wait = 0;
                    jumpBasic = false;
                    basicMove = false;
                }

            }


        } else if (hp>0){attack();}


        if (!isJumping) {
            gravitation();
        }

        setCurrentImage(xPos-oldX,yPos-oldY,moveSeq);
        System.out.println(wait);

    }

    public void chomp(Rectangle pPlayer){
        int[] xValues;
        int[] yValues;

        if (direction == 2){
            xValues = new int[]{xPos - Control.camera.getXOffset() + 23 + 300, xPos - Control.camera.getXOffset() + 23+300, xPos - Control.camera.getXOffset() + 23 + 300 + 300};
            yValues = new int[]{yPos - Control.camera.getYOffset() - 36, yPos - Control.camera.getYOffset() - 36 + 100, yPos - Control.camera.getYOffset() - 36 + 50};

            if (chomping == 0){
                chompDir = "right";
            }
        } else {
            xValues = new int[]{xPos - Control.camera.getXOffset() + 23, xPos - Control.camera.getXOffset() + 23, xPos - Control.camera.getXOffset() + 23 - 300};
            yValues = new int[]{yPos - Control.camera.getYOffset() - 36, yPos - Control.camera.getYOffset() - 36 + 100, yPos - Control.camera.getYOffset() - 36 + 50};

            if (chomping == 0){
                chompDir = "left";
            }
        }

        Polygon chompBox = new Polygon(xValues, yValues, 3);

        if (chompBox.intersects(pPlayer)){
            control.player.damage(10);
        }

    }

    public void damage(){
        lifePoints-=10;
    }

    public void attack (){

        String choice="nothing";

        if (grounded){
            rngNumber = rng.nextInt(6);
            if(rngNumber > 1){
                basicMove = true;
                choice = "basic";
            } else if (rngNumber == 1) {
                digMove = true;
                choice = "dig";
            } else {
                zoomMove = true;
                choice= "zoom";
            }
        }

        System.out.println(choice);
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
