
package clases;

import java.awt.Image;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author xp
 */
public class Disparo {
   Image imagenDisparo = null;
    //coordenadas del disparo
   private int x = 0;
   private int y = 0;
   
   public Disparo(){
        try {
            imagenDisparo = ImageIO.read((getClass().getResource("/imagenes/disparo.png")));
        } catch (IOException ex) {
           
        }
   }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
   
}
