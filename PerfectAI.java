
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import java.util.Comparator;

/// An implementation of A*
/**
 * 
 * 
 * @author Benjamin Roye and Stefan Peterson
 */
public class PerfectAI implements AIModule
{
    private class PathNode {
        // Location this node represents
        public Point location;
        public PathNode parent;
        public double height;
        public double costToHere;
        public double costGuessToTarget;
        public double costGuessTotal;
            
        public PathNode(Point p, PathNode parent, double cost, double height) {
            this.location = p;
            this.parent = parent;
            this.costToHere = cost;
            this.height = height;
            costGuessToTarget = 0; // Just getting dijkstra's right now.
            costGuessTotal = costToHere + costGuessToTarget;
        }
    }
    // To use some of the provided java data structures we need a comparator so order PathNodes
    private class PathNodeComparator implements Comparator<PathNode> {
        public int compare(PathNode a, PathNode b) {
            return (int) (a.costGuessTotal - b.costGuessTotal);
        }
    }
    /// Creates the path to the goal.
    public List<Point> createPath(final TerrainMap map)
    {
        // Holds the resulting path
        final ArrayList<Point> path = new ArrayList<Point>();

    

        // Keep track of where we are and add the start point.
        final Point currentPoint = map.getStartPoint();
        path.add(new Point(currentPoint));

        final Point target = map.getEndPoint();
        /*
        while(!(currentPoint.x == target.x && currentPoint.y == target.y)) {

        }
        */
        // We're done!  Hand it back.
        return path;
    }
}
