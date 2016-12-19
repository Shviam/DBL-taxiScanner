/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import static java.lang.Integer.max;
import static java.util.Arrays.fill;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

/**
 *
 * @author -Jur-
 */
public class Heuristic {
    //classes
    Input nodeArray;
    
    //heuristic variables
    Node[] nodes;
    int[][] heuristicDis;
    public int number_waypoints = 16;
    int number_nodes;
    
    //initializing the waypoint array for heuristic function
    Heuristic(Input in){
        //getting a random start waypoint
        this.nodeArray = in;
        nodes = nodeArray.getNodeArray();
        number_nodes = Input.number_nodes;
        
        Random rand = new Random();
        int start = rand.nextInt(number_nodes);
        
        //initialize waypoint array
        heuristicDis = new int[number_waypoints+5][number_nodes+5];
        
        //run BFS for all waypoints
        for(int i=0; i<number_waypoints; i++){
            //make queue for the BFS
            Queue<Integer> Q = new LinkedList<>();
            Q.add(start);
            nodes[start].waypoint = true;
            
            //fill array with -1, meaning unvisited
            fill(heuristicDis[i], -1);
            //set start distance to 0
            heuristicDis[i][start] = 0;
            while(!Q.isEmpty()){
                int u = Q.remove();
                for(int e : nodes[u].neighbours){
                    //if neighbour is unvisited
                    if(heuristicDis[i][e] == -1){
                        heuristicDis[i][e] = heuristicDis[i][u] + 1;
                        Q.add(e);
                        
                        //get the next start waypoint as farthest from the current
                        if(heuristicDis[i][e] > heuristicDis[i][start] && !nodes[e].waypoint){
                            start = e;
                        }
                    }
                }
            }
        }
    }
    
    int heuristic(int A, int B){
        int dis = 0;
        
        for(int i=0; i<number_waypoints; i++){
            //triangle inequality
            int wayDis = max(heuristicDis[i][A] - heuristicDis[i][B], heuristicDis[i][B] - heuristicDis[i][A]);
            //get the max from all the waypoints
            dis = max(dis, wayDis);
        }
        return dis;
    }
}
