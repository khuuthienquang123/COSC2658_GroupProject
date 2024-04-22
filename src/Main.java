import java.util.List;

public class Main {
    public static void main(String[] args){
        Map2D map = new Map2D();

        // Add places with their services
        map.add(new Place(1000, 2000, List.of("Restaurant", "ATM")));
        map.add(new Place(1500, 2500, List.of("Gas Station")));
        map.add(new Place(3000, 3000, List.of("Coffee Shop", "Restaurant")));

        // Edit services for a place
        map.edit(0, List.of("Restaurant")); // Assuming place at index 0 needs editing

        // Remove a place
        map.remove(1); // Assuming place at index 1 needs to be removed

        // Search for places offering a specific service within a bounding rectangle
        List<Place> searchResults = map.search(1000, 2000, 5000, 5000, "Restaurant");

        // Display results
        for (Place p : searchResults) {
            System.out.println("Place at (" + p.getX() + ", " + p.getY() + ") offers: " + String.join(", ", p.getServices()));
        }
    }
}
