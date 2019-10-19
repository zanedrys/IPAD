/**
 * 
 * diewald_CV_kit v1.1
 * 
 * this library provides simple tools needed in computer-vision.
 * 
 * 
 * 
 *   (C) 2012    Thomas Diewald
 *               http://www.thomasdiewald.com
 *   
 *   last built: 12/13/2012
 *   
 *   download:   http://thomasdiewald.com/processing/libraries/diewald_CV_kit/
 *   source:     https://github.com/diwi/diewald_CV_kit 
 *   
 *   tested OS:  osx,windows
 *   processing: 1.5.1, 2.07
 *
 *
 *
 *
 * This source is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This code is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 * 
 * A copy of the GNU General Public License is available on the World
 * Wide Web at <http://www.gnu.org/copyleft/gpl.html>. You can also
 * obtain it by writing to the Free Software Foundation,
 * Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 */



package diewald_CV_kit.utility;

import java.util.ArrayList;
import java.util.List;



/**
 * just a common double linked list.<br>
 * <br>
 * 
 * 
 * @author thomas diewald (c) 2011
 *
 */
public final class DoubleLinkedList<E>{
  private Node<E> first   = null;
  private Node<E> current = null;
  private int size = 0;
  private int current_position = 0;
  
  private Node<E> marker = null;
  
  public DoubleLinkedList(){
  }
  
  /**
   * add a new node-value to the list, AFTER the current position.
   * @param node_value the new node value.
   */
  public final void add(E node_value){
    if (current == null){
      current = new Node<E>(node_value);
      current.next = current;
      current.prev = current;
      current_position = 0;
      first = current;
      marker = first;
    } else {
      Node<E> tmp = current;
      current = new Node<E>(node_value);
      current.prev = tmp;
      current.next = tmp.next;
      current.next.prev = current;
      current.prev.next = current; 
      current_position++;
    }
    size++;
  }
  
  
  /**
   * removes the current node from the list.
   * @return the removed node.
   */
  public final Node<E> removeCurrentNode(){
    if( current == null )
      return null;
      
    Node<E> node_to_remove = current;
    
    if( --size == 0){
      clear();
      return node_to_remove;
    }
        
    current.prev.next = current.next;
    current.next.prev = current.prev;
    current = current.next;
    current_position--;

    if( node_to_remove == first)
      first = current;
    if( node_to_remove == marker )
      marker = current;
    return node_to_remove;
  }
  
  
  /**
   * clear the list.
   */
  public final void clear(){
    current = null;
    marker  = null;
    first   = null;
    size    = 0;
    current_position = 0;
  }
  
  
  /**
   * set a mark at the current node.
   * @return the list itself.
   */
  public final DoubleLinkedList<E> setMark(){
    marker = current;
    return this;
  }
  /**
   * move to previously set mark.
   * @return the list itself.
   */
  public final DoubleLinkedList<E> gotoMark(){
    current = marker;
    return this;
  }
  /**
   * returns the current node.
   * @return the current node.
   */
  public final Node<E> getCurrentNode(){
    return current;
  }
  /**
   * returns the current position as an integer.
   * @return the current position as an integer.
   */
  public final int getPos(){
    return current_position;
  }
  /**
   * returns the size of the list (number of nodes).
   * @return the size of the list (number of nodes).
   */
  public final int size(){
    return size;
  }
  
  
  /**
   * move to the first node in the list.
   * @return the list itself.
   */
  public final DoubleLinkedList<E> gotoFirst(){
    current = first;
    current_position = 0;
    return this;
  }
  
  /**
   * move to the next node in the list.
   * @return the list itself.
   */
  public final DoubleLinkedList<E> gotoNext(){
    if( current != null) {
      current = current.next;
      current_position++;
    }
    return this;
  }
  
  /**
   * move to the previous node in the list.
   * @return the list itself.
   */
  public final DoubleLinkedList<E> gotoPrev(){
    if( current != null) {
      current = current.prev;
      current_position--;
    }
    return this;
  }
  
  /**
   * convert this list to a new array. 
   * @param array null, or ideally an array with the size of the list (to save calculation time).
   * @return the new array.
   */
  @SuppressWarnings("unchecked")
  public final E[] toArray(E array[]){
    if( array == null || array.length != size )
      array = (E[])new Object[size];
    
    Node<E> tmp = current;
    gotoFirst().gotoPrev();
    for(int i = 0; i < size; i++)
      array[i] =  gotoNext().getCurrentNode().value;
    
    current = tmp;
    return array;
  }
  
  /**
   * convert this list to a new list.
   * @return the new list.
   */
  public final List<E> toList(){
    List<E> list = new ArrayList<E>(size);

    Node<E> tmp = current;
    gotoFirst().gotoPrev();
    for(int i = 0; i < size; i++)
      list.add(gotoNext().getCurrentNode().value);
    
    current = tmp;
    return list;
  }
  
  
  /**
   * each node has a link to a next node, and a link to a previous node, which actually defines a double-linked-list.
   * 
   * @author thomas
   *
   * @param <T>
   */
  public final class Node<T>{
    protected Node<T> next = null;
    protected Node<T> prev = null;
    protected T value;
    protected Node(T value){
      this.value = value;
    }
    /**
     * returns the nodes value.
     * @return the nodes value.
     */
    public final T get(){
      return value;
    }
  }
  
}




