package com.idme.service;

import com.huawei.innovation.rdm.coresdk.basic.dto.ObjectReferenceParamDTO;
import com.huawei.innovation.rdm.coresdk.basic.dto.PersistObjectIdModifierDTO;
import com.huawei.innovation.rdm.coresdk.basic.enums.ConditionType;
import com.huawei.innovation.rdm.coresdk.basic.vo.QueryRequestVo;
import com.huawei.innovation.rdm.coresdk.basic.vo.RDMPageVO;
import com.huawei.innovation.rdm.minibom.delegator.PartBOMLinkDelegator;
import com.huawei.innovation.rdm.minibom.delegator.PartDelegator;
import com.huawei.innovation.rdm.minibom.dto.relation.PartBOMLinkCreateDTO;
import com.huawei.innovation.rdm.minibom.dto.relation.PartBOMLinkUpdateDTO;
import com.huawei.innovation.rdm.minibom.dto.relation.PartBOMLinkViewDTO;
import com.idme.controller.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PartBOMLinkService {

    @Autowired
    private PartBOMLinkDelegator partBOMLinkDelegator;
    @Autowired
    private PartDelegator partDelegator;
    @Autowired
    private PartService partService;


    public Result<String> createPartBOMLink(PartBOMLinkCreateDTO dto) {
        if (dto == null) {
            return Result.error("请求数据不能为空");
        }

        try {
            dto.getSource().setClazz("com.huawei.innovation.rdm.minibom.entity.Part");
            dto.getTarget().setClazz("com.huawei.innovation.rdm.minibom.entity.PartMaster");
            dto.getTenant().setClazz("com.huawei.innovation.rdm.minibom.entity.Tenant");
            partBOMLinkDelegator.create(dto);
            return Result.success("子项创建成功");
        } catch (Exception e) {
            return Result.error("创建失败：" + e.getMessage());
        }
    }

    public Result<String> deletePartBOMLink(Long id) {
        if (id == null) {
            return Result.error("ID 不能为空");
        }
        try {
            QueryRequestVo queryRequestVo = new QueryRequestVo();
            queryRequestVo.addCondition("id", ConditionType.EQUAL, id);

            List<PartBOMLinkViewDTO> result = partBOMLinkDelegator.find(queryRequestVo, new RDMPageVO(1, 1));
            if (result == null || result.isEmpty()) {
                return Result.error("未找到指定 ID 的记录");
            }
            PersistObjectIdModifierDTO modifier = new PersistObjectIdModifierDTO();
            modifier.setId(id);
            partBOMLinkDelegator.delete(modifier);
            return Result.success("子项删除成功");
        } catch (Exception e) {
            return Result.error("删除失败：" + e.getMessage());
        }
    }

    public Result<String> updatePartBOMLink(PartBOMLinkUpdateDTO dto) {
        if (dto == null || dto.getId() == null) {
            return Result.error("请求数据或 ID 不能为空");
        }

        try {
            // 根据 ID 查询现有的 PartBOMLink
            QueryRequestVo queryRequestVo = new QueryRequestVo();
            queryRequestVo.addCondition("id", ConditionType.EQUAL, dto.getId());

            List<PartBOMLinkViewDTO> existingList = partBOMLinkDelegator.find(queryRequestVo, new RDMPageVO(1, 1));
            if (existingList == null || existingList.isEmpty()) {
                return Result.error("未找到指定 ID 的 PartBOMLink");
            }

            PartBOMLinkViewDTO existing = existingList.get(0);

            // 创建新的 PartBOMLink 实例
            PartBOMLinkCreateDTO newDto = new PartBOMLinkCreateDTO();
            newDto.setSource(new ObjectReferenceParamDTO(existing.getSource().getId(), "com.huawei.innovation.rdm.minibom.entity.Part"));
            newDto.setTarget(new ObjectReferenceParamDTO(existing.getTarget().getId(), "com.huawei.innovation.rdm.minibom.entity.PartMaster"));
            newDto.setTenant(new ObjectReferenceParamDTO(existing.getTenant().getId(), "com.huawei.innovation.rdm.minibom.entity.Tenant"));

            // 更新 position 和 quantity 字段
            if (dto.getPosition() != null) {
                newDto.setPosition(dto.getPosition());
            } else {
                newDto.setPosition(existing.getPosition());
            }

            if (dto.getQuantity() != null) {
                newDto.setQuantity(dto.getQuantity());
            } else {
                newDto.setQuantity(existing.getQuantity());
            }

            // 创建新的 PartBOMLink
            partBOMLinkDelegator.create(newDto);

            // 删除旧的 PartBOMLink
            PersistObjectIdModifierDTO deleteDto = new PersistObjectIdModifierDTO();
            deleteDto.setId(existing.getId());
            partBOMLinkDelegator.delete(deleteDto);

            return Result.success("PartBOMLink 更新成功");

        } catch (Exception e) {
            return Result.error("更新失败: " + e.getMessage());
        }
    }

    /**
     * 根据 sourceId 查询 PartBOMLink
     *
     * @param sourceId 部件源 ID
     * @return 匹配的 PartBOMLink 列表
     */
    public Result<List<PartBOMLinkViewDTO>> getPartBOMLinksBySourceId(String sourceId) {
        if (sourceId == null || sourceId.isEmpty()) {
            return Result.error("sourceId 不能为空");
        }

        try {
            // 构建查询条件
            QueryRequestVo queryRequestVo = new QueryRequestVo();
            queryRequestVo.addCondition("source.id", ConditionType.EQUAL, sourceId);

            // 执行查询
            List<PartBOMLinkViewDTO> result = partBOMLinkDelegator.find(queryRequestVo, new RDMPageVO(1, 100));

            if (result == null || result.isEmpty()) {
                return Result.error("未找到子项");
            }

            return Result.success(result);
        } catch (Exception e) {
            return Result.error("查询失败: " + e.getMessage());
        }
    }

    public Result<List<PartBOMLinkViewDTO>> getPartBOMLinksByTargetId(String targetId) {
        if (targetId == null || targetId.isEmpty()) {
            return Result.error("targetId 不能为空");
        }

        try {
            // 构建查询条件
            QueryRequestVo queryRequestVo = new QueryRequestVo();
            queryRequestVo.addCondition("target.id", ConditionType.EQUAL, targetId);

            // 执行查询
            List<PartBOMLinkViewDTO> result = partBOMLinkDelegator.find(queryRequestVo, new RDMPageVO(1, 100));

            if (result == null || result.isEmpty()) {
                return Result.error("未找到父项");
            }

            return Result.success(result);
        } catch (Exception e) {
            return Result.error("查询失败: " + e.getMessage());
        }
    }

}