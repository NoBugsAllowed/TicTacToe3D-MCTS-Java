package sample.mcts;

import sample.ArtificialPlayer;
import sample.Board;
import sample.Position;

import java.util.List;

public class MtcsUctPlayer implements ArtificialPlayer {

    private int id;
    private GameTree root;
    private ChoseNodePolicy insidePolicy;
    private ChoseNodePolicy outsidePolicy;
    private Board currentBoard;

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

    public MtcsUctPlayer(int id, ChoseNodePolicy insidePolicy, ChoseNodePolicy outsidePolicy) {
        this.id = id;
        this.insidePolicy = insidePolicy;
        this.outsidePolicy = outsidePolicy;
    }

    public GameTree getRoot() {
        return root;
    }

    private GameTree Selection(GameTree node) {
        if(node.IsLeaf())
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
        GameTree ret = Selection(n);
        return ret;
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
            int winner = n.Winner();
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
            if(node.getLastMove()!=null)
                node.getBoard().TakeElement(node.getLastMove().getX(),node.getLastMove().getY());
        }
        if (node.getParent() != null)
            BackPropagation(node.getParent(), reward);
    }

    @Override
    public void PrepareMove(int timeToMove, Board board) {
        root = ConstructTree(board, 0);
        for (int i = 0; i < timeToMove; i++) {
            GameTree node = Selection(root);
            int reward = Simulation(node);
            BackPropagation(node, reward);
        }
    }

    @Override
    public Position MakeMove() {
        double bestMeanReward = 0;
        Position bestPosition = null;
        for(int i=0;i<root.getChildren().size();i++)
        {
            GameTree child =root.getChildren().get(i);
            double reward = (double)child.getReward()/(double)child.getSimulations();
            if(reward>bestMeanReward)
            {
                bestMeanReward = reward;
                bestPosition = child.getLastMove();
            }
        }
        return bestPosition;
    }
}