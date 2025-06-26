package com.idme.controller;

import com.huawei.innovation.rdm.xdm.dto.entity.ClassificationNodeViewDTO;
import com.huawei.innovation.rdm.xdm.dto.relation.EXADefinitionLinkCreateDTO;
import com.huawei.innovation.rdm.xdm.dto.relation.EXADefinitionLinkViewDTO;
import com.huawei.innovation.rdm.coresdk.basic.dto.ObjectReferenceParamDTO;
import com.idme.request.BatchDeleteRequest;
import com.idme.service.EXADefinitionLinkService;
import com.idme.controller.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 分类属性关联管理接口
 *
 * 提供分类与属性之间的批量创建、删除及查询功能
 * 所有接口返回统一格式：Result<T>
 */
@RestController
@RequestMapping("/EXADefinitionLink")
public class EXADefinitionLinkController {

    @Autowired
    private EXADefinitionLinkService exaDefinitionLinkService;

    /**
     * 批量创建分类属性关联
     *
     * @param requests 请求数据列表
     * @return Result<List<EXADefinitionLinkViewDTO>> 操作结果
     */
    @PostMapping("/batchCreate")
    public Result<List<EXADefinitionLinkViewDTO>> batchCreateEXADefinitionLinks(@RequestBody List<EXADefinitionLinkCreateDTO> requests) {
        return exaDefinitionLinkService.batchCreateEXADefinitionLinks(requests);
    }

    /**
     * 批量删除分类属性关联
     *
     * @param request 包含要删除的 ID 列表
     * @return Result<Map<String, Object>> 操作结果
     */
    @PostMapping("/batchDelete")
    public Result<Map<String, Object>> batchDeleteEXADefinitionLinks(@RequestBody BatchDeleteRequest request) {
        return exaDefinitionLinkService.batchDeleteEXADefinitionLinks(request);
    }

    /**
     * 根据分类 ID 查询其关联的属性
     *
     * @param classificationNodeId 分类节点 ID
     * @return Result<List<EXADefinitionLinkViewDTO>> 属性列表
     */
    @GetMapping("/listByClassification")
    public Result<List<EXADefinitionLinkViewDTO>> listAttributesByClassification(@RequestParam String classificationNodeId) {
        return exaDefinitionLinkService.listAttributesByClassification(classificationNodeId);
    }

    /**
     * 根据分类 ID 查询其父节点关联的属性
     *
     * @param classificationNodeId 分类节点 ID
     * @return Result<List<EXADefinitionLinkViewDTO>> 父节点关联的属性列表
     */
    @GetMapping("/getParentAttributesByClassification")
    public Result<List<EXADefinitionLinkViewDTO>> getParentAttributesByClassification(@RequestParam String classificationNodeId) {
        return exaDefinitionLinkService.getParentAttributesByClassification(classificationNodeId);
    }

    /**
     * 根据属性业务编码查询使用该属性的分类
     *
     * @param businessCode 属性业务编码
     * @return Result<List<ClassificationNodeViewDTO>> 分类信息列表
     */
    @GetMapping("/listClassificationsByAttribute")
    public Result<List<ClassificationNodeViewDTO>> listClassificationsByAttribute(@RequestParam String businessCode) {
        return exaDefinitionLinkService.listClassificationsByAttribute(businessCode);
    }
}
