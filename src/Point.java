import java.util.HashMap;

public class Point {

    static HashMap<String,Point> points = new HashMap<>();

    double x, y;
    String parentAdd;

    public void setParentAdd(String parentAdd) {
        this.parentAdd = parentAdd;
    }

    Point(int x, int y) {
        this.x = x;
        this.y = y;
        String key=String.format("%d-%d",x,y);
        points.put(key,this);
    }
}
