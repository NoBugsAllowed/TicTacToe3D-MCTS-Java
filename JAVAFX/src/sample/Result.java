package sample;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Result {
    public Result() {
        P1Wins = 0;
        P2Wins = 0;
        Draws = 0;
    }

    public int P1Wins;
    public int P2Wins;
    public int Draws;

    @Override
    public String toString() {
        return    "P1Wins=" + P1Wins +
                "\nP2Wins=" + P2Wins +
                "\nDraws="  + Draws;
    }

    public void SaveToFile(String fileName)
            throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
        writer.write(this.toString());
        writer.close();
    }
}
