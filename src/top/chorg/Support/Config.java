package top.chorg.Support;

import java.io.Serializable;

public class Config implements Serializable {
    private static final long serialVersionUID = 666666666666666666L;

    private class Element implements Serializable {
        public Element(String key, Serializable value) {
            this.key = key;
            this.value = value;
        }
        public String key;
        public Serializable value;
    }
    private Element[] data;
    private int size;
    private int used;

    public Config() {
        used = 0;
        size = 32;
        data = new Element[size];
    }

    private void checkExpand() {
        if (size == used) {
            size *= 2;
            Element[] temp = new Element[size];
            System.arraycopy(data, 0, temp, 0, used);
            data = temp;
        }
    }

    public Serializable get(String key) {
        if (!containsKey(key)) throw new IllegalAccessError("Key '"+ key +"' not exists!");
        return data[indexOf(key)].value;
    }

    public void put(String key, Serializable value) {
        checkExpand();
        if (containsKey(key)) throw new IllegalAccessError("Key '"+ key +"' already exists!");
        data[used] = new Element(key, value);
        used++;
    }

    public void replace(String key, Serializable value) {
        if (!containsKey(key)) throw new IllegalAccessError("Key '"+ key +"' not exists!");
        data[used] = new Element(key, value);
    }

    public boolean containsKey(String key) {
        return indexOf(key) != -1;
    }

    private int indexOf(String key) {
        for (int i = 0; i < used; i++) {
            if (data[i].key.equals(key)) return i;
        }
        return -1;
    }
}
