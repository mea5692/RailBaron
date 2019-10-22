package student;

import model.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * A class that represents a player in a Railroad Barons game.
 *
 * @author Matt Agger, mea5692@rit.edu
 * @author Zachary Talis, zft5955@rit.edu
 */
public class PlayerImpl implements Player {

    /** The baron representing this player */
    private Baron baron;

    /** The observers of this player */
    private LinkedList<PlayerObserver> observers;

    /** The number of game pieces that the player has remaining */
    private int numPieces;

    /** The player's current cards */
    private LinkedList<Card> cards;

    /** The player's current score */
    private int score;

    /** The LinkedList of routes claimed by this player */
    private LinkedList<Route> claimedRoutes;

    /** The most recently dealt pair of cards */
    private Pair lastTwoCards;

    /** The boolean for whether the player has claimed a route this turn yet */
    private boolean hasClaimedRoute = true;

    /** A Railroad Barons game */
    private RailroadBarons railroadBarons;

    /** The stations that are the most west/east/north/south on the map */
    private HashMap<String, LinkedList<Station>> stationsMostWENS;

    /** The boolean for whether the player has received points for claiming
     *  a route from the west to the east */
    private boolean claimedWE = false;

    /** The boolean for whether the player has received points for claiming
     *  a route from the north to the south */
    private boolean claimedNS = false;

    /** The graph of the player's claimed routes */
    private Graph graph;

    /**
     * The constructor for this class.
     *
     * @param baron the baron representing this player
     * @param railroadBarons a Railroad Barons game
     */
    PlayerImpl(Baron baron, RailroadBarons railroadBarons) {
        this.baron = baron;
        observers = new LinkedList<>();
        this.railroadBarons = railroadBarons;
    }

    /**
     * Finds which stations are the most west, east, north, and south.
     *
     * @return a HashMap containing all of the stations, listed under
     * their identifiers.
     */
    private HashMap<String, LinkedList<Station>> getStationsMostWENS() {
        HashMap<String, LinkedList<Station>> stations = new HashMap<>();
        RailroadMap railroadMap = railroadBarons.getRailroadMap();
        stations.put("W", new LinkedList<>());
        int i = 0;
        while(stations.get("W").isEmpty()) {
            for(Route route : railroadMap.getRoutes()) {
                if(route.getOrigin().getCol() == i
                        && !stations.get("W").contains(route.getOrigin()))
                    stations.get("W").add(route.getOrigin());
                if(route.getDestination().getCol() == i
                        && !stations.get("W").contains(route.getDestination()))
                    stations.get("W").add(route.getDestination());
            }
            i++;
        }
        stations.put("E", new LinkedList<>());
        i = railroadMap.getCols() - 1;
        while(stations.get("E").isEmpty()) {
            for(Route route : railroadMap.getRoutes()) {
                if(route.getOrigin().getCol() == i
                        && !stations.get("E").contains(route.getOrigin()))
                    stations.get("E").add(route.getOrigin());
                if(route.getDestination().getCol() == i
                        && !stations.get("E").contains(route.getDestination()))
                    stations.get("E").add(route.getDestination());
            }
            i--;
        }
        stations.put("N", new LinkedList<>());
        i = 0;
        while(stations.get("N").isEmpty()) {
            for (Route route : railroadMap.getRoutes()) {
                if (route.getOrigin().getRow() == i
                        && !stations.get("N").contains(route.getOrigin()))
                    stations.get("N").add(route.getOrigin());
                if (route.getDestination().getRow() == i
                        && !stations.get("N").contains(route.getDestination()))
                    stations.get("N").add(route.getDestination());
            }
            i++;
        }
        stations.put("S", new LinkedList<>());
        i = railroadMap.getRows() - 1;
        while(stations.get("S").isEmpty()) {
            for (Route route : railroadMap.getRoutes()) {
                if (route.getOrigin().getRow() == i
                        && !stations.get("S").contains(route.getOrigin()))
                    stations.get("S").add(route.getOrigin());
                if (route.getDestination().getRow() == i
                        && !stations.get("S").contains(route.getDestination()))
                    stations.get("S").add(route.getDestination());
            }
            i--;
        }
        return stations;
    }

    /**
     * This is called at the start of every game to reset the player to its
     * initial state.
     *
     * @param dealt the hand of cards dealt to the player at the start of the
     *              game
     */
    @Override
    public void reset(Card... dealt) {
        numPieces = 45;
        cards = new LinkedList<>();
        for(int i = 0; i < dealt.length; i++)
            cards.add(dealt[i]);
        score = 0;
        claimedRoutes = new LinkedList<>();
        lastTwoCards = new PairImpl(Card.NONE, Card.NONE);
        stationsMostWENS = getStationsMostWENS();
        graph = new Graph();
    }

