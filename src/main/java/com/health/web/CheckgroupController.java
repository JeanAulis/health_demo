package com.health.web;

import com.health.domain.dto.CheckgroupDTO;
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

    // 新增检查组
    @PostMapping("/checkgroup/add")
    public Result add(@RequestBody CheckgroupDTO checkgroupDTO) {
        log.info("新增检查组请求，参数：{}", checkgroupDTO);
        return checkgroupService.add(checkgroupDTO);
    }

    // 编辑检查组
    // ①回显ID
    @GetMapping("/checkgroup/findById")
    public Result findById(Integer id) {
        log.info("编辑检查组请求，参数：{}", id);
        return checkgroupService.findById(id);
    }

    // ②中间表查询
    @GetMapping("/checkgroup/findCheckItemIdsByCheckGroupId")
    public Result findCheckItemIdsByCheckGroupId(Integer id) {
        log.info("查询检查组关联的检查项ID列表请求，参数：{}", id);
        return checkgroupService.findCheckItemIdsByCheckGroupId(id);
    }

    // ③修改检查组（先删除后添加）
    @PostMapping("/checkgroup/edit")
    public Result edit(@RequestBody CheckgroupDTO checkgroupDTO, @RequestParam("checkitemIds") Integer[] checkitemIds) {
        log.info("编辑检查组请求，参数：{}，检查项IDs：{}", checkgroupDTO, checkitemIds);
        return checkgroupService.edit(checkgroupDTO, checkitemIds);
    }

}