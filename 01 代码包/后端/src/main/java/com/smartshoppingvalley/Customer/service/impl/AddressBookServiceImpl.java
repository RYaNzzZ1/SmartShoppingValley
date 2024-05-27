package com.smartshoppingvalley.Customer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.smartshoppingvalley.Customer.entity.AddressBook;
import com.smartshoppingvalley.Customer.mapper.AddressBookMapper;
import com.smartshoppingvalley.Customer.service.IAddressBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 地址管理 服务实现类
 * </p>
 */
@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements IAddressBookService {

    @Autowired
    private AddressBookMapper addressBookMapper;

    @Override
    public List<AddressBook> getAddressesById(int id) {
        QueryWrapper<AddressBook> wrapper = new QueryWrapper<>();
        wrapper.eq("customer_id", id);
        wrapper.eq("is_deleted", 0);
        return addressBookMapper.selectList(wrapper);
    }

    @Override
    public int addAddr(AddressBook addr) {
        //若此地址是该用户添加的第一条地址，则设为默认
        QueryWrapper<AddressBook> wrapper = new QueryWrapper<>();
        wrapper.eq("customer_id", addr.getCustomerId());
        if (addressBookMapper.selectList(wrapper).isEmpty())
            addr.setIsDefault(1);
        return addressBookMapper.insert(addr);
    }

    @Override
    public int deleteAddrById(int id) {
        UpdateWrapper<AddressBook> wrapper = new UpdateWrapper<>();
        wrapper.set("is_deleted", 1);
        wrapper.eq("id", id);
        return addressBookMapper.update(null, wrapper);
    }

    @Override
    public int editAddress(AddressBook addr) {
        QueryWrapper<AddressBook> wrapper = new QueryWrapper<>();
        wrapper.eq("id", addr.getId());
        return addressBookMapper.update(addr, wrapper);
    }

    @Override
    public void setDefault(int id) {
        AddressBook addr = new AddressBook();
        addr.setIsDefault(0);
        //先将该用户所有address设为非默认
        Long customerId = addressBookMapper.selectById(id).getCustomerId();
        QueryWrapper<AddressBook> wrapper1 = new QueryWrapper<>();
        wrapper1.eq("customer_id", customerId);
        wrapper1.eq("is_deleted", 0);
        addressBookMapper.update(addr, wrapper1);
        //再将id地址设为默认
        wrapper1.eq("id", id);
        addr.setIsDefault(1);
        addressBookMapper.update(addr, wrapper1);
    }

}
