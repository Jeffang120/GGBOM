package com.idme.service;

import com.huawei.innovation.rdm.coresdk.basic.dto.ObjectReferenceParamDTO;
import com.huawei.innovation.rdm.coresdk.basic.dto.PersistObjectIdDecryptDTO;
import com.huawei.innovation.rdm.coresdk.basic.dto.PersistObjectIdsModifierDTO;
import com.huawei.innovation.rdm.coresdk.basic.enums.ConditionType;
import com.huawei.innovation.rdm.coresdk.basic.vo.DeleteByConditionVo;
import com.huawei.innovation.rdm.coresdk.basic.vo.QueryRequestVo;
import com.huawei.innovation.rdm.coresdk.basic.vo.RDMPageVO;
import com.huawei.innovation.rdm.xdm.bean.relation.EXADefinitionLink;
import com.huawei.innovation.rdm.xdm.delegator.ClassificationNodeDelegator;
import com.huawei.innovation.rdm.xdm.delegator.EXADefinitionLinkDelegator;
import com.huawei.innovation.rdm.xdm.dto.entity.ClassificationNodeViewDTO;
import com.huawei.innovation.rdm.xdm.dto.relation.EXADefinitionLinkCreateDTO;
import com.huawei.innovation.rdm.xdm.dto.relation.EXADefinitionLinkViewDTO;
import com.idme.request.BatchDeleteRequest;
import com.idme.controller.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class EXADefinitionLinkService {

    @Autowired
    private EXADefinitionLinkDelegator exaDefinitionLinkDelegator;

    @Autowired
    private ClassificationNodeDelegator classificationNodeDelegator;

    public Result<List<EXADefinitionLinkViewDTO>> batchCreateEXADefinitionLinks(List<EXADefinitionLinkCreateDTO> requests) {
        if (requests == null || requests.isEmpty()) {
            return Result.error("请求数据不能为空");
        }

        try {
            List<EXADefinitionLinkCreateDTO> createDTOs = new ArrayList<>();

            for (EXADefinitionLinkCreateDTO request : requests) {
                EXADefinitionLinkCreateDTO dto = new EXADefinitionLinkCreateDTO();

                // 设置 tenant
                if (request.getTenant() != null && request.getTenant().getId() != null) {
                    ObjectReferenceParamDTO tenantRef = new ObjectReferenceParamDTO();
                    tenantRef.setId(request.getTenant().getId());
                    tenantRef.setClazz(request.getTenant().getClazz());
                    dto.setTenant(tenantRef);
                }

                // 设置 source
                if (request.getSource() != null && request.getSource().getId() != null) {
                    ObjectReferenceParamDTO sourceRef = new ObjectReferenceParamDTO();
                    sourceRef.setId(request.getSource().getId());
                    sourceRef.setClazz(request.getSource().getClazz());
                    dto.setSource(sourceRef);
                }

                // 设置 target
                if (request.getTarget() != null && request.getTarget().getId() != null) {
                    ObjectReferenceParamDTO targetRef = new ObjectReferenceParamDTO();
                    targetRef.setId(request.getTarget().getId());
                    targetRef.setClazz(request.getTarget().getClazz());
                    dto.setTarget(targetRef);
                }

                // 设置 defaultValue
                dto.setDefaultValue(request.getDefaultValue());

                createDTOs.add(dto);
            }

            List<EXADefinitionLinkViewDTO> viewList = exaDefinitionLinkDelegator.batchCreate(createDTOs);
            return Result.success(viewList);

        } catch (Exception e) {
            return Result.error("批量创建失败: " + e.getMessage());
        }
    }

    public Result<Map<String, Object>> batchDeleteEXADefinitionLinks(BatchDeleteRequest request) {
        if (request == null || request.getIds() == null || request.getIds().isEmpty()) {
            return Result.error("要删除的ID不能为空");
        }

        try {
            DeleteByConditionVo deleteVo = new DeleteByConditionVo();
            QueryRequestVo queryRequestVo = new QueryRequestVo();
            queryRequestVo.addCondition("id", ConditionType.IN, String.join(",", request.getIds()));
            deleteVo.setCondition(queryRequestVo);


            String id=request.getIds().get(0);
            PersistObjectIdsModifierDTO modifier = new PersistObjectIdsModifierDTO();
            List<Long> list=new ArrayList<>();
            list.add(Long.parseLong(id));
            modifier.setIds(list);
            int i = exaDefinitionLinkDelegator.batchDelete(modifier);
            if(i==0){
                return Result.error("未找到指定 ID 的记录");
            }
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("result", "SUCCESS");
            responseData.put("data", Collections.singletonList(i));


            return Result.success(responseData);
        } catch (Exception e) {
            return Result.error("批量删除失败: " + e.getMessage());
        }
    }

    public Result<List<EXADefinitionLinkViewDTO>> listAttributesByClassification(String classificationNodeId) {
        if (classificationNodeId == null || classificationNodeId.isEmpty()) {
            return Result.error("分类ID不能为空");
        }

        try {
            QueryRequestVo queryRequestVo = new QueryRequestVo();
            queryRequestVo.addCondition("target.id", ConditionType.EQUAL, classificationNodeId);

            RDMPageVO page = new RDMPageVO(1, 100);
            List<EXADefinitionLinkViewDTO> result = exaDefinitionLinkDelegator.find(queryRequestVo, page);

            if (result == null || result.isEmpty()) {
                return Result.error("未找到该分类下的属性");
            }

            return Result.success(result);
        } catch (Exception e) {
            return Result.error("查询失败: " + e.getMessage());
        }
    }
    private List<EXADefinitionLinkViewDTO> getAllParentAttributes(String nodeId, boolean includeCurrent) throws Exception {
        List<EXADefinitionLinkViewDTO> resultList = new ArrayList<>();

        // 1. 查询当前节点详情
        QueryRequestVo queryVo = new QueryRequestVo();
        queryVo.addCondition("id", ConditionType.EQUAL, nodeId);
        List<ClassificationNodeViewDTO> nodes = classificationNodeDelegator.find(queryVo, new RDMPageVO(1, 1));

        if (nodes == null || nodes.isEmpty()) {
            return resultList;
        }

        // 2. 选择性添加当前节点属性
        if (includeCurrent) {
            QueryRequestVo attrQuery = new QueryRequestVo();
            attrQuery.addCondition("target.id", ConditionType.EQUAL, nodeId);
            List<EXADefinitionLinkViewDTO> attributes = exaDefinitionLinkDelegator.find(attrQuery, new RDMPageVO(1, 100));
            if (attributes != null) {
                resultList.addAll(attributes);
            }
        }

        // 3. 递归处理父节点
        ClassificationNodeViewDTO currentNode = nodes.get(0);
        if (currentNode.getParentNode() != null && currentNode.getParentNode().getId() != null) {
            resultList.addAll(
                    getAllParentAttributes(
                            currentNode.getParentNode().getId().toString(),
                            true // 父节点总是包含自身属性
                    )
            );
        }
        return resultList;
    }
    public Result<List<EXADefinitionLinkViewDTO>> getParentAttributesByClassification(String classificationNodeId) {
        if (classificationNodeId == null || classificationNodeId.isEmpty()) {
            return Result.error("分类ID不能为空");
        }

        try {
            List<EXADefinitionLinkViewDTO> allAttributes =
                    getAllParentAttributes(classificationNodeId, false); // 初始调用不包含当前节点

            if (allAttributes.isEmpty()) {
                return Result.error("未找到任何属性");
            }
            return Result.success(allAttributes);
        } catch (Exception e) {
            return Result.error("查询失败: " + e.getMessage());
        }
    }


    public Result<List<ClassificationNodeViewDTO>> listClassificationsByAttribute(String businessCode) {
        if (businessCode == null || businessCode.isEmpty()) {
            return Result.error("属性业务编码不能为空");
        }

        try {
            QueryRequestVo queryRequestVo = new QueryRequestVo();
            queryRequestVo.addCondition("source.businessCode", ConditionType.EQUAL, businessCode);

            RDMPageVO page = new RDMPageVO(1, 100);
            List<EXADefinitionLinkViewDTO> links = exaDefinitionLinkDelegator.find(queryRequestVo, page);

            if (links == null || links.isEmpty()) {
                return Result.error("未找到该属性被使用的分类");
            }

            Set<String> classificationIds = new HashSet<>();
            for (EXADefinitionLinkViewDTO link : links) {
                if (link.getTarget() != null && link.getTarget().getId() != null) {
                    classificationIds.add(link.getTarget().getId().toString());
                }
            }

            if (classificationIds.isEmpty()) {
                return Result.error("未找到对应的分类节点");
            }

            QueryRequestVo classificationQuery = new QueryRequestVo();
            classificationQuery.addCondition("id", ConditionType.IN, new ArrayList<>(classificationIds));

            List<ClassificationNodeViewDTO> classifications = classificationNodeDelegator.find(classificationQuery, new RDMPageVO(1, 100));

            if (classifications == null || classifications.isEmpty()) {
                return Result.error("未找到对应的分类信息");
            }

            return Result.success(classifications);
        } catch (Exception e) {
            return Result.error("查询失败: " + e.getMessage());
        }
    }

}
