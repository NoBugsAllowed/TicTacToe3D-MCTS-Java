package sample;

import sample.mcts.GameTree;

import java.util.ArrayList;
import java.util.List;

public class HeuristicPlayer implements ArtificialPlayer {
    private int id;
    private GameTree root;
    private List<Double> rewards;

    public HeuristicPlayer(int id) {
        this.id = id;
    }

    @Override
    public void PrepareMove(int timeToMove, Board board) {
        root = ConstructTree(board, 0);
        rewards = PrepareRewards(root);
    }

    @Override
    public Position MakeMove() {
        return SelectChildren(root.MakeChildren(),rewards).getLastMove();
    }

    @Override
    public int GetId() {
        return id;
    }

    @Override
    public void ResetState() {
        root = null;
        rewards = null;
    }

    private List<Double> PrepareRewards(GameTree node) {
        List<Double> rewards = new ArrayList<>();
        List<GameTree> children = node.MakeChildren();
        for (int i = 0; i < children.size(); i++) {
            GameTree child = children.get(i);
            int x = child.getLastMove().getX();
            int y = child.getLastMove().getY();
            node.getBoard().PutElement(x,y, node.getPlayerToMove());
            int z = node.getBoard().GetZTop(x,y);
            double reward = node.getBoard().GetHeuristicFastReward(node.getPlayerToMove(),x,y,z);
            rewards.add(reward);
            node.getBoard().TakeElement(child.getLastMove().getX(), child.getLastMove().getY());
        }
        return rewards;
    }

    public GameTree ConstructTree(Board board, int depth) {
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

    private GameTree SelectChildren(List<GameTree> nodes, List<Double> rewards) {
        double sum = 0;
        for(int i=0;i<rewards.size();i++)
        {
            sum += rewards.get(i);
        }

        double num = Math.random() * sum;
        sum = 0;
        for(int i=0;i<rewards.size();i++)
        {
            sum += rewards.get(i);
            if(num<=sum)
                return nodes.get(i);
        }
        return nodes.get(nodes.size()-1);
    }
}
