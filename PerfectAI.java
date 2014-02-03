
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
    HashMap<Point, Double> gScore = new HashMap<Point, Double>();
    
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
            double aGScore = gScore.get(a);
            double bGScore = gScore.get(b);
            double diff = (aGScore - bGScore);
            if(diff < 0) {
                System.out.println("-1");
                return -1;
            }
            else if (diff == 0) {
                System.out.println("0");
                return 0;
            }
            else {
                System.out.println("1");
                return 1;
            }
        }
    }

    private List<Point> reconstructPath(HashMap<Point, Point> cameFrom, Point p) {
        Stack<Point> s = new Stack<Point>();
        Point curr = new Point();
        curr = p;
        boolean isNull = true;
        while(isNull) {
            s.push(curr);
            curr = cameFrom.get(curr);
            if(curr == null)
                isNull = false;
        }
        List<Point> thePath= new ArrayList<Point>();
        for(int i = 0; i < s.size(); i++){
            thePath.add(s.pop());
        }
        return s;
    }

    /// Creates the path to the goal.
    public List<Point> createPath(final TerrainMap map)
    {
        //ArrayList<Point> path = new ArrayList<Point>();
        // Holds the resulting path which we call the closedSet
        HashSet<Point> closedSet = new HashSet<Point>();
        // The set of all nodes adjacent to nodes currently or previously picked as path nodes 
        TreeSet<Point> openSet = new TreeSet<Point>(new PointComparator());
        HashMap<Point, Point> cameFrom = new HashMap<Point, Point>();


        // Keep track of where we are and add the start point.
        Point current = new Point();
        current = map.getStartPoint();
        gScore.put(current, new Double(0));
        cameFrom.put(current, null);
        openSet.add(current);

        final Point start = map.getStartPoint();
        final Point target = map.getEndPoint();

        // Holds the resulting path, its garbage
        final ArrayList<Point> thispath = new ArrayList<Point>();


        while(!openSet.isEmpty()){
            current = openSet.pollFirst();
            System.out.println("curr: x=" + current.x + " y=" + current.y);
            if (current.equals(target)) 
                return reconstructPath(cameFrom, current);
                //return thispath;
                
            closedSet.add(current);
            Point[] neighbors = map.getNeighbors(current);
            for(int i = 0; i < neighbors.length; i++) {
                Point neighbor = neighbors[i];
                System.out.println(neighbor.x + ", " + neighbor.y);
                double tentativeGScore = gScore.get(current) + map.getCost(current, neighbor);
                //System.out.println(node.toString());
                if(closedSet.contains(neighbor)) {
                    continue;
                }
                if(!openSet.contains(neighbor) || (tentativeGScore < gScore.get(neighbor))) {
                    cameFrom.put(neighbor, current);
                    gScore.put(neighbor, (gScore.get(current) + map.getCost(current, neighbor)));                
                } 
                else if(!openSet.contains(neighbor)) {
                    openSet.add(neighbor);
                }

            }

            //m++;
            //if(m>5) openSet.clear();
        }
        return thispath;
    }
}
