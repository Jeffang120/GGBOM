package com.idme.controller;

import com.huawei.innovation.rdm.coresdk.basic.dto.ObjectReferenceParamDTO;
import com.huawei.innovation.rdm.coresdk.basic.enums.ConditionType;
import com.huawei.innovation.rdm.coresdk.basic.vo.DeleteByConditionVo;
import com.huawei.innovation.rdm.coresdk.basic.vo.QueryRequestVo;
import com.huawei.innovation.rdm.coresdk.basic.vo.RDMPageVO;
 import com.huawei.innovation.rdm.xdm.delegator.ClassificationNodeDelegator;
import com.huawei.innovation.rdm.xdm.delegator.ClassificationNodeDelegator;


import com.huawei.innovation.rdm.xdm.dto.entity.ClassificationNodeCreateDTO;
import com.huawei.innovation.rdm.xdm.dto.entity.ClassificationNodeQueryViewDTO;
import com.huawei.innovation.rdm.xdm.dto.entity.ClassificationNodeUpdateDTO;
import com.huawei.innovation.rdm.xdm.dto.entity.ClassificationNodeViewDTO;
import com.idme.service.CategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 分类管理接口
 *
 * 提供分类的创建、删除、更新及查询操作（支持按名称、ID 查询及获取所有分类）
 * 所有接口返回统一格式：Result<T>
 */
@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 创建新的分类
     *
     * @param dto 包含分类信息的 DTO 对象
     * @return Result<String> 操作结果，成功返回 "创建成功"，失败返回错误信息
     */
    @PostMapping("/create")
    public Result<String> createCategory(@RequestBody ClassificationNodeCreateDTO dto) {
        return categoryService.createCategory(dto);
    }

    /**
     * 删除指定分类
     *
     * @param payload 请求体，包含要删除的分类 ID
     * @return Result<String> 操作结果，成功返回 "删除成功"，失败返回错误信息
     */
    @PostMapping("/delete")
    public Result<String> deleteCategory(@RequestBody Map<String, String> payload) {
        String id = payload.get("id");
        return categoryService.deleteCategory(id);
    }

    /**
     * 根据 ID 查询分类详情
     *
     * @param payload 请求体，包含分类 ID
     * @return Result<List<ClassificationNodeViewDTO>> 查询结果
     */
    @PostMapping("/getById")
    public Result<List<ClassificationNodeViewDTO>> getCategoryById(@RequestBody Map<String, String> payload) {
        String id = payload.get("id");
        return categoryService.getCategoryById(id);
    }

    /**
     * 更新分类信息
     *
     * @param updateDTO 包含新数据的 DTO 对象
     * @return Result<Map<String, Object>> 操作结果
     */
    @PostMapping("/update")
    public Result<Map<String, Object>> updateCategory(@RequestBody ClassificationNodeUpdateDTO updateDTO) {
        return categoryService.updateCategory(updateDTO);
    }

    /**
     * 根据名称关键字搜索分类
     *
     * @param keyword 查询关键字
     * @return Result<List<ClassificationNodeViewDTO>> 查询结果
     */
    @GetMapping("/query")
    public Result<List<ClassificationNodeViewDTO>> queryCategoriesByName(@RequestParam String keyword) {
        return categoryService.queryCategoriesByName(keyword);
    }

    /**
     * 获取某个分类的所有子分类
     *
     * @param parentId 父分类 ID
     * @return Result<List<ClassificationNodeQueryViewDTO>> 子分类列表
     */
    @GetMapping("/children")
    public Result<List<ClassificationNodeQueryViewDTO>> getChildrenByParentId(@RequestParam String parentId) {
        return categoryService.getChildrenByParentId(parentId);
    }

    /**
     * 获取系统中所有的分类
     *
     * @return Result<List<ClassificationNodeViewDTO>> 所有分类信息列表
     */
    @GetMapping("/getAll")
    public Result<List<ClassificationNodeViewDTO>> getAllCategories() {
        return categoryService.getAllCategories();
    }
}


