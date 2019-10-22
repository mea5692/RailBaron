package student;

import model.Station;

/**
 * Represents a train station on the Railroad Barons map (in the model).
 *
 * @author Matt Agger, mea5692@rit.edu
 * @author Zachary Talis, zft5955@rit.edu
 */
public class StationImpl extends SpaceImpl implements Station {

    // The name of this station
    private String name;

    /**
     * The constructor for this class.
     * @param row sets the row coordinate.
     * @param column sets the column coordinate.
     * @param name sets the name of this station.
     */
    public StationImpl(int row, int column, String name) {
        super(row, column);
        this.name = name;
    }

    /**
     * Getter method for name.
     * @return the name of this station.
     */
    @Override
    public String getName() { return name; }
}
