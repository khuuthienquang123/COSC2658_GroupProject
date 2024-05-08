package Map2D.QuadTree;

import Map2D.Place;

import java.util.Collection;
import java.util.List;


public class QuadTree {
    QuadTreeNode root;

    public QuadTree(int x1, int y1, int x2, int y2) {
        root = new QuadTreeNode(x1, y1, x2, y2);
    }

    public void add(Place place) {
        root.add(place);
    }

    public List<Place> search(int x1, int y1, int width, int height, String service) {
        return root.queryPlace(x1, y1, width + x1, height + y1, service);
    }

    public void editPlace(Place place, Collection<String> newServices) {
        root.edit(place, newServices);
    }

    public boolean removePlace(int x, int y) {
        if (root != null) {
            root.removePlace(x, y);
            return true;
        }

        return false;
    }

    public void display(){
        System.out.println("Displaying places:");
        root.displayPlaces();
    }
}