function isValidUsername(str) {
  return ["admin", "editor"].indexOf(str.trim()) >= 0;
}

function isExternal(path) {
  return /^(https?:|mailto:|tel:)/.test(path);
}

function isCellPhone(val) {
  if (!/(6|8|9)\d{7}$/.test(val)) {
    return false;
  } else {
    return true;
  }
}

//校验账号
function checkUserName(rule, value, callback) {
  if (value == "") {
    callback(new Error("Please enter username"));
  } else if (value.length > 20 || value.length < 3) {
    callback(new Error("Length of username is in range of 3-20"));
  } else {
    callback();
  }
}

//校验姓名
function checkName(rule, value, callback) {
  if (value == "") {
    callback(new Error("Please enter name"));
  } else if (value.length > 12) {
    callback(new Error("Length of name is in range of 1-12"));
  } else {
    callback();
  }
}

function checkPhone(rule, value, callback) {
  // let phoneReg = /(^1[3|4|5|6|7|8|9]\d{9}$)|(^09\d{8}$)/;
  if (value == "") {
    callback(new Error("Please enter phone number"));
  } else if (!isCellPhone(value)) {
    //引入methods中封装的检查手机格式的方法
    callback(new Error("Enter the correct Singapore number"));
  } else {
    callback();
  }
}

function validID(rule, value, callback) {
  // start with STFG followed by 7 numbers and 1 character
  let reg = /^[STFG]\d{7}[A-Z]$/;
  if (value == "") {
    callback(new Error("Please enter your id"));
  } else if (reg.test(value)) {
    callback();
  } else {
    callback(new Error("Incorrect Singapore id"));
  }
}
