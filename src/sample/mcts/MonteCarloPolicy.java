package sample.mcts;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class MonteCarloPolicy implements ChoseNodePolicy {

    @Override
    public int BestNode(GameTree node) {
        List<GameTree> children = node.MakeChildren();
        if (children.size() > 0) {
            int num = ThreadLocalRandom.current().nextInt(0, children.size());
            return num;
        }
        return -1;
    }
}
