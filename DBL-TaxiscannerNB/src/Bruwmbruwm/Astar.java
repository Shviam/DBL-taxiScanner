/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Bruwmbruwm;

import java.util.Comparator;
import java.util.PriorityQueue;

/**
 *
 * @author s155650
 */
public class Astar{
        Comparator<Node> comparator = new AstarComparator(); 
        PriorityQueue<Node> queue = 
        new PriorityQueue<Node>(10, comparator);
}





