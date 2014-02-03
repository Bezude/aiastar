
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Stack;
import java.util.HashMap;
import java.util.Map;

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
    private class PointComparator implements Comparator<Point> {
        public int compare(Point a, Point b) {
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

    // private List<Point> reconstructPath(Point p) {
    //     Stack<Point> s = new Stack<Point>();
    //     while(p.parent != null) {
    //         s.push(p.location);
    //         p = p.parent;
    //     }
    //     s.push(p.location);
    //     return s;
    // }

    /// Creates the path to the goal.
    public List<Point> createPath(final TerrainMap map)
    {
        //ArrayList<Point> path = new ArrayList<Point>();
        // Holds the resulting path which we call the closedSet
        HashSet<Point> closedSet = new HashSet<Point>();
        // The set of all nodes adjacent to nodes currently or previously picked as path nodes 
        TreeSet<Point> openSet = new TreeSet<Point>(new PointComparator());
        Map<Point, Point> cameFrom = new HashMap<Point, Point>();
        Map<Point, double> gScore = new HashMap<Point, double>();

        // Keep track of where we are and add the start point.
        Point current = new Point(map.getStartPoint());
        gScore.put(current, 0);
        cameFrom.put(current, null);
        openSet.add(current);

        final Point start = map.getStartPoint();
        final Point target = map.getEndPoint();

        //-------------------------------------------
        // Holds the resulting path
        final ArrayList<Point> thispath = new ArrayList<Point>();

        // Keep track of where we are and add the start point.
        final Point CurrentPoint = map.getStartPoint();
        thispath.add(new Point(CurrentPoint));

        // Keep moving horizontally until we match the target.
        while(map.getEndPoint().x != CurrentPoint.x)
        {
            if(map.getEndPoint().x > CurrentPoint.x)
                ++CurrentPoint.x;
            else
                --CurrentPoint.x;
            thispath.add(new Point(CurrentPoint));
        }

        // Keep moving vertically until we match the target.
        while(map.getEndPoint().y != CurrentPoint.y)
        {
            if(map.getEndPoint().y > CurrentPoint.y)
                ++CurrentPoint.y;
            else
                --CurrentPoint.y;
            thispath.add(new Point(CurrentPoint));
        }

        // We're done!  Hand it back.
        //------------------------------------------- 
        while(!openSet.isEmpty()){
            current = openSet.pollFirst();
            //System.out.println("curr: x=" + current.location.x + " y=" + current.location.y);
            if (current.equals(target)) 
                return reconstructPath(current);
                //return thispath;
                
            closedSet.add(current);
            Point[] neighbors = map.getNeighbors(current);
            for(int i = 0; i < neighbors.length; i++) {
                double tentativeGScore = current + map.getCost(current.location, neighbors[i]);
                    Point neighbor = neighbors[i];
                //System.out.println(node.toString());
                if(closedSet.contains(neighbor)) {
                    //System.out.println("[" + i + "] skipped");
                    continue;
                }
                if(!openSet.contains(neighbor || tentativeGScore < map.getCost(start, neighbors[i])) {
                    cameFrom.put(neighbor, current);
                    gScore.put(neighbor, (gScore.get(current) + map.getCost(current, neighbor)));                
                } else if(!openSet.contains(neighbor)){
                    openSet.add(neighbor);
                }

            }

            //m++;
            //if(m>5) openSet.clear();
        }
        return thispath;
    }
}
