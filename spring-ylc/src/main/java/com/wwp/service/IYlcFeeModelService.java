package com.wwp.service;

import com.wwp.entity.YlcFeeModel;

public interface IYlcFeeModelService {
    YlcFeeModel getFeeModel(String id);

    void updateFeeModel(YlcFeeModel ylcFeeModel);

    void addFeeModel(YlcFeeModel ylcFeeModel);
}
