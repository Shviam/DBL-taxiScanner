/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Bruwmbruwm;
import java.util.Stack;
/**
 *
 * @author s156035
 */
public class Taxi {
    Stack<Integer> path = new Stack();
    int taxi_id;
    Customer served;
    Node destination;
    int taxiPosition;
    //Node node[];    
    int storeDegree[];
    
    
    public void Taxi(int id){
        taxi_id = id;
    }
    
    public boolean isIdle (){
        if(destination != null){
            return true;
        }
        else{
            return false;
        }
    }
      
        
    
    //Pop top of stack
    public int move(){
        return (int)(path.pop());
    }
}
