package sample.mcts;

import sample.Position;

import javax.swing.tree.TreeNode;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Predicate;

public class SecondModyficationUctPolicy implements ChoseNodePolicy {

    private double k;
    BasicUctPolicy policy;

    public SecondModyficationUctPolicy(double k) {
        this.k = k;
        policy = new BasicUctPolicy(k);
    }

    @Override
    public int BestNode(GameTree node) {
        double x = Math.random();
        if(x>=0.5){
            return policy.BestNode(node);
        }
        List<GameTree> possibleChildren = node.MakeChildren();
        List<GameTree> children = node.getChildren();

        int i = 0;
        while(i<possibleChildren.size())
        {
            Position pos = possibleChildren.get(i).getLastMove();
            if(children.stream().filter(c -> c.getLastMove().Equals(pos)).findFirst().isPresent())
            {
                possibleChildren.removeIf(c -> c.getLastMove().Equals(pos));
            }
            else {
                i++;
            }
        }
        if(possibleChildren.size()==0)
            return policy.BestNode(node);
        int index = ThreadLocalRandom.current().nextInt(0, possibleChildren.size());

        GameTree child = possibleChildren.get(index);

        possibleChildren = node.MakeChildren();

        return indexOf(possibleChildren, c -> c.getLastMove().Equals(child.getLastMove()));
    }

    private static <T> int indexOf(List<T> list, Predicate<? super T> predicate) {
        for(ListIterator<T> iter = list.listIterator(); iter.hasNext(); )
            if(predicate.test(iter.next())) return iter.previousIndex();
        return -1;
    }
}