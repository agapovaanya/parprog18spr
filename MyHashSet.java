package my.java.HashSet;

import java.util.*;

public class MyHashSet<T> implements Set<T> {

    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final int MAXIMUM_CAPACITY = 1073741824;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private int myThreshold;

    transient Entry<T>[] myTable;
    transient int mySize;
    final float myLoadFactor;

    public MyHashSet() {
        myLoadFactor = DEFAULT_LOAD_FACTOR;
        myThreshold = (int)(DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
        myTable = new Entry[DEFAULT_INITIAL_CAPACITY];
    }

    static int hash(int h) {
        h ^= (h >>> 20) ^ (h >>> 12);
        return h ^ (h >>> 7) ^ (h >>> 4);
    }

    static int indexFor(int h, int length) {
        return h & (length-1);
    }

    public int size() {
        return mySize;
    }

    public boolean isEmpty() {
        return mySize == 0;
    }

    public boolean contains(Object key) {
        return getEntry(key) != null;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            Entry<T> current = null;
            int currentTablePos = 0;

            @Override
            public boolean hasNext() {
                if (current == null || current.next == null) {
                    for(int i = currentTablePos + 1; i < myTable.length; i++) {
                        if(myTable[i] != null) {
                            return true;
                        }
                    }
                } else {
                    return true;
                }
                return false;
            }

            @Override
            public T next() {
                if (!hasNext())
                    throw new IllegalStateException();

                if (current == null || current.next == null) {
                    currentTablePos++;
                    for(;currentTablePos < myTable.length; currentTablePos++) {
                        if(myTable[currentTablePos] != null) {
                            current = myTable[currentTablePos];
                            break;
                        }
                    }
                } else {
                    current = current.next;
                }
                return (T) current.value;
            }

            public void remove() {
            }
        };
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public <T1> T1[] toArray(T1[] t1s) {
        return null;
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends T> collection) {
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        return false;
    }

    @Override
    public void clear() {

    }

    @Override
    public Spliterator<T> spliterator() {
        return null;
    }

    final Entry<T> getEntry(Object key) {
        int hash = (key == null) ? 0 : hash(key.hashCode());
        for (Entry<T> e = myTable[indexFor(hash, myTable.length)]; e != null; e = e.next) {
            Object k;
            if (e.hash == hash)
                return e;
        }
        return null;
    }

    @Override
    public boolean add(T value) {
        int hash = hash(value.hashCode());
        int i = indexFor(hash, myTable.length);
        for (Entry<T> e = myTable[i]; e != null; e = e.next) {
            if (e.hash == hash && value.equals(e.value)) {
                e.value = value;
                return false;
            }
        }

        addEntry(hash, value, i);
        return true;
    }

    @Override
    public boolean remove(Object value) {
        int hash = hash(value.hashCode());
        int i = indexFor(hash, myTable.length);

        Entry<T> prevEntry = null;

        for (Entry<T> e = myTable[i]; e != null; e = e.next) {
            if (e.hash == hash && value.equals(e.value)) {
                mySize--;
                if(prevEntry == null) {
                    myTable[i] = e.next;
                }
                else {
                    prevEntry.next = e.next;
                }
                return true;
            }
            prevEntry = e;
        }

        return false;
    }

    void addEntry(int hash, T value, int bucketIndex) {
        Entry<T> e = myTable[bucketIndex];
        myTable[bucketIndex] = new Entry<T>(hash, value, e);
        if (mySize++ >= myThreshold)
            resize(2 * myTable.length);
    }

    void resize(int newCapacity) {
        Entry[] oldTable = myTable;
        int oldCapacity = oldTable.length;
        if (oldCapacity == MAXIMUM_CAPACITY) {
            myThreshold = Integer.MAX_VALUE;
            return;
        }

        Entry[] newTable = new Entry[newCapacity];
        transfer(newTable);
        myTable = newTable;
        myThreshold = (int)(newCapacity * myLoadFactor);
    }

    void transfer(Entry[] newTable) {
        Entry<T>[] src = myTable;
        int newCapacity = newTable.length;
        for (int j = 0; j < src.length; j++) {
            Entry<T> e = src[j];
            if (e != null) {
                src[j] = null;
                do {
                    Entry<T> next = e.next;
                    int i = indexFor(e.hash, newCapacity);
                    e.next = newTable[i];
                    newTable[i] = e;
                    e = next;
                } while (e != null);
            }
        }
    }

    static class Entry<T> {
        T value;
        Entry<T> next;
        final int hash;

        Entry(int h, T v, Entry<T> n) {
            value = v;
            next = n;
            hash = h;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Entry<?> entry = (Entry<?>) o;
            return hash == entry.hash &&
                    Objects.equals(value, entry.value) &&
                    Objects.equals(next, entry.next);
        }

        @Override
        public int hashCode() {
            return Objects.hash(value, next, hash);
        }
    }
}
