import java.awt.*;

public class Layer {

    private int tileWidth; //Das ist die Breite eines Tiles in der Map (bzw. im Layer); kann differieren von der Tilebriete im TileSet
    private int tileHeight; //Das ist die Höhe eines Tiles in der Map; kann differieren von der Tilehöhe im TileSet
    private int numberOfTilesX;
    private int numberOfTilesY;
    TileSet tileSet;
    public Tile[][] tiles;

    public Layer(LayerDataSet pDataSet) {
        numberOfTilesX = pDataSet.numberOfTilesX;
        numberOfTilesY = pDataSet.numberOfTilesY;
        this.tileWidth = pDataSet.tileWidth;
        this.tileHeight = pDataSet.tileHeight;
        tileSet = new TileSet(pDataSet.tileSetData);
        createLayer(pDataSet.tilesData);
    }

    public void createLayer(String[] tilesData) {
        tiles = new Tile[numberOfTilesX][numberOfTilesY];
        try {
            int i = 0;
            for (int zeileY = 0; zeileY < numberOfTilesY; zeileY++) {
                for (int spalteX = 0; spalteX < numberOfTilesX; spalteX++) {
                    String[] temp = tilesData[i].split("-");
                    int index = Integer.parseInt(temp[0]);
                    Image img = tileSet.tiles[index].getImage();
                    tiles[spalteX][zeileY] = new Tile(img); //Breite und Höhe werden nicht mehr übergeben.

                    //temporärer Code für Zwischenstand:
                    if (temp[1].equals("B")) {
                        tiles[spalteX] [zeileY].setBlocked(true);
                    }
                    tiles[spalteX][zeileY].setFlagB(temp[1]);
                    if (tiles[spalteX][zeileY].getFlagF().equals("B")) {
                        tiles[spalteX][zeileY].setBlocked(true);
                        tiles[spalteX][zeileY].setPermanentBlocked(false);
                    }

                    if (temp[1].equals("P")) {
                        tiles[spalteX][zeileY].setBlocked(true);
                        tiles[spalteX][zeileY].setPermanentBlocked(true);
                    }

                    if (temp[1].equals("F")) {
                        tiles[spalteX][zeileY].setBlocked(false);
                        tiles[spalteX][zeileY].setPermanentBlocked(false);
                    }

                    tiles[spalteX][zeileY].setFlagI(temp[2]);
                    if (tiles[spalteX][zeileY].getFlagI().equals("I")) {
                        tiles[spalteX][zeileY].setHasItem(true);
                    }

                    index = Integer.parseInt(temp[3]);
                    img = tileSet.tiles[index].getImage();
                    tiles[spalteX][zeileY].setSubImage(img);
                    i++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getTileWidth() {
        return tileWidth;
    }

    public int getTileHeight() {
        return tileHeight;
    }

    public void renderLayer(Graphics2D g2d) {
        try {
            for (int zeileY = 0; zeileY<numberOfTilesY ; zeileY++){
                for (int spalteX = 0; spalteX < numberOfTilesX; spalteX++){
                    tiles[spalteX][zeileY].renderTile(g2d,spalteX,zeileY, tileWidth, tileHeight);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Fehler beim Rendern der Map.");
        }
    }
}




