/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Bruwmbruwm;

import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Stack;

/**
 *
 * @author -Jur-
 */
public class Astar {
    //classes
    Heuristic h;
    
    //final variables
    int number_nodes;
    //changeable variables
    Node[] nodes;
    
    Astar(Node[] nodes, int number_nodes){
        this.nodes = nodes;
        this.number_nodes = number_nodes;
        this.h = new Heuristic(this.nodes, this.number_nodes);
    }
    
    Stack<Integer> aStar(int source, int goal){
        PriorityQueue<NodeDist> Q = new PriorityQueue<>();
        ArrayList<Integer> N = new ArrayList<>();
        //resetting the node values
        /*for (Node node : nodes) {
            node.distance = Integer.MAX_VALUE;
            node.visited = false;
        }*/
        //setting the source distance to 0 and pushing to queue
        nodes[source].distance = 0;
        N.add(source);
        Q.add(new NodeDist(source, 0));
        while(!Q.isEmpty()){
            NodeDist nd = Q.poll();
            //in case of multiple entries in queue, this node is already explored
            if(nodes[nd.i].visited) continue;
            nodes[nd.i].visited = true;
            //if we reached the goal, stop searching
            if(nd.i == goal) break;
            
            //check every neighbour of k
            for(int u : nodes[nd.i].neighbours){
                //if already explored, skip it
                if(nodes[u].visited) 
                    continue;
                //update distance to source, and fscore = dis to source + heuristic to end
                int newDis = nodes[nd.i].distance + 1;
                int fscore = newDis + h.heuristic(u, goal);
                
                //in case there exist a faster path to u
                if(newDis < nodes[u].distance){
                    if(nodes[u].distance == Integer.MAX_VALUE){
                        N.add(u);
                    }
                    //set node values and add to queue
                    nodes[u].distance = newDis;
                    nodes[u].parent = nd.i;
                    Q.add(new NodeDist(u, fscore));
                }
            }
        }
        //find the path by backtracking from the goal
        int k = goal;
        Stack<Integer> p = new Stack<>();
        while(k != source){
            p.add(k);
            k = nodes[k].parent;
        }
        
        for (int n : N) {
            nodes[n].distance = Integer.MAX_VALUE;
            nodes[n].visited = false;
        }
        
        return p;
    }
}
