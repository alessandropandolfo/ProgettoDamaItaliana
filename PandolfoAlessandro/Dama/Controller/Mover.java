package PandolfoAlessandro.Dama.Controller;

import PandolfoAlessandro.Dama.Model.Damiera;

/**
 * implements the move
 * @author Alessandro
 */
public class Mover {

    private final Damiera board;
    private Eater eater;
    private int xPed;
    private int yPed;
    private int xDest;
    private int yDest;
    private int valoreCella;
    private int valorePedina;

    /**
     *
     * @param tavola table on which operations are performed
     * @param xPed x position of the pawn in the table
     * @param yPed y position of the pawn in the table
     * @param x x position of the target cell
     * @param y y position of the target cell
     */
    public Mover(Damiera tavola, int xPed, int yPed, int x, int y) {
        this.xPed = xPed;
        this.yPed = yPed;
        this.xDest = x;
        this.yDest = y;
        this.board = tavola;
        eater = new Eater(this.board, this.xPed, this.yPed, this.xDest, this.yDest);
        this.valoreCella = this.board.getCell(x, y);
        this.valorePedina = this.board.getCell(xPed, yPed);
    }

    /**
     * It's used if you need to canMoveAgain
     * 
     * @param tavola table on which operations are performed
     * @param x x position of the target cell
     * @param y y position of the target cell
     */
    public Mover(Damiera tavola, int xPed, int yPed) {
        this.xPed = xPed;
        this.yPed = yPed;
        this.board = tavola;
        eater = new Eater(this.board, this.xPed, this.yPed, this.xDest, this.yDest);
        this.valorePedina = this.board.getCell(xPed, yPed);
    }

    /**
     *
     * @return true if no pawn is in multiple eaten or if the token is eaten in multiple
     */
    public boolean inMangiataMultipla() {
        int[] ped = board.getMangiataMultipla();
        if (ped[0] == -1 && ped[1] == -1) {
            return true;
        }
        if ((ped[0] == xPed && ped[1] == yPed) && (eater.canEat())) {
            return true;
        }
        return false;
    }

    /**
     *
     * @return true if no man is obliged to eat or if the token is
     *  one of those forced to eat
     */
    public boolean inMangiataObbligatoria() {
        int length = board.mangiataObbligatoria().length;
        if (length == 0) {
            return true;
        }
        boolean nonObbligato = true;
        int[][] pedine = board.mangiataObbligatoria();
        for (int[] ped : pedine) {
            if (valorePedina % 2 == board.getCell(ped[0], ped[1]) % 2) {
                nonObbligato = false;
            }
            if ((ped[0] == xPed && ped[1] == yPed) && (eater.canEat())) {
                return true;
            }
        }
        return nonObbligato;
    }

    /**
     *
     * @return true, if can move
     */
    public boolean canMove() {
        if ((xPed >= 0 && xPed < 8) && (yPed >= 0 && yPed < 8)
                && (xDest >= 0 && xDest < 8) && (yDest >= 0 && yDest < 8)) {
            valoreCella = board.getCell(xDest, yDest);
            if (inMangiataMultipla()
                    && inMangiataObbligatoria() && valoreCella == 0
                    && valorePedina != 0) {
                if (this.valorePedina > 2) {
                    return canMoveDamone();
                } else {
                    return canMovePedina();
                }
            }
        }
        return false;
    }

    private boolean canMoveDamone() {
        if (((xPed == xDest + 2) || (xPed == xDest - 2))
                && ((yPed == yDest + 2) || (yPed == yDest - 2))) {
            return eater.canEat();
        }
        if (((xPed == xDest + 1) || (xPed == xDest - 1))
                && ((yPed == yDest + 1) || (yPed == yDest - 1))) {
            return true;
        }
        return false;
    }

    private boolean canMovePedina() {
        int dirGiocatore = board.giocatore((valorePedina == 1 || valorePedina == 3) ? 1 : 2);
        if ((xPed == xDest + 2 * dirGiocatore) && ((yPed == yDest + 2) || (yPed == yDest - 2))) {
            return eater.canEat();
        }
        if ((xPed == xDest + dirGiocatore) && ((yPed == yDest + 1) || (yPed == yDest - 1))) {
            return true;
        }
        return false;
    }

    /**
     *
     * @return true, if can move
     */
    public boolean move() {
        if (this.canMove()) {
            if (eater.eat()) {
                board.getGame().cambioTurno();
                if (eater.canEatAgain()) {
                    int[] pedMM = {xDest, yDest};
                    board.setMangiataMultipla(pedMM);
                    if (board.getMangiataMultipla()[0] != -1 && board.getMangiataMultipla()[0] != -1) {
                        board.getGame().cambioTurno();
                    }
                } else {
                    board.resetMangiataMultipla();
                }
            } else {
                int direzione = board.giocatore(valorePedina);
                if (((xDest == 7 && direzione == -1) || (xDest == 0 && direzione == 1)) && (valorePedina < 3)) {
                    board.setCell(valorePedina + 2, xDest, yDest);
                } else {
                    board.setCell(valorePedina, xDest, yDest);
                }
                board.setCell(0, xPed, yPed);
                board.getGame().cambioTurno();
            }
            return true;
        }
        return false;
    }

    /**
     *
     * @return true, if can move again
     */
    public boolean canMoveAgain() {
        eater = new Eater(board, 0, 0, xPed, yPed);
        if(eater.canEatAgain()){
            return true;
        }
        if (board.getCell(xPed, yPed) > 2) {
            return canMoveAgainDama();
        }
        return canMoveAgainPedina();
    }

    private boolean canMoveAgainDama() {
        if (xPed < 7) {
            this.xDest = xPed + 1;
            if (yPed < 7) {
                this.yDest = yPed + 1;
                if (canMove()) {
                    return true;
                }
            }
            if (xPed > 0) {
                this.yDest = yPed - 1;
                if (canMove()) {
                    return true;
                }
            }
        }
        if (xPed > 0) {
            this.xDest = xPed + 1;
            if (yPed < 7) {
                this.yDest = yPed + 1;
                if (canMove()) {
                    return true;
                }
            }
            if (xPed > 0) {
                this.yDest = yPed - 1;
                if (canMove()) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean canMoveAgainPedina() {
        int dir = board.giocatore(valorePedina);
        if ((xPed - dir > 0 && dir > 0) || (xPed - dir < 8 && dir < 0)) {
            this.xDest = xPed - dir;
            if (yPed < 7) {
                this.yDest = yPed + 1;
                if (canMove()) {
                    return true;
                }
            }
            if (xPed > 0) {
                this.yDest = yPed - 1;
                if (canMove()) {
                    return true;
                }
            }
        }
        return false;
    }
}
