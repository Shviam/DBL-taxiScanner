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
    HotSpot[] hotspots;
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
    
    Queue<Customer> cus_waiting = new LinkedList<>();
    
    //Temporary globals
    int max_distance;
    boolean ended;
    String temp;
    Astar astar;
    
    public void run(){
        /****************************************************/
        /* Read preamble ************************************/
        /****************************************************/
        input.readPreamble();
        astar = new Astar(input);
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
                cus_waiting.add(new Customer(start_pos, end_pos));
            }
            
            //assign waiting customers to taxis
            while(!cus_waiting.isEmpty()){
                Taxi t = new Taxi();
                boolean assigned = false;
                for(int y = 0; y < Input.number_of_taxis; y++){
                    if(taxis[y].isIdle()){
                        if(!assigned || astar.h.heuristic(t.taxiPosition,cus_waiting.peek().current_node) > astar.h.heuristic(taxis[y].taxiPosition, cus_waiting.peek().current_node)){
                            t = taxis[y];
                            assigned = true;
                            
                        }
                    }
                }
                if(assigned){
                    t.served = cus_waiting.poll();
                    t.path = astar.aStar(t.taxiPosition, t.served.current_node);
                } else {
                    break;
                }
            }
            
            //Check for non-idle taxi
            for(int y = 0; y < Input.number_of_taxis; y++){
                if(!taxis[y].path.isEmpty()){
                  taxis[y].taxiPosition = taxis[y].path.pop();
                  output.taxiGoTo(y, taxis[y].taxiPosition);
                }
                else if(!taxis[y].isIdle()){
                    doFunction(taxis[y], y);
                }
            }
            output.sendOutput();
        }
    }
    
    public static void main(String[] args) {
        (new Bruwmbruwm()).run();
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
    
    public void doFunction(Taxi t, int Id){
         //Will do the function the taxi is set to do
         switch (t.function){
             case "PICK":
                 //Pick up the passenger at the current node
                 //And determine the path from the current node to the destination
                 output.pickUpPassenger(Id, t.served.goal_node);
                 t.path = astar.aStar(t.taxiPosition, t.served.goal_node);
                 t.function = "DROP";
                 return;
                    
             case "DROP":
                 //Drop off the passenger
                 output.dropOffPassenger(Id, t.served.goal_node);
                 returnToHotspot(t);
                 return;
                 
             case "IDLE":
                 //do nothing wait for instructions
                 return;
                 
             default:
                 return;
         }   
     }
     
     public void returnToHotspot(Taxi t){
         int min_distance = Integer.MAX_VALUE;
         HotSpot nearest = hotspots[1];
         for(int x = 0; x < 1/*number of hotspots*/; x++){
             if (astar.h.heuristic(t.taxiPosition, 1/*hotspotlocation*/) < min_distance){
                 min_distance = astar.h.heuristic(t.taxiPosition, 1/*hotspotlocation*/);
                 nearest = hotspots[x];
             }
         }
         t.path = astar.aStar(t.taxiPosition, nearest.node.position);
         t.function = "idle";
         return;
     }
}
