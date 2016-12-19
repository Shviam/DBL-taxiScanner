/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.PriorityQueue;
import java.util.Stack;

/**
 *
 * @author -Jur-
 */
public class Astar {
    //classes
    Heuristic h;
    Input nodeArray;
    
    //final variables
    int number_nodes = Input.number_nodes;
    //changeable variables
    Node[] nodes;
    
    Astar(Input in){
        this.nodeArray = in;
        this.h = new Heuristic(in);
        nodes = nodeArray.getNodeArray();
    }
    
    Stack<Integer> aStar(int source, int goal){
        PriorityQueue<NodeDist> Q = new PriorityQueue<>();
        //resetting the node values
        for (Node node : nodes) {
            node.distance = Integer.MAX_VALUE;
            node.visited = false;
        }
        //setting the source distance to 0 and pushing to queue
        nodes[source].distance = 0;
        Q.add(new NodeDist(source, h.heuristic(source, goal)));
        while(!Q.isEmpty()){
            NodeDist nd = Q.poll();
            int k = nd.i;
            int d = nd.d;
            //in case of multiple entries in queue, this node is already explored
            if(nodes[k].visited) continue;
            nodes[k].visited = true;
            //if we reached the goal, stop searching
            if(k == goal) break;
            
            //check every neighbour of k
            for(int u : nodes[k].neighbours){
                //if already explored, skip it
                if(nodes[u].visited) 
                    continue;
                //update distance to source, and fscore = dis to source + heuristic to end
                int newDis = nodes[k].distance + 1;
                int fscore = newDis + h.heuristic(u, goal);
                
                //in case there exist a faster path to u
                if(newDis < nodes[u].distance){
                    //set node values and add to queue
                    nodes[u].distance = newDis;
                    nodes[u].parent = k;
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
        return p;
    }
}
