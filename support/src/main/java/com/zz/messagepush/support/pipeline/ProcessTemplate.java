package com.zz.messagepush.support.pipeline;

import java.util.List;

/**
 * @Description 业务执行的模板
 * @Author 张卫刚
 * @Date Created on 2023/3/14
 */
public class ProcessTemplate {

    private List<BusinessProcess> processList;

    public void setProcessList(List<BusinessProcess> list){
        this.processList = list;
    }

    public List<BusinessProcess> getProcessList(){
        return processList;
    }
}
