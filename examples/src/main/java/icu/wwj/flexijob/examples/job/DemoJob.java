package icu.wwj.flexijob.examples.job;

import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.elasticjob.api.ShardingContext;
import org.apache.shardingsphere.elasticjob.simple.job.SimpleJob;

@Slf4j
public class DemoJob implements SimpleJob {
    
    @Override
    public void execute(final ShardingContext shardingContext) {
        log.info("{}", shardingContext);
    }
}
