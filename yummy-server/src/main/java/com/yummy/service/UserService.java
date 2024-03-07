package com.yummy.service;

import com.yummy.dto.UserLoginDTO;
import com.yummy.entity.User;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    User wxLogin(UserLoginDTO userLoginDTO);
}
