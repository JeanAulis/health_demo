package com.health.web;

import com.health.domain.entity.Checkitem;
import com.health.domain.query.PageQueryDTO;
import com.health.domain.vo.Result;
import com.health.service.CheckitemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class CheckitemController {

    @Autowired
    private CheckitemService checkitemService;

    // 分页查询检查项
    @PostMapping("/checkitem/findPage")
    public Result findPage(@RequestBody PageQueryDTO pageQueryDTO) {
        log.info("分页查询检查项请求，参数：{}", pageQueryDTO);
        return checkitemService.findPage(pageQueryDTO);
    }

    // 新增检查项数据
    @PostMapping("/checkitem/add")
    public Result add(@RequestBody Checkitem checkitem){
        log.info("新增检查项请求，参数：{}", checkitem);

        return checkitemService.add(checkitem);
    }

    // 查询所有检查项数据
    @GetMapping("/checkitem/findAll")
    public Result findAll(){
        log.info("查询所有检查项请求");
        
        return checkitemService.findAll();
    }
}
