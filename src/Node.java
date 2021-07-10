import java.util.ArrayList;
import java.util.HashMap;

public class Node {

    static HashMap<String, Node> treeNodes = new HashMap<>();

    String address;
    double right, left, top, bottom;

    ArrayList<Point> points;

    Node northWest;
    Node northEast;
    Node southWest;
    Node southEast;

    Node(double right, double left, double top, double bottom, String address) {

        this.right = right;
        this.left = left;
        this.top = top;
        this.bottom = bottom;
        points = new ArrayList<>();
        this.address = address;

        treeNodes.put(address, this);
    }

    public void DeletePoint(Point target) {
        double x = target.x, y = target.y;
        for (int i = 0; i < points.size(); i++) {
            Point point = points.get(i);
            if (point.x == x && point.y == y) {
                points.remove(i);
            }
        }

        this.compressor();
    }

    public void compressor() {
        String add = this.address;
        String parentAdd = add.substring(0, add.length() - 3);
        Node parent = Node.treeNodes.get(parentAdd);
        Node NW = parent.northWest,
                NE = parent.northEast,
                SW = parent.southWest,
                SE = parent.southEast;
        int NWpnts = NW.points.size(),
                NEpnts = NE.points.size(),
                SWpnts = SW.points.size(),
                SEpnts = SE.points.size();
        if (NWpnts == 0 && NEpnts == 0 && SWpnts == 0 && SEpnts == 0) {
            NW = null;
            NE = null;
            SW = null;
            SE = null;
        }
    }

    public void insertPoint(Point point) {
        points.add(point);
    }

    public void divide(Point point) {
        northWest = new Node(right, left / 2, top, bottom / 2, this.address.concat("00"));
        northEast = new Node(left / 2, left, top, bottom / 2, this.address.concat("01"));
        southWest = new Node(right, left / 2, bottom / 2, bottom, this.address.concat("10"));
        southEast = new Node(left / 2, left, bottom / 2, bottom, this.address.concat("11"));

        for (Point oldPoint : points) {
            goToChildren(oldPoint);
        }
        goToChildren(point);
        points = null;
    }

    private void goToChildren(Point point) {

        double x = point.x, y = point.y;
        if (x < left / 2 && y < bottom / 2) {

            northWest.insertPoint(point);
            point.parentAdd = northWest.address;

        } else if (x >= left / 2 && y < bottom / 2) {

            northEast.insertPoint(point);
            point.parentAdd = northEast.address;

        } else if (x < left / 2 && y >= bottom / 2) {

            southWest.insertPoint(point);
            point.parentAdd = southWest.address;

        } else if (x >= left / 2 && y >= bottom / 2) {

            southEast.insertPoint(point);
            point.parentAdd = southEast.address;

        }
    }

    public int pointsCount() {
        return this.points.size();
    }

    public boolean isLeaf() {
        if (this.northWest != null) {
            return true;
        } else {
            return false;
        }
    }

}
