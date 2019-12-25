/*
 * Copyright 1999-2018 Alibaba Group Holding Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.csp.sentinel.slots;

import com.alibaba.csp.sentinel.slotchain.DefaultProcessorSlotChain;
import com.alibaba.csp.sentinel.slotchain.ProcessorSlotChain;
import com.alibaba.csp.sentinel.slotchain.SlotChainBuilder;
import com.alibaba.csp.sentinel.slots.block.authority.AuthoritySlot;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeSlot;
import com.alibaba.csp.sentinel.slots.block.flow.FlowSlot;
import com.alibaba.csp.sentinel.slots.clusterbuilder.ClusterBuilderSlot;
import com.alibaba.csp.sentinel.slots.logger.LogSlot;
import com.alibaba.csp.sentinel.slots.nodeselector.NodeSelectorSlot;
import com.alibaba.csp.sentinel.slots.statistic.StatisticSlot;
import com.alibaba.csp.sentinel.slots.system.SystemSlot;

/**
 * Builder for a default {@link ProcessorSlotChain}.
 *
 * @author qinan.qn
 * @author leyou
 */
//默认的 slot chain 构造器
public class DefaultSlotChainBuilder implements SlotChainBuilder {

    @Override
    public ProcessorSlotChain build() {
        //DefaultProcessorSlotChain 默认的slot链处理器
        ProcessorSlotChain chain = new DefaultProcessorSlotChain();
        //NodeSelectorSlot 收集 资源 的路径,将这些资源的调用路径,以树状结构存储起来
        chain.addLast(new NodeSelectorSlot());
        //ClusterBuilderSlot 存储 资源 集群的统计信息以及调用者信息
        chain.addLast(new ClusterBuilderSlot());
        chain.addLast(new LogSlot());
        //StatisticSlot 记录、统计不同维度的 runtime 指标监控信息
        chain.addLast(new StatisticSlot());
        //AuthoritySlot 黑白名单
        chain.addLast(new AuthoritySlot());
        //SystemSlot 系统的状态,来控制总的入口流量
        chain.addLast(new SystemSlot());
        //FlowSlot 根据预设的限流规则以及前面 slot 统计的状态,来进行流量控制
        chain.addLast(new FlowSlot());
        //DegradeSlot 通过统计信息以及预设的规则,熔断降级
        chain.addLast(new DegradeSlot());

        return chain;
    }

}
