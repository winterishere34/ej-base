package com.wsx.ejbase.service;

import com.wsx.ejbase.entity.po.JobInfo;

public interface TaskService {

    Object RegisterTask(JobInfo jobInfo);

    Object DisableTask(String jobName, String serverIp);

    Object EnableTask(String jobName, String serverIp);

    Object TriggerTask(String jobName, String serverIp);
}
