/*
 * Copyright (c) 2009-2010 jMonkeyEngine
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'jMonkeyEngine' nor the names of its contributors
 *   may be used to endorse or promote products derived from this software
 *   without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.jme3.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * Implementation of a Map that favors iteration speed rather than
 * get/put speed.
 *
 * @author Kirill Vainer
 */
public final class ListMap<K, V> implements Map<K, V>, Cloneable, Serializable {

    public static void main(String[] args){
        Map<String, String> map = new ListMap<String, String>();
        map.put( "bob", "hello");
        System.out.println(map.get("bob"));
        map.remove("bob");
        System.out.println(map.size());

        map.put("abc", "1");
        map.put("def", "2");
        map.put("ghi", "3");
        map.put("jkl", "4");
        map.put("mno", "5");
        System.out.println(map.get("ghi"));
    }

    private final static class ListMapEntry<K, V> implements Map.Entry<K, V>, Cloneable {

        private final K key;
        private V value;

        public ListMapEntry(K key, V value){
            this.key = key;
            this.value = value;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }

        public V setValue(V v) {
            throw new UnsupportedOperationException();
        }

        @Override
        public ListMapEntry<K, V> clone(){
            return new ListMapEntry<K, V>(key, value);
        }

    }
    
    private final ArrayList<ListMapEntry<K,V>> entries;

    public ListMap(){
       entries = new ArrayList<ListMapEntry<K,V>>();
    }

    public ListMap(int initialCapacity){
        entries = new ArrayList<ListMapEntry<K, V>>(initialCapacity);
    }

    public ListMap(Map<? extends K, ? extends V> map){
        entries = new ArrayList<ListMapEntry<K, V>>(map.size());
        putAll(map);
    }

    public int size() {
        return entries.size();
    }

    public Entry<K, V> getEntry(int index){
        return entries.get(index);
    }

    public V getValue(int index){
        return entries.get(index).value;
    }

    public K getKey(int index){
        return entries.get(index).key;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    private static boolean keyEq(Object keyA, Object keyB){
        return keyA.hashCode() == keyB.hashCode() ? (keyA == keyB) || keyA.equals(keyB) : false;
    }

    private static boolean valEq(Object a, Object b){
        return a == null ? (b == null) : a.equals(b);
    }

    public boolean containsKey(Object key) {
        if (key == null)
            throw new IllegalArgumentException();

        for (int i = 0; i < entries.size(); i++){
            ListMapEntry<K,V> entry = entries.get(i);
            if (keyEq(entry.key, key))
                return true;
        }
        return false;
    }

    public boolean containsValue(Object value) {
        for (int i = 0; i < entries.size(); i++){
            if (valEq(entries.get(i).value, value))
                return true;
        }
        return false;
    }

    public V get(Object key) {
        if (key == null)
            throw new IllegalArgumentException();

        for (int i = 0; i < entries.size(); i++){
            ListMapEntry<K,V> entry = entries.get(i);
            if (keyEq(entry.key, key))
                return entry.value;
        }
        return null;
    }

    public V put(K key, V value) {
        if (key == null)
            throw new IllegalArgumentException();

        // check if entry exists, if yes, overwrite it with new value
        for (int i = 0; i < entries.size(); i++){
            ListMapEntry<K,V> entry = entries.get(i);
            if (keyEq(entry.key, key)){
                V prevValue = entry.value;
                entry.value = value;
                return prevValue;
            }
        }
        
        // add a new entry
        entries.add(new ListMapEntry<K, V>(key, value));
        return null;
    }

    public V remove(Object key) {
        if (key == null)
            throw new IllegalArgumentException();

        for (int i = 0; i < entries.size(); i++){
            ListMapEntry<K,V> entry = entries.get(i);
            if (keyEq(entry.key, key)){
                return entries.remove(i).value;
            }
        }
        return null;
    }

    public void putAll(Map<? extends K, ? extends V> map) {
        if (map instanceof ListMap){
            ListMap<K, V> listMap = (ListMap<K, V>) map;
            ArrayList<ListMapEntry<K, V>> otherEntries = listMap.entries;
            for (int i = 0; i < otherEntries.size(); i++){
                ListMapEntry<K, V> entry = otherEntries.get(i);
                put(entry.key, entry.value);
            }
        }else{
            for (Map.Entry<? extends K, ? extends V> entry : map.entrySet()){
                put(entry.getKey(), entry.getValue());
            }
        }
        
    }

    public void clear() {
        entries.clear();
    }

    @Override
    public ListMap<K, V> clone(){
        ListMap<K, V> clone = new ListMap<K, V>(size());
        clone.putAll(this);
        return clone;
    }

    public Set<K> keySet() {
        HashSet<K> keys = new HashSet<K>();
        for (int i = 0; i < entries.size(); i++){
            ListMapEntry<K,V> entry = entries.get(i);
            keys.add(entry.key);
        }
        return keys;
    }

    public Collection<V> values() {
        ArrayList<V> values = new ArrayList<V>();
        for (int i = 0; i < entries.size(); i++){
            ListMapEntry<K,V> entry = entries.get(i);
            values.add(entry.value);
        }
        return values;
    }

    public Set<Entry<K, V>> entrySet() {
        HashSet<Entry<K, V>> entryset = new HashSet<Entry<K, V>>();
        entryset.addAll(entries);
        return entryset;
    }

}
