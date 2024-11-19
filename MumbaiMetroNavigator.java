import java.util.*;

class MumbaiMetroNavigator {
   
    private static final String[] STATIONS = {
        "Versova", "DN Nagar", "Azad Nagar", "Andheri", "Western Express Highway",
        "Chakala", "Marol Naka", "Airport Road", "Saki Naka", "Asalpha",
        "Jagruti Nagar", "Ghatkopar", "Powai", "Vikhroli", "Thane"
    };

    private static final int NUM_STATIONS = STATIONS.length;

    // Distance matrix (in kilometers)
    private static final int[][] DISTANCES = {
        {0, 2, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {2, 0, 1, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {3, 1, 0, 2, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 3, 2, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 4, 1, 0, 2, 3, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 2, 0, 1, 2, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 3, 1, 0, 2, 4, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 2, 2, 0, 3, 1, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 4, 3, 0, 1, 2, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 2, 3, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 2, 2, 0, 1, 4, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 1, 0, 2, 3, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4, 2, 0, 1, 2},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 1, 0, 3},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 3, 0}
    };

   
    private static final int COST_PER_KM = 10;
    private static final int TIME_PER_KM = 2;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Mumbai Metro Navigator");
        System.out.println("Available Stations:");
        for (int i = 0; i < NUM_STATIONS; i++) {
            System.out.println((i + 1) + ". " + STATIONS[i]);
        }
        System.out.print("Enter the source station number: ");
        int source = scanner.nextInt() - 1;

        System.out.print("Enter the destination station number: ");
        int destination = scanner.nextInt() - 1;

        if (source < 0 || source >= NUM_STATIONS || destination < 0 || destination >= NUM_STATIONS) {
            System.out.println("Invalid station selection. Please try again.");
            return;
        }
        DijkstraResult result = dijkstra(source, destination);
        System.out.println("\nShortest Path: " + String.join(" -> ", result.path));
        System.out.println("Shortest Distance: " + result.distance + " km");
        System.out.println("Total Cost: ₹" + result.distance * COST_PER_KM);
        System.out.println("Estimated Time: " + result.distance * TIME_PER_KM + " minutes");

        scanner.close();
    }
    private static DijkstraResult dijkstra(int src, int dest) {
        int[] distances = new int[NUM_STATIONS];
        boolean[] visited = new boolean[NUM_STATIONS];
        int[] previous = new int[NUM_STATIONS];

        Arrays.fill(distances, Integer.MAX_VALUE);
        Arrays.fill(previous, -1);

        distances[src] = 0;

        for (int i = 0; i < NUM_STATIONS - 1; i++) {
            int u = findMinDistance(distances, visited);
            visited[u] = true;

            for (int v = 0; v < NUM_STATIONS; v++) {
                if (!visited[v] && DISTANCES[u][v] != 0 && distances[u] != Integer.MAX_VALUE
                        && distances[u] + DISTANCES[u][v] < distances[v]) {
                    distances[v] = distances[u] + DISTANCES[u][v];
                    previous[v] = u;
                }
            }
        }

        return buildResult(src, dest, distances, previous);
    }
    private static int findMinDistance(int[] distances, boolean[] visited) {
        int min = Integer.MAX_VALUE, minIndex = -1;

        for (int v = 0; v < NUM_STATIONS; v++) {
            if (!visited[v] && distances[v] <= min) {
                min = distances[v];
                minIndex = v;
            }
        }

        return minIndex;
    }
    private static DijkstraResult buildResult(int src, int dest, int[] distances, int[] previous) {
        List<String> path = new ArrayList<>();
        int current = dest;

        while (current != -1) {
            path.add(0, STATIONS[current]);
            current = previous[current];
        }

        return new DijkstraResult(path, distances[dest]);
    }
    private static class DijkstraResult {
        List<String> path;
        int distance;

        DijkstraResult(List<String> path, int distance) {
            this.path = path;
            this.distance = distance;
        }
    }
}
