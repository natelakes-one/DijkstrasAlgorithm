package com.elevenrax.GraphElements;


import com.elevenrax.Graph.Graph;

public class Arc {

    private Node mNext;
    private int mWeight;


    public Arc(Node next, int weight) {
        mNext = next;
        mWeight = weight;
    }

    public Node getDestinationNode() {
        return mNext;
    }


    public int getWeight() {
        return mWeight;
    }


    public void setWeight(int weight) {
        mWeight = weight;
    }


    @Override
    public String toString() {
        return "[" + mNext.getId() + "]" + " w(" + mWeight + ") ";
    }

}