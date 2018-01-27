import java.util.HashSet;
import java.util.Random;

public class Suchtbaum<T extends Comparable<T>> {

  public static void main(String[] args) {
    final int READERS = 30;
    final int WRITERS = 2;
    HashSet<Integer> testSet = new HashSet<>();
    Random random = new Random();
    int n = 10000;
    for (int i = 0; i < n; i++) {
      testSet.add(random.nextInt(20 * n));
    }
    Suchtbaum<Integer> suchti = new Suchtbaum<>();
    for (Integer i : testSet) {
      try {
        suchti.insert(i);
      } catch (InterruptedException e) {
      }
    }
    for (int i = 0; i < READERS; i++) {
      Thread t = new Thread(() -> {
        while (true) {
          try {
            suchti.toString();
            suchti.contains(42);
          } catch (InterruptedException e) {
          }
        }
      });
      t.setName(t.getName() + " Reader");
      t.start();
    }
    for (int i = 0; i < WRITERS; i++) {
      Thread writer = new Thread(() -> {
        int a = -new Random().nextInt(Integer.MAX_VALUE);
        while (true) {
          try {
            suchti.insert(a);
            Thread.sleep(1000);
            suchti.remove(a);
          } catch (InterruptedException e) {
          }
        }
      });
      writer.setName(writer.getName() + " Writer");
      writer.start();
    }
  }

  private class SuchtbaumElement {

    private T element;
    private SuchtbaumElement left = null;
    private SuchtbaumElement right = null;
    private SuchtbaumElement parent = null;

    private SuchtbaumElement(SuchtbaumElement left, SuchtbaumElement right,
        SuchtbaumElement parent, T element) {
      this.left = left;
      this.right = right;
      this.parent = parent;
      this.element = element;
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

    public SuchtbaumElement getParent() {
      return parent;
    }

    public void setParent(SuchtbaumElement parent) {
      this.parent = parent;
    }

    public void setElement(T element) {
      this.element = element;
    }

    public T getElement() {
      return this.element;
    }

  }

  private SuchtbaumElement root;
  private RWLock lock = new RWLock();

  public void insert(T element) throws InterruptedException {
    lock.startWrite();

    try {
      if (root == null) {
        root = new SuchtbaumElement(null, null, null, element);
        return;
      }
      SuchtbaumElement current = root;
      SuchtbaumElement parent = root;
      boolean left = false;
      while (current != null) {
        if (element.compareTo(current.getElement()) < 0) {
          parent = current;
          left = true;
          current = current.getLeft();
        } else if (element.compareTo(current.getElement()) > 0) {
          parent = current;
          left = false;
          current = current.getRight();
        } else {
          throw new RuntimeException(
              String.format("Element %s is already in the tree", element));
        }
      }
      SuchtbaumElement leaf = new SuchtbaumElement(null, null, parent, element);
      if (left) {
        parent.setLeft(leaf);
      } else {
        parent.setRight(leaf);
      }
    } finally {
      lock.endWrite();
    }
  }


  public boolean contains(T element) throws InterruptedException {
    lock.startRead();
    SuchtbaumElement parent = root;
    boolean found = false;
    while (parent != null) {
      int compare = element.compareTo(parent.getElement());
      if (compare == 0) {
        found = true;
      }
      parent = compare < 0 ? parent.getLeft() : parent.getRight();
    }
    lock.endRead();
    return found;
  }

  public void remove(T element) throws InterruptedException {
    lock.startWrite();
    try {
      remove(root, element);
    } finally {
      lock.endWrite();
    }
  }

  private void remove(SuchtbaumElement node, T element) {
    while (node != null) {
      SuchtbaumElement prev = node;
      SuchtbaumElement current = node;
      // find the element to delete
      while (current != null) {
        prev = current;
        if (element.compareTo(current.getElement()) < 0) {
          current = current.getLeft();
        } else if (element.compareTo(current.getElement()) > 0) {
          current = current.getRight();
        } else {
          break;
        }
      }
      if (prev.getElement().compareTo(element) != 0) {
        throw new RuntimeException(
            String.format("The Element %s is not in the tree", element));
      }
      current = prev;
      // handle the three possible scenarios for deletion
      if (current.getLeft() == null && current.getRight() == null) {
        replace(current, null);
        node = null;
      } else if (current.getLeft() == null && current.getRight() != null) {
        replace(current, current.getRight());
        node = null;
      } else if (current.getLeft() != null && current.getRight() == null) {
        replace(current, current.getLeft());
        node = null;
      } else {
        SuchtbaumElement predecessor = max(current.getLeft());
        current.setElement(predecessor.getElement());
        node = predecessor;
        element = predecessor.getElement();
      }
    }
  }

  private void replace(SuchtbaumElement node, SuchtbaumElement newNode) {
    if (node.getParent() != null) {
      if (node.getParent().getLeft() != null &&
          node.getParent().getLeft().getElement().compareTo(node.getElement()) == 0) {
        node.getParent().setLeft(newNode);
      } else {
        node.getParent().setRight(newNode);
      }
    } else {
      this.root = newNode;
      newNode.setParent(null);
    }
    if (newNode != null) {
      newNode.setParent(node.getParent());
    }
  }

  private SuchtbaumElement max(SuchtbaumElement root) {
    SuchtbaumElement prev = root;
    while (root != null) {
      prev = root;
      root = root.getRight();
    }
    return prev;
  }

  private void printSubtree(SuchtbaumElement root, StringBuilder sb) {
    if (root == null) {
      return;
    }
    sb.append(String.format("%s;\n", root.getElement()));
    if (root.getParent() != null) {
      sb.append(String.format("%s -> %s [label=parent];\n",
          root.getElement(), root.getParent().getElement()));
    }
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
    String res = "";
    try {
      lock.startRead();
      StringBuilder sb = new StringBuilder();
      sb.append("digraph G {\n");
      printSubtree(root, sb);
      sb.append("}\n");
      res = sb.toString();
      lock.endRead();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    return res;
  }

  class RWLock {

    private volatile int readers = 0;
    private boolean writer = false;

    public synchronized void startRead() throws InterruptedException {
      while (readers < 0 || writer) {
        System.out.printf("%s paused\n", Thread.currentThread().getName());
        wait();
      }
      readers++;
    }

    public synchronized void endRead() throws InterruptedException {
      readers--;
      if (readers == 0) {
        notifyAll();
      }
    }

    public synchronized void startWrite() throws InterruptedException {
      while (readers != 0) {
        System.out.printf("%s paused\n", Thread.currentThread().getName());
        writer = true;
        wait();
      }
      System.out.printf("%s continued\n", Thread.currentThread().getName());
      readers = -1;
    }

    public synchronized void endWrite() throws InterruptedException {
      readers = 0;
      writer = false;
      notifyAll();
    }

  }
}
