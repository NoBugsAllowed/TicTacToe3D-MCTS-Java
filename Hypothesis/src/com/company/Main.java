package com.company;

import sample.ArtificialPlayer;
import sample.Board;
import sample.Position;
import sample.Result;
import sample.mcts.BasicUctPolicy;
import sample.mcts.MctsUctPlayer;
import sample.mcts.MonteCarloPolicy;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
//        int pp1 = 0;
//        int pp2 = 0;
//        for(int i=0;i<50;i++) {
//            ArtificialPlayer p1 = new MctsUctPlayer(1, new BasicUctPolicy(20, i+77), new MonteCarloPolicy());
//            ArtificialPlayer p2 = new MctsUctPlayer(2, new BasicUctPolicy(1, i), new MonteCarloPolicy());
//            if(Play(p1, p2)==1)
//                pp1++;
//            else
//                pp2++;
//        }
//
//        for(int i=0;i<50;i++) {
//            ArtificialPlayer p1 = new MctsUctPlayer(1, new BasicUctPolicy(20, i+72), new MonteCarloPolicy());
//            ArtificialPlayer p2 = new MctsUctPlayer(2, new BasicUctPolicy(1, i), new MonteCarloPolicy());
//            if(Play(p2, p1)==1)
//                pp1++;
//            else
//                pp2++;
//        }
//
//        System.out.println("p1 wins-"+pp1);
//        System.out.println("p2 wins-"+pp2);

        Result res = hypothesis1(
                new MctsUctPlayer(1, new BasicUctPolicy(1, 1), new MonteCarloPolicy()),
                new MctsUctPlayer(2, new BasicUctPolicy(1, 2), new MonteCarloPolicy()),
                500, 500);
        res.SaveToFile("hypothesis1.txt");
    }

    private static int Play(ArtificialPlayer p1, ArtificialPlayer p2, int moveTime) {
        System.out.println("Play Started");
        Board board = new Board(4);
        int movesCount = 0;
        while (true) {

            new Thread(new Runnable() {
                public void run() {
                    p1.PrepareMove(10000, board);
                }
            }).start();

            try {
                Thread.sleep(moveTime);
                Position pos = p1.MakeMove();
                board.PutElement(pos.getX(), pos.getY(), p1.GetId());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            movesCount++;

            if (board.Winner() != 0) {
                System.out.println("Player " + board.Winner() + " win");
                return board.Winner();
            }

            new Thread(new Runnable() {
                public void run() {
                    p2.PrepareMove(10000, board);
                }
            }).start();

            try {
                Thread.sleep(moveTime);
                Position pos = p2.MakeMove();
                board.PutElement(pos.getX(), pos.getY(), p2.GetId());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            movesCount++;

            if (board.Winner() != 0) {
                System.out.println("Player " + board.Winner() + " win");
                return board.Winner();
            }
        }
    }

    private static Result hypothesis1(ArtificialPlayer p1, ArtificialPlayer p2, int gamesCount, int moveTime) {
        Result r = new Result();
        for (int i = 0; i < gamesCount; i++) {
            System.out.println(i);
            int res = Play(p1, p2, moveTime);
            if (res == p1.GetId()) {
                r.P1Wins++;
            } else if (res == p2.GetId()) {
                r.P2Wins++;
            } else {
                r.Draws++;
            }
            p1.ResetState();
            p2.ResetState();
        }
        return r;
    }
}
