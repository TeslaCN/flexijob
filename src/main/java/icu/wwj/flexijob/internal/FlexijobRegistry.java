package icu.wwj.flexijob.internal;

import org.apache.shardingsphere.elasticjob.lite.api.bootstrap.impl.ScheduleJobBootstrap;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public final class FlexijobRegistry {
    
    private final ConcurrentMap<String, ScheduleJobBootstrap> jobBootstraps = new ConcurrentHashMap<>();
    
    public void registerJob(final String jobName, final ScheduleJobBootstrap scheduleJobBootstrap) {
        jobBootstraps.put(jobName, scheduleJobBootstrap);
    }
    
    public void shutdownAll() {
        jobBootstraps.values().forEach(ScheduleJobBootstrap::shutdown);
    }
}
