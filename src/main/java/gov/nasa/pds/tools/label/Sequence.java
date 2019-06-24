// Copyright 2019, California Institute of Technology ("Caltech").
// U.S. Government sponsorship acknowledged.
//
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions are met:
//
// • Redistributions of source code must retain the above copyright notice,
// this list of conditions and the following disclaimer.
// • Redistributions must reproduce the above copyright notice, this list of
// conditions and the following disclaimer in the documentation and/or other
// materials provided with the distribution.
// • Neither the name of Caltech nor its operating division, the Jet Propulsion
// Laboratory, nor the names of its contributors may be used to endorse or
// promote products derived from this software without specific prior written
// permission.
//
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
// AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
// IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
// ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
// LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
// CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
// SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
// INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
// CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
// ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
// POSSIBILITY OF SUCH DAMAGE.

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
   *          to load into sequence
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

  public String toString() {
    String value = "(";

    for (Iterator<Value> i = listIterator(); i.hasNext();) {
      value += i.next().toString();
      if (i.hasNext()) {
        value += ", ";
      }
    }
    value += ")";

    return value;
  }

  /**
   * Provides a string representation of the Sequence with single or double
   * quotes surrounding each value if the retainQuotes flag is set to true and
   * the ValueType was SINGLE_QUOTED or DOUBLE_QUOTED for a value.
   * 
   * @param retainQuotes
   *          Set to true to return the value with quotes surrounding it if it
   *          was originally there in the label.
   * 
   * @return The string representation of the Sequence.
   */
  public String toString(boolean retainQuotes) {
    String value = "(";

    for (Iterator<Value> i = listIterator(); i.hasNext();) {
      Value v = i.next();
      if (v instanceof Scalar) {
        value += ((Scalar) v).toString(retainQuotes);
      } else {
        value += v.toString();
      }
      if (i.hasNext()) {
        value += ", ";
      }
    }
    value += ")";

    return value;
  }

  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }

    if (!(object instanceof Sequence)) {
      return false;
    }
    Sequence that = (Sequence) object;
    if (this.size() != that.size()) {
      return false;
    }
    for (int i = 0; i < this.size(); i++) {
      if (!this.get(i).equals(that.get(i))) {
        return false;
      }
    }
    return true;
  }

  public int hashcode() {
    return this.values.hashCode();
  }
}
