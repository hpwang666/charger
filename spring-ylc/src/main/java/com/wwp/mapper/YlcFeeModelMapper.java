package com.wwp.mapper;

import com.wwp.entity.YlcFeeModel;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface YlcFeeModelMapper {
    YlcFeeModel getFeeModelById(String id);
    void updateFeeModel(YlcFeeModel ylcFeeModel);
    void add(YlcFeeModel ylcFeeModel);
}
