package com.example.etl_off.etl;

import java.time.Duration;

/**
 * Performance report produced after an import.
 */
public class ImportPerformanceReport {

    private final int totalLines;
    private final int importedProducts;
    private final int rejectedLines;
    private final Duration parsingDuration;
    private final Duration persistenceDuration;
    private final Duration totalDuration;
    private final Duration processCpuDuration;
    private final long usedMemoryBeforeMb;
    private final long usedMemoryAfterMb;
    private final int peakThreadCount;
    private final int virtualTasks;

    public ImportPerformanceReport(int totalLines,
                                   int importedProducts,
                                   int rejectedLines,
                                   Duration parsingDuration,
                                   Duration persistenceDuration,
                                   Duration totalDuration,
                                   Duration processCpuDuration,
                                   long usedMemoryBeforeMb,
                                   long usedMemoryAfterMb,
                                   int peakThreadCount,
                                   int virtualTasks) {
        this.totalLines = totalLines;
        this.importedProducts = importedProducts;
        this.rejectedLines = rejectedLines;
        this.parsingDuration = parsingDuration;
        this.persistenceDuration = persistenceDuration;
        this.totalDuration = totalDuration;
        this.processCpuDuration = processCpuDuration;
        this.usedMemoryBeforeMb = usedMemoryBeforeMb;
        this.usedMemoryAfterMb = usedMemoryAfterMb;
        this.peakThreadCount = peakThreadCount;
        this.virtualTasks = virtualTasks;
    }

    /**
     * Formats the report as readable log lines.
     *
     * @return formatted report
     */
    public String toLogMessage() {
        return "Open Food Facts import finished\n"
                + "- read lines: " + totalLines + "\n"
                + "- imported products: " + importedProducts + "\n"
                + "- rejected lines: " + rejectedLines + "\n"
                + "- parsing time: " + parsingDuration.toMillis() + " ms\n"
                + "- persistence time: " + persistenceDuration.toMillis() + " ms\n"
                + "- total time: " + totalDuration.toMillis() + " ms\n"
                + "- process CPU time: " + processCpuDuration.toMillis() + " ms\n"
                + "- used heap: " + usedMemoryBeforeMb + " MB -> " + usedMemoryAfterMb + " MB\n"
                + "- peak JVM threads: " + peakThreadCount + "\n"
                + "- virtual-thread tasks: " + virtualTasks + "\n";
    }
}
