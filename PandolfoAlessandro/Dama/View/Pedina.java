package PandolfoAlessandro.Dama.View;

import java.awt.Color;
import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 * Representation of the pawn
 * @author Alessandro
 */
public class Pedina extends JButton {

    private int x;
    private int y;
    private Gioco game;
    private final int COLOR;
    private boolean damone;
    //percorsi per le immagini delle pedina
    private final static String pathPedinaBianca = "contenuti/pb.gif";
    private final static String pathPedinaNera = "contenuti/pn.gif";
    private final static String pathDamaBianca = "contenuti/db.gif";
    private final static String pathDamaNera = "contenuti/dn.gif";

    /**
     *
     * @param gioco indicates the game is connected to the pawn
     * @param x x position in the board
     * @param y y position in the board
     */
    public Pedina(Gioco gioco, int x, int y) {
        super();
        this.game = gioco;
        this.x = x;
        this.y = y;
        this.setBorder(null);
        int valore = gioco.getBoard().getCell(x, y);
        this.COLOR = (valore < 3) ? valore : valore - 2;
        this.setIcon(new ImageIcon((COLOR == 1) ? pathPedinaBianca : pathPedinaNera));
        if (valore > 2) {
            this.damone();
        } else {
            this.damone = false;
        }
        this.setBackground(Color.BLACK);
        if (game.getBoard().getColor() == COLOR) {
            this.addActionListener(game.getActionListener(this));
        }

    }

    /**
     *
     * @return indicates the game is connected to the pawn
     */
    public Gioco getGame() {
        return game;
    }

    /**
     *
     * @return x position in the board
     */
    public int getPosX() {
        return this.x;
    }

    /**
     *
     * @return y position in the board
     */
    public int getPosY() {
        return this.y;
    }

    /**
     *
     * @return color of pawn
     */
    public int getCOLOR() {
        return COLOR;
    }

    /**
     *
     * @return true, if this is a dama, false, if it's a pawn
     */
    public boolean isDamone() {
        return damone;
    }

    /**
     * does become a pawn in a dama
     */
    public final void damone() {
        this.damone = true;
        this.setIcon(null);
        this.setIcon(new ImageIcon((COLOR == 1) ? pathDamaBianca : pathDamaNera));
    }
}
