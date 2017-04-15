package com.test.resource.controller;

import com.test.mysql.entity.User;
import com.test.mysql.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;


import org.springframework.web.bind.annotation.CrossOrigin;
//import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class UserController {
    @Autowired
    private UserRepository userRepository;

    /**
     *
     * @param puser  通过Principal 对象获取用户名
     * @return
     */
    @CrossOrigin(origins = "http://10.89.0.119:8866")
    @RequestMapping("/user")
    //@GetMapping("/greeting")
    public Map<String, Object> user(Principal puser) {
        User user = userRepository.findByName(puser.getName());
        Map<String, Object> userinfo = new HashMap<>();
        userinfo.put("id", user.getId());
        userinfo.put("name",user.getName());
        userinfo.put("email", user.getEmail());
        userinfo.put("department",user.getDepartment().getName());
        userinfo.put("createdate", user.getCreatedate());
        return userinfo;
    }
}
