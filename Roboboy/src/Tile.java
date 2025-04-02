import java.awt.*;
import java.awt.geom.Rectangle2D;

public class Tile {
    // public int width; //Das ist die Original-Breite des Tiles im TileSet; die Breite, die das Tile in der Map hat, wird in der Render-Methode übergeben. Werden nicht mehr benötigt.
    // public int height; //Das ist die Original-Höhe des Tiles im TileSet; die Höhe, die das Tile in der Map hat, wird in der Render-Methode übergeben. Werden nicht mehr benötigt.
    private Image basicImage;
    private Image subImage;
    private Image currentImage;
    private boolean blocked = false;
    private boolean permanentBlocked = false;
    private boolean hasItem = false;
    private String flagB = "F";
    private String flagI = "N";
    private int xPos;
    private int yPos;
    private Rectangle tileShape;
    public boolean isNeighbour = false;


    public Tile(Image pTileImage) {
        basicImage = pTileImage;
        currentImage = basicImage;
        tileShape = new Rectangle(xPos, yPos, 64, 64);
    }

    public void setPermanentBlocked(boolean permanentBlocked) {
        this.permanentBlocked = permanentBlocked;
    }

    public void setSubImage(Image subImage) {
        this.subImage = subImage;
    }

    public void itemCatched() {
        currentImage = subImage;
        hasItem = true;
        if (!permanentBlocked) {
            blocked = false;
        }
    }

    public boolean hasItem(){
        return hasItem;
    }

    public Rectangle2D getShape(){return tileShape;}

    public String getFlagI() {
        return flagI;
    }

    public String getFlagF() {
        return flagB;
    }

    public void setFlagI(String flagI) {
        this.flagI = flagI;
    }

    public void setFlagB(String flagB) {
        this.flagB = flagB;
    }

    public void setHasItem(boolean hasItem) {
        this.hasItem = hasItem;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    public Image getImage() {
        return basicImage;
    }

    public void renderTile(Graphics2D g2d, int pSpalteX, int pZeileY, int pTileWidth, int pTileHeight) {
        xPos = pSpalteX * pTileWidth - Control.camera.getXOffset();
        yPos = pZeileY * pTileHeight - Control.camera.getYOffset();
        g2d.drawImage(currentImage, xPos , yPos , pTileWidth, pTileHeight, null);
        tileShape = new Rectangle(xPos, yPos, pTileWidth, pTileHeight);
    }

    public int xPos(){
        return xPos;
    }

    public int yPos(){
        return yPos;
    }

}

