/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package Bruwmbruwm;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;
/**
 *
 * @author s156035
 */

enum State {
    IDLE, PICK, DROP
}

public class Taxi {
    Stack<Integer> path = new Stack();
    int taxi_id;
    Queue<Customer> customer_queue;
    Customer pick_up;
    int taxiPosition;
    //Node node[];    
    State function;
    
    public Taxi(int id){
        taxi_id = id;
        this.function = State.IDLE;
        customer_queue = new LinkedList<>();
    }
    
    public boolean isIdle (){
        return function == State.IDLE;
    }
    
    //Pop top of stack
    public int move(){
        return (int)(path.pop());
    }
}
