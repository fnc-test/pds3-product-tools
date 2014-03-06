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

/**
 * This class implements the Collection interface to hide its backing. It is a
 * unordered set of {@link Value}. A set can appear on the right hand side of an
 * {@link AttributeStatement}.
 * 
 * @author pramirez
 * @author jagander
 * @version $Revision$
 * 
 */
public class Set implements Collection<Scalar>, Value {
  private List<Scalar> values;

  public Set() {
    this.values = new ArrayList<Scalar>();
  }

  public Set(Collection<Scalar> values) {
    this.values = new ArrayList<Scalar>(values);
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.util.Collection#size()
   */
  public int size() {
    return this.values.size();
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.util.Collection#isEmpty()
   */
  public boolean isEmpty() {
    return this.values.isEmpty();
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.util.Collection#contains(java.lang.Object)
   */
  public boolean contains(Object object) {
    return this.values.contains(object);
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.util.Collection#iterator()
   */
  public Iterator<Scalar> iterator() {
    return this.values.iterator();
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.util.Collection#toArray()
   */
  public Scalar[] toArray() {
    return this.values.toArray(new Scalar[this.values.size()]);
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.util.Collection#toArray(java.lang.Object[])
   */
  public <T> T[] toArray(T[] objects) {
    return this.values.toArray(objects);
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.util.Collection#add(java.lang.Object)
   */
  public boolean add(Scalar object) {
    return this.values.add(object);
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.util.Collection#remove(java.lang.Object)
   */
  public boolean remove(Object object) {
    return this.values.remove(object);
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.util.Collection#containsAll(java.util.Collection)
   */
  public boolean containsAll(Collection<?> vals) {
    return this.values.containsAll(vals);
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.util.Collection#addAll(java.util.Collection)
   */
  public boolean addAll(Collection<? extends Scalar> vals) {
    return this.values.addAll(vals);
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.util.Collection#removeAll(java.util.Collection)
   */
  public boolean removeAll(Collection<?> vals) {
    return this.values.removeAll(vals);
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.util.Collection#retainAll(java.util.Collection)
   */
  public boolean retainAll(Collection<?> vals) {
    return this.values.retainAll(vals);
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.util.Collection#clear()
   */
  public void clear() {
    this.values.clear();
  }

  public String normalize() {
    return StrUtils.normalize(this.toString());
  }

  public String toString() {
    String value = "{";

    for (Iterator<Scalar> i = iterator(); i.hasNext();) {
      value += i.next().toString();
      if (i.hasNext()) {
        value += ", ";
      }
    }
    value += "}";

    return value;
  }

  /**
   * Provides a string representation of the Set with single or double quotes
   * surrounding each value if the retainQuotes flag is set to true and the
   * ValueType was SINGLE_QUOTED or DOUBLE_QUOTED for a value.
   * 
   * @param retainQuotes
   *          Set to true to return the value with quotes surrounding it if it
   *          was originally there in the label.
   * 
   * @return The string representation of the Set.
   */
  public String toString(boolean retainQuotes) {
    String value = "{";

    for (Iterator<Scalar> i = iterator(); i.hasNext();) {
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
    value += "}";

    return value;
  }

  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }
    if (!(object instanceof Set)) {
      return false;
    }
    Set that = (Set) object;
    if (this.size() != that.size()) {
      return false;
    }
    for (Iterator<Scalar> i = this.iterator(); i.hasNext();) {
      if (!that.contains(i.next())) {
        return false;
      }
    }
    return true;
  }

  public int hashcode() {
    return this.values.hashCode();
  }

}