    /**
     * Adds an observer that will be notified when the player changes in some
     * way.
     *
     * @param observer the new PlayerObserver
     */
    @Override
    public void addPlayerObserver(PlayerObserver observer) {
        observers.add(observer);
    }

    /**
     * Removes an observer so that it is no longer notified when the player
     * changes in some way.
     *
     * @param observer the PlayerObserver to remove
     */
    @Override
    public void removePlayerObserver(PlayerObserver observer) {
        observers.remove(observer);
    }

    /**
     * The baron as which this player is playing the game.
     *
     * @return the baron as which this player is playing
     */
    @Override
    public Baron getBaron() {
        return baron;
    }

    /**
     * Used to start the player's next turn. A pair of cards is dealt to the
     * player, and the player is once again able to claim a route on the map.
     *
     * @param dealt a pair of cards to the player
     */
    @Override
    public void startTurn(Pair dealt) {
        lastTwoCards = dealt;
        if (dealt.getFirstCard() != Card.NONE)
            cards.add(dealt.getFirstCard());
        if (dealt.getSecondCard() != Card.NONE)
            cards.add(dealt.getSecondCard());
        hasClaimedRoute = false;
    }

    /**
     * Returns the most recently dealt pair of cards. Note that one or both of
     * the cards may have a value of Card.NONE.
     *
     * @return The most recently dealt pair of cards
     */
    @Override
    public Pair getLastTwoCards() {
        return lastTwoCards;
    }

    /**
     * Returns the number of the specific kind of card that the player
     * currently has in hand. Note that the number may be 0.
     *
     * @param card the card of interest
     * @return the number of the specific kind of card that the player
     * current has in hand.
     */
    @Override
    public int countCardsInHand(Card card) {
        int count = 0;
        for(Card currentCard : cards) {
            if(currentCard.equals(card))
                count++;
        }
        return count;
    }

    /**
     * Returns the number of game pieces that the player has remaining. Note
     * that the number may be 0.
     *
     * @return the number of game pieces that the player has remaining
     */
    @Override
    public int getNumberOfPieces() {
        return numPieces;
    }

    /**
     * Returns true iff the following conditions are true:
     * - The route is not already claimed by this or some other baron.
     * - The player has not already claimed a route this turn (players are
     *   limited to one claim per turn).
     * - The player has enough cards (including ONE wild card, if necessary) to
     *   claim the route.
     * - The player has enough train pieces to claim the route.
     *
     * @param route The route being tested to determine whether or not the
     *              player is able to claim it.
     * @return true if the player is able to claim the specified route, and
     * false otherwise
     */
    @Override
    public boolean canClaimRoute(Route route) {
        if(!(route.getBaron().equals(Baron.UNCLAIMED)))
            return false;
        if(hasClaimedRoute)
            return false;
        if(!(canContinuePlaying(route.getLength())))
            return false;
        return true;
    }

