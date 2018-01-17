package heap;

import java.util.Optional;
import java.util.function.Consumer;

public class BinomialTreeNode<T extends Comparable<T>> {

  private T element;

  public T getElement() {
    return element;
  }

  private BinomialTreeNode<T>[] children;

  private Optional<BinomialTreeNode<T>> mother;

  private Optional<Consumer<BinomialTreeNode<T>>> propagateRootChange;

  private BinomialHeapHandle<T> handle;

  BinomialHeapHandle<T> getHandle() {
    return handle;
  }

  void setPropagateRootChange(Optional<Consumer<BinomialTreeNode<T>>> propagateRootChange) {
    this.propagateRootChange = propagateRootChange;
  }

  void setPropagateRootChange(Consumer<BinomialTreeNode<T>> propagateRootChange) {
    setPropagateRootChange(Optional.of(propagateRootChange));
  }

  void replaceWithSmallerElement(T element) {
    if (element.compareTo(this.element) > 0) {
      throw new RuntimeException("Do you know what 'smaller' means?");
    }
    this.element = element;
    siftUp();
  }

  boolean validTree(Optional<BinomialTreeNode<T>> mother) {
    if (!this.mother.equals(mother)) {
      throw new RuntimeException();
    }
    Optional<Integer> rankLast = Optional.empty();
    for (int i = 0; i < children.length; i++) {
      if (!children[i].validTree(Optional.of(this))) {
        return false;
      }
      if (rankLast.isPresent() && children[i].rank() <= rankLast.get()) {
        return false;
      }
      if (children[i].getElement().compareTo(element) < 0) {
        return false;
      }
      rankLast = Optional.of(children[i].rank());
    }
    return true;
  }

  /**
   * Gibt eine Menge von Teilbäumen zurück, in die der aktuelle Baum zerfällt, wenn man den Knoten
   * des minimalen Elements entfernt.
   *
   * @return die Menge von Teilbäumen
   */
  BinomialTreeNode<T>[] deleteMin() {
    for (int i = 0; i < children.length; i++) {
      children[i].mother = Optional.empty();
    }
    return children;
  }

  @SuppressWarnings("unchecked")
  public BinomialTreeNode(T element) {
    this.children = new BinomialTreeNode[0];
    this.element = element;
    this.mother = Optional.empty();
    this.handle = new BinomialHeapHandle<T>(this);
  }

  /**
   * Ermittelt das minimale Element im Teilbaum.
   *
   * @return das minimale Element
   */
  public T min() {
    return element;
  }

  /**
   * Gibt den Rang des Teilbaumes zurück.
   *
   * @return der Rang des Teilbaumes
   */
  public int rank() {
    return children.length;
  }

  /**
   * Diese Methode repariert den Baum ab dem aktuellen Knoten nach oben, wenn sich ein Schlüssel
   * verringert hat.
   */
  private void siftUp() {
    if (!mother.isPresent()) {
      /*
       * Wenn es keinen Mutterknoten gibt, sind wir an der Wurzel des
       * Baumes. Wir propagieren die Änderung daher zum Haufen.
       */
      propagateRootChange.get().accept(this);
      return;
    }
    if (mother.get().element.compareTo(this.element) > 0) {
      /*
       * Wenn unser Element kleiner ist als das der Mutter,
       * vertauschen wir es mit ihrem Element. Außerdem stellen wir
       * sicher, dass die Handles weiterhin auf die richtigen
       * Knoten zeigen. Schließlich fahren wir mit dem Reparieren
       * bei der Mutter fort.
       */

      T elementThis = this.element;
      BinomialHeapHandle<T> handleThis = handle;

      handle.setNode(mother.get());
      mother.get().handle.setNode(this);

      this.handle = mother.get().handle;
      this.element = mother.get().element;

      mother.get().element = elementThis;
      mother.get().handle = handleThis;

      mother.get().siftUp();
    }
  }

  /**
   * Diese Methode vereint zwei Bäume des gleichen Ranges.
   *
   * @param a der erste Baum
   * @param b der zweite Baum
   * @return denjenigen der beiden Bäume, an den der andere angehängt wurde
   */
  public static <T extends Comparable<T>> BinomialTreeNode<T> merge(BinomialTreeNode<T> a,
      BinomialTreeNode<T> b) {
    if (a.children.length != b.children.length) {
      throw new RuntimeException("Unable to merge trees of different rank!");
    }
    @SuppressWarnings("unchecked")
    BinomialTreeNode<T>[] children = new BinomialTreeNode[a.children.length + 1];
    BinomialTreeNode<T> bigger;
    BinomialTreeNode<T> smaller;
    if (a.element.compareTo(b.element) <= 0) {
      smaller = a;
      bigger = b;
    } else {
      smaller = b;
      bigger = a;
    }
    children[children.length - 1] = bigger;
    for (int i = 0; i < children.length - 1; i++) {
      children[i] = smaller.children[i];
    }
    smaller.children = children;
    bigger.mother = Optional.of(smaller);
    return smaller;
  }
}
