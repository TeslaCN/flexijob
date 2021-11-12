package icu.wwj.flexijob.examples;

import icu.wwj.flexijob.Flexijob;
import org.apache.shardingsphere.elasticjob.reg.base.CoordinatorRegistryCenter;

public final class StartFlexijobDaemon {
    
    public static void main(String[] args) throws InterruptedException {
        CoordinatorRegistryCenter registryCenter = RegistryCenters.createRegistryCenter("127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183", "flexijob-examples");
        Flexijob flexijob = new Flexijob(registryCenter);
        flexijob.start();
        Thread.currentThread().join();
    }
}
