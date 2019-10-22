package student;

import model.Space;

/**
 * Represents a space on the Railroad Barons map (in the model).
 *
 * @author Matt Agger, mea5692@rit.edu
 * @author Zachary Talis, zft5955@rit.edu
 */
public class SpaceImpl implements Space {

    // Holds the row coordinate for this space
    private int row;
    // Holds the column coordinate for this space
    private int column;

    /**
     * The constructor for this class.
     * @param row sets the row coordinate
     * @param column sets the column coordinate
     */
    SpaceImpl (int row, int column) {
        this.row = row;
        this.column = column;
    }

    /**
     * Getter method for row.
     * @return the row coordinate.
     */
    @Override
    public int getRow() {return row;}

    /**
     * Getter method for column.
     * @return the column coordinate.
     */
    @Override
    public int getCol() {return column;}

    /**
     * See if this and another space are in the same place
     * @param other The other space to which this space is being compared for
     *              collocation.
     *
     * @return Whether or not the two spaces occupy the same physical location
     * on the map.
     */
    @Override
    public boolean collocated(Space other) {
        return (other.getRow() == getRow() && other.getCol() == getCol());
    }
}
