import java.util.Objects;

/**
 * @author Patrick Zdarsky / Rxcki
 */
public class GenericKeyValuePair<K, V> {

    private final K key;
    private V value;

    public GenericKeyValuePair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GenericKeyValuePair)) return false;
        GenericKeyValuePair<?, ?> that = (GenericKeyValuePair<?, ?>) o;
        return Objects.equals(key, that.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key);
    }
}
