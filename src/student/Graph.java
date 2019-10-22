package student;

import model.Route;
import model.Station;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Holds representation of a graph as well as functions to interact with the
 * graph.
 *
 * @author Matt Agger, mea5692@rit.edu
 * @author Zachary Talis, zft5955@rit.edu
 */
public class Graph {

    /** A map used to represent the graph */
    private Map<Station, Node> graph;

    /**
     * The constructor for this class.
     */
    public Graph() {
        graph = new HashMap<>();
    }

    /**
     * Add the parts of a route to the graph.
     * @param route the route being used
     */
    public void addRoute(Route route) {
        if(!isInGraph(route.getOrigin()))
            graph.put(route.getOrigin(), new Node(route.getOrigin()));
        if(!isInGraph(route.getDestination()))
            graph.put(route.getDestination(), new Node(route.getDestination()));
        graph.get(route.getOrigin()).addNeighbor(
                graph.get(route.getDestination()));
        graph.get(route.getDestination()).addNeighbor(
                graph.get(route.getOrigin()));
    }

    /**
     * Method to check if the given station node is in the graph.
     *
     * @param station the station of a node
     * @return whether the given station node is in the graph
     */
    public boolean isInGraph(Station station) {
        return graph.containsKey(station);
    }

    /**
     * Method to compute the shortest path in a graph from a start node to a
     * finish node.
     * @param start the station of the start node
     * @param finish the station of the end node
     * @return the shortest path
     */
    public int getShortestPath(Station start, Station finish) {
        Node startNode, finishNode;
        startNode = graph.get(start);
        finishNode = graph.get(finish);
        Map<Node, Integer> distance = new HashMap<>();
        Map<Node, Node> predecessors = new HashMap<>();
        dijkstra(startNode, distance, predecessors);
        if(distance.get(finishNode) == Integer.MAX_VALUE)
            return -1;
        else {
            return distance.get(finishNode);
        }
    }

    /**
     * Method to compute the shortest path to all other nodes.
     * @param startNode the starting node
     * @param distance the map holding the minimum distance to any node
     * @param predecessors the map holding predecessors along any shortest path
     */
    private void dijkstra(Node startNode, Map<Node, Integer> distance,
                          Map<Node, Node> predecessors) {
        for(Station station : graph.keySet())
            distance.put(graph.get(station), Integer.MAX_VALUE);
        distance.put(startNode, 0);
        predecessors.put(startNode, startNode);
        List<Node> priorityQ = new LinkedList<>();
        for(Station station : graph.keySet())
            priorityQ.add(graph.get(station));
        while(!priorityQ.isEmpty()) {
            Node U = dequeueMin(priorityQ, distance);
            if(distance.get(U) == Integer.MAX_VALUE)
                return;
            for(Edge e : U.getEdges()) {
                Integer w = e.getWeight();
                Node n = e.getToNode();
                Integer distViaU = distance.get(U) + w;
                if(distance.get(n) > distViaU) {
                    distance.put(n, distViaU);
                    predecessors.put(n,  U);
                }
            }
        }
    }

    /**
     * Basic implementation of a priority queue that searches for the minimum.
     */
    private Node dequeueMin(List<Node> priorityQ, Map<Node, Integer> distance) {
        Node minNode = priorityQ.get(0);
        for(Node n : priorityQ) {
            if(distance.get(n) < distance.get(minNode))
                minNode = n;
        }
        return priorityQ.remove(priorityQ.indexOf(minNode));
    }

}
