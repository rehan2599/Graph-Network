import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Graph {
    private Map<String, List<Edge>> adjacencyList = new HashMap<>();

    public static class Edge {
        String source;
        String vertex;
        double weight;

        public Edge(String source, String vertex, double weight) {
            this.source = source;
            this.vertex = vertex;
            this.weight = weight;
        }

        @Override
        public String toString() {
            return "Edge from " + source + " to " + vertex + " with weight " + weight;
        }
    }

    public void addVertex(String vertex) {
        adjacencyList.putIfAbsent(vertex, new ArrayList<>());
    }

    public void addEdge(String source, String destination, double weight) {
        this.addVertex(source);
        this.addVertex(destination);
        adjacencyList.get(source).add(new Edge(source, destination, weight));

    }

    public List<Edge> getEdges(String vertex) {
        return adjacencyList.getOrDefault(vertex, new ArrayList<>());
    }

    public List<Edge> getAllEdges() {
        List<Edge> allEdges = new ArrayList<>();
        for (List<Edge> edges : adjacencyList.values()) {
            for (Edge edge : edges) {
                if (!allEdges.contains(edge)) {
                    allEdges.add(edge);
                }
            }
        }
        return allEdges;
    }

    public boolean containsVertex(String vertex) {
        return adjacencyList.containsKey(vertex);
    }


    public List<Edge> getAdjacentVertices(String vertex) {
        return new ArrayList<>(adjacencyList.getOrDefault(vertex, new ArrayList<>()));
    }


    public Set<String> getAllVertices() {
        return adjacencyList.keySet();
    }

    public void loadFromCSV(String filePath) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy H:mm");
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] parts = line.trim().split(",");
                if (parts.length >= 5) {
                    String origin = parts[0].trim();
                    String destination = parts[1].trim();
                    LocalDateTime departureTime = LocalDateTime.parse(parts[3].trim(), formatter);
                    LocalDateTime arrivalTime = LocalDateTime.parse(parts[4].trim(), formatter);
                    long durationMinutes = java.time.Duration.between(departureTime, arrivalTime).toMinutes();
                    double weight = durationMinutes / 60.0;
                    addEdge(origin, destination, weight);
                }
            }
        } catch (IOException e) {
            System.err.println("An error occurred while reading the CSV file: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
