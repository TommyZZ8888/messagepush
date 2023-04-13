package com.zz.messagepush.cron.csv

import cn.hutool.core.text.csv.CsvRow
import cn.hutool.core.text.csv.CsvRowHandler
import lombok.Data

/**
 * @Description
 * @Author 张卫刚
 * @Date Created on 2023/4/13
 */

@Data
public class CountFileRowHandler implements CsvRowHandler {

    private Long rowSize;

    @Override
    public void handle(CsvRow csvRow) {
        rowSize++;
    }

    public Long getCsvRowCount() {
        return rowSize;
    }

}
