import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class Map {
    public ArrayList<Layer> layer = new ArrayList<>();
    private int mapWidth;
    private int mapHeight;


    public Map(String pMapPlanPath) {
        readAndCreateMap(pMapPlanPath);
    }

    public void readAndCreateMap(String pPath) {
        try (BufferedReader in = new BufferedReader(new FileReader(pPath))) {
            int numberOfLayers = Integer.parseInt(in.readLine()); //Zeile 1: Anzahl der Layer der Map
            for (int i = 0; i < numberOfLayers; i++) {
                LayerDataSet layerDataSet = new LayerDataSet();
                layerDataSet.tileSetData = in.readLine().split("#"); // Zeile 2: Daten für das Tileset

                String[] token = in.readLine().split("#"); // Zeile 3: Größe des Layers und Größe der Tiles im Layer
                layerDataSet.numberOfTilesX = Integer.parseInt(token[0]);
                layerDataSet.numberOfTilesY = Integer.parseInt(token[1]);
                layerDataSet.tileWidth = Integer.parseInt(token[2]);
                layerDataSet.tileHeight = Integer.parseInt(token[3]);
                mapWidth = layerDataSet.numberOfTilesX * layerDataSet.tileWidth;
                mapHeight = layerDataSet.numberOfTilesY * layerDataSet.tileHeight;

                //Daten für die einzelnen Tiles:
                StringBuilder sb = new StringBuilder();
                for (int j = 0; j < layerDataSet.numberOfTilesY; j++) {
                    sb.append(in.readLine());
                }
                layerDataSet.tilesData = sb.toString().split("#");
                Layer temp = new Layer(layerDataSet);
                layer.add(temp);

            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public ArrayList<Layer> getLayerList() {
        return layer;
    }

    public int getMapWidth() {
        return mapWidth;
    }

    public int getMapHeight() {
        return mapHeight;
    }

    public void renderMap(Graphics2D g2d) {
        try {
            for (Layer l: layer) {
                l.renderLayer(g2d);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Fehler beim Rendern der Map.");
        }
    }

    public ArrayList<Tile> getNeighbours(int xPos, int yPos, int height, int width){
        return layer.get(0).getNeighbours(xPos, yPos, height, width);
    }
}



