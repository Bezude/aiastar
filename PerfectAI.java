
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

        public String toString() {
            return "PN: x=" + location.x + " y=" + location.y
                                 + "; Parent: x=" + parent.location.x + " y=" + parent.location.y
                                 + "; Cost: " + costToHere;
        }
    }
    // To use some of the provided java data structures we need a comparator to order PathNodes
    private class PathNodeComparator implements Comparator<PathNode> {
        public int compare(PathNode a, PathNode b) {
            double diff = (a.costGuessTotal - b.costGuessTotal);
            if(diff < 0) {
                return -1;
            }
            else if (diff == 0) {
                return 0;
            }
            else {
                return 1;
            }
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
        System.out.println("My End: X=" + p.location.x + " Y=" + p.location.y);
        Stack<Point> s = new Stack<Point>();
        while(p.parent != null) {
            s.push(p.location);
            p = p.parent;
        }
        s.push(p.location);
        System.out.println("My Start: X=" + p.location.x + " Y=" + p.location.y);
        return s;
    }

    /// Creates the path to the goal.
    public List<Point> createPath(final TerrainMap map)
    {
        //ArrayList<Point> path = new ArrayList<Point>();
        // Holds the resulting path which we call the closedSet
        HashSet<Point> closedSet = new HashSet<Point>();
        // The set of all nodes adjacent to nodes currently or previously picked as path nodes 
        TreeSet<PathNode> openSet = new TreeSet<PathNode>(new PathNodeComparator());
        

        // Keep track of where we are and add the start point.
        PathNode currentNode = new PathNode(map.getStartPoint(), null, 0, map.getTile(map.getStartPoint()));
        openSet.add(currentNode);

        final Point target = map.getEndPoint();
        System.out.println("Start: X=" + currentNode.location.x + " Y=" + currentNode.location.y);
        System.out.println("End: X=" + target.x + " Y=" + target.y);

        //int m = 0;
        while(!openSet.isEmpty()){
            currentNode = openSet.pollFirst();
            //System.out.println("curr: x=" + currentNode.location.x + " y=" + currentNode.location.y);
            if (currentNode.location.equals(target)) 
                //return reconstructPath(currentNode);
            closedSet.add(currentNode.location);
            Point[] neighbors = map.getNeighbors(currentNode.location);
            for(int i = 0; i < neighbors.length; i++) {
                PathNode node = new PathNode(   neighbors[i],
                                                currentNode,
                                                currentNode.costToHere + map.getCost(currentNode.location, neighbors[i]),
                                                map.getTile(neighbors[i]));
                //System.out.println(node.toString());
                if(closedSet.contains(node.location)) {
                    //System.out.println("[" + i + "] skipped");
                    continue;
                }
                if(!openSet.contains(node)) {
                    //System.out.println("[" + i + "] " + neighbors[i].toString());
                    openSet.add(node);
                }
            }

            //m++;
            //if(m>5) openSet.clear();
        }

        // Holds the resulting path
        final ArrayList<Point> path = new ArrayList<Point>();

        // Keep track of where we are and add the start point.
        final Point CurrentPoint = map.getStartPoint();
        path.add(new Point(CurrentPoint));

        // Keep moving horizontally until we match the target.
        while(map.getEndPoint().x != CurrentPoint.x)
        {
            if(map.getEndPoint().x > CurrentPoint.x)
                ++CurrentPoint.x;
            else
                --CurrentPoint.x;
            path.add(new Point(CurrentPoint));
        }

        // Keep moving vertically until we match the target.
        while(map.getEndPoint().y != CurrentPoint.y)
        {
            if(map.getEndPoint().y > CurrentPoint.y)
                ++CurrentPoint.y;
            else
                --CurrentPoint.y;
            path.add(new Point(CurrentPoint));
        }

        // We're done!  Hand it back.
        return path;
    }
}
