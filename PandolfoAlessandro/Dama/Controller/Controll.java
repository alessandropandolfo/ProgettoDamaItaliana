package PandolfoAlessandro.Dama.Controller;

import PandolfoAlessandro.Dama.Model.Damiera;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.JOptionPane;

/**
 * Checks the data in the Damiera
 * @author Alessandro
 */
public class Controll {

    private Damiera board;
    private ArrayList<int[][]> pedineInTavola;
    private ArrayList<Integer> ripetizioni;

    /**
     *
     * @param board the board it must control
     */
    public Controll(Damiera board) {
        this.board = board;
        this.pedineInTavola = new ArrayList<>();
        this.ripetizioni = new ArrayList<>();
    }
    
    /**
     *
     * @return an array containing the pawns required to eat
     */
    public int[][] mangiataObbligatoria() {
        int[][] arrPed;
        ArrayList<int[]> pedine = new ArrayList<>();
        pedine.clear();
        Eater eat;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board.getCell(i, j) != 0) {
                    eat = new Eater(board, i, j, i, j);
                    if (eat.canEatAgain()) {
                        int[] temp = new int[2];
                        temp[0] = i;
                        temp[1] = j;
                        pedine.add(temp);
                    }
                }
            }
        }
        arrPed = new int[pedine.size()][2];
        int i = 0;
        while (i < pedine.size()) {
            arrPed[i] = pedine.get(i);
            i++;
        }
        return arrPed;
    }

    /**
     *
     * @return true, if a player win
     */
    public boolean checkWin() {
        String vincitore;
        int colorePerdente = 1;
        if (!colorIsPresent(colorePerdente)) {
            if (colorePerdente == board.getColor()) {
                vincitore = "Hai perso";
            } else {
                vincitore = "Hai vinto";
            }
            JOptionPane.showMessageDialog(null, vincitore);
            return true;
        }
        if (!bloccato(colorePerdente)) {
            if (colorePerdente == board.getColor()) {
                System.out.println(this.toString());
                vincitore = "Giocatore bloccato!\nHai perso";
            } else {
                vincitore = "Giocatore bloccato!\nHai vinto";
            }
            JOptionPane.showMessageDialog(null, vincitore);
            return true;
        }
        colorePerdente = 2;
        if (!colorIsPresent(colorePerdente)) {
            if (colorePerdente == board.getColor()) {
                vincitore = "Hai perso";
            } else {
                vincitore = "Hai vinto";
            }
            JOptionPane.showMessageDialog(null, vincitore);
            return true;
        }
        if (!bloccato(colorePerdente)) {
            if (colorePerdente == board.getColor()) {
                vincitore = "Giocatore bloccato!\nHai perso";
            } else {
                vincitore = "Giocatore bloccato!\nHai vinto";
            }
            JOptionPane.showMessageDialog(null, vincitore);
            return true;
        }
        if(isPatta()){
            JOptionPane.showMessageDialog(null, "Patta!\nLe posizioni si sono ripetute 3 volte");
            return true;
        }
        return false;
    }

    /**
     *
     * @param color color of selected pawn
     * @return true, if pawn's color is present
     */
    public boolean colorIsPresent(int color) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board.getCell(i, j) % 2 == color % 2 && board.getCell(i, j) != 0) {
                    return true;
                }
            }
        }
        return false;
    }
    
    /**
     * saving positions in the matrix
     * @param posizioni matrix to save
     */
    public void aggiunta(int[][] posizioni){
        boolean trovato = false;
        for(int[][] celle :pedineInTavola){
            if(Arrays.equals(celle, posizioni)){
                trovato = true;
                ripetizioni.add(pedineInTavola.indexOf(celle), ripetizioni.get(pedineInTavola.indexOf(celle)));
            }
        }
        if(!trovato){
            pedineInTavola.add(posizioni);
            ripetizioni.add(pedineInTavola.indexOf(posizioni), 0);
        }
    }
    
    private boolean bloccato(int colorePerdente) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board.getCell(i, j) != 0) {
                    int col = (board.getCell(i, j) == 1 || board.getCell(i, j) == 3) ? 1 : 2;
                    if (col == colorePerdente) {
                        Mover muovere = new Mover(board, i, j);
                        if (muovere.canMoveAgain() || !muovere.inMangiataMultipla()) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private boolean isPatta() {
        for(Integer i:ripetizioni){
            if(i>2){
                return true;
            }
        }
        return false;
    }
}

