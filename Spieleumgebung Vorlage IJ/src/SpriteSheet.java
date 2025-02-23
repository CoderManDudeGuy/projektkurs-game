import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.*;


public class SpriteSheet {
    private BufferedImage spriteSheet;
    private BufferedImage[][] sprites;

    public SpriteSheet(String pPath, int pDirections, int pMoves, int pWidth, int pHeight){
        sprites = new BufferedImage[pDirections][pMoves];
        try {
            spriteSheet = ImageIO.read(new File(pPath));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        for(int dir = 0; dir < pDirections; dir++) {
            for(int mov = 0; mov < pMoves; mov++) {
                sprites[dir][mov] = spriteSheet.getSubimage(mov * pWidth, dir * pHeight, pWidth, pHeight);
            }
        }
    }
    public BufferedImage getSpriteElement(int pDirections, int pMove) {
        return sprites[pDirections][pMove];
    }
}
