/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bruwmbruwm;

/**
 *
 * @author s156035
 */
class Node{
    int[] neighbours;
    int i = 0;
    int position;

    public Node(int x){
        position = x;
    }

    //initialise the array with the amount of edges specified in the Preamble
    public void initialise(int di){		
        neighbours = new int[di];
        return;
    }

    //Add an edge to the neighbours
    public void write_neighbour(int neighbour){		
        neighbours[i] = neighbour;
        i++;						//increse index
        return;
    }
}