package com.souvikmondal01.flare.performance

import com.google.firebase.perf.metrics.Trace

interface PerformanceTracker {
    fun startTrace(name: String): Trace
    fun stopTrace(trace: Trace)
}
