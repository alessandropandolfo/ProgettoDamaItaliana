package PandolfoAlessandro.Dama.Controller;

import PandolfoAlessandro.Dama.Model.Damiera;
import PandolfoAlessandro.Dama.View.Gioco;
import java.util.ArrayList;

/**
 * It's the computer player
 * @author Alessandro
 */
public class Com {

    private Gioco game;
    private Damiera board;
    int prof;

    /**
     *
     * @param gioco indicates the game in which it participates
     */
    public Com(Gioco gioco) {
        this.game = gioco;
        this.board = gioco.getBoard();
    }

    /**
     * makes the choices and moves
     */
    public void play() {
        prof = 0;
        int[][] pedine = usabili();
        ArrayList<int[][]> celle = new ArrayList<>();
        for (int i = 0; i < pedine.length; i++) {
            int[][] cells = whereMove(pedine[i][0], pedine[i][1]);
            celle.add(i, cells);
        }
        ArrayList<int[]> valori = new ArrayList<>();
        for (int i = 0; i < pedine.length; i++) {
            int[] valore = new int[celle.get(i).length];
            int j = 0;
            for (int[] cella : celle.get(i)) {
                valore[j] = valueOfMove(pedine[i][0], pedine[i][1], cella[0], cella[1]);
            }
            j++;
            valori.add(i, valore);
        }
        int migliore = 0;
        int pos = 0;
        int mossa = 0;
        for (int[] value : valori) {
            for (int z = 0; z < value.length; z++) {
                if (value[z] > migliore) {
                    pos = valori.indexOf(value);
                    mossa = z;
                }
            }
        }
        Mover mover = new Mover(board, pedine[pos][0], pedine[pos][1], celle.get(pos)[mossa][0], celle.get(pos)[mossa][1]);
        if (mover.move()) {
            if (!game.isTurno() && ((game.getBoard().getMangiataMultipla()[0] != celle.get(pos)[mossa][0])
                    && (game.getBoard().getMangiataMultipla()[1] != celle.get(pos)[mossa][1]))) {
                game.cambioTurno();
            } else {
                if (game.isTurno() && ((game.getBoard().getMangiataMultipla()[0] == celle.get(pos)[mossa][0])
                        && (game.getBoard().getMangiataMultipla()[1] == celle.get(pos)[mossa][1]))) {
                    game.cambioTurno();
                }
            }
        }
    }

