package icu.wwj.flexijob.examples;

import icu.wwj.flexijob.FlexijobClient;
import icu.wwj.flexijob.examples.job.DemoJob;
import org.apache.shardingsphere.elasticjob.api.JobConfiguration;
import org.apache.shardingsphere.elasticjob.reg.base.CoordinatorRegistryCenter;

public class CreateNewJob {
    
    public static void main(String[] args) {
        CoordinatorRegistryCenter registryCenter = RegistryCenters.createRegistryCenter("127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183", "flexijob-examples");
        JobConfiguration jobConfiguration = JobConfiguration.newBuilder("flexijob-0", 3).cron("0/10 * * * * ?").build();
        FlexijobClient client = new FlexijobClient(registryCenter);
        client.addJob(jobConfiguration, DemoJob.class);
    }
}
