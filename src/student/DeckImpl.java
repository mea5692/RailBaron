package student;

import model.Card;
import model.Deck;

import java.util.Random;

/**
 * A deck of cards used in a Railroad Barons game. The default deck has 20 of
 * each type of playable card.
 *
 * @author Matt Agger, mea5692@rit.edu
 * @author Zachary Talis, zft5955@rit.edu
 */
public class DeckImpl implements Deck {

    /** The number of total cards left */
    private int numTotalCardsLeft;

    /** The number of wild cards left */
    private int numWildCardsLeft;

    /** The number of black cards left */
    private int numBlackCardsLeft;

    /** The number of blue cards left */
    private int numBlueCardsLeft;

    /** The number of green cards left */
    private int numGreenCardsLeft;

    /** The number of orange cards left */
    private int numOrangeCardsLeft;

    /** The number of pink cards left */
    private int numPinkCardsLeft;

    /** The number of red cards left */
    private int numRedCardsLeft;

    /** The number of white cards left */
    private int numWhiteCardsLeft;

    /** The number of yellow cards left */
    private int numYellowCardsLeft;

    /** The variable used to draw a random card */
    Random random;

    /**
     * The constructor for this class.
     */
    DeckImpl() {
        reset();
    }

    /**
     * Resets the deck to its starting state. Restores any cards that were
     * drawn and shuffles the deck.
     */
    @Override
    public void reset() {
        numTotalCardsLeft = 180;
        numWildCardsLeft = 20;
        numBlackCardsLeft = 20;
        numBlueCardsLeft = 20;
        numGreenCardsLeft = 20;
        numOrangeCardsLeft = 20;
        numPinkCardsLeft = 20;
        numRedCardsLeft = 20;
        numWhiteCardsLeft = 20;
        numYellowCardsLeft = 20;
        random = new Random();
    }

    /**
     * Draws the next card from the "top" of the deck.
     *
     * @return the next card, unless the deck is empty, in which case this
     * should return Card.NONE
     */
    @Override
    public Card drawACard() {

        if(numTotalCardsLeft == 0)
            return Card.NONE;
        while(true) {
            int cardType = random.nextInt(9);
            switch (cardType) {
                case 0:
                    if (numWildCardsLeft > 0) {
                        numTotalCardsLeft--;
                        numWildCardsLeft--;
                        return Card.WILD;
                    }
                    break;
                case 1:
                    if (numBlackCardsLeft > 0) {
                        numTotalCardsLeft--;
                        numBlackCardsLeft--;
                        return Card.BLACK;
                    }
                    break;
                case 2:
                    if(numBlueCardsLeft > 0) {
                        numTotalCardsLeft--;
                        numBlueCardsLeft--;
                        return Card.BLUE;
                    }
                    break;
                case 3:
                    if(numGreenCardsLeft > 0) {
                        numTotalCardsLeft--;
                        numGreenCardsLeft--;
                        return Card.GREEN;
                    }
                    break;
                case 4:
                    if(numOrangeCardsLeft > 0) {
                        numTotalCardsLeft--;
                        numOrangeCardsLeft--;
                        return Card.ORANGE;
                    }
                    break;
                case 5:
                    if(numPinkCardsLeft > 0) {
                        numTotalCardsLeft--;
                        numPinkCardsLeft--;
                        return Card.PINK;
                    }
                    break;
                case 6:
                    if(numRedCardsLeft > 0) {
                        numTotalCardsLeft--;
                        numRedCardsLeft--;
                        return Card.RED;
                    }
                    break;
                case 7:
                    if(numWhiteCardsLeft > 0) {
                        numTotalCardsLeft--;
                        numWhiteCardsLeft--;
                        return Card.WHITE;
                    }
                    break;
                case 8:
                    if(numYellowCardsLeft > 0) {
                        numTotalCardsLeft--;
                        numYellowCardsLeft--;
                        return Card.YELLOW;
                    }
                    break;
            }
        }
    }

    /**
     * Returns the number of cards that have yet to be drawn.
     *
     * @return the number of cards that have yet to be drawn
     */
    @Override
    public int numberOfCardsRemaining() {
        return numTotalCardsLeft;
    }
}
