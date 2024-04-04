package com.elevenrax;

import com.elevenrax.Graph.Graph;
import com.elevenrax.GraphElements.Node;

import java.io.File;
import java.math.BigInteger;


public class Main {

    public static void main(String[] args) {

        // Run the tests paths
        // Uncomment out CMD line start to build CMD line app
        testPaths();

        // CMD line start
//        File readFile = new File(args[0]);
//        BigInteger start = new BigInteger(args[1]);
//        BigInteger end = new BigInteger(args[2]);
//
//        Graph g = new Graph(readFile);
//        Node goal = g.findShortestPath(start, end);
//
//        printResult(goal);

    }


    private static void printResult(Node goal) {
        if (goal != null) {
            System.out.println(goal.getAccumulatedMoveCost());
            System.out.println(goal.getShortestPathToString());
        }
        else {
            System.out.println("No Path");
        }
    }


    private static void testPaths() {

        File readFile = new File("./Data/osm-test-graph.dat");
        Graph g = new Graph(readFile);

        // Test case 1
        // Expected: No Path
        System.out.println("Test Case 1");
        BigInteger start = new BigInteger("316319897");
        BigInteger end = new BigInteger("313148296");

        Node goal = g.findShortestPath(start, end);
        printResult(goal);
        g.prepareGraphForNewSearch();

        // Test case 2
        // Expected: 314180912 -> 314180913 -> 317370158 -> 317370159 -> 317370160 -> 317370161 -> 317370161 -> 317370212 -> 317370213 -> 317370213
        // Expected: 159
        System.out.println("Test Case 2");
        start = new BigInteger("314180912");
        end = new BigInteger("317370213");

        goal = g.findShortestPath(start, end);
        printResult(goal);
        g.prepareGraphForNewSearch();


        // Test case 3
        // 316319897 -> 316319936
        // Expected: 121
        g = new Graph(readFile);
        System.out.println("Test Case 3");
        start = new BigInteger("316319897");
        end = new BigInteger("316319936");

        goal = g.findShortestPath(start, end);
        printResult(goal);
        g.prepareGraphForNewSearch();


        // Test case 4
        // Expected: No Path
        System.out.println("Test Case 4");
        start = new BigInteger("319301618");
        end = new BigInteger("35998283");

        goal = g.findShortestPath(start, end);
        printResult(goal);
        g.prepareGraphForNewSearch();


        // Test case 5
        // Expected: No Path
        System.out.println("Test Case 5");
        start = new BigInteger("319301625");
        end = new BigInteger("1110522663");

        goal = g.findShortestPath(start, end);
        printResult(goal);
        g.prepareGraphForNewSearch();


        // Test case 6
        // Expected: No Path
        System.out.println("Test Case 6");
        start = new BigInteger("1570192693");
        end = new BigInteger("2702991700");

        goal = g.findShortestPath(start, end);
        printResult(goal);
        g.prepareGraphForNewSearch();


        // Test case 7
        // 313983590 -> 313983591 -> 313983592 -> 313983593 -> 429641745 -> 429641746
        // Expected: 164
        System.out.println("Test Case 7");
        start = new BigInteger("313983590");
        end = new BigInteger("429641746");

        goal = g.findShortestPath(start, end);
        printResult(goal);
        g.prepareGraphForNewSearch();
    }

}
