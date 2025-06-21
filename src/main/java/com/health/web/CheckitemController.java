package com.health.web;

import com.health.domain.query.PageQueryDTO;
import com.health.domain.vo.Result;
import com.health.service.CheckitemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    // 根据UserName查询检查项
}
