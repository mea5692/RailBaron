package student;

import model.*;

import java.util.LinkedList;
import java.util.List;

/**
 * Represents a route on the Railroad Barons map (in the model).
 *
 * @author Matt Agger, mea5692@rit.edu
 * @author Zachary Talis, zft5955@rit.edu
 */
public class RouteImpl implements Route {

    // The owner of this track
    private Baron owner;
    // The station this route begins at
    private Station origin;
    // The station this route ends at
    private Station destination;
    // The orientation of this route
    private Orientation orientation;
    // The tracks that are a part of this route
    private LinkedList<Track> tracks;
    /**
     * The constructor for this class.
     * @param owner sets the route owner.
     * @param origin sets the route origin.
     * @param destination sets the route destination.
     * @param orientation sets the route orientation.
     * @param tracks sets the tracks that are part of this route.
     */
    public RouteImpl(Baron owner, Station origin, Station destination,
                     Orientation orientation, LinkedList<Track> tracks) {
        this.owner = owner;
        this.origin = origin;
        this.destination = destination;
        this.orientation = orientation;
        this.tracks = tracks;
    }

    /**
     * Getter method for owner.
     * @return the owner of this route.
     */
    @Override
    public Baron getBaron() { return owner; }

    /**
     * Getter method for origin.
     * @return the origin of this route.
     */
    @Override
    public Station getOrigin() { return origin; }

    /**
     * Getter method for destination.
     * @return the destination of this route.
     */
    @Override
    public Station getDestination() { return destination; }

    /**
     * Getter method for orientation.
     * @return the orientation of this route.
     */
    @Override
    public Orientation getOrientation() { return orientation; }

    /**
     * Getter method for tracks.
     * @return the tracks that are a part of this route.
     */
    @Override
    public List<Track> getTracks() { return tracks; }

    /**
     * Get the length of this route.
     * @return the length of tracks, the list of tracks in this route.
     */
    @Override
    public int getLength() { return tracks.size(); }

    /**
     * Get the point value this route is worth.
     * @return the route's point value, based on the provided algorithm.
     */
    @Override
    public int getPointValue() {
        switch (tracks.size()) {
            case 1: return 1;
            case 2: return 2;
            case 3: return 4;
            case 4: return 7;
            case 5: return 10;
            case 6: return 15;
            default: return (5*(getLength()-3));
        }
    }

    /**
     * Check to see if a specific coordinate is part of this route.
     * @param space The {@link Space} that may be in this route.
     *
     * @return whether or not a specific coordinate is part of this route.
     */
    @Override
    public boolean includesCoordinate(Space space) {
        for (Track track : tracks) { if (track.collocated(space)) return true; }
        return false;
    }

    /**
     * Claim this route for a specific baron, if the route is unclaimed.
     * @param claimant The {@link Baron} attempting to claim the route. Must
     *                 not be null or {@link Baron#UNCLAIMED}.
     * @return whether or not the route has been successfully claimed.
     */
    @Override
    public boolean claim(Baron claimant) {
        if (!(owner.equals(Baron.UNCLAIMED))) return false;
        else {
            owner = claimant;
            return true;
        }
    }
}
