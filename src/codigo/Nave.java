
package codigo;

import java.awt.Image;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author Yoel Cano
 */
public class Nave {
    Image imagenNave = null;
        
       //posición x-y de la nave
    private int x = 0;
    private int y = 0;
    private int anchoMaximo;
    private int anchoNave;
    
    public Nave (int ancho){
        anchoMaximo = ancho;
        try {
            imagenNave = ImageIO.read((getClass().getResource("/imagenes/nave.png")));
        } catch (IOException ex) {
            
        }
        anchoNave = imagenNave.getWidth(null);
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        if (x<0){
            x=0;
        }
        if (x > anchoMaximo - anchoNave){
            x = anchoMaximo - anchoNave;
        }
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getAnchoNave() {
        return anchoNave;
    }
    
}
