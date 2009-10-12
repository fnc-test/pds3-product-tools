// Copyright 2006-2010, by the California Institute of Technology.
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

import gov.nasa.arc.pds.tools.util.StrUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * This class implements the List interface to hide its backing. It is a ordered
 * set of {@link Value}. A sequence can appear on the right hand side of an
 * {@link AttributeStatement}.
 * 
 * @author pramirez
 * @author jagander
 * @version $Revision$
 * 
 */
public class Sequence implements List<Value>, Value {
    private List<Value> values;

    /**
     * Constructs an empty sequence of values.
     */
    public Sequence() {
        this.values = new ArrayList<Value>();
    }

    /**
     * Constructs a sequence with the ordered value
     * 
     * @param values
     *            to load into sequence
     */
    public Sequence(List<Value> values) {
        this.values = new ArrayList<Value>(values);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.List#containsAll(java.util.Collection)
     */
    public boolean containsAll(Collection<?> vals) {
        return this.values.containsAll(vals);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.List#addAll(java.util.Collection)
     */
    public boolean addAll(Collection<? extends Value> vals) {
        return this.values.addAll(vals);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.List#addAll(int, java.util.Collection)
     */
    public boolean addAll(int index, Collection<? extends Value> vals) {
        return this.values.addAll(index, vals);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.List#removeAll(java.util.Collection)
     */
    public boolean removeAll(Collection<?> vals) {
        return this.values.removeAll(vals);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.List#retainAll(java.util.Collection)
     */
    public boolean retainAll(Collection<?> vals) {
        return this.values.retainAll(vals);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.List#clear()
     */
    public void clear() {
        this.values.clear();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.List#get(int)
     */
    public Value getValue(int index) {
        return this.values.get(index);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.List#set(int, java.lang.Object)
     */
    public Value set(int index, Value value) {
        return this.values.set(index, value);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.List#remove(int)
     */
    public Scalar removeScalar(int index) {
        return (Scalar) this.values.remove(index);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.List#listIterator()
     */
    public ListIterator<Value> listIterator() {
        return this.values.listIterator();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.List#listIterator(int)
     */
    public ListIterator<Value> listIterator(int index) {
        return this.values.listIterator(index);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.List#subList(int, int)
     */
    public List<Value> subList(int fromIndex, int toIndex) {
        return this.values.subList(fromIndex, toIndex);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.List#contains(java.lang.Object)
     */
    public boolean contains(Object object) {
        return this.values.contains(object);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.List#toArray(java.lang.Object[])
     */
    public <T> T[] toArray(T[] objects) {
        return this.values.toArray(objects);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.List#add(java.lang.Object)
     */
    public boolean add(Value object) {
        return this.values.add(object);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.List#remove(java.lang.Object)
     */
    public boolean remove(Object object) {
        return this.values.remove(object);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.List#add(int, java.lang.Object)
     */
    public void add(int index, Value object) {
        this.values.add(index, object);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.List#indexOf(java.lang.Object)
     */
    public int indexOf(Object object) {
        return this.values.indexOf(object);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.List#lastIndexOf(java.lang.Object)
     */
    public int lastIndexOf(Object object) {
        return this.values.lastIndexOf(object);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.List#toArray()
     */
    public Object[] toArray() {
        return this.values.toArray();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.List#get(int)
     */
    public Value get(int index) {
        return this.values.get(index);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.List#remove(int)
     */
    public Value remove(int index) {
        return this.values.remove(index);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.List#size()
     */
    public int size() {
        return this.values.size();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.List#isEmpty()
     */
    public boolean isEmpty() {
        return this.values.isEmpty();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.List#iterator()
     */
    public Iterator<Value> iterator() {
        return this.values.iterator();
    }

    public String normalize() {
        return StrUtils.normalize(this.toString());
    }

}
