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
