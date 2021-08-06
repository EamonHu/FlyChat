package com.iflytek.chat.pojo;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName User
 * @Description 用户类
 * @Author ymhu5
 * @Date 2021/8/6 10:32
 * @Version 1.0
 */
@Data
public class User implements Serializable {

    private String userName;
    private String passWord;
    private Boolean status;

}
