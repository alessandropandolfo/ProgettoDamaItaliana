
package PandolfoAlessandro.Dama.View;

import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

/**
 * Representation of the cells
 * @author Alessandro
 */
public class Cella extends JPanel{
    private final int X;
    private final int Y;
    //1: bianco
    //2: nero
    private final int COLOR;
    
    /**
     *
     * @param game indicates the game is connected to the cell
     * @param colore color of cell
     * @param posX x position in the board
     * @param posY y position in the board
     */
    public Cella(Gioco game, int colore, int posX, int posY){
        super();
        this.COLOR = colore;
        this.X = posX;
        this.Y = posY;
        if(colore == 2){
            this.setBackground(Color.BLACK);
        }
        this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        this.setVisible(true);
        this.addMouseListener(game.getMouseListener(this));
    }
    
    /**
     *
     * @return x position in the board
     */
    public int getPosX(){
        return X;
    }
    
    /**
     *
     * @return x position in the board
     */
    public int getPosY(){
        return Y;
    }
    
    /**
     *
     * @return color of cell
     */
    public int getColor(){
        return this.COLOR;
    }
    
    
}
