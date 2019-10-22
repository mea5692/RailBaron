package student;

/**
 * Class representing an edge in a graph.
 *
 * @author Matt Agger, mea5692@rit.edu
 * @author Zachary Talis, zft5955@rit.edu
 */
public class Edge {

    /** The source node of the directed edge */
    private Node fromNode;

    /** The destination node of the directed edge */
    private Node toNode;

    /** The weight of the directed edge */
    private int weight;

    /**
     * The constructor for this class.
     *
     * @param from the source node of the directed edge
     * @param to the destination node of the directed edge
     */
    public Edge(Node from, Node to) {
        fromNode = from;
        toNode = to;
        weight = 1;
    }

    /**
     * Get the source node of the directed edge.
     *
     * @return the source node of the directed edge
     */
    public Node getFromNode() {
        return fromNode;
    }

    /**
     * Get the destination node of the directed edge.
     *
     * @return the destination node of the directed edge
     */
    public Node getToNode() {
        return toNode;
    }

    /**
     * Get the weight of the directed edge.
     *
     * @return the weight of the directed edge
     */
    public int getWeight() {
        return weight;
    }

}
