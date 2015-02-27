
package codigo;

import java.awt.Image;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

/**
 *
 * @author Yoel Cano
 */
public class Explosion {
    //creo las imagenes de la explosión
    Image imagenExplosion = null;
    Image imagenExplosion2 = null;
    Clip sonidoExplosion;
    
    //coordenadas de la explosión
    private int x = 0;
    private int y = 0;
    
    //creo un valor que es el tiempo que durará la primera imágen
    private int tiempoDeVida = 50;
    
    public Explosion(){
        try {
            sonidoExplosion = AudioSystem.getClip();
            sonidoExplosion.open(AudioSystem.getAudioInputStream(getClass().getResource("/sonidos/explosion.wav")));
            
            imagenExplosion = ImageIO.read((getClass().getResource("/imagenes/e1.png")));
            imagenExplosion2 = ImageIO.read((getClass().getResource("/imagenes/e2.png")));
        } catch (Exception ex) {
            
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

    public int getTiempoDeVida() {
        return tiempoDeVida;
    }

    public void setTiempoDeVida(int tiempoDeVida) {
        this.tiempoDeVida = tiempoDeVida;
    }
    
}
