package com.kmsf.implementation;

import com.kmsf.implementation.StepFunction;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

class StepFunctionTest {

    @Test
    void shouldBuildSimpleStepFunction() {
        // given
        var function = new StepFunction(0);
        // when
        function = function.addStep(0, 10);
        // then
        assertEquals(0, function.apply(-10));
        assertEquals(10, function.apply(0));
        assertEquals(10, function.apply(10));
    }

    @Test
    void shouldBuildTwoStepFunction() {
        // given
        var function = new StepFunction(0);
        // when
        function = function.addStep(0, 10);
        function = function.addStep(10, 100);
        // then
        assertEquals(2, function.size());
        assertEquals(0, function.apply(-5));
        assertEquals(10, function.apply(0));
        assertEquals(10, function.apply(5));
        assertEquals(100, function.apply(10));
        assertEquals(100, function.apply(15));
    }

    @Test
    void shouldBuildStepFunction() {
        // given
        var function = new StepFunction("undefined")
            .addStep(0, "very-low")
            .addStep(51, "low")
            .addStep(101, "high")
            .addStep(151, "very-high");
        // when
        var results = Arrays.stream(new Integer[]{-10,10,60,120,200}).map(function).toArray();
        // then
        assertArrayEquals(
                new String[]{"undefined","very-low","low","high","very-high"},
                results);
    }

    @Test
    void shouldBuildConstantFunction() {
        // given
        var function = new StepFunction(0);
        // when
        var results = Arrays.stream(new Integer[]{-10,10,60,120,200}).map(function).toArray();
        // then
        assertArrayEquals(
                new Integer[]{0,0,0,0,0},
                results);
    }

    @Test
    void shouldBuildStepFunctionWhateverTheOrderDefinition() {
        // given
        var function = new StepFunction("undefined")
            .addStep(151, "very-high")
            .addStep(0, "very-low")
            .addStep(101, "high")
            .addStep(51, "low");
        // when
        var results = Arrays.stream(new Integer[]{-10,10,60,120,200}).map(function).toArray();
        // then
        assertArrayEquals(
                new String[]{"undefined","very-low","low","high","very-high"},
                results);
    }

    @Test
    void shouldNotAllowDefiningTwiceTheSamePointWithDifferentValue() {
        // given
        StepFunction function = new StepFunction("undefined").addStep(0, "very-low").addStep(51, "low");
        // when
        // ???
        // then
        assertThrows(IllegalArgumentException.class, () -> function.addStep(51, "high"));
    }

    @Test
    void shouldAllowDefiningTwiceTheSamePointWithSameValue() {
        // given
        StepFunction function = new StepFunction("undefined").addStep(0, "very-low").addStep(51, "low");
        // when
        // ???
        // then
        assertDoesNotThrow(() -> function.addStep(51, "low"));
        assertEquals(function, function.addStep(51, "low"));
    }

    @Test
    void shouldImplementIdentity() {
        // given
        var steps = new Integer[]{0,51,101,151};
        var function = new StepFunction<>(Integer.MIN_VALUE).addSteps(steps, steps);
        // when
        var values = Arrays.stream(steps).map(function).toArray();
        // then
        assertArrayEquals(steps, values);
    }

    @Test
    void shouldReturnUndefined() {
        // given
        var steps = new Integer[]{0,51,101,151};
        var function = new StepFunction<>(Integer.MIN_VALUE).addSteps(steps, steps);
        // when
        var value = function.apply(-1);
        // then
        assertEquals(Integer.MIN_VALUE, value);
    }

    @Test
    void shouldImplementLinearByIntervalFunction() {
        // given
        var function = new StepFunction<>((Function<Integer, Integer>)new StepFunction<Integer, Integer>(0))
                .addStep(0, input -> input)
                .addStep(42, input -> 2*input)
                .addStep(84, input -> 4*input);
        // when
        Function<Integer, Integer> finalFunction = x -> function.apply(x).apply(x);
        var results = Arrays.stream(new Integer[]{-10,10,60,120}).map(x -> finalFunction.apply(x)).toArray();
        // then
        assertArrayEquals(
                new Integer[]{0,10,120,480},
                results);

    }

    @Test
    void shouldImplementEqualsAndHashcode() {
        // given
        var myFunction = new StepFunction<>("a").addStep(20, "b").addStep(30, "c");
        // when
        var sameFunction = new StepFunction<>("a").addStep(20, "b").addStep(30, "c");
        var anotherFunction = new StepFunction<>("a").addStep(30, "c").addStep(20, "b");
        var differentFunction = new StepFunction<>("a").addStep(20, "c").addStep(30, "b");
        // then
        assertEquals(myFunction, anotherFunction);
        assertEquals(myFunction, myFunction);
        assertEquals(myFunction, sameFunction);
        assertEquals(myFunction, anotherFunction);
        assertEquals(anotherFunction, myFunction);
        assertNotEquals(myFunction, differentFunction);
        assertNotEquals(differentFunction, myFunction);
        assertNotEquals(myFunction, new Object());
        assertEquals(myFunction.hashCode(), anotherFunction.hashCode());
    }

    @Test
    void shouldPrintItselfInAscendingOrder() {
        // given
        var myFunction = new StepFunction<>("a").addStep(20, "b").addStep(30, "c");
        // when
        var print = myFunction.toString();
        //
        assertEquals("f=[a;(20=b);(30=c)]", print);
    }

}