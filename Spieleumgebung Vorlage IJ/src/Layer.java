import java.awt.*;
import java.util.ArrayList;

public class Layer {

    private int tileWidth; //Das ist die Breite eines Tiles in der Map (bzw. im Layer); kann differieren von der Tilebriete im TileSet
    private int tileHeight; //Das ist die Höhe eines Tiles in der Map; kann differieren von der Tilehöhe im TileSet
    private int numberOfTilesX;
    private int numberOfTilesY;
    public ArrayList<Integer> enemiesPos = new ArrayList<>();
    private ArrayList<Tile> neighbours = new ArrayList<>();
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

                    if (temp[2].equals("E")){
                        enemiesPos.add(0);
                        enemiesPos.add(spalteX*64);
                        enemiesPos.add(zeileY*64);
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

    public ArrayList<Tile> getNeighbours(int xPos, int yPos, int height, int width){
        neighbours.clear();

        if(xPos/tileWidth - 1 >= 0 && yPos/tileHeight - 1 >= 0){
            //left
            //up
            tiles[(xPos/tileWidth)-1][(yPos/tileHeight)-1].isNeighbour = true;
            neighbours.add(tiles[(xPos/tileWidth)-1][(yPos/tileHeight)-1]);
        }

        if(yPos/tileHeight - 1 >= 0){
            //up
            tiles[(xPos/tileWidth)][(yPos/tileHeight)-1].isNeighbour = true;
            neighbours.add(tiles[(xPos/tileWidth)][(yPos/tileHeight)-1]);
        }

        if(yPos/tileHeight - 1 >= 0 && xPos/tileWidth + 1 < numberOfTilesX){
            //up
            //right
            tiles[(xPos/tileWidth)+1][(yPos/tileHeight)-1].isNeighbour = true;
            neighbours.add(tiles[(xPos/tileWidth)+1][(yPos/tileHeight)-1]);
        }

        if(xPos/tileWidth - 1 >= 0){
            //left
            tiles[(xPos/tileWidth-1)][(yPos/tileHeight)].isNeighbour = true;
            neighbours.add(tiles[(xPos/tileWidth)-1][(yPos/tileHeight)]);
        }

        if(xPos/tileWidth + 1 < numberOfTilesX){
            //right
            tiles[(xPos/tileWidth)+1][(yPos/tileHeight)].isNeighbour = true;
            neighbours.add(tiles[(xPos/tileWidth)+1][(yPos/tileHeight)]);
        }

        if(yPos/tileHeight + 1 < numberOfTilesY && xPos/tileWidth - 1 >= 0){
            //down
            //left
            tiles[(xPos/tileWidth)-1][(yPos/tileHeight)+1].isNeighbour = true;
            neighbours.add(tiles[(xPos/tileWidth)-1][(yPos/tileHeight)+1]);
        }

        if(yPos/tileHeight + 1 < numberOfTilesY){
            //down
            tiles[(xPos/tileWidth)][(yPos/tileHeight)+1].isNeighbour = true;
            neighbours.add(tiles[(xPos/tileWidth)][(yPos/tileHeight)+1]);
        }

        if(xPos/tileWidth + 1 <= numberOfTilesX && yPos/tileHeight + 1 < numberOfTilesY){
            //right
            //down
            tiles[(xPos/tileWidth)+1][(yPos/tileHeight)+1].isNeighbour = true;
            neighbours.add(tiles[(xPos/tileWidth)+1][(yPos/tileHeight)+1]);
        }

//        if((yPos/tileWidth)-1 >= 0) {
//
//        }
//        if((xPos/tileWidth)-1 >= 0&&(yPos/tileWidth)-1 >= 0) {
//
//        }
//        if((xPos/tileWidth)-1 >= 0) {
//
//        }
//        if((xPos/tileWidth)-1 >= 0&&(yPos/tileWidth)+1 <) {
//
//        }


        return neighbours;
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




