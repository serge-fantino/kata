package com.kmsf.implementation;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 * A step function is... well, better check https://en.wikipedia.org/wiki/Step_function
 *
 * You must create the function with a builder. Start by defining the function constant value.
 * Then you can add point x1 with value y1, so that any point x'>=x1 will have the value y1
 * Note that you don't have to add point in any particular order. At the end if you added n points, they will be reordered as x1,..,xn so that:
 * x1<...<xn
 *
 * Adding the same point twice will cause an error.
 *
 * Eg: new Builder(a0).add(x1,a1).add(x2,a2).add(x4,a4).add(x3,a3).build() will result in the nice step function depicted in the wikipedia page.
 *
 * The resulting step function is a Function, so just use it or apply()
 *
 * @param <IN> this is the function type input
 * @param <OUT> this is the function type output
 */
public class StepFunction<IN extends Comparable, OUT> implements Function<IN, OUT> {

    public static class Builder<IN extends Comparable, OUT> {

        private final OUT undefined;
        private List<Step<IN, OUT>> steps = new ArrayList<>();

        public Builder(OUT undefined) {
            this.undefined = undefined;
        }

        public Builder<IN, OUT> add(IN step, OUT value) {
            if (assertUniqueValue(step, value)) {
                this.steps.add(new Step<IN, OUT>(step, value));
            }
            return this;
        }

        private boolean assertUniqueValue(IN step, OUT value) {
            Optional<Step<IN, OUT>> duplicate = this.steps.stream().filter(x -> x.step.equals(step)).findFirst();
            if (duplicate.isPresent()) {
                if (duplicate.get().value.equals(value)) return false;
                throw new IllegalArgumentException("this step is already defined with a different value ("+duplicate.get().step+","+duplicate.get().value+")");
            }
            return true;
        }

        public Builder<IN, OUT> addAll(IN[] steps, OUT[] values) {
            assert steps.length==values.length;
            for (int index = 0; index < steps.length; index++) {
                add(steps[index], values[index]);
            }
            return this;
        }

        public StepFunction<IN, OUT> build() {
            return new StepFunction<IN, OUT>(steps, undefined);
        }
    }

    private final OUT undefined;
    private List<Step<IN, OUT>> orderedSteps = new ArrayList<>();

    protected StepFunction(List<Step<IN, OUT>> steps, OUT undefined) {
        this.undefined = undefined;
        this.orderedSteps = new ArrayList<>(steps);
        this.orderedSteps.sort(Comparator.reverseOrder());
    }

    @Override
    public OUT apply(IN s) {
        for (Step<IN, OUT> step : orderedSteps) {
            if (step.step.compareTo(s)<=0) return step.getValue();
        }
        return this.undefined;
    }

    private static class Step<IN extends Comparable, OUT> implements Comparable<Step<IN, OUT>> {

        private IN step;
        private OUT value;

        public Step(IN step, OUT value) {
            this.step = step;
            this.value = value;
        }

        public OUT getValue() {
            return value;
        }

        @Override
        public int compareTo(Step<IN, OUT> o) {
            return this.step.compareTo(o.step);
        }
    }
}
