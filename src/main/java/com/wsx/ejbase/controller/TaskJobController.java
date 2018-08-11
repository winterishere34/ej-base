package com.wsx.ejbase.controller;

import com.wsx.ejbase.entity.po.JobInfo;
import com.wsx.ejbase.service.TaskService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "api/TaskJobs")
public class TaskJobController {

    @Autowired
    private TaskService taskService;

    @PostMapping("/register")
    public Object TaskRegister(@RequestBody JobInfo jobInfo) {
        return taskService.RegisterTask(jobInfo);
    }

    @PostMapping("/disable")
    public Object DisableTask(@RequestBody String data) {
        String jobName = JSONObject.fromObject(data).getString("jobName");
        return taskService.DisableTask(jobName, null);
    }

    @PostMapping("/enable")
    public Object EnableTask(@RequestBody String data) {
        String jobName = JSONObject.fromObject(data).getString("jobName");
        return taskService.EnableTask(jobName, null);
    }

    @PostMapping("/trigger")
    public Object TriggerTask(@RequestBody String data) {
        String jobName = JSONObject.fromObject(data).getString("jobName");
        return taskService.TriggerTask(jobName, null);
    }

}
