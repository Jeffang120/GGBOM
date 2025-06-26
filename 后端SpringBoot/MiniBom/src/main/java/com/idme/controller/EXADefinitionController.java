package com.idme.controller;

import com.huawei.innovation.rdm.coresdk.basic.vo.DeleteByConditionVo;
import com.huawei.innovation.rdm.coresdk.basic.vo.QueryRequestVo;
import com.huawei.innovation.rdm.xdm.dto.entity.EXADefinitionCreateDTO;
import com.huawei.innovation.rdm.xdm.dto.entity.EXADefinitionUpdateDTO;
import com.huawei.innovation.rdm.xdm.dto.entity.EXADefinitionViewDTO;
import com.huawei.innovation.rdm.xdm.dto.relation.EXADefinitionLinkHistoryViewDTO;
import com.idme.request.DeleteRequest;
import com.idme.request.QueryByIdWithSortRequest;
import com.idme.service.EXADefinitionService;
import com.idme.controller.Result;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * EXA定义管理接口
 *
 * 提供对扩展属性定义的增删改查操作
 * 所有接口返回统一格式：Result<T>
 */
@RestController
@RequestMapping("/exa")
public class EXADefinitionController {

    @Autowired
    private EXADefinitionService exaDefinitionService;

    /**
     * 创建一个新的 EXA 定义
     *
     * @param dto 包含创建所需数据的 DTO 对象
     * @return Result<String> 操作结果，成功返回 "创建成功"，失败返回错误信息
     */
    @PostMapping("/create")
    public Result<String> create(@RequestBody EXADefinitionCreateDTO dto) {
        return Result.from(exaDefinitionService.createEXADefinition(dto));
    }

    /**
     * 根据关键词模糊匹配中文名或英文名进行查询
     *
     * @param keyword 查询关键字
     * @return Result<List<EXADefinitionViewDTO>> 查询结果列表
     */
    @GetMapping("/querybyname")
    public Result<List<EXADefinitionViewDTO>> query(@RequestParam String keyword) {
        return exaDefinitionService.queryByNames(keyword);
    }

    /**
     * 获取系统中所有的属性定义
     *
     * @return Result<List<EXADefinitionViewDTO>> 所有属性定义信息
     */
    @GetMapping("/getAll")
    public Result<List<EXADefinitionViewDTO>> getAllAttributes() {
        return exaDefinitionService.getAllAttributes();
    }


    /**
     * 根据 ID 删除 EXA 定义
     *
     * @param deleteRequest 包含要删除记录的 ID 的请求对象
     * @return Result<String> 操作结果，成功返回 "删除成功"，失败返回错误信息
     */
    @PostMapping("/delete")
    public Result<String> deleteById(@RequestBody DeleteRequest deleteRequest) {
        String id = deleteRequest.getId();
        if (id == null || id.isEmpty()) {
            return Result.error("id 不能为空");
        }
        return exaDefinitionService.deleteById(id);

    }
    /**
     * 更新 EXA 定义描述信息
     *
     * @param oldName 原始中文名称
     * @param dto     包含新数据的 DTO 对象
     * @return Result<String> 操作结果，成功返回 "更新成功"，失败返回错误信息
     */
    @PutMapping("/update")
    public Result<String> updateDescription(@RequestParam String oldName,
                                            @RequestBody EXADefinitionUpdateDTO dto) {
        return exaDefinitionService.updateEXADefinition(oldName, dto);
    }
}

