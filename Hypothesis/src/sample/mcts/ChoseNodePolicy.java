package sample.mcts;

import java.util.List;

public interface ChoseNodePolicy {
    public int BestNode(GameTree node);
}
