package com.smartshoppingvalley.Customer.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.smartshoppingvalley.Customer.entity.Customer;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author wyh
 * @since 2023-08-28
 */
public interface ICustomerService extends IService<Customer> {
    //发送验证码
    void sendVerificationCode(String toEmail, int verificationCode);

    //保存用户
    int saveCustomer(Customer customer);

    //登录验证密码是否正确
    int isPass(String username, String password);

    /*
    分页显示全部客户
     */
    IPage<Customer> getCustomerAtPage(int p, int size);

    /*
    搜索用户并分页显示
     */
    IPage<Customer> searchCustomerAtPage(int p, int size, String name);

    /*
    修改用户的status
     */
    int changeCustomerStatus(int id);

    Long getIdByUsername(String username);
}
