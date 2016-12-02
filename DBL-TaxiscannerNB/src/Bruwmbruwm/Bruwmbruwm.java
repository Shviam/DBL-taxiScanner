package Bruwmbruwm;

import static java.lang.Integer.max;
import java.util.ArrayList;
import static java.util.Arrays.fill;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;
import java.util.Stack;

/**
 *
 * @author s156035
 */
public class Bruwmbruwm {
    // Preamble variables
    float alpha;
    int max_drop_off_time;
    int number_of_taxis, seats;
    Node[] nodes;
    Taxi[] taxis;
    public int number_nodes;
    int training_time;
    int total_time;   
    
    //heuristic variables
    int[][] heuristicDis;
    public int number_waypoints = 16;
    
    //Temporary globals
    int max_distance;
    boolean ended;
    
    //Preamble
    TaxiScanner input = TaxiScanner.getInstance();
    int preamble_length;
    
    Output output = new Output();
    
    String temp;
    
    
    ArrayList<Customer> customers = new ArrayList<>();
    
    public void run(){
        preamble_length = Integer.parseInt(input.nextLine());
        
        for (int x = 1; x < preamble_length; x++){
            //Read the preamble
            switch(x){
                case 1:
                    alpha = Float.parseFloat(input.nextLine());
                    break;
                case 2:
                    max_drop_off_time = Integer.parseInt(input.nextLine());
                    break;
                case 3:
                    temp = input.nextLine();
                    
                    //Find end of first integer
                    int whitespace = temp.indexOf(" ");
                    number_of_taxis = Integer.parseInt(temp.substring(0, whitespace)); 
                    
                    taxis = new Taxi[number_of_taxis];
                    for(int y = 0; y < number_of_taxis; y++){
                        taxis[y] = new Taxi();
                    }
                    
                    seats = Integer.parseInt(temp.substring(whitespace+1, temp.length()));
                    break;
                case 4:
                    number_nodes = Integer.parseInt(input.nextLine());
                    nodes = new Node[number_nodes];
                    for(int y = 0; y < number_nodes; y++){
                        nodes[y] = new Node(y);
                    }
                    break;
            }
            
            if(inBetween(x, 5, 5+number_nodes)){
                //Take the number up to the first whitespace, and then delete it.
                temp = input.nextLine();
                int whitespace = temp.indexOf(" ");
                int size = Integer.parseInt(temp.substring(0, whitespace)); 
                temp = temp.substring(whitespace+1, temp.length());

                //initialise node
                nodes[x-5].initialise(size);

                for(int y = 0; y < size; y++){
                    int end = temp.indexOf(" ");
                    int neighbour = Integer.parseInt(temp.substring(0, whitespace));
                    temp = temp.substring(end+1, temp.length());

                    nodes[x-5].write_neighbour(neighbour);
                };       
            }
            if(x == 4+number_nodes){
                temp = input.nextLine();
                int whitespace = temp.indexOf(" ");
                training_time = Integer.parseInt(temp.substring(0, whitespace));
                total_time = Integer.parseInt(temp.substring(whitespace+1, temp.length()));
            }
            
            
        }
        /****************************************************/
        /* Determine initial taxi position ******************/
        /****************************************************/
        input.println("c");
        
        /****************************************************/
        /* Process Training Time ****************************/
        /****************************************************/
        for (int x = 0; x < training_time; x++){
            input.nextLine();
            input.println("c");
        }
        /****************************************************/
        /* BWRUMBWRUM MOTHERFUCKERS *************************/
        /****************************************************/
        for (int x = training_time; x < total_time; x++){

            // Process clients into the arraylist

            temp = input.nextLine();
            int whitespace = temp.indexOf(" ");
            int amount_of_customers = Integer.parseInt(temp.substring(0, whitespace));
            for(int y = 0; y < amount_of_customers; y++){
                temp = temp.substring(whitespace);
                whitespace = temp.indexOf(" ");
                int start_pos = Integer.parseInt(temp.substring(0, whitespace));
                
                temp = temp.substring(whitespace);
                whitespace = temp.indexOf(" ");
                int end_pos = Integer.parseInt(temp.substring(0, whitespace));
                customers.add(new Customer(start_pos, end_pos));
            }
            
            //Check for idle taxi
            for(int y = 0; y < number_of_taxis; y++){
              if(!taxis[y].isIdle()){
                  //Take next passanger
              }
            }
        }
    }
    
