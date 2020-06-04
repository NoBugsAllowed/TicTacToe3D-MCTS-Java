package sample.mcts;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class BasicUctPolicy implements ChoseNodePolicy {

    private double C;
    private Random r;

    public BasicUctPolicy(double c) {
        C = c;
        this.r = new Random(1);
    }

    public BasicUctPolicy(double c, int seed) {
        C = c;
        this.r = new Random(seed);
    }

    @Override
    public int BestNode(GameTree node) {

        // jeśli jakiś następnik nie był używany to go użyj
        List<GameTree> children = node.MakeChildren();
        List<GameTree> notUsedYetNodes = new ArrayList<>();

        for (int i = 0; i < children.size(); i++) {
            if (children.get(i).getSimulations() == 0)
                notUsedYetNodes.add(children.get(i));
        }

        int size = notUsedYetNodes.size();
        if (size > 0) {
            int num = children.indexOf(notUsedYetNodes.get(r.nextInt(size)));
            return num;
        }

        // jeśli wszystkie następniki są użyte
        double uctMaxValue = -1;
        int maxIndex = -1;
        for (int i = 0; i < children.size(); i++) {
            double q = (double) node.getReward() / (double) node.getSimulations();
            double uctValue = q + C * Math.sqrt(Math.log((double) node.getSimulations()) / children.get(i).getSimulations());
            if (uctValue > uctMaxValue) {
                uctMaxValue = uctValue;
                maxIndex = i;
            }
        }
        if (maxIndex < 0) {
            int rrr = 5;
        }
        return maxIndex;
    }
}
