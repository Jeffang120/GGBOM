package com.idme.service;

import com.huawei.innovation.rdm.coresdk.basic.dto.*;
import com.huawei.innovation.rdm.coresdk.basic.enums.ConditionType;
import com.huawei.innovation.rdm.coresdk.basic.vo.DeleteByConditionVo;
import com.huawei.innovation.rdm.coresdk.basic.vo.QueryCondition;
import com.huawei.innovation.rdm.coresdk.basic.vo.QueryRequestVo;
import com.huawei.innovation.rdm.coresdk.basic.vo.RDMPageVO;
import com.huawei.innovation.rdm.minibom.delegator.PartDelegator;
import com.huawei.innovation.rdm.minibom.dto.entity.PartCreateDTO;
import com.huawei.innovation.rdm.minibom.dto.entity.PartQueryViewDTO;
import com.huawei.innovation.rdm.minibom.dto.entity.PartUpdateDTO;
import com.huawei.innovation.rdm.minibom.dto.entity.PartViewDTO;

import com.idme.controller.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PartService {

    @Autowired
    private PartDelegator partDelegator;

    /**
     * 创建新的部件
     */
    public Result<String> createPart(PartCreateDTO dto) {
        if (dto == null) {
            return Result.error("请求数据不能为空");
        }
        if (dto.getName() == null || dto.getName().isEmpty()) {
            return Result.error("部件名称不能为空");
        }

        try {
            QueryRequestVo queryRequestVo = new QueryRequestVo();
            queryRequestVo.addCondition("name", ConditionType.EQUAL, dto.getName());

            List<PartViewDTO> existingList = partDelegator.find(queryRequestVo, new RDMPageVO(1, 1));
            if (existingList != null && !existingList.isEmpty()) {
                return Result.error("部件名称已存在");
            }

            // 调用 delegator 创建部件
            partDelegator.create(dto);
            return Result.success("部件创建成功");

        } catch (Exception e) {
            System.out.println("创建失败: " + e.getMessage());
            return Result.error("部件创建失败: " + e.getMessage());
        }
    }

    /**
     * 完整更新流程：检出 → 更新 → 检入
     *
     * @param updateDTO 包含 ID 和新数据的对象
     * @return Result<String> 成功或失败信息
     */
    public Result<String> updatePartWithCheckout(PartUpdateDTO updateDTO) {
        String id = updateDTO.getId().toString();
        if (id == null || id.isEmpty()) {
            return Result.error("部件ID不能为空");
        }

        try {

            VersionCheckOutDTO checkOutDTO = new VersionCheckOutDTO();
            checkOutDTO.setMasterId(updateDTO.getMaster().getId());
            // Step 1: 检出
            PartViewDTO checkout = partDelegator.checkout(checkOutDTO);

            updateDTO.setId(checkout.getId());
            updateDTO.setModifier(checkout.getCheckOutUserName());
            // Step 2: 更新
            partDelegator.update(updateDTO);
            // Step 3: 检入
            VersionCheckInDTO checkInDTO = new VersionCheckInDTO(updateDTO.getMaster().getId(), "更新部件");
            checkInDTO.setModifier(checkout.getCheckOutUserName());
            partDelegator.checkin(checkInDTO);


            return Result.success("部件更新成功");

        } catch (Exception e) {
            System.out.println("更新失败: " + e.getMessage());
            return Result.error("更新失败: " + e.getMessage());
        }
    }

    public Result<String> deletePart(String id) //根据masterId删除part
    {
        if (id == null || id.isEmpty()) {
            return Result.error("部件ID不能为空");
        }

        try {
            QueryRequestVo queryRequestVo = new QueryRequestVo();
            queryRequestVo.addCondition("master.id", ConditionType.EQUAL, id);

            List<PartViewDTO> existingList = partDelegator.find(queryRequestVo, new RDMPageVO(1, 1));
            if (existingList == null || existingList.isEmpty()) {
                return Result.error("未找到指定部件");
            }


            MasterIdsModifierDTO modifierDTO = new MasterIdsModifierDTO();
            List<Long> ids = new ArrayList<>();
            ids.add(Long.parseLong(id));
            modifierDTO.setMasterIds(ids);
            partDelegator.batchDelete(modifierDTO);




            return Result.success("部件删除成功");

        } catch (Exception e) {
            return Result.error("删除失败: " + e.getMessage());
        }
    }

    /**
     * 根据编码或名称关键字搜索匹配的部件
     */
    public Result<List<PartViewDTO>> queryPartsByName(String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            return Result.error("搜索关键词不能为空");
        }


        try {
            QueryRequestVo queryRequestVo = new QueryRequestVo();
            QueryCondition filter = new QueryCondition();
            filter.setJoiner("or");
            filter
                    .addCondition("name", ConditionType.LIKE, keyword);
//                    .addCondition("id", ConditionType.LIKE, keyword);
            queryRequestVo.setFilter(filter);

            List<PartViewDTO> result = partDelegator.find(queryRequestVo, new RDMPageVO(1, 100));


            if (result == null || result.isEmpty()) {
                return Result.error("未找到匹配的部件");
            }

            return Result.success(result);
        } catch (Exception e) {
            return Result.error("查询失败: " + e.getMessage());
        }
    }



    /**
     * 根据 ID 查询部件详情
     */
    public Result<List<PartViewDTO>> getPartById(String id) {
        if (id == null || id.isEmpty()) {
            return Result.error("部件ID不能为空");
        }

        try {
            QueryRequestVo queryRequestVo = new QueryRequestVo();
            queryRequestVo.addCondition("id", ConditionType.EQUAL, id);

            List<PartViewDTO> result = partDelegator.find(queryRequestVo, new RDMPageVO(1, 1));

            if (result == null || result.isEmpty()) {
                return Result.error("未找到指定部件");
            }

            return Result.success(result);
        } catch (Exception e) {
            return Result.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 获取所有部件信息
     */


    public Result<List<PartViewDTO>> getAllParts() {
        try {
            QueryRequestVo queryRequestVo = new QueryRequestVo(); // 空条件表示查询全部
            List<PartViewDTO> parts = partDelegator.find(queryRequestVo, new RDMPageVO(1, 100));

            if (parts == null || parts.isEmpty()) {
                return Result.error("未找到任何部件");
            }

            return Result.success(parts);
        } catch (Exception e) {
            return Result.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 删除指定部件的最新版本
     *
     * @param masterId 部件主ID
     * @return Result<String> 操作结果
     */
    public Result<String> deleteLatestVersion(String masterId) {
        if (masterId == null || masterId.isEmpty()) {
            return Result.error("masterId 不能为空");
        }

        try {
            VersionMasterModifierDTO modifierDTO = new VersionMasterModifierDTO();
            modifierDTO.setMasterId(Long.parseLong(masterId));
            // 调用 delegator 删除最新版本
            partDelegator.deleteLatestVersion(modifierDTO);

            return Result.success("最新版本删除成功");

        } catch (Exception e) {
            return Result.error("删除失败: " + e.getMessage());
        }
    }
    /**
     * 根据 masterId 获取部件的所有版本
     */
    public Result<List<PartQueryViewDTO>> getAllVersionsOfPart(String masterId) {
        if (masterId == null || masterId.isEmpty()) {
            return Result.error("masterId 不能为空");
        }

        try {

            VersionMasterDTO versionMasterDTO = new VersionMasterDTO();
            versionMasterDTO.setMasterId(Long.parseLong(masterId));
            List<PartQueryViewDTO> result = partDelegator.getAllVersions(versionMasterDTO, new RDMPageVO(1, 10));

            if (result == null || result.isEmpty()) {
                return Result.error("未找到指定部件的任何版本");
            }

            return Result.success(result);
        } catch (Exception e) {
            return Result.error("查询失败: " + e.getMessage());
        }
    }
    /**
     * 根据 masterId 查询对应的 PartViewDTO 列表
     */
    public Result<List<PartViewDTO>> getPartByMasterId(String masterId) {
        if (masterId == null || masterId.isEmpty()) {
            return Result.error("masterId 不能为空");
        }

        try {
            QueryRequestVo queryRequestVo = new QueryRequestVo();
            // 添加基于 "master.id" 的查询条件
            queryRequestVo.addCondition("master.id", ConditionType.EQUAL, masterId);

            // 执行查询
            List<PartViewDTO> result = partDelegator.find(queryRequestVo, new RDMPageVO(1, 1));

            if (result == null || result.isEmpty()) {
                return Result.error("未找到与该 masterId 关联的部件");
            }

            return Result.success(result);
        } catch (Exception e) {
            return Result.error("查询失败: " + e.getMessage());
        }
    }


}
