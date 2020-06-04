package sample;

import sample.Board;
import sample.Position;

public interface ArtificialPlayer {
    public void PrepareMove(int timeToMove, Board board);
    public Position MakeMove();
    public int GetId();
    public void ResetState();
}
