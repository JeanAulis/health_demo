package com.health.web;

import com.health.domain.query.PageQueryDTO;
import com.health.domain.vo.Result;
import com.health.service.CheckgroupService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class CheckgroupController {

    @Autowired
    private CheckgroupService checkgroupService;

    // 分页查询检查组
    @PostMapping("/checkgroup/findPage")
    public Result findPage(@RequestBody PageQueryDTO pageQueryDTO) {
        log.info("分页查询检查组请求，参数：{}", pageQueryDTO);
        return checkgroupService.findPage(pageQueryDTO);
    }

    // 删除检查组
    @GetMapping("/checkgroup/delete")
    public Result delete(@RequestParam Integer id) {
        log.info("删除检查组请求，参数：{}", id);
        return checkgroupService.delete(id);
    }
}