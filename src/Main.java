import Place.Place;
import QuadTree.QuadTree;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class Main {
    public static void main(String[] args){
        QuadTree tree = new QuadTree(0, 0, 10000000, 10000000);

        // Add places with their services
        Place place1 = new Place(9500000, 9750000);
        place1.setServices(Arrays.asList("Coffee Shop", "ATM"));
        tree.add(place1);

        Place place2 = new Place(150, 250);
        place2.setServices(Arrays.asList("Restaurant"));
        tree.add(place2);
        // Edit services for a place
        tree.editPlace(place1, Arrays.asList("Bakery", "WiFi"));

        // Remove a place
        tree.removePlace(place2); // Assuming place at index 1 needs to be removed

        // Search for places offering a specific service within a bounding rectangle
        List<Place> searchResults = tree.search(900000, 800000, 9000000, 9000000, "Bakery");

        // Display results
        int count = 0;
        for (Place p : searchResults){
            System.out.println("Place at (" + p.getX() + ", " + p.getY() + ") offers: " + String.join(", ", p.getServices()));
            count++;
            if(count == 50){
                break;
            }
        }
    }
}
