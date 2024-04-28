import java.util.*;

public class BFS {

    public static void bfs(Graph graph, String start) {
        Queue<String> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();

        queue.add(start);
        visited.add(start);

        while (!queue.isEmpty()) {
            String current = queue.poll();
            System.out.println("Visited: " + current);
            for (Graph.Edge neighbor : graph.getEdges(current)) {
                if (!visited.contains(neighbor.vertex)) {
                    visited.add(neighbor.vertex);
                    queue.add(neighbor.vertex);
                }
            }
        }
    }
}
