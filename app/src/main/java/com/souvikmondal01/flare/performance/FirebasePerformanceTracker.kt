package com.souvikmondal01.flare.performance

import com.google.firebase.perf.FirebasePerformance
import com.google.firebase.perf.metrics.Trace
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebasePerformanceTracker @Inject constructor() : PerformanceTracker {
    override fun startTrace(name: String): Trace {
        return FirebasePerformance.getInstance().newTrace(name).apply { start() }
    }

    override fun stopTrace(trace: Trace) {
        trace.stop()
    }
}
