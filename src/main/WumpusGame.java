package main;

import java.util.Scanner;


public class WumpusGame {
    private boolean tracing;
    private Scanner scanner;
    private WumpusBot bot;
    /**
     * times of playing the game
     */
    private int timesPlay = 0;
    /**
     * times of winning the game
     */
    private int timesWin = 0;
    /**
     * times of the lost that eaten by Wumpus
     */
    private int timesLostEat = 0;
    /**
     * times of the lost that fall into pit
     */
    private int timesLostPit = 0;
    /**
     * times of quiting the game
     */
    private int timesLostQuit = 0;
    /**
     * the number of the arrows.
     * Its initial value is 3
     */
    private int arrowNum;


    public WumpusGame() {
        bot = new WumpusBot();
        bot.setTracing(false);
        setTracing(false);
        scanner = new Scanner(System.in);
        play();
    }


    public void play() {
        trace("play method");

        //welcome statement
        System.out.println("Hunt the Wumpus!");
        System.out.println("================");
        System.out.println("You have to find and shoot the wumpus and not fall in the pit." + "\n");

        //game start
        System.out.print("Would you like to play Hunt the Wumpus? ");
        while (scanner.nextLine().equals("y")) {
            arrowNum = 3;
            bot.newGame();
            printState();
            int temp;
            boolean gameOver = false;
            while (!gameOver) {
                System.out.print("\n");
                System.out.print("Please choose from (W)alk, (S)hoot, or (Q)uit: ");
                String order = scanner.nextLine();
                if (order.equals("w")) {
                    System.out.print("Which cave would you like to walk into? ");
                    temp = Integer.parseInt(scanner.nextLine());
                    if (doWalk(bot.tryWalk(temp))) {
                        System.out.print("\n" + "Would you like to play Hunt the Wumpus again? ");
                        gameOver = true;
                    } else {
                        printState();
                    }
                } else if (order.equals("s")) {
                    if (arrowNum == 0) {
                        System.out.println("You can't shoot -- you have no arrows left!");
                        printState();
                    } else {
                        System.out.print("Which cave would you like to shoot into? ");
                        temp = Integer.parseInt(scanner.nextLine());
                        if (doShoot(bot.tryShoot(temp))) {
                            System.out.println("\n" + "Would you like to play Hunt the Wumpus again?");
                            gameOver = true;
                        } else {
                            printState();
                        }
                    }
                } else if (order.equals("q")) {
                    System.out.print("\n" + "Would you like to play Hunt the Wumpus again? ");
                    timesPlay++;
                    timesLostQuit++;
                    gameOver = true;
                } else {
                    gameOver = true;
                }

            }

        }
        explain();

    }

    /**
     *
     * @param statement the status of movement return by bot.tryShoot
     * @return whether the game is over
     */
    public boolean doShoot(int statement) {
        if (statement == bot.SUCCESS) {
            System.out.println("It's a direct hit. You've killed the wumpus!");
            trace("kill the wumpus");
            timesPlay++;
            timesWin++;
            arrowNum--;
            return true;
        } else if (statement == bot.FAILURE) {
            System.out.println("Arrow missed. I guess the wumpus wasn't in there...");
            trace("missed");
            arrowNum--;
            return false;
        } else if (statement == bot.IMPOSSIBLE) {
            System.out.println("What a waste. The arrow just bounced off the wall...");
            trace("missed");
            arrowNum--;
            return false;
        }
        return false;
    }

    /**
     * to do the move of walk
     * @param statement the status of movement return by bot.tryWalk
     * @return whether the game is over
     */
    public boolean doWalk(int statement) {
        if (statement == bot.EATEN) {
            System.out.println("You're dead. The wumpus gotcha!");
            trace("eaten by wu,pus");
            timesPlay++;
            timesLostEat++;
            return true;
        } else if (statement == bot.FELL) {
            System.out.println("You're dead. You just fell in the pit.");
            trace("fell into pit");
            timesPlay++;
            timesLostPit++;
            return true;
        } else if (statement == bot.IMPOSSIBLE) {
            System.out.println("You have a nasty bruise on your shoulder from bumping into the wall.");
            trace("error walk");
            return false;
        } else if (statement == bot.SUCCESS) {
            System.out.println("Walk successful.");
            return false;
        }
        return false;
    }

    /**
     * print the current state
     */
    public void printState() {
        System.out.print("\n");
        System.out.println("You are in cave #" + bot.getCurrent() + ".");
        System.out.println("To your left is #" + bot.nextCave('l') + ", to your right is #" + bot.nextCave('r') + ", and ahead is #" + bot.nextCave('a') + ".");
        System.out.println("You have " + arrowNum + " arrows remaining.");
        if (bot.wumpusNear()) {
            System.out.print("\n");
            System.out.println("You can smell something horrible.");
        } else if (bot.pitNear()) {
            System.out.print("\n");
            System.out.println("You feel a cold wind.");
        }

    }

    /**
     * print the explanation of the game
     */
    public void explain() {
        trace("explain the game");
        System.out.print("\n");
        System.out.println("You played a total of " + timesPlay + " games.");
        System.out.println("You won " + timesWin + " games.");
        System.out.println("You lost (by being eaten) " + timesLostEat + " games.");
        System.out.println("You lost (by falling in the pit) " + timesLostPit + " games.");
        System.out.println("You lost (due to quitting) " + timesLostQuit + " games.");
    }


    public void setTracing(boolean onOff) {
        tracing = onOff;
    }


    public void trace(String message) {
        if (tracing) {
            System.out.println("WumpusGame: " + message);
        }
    }
}
