public class Camera {

    private final int mapWidth, mapHeight;
    private final int gamePanelWidth, gamePanelHeight;
    private int xOffset =0, yOffset = 0;


    public Camera(int pMapWidth, int pMapHeight, int pGamePanelWidth, int pGamePanelHeight) {
        mapWidth = pMapWidth;
        mapHeight = pMapHeight;
        gamePanelWidth = pGamePanelWidth;
        gamePanelHeight = pGamePanelHeight;
    }

    public void centerOnMover(Mover pMover){
        xOffset = (int) pMover.getLocation().getX() - gamePanelWidth/2;
        if (xOffset < 0){
            xOffset = 0;
        } else{
            int t = mapWidth - gamePanelWidth;
            if (xOffset > t){
                xOffset = t;
            }
        }

        yOffset = (int) pMover.getLocation().getY() - gamePanelHeight/2;
        if (yOffset < 0){
            yOffset = 0;
        } else{
            int t = mapHeight - gamePanelHeight;
            if (yOffset > t){
                yOffset = t;
            }
        }
    }

    public int getXOffset() {
        return xOffset;
    }

    public int getYOffset() {
        return yOffset;
    }
}
