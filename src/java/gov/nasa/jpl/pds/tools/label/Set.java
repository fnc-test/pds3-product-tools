//Copyright (c) 2005, California Institute of Technology.
//ALL RIGHTS RESERVED. U.S. Government sponsorship acknowledged.
//
// $Id$ 
//

package gov.nasa.jpl.pds.tools.label;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

/**
 * This class implements the Collection interface to hide its backing.
 * It is a unordered set of {@link Value}. A set can appear on 
 * the right hand side of an {@link AttributeStatement}.
 * @author pramirez
 * @version $Revision$
 * 
 */
public class Set implements Collection, Value {
    private List values;
    
    public Set() {
        values = new ArrayList();
    }
    
    public Set(Collection values) {
        values = new ArrayList(values);
    }

    /* (non-Javadoc)
     * @see java.util.Collection#size()
     */
    public int size() {
        return values.size();
    }

    /* (non-Javadoc)
     * @see java.util.Collection#isEmpty()
     */
    public boolean isEmpty() {
        return values.isEmpty();
    }

    /* (non-Javadoc)
     * @see java.util.Collection#contains(java.lang.Object)
     */
    public boolean contains(Object object) {
        return values.contains(object);
    }

    /* (non-Javadoc)
     * @see java.util.Collection#iterator()
     */
    public Iterator iterator() {
        return values.iterator();
    }

    /* (non-Javadoc)
     * @see java.util.Collection#toArray()
     */
    public Object[] toArray() {
        return values.toArray();
    }

    /* (non-Javadoc)
     * @see java.util.Collection#toArray(java.lang.Object[])
     */
    public Object[] toArray(Object[] objects) {
        return values.toArray(objects);
    }

    /* (non-Javadoc)
     * @see java.util.Collection#add(java.lang.Object)
     */
    public boolean add(Object object) {
        return values.add(object);
    }

    /* (non-Javadoc)
     * @see java.util.Collection#remove(java.lang.Object)
     */
    public boolean remove(Object object) {
        return values.remove(object);
    }

    /* (non-Javadoc)
     * @see java.util.Collection#containsAll(java.util.Collection)
     */
    public boolean containsAll(Collection values) {
        return this.values.containsAll(values);
    }

    /* (non-Javadoc)
     * @see java.util.Collection#addAll(java.util.Collection)
     */
    public boolean addAll(Collection values) {
        return this.values.addAll(values);
    }

    /* (non-Javadoc)
     * @see java.util.Collection#removeAll(java.util.Collection)
     */
    public boolean removeAll(Collection values) {
        return this.values.removeAll(values);
    }

    /* (non-Javadoc)
     * @see java.util.Collection#retainAll(java.util.Collection)
     */
    public boolean retainAll(Collection values) {
        return this.values.retainAll(values);
    }

    /* (non-Javadoc)
     * @see java.util.Collection#clear()
     */
    public void clear() {
        values.clear();
    }

}