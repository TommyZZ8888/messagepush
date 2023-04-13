package com.zz.messagepush.cron.utils;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.text.csv.*;
import com.alibaba.nacos.shaded.com.google.common.base.Throwables;
import com.zz.messagepush.cron.csv.CountFileRowHandler;
import com.zz.messagepush.cron.domain.vo.CrowdInfoVO;
import lombok.extern.slf4j.Slf4j;

import java.io.FileNotFoundException;
import java.io.FileReader;
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
     * csv文件存储接收者的列名
     */
    public static final String RECEIVER_KEY = "userId";

    /**
     * 读取csv信息
     * 第一行默认为头信息，第一列默认为接收者id
     *
     * @param path
     * @return
     */
    @Deprecated
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
                result.add(CrowdInfoVO.builder().receiver(row.get(0)).params(params).build());
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("TaskHandler#getCsvRowList fail!{}", Throwables.getStackTraceAsString(e));
        }
        return result;
    }

    /**
     * 读取csv文件，每读取一行都会调用CsvRowHandler对应的方法
     *
     * @param path
     * @param csvRowHandler
     */
    public static void getCsvRow(String path, CsvRowHandler csvRowHandler) {
        try {
            CsvReader reader = CsvUtil.getReader(new FileReader(path), new CsvReadConfig().setContainsHeader(true));
            reader.read(csvRowHandler);
        } catch (Exception e) {
            log.error("ReadFileUtil#getCsvRow fail:{}", Throwables.getStackTraceAsString(e));
        }
    }


    /**
     * 从文件的每一行数据获取param信息
     *
     * @param fieldMap
     * @return
     */
    public static Map<String, String> getParamFromLine(Map<String, String> fieldMap) {
        Map<String, String> map = new HashMap<>();
        for (Map.Entry<String, String> entry : fieldMap.entrySet()) {
            if (!RECEIVER_KEY.equals(entry.getKey())) {
                map.put(entry.getKey(), entry.getValue());
            }
        }
        return map;
    }


    /**
     * 读取csv文件，获取文件中的行数
     *
     * @param path
     * @param countFileRowHandler
     * @return
     */
    public static Long countCsvRow(String path, CountFileRowHandler countFileRowHandler) {
        try {
            CsvReader reader = CsvUtil.getReader(new FileReader(path), new CsvReadConfig().setContainsHeader(true));

            reader.read(countFileRowHandler);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return countFileRowHandler.getCsvRowCount();
    }
}
