package sample.mcts;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class MonteCarloHeuristicPolicy implements ChoseNodePolicy {

    @Override
    public int BestNode(GameTree node) {

        List<GameTree> children = node.MakeChildren();
        double maxReward = Double.NEGATIVE_INFINITY;
        int maxRewadrIndex = 0;
        for (int i = 0; i < children.size(); i++) {
            GameTree child = children.get(i);
            int x = child.getLastMove().getX();
            int y = child.getLastMove().getY();
            node.getBoard().PutElement(child.getLastMove().getX(), child.getLastMove().getY(), node.getPlayerToMove());
            int z = node.getBoard().GetZTop(x,y);
            double reward = node.getBoard().GetHeuristicFastReward(node.getPlayerToMove(),x,y,z);
            if (reward > maxReward) {
                maxReward = reward;
                maxRewadrIndex = i;
            }
            node.getBoard().TakeElement(child.getLastMove().getX(), child.getLastMove().getY());
        }

        return maxRewadrIndex;
    }
}