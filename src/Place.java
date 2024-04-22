import java.util.ArrayList;
import java.util.List;

public class Place {
    private final int x, y;
    private final List<String> services;

    public Place(int x, int y, List<String> services) {
        this.x = x;
        this.y = y;
        this.services = new ArrayList<>(services);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public List<String> getServices() {
        return services;
    }
}
