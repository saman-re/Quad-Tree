public class MinDistanceResult {
    private double distance;
    private Point minPoint;

    public MinDistanceResult() {
    }

    public void setMinDistance(double distance) {
        this.distance = distance;
    }

    public void setMinPoint(Point minPoint) {
        this.minPoint = minPoint;
    }

    public double getMinDistance() {
        return distance;
    }

    public Point getMinPoint() {
        return minPoint;
    }
}
