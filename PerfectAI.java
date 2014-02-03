
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import java.util.Set;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Stack;
import java.util.HashMap;
import java.util.Map;
import java.util.*;

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
            Double aGScore = gScore.get(a);
            Double bGScore = gScore.get(b);
            
            if(aGScore == null)
                return -1;
            else if(bGScore == null)
                return 1;
            
            Double diff = (aGScore - bGScore);
            
            if(diff < 0.0)
                return -1;
            else if (diff == 0)
                return 0;
            else
                return 1;
        }
    }

    private List<Point> reconstructPath(HashMap<Point, Point> cameFrom, Point p) {
        Stack<Point> s = new Stack<Point>();
        Point curr = new Point();
        curr = p;
        int count = 0;
        boolean isNull = true;
        while(isNull) {
            s.push(curr);
            curr = cameFrom.get(curr);
            if(curr == null)
                isNull = false;
            count++;
        }
        ArrayList<Point> thePath= new ArrayList<Point>();

        for(int i = 0; i < count; i++){
            thePath.add(i, s.pop());
        }
        return thePath;
    }

    /// Creates the path to the goal.
    public List<Point> createPath(final TerrainMap map)
    {        
        // Holds the resulting path, its garbage
        final ArrayList<Point> thispath = new ArrayList<Point>();
        // Holds the resulting path which we call the closedSet
        HashSet<Point> closedSet = new HashSet<Point>();
        // The set of all nodes adjacent to nodes currently or previously picked as path nodes 
        TreeSet<Point> openSet = new TreeSet<Point>(new PointComparator());
        HashMap<Point, Point> cameFrom = new HashMap<Point, Point>();
        HashMap<Point, Point> fScore = new HashMap<Point, Point>();


        // Keep track of where we are and add the start point.
        Point current = new Point();
        current = map.getStartPoint();
        gScore.put(current, new Double(0.0));
        cameFrom.put(current, null);
        openSet.add(current);

        final Point start = map.getStartPoint();
        final Point target = map.getEndPoint();

        while(!openSet.isEmpty()) {
            current = openSet.pollFirst();
            if (current.equals(target)) 
                return reconstructPath(cameFrom, current);
                
            closedSet.add(current);
            Point[] neighbors = map.getNeighbors(current);
            for(int i = 0; i < neighbors.length; i++) {
                Point neighbor = neighbors[i];

                if(closedSet.contains(neighbor)) {
                    continue;
                }
                double tentativeGScore = gScore.get(current) + map.getCost(current, neighbor);
                if(!openSet.contains(neighbor) || (tentativeGScore < gScore.get(neighbor))) {
                    cameFrom.put(neighbor, current);
                    gScore.put(neighbor, tentativeGScore);  
                    if(!openSet.contains(neighbor)) {
                        openSet.add(neighbor);
                    }
                }
            }
        }
        return thispath;
    }
}
