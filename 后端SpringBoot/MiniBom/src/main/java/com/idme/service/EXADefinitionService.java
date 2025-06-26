package com.idme.service;

import com.huawei.innovation.rdm.coresdk.basic.dto.PersistObjectIdsModifierDTO;
import com.huawei.innovation.rdm.coresdk.basic.enums.ConditionType;
import com.huawei.innovation.rdm.coresdk.basic.vo.DeleteByConditionVo;
import com.huawei.innovation.rdm.coresdk.basic.vo.QueryRequestVo;
import com.huawei.innovation.rdm.coresdk.basic.vo.RDMPageVO;
import com.huawei.innovation.rdm.xdm.dto.entity.EXADefinitionBusinessCodeDTO;
import com.huawei.innovation.rdm.xdm.dto.entity.EXADefinitionCreateDTO;
import com.huawei.innovation.rdm.xdm.delegator.EXADefinitionDelegator;
import com.huawei.innovation.rdm.xdm.dto.entity.EXADefinitionUpdateDTO;
import com.huawei.innovation.rdm.xdm.dto.entity.EXADefinitionViewDTO;
import com.huawei.innovation.rdm.xdm.dto.relation.EXADefinitionLinkHistoryViewDTO;
import com.huawei.innovation.rdm.xdm.dto.relation.EXADefinitionLinkViewDTO;
import com.idme.controller.Result;
import com.idme.util.ConstraintUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EXADefinitionService {

    @Autowired
    private EXADefinitionDelegator exaDefinitionDelegator;
    @Autowired
    private EXADefinitionLinkService exaDefinitionLinkService;

    public String createEXADefinition(EXADefinitionCreateDTO dto) {
        if (dto == null) {
            return "请求数据不能为空";
        }

        if (dto.getName() == null || dto.getName().isEmpty()) {
            return "中文名称不能为空";
        }
        if (dto.getNameEn() == null || dto.getNameEn().isEmpty()) {
            return "英文名称不能为空";
        }
        if (dto.getDisableFlag() == null) {
            return "字段 disableFlag 是必填项";
        }
        // 校验中文名是否已存在
        QueryRequestVo nameQuery = new QueryRequestVo();
        nameQuery.addCondition("name", ConditionType.EQUAL, dto.getName());
        List<EXADefinitionViewDTO> existingByName = exaDefinitionDelegator.find(nameQuery, new RDMPageVO(1, 1));
        if (existingByName != null && !existingByName.isEmpty()) {
            return "中文名称已存在";
        }

        // 校验英文名是否已存在
        QueryRequestVo nameEnQuery = new QueryRequestVo();
        nameEnQuery.addCondition("nameEn", ConditionType.EQUAL, dto.getNameEn());
        List<EXADefinitionViewDTO> existingByNameEn = exaDefinitionDelegator.find(nameEnQuery, new RDMPageVO(1, 1));
        if (existingByNameEn != null && !existingByNameEn.isEmpty()) {
            return "英文名称已存在";
        }
        // 自动填充 constraint
        if (dto.getConstraint() == null || dto.getConstraint().trim().isEmpty()) {
            if (dto.getType() == null) {
                return "字段 type 是必填项";
            }
            try {
                dto.setConstraint(ConstraintUtil.getDefaultConstraint(dto.getType()));
            } catch (IllegalArgumentException e) {
                return "不支持的字段类型: " + dto.getType();
            }
        }

        try {
            exaDefinitionDelegator.create(dto);
            return "创建成功";
        } catch (Exception e) {
            return "创建失败: " + e.getMessage();
        }
    }

    /**
     * 获取所有属性定义信息（无参数）
     */
    public Result<List<EXADefinitionViewDTO>> getAllAttributes() {
        try {
            QueryRequestVo queryRequestVo = new QueryRequestVo(); // 空条件表示查询全部
            List<EXADefinitionViewDTO> attributes = exaDefinitionDelegator.find(queryRequestVo, new RDMPageVO(1, 100));

            if (attributes == null || attributes.isEmpty()) {
                return Result.error("未找到任何属性定义");
            }

            return Result.success(attributes);
        } catch (Exception e) {
            return Result.error("查询失败: " + e.getMessage());
        }
    }


    public Result<List<EXADefinitionViewDTO>> queryByNames(String keyword) {
        if (keyword== null||keyword.isEmpty()) {
            return Result.error("必须提供中文名或英文名进行查询");
        }

        try {
            QueryRequestVo queryRequestVo = new QueryRequestVo();

                queryRequestVo.or().
                        addCondition("name", ConditionType.LIKE, keyword)
                        .addCondition("nameEn", ConditionType.LIKE, keyword);


            List<EXADefinitionViewDTO> result = exaDefinitionDelegator.find(queryRequestVo, new RDMPageVO(1, 100));

            if (result == null || result.isEmpty()) {
                return Result.error("未找到匹配的记录");
            }

            return Result.success(result);

        } catch (Exception e) {
            return Result.error("查询失败: " + e.getMessage());
        }
    }


    public Result<String> deleteById(String id) {
        if (id == null || id.isEmpty()) {
            return Result.error("ID 不能为空");
        }

        try {
            QueryRequestVo queryRequestVo = new QueryRequestVo();
            queryRequestVo.addCondition("id", ConditionType.EQUAL, id);

            List<EXADefinitionViewDTO> result = exaDefinitionDelegator.find(queryRequestVo, new RDMPageVO(1, 1));
            if (result == null || result.isEmpty()) {
                return Result.error("未找到指定 ID 的记录");
            }

            PersistObjectIdsModifierDTO modifier = new PersistObjectIdsModifierDTO();
            List<Long> list=new ArrayList<>();
            list.add(Long.parseLong(id));
            modifier.setIds(list);
            exaDefinitionDelegator.batchDelete(modifier);


            return Result.success("删除成功");

        } catch (Exception e) {
            return Result.error("删除失败: " + e.getMessage());
        }
    }


    public Result<String> updateEXADefinition(String oldName, EXADefinitionUpdateDTO dto) {
        if (oldName == null || oldName.isEmpty()) {
            return Result.error("原始中文名称不能为空");
        }
        if (dto == null) {
            return Result.error("请求数据不能为空");
        }

        try {
            // 查询原记录
            QueryRequestVo queryRequestVo = new QueryRequestVo();
            queryRequestVo.addCondition("name", ConditionType.EQUAL, oldName);

            List<EXADefinitionViewDTO> existingList = exaDefinitionDelegator.find(queryRequestVo, new RDMPageVO(1, 1));
            if (existingList == null || existingList.isEmpty()) {
                return Result.error("未找到该中文名称的记录");
            }

            EXADefinitionViewDTO existing = existingList.get(0);

            // 构造更新对象 —— 只允许修改 description 和 descriptionEn
            EXADefinitionUpdateDTO updateDto = new EXADefinitionUpdateDTO();

            // 保留所有原始字段值
            updateDto.setId(existing.getId());
            updateDto.setName(existing.getName());
            updateDto.setEnumNameEn(existing.getNameEn());
            updateDto.setDisableFlag(existing.getDisableFlag());
            updateDto.setType(existing.getType());
            updateDto.setConstraint(existing.getConstraint());

            // 允许修改的字段
            updateDto.setDescription(dto.getDescription());
            updateDto.setDescriptionEn(dto.getDescriptionEn());

            // 调用 Delegator 的 update 方法
            exaDefinitionDelegator.update(updateDto);

            return Result.success("更新成功");

        } catch (Exception e) {
            return Result.error("更新失败: " + e.getMessage());
        }
    }


}

