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

import lombok.RequiredArgsConstructor;
import org.apache.curator.framework.api.transaction.CuratorOp;
import org.apache.shardingsphere.elasticjob.api.ElasticJob;
import org.apache.shardingsphere.elasticjob.api.JobConfiguration;
import org.apache.shardingsphere.elasticjob.infra.pojo.JobConfigurationPOJO;
import org.apache.shardingsphere.elasticjob.infra.yaml.YamlEngine;
import org.apache.shardingsphere.elasticjob.lite.internal.storage.JobNodePath;
import org.apache.shardingsphere.elasticjob.lite.internal.storage.JobNodeStorage;
import org.apache.shardingsphere.elasticjob.reg.base.CoordinatorRegistryCenter;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Flexijob client.
 */
@RequiredArgsConstructor
public class FlexijobClient {
    
    private final CoordinatorRegistryCenter registryCenter;
    
    /**
     * Add classed job to register center. The ElasticJob Class must have a no args constructor.
     *
     * @param jobConfiguration job configuration
     * @param elasticJobClass  ElasticJob Class
     */
    public void addJob(final JobConfiguration jobConfiguration, final Class<? extends ElasticJob> elasticJobClass) {
        addJob(JobConfigurationPOJO.fromJobConfiguration(jobConfiguration), elasticJobClass.getName());
    }
    
    /**
     * Add typed job to register center.
     *
     * @param jobConfiguration job configuration
     * @param elasticJobType   ElasticJob type
     */
    public void addJob(final JobConfiguration jobConfiguration, final String elasticJobType) {
        addJob(JobConfigurationPOJO.fromJobConfiguration(jobConfiguration), elasticJobType);
    }
    
    private void addJob(final JobConfigurationPOJO jobConfigurationPOJO, final String elasticJobClassOrType) {
        JobNodePath jobNodePath = new JobNodePath(jobConfigurationPOJO.getJobName());
        JobNodeStorage jobNodeStorage = new JobNodeStorage(registryCenter, jobConfigurationPOJO.getJobName());
        jobNodeStorage.executeInTransaction(transactionOp -> {
            List<CuratorOp> result = new ArrayList<>(2);
            result.add(transactionOp.create().forPath("/" + jobConfigurationPOJO.getJobName(), elasticJobClassOrType.getBytes(StandardCharsets.UTF_8)));
            result.add(transactionOp.create().forPath(jobNodePath.getConfigNodePath(), YamlEngine.marshal(jobConfigurationPOJO).getBytes(StandardCharsets.UTF_8)));
            return result;
        });
    }
}
