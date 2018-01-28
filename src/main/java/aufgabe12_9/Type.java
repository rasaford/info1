package aufgabe12_9;

// utf8: "Köpfchen in das Wasser, Schwänzchen in die Höh." -CIA-Verhörmethode
public class Type {

  private String name;

  Type(String name) {
    this.name = name;
  }

  public Type setName(String name) {
    this.name = name;
    return this;
  }

  String codeString() {
    return name;
  }

  @Override
  public String toString() {
    return codeString();
  }
}

class Int extends Type {

  public Int() {
    super("int");
  }
}

class IntArray extends Type {

  public IntArray() {
    super("int[]");
  }
}

class Object extends Type {

  public Object(String name) {
    super(name);
  }
}

class ObjectArray extends Type {

  public ObjectArray(String name) {
    super(name + "[]");
  }
}
