package com.wsx.ejbase.entity.po;

import lombok.Data;

@Data
public class JobInfo {

    private Integer id;

    private String jobName;

    private String param;

    private String cron;

    private String description;

    private Integer taskType;

    private String nodePath;

}
