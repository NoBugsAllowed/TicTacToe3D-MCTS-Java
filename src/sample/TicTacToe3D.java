package sample;

import java.util.ArrayList;

public class TicTacToe3D {
    public interface OnMoveMadeListener {
        public void moveMade(int player, int x, int y, int z);
        public void gameFinished(int player,String name);
    }

    public class GameStartedException extends Exception {
        public GameStartedException() {
            super();
        }
    }

    public enum GameState {
        WAITING, STARTED, FINISHED
    }

    private static int EMPTY_FIELD = 0; // puste pole
    private GameState gameState;
    private int size; // rozmiar planszy (size x size x size)
    private int[][][] board; // trojwymiarowa plansza, liczby na poszczegolnych polach oznaczaja czyjego gracza element tam sie znajduje, ostatni wymiar to caly slupek
    private String [] players;
    private int winner;
    private ArrayList<OnMoveMadeListener> moveListeners;

    public TicTacToe3D(int size) {
        this.winner = 0;
        this.size = size;
        this.board = new int[size][size][size];
        this.gameState = GameState.WAITING;
        this.players = new String[2];
        for(int x=0;x<size;x++) {
            for(int y=0;y<size;y++) {
                for(int z=0;z<size;z++) {
                    board[x][y][z]=EMPTY_FIELD;
                }
            }
        }
        this.moveListeners = new ArrayList<OnMoveMadeListener>();
    }

    public static int getEmptyField() {
        return EMPTY_FIELD;
    }

    public GameState getGameState() {
        return gameState;
    }

    public int getSize() {
        return size;
    }

    public int[][][] getBoard() {
        return board;
    }

    public int getWinner() {
        return winner;
    }
    // Do ustawiania kulki w GUI
    public void attachOnMoveListener(OnMoveMadeListener listener) {
        moveListeners.add(listener);
    }

    // Rejestruje gracza o podanej nazwie i zwraca jego numer, który ma być wykorzystywany potem do wykonywania ruchu
    public int registerPlayer(String name) throws GameStartedException {
        if(gameState==GameState.WAITING && name!=null && !name.isEmpty()) {
            if(players[0]==null) {
                players[0]=name;
                return 1;
            }
            else {
                players[1]=name;
                gameState=GameState.STARTED;
                return 2;
            }
        }
        throw new GameStartedException();
    }

    public int getStickElementsCount(int x, int y) {
        for(int k=0;k<size;k++) {
            if(board[x][y][k]==EMPTY_FIELD) {
                return k;
            }
        }
        return size;
    }

    public boolean makeMove(int player, int x, int y) {
        if(gameState==GameState.STARTED) {
            int z = 0;
            // wspolrzedne wykraczaja poza wymiary planszy
            if(x<0||x>size-1 ||y<0||y>size-1) return false;
            // pełen słupek
            if(board[x][y][size-1]!=EMPTY_FIELD) return false;
            for(int k=0;k<size;k++) {
                if(board[x][y][k]==EMPTY_FIELD) {
                    z = k;
                    break;
                }
            }
            board[x][y][z] = player;
            if(isGameOver(x,y,z)) {
                winner = player;
                gameState = GameState.FINISHED;
                for (OnMoveMadeListener listener : moveListeners) {
                    listener.gameFinished(player,players[player-1]);
                }
            }
            // notify listeners
            for (OnMoveMadeListener listener : moveListeners) {
                listener.moveMade(player,x,y,z);
            }
            return true;
        }
        return false;
    }

    // sprawdza czy gra jest zakonczona, wiedzac ze zostal wlasnie postawiony znak na wspolrzednych x,y,z, wartosc w board[x][y][z] musi byc juz zmieniona
    public boolean isGameOver(int x, int y, int z){
        Coords position = new Coords(x,y,z);
        if(!position.isCorrect) return false;
        for(int i=-1; i<2; i++)
            for(int j=-1; j<2; j++)
                for(int k=-1; k<2; k++){
                    if(i==0 && j==0 && k==0) continue;
                    Coords c2 = new Coords(position.getX()+i,position.getY()+j,position.getZ()+k);
                    Coords c3 = new Coords(position.getX()+2*i,position.getY()+2*j,position.getZ()+2*k);
                    Coords c4 = new Coords(position.getX()+3*i,position.getY()+3*j,position.getZ()+3*k);
                    if(c2.isCorrect && c3.isCorrect && c4.isCorrect){
                        if(board[position.getX()][position.getY()][position.getZ()]==
                                board[c2.getX()][c2.getY()][c2.getZ()]&&board[c2.getX()][c2.getY()][c2.getZ()]
                                ==board[c3.getX()][c3.getY()][c3.getZ()]&&board[c4.getX()][c4.getY()][c4.getZ()]
                                ==board[c3.getX()][c3.getY()][c3.getZ()]) return true;}
                    c4 = new Coords(position.getX()-i,position.getY()-j,position.getZ()-k);
                    if(c2.isCorrect && c3.isCorrect && c4.isCorrect){
                        if(board[position.getX()][position.getY()][position.getZ()]==
                                board[c2.getX()][c2.getY()][c2.getZ()]&&board[c2.getX()][c2.getY()][c2.getZ()]
                                ==board[c3.getX()][c3.getY()][c3.getZ()]&&board[c4.getX()][c4.getY()][c4.getZ()]
                                ==board[c3.getX()][c3.getY()][c3.getZ()]) return true;}
                }
        return false;
    }
}