    /**
     * Claims the given route on behalf of this player's Railroad Baron. It is
     * possible that the player has enough cards in hand to claim the route by
     * using different combinations of card.
     *
     * @param route the route to claim
     * @throws RailroadBaronsException if the route cannot be claimed, i.e. if
     * the canClaimRoute(Route) method returns false
     */
    @Override
    public void claimRoute(Route route) throws RailroadBaronsException {
        if(!(canClaimRoute(route)))
            throw new RailroadBaronsException("Route cannot be claimed!");
        Card cardWithLowestNumCards = Card.NONE;
        int lowestNumCards = 20;
        for(int i = 1; i < 9; i++) {
            Card card = Card.NONE;
            switch(i) {
                case 1:
                    card = Card.BLACK;
                    break;
                case 2:
                    card = Card.BLUE;
                    break;
                case 3:
                    card = Card.GREEN;
                    break;
                case 4:
                    card = Card.ORANGE;
                    break;
                case 5:
                    card = Card.PINK;
                    break;
                case 6:
                    card = Card.RED;
                    break;
                case 7:
                    card = Card.WHITE;
                    break;
                case 8:
                    card = Card.YELLOW;
                    break;
            }
            if(countCardsInHand(card) <= lowestNumCards
                    && countCardsInHand(card) >= route.getLength()) {
                cardWithLowestNumCards = card;
                lowestNumCards = countCardsInHand(card);
            }
        }
        if(!(cardWithLowestNumCards.equals(Card.NONE))) {
            for(int i = 0; i < route.getLength(); i++)
                cards.remove(cardWithLowestNumCards);
        } else {
            cardWithLowestNumCards = Card.NONE;
            lowestNumCards = 20;
            for(int i = 1; i < 9; i++) {
                Card card = Card.NONE;
                switch(i) {
                    case 1:
                        card = Card.BLACK;
                        break;
                    case 2:
                        card = Card.BLUE;
                        break;
                    case 3:
                        card = Card.GREEN;
                        break;
                    case 4:
                        card = Card.ORANGE;
                        break;
                    case 5:
                        card = Card.PINK;
                        break;
                    case 6:
                        card = Card.RED;
                        break;
                    case 7:
                        card = Card.WHITE;
                        break;
                    case 8:
                        card = Card.YELLOW;
                        break;
                }
                if(countCardsInHand(card) <= lowestNumCards
                        && countCardsInHand(card) >= route.getLength() - 1) {
                    cardWithLowestNumCards = card;
                    lowestNumCards = countCardsInHand(card);
                }
            }
            for(int i = 0; i < route.getLength() - 1; i++)
                cards.remove(cardWithLowestNumCards);
            cards.remove(Card.WILD);
        }
        route.claim(baron);
        numPieces -= route.getLength();
        score += route.getPointValue();
        claimedRoutes.add(route);
        hasClaimedRoute = true;

        graph.addRoute(route);
        if(!claimedWE) {
            for(Station stationW : stationsMostWENS.get("W")) {
                for(Station stationE : stationsMostWENS.get("E")) {
                    if(graph.isInGraph(stationW)
                            && graph.isInGraph(stationE)) {
                        int path = graph.getShortestPath(stationW, stationE);
                        if(path >= 2) {
                            score += 5
                                    * railroadBarons.getRailroadMap().getCols();
                            claimedWE = true;
                            break;
                        }
                    }
                }
            }
        }
        if(!claimedNS) {
            for(Station stationN : stationsMostWENS.get("N")) {
                for(Station stationS : stationsMostWENS.get("S")) {
                    if(graph.isInGraph(stationN)
                            && graph.isInGraph(stationS)) {
                        int path = graph.getShortestPath(stationN, stationS);
                        if(path >= 2) {
                            score += 5
                                    * railroadBarons.getRailroadMap().getRows();
                            claimedNS = true;
                            break;
                        }
                    }
                }
            }
        }

        for(PlayerObserver observer : observers)
            observer.playerChanged(this);
    }

    /**
     * Returns the collection of routes claimed by this player.
     *
     * @return the collection of routes claimed by this player
     */
    @Override
    public Collection<Route> getClaimedRoutes() {
        return claimedRoutes;
    }

    /**
     * Returns the player's current score based on the point value of each
     * route that the player has currently claimed.
     *
     * @return the player's current score
     */
    @Override
    public int getScore() {
        return score;
    }

    /**
     * Returns true iff the following conditions are true:
     * - The player has enough cards (including wild cards) to claim a route of
     *   the specified length.
     * - The player has enough train pieces to claim a route of the specified
     *   length.
     *
     * @param shortestUnclaimedRoute the length of the shortest unclaimed route
     *                               in the current game.
     * @return true if the player can claim such a route, and false otherwise
     */
    @Override
    public boolean canContinuePlaying(int shortestUnclaimedRoute) {
        int maxCards = 0;
        if(countCardsInHand(Card.BLACK) > maxCards)
            maxCards = countCardsInHand(Card.BLACK);
        if(countCardsInHand(Card.BLUE) > maxCards)
            maxCards = countCardsInHand(Card.BLUE);
        if(countCardsInHand(Card.GREEN) > maxCards)
            maxCards = countCardsInHand(Card.GREEN);
        if(countCardsInHand(Card.ORANGE) > maxCards)
            maxCards = countCardsInHand(Card.ORANGE);
        if(countCardsInHand(Card.PINK) > maxCards)
            maxCards = countCardsInHand(Card.PINK);
        if(countCardsInHand(Card.RED) > maxCards)
            maxCards = countCardsInHand(Card.RED);
        if(countCardsInHand(Card.WHITE) > maxCards)
            maxCards = countCardsInHand(Card.WHITE);
        if(countCardsInHand(Card.YELLOW) > maxCards)
            maxCards = countCardsInHand(Card.YELLOW);
        if(countCardsInHand(Card.WILD) > 0 && maxCards > 0)
            maxCards++;
        if(shortestUnclaimedRoute > maxCards)
            return false;
        if(shortestUnclaimedRoute > numPieces)
            return false;
        return true;
    }

    /**
     * Allow the player name to be printed properly.
     * @return the color of this Player.
     */
    @Override
    public String toString() {
        return baron.toString() + " Baron";
    }
}
