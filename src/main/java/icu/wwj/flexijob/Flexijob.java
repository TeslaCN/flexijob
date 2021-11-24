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

package icu.wwj.flexijob;

import icu.wwj.flexijob.internal.FlexijobRegistry;
import icu.wwj.flexijob.internal.listener.NewJobListener;
import icu.wwj.flexijob.internal.schedule.ScheduleService;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.recipes.cache.CuratorCache;
import org.apache.shardingsphere.elasticjob.reg.base.CoordinatorRegistryCenter;

@Slf4j
public final class Flexijob {
    
    private final FlexijobRegistry flexijobRegistry = new FlexijobRegistry();
    
    private final CoordinatorRegistryCenter registryCenter;
    
    private final ScheduleService scheduleService;
    
    public Flexijob(final CoordinatorRegistryCenter registryCenter) {
        this.registryCenter = registryCenter;
        scheduleService = new ScheduleService(registryCenter, flexijobRegistry);
    }
    
    public void start() {
        startListeners();
        log.info("Flexijob started.");
    }
    
    private void startListeners() {
        registryCenter.addCacheData("/");
        CuratorCache cache = (CuratorCache) registryCenter.getRawCache("/");
        cache.listenable().addListener(new NewJobListener(scheduleService));
    }
    
    public void shutdown() {
        flexijobRegistry.shutdownAll();
    }
}
