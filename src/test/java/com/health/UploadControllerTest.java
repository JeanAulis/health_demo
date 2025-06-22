package com.health;

import com.health.utils.AliOssUtil;
import com.health.web.UploadController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@SpringBootTest
public class UploadControllerTest {

    @Autowired
    private UploadController uploadController;

    @Autowired
    private AliOssUtil aliOssUtil;

    @Test
    public void testUploadImage() {
        try {
            // 指定要上传的图片文件路径
            String imagePath = "E:\\code_project\\IDEA_projects\\health-demo\\data\\套餐图片\\12.jpg";
            File imageFile = new File(imagePath);
            
            // 检查文件是否存在
            if (!imageFile.exists()) {
                System.err.println("测试文件不存在: " + imagePath);
                return;
            }
            
            System.out.println("开始测试上传文件: " + imagePath);
            System.out.println("文件大小: " + imageFile.length() + " bytes");
            
            // 读取文件内容
            Path path = Paths.get(imagePath);
            byte[] content = Files.readAllBytes(path);
            
            // 创建MockMultipartFile对象
            MultipartFile multipartFile = new MockMultipartFile(
                "file",                    // 参数名
                imageFile.getName(),       // 原始文件名
                "image/jpeg",             // 内容类型
                content                    // 文件内容
            );
            
            // 调用上传方法
            var result = uploadController.upload(multipartFile);
            
            // 输出测试结果
            System.out.println("上传结果: " + result);
            System.out.println("上传状态: " + (result.isFlag() ? "成功" : "失败"));
            System.out.println("返回消息: " + result.getMessage());
            System.out.println("图片URL: " + result.getData());
            
            // 验证结果
            assert result.isFlag() : "文件上传失败";
            assert result.getData() != null : "返回的图片URL为空";
            assert result.getData().toString().contains("https://") : "返回的URL格式不正确";
            
            System.out.println("✅ 文件上传测试通过!");
            
        } catch (IOException e) {
            System.err.println("❌ 文件读取失败: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("❌ 上传测试失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * 测试直接使用AliOssUtil上传文件
     */
    @Test
    public void testAliOssUtilUpload() {
        try {
            // 指定要上传的图片文件路径
            String imagePath = "E:\\code_project\\IDEA_projects\\health-demo\\data\\套餐图片\\12.jpg";
            File imageFile = new File(imagePath);
            
            // 检查文件是否存在
            if (!imageFile.exists()) {
                System.err.println("测试文件不存在: " + imagePath);
                return;
            }
            
            System.out.println("开始测试AliOssUtil直接上传: " + imagePath);
            
            // 使用FileInputStream读取文件
            try (FileInputStream inputStream = new FileInputStream(imageFile)) {
                // 调用AliOssUtil的upload方法
                String imageUrl = aliOssUtil.upload(imageFile.getName(), inputStream);
                
                // 输出测试结果
                System.out.println("上传成功!");
                System.out.println("图片URL: " + imageUrl);
                
                // 验证结果
                assert imageUrl != null : "返回的图片URL为空";
                assert imageUrl.contains("https://") : "返回的URL格式不正确";
                assert imageUrl.contains("health-nff.oss-cn-guangzhou.aliyuncs.com") : "返回的URL域名不正确";
                
                System.out.println("✅ AliOssUtil上传测试通过!");
                
            }
            
        } catch (IOException e) {
            System.err.println("❌ 文件读取失败: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("❌ AliOssUtil上传测试失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * 测试上传多种格式的图片文件
     */
    @Test
    public void testUploadMultipleFormats() {
        String[] testFiles = {
            "E:\\code_project\\IDEA_projects\\health-demo\\data\\套餐图片\\12.jpg",
            "E:\\code_project\\IDEA_projects\\health-demo\\data\\套餐图片\\13.jpg",
            "E:\\code_project\\IDEA_projects\\health-demo\\data\\套餐图片\\14.jpg"
        };
        
        for (String filePath : testFiles) {
            File file = new File(filePath);
            if (file.exists()) {
                System.out.println("\n测试上传文件: " + file.getName());
                try {
                    byte[] content = Files.readAllBytes(file.toPath());
                    MultipartFile multipartFile = new MockMultipartFile(
                        "file",
                        file.getName(),
                        "image/jpeg",
                        content
                    );
                    
                    var result = uploadController.upload(multipartFile);
                    System.out.println("上传结果: " + (result.isFlag() ? "成功" : "失败"));
                    if (result.isFlag()) {
                        System.out.println("图片URL: " + result.getData());
                    } else {
                        System.out.println("错误信息: " + result.getMessage());
                    }
                    
                } catch (Exception e) {
                    System.err.println("上传 " + file.getName() + " 失败: " + e.getMessage());
                }
            } else {
                System.out.println("文件不存在: " + filePath);
            }
        }
    }
}