package com.atguigu.oss.controller.service.service.Impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.atguigu.oss.controller.service.OssService;
import com.atguigu.oss.utils.ConstantPropertiesUtils;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
public class OssServiceImpl implements OssService {
    @Override
    public String ossFileos(MultipartFile  file) {
//        获取oss 对应数据
        String endpoint = ConstantPropertiesUtils.END_POINT;
        String keyId = ConstantPropertiesUtils.KEY_ID;
        String keysecret = ConstantPropertiesUtils.KEYSECRET;
        String backrtname = ConstantPropertiesUtils.BUCKETNAME;


        //获取文件上传的输入流
        try {
            //创建oss实例
            OSS ossClient = new OSSClientBuilder().build(endpoint, keyId, keysecret);
            InputStream inputStream = file.getInputStream();
//                获取文件名称
                String fileName = file.getOriginalFilename();

                //避免文件上传名称重复
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
            fileName = fileName + uuid;

            //获取上传日期 , 对上传的文件进行分类
            // 根据上传

            String data = new DateTime().toString("yyyy/MM/dd");
            fileName = data + "/" +fileName;

            //调用oss方法完成上传
                //第一个参数 Bucket 名称
                //第二个参数  上传到oss的文件路径和文件名称  // xxx/p.xxx
                //第三个参数: 上传文件的流
                ossClient.putObject(backrtname , fileName , inputStream);
                ossClient.shutdown();
            //将上传之后的文件路径返回 , 用于保存到数据库
            // 上传到阿里云的路径需要手动拼接

//           https://atsusu.oss-cn-chengdu.aliyuncs.com/20220528/5.png
            String url = "https://"+backrtname+"."+endpoint+"/"+fileName;
            return url;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
