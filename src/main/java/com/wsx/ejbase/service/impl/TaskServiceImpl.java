package com.wsx.ejbase.service.impl;

import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.JobTypeConfiguration;
import com.dangdang.ddframe.job.lite.api.JobScheduler;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import com.google.common.base.Optional;
import com.wsx.ejbase.common.EJBaseFactory;
import com.wsx.ejbase.entity.po.JobInfo;
import com.wsx.ejbase.service.TaskService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("taskService")
@Transactional
public class TaskServiceImpl implements TaskService {

    /**
     * 从properties读取ZK server list
     */
    @Value("${server.port}")
    public String port;

    /**
     * 从properties读取ZK server list
     */
    @Value("${regCenter.serverList}")
    public String serverList;

    /**
     * 从properties读取ZK name space, 这是为多项目共同使用
     * 一个ZK，目前阶段先使用一个name space
     */
    @Value("${regCenter.namespace}")
    public String namespace;

    /**
     * 从properties读取ZK digest, 连接Zookeeper的权限令牌
     * 缺省为不需要权限验证
     */
    @Value("${regCenter.digest}")
    public String digest;

    @Override
    public Object RegisterTask(JobInfo jobInfo) {
        JobCoreConfiguration coreConfig = EJBaseFactory.getJobCoreConfiguration(jobInfo);
        JobTypeConfiguration jobTypeConfiguration = EJBaseFactory.getJobTypeConfiguration(jobInfo, coreConfig);
        LiteJobConfiguration liteJobConfiguration = EJBaseFactory.getLiteJobConfiguration(jobTypeConfiguration);
        JobScheduler jobScheduler = EJBaseFactory.getJobScheduler(EJBaseFactory.getCoordinatorRegistryCenter(EJBaseFactory.getRegCenterConfig(serverList, namespace, digest)), liteJobConfiguration);
        jobScheduler.init();
        return null;
    }

    @Override
    public Object DisableTask(String jobName, String serverIp) {
        EJBaseFactory.getJobOperatorAPI(EJBaseFactory.getRegCenterConfig(serverList, namespace, digest)).disable(Optional.fromNullable(jobName), Optional.fromNullable(serverIp));
        return null;
    }

    @Override
    public Object EnableTask(String jobName, String serverIp) {
        EJBaseFactory.getJobOperatorAPI(EJBaseFactory.getRegCenterConfig(serverList, namespace, digest)).enable(Optional.fromNullable(jobName), Optional.fromNullable(serverIp));
        return null;
    }

    @Override
    public Object TriggerTask(String jobName, String serverIp) {
        EJBaseFactory.getJobOperatorAPI(EJBaseFactory.getRegCenterConfig(serverList, namespace, digest)).trigger(Optional.fromNullable(jobName), Optional.fromNullable(serverIp));
        return null;
    }
}
