/*
 * Copyright 2021 Weijie Wu
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
