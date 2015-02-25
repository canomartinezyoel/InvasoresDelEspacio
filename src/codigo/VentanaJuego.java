/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package codigo;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.Timer;

/**
 *
 * @author Yoel Cano
 */
public class VentanaJuego extends javax.swing.JFrame {
    //alto y ancho de la ventana
    int anchoPantalla = 800;
    int altoPantalla = 650;
    
    //Buffer para dibujar
    BufferedImage buffer = null;
    
    //declaro un objeto de tipo nave
    Nave miNave = new Nave(anchoPantalla);
    
    //declaro dos variables booleanas que controlen el movimiento de la nave
    boolean pulsadaDerecha = false;
    boolean pulsadaIzquierda = false;
    
    ArrayList <Disparo> listaDisparos = new ArrayList();
    Disparo disparoAux;
    
    ArrayList <Marciano> listaMarcianos = new ArrayList();
    int velocidadMarciano = 1;
    
    ArrayList <Explosion> listaExplosiones = new ArrayList();
    
    int contadorTiempo = 0;
    
    //Timer llama a bucle del juego cada 10ms.
    Timer temporizador = new Timer(10, new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            //aquí llamo al método del juego
           bucleDelJuego();
        }
    });
    
    
    /**
     * Creates new form VentanaJuego
     */
    public VentanaJuego() {
        initComponents();
        this.setSize(anchoPantalla, altoPantalla +50);
        
        //creamos el buffer a partir del JPanel
        buffer = (BufferedImage) jPanel1.createImage(anchoPantalla, altoPantalla);
        buffer.createGraphics();
        
        //inicio el juego
        temporizador.start();
        
        //posiciona la nave abajo del todo
        miNave.setX(anchoPantalla /2);
        miNave.setY(altoPantalla - miNave.imagenNave.getHeight(null));
        
        //inicializo el arrayList de los marcianos
        for(int j=0; j<4; j++){
            for(int i=0; i< 10; i++){
                Marciano m = new Marciano();
                m.setX(i * (m.ancho + 15));
                m.setY(m.ancho * j);
                listaMarcianos.add(m);
                
            }
        }
    }
    
    private void pintaMarcianos(Graphics2D miGrafico){
        //uso una variable booleana para indicar si ha tocado
        //en la pared derecha o en la pared izquierda
        //este bucle recorre la lista de marcianos y los
        //va pintando en las coordenada correspondiente
        boolean cambia = false;
        
        for (int i=0; i<listaMarcianos.size(); i++){
            Marciano m = listaMarcianos.get(i);
            m.setX(m.getX() + velocidadMarciano);
            //si choca en la pared derecha
            if ((m.getX() + m.ancho) > anchoPantalla){
                cambia = true;
            }
            //si choca en la pared izquierda
            if (m.getX() <=0){
                cambia = true;
            }
            //Dibujo la imagen correspondiente de los marcianor que cambbian en funcion de un timer.
            if(contadorTiempo < 50){
                miGrafico.drawImage(m.imagen1, m.getX(), m.getY(), null);
            }
            else{
                miGrafico.drawImage(m.imagen2, m.getX(), m.getY(), null);
            }
        }
        //si ha tocado cambia la velocidad
        if (cambia){
            velocidadMarciano = -velocidadMarciano;
            for(int i=0; i<listaMarcianos.size(); i++){
                Marciano m = listaMarcianos.get(i);
                m.setY(m.getY() + m.ancho/2);
            }                    //Desplazamiento de los marcianos hacia abajo.
        }
    }
    
    private void pintaNave(Graphics2D g2){
        if (pulsadaIzquierda){
            miNave.setX(miNave.getX() - 4);
        }                              //Velocidad desplazamiento de la nave
        else if (pulsadaDerecha){
            miNave.setX(miNave.getX() + 4);
        }                              //Velocidad desplazamiento de la nave
        g2.drawImage(miNave.imagenNave, miNave.getX(), miNave.getY(), null);
    }
    
    private void pintoDisparos(Graphics2D g2){
        //pinta los disparos
        for(int i=0; i<listaDisparos.size(); i++){
            disparoAux = listaDisparos.get(i);
            disparoAux.setY(disparoAux.getY() - 10);
            if (disparoAux.getY() < 0){
                listaDisparos.remove(i);
            }
            g2.drawImage(disparoAux.imagenDisparo, disparoAux.getX(), disparoAux.getY(), null);
        }
    }
    
    private void pintaExplosiones(Graphics2D g2){
        //pinta la explosion
        for (int i=0; i<listaExplosiones.size(); i++){
            Explosion e = listaExplosiones.get(i);
            e.setTiempoDeVida(e.getTiempoDeVida() - 1);
            if (e.getTiempoDeVida() >= 25){
                g2.drawImage(e.imagenExplosion, e.getX(), e.getY(), null);
            }
            else {
                g2.drawImage(e.imagenExplosion2, e.getX(), e.getY(), null);
            }
            
            //si el tiempo de la vida de la explosión es menor que 0 la elimino
            if (e.getTiempoDeVida() <= 0){
                listaExplosiones.remove(i);
            }
        }
    }
    
    private void chequeaColision(){
        //creo un marco para guardar el borde de la imagen del marciano
        Rectangle2D.Double rectanguloMarciano = new Rectangle2D.Double();
        
        //creo un marco para guardar el borde de la imagen del disparo
        Rectangle2D.Double rectanguloDisparo = new Rectangle2D.Double();
        
        //creo un marco para guardar el borde de la imagen de la explosion
        Rectangle2D.Double rectanguloExplosion = new Rectangle2D.Double();
        
        //ahora leo la lista de disparos
        for (int j=0; j<listaDisparos.size(); j++){
            Disparo d = listaDisparos.get(j);
            //asigno al rectángulo las dimensiones del disparo y su posición
            rectanguloDisparo.setFrame(d.getX(), d.getY(), d.imagenDisparo.getWidth(null), d.imagenDisparo.getHeight(null));
            
            boolean disparoABorrar =  false;
            //leo la lista de marcianos y comparo uno a uno con el disparo
            for (int i=0; i<listaMarcianos.size(); i++){
                Marciano m = listaMarcianos.get(i);
                rectanguloMarciano.setFrame(m.getX(), m.getY(), m.ancho, m.ancho);
                if (rectanguloDisparo.intersects(rectanguloMarciano)){
                    Explosion e = new Explosion();
                    e.setX(m.getX());
                    e.setY(m.getY());
                    listaExplosiones.add(e);
                    listaMarcianos.remove(i);
                    //listaDisparos.remove(j);
            
                    disparoABorrar = true;
                }
            }
            if (disparoABorrar){
                listaDisparos.remove(j);
            }
            
        }
        
    }
    
    private void actualizaContadorTiempo(){
        contadorTiempo++;
        if(contadorTiempo > 100){
            contadorTiempo = 0;
        }
    }
    
    private void bucleDelJuego(){
        //primero apunto al buffer
        Graphics2D g2 =(Graphics2D) buffer.getGraphics();

        //pinto un réctangulo negro del tamaño de la ventana
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0 ,anchoPantalla, altoPantalla);
        
        /////////////////////////////////////////////////////
        ////////////código del juego/////////////////////////
        
        pintaMarcianos(g2);
        pintaNave(g2);
        pintoDisparos(g2);
        chequeaColision();
        pintaExplosiones(g2);
        actualizaContadorTiempo();
        /////////////////////////////////////////////////////
        //apunto al JPanel y dibujo el buffer sobre el JPanel
        g2 = (Graphics2D) jPanel1.getGraphics();
        g2.drawImage(buffer, 0, 0, null);
        
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                formKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 388, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 288, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyReleased
        if (evt.getKeyCode() != KeyEvent.VK_SPACE){
            pulsadaIzquierda = false;
            pulsadaDerecha = false;
        }
    }//GEN-LAST:event_formKeyReleased

    private void formKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyPressed
       if (evt.getKeyCode() == KeyEvent.VK_LEFT){
            pulsadaIzquierda = true;
            pulsadaDerecha = false;
       }
       if (evt.getKeyCode() == KeyEvent.VK_RIGHT){
            pulsadaIzquierda = false;
            pulsadaDerecha = true;
       }
       //añado un disparo si se ha pulsado la barra espaciadora
       if ((evt.getKeyCode() == KeyEvent.VK_SPACE) && (listaDisparos.size() < 2)){
                                                                             //2 disparos maximos simultaneos.
            Disparo d = new Disparo();
            d.setX( miNave.getX() + miNave.getAnchoNave() /2 - d.imagenDisparo.getWidth(null)/2);
            d.setY( miNave.getY());
            
            //agrego el disparo a la lista de disparos
            listaDisparos.add(d);
       }
    }//GEN-LAST:event_formKeyPressed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(VentanaJuego.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(VentanaJuego.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(VentanaJuego.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(VentanaJuego.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new VentanaJuego().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
}
