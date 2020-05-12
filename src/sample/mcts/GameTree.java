package sample.mcts;

import sample.Board;
import sample.Position;

import java.util.ArrayList;
import java.util.List;

public class GameTree {
    private int playerToMove;
    private List<GameTree> children;
    private GameTree Parent;
    private Position lastMove;
    private Board board;
    private int reward;
    private int simulations;
    private boolean realNode;

    public GameTree(int playerToMove, Position lastMove, Board board, GameTree parent, boolean realNode) {
        this.playerToMove = playerToMove;
        this.lastMove = lastMove;
        this.board = board;
        this.children = new ArrayList<>();
        this.Parent = parent;
        this.realNode = realNode;
        reward = 0;
        simulations = 0;
    }

    public Position getLastMove() {
        return lastMove;
    }

    public List<GameTree> getChildren() {
        return children;
    }

    public int getReward() {
        return reward;
    }

    public int getSimulations() {
        return simulations;
    }

    public GameTree getParent() {
        return Parent;
    }

    public boolean isRealNode() {
        return realNode;
    }

    public void setRealNode(boolean realNode) {
        this.realNode = realNode;
    }

    public void setReward(int reward) {
        this.reward = reward;
    }

    public void setSimulations(int simulations) {
        this.simulations = simulations;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public int getPlayerToMove() {
        return playerToMove;
    }

    // funkcja zwraca listę złożoną z aktualnych dzieci węzła połączonymi z węzłami, które mogą istnieć ale nie należą aktualnie do drzewa
    public List<GameTree> MakeChildren() {
        List<GameTree> list = new ArrayList<>();
        if (!IsLeaf()) {
            for (int i = 0; i < board.getSize(); i++)
                for (int j = 0; j < board.getSize(); j++) {
                    if (board.TestElement(i, j)) {
                        GameTree node = GetChildrenByPosition(new Position(i, j));
                        if (node != null)
                            list.add(node);
                        else
                            list.add(new GameTree(3 - playerToMove, new Position(i, j), board, this, false)); // gracze mają id 1 lub 2 stąd 3-id
                    }
                }
        }
        return list;
    }

    public void SetChildren() {
        this.children = MakeChildren();
    }

    public GameTree AddChild(Position pos) {
        if ((children.size() < board.getSize() * board.getSize()) && (children.size() > 0
                || !IsLeaf())) {
            GameTree tree = children.stream()
                    .filter(t -> pos.getX() == t.getLastMove().getX() && pos.getY() == t.getLastMove().getY())
                    .findAny()
                    .orElse(null);
            if (tree != null) return null;
            GameTree n = new GameTree(3 - playerToMove, pos, board, this, true);
            children.add(n);
            return n;
        }
        return null;
    }

    private GameTree GetChildrenByPosition(Position pos) {
        return children.stream()
                .filter(t -> pos.getX() == t.getLastMove().getX() && pos.getY() == t.getLastMove().getY())
                .findAny()
                .orElse(null);
    }

    public boolean IsLeaf() {
        int winner = board.Winner();
        return winner != 0 || board.IsDraw();
    }

    public int Winner() {return board.Winner();}

    @Override
    public String toString() {
        return "GameTree{" +
                "playerToMove=" + playerToMove +
                ", children=" + children +
                ", lastMove=" + lastMove +
                ", board=" + board +
                '}';
    }

    public void Write() {
        System.out.println(toString());
    }
}
