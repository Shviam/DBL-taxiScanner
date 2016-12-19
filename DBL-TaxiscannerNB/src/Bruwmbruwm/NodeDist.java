/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author s151914
 */
public class NodeDist implements Comparable<NodeDist>{
    int i, d;
    NodeDist(int index, int dis){
        i = index;
        d = dis;
    }
    @Override
    public int compareTo(NodeDist other){
        return (d-other.d);
    }
}
