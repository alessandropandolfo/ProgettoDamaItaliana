package PandolfoAlessandro.Dama.Model;

import PandolfoAlessandro.Dama.Controller.Controll;
import PandolfoAlessandro.Dama.View.Gioco;
import java.util.Arrays;
import java.util.Objects;

/**
 * This class represents the model of the game board of Italian dama.
 * 
 * Represents the table with an array of integers:
 * 
 *  0 = null cell; 
 *  1 = white pawn; 
 *  2 = black pawn; 
 *  3 = white dama;
 *  4 = black dama;
 * 
 * @author Alessandro
 */
public class Damiera {

    private static final int WIDTH = 8;
    private static final int HEIGTH = 8;
    private Gioco game;
    //Il valore corrisponde al colore della pedina ( 1 = bianca, 2 = nera)
    private int color;
    /*
     * Le celle contengono valori numerici corrispondenti a: 
     * - 0 = cella vuota 
     * - 1 = pedina bianca 
     * - 2 = pedina nera 
     * - 3 = dama bianca(intesa come damone)
     * - 4 = dama nera(intesa come damone)
     */
    private int[][] cells;
    //Salva la posizione della pedina in mangiata multipla
    //mangiataMultipla[0] = x pedina
    //mangiataMultipla[1] = y pedina
    private int[] mangiataMultipla;
    private int numMultipleEat;
    private Controll controller;
    

    /**
     * 
     * @param gioco indicates the game is connected to the board
     * @param color indicates the color chosen by the player
     */
    public Damiera(Gioco gioco, int color) {
        this.game = gioco;
        //Al giocatore è data la possibilità di decidere il colore con cui giocare
        //Il colore vale 1 o 2, questo viene controllato prima di creare la Damiera 
        this.color = color;
        int colCom = (color == 1) ? 2 : 1;
        cells = new int[HEIGTH][WIDTH];
        this.mangiataMultipla = new int[2];
        resetMangiataMultipla();
        /*
         * La damiera viene creata con già le pedine nelle posizioni di
         * partenza: 
         * 
         * - Le pedine del giocatore sono messe in basso; 
         * - Le pedine del computer in alto;
         */
        for (int i = 0; i < HEIGTH; i++) {
            for (int j = 0; j < WIDTH; j++) {
                if (i < 3 || i > HEIGTH - 4) {
                    if (i == 0 || i == 2) {
                        if (j % 2 == 0) {
                            cells[i][j] = colCom;
                        } else {
                            cells[i][j] = 0;
                        }
                    }
                    if (i == 1) {
                        if (j % 2 != 0) {
                            cells[i][j] = (colCom == 1) ? 1 : 2;
                        } else {
                            cells[i][j] = 0;
                        }
                    }
                    if (i == HEIGTH - 1 || i == HEIGTH - 3) {
                        if (j % 2 != 0) {
                            cells[i][j] = (color == 1) ? 1 : 2;
                        } else {
                            cells[i][j] = 0;
                        }
                    }
                    if (i == HEIGTH - 2) {
                        if (j % 2 == 0) {
                            cells[i][j] = (color == 1) ? 1 : 2;
                        } else {
                            cells[i][j] = 0;
                        }
                    }
                } else {
                    cells[i][j] = 0;
                }
            }
        }
    }
    
    /**
     * creates the associated controller to this Damiera
     */
    public void controll(){
        this.controller = new Controll(this);
    }

    /**
     *
     * @param mangiataMultipla pawn that is in multiple eat
     */
    public void setMangiataMultipla(int[] mangiataMultipla) {
        if (numMultipleEat == 2) {
            resetMangiataMultipla();
        } else {
            numMultipleEat++;
            this.mangiataMultipla = mangiataMultipla;
        }
    }

    /**
     *
     * @return pawn that is in multiple eat
     */
    public int[] getMangiataMultipla() {
        return mangiataMultipla;
    }

    /**
     *
     * @return an array containing the pawns required to eat
     */
    public int[][] mangiataObbligatoria() {
        return controller.mangiataObbligatoria();
    }

    //ritorna 1 se la pedina è del giocatore, -1 se la pedina è del computer
    /**
     *
     * @param colorePedina colore della pedina
     * @return 1, if the pawn belongs to human player, -1 ,if the pawn belongs to computer player
     */
    public int giocatore(int colorePedina) {
        if (colorePedina == 1) {
            if (color == 1) {
                return 1;
            } else {
                return -1;
            }
        }
        if (color == 2) {
            return 1;
        }
        return -1;
    }

    /**
     *
     * @return true, if a player win
     */
    public boolean checkWin() {
        return controller.checkWin();
    }

    
    /**
     *
     * @return color chosen by the human player
     */
    public int getColor() {
        return color;
    }

    /**
     *
     * @return the current game
     */
    public Gioco getGame() {
        return game;
    }

    /**
     *
     * @return the array model of game board
     */
    public int[][] getCells() {
        return cells;
    }

    /**
     *
     * @param x x position of chosen cell
     * @param y y position of chosen cell
     * @return value of cell in (x,y) position
     */
    public int getCell(int x, int y) {
        return this.cells[x][y];
    }

    /**
     *
     * @param value value of cell
     * @param x x position of chosen cell
     * @param y y position of chosen cell
     */
    public void setCell(int value, int x, int y) {
        this.cells[x][y] = value;
    }

    /**
     *
     * @param obj
     * @return true,if obj is equals to this
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Damiera other = (Damiera) obj;
        if (!Objects.equals(this.game, other.game)) {
            return false;
        }
        if (this.color != other.color) {
            return false;
        }
        if (!Arrays.deepEquals(this.cells, other.cells)) {
            return false;
        }
        if (!Arrays.equals(this.mangiataMultipla, other.mangiataMultipla)) {
            return false;
        }
        return true;
    }

    /**
     * resets the variable MangiataMultipla and the number of pawn eat with multiple eat
     */
    public final void resetMangiataMultipla() {
        numMultipleEat = 0;
        mangiataMultipla[0] = -1;
        mangiataMultipla[1] = -1;
    }

    

    /**
     * copy board in this Damiera
     * @param board 
     */
    public void copyBoard(Damiera board) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                this.setCell(board.getCell(i, j), i, j);
            }
        }
        this.setMangiataMultipla(board.getMangiataMultipla());
        this.numMultipleEat = board.numMultipleEat;
    }
}
