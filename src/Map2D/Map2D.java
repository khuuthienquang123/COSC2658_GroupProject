package Map2D;

import Map2D.QuadTree.QuadTree;
import Map2D.QuadTree.QuadTreeNode;

import java.io.*;
import java.util.*;

public class Map2D {
    private static final File file = new File("src/Data/place.txt");

    public static void main(String[] args) {
        QuadTree tree = new QuadTree(0, 0, 10000000, 10000000);

        System.out.println("Checking file path: " + file.getAbsolutePath()); // Debug: Check file path

        try {
            Scanner scanner = new Scanner(file);
            System.out.println("File opened successfully.");  // Debug: File opened

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                System.out.println("info of the place: coordinate, name, service type: " + line);  // Debug: Output read line
                addPlaceFromLine(line, tree);
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
            return;
        }

        // Adding a new place based on user input
        Scanner userInput = new Scanner(System.in);
        System.out.println("Please enter the new place details:");
        System.out.print("Enter X coordinate: ");
        int x = userInput.nextInt();
        System.out.print("Enter Y coordinate: ");
        int y = userInput.nextInt();
        userInput.nextLine(); // Consume the newline left-over
        System.out.print("Enter place name: ");
        String name = userInput.nextLine();
        System.out.print("Enter service type: ");
        String service = userInput.nextLine();

        addPlaceToFile(x, y, name, service);
        addPlaceFromLine(x + ", " + y + ", " + name + ", " + service, tree);
        userInput.close();

        List<Place> allPlaces = tree.search(0, 0, 10000000, 10000000, null);
        if (allPlaces.isEmpty()) {
            System.out.println(" places loaded into the tree.");  // Debug: No data loaded
        } else {
            for (Place place : allPlaces) {
                System.out.println("Place: " + place.getName() + " at (" + place.getX() + ", " + place.getY() + ") offers: " + String.join(", ", place.getServices()));
            }
        }
    }

    private static void addPlaceFromLine(String line, QuadTree tree) {
        try {
            String[] parts = line.trim().split(",\\s*");
            if (parts.length < 4) {
                System.out.println("" + line);  // Debug: Incomplete line
                return;
            }

            int x = Integer.parseInt(parts[0]);
            int y = Integer.parseInt(parts[1]);
            String name = parts[2];
            String service = parts[3];

            Place place = new Place(x, y, name);
            place.setServices(List.of(service));
            tree.add(place);
            System.out.println("Added place: " + name);  // Debug: Confirm addition
        } catch (Exception e) {
            System.out.println(" " + line + "; " + e.getMessage());
        }
    }

