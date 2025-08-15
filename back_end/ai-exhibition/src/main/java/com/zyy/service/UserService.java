package com.zyy.service;

import com.zyy.dto.UserLoginDTO;
import com.zyy.entity.User;

public interface UserService {
    User dyLogin(UserLoginDTO userLoginDTO);
}
