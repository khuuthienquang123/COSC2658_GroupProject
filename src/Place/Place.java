package Place;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Place {
    private final int x, y;
    private Set<String> services;

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
}
