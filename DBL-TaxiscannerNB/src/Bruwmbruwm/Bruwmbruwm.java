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
    
    //changeable variables
    Node[] nodes = input.getNodeArray();
    Taxi[] taxis = input.getTaxiArray();
    ArrayList<Customer> customers = input.getCustomerList();
    
    //Temporary globals
    int max_distance;
    boolean ended;
    String temp;
    /*
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
    */
    
    public void run(){
        input.readPreamble();
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
    
    
    

    
}
