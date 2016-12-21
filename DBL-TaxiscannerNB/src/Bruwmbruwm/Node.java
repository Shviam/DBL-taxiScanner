/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author s156035
 */
class Node{
    int[] neighbours;
    public int di = 0; //amount of neighbour
    int position;
    
    int frequence = 0;
    
    //needed for A star search
    int distance;
    int fscore;
    boolean visited;
    boolean waypoint;
    int parent;

    public Node(int x){
        position = x;
        waypoint = false;
    }

    //initialise the array with the amount of edges specified in the Preamble
    public void initialise(int di){		
        neighbours = new int[di];
    }

    //Add an edge to the neighbours
    public void write_neighbour(int neighbour){		
        neighbours[di] = neighbour;
        di++;		
    }
}
