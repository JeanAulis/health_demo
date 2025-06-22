package com.health.web;

import com.health.domain.vo.Result;
import com.health.utils.AliOssUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
public class UploadController {

    @Autowired
    private AliOssUtil aliOssUtil;

    @PostMapping("/setmeal/upload")
    public Result upload(MultipartFile file) throws IOException {
        String fliename = UUID.randomUUID() + file.getOriginalFilename();
        String imagPath = aliOssUtil.upload(fliename, file.getInputStream());
        return Result.success(imagPath);
    }
}
