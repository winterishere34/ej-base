package com.wsx.ejbase.common;

import java.util.Collection;
import java.util.List;

import javax.sql.DataSource;

import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.JobTypeConfiguration;
import com.dangdang.ddframe.job.config.dataflow.DataflowJobConfiguration;
import com.dangdang.ddframe.job.config.script.ScriptJobConfiguration;
import com.dangdang.ddframe.job.config.simple.SimpleJobConfiguration;
import com.dangdang.ddframe.job.event.JobEventConfiguration;
import com.dangdang.ddframe.job.event.rdb.JobEventRdbConfiguration;
import com.dangdang.ddframe.job.lite.api.JobScheduler;
import com.dangdang.ddframe.job.lite.api.listener.ElasticJobListener;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import com.dangdang.ddframe.job.lite.lifecycle.api.JobAPIFactory;
import com.dangdang.ddframe.job.lite.lifecycle.api.JobOperateAPI;
import com.dangdang.ddframe.job.lite.lifecycle.api.JobSettingsAPI;
import com.dangdang.ddframe.job.lite.lifecycle.api.JobStatisticsAPI;
import com.dangdang.ddframe.job.lite.lifecycle.api.ServerStatisticsAPI;
import com.dangdang.ddframe.job.lite.lifecycle.domain.JobBriefInfo;
import com.dangdang.ddframe.job.lite.lifecycle.domain.ServerBriefInfo;
import com.dangdang.ddframe.job.lite.lifecycle.internal.reg.RegistryCenterFactory;
import com.dangdang.ddframe.job.reg.base.CoordinatorRegistryCenter;

import com.google.common.base.Optional;
import com.wsx.ejbase.entity.po.JobInfo;
import com.wsx.ejbase.entity.to.RegistryCenterConfiguration;
import com.wsx.ejbase.job.DynamicDataFlowJob;
import com.wsx.ejbase.job.DynamicSimpleJob;

public final class EJBaseFactory {

    /**
     * 获取ZK配置
     * @param serverList
     * @param namespace
     * @param digest
     * @return ZK 配置信息
     */
    public static RegistryCenterConfiguration getRegCenterConfig(String serverList, String namespace, String digest) {
        RegistryCenterConfiguration regCenterConfig = new RegistryCenterConfiguration();
        regCenterConfig.setName(namespace); //Use namespace as the configuration name, actually it is not used.
        regCenterConfig.setNamespace(namespace);
        regCenterConfig.setZkAddressList(serverList);
        regCenterConfig.setDigest(digest);


        return regCenterConfig;
    };

    /**
     * 获取ZK 注册中心
     * @return
     */
    public static CoordinatorRegistryCenter getCoordinatorRegistryCenter(RegistryCenterConfiguration regCenterConfig) {
        return RegistryCenterFactory.createCoordinatorRegistryCenter(regCenterConfig.getZkAddressList(),
                regCenterConfig.getNamespace(),
                Optional.fromNullable(regCenterConfig.getDigest()));
    }

    /**
     * 获取jobName list
     * @param regCenter
     * @return
     */
    public static List<String> getJobNames(CoordinatorRegistryCenter regCenter){
        return regCenter.getChildrenKeys("/");
    }

    /**
     *  定义日志数据库事件溯源配置
     * @param dataSource
     * @return
     */
    public static JobEventConfiguration getJobEventConfiguration(DataSource dataSource) {
        return new JobEventRdbConfiguration(dataSource);
    }

    /**
     * 获取job配置
     * @param jobInfo
     * @return
     */
    public static JobCoreConfiguration getJobCoreConfiguration(JobInfo jobInfo) {
        JobCoreConfiguration coreConfig = JobCoreConfiguration.newBuilder(jobInfo.getJobName(), jobInfo.getCron(), 6)
                .description(jobInfo.getDescription())
                .failover(true)
                .jobParameter(jobInfo.getParam())
                .misfire(true)
                .build();

        return coreConfig;
    }

