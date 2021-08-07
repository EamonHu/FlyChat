package com.iflytek.chat.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iflytek.chat.pojo.ResultMessage;

/**
 * @Author: ymhu5
 * @Description: 工具类：封装ResultMessage
 * @Date: 2021/8/5 23:46
 */
public class MessageUtils {

    public static String getMessage(boolean isSystemMessage,String fromName,Object message){
        try{
            ResultMessage resultMessage = new ResultMessage();
            resultMessage.setSystem(isSystemMessage);
            resultMessage.setMessage(message);
            if(fromName != null){
                resultMessage.setFromName(fromName);
            }
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(resultMessage);
        }catch (JsonProcessingException e){
            e.printStackTrace();
        }
        return null;
    }
}
