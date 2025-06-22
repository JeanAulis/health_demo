package com.health.web;

import com.health.domain.query.PageQueryDTO;
import com.health.domain.vo.Result;
import com.health.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    @PostMapping("/setmeal/findPage")
    public Result findPage(@RequestBody PageQueryDTO pageQueryDTO){
        log.info("分页查询检查项请求，参数：{}", pageQueryDTO);
        return setmealService.findPage(pageQueryDTO);
    }

//    @PostMapping("/setmeal/upload")
//    public Result upload(@RequestParam("image") MultipartFile file){
//        log.info("文件上传：{}", file.getOriginalFilename());
//        String url = AliyunOssClient.upload(file);
//        log.info("文件上传完成，结果：{}", url);
//        return Result.success(url);
//    }


}
