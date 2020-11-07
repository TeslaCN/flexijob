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
