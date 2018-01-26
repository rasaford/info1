
public class DoublyLinkedList {

  Entry head;
  int size;

  /**
   * constructor isEmpty DoublyLinkedList
   */
  public DoublyLinkedList() {
    //TODO
  }


  /**
   * Returns the number of elements in this list.
   *
   * @return number of elements in this list
   */
  public int size() {
    //TODO
    return -1;
  }

  /**
   * Appends a new element with value info to the end of this list
   *
   * @param info value of the new element
   */
  public void add(int info) {
    //TODO
  }

  /**
   * Inserts a new element with value info at the specified position in this list.
   *
   * @param index position where the element is inserted, from 0 ... list.size()
   * @param info value of the new element
   */
  public void add(int index, int info) {
    //TODO
  }


  /**
   * Removes and returns the element at position index from this list.
   *
   * @param index position of the element that is removed
   * @return value of the removed element
   */
  public int remove(int index) {
    //TODO
    return -1;
  }

  /**
   * shifts the list the specified number of positions to the left example: [1,5,6,7]
   * ---shift(2)---> [6,7,1,5]
   *
   * @param index number of position to shift, from 0 to size-1
   */
  public void shiftLeft(int index) {
    //TODO
  }

  @Override
  public String toString() {
    String out = "[";
    if (head != null) {
      out += head.elem;
      Entry tmp = head.next;
      while (tmp != null) {
        out = out + "," + tmp.elem;
        tmp = tmp.next;
      }
    }
    out += "]";
    return out;
  }

  public static void main(String[] args) {
    //TODO
  }

  class Entry {

    Entry prev;
    Entry next;
    int elem;

    public Entry(Entry prev, Entry next, int elem) {
      this.prev = prev;
      this.next = next;
      this.elem = elem;
    }

  }
}
 
