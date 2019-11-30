package com.kmsf.becquerel;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class BecquerelAlertDetectorTest {

    @Test
    void shouldDetectAlertLevelAccordingToSpecs() {
        // given
        var measures = new Float[]{-10f,10f,60f,120f,200f};
        // when
        var alerts = Arrays.stream(measures).map(BecquerelService.alertDetector).toArray();
        // then
        assertArrayEquals(
                new AlertLevel[]{AlertLevel.Undefined, AlertLevel.VeryLow, AlertLevel.Low, AlertLevel.High, AlertLevel.VeryHigh},
                alerts);
    }

}