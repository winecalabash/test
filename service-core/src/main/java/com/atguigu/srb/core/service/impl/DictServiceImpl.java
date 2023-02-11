package com.atguigu.srb.core.service.impl;

import com.alibaba.excel.EasyExcel;
import com.atguigu.srb.core.listener.ExcelDictDTOListener;
import com.atguigu.srb.core.pojo.dto.ExcelDictDTO;
import com.atguigu.srb.core.pojo.entity.Dict;
import com.atguigu.srb.core.mapper.DictMapper;
import com.atguigu.srb.core.service.DictService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.ibatis.reflection.ExceptionUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 数据字典 服务实现类
 * </p>
 *
 * @author Helen
 * @since 2022-11-15
 */
@Slf4j
@Service
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {

    @Resource
    private RedisTemplate redisTemplate;

    @Transactional(rollbackFor = {Exception.class})
    @Override
    public void importData(InputStream inputStream) {
        EasyExcel.read(inputStream, ExcelDictDTO.class,new ExcelDictDTOListener(baseMapper)).sheet().doRead();
        log.info("Excel导入成功");

    }

    @Override
    public List<ExcelDictDTO> listDictData() {

        List<Dict> dictList = baseMapper.selectList(null);
        //创建ExcelDictDTO列表，将Dict列表转换成ExcelDictDTO列表
        ArrayList<ExcelDictDTO> excelDictDTOList = new ArrayList<>(dictList.size());
        dictList.forEach(dict -> {

            ExcelDictDTO excelDictDTO = new ExcelDictDTO();
            BeanUtils.copyProperties(dict, excelDictDTO);
            excelDictDTOList.add(excelDictDTO);
        });
        return excelDictDTOList;
    }

    @Override
    public List<Dict> listByParentId(Long parentId) {

        try {
            //首先查询redis中是否存在数据列表
            log.info("从redis中获取数据列表");
            List<Dict> dictList = (List<Dict>)redisTemplate.opsForValue().get("srb:core:dictList" + parentId);
            //如果存在则从redis中直接返回数据列表
            if (dictList != null ) {
                return dictList;
            }
        } catch (Exception e) {
            log.error("redis服务器异常:"+ ExceptionUtils.getStackTrace(e));
        }

        //如果不存在则在数据库中查询数据源
        LambdaQueryWrapper<Dict> dictQueryWrapper = new LambdaQueryWrapper<>();
        dictQueryWrapper.eq(Dict::getParentId,parentId);
        List<Dict> list = list(dictQueryWrapper);
        list.forEach(dict -> {
            //判断当前节点是否有子节点，找到当前的dict下级有没有子节点
            dict.setHasChildren(hasChildren(dict.getId()));
        });

        //将数据存入redis
        try {
            log.info("数据存入redis");
            redisTemplate.opsForValue().set("srb:core:dictList" + parentId,list,5,TimeUnit.MINUTES);
        } catch (Exception e) {
            log.error("redis服务器异常:"+ ExceptionUtils.getStackTrace(e));
        }
        //返回数据列表
        return list;

    }

    private boolean hasChildren(Long id){
        LambdaQueryWrapper<Dict> dictQueryWrapper = new LambdaQueryWrapper<>();
        dictQueryWrapper.eq(Dict::getParentId,id);
        Integer count = count(dictQueryWrapper);
        if (count > 0) {
            return true;
        }
        return false;

    }
}
