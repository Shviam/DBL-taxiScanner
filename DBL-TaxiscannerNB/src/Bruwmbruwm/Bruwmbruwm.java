package Bruwmbruwm;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

/**
 *
 * @author s156035
 */
public class Bruwmbruwm {
    
    //hotspot variables
    int number_hotspots = 10;
    HotSpot[] hotspots = new HotSpot[number_hotspots];
    //HotSpot[] hotspots2 = new HotSpot[10];
    //classes

    Output output = new Output();
    TaxiScanner taxiscanner = TaxiScanner.getInstance();

    //initialize variables
    float alpha;
    int max_drop_off_time;
    int number_of_taxis, seats;
    int number_nodes;
    int training_time;
    int total_time;
    int preamble_length;
    Node[] nodes;
    Taxi[] taxis;
    ArrayList<Customer> customers = new ArrayList<>();

    public int[] frequence;  //How often a node (sorted by index) is accessed
    Queue<Customer> cus_waiting = new LinkedList<>();
    int idle_taxis;
    
    //Temporary globals
    int max_distance;
    boolean ended;
    String temp;
    Astar astar;

    public void run() {
        /****************************************************/
        /* Read preamble ************************************/
        /****************************************************/
        readPreamble();
        astar = new Astar(nodes,number_nodes);
        taxiscanner.println("c");
        frequence = new int[number_nodes];

        /****************************************************/
        /* Process Training Time ****************************/
        /****************************************************/
        for (int x = 0; x < training_time; x++) {
            temp = taxiscanner.nextLine();
            int whitespace = temp.indexOf(" ");
            int amount_of_customers;
            if(whitespace != -1){
                amount_of_customers = Integer.parseInt(temp.substring(0, whitespace));
            }
            else{
                amount_of_customers = Integer.parseInt(temp.substring(0, temp.length()));
            }
            for (int y = 0; y < amount_of_customers; y++) {
                int end_pos;
                int start_pos;
                temp = temp.substring(whitespace + 1);
                whitespace = temp.indexOf(" ");
                start_pos = Integer.parseInt(temp.substring(0, whitespace));

                temp = temp.substring(whitespace + 1);
                whitespace = temp.indexOf(" ");
                
                if(whitespace != -1){
                    end_pos = Integer.parseInt(temp.substring(0, whitespace));
                }
                else{
                    end_pos = Integer.parseInt(temp.substring(0, temp.length()));
                }

                //Look at the path the customer will take and assign weight to the nodes along it
                weighPaths(new Customer(start_pos, end_pos));
            }
            if(x == training_time - 1){
                //Distribute taxis
                distributeTaxis();
            }
            output.sendOutput();
        }
        /****************************************************/
        /* BWRUMBWRUM MOTHERFUCKERS *************************/
        /****************************************************/
        
        
        
        for (int x = training_time; x < total_time; x++) {
        //while(taxiscanner.hasNextLine()){
            
            // Process clients into the arraylist
            temp = taxiscanner.nextLine();
            int whitespace = temp.indexOf(" ");
            int amount_of_customers;
            if(whitespace != -1){
                amount_of_customers = Integer.parseInt(temp.substring(0, whitespace));
            }
            else{
                amount_of_customers = Integer.parseInt(temp.substring(0, temp.length()));
            }
            for (int y = 0; y < amount_of_customers; y++) {
                int end_pos;
                int start_pos;
                temp = temp.substring(whitespace + 1);
                whitespace = temp.indexOf(" ");
                start_pos = Integer.parseInt(temp.substring(0, whitespace));

                temp = temp.substring(whitespace + 1);
                whitespace = temp.indexOf(" ");
                
                if(whitespace != -1){
                    end_pos = Integer.parseInt(temp.substring(0, whitespace));
                }
                else{
                    end_pos = Integer.parseInt(temp.substring(0, temp.length()));
                }
                cus_waiting.add(new Customer(start_pos, end_pos));
            }
            /*
            //assign waiting customers to taxis
            assignCustomer();

            //Check for non-idle taxi
            checkTaxis();

            }*/
            assignCustomer();
            processMoves();
            output.sendOutput();
        }
        while(idle_taxis < number_of_taxis || !cus_waiting.isEmpty()){
            assignCustomer();
            processMoves();
            //System.out.println("waiting " + cus_waiting.size() + "idle " + idle_taxis);
            output.sendOutput();
        }
    }
    
