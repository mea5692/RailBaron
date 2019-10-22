package student;

import model.Baron;
import model.Orientation;
import model.Route;
import model.Track;

/**
 * Represents a track segment on the Railroad Barons map (in the model).
 *
 * @author Matt Agger, mea5692@rit.edu
 * @author Zachary Talis, zft5955@rit.edu
 */
public class TrackImpl extends SpaceImpl implements Track {

    // The owner of this track
    private Baron owner;
    // The route this track is a part of
    private Route route;

    /**
     * The constructor for this class.
     * @param row sets the row coordinate.
     * @param column sets the column coordinate.
     * @param route sets the track route.
     */
    public TrackImpl(int row, int column, RouteImpl route) {
        super(row, column);
        this.route = route;
    }

    /**
     * Getter method for orientation.
     * @return the orientation of this track.
     */
    @Override
    public Orientation getOrientation() { return route.getOrientation(); }

    /**
     * Getter method for owner.
     * @return the owner of this track.
     */
    @Override
    public Baron getBaron() { return route.getBaron(); }

    /**
     * Getter method for route.
     * @return the route of this track.
     */
    @Override
    public Route getRoute() { return route; }
}
