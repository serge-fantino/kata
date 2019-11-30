package com.kmsf.implementation;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class HeadTailListTest {

    @Test
    void shouldCreateAnEmptyList() {
        // given
        var empty = new HeadTailList<Integer>();
        // when
        var isEmpty = empty.isEmpty();
        // then
        assertTrue(isEmpty);
        assertEquals(0, empty.size());
        assertThrows(NoSuchElementException.class, empty::head);
        assertThrows(NoSuchElementException.class, empty::tail);
    }

    @Test
    void shouldCreateSingleton() {
        // given
        var empty = new HeadTailList<Integer>();
        // when
        var singleton = empty.add(10);
        // then
        assertEquals(1, singleton.size());
        assertEquals(10, singleton.head());
        assertFalse(singleton.isEmpty());
        assertTrue(singleton.tail().isEmpty());
        assertNotEquals(empty, singleton);
        assertTrue(empty.isEmpty());
        assertEquals(empty, singleton.tail());
    }

    @Test
    void shouldExtendList() {
        // given
        var singleton = new HeadTailList<Integer>(10);
        // when
        var mylist = singleton.add(20).add(30);
        // then
        assertEquals(3, mylist.size());
        assertFalse(mylist.isEmpty());
        assertEquals(30, mylist.head());
        assertEquals(singleton.add(20), mylist.tail());
        assertEquals(singleton, mylist.tail().tail());
        assertEquals(new HeadTailList<>(), mylist.tail().tail().tail());
        assertThrows(NoSuchElementException.class, () -> mylist.tail().tail().tail().tail());
    }

    @Test
    void shouldContainsElements() {
        // given
        var singleton = new HeadTailList<Integer>(10);
        // when
        var mylist = singleton.add(20).add(30);
        // then
        assertTrue(mylist.contains(10));
        assertTrue(mylist.contains(20));
        assertTrue(mylist.contains(30));
        assertFalse(mylist.contains(0));
    }

    @Test
    void shouldIterateInLILOOrder() {
        // given
        var mylist = new HeadTailList<Integer>(10).add(20).add(30);
        // when
        var iterator = mylist.iterator();
        //
        assertTrue(iterator.hasNext());
        assertEquals(30, iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(20, iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(10, iterator.next());
        assertFalse(iterator.hasNext());
        assertThrows(NoSuchElementException.class, iterator::next);
    }

    @Test
    void shouldImplementsEqualsAndHashcode() {
        // given
        var myList = new HeadTailList<Integer>(10).add(20).add(30);
        // when
        var anotherList = new HeadTailList<Integer>(10).add(20).add(30);
        var differentList = new HeadTailList<Integer>(10).add(30).add(30);
        // then
        assertEquals(myList, anotherList);
        assertEquals(anotherList, myList);
        assertNotEquals(myList, differentList);
        assertNotEquals(differentList, myList);
        assertNotEquals(myList, new Object());
        assertEquals(myList.hashCode(), anotherList.hashCode());
    }

    @Test
    void shouldPrintItselfInFIFOOrder() {
        // given
        var myList = new HeadTailList<Integer>(10).add(20).add(30);
        // when
        var print = myList.toString();
        //
        assertEquals("10;20;30", print);
    }

}