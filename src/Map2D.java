import java.util.ArrayList;
import java.util.List;

public class Map2D {
    private final List<Place> places;

    public Map2D() {
        this.places = new ArrayList<>();
    }

    public void edit(int placeIndex, List<String> newServices) {
            Place place = places.get(placeIndex);
            place.getServices().clear();
            place.getServices().addAll(newServices);
    }

    public void add(Place place){
        places.add(place);
    }

    public void remove(int placeIndex) {places.remove(placeIndex);}

    public List<Place> search(int topLeftX, int topLeftY, int width, int height, String serviceType){
        List<Place> result = new ArrayList<Place>();
        for(Place place :places){
            if(isWithinBound(place, topLeftX, topLeftY, width, height) &&
                place.getServices().contains(serviceType)){
                result.add(place);
                if(result.size() == 50){
                    break;
                }
            }
        }
        return result;
    }

    private boolean isWithinBound(Place place, int topLeftX, int topLeftY, int width, int height){
        return place.getX() >= topLeftX &&
                place.getX() <= topLeftX + width &&
                place.getY() >= topLeftY &&
                place.getY() <= topLeftY + height;
    }
}
