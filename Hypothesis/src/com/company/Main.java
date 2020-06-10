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
        Result res;
        // Hipoteza 1 - Czy gracz rozpoczynajacy wygrywa czesciej
        System.out.println("Hipoteza 1");
        res = playGames(
                new MctsUctPlayer(1, new BasicUctPolicy(Math.sqrt(2), 1), new MonteCarloPolicy()),
                new MctsUctPlayer(2, new BasicUctPolicy(Math.sqrt(2), 2), new MonteCarloPolicy()),
                100, 1000);
        res.SaveToFile("hypothesis1.txt");
        // Hipoteza 2 - Czy modyfikacje lepsze niz wersja podstawowa
        System.out.println("Hipoteza 2, modyfikacja 1");
        res = playGamesSwitchPlayers(
                new MctsUctPlayer(1, new BasicUctPolicy(Math.sqrt(2), 1), new MonteCarloPolicy()),
                new MctsUctPlayer(2, new BestNodeUtcPolicy(Math.sqrt(2), 0.1), new MonteCarloPolicy()),
                500, 500);
        res.SaveToFile("basic_vs_mod1.txt");
        System.out.println("Hipoteza 2, modyfikacja 2");
        res = playGamesSwitchPlayers(
                new MctsUctPlayer(1, new BasicUctPolicy(Math.sqrt(2), 1), new MonteCarloPolicy()),
                new MctsUctPlayer(2, new SecondModyficationUctPolicy(0.1), new MonteCarloPolicy()),
                500, 500);
        res.SaveToFile("basic_vs_mod2.txt");
        // Hipoteza 3 - Czy wartosc C=sqrt(2) lepsza niz inne
//        System.out.println("Hipoteza 3, C=0.5");
//        res = playGamesSwitchPlayers(
//                new MctsUctPlayer(1, new BasicUctPolicy(Math.sqrt(2), 1), new MonteCarloPolicy()),
//                new MctsUctPlayer(2, new BasicUctPolicy(0.5, 2), new MonteCarloPolicy()),
//                200, 200);
//        res.SaveToFile("hypothesis3_0_5.txt");
//        System.out.println("Hipoteza 3, C=1");
//        res = playGamesSwitchPlayers(
//                new MctsUctPlayer(1, new BasicUctPolicy(Math.sqrt(2), 1), new MonteCarloPolicy()),
//                new MctsUctPlayer(2, new BasicUctPolicy(1, 2), new MonteCarloPolicy()),
//                200, 200);
//        res.SaveToFile("hypothesis3_1.txt");
//        System.out.println("Hipoteza 3, C=2");
//        res = playGamesSwitchPlayers(
//                new MctsUctPlayer(1, new BasicUctPolicy(Math.sqrt(2), 1), new MonteCarloPolicy()),
//                new MctsUctPlayer(2, new BasicUctPolicy(2, 2), new MonteCarloPolicy()),
//                200, 200);
//        res.SaveToFile("hypothesis3_2.txt");
//        System.out.println("Hipoteza 3, C=2.5");
//        res = playGamesSwitchPlayers(
//                new MctsUctPlayer(1, new BasicUctPolicy(Math.sqrt(2), 1), new MonteCarloPolicy()),
//                new MctsUctPlayer(2, new BasicUctPolicy(2.5, 2), new MonteCarloPolicy()),
//                200, 200);
//        res.SaveToFile("hypothesis3_2_5.txt");
    }

    private static int play(ArtificialPlayer p1, ArtificialPlayer p2, int moveTime) {
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
                return board.Winner();
            }
        }
    }
    // Symulacja wielu rozgrywek
    private static Result playGames(ArtificialPlayer p1, ArtificialPlayer p2, int gamesCount, int moveTime) {
        Result r = new Result();
        for (int i = 0; i < gamesCount; i++) {
            System.out.println(i);
            int res = play(p1, p2, moveTime);
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

    // Symulacja wielu rozgrywek, po polowie gier nastepuje zamiana graczy
    private static Result playGamesSwitchPlayers(ArtificialPlayer p1, ArtificialPlayer p2, int gamesCount, int moveTime) {
        Result r = new Result();
        for (int i = 0; i < gamesCount/2; i++) {
            System.out.println(i);
            int res = play(p1, p2, moveTime);
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
        for (int i = 0; i < gamesCount/2; i++) {
            System.out.println(gamesCount/2 + i);
            int res = play(p2, p1, moveTime);
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
