package Bruwmbruwm;

import java.util.Stack;
import java.util.ArrayList;

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
    public int number_nodes;
    int training_time;
    int total_time;   
    
    
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
        }
    }
    
    public static void main(String[] args) {
        (new Bruwmbruwm()).run();
    }
    
    
    /****************************************************************/
    /* Misc Methods *************************************************/
    /****************************************************************/
    public boolean inBetween(int x, int lower_bound, int upper_bound){
        if(x < lower_bound || x > upper_bound){
            return false;
        }
        else {
            return true;
        }
    }
    
    //Bfs algorithm
    public void BFS(Node start_node, Node end_node, Taxi taxi){
        for(int x = 0; x < number_nodes; x++){
            nodes[x].distannce = number_nodes;
        }
        max_distance = number_nodes;
        
        //the end and start are swapped, so the Stack doesn't go nuts
        BFSpathfinder(end_node, start_node, taxi);
        return;
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
                max_distance = nodes[current_node.neighbours[x]].distannce;
                taxi.path.push(nodes[current_node.neighbours[x]]);
                return true;
            }
        }
        return false;
    }
}
