/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Bruwmbruwm;

import java.util.ArrayList;

/**
 *
 * @author -Jur-
 */
public class Input {
    //This class reads the preamble and makes all the variables of it public to all other classes.
    //Most of the variables are final...
    //Preamble classes
    TaxiScanner input = TaxiScanner.getInstance();

    
    // (Final) Preamble variables
    public static float alpha;
    public static int max_drop_off_time;
    public static int number_of_taxis, seats;
    public static int number_nodes;
    public static int training_time;
    public static int total_time;
    public static int preamble_length;
    public static Node[] nodes;
    public static Taxi[] taxis;
    public static ArrayList<Customer> customers = new ArrayList<>();
    
        String temp;
    
    // Methods to get changeable variables, after updating use the set method so it is updated in all classes
    public ArrayList<Customer> getCustomerList(){
        return customers;
    }
    public void setCustomerList(ArrayList<Customer> customers){
        Input.customers = customers;
    }
    public Taxi[] getTaxiArray(){
        return taxis;
    }
    public void setTaxiArray(Taxi[] taxis){
        Input.taxis = taxis;
    }
    public Node[] getNodeArray(){
        return nodes;
    }
    public void setNodeArray(Node[] nodes){
        Input.nodes = nodes;
    }
    
    //Read the preamble and set the corresponding variables
    public void readPreamble(){
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
                        taxis[y] = new Taxi(y);
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
                temp = input.nextLine();
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
