import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class QuadTree {

    Node Root;
    private static final int BUCKET_SIZE = 4;

    public QuadTree() {

        Root = new Node(0, 100, 0, 100, "");

    }

    public Point[] findNNP(double x, double y, int n) {
        Point[] result = new Point[n];
        double min = Double.MAX_VALUE;

        HashMap<String, Point> foundPoint = new HashMap<>();
//        min = findMin(x, y, Root, foundPoint);
        for (int i = 0; i < n; i++) {

            MinDistanceResult newNP= findNP(x, y, foundPoint, min, Root);
            if (newNP != null){
                Point newPoint = newNP.getMinPoint();
                min =newNP.getMinDistance();
                String key = String.format("%d-%d", newPoint.x, newPoint.y);
                foundPoint.put(key, newPoint);
                result[i] = newPoint;
            }else{
                break;
            }
        }
        return result;
    }

    public ArrayList<Point> findRangePoint(double x,double y,double range){
        ArrayList<Point> result =new ArrayList<>();

        double min = Double.MAX_VALUE;
        HashMap<String, Point> foundPoint = new HashMap<>();

        while(true){

            MinDistanceResult newNP= findNP(x, y, foundPoint, min, Root);
            if (newNP != null || newNP.getMinDistance() <= range){
                Point newPoint = newNP.getMinPoint();
                min =newNP.getMinDistance();
                String key = String.format("%d-%d", newPoint.x, newPoint.y);
                foundPoint.put(key, newPoint);
                result.add(newPoint);
            }else{
                break;
            }
        }
        return result;
    }

//    public double distanceCalc(double x1, double y1, Point point) {
//        double x2 = point.x, y2 = point.y;
//        return Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
//    }

    public MinDistanceResult pointsIterator(double x1, double y1, Node root, HashMap<String, Point> foundPoints) {

        double min = Double.MAX_VALUE;
        MinDistanceResult result = new MinDistanceResult();

        for (Point point : root.points) {

            double x2 = point.x, y2 = point.y;
            String key = String.format("%d-%d", x2, x2);

            if (foundPoints.get(key) == null) {

                double distance = Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
                if (distance <= min) {
                    min = distance;
                    result.setMinPoint(point);
                }

            }
        }
        result.setMinDistance(min);
        return result;
    }

    public MinDistanceResult findNP(double x, double y, HashMap<String, Point> foundPoint, double min, Node root) {

        double disX = Math.min(Math.abs(root.left - x), Math.abs(root.right - x)),
                disY = Math.min(Math.abs(root.left - y), Math.abs(root.right - y));
        double distance = Math.sqrt(disX * disX + disY * disY);

        if (root.isLeaf()) {
            if (distance <= min) {

                return pointsIterator(x, y, root, foundPoint);

            }
        } else if (distance <= min) {
            MinDistanceResult result = null, temp;
            temp = findNP(x, y, foundPoint, min, root.northWest);
            if ( temp!=null && temp.getMinDistance() < min) {
                min = temp.getMinDistance();
                result = temp;
            }

            temp = findNP(x, y, foundPoint, min, root.northEast);
            if (temp!=null && temp.getMinDistance() < min) {
                min = temp.getMinDistance();
                result = temp;
            }

            temp = findNP(x, y, foundPoint, min, root.southWest);
            if (temp!=null && temp.getMinDistance() < min) {
                min = temp.getMinDistance();
                result = temp;
            }

            temp = findNP(x, y, foundPoint, min, root.southEast);
            if (temp!=null && temp.getMinDistance() < min) {
                min = temp.getMinDistance();
                result = temp;
            }

            return result;
        }

        return null;
    }

//    public double findMin(double x, double y, Node root, HashMap<String, Point> foundPoints) {
//        double left = root.left, bottom = root.bottom;
//        if (root.isLeaf()) {
//            double min = Double.MAX_VALUE;
//            String key = root.address;
//            Node parent = Node.treeNodes.get(key.substring(0, key.length() - 3));
//            double distance;
//
//            distance = pointsIterator(x, y, parent.northWest, foundPoints);
//            if (distance <= min) {
//                min = distance;
//            }
//
//            distance = pointsIterator(x, y, parent.northEast, foundPoints);
//            if (distance <= min) {
//                min = distance;
//            }
//
//            distance = pointsIterator(x, y, parent.southWest, foundPoints);
//            if (distance <= min) {
//                min = distance;
//            }
//
//            distance = pointsIterator(x, y, parent.southEast, foundPoints);
//            if (distance <= min) {
//                min = distance;
//            }
//            return min;
//        } else if (x < left / 2 && y < bottom / 2) {
//
//            return findMin(x, y, root.northWest, foundPoints);
//
//        } else if (x >= left / 2 && y < bottom / 2) {
//
//            return findMin(x, y, root.northEast, foundPoints);
//
//        } else if (x < left / 2 && y >= bottom / 2) {
//
//            return findMin(x, y, root.southWest, foundPoints);
//
//        } else {
//
//            return findMin(x, y, root.southEast, foundPoints);
//
//        }
//    }


    public List<Point> search(String prefix) {
        Node searchTarget = Node.treeNodes.get(prefix);
        if (searchTarget.isLeaf()) {
            return searchTarget.points;
        } else {
            List NWpntsList = search(searchTarget.northWest.address);
            List NEpntsList = search(searchTarget.northEast.address);
            List SWpntsList = search(searchTarget.southWest.address);
            List SEpntsList = search(searchTarget.southEast.address);
            List points = new ArrayList(NWpntsList);
            points.addAll(NEpntsList);
            points.addAll(SWpntsList);
            points.addAll(SEpntsList);
            return points;
        }
    }

    public void delete(double x, double y) {
        String key = String.format("%d-%d", x, y);
        Point target = Point.points.get(key);
        String parentAdd = target.parentAdd;
        Node parent = Node.treeNodes.get(parentAdd);
        parent.DeletePoint(target);
    }


    public void insert(Node root, Point point) {

        if (root.isLeaf() && root.pointsCount() < BUCKET_SIZE) {

            root.insertPoint(point);

        } else if (root.isLeaf()) {

            root.divide(point);

        } else {

            double x = point.x, y = point.y;
            double left = root.left, bottom = root.bottom;

            if (x < left / 2 && y < bottom / 2) {

                insert(root.northWest, point);

            } else if (x >= left / 2 && y < bottom / 2) {

                insert(root.northEast, point);

            } else if (x < left / 2 && y >= bottom / 2) {

                insert(root.southWest, point);

            } else if (x >= left / 2 && y >= bottom / 2) {

                insert(root.southEast, point);

            }
        }
    }

}
