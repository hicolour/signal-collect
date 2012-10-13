/*
 *  @author Philip Stutz
 *
 *  Copyright 2012 University of Zurich
 *      
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *  
 *         http://www.apache.org/licenses/LICENSE-2.0
 *  
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.signalcollect.storage

import com.signalcollect.interfaces.VertexStore
import com.signalcollect.Vertex
import scala.util.MurmurHash

class VertexMap(
    initialSize: Int = 32768,
    rehashFraction: Float = 0.75f) extends VertexStore {
  assert(initialSize > 0)
  var maxSize = nextPowerOfTwo(initialSize)
  assert(maxSize > 0 && maxSize >= initialSize, "Initial size is too large.")
  var maxElements = rehashFraction * maxSize
  var values = new Array[Vertex[_, _]](maxSize)
  var keys = new Array[Int](maxSize)
  var mask = maxSize - 1

  override def size = numberOfElements
  def isEmpty = numberOfElements == 0
  var numberOfElements = 0

  def foreach[U](f: Vertex[_, _] => U) {
    var i = 0
    var elementsProcessed = 0
    while (elementsProcessed < numberOfElements) {
      if (keys(i) != 0) {
        f(values(i))
        elementsProcessed += 1
      }
      i += 1
    }
  }

  protected def tryDouble {
    // 1073741824 is the largest size and cannot be doubled anymore.
    if (maxSize != 1073741824) {
      val oldSize = maxSize
      val oldValues = values
      val oldKeys = keys
      maxSize *= 2
      maxElements = rehashFraction * maxSize
      values = new Array[Vertex[_, _]](maxSize)
      keys = new Array[Int](maxSize)
      mask = maxSize - 1
      numberOfElements = 0
      var i = 0
      while (i < oldSize) {
        if (oldKeys(i) != 0) {
          put(oldValues(i))
        }
        i += 1
      }
    }
  }

  // http://stackoverflow.com/questions/279539/best-way-to-remove-an-entry-from-a-hash-table
  def remove(id: Any) {
    val key = MurmurHash.finalizeHash(id.hashCode) | Int.MinValue
    var position = key & mask
    var keyAtPosition = keys(position)
    while (keyAtPosition != 0 && key != keyAtPosition && id != values(position).id) {
      position = (position + 1) & mask
      keyAtPosition = keys(position)
    }
    // We can only remove the entry if it was found.
    if (keyAtPosition != 0) {
      keys(position) = 0
      values(position) = null
      numberOfElements -= 1
      position = (position + 1) & mask
      keyAtPosition = keys(position)
      if (keyAtPosition != 0) {
        val vertex = values(position)
        remove(vertex.id)
        put(vertex)
      }
    }
  }

  def get(vertexId: Any): Vertex[_, _] = {
    val key = MurmurHash.finalizeHash(vertexId.hashCode) | Int.MinValue
    var position = key & mask
    var keyAtPosition = keys(position)
    while (keyAtPosition != 0 && key != keyAtPosition && vertexId != values(position).id) {
      position = (position + 1) & mask
      keyAtPosition = keys(position)
    }
    if (keyAtPosition != 0) {
      values(position)
    } else {
      null.asInstanceOf[Vertex[_, _]]
    }
  }

  // Only put if no vertex with the same id is present. If a vertex was put, return true.
  def put(vertex: Vertex[_, _]): Boolean = {
    val key = MurmurHash.finalizeHash(vertex.id.hashCode) | Int.MinValue
    var position = key & mask
    var keyAtPosition: Int = keys(position)
    while (keyAtPosition != 0 && key != keyAtPosition && vertex.id != values(position).id) {
      position = (position + 1) & mask
      keyAtPosition = keys(position)
    }
    var doPut = keyAtPosition == 0
    if (doPut) {
      keys(position) = key
      values(position) = vertex
      numberOfElements += 1
      if (numberOfElements >= maxElements) {
        if (numberOfElements >= maxSize) {
          throw new OutOfMemoryError("The hash map is full and cannot be expanded any further.")
        }
        tryDouble
      }
    }
    doPut
  }

  protected def nextPowerOfTwo(x: Int): Int = {
    assert(x > 0)
    var r = x - 1
    r |= r >> 1
    r |= r >> 2
    r |= r >> 4
    r |= r >> 8
    r |= r >> 16
    r + 1
  }

}