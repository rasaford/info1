public class Suchtbaum<T extends Comparable<T>> {

  private class SuchtbaumElement {

    private T element;
    private SuchtbaumElement left = null;
    private SuchtbaumElement right = null;

    private SuchtbaumElement(SuchtbaumElement left, SuchtbaumElement right,
        T element) {
      this.left = left;
      this.right = right;
      this.element = element;
    }

    public T getElement() {
      return element;
    }

    public SuchtbaumElement getLeft() {
      return left;
    }

    public void setLeft(SuchtbaumElement left) {
      this.left = left;
    }

    public SuchtbaumElement getRight() {
      return right;
    }

    public void setRight(SuchtbaumElement right) {
      this.right = right;
    }

  }

  private SuchtbaumElement root;
  private volatile boolean writingData = false;
  private AtomicInteger readCounter = new AtomicInteger();

  public synchronized void insert(T element) throws InterruptedException {
    writingData = true;
    while (readCounter.get() != 0) {
      wait();
    }

    SuchtbaumElement node = search(root, element);
    SuchtbaumElement newNode = new SuchtbaumElement(null, null, element);
    if (node == null) {
      this.root = newNode;
    } else if (node.getElement().compareTo(element) > 0) {
      node.setLeft(newNode);
    } else if (node.getElement().compareTo(element) < 0) {
      node.setRight(newNode);
    } else {
      writingData = false;
      notify();
      throw new RuntimeException(String.format("%s is already contained within the tree", element));
    }
    writingData = false;
    notify();
  }

  public boolean contains(T element) throws InterruptedException {
    while (writingData) {
      wait();
    }
    readCounter.increment();
    SuchtbaumElement node = search(root, element);
    boolean result = node != null &&
        (node.getLeft() != null && node.getLeft().getElement().compareTo(element) == 0
            || node.getRight() != null && node.getRight().getElement().compareTo(element) == 0);
    readCounter.decrement();
    return result;
  }

  public synchronized void remove(T element) throws InterruptedException {
    writingData = true;
    while (readCounter.get() != 0) {
      wait();
    }

    SuchtbaumElement parent = search(root, element);
    SuchtbaumElement toDelete;
    if ((toDelete = parent.getLeft()) != null &&
        element.compareTo(parent.getLeft().getElement()) == 0) {
      if (toDelete.getLeft() == null) {
        parent.setLeft(toDelete.getRight());
      } else {
        leftDelete(parent, toDelete);
      }
    } else if ((toDelete = parent.getRight()) != null &&
        element.compareTo(parent.getRight().getElement()) == 0) {
      if (toDelete.getLeft() == null) {
        parent.setRight(toDelete.getRight());
      } else {
        leftDelete(parent, toDelete);
      }
    } else {
      writingData = false;
      notify();
      throw new RuntimeException(
          String.format("%s is not in the tree and cannot be deleted", element));
    }
    writingData = false;
    notify();
  }

  private void leftDelete(SuchtbaumElement parent, SuchtbaumElement toDelete) {
    SuchtbaumElement max = max(toDelete.getLeft());
    max.setLeft(toDelete.getLeft());
    max.setRight(toDelete.getRight());
    parent.setLeft(max);
  }


  private SuchtbaumElement max(SuchtbaumElement root) {
    SuchtbaumElement prev = root;
    while (root != null) {
      prev = root;
      root = root.getRight();
    }
    return prev;
  }

  /**
   * @return the parent of the found node. Is null if the root is null.
   */
  private SuchtbaumElement search(SuchtbaumElement root, T element) {
    SuchtbaumElement parent = root;
    while (root != null) {
      int compare = element.compareTo(root.getElement());
      if (compare == 0) {
        return parent;
      }
      parent = root;
      root = compare < 0 ? root.getLeft() : root.getRight();
    }
    return parent;
  }

  private void printSubtree(SuchtbaumElement root, StringBuilder sb) {
    if (root == null) {
      return;
    }
    sb.append(String.format("%s;\n", root.getElement()));
    if (root.getLeft() != null) {
      sb.append(String.format("%s -> %s [label=left];\n",
          root.getElement(), root.getLeft().getElement()));
      printSubtree(root.getLeft(), sb);
    }
    if (root.getRight() != null) {
      sb.append(String.format("%s -> %s [label=right];\n",
          root.getElement(), root.getRight().getElement()));
      printSubtree(root.getRight(), sb);
    }
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    try {
      while (writingData) {
        wait();
      }
      readCounter.increment();
      sb.append("digraph G {\n");
      printSubtree(root, sb);
      sb.append("}\n");
    } catch (InterruptedException e) {
    } finally {
      readCounter.decrement();
      notifyAll();
    }
    return sb.toString();
  }

  class AtomicInteger {

    private int counter;

    public synchronized void increment() {
      counter++;
    }

    public synchronized void decrement() {
      counter--;
    }

    public synchronized int get() {
      return counter;
    }

  }
}
