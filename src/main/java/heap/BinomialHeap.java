package heap;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Optional;

public class BinomialHeap<T extends Comparable<T>> {
  private ArrayList<BinomialTreeNode<T>> trees;

  private Optional<BinomialTreeNode<T>> minimum;

  private int size;

  public int getSize() {
    return size;
  }

  /**
   * Diese Methode addiert zwei binomiale Bäume und einen Carry-Baum zusammen. Jeder der drei Bäume
   * kann durch 0 nicht-existent sein.
   * 
   * @param a der erste Baum
   * @param b der zweite Baum
   * @param carry der Carry-Baum
   * @return das Additionsergebnis (1) und ein neuer Carry-Baum (2)
   */
  private Pair<Optional<BinomialTreeNode<T>>, Optional<BinomialTreeNode<T>>> add(
      Optional<BinomialTreeNode<T>> a, Optional<BinomialTreeNode<T>> b,
      Optional<BinomialTreeNode<T>> carry) {
    @SuppressWarnings("unchecked")
  Optional<BinomialTreeNode<T>>[] nodes =
        (Optional<BinomialTreeNode<T>>[]) Array.newInstance(Optional.class, 3);
    for (int i = 0; i < nodes.length; i++)
      nodes[i] = Optional.empty();
    int count = 0;
    if (a.isPresent())
      nodes[count++] = a;
    if (b.isPresent())
      nodes[count++] = b;
    if (carry.isPresent())
      nodes[count++] = carry;
    if (count <= 1)
      return new Pair<>(nodes[0], Optional.empty());
    else {
      assert (nodes[0].get().rank() == nodes[1].get().rank());
      return new Pair<>(nodes[2],
          Optional.of(BinomialTreeNode.merge(nodes[0].get(), nodes[1].get())));
    }
  }

  /**
   * Diese Methode wird aufgerufen, wenn sich ein Baum durch die replaceWithSmallerElement()-Operation verändert
   * hat.
   * 
   * @param index der Index des Baumes
   * @param node die Wurzel des Baumes
   */
  private void updateTree(int index, BinomialTreeNode<T> node) {
    trees.set(index, node);
    if (node.getElement().compareTo(minimum.get().getElement()) < 0)
      minimum = Optional.of(node);
  }

  /**
   * Diese Methode vereint eine nach Rang sortierte Menge von binomialen Bäumen mit dem aktuellen
   * Haufen.
   * 
   * @param heads die Menge an zu mergenden Binomialbäumen
   */
  private void merge(BinomialTreeNode<T>[] heads, boolean ignoreMinimum) {
    Optional<BinomialTreeNode<T>> minimum = Optional.empty();
    ArrayList<BinomialTreeNode<T>> treesNew = new ArrayList<BinomialTreeNode<T>>();
    Optional<BinomialTreeNode<T>> carry = Optional.empty();
    int rank = 0;
    int treeIndex = 0;
    int headsIndex = 0;
    while (true) {
      Optional<BinomialTreeNode<T>> a = Optional.empty();
      int minRank = 0;
      /*
       * Zunächst prüfen wir, ob es einen Baum im Haufen vom Rang 'rank' gibt. Die Schleife dient
       * dazu, ggf. den Minimumsbaum zu überspringen, wenn dieser entfernt werden soll.
       */
      while (treeIndex < trees.size()) {
        BinomialTreeNode<T> tree = trees.get(treeIndex);
        if (ignoreMinimum && tree == this.minimum.get()) {
          treeIndex++;
          continue;
        }
        minRank = tree.rank();
        if (tree.rank() == rank) {
          a = Optional.of(tree);
          treeIndex++;
        }
        break;
      }
      /*
       * Als nächstes prüfen wir, ob es einen Baum in 'heads' vom Rang 'rank' gibt und aktualisieren
       * den minimalen Rang 'minRank' passend.
       */
      Optional<BinomialTreeNode<T>> b = Optional.empty();
      if (headsIndex < heads.length) {
        BinomialTreeNode<T> tree = heads[headsIndex];
        if (tree.rank() < minRank)
          minRank = tree.rank();
        if (tree.rank() == rank) {
          b = Optional.of(tree);
          headsIndex++;
        }
      }
      if (!a.isPresent() && !b.isPresent() && !carry.isPresent()) {
        if (treeIndex >= trees.size() && headsIndex >= heads.length)
          /*
           * Wenn es keine Bäume im Haufen, keine Bäume in 'heads' und kein Carry gibt, sind wir
           * fertig.
           */
          break;
        else {
          /*
           * Eine Lücke der Ränge, die sowohl im Haufen, als auch in 'heads' existiert, kann
           * übersprungen werden.
           */
          rank = minRank;
          continue;
        }
      } else
        rank++;
      /*
       * Wir berechnen den nächsten Baum und den Carry-Baum.
       */
      Pair<Optional<BinomialTreeNode<T>>, Optional<BinomialTreeNode<T>>> result = add(a, b, carry);
      Optional<BinomialTreeNode<T>> x = result._1;
      carry = result._2;
      if (x.isPresent()) {
        /*
         * Evtl. müssen wir das Minimum des neuen Baums aktualisieren.
         */
        if (!minimum.isPresent() || x.get().getElement().compareTo(minimum.get().getElement()) < 0)
          minimum = x;
        final int sizeCurrent = treesNew.size();
        /*
         * Wir geben dem Teilbaum ein Callback, welches bei Änderungen durch die
         * replaceWithSmallerElement()-Operation aufgerufen wrid.
         */
        x.get().setPropagateRootChange(node -> updateTree(sizeCurrent, node));
        treesNew.add(x.get());
      }
    }
    this.trees = treesNew;
    this.minimum = minimum;
  }

