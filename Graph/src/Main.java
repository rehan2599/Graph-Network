import java.util.Map;
import java.util.*;

public class Main {
    private static final double INF = 1e7;

    public static void main(String[] args) {

        Graph graph = new Graph();
        graph.loadFromCSV("data/flight.csv");


        System.out.println("Dijkstra's Algorithm Results:");
        printDijkstrasResult(graph, "JFK");

        System.out.println("\nBreadth-First Search Results:");
        if (graph.containsVertex("JFK")) {
            BFS.bfs(graph, "JFK");
        } else {
            System.out.println("Vertex 'JFK' not found in the graph.");
        }

        System.out.println("Floyd Warshall Algorithm Results:");

        // Map each airport to a unique integer id
        Map<String, Integer> airportIds = new HashMap<>();
        Map<Integer, String> indexToAirport = new HashMap<>();
        int id = 0;
        for (String airport : graph.getAllVertices()) {
            airportIds.put(airport, id);
            indexToAirport.put(id, airport);
            id++;
        }

        // Initialize the matrix
        double[][] distances = new double[airportIds.size()][airportIds.size()];
        for (double[] row : distances) {
            Arrays.fill(row, INF);
        }
        for (String airport : airportIds.keySet()) {
            int airportId = airportIds.get(airport);
            distances[airportId][airportId] = 0; // Distance to itself is 0
        }

        // Fill the matrix with the known edge weights
        for (Graph.Edge edge : graph.getAllEdges()) {
            int fromId = airportIds.get(edge.source);
            int toId = airportIds.get(edge.vertex);
            distances[fromId][toId] = edge.weight; // Direct flight distance
        }

        // Print initial matrix
        System.out.println("Initial Distance Matrix:");
        printMatrix(distances, indexToAirport);

        // Floyd-Warshall algorithm
        for (int k = 0; k < airportIds.size(); k++) {
            for (int i = 0; i < airportIds.size(); i++) {
                for (int j = 0; j < airportIds.size(); j++) {
                    if (distances[i][k] + distances[k][j] < distances[i][j]) {
                        distances[i][j] = distances[i][k] + distances[k][j];
                    }
                }
            }
        }

        // Print final matrix
        System.out.println("Final Distance Matrix after applying Floyd-Warshall:");
        printMatrix(distances, indexToAirport);

    }

    private static void printDijkstrasResult(Graph graph, String start) {
        if (graph.containsVertex(start)) {
            Map<String, Double> distances = DijkstraAlgorithm.dijkstra(graph, start);
            System.out.println("Shortest travel times from " + start + " to all reachable airports:");
            distances.forEach((destination, distance) -> {
                if (!destination.equals(start)) {
                    System.out.printf("- From %s to %s: %.2f hours%n", start, destination, distance);
                }
            });
        } else {
            System.out.println("Vertex '" + start + "' not found in the graph.");
        }
    }

    private static void printMatrix(double[][] matrix, Map<Integer, String> indexToAirport) {
        System.out.print("     ");
        for (int i = 0; i < matrix.length; i++) {
            System.out.printf("%5s ", indexToAirport.get(i));
        }
        System.out.println();

        for (int i = 0; i < matrix.length; i++) {
            System.out.printf("%5s ", indexToAirport.get(i));
            for (int j = 0; j < matrix.length; j++) {
                if (matrix[i][j] == INF) {
                    System.out.print(" INF ");
                } else {
                    System.out.printf("%5.2f ", matrix[i][j]);
                }
            }
            System.out.println();
        }
    }
}
