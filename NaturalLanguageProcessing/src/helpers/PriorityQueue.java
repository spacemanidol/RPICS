package helpers;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;

/**
 * A smarter PriorityQueue class, that's capable of handling key-value pairs.
 * 
 * @author Dwight Naylor
 * 
 * @param <K>
 *            The type of the key.
 * @param <V>
 *            The type of the values.
 */
public class PriorityQueue<K, V> {

	private int size;
	private Hashtable<V, Integer> valueCounts;
	private Hashtable<K, ArrayList<V>> valueTable;
	private java.util.PriorityQueue<K> keyQueue;

	public V poll() {
		if (!keyQueue.isEmpty()) {
			V ret = valueTable.get(keyQueue.peek()).remove(0);
			valueCounts.put(ret, valueCounts.get(ret) - 1);
			if (valueCounts.get(ret) == 0) {
				valueCounts.remove(ret);
			}
			if (valueTable.get(keyQueue.peek()).size() == 0) {
				keyQueue.poll();
			}
			size--;
			return ret;
		}
		return null;
	}

	public String toString() {
		StringBuffer ret = new StringBuffer("{");
		Iterator<K> keyIterator = keyQueue.iterator();
		while (keyIterator.hasNext()) {
			if (ret.length() > 1) {
				ret.append(", ");
			}
			K key = keyIterator.next();
			ret.append(key + "=" + this.valueTable.get(key));
		}
		ret.append("}");
		return ret.toString();
	}

	public V peek() {
		if (!keyQueue.isEmpty()) {
			return valueTable.get(keyQueue.peek()).get(0);
		}
		return null;
	}

	public void remove(K key, V value) {
		if (!valueTable.containsKey(key) || !valueCounts.containsKey(value)) {
			return;
		}
		valueCounts.put(value, valueCounts.get(value) + 1);
		valueTable.get(key).remove(value);
		if (valueTable.get(key).size() == 0) {
			keyQueue.remove(key);
			valueTable.remove(key);
		}
		size--;
	}

	public PriorityQueue() {
		clear();
	}

	public boolean put(K key, V value) {
		if (!valueTable.containsKey(key)) {
			valueTable.put(key, new ArrayList<V>());
		}
		if (!valueCounts.containsKey(value)) {
			valueCounts.put(value, 0);
		}
		valueCounts.put(value, valueCounts.get(value) + 1);
		valueTable.get(key).add(value);
		if (!keyQueue.contains(key)) {
			keyQueue.add(key);
		}
		size++;
		return true;
	}

	public void clear() {
		valueCounts = new Hashtable<V, Integer>();
		valueTable = new Hashtable<K, ArrayList<V>>();
		keyQueue = new java.util.PriorityQueue<K>();
		size = 0;
	}

	public boolean contains(Object V) {
		return valueCounts.containsKey(V) && valueCounts.get(V) > 0;
	}

	public boolean containsAll(Collection<?> collection) {
		for (Iterator<?> iterator = collection.iterator(); iterator.hasNext();) {
			if (!contains((Object) iterator.next())) {
				return false;
			}
		}
		return true;
	}

	public boolean isEmpty() {
		return size == 0;
	}

	@SuppressWarnings("unchecked")
	public Iterator<V> iterator() {
		return (Iterator<V>) Arrays.asList(toArray()).iterator();
	}

	public int size() {
		return size;
	}

	public Object[] toArray() {
		ArrayList<V> values = new ArrayList<V>();
		Object[] keys = this.keyQueue.toArray();
		Arrays.sort(keys);
		for (int i = 0; i < keys.length; i++) {
			values.addAll(valueTable.get(keys[i]));
		}
		return values.toArray();
	}
}
