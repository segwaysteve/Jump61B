package jump61;

import java.util.ArrayList;
import java.util.Random;

import static jump61.Side.*;

/** An automated Player.
 *  @author P. N. Hilfinger
 */
class AI extends Player {

    /** A new player of GAME initially COLOR that chooses moves automatically.
     *  SEED provides a random-number seed used for choosing moves.
     */
    AI(Game game, Side color, long seed) {
        super(game, color);
        _random = new Random(seed);
    }

    @Override
    String getMove() {
        Board board = getGame().getBoard();

        assert getSide() == board.whoseMove();
        int choice = searchForMove();
        getGame().reportMove(board.row(choice), board.col(choice));
        return String.format("%d %d", board.row(choice), board.col(choice));
    }

    /** Return a move after searching the game tree to DEPTH>0 moves
     *  from the current position. Assumes the game is not over. */
    private int searchForMove() {
        Board work = new Board(getBoard());
        assert getSide() == work.whoseMove();
        _foundMove = -1;
        if (getSide() == RED) {
            minMax(work, 4, true, 1, min, max);
        } else {
            minMax(work, 4, true, -1, min, max);
        }
        return _foundMove;
    }


    /** Find a move from position BOARD and return its value, recording
     *  the move found in _foundMove iff SAVEMOVE. The move
     *  should have maximal value or have value > BETA if SENSE==1,
     *  and minimal value or value < ALPHA if SENSE==-1. Searches up to
     *  DEPTH levels.  Searching at level 0 simply returns a static estimate
     *  of the board value and does not set _foundMove. If the game is over
     *  on BOARD, does not set _foundMove. */
    private int minMax(Board board, int depth, boolean saveMove,
                       int sense, int alpha, int beta) {
        int move = 0;
        int boardValue = staticEval(board, Integer.MAX_VALUE);
        if (depth == 0 || board.getWinner() != null) {
            return boardValue;
        }
        if (sense == 1) {
            boardValue = min;
            ArrayList<Integer> legalMoves = new ArrayList<>();
            for (int i = 0; i < board.length(); i++) {
                if (board.isLegal(RED, i)) {
                    legalMoves.add(i);
                }
            }
            for (int legalMove : legalMoves) {
                board.addSpot(RED, legalMove);
                int nextBoardValue = minMax(board, depth - 1,
                        false, -sense, alpha, beta);
                board.undo();
                boardValue = Math.max(nextBoardValue, boardValue);
                alpha = Math.max(alpha, boardValue);
                move = legalMove;
                if (beta <= alpha) {
                    break;
                }
            }
        } else {
            boardValue = max;
            ArrayList<Integer> legalMoves = new ArrayList<>();
            for (int i = 0; i < board.length(); i++) {
                if (board.isLegal(BLUE, i)) {
                    legalMoves.add(i);
                }
            }
            for (int legalMove : legalMoves) {
                board.addSpot(BLUE, legalMove);
                int nextBoardValue = minMax(board, depth - 1,
                        false, -sense, alpha, beta);
                board.undo();
                boardValue = Math.min(nextBoardValue, boardValue);
                beta = Math.min(beta, boardValue);
                move = legalMove;
                if (beta <= alpha) {
                    break;
                }
            }
        }
        if (saveMove) {
            _foundMove = move;
        }
        return boardValue;
    }

    /** Return a heuristic estimate of the value of board position B.
     *  Use WINNINGVALUE to indicate a win for Red and -WINNINGVALUE to
     *  indicate a win for Blue. */
    private int staticEval(Board b, int winningValue) {
        int value = b.numOfSide(RED) - b.numOfSide(BLUE);
        if (b.getWinner() == RED) {
            return max;
        } else if (b.getWinner() == BLUE) {
            return min;
        }
        return value;
    }

    /** A random-number generator used for move selection. */
    private Random _random;

    /** Used to convey moves discovered by minMax. */
    private int _foundMove;

    /** Maximum integer value. */
    private int max = Integer.MAX_VALUE;

    /** Minimum integer value. */
    private int min = Integer.MIN_VALUE;
}
