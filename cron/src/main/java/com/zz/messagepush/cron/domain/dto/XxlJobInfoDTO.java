package com.zz.messagepush.cron.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @author DELL
 * @TableName xxl_job_info
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Builder
public class XxlJobInfoDTO {


    private Integer id;


    private Integer jobGroup;


    private String jobDesc;


    private Date addTime;


    private Date updateTime;


    private String author;


    private String alarmEmail;


    private String scheduleType;


    private String scheduleConf;


    private String misfireStrategy;


    private String executorRouteStrategy;


    private String executorHandler;


    private String executorParam;


    private String executorBlockStrategy;


    private Integer executorTimeout;


    private Integer executorFailRetryCount;


    private String glueType;


    private String glueSource;


    private String glueRemark;


    private Date glueUpdateTime;


    private String childJobId;


    private Integer triggerStatus;


    private Long triggerLastTime;


    private Long triggerNextTime;

}