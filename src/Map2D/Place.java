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

    // Method to add multiple services
    public void addServices(Set<String> services) {
        this.services.addAll(services);
    }
}
