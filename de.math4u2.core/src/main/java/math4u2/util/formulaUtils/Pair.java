package math4u2.util.formulaUtils;

import java.io.Serializable;

/**
 * Class that represents a pair of keys that is used for storage purposes
 * 
 * @author Erich Seifert
 * @version $Revision: 1.1 $
 */
public class Pair implements Serializable {
    protected Object first;

    protected Object second;

    /** Standard constructor */
    protected Pair() {
    }

    /** Standard constructor */
    public Pair(Object first, Object second) {
        this.first = first;
        this.second = second;
    }

    /**
     * Overriden hashCode functionality which is used for equality test in the
     * <code>HashMap</code> container
     * 
     * @return Hash code for <code>HashMap</code> storage
     */
    public int hashCode() {
        return first.hashCode() + second.hashCode();
    }

    /**
     * Overriden equals functionality which is used for storage in the
     * <code>HashMap</code> container
     * 
     * @return <code>true</code> if the <code>Pair</code> s have equal keys,
     *         otherwise <code>false</code>.
     */
    public boolean equals(Object o) {
        if ((o == null) || !(o instanceof Pair))
            return false;

        if (o == this)
            return true;

        Pair p = (Pair) o;
        if (!p.first.equals(first) || !p.second.equals(second))
            return false;

        return true;
    }

    /**
     * Returns the first key of this <code>Pair</code>.
     * 
     * @return First key object
     */
    public final Object getFirst() {
        return first;
    }

    /**
     * Returns the second key of this <code>Pair</code>.
     * 
     * @return Second key object
     */
    public final Object getSecond() {
        return second;
    }
}