package student;

import model.*;

import java.util.Collection;
import java.util.LinkedList;

/**
 * Represents a Railroad Barons map (in the model).
 *
 * @author Matt Agger, mea5692@rit.edu
 * @author Zachary Talis, zft5955@rit.edu
 */
public class RailroadMapImpl implements RailroadMap {

    // The double array of all spaces on the map
    // IMPORTANT: SpaceImpl[row][column]
    private Space[][] spaces;

    // The LinkedList of all routes on the map
    private LinkedList<Route> routes;

    // The observers to this map
    private LinkedList<RailroadMapObserver> observers;

    /**
     * The class constructor.
     * @param spaces sets the double array of spaces
     * @param routes sets the LinkedList of routes
     */
    public RailroadMapImpl(Space[][] spaces, LinkedList<Route> routes) {
        this.spaces = spaces;
        this.routes = routes;
        observers = new LinkedList<>();
    }

    /**
     * Add the specified observer to the map.
     * @param observer The {@link RailroadMapObserver} being added to the map.
     */
    @Override
    public void addObserver(RailroadMapObserver observer) {
        observers.add(observer);
    }

    /**
     * Remove the specified observer from the map.
     * @param observer The observer to remove from the collection of
     *                 registered observers that will be notified of.
     */
    @Override
    public void removeObserver(RailroadMapObserver observer) {
        observers.remove(observer);
    }

    /**
     * Return the number of rows in the map.
     * @return the number of rows in the map.
     */
    @Override
    public int getRows() { return spaces.length; }

    /**
     * Return the number of columns in the map.
     * @return the number of columns in the map.
     */
    @Override
    public int getCols() {
        if (spaces.length == 0) return 0;
        else return spaces[0].length; }

    /**
     * Return the space located at the specified coordinates. Coordinates
     * start at (0,0).
     * @param row The row of the desired {@link Space}.
     * @param col The column of the desired {@link Space}.
     *
     * @return the Space at the specified location, or null if the location
     * doesn't exist on the map.
     */
    @Override
    public Space getSpace(int row, int col) {
        if (spaces.length >= row && spaces[0].length >= col) {
            return spaces[row][col];
        } return null;
    }

    /**
     * Return the route that contains the track at the specified location
     * (if such a route exists).
     * @param row The row of the location of one of the
     *            {@link model.Track tracks} in the route.
     * @param col The column of the location of one of the
     * {@link model.Track tracks} in the route.
     *
     * @return the Route that contains the Track at the specified location,
     * or null if there is no such Route.
     */
    @Override
    public Route getRoute(int row, int col) {
        for (Route route : routes) {
            if (route.includesCoordinate(spaces[row][col])) return route;
        } return null;
    }

    /**
     * Called to update the map when a Baron has claimed a route.
     * @param route The {@link Route} that has been claimed.
     */
    @Override
    public void routeClaimed(Route route) {
        for (RailroadMapObserver observer : observers) {
            observer.routeClaimed(this, route);
        }
    }

    /**
     * Return the length of the shortest unclaimed route in the map.
     * @return the length of the shortest unclaimed route.
     */
    @Override
    public int getLengthOfShortestUnclaimedRoute() {
        int shortestLength = getRows() + getCols();
        for (Route route : routes) {
            if (route.getLength() < shortestLength && route.getBaron() == Baron.UNCLAIMED)
                shortestLength = route.getLength();
        }
        return shortestLength;
    }

    /**
     * Return all of the Routes in this map.
     * @return a Collection of all of the Routes in this RailroadMap.
     */
    @Override
    public Collection<Route> getRoutes() { return routes; }
}
