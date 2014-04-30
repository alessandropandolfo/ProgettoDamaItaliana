package PandolfoAlessandro.Dama.Controller;

import PandolfoAlessandro.Dama.Model.Damiera;

/**
 * implements the eat
 * @author Alessandro
 */
public class Eater {

    private final Damiera board;
    private int xPed;
    private int yPed;
    private int xDest;
    private int yDest;
    private int valorePedina;
    private int valCellaIntermedia;

    /**
     *
     * @param tavola table on which operations are performed
     * @param xP x position of the pawn in the table
     * @param yP y position of the pawn in the table
     * @param x x position of the target cell
     * @param y y position of the target cell
     */
    public Eater(Damiera tavola, int xP, int yP, int x, int y) {
        this.board = tavola;
        this.xPed = xP;
        this.yPed = yP;
        this.xDest = x;
        this.yDest = y;
        this.valorePedina = this.board.getCell(xPed, yPed);
    }

    /**
     *
     * @return value of the cell in the middle between the pawn and the target cell
     */
    public int getValCellaIntermedia() {
        return valCellaIntermedia;
    }

    /**
     *
     * @return true, if pawn can eat
     */
    public boolean canEat() {
        if (valorePedina > 2) {
            return canEatDamone();
        }
        return canEatPedina();
    }

    private boolean canEatDamone() {
        if (xPed < xDest) {
            if (yPed < yDest) {
                if (xPed > 6 || yPed > 6) {
                    return false;
                }
                valCellaIntermedia = board.getCell(xPed + 1, yPed + 1);
            } else {
                if (xPed > 6 || yPed < 1) {
                    return false;
                }
                valCellaIntermedia = board.getCell(xPed + 1, yPed - 1);
            }
        } else {
            if (yPed < yDest) {
                if (xPed < 1 || yPed > 6) {
                    return false;
                }
                valCellaIntermedia = board.getCell(xPed - 1, yPed + 1);
            } else {
                if (xPed < 1 || yPed < 1) {
                    return false;
                }
                valCellaIntermedia = board.getCell(xPed - 1, yPed - 1);
            }
        }
        if ((valCellaIntermedia % 2 != valorePedina % 2) && (valCellaIntermedia != 0) && (board.getCell(xDest, yDest) == 0)) {
            return true;
        }
        return false;
    }

    private boolean canEatPedina() {
        if (xPed - board.giocatore(valorePedina) < 7 && xPed - board.giocatore(valorePedina) >= 0) {
            if (yPed < yDest) {
                if (yPed > 6) {
                    return false;
                }
                valCellaIntermedia = board.getCell(xPed - board.giocatore(valorePedina), yPed + 1);
            } else {
                if (yPed < 1) {
                    return false;
                }
                valCellaIntermedia = board.getCell(xPed - board.giocatore(valorePedina), yPed - 1);
            }
        } else {
            return false;
        }
        if ((valCellaIntermedia != valorePedina)
                && ((valCellaIntermedia < 3)
                || (board.getMangiataMultipla()[0] == xPed && board.getMangiataMultipla()[0] == xPed))
                && (valCellaIntermedia != 0) && (board.getCell(xDest, yDest) == 0)) {
            return true;
        }
        return false;
    }

    /**
     *
     * @return true, if the pawn can eat
     */
    public boolean eat() {
        if (canEat()) {
            if (valorePedina > 2) {
                eatDamone();
            } else {
                eatPedina();
            }
            return true;
        }
        return false;
    }

    private void eatDamone() {
        int xInt;
        int yInt;
        if (xPed < xDest) {
            xInt = xPed + 1;
            if (yPed < yDest) {
                yInt = yPed + 1;
            } else {
                yInt = yPed - 1;
            }
        } else {
            xInt = xPed - 1;
            if (yPed < yDest) {
                yInt = yPed + 1;
            } else {
                yInt = yPed - 1;
            }
        }
        board.setCell(valorePedina, xDest, yDest);
        board.setCell(0, xInt, yInt);
        board.setCell(0, xPed, yPed);
    }

    private void eatPedina() {
        int xInt = xPed - board.giocatore(valorePedina);
        int yInt;
        if (yPed < yDest) {
            yInt = yPed + 1;
        } else {
            yInt = yPed - 1;
        }
        int direzione = board.giocatore(valorePedina);
        if (((xDest == 7 && direzione == -1) || (xDest == 0 && direzione == 1)) && (valorePedina < 3)) {
            board.setCell(valorePedina + 2, xDest, yDest);
        } else {
            board.setCell(valorePedina, xDest, yDest);
        }
        board.setCell(0, xInt, yInt);
        board.setCell(0, xPed, yPed);
    }

    /**
     *
     * @return true, if the pawn can eat again
     */
    public boolean canEatAgain() {
        this.valorePedina = board.getCell(xDest, yDest);
        this.xPed = xDest;
        this.yPed = yDest;
        if (valorePedina > 2) {
            return canEatAgainDama();
        }
        return canEatAgainPedina();
    }

    private boolean canEatAgainPedina() {
        int dirGiocatore = board.giocatore(valorePedina);
        if ((xPed > 1 && dirGiocatore == 1) || (xPed < 6 && dirGiocatore == -1)) {
            this.xDest = xPed - 2 * dirGiocatore;
            if (yPed < 6) {
                this.yDest = yPed + 2;
                if (canEat()) {
                    return true;
                }
            }
            if (yPed > 1) {
                this.yDest = yPed - 2;
                if (canEat()) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean canEatAgainDama() {
        if (xPed < 6) {
            this.xDest = xPed + 2;
            if (yPed < 6) {
                this.yDest = yPed + 2;
                if (canEat()) {
                    return true;
                }
            }
            if (yPed > 1) {
                this.yDest = yPed - 2;
                if (canEat()) {
                    return true;
                }
            }
        }
        if (xPed > 1) {
            this.xDest = xPed - 2;
            if (yPed < 6) {
                this.yDest = yPed + 2;
                if (canEat()) {
                    return true;
                }
            }
            if (yPed > 1) {
                this.yDest = yPed - 2;
                if (canEat()) {
                    return true;
                }
            }
        }
        return false;
    }
}
