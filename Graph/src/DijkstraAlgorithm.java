import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class DijkstraAlgorithm {

    public static Map<String, Double> dijkstra(Graph graph, String start) {
        Map<String, Double> distances = new HashMap<>();
        PriorityQueue<Graph.Edge> pq = new PriorityQueue<>((a, b) -> Double.compare(a.weight, b.weight));


        for (String vertex : graph.getAllVertices()) {
            distances.put(vertex, Double.MAX_VALUE);
        }
        distances.put(start, 0.0);
        pq.add(new Graph.Edge(start, start, 0.0));

        while (!pq.isEmpty()) {
            Graph.Edge smallest = pq.poll();
            for (Graph.Edge neighbor : graph.getAdjacentVertices(smallest.vertex)) {
                double alt = distances.get(smallest.vertex) + neighbor.weight;
                if (alt < distances.get(neighbor.vertex)) {
                    distances.put(neighbor.vertex, alt);
                    pq.add(new Graph.Edge(smallest.vertex, neighbor.vertex, alt));
                }
            }
        }

        return distances;
    }
}
