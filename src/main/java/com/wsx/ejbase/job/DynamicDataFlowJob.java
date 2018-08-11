package com.wsx.ejbase.job;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.dataflow.DataflowJob;
import com.wsx.ejbase.entity.po.JobInfo;

import java.util.ArrayList;
import java.util.List;

public class DynamicDataFlowJob implements DataflowJob<JobInfo> {

    @Override
    public List<JobInfo> fetchData(ShardingContext shardingContext) {
        List<JobInfo> jobInfo = new ArrayList<JobInfo>();
        return jobInfo;
    }

    @Override
    public void processData(ShardingContext shardingContext, List<JobInfo> list) {

    }

}