    public static void main(String[] args) {
        (new Bruwmbruwm()).run();
    }
    
    
    /****************************************************************/
    /* Misc Methods *************************************************/
    /****************************************************************/
    public boolean inBetween(int x, int lower_bound, int upper_bound){
        return !(x < lower_bound || x > upper_bound);
    }
    
    //Bfs algorithm
    public void BFS(Node start_node, Node end_node, Taxi taxi){
        for(int x = 0; x < number_nodes; x++){
            nodes[x].distance = number_nodes;
        }
        max_distance = number_nodes;
        
        //the end and start are swapped, so the Stack doesn't go nuts
        BFSpathfinder(end_node, start_node, taxi);
    }
    public boolean BFSpathfinder(Node current_node, Node end_node, Taxi taxi){
        for(int x = 0; x < current_node.position; x++){
            if (ended){
                return false;
            }
            else if (current_node.neighbours[x] != end_node.position){
                if (BFSpathfinder(nodes[current_node.neighbours[x]], end_node, taxi)){
                    taxi.path.push(current_node);
                    return true;
                } 
                else{
                    return false;
                }
            }
            else{
                max_distance = nodes[current_node.neighbours[x]].distance;
                taxi.path.push(nodes[current_node.neighbours[x]]);
                return true;
            }
        }
        return false;
    }
    
    //initializing the waypoint array for heuristic function
    void initHeuristic(){
        //getting a random start waypoint
        Random rand = new Random();
        int start = rand.nextInt(number_nodes);
        
        //initialize waypoint array
        heuristicDis = new int[number_waypoints+5][number_nodes+5];
        
        //run BFS for all waypoints
        for(int i=0; i<number_waypoints; i++){
            //make queue for the BFS
            Queue<Integer> Q = new LinkedList<>();
            Q.add(start);
            nodes[start].waypoint = true;
            
            //fill array with -1, meaning unvisited
            fill(heuristicDis[i], -1);
            //set start distance to 0
            heuristicDis[i][start] = 0;
            while(!Q.isEmpty()){
                int u = Q.remove();
                for(int e : nodes[u].neighbours){
                    //if neighbour is unvisited
                    if(heuristicDis[i][e] == -1){
                        heuristicDis[i][e] = heuristicDis[i][u] + 1;
                        Q.add(e);
                        
                        //get the next start waypoint as farthest from the current
                        if(heuristicDis[i][e] > heuristicDis[i][start] && !nodes[e].waypoint){
                            start = e;
                        }
                    }
                }
            }
        }
    }
    
    int heuristic(int A, int B){
        int dis = 0;
        
        for(int i=0; i<number_waypoints; i++){
            //triangle inequality
            int wayDis = max(heuristicDis[i][A] - heuristicDis[i][B], heuristicDis[i][B] - heuristicDis[i][A]);
            //get the max from all the waypoints
            dis = max(dis, wayDis);
        }
        return dis;
    }
    
    Stack<Integer> aStar(int source, int goal){
        PriorityQueue<NodeDist> Q = new PriorityQueue<>();
        //resetting the node values
        for (Node node : nodes) {
            node.distance = Integer.MAX_VALUE;
            node.visited = false;
        }
        //setting the source distance to 0 and pushing to queue
        nodes[source].distance = 0;
        Q.add(new NodeDist(source, heuristic(source, goal)));
        while(!Q.isEmpty()){
            NodeDist nd = Q.poll();
            int k = nd.i;
            int d = nd.d;
            //in case of multiple entries in queue, this node is already explored
            if(nodes[k].visited) continue;
            nodes[k].visited = true;
            //if we reached the goal, stop searching
            if(k == goal) break;
            
            //check every neighbour of k
            for(int u : nodes[k].neighbours){
                //if already explored, skip it
                if(nodes[u].visited) 
                    continue;
                //update distance to source, and fscore = dis to source + heuristic to end
                int newDis = nodes[k].distance + 1;
                int fscore = newDis + heuristic(u, goal);
                
                //in case there exist a faster path to u
                if(newDis < nodes[u].distance){
                    //set node values and add to queue
                    nodes[u].distance = newDis;
                    nodes[u].parent = k;
                    Q.add(new NodeDist(u, fscore));
                }
            }
        }
        //find the path by backtracking from the goal
        int k = goal;
        Stack<Integer> p = new Stack<>();
        while(k != source){
            p.add(k);
            k = nodes[k].parent;
        }
        return p;
    }
}
