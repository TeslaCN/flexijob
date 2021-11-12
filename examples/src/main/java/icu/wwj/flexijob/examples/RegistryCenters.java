package icu.wwj.flexijob.examples;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.shardingsphere.elasticjob.reg.base.CoordinatorRegistryCenter;
import org.apache.shardingsphere.elasticjob.reg.zookeeper.ZookeeperConfiguration;
import org.apache.shardingsphere.elasticjob.reg.zookeeper.ZookeeperRegistryCenter;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RegistryCenters {
    
    public static CoordinatorRegistryCenter createRegistryCenter(final String serverLists, final String namespace) {
        ZookeeperConfiguration zookeeperConfiguration = new ZookeeperConfiguration(serverLists, "flexijob-examples");
        CoordinatorRegistryCenter result = new ZookeeperRegistryCenter(zookeeperConfiguration);
        result.init();
        return result;
    }
}
