package Map2D.QuadTree;

import Map2D.Place;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class QuadTreeNode {
    static final int CAPACITY = 4;
    List<Place> places;
    QuadTreeNode[] children;

    // Bounding box - top left (x1,y1) & bottom right (x2,y2)
    private final int x1;
    private final int y1;
    private final int x2;
    private final int y2;

    public QuadTreeNode(int x1, int y1, int x2, int y2){
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.places = new ArrayList<>();
        this.children = new QuadTreeNode[CAPACITY];
    }

    private boolean contains(int x, int y){
        return (x >= x1 && x < x2 && y >= y1 && y < y2);
    }

    public void add(Place place){
        if(places.size() <= CAPACITY || (x2 - x1 <= 1 && y2 - y1 <= 1)){
            places.add(place);
        }else {
            if(children[0] == null){
                subdivide();
            }
            for(QuadTreeNode child : children){
                if(child.contains(place.getX(), place.getY())){
                    child.add(place);
                    return;
                }
            }
            throw new RuntimeException("Place does not fit into any quadrant!");
        }
    }

    public void subdivide(){
        int midX = (x1 + x2) / 2;
        int midY = (y1 + y2) / 2;
        children[0] = new QuadTreeNode(x1, y1, midX, midY); //Top left
        children[1] = new QuadTreeNode(midX, y1, x2, midY); //Top right
        children[2] = new QuadTreeNode(x1, midY, midX, y2); //Bottom left
        children[3] = new QuadTreeNode(midX, midY, x2, y2); //Bottom right

        for(Place place: places){
            boolean placed = false;
            for(QuadTreeNode child: children){
                if(child.contains(place.getX(), place.getY())){
                    child.add(place);
                    placed = true;
                    break;
                }
            }
            if(!placed){
                throw new RuntimeException("Place does not fit into any child, though it should!");
            }
        }
        places.clear();
    }

    private boolean intersects(int searchX1, int searchY1, int searchX2, int searchY2){
        return !(x1 > searchX2 || x2 < searchX1 || y1 > searchY2 || y2 < searchY1);
    }

    public List<Place> queryPlace(int searchX1, int searchY1, int searchX2, int searchY2, String service){
        List<Place> result = new ArrayList<>();


        if(!intersects(searchX1, searchY1, searchX2, searchY2)){
            return result;
        }

        for(Place place: places){
            if(place.getX() >= searchX1 && place.getX() <= searchX2 &&
                    place.getY() >= searchY1 && place.getY() <= searchY2 && place.getServices().contains(service)){
                result.add(place);
            }
        }
        if(children[0] != null){
            for(QuadTreeNode child : children){
                result.addAll(child.queryPlace(searchX1, searchY1, searchX2, searchY2, service));
            }
        }
        return result;
    }


    public Place findPlace(int x, int y){
        if(!contains(x, y)){
            return null;
        }

        for(Place place: places){
            if(place.getX() == x && place.getY() == y){
                return place;
            }
            if(children[0] != null){
                for(QuadTreeNode child: children){
                    Place result = child.findPlace(x, y);
                    if(result != null){
                        return result;
                    }
                }
            }
        }
        return null;
    }

    public void edit(Place place, Collection<String> newService){
        Place foundPlace = findPlace(place.getX(), place.getY());
        if(foundPlace != null){
            foundPlace.setServices(newService);
        }else{
            throw new RuntimeException("Invalid place to edit");
        }
    }

    //
    // Method to remove a place based on coordinates
    public boolean removePlace(int x, int y) {
        if (children[0] == null) {
            // This is a leaf node, attempt to remove the place directly from this node
            boolean removed = places.removeIf(place -> place.getX() == x && place.getY() == y);
            if (removed) {
                System.out.println("Place removed from node [" + x1 + ", " + y1 + ", " + x2 + ", " + y2 + "]");
            }
            return removed;
        } else {
            // This is an internal node, recurse into the correct child
            for (QuadTreeNode child : children) {
                if (child.contains(x, y)) {
                    return child.removePlace(x, y);
                }
            }
        }
        return false; // Place not found in any children or at this node
    }

    public void displayPlaces() {
        if (children[0] == null) {
            for (Place place : places) {
                System.out.println("Place at (" + place.getX() + ", " + place.getY() + ")");
            }
        } else {
            for (QuadTreeNode child : children) {
                child.displayPlaces();
            }
        }
    }
}
