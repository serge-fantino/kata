package com.kmsf.implementation;

import com.kmsf.implementation.StepFunction;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class StepFunctionTest {

    @Test
    void shouldBuildStepFunction() {
        // given
        var builder = new StepFunction.Builder("undefined");
        builder.add(0, "very-low");
        builder.add(51, "low");
        builder.add(101, "high");
        builder.add(151, "very-high");
        // when
        var function = builder.build();
        // then
        assertArrayEquals(
                new String[]{"undefined","very-low","low","high","very-high"}
                ,Arrays.stream(new Integer[]{-10,10,60,120,200}).map(function).toArray());
    }

    @Test
    void shouldBuildStepFunctionWhateverTheOrderDefinition() {
        // given
        var builder = new StepFunction.Builder("undefined");
        builder.add(151, "very-high");
        builder.add(0, "very-low");
        builder.add(101, "high");
        builder.add(51, "low");
        // when
        var function = builder.build();
        // then
        assertArrayEquals(
                new String[]{"undefined","very-low","low","high","very-high"}
                ,Arrays.stream(new Integer[]{-10,10,60,120,200}).map(function).toArray());
    }

    @Test
    void shouldNotAllowDefiningTwiceTheSamePointWithDifferentValue() {
        // given
        var builder = new StepFunction.Builder("undefined");
        // when
        builder.add(0, "very-low");
        builder.add(51, "low");
        // then
        assertThrows(IllegalArgumentException.class, () -> builder.add(51, "high"));
    }

    @Test
    void shouldAllowDefiningTwiceTheSamePointWithSameValue() {
        // given
        var builder = new StepFunction.Builder("undefined");
        // when
        builder.add(0, "very-low");
        builder.add(51, "low");
        // then
        assertDoesNotThrow(() -> builder.add(51, "low"));
    }

    @Test
    void shouldImplementIdentity() {
        // given
        var steps = new Integer[]{0,51,101,151};
        var builder = new StepFunction.Builder<>(Integer.MIN_VALUE).addAll(steps, steps);
        // when
        var values = Arrays.stream(steps).map(builder.build()).toArray();
        // then
        assertArrayEquals(steps, values);
    }

    @Test
    void shouldReturnUndefined() {
        // given
        var steps = new Integer[]{0,51,101,151};
        var builder = new StepFunction.Builder<>(Integer.MIN_VALUE).addAll(steps, steps);
        // when
        var value = builder.build().apply(-1);
        // then
        assertEquals(Integer.MIN_VALUE, value);
    }

}