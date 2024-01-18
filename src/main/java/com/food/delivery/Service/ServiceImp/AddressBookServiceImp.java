package com.food.delivery.Service.ServiceImp;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.food.delivery.Entity.AddressBook;
import com.food.delivery.Mapper.AddressBookMapper;
import com.food.delivery.Service.AddresBookService;
import org.springframework.stereotype.Service;

@Service
public class AddressBookServiceImp extends ServiceImpl<AddressBookMapper, AddressBook>
    implements AddresBookService {}
