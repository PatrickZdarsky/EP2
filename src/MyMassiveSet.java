import codedraw.CodeDraw;

/**
 * @author Patrick Zdarsky / Rxcki
 */
public class MyMassiveSet implements MassiveSet{

    private final MassiveForceTreeMap treeMap;

    public MyMassiveSet(MassiveForceTreeMap treeMap) {
        this.treeMap = treeMap;
    }

    @Override
    public void draw(CodeDraw cd) {

    }

    @Override
    public MassiveIterator iterator() {
        return treeMap.iterator();
    }

    @Override
    public boolean contains(Massive element) {
        return treeMap.containsKey(element);
    }

    @Override
    public void remove(Massive element) {
        treeMap.remove(element);
    }

    @Override
    public void clear() {
        treeMap.clear();
    }

    @Override
    public int size() {
        return treeMap.count();
    }

    @Override
    public MassiveLinkedList toList() {
        var list = new MassiveLinkedList();

        for (Massive massive : this) {
            list.addLast(massive);
        }

        return list;
    }
}
