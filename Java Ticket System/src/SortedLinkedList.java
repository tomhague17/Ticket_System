import java.util.LinkedList;

/**
 * A class representing a subclass of the LinkedList class, to help us create SortedLinkedLists, where items in each
 * list will be ordered lexicographically.
 * If an item already exists, it won't be added again.
 * @author Thomas Hague
 * @param <E> (represents any type that can extend the comparable class)
 */

public class SortedLinkedList<E extends Comparable<? super E>> extends LinkedList<E> {

    /**
     * A method for inserting items from the input file into our SortedLinkedLists, in ascending lexicographic order.
     * If an item already exists in the SortedLinkedList, the method exits.
     * Otherwise, an item is inserted directly before the lowest item that is lexicographically greater.
     * @param e (item)
     */
    public void insert(E e) {
        int item;
        boolean itemExists = false;
        for (item = 0; item < this.size(); item++) {
            if (get(item).compareTo(e) == 0) { // 0 is returned, two items being compared are the same
                itemExists = true;
                break;
            } else if (get(item).compareTo(e) > 0) { // break out the loop when we find the spot to insert
                break;
            }
        }
        if (!itemExists) { // if item doesn't exist, add it at the break-out spot.
            super.add(item, e);
        }
    }
}