    public static void main(String[] args) {
        (new Bruwmbruwm()).run();
    }

    //assign waiting customers to taxis
    void assignCustomer() {
        while (!cus_waiting.isEmpty()) {
            //ListIterator<Taxi> max = taxi_idle.listIterator();
            Taxi t = new Taxi(1); //The initialisation is just a dummy so the code doesn't complain. It's a feature.
            boolean assigned = false;
            int max_cus = seats - 1;
            int smallest_path = Integer.MAX_VALUE;

            for(int i=0; i<number_of_taxis; i++){
                if(taxis[i].function != State.PICK && taxis[i].customer_queue.size() <= max_cus){
                    assigned = true;
                    if(taxis[i].customer_queue.size() < max_cus){
                        max_cus = taxis[i].customer_queue.size();
                        smallest_path = Integer.MAX_VALUE;
                    }
                    int extended;
                    if(!taxis[i].customer_queue.isEmpty()){
                        extended = extendedPath(taxis[i], cus_waiting.peek().current_node);
                    } else {
                        extended = astar.h.heuristic(taxis[i].taxiPosition, cus_waiting.peek().current_node);
                    }
                    if(extended < smallest_path){
                        t = taxis[i];
                        smallest_path = extended;
                    }
                }
            }
            if(!t.customer_queue.isEmpty() && smallest_path > astar.h.heuristic(t.taxiPosition, t.customer_queue.peek().goal_node)){
                assigned = false;
            }
                
            if (assigned) { 
                //t.customer_queue.add(cus_waiting.poll());
                t.pick_up = cus_waiting.poll();
                t.path = astar.aStar(t.taxiPosition, t.pick_up.current_node);
                //System.out.println(t.taxi_id + " heejo " + t.pick_up.current_node + " " + t.pick_up.goal_node);
                t.function = State.PICK;
                //idle_taxis--;
                //max.remove();
            } else {
                break;
            }
        }
    }
    
    int extendedPath(Taxi t, int des){
        int cur_path = astar.h.heuristic(t.taxiPosition, t.customer_queue.peek().goal_node);
        int ext_path = astar.h.heuristic(t.taxiPosition, des) + astar.h.heuristic(des, t.customer_queue.peek().goal_node);
        return ext_path - cur_path;
    }
    
    void processMoves() {
        idle_taxis = 0;
        for (int y = 0; y < number_of_taxis; y++) {

            if (!taxis[y].path.isEmpty()) {
                taxis[y].taxiPosition = taxis[y].path.pop();
                output.taxiGoTo(y, taxis[y].taxiPosition);
            } else if (!taxis[y].isIdle()) {
                doFunction(taxis[y], y);
            } else {
                idle_taxis++;
            }
        }
    }

    public void weighPaths(Customer passenger) {
        Stack<Integer> path = astar.aStar(passenger.current_node, passenger.goal_node);
        while (!path.isEmpty()) {
            frequence[path.pop()]++;
        }
    }

    public int[] getHighestFreq(int amount) {
        int[] out = new int[amount];
        for (int x = 0; x < amount; x++) {
            int highest = 0;
            for (int y = 0; y < number_nodes; y++) {
                if (frequence[y] > frequence[highest]) {
                    highest = y;
                }
            }
            out[x] = highest;
            frequence[highest] = 0;
        }
        return out;
    }
    
