package student;

import model.Station;

import java.util.LinkedList;
import java.util.List;

/**
 * Class representing a node in a graph.
 *
 * @author Matt Agger, mea5692@rit.edu
 * @author Zachary Talis, zft5955@rit.edu
 */
public class Node {

    /** The station associated with this node */
    private Station station;

    /** The neighbors of this node stored as a list of edges */
    private List<Edge> neighbors;

    /**
     * The constructor for this class.
     *
     * @param station the station associated with this node
     */
    public Node(Station station) {
        this.station = station;
        neighbors = new LinkedList<>();
    }

    /**
     * Get the station associated with this node.
     *
     * @return station
     */
    public Station getStation() {
        return station;
    }

    /**
     * Add a neighbor to this node.
     *
     * @param n the node to add as a neighbor
     */
    public void addNeighbor(Node n) {
        Edge e = new Edge(this, n);
        neighbors.add(e);
    }

    /**
     * Get a list of outgoing edges for this node.
     * @return a list of outgoing edges
     */
    public List<Edge> getEdges() {
        return new LinkedList<>(neighbors);
    }

}
