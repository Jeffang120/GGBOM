package com.idme.controller;

import com.huawei.innovation.rdm.minibom.dto.relation.PartBOMLinkCreateDTO;
import com.huawei.innovation.rdm.minibom.dto.relation.PartBOMLinkUpdateDTO;
import com.huawei.innovation.rdm.minibom.dto.relation.PartBOMLinkViewDTO;
import com.idme.request.PartBomLinkDeleteRequest;
import com.idme.request.SourceIdRequest;
import com.idme.request.TargetIdRequest;
import com.idme.service.PartBOMLinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * BOM链接管理接口
 *
 * 提供 BOM 链接的创建、删除、更新及查询操作（支持通过 sourceId 和 targetId 查询）
 * 所有接口返回统一格式：Result<T>
 */
@RestController
@RequestMapping("/PartBomLink")
public class PartBomLinkController {

    @Autowired
    private PartBOMLinkService partBOMLinkService;

    /**
     * 创建 BOM 链接
     *
     * @param dto 创建请求体，包含源、目标、租户等信息
     * @return Result<String> 操作结果，成功返回 "BOM链接创建成功"，失败返回错误信息
     */
    @PostMapping("/create")
    public Result<String> createBOMLink(@RequestBody PartBOMLinkCreateDTO dto) {
        return partBOMLinkService.createPartBOMLink(dto);
    }

    /**
     * 删除 BOM 链接
     *
     * @param request 请求体，包含要删除的 ID
     * @return Result<String> 操作结果，成功返回 "BOM链接删除成功"，失败返回错误信息
     */
    @PostMapping("/delete")
    public Result<String> deleteBOMLink(@RequestBody PartBomLinkDeleteRequest request) {
        Long id = request.getId();
        return partBOMLinkService.deletePartBOMLink(id);
    }

    /**
     * 更新 BOM 链接
     *
     * @param dto 包含更新数据的 DTO 对象
     * @return Result<String> 操作结果，成功返回 "BOM链接更新成功"，失败返回错误信息
     */
    @PostMapping("/update")
    public Result<String> updatePartBOMLink(@RequestBody PartBOMLinkUpdateDTO dto) {
        return partBOMLinkService.updatePartBOMLink(dto);
    }

    /**
     * 根据 sourceId 查询 PartBOMLink 列表
     *
     * @param request 包含 sourceId 的请求对象
     * @return Result<List<PartBOMLinkViewDTO>> 查询结果列表
     */
    @PostMapping("/query-by-source-id")
    public Result<List<PartBOMLinkViewDTO>> getPartBOMLinksBySourceId(@RequestBody SourceIdRequest request) {
        String sourceId = request.getSourceId();
        return partBOMLinkService.getPartBOMLinksBySourceId(sourceId);
    }

    /**
     * 根据 targetId 查询 PartBOMLink 列表
     *
     * @param request 包含 targetId 的请求对象
     * @return Result<List<PartBOMLinkViewDTO>> 查询结果列表
     */
    @PostMapping("/query-by-target-id")
    public Result<List<PartBOMLinkViewDTO>> getPartBOMLinksByTargetId(@RequestBody TargetIdRequest request) {
        String targetId = request.getTargetId();
        return partBOMLinkService.getPartBOMLinksByTargetId(targetId);
    }
}
