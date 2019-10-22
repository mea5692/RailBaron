package student;

import model.*;

import java.util.Collection;
import java.util.LinkedList;

/**
 * A Railroad Barons game. The main entry point into the model for the entire
 * game.
 *
 * @author Matt Agger, mea5692@rit.edu
 * @author Zachary Talis, zft5955@rit.edu
 */
public class RailroadBaronsImpl implements RailroadBarons {

    /** The observers of this player */
    private LinkedList<RailroadBaronsObserver> observers;

    /** The map for this game of Railroad Barons */
    private RailroadMap map;

    /** The deck for this game of Railroad Barons */
    private Deck deck;

    /** The players in this game of Railroad Barons */
    private LinkedList<Player> players;

    /** The current player */
    private Player currentPlayer;

    /**
     * The constructor for this class.
     */
    public RailroadBaronsImpl() {
        observers = new LinkedList<>();
        players = new LinkedList<>();
        for(int i = 0; i < 4; i++) {
            Baron baron = Baron.UNCLAIMED;
            if(i == 0)
                baron = Baron.RED;
            else if(i == 1)
                baron = Baron.GREEN;
            else if(i == 2)
                baron = Baron.YELLOW;
            else
                baron = Baron.BLUE;
            players.add(new PlayerImpl(baron, this));
        }
    }

    /**
     * Adds a new observer to the collection of observers that will be notified
     * when the state of the game changes.
     *
     * @param observer The {@link RailroadBaronsObserver} to add to the
     *                 Collection of observers.
     */
    @Override
    public void addRailroadBaronsObserver(RailroadBaronsObserver observer) {
        observers.add(observer);
    }

    /**
     * Removes the observer from the collection of observers that will be
     * notified when the state of the game changes.
     *
     * @param observer The {@link RailroadBaronsObserver} to remove.
     */
    @Override
    public void removeRailroadBaronsObserver(RailroadBaronsObserver observer) {
        observers.remove(observer);
    }

    /**
     * Starts a new Railroad Barons game with the specified map and a default
     * deck of cards. If a game is currently in progress, the progress is lost.
     * There is no warning!
     *
     * @param map The {@link RailroadMap} on which the game will be played.
     */
    @Override
    public void startAGameWith(RailroadMap map) {

        // If the map is empty, exit the game before it starts.
        if (map.getRoutes().size() == 0) {
            System.out.println("Empty map.");
            System.exit(1);
        }

        this.map = map;
        deck = new DeckImpl();
        for(Player player : players) {
            Card[] dealt = new Card[4];
            for(int j = 0; j < 4; j++)
                dealt[j] = deck.drawACard();
            player.reset(dealt);
        }
        currentPlayer = players.get(0);
        Pair dealt = new PairImpl(this.deck.drawACard(), this.deck.drawACard());
        currentPlayer.startTurn(dealt);
        for(RailroadBaronsObserver observer : observers)
            observer.turnStarted(this, currentPlayer);
    }

    /**
     * Starts a new Railroad Barons game with the specified map and deck of
     * cards. This means that the game should work with any implementation of
     * the Deck interface (not just a specific implementation)! Otherwise, the
     * starting state of the game is the same as a normal game.
     *
     * @param map The {@link RailroadMap} on which the game will be played.
     * @param deck The {@link Deck} of cards used to play the game. This may
     *             be ANY implementation of the {@link Deck} interface,
     *             meaning that a valid implementation of the
     *             {@link RailroadBarons} interface should use only the
     */
    @Override
    public void startAGameWith(RailroadMap map, Deck deck) {

        // If the map is empty, exit the game before it starts.
        if (map.getRoutes().size() == 0) {
            System.out.println("Empty map.");
            System.exit(1);
        }

        this.map = map;
        this.deck = deck;
        for(Player player : players) {
            Card[] dealt = new Card[4];
            for(int j = 0; j < 4; j++)
                dealt[j] = deck.drawACard();
            player.reset(dealt);
        }
        currentPlayer = players.get(0);
        Pair dealt = new PairImpl(this.deck.drawACard(), this.deck.drawACard());
        currentPlayer.startTurn(dealt);
        for(RailroadBaronsObserver observer : observers)
            observer.turnStarted(this, currentPlayer);
    }

