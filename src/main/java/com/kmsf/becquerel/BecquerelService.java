package com.kmsf.becquerel;

import com.kmsf.implementation.StepFunction;

public class BecquerelService {

    /**
     * Detector return an Alert based on the measured becquerel level
     */
    public static final StepFunction<Float, AlertLevel> alertDetector =
            new StepFunction<Float, AlertLevel>(AlertLevel.Undefined)
                .addStep(0f, AlertLevel.VeryLow)
                .addStep(51f, AlertLevel.Low)
                .addStep(101f, AlertLevel.High)
                .addStep(151f, AlertLevel.VeryHigh);

}
