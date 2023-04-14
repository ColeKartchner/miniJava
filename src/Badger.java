public class Badger {

    private static int badgerCount = 0;
    public static final Badger Diggy = new Badger("Diggy McStripes"); // name suggested by ChatGPT

    public final String name;

    public Badger(String name) {
        this.name = name;
        ++badgerCount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        throw new RuntimeException("Unimplemented: Badgers are immutable!");
    }

    public static final int population() {
        return badgerCount;
    }

    @Override
    public String toString() {
        return String.format("Badger[name=%s]", name);
    }
}
