package com.lib_common.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

import android.content.Context;


/**
 * 省市区工具
 *
 * @author loar
 */
public class PCAUtil {

    public static Map<String, Map<String, List<String>>> getPCA(Context context, String filename) {
        Map<String, Map<String, List<String>>> result = null;
        try {
            StringBuffer str = new StringBuffer();
            InputStream is = context.getAssets().open(filename);
            BufferedReader br = new BufferedReader(new InputStreamReader(is,
                    "UTF-8"));
            String temp = "";
            while (br != null && null != (temp = br.readLine())) {
                str.append(temp);
            }
            result = GsonUtil.fromJsonString(str.toString(), Map.class);
//			result = JSON.parseObject(str.toString(), Map.class);
        } catch (IOException e) {
            ToastUtil.printErr(e);
        }
        return result;
    }

}