  @SuppressWarnings("unused")
  private boolean validHeap() {
    if (trees.isEmpty())
      return true;
    T minE = peek();
    Optional<Integer> rankLast = Optional.empty();
    for (int i = 0; i < trees.size(); i++) {
      if (!trees.get(i).validTree(Optional.empty()))
        return false;
      if (rankLast.isPresent() && trees.get(i).rank() <= rankLast.get())
        return false;
      if (trees.get(i).getElement().compareTo(minE) < 0)
        return false;
      rankLast = Optional.of(trees.get(i).rank());
    }
    return true;
  }

  /**
   * Dieser Konstruktor baut einen leeren Haufen.
   */
  public BinomialHeap() {
    trees = new ArrayList<>();
    minimum = Optional.empty();
    size = 0;
  }

  /**
   * Diese Methode fügt ein Element in den Haufen ein.
   * 
   * @param element das einzufügende Element
   */
  @SuppressWarnings("unchecked")
  public Object insert(T element) {
    BinomialTreeNode<T> node = new BinomialTreeNode<T>(element);
    merge(new BinomialTreeNode[] {node}, false);
    size++;
    return node.getHandle();
  }

  /**
   * Diese Methode ermittelt das minimale Element im binomialen Haufen.
   * 
   * @return das minimale Element
   */
  public T peek() {
    if (!minimum.isPresent())
      throw new RuntimeException("Empty :-(");
    return minimum.get().getElement();
  }

  /**
   * Diese Methode entfernt das minimale Element aus dem binomialen Haufen und gibt es zurück.
   * 
   * @return das minimale Element
   */
  public T poll() {
    T min = peek();
    BinomialTreeNode<T> minimumTree = minimum.get();
    BinomialTreeNode<T>[] children = minimumTree.deleteMin();
    merge(children, true);
    size--;
    // if(!validHeap())
    // throw new RuntimeException("Invalid tree!");
    return min;
  }

  public void replaceWithSmallerElement(Object handle, T elementNew) {
    @SuppressWarnings("unchecked")
    BinomialHeapHandle<T> handleCasted = (BinomialHeapHandle<T>) handle;
    handleCasted.getNode().replaceWithSmallerElement(elementNew);
    // if(!validHeap())
    // throw new RuntimeException("Invalid tree!");
  }
}
