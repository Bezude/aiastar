
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Stack;

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
    private List<Point> reconstructPath(PathNode p) {
        Stack<Point> s = new Stack<Point>();
        while(p.parent != null) {
            s.push(p.location);
            p = p.parent;
        }
        s.push(p.location);
        return s;
    }

    /// Creates the path to the goal.
    public List<Point> createPath(final TerrainMap map)
    {
        ArrayList<Point> path = new ArrayList<Point>();
        // Holds the resulting path which we call the closedSet
        HashSet<PathNode> closedSet = new HashSet<PathNode>();
        // The set of all nodes adjacent to nodes currently or previously picked as path nodes 
        TreeSet<PathNode> openSet = new TreeSet<PathNode>(new PathNodeComparator());
        

        // Keep track of where we are and add the start point.
        PathNode currentNode = new PathNode(map.getStartPoint(), null, 0, map.getTile(map.getStartPoint()));
        openSet.add(currentNode);

        final Point target = map.getEndPoint();

        while(!openSet.isEmpty()){
            currentNode = openSet.pollFirst();
            if (currentNode.location == target) 
                return reconstructPath(currentNode);
            closedSet.add(currentNode);
            Point[] neighbors = map.getNeighbors(currentNode.location);
            for(int i = 0; i < neighbors.length; i++) {
                PathNode node = new PathNode(   neighbors[i],
                                                currentNode,
                                                currentNode.costToHere + map.getCost(currentNode.location, neighbors[i]),
                                                map.getTile(neighbors[i]));
                if(closedSet.contains(node)) {
                    continue;
                }
                if(!openSet.contains(node)) {
                    openSet.add(node);
                }
            }

            // ArrayList
        }

        /*
        while(!(currentPoint.x == target.x && currentPoint.y == target.y)) {

        }
        */
        // We're done!  Hand it back.
        return path;
    }
}
