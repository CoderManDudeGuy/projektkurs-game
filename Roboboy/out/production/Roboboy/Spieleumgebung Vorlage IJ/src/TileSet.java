import java.io.*;
import javax.imageio.*;
import java.awt.image.BufferedImage;


public class TileSet {
    private String tileSetImagePath;
    public int numberOfTilesX, numberOfTilesY;
    private BufferedImage tileSetImage;
    public Tile [] tiles;
    private final int  border;
    private final int tileWidth;
    private final int tileHeight;

    public TileSet(String [] pTileSetData) {
        tileSetImagePath = pTileSetData[0];
        numberOfTilesX = Integer.parseInt(pTileSetData[1]);
        numberOfTilesY = Integer.parseInt(pTileSetData[2]);
        tileWidth = Integer.parseInt(pTileSetData[3]);
        tileHeight = Integer.parseInt(pTileSetData[4]);
        border = Integer.parseInt(pTileSetData[5]);
        tiles = new Tile[(numberOfTilesX * numberOfTilesY) +1 ]; //+1 weil das erste Tile das leere Tile ist.
        tiles [0] = new Tile(null); //, 0);
        createTileSet();
    }

    public int getTileWidth() {
        return tileWidth;
    }

    public int getTileHeight() {
        return tileHeight;
    }

    public void createTileSet(){
            try {
                tileSetImage = ImageIO.read(new File(Control.resFolder+tileSetImagePath)); ///Unterordnerangabe im Control!!!!!!!!
            } catch (IOException e) {
                System.out.println("Das Programm kann das Image-File nicht finden. Kopiere die Datei " + tileSetImagePath + " in den Ordner res des Programmverzeichnisses");
                e.printStackTrace();
        }

        int index = 1;
        for(int zeileY = 0; zeileY < numberOfTilesY; zeileY++) {
            for(int spalteX = 0; spalteX < numberOfTilesX; spalteX++) {
                BufferedImage bi = tileSetImage.getSubimage(spalteX * (tileWidth +border), zeileY * (tileHeight +border), tileWidth, tileHeight);
                tiles[index] = new Tile(bi);
                index++;
            }
        }
    }
}
