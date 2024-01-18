package com.food.delivery.Controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.food.delivery.Entity.AddressBook;
import com.food.delivery.Helper.BaseContext;
import com.food.delivery.Helper.Result;
import com.food.delivery.Service.AddresBookService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/addressBook")
public class AddressBookController {

  @Autowired private AddresBookService addresBookService;

  @PostMapping
  public Result<String> save(@RequestBody AddressBook addressBook) {
    // log.info(addressBook.toString());
    addressBook.setUserId(BaseContext.getCurrentId());
    addresBookService.save(addressBook);
    return Result.success("adressBook saved successfully");
  }

  @GetMapping("/list")
  public Result<List<AddressBook>> list(AddressBook addressBook) {

    Long currentUserId = BaseContext.getCurrentId();
    addressBook.setUserId(currentUserId);

    LambdaQueryWrapper<AddressBook> lambdaQueryWrapper = new LambdaQueryWrapper<>();
    lambdaQueryWrapper
        .eq(currentUserId != null, AddressBook::getUserId, currentUserId)
        .orderByDesc(AddressBook::getIsDefault, AddressBook::getUpdateTime);

    // select * from addressBook where userId = currentUserId order by updateTime DESC;
    List<AddressBook> list = addresBookService.list(lambdaQueryWrapper);
    ;

    return Result.success(list);
  }

  @GetMapping("/{addressBookId}")
  public Result<AddressBook> get(@PathVariable Long addressBookId) {
    AddressBook addressBook = addresBookService.getById(addressBookId);
    if (addressBook != null) return Result.success(addressBook);
    return Result.error("addressBook can not be found");
  }

  @Transactional
  @PutMapping("/default")
  public Result<String> setDefault(@RequestBody AddressBook addressBook) {

    // get currUserId
    Long currentUserId = BaseContext.getCurrentId();

    // update all addressbooks for the user default = 0 (no default)
    LambdaUpdateWrapper<AddressBook> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
    lambdaUpdateWrapper.eq(currentUserId != null, AddressBook::getUserId, currentUserId);
    lambdaUpdateWrapper.set(AddressBook::getIsDefault, 0);

    // update address_book set is_default = 0 where user_id = ?
    addresBookService.update(lambdaUpdateWrapper);

    // update the current addressbook default = 1 (set as default address)
    addressBook.setIsDefault(1);
    addresBookService.updateById(addressBook);

    return Result.success("defaultAddress set successfully");
  }

  @PutMapping
  public Result<String> update(@RequestBody AddressBook addressBook) {
    if (addressBook == null) return Result.error("Requesst payload null error");
    AddressBook addressBook2 = addresBookService.getById(addressBook.getId());
    if (addressBook2 == null) return Result.error("addressBook not found in database");
    addresBookService.updateById(addressBook);

    return Result.success("addressBook updated successfully");
  }

  @DeleteMapping
  public Result<String> delete(@RequestParam Long ids) {
    if (ids == null) return Result.error("id not provided");
    LambdaQueryWrapper<AddressBook> lambdaQueryWrapper = new LambdaQueryWrapper<>();
    lambdaQueryWrapper
        .eq(AddressBook::getId, ids)
        .eq(
            AddressBook::getUserId,
            BaseContext.getCurrentId()); // make sure the user and the address aligns
    addresBookService.remove(lambdaQueryWrapper);
    return Result.success("Addressbook deleted successfully");
  }
}
