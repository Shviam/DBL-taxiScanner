package Bruwmbruwm;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Queue;
import java.util.Stack;

/**
 *
 * @author s156035
 */
public class Bruwmbruwm {
    HotSpot[] hotspots = new HotSpot[10];
    //HotSpot[] hotspots2 = new HotSpot[10];
    //classes
    Input input = new Input();
    Output output = new Output();
    TaxiScanner taxiscanner = TaxiScanner.getInstance();
    
    //initialize variables
    int training_time;
    int total_time;
    public int[] frequence;  //How often it is accessed
    Node[] nodes;
    Taxi[] taxis;
    ArrayList<Customer> customers;
    Queue<Customer> cus_waiting = new LinkedList<>();
    LinkedList<Taxi> taxi_idle = new LinkedList<>();
    
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
        Taxi[] taxis = input.getTaxiArray();
        for (Taxi taxi : taxis) {
            taxi_idle.add(taxi);
        }
        training_time = Input.training_time;
        total_time = Input.total_time;
        customers = input.getCustomerList();
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
                ListIterator<Taxi> it = taxi_idle.listIterator();
                ListIterator<Taxi> max = taxi_idle.listIterator();
                Taxi t = new Taxi();
                boolean assigned = false;
                if(it.hasNext()){
                    t = it.next();
                    max = it;
                    assigned = true;
                }
                while(it.hasNext()) {
                    Taxi s = it.next();
                    if (astar.h.heuristic(t.taxiPosition, cus_waiting.peek().current_node) > astar.h.heuristic(s.taxiPosition, cus_waiting.peek().current_node)) {
                        t = s;
                        max = it;
                    }
                }
                if (assigned){
                    t.served = cus_waiting.poll();
                    t.path = astar.aStar(t.taxiPosition, t.served.current_node);
                    t.function = State.PICK;
                    max.remove();
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
     int storeDegree[] = new int[Input.number_nodes];
     public int[] getHighestDegree(int degreeNumber){
        int degree;
        int index ;
        int large[] = new int[5];
        int max;
        for(int i=0;i<Input.number_nodes;i++){
            storeDegree[i] = nodes[i].di;           
        }
        for(int j=1; j<=5;j++){
            max = storeDegree[0];
            index = 0;
            for(int i=0;i < storeDegree.length;i++){
                if(max < storeDegree[i]){
                    max = storeDegree[i];
                    index = i;
                }
            }
            large[j] = max;
            storeDegree[index]= Integer.MIN_VALUE;
        }
        return large;
    }
     
     public void hotSpotFreq(){
         int[] hotspotindices = getHighestFreq(5);                
         for(int i=0;i<5;i++){
             hotspots[i] = new HotSpot(nodes[hotspotindices[i]]);
         }
     }
     
     public void hotSpotDegree(){
         int [] hotspotindices = getHighestDegree(5);
         for(int i=5;i<10;i++){
             hotspots[i] = new HotSpot(nodes[hotspotindices[i]]);
         }
     }
    
    public void doFunction(Taxi t, int Id){
         //Will do the function the taxi is set to do
         switch (t.function){
             case PICK:
                 //Pick up the passenger at the current node
                 //And determine the path from the current node to the destination
                 output.pickUpPassenger(Id, t.served.goal_node);
                 t.path = astar.aStar(t.taxiPosition, t.served.goal_node);
                 t.function = State.DROP;
                 return;
                    
             case DROP:
                 //Drop off the passenger
                 output.dropOffPassenger(Id, t.served.goal_node);
                 returnToHotspot(t);
                 return;
                 
             case IDLE:
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
         t.function = State.IDLE;
         taxi_idle.add(t);
         return;
     }
}
