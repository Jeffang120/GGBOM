package com.idme.service;


import com.huawei.innovation.rdm.coresdk.basic.dto.ObjectReferenceParamDTO;
import com.huawei.innovation.rdm.coresdk.basic.dto.PersistObjectIdModifierDTO;
import com.huawei.innovation.rdm.coresdk.basic.dto.PersistObjectIdsModifierDTO;
import com.huawei.innovation.rdm.coresdk.basic.dto.QueryChildListDTO;
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
import com.idme.controller.Result;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

@Service
public class CategoryService {

    @Autowired
    private ClassificationNodeDelegator classificationNodeDelegator;

    public Result<String> createCategory(ClassificationNodeCreateDTO dto) {
        if (dto == null) {
            return Result.error("请求数据不能为空");
        }

        if (dto.getName() == null || dto.getName().isEmpty()) {
            return Result.error("分类名称不能为空");
        }

        try {
            QueryRequestVo queryRequestVo = new QueryRequestVo();
            queryRequestVo.addCondition("name", ConditionType.EQUAL, dto.getName());

            List<ClassificationNodeViewDTO> existingCategories = classificationNodeDelegator.find(queryRequestVo, new RDMPageVO(1, 1));
            if (existingCategories != null && !existingCategories.isEmpty()) {
                return Result.error("分类名称已存在");
            }

            classificationNodeDelegator.create(dto);
            return Result.success("分类创建成功");

        } catch (Exception e) {
            return Result.error("分类创建失败: " + e.getMessage());
        }
    }

    public Result<String> deleteCategory(String id) {
        if (id == null || id.isEmpty()) {
            return Result.error("分类ID不能为空");
        }

        try {
            QueryRequestVo queryRequestVo = new QueryRequestVo();
            queryRequestVo.addCondition("id", ConditionType.EQUAL, id);

            List<ClassificationNodeViewDTO> existingCategories = classificationNodeDelegator.find(queryRequestVo, new RDMPageVO(1, 1));
            if (existingCategories == null || existingCategories.isEmpty()) {
                return Result.error("未找到指定的分类");
            }


            PersistObjectIdsModifierDTO modifierDTO = new PersistObjectIdsModifierDTO();
            List<Long> ids = new ArrayList<>();
            ids.add(Long.parseLong(id));
            modifierDTO.setIds(ids);
            classificationNodeDelegator.batchDelete(modifierDTO);


            return Result.success("分类删除成功");
        } catch (Exception e) {
            return Result.error("分类删除失败: " + e.getMessage());
        }
    }

    public Result<List<ClassificationNodeViewDTO>> getCategoryById(String id) {
        if (id == null || id.isEmpty()) {
            return Result.error("分类ID不能为空");
        }

        try {
            QueryRequestVo queryRequestVo = new QueryRequestVo();
            queryRequestVo.addCondition("id", ConditionType.EQUAL, id);

            List<ClassificationNodeViewDTO> categoryList = classificationNodeDelegator.find(queryRequestVo, new RDMPageVO(1, 1));

            if (categoryList == null || categoryList.isEmpty()) {
                return Result.error("未找到指定分类");
            }

            return Result.success(categoryList);
        } catch (Exception e) {
            return Result.error("获取分类失败：" + e.getMessage());
        }
    }

    public Result<Map<String, Object>> updateCategory(ClassificationNodeUpdateDTO updateDTO) {
        Long id = updateDTO.getId();
        if (id == null || id == 0) {
            return Result.error("分类ID不能为空");
        }

        try {
            QueryRequestVo queryRequestVo = new QueryRequestVo();
            queryRequestVo.addCondition("id", ConditionType.EQUAL, id);

            List<ClassificationNodeViewDTO> existingCategories = classificationNodeDelegator.find(queryRequestVo, new RDMPageVO(1, 1));
            if (existingCategories == null || existingCategories.isEmpty()) {
                return Result.error("未找到指定的分类");
            }

            classificationNodeDelegator.update(updateDTO);

            List<ClassificationNodeViewDTO> updatedList = classificationNodeDelegator.find(queryRequestVo, new RDMPageVO(1, 1));

            Map<String, Object> responseData = new HashMap<>();
            responseData.put("result", "SUCCESS");
            responseData.put("data", updatedList);
            responseData.put("errors", Collections.emptyList());

            return Result.success(responseData);
        } catch (Exception e) {
            return Result.error("分类更新失败: " + e.getMessage());
        }
    }

    public Result<List<ClassificationNodeViewDTO>> queryCategoriesByName(String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            return Result.error("搜索关键词不能为空");
        }

        try {
            QueryRequestVo queryRequestVo = new QueryRequestVo();
            queryRequestVo.or()
                    .addCondition("name", ConditionType.LIKE, keyword)
                    .addCondition("nameEn", ConditionType.LIKE, keyword)
                    .addCondition("businessCode", ConditionType.LIKE, keyword);


            List<ClassificationNodeViewDTO> result = classificationNodeDelegator.find(queryRequestVo, new RDMPageVO(1, 10));

            if (result == null || result.isEmpty()) {
                return Result.error("未找到匹配的分类");
            }

            return Result.success(result);
        } catch (Exception e) {
            return Result.error("查询失败: " + e.getMessage());
        }
    }
    public Result<List<ClassificationNodeQueryViewDTO>> getChildrenByParentId(String parentId) {
        if (parentId == null || parentId.isEmpty()) {
            return Result.error("父分类ID不能为空");
        }

        try {
            QueryChildListDTO dto = new QueryChildListDTO();
            dto.setParentId(Long.parseLong(parentId));
            // 调用 delegator 获取子分类列表
            List<ClassificationNodeQueryViewDTO> children = classificationNodeDelegator.getChildList(dto, new RDMPageVO(1, 10));

            if (children == null || children.isEmpty()) {
                return Result.error("该分类没有子分类");
            }

            return Result.success(children);
        } catch (Exception e) {
            return Result.error("查询失败: " + e.getMessage());
        }
    }
    /**
     * 获取所有分类信息（无参数）
     */
    public Result<List<ClassificationNodeViewDTO>> getAllCategories() {
        try {
            QueryRequestVo queryRequestVo = new QueryRequestVo(); // 空条件表示查询全部
            List<ClassificationNodeViewDTO> categories = classificationNodeDelegator.find(queryRequestVo, new RDMPageVO(1, 100));

            if (categories == null || categories.isEmpty()) {
                return Result.error("未找到任何分类");
            }

            return Result.success(categories);
        } catch (Exception e) {
            return Result.error("查询失败: " + e.getMessage());
        }
    }


}
