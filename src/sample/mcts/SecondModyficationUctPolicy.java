package sample.mcts;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class SecondModyficationUctPolicy implements ChoseNodePolicy {

    @Override
    public int BestNode(GameTree node) {

        List<GameTree> children = node.MakeChildren();
        return ThreadLocalRandom.current().nextInt(0, children.size());
    }
}