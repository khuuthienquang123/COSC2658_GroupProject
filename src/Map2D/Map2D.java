package Map2D;

import Map2D.QuadTree.QuadTree;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.*;

public class Map2D {
    private static final File file = new File("src/Data/place.txt");

    /*
     *** ADD NEW PLACE ***
                             */
    public static void addPlace(QuadTree tree){
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
                System.out.println(line);  // Debug: Incomplete line
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
        try (FileWriter fw = new FileWriter(file, true)) {
            fw.write(data);
            System.out.println("Successfully added place to file: " + name);
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }

    /*
     *** EDIT PLACE ***
                             */

    public static void editPlace() {
        System.out.println("\n*** EDIT PLACE ***");

        //Set path for temp file
        File temp = new File("src/Data/temp.txt");
        Scanner scanner = new Scanner(System.in);

        //User input
        System.out.print("Enter name of the place to edit: ");
        String placeName = scanner.nextLine();

        try {
            //call reader, writer
            BufferedReader reader = new BufferedReader(new FileReader(file));
            BufferedWriter writer = new BufferedWriter(new FileWriter(temp));

            String currentLine;

            while ((currentLine = reader.readLine()) != null) {
                String[] token = currentLine.split(",");
                //parsing X
                String locationX = token[0].trim();
                //Parsing Y
                String locationY = token[1].trim();
                //parsing name
                String name = token[2].trim();
                // parsing services
                Set<String> services = new HashSet<>();

                //check if the name is input correctly
                if(name.equals(placeName)){
                    System.out.println("\n--UPDATE PLACE INFORMATION--");

                    System.out.print("Please enter the number of services: ");
                    int servicesNum = scanner.nextInt();
                    scanner.nextLine();

                    int counter = 0;
                    if(servicesNum <= 0) {
                        System.out.println("Invalid number of services");
                        return;
                    }else {
                        while(counter < servicesNum){
                            System.out.print("Enter new service " + (counter+1) + ": ");
                            String newService = scanner.nextLine();
                            services.add(newService);
                            counter++;
                        }
                    }
                    writer.write(locationX + ", " + locationY + ", " + name + ", ");

                    int check = 0;
                    for(String service : services) {
                        if(check < services.size() - 1) {
                            writer.write(service + ", ");
                        } else {
                            writer.write(service); // Last service, no comma after
                        }
                        check++;
                    }
                }else {
                    writer.write(currentLine + System.lineSeparator());
                }
            }
            writer.close();
            reader.close();

            //Replace temp file to the existing file
            Files.move(temp.toPath(), file.toPath(), StandardCopyOption.REPLACE_EXISTING);

        } catch (Exception e) {
            System.err.println("Error processing the file: " + e.getMessage());
            throw new RuntimeException("Failed to edit places in the file", e);
        }

        System.out.println("Update successfully!\n");
    }

    /*
     *** REMOVE PLACE ***
     */
    public static void removePlace(){
        System.out.println("\n*** REMOVE PLACE ***");

        List<Place> check = getList();

        //Set path for temp file
        File temp = new File("src/Data/tempRemove.txt");
        Scanner scannerRemove = new Scanner(System.in);

        //User input
        System.out.print("Enter name of the place to remove: ");
        String placeRemove = scannerRemove.nextLine();

        try {
            //call reader, writer
            BufferedReader reader = new BufferedReader(new FileReader(file));
            BufferedWriter writer = new BufferedWriter(new FileWriter(temp));

            String currentLine, lineToRemove = null;

            if(!checkPlaceName(placeRemove)){
                System.out.println("Invalid name!");
                return;
            }

            for(Place place : check) {
                if(place.getName().equals(placeRemove)){
                    lineToRemove = place.getX() + ", " +
                                  place.getY() + ", " +
                                  place.getName() + ", " +
                                  String.join("," ,place.getServices());

                }
            }

            while ((currentLine = reader.readLine()) != null) {
                if(currentLine.equals(lineToRemove)){
                    continue;
                }
                writer.write(currentLine + System.lineSeparator());
            }
            writer.close();
            reader.close();

            //Replace temp file to the existing file
            Files.move(temp.toPath(), file.toPath(), StandardCopyOption.REPLACE_EXISTING);

        } catch (Exception e) {
            System.err.println("Error processing the file: " + e.getMessage());
            throw new RuntimeException("Failed to edit places in the file", e);
        }

        System.out.println("Successfully remove place!\n");
    }

    public static String getServices(){
        Map<Integer, String> serviceMap = new HashMap<>();

        Scanner scanner = new Scanner(System.in);

        System.out.println("\n-- LIST OF SERVICES --");
        System.out.println("1. Movie Theater");
        System.out.println("2. Park");
        System.out.println("3. Library");
        System.out.println("4. Restaurant");
        System.out.println("5. Cafe");
        System.out.println("6. Entertainment");
        System.out.println("7. Gym");
        System.out.println("8. Event Venue");
        System.out.println("9. Zoo");
        System.out.println("10. Bar");
        System.out.println("11. Shopping Mall");
        System.out.println("12. Tourist Attraction");
        System.out.println("13. Market");
        System.out.println("14. Historic Site");
        System.out.println("15. Museum");

        System.out.print("\nEnter the number of the service: ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        // Inserting pairs in the Map
        // using put() method
        serviceMap.put(1, "Movie Theater");
        serviceMap.put(2, "Park");
        serviceMap.put(3, "Library");
        serviceMap.put(4, "Restaurant");
        serviceMap.put(5, "Cafe");
        serviceMap.put(6, "Entertainment");
        serviceMap.put(7, "Gym");
        serviceMap.put(8, "Event Venue");
        serviceMap.put(9, "Zoo");
        serviceMap.put(10, "Bar");
        serviceMap.put(11, "Shopping Mall");
        serviceMap.put(12, "Tourist Attraction");
        serviceMap.put(13, "Market");
        serviceMap.put(14, "Historic Site");
        serviceMap.put(15, "Museum");

        String result = null;
        // Traversing through Map using for-each loop
        for(Map.Entry<Integer, String> service: serviceMap.entrySet()) {
            if(service.getKey() == choice){
                result = service.getValue();
            }
        }
        if(result == null){
            System.out.println("Invalid input number");
        }
        return result;
    }

    //Create list of places from the txt file
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

    //Search place based on service
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

    //Get max distance
    private static double getMaxNum(List<Double> list){
        double max = 0;
        for(double num: list){
            if(num > max){
                max = num;
            }
        }
        return max;
    }

    //Get min distance
    private static double getMinNum(List<Double> list){
        double min = Integer.MAX_VALUE;
        for(double num: list){
            if(num < min){
                min = num;
            }
        }
        return min;
    }

    //Display available places
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

    //Display available places for current location
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

    //Search places in the bounded area
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

    //Check if service is available or not
    private static boolean checkService(String service){
        List<Place> places = getList();

        for(Place place : places) {
            if(place.getServices().contains(service)){
                return true;
            }
        }
        return false;
    }

    //Check if service is existing or not
    private static boolean checkPlaceName(String name){
        List<Place> places = getList();

        for(Place place : places) {
            if(place.getName().contains(name)){
                return true;
            }
        }
        return false;
    }
    /*
           *** SEARCH PLACES IN BOUNDED AREA ***
                                                    */
    public static void searchInBoundedArea(){
        Scanner scanner = new Scanner(System.in);

        System.out.println("\n*** FIND PLACES FROM THE GIVEN LOCATION *** ");

        //User input
        String service = getServices();

        System.out.print("Enter the location name: ");
        String location = scanner.nextLine();

        int locationX = 0, locationY = 0;
        String currentLine = "";

        if(checkService(service) && checkPlaceName(location)){
            /* READ THE TXT FILE */
            try {
                BufferedReader reader = new BufferedReader(new FileReader(file));

                while ((currentLine = reader.readLine()) != null) {
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

                for (Place place : places_service) {
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
        }else {
            System.out.println("Invalid input");
        }
    }

    /*
         *** SEARCH PLACES IN BOUNDED AREA WITH CURRENT LOCATION ***
                                                                          */

    public static void currentLocationCalculate(){

        System.out.println("\n*** FIND PLACES FROM YOUR CURRENT LOCATION *** ");

        //User input
        String serviceType = getServices();

        if(checkService(serviceType)){
            //Set the default coordinates for the current location
            int locationX = 10005000, locationY = 10005000;

            //list for places with chosen service
            List<Place> availablePlaces = searchLocation_service(serviceType);

            //List to store the distance for comparison to get the max distance
            List<Double> listOfDistances = new ArrayList<>();

            for (Place place : availablePlaces) {
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
        }else{
            System.out.println("Invalid service!");
        }
    }
}