    /**
     * 获取job type配置
     * @param jobInfo
     * @param coreConfig
     * @return
     */
    public static JobTypeConfiguration getJobTypeConfiguration(JobInfo jobInfo, JobCoreConfiguration coreConfig){
        JobTypeConfiguration jobTypeConfiguration = null;
        if (jobInfo.getTaskType() == 0) {
            jobTypeConfiguration = new SimpleJobConfiguration(coreConfig, DynamicSimpleJob.class.getCanonicalName());
        }else if(jobInfo.getTaskType() == 1){
            jobTypeConfiguration = new DataflowJobConfiguration(coreConfig, DynamicDataFlowJob.class.getCanonicalName(),false);
        }else if(jobInfo.getTaskType() == 2){
            jobTypeConfiguration = new ScriptJobConfiguration(coreConfig, jobInfo.getParam());
        }
        return jobTypeConfiguration;
    }

    /**
     * 获取lite job的配置
     * @param jobTypeConfiguration
     * @return
     */
    public static LiteJobConfiguration getLiteJobConfiguration(JobTypeConfiguration jobTypeConfiguration){
        LiteJobConfiguration liteJobConfiguration = LiteJobConfiguration.newBuilder(jobTypeConfiguration).overwrite(true).build();
        return liteJobConfiguration;
    }

    /**
     * 获取job scheduler
     * @param regCenter
     * @param liteJobConfig
     * @param elasticJobListeners
     * @return
     */
    public static JobScheduler getJobScheduler(CoordinatorRegistryCenter regCenter, LiteJobConfiguration liteJobConfig, ElasticJobListener... elasticJobListeners){
        return new JobScheduler(regCenter, liteJobConfig, elasticJobListeners);
    }

    /**
     * 获取JobOperateAPI
     * @param regCenterConfig
     * @return
     */
    public static JobOperateAPI getJobOperatorAPI(RegistryCenterConfiguration regCenterConfig) {
        return JobAPIFactory.createJobOperateAPI(regCenterConfig.getZkAddressList(), regCenterConfig.getNamespace(), Optional.fromNullable(regCenterConfig.getDigest()));
    }

    /**
     * 获取JobSettingsAPI
     * @param regCenterConfig
     * @return
     */
    public static JobSettingsAPI getJobSettingsAPI(RegistryCenterConfiguration regCenterConfig) {
        return JobAPIFactory.createJobSettingsAPI(regCenterConfig.getZkAddressList(), regCenterConfig.getNamespace(), Optional.fromNullable(regCenterConfig.getDigest()));
    }

    /**
     * 获取getJobStatisticsAPI
     * @param regCenterConfig
     * @return
     */
    public static JobStatisticsAPI getJobStatisticsAPI(RegistryCenterConfiguration regCenterConfig) {
        return JobAPIFactory.createJobStatisticsAPI(regCenterConfig.getZkAddressList(), regCenterConfig.getNamespace(), Optional.fromNullable(regCenterConfig.getDigest()));
    }

    /**
     * 根据ip获取job列表
     * @param jobStatisticsAPI
     * @param serverIp
     * @return
     */
    public static Collection<JobBriefInfo> getJobs(JobStatisticsAPI jobStatisticsAPI, String serverIp) {
        return jobStatisticsAPI.getJobsBriefInfo(serverIp);
    }

    /**
     * getServerStatisticsAPI
     * @param regCenterConfig
     * @return
     */
    public static ServerStatisticsAPI getServerStatisticsAPI(RegistryCenterConfiguration regCenterConfig) {
        return JobAPIFactory.createServerStatisticsAPI(regCenterConfig.getZkAddressList(), regCenterConfig.getNamespace(), Optional.fromNullable(regCenterConfig.getDigest()));
    }

    /**
     * getAllServersBriefInfo
     * @param serverStatisticsAPI
     * @return
     */
    public static Collection<ServerBriefInfo> getAllServersBriefInfo(ServerStatisticsAPI serverStatisticsAPI) {
        return serverStatisticsAPI.getAllServersBriefInfo();
    }
}
