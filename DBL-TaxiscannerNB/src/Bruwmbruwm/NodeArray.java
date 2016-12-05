/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Bruwmbruwm;

/**
 *
 * @author -Jur-
 */
public class NodeArray {
    private Node[] nodes;
    private int number_nodes;
    
    public int getNumberNodes(){
        return number_nodes;
    }
    public void setNumberNodes(int number_nodes){
        this.number_nodes = number_nodes;
    }
    public Node[] getNodeArray(){
        return nodes;
    }
    public void setNodeArray(Node[] nodes){
        this.nodes = nodes;
    }
}
