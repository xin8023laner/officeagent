package com.common.utils;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.common.model.MsMessage;

/**
 * 自定义Json解析格式
 * Created by zhangruntao on 15/5/20.
 */
public class JsonParser {

    private static JsonParser jsonParser;
    private Context context;

    private JsonParser(Context context){
        this.context = context;
    }

    public static JsonParser getJsonParser(Context context){
        if(jsonParser == null)
            return new JsonParser(context);
        return jsonParser;
    }

    public MsMessage parseJsonToMsMessage(String json) {
        MsMessage msMessage = new MsMessage();
        try {

            JSONObject jsonObj = new JSONObject(json);
            if (json.contains("data\":[")) {// json数组
                msMessage.setData(jsonObj.getJSONArray("data"));
            } else {// json对象
                if (!jsonObj.get("data").toString().equals("null")) {// 判断对象是否为空

                    msMessage.setData(jsonObj.get("data").toString());
                }else{
                    msMessage.setResult(1);
                    msMessage.setMessage("查无结果！");
                  return msMessage;
                } 
            
            }

            msMessage.setMessage(jsonObj.getString("message"));
            msMessage.setResult(jsonObj.getInt("result"));

        } catch (JSONException e) {
            msMessage.setResult(1);
            msMessage.setMessage("解析异常："+e.getMessage());
            return msMessage;
        }
        return msMessage;
    }
    
}
