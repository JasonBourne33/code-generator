package com.example.cseckill.service.impl;

import com.example.cseckill.entity.User;
import com.example.cseckill.mapper.UserMapper;
import com.example.cseckill.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 用户表
 *
 * @author chaos
 * @date 2022-04-27
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

}
