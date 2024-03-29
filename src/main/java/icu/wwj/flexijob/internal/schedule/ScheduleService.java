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

package icu.wwj.flexijob.internal.schedule;

import com.google.common.base.Strings;
import icu.wwj.flexijob.internal.FlexijobRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.elasticjob.api.ElasticJob;
import org.apache.shardingsphere.elasticjob.api.JobConfiguration;
import org.apache.shardingsphere.elasticjob.lite.api.bootstrap.impl.ScheduleJobBootstrap;
import org.apache.shardingsphere.elasticjob.lite.internal.config.ConfigurationService;
import org.apache.shardingsphere.elasticjob.reg.base.CoordinatorRegistryCenter;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public final class ScheduleService {
    
    private final CoordinatorRegistryCenter registryCenter;
    
    private final FlexijobRegistry flexijobRegistry;
    
    public void scheduleJob(final String jobName) {
        String elasticjobClassOrType = registryCenter.get("/" + jobName);
        if (Strings.isNullOrEmpty(elasticjobClassOrType)) {
            log.debug("The job '{}' missing elasticjob class or type will not be scheduled", jobName);
            return;
        }
        ConfigurationService configurationService = new ConfigurationService(registryCenter, jobName);
        JobConfiguration jobConfiguration = configurationService.load(false);
        if (Strings.isNullOrEmpty(jobConfiguration.getCron())) {
            log.debug("The job '{}' missing cron will not be scheduled", jobName);
            return;
        }
        ScheduleJobBootstrap scheduleJobBootstrap = instantiate(elasticjobClassOrType)
                .map(elasticJob -> new ScheduleJobBootstrap(registryCenter, elasticJob, jobConfiguration))
                .orElseGet(() -> new ScheduleJobBootstrap(registryCenter, elasticjobClassOrType, jobConfiguration));
        scheduleJobBootstrap.schedule();
        flexijobRegistry.registerJob(jobName, scheduleJobBootstrap);
    }
    
    private Optional<ElasticJob> instantiate(final String elasticjob) {
        Class<?> elasticjobClass;
        try {
            elasticjobClass = Class.forName(elasticjob);
        } catch (final ClassNotFoundException ignored) {
            return Optional.empty();
        }
        try {
            Constructor<?> noArgConstructor = elasticjobClass.getConstructor();
            return Optional.of((ElasticJob) noArgConstructor.newInstance());
        } catch (final NoSuchMethodException ex) {
            log.error("Require no args constructor for {}", elasticjob, ex);
        } catch (final IllegalAccessException | InstantiationException | InvocationTargetException ex) {
            log.warn(ex.getLocalizedMessage(), ex);
        }
        return Optional.empty();
    }
}
