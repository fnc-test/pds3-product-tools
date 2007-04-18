// Copyright 2006-2007, by the California Institute of Technology.
// ALL RIGHTS RESERVED. United States Government Sponsorship acknowledged.
// Any commercial use must be negotiated with the Office of Technology Transfer
// at the California Institute of Technology.
//
// This software is subject to U. S. export control laws and regulations
// (22 C.F.R. 120-130 and 15 C.F.R. 730-774). To the extent that the software
// is subject to U.S. export control laws and regulations, the recipient has
// the responsibility to obtain export licenses or other export authority as
// may be required before exporting such information to foreign countries or
// providing access to foreign nationals.
//
// $Id$ 
//

package gov.nasa.pds.tools.label;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.ListIterator;

/**
 * This class implements the List interface to hide its backing.
 * It is a ordered set of {@link Value}. A sequence can appear on 
 * the right hand side of an {@link AttributeStatement}.
 * @author pramirez
 * @version $Revision$
 * 
 */
public class Sequence implements List, Value {
    private List values;

    /**
     * Constructs an empty sequence of values.
     */
    public Sequence() {
        values = new ArrayList();
    }
    
    /**
     * Constructs a sequence with the ordered value
     * @param values to load into sequence
     */
    public Sequence(List values) {
        this.values = new ArrayList(values);
    }

    /* (non-Javadoc)
     * @see java.util.List#containsAll(java.util.Collection)
     */
    public boolean containsAll(Collection values) {
        return this.values.containsAll(values);
    }

    /* (non-Javadoc)
     * @see java.util.List#addAll(java.util.Collection)
     */
    public boolean addAll(Collection values) {
        return this.values.addAll(values);
    }

    /* (non-Javadoc)
     * @see java.util.List#addAll(int, java.util.Collection)
     */
    public boolean addAll(int index, Collection values) {
        return this.values.addAll(index, values);
    }

    /* (non-Javadoc)
     * @see java.util.List#removeAll(java.util.Collection)
     */
    public boolean removeAll(Collection values) {
        return this.values.removeAll(values);
    }

    /* (non-Javadoc)
     * @see java.util.List#retainAll(java.util.Collection)
     */
    public boolean retainAll(Collection values) {
        return this.values.retainAll(values);
    }

    /* (non-Javadoc)
     * @see java.util.List#clear()
     */
    public void clear() {
        values.clear();
    }

    /* (non-Javadoc)
     * @see java.util.List#get(int)
     */
    public Value getValue(int index) {
        return (Value) values.get(index);
    }

    /* (non-Javadoc)
     * @see java.util.List#set(int, java.lang.Object)
     */
    public Value set(int index, Value value) {
        return (Value) values.set(index, value);
    }

    /* (non-Javadoc)
     * @see java.util.List#remove(int)
     */
    public Scalar removeScalar(int index) {
        return (Scalar) values.remove(index);
    }

    /* (non-Javadoc)
     * @see java.util.List#listIterator()
     */
    public ListIterator listIterator() {
        return values.listIterator();
    }

    /* (non-Javadoc)
     * @see java.util.List#listIterator(int)
     */
    public ListIterator listIterator(int index) {
        return values.listIterator(index);
    }

    /* (non-Javadoc)
     * @see java.util.List#subList(int, int)
     */
    public List subList(int fromIndex, int toIndex) {
        return values.subList(fromIndex, toIndex);
    }

    /* (non-Javadoc)
     * @see java.util.List#contains(java.lang.Object)
     */
    public boolean contains(Object object) {
        return values.contains(object);
    }

    /* (non-Javadoc)
     * @see java.util.List#toArray(java.lang.Object[])
     */
    public Object[] toArray(Object[] objects) {
        return values.toArray(objects);
    }

    /* (non-Javadoc)
     * @see java.util.List#add(java.lang.Object)
     */
    public boolean add(Object object) {
        return values.add(object);
    }

    /* (non-Javadoc)
     * @see java.util.List#remove(java.lang.Object)
     */
    public boolean remove(Object object) {
        return values.remove(object);
    }

    /* (non-Javadoc)
     * @see java.util.List#set(int, java.lang.Object)
     */
    public Object set(int index, Object object) {
        return values.set(index, object);
    }

    /* (non-Javadoc)
     * @see java.util.List#add(int, java.lang.Object)
     */
    public void add(int index, Object object) {
         values.add(index, object);
    }

    /* (non-Javadoc)
     * @see java.util.List#indexOf(java.lang.Object)
     */
    public int indexOf(Object object) {
       return values.indexOf(object);
    }

    /* (non-Javadoc)
     * @see java.util.List#lastIndexOf(java.lang.Object)
     */
    public int lastIndexOf(Object object) {
        return values.lastIndexOf(object);
    }

    /* (non-Javadoc)
     * @see java.util.List#toArray()
     */
    public Object[] toArray() {
        return values.toArray();
    }

    /* (non-Javadoc)
     * @see java.util.List#get(int)
     */
    public Object get(int index) {
        return values.get(index);
    }

    /* (non-Javadoc)
     * @see java.util.List#remove(int)
     */
    public Object remove(int index) {
        return values.remove(index);
    }


    /* (non-Javadoc)
     * @see java.util.List#size()
     */
    public int size() {
        return values.size();
    }


    /* (non-Javadoc)
     * @see java.util.List#isEmpty()
     */
    public boolean isEmpty() {
        return values.isEmpty();
    }


    /* (non-Javadoc)
     * @see java.util.List#iterator()
     */
    public Iterator iterator() {
        return values.iterator();
    }
}
