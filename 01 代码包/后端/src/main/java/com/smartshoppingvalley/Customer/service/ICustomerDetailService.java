package com.smartshoppingvalley.Customer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.smartshoppingvalley.Customer.entity.CustomerDetail;

/**
 * <p>
 * 服务类
 * </p>
 */
public interface ICustomerDetailService extends IService<CustomerDetail> {
    /*
    admin调用，显示用户详情
     */
    CustomerDetail getCustomerDetail(int customerId);

    void addCustomerDetail(CustomerDetail customerDetail);
}
