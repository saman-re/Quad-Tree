public class QuadTree {

    Node Root;
    private static final int BUCKET_SIZE = 4;

    public QuadTree() {

        Root = new Node(0, 100, 0, 100, "");

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

    public void delete() {
    }

    public void search() {
    }
}
