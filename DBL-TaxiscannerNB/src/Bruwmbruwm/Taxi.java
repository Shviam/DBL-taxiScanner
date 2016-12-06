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
    Stack path = new Stack();
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
        
    
    //Pop top of stack
    public int move(){
        return (int)(path.pop());
    }
}
