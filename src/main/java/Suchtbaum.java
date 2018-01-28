import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Suchtbaum<T extends Comparable<T>> {

  private static volatile boolean done;

  public static void main(String[] args) {
    Suchtbaum<String> tree = new Suchtbaum<>();

    Random rand = new Random();
    int read = 20;
    int write = 3;
    done = false;
    for (int i = 0; i < read; i++) {
      Runnable task = new Runnable() {
        @Override
        public void run() {
          try {
            while (!done) {
              tree.toString();
              tree.contains("this is a test");
            }
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }
      };
      Thread reader = new Thread(task);
      reader.setName(reader.getName() + " READER");
      reader.start();
    }
    for (int j = 0; j < write; j++) {
      int negRand = ~rand.nextInt(Integer.MAX_VALUE / 2);
      Runnable task = new Runnable() {
        @Override
        public void run() {
          try {
            while (!done) {
              List<String> inserts = new ArrayList<>();
              for (int i = 0; i < rand.nextInt(20); i++) {
                String random = Integer.toHexString(rand.nextInt(Integer.MAX_VALUE));
                inserts.add(random);
                tree.insert(random);
              }
              System.out.println(Thread.currentThread().getName() + " is done writing");
              Thread.sleep(2000);
              for (String r : inserts) {
                tree.remove(r);
              }

            }
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }
      };
      Thread writer = new Thread(task);
      writer.setName(writer.getName() + " WRITER");
      writer.start();
    }

    int timeToRun = 20000;
    try {
      Thread.sleep(timeToRun);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    System.out.println("stopping all the threads");
    done = true;
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
      if (newNode != null) {
        newNode.setParent(null);
      }
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
      readers = -1;
    }

    public synchronized void endWrite() throws InterruptedException {
      readers = 0;
      writer = false;
      notifyAll();
    }

  }
}
