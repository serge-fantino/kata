package com.kmsf.implementation;

import java.util.*;
import java.util.function.Function;

/**
 * A step function is... well, better check https://en.wikipedia.org/wiki/Step_function
 *
 * The function is a pure functional one, you can create functions incrementally by adding steps (which result in a new function).
 * The initial function is a constant function.
 * Then you can add point x1 with value y1, so that any point x'>=x1 will have the value y1
 * Note that you don't have to add point in any particular order. At the end if you added n points, they will be reordered as x1,..,xn so that:
 * x1<...<xn
 *
 * Adding the different point with same limit value will cause an error.
 *
 * Eg: new StepFunction(a0).addStep(x1,a1).addStep(x2,a2).addStep(x4,a4).addStep(x3,a3) will result in the nice step function depicted in the wikipedia page.
 *
 * The resulting step function is a Function, so just use it or apply()
 *
 * @param <IN> this is the function type input
 * @param <OUT> this is the function type output
 */
public class StepFunction<IN extends Comparable, OUT> implements Function<IN, OUT> {

    private final OUT constantValue;
    private HeadTailList<Step<IN, OUT>> orderedSteps = new HeadTailList<>();

    public StepFunction(OUT constantValue) {
        this.constantValue = constantValue;
    }

    /**
     * create a new StepFUnction by adding a new Step at the right position
     * @param function
     * @param newStep
     */
    protected StepFunction(StepFunction<IN, OUT> function, Step<IN, OUT> newStep) {
        this.constantValue = function.constantValue;
        this.orderedSteps = insertStep(function.orderedSteps, newStep);
    }

    public StepFunction<IN, OUT> addStep(IN limit, OUT value) {
        Step step = new Step(limit, value);
        if (orderedSteps.contains(step)) return this;
        return new StepFunction<IN, OUT>(this, new Step<IN, OUT>(limit, value));
    }

    public StepFunction<IN, OUT> addSteps(IN[] steps, OUT[] values) {
        assert steps.length==values.length;
        var function = this;
        for (int index = 0; index < steps.length; index++) {
            function = function.addStep(steps[index], values[index]);
        }
        return function;
    }

    @Override
    public OUT apply(IN input) {
        // given: orderedSteps is ordered in reverse order, eg == {x1,..,xn}
        // so that x1>...>xn
        // when: input>=xj AND for any i<j => input<xi
        // then: input>xk if k>j because input>=xj>xk
        // and then: xi>input>=xj for any i<j (so if j=1, there is no i)
        for (Step<IN, OUT> step : orderedSteps) {
            if (step.contains(input)) return step.getValue();
        }
        return this.constantValue;
    }

    public int size() {
        return orderedSteps.size();
    }

    private HeadTailList<Step<IN,OUT>> insertStep(HeadTailList<Step<IN,OUT>> orderedSteps, Step<IN,OUT> newStep) {
        if (orderedSteps.isEmpty()) return new HeadTailList(newStep);
        var head = orderedSteps.head();
        if (head.compareTo(newStep)<0) return orderedSteps.add(newStep);
        if (head.compareTo(newStep)==0)
            throw new IllegalArgumentException("a step with limit "+head.limit+" already exists with a different value "+head.value);
        return insertStep(orderedSteps.tail(), newStep).add(head);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj==this) return true;
        if (!(obj instanceof StepFunction)) return false;
        StepFunction fun = (StepFunction) obj;
        return fun.constantValue.equals(this.constantValue) && fun.orderedSteps.equals(this.orderedSteps);
    }

    @Override
    public int hashCode() {
        return Objects.hash(constantValue, orderedSteps);
    }

    @Override
    public String toString() {
        return "f=["+constantValue+";"+orderedSteps+"]";
    }

    private static class Step<IN extends Comparable, OUT> implements Comparable<Step<IN, OUT>> {

        private IN limit;
        private OUT value;

        public Step(IN limit, OUT value) {
            this.limit = limit;
            this.value = value;
        }

        public OUT getValue() {
            return value;
        }

        @Override
        public int compareTo(Step<IN, OUT> o) {
            return this.limit.compareTo(o.limit);
        }

        /**
         * return true if limit<=input, e.g input is inside [limit, +infinity[
         * @param input
         * @return
         */
        public boolean contains(IN input)  {
            return limit.compareTo(input)<=0;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj==this) return true;
            if (!(obj instanceof Step)) return false;
            Step step = (Step) obj;
            return this.limit.equals(step.limit) && this.value.equals(step.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(limit, value);
        }

        @Override
        public String toString() {
            return "("+limit+"="+value+")";
        }
    }
}
