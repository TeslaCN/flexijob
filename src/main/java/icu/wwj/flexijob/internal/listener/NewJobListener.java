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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.CuratorCacheListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@RequiredArgsConstructor
public final class NewJobListener implements CuratorCacheListener {
    
    private static final Pattern CONFIG_PATH_PATTERN = Pattern.compile("/([^/]+)/config");
    
    private final ScheduleService scheduleService;
    
    @Override
    public void event(final Type type, final ChildData oldData, final ChildData data) {
        Matcher matcher;
        if (Type.NODE_CREATED != type || !(matcher = CONFIG_PATH_PATTERN.matcher(data.getPath())).matches()) {
            return;
        }
        String jobName = matcher.group(1);
        log.info("New job '{}'", jobName);
        scheduleService.scheduleJob(jobName);
    }
}
