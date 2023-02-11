package com.atguigu.srb.core.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.atguigu.srb.core.mapper.DictMapper;
import com.atguigu.srb.core.pojo.dto.ExcelDictDTO;
import com.atguigu.srb.core.service.impl.DictServiceImpl;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
//@AllArgsConstructor //全参
@NoArgsConstructor //无参
public class ExcelDictDTOListener extends AnalysisEventListener<ExcelDictDTO> {

    private List<ExcelDictDTO> list = new ArrayList<>();
    private static final int BATCH_SIZE = 5;
    private DictMapper dictMapper;
    private DictServiceImpl dictService;

    public ExcelDictDTOListener(DictMapper dictMapper) {
        this.dictMapper = dictMapper;
    }

    public ExcelDictDTOListener(DictServiceImpl dictService) {
        this.dictService = dictService;
    }

    @Override
    public void invoke(ExcelDictDTO excelDictDTO, AnalysisContext analysisContext) {
        log.info("解析到一条记录：{}",excelDictDTO);

        //将数据存入数据列表
        list.add(excelDictDTO);
        if ((list.size() >= BATCH_SIZE)){
            saveData(list);
            list.clear();
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        saveData(list);
        log.info("解析所有数据完成！");
    }

    private void saveData(List list) {
        log.info("{} 条数据被存入数据库。。。。。",list.size());

        if (!list.isEmpty()) {
            dictMapper.insertBatch(list);
//            dictService.saveBatch(list);
            log.info("{} 条数据被存入数据库成功", list.size());
        }
    }
}
