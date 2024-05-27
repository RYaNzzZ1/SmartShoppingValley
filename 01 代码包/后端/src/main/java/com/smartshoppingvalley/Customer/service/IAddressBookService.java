package com.smartshoppingvalley.Customer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.smartshoppingvalley.Customer.entity.AddressBook;

import java.util.List;

/**
 * <p>
 * 地址管理 服务类
 * </p>
 */
public interface IAddressBookService extends IService<AddressBook> {
    List<AddressBook> getAddressesById(int id);

    int addAddr(AddressBook addr);

    int deleteAddrById(int id);

    int editAddress(AddressBook addr);

    void setDefault(int id);

}
