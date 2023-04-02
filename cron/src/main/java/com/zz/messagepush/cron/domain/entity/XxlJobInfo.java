package com.zz.messagepush.cron.domain.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.internal.build.AllowPrintStacktrace;

/**
 * 
 * @author DELL
 * @TableName xxl_job_info
 */
//@Table(name="xxl_job_info")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Builder
@Entity
public class XxlJobInfo implements Serializable {
    /**
     * 
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 执行器主键ID
     */
//    @Column(name = "job_group")
    private Integer jobGroup;

    /**
     * 
     */
//    @Column(name = "job_desc")
    private String jobDesc;

    /**
     * 
     */
//    @Column(name = "add_time")
    private Date addTime;

    /**
     * 
     */
//    @Column(name = "update_time")
    private Date updateTime;

    /**
     * 作者
     */
    private String author;

    /**
     * 报警邮件
     */
//    @Column(name = "alarm_email")
    private String alarmEmail;

    /**
     * 调度类型
     */
//    @Column(name = "schedule_type")
    private String scheduleType;

    /**
     * 调度配置，值含义取决于调度类型
     */
//    @Column(name = "schedule_conf")
    private String scheduleConf;

    /**
     * 调度过期策略
     */
//    @Column(name = "misfire_strategy")
    private String misfireStrategy;

    /**
     * 执行器路由策略
     */
//    @Column(name = "executor_route_strategy")
    private String executorRouteStrategy;

    /**
     * 执行器任务handler
     */
//    @Column(name = "executor_handler")
    private String executorHandler;

    /**
     * 执行器任务参数
     */
//    @Column(name = "executor_param")
    private String executorParam;

    /**
     * 阻塞处理策略
     */
//    @Column(name = "executor_block_strategy")
    private String executorBlockStrategy;

    /**
     * 任务执行超时时间，单位秒
     */
//    @Column(name = "executor_timeout")
    private Integer executorTimeout;

    /**
     * 失败重试次数
     */
//    @Column(name = "executor_fail_retry_count")
    private Integer executorFailRetryCount;

    /**
     * GLUE类型
     */
//    @Column(name = "glue_type")
    private String glueType;

    /**
     * GLUE源代码
     */
//    @Column(name = "glue_source")
    private String glueSource;

    /**
     * GLUE备注
     */
//    @Column(name = "glue_remark")
    private String glueRemark;

    /**
     * GLUE更新时间
     */
//    @Column(name = "glue_updatetime")
    private Date glueUpdatetime;

    /**
     * 子任务ID，多个逗号分隔
     */
//    @Column(name = "child_jobid")
    private String childJobid;

    /**
     * 调度状态：0-停止，1-运行
     */
//    @Column(name = "trigger_status")
    private Integer triggerStatus;

    /**
     * 上次调度时间
     */
//    @Column(name = "trigger_last_time")
    private Long triggerLastTime;

    /**
     * 下次调度时间
     */
//    @Column(name = "trigger_next_time")
    private Long triggerNextTime;

    private static final long serialVersionUID = 1L;
}