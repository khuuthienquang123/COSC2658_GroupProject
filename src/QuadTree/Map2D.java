package QuadTree;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class Map2D {
    private Map<String, Map<Coordinate, Set<String>>> places;

    public Map2D() {
        places = new HashMap<>();
    }

    public void add(int x, int y, String[] services) {
        Coordinate coordinate = new Coordinate(x, y);
        for (String service : services) {
            places.computeIfAbsent(service, k -> new HashMap<>())
                    .computeIfAbsent(coordinate, k -> new HashSet<>())
                    .addAll(Set.of(services));
        }
    }

    public void edit(int x, int y, String[] newServices) {
        Coordinate coordinate = new Coordinate(x, y);
        for (Map.Entry<String, Map<Coordinate, Set<String>>> entry : places.entrySet()) {
            if (entry.getValue().containsKey(coordinate)) {
                entry.getValue().get(coordinate).clear();
                entry.getValue().get(coordinate).addAll(Set.of(newServices));
            }
        }
    }

    public void remove(int x, int y) {
        Coordinate coordinate = new Coordinate(x, y);
        for (Map.Entry<String, Map<Coordinate, Set<String>>> entry : places.entrySet()) {
            entry.getValue().remove(coordinate);
        }
    }

    public List<Place> search(int top_left_x, int top_left_y, int width, int height, String serviceType, int maxResults) {
        int center_x = top_left_x + width / 2;
        int center_y = top_left_y + height / 2;
        List<Place> results = new ArrayList<>();

        System.out.println("Search Center: (" + center_x + ", " + center_y + ")");
        places.getOrDefault(serviceType, Collections.emptyMap()).forEach((coord, services) -> {
            if (coord.x >= top_left_x && coord.x <= top_left_x + width && coord.y >= top_left_y && coord.y <= top_left_y + height) {
                double dist = Math.sqrt(Math.pow(center_x - coord.x, 2) + Math.pow(center_y - coord.y, 2));
                System.out.println("Checking: (" + coord.x + ", " + coord.y + ") - Distance: " + dist);
                results.add(new Place(dist, coord.x, coord.y, services));
            }
        });

        Collections.sort(results);
        return results.subList(0, Math.min(maxResults, results.size()));
    }

    public static void main(String[] args) {
        Map2D map = new Map2D();
        map.add(1200, 2200, new String[]{"coffee_shop", "restaurant"});
        map.add(1500, 2500, new String[]{"coffee_shop"});
        map.add(1600, 2600, new String[]{"coffee_shop"});
        map.add(3000, 4000, new String[]{"hospital"});

        List<Place> searchResults = map.search(750, 1750, 500, 500, "coffee_shop", 50);
        for (Place p : searchResults) {
            System.out.println(p);
        }
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

class Place implements Comparable<Place> {
    double distance;
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
