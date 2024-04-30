package Map2D;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class Place {
    private final int x, y;
    private final String name;
    private final Set<String> services;

    public Place(int x, int y, String name) {
        this.x = x;
        this.y = y;
        this.name = name;
        this.services = new HashSet<>();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Set<String> getServices() {
        return services;
    }

    public String getName() {
        return name;
    }

    public void setServices(Collection<String> newServices) {
        this.services.clear();
        this.services.addAll(newServices);
    }

    // Method to add a service
    public void addService(String service) {
        this.services.add(service);
    }

    // Method to add multiple services
    public void addServices(Set<String> services) {
        this.services.addAll(services);
    }

    /* CALCULATE THE DISTANCE */
    public static void calculateDistance(Place p1, Place p2) {
        double distance = Math.sqrt((p1.getX() - p2.getX()) * (p1.getX() - p2.getX()) +
                (p1.getY() - p2.getY()) * (p1.getY() - p2.getY()));

        System.out.println("Distance from " + p1.getName()+ " to " + p2.getName() + ": " +(int) distance +
                "m.");
    }
}
