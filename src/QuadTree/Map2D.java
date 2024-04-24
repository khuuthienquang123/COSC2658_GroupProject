package QuadTree;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Map2D {
    // A map from service types to another map, which maps coordinates to a set of services.
    private Map<String, Map<Coordinate, Set<String>>> places;

    public Map2D() {
        places = new HashMap<>();
    }

    // Adds services at a given coordinate.
    public void add(int x, int y, String[] services) {
        Coordinate coordinate = new Coordinate(x, y);
        for (String service : services) {
            places.computeIfAbsent(service, k -> new HashMap<>())
                    .computeIfAbsent(coordinate, k -> new HashSet<>())
                    .addAll(Set.of(services));
        }
    }

    // Edits the services at a given coordinate.
    public void edit(int x, int y, String[] newServices) {
        Coordinate coordinate = new Coordinate(x, y);
        for (Map.Entry<String, Map<Coordinate, Set<String>>> entry : places.entrySet()) {
            if (entry.getValue().containsKey(coordinate)) {
                entry.getValue().get(coordinate).clear();
                entry.getValue().get(coordinate).addAll(Set.of(newServices));
            }
        }
    }

    // Removes all services from a given coordinate.
    public void remove(int x, int y) {
        Coordinate coordinate = new Coordinate(x, y);
        for (Map.Entry<String, Map<Coordinate, Set<String>>> entry : places.entrySet()) {
            entry.getValue().remove(coordinate);
        }
    }

    // Searches for places within a specified rectangle area and returns a sorted list of places.
    public List<Place> search(int top_left_x, int top_left_y, int width, int height, String serviceType, int maxResults) {
        int center_x = top_left_x + width / 2;
        int center_y = top_left_y + height / 2;
        List<Place> results = new ArrayList<>();

        places.getOrDefault(serviceType, Collections.emptyMap()).forEach((coord, services) -> {
            if (coord.x >= top_left_x && coord.x <= top_left_x + width && coord.y >= top_left_y && coord.y <= top_left_y + height) {
                double dist = Math.sqrt(Math.pow(center_x - coord.x, 2) + Math.pow(center_y - coord.y, 2));
                results.add(new Place(dist, coord.x, coord.y, services));
            }
        });

        Collections.sort(results);
        return results.subList(0, Math.min(maxResults, results.size()));
    }

    public static void main(String[] args) {
        Map2D map = new Map2D();
        // Sample data for testing
        map.add(1200, 2200, new String[]{"coffee_shop", "restaurant"});
        map.add(1500, 2500, new String[]{"coffee_shop"});
        map.add(1600, 2600, new String[]{"coffee_shop"});
        map.add(3000, 4000, new String[]{"hospital"});
        map.add(2500, 3300, new String[]{"bakery", "coffee_shop"});
        map.add(1800, 2400, new String[]{"library"});
        map.add(10000000, 10000000, new String[]{"shopping_mall"});
        map.add(10000000, 10005000, new String[]{"university"});
        map.add(10005000, 10000000, new String[]{"supermarket"});
        map.add(10100000, 10100000, new String[]{"stadium"});
        map.add(10200000, 10200000, new String[]{"movie_theater"});
        map.add(10300000, 10300000, new String[]{"museum"});

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the service type you want to search for (e.g., coffee_shop):");
        String serviceType = scanner.nextLine();
        System.out.println("Enter the top-left X coordinate of your search area:");
        int tlx = scanner.nextInt();
        System.out.println("Enter the top-left Y coordinate of your search area:");
        int tly = scanner.nextInt();
        System.out.println("Enter the width of your search area:");
        int width = scanner.nextInt();
        System.out.println("Enter the height of your search area:");
        int height = scanner.nextInt();
        System.out.println("Enter the maximum number of results you want:");
        int maxResults = scanner.nextInt();

        List<Place> searchResults = map.search(tlx, tly, width, height, serviceType, maxResults);
        for (Place p : searchResults) {
            System.out.println(p);
        }

        scanner.close();
    }
}

class Coordinate {
    int x, y;

    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public int hashCode() {
        return 31 * x + y;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Coordinate that = (Coordinate) obj;
        return x == that.x && y == that.y;
    }
}

class Place implements Comparable<Place> { public double distance;
    int x, y;
    Set<String> services;

    public Place(double distance, int x, int y, Set<String> services) {
        this.distance = distance;
        this.x = x;
        this.y = y;
        this.services = services;
    }

    @Override
    public int compareTo(Place other) {
        return Double.compare(this.distance, other.distance);
    }

    @Override
    public String toString() {
        return "Distance: " + String.format("%.2f", distance) + ", Location: (" + x + ", " + y + "), Services: " + services;
    }
}
