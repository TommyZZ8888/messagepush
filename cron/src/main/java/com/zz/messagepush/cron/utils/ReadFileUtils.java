package com.zz.messagepush.cron.utils;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.text.csv.CsvData;
import cn.hutool.core.text.csv.CsvRow;
import cn.hutool.core.text.csv.CsvUtil;
import com.alibaba.nacos.shaded.com.google.common.base.Throwables;
import com.zz.messagepush.cron.domain.vo.CrowdInfoVO;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description 读取人群文件工具类
 * @Author 张卫刚
 * @Date Created on 2023/4/4
 */
@Slf4j
public class ReadFileUtils {

    /**
     * 读取csv信息
     * 第一行默认为头信息，第一列默认为接收者id
     * @param path
     * @return
     */
    public static List<CrowdInfoVO> getCsvRowList(String path) {
        List<CrowdInfoVO> result = new ArrayList<>();

        try {
            CsvData csvData = CsvUtil.getReader().read(FileUtil.file(path));
            if (csvData == null || csvData.getRow(0) == null || csvData.getRow(1) == null) {
                log.error("no data");
            }
            CsvRow header = csvData.getRow(0);
            //第一行默认为头信息，第一列默认为接收者id
            for (int i = 1; i < csvData.getRowCount(); i++) {
                CsvRow row = csvData.getRow(i);
                Map<String, String> params = new HashMap<>();
                for (int j = 1; j < header.size(); j++) {
                    params.put(header.get(j), row.get(j));
                }
                result.add(CrowdInfoVO.builder().id(row.get(0)).params(params).build());
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("TaskHandler#getCsvRowList fail!{}", Throwables.getStackTraceAsString(e));
        }
        return result;
    }
}
