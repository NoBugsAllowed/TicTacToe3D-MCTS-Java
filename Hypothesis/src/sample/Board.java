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

    public int GetZTop(int x, int y) {
        if (board[x][y][0] == 0) return -1;
        for (int i = 1; i < size; i++) {
            if (board[x][y][i - 1] != 0 && board[x][y][i] == 0) return i - 1;
        }
        return size - 1;
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

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                for (int k = 0; k < size; k++) {
                    if (board[i][j][k] == 0) {
                        return 0;
                    }
                }
            }
        }

        //remis
        return -1;
    }

    public int FastWinner(int player, int x, int y, int z) {
        //proste linie
        boolean isline = false;
        for (int i = 0; i < size; i++) {
            if (board[x][y][i] != player) isline = false;
        }
        if (isline) return player;

        isline = false;
        for (int i = 0; i < size; i++) {
            if (board[x][i][z] != player) isline = false;
        }
        if (isline) return player;

        isline = false;
        for (int i = 0; i < size; i++) {
            if (board[i][y][z] != player) isline = false;
        }
        if (isline) return player;

        // przekątne w ramach płaszczyzn
        if (x == y) {
            isline = false;
            for (int i = 0; i < size; i++) {
                if (board[i][i][z] != player) isline = false;
            }
            if (isline) return player;
        }
        if (x + y == size - 1) {
            isline = false;
            for (int i = 0; i < size; i++) {
                if (board[i][size - 1 - i][z] != player) isline = false;
            }
            if (isline) return player;
        }

        if (x == z) {
            isline = false;
            for (int i = 0; i < size; i++) {
                if (board[i][y][i] != player) isline = false;
            }
            if (isline) return player;
        }
        if (x + z == size - 1) {
            isline = false;
            for (int i = 0; i < size; i++) {
                if (board[i][y][size - 1 - i] != player) isline = false;
            }
            if (isline) return player;
        }

        if (z == y) {
            isline = false;
            for (int i = 0; i < size; i++) {
                if (board[x][i][i] != player) isline = false;
            }
            if (isline) return player;
        }
        if (z + y == size - 1) {
            isline = false;
            for (int i = 0; i < size; i++) {
                if (board[x][i][size - 1 - i] != player) isline = false;
            }
            if (isline) return player;
        }

        // główne przekątne sześcianu
        if (x == y && y == z) {
            isline = false;
            for (int i = 0; i < size; i++) {
                if (board[i][i][i] != player) isline = false;
            }
            if (isline) return player;
        }

        if (x == z && y + z == size - 1) {
            isline = false;
            for (int i = 0; i < size; i++) {
                if (board[i][size - 1 - i][i] != player) isline = false;
            }
            if (isline) return player;
        }

        if (x == y && y + z == size - 1) {
            isline = false;
            for (int i = 0; i < size; i++) {
                if (board[i][i][size - 1 - i] != player) isline = false;
            }
            if (isline) return player;
        }

        if (y == z && y + x == size - 1) {
            isline = false;
            for (int i = 0; i < size; i++) {
                if (board[size - 1 - i][i][i] != player) isline = false;
            }
            if (isline) return player;
        }
        return 0;
    }

    public double GetHeuristicFastReward(int player, int x, int y, int z) {
        double sumReward = 0;
        boolean isline = true;
        int firstLinePlayer = 0;
        int lineSum = 0;
        for (int i = 0; i < size; i++) {
            if (firstLinePlayer == 0 && board[x][y][i] != 0) {
                firstLinePlayer = board[x][y][i];
            }
            if (board[x][y][i] == 3 - firstLinePlayer) {
                isline = false;
                break;
            }
            if (board[x][y][i] == firstLinePlayer) lineSum += 1;
        }
        if (firstLinePlayer != 0 && isline) {
            if (firstLinePlayer == player) sumReward += Math.pow(10, lineSum);
            else sumReward -= Math.pow(10, lineSum);
        }

        isline = true;
        firstLinePlayer = 0;
        lineSum = 0;
        for (int i = 0; i < size; i++) {
            if (firstLinePlayer == 0 && board[x][i][z] != 0) {
                firstLinePlayer = board[x][i][z];
            }
            if (board[x][i][z] == 3 - firstLinePlayer) {
                isline = false;
                break;
            }
            if (board[x][i][z] == firstLinePlayer) lineSum += 1;
        }
        if (firstLinePlayer != 0 && isline) {
            if (firstLinePlayer == player) sumReward += Math.pow(10, lineSum);
            else sumReward -= Math.pow(10, lineSum);
        }

        isline = true;
        firstLinePlayer = 0;
        lineSum = 0;
        for (int i = 0; i < size; i++) {
            if (firstLinePlayer == 0 && board[i][y][z] != 0) {
                firstLinePlayer = board[i][y][z];
            }
            if (board[i][y][z] == 3 - firstLinePlayer) {
                isline = false;
                break;
            }
            if (board[i][y][z] == firstLinePlayer) lineSum += 1;
        }
        if (firstLinePlayer != 0 && isline) {
            if (firstLinePlayer == player) sumReward += Math.pow(10, lineSum);
            else sumReward -= Math.pow(10, lineSum);
        }

        // przekątne w ramach płaszczyzn
        if (x == y) {
            isline = true;
            firstLinePlayer = 0;
            lineSum = 0;
            for (int i = 0; i < size; i++) {
                if (firstLinePlayer == 0 && board[i][i][z] != 0) {
                    firstLinePlayer = board[i][i][z];
                }
                if (board[i][i][z] == 3 - firstLinePlayer) {
                    isline = false;
                    break;
                }
                if (board[i][i][z] == firstLinePlayer) lineSum += 1;
            }
            if (firstLinePlayer != 0 && isline) {
                if (firstLinePlayer == player) sumReward += Math.pow(10, lineSum);
                else sumReward -= Math.pow(10, lineSum);
            }
        }
        if (x + y == size - 1) {
            isline = true;
            firstLinePlayer = 0;
            lineSum = 0;
            for (int i = 0; i < size; i++) {
                if (firstLinePlayer == 0 && board[i][size - 1 - i][z] != 0) {
                    firstLinePlayer = board[i][size - 1 - i][z];
                }
                if (board[i][size - 1 - i][z] == 3 - firstLinePlayer) {
                    isline = false;
                    break;
                }
                if (board[i][size - 1 - i][z] == firstLinePlayer) lineSum += 1;
            }
            if (firstLinePlayer != 0 && isline) {
                if (firstLinePlayer == player) sumReward += Math.pow(10, lineSum);
                else sumReward -= Math.pow(10, lineSum);
            }
        }

        if (x == z) {
            isline = true;
            firstLinePlayer = 0;
            lineSum = 0;
            for (int i = 0; i < size; i++) {
                if (firstLinePlayer == 0 && board[i][y][i] != 0) {
                    firstLinePlayer = board[i][y][i];
                }
                if (board[i][y][i] == 3 - firstLinePlayer) {
                    isline = false;
                    break;
                }
                if (board[i][y][i] == firstLinePlayer) lineSum += 1;
            }
            if (firstLinePlayer != 0 && isline) {
                if (firstLinePlayer == player) sumReward += Math.pow(10, lineSum);
                else sumReward -= Math.pow(10, lineSum);
            }
        }
        if (x + z == size - 1) {
            isline = true;
            firstLinePlayer = 0;
            lineSum = 0;
            for (int i = 0; i < size; i++) {
                if (firstLinePlayer == 0 && board[i][y][size - 1 - i] != 0) {
                    firstLinePlayer = board[i][y][size - 1 - i];
                }
                if (board[i][y][size - 1 - i] == 3 - firstLinePlayer) {
                    isline = false;
                    break;
                }
                if (board[i][y][size - 1 - i] == firstLinePlayer) lineSum += 1;
            }
            if (firstLinePlayer != 0 && isline) {
                if (firstLinePlayer == player) sumReward += Math.pow(10, lineSum);
                else sumReward -= Math.pow(10, lineSum);
            }
        }

        if (z == y) {
            isline = true;
            firstLinePlayer = 0;
            lineSum = 0;
            for (int i = 0; i < size; i++) {
                if (firstLinePlayer == 0 && board[x][i][i] != 0) {
                    firstLinePlayer = board[x][i][i];
                }
                if (board[x][i][i] == 3 - firstLinePlayer) {
                    isline = false;
                    break;
                }
                if (board[x][i][i] == firstLinePlayer) lineSum += 1;
            }
            if (firstLinePlayer != 0 && isline) {
                if (firstLinePlayer == player) sumReward += Math.pow(10, lineSum);
                else sumReward -= Math.pow(10, lineSum);
            }
        }
        if (z + y == size - 1) {
            isline = true;
            firstLinePlayer = 0;
            lineSum = 0;
            for (int i = 0; i < size; i++) {
                if (firstLinePlayer == 0 && board[x][i][size - 1 - i] != 0) {
                    firstLinePlayer = board[x][i][size - 1 - i];
                }
                if (board[x][i][size - 1 - i] == 3 - firstLinePlayer) {
                    isline = false;
                    break;
                }
                if (board[x][i][size - 1 - i] == firstLinePlayer) lineSum += 1;
            }
            if (firstLinePlayer != 0 && isline) {
                if (firstLinePlayer == player) sumReward += Math.pow(10, lineSum);
                else sumReward -= Math.pow(10, lineSum);
            }
        }

        // główne przekątne sześcianu
        if (x == y && y == z) {
            isline = true;
            firstLinePlayer = 0;
            lineSum = 0;
            for (int i = 0; i < size; i++) {
                if (firstLinePlayer == 0 && board[i][i][i] != 0) {
                    firstLinePlayer = board[i][i][i];
                }
                if (board[i][i][i] == 3 - firstLinePlayer) {
                    isline = false;
                    break;
                }
                if (board[i][i][i] == firstLinePlayer) lineSum += 1;
            }
            if (firstLinePlayer != 0 && isline) {
                if (firstLinePlayer == player) sumReward += Math.pow(10, lineSum);
                else sumReward -= Math.pow(10, lineSum);
            }
        }

        if (x == z && y + z == size - 1) {
            isline = true;
            firstLinePlayer = 0;
            lineSum = 0;
            for (int i = 0; i < size; i++) {
                if (firstLinePlayer == 0 && board[i][size - 1 - i][i] != 0) {
                    firstLinePlayer = board[i][size - 1 - i][i];
                }
                if (board[i][size - 1 - i][i] == 3 - firstLinePlayer) {
                    isline = false;
                    break;
                }
                if (board[i][size - 1 - i][i] == firstLinePlayer) lineSum += 1;
            }
            if (firstLinePlayer != 0 && isline) {
                if (firstLinePlayer == player) sumReward += Math.pow(10, lineSum);
                else sumReward -= Math.pow(10, lineSum);
            }
        }

        if (x == y && y + z == size - 1) {
            isline = true;
            firstLinePlayer = 0;
            lineSum = 0;
            for (int i = 0; i < size; i++) {
                if (firstLinePlayer == 0 && board[i][i][size - 1 - i] != 0) {
                    firstLinePlayer = board[i][i][size - 1 - i];
                }
                if (board[i][i][size - 1 - i] == 3 - firstLinePlayer) {
                    isline = false;
                    break;
                }
                if (board[i][i][size - 1 - i] == firstLinePlayer) lineSum += 1;
            }
            if (firstLinePlayer != 0 && isline) {
                if (firstLinePlayer == player) sumReward += Math.pow(10, lineSum);
                else sumReward -= Math.pow(10, lineSum);
            }
        }

        if (y == z && y + x == size - 1) {
            isline = true;
            firstLinePlayer = 0;
            lineSum = 0;
            for (int i = 0; i < size; i++) {
                if (firstLinePlayer == 0 && board[size - 1 - i][i][i] != 0) {
                    firstLinePlayer = board[size - 1 - i][i][i];
                }
                if (board[size - 1 - i][i][i] == 3 - firstLinePlayer) {
                    isline = false;
                    break;
                }
                if (board[size - 1 - i][i][i] == firstLinePlayer) lineSum += 1;
            }
            if (firstLinePlayer != 0 && isline) {
                if (firstLinePlayer == player) sumReward += Math.pow(10, lineSum);
                else sumReward -= Math.pow(10, lineSum);
            }
        }


        return sumReward;
    }

    public boolean IsDraw() {
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                if (board[i][j][size - 1] == 0)
                    return false;
        return true;
    }

    public double GetHeuristicReward(int player) {
        double sumReward = 0;

        //zwykłe linie
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++) {
                int PlayerOnFirstTile = board[i][j][0];
                if (PlayerOnFirstTile != 0) {
                    boolean isLine = true;
                    int lineLength = 1;
                    for (int k = 1; k < size; k++) {
                        if (board[i][j][k] == 3 - PlayerOnFirstTile) {
                            isLine = false;
                            break;
                        }
                        lineLength += 1;
                    }
                    if (isLine) {
                        if (PlayerOnFirstTile == player)
                            sumReward += Math.pow(10, lineLength);
                        else
                            sumReward -= Math.pow(10, lineLength);
                    }
                }
            }

        for (int i = 0; i < size; i++)
            for (int k = 0; k < size; k++) {
                int PlayerOnFirstTile = board[i][0][k];
                if (PlayerOnFirstTile != 0) {
                    boolean isLine = true;
                    int lineLength = 1;
                    for (int j = 1; j < size; j++) {
                        if (board[i][j][k] == 3 - PlayerOnFirstTile) {
                            isLine = false;
                            break;
                        }
                        lineLength += 1;
                    }
                    if (isLine) {
                        if (PlayerOnFirstTile == player)
                            sumReward += Math.pow(10, lineLength);
                        else
                            sumReward -= Math.pow(10, lineLength);
                    }
                }
            }

        for (int k = 0; k < size; k++)
            for (int j = 0; j < size; j++) {
                int PlayerOnFirstTile = board[0][j][k];
                if (PlayerOnFirstTile != 0) {
                    boolean isLine = true;
                    int lineLength = 1;
                    for (int i = 1; i < size; i++) {
                        if (board[i][j][k] == 3 - PlayerOnFirstTile) {
                            isLine = false;
                            break;
                        }
                        lineLength += 1;
                    }
                    if (isLine) {
                        if (PlayerOnFirstTile == player)
                            sumReward += Math.pow(10, lineLength);
                        else
                            sumReward -= Math.pow(10, lineLength);
                    }
                }
            }

        //zwykłe przekątne
        for (int k = 0; k < size; k++) {
            int PlayerOnFirstTile = board[0][0][k];
            if (PlayerOnFirstTile != 0) {
                boolean isLine = true;
                int lineLength = 1;
                for (int i = 1; i < size; i++) {
                    if (board[i][i][k] == 3 - PlayerOnFirstTile) {
                        isLine = false;
                        break;
                    }
                    lineLength += 1;
                }
                if (isLine) {
                    if (PlayerOnFirstTile == player)
                        sumReward += Math.pow(10, lineLength);
                    else
                        sumReward -= Math.pow(10, lineLength);
                }
            }

            PlayerOnFirstTile = board[size - 1][0][k];
            if (PlayerOnFirstTile != 0) {
                boolean isLine = true;
                int lineLength = 1;
                for (int i = 1; i < size; i++) {
                    if (board[size - 1 - i][i][k] == 3 - PlayerOnFirstTile) {
                        isLine = false;
                        break;
                    }
                    lineLength += 1;
                }
                if (isLine) {
                    if (PlayerOnFirstTile == player)
                        sumReward += Math.pow(10, lineLength);
                    else
                        sumReward -= Math.pow(10, lineLength);
                }
            }
        }

        for (int j = 0; j < size; j++) {
            int PlayerOnFirstTile = board[0][j][0];
            if (PlayerOnFirstTile != 0) {
                boolean isLine = true;
                int lineLength = 1;
                for (int i = 1; i < size; i++) {
                    if (board[i][j][i] == 3 - PlayerOnFirstTile) {
                        isLine = false;
                        break;
                    }
                    lineLength += 1;
                }
                if (isLine) {
                    if (PlayerOnFirstTile == player)
                        sumReward += Math.pow(10, lineLength);
                    else
                        sumReward -= Math.pow(10, lineLength);
                }
            }

            PlayerOnFirstTile = board[size - 1][j][0];
            if (PlayerOnFirstTile != 0) {
                boolean isLine = true;
                int lineLength = 1;
                for (int i = 1; i < size; i++) {
                    if (board[size - 1 - i][j][i] == 3 - PlayerOnFirstTile) {
                        isLine = false;
                        break;
                    }
                    lineLength += 1;
                }
                if (isLine) {
                    if (PlayerOnFirstTile == player)
                        sumReward += Math.pow(10, lineLength);
                    else
                        sumReward -= Math.pow(10, lineLength);
                }
            }
        }

        for (int k = 0; k < size; k++) {
            int PlayerOnFirstTile = board[k][0][0];
            if (PlayerOnFirstTile != 0) {
                boolean isLine = true;
                int lineLength = 1;
                for (int i = 1; i < size; i++) {
                    if (board[k][i][i] == 3 - PlayerOnFirstTile) {
                        isLine = false;
                        break;
                    }
                    lineLength += 1;
                }
                if (isLine) {
                    if (PlayerOnFirstTile == player)
                        sumReward += Math.pow(10, lineLength);
                    else
                        sumReward -= Math.pow(10, lineLength);
                }
            }

            PlayerOnFirstTile = board[k][0][size - 1];
            if (PlayerOnFirstTile != 0) {
                boolean isLine = true;
                int lineLength = 1;
                for (int i = 1; i < size; i++) {
                    if (board[k][i][size - 1 - i] == 3 - PlayerOnFirstTile) {
                        isLine = false;
                        break;
                    }
                    lineLength += 1;
                }
                if (isLine) {
                    if (PlayerOnFirstTile == player)
                        sumReward += Math.pow(10, lineLength);
                    else
                        sumReward -= Math.pow(10, lineLength);
                }
            }
        }

        //duże przekątne
        int PlayerOnFirstTile = board[0][0][0];
        if (PlayerOnFirstTile != 0) {
            boolean isLine = true;
            int lineLength = 1;
            for (int i = 1; i < size; i++) {
                if (board[i][i][i] == 3 - PlayerOnFirstTile) {
                    isLine = false;
                    break;
                }
                lineLength += 1;
            }
            if (isLine) {
                if (PlayerOnFirstTile == player)
                    sumReward += Math.pow(10, lineLength);
                else
                    sumReward -= Math.pow(10, lineLength);
            }
        }

        PlayerOnFirstTile = board[size - 1][0][0];
        if (PlayerOnFirstTile != 0) {
            boolean isLine = true;
            int lineLength = 1;
            for (int i = 1; i < size; i++) {
                if (board[size - 1 - i][i][i] == 3 - PlayerOnFirstTile) {
                    isLine = false;
                    break;
                }
                lineLength += 1;
            }
            if (isLine) {
                if (PlayerOnFirstTile == player)
                    sumReward += Math.pow(10, lineLength);
                else
                    sumReward -= Math.pow(10, lineLength);
            }
        }

        PlayerOnFirstTile = board[0][size - 1][0];
        if (PlayerOnFirstTile != 0) {
            boolean isLine = true;
            int lineLength = 1;
            for (int i = 1; i < size; i++) {
                if (board[i][size - 1 - i][i] == 3 - PlayerOnFirstTile) {
                    isLine = false;
                    break;
                }
                lineLength += 1;
            }
            if (isLine) {
                if (PlayerOnFirstTile == player)
                    sumReward += Math.pow(10, lineLength);
                else
                    sumReward -= Math.pow(10, lineLength);
            }
        }

        PlayerOnFirstTile = board[0][0][size - 1];
        if (PlayerOnFirstTile != 0) {
            boolean isLine = true;
            int lineLength = 1;
            for (int i = 1; i < size; i++) {
                if (board[i][i][size - 1 - i] == 3 - PlayerOnFirstTile) {
                    isLine = false;
                    break;
                }
                lineLength += 1;
            }
            if (isLine) {
                if (PlayerOnFirstTile == player)
                    sumReward += Math.pow(10, lineLength);
                else
                    sumReward -= Math.pow(10, lineLength);
            }
        }
        return sumReward;
    }

    public void Print() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                for (int k = 0; k < size; k++) {
                    System.out.print(board[k][j][i] + " ");
                }
                System.out.println();
            }
            System.out.println();
        }
    }
}
