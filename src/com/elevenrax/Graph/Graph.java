package com.elevenrax.Graph;

import com.elevenrax.GraphElements.Arc;
import com.elevenrax.GraphElements.Node;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.util.*;

public class Graph {

    // Adjacency Map implementation
    private HashMap<Node, List<Arc>> mGraph;


    /**
     * Creates a graph from a File.
     * Supported schema of file:
     *
     *   node_id1
     *      or
     *   node_id1 node_id2 weight
     *
     * @param graphFile File pointing to a textfile containing OSM map data
     */
    public Graph(File graphFile) {

        mGraph = new HashMap<>();

        Scanner scanner = null;
        try {
            scanner = new Scanner(graphFile);
        }
        catch (FileNotFoundException ex) {
            ex.printStackTrace();
            System.exit(-1);
        }

        while (scanner.hasNext()) {
            String data = scanner.nextLine();
            String[] row = data.split("\\s+");

            if (row.length == 1) {
                makeNode( new BigInteger(row[0]) );
            }
            else {
                makeNodeWithArc(
                        new BigInteger(row[0]),
                        new BigInteger(row[1]),
                        Integer.valueOf(row[2])
                );
            }
        }

        scanner.close();
    }


    /**
     * For records read in with a single node, creates the node
     * @param id OSM Id for node to be created
     */
    private void makeNode(BigInteger id) {
        Node n = new Node(id);
        if (!mGraph.containsKey(n)) {
            mGraph.put(n, null);
        }
    }


    /**
     * Creates an arc from `id1` to `id2` with weight `weight`
     * Adds the Arc to the Adjacency Map.
     *
     * @param id1       From node
     * @param id2       To node
     * @param weight    Distance between From and To
     */
    private void makeNodeWithArc(BigInteger id1, BigInteger id2, int weight) {
        makeNode(id1);
        makeNode(id2);

        Node n1 = getNodeWithGivenId(id1);
        Node n2 = getNodeWithGivenId(id2);

        // A new obj but hashCode and equals overridden in Node class
        List<Arc> arcs = mGraph.get( n1 );

        if (arcs == null) {
            Arc newArc = new Arc(n2, weight);
            List<Arc> arcList = new ArrayList<>();
            arcList.add(newArc);
            mGraph.put(n1, arcList);
        }
        else {
            arcs.add( new Arc(n2, weight) );
        }
    }


    /**
     * Returns a node from the Adjacency Map for a given node id.
     * @param id The OSM node id for which you require the node
     * @return The target node
     */
    private Node getNodeWithGivenId(BigInteger id) {
        // TODO Slow and uncessesary: Store Nodes in HashMap <ID:int, Node> for O(1) retrieval
        Iterator<Node> nodeIter = mGraph.keySet().iterator();
        while (nodeIter.hasNext()) {
            Node n = nodeIter.next();
            if (n.getId().compareTo(id) == 0) {
                return n;
            }
        }
        return null;
    }


    /**
     * Finds the shortest path using Dijkstra's Algorithm.
     *
     * @param start     Where the journey begins
     * @param end       Where the journey ends
     * @return          The end node, which contains the length of the trip.
     *                    Null if no such path exists.
     */
    public Node findShortestPath(BigInteger start, BigInteger end) {

        List<Node> unsettled = new ArrayList<>();
        List<Node> settled = new ArrayList<>();

        Node startNode = getNodeWithGivenId(start);
        startNode.setAccumulatedMoveCost(0);

        unsettled.add(startNode);


        while ( !unsettled.isEmpty() ) {

            Node currentNode = getCheapestCandidateNode(unsettled);
            unsettled.remove(currentNode);

            // Calculate new distances to neighbours
            List<Arc> currentNodeNeighbours = mGraph.get(currentNode);

            if (currentNodeNeighbours != null) {
                for (Arc neighbour : currentNodeNeighbours) {

                    Node nextNodeCandidate = neighbour.getDestinationNode();

                    if (!settled.contains(nextNodeCandidate)) {
                        calculateMinDistance(
                                nextNodeCandidate,
                                neighbour.getWeight(),
                                currentNode
                        );
                        unsettled.add(nextNodeCandidate);
                    }

                }
            }
            settled.add(currentNode);
        }

        /*
            Returns null if there is no path to target from source
            In the remote chance that there's a path equal to Integer.MAX_VALUE;
        */
        Node n = getNodeWithGivenId(end);
        if (settled.contains(n) ) {
            return n;
        }
        return null;
    }


    /**
     * Calculates the aggregate cost of the next move where next move is cheaper.
     * Outside of Spec: Implemented List of moves for debugging purposes
     *
     * @param candidateNode Possible next move
     * @param arcWeight     Cost of the move from sourceNode to candidateNode
     * @param sourceNode    Node looking to mode away from
     */
    private void calculateMinDistance(Node candidateNode, int arcWeight, Node sourceNode) {
        int sourceDistance = sourceNode.getAccumulatedMoveCost();
        if ( sourceDistance + arcWeight < candidateNode.getAccumulatedMoveCost() ) {
            candidateNode.setAccumulatedMoveCost(sourceDistance + arcWeight);
            LinkedList<Node> shortestPath = new LinkedList<>(sourceNode.getShortestPath());
            shortestPath.add(sourceNode);
            candidateNode.setSortestPath(shortestPath);
        }
    }


    /**
     * Returns the cheapest (shortest distance) node to move to from the unsettled list.
     *
     * @param unsettled Nodes that are yet to be visited
     * @return The cheapest node.
     */
    private Node getCheapestCandidateNode(List<Node> unsettled) {

        Iterator<Node> unsettledIterator = unsettled.iterator();

        Node lowestDistanceNode = null;
        int cheapestNextMove = Integer.MAX_VALUE;

        while ( unsettledIterator.hasNext() ) {
            Node node = unsettledIterator.next();
            if (node.getAccumulatedMoveCost() < cheapestNextMove) {
                lowestDistanceNode = node;
                cheapestNextMove = node.getAccumulatedMoveCost();
            }
        }

        return lowestDistanceNode;
    }


    /**
     *  Handy to visualise the Adjacency Map Structure of mGraph
     */
    public void logGraph() {
        Iterator<Node> nodeIter = mGraph.keySet().iterator();
        while (nodeIter.hasNext()) {
            Node n = nodeIter.next();
            System.out.printf( "\nN: " + n.getId() + ":  \n" );

            List<Arc> arcsForNode = mGraph.get(n);
            if (arcsForNode != null) {
                System.out.printf( "-----neighbours-----\n" );
                for (int i = 0; i < arcsForNode.size(); i++) {
                    if (i < arcsForNode.size()-1) {
                        System.out.printf( arcsForNode.get(i).toString() + ", " );
                    }
                    else {
                        System.out.printf( arcsForNode.get(i).toString() + "\n\n" );
                    }
                }
            } else {
                System.out.printf("----empty-----\n");
            }
        }
    }


    /**
     *  Used for test cases to sanitise graph for new search.
     */
    public void prepareGraphForNewSearch() {
        Iterator<Node> nodeIterator = mGraph.keySet().iterator();
        while( nodeIterator.hasNext() ) {
            Node node = nodeIterator.next();
            node.resetNode();
        }
    }

}
