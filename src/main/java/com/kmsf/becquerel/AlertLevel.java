package com.kmsf.becquerel;

/**
 * universal Alert levels
 */
public enum AlertLevel {
    Undefined, // the measures are not in the valid range
    VeryLow, // hum, so low it is worrying ?
    Low, // ok, business as usual
    High, // time to invest in a shelter
    VeryHigh // doomed
}
