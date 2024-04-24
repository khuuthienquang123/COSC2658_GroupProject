package Place;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Place implements Comparable<Place>{
    private final int x, y;
    private Set<String> services;
    private double distance;

    public Place(int x, int y) {
        this.x = x;
        this.y = y;
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

    public void setServices(Collection<String> newServices) {
        this.services.clear();
        this.services.addAll(newServices);
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

