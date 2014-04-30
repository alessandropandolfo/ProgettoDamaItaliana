package PandolfoAlessandro.Dama.View;

import PandolfoAlessandro.Dama.Controller.Com;
import PandolfoAlessandro.Dama.Controller.Mover;
import PandolfoAlessandro.Dama.Model.Damiera;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * class representing the game
 * @author Alessandro
 */
public class Gioco extends JFrame {

    private boolean turno;
    private Damiera board;
    private Pedina pedinaSelezionata;
    private Com com;
    private boolean nonFinita;

    /**
     *
     * @param colore indicates the color chosen by the player
     */
    public Gioco(int colore) {
        super("Scacchiera");
        if (colore == 1) {
            turno = true;
        } else {
            turno = false;
        }
        nonFinita = true;
        board = new Damiera(this, colore);
        board.controll();
        pedinaSelezionata = null;
        this.setSize(75 * 8, 75 * 8);
        this.setResizable(false);
        GridLayout layout = new GridLayout(8, 8);
        layout.setHgap(0);
        layout.setVgap(0);
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                int coloreCella;
                if ((i % 2 == 0 && j % 2 == 0) || (i % 2 != 0 && j % 2 != 0)) {
                    coloreCella = 2;
                } else {
                    coloreCella = 1;
                }
                this.add(new Cella(this, coloreCella, i, j));
            }
        }
        this.setLayout(layout);
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        com = new Com(this);
        if (!turno) {
            try {
                Thread.sleep(600, 0);
                com.play();
            } catch (InterruptedException ex) {
                Logger.getLogger(Gioco.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        printDama();
    }

    private void printDama() {
        for (Component cnt : this.getContentPane().getComponents()) {
            Cella c = (Cella) cnt;
            c.removeAll();
            if (c.getColor() == 2) {
                int colorePed = board.getCell(c.getPosX(), c.getPosY());
                if (colorePed > 0 && colorePed < 5) {
                    Pedina ped = new Pedina(this, c.getPosX(), c.getPosY());;
                    c.add(ped);
                }
            }
        }
        repaint();
        this.setVisible(true);
    }

    /**
     *
     * @param ped pawn that requires the action listener
     * @return ActionListener for ped
     */
    public java.awt.event.ActionListener getActionListener(final Pedina ped) {
        return new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                if (turno) {
                    if (pedinaSelezionata == null) {
                        pedinaSelezionata = ped;
                    } else {
                        if (pedinaSelezionata != ped) {
                            pedinaSelezionata = null;
                        }
                    }
                }
            }
        };
    }

    /**
     *
     * @param cell cell that requires the muose listener
     * @return MouseListener for cell
     */
    public MouseListener getMouseListener(final Cella cell) {

        return new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent me) {
                if (pedinaSelezionata != null) {
                    Mover move = new Mover(returnGame().getBoard(),
                            pedinaSelezionata.getPosX(), pedinaSelezionata.getPosY(), cell.getPosX(), cell.getPosY());
                    if (!move.move()) {
                        JOptionPane.showMessageDialog(null, "mossa non valida");
                    } else {
                        if (board.checkWin()) {
                            setNonFinita(false);
                            board.getGame().getContentPane().setEnabled(false);
                        }
                    }
                    pedinaSelezionata = null;
                }
                while (!turno && nonFinita) {
                    try {
                        Thread.sleep(600, 0);
                        com.play();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Gioco.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    if (board.checkWin()) {
                        setNonFinita(false);
                        board.getGame().getContentPane().setEnabled(false);
                    }
                }
                printDama();
            }

            @Override
            public void mousePressed(MouseEvent me) {
            }

            @Override
            public void mouseReleased(MouseEvent me) {
            }

            @Override
            public void mouseEntered(MouseEvent me) {
            }

            @Override
            public void mouseExited(MouseEvent me) {
            }
        };
    }

    private Gioco returnGame() {
        return this;
    }

    /**
     *
     * @return board on which the game is played
     */
    public Damiera getBoard() {
        return board;
    }

    /**
     *
     * @param nonFinita true,if game is not finish, false, if is finish
     */
    public void setNonFinita(boolean nonFinita) {
        this.nonFinita = nonFinita;
    }

    /**
     *
     * @return true,if it is the human player's turn , false, if it is the computer turn
     */
    public boolean isTurno() {
        return turno;
    }

    /**
     *  change turn
     */
    public void cambioTurno() {
        this.turno = (turno) ? false : true;
    }
}
