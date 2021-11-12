package icu.wwj.flexijob.examples;

import icu.wwj.flexijob.examples.job.DemoJob;
import org.apache.curator.framework.api.transaction.CuratorOp;
import org.apache.shardingsphere.elasticjob.api.JobConfiguration;
import org.apache.shardingsphere.elasticjob.infra.pojo.JobConfigurationPOJO;
import org.apache.shardingsphere.elasticjob.infra.yaml.YamlEngine;
import org.apache.shardingsphere.elasticjob.lite.internal.storage.JobNodePath;
import org.apache.shardingsphere.elasticjob.lite.internal.storage.JobNodeStorage;
import org.apache.shardingsphere.elasticjob.reg.base.CoordinatorRegistryCenter;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class CreateNewJob {
    
    public static void main(String[] args) {
        CoordinatorRegistryCenter registryCenter = RegistryCenters.createRegistryCenter("127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183", "flexijob-examples");
        JobConfigurationPOJO jobConfigurationPOJO = JobConfigurationPOJO.fromJobConfiguration(JobConfiguration.newBuilder("flexijob-0", 3).cron("0/10 * * * * ?").build());
        JobNodePath jobNodePath = new JobNodePath(jobConfigurationPOJO.getJobName());
        JobNodeStorage jobNodeStorage = new JobNodeStorage(registryCenter, jobConfigurationPOJO.getJobName());
        jobNodeStorage.executeInTransaction(transactionOp -> {
            List<CuratorOp> result = new ArrayList<>(2);
            result.add(transactionOp.create().forPath("/" + jobConfigurationPOJO.getJobName(), DemoJob.class.getName().getBytes(StandardCharsets.UTF_8)));
            result.add(transactionOp.create().forPath(jobNodePath.getConfigNodePath(), YamlEngine.marshal(jobConfigurationPOJO).getBytes(StandardCharsets.UTF_8)));
            return result;
        });
    }
}
