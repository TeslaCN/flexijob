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

package icu.wwj.flexijob.examples;

import icu.wwj.flexijob.FlexijobClient;
import icu.wwj.flexijob.examples.job.DemoJob;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.elasticjob.api.JobConfiguration;
import org.apache.shardingsphere.elasticjob.reg.base.CoordinatorRegistryCenter;

import java.util.concurrent.TimeUnit;

@Slf4j
public class CreateNewJob {
    
    public static void main(String[] args) throws InterruptedException {
        CoordinatorRegistryCenter registryCenter = RegistryCenters.createRegistryCenter("127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183", "flexijob-examples");
        JobConfiguration jobConfiguration = JobConfiguration.newBuilder("flexijob-0", 3).cron("0/10 * * * * ?").build();
        FlexijobClient client = new FlexijobClient(registryCenter);
        log.info("Adding job [{}]...", jobConfiguration.getJobName());
        client.addJob(jobConfiguration, DemoJob.class);
        log.info("Shutdown job [{}] after 25 seconds.", jobConfiguration.getJobName());
        TimeUnit.SECONDS.sleep(25);
        log.info("Shutting down job [{}]...", jobConfiguration.getJobName());
        client.removeJob(jobConfiguration.getJobName());
    }
}