    private static void addPlaceToFile(int x, int y, String name, String service) {
        String data = x + ", " + y + ", " + name + ", " + service + "\n";
        try (FileWriter fw = new FileWriter("src/Map2D/place.txt", true)) {
            fw.write(data);
            System.out.println("Successfully added place to file: " + name);
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }

    public static List<Place> getList(){
        List<Place> placeList = new ArrayList<>();

        /* READ THE TXT FILE */
        try{
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String currentLine;

            while ((currentLine = reader.readLine()) != null) { // Read until end of file
                String[] token = currentLine.split(",");
                if (token.length > 3) {
                    try{

                        int locationX = Integer.parseInt(token[0].trim());
                        int locationY = Integer.parseInt(token[1].trim());
                        String locationName = token[2].trim();

                        // Collect services
                        Set<String> services = new HashSet<>();
                        for (int i = 3; i < token.length; i++) {
                            services.add(token[i].trim());
                        }

                        // Create and add Place to the list
                        Place place = new Place(locationX, locationY, locationName);
                        place.addServices(services);
                        placeList.add(place);

                    }catch (NumberFormatException e) {
                        System.err.println("Skipping line due to parsing error: " + currentLine);
                    }
                }
            }
            reader.close();

        } catch (Exception e) {
                System.err.println("Error reading file: " + e.getMessage());
                throw new RuntimeException("Failed to read places from file", e); // Throw an exception to indicate failure
        }
        return placeList;
    }

    public static List<Place> searchLocation_service(String service) {
        List<Place> places = getList();

        List<Place> results = new ArrayList<>();

        for (Place place : places) {
            if (place.getServices().contains(service)) {
                results.add(place);
            }
        }
        return results;
    }

    private static double getMaxNum(List<Double> list){
        double max = 0;
        for(double num: list){
            if(num > max){
                max = num;
            }
        }
        return max;
    }

    private static double getMinNum(List<Double> list){
        double min = Integer.MAX_VALUE;
        for(double num: list){
            if(num < min){
                min = num;
            }
        }
        return min;
    }

    private static void displayPlaces(List<Place> places,int centerX, int centerY){
        System.out.println("*** LIST OF PLACES ***");
        for(Place place: places){
            //Calculate the distance
            int deltaX = centerX - place.getX();
            int deltaY = centerY - place.getY();
            double dist = Math.sqrt(deltaX * deltaX + deltaY * deltaY);

            //display
            System.out.println("Distance from " + place.getName() + " to your given location is: " + (int) dist + "m");
        }
    }

    private static void displayPlacesForCurrent(List<Place> places,int centerX, int centerY){
        System.out.println("*** LIST OF PLACES ***");
        for(Place place: places){
            //Calculate the distance
            int deltaX = centerX - place.getX();
            int deltaY = centerY - place.getY();
            double dist = Math.sqrt(deltaX * deltaX + deltaY * deltaY);

            //display
            System.out.println("Distance from your destination to " + place.getName() + "is: " + (int) dist + "m");
        }
    }

    private static List<Place> searchNearby(int centerX, int centerY, double distance, String service) {
        List<Place> places = getList();
        List<Place> results = new ArrayList<>();

        for (Place place : places) {
            if (place.getServices().contains(service)) {
                int deltaX = centerX - place.getX();
                int deltaY = centerY - place.getY();
                double dist = Math.sqrt(deltaX * deltaX + deltaY * deltaY);

                if (dist <= distance) {
                    results.add(place);
                }
            }
        }
        return results;
    }
    /*
           *** SEARCH PLACES IN BOUNDED AREA ***
                                                    */
    public static void searchInBoundedArea(){
        Scanner scanner = new Scanner(System.in);

        //User input
        System.out.print("Enter the type of service you want to search: ");
        String service = scanner.nextLine();
        System.out.print("Enter the location name: ");
        String location = scanner.nextLine();

        int locationX = 0, locationY = 0;
        String currentLine = "";

        /* READ THE TXT FILE */
        try{
            BufferedReader reader = new BufferedReader(new FileReader(file));

            while ((currentLine = reader.readLine() )!= null) {
                String[] token = currentLine.split(",");

                String locationName = token[2].trim();
                int x = Integer.parseInt(token[0].trim());
                int y = Integer.parseInt(token[1].trim());

                if (location.equals(locationName)) {
                    locationX = x;
                    locationY = y;
                }

            }
                //list for places with chosen service
                List<Place> places_service = searchLocation_service(service);

                //List to store the distance for comparison to get the max distance
                List<Double> listOfDistances = new ArrayList<>();

                for(Place place: places_service){
                    int deltaX = locationX - place.getX();
                    int deltaY = locationY - place.getY();
                    Double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);

                    //Add the distance to the list
                    listOfDistances.add(distance);
                }
                // Get maximum and minimum distances
                double maxDistance = getMaxNum(listOfDistances);
                System.out.println("\nThe furthest distance is: " + (int) maxDistance + "m");

                double minDistance = getMinNum(listOfDistances);
                 System.out.println("The closest distance is: " + (int) minDistance + "m\n");

                //get nearby places
                List<Place> nearbyPlaces = searchNearby(locationX, locationY, maxDistance, service);

                //display
                displayPlaces(nearbyPlaces, locationX, locationY);

            reader.close();
        } catch (Exception e) {
            System.err.println("Error parsing integers from line: " + currentLine);
        }
    }



    /*
         *** SEARCH PLACES IN BOUNDED AREA WITH CURRENT LOCATION ***
                                                                          */

    public static void currentLocationCalculate(){
        Scanner scanner = new Scanner(System.in);

        //User input
        System.out.print("Enter the type of services: ");
        String serviceType = scanner.nextLine();

        //Set the default coordinates for the current location
        int locationX = 10005000, locationY = 10005000;

        //list for places with chosen service
        List<Place> availablePlaces = searchLocation_service(serviceType);

        //List to store the distance for comparison to get the max distance
        List<Double> listOfDistances = new ArrayList<>();

        for(Place place: availablePlaces){
            int deltaX = locationX - place.getX();
            int deltaY = locationY - place.getY();
            Double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);

            //Add the distance to the list
            listOfDistances.add(distance);
        }

        // Get maximum and minimum distances
        double maxDistance = getMaxNum(listOfDistances);
        System.out.println("\nThe furthest distance is: " + (int) maxDistance + "m");

        double minDistance = getMinNum(listOfDistances);
        System.out.println("The closest distance is: " + (int) minDistance + "m\n");

        //get nearby places
        List<Place> nearbyPlaces = searchNearby(locationX, locationY, maxDistance, serviceType);

        //display
        displayPlacesForCurrent(nearbyPlaces, locationX, locationY);
    }
}