    public int[] getHighestDegree(int degreeNumber) {
        int storeDegree[] = new int[number_nodes];

        int index ;
        int large[] = new int[5];
        int max;
        for(int i=0;i<number_nodes;i++){
            storeDegree[i] = nodes[i].di;           
        }
        for(int j=0; j<5;j++){
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

    public void hotSpotFreq() {
        int[] hotspotindices = getHighestFreq(5);
        for (int i = 0; i < 5; i++) {
            hotspots[i] = new HotSpot(nodes[hotspotindices[i]]);
        }
    }

    public void hotSpotDegree() {
        int[] hotspotindices = getHighestDegree(5);
        for (int i = 5; i < 10; i++) {
            hotspots[i] = new HotSpot(nodes[hotspotindices[i-5]]);
        }
    }

    public void doFunction(Taxi t, int Id) {
        //Will do the function the taxi is set to do
        switch (t.function) {
            case PICK:
                //Pick up the passenger at the current node
                //And determine the path from the current node to the destination
                output.pickUpPassenger(Id, t.pick_up.goal_node);
                t.customer_queue.add(t.pick_up);
                t.path = astar.aStar(t.taxiPosition, t.customer_queue.peek().goal_node);
                t.function = State.DROP;
                break;

            case DROP:
                //Drop off the passenger
                output.dropOffPassenger(Id, t.customer_queue.peek().goal_node);
                t.customer_queue.poll();
                if(t.customer_queue.isEmpty()){
                    setIdle(t);
                    if(cus_waiting.isEmpty()) returnToHotspot(t);
                } else {
                    processCustomer(t);
                }
                break;

            case IDLE:
                //do nothing wait for instructions
                break;
        }
    }

    public void returnToHotspot(Taxi t) {
        int min_distance = Integer.MAX_VALUE;
        HotSpot nearest = hotspots[0];
        for (int x = 0; x < number_hotspots; x++) {
            if (astar.h.heuristic(t.taxiPosition, hotspots[x].node.position) < min_distance) {
                min_distance = astar.h.heuristic(t.taxiPosition, hotspots[x].node.position);
                nearest = hotspots[x];
            }
        }
        t.path = astar.aStar(t.taxiPosition, nearest.node.position);
    }
    
    public void setIdle(Taxi t){
        t.function = State.IDLE;
        idle_taxis++;
    }
    
    public void distributeTaxis(){
        //Establish hotspots
        hotSpotFreq();
        hotSpotDegree();
        
        for(int x = 0; x < number_of_taxis; x++){
            taxis[x].taxiPosition = hotspots[x%number_hotspots].node.position;
            output.taxiSetPosition(x, taxis[x].taxiPosition);
        }
    }
    
    public void processCustomer(Taxi taxi){
        taxi.path = astar.aStar(taxi.taxiPosition, taxi.customer_queue.peek().current_node);
        taxi.function = State.DROP;
    }
    
    //Read the preamble and set the corresponding variables
    public void readPreamble(){
        preamble_length = Integer.parseInt(taxiscanner.nextLine());
        
        for (int x = 1; x < preamble_length; x++){
            //Read the preamble
            switch(x){
                case 1:
                    alpha = Float.parseFloat(taxiscanner.nextLine());
                    break;
                case 2:
                    max_drop_off_time = Integer.parseInt(taxiscanner.nextLine());
                    break;
                case 3:
                    temp = taxiscanner.nextLine();
                    
                    //Find end of first integer
                    int whitespace = temp.indexOf(" ");
                    number_of_taxis = Integer.parseInt(temp.substring(0, whitespace)); 
                    
                    taxis = new Taxi[number_of_taxis];
                    for(int y = 0; y < number_of_taxis; y++){
                        taxis[y] = new Taxi(y);
                    }
                    
                    seats = Integer.parseInt(temp.substring(whitespace+1, temp.length()));
                    break;
                case 4:
                    number_nodes = Integer.parseInt(taxiscanner.nextLine());
                    nodes = new Node[number_nodes];
                    for(int y = 0; y < number_nodes; y++){
                        nodes[y] = new Node(y);
                    }
                    break;
            }
            
            if(inBetween(x, 5, 5+number_nodes)){
                //Take the number up to the first whitespace, and then delete it.
                temp = taxiscanner.nextLine();
                int whitespace = temp.indexOf(" ");
                int size = Integer.parseInt(temp.substring(0, whitespace)); 
                temp = temp.substring(whitespace+1, temp.length());

                //initialise node
                nodes[x-5].initialise(size);

                for(int y = 0; y < size; y++){
                    whitespace = temp.indexOf(" ");
                    int neighbour = 0;
                    if(whitespace != -1){
                        neighbour = Integer.parseInt(temp.substring(0, whitespace));
                    }
                    else{
                        neighbour = Integer.parseInt(temp.substring(0, temp.length()));
                    }
                    temp = temp.substring(whitespace+1, temp.length());

                    nodes[x-5].write_neighbour(neighbour);
                }     
            }
            if(x == 4+number_nodes){
                temp = taxiscanner.nextLine();
                int whitespace = temp.indexOf(" ");
                training_time = Integer.parseInt(temp.substring(0, whitespace));
                total_time = Integer.parseInt(temp.substring(whitespace+1, temp.length()));
            }
            
            
        }
    }
    
    
    //Quality of life methods    
    public boolean inBetween(int x, int lower_bound, int upper_bound){
        return !(x < lower_bound || x > upper_bound);
    }
}

