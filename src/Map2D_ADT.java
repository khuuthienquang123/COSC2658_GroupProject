import Map2D.Map2D;
import Map2D.Place;
import Map2D.QuadTree.QuadTree;

import java.util.List;
import java.util.Scanner;

public class Map2D_ADT {
    public static void main(String[] args){
        QuadTree tree = new QuadTree(0, 0, 10000000, 10000000);

        // Adding places with names and services
        Place place1 = new Place(9500000, 9750000, "High Point");
        place1.setServices(List.of("Coffee Shop", "ATM"));
        tree.add(place1);

        Place place2 = new Place(150, 250, "Seaside Restaurant");
        place2.setServices(List.of("Restaurant"));
        tree.add(place2);

        Place place3 = new Place(500, 1000, "Morning Brew");
        place3.setServices(List.of("Coffee Shop"));
        tree.add(place3);

        Place place4 = new Place(750, 850, "City Library");
        place4.setServices(List.of("Library"));
        tree.add(place4);

        Place place5 = new Place(2000, 3000, "General Hospital");
        place5.setServices(List.of("Hospital"));
        tree.add(place5);

        Place place6 = new Place(1100, 2200, "Sweet Treats");
        place6.setServices(List.of("Bakery", "Coffee Shop"));
        tree.add(place6);

        Place place7 = new Place(9000000, 8000000, "Valley School");
        place7.setServices(List.of("School"));
        tree.add(place7);

        Place place8 = new Place(2700, 3500, "Fitness Gym");
        place8.setServices(List.of("Gym"));
        tree.add(place8);


        Place place9 = new Place(4800, 2900, "Co-op mart");
        place9.setServices(List.of("Supermarket"));
        tree.add(place9);

        Place place10 = new Place(3900, 2700, "CGV");
        place10.setServices(List.of("Movie Theater"));
        tree.add(place10);

        // Edit services for a place
        tree.editPlace(place1, List.of("Bakery"));

        // Remove a place
        tree.removePlace(place2); // Assuming place at index 1 needs to be removed

        // Search for places offering a specific service within a bounding rectangle
        List<Place> searchResults = tree.search(0, 0, 10000000, 10000000, "School");

        // Display results
        int count = 0;
        for (Place p : searchResults){
            System.out.println("Place at (" + p.getX() + ", " + p.getY() + ") offers: " + String.join(", ", p.getServices()));
            count++;
            if(count == 50){
                break;
            }
        }

        /*       **************
                 *** MAP 2D ***
                 **************        */

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("-- WELCOME --");
            System.out.println("--------------------------------");
            System.out.println(" -- MENU -- ");
            System.out.println("1. Search in Bounded Area");
            System.out.println("2. Calculate Current Location");
            System.out.println("3. Add Place");
            System.out.println("4. Edit Place");
            System.out.println("5. Remove Place");
            System.out.println("6. Exit");
            System.out.print("\nPlease enter a number: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    Map2D.searchInBoundedArea();
                    break;
                case 2:
                    Map2D.currentLocationCalculate();
                    break;
                case 3:
                    Map2D.addPlace(tree);
                    break;
                case 4:
                    Map2D.editPlace();
                    break;
                case 5:
                    Map2D.removePlace();
                    break;
                case 6:
                    System.out.println("Exiting program.");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice, please try again.");
                    break;
            }
        }
    }
}

