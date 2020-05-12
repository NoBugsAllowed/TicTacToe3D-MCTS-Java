package sample;

public class Board {
    private int[][][] board;
    private int size;

    public int[][][] getBoard() {
        return board;
    }

    public void setBoard(int[][][] board) {
        this.board = board;
    }

    public int getSize() {
        return size;
    }

    public Board(int size) {
        this.size = size;
        board = new int[size][size][size];
    }

    public boolean PutElement(int x, int y, int element) {
        for (int i = 0; i < size; i++) {
            if (board[x][y][i] == 0) {
                board[x][y][i] = element;
                return true;
            }
        }
        return false;
    }

    public boolean TestElement(int x, int y) {
        for (int i = 0; i < size; i++) {
            if (board[x][y][i] == 0)
                return true;
        }
        return false;
    }

    public int TakeElement(int x, int y) {
        for (int k = size - 1; k >= 0; k--) {
            if (board[x][y][k] != 0) {
                int tmp = board[x][y][k];
                board[x][y][k] = 0;
                return tmp;
            }
        }
        return 0;
    }

    public Board Clone() {
        Board clone = new Board(size);
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                for (int k = 0; k < size; k++)
                    clone.board[i][j][k] = board[i][j][k];
        return clone;
    }

    //zwraca id gracza zwycięskiego jeśli taki już jest wpp zwraca 0;
    public int Winner() {

        //zwykłe linie
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++) {
                int PlayerOnFirstTile = board[i][j][0];
                if (PlayerOnFirstTile != 0) {
                    boolean isLine = true;
                    for (int k = 1; k < size; k++) {
                        if (board[i][j][k] != PlayerOnFirstTile) {
                            isLine = false;
                            break;
                        }
                    }
                    if (isLine) return PlayerOnFirstTile;
                }
            }

        for (int i = 0; i < size; i++)
            for (int k = 0; k < size; k++) {
                int PlayerOnFirstTile = board[i][0][k];
                if (PlayerOnFirstTile != 0) {
                    boolean isLine = true;
                    for (int j = 1; j < size; j++) {
                        if (board[i][j][k] != PlayerOnFirstTile) {
                            isLine = false;
                            break;
                        }
                    }
                    if (isLine) return PlayerOnFirstTile;
                }
            }

        for (int k = 0; k < size; k++)
            for (int j = 0; j < size; j++) {
                int PlayerOnFirstTile = board[0][j][k];
                if (PlayerOnFirstTile != 0) {
                    boolean isLine = true;
                    for (int i = 1; i < size; i++) {
                        if (board[i][j][k] != PlayerOnFirstTile) {
                            isLine = false;
                            break;
                        }
                    }
                    if (isLine) return PlayerOnFirstTile;
                }
            }

        //zwykłe przekątne
        for (int k = 0; k < size; k++) {
            int PlayerOnFirstTile = board[0][0][k];
            if (PlayerOnFirstTile != 0) {
                boolean isLine = true;
                for (int i = 1; i < size; i++) {
                    if (board[i][i][k] != PlayerOnFirstTile) {
                        isLine = false;
                        break;
                    }
                }
                if (isLine) return PlayerOnFirstTile;
            }

            PlayerOnFirstTile = board[size - 1][0][k];
            if (PlayerOnFirstTile != 0) {
                boolean isLine = true;
                for (int i = 1; i < size; i++) {
                    if (board[size - 1 - i][i][k] != PlayerOnFirstTile) {
                        isLine = false;
                        break;
                    }
                }
                if (isLine) return PlayerOnFirstTile;
            }
        }

        for (int j = 0; j < size; j++) {
            int PlayerOnFirstTile = board[0][j][0];
            if (PlayerOnFirstTile != 0) {
                boolean isLine = true;
                for (int i = 1; i < size; i++) {
                    if (board[i][j][i] != PlayerOnFirstTile) {
                        isLine = false;
                        break;
                    }
                }
                if (isLine) return PlayerOnFirstTile;
            }

            PlayerOnFirstTile = board[size - 1][j][0];
            if (PlayerOnFirstTile != 0) {
                boolean isLine = true;
                for (int i = 1; i < size; i++) {
                    if (board[size - 1 - i][j][i] != PlayerOnFirstTile) {
                        isLine = false;
                        break;
                    }
                }
                if (isLine) return PlayerOnFirstTile;
            }
        }

        for (int k = 0; k < size; k++) {
            int PlayerOnFirstTile = board[k][0][0];
            if (PlayerOnFirstTile != 0) {
                boolean isLine = true;
                for (int i = 1; i < size; i++) {
                    if (board[k][i][i] != PlayerOnFirstTile) {
                        isLine = false;
                        break;
                    }
                }
                if (isLine) return PlayerOnFirstTile;
            }

            PlayerOnFirstTile = board[k][0][size - 1];
            if (PlayerOnFirstTile != 0) {
                boolean isLine = true;
                for (int i = 1; i < size; i++) {
                    if (board[k][i][size - 1 - i] != PlayerOnFirstTile) {
                        isLine = false;
                        break;
                    }
                }
                if (isLine) return PlayerOnFirstTile;
            }
        }

        //duże przekątne
        int PlayerOnFirstTile = board[0][0][0];
        if (PlayerOnFirstTile != 0) {
            boolean isLine = true;
            for (int i = 1; i < size; i++) {
                if (board[i][i][i] != PlayerOnFirstTile) {
                    isLine = false;
                    break;
                }
            }
            if (isLine) return PlayerOnFirstTile;
        }

        PlayerOnFirstTile = board[size - 1][0][0];
        if (PlayerOnFirstTile != 0) {
            boolean isLine = true;
            for (int i = 1; i < size; i++) {
                if (board[size - 1 - i][i][i] != PlayerOnFirstTile) {
                    isLine = false;
                    break;
                }
            }
            if (isLine) return PlayerOnFirstTile;
        }

        PlayerOnFirstTile = board[0][size - 1][0];
        if (PlayerOnFirstTile != 0) {
            boolean isLine = true;
            for (int i = 1; i < size; i++) {
                if (board[i][size - 1 - i][i] != PlayerOnFirstTile) {
                    isLine = false;
                    break;
                }
            }
            if (isLine) return PlayerOnFirstTile;
        }

        PlayerOnFirstTile = board[0][0][size - 1];
        if (PlayerOnFirstTile != 0) {
            boolean isLine = true;
            for (int i = 1; i < size; i++) {
                if (board[i][i][size - 1 - i] != PlayerOnFirstTile) {
                    isLine = false;
                    break;
                }
            }
            if (isLine) return PlayerOnFirstTile;
        }

        return 0;
    }

    public boolean IsDraw() {
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                if (board[i][j][size - 1] == 0)
                    return false;
        return true;
    }
}
