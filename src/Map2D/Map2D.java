package Map2D;

import Map2D.Place;
import Map2D.QuadTree.QuadTree;

import java.io.*;
import java.util.List;
import java.util.Scanner;

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

    public void searchInBoundedArea(){
        Scanner scanner = new Scanner(System.in);

        //User input
        System.out.print("Enter the first location name: ");
        String location1 = scanner.next();
        System.out.print("Enter the second location name: ");
        String location2 = scanner.next();

        /*READ THE TXT FILE*/
        try{
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String currentLine;

            if ((currentLine = reader.readLine()) != null){
                String[] token = currentLine.split(",");
                String locationName = token[3].trim();

                if(location1.equals(locationName)){

                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

