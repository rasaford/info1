import java.util.concurrent.Semaphore;

public class Suchtbaum<T extends Comparable<T>> {

  private class SuchtbaumElement {

    private T element;
    private SuchtbaumElement left = null;
    private SuchtbaumElement right = null;

    private SuchtbaumElement(SuchtbaumElement left, SuchtbaumElement right, T element) {
      this.left = left;
      this.right = right;
      this.element = element;
    }

    public T getElement() {
      return element;
    }

    public void setElement(T element) {
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
  }

  private Semaphore s = new Semaphore(1);
  private SuchtbaumElement root;

  public void insert(T element) throws InterruptedException {
    SuchtbaumElement parent = search(root, element);
    SuchtbaumElement newNode = new SuchtbaumElement(null, null, element);
    if (parent == null) {
      this.root = newNode;
    } else if (parent.getElement().compareTo(element) > 0) {
      parent.setLeft(newNode);
    } else if (parent.getElement().compareTo(element) < 0) {
      parent.setRight(newNode);
    } else {
      throw new RuntimeException(
          String.format("%s is already contained within the tree", element));
    }
  }

  public boolean contains(T element) throws InterruptedException {
    SuchtbaumElement parent = search(root, element);
    return parent != null &&
        parent.getRight() != null &&
        parent.getLeft() != null &&
        (parent.getRight().getElement().equals(element) ||
            parent.getLeft().getElement().equals(element));
  }

  public void remove(T element) throws InterruptedException {
    SuchtbaumElement parent = search(root, element);
    if (parent == null || parent.getLeft() != null && parent.getRight() != null) {
      throw new RuntimeException(
          String.format("%s is not in the tree and cannot be deleted", element));
    }
    // TODO write remove function
  }

  private SuchtbaumElement search(SuchtbaumElement root, T element) {
    SuchtbaumElement parent = root;
    while (root != null) {
      int compare = root.getElement().compareTo(element);
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
    sb.append(String.format("%s;", root.getElement()));
    if (root.getLeft() != null) {
      sb.append(String.format("%s -> %s [label=left];",
          root.getElement(), root.getLeft().getElement()));
      printSubtree(root.getLeft(), sb);
    }
    if (root.getRight() != null) {
      sb.append(String.format("%s -> %s [label=right];",
          root.getElement(), root.getRight().getElement()));
      printSubtree(root.getRight(), sb);
    }
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("digraph G {\n");
    printSubtree(root, sb);
    sb.append("}\n");
    return sb.toString();
  }
}
