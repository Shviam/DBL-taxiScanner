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
    //classes
    Input input = new Input();
    Output output = new Output();
    TaxiScanner taxiscanner = TaxiScanner.getInstance();
    
    //final variables
    int training_time = Input.training_time;
    int total_time = Input.total_time;
    
    //heuristic variables
    int[][] heuristicDis;
    public int number_waypoints = 16;
    //TaxiDistr Hueristic
    public int[] frequence;  //How often it is accessed
    //changeable variables
    Node[] nodes = input.getNodeArray();
    Taxi[] taxis = input.getTaxiArray();
    ArrayList<Customer> customers = input.getCustomerList();
    
    //Temporary globals
    int max_distance;
    boolean ended;
    String temp;
    Astar astar = new Astar();
    
    public void run(){
        /****************************************************/
        /* Determine initial taxi position ******************/
        /****************************************************/
        taxiscanner.println("c");
        
        /****************************************************/
        /* Process Training Time ****************************/
        /****************************************************/
        for (int x = 0; x < training_time; x++){
            temp = taxiscanner.nextLine();
            int whitespace = temp.indexOf(" ");
            int amount_of_customers = Integer.parseInt(temp.substring(0, whitespace));
            for(int y = 0; y < amount_of_customers; y++){
                temp = temp.substring(whitespace);
                whitespace = temp.indexOf(" ");
                int start_pos = Integer.parseInt(temp.substring(0, whitespace));
                
                temp = temp.substring(whitespace);
                whitespace = temp.indexOf(" ");
                int end_pos = Integer.parseInt(temp.substring(0, whitespace));
                
                //Look at the path the customer will take and assign weight to the nodes along it
                weighPaths(new Customer(start_pos, end_pos));
            }
            taxiscanner.println("c");
        }
        /****************************************************/
        /* BWRUMBWRUM MOTHERFUCKERS *************************/
        /****************************************************/
        for (int x = training_time; x < total_time; x++){

            // Process clients into the arraylist

            temp = taxiscanner.nextLine();
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
            for(int y = 0; y < input.number_of_taxis; y++){
              if(!taxis[y].isIdle()){
                  //Take next passanger
              }
            }
        }
    }
    
    public static void main(String[] args) {
        (new Bruwmbruwm()).run();
    }
    
    //Bfs algorithm
    public void BFS(Node start_node, Node end_node, Taxi taxi){
        for(int x = 0; x < Input.number_nodes; x++){
            nodes[x].distance = Input.number_nodes;
        }
        max_distance = Input.number_nodes;
        
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
     int storeDegree[];
      public void neighbour(Node  currentNode){
        int degree;
        int large[] = new int[5];
        int max = storeDegree[0];
        for(int i=0;i<currentNode.position;i++){
             degree = currentNode.neighbours[i];
             storeDegree[i]=degree;           
        }
        for(int i=0;i<=storeDegree.length;i++){
            if(max < large[i]){
                max = storeDegree[i];
                large[i] = max;
            }
        }
        for(int j=0; j<=5;j++){
            System.out.println("largest 5 degree"+large[j]);
        }
    }
    
    
    
    public void weighPaths(Customer passenger){
        Stack<Integer> path = astar.aStar(passenger.current_node, passenger.goal_node);
        while(!path.isEmpty()){
            frequence[path.pop()]++;
        }
    }
    public int[] getHighestFreq(int amount){
        int[] out = new int[amount];
        for(int x = 0; x < amount; x++){
            int highest = 0;
            for(int y = 0; y < Input.number_nodes; y++){
                if(frequence[y] > highest){
                    highest = frequence[y];
                }
            }
            out[x] = highest;
        }
        return out;
    }
     public void highestDegree(Node  currentNode){
        int degree;
        int large[] = new int[5];
        int max = storeDegree[0];
        for(int i=0;i<currentNode.position;i++){
             degree = currentNode.neighbours[i];
             storeDegree[i]=degree;           
        }
        for(int i=0;i<=storeDegree.length;i++){
            if(max < large[i]){
                max = storeDegree[i];
                large[i] = max;
            }
        }
        for(int j=0; j<=5;j++){
            System.out.println("largest 5 degree"+large[j]);
        }
    } 
    
}
