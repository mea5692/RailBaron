package student;

import model.Card;
import model.Pair;

/**
 * Cards in a Railroad Barons game are dealt to each Player in pairs. This
 * class is used to hold one such pair of cards. Note that, if the deck is
 * empty, one or both cards may have a value of "none."
 *
 * @author Matt Agger, mea5692@rit.edu
 * @author Zachary Talis, zft5955@rit.edu
 */
public class PairImpl implements Pair {

    /** The first card in the pair */
    private Card firstCard;

    /** The second card in the pair */
    private Card secondCard;

    /**
     * The constructor for this class.
     *
     * @param firstCard the first card in the pair
     * @param secondCard the second card in the pair
     */
    PairImpl(Card firstCard, Card secondCard) {
        this.firstCard = firstCard;
        this.secondCard = secondCard;
    }

    /**
     * Returns the first card in the pair. Note that, if the game deck is
     * empty, the value of this card may be "none."
     *
     * @return the first card in the pair
     */
    @Override
    public Card getFirstCard() {
        return firstCard;
    }

    /**
     * Returns the second card in the pair. Note that, if the game deck is
     * empty, the value of this card may be "none."
     *
     * @return the second card in the pair
     */
    @Override
    public Card getSecondCard() {
        return secondCard;
    }
}
