package com.elevenrax.GraphElements;


import java.math.BigInteger;
import java.util.LinkedList;


public class Node {

    private BigInteger mID;

    // To handle finding ShortestPath
    private int mAccumulatedMoveCost;
    private LinkedList<Node> mShortestPath;


    public Node(BigInteger id) {
        mID = id;
        mAccumulatedMoveCost = Integer.MAX_VALUE;
        mShortestPath = new LinkedList<>();
    }


    public BigInteger getId() {
        return this.mID;
    }


    public void setAccumulatedMoveCost(int cost) {
        mAccumulatedMoveCost = cost;
    }


    public int getAccumulatedMoveCost() {
        return mAccumulatedMoveCost;
    }


    public LinkedList<Node> getShortestPath() {
        return mShortestPath;
    }


    public void setSortestPath(LinkedList<Node> shortestPath) {
        mShortestPath = shortestPath;
    }


    public String getShortestPathToString() {

        LinkedList<Node> nodesInPath = new LinkedList<>();
        for (Node n : mShortestPath) {
            Node newNode = ((Node) n.clone());
            nodesInPath.add(newNode);
        }
        nodesInPath.add(this);

        String path = "";
        for (int i = 0; i < nodesInPath.size(); i++) {
            Node n = nodesInPath.get(i);
            if (i < nodesInPath.size() - 1) {
                path += n.getId() + " -> ";
            }
            else {
                path += n.getId();
            }
        }
        return path;
    }


    /*
        These two overriden methods ensure the integer ID of an OSM node is the identifier.
        Thus giving O(1) search in the Graph (for adjacent nodes) and preventing the insertion of
        two identical nodes with the same ID (but different system generated default hashCodes).
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Node && obj != null) {
            Node other = (Node) obj;
            return this.getId().equals(other.getId());
        }
        return false;
    }


    @Override
    public int hashCode() {
        return this.toString().hashCode();
    }


    @Override
    public String toString() {
        return "Node: " + String.valueOf(this.getId());
    }


    @Override
    protected Object clone() {
        return new Node(this.getId());
    }

    public void resetNode() {
        mAccumulatedMoveCost = Integer.MAX_VALUE;
        mShortestPath = new LinkedList<>();
    }
}
