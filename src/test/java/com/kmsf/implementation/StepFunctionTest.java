package com.kmsf.implementation;

import com.kmsf.implementation.StepFunction;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

class StepFunctionTest {

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
    void shouldImplementALinearByIntervalFunction() {
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

}