package heap;


public class BinomialHeapHandle<T extends Comparable<T>> {
  private BinomialTreeNode<T> node;
  
  BinomialTreeNode<T> getNode() {
    return node;
  }
  
  void setNode(BinomialTreeNode<T> node) {
    this.node = node;
  }
  
  BinomialHeapHandle(BinomialTreeNode<T> node) {
    this.node = node;
  }
}
