package main;


import java.util.Random;


public class WumpusBot {
    // final instance variables
    public final int IMPOSSIBLE = -1;    // operation unsuccessful because it was not allowed
    public final int SUCCESS = 0;        // operation successful
    public final int EATEN = 1;            // operation unsuccessful because player found by wumpus
    public final int FELL = 2;            // operation unsuccessful because player fell in pit
    public final int FAILURE = 3;        // operation unsuccessful for reason other than above two reasons

    private final int NUM_CAVES = 8;                    // number of caves
    private final int[] RIGHT_CAVE = {3, 0, 1, 2, 5, 6, 7, 4};    // caves to right
    private final int[] LEFT_CAVE = {1, 2, 3, 0, 7, 4, 5, 6};    // caves to left
    private final int[] MID_CAVE = {4, 5, 6, 7, 0, 1, 2, 3};    // caves to front

    //non-final instance variables
    private int currCave;        // current location of player
    private int wumpusPos;        // cave where the wumpus lives
    private int pitPos;            // cave where the cave is found
    private Random generator;    // to use for random placement in caves
    private boolean tracing;    // switch for tracing messages


    /**
     * creates a bot for a Hunt the Wumpus game
     */
    public WumpusBot() {
        generator = new Random();
        tracing = true;                // assume program is being debugged
        generator.setSeed(101);        // seed RNG for tracing purposes
    }


    /**
     * provide the number of caves in the cave system
     *
     * @return indicates how many caves in the system
     */
    public int getNumCaves() {
        return NUM_CAVES;
    }


    /**
     * randomly determines unique locations of start (currentCave), pit, wumpus
     *
     * @return indicates the starting location (cave)
     */
    public int newGame() {
        int pos;

        // determine player's position
        pos = generator.nextInt(NUM_CAVES);
        currCave = pos;
        trace("player starts at " + currCave);

        // determine wumpus' position
        pos = generator.nextInt(NUM_CAVES);
        while (pos == currCave) {
            pos = generator.nextInt(NUM_CAVES);
        }
        wumpusPos = pos;
        trace("wumpus is at " + wumpusPos);

        // determine position of pit
        pos = generator.nextInt(NUM_CAVES);
        while ((pos == currCave) || (pos == wumpusPos)) {
            pos = generator.nextInt(NUM_CAVES);
        }
        pitPos = pos;
        trace("pit is at " + pitPos);

        return currCave;
    }


    /**
     * determine if wumpus is in adjacent cave
     *
     * @return whether wumpus location is in a cave connected to current
     */
    public boolean wumpusNear() {
        boolean isClose = false;

        if ((RIGHT_CAVE[currCave] == wumpusPos) ||
                (MID_CAVE[currCave] == wumpusPos) ||
                (LEFT_CAVE[currCave] == wumpusPos)) {
            isClose = true;
            trace("wumpus is close ");
        } else {
            trace("wumpus is not close");
        }

        return isClose;
    }


    /**
     * determine if pit is in adjacent cave
     *
     * @return whether pit location is in a cave connected to current
     */
    public boolean pitNear() {
        boolean isClose = false;

        if ((RIGHT_CAVE[currCave] == pitPos) ||
                (MID_CAVE[currCave] == pitPos) ||
                (LEFT_CAVE[currCave] == pitPos)) {
            isClose = true;
            trace("pit is close");
        } else {
            trace("pit is not close");
        }

        return isClose;
    }


    /**
     * try to move the player to another cave
     *
     * @param into indicates the cave to try to move into
     * @return status of movement
     * SUCCESS: move was successful (current position changed)
     * IMPOSSIBLE: move was impossible (current position not changed)
     * EATEN: moved and was eaten
     * FELL: moved and fell in pit
     */
    public int tryWalk(int into) {
        int result;

        // check direction
        if ((RIGHT_CAVE[currCave] == into) ||
                (MID_CAVE[currCave] == into) ||
                (LEFT_CAVE[currCave] == into)) {
            trace("move into cave " + into);
            currCave = into;
            if (currCave == wumpusPos) {
                result = EATEN;
            } else {
                if (currCave == pitPos) {
                    result = FELL;
                } else {
                    result = SUCCESS;
                }
            }
        } else {
            result = IMPOSSIBLE;
        }

        trace("result of attempt to move: " + result);

        return result;
    }


    /**
     * try to shoot into another cave
     *
     * @param into indicates the cave to try to shoot into
     * @return status of shooting
     * SUCCESS: shot was successful (wumpus killed)
     * IMPOSSIBLE: shot was impossible (no arrow used)
     * FAILURE: shot was unsuccessful (wumpus not present)
     */
    public int tryShoot(int into) {
        int result;

        if ((RIGHT_CAVE[currCave] == into) ||
                (MID_CAVE[currCave] == into) ||
                (LEFT_CAVE[currCave] == into)) {
            trace("shoot  into cave " + into);
            trace("wumpus is at " + wumpusPos);

            if (into == wumpusPos) {
                result = SUCCESS;
            } else {
                result = FAILURE;
            }
        } else {
            result = IMPOSSIBLE;
        }

        trace("result of attempt to shoot: " + result);

        return result;
    }


    /**
     * provide the number of the current cave
     *
     * @return which cave number player is within
     */
    public int getCurrent() {
        trace("in cave number " + currCave);
        return currCave;
    }


    /**
     * determine number of adjacent cave given its direction
     *
     * @param direction indicates the direction required (l - left, r - right, a - ahead)   *  @return status of movement
     * @return number of cave in that direction or IMPOSSIBLE if invalid parameter
     */
    public int nextCave(char direction) {
        final char LEFT = 'l';    // left
        final char AHEAD = 'a';    // ahead
        final char RIGHT = 'r';    // right

        int nextIs;

        switch (direction) {
            case LEFT:
                nextIs = LEFT_CAVE[currCave];
                break;
            case AHEAD:
                nextIs = MID_CAVE[currCave];
                break;
            case RIGHT:
                nextIs = RIGHT_CAVE[currCave];
                break;
            default:
                nextIs = IMPOSSIBLE;
        }
        trace("next cave on " + direction + " is " + nextIs);

        return nextIs;
    }


    /**
     * turn tracing messages on or off (if off it is assumed that debugging is not occurring and so a new (unseeded) RNG is provided
     *
     * @param onOff indicates the required state of messages (true on, false off)
     */
    public void setTracing(boolean onOff) {
        if (!onOff)    // not tracing so get an unseeded RNG
        {
            generator = new Random();
        }

        tracing = onOff;
    }


    /**
     * displays tracing messages
     *
     * @param message the message to be displayed if instance variable tracing is true
     */
    public void trace(String message) {
        if (tracing) {
            System.out.println("WumpusBot: " + message);
        }
    }

}