    /**
     * Returns the map currently being used for play. If a game is not in
     * progress, this may be null!
     *
     * @return The RailroadMap being used for play.
     */
    @Override
    public RailroadMap getRailroadMap() {
        return map;
    }

    /**
     * Returns the number of cards that remain to be dealt in the current
     * game's deck.
     *
     * @return The number of cards that have not yet been dealt in the game's
     * Deck.
     */
    @Override
    public int numberOfCardsRemaining() {
        return deck.numberOfCardsRemaining();
    }

    /**
     * Returns whether or not the player can claim a route at a
     * specific coordinate.
     *
     * @param row The row of a {@link Track} in the {@link Route} to check.
     * @param col The column of a {@link Track} in the {@link Route} to check.
     * @return whether or not the player can claim a route at a
     * specific coordinate.
     */
    @Override
    public boolean canCurrentPlayerClaimRoute(int row, int col) {
        return currentPlayer.canClaimRoute(map.getRoute(row, col));
    }

    /**
     * Attempts to claim the route at the specified location on behalf of the
     * current player.
     *
     * @param row The row of a {@link Track} in the {@link Route} to claim.
     * @param col The column of a {@link Track} in the {@link Route} to claim.
     */
    @Override
    public void claimRoute(int row, int col) {
        if(canCurrentPlayerClaimRoute(row, col)) {
            try {
                currentPlayer.claimRoute(map.getRoute(row, col));
                map.routeClaimed(map.getRoute(row, col));
            } catch (RailroadBaronsException rbe) {
                System.out.println(rbe.getMessage());
            }
        }
    }

    /**
     * Called when the current player ends their turn.
     */
    @Override
    public void endTurn() {
        for(RailroadBaronsObserver observer : observers)
            observer.turnEnded(this, currentPlayer);
        if(!gameIsOver()) {
            if (currentPlayer.equals(players.get(0)))
                currentPlayer = players.get(1);
            else if (currentPlayer.equals(players.get(1)))
                currentPlayer = players.get(2);
            else if (currentPlayer.equals(players.get(2)))
                currentPlayer = players.get(3);
            else
                currentPlayer = players.get(0);
            Pair dealt = new PairImpl(this.deck.drawACard(), this.deck.drawACard());
            currentPlayer.startTurn(dealt);
            for (RailroadBaronsObserver observer : observers)
                observer.turnStarted(this, currentPlayer);
        }
    }

    /**
     * Returns the player whose turn it is.
     *
     * @return The Player that is currently taking a turn.
     */
    @Override
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Returns all of the players currently playing the game.
     *
     * @return The Players currently playing the game.
     */
    @Override
    public Collection<Player> getPlayers() {
        return players;
    }

    /**
     * Indicates whether or not the game is over. This occurs when no more
     * plays can be made.
     *
     * @return True if the game is over, false otherwise.
     */
    @Override
    public boolean gameIsOver() {
        for(Player player : players) {
            if(player.canContinuePlaying(
                    map.getLengthOfShortestUnclaimedRoute()))
                return false;
        }

        if (deck.numberOfCardsRemaining() != 0) {
            for (Route route : map.getRoutes()) {
                if (route.getBaron() == Baron.UNCLAIMED)
                    return false;
            }
        }

        Player winner = new PlayerImpl(Baron.UNCLAIMED, this);
        int winnerScore = 0;
        for(Player player : players) {
            if(player.getScore() > winnerScore) {
                winner = player;
                winnerScore = player.getScore();
            }
        }
        for(RailroadBaronsObserver observer : observers)
            observer.gameOver(this, winner);
        return true;
    }
}