    private int[][] usabili() {
        ArrayList<int[]> usabili = new ArrayList<>();
        int colAvv = (board.getColor() == 1) ? 2 : 1;
        Mover mover;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if ((board.getCell(i, j) % 2 == colAvv % 2) && (board.getCell(i, j) != 0)) {
                    mover = new Mover(board, i, j);
                    if (mover.canMoveAgain() || !mover.inMangiataMultipla()) {
                        int[] temp = new int[2];
                        temp[0] = i;
                        temp[1] = j;
                        usabili.add(temp);
                    }
                }
            }
        }
        int[][] arrPed = new int[usabili.size()][2];
        int i = 0;
        while (i < usabili.size()) {
            arrPed[i] = usabili.get(i);
            i++;
        }
        return arrPed;
    }

    private int[][] whereMove(int x, int y) {
        ArrayList<int[]> celle = new ArrayList<>();
        Mover mover;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board.getCell(i, j) == 0) {
                    mover = new Mover(board, x, y, i, j);
                    if (mover.canMove()) {
                        int[] tmp = new int[2];
                        tmp[0] = i;
                        tmp[1] = j;
                        celle.add(tmp);
                    }
                }
            }
        }
        int[][] arrPed = new int[celle.size()][2];
        int i = 0;
        while (i < celle.size()) {
            arrPed[i] = celle.get(i);
            i++;
        }
        return arrPed;
    }

    private int valueOfMove(int xPed, int yPed, int x, int y) {
        prof++;
        int point = 1;
        int mangiate = howCanEat(board, xPed, yPed, x, y);
        int persePreMossa = howLost(board);
        Damiera test = new Damiera(game, board.getColor());
        test.copyBoard(board);
        test.controll();
        Mover mover = new Mover(test, xPed, yPed, x, y);
        mover.move();
        int persePostMossa = howLost(test);
        point += 2*(persePostMossa - persePreMossa);
        point += mangiate;
        if (prof == 1) {
            int[][] pedine = usabili();
            ArrayList<int[][]> celle = new ArrayList<>();
            for (int i = 0; i < pedine.length; i++) {
                int[][] cells = whereMove(pedine[i][0], pedine[i][1]);
                celle.add(i, cells);
            }
            ArrayList<int[]> valori = new ArrayList<>();
            for (int i = 0; i < pedine.length; i++) {
                int[] valore = new int[celle.get(i).length];
                int j = 0;
                for (int[] cella : celle.get(i)) {
                    valore[j] = valueOfMove(pedine[i][0], pedine[i][1], cella[0], cella[1]);
                }
                j++;
                valori.add(i, valore);
            }
            int migliore = 0;
            int pos = 0;
            int mossa = 0;
            for (int[] value : valori) {
                for (int z = 0; z < value.length; z++) {
                    if (value[z] > migliore) {
                        pos = valori.indexOf(value);
                        mossa = z;
                    }
                }
            }
            point+=migliore;
        }
        return point;
    }

    private int howCanEat(Damiera board1, int xPed, int yPed, int x, int y) {
        Damiera test = new Damiera(game, board1.getColor());
        test.copyBoard(board1);
        test.controll();
        double mangiate = howEat(test, xPed, yPed, x, y);
        return (int) Math.ceil(mangiate);
    }

    private double howEat(Damiera test, int xPed, int yPed, int x, int y) {
        Eater eater = new Eater(test, xPed, yPed, x, y);
        if (eater.eat()) {
            if (eater.getValCellaIntermedia() > 2) {
                if (eater.canEatAgain()) {
                    double val1;
                    if (x + 2 < 8 && y + 2 < 8) {
                        val1 = howEat(test, x, y, x + 2, y + 2);
                    } else {
                        val1 = 0;
                    }
                    double val2;
                    if (x + 2 < 8 && y - 2 >= 0) {
                        val2 = howEat(test, x, y, x + 2, y - 2);
                    } else {
                        val2 = 0;
                    }
                    double val3;
                    if (x - 2 >= 0 && y + 2 < 8) {
                        val3 = howEat(test, x, y, x - 2, y + 2);
                    } else {
                        val3 = 0;
                    }
                    double val4;
                    if (x - 2 >= 0 && x - 2 >= 0) {
                        val4 = howEat(test, x, y, x - 2, y - 2);
                    } else {
                        val4 = 0;
                    }
                    return (Math.max(val1, val2) > Math.max(val3, val4)) ? Math.max(val1, val2) : Math.max(val3, val4);
                }
                return 1.5;
            }
            if (eater.canEatAgain()) {
                double val1;
                double val2;
                int dir = test.giocatore(test.getCell(x, y));
                if ((x - 2 * dir >= 0 && dir > 0) || (x - 2 * dir < 8 && dir < 0)) {
                    if (y + 2 < 8) {
                        val1 = howEat(test, x, y, x - 2 * dir, y + 2);
                    } else {
                        val1 = 0;
                    }
                    if (y - 2 >= 0) {
                        val2 = howEat(test, x, y, x - 2 * dir, y - 2);
                    } else {
                        val2 = 0;
                    }
                } else {
                    return 1;
                }
                return Math.max(val1, val2);
            }
            return 1;
        }
        return 0;
    }

    private int howLost(Damiera board1) {
        int perse = 0;
        int colAvv = (board1.getColor() == 1) ? 2 : 1;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if ((board1.getCell(i, j) % 2 == colAvv % 2) && (board1.getCell(i, j) != 0)) {
                    int tmp = lost(board1, i, j);
                    if (tmp > perse) {
                        perse = tmp;
                    }
                }
            }
        }
        return perse;
    }

    private int lost(Damiera board1, int x, int y) {
        if (board1.getCell(x, y) > 2) {
            int perdita1 = 0;
            if (x + 2 < 8 && y + 2 < 8) {
                perdita1 = howCanEat(board1, x, y, x + 2, y + 2);
            }
            int perdita2 = 0;
            if (x + 2 < 8 && y - 2 >= 0) {
                perdita2 = howCanEat(board1, x, y, x + 2, y - 2);
            }
            int perdita3 = 0;
            if (y - 2 >= 0 && y + 2 < 8) {
                perdita3 = howCanEat(board1, x, y, x - 2, y + 2);
            }
            int perdita4 = 0;
            if (y - 2 >= 0 && y - 2 >= 0) {
                perdita4 = howCanEat(board1, x, y, x - 2, y - 2);
            }
            return (Math.max(perdita1, perdita2) > Math.max(perdita3, perdita4))
                    ? Math.max(perdita1, perdita2) : Math.max(perdita3, perdita4);
        }
        int perdita1 = 0;
        int perdita2 = 0;
        int dir = board1.giocatore(board1.getCell(x, y));
        if ((x - 2 * dir >= 0 && dir > 0) || (x - 2 * dir < 8 && dir < 0)) {
            if (y + 2 < 8) {
                perdita1 = howCanEat(board1, x, y, x - 2 * dir, y + 2);
            }
            if (y - 2 >= 0) {
                perdita2 = howCanEat(board1, x, y, x - 2 * dir, y - 2);
            }
        }
        return Math.max(perdita1, perdita2);
    }
}