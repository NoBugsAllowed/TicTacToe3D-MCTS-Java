package sample.mcts;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class MonteCarloPolicy implements ChoseNodePolicy {

    private Random r;

    public MonteCarloPolicy(int seed){
        this.r = new Random(seed);
    }

    public MonteCarloPolicy() {
        this.r = new Random(1);
    }

    @Override
    public int BestNode(GameTree node) {
        List<GameTree> children = node.MakeChildren();
        if (children.size() > 0) {
            int num = r.nextInt(children.size());
            return num;
        }
        return -1;
    }
}
