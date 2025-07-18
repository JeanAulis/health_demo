package com.health.utils;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.ObjectMetadata;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

//阿里云存储工具类
@Component
@Data
public class AliOssUtil {

    private String key = ""; //访问key
    private String secret = "";//访问秘钥
    private String endpoint = "oss-cn-guangzhou.aliyuncs.com";//端点
    private String bucket = "health-nff";//桶名
    private String url = "https://health-nff.oss-cn-guangzhou.aliyuncs.com";//访问域名

    //文件上传
    public String upload(String fileName, InputStream inputStream) {

        //创建客户端
        OSS ossClient = new OSSClientBuilder().build(endpoint, key, secret);

        //设置文件最终的路径和名称
        String objectName = "images/" + new SimpleDateFormat("yyyy/MM/dd").format(new Date())
                + "/" + System.currentTimeMillis() + fileName.substring(fileName.lastIndexOf("."));

        //meta设置请求头,解决访问图片地址直接下载
        ObjectMetadata meta = new ObjectMetadata();
        meta.setContentType(getContentType(fileName.substring(fileName.lastIndexOf("."))));

        //上传
        ossClient.putObject(bucket, objectName, inputStream, meta);

        //关闭客户端
        ossClient.shutdown();

        return url + "/" + objectName;
    }

    //文件后缀处理
    private String getContentType(String FilenameExtension) {
        if (FilenameExtension.equalsIgnoreCase(".bmp")) {
            return "image/bmp";
        }
        if (FilenameExtension.equalsIgnoreCase(".gif")) {
            return "image/gif";
        }
        if (FilenameExtension.equalsIgnoreCase(".jpeg") ||
                FilenameExtension.equalsIgnoreCase(".jpg") ||
                FilenameExtension.equalsIgnoreCase(".png")) {
            return "image/jpg";
        }
        return "image/jpg";
    }
}