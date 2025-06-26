package com.idme.controller;

import com.huawei.innovation.rdm.minibom.dto.entity.PartCreateDTO;
import com.huawei.innovation.rdm.minibom.dto.entity.PartQueryViewDTO;
import com.huawei.innovation.rdm.minibom.dto.entity.PartUpdateDTO;
import com.huawei.innovation.rdm.minibom.dto.entity.PartViewDTO;

import com.idme.service.PartService;
import com.idme.controller.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 部件管理接口
 *
 * 提供部件的创建、删除、更新、查询等操作
 * 所有接口返回统一格式：Result<T>
 */
@RestController
@RequestMapping("/part")
public class PartController {

    @Autowired
    private PartService partService;

    /**
     * 创建新的部件
     *
     * @param dto 包含部件信息的 DTO 对象
     * @return Result<String> 操作结果，成功返回 "创建成功"，失败返回错误信息
     */
    @PostMapping("/create")
    public Result<String> createPart(@RequestBody PartCreateDTO dto) {
        return partService.createPart(dto);
    }

    /**
     * 完整更新流程：检出 → 更新 → 检入
     *
     * @param updateDTO 包含更新数据的 DTO 对象
     * @return Result<String> 操作结果，成功返回 "更新成功"，失败返回错误信息
     */
    @PostMapping("/updateWithCheckout")
    public Result<String> updateWithCheckout(@RequestBody PartUpdateDTO updateDTO) {
        return partService.updatePartWithCheckout(updateDTO);
    }

    /**
     * 删除指定部件
     *
     * @param payload 请求体，包含要删除的部件 ID
     * @return Result<String> 操作结果，成功返回 "部件删除成功"，失败返回错误信息
     */
    @PostMapping("/delete")
    public Result<String> deletePart(@RequestBody Map<String, String> payload) {
        String id = payload.get("masterId");
        return partService.deletePart(id);
    }

    /**
     * 根据名称关键字搜索部件
     *
     * @param keyword 查询关键字
     * @return Result<List<PartViewDTO>> 查询结果
     */
    @GetMapping("/query")
    public Result<List<PartViewDTO>> queryPartsByName(@RequestParam String keyword) {
        return partService.queryPartsByName(keyword);
    }

    /**
     * 删除指定部件的最新版本
     *
     * @param payload 请求体，包含 masterId
     * @return Result<String> 操作结果，成功返回 "最新版本删除成功"，失败返回错误信息
     */
    @PostMapping("/deleteLatestVersion")
    public Result<String> deleteLatestVersion(@RequestBody Map<String, String> payload) {
        String masterId = payload.get("masterId");
        return partService.deleteLatestVersion(masterId);
    }

    /**
     * 根据 ID 查询部件详情
     *
     * @param payload 请求体，包含部件 ID
     * @return Result<List<PartViewDTO>> 查询结果
     */
    @PostMapping("/getById")
    public Result<List<PartViewDTO>> getPartById(@RequestBody Map<String, String> payload) {
        String id = payload.get("id");
        return partService.getPartById(id);
    }

    /**
     * 获取系统中所有的部件
     *
     * @return Result<List<PartViewDTO>> 所有部件信息列表
     */
    @GetMapping("/getAll")
    public Result<List<PartViewDTO>> getAllParts() {
        return partService.getAllParts();
    }
    /**
     * 获取指定部件的所有版本
     *
     * @param masterId 部件的主 ID
     * @return Result<List<PartViewDTO>> 返回该部件所有版本的列表
     */
    @GetMapping("/getAllVersions")// "version": "A","iteration": 3,用这两个构建版本号
    public Result<List<PartQueryViewDTO>> getAllVersionsOfPart(@RequestParam String masterId) {
        return partService.getAllVersionsOfPart(masterId);
    }
    /**
     * 根据 masterId 查询部件详情
     *
     * @param masterId 部件主 ID
     * @return Result<List<PartViewDTO>> 查询结果
     */
    @PostMapping("/queryByMasterId")
    public Result<List<PartViewDTO>> getPartByMasterId(@RequestParam String masterId) {
        return partService.getPartByMasterId(masterId);
    }


}
