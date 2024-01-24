package com.zun.ojapiclientsdk.client;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import com.zun.ojapiclientsdk.model.ExecuteCodeRequest;
import com.zun.ojapiclientsdk.model.ExecuteCodeResponse;
import com.zun.ojapiclientsdk.util.SignUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class OjApiClient {

    private final String accessKey;

    private final String secretKey;

    public  final String apiGatewayUrl;

    public OjApiClient(String accessKey, String secretKey, String apiGatewayUrl) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
        this.apiGatewayUrl = apiGatewayUrl;
    }

    /**
     * 获取请求头的哈希映射
     *
     * @param body 请求体内容
     * @return 包含请求头参数的哈希映射
     */
    public Map<String, String> getHeaderMap(String body) {
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put("accessKey", accessKey);
        //注意：不能直接发送密钥
        //hashMap.put("secretKey",secretKey);
        //生成随机数（生成一个包含100个随机数字的字符串）
        hashMap.put("nonce", RandomUtil.randomNumbers(100));
        //解决参数中文乱码
        try {
            body = URLEncoder.encode(body, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        hashMap.put("body",body);

        //当前时间戳
        //System.currentTimeMil1is()返回当前时间的毫秒数。通过除以1000,可以将毫秒数转换为秒数，以得到当前时间戳的秒级表示
        hashMap.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
        //生成签名
        hashMap.put("sign", SignUtils.genSign(body, secretKey));
        return hashMap;
    }

    /**
     * 调用执行代码接口，Scanner传参
     *
     * @param executeCodeRequest 执行代码请求
     * @return 执行后输出结果
     */
    public ExecuteCodeResponse execCodeAcmPattern(ExecuteCodeRequest executeCodeRequest) {
        //将用户对象转换为JS0N字符串
        String json = JSONUtil.toJsonStr(executeCodeRequest);
        HttpResponse httpResponse = HttpRequest.post("http://" + apiGatewayUrl + "/open/exec/java/native/acm")
                //添加请求头
                .addHeaders(getHeaderMap(json))
                //设置请求体
                .body(json)
                //发送POST请求
                .execute();
        if (httpResponse.isOk()) {
            return JSONUtil.toBean(httpResponse.body(), ExecuteCodeResponse.class);
        } else {
            throw new RuntimeException("执行代码出错, " + httpResponse.getStatus());
        }
    }

    /**
     * 调用执行代码接口，Args参数传参
     *
     * @param executeCodeRequest 执行代码请求
     * @return 执行后输出结果
     */
    public ExecuteCodeResponse execCodeArgsPattern(ExecuteCodeRequest executeCodeRequest) {
        //将用户对象转换为]S0N字符串
        String json = JSONUtil.toJsonStr(executeCodeRequest);
        HttpResponse httpResponse = HttpRequest.post("http://" + apiGatewayUrl + "/open/exec/java/native/args")
                //添加请求头
                .addHeaders(getHeaderMap(json))
                //设置请求体
                .body(json)
                //发送POST请求
                .execute();
        if (httpResponse.isOk()) {
            return JSONUtil.toBean(json, ExecuteCodeResponse.class);
        } else {
            throw new RuntimeException("执行代码出错, " + httpResponse.getStatus());
        }
    }

}
