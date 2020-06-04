package sample.mcts;

import sample.ArtificialPlayer;
import sample.Board;
import sample.Position;

import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class MctsUctPlayer implements ArtificialPlayer {

    private int id;
    private GameTree root;
    private ChoseNodePolicy insidePolicy;
    private ChoseNodePolicy outsidePolicy;
    private Board currentBoard;
    private ReentrantLock lock;
    private int keepConstructTree = 0; // 0 - nic się nie dzieje, 1 - wykonywany jest PrepareMove, 2 - kończony jest PrepareMove, 3 - Wykonywany jest MakeMove

    public GameTree ConstructTree(Board board, int depth) {
        currentBoard = board.Clone();
        GameTree node = new GameTree(id, null, board, null, true);
        ConstructTreeRec(board, depth, node);
        return node;
    }

    private void ConstructTreeRec(Board board, int depth, GameTree node) {
        if (!node.IsLeaf() && depth > 0) {
            node.SetChildren();
            List<GameTree> child = node.getChildren();
            for (int i = 0; i < child.size(); i++) {
                board.PutElement(child.get(i).getLastMove().getX(), child.get(i).getLastMove().getY(), child.get(i).getPlayerToMove());
                ConstructTreeRec(board, depth - 1, child.get(i));
                board.TakeElement(child.get(i).getLastMove().getX(), child.get(i).getLastMove().getY());
            }
        }
    }

    public MctsUctPlayer(int id, ChoseNodePolicy insidePolicy, ChoseNodePolicy outsidePolicy) {
        this.id = id;
        this.insidePolicy = insidePolicy;
        this.outsidePolicy = outsidePolicy;
        this.lock = new ReentrantLock();
    }

    public GameTree getRoot() {
        return root;
    }

    private GameTree Selection(GameTree node) {
        if (node.IsLeaf())
            return node;
        List<GameTree> nodes = node.MakeChildren();
        int index = insidePolicy.BestNode(node);
        GameTree n = nodes.get(index);
        node.getBoard().PutElement(n.getLastMove().getX(), n.getLastMove().getY(), node.getPlayerToMove());
        if (!n.isRealNode()) {
            // brzydkie pominięcie błędu
            node.getBoard().TakeElement(n.getLastMove().getX(), n.getLastMove().getY());
            n = node.AddChild(new Position(n.getLastMove().getX(), n.getLastMove().getY()));
            node.getBoard().PutElement(n.getLastMove().getX(), n.getLastMove().getY(), node.getPlayerToMove());
            return n;
        }
        return Selection(n);
    }

    private int Simulation(GameTree node) {
        if (node.IsLeaf()) {
            int winner = node.Winner();
            if (winner == id)
                return 2;
            if (winner == 0)
                return 1;
            return 0;
        }
        List<GameTree> nodes = node.MakeChildren();
        int index = outsidePolicy.BestNode(node);
        GameTree n = nodes.get(index);
        node.getBoard().PutElement(n.getLastMove().getX(), n.getLastMove().getY(), node.getPlayerToMove());
        if (n.IsLeaf()) {
            //int winner = n.Winner();
            int x = n.getLastMove().getX();
            int y = n.getLastMove().getY();
            int winner = n.FastWinner(node.getPlayerToMove(), x, y, node.getBoard().GetZTop(x, y));
            node.getBoard().TakeElement(n.getLastMove().getX(), n.getLastMove().getY());
            if (winner == id)
                return 2;
            if (winner == 0)
                return 1;
            return 0;
        }
        int ret = Simulation(n);
        node.getBoard().TakeElement(n.getLastMove().getX(), n.getLastMove().getY());
        return ret;
    }

    private void BackPropagation(GameTree node, int reward) {
        if (node.isRealNode()) {
            node.setReward(node.getReward() + reward);
            node.setSimulations(node.getSimulations() + 1);
            if (node.getMaxReward() < reward)
                node.setMaxReward(reward);
            if (node.getLastMove() != null)
                node.getBoard().TakeElement(node.getLastMove().getX(), node.getLastMove().getY());
        }
        if (node.getParent() != null)
            BackPropagation(node.getParent(), reward);
    }

    @Override
    public void PrepareMove(int timeToMove, Board board) {
        SetKeepConstructTree(1);
        root = ConstructTree(board, 0);
        while (true) {
            if (GetKeepConstructTree()==1) {
                GameTree node = Selection(root);
                int reward = Simulation(node);
                BackPropagation(node, reward);
            }
            else
            {
                SetKeepConstructTree(3);
                return;
            }
        }
    }

    @Override
    public Position MakeMove() {
        SetKeepConstructTree(2);
        while (GetKeepConstructTree()!=3);
        double bestMeanReward = 0;
        Position bestPosition = null;
        List<GameTree> children = root.MakeChildren();
        for (int i = 0; i < children.size(); i++) {
            GameTree child = children.get(i);
            double reward = (double) child.getReward() / (double) child.getSimulations();
            if (reward > bestMeanReward) {
                bestMeanReward = reward;
                bestPosition = child.getLastMove();
            }
        }
        if(bestPosition == null)
        {

            int kkk = 4;
        }
        return bestPosition;
    }

    @Override
    public int GetId() {
        return id;
    }

    @Override
    public void ResetState() {
        root = null;
        keepConstructTree = 0;
    }

    private int GetKeepConstructTree() {
        lock.lock();
        int tmp = keepConstructTree;
        lock.unlock();
        return tmp;
    }

    private void SetKeepConstructTree(int value) {
        lock.lock();
        keepConstructTree = value;
        lock.unlock();
    }
}
