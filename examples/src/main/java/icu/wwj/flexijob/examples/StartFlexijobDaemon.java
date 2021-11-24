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
