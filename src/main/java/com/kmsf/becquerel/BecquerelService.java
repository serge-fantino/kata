package com.kmsf.becquerel;

import com.kmsf.implementation.StepFunction;

public class BecquerelService {

    /**
     * Detector return an Alert based on the measured becquerel level
     */
    public static final StepFunction<Float, AlertLevel> alertDetector =
            new StepFunction.Builder<Float, AlertLevel>(AlertLevel.Undefined)
                .add(0f, AlertLevel.VeryLow)
                .add(51f, AlertLevel.Low)
                .add(101f, AlertLevel.High)
                .add(151f, AlertLevel.VeryHigh)
                .build();

}
