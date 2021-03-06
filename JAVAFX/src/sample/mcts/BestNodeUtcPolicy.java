package sample.mcts;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class BestNodeUtcPolicy implements ChoseNodePolicy {

    private double C;
    private double k;

    public BestNodeUtcPolicy(double c, double k) {
        C = c;
        this.k = k;
    }

    @Override
    public int BestNode(GameTree node) {

        // jeśli jakiś następnik nie był używany to go użyj
        List<GameTree> children = node.MakeChildren();
        List<GameTree> notUsedYetNodes = new ArrayList<>();

        for (int i = 0; i < children.size(); i++) {
            if (children.get(i).getSimulations() == 0) {
                notUsedYetNodes.add(children.get(i));
            }
        }

        int size = notUsedYetNodes.size();
        if (size > 0) {
            int num = children.indexOf(notUsedYetNodes.get(ThreadLocalRandom.current().nextInt(0, size)));
            return num;
        }

        // jeśli wszystkie następniki są użyte
        double uctMaxValue = -1;
        int maxIndex = -1;
        for (int i = 0; i < children.size(); i++) {
            double q = (double) node.getReward() / (double) node.getSimulations();
            double uctValue = k * node.getMaxReward() + (1 - k) * q + C * Math.sqrt(Math.log((double) node.getSimulations()) / children.get(i).getSimulations());
            if (uctValue > uctMaxValue) {
                uctMaxValue = uctValue;
                maxIndex = i;
            }
        }
        return maxIndex;
    }
}