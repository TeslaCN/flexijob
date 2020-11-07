package icu.wwj.flexijob.internal.listener;

import icu.wwj.flexijob.internal.schedule.ScheduleService;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.CuratorCacheListener;
import org.apache.zookeeper.data.Stat;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
final class NewJobListenerTest {
    
    @Mock
    ScheduleService scheduleService;
    
    @InjectMocks
    NewJobListener newJobListener;
    
    @Test
    void assertNewJobCreated() {
        newJobListener.event(CuratorCacheListener.Type.NODE_CREATED, null, new ChildData("/test-job/config", new Stat(), new byte[0]));
        verify(scheduleService).scheduleJob("test-job");
    }
    
    @Test
    void assertNotNewJobCreated() {
        newJobListener.event(CuratorCacheListener.Type.NODE_CREATED, null, new ChildData("/test-job", new Stat(), new byte[0]));
        verify(scheduleService, never()).scheduleJob(anyString());
    }
}
