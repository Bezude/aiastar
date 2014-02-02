
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

    // private List<Point> reonstructPath(Point point) {
    //     List<point> path = new List<Point>();
    //     while(!(point.getPreviousNode() == null)) {
    //         point.prependWayPoint(node);
    //         point = point.getPreviousNode();
    //     }
    //     this.shortestPath = path;
    //     return path;
    // }

    /// Creates the path to the goal.
    public List<Point> createPath(final TerrainMap map)
    {
        // Holds the resulting path which we call the closedSet
        final ArrayList<Point> closedSet = new ArrayList<Point>();
        // The set of all nodes adjacent to nodes currently or previously picked as path nodes 
        private ArrayList<Point> openSet = new ArrayList<Point>();
        

        // Keep track of where we are and add the start point.
        final Point currentPoint = map.getStartPoint();
        closedSet.add(new Point(currentPoint));

        final Point target = map.getEndPoint();

        while(!openSet.isEmpty()){
            currentPoint = //next node with lowest f()
            if (currentPoint = map.getEndPoint()) 
                return reconstructPath(currentPoint);
            openSet.remove(currentPoint);
            closedSet.add(currentPoint);

            ArrayList
        }

        /*
        while(!(currentPoint.x == target.x && currentPoint.y == target.y)) {

        }
        */
        // We're done!  Hand it back.
        return openSet;
    }
}
