package com.food.delivery.Controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.food.delivery.Entity.ShoppingCart;
import com.food.delivery.Helper.BaseContext;
import com.food.delivery.Helper.Result;
import com.food.delivery.Service.ShoppingCartService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/shoppingCart")
public class ShoppingCartController {

  @Autowired private ShoppingCartService shoppingCartService;

  @GetMapping("list")
  public Result<List<ShoppingCart>> list() {
    Long currentId = BaseContext.getCurrentId();
    LambdaQueryWrapper<ShoppingCart> lambdaQueryWrapper = new LambdaQueryWrapper<>();
    lambdaQueryWrapper
        .eq(currentId != null, ShoppingCart::getUserId, currentId)
        .orderByDesc(ShoppingCart::getCreateTime);
    List<ShoppingCart> list = shoppingCartService.list(lambdaQueryWrapper);
    ;
    return Result.success(list);
  }

  @PostMapping("/add")
  public Result<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart) {

    Long userId = BaseContext.getCurrentId();
    shoppingCart.setUserId(userId);

    LambdaQueryWrapper<ShoppingCart> lambdaQueryWrapper = new LambdaQueryWrapper<>();
    lambdaQueryWrapper.eq(userId != null, ShoppingCart::getUserId, userId);

    // Write lambdaQuery based on shoppingCart type (setId OR mealId)
    if (shoppingCart.getDishId() != null) {
      // dish type
      lambdaQueryWrapper.eq(ShoppingCart::getDishId, shoppingCart.getDishId());
    } else {
      // setMeal type
      lambdaQueryWrapper.eq(ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
    }

    // check if add new record or update record with qunatity increased
    // select count(*) from shoppingCart where userId = ? and setMealId/dishId = ?
    ShoppingCart getOne = shoppingCartService.getOne(lambdaQueryWrapper);
    if (getOne != null) {
      Integer currQuantity = getOne.getNumber();
      getOne.setNumber(currQuantity + 1);
      shoppingCartService.updateById(getOne); // update the current shoppingCart
      return Result.success(getOne);
    } else {
      shoppingCart.setNumber(1); // default is 1
      shoppingCartService.save(shoppingCart);
      return Result.success(shoppingCart);
    }
  }

  @PostMapping("/sub")
  public Result<ShoppingCart> sub(@RequestBody ShoppingCart shoppingCart) {
    Long dishId = shoppingCart.getDishId();
    Long setmealId = shoppingCart.getSetmealId();

    Long userId = BaseContext.getCurrentId();
    LambdaQueryWrapper<ShoppingCart> lambdaQueryWrapper = new LambdaQueryWrapper<>();
    lambdaQueryWrapper.eq(userId != null, ShoppingCart::getUserId, userId);
    // update if it is a dish
    if (dishId != null) {
      lambdaQueryWrapper.eq(ShoppingCart::getDishId, dishId);
    }
    // else update setmeal
    else {
      lambdaQueryWrapper.eq(setmealId != null, ShoppingCart::getSetmealId, setmealId);
    }

    ShoppingCart getOne = shoppingCartService.getOne(lambdaQueryWrapper);
    Integer quantity = getOne.getNumber();
    Integer updateQuantity = quantity - 1;

    getOne.setNumber(
        quantity
            - 1); // when updateQuantity == 0, we still need to return the getOne with number = 0 to
    // frontend to reflect the changes
    if (updateQuantity == 0) shoppingCartService.removeById(getOne.getId());
    else if (updateQuantity < 0) return Result.error("updateQuantity smaller than 0.");
    else {
      shoppingCartService.update(getOne, lambdaQueryWrapper);
    }
    return Result.success(getOne);
  }

  @DeleteMapping("/clean")
  public Result<String> clean() {
    Long userId = BaseContext.getCurrentId();
    if (userId == null) return Result.error("User not login"); // 没有这一句的话有可能误删整个shoppingCart表
    LambdaQueryWrapper<ShoppingCart> lambdaQueryWrapper = new LambdaQueryWrapper<>();

    lambdaQueryWrapper.eq(ShoppingCart::getUserId, userId);
    // Delete * from shoppinCart where userId = ?
    shoppingCartService.remove(lambdaQueryWrapper);

    return Result.success("shoppingCart cleaned successfully");
  }
}
