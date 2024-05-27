package com.smartshoppingvalley.Customer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.smartshoppingvalley.Customer.entity.Customer;
import com.smartshoppingvalley.Customer.mapper.CustomerMapper;
import com.smartshoppingvalley.Customer.service.ICustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author wyh
 * @since 2023-08-28
 */
@Service
public class CustomerServiceImpl extends ServiceImpl<CustomerMapper, Customer> implements ICustomerService {
    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private CustomerMapper customerMapper;


    @Override
    public void sendVerificationCode(String toEmail, int verificationCode) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("wyh3.14@foxmail.com");
        message.setTo(toEmail);
        message.setSubject("乐淘电子商务系统|验证码");
        message.setText("【乐淘电子商务系统】尊敬的用户：" + toEmail + "，您好！\n" +
                "欢迎使用乐淘电子商务系统，您的验证码是： " + verificationCode +
                "，有效期为60分钟，请勿泄露和转发。如非本人操作，请忽略此邮件。");

        javaMailSender.send(message);
    }

    @Override
    public int saveCustomer(Customer customer) {
        return customerMapper.insert(customer);
    }

    /**
     * @param username
     * @param password
     * @return -1  用户被禁用
     * -2  密码错误
     * 1   登陆成功
     */
    @Override
    public int isPass(String username, String password) {
        QueryWrapper<Customer> wrapper = new QueryWrapper<>();
        wrapper.eq("username", username);
        Customer customer = customerMapper.selectOne(wrapper);
        if (customer.getStatus() != 1)
            return -1;
        if (!customer.getPassword().equals(password))
            return -2;
        return 1;
    }

    @Override
    public IPage<Customer> getCustomerAtPage(int p, int size) {
        Page<Customer> page = new Page<>(p, size);
        return customerMapper.selectPage(page, null);
    }

    @Override
    public IPage<Customer> searchCustomerAtPage(int p, int size, String name) {
        QueryWrapper<Customer> wrapper = new QueryWrapper<>();
        wrapper.eq("username", name);
        Page<Customer> page = new Page<>(p, size);
        return customerMapper.selectPage(page, wrapper);
    }

    @Override
    public int changeCustomerStatus(int id) {
        QueryWrapper<Customer> wrapper = new QueryWrapper<>();
        wrapper.eq("id", id);
        Customer cusSearch = customerMapper.selectOne(wrapper);
        Customer customer = new Customer();
        if (cusSearch.getStatus() == 1)
            customer.setStatus(2);
        else customer.setStatus(1);
        return customerMapper.update(customer, wrapper);
    }

    /**
     * @param username
     * @return -1 用户不存在
     * id 用户id
     */
    @Override
    public Long getIdByUsername(String username) {
        QueryWrapper<Customer> wrapper = new QueryWrapper<>();
        wrapper.eq("username", username);
        Customer customer = customerMapper.selectOne(wrapper);
        if (customer == null)
            return -1L;
        return customer.getId();
    }
}
