<template>
    <div class="partbom-container">
        <div class="search-section">
            <!-- 统一搜索界面 -->
            <div class="unified-search">
                <div class="title-with-logo">
                    <img src="@/assets/favicon.png" alt="GGBOM Logo" class="logo-img">
                    <h4>部件信息查询</h4>
                </div>
                <div class="search-options-horizontal">
                    <!-- 关键字搜索 -->
                    <div class="search-option-horizontal">
                        <div class="option-header">
                            <el-radio v-model="searchType" label="keyword" size="large">
                                关键字搜索
                            </el-radio>
                        </div>
                        <div class="option-content">
                            <el-input
                                v-model="keywordInput"
                                placeholder="请输入部件名称关键字"
                                clearable
                                @keyup.enter="handleUnifiedSearch"
                                :disabled="searchType !== 'keyword'"
                                class="search-input-compact"
                            >
                                <template #prefix>
                                    <el-icon><Search /></el-icon>
                                </template>
                            </el-input>
                        </div>
                    </div>
                    
                    <!-- 精确查询 -->
                    <div class="search-option-horizontal">
                        <div class="option-header">
                            <el-radio v-model="searchType" label="precise" size="large">
                                精确查询
                            </el-radio>
                        </div>
                        <div class="option-content">
                            <el-input 
                                v-model="codeInput" 
                                placeholder="请输入部件编码"
                                @keyup.enter="handleUnifiedSearch"
                                :disabled="searchType !== 'precise'"
                                class="search-input-compact"
                            >
                                <template #prefix>
                                    <el-icon><Key /></el-icon>
                                </template>
                            </el-input>
                        </div>
                    </div>
                </div>
                
                <div class="search-actions">
                    <el-button type="primary" class="search-btn" @click="handleUnifiedSearch">
                        <el-icon><Search /></el-icon>搜索
                    </el-button>
                    <el-button type="info" class="reset-btn" @click="handleUnifiedReset">
                        <el-icon><Refresh /></el-icon>重置
                    </el-button>
                </div>
            </div>
        </div> 

        <div class="content-section">
            <div class="action-bar">
                <el-button type="primary" @click="add=true" class="add-btn">
                    <el-icon><Plus /></el-icon>创建
                </el-button>

            </div>
            
            <div class="table-container">
                <el-table 
                    :data="tableData1" 
                    height="350" 
                    class="custom-table"
                    v-loading="loading"
                    element-loading-text="正在加载部件信息..."
                >
                    <el-table-column label="" width="50">
                        <template #default="scope">
                            {{ scope.$index + 1 }}
                        </template>
                    </el-table-column>
                    <el-table-column prop="ComponentCode" label="部件编码" width="250" />
                    <el-table-column prop="PartName" label="部件名称" width="250" />
                    <el-table-column prop="VersionNumber" label="版本号" width="250" />
                    <el-table-column prop="AssemblyMode" label="装配模式" width="250"/>
                    <el-table-column prop="ClassificationCode" label="分类码" width="250"/>
                    <el-table-column label="操作" fixed="right" width="120">
                        <template #default="scope">
                            <el-button type="primary" @click="handleEditPart(scope.row)" link>
                                <el-icon><Edit /></el-icon>
                            </el-button>
                            <el-button type="danger" @click="handleDeletePart(scope.row)" link>
                                <el-icon><Delete /></el-icon>
                            </el-button>
                        </template>
                    </el-table-column>
                </el-table>
            </div>
        </div>
        
        <el-dialog v-model="add" title="创建新部件" width="850" class="create-part-dialog">
            <div class="create-dialog-content">
                <!-- 基本信息卡片 -->
                <div class="info-card basic-info-card">
                    <div class="card-header">
                        <div class="header-icon">
                            <el-icon size="20"><Setting /></el-icon>
                        </div>
                        <h3 class="card-title">基本信息</h3>
                    </div>
                    <div class="card-content">
                        <el-form :model="form1" class="enhanced-form" label-position="top">
                            <div class="form-grid">
                                <el-form-item label="部件名称" class="form-item-enhanced">
                                    <el-input 
                                        v-model="form1.PartName" 
                                        placeholder="请输入部件名称"
                                        class="enhanced-input"
                                        prefix-icon="Box"
                                    />
                        </el-form-item>
                                <el-form-item label="默认单位" class="form-item-enhanced">
                                    <el-select 
                                        v-model="form1.DefaultUnit" 
                                        placeholder="请选择默认单位"
                                        class="enhanced-select"
                                    >
                                        <el-option 
                                            label="个" 
                                            value="个"
                                        >
                                            <div class="option-item">
                                                <span class="option-code">个</span>
                                                <span class="option-desc">个</span>
                                            </div>
                                        </el-option>
                                        <el-option 
                                            label="台" 
                                            value="台"
                                        >
                                            <div class="option-item">
                                                <span class="option-code">台</span>
                                                <span class="option-desc">台</span>
                                            </div>
                                        </el-option>
                                        <el-option 
                                            label="套" 
                                            value="套"
                                        >
                                            <div class="option-item">
                                                <span class="option-code">套</span>
                                                <span class="option-desc">套</span>
                                            </div>
                                        </el-option>
                                        <el-option 
                                            label="件" 
                                            value="件"
                                        >
                                            <div class="option-item">
                                                <span class="option-code">件</span>
                                                <span class="option-desc">件</span>
                                            </div>
                                        </el-option>
                                    </el-select>
                        </el-form-item>
                                <el-form-item label="所属仓库" class="form-item-enhanced">
                                    <el-select 
                                        v-model="form1.Source" 
                                        placeholder="请选择所属仓库" 
                                        class="enhanced-select"
                                    >
                                        <el-option 
                                            label="零件区" 
                                            value="1"
                                        >
                                            <div class="option-item">
                                                <span class="option-code">1</span>
                                                <span class="option-desc">零件区</span>
                                            </div>
                                        </el-option>
                                        <el-option 
                                            label="材料区" 
                                            value="2"
                                        >
                                            <div class="option-item">
                                                <span class="option-code">2</span>
                                                <span class="option-desc">材料区</span>
                                            </div>
                                        </el-option>
                                        <el-option 
                                            label="成品区" 
                                            value="3"
                                        >
                                            <div class="option-item">
                                                <span class="option-code">3</span>
                                                <span class="option-desc">成品区</span>
                                            </div>
                                        </el-option>
                                    </el-select>
                        </el-form-item>
                                <el-form-item label="装配模式" class="form-item-enhanced">
                                    <el-select 
                                        v-model="form1.AssemblyMode" 
                                        placeholder="请选择装配模式" 
                                        class="enhanced-select"
                                    >
                                        <el-option 
                                            label="SM - 安全模式" 
                                            value="SM"
                                        >
                                            <div class="option-item">
                                                <span class="option-code">SM</span>
                                                <span class="option-desc">安全模式</span>
                                            </div>
                                        </el-option>
                                        <el-option 
                                            label="NM - 一般模式" 
                                            value="NM"
                                        >
                                            <div class="option-item">
                                                <span class="option-code">NM</span>
                                                <span class="option-desc">一般模式</span>
                                            </div>
                                        </el-option>
                                        <el-option 
                                            label="QM - 快速模式" 
                                            value="QM"
                                        >
                                            <div class="option-item">
                                                <span class="option-code">QM</span>
                                                <span class="option-desc">快速模式</span>
                                            </div>
                                        </el-option>
                                    </el-select>
                        </el-form-item>
                            </div>
                            <div class="classification-section">
                                <el-form-item label="分类选择" class="form-item-enhanced classification-form-item">
                                    <div class="classification-wrapper">
                            <el-input
                                v-model="selectedClassificationName"
                                            placeholder="点击选择分类"
                                readonly
                                @click="showClassificationDialog = true"
                                            class="enhanced-input classification-input"
                            >
                                            <template #prefix>
                                                <el-icon><FolderOpened /></el-icon>
                                            </template>
                                <template #suffix>
                                                <el-icon class="classification-arrow">
                                        <ArrowDown />
                                    </el-icon>
                                </template>
                            </el-input>

                                    </div>
                        </el-form-item>
                    </div>
                </el-form>
                    </div>
                </div>

                <!-- 扩展属性卡片 -->
                <div class="info-card extended-info-card">
                    <div class="card-header">
                        <div class="header-icon">
                            <el-icon size="20"><Grid /></el-icon>
                        </div>
                        <h3 class="card-title">扩展属性</h3>
                    </div>
                    <div class="card-content">
                        <!-- 有扩展属性时显示 -->
                        <el-form 
                            :model="dynamicExtendedForm" 
                            v-show="isextend1 && classificationAttributes.length > 0" 
                            class="enhanced-form"
                            label-position="top"
                        >
                            <div class="attributes-header">
                                <div class="attributes-badge">
                                    <el-icon><Star /></el-icon>
                                    <span>共 {{ classificationAttributes.length }} 个属性</span>
                                </div>
                            </div>
                            <div class="attributes-grid">
                        <el-form-item 
                            v-for="attribute in classificationAttributes" 
                            :key="attribute.id"
                            :label="attribute.name"
                                    class="form-item-enhanced attribute-item"
                        >
                            <!-- 颜色属性使用下拉框 -->
                            <el-select 
                                v-if="attribute.name === '颜色' || attribute.name === 'color' || attribute.name === 'Color'"
                                v-model="dynamicExtendedForm[attribute.id]" 
                                :placeholder="`请选择${attribute.name}`"
                                class="enhanced-select"
                                clearable
                            >
                                <el-option label="白色" value="White" />
                                <el-option label="黑色" value="Black" />
                                <el-option label="灰色" value="Gray" />
                                <el-option label="金色" value="Gold" />
                                <el-option label="红色" value="Red" />
                                <el-option label="绿色" value="Green" />
                                <el-option label="紫色" value="Purple" />
                                <el-option label="蓝色" value="Blue" />
                                <el-option label="黄色" value="Yellow" />
                                <el-option label="橙色" value="Orange" />
                            </el-select>
                            
                            <!-- 屏幕类型属性使用下拉框 -->
                            <el-select 
                                v-else-if="attribute.name === '屏幕类型' || attribute.name === 'screen_type' || attribute.name === 'Screen Type' || attribute.name === 'screenType'"
                                v-model="dynamicExtendedForm[attribute.id]" 
                                :placeholder="`请选择${attribute.name}`"
                                class="enhanced-select"
                                clearable
                            >
                                <el-option label="液晶显示" value="LCD" />
                                <el-option label="有机发光二极管" value="OLED" />
                                <el-option label="微米级" value="MicroLED" />
                            </el-select>
                            
                            <!-- 防水等级属性使用下拉框 -->
                            <el-select 
                                v-else-if="attribute.name === '防水等级' || attribute.name === 'waterproof_level' || attribute.name === 'Waterproof Level' || attribute.name === 'waterproofLevel'"
                                v-model="dynamicExtendedForm[attribute.id]" 
                                :placeholder="`请选择${attribute.name}`"
                                class="enhanced-select"
                                clearable
                            >
                                <el-option label="IP68" value="IP68" />
                                <el-option label="IP67" value="IP67" />
                                <el-option label="IP64" value="IP64" />
                                <el-option label="IP53" value="IP53" />
                            </el-select>
                            
                            <!-- 其他属性仍使用输入框 -->
                            <div v-else class="attribute-input-wrapper">
                                <el-input 
                                    v-model="dynamicExtendedForm[attribute.id]" 
                                    :placeholder="`请输入${attribute.name}`"
                                    class="enhanced-input"
                                >
                                    <template #suffix v-if="attribute.unit">
                                        <span class="unit-suffix">{{ attribute.unit }}</span>
                                    </template>
                                </el-input>
                            </div>
                            
                            <div class="attribute-meta" v-if="attribute.description">
                                <el-text type="info" size="small">{{ attribute.description }}</el-text>
                            </div>
                        </el-form-item>
                    </div>
                </el-form>
                
                        <!-- 无扩展属性时显示 -->
                        <div v-show="isextend1 && classificationAttributes.length === 0" class="no-attributes-state">
                            <div class="empty-state">
                                <div class="empty-icon">
                                    <el-icon size="64" color="#d1d5db">
                                        <Document />
                                </el-icon>
                            </div>
                                <div class="empty-content">
                                    <h4>暂无扩展属性</h4>
                                    <p>当前分类没有关联的扩展属性，您可以直接创建部件</p>
                            </div>
                        </div>
                    </div>

                        <!-- 未选择分类时显示 -->
                        <div v-show="!isextend1" class="no-classification-state">
                            <div class="empty-state">
                                <div class="empty-icon">
                                    <el-icon size="64" color="#d1d5db">
                                        <FolderOpened />
                                    </el-icon>
                                </div>
                                <div class="empty-content">
                                    <h4>请先选择分类</h4>
                                    <p>选择分类后将显示相关的扩展属性配置</p>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <template #footer>
                <div class="enhanced-dialog-footer">
                    <el-button @click="handleCancelAdd" class="enhanced-cancel-btn">
                        <el-icon><Close /></el-icon>
                        取消
                    </el-button>
                    <el-button type="primary" @click="createPart" class="enhanced-confirm-btn" :loading="loading">
                        <el-icon v-if="!loading"><Check /></el-icon>
                        {{ loading ? '创建中...' : '创建部件' }}
                    </el-button>
                </div>
            </template>
        </el-dialog>

        <!-- 创建部件 - 分类选择对话框 -->
        <el-dialog 
            v-model="showClassificationDialog" 
            title="选择分类" 
            width="600px"
            class="custom-dialog"
        >
            <div class="classification-dialog-content">
                <div class="search-section">
                    <el-input
                        v-model="classificationSearchKeyword"
                        placeholder="搜索分类名称或分类码"
                        clearable
                        prefix-icon="Search"
                        @input="handleClassificationSearch"
                        class="search-input"
                    />
                </div>
                <div class="tree-section">
                    <el-tree
                        ref="classificationTreeRef"
                        :data="classificationTreeData"
                        :props="classificationTreeProps"
                        @node-click="handleClassificationNodeClick"
        
                        :load="loadClassificationNode"
                        lazy
                        :filter-node-method="filterClassificationNode"
                        class="classification-tree"
                    />
                </div>
                <div class="selected-info" v-if="tempSelectedClassification">
                    <el-text type="primary">
                        已选择：{{ tempSelectedClassification.name }} ({{ tempSelectedClassification.businessCode }})
                    </el-text>
                </div>
            </div>
            <template #footer>
                <div class="dialog-footer">
                    <el-button @click="cancelClassificationSelection" class="cancel-btn">取消</el-button>
                    <el-button type="primary" @click="confirmClassificationSelection" class="confirm-btn">确认</el-button>
                </div>
            </template>
        </el-dialog>

        <!-- 编辑部件 - 分类选择对话框 -->
        <el-dialog 
            v-model="showEditClassificationDialog" 
            title="选择分类" 
            width="600px"
            class="custom-dialog"
        >
            <div class="classification-dialog-content">
                <div class="search-section">
                    <el-input
                        v-model="editClassificationSearchKeyword"
                        placeholder="搜索分类名称或分类码"
                        clearable
                        prefix-icon="Search"
                        @input="handleEditClassificationSearch"
                        class="search-input"
                    />
                </div>
                <div class="tree-section">
                    <el-tree
                        ref="editClassificationTreeRef"
                        :data="editClassificationTreeData"
                        :props="classificationTreeProps"
                        @node-click="handleEditClassificationNodeClick"

                        :load="loadEditClassificationNode"
                        lazy
                        :filter-node-method="filterEditClassificationNode"
                        class="classification-tree"
                    />
                </div>
                <div class="selected-info" v-if="editTempSelectedClassification">
                    <el-text type="primary">
                        已选择：{{ editTempSelectedClassification.name }} ({{ editTempSelectedClassification.businessCode }})
                    </el-text>
                </div>
            </div>
            <template #footer>
                <div class="dialog-footer">
                    <el-button @click="cancelEditClassificationSelection" class="cancel-btn">取消</el-button>
                    <el-button type="primary" @click="confirmEditClassificationSelection" class="confirm-btn">确认</el-button>
                </div>
            </template>
        </el-dialog>

        <!-- 查看父项对话框 -->
        <el-dialog 
            v-model="showParentItemsDialog" 
            title="查看父项" 
            width="800px"
            class="parent-items-dialog"
            :close-on-click-modal="false"
            :destroy-on-close="true"
        >
            <div class="parent-items-content">
                <div class="current-part-info">
                    <h4>当前部件信息</h4>
                    <p v-if="currentEditingPart">
                        <strong>部件名称：</strong>{{ currentEditingPart.PartName }}
                        <strong style="margin-left: 20px;">部件编码：</strong>{{ currentEditingPart.ComponentCode }}
                    </p>
                </div>
                
                <div class="parent-parts-section">
                    <h4>父部件列表</h4>
                    <div class="parent-parts-table-container">
                        <el-table 
                            :data="parentParts" 
                            height="400" 
                            class="parent-parts-table"
                            v-loading="parentPartsLoading"
                            element-loading-text="正在加载父部件信息..."
                            empty-text="当前部件暂无父部件"
                        >
                            <el-table-column label="序号" width="60" align="center">
                                <template #default="scope">
                                    {{ scope.$index + 1 }}
                                </template>
                            </el-table-column>
                            <el-table-column prop="parentPartId" label="父部件ID" width="200" />
                            <el-table-column prop="parentPartName" label="父部件名称" width="200" />
                            <el-table-column prop="version" label="版本号" width="120" align="center">
                                <template #default="scope">
                                    <el-tag type="info" size="small">{{ scope.row.version }}</el-tag>
                                </template>
                            </el-table-column>
                            <el-table-column prop="assemblyMode" label="装配模式" width="120" align="center">
                                <template #default="scope">
                                    <el-tag type="success" size="small">{{ scope.row.assemblyMode }}</el-tag>
                                </template>
                            </el-table-column>
                        </el-table>
                    </div>
                </div>
                

            </div>
            

        </el-dialog>

        <!-- BOM树对话框 -->
        <el-dialog 
            v-model="showBomTreeDialog" 
            title="BOM清单树" 
            width="800px"
            class="bom-tree-dialog"
            :close-on-click-modal="false"
            :destroy-on-close="true"
        >
            <div class="bom-tree-content">
                <div class="current-part-info">
                    <h4>当前部件信息</h4>
                    <p v-if="currentEditingPart">
                        <strong>部件名称：</strong>{{ currentEditingPart.PartName }}
                        <strong style="margin-left: 20px;">部件编码：</strong>{{ currentEditingPart.ComponentCode }}
                    </p>
                </div>
                
                <div class="bom-tree-section">
                    <h4>BOM结构树</h4>
                    <div class="bom-tree-container">
                        <el-tree
                            ref="bomTreeRef"
                            :data="bomTreeData"
                            :props="bomTreeProps"
                            :load="loadBomTreeNode"
                            lazy
                            node-key="id"
                            :expand-on-click-node="false"
                            class="bom-tree"
                            v-loading="bomTreeLoading"
                            element-loading-text="正在加载BOM结构..."
                        >
                            <template #default="{ node, data }">
                                <div class="bom-tree-node">
                                    <el-icon v-if="data.isRoot" class="node-icon root-icon"><Box /></el-icon>
                                    <el-icon v-else class="node-icon child-icon"><Goods /></el-icon>
                                    <span class="node-label">{{ data.label }}</span>
                                    <span class="node-info" v-if="data.position || data.quantity">
                                        <el-tag size="small" type="info" v-if="data.position">{{ data.position }}</el-tag>
                                        <el-tag size="small" type="success" v-if="data.quantity">数量: {{ data.quantity }}</el-tag>
                                    </span>
                                </div>
                            </template>
                        </el-tree>
                    </div>
                </div>
                
                <div class="bom-tree-summary" v-if="bomTreeStats">
                    <el-row :gutter="20">
                        <el-col :span="8">
                            <el-statistic title="总节点数" :value="bomTreeStats.totalNodes" />
                        </el-col>
                        <el-col :span="8">
                            <el-statistic title="最大层级" :value="bomTreeStats.maxLevel" />
                        </el-col>
                        <el-col :span="8">
                            <el-statistic title="叶子节点" :value="bomTreeStats.leafNodes" />
                        </el-col>
                    </el-row>
                </div>
            </div>
            
            <template #footer>
                <div class="dialog-footer">
                    <el-button @click="refreshBomTree" :loading="bomTreeLoading" class="refresh-btn">
                        <el-icon><Refresh /></el-icon>刷新
                                </el-button>
                            </div>
            </template>
        </el-dialog>

        <!-- 修改BOM子项对话框 -->
        <el-dialog 
            v-model="showEditBomChildDialog" 
            title="修改BOM子项" 
            width="500px"
            class="edit-bom-child-dialog"
            :close-on-click-modal="false"
            :destroy-on-close="true"
        >
            <div class="edit-bom-child-content">
                <div class="child-part-info">
                    <h4>子部件信息</h4>
                    <p v-if="editingBomChild">
                        <strong>部件编码：</strong>{{ editingBomChild.childPartCode }}<br>
                        <strong>部件名称：</strong>{{ editingBomChild.childPartName }}
                    </p>
                            </div>
                
                <el-form :model="editBomChildForm" label-position="top" class="edit-bom-child-form">
                    <el-form-item label="部件位号" required>
                        <el-input 
                            v-model="editBomChildForm.position" 
                            placeholder="请输入部件位号（如：1-1）"
                            clearable
                        />
                        <div class="form-tip">
                            <el-text type="info" size="small">位号用于标识部件在BOM中的位置</el-text>
                        </div>
                    </el-form-item>
                    
                    <el-form-item label="数量" required>
                        <el-input-number 
                            v-model="editBomChildForm.quantity" 
                            :min="1"
                            :max="9999"
                            controls-position="right"
                            style="width: 100%;"
                        />
                        <div class="form-tip">
                            <el-text type="info" size="small">该子部件在父部件中的使用数量</el-text>
                        </div>
                    </el-form-item>
                </el-form>
            </div>
            
            <template #footer>
                <div class="dialog-footer">
                    <el-button @click="showEditBomChildDialog = false" class="cancel-btn">取消</el-button>
                    <el-button type="primary" @click="confirmUpdateBomChild" :loading="editBomChildLoading" class="confirm-btn">
                        <el-icon v-if="!editBomChildLoading"><Check /></el-icon>
                        {{ editBomChildLoading ? '保存中...' : '保存修改' }}
                    </el-button>
                </div>
            </template>
        </el-dialog>

        <!-- BOM关系配置对话框 -->
        <el-dialog 
            v-model="showBomConfigDialog" 
            title="📝 配置BOM关系" 
            width="500px"
            class="bom-config-dialog"
            :close-on-click-modal="false"
            :destroy-on-close="true"
        >
            <div class="bom-config-content">
                <div class="parts-info">
                    <div class="part-item parent-part">
                        <h4>父部件</h4>
                        <p v-if="currentEditingPart">
                            <strong>名称：</strong>{{ currentEditingPart.PartName }}<br>
                            <strong>编码：</strong>{{ currentEditingPart.ComponentCode }}
                        </p>
                            </div>
                    <div class="arrow-icon">
                        <el-icon size="24"><ArrowRight /></el-icon>
                            </div>
                    <div class="part-item child-part">
                        <h4>子部件</h4>
                        <p v-if="selectedChildPart">
                            <strong>名称：</strong>{{ selectedChildPart.PartName }}<br>
                            <strong>编码：</strong>{{ selectedChildPart.ComponentCode }}
                        </p>
                        </div>
                </div>
                
                <el-form :model="bomConfigForm" label-position="top" class="bom-config-form">
                    <el-form-item label="部件位号" required>
                        <el-input 
                            v-model="bomConfigForm.position" 
                            placeholder="请输入部件位号（如：1-1）"
                            clearable
                        />
                        <div class="form-tip">
                            <el-text type="info" size="small">位号用于标识部件在BOM中的位置</el-text>
                        </div>
                    </el-form-item>
                    
                    <el-form-item label="数量" required>
                        <el-input-number 
                            v-model="bomConfigForm.quantity" 
                            :min="1"
                            :max="9999"
                            controls-position="right"
                            style="width: 100%;"
                        />
                        <div class="form-tip">
                            <el-text type="info" size="small">该子部件在父部件中的使用数量</el-text>
                        </div>
                    </el-form-item>
                </el-form>
            </div>
            
            <template #footer>
                <div class="dialog-footer">
                    <el-button @click="showBomConfigDialog = false" class="cancel-btn">取消</el-button>
                    <el-button type="primary" @click="confirmCreateBomLink" :loading="bomConfigLoading" class="confirm-btn">
                        <el-icon v-if="!bomConfigLoading"><Check /></el-icon>
                        {{ bomConfigLoading ? '创建中...' : '确认创建' }}
                                </el-button>
                            </div>
            </template>
        </el-dialog>

        <!-- 新增子项选择对话框 -->
        <el-dialog 
            v-model="showAddBomItemDialog" 
            title="新增子项 - 选择部件" 
            width="1200px"
            class="add-bom-item-dialog"
            :close-on-click-modal="false"
            :destroy-on-close="true"
        >
            <div class="add-bom-dialog-content">
                <div class="dialog-header-info">
                    <div class="parent-part-info">
                        <h4>父部件信息</h4>
                        <p v-if="currentEditingPart">
                            <strong>部件名称：</strong>{{ currentEditingPart.PartName }}
                            <strong style="margin-left: 20px;">部件编码：</strong>{{ currentEditingPart.ComponentCode }}
                        </p>
                            </div>
                    <div class="search-section">
                        <el-input
                            v-model="bomSearchKeyword"
                            placeholder="搜索部件名称或编码"
                            clearable
                            prefix-icon="Search"
                            @input="handleBomSearch"
                            class="search-input"
                            style="width: 300px;"
                        />
                        <el-button type="primary" @click="loadAllPartsForBom" :loading="bomPartsLoading">
                            <el-icon><Refresh /></el-icon>刷新
                        </el-button>
                        </div>
                </div>
                
                <div class="parts-table-container">
                    <el-table 
                        :data="filteredBomParts" 
                        height="500" 
                        class="bom-parts-table"
                        v-loading="bomPartsLoading"
                        element-loading-text="正在加载部件信息..."
                    >
                        <el-table-column label="序号" width="60" align="center">
                            <template #default="scope">
                                {{ scope.$index + 1 }}
                            </template>
                        </el-table-column>
                        <el-table-column prop="ComponentCode" label="部件编码" width="200" />
                        <el-table-column prop="PartName" label="部件名称" width="200" />
                        <el-table-column prop="VersionNumber" label="版本号" width="120" align="center" />
                        <el-table-column prop="AssemblyMode" label="装配模式" width="120" align="center" />
                        <el-table-column prop="ClassificationCode" label="分类码" width="150" />
                        <el-table-column label="操作" width="120" align="center" fixed="right">
                            <template #default="scope">
                                <el-button 
                                    type="primary" 
                                    size="small" 
                                    @click="handleAddBomRelation(scope.row)"
                                    :disabled="scope.row.ComponentCode === currentEditingPart?.ComponentCode"
                                >
                                    <el-icon><Plus /></el-icon>新增子项
                                </el-button>
                            </template>
                        </el-table-column>
                    </el-table>
                            </div>
                

            </div>

        </el-dialog>

        <!-- 版本详情查看对话框 -->
        <el-dialog 
            v-model="showVersionDetailDialog" 
            title="版本详情" 
            width="800px"
            class="version-detail-dialog"
            :close-on-click-modal="false"
            :destroy-on-close="true"
        >
            <div class="version-detail-content" v-if="selectedVersionDetail">
                <el-descriptions :column="2" border size="default">
                    <el-descriptions-item label="版本信息">
                        <el-tag type="primary" size="large">{{ selectedVersionDetail.displayVersion }}</el-tag>
                    </el-descriptions-item>
                    <el-descriptions-item label="部件名称">
                        {{ selectedVersionDetail.name }}
                    </el-descriptions-item>
                    <el-descriptions-item label="装配模式">
                        {{ selectedVersionDetail.assemblyMode }}
                    </el-descriptions-item>
                    <el-descriptions-item label="状态">
                        <el-tag :type="getStateTagType(selectedVersionDetail.state)">
                            {{ getStateText(selectedVersionDetail.state) }}
                        </el-tag>
                    </el-descriptions-item>
                    <el-descriptions-item label="创建时间">
                        {{ formatDateTime(selectedVersionDetail.createTime) }}
                    </el-descriptions-item>
                    <el-descriptions-item label="创建者">
                        {{ selectedVersionDetail.creator }}
                    </el-descriptions-item>
                    <el-descriptions-item label="修改时间">
                        {{ formatDateTime(selectedVersionDetail.modifyTime) }}
                    </el-descriptions-item>
                    <el-descriptions-item label="修改者">
                        {{ selectedVersionDetail.modifier }}
                    </el-descriptions-item>
                </el-descriptions>
                
                <div class="version-attributes" v-if="selectedVersionDetail.clsAttrs && selectedVersionDetail.clsAttrs.length > 0">
                    <h4>扩展属性</h4>
                    <el-table :data="formatVersionAttributes(selectedVersionDetail.clsAttrs)" size="small">
                        <el-table-column prop="name" label="属性名称" width="150" />
                        <el-table-column prop="value" label="属性值" />
                    </el-table>
                </div>
                
            </div>
        </el-dialog>

        <el-dialog v-model="edit" title="修改部件" width="850" class="edit-part-dialog">
            <el-tabs type="border-card" class="enhanced-tabs" @tab-click="handleTabClick">
                <el-tab-pane label="基本属性">
                    <div class="edit-dialog-content">
                        <!-- 基本信息卡片 -->
                        <div class="info-card basic-info-card">
                            <div class="card-header">
                                <div class="header-icon">
                                    <el-icon size="20"><Setting /></el-icon>
                                </div>
                                <h3 class="card-title">基本信息</h3>
                            </div>
                            <div class="card-content">
                                <el-form :model="editForm" class="enhanced-form" label-position="top">
                                    <div class="form-grid">
                                        <el-form-item label="部件名称" class="form-item-enhanced">
                                            <el-input 
                                                v-model="editForm.PartName" 
                                                placeholder="请输入部件名称"
                                                class="enhanced-input"
                                                prefix-icon="Box"
                                            />
                                </el-form-item>
                                        <el-form-item label="默认单位" class="form-item-enhanced">
                                            <el-select 
                                                v-model="editForm.DefaultUnit" 
                                                placeholder="请选择默认单位"
                                                class="enhanced-select"
                                            >
                                                <el-option 
                                                    label="个" 
                                                    value="个"
                                                >
                                                    <div class="option-item">
                                                        <span class="option-code">个</span>
                                                        <span class="option-desc">个</span>
                                                    </div>
                                                </el-option>
                                                <el-option 
                                                    label="台" 
                                                    value="台"
                                                >
                                                    <div class="option-item">
                                                        <span class="option-code">台</span>
                                                        <span class="option-desc">台</span>
                                                    </div>
                                                </el-option>
                                                <el-option 
                                                    label="套" 
                                                    value="套"
                                                >
                                                    <div class="option-item">
                                                        <span class="option-code">套</span>
                                                        <span class="option-desc">套</span>
                                                    </div>
                                                </el-option>
                                                <el-option 
                                                    label="件" 
                                                    value="件"
                                                >
                                                    <div class="option-item">
                                                        <span class="option-code">件</span>
                                                        <span class="option-desc">件</span>
                                                    </div>
                                                </el-option>
                                            </el-select>
                                </el-form-item>
                                        <el-form-item label="所属仓库" class="form-item-enhanced">
                                            <el-select 
                                                v-model="editForm.Source" 
                                                placeholder="请选择所属仓库" 
                                                class="enhanced-select"
                                            >
                                                <el-option 
                                                    label="零件区" 
                                                    value="1"
                                                >
                                                    <div class="option-item">
                                                        <span class="option-code">1</span>
                                                        <span class="option-desc">零件区</span>
                                                    </div>
                                                </el-option>
                                                <el-option 
                                                    label="材料区" 
                                                    value="2"
                                                >
                                                    <div class="option-item">
                                                        <span class="option-code">2</span>
                                                        <span class="option-desc">材料区</span>
                                                    </div>
                                                </el-option>
                                                <el-option 
                                                    label="成品区" 
                                                    value="3"
                                                >
                                                    <div class="option-item">
                                                        <span class="option-code">3</span>
                                                        <span class="option-desc">成品区</span>
                                                    </div>
                                                </el-option>
                                            </el-select>
                                </el-form-item>
                                        <el-form-item label="装配模式" class="form-item-enhanced">
                                            <el-select 
                                                v-model="editForm.AssemblyMode" 
                                                placeholder="请选择装配模式" 
                                                class="enhanced-select"
                                            >
                                                <el-option 
                                                    label="SM - 安全模式" 
                                                    value="SM"
                                                >
                                                    <div class="option-item">
                                                        <span class="option-code">SM</span>
                                                        <span class="option-desc">安全模式</span>
                                                    </div>
                                                </el-option>
                                                <el-option 
                                                    label="NM - 一般模式" 
                                                    value="NM"
                                                >
                                                    <div class="option-item">
                                                        <span class="option-code">NM</span>
                                                        <span class="option-desc">一般模式</span>
                                                    </div>
                                                </el-option>
                                                <el-option 
                                                    label="QM - 快速模式" 
                                                    value="QM"
                                                >
                                                    <div class="option-item">
                                                        <span class="option-code">QM</span>
                                                        <span class="option-desc">快速模式</span>
                                                    </div>
                                                </el-option>
                                    </el-select>
                                </el-form-item>

                                    </div>
                                    <div class="classification-section">
                                        <el-form-item label="分类选择" class="form-item-enhanced classification-form-item">
                                            <div class="classification-wrapper">
                                                <el-input
                                                    v-model="editSelectedClassificationName"
                                                    placeholder="点击选择分类"
                                                    readonly
                                                    @click="showEditClassificationDialog = true"
                                                    class="enhanced-input classification-input"
                                                >
                                                    <template #prefix>
                                                        <el-icon><FolderOpened /></el-icon>
                                                    </template>
                                                    <template #suffix>
                                                        <el-icon class="classification-arrow">
                                                            <ArrowDown />
                                                        </el-icon>
                                                    </template>
                                                </el-input>

                                            </div>
                                </el-form-item>
                            </div>
                        </el-form>
                            </div>
                        </div>

                        <!-- 扩展属性卡片 -->
                        <div class="info-card extended-info-card">
                            <div class="card-header">
                                <div class="header-icon">
                                    <el-icon size="20"><Grid /></el-icon>
                                </div>
                                <h3 class="card-title">扩展属性</h3>
                            </div>
                            <div class="card-content">
                                <!-- 有扩展属性时显示 -->
                                <el-form 
                                    :model="editDynamicExtendedForm" 
                                    v-show="isextend2 && editClassificationAttributes.length > 0" 
                                    class="enhanced-form"
                                    label-position="top"
                                >

                                    <div class="attributes-grid">
                                        <el-form-item 
                                            v-for="attribute in editClassificationAttributes" 
                                            :key="attribute.id"
                                            :label="attribute.name"
                                            class="form-item-enhanced attribute-item"
                                        >
                                            <!-- 颜色属性使用下拉框 -->
                                            <el-select 
                                                v-if="attribute.name === '颜色' || attribute.name === 'color' || attribute.name === 'Color'"
                                                v-model="editDynamicExtendedForm[attribute.id]" 
                                                :placeholder="`请选择${attribute.name}`"
                                                class="enhanced-select"
                                                clearable
                                            >
                                                <el-option label="白色" value="White" />
                                                <el-option label="黑色" value="Black" />
                                                <el-option label="灰色" value="Gray" />
                                                <el-option label="金色" value="Gold" />
                                                <el-option label="红色" value="Red" />
                                                <el-option label="绿色" value="Green" />
                                                <el-option label="紫色" value="Purple" />
                                                <el-option label="蓝色" value="Blue" />
                                                <el-option label="黄色" value="Yellow" />
                                                <el-option label="橙色" value="Orange" />
                                            </el-select>
                                            
                                            <!-- 屏幕类型属性使用下拉框 -->
                                            <el-select 
                                                v-else-if="attribute.name === '屏幕类型' || attribute.name === 'screen_type' || attribute.name === 'Screen Type' || attribute.name === 'screenType'"
                                                v-model="editDynamicExtendedForm[attribute.id]" 
                                                :placeholder="`请选择${attribute.name}`"
                                                class="enhanced-select"
                                                clearable
                                            >
                                                <el-option label="液晶显示" value="LCD" />
                                                <el-option label="有机发光二极管" value="OLED" />
                                                <el-option label="微米级" value="MicroLED" />
                                            </el-select>
                                            
                                            <!-- 防水等级属性使用下拉框 -->
                                            <el-select 
                                                v-else-if="attribute.name === '防水等级' || attribute.name === 'waterproof_level' || attribute.name === 'Waterproof Level' || attribute.name === 'waterproofLevel'"
                                                v-model="editDynamicExtendedForm[attribute.id]" 
                                                :placeholder="`请选择${attribute.name}`"
                                                class="enhanced-select"
                                                clearable
                                            >
                                                <el-option label="IP68" value="IP68" />
                                                <el-option label="IP67" value="IP67" />
                                                <el-option label="IP64" value="IP64" />
                                                <el-option label="IP53" value="IP53" />
                                            </el-select>
                                            
                                            <!-- 其他属性仍使用输入框 -->
                                            <div v-else class="attribute-input-wrapper">
                                                <el-input 
                                                    v-model="editDynamicExtendedForm[attribute.id]" 
                                                    :placeholder="`请输入${attribute.name}`"
                                                    class="enhanced-input"
                                                >
                                                    <template #suffix v-if="attribute.unit">
                                                        <span class="unit-suffix">{{ attribute.unit }}</span>
                                                    </template>
                                                </el-input>
                                            </div>
                                            
                                            <div class="attribute-meta" v-if="attribute.description">
                                                <el-text type="info" size="small">{{ attribute.description }}</el-text>
                                            </div>
                                </el-form-item>
                            </div>
                        </el-form>
                                
                                <!-- 无扩展属性时显示 -->
                                <div v-show="isextend2 && editClassificationAttributes.length === 0" class="no-attributes-state">
                                    <div class="empty-state">
                                        <div class="empty-icon">
                                            <el-icon size="64" color="#d1d5db">
                                                <Document />
                                            </el-icon>
                                        </div>
                                        <div class="empty-content">
                                            <h4>暂无扩展属性</h4>
                                            <p>当前分类没有关联的扩展属性，您可以直接保存修改</p>
                                        </div>
                                    </div>
                                </div>

                                <!-- 未选择分类时显示 -->
                                <div v-show="!isextend2" class="no-classification-state">
                                    <div class="empty-state">
                                        <div class="empty-icon">
                                            <el-icon size="64" color="#d1d5db">
                                                <FolderOpened />
                                            </el-icon>
                                        </div>
                                        <div class="empty-content">
                                            <h4>请先选择分类</h4>
                                            <p>选择分类后将显示相关的扩展属性配置</p>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div>
                        <div class="enhanced-dialog-footer">
                            <el-button @click="handleCancelEdit" class="enhanced-cancel-btn">
                            <el-icon><Close /></el-icon>
                            取消
                            </el-button>

                        <el-button type="primary" @click="updatePart" class="enhanced-confirm-btn" :loading="loading">
                            <el-icon v-if="!loading"><Check /></el-icon>
                                {{ loading ? '保存中...' : '保存修改' }}
                            </el-button>
                        </div>
                    </div>
                </el-tab-pane>

                <el-tab-pane label="版本管理">
                    <div class="version-management-content">
                        <div class="version-header">
                            <div class="version-info">
                                <h4>部件版本历史</h4>
                                <p v-if="currentEditingPart">
                                    <strong>部件名称：</strong>{{ currentEditingPart.PartName }}
                                    <strong style="margin-left: 20px;">Master ID：</strong>{{ currentEditingPart.masterId }}
                                </p>
                            </div>
                            <div class="version-actions">
                                <el-button type="primary" @click="loadPartVersions" :loading="versionsLoading">
                                    <el-icon><Refresh /></el-icon>刷新版本
                                </el-button>
                                <el-button 
                                    type="danger" 
                                    @click="deleteLatestVersion" 
                                    :loading="deleteVersionLoading"
                                    :disabled="!currentEditingPart || partVersions.length === 0"
                                >
                                    <el-icon><Delete /></el-icon>删除最新版本
                                </el-button>
                            </div>
                        </div>
                        
                        <div class="version-table-container">
                            <el-table 
                                :data="partVersions" 
                                v-loading="versionsLoading"
                                element-loading-text="正在加载版本信息..."
                                class="version-table"
                            >
                                <el-table-column label="序号" width="60" align="center">
                                    <template #default="scope">
                                        {{ scope.$index + 1 }}
                                    </template>
                                </el-table-column>
                                <el-table-column prop="version" label="大版本" width="80" align="center">
                                    <template #default="scope">
                                        <el-tag :type="scope.row.isLatestMajor ? 'success' : 'info'">
                                            {{ scope.row.version }}
                                        </el-tag>
                                    </template>
                                </el-table-column>
                                <el-table-column prop="iteration" label="小版本" width="80" align="center">
                                    <template #default="scope">
                                        <el-tag :type="scope.row.isLatestMinor ? 'warning' : 'info'" size="small">
                                            {{ scope.row.iteration || 0 }}
                                        </el-tag>
                                    </template>
                                </el-table-column>
                                <el-table-column prop="displayVersion" label="完整版本" width="120" align="center">
                                    <template #default="scope">
                                        <strong>{{ scope.row.displayVersion }}</strong>
                                    </template>
                                </el-table-column>
                                <el-table-column prop="name" label="部件名称" min-width="150" />
                                <el-table-column prop="assemblyMode" label="装配模式" width="100" align="center" />
                                <el-table-column prop="state" label="状态" width="100" align="center">
                                    <template #default="scope">
                                        <el-tag :type="getStateTagType(scope.row.state)" size="small">
                                            {{ getStateText(scope.row.state) }}
                                        </el-tag>
                                    </template>
                                </el-table-column>
                                <el-table-column prop="createTime" label="创建时间" width="180" align="center">
                                    <template #default="scope">
                                        {{ formatDateTime(scope.row.createTime) }}
                                    </template>
                                </el-table-column>
                                <el-table-column prop="creator" label="创建者" width="120" align="center" />
                                <el-table-column label="操作" width="100" align="center" fixed="right">
                                    <template #default="scope">
                                        <el-button 
                                            type="primary" 
                                            size="small" 
                                            @click.stop="viewVersionDetail(scope.row)"
                                            link
                                        >
                                            <el-icon><View /></el-icon>查看
                                        </el-button>
                                    </template>
                                </el-table-column>
                            </el-table>
                        </div>
                        
                        <div class="version-summary" v-if="partVersions.length > 0">
                            <el-row :gutter="20">
                                <el-col :span="6">
                                    <el-statistic title="总版本数" :value="partVersions.length" />
                                </el-col>
                                <el-col :span="6">
                                    <el-statistic title="大版本数" :value="majorVersionCount" />
                                </el-col>
                                <el-col :span="6">
                                    <el-statistic title="最新大版本" :value="latestMajorVersion" />
                                </el-col>
                                <el-col :span="6">
                                    <el-statistic title="最新小版本" :value="latestMinorVersion" />
                                </el-col>
                            </el-row>
                        </div>
                    </div>
                </el-tab-pane>

                <el-tab-pane label="BOM清单">
                    <div class="bom-management-content">
                        <div class="bom-header">
                            <div class="bom-info">
                                <h4>BOM清单管理</h4>
                                <p v-if="currentEditingPart">
                                    <strong>部件名称：</strong>{{ currentEditingPart.PartName }}
                                    <strong style="margin-left: 20px;">部件编码：</strong>{{ currentEditingPart.ComponentCode }}
                                </p>
                            </div>
                    <div class="bom-actions">
                                <el-button type="primary" @click="handleAddBomItem" :loading="bomLoading">
                            <el-icon><Plus /></el-icon>新增子项
                        </el-button>
                                <el-button type="success" @click="handleViewBomTree" :loading="bomTreeLoading">
                                    <el-icon><List /></el-icon>查看BOM清单
                        </el-button>
                                <el-button type="info" @click="loadBomChildParts" :loading="bomChildPartsLoading">
                                    <el-icon><Refresh /></el-icon>刷新BOM清单
                                </el-button>
                                <el-button type="warning" @click="handleViewParentItems" :loading="bomLoading">
                                    <el-icon><Top /></el-icon>查看父项
                        </el-button>
                            </div>
                    </div>

                        <div class="bom-content">
                            <div class="bom-child-parts">
                                <h4>子部件清单</h4>
                                <div class="child-parts-table-container">
                                    <el-table 
                                        :data="bomChildParts" 
                                        height="400" 
                                        class="child-parts-table"
                                        v-loading="bomChildPartsLoading"
                                        element-loading-text="正在加载子部件信息..."
                                        empty-text="当前部件暂无子部件"
                                    >
                                        <el-table-column label="序号" width="60" align="center">
                            <template #default="scope">
                                {{ scope.$index + 1 }}
                            </template>
                        </el-table-column>
                                        <el-table-column prop="childPartCode" label="子部件编码" width="200" />
                                        <el-table-column prop="childPartName" label="子部件名称" width="200" />
                                        <el-table-column prop="position" label="位号" width="120" align="center" />
                                        <el-table-column prop="quantity" label="数量" width="100" align="center" />
                                        <el-table-column prop="versionNumber" label="版本号" width="120" align="center" />
                                        <el-table-column label="操作" width="160" align="center" fixed="right">
                            <template #default="scope">
                                                <el-button 
                                                    type="primary" 
                                                    size="small" 
                                                    @click="handleEditBomChild(scope.row)"
                                                    link
                                                >
                                                    <el-icon><Edit /></el-icon>修改
                                </el-button>
                                                <el-button 
                                                    type="danger" 
                                                    size="small" 
                                                    @click="handleDeleteBomChild(scope.row)"
                                                    link
                                                >
                                                    <el-icon><Delete /></el-icon>删除
                                </el-button>
                            </template>
                        </el-table-column>
                    </el-table>
                        </div>
                            </div>
                            
                            <div class="bom-summary" v-if="currentEditingPart">
                                <el-row :gutter="20">
                                    <el-col :span="8">
                                        <el-statistic title="子部件数量" :value="bomChildParts.length" />
                                    </el-col>
                                    <el-col :span="8">
                                        <el-statistic title="父部件数量" :value="bomParentCount" />
                                    </el-col>
                                    <el-col :span="8">
                                        <el-statistic title="BOM层级" :value="bomLevelCount" />
                                    </el-col>
                                </el-row>
                            </div>
                            </div>
                        </div>
                </el-tab-pane>


            </el-tabs>

        </el-dialog>
    </div>
</template>

//导入api函数、第三方库、图标、消息提示组件、对话框组件
<script>
import { reactive, ref, watch, onMounted, computed } from 'vue';
import axios from 'axios';
import { InfoFilled, ArrowDown, Key, Setting, Grid, Star, Document, FolderOpened, Close, Check, Box, Goods, Location, View, Refresh, Delete, Plus, List, Top, ArrowRight, Edit } from '@element-plus/icons-vue';
import { ElMessage, ElMessageBox } from 'element-plus';

//注册图表组件为当前组件的子组件，无需再动态引入
export default{
    components: {
        InfoFilled,
        ArrowDown,
        Key,
        Setting,
        Grid,
        Star,
        Document,
        FolderOpened,
        Close,
        Check,
        Box,
        Goods,
        Location,
        Plus,
        List,
        Top,
        ArrowRight,
        Edit
    },

    //Composition API 的入口函数，在组件创建之前执行。用于定义组件的：响应式数据（替代 data()）方法（替代 methods）计算属性（替代 computed）生命周期钩子（替代 created、mounted 等）
    setup(){
        // 统一搜索相关变量
        let searchType=ref('keyword') // 'keyword' 或 'precise'
        let codeInput=ref('')
        let keywordInput=ref('')
        
        let add=ref(false)
        let edit=ref(false)
        
        // 将tableData1改为响应式数据
        let tableData1=ref([])
        let loading=ref(false)
        
        // 调用后端 API (/api/part/create) 创建一个新的部件
        // 创建part的函数
        // 该函数在此时触发<el-button @click="createPart">提交</el-button>
        const createPart = async () => {
            try {
                // 表单验证
                if (!form1.PartName.trim()) {
                    ElMessage.error('请输入部件名称');
                    return;
                }
                
                if (!form1.AssemblyMode.trim()) {
                    ElMessage.error('请输入装配模式');
                    return;
                }
                
                if (!form1.Classification) {
                    ElMessage.error('请选择分类');
                    return;
                }
                
                loading.value = true;
                
                // 构建分类属性DPA对象
                const dpaObj = {};
                const needSetNullAttrsForClsAttrs = [];
                
                if (classificationAttributes.value.length > 0) {
                    classificationAttributes.value.forEach(attr => {
                        const attrValue = dynamicExtendedForm.value[attr.id];
                        const englishName = attr.nameEn || attr.name;
                        
                        if (attr.id && englishName) {
                            if (attrValue && attrValue.toString().trim()) {
                                dpaObj[englishName] = attrValue.toString().trim();
                            }
                            needSetNullAttrsForClsAttrs.push(`clsAttrs.DPA.${englishName}`);
                        }
                    });
                }
                
                // 构建完整的请求参数
                const requestData = {
                    name: form1.PartName.trim(),
                    description: form1.DefaultUnit || "",
                    assemblyMode: form1.AssemblyMode.trim(),
                    securityLevel: "internal",
                    clsAttrs: [{"DPA": dpaObj}],
                    extAttrs: [{
                        name: "DPA",
                        type: selectedClassificationInfo.value?.type || "CATEGORY",
                        value: form1.Classification.toString()
                    }],
                    warehouseInfo: {
                        id: form1.Source || "1",
                        clazz: "Warehouse",
                        name: getWarehouseName(form1.Source)
                    },
                    tenant: {
                        id: "-1",
                        clazz: "Tenant",
                        name: "basicTenant"
                    },
                    branch: {
                        tenant: {
                            name: "basicTenant",
                            id: "-1",
                            clazz: "Tenant"
                        },
                        needSetNullAttrs: ["modifier", "creator", "rdmExtensionType", "id"]
                    },
                    master: {
                        tenant: {
                            name: "basicTenant",
                            id: "-1",
                            clazz: "Tenant"
                        },
                        needSetNullAttrs: ["modifier", "rdmExtensionType", "creator", "id"]
                    },
                    creator: "hid_vv7b-0o0-beyfum 069a5a78b6a742c8b48d74377083df17",
                    modifier: "hid_vv7b-0o0-beyfum 069a5a78b6a742c8b48d74377083df17",
                    needSetNullAttrs: [
                        "kiaguid", "branch", "master", "modifier", "rdmExtensionType",
                        "workingState", "creator", "checkOutTime", "checkOutUserName", 
                        "preVersionId", "description", "id",
                        ...needSetNullAttrsForClsAttrs
                    ]
                };
                
                const response = await axios.post('/api/part/create', requestData);
                
                if (response.data.code === '200' || response.data.code === 200) {
                    ElMessage.success(response.data.data || response.data.message || '部件创建成功');
                    add.value = false;
                    resetForm();
                    await fetchPartData();
                } else {
                    const errorMessage = response.data.message || response.data.data || '部件创建失败';
                    ElMessage.error(errorMessage);
                }
                
            } catch (error) {
                ElMessage.error('操作失败');
            } finally {
                loading.value = false;
            }
        };
        
        // 获取仓库名称的辅助函数
        const getWarehouseName = (warehouseId) => {
            const warehouseMap = {
                '1': '零件区',
                '2': '材料区', 
                '3': '成品区'
            };
            return warehouseMap[warehouseId] || '零件区';
        };
        
        // 重置表单的函数
        const resetForm = () => {
            // 重置基本表单
            form1.PartName = '';
            form1.DefaultUnit = '';
            form1.Source = '';
            form1.AssemblyMode = '';
            form1.Classification = '';
            
            // 重置分类选择
            selectedClassificationName.value = '';
            selectedClassificationInfo.value = null;
            tempSelectedClassification.value = null;
            
            // 重置扩展属性
            classificationAttributes.value = [];
            dynamicExtendedForm.value = {};
            isextend1.value = false;
        };
        
        // 处理取消添加操作
        const handleCancelAdd = () => {
            add.value = false;
            resetForm();
        };
        
        // 处理编辑部件
        const handleEditPart = async (row) => {
            try {
                loading.value = true;
                currentEditingPart.value = row;
                
                const response = await axios.post('/api/part/getById', { id: row.ComponentCode });
                
                if (response.data.code === '200' && response.data.data && response.data.data.length > 0) {
                    const partDetail = response.data.data[0];
                    currentEditingPart.value = { ...row, _fullDetail: partDetail };
                    
                    editForm.PartName = partDetail.name || '';
                    editForm.DefaultUnit = partDetail.description || partDetail.defaultUnit || '';
                    editForm.Source = (partDetail.warehouseInfo && partDetail.warehouseInfo.id) || '';
                    editForm.AssemblyMode = partDetail.assemblyMode || '';
                    
                    if (partDetail.extAttrs && partDetail.extAttrs.length > 0) {
                        const dpaAttr = partDetail.extAttrs.find(attr => attr.name === 'DPA');
                        if (dpaAttr && dpaAttr.value) {
                            editForm.Classification = dpaAttr.value.id || dpaAttr.value;
                            editSelectedClassificationName.value = `${dpaAttr.value.name || ''} (${dpaAttr.value.businessCode || ''})`;
                            editSelectedClassificationInfo.value = dpaAttr.value;
                            
                            await loadEditClassificationAttributes(editForm.Classification);
                            
                            if (partDetail.clsAttrs && partDetail.clsAttrs.length > 0) {
                                const dpaObj = partDetail.clsAttrs[0].DPA;
                                if (dpaObj) {
                                    editClassificationAttributes.value.forEach(attr => {
                                        const englishName = attr.nameEn || attr.name;
                                        if (dpaObj[englishName] !== undefined) {
                                            let displayValue = dpaObj[englishName];
                                            
                                            // 对特定属性进行特殊处理，提取cnName用于表单显示
                                            if (englishName === 'ScreenType' || englishName === 'Color' || englishName === 'WaterproofingGrade') {
                                                try {
                                                    // 如果值是对象且包含cnName，则使用cnName
                                                    if (typeof displayValue === 'object' && displayValue !== null && displayValue.cnName) {
                                                        displayValue = displayValue.cnName;
                                                    }
                                                    // 如果值是字符串但看起来像JSON，尝试解析
                                                    else if (typeof displayValue === 'string' && displayValue.includes('"cnName"')) {
                                                        const parsedValue = JSON.parse(displayValue);
                                                        if (parsedValue.cnName) {
                                                            displayValue = parsedValue.cnName;
                                                        }
                                                    }
                                                    
                                                    // 如果是颜色属性，且值是中文，需要转换为英文值
                                                    if (englishName === 'Color' && typeof displayValue === 'string') {
                                                        const colorMapping = {
                                                            '白色': 'White',
                                                            '黑色': 'Black', 
                                                            '灰色': 'Gray',
                                                            '金色': 'Gold',
                                                            '红色': 'Red',
                                                            '绿色': 'Green',
                                                            '紫色': 'Purple',
                                                            '蓝色': 'Blue',
                                                            '黄色': 'Yellow',
                                                            '橙色': 'Orange'
                                                        };
                                                        
                                                        if (colorMapping[displayValue]) {
                                                            displayValue = colorMapping[displayValue];
                                                        }
                                                    }
                                                    
                                                    // 如果是屏幕类型属性，且值是中文，需要转换为英文值
                                                    if (englishName === 'ScreenType' && typeof displayValue === 'string') {
                                                        const screenTypeMapping = {
                                                            '液晶显示': 'LCD',
                                                            '有机发光二极管': 'OLED',
                                                            '微米级': 'MicroLED'
                                                        };
                                                        
                                                        if (screenTypeMapping[displayValue]) {
                                                            displayValue = screenTypeMapping[displayValue];
                                                        }
                                                    }
                                                    
                                                    // 如果是防水等级属性，保持原值（已经是英文）
                                                    
                                                } catch (error) {
                                                    // 解析失败时保持原值
                                                    console.warn(`解析属性值失败: ${englishName}`, error);
                                                }
                                            }
                                            
                                            editDynamicExtendedForm.value[attr.id] = displayValue;
                                        }
                                    });
                                }
                            }
                        }
                    }
                    
                    partVersions.value = [];
                    selectedVersionDetail.value = null;
                    showVersionDetailDialog.value = false;
                    bomChildParts.value = [];
                    edit.value = true;
                    
                    if (partDetail.master && partDetail.master.id) {
                        currentEditingPart.value.masterId = partDetail.master.id;
                        await loadPartVersions();
                    }
                } else {
                    ElMessage.error('获取部件详细信息失败');
                }
            } catch (error) {
                ElMessage.error('操作失败');
            } finally {
                loading.value = false;
            }
        };
        
        // 处理取消编辑操作
        const handleCancelEdit = () => {
            edit.value = false;
            resetEditForm();
        };
        
        // 重置编辑表单
        const resetEditForm = () => {
            editForm.PartName = '';
            editForm.DefaultUnit = '';
            editForm.Source = '';
            editForm.AssemblyMode = '';
            editForm.Classification = '';
            
            editSelectedClassificationName.value = '';
            editSelectedClassificationInfo.value = null;
            editTempSelectedClassification.value = null;
            currentEditingPart.value = null;
            
            editClassificationAttributes.value = [];
            editDynamicExtendedForm.value = {};
            isextend2.value = false;
            
            // 清理BOM相关数据
            bomChildParts.value = [];
            partVersions.value = [];
            selectedVersionDetail.value = null;
        };
        
        // 加载编辑时的分类属性
        const loadEditClassificationAttributes = async (classificationId) => {
            try {

                
                // 获取父级属性
                const parentResponse = await axios.get(`/api/EXADefinitionLink/getParentAttributesByClassification?classificationNodeId=${classificationId}`);
                let attributes = [];
                
                if (parentResponse.data.data) {
                    attributes = [...attributes, ...parentResponse.data.data.map(item => item.source)];
                }
                
                // 获取直接关联的属性
                const directResponse = await axios.get(`/api/EXADefinitionLink/listByClassification?classificationNodeId=${classificationId}`);
                
                if (directResponse.data.data) {
                    attributes = [...attributes, ...directResponse.data.data.map(item => item.source)];
                }
                
                // 去重（基于属性ID）
                const uniqueAttributes = attributes.filter((attr, index, self) => 
                    index === self.findIndex(a => a.id === attr.id)
                );
                
                // 为每个属性查询计量单位
                const attributesWithUnits = await Promise.all(
                    uniqueAttributes.map(async (attr) => {
                        const unit = await getAttributeUnit(attr.name);
                        return {
                            ...attr,
                            unit: unit // 添加计量单位字段
                        };
                    })
                );
                
                editClassificationAttributes.value = attributesWithUnits;
                
                // 初始化动态表单
                const formData = {};
                attributesWithUnits.forEach(attr => {
                    formData[attr.id] = '';
                });
                editDynamicExtendedForm.value = formData;
                
                // 显示扩展属性部分
                isextend2.value = true;
                
            } catch (error) {
                editClassificationAttributes.value = [];
                editDynamicExtendedForm.value = {};
            }
        };
        
        // 更新部件
        const updatePart = async () => {
            try {
                // 表单验证
                if (!editForm.PartName.trim()) {
                    ElMessage.error('请输入部件名称');
                    return;
                }
                
                if (!editForm.AssemblyMode.trim()) {
                    ElMessage.error('请选择装配模式');
                    return;
                }
                
                if (!currentEditingPart.value) {
                    ElMessage.error('无法获取部件信息');
                    return;
                }
                
                // 显示加载状态
                loading.value = true;
                
                // 构建分类属性DPA对象
                const dpaObj = {};
                
                if (editClassificationAttributes.value.length > 0) {
                    editClassificationAttributes.value.forEach(attr => {
                        const attrValue = editDynamicExtendedForm.value[attr.id];
                        // 获取属性的英文名称，优先使用nameEn，如果没有则使用name
                        const englishName = attr.nameEn || attr.name;
                        
                        if (attr.id && englishName) {
                            if (attrValue && attrValue.toString().trim()) {
                                dpaObj[englishName] = attrValue.toString().trim();
                            } else {
                                dpaObj[englishName] = "";
                            }
                        }
                    });
                }
                
                // 获取部件详细信息
                const partDetail = currentEditingPart.value._fullDetail;
                if (!partDetail) {
                    ElMessage.error('缺少部件详细信息，请重新选择编辑');
                    loading.value = false;
                    return;
                }
                
                // 按照新的JSON格式构建更新请求参数
                const updateData = {
                    id: currentEditingPart.value.ComponentCode,
                    name: editForm.PartName.trim(),
                    description: editForm.DefaultUnit || "",
                    assemblyMode: editForm.AssemblyMode.trim(),
                    checkOutUserName: "hid_vv7b-0o0-beyfum 069a5a78b6a742c8b48d74377083df17",
                    
                    // 分类信息 - 简化格式
                    extAttrs: editForm.Classification ? [{
                        name: "DPA",
                        value: editForm.Classification.toString()
                    }] : [],
                    
                    // 分类属性 - 保持原格式
                    clsAttrs: [{
                        "DPA": dpaObj
                    }],
                    
                    // 仓库信息 - 简化格式
                    warehouseInfo: {
                        id: editForm.Source || "1",
                        clazz: "Warehouse"
                    },
                    
                    // Master信息 - 使用真实的master.id
                    master: {
                        tenant: {
                            name: "basicTenant",
                        id: "-1",
                            clazz: "Tenant"
                        },
                        modifier: "hid_vv7b-0o0-beyfum 069a5a78b6a742c8b48d74377083df17",
                        rdmExtensionType: "PartMaster",
                        creator: "hid_vv7b-0o0-beyfum 069a5a78b6a742c8b48d74377083df17",
                        id: partDetail.master && partDetail.master.id ? partDetail.master.id : "",
                        clazz: "PartMaster",
                        name: ""
                    }
                };
                
                const response = await axios.post('/api/part/updateWithCheckout', updateData);
                        
                        if (response.data.code === '200') {
                            ElMessage.success('部件修改成功！');
                            
                            // 关闭编辑对话框
                            edit.value = false;
                            
                            // 重置表单
                            resetEditForm();
                            
                            // 刷新数据
                            await fetchPartData();
                        } else {
                    ElMessage.error(response.data.message || '部件修改失败');
                        }
                        
                    } catch (error) {
                ElMessage.error('操作失败');
            } finally {
                loading.value = false;
            }
        };
        
        // 版本管理相关方法
        // 加载部件所有版本
        const loadPartVersions = async () => {
            if (!currentEditingPart.value || !currentEditingPart.value.masterId) {
                ElMessage.error('无法获取部件Master ID');
                return;
            }
            
            try {
                versionsLoading.value = true;
                const response = await axios.get('/api/part/getAll');
                
                if (response.data.code === '200' && response.data.data) {
                    const allVersions = response.data.data.filter(part => 
                        part.master && part.master.id === currentEditingPart.value.masterId
                    );
                    
                    const processedVersions = allVersions.map(version => {
                        const iteration = getIterationNumber(version);
                        const displayVersion = iteration > 0 ? `${version.version}-${iteration}` : version.version;
                        
                        return {
                            ...version,
                            iteration: iteration,
                            displayVersion: displayVersion,
                            createTime: version.createTime || version.creationTime,
                            modifyTime: version.modifyTime || version.modificationTime,
                            creator: version.creator || version.creatorName || '未知',
                            modifier: version.modifier || version.modifierName || '未知',
                            state: version.state || version.workingState || 'UNKNOWN'
                        };
                    });
                    
                    processedVersions.sort((a, b) => {
                        if (a.version !== b.version) {
                            return a.version.localeCompare(b.version);
                        }
                        return (a.iteration || 0) - (b.iteration || 0);
                    });
                    
                    markLatestVersions(processedVersions);
                    partVersions.value = processedVersions;
                    ElMessage.success(`加载到 ${partVersions.value.length} 个版本`);
                } else {
                    ElMessage.error('获取版本数据失败');
                    partVersions.value = [];
                }
            } catch (error) {
                ElMessage.error('操作失败');
                partVersions.value = [];
            } finally {
                versionsLoading.value = false;
            }
        };
        
        // 标记最新版本
        const markLatestVersions = (versions) => {
            // 找出最新的大版本
            const latestMajor = versions.reduce((latest, current) => {
                return current.version > latest ? current.version : latest;
            }, '');
            
            // 按大版本分组，找出每个大版本的最新小版本
            const versionGroups = {};
            versions.forEach(version => {
                if (!versionGroups[version.version]) {
                    versionGroups[version.version] = [];
                }
                versionGroups[version.version].push(version);
            });
            
            // 标记最新版本
            Object.keys(versionGroups).forEach(majorVersion => {
                const groupVersions = versionGroups[majorVersion];
                const maxIteration = Math.max(...groupVersions.map(v => v.iteration || 0));
                
                groupVersions.forEach(version => {
                    version.isLatestMajor = version.version === latestMajor;
                    version.isLatestMinor = version.iteration === maxIteration;
                });
            });
        };
        
        // 删除最新版本
        const deleteLatestVersion = async () => {
            if (!currentEditingPart.value) {
                ElMessage.error('请先选择要删除版本的部件');
                return;
            }
            
            let masterId = null;
            if (currentEditingPart.value._fullDetail && currentEditingPart.value._fullDetail.master) {
                masterId = currentEditingPart.value._fullDetail.master.id;
            } else if (currentEditingPart.value.masterId) {
                masterId = currentEditingPart.value.masterId;
            }
            
            if (!masterId) {
                ElMessage.error('无法获取部件的Master ID，无法删除版本');
                return;
            }
            
            try {
                await ElMessageBox.confirm(
                    `确定要删除部件"${currentEditingPart.value.PartName || currentEditingPart.value.name}"的最新版本吗？此操作不可撤销！`,
                    '删除确认',
                    {
                        confirmButtonText: '确定删除',
                        cancelButtonText: '取消',
                        type: 'warning',
                        confirmButtonClass: 'el-button--danger'
                    }
                );
                
                deleteVersionLoading.value = true;
                const response = await axios.post('/api/part/deleteLatestVersion', {
                    masterId: masterId
                });
                
                if (response.data.code === '200' || response.data.code === 200) {
                    ElMessage.success(response.data.data || response.data.message || '最新版本删除成功');
                    await loadPartVersions();
                    
                    if (partVersions.value.length === 0) {
                        ElMessage.info('该部件已无版本记录');
                        await fetchPartData();
                    }
                } else {
                    const errorMessage = response.data.message || response.data.data || '删除最新版本失败';
                    ElMessage.error(errorMessage);
                }
            } catch (error) {
                if (error.message && error.message.includes('cancel')) {
                    return;
                }
                
                ElMessage.error('操作失败');
            } finally {
                deleteVersionLoading.value = false;
            }
        };
        
        // 查看版本详情
        const viewVersionDetail = (version) => {
            selectedVersionDetail.value = version;
            setTimeout(() => {
                showVersionDetailDialog.value = true;
            }, 50);
        };
        
        // BOM管理相关方法
        // 新增子项
        const handleAddBomItem = async () => {
            if (!currentEditingPart.value) {
                ElMessage.error('请先选择要操作的部件');
                return;
            }
            try {
                // 打开对话框
                showAddBomItemDialog.value = true;
                
                // 加载所有部件数据
                await loadAllPartsForBom();
            } catch (error) {
                ElMessage.error('操作失败');
            }
        };
        
        // 加载所有部件数据用于BOM选择
        const loadAllPartsForBom = async () => {
            try {
                bomPartsLoading.value = true;
                const response = await axios.get('/api/part/getAll');
                if (response.data.code === '200') {
                    if (response.data.data && Array.isArray(response.data.data)) {
                        // 筛选出每个部件的最新版本和最新迭代
                        const latestParts = filterLatestVersionsForAll(response.data.data);
                        
                        // 进行数据映射
                        let mappedData = mapPartData(latestParts);
                        // 移除临时数据
                        mappedData.forEach(item => delete item._originalData);
                        
                        bomParts.value = mappedData;
                        filteredBomParts.value = mappedData;
                    } else {
                        bomParts.value = [];
                        filteredBomParts.value = [];
                    }
                } else {
                    bomParts.value = [];
                    filteredBomParts.value = [];
                    ElMessage.error('获取部件信息失败');
                }
            } catch (error) {
                bomParts.value = [];
                filteredBomParts.value = [];
                ElMessage.error('操作失败');
            } finally {
                bomPartsLoading.value = false;
            }
        };
        
        // BOM搜索处理
        const handleBomSearch = () => {
            const keyword = bomSearchKeyword.value.trim().toLowerCase();
            if (!keyword) {
                filteredBomParts.value = bomParts.value;
            } else {
                filteredBomParts.value = bomParts.value.filter(part => 
                    (part.PartName && part.PartName.toLowerCase().includes(keyword)) ||
                    (part.ComponentCode && part.ComponentCode.toLowerCase().includes(keyword)) ||
                    (part.ClassificationCode && part.ClassificationCode.toLowerCase().includes(keyword))
                );
            }
        };
        
        // 添加BOM关系
        const handleAddBomRelation = async (childPart) => {
            if (!currentEditingPart.value) {
                ElMessage.error('父部件信息缺失');
                return;
            }
            if (childPart.ComponentCode === currentEditingPart.value.ComponentCode) {
                ElMessage.warning('不能将部件添加为自己的子项');
                return;
            }
            // 设置选中的子部件
            selectedChildPart.value = childPart;
            // 重置表单
            bomConfigForm.position = '';
            bomConfigForm.quantity = 1;
            // 打开配置对话框
            showBomConfigDialog.value = true;
        };
        
        // 递归获取所有祖先的masterId（防止循环引用）
        const getAllAncestorMasterIds = async (currentMasterId, visited = new Set()) => {
            // 防止无限递归
            if (visited.has(currentMasterId)) {
                return [];
            }
            visited.add(currentMasterId);
            const ancestorIds = [];
            try {
                // 通过masterId查询父项
                const response = await axios.post('/api/PartBomLink/query-by-target-id', {
                    targetId: currentMasterId
                });
                
                                 if (response.data.code === '200' && response.data.data && Array.isArray(response.data.data)) {
                     for (const parentItem of response.data.data) {
                         // 获取父项的masterId - 更准确的获取方式
                         let parentMasterId = null;
                         
                         // 首先尝试从source.id获取（这通常是part的id）
                         if (parentItem.source?.id) {
                             // 通过part id获取对应的masterId
                             try {
                                 const partResponse = await axios.post('/api/part/getById', { 
                                     id: parentItem.source.id 
                                 });
                                 
                                 if (partResponse.data.code === '200' && partResponse.data.data && partResponse.data.data.length > 0) {
                                     const partDetail = partResponse.data.data[0];
                                     if (partDetail.master && partDetail.master.id) {
                                         parentMasterId = partDetail.master.id;
                                     }
                                 }
                             } catch (error) {
                                 // 如果获取失败，尝试其他方式
                                 parentMasterId = parentItem.source?.master?.id || parentItem.source?.id;
                             }
                         } else {
                             // 备用方式
                             parentMasterId = parentItem.source?.master?.id;
                         }
                         
                         if (parentMasterId) {
                             ancestorIds.push(parentMasterId);
                             // 递归获取更上层的祖先
                             const upperAncestors = await getAllAncestorMasterIds(parentMasterId, visited);
                             ancestorIds.push(...upperAncestors);
                         }
                     }
                 }
            } catch (error) {
                console.warn('获取祖先信息时出错:', error);
            }
            
            return [...new Set(ancestorIds)]; // 去重
        };
        
        // 检查是否会造成循环引用
        const checkForCircularReference = async (currentMasterId, candidateChildMasterId) => {
            try {
                // 获取当前部件的所有祖先masterId
                const ancestorIds = await getAllAncestorMasterIds(currentMasterId);
                
                // 检查候选子部件的masterId是否在祖先链中
                if (ancestorIds.includes(candidateChildMasterId)) {
                    return true; // 存在循环引用
                }
                
                // 检查候选子部件是否就是当前部件本身
                if (currentMasterId === candidateChildMasterId) {
                    return true; // 自己不能作为自己的子部件
                }
                
                return false; // 无循环引用
            } catch (error) {
                console.warn('循环引用检查时出错:', error);
                return false; // 检查失败时允许创建，但记录警告
            }
        };

        // 确认创建BOM链接
        const confirmCreateBomLink = async () => {
            // 表单验证
            if (!bomConfigForm.position.trim()) {
                ElMessage.error('请输入部件位号');
                return;
            }
            
            if (!bomConfigForm.quantity || bomConfigForm.quantity < 1) {
                ElMessage.error('请输入有效的数量');
                return;
            }
            
            if (!currentEditingPart.value || !selectedChildPart.value) {
                ElMessage.error('部件信息缺失');
                return;
            }
            
            try {
                bomConfigLoading.value = true;
                
                // 获取父部件的详细信息
                const parentDetail = currentEditingPart.value._fullDetail;
                if (!parentDetail) {
                    ElMessage.error('无法获取父部件详细信息');
                    return;
                }
                
                // 获取当前部件的masterId
                const currentMasterId = parentDetail.master?.id;
                if (!currentMasterId) {
                    ElMessage.error('无法获取当前部件的Master ID');
                    return;
                }
                
                // 获取子部件的masterId
                let childMasterId = '';
                if (selectedChildPart.value.masterId) {
                    childMasterId = selectedChildPart.value.masterId;
                } else {
                    const childResponse = await axios.post('/api/part/getById', { 
                        id: selectedChildPart.value.ComponentCode 
                    });
                    
                    if (childResponse.data.code === '200' && childResponse.data.data && childResponse.data.data.length > 0) {
                        const childDetail = childResponse.data.data[0];
                        if (childDetail.master && childDetail.master.id) {
                            childMasterId = childDetail.master.id;
                        } else {
                            ElMessage.error('无法获取子部件的Master ID');
                            return;
                        }
                    } else {
                        ElMessage.error('获取子部件详细信息失败');
                        return;
                    }
                }
                
                // 循环引用检查
                const hasCircularReference = await checkForCircularReference(currentMasterId, childMasterId);
                if (hasCircularReference) {
                    ElMessage.error(`⚠️ 不能添加该部件作为子项：这将导致BOM树出现循环引用。\n\n该部件"${selectedChildPart.value.PartName}"是当前部件的祖先部件或就是当前部件本身。\n\n为了保持BOM树结构的完整性，系统禁止这种循环引用。`);
                    return;
                } else {
                    ElMessage.success('✅ 循环引用检查通过，可以安全添加该子项');
                }
                  
                  const bomLinkData = {
                    rdmExtensionType: "PartBOMLink",
                    source: {
                        id: currentEditingPart.value.ComponentCode,
                        clazz: "Part",
                        name: currentEditingPart.value.PartName
                    },
                    position: bomConfigForm.position.trim(),
                    target: {
                        id: childMasterId,
                        clazz: "PartMaster",
                        name: ""
                    },
                    quantity: bomConfigForm.quantity.toString(),
                    tenant: {
                        id: "-1",
                        clazz: "Tenant",
                        name: "basicTenant"
                    },
                    creator: "hid_vv7b-0o0-beyfum 069a5a78b6a742c8b48d74377083df17",
                    modifier: "hid_vv7b-0o0-beyfum 069a5a78b6a742c8b48d74377083df17"
                };
                
                // 调用创建BOM链接接口
                const response = await axios.post('/api/PartBomLink/create', bomLinkData);
                
                if (response.data.code === '200') {
                    ElMessage.success(response.data.data || response.data.message || '子项创建成功');
                    
                    // 关闭配置对话框
                    showBomConfigDialog.value = false;
                    
                    // 刷新BOM子部件列表
                    await loadBomChildParts();
                  } else {
                    const errorMessage = response.data.message || '创建BOM链接失败';
                    ElMessage.error(errorMessage);
                }
                
            } catch (error) {
                ElMessage.error('操作失败');
            } finally {
                bomConfigLoading.value = false;
            }
        };
        
        // 加载BOM子部件列表
        const loadBomChildParts = async () => {
            if (!currentEditingPart.value) {
                return;
            }
            try {
                bomChildPartsLoading.value = true;
                // 调用查询子部件接口
                const response = await axios.post('/api/PartBomLink/query-by-source-id', {
                    sourceId: currentEditingPart.value.ComponentCode
                });
                
                if (response.data.code === '200') {
                    if (response.data.data && Array.isArray(response.data.data)) {
                        // 处理子部件数据，获取详细信息
                        const childPartsData = await Promise.all(
                            response.data.data.map(async (bomLink) => {
                                try {
                                    // 检查BOM链接数据的所有字段，找到正确的masterId
                                    let childMasterId = null;
                                    if (bomLink.target && bomLink.target.id) {
                                        childMasterId = bomLink.target.id;
                                    } else if (bomLink.id) {
                                        childMasterId = bomLink.id;
                                    } else if (bomLink.masterId) {
                                        childMasterId = bomLink.masterId;
                                    }
                                    
                                    if (!childMasterId) {
                                        return {
                                            bomLinkId: 'unknown',
                                            childPartCode: '未知',
                                            childPartName: '无法获取masterId',
                                            position: bomLink.position || '-',
                                            quantity: bomLink.quantity || '1',
                                            versionNumber: '-',
                                            masterId: 'unknown',
                                            _bomLinkData: bomLink,
                                            _partDetail: null
                                        };
                                    }
                                    
                                    // 通过masterId获取子部件详细信息
                                    const partResponse = await axios.post(`/api/part/queryByMasterId?masterId=${childMasterId}`);
                                    
                                    if (partResponse.data.code === '200' && partResponse.data.data) {
                                        let partDetail;
                                        if (Array.isArray(partResponse.data.data)) {
                                            partDetail = partResponse.data.data[0];
                                        } else {
                                            partDetail = partResponse.data.data;
                                        }
                                        
                                        if (partDetail) {
                                            return {
                                                bomLinkId: bomLink.id || bomLink.linkId,
                                                childPartCode: partDetail.id || childMasterId,
                                                childPartName: partDetail.name || '未知部件',
                                                position: bomLink.position || '-',
                                                quantity: bomLink.quantity || '1',
                                                versionNumber: partDetail.version || '-',
                                                masterId: childMasterId,
                                                _bomLinkData: bomLink,
                                                _partDetail: partDetail
                                            };
                                        }
                                    }
                                    
                                    return {
                                        bomLinkId: bomLink.id || bomLink.linkId,
                                        childPartCode: childMasterId,
                                        childPartName: '未知部件',
                                        position: bomLink.position || '-',
                                        quantity: bomLink.quantity || '1',
                                        versionNumber: '-',
                                        masterId: childMasterId,
                                        _bomLinkData: bomLink,
                                        _partDetail: null
                                    };
                                } catch (error) {
                                    return {
                                        bomLinkId: bomLink.id || bomLink.linkId,
                                        childPartCode: bomLink.id,
                                        childPartName: '获取失败',
                                        position: bomLink.position || '-',
                                        quantity: bomLink.quantity || '1',
                                        versionNumber: '-',
                                        masterId: bomLink.id,
                                        _bomLinkData: bomLink,
                                        _partDetail: null
                                    };
                                }
                            })
                        );
                        
                        bomChildParts.value = childPartsData;
                    } else {
                        bomChildParts.value = [];
                    }
                } else {
                    bomChildParts.value = [];
                    ElMessage.error(response.data.message || '获取BOM子部件失败');
                }
            } catch (error) {
                bomChildParts.value = [];
                ElMessage.error('操作失败');
            } finally {
                bomChildPartsLoading.value = false;
            }
        };
        
        // 标签页点击处理
        const handleTabClick = (tab) => {
            if (tab.props.label === 'BOM清单') {
                if (currentEditingPart.value) {
                    loadBomChildParts();
                }
            }
        };
        
        // 编辑BOM子项
        const handleEditBomChild = (childPart) => {
            if (!childPart || !childPart._bomLinkData) {
                ElMessage.error('无法获取BOM链接信息');
                return;
            }
            
            // 设置编辑的子项数据
            editingBomChild.value = childPart;
            
            // 初始化表单数据
            editBomChildForm.position = childPart.position || '';
            editBomChildForm.quantity = parseInt(childPart.quantity) || 1;
            
            // 显示编辑对话框
            showEditBomChildDialog.value = true;
        };
        
        // 确认更新BOM子项
        const confirmUpdateBomChild = async () => {
            // 表单验证
            if (!editBomChildForm.position.trim()) {
                ElMessage.error('请输入部件位号');
                return;
            }
            
            if (!editBomChildForm.quantity || editBomChildForm.quantity < 1) {
                ElMessage.error('请输入有效的数量');
                return;
            }
            
            if (!editingBomChild.value || !editingBomChild.value._bomLinkData) {
                ElMessage.error('BOM链接信息缺失');
                return;
            }
            
            try {
                editBomChildLoading.value = true;
                
                // 获取BOM链接ID
                const bomLinkId = editingBomChild.value._bomLinkData.id || editingBomChild.value.bomLinkId;
                
                // 调用更新接口
                const response = await axios.post('/api/PartBomLink/update', {
                    id: bomLinkId,
                    position: editBomChildForm.position.trim(),
                    quantity: editBomChildForm.quantity.toString()
                });
                
                if (response.data.code === '200') {
                    ElMessage.success(response.data.data || response.data.message || 'BOM子项更新成功');
                    
                    // 关闭对话框
                    showEditBomChildDialog.value = false;
                    
                    // 刷新BOM子部件列表
                    await loadBomChildParts();
                    
                } else {
                    const errorMessage = response.data.message || 'BOM子项更新失败';
                    ElMessage.error(errorMessage);
                }
                
            } catch (error) {

                
                ElMessage.error('操作失败');
            } finally {
                editBomChildLoading.value = false;
            }
        };
        
        // 删除BOM子项
        const handleDeleteBomChild = async (childPart) => {
            if (!childPart || !childPart._bomLinkData) {
                ElMessage.error('无法获取BOM链接信息');
                return;
            }
            
            try {
                await ElMessageBox.confirm(
                    `确定要删除子部件"${childPart.childPartName}"吗？此操作不可撤销！`,
                    '删除确认',
                    {
                        confirmButtonText: '确定删除',
                        cancelButtonText: '取消',
                        type: 'warning',
                        confirmButtonClass: 'el-button--danger'
                    }
                );
                
                // 获取BOM链接ID
                const bomLinkId = childPart._bomLinkData.id || childPart.bomLinkId;
                
                // 调用删除接口
                const response = await axios.post('/api/PartBomLink/delete', {
                    id: bomLinkId
                });
                
                if (response.data.code === '200') {
                    ElMessage.success(response.data.data || response.data.message || '子项删除成功');
                    
                    // 刷新BOM子部件列表
                    await loadBomChildParts();
                    
                } else {
                    const errorMessage = response.data.message || '删除BOM子项失败';
                    ElMessage.error(errorMessage);
                }
                
            } catch (error) {
                if (error.message && error.message.includes('cancel')) {
                    // 用户取消删除
                    return;
                }
                
                ElMessage.error('操作失败');
            }
        };
        
        // 查看父项
        const handleViewParentItems = async () => {
            if (!currentEditingPart.value) {
                ElMessage.error('请先选择要操作的部件');
                return;
            }
            
            // 获取当前部件的masterId
            let currentMasterId = null;
            if (currentEditingPart.value.masterId) {
                currentMasterId = currentEditingPart.value.masterId;
            } else if (currentEditingPart.value._fullDetail && currentEditingPart.value._fullDetail.master) {
                currentMasterId = currentEditingPart.value._fullDetail.master.id;
            }
            
            if (!currentMasterId) {
                ElMessage.error('无法获取当前部件的Master ID');
                return;
            }
            
            try {
                parentPartsLoading.value = true;
                
                // 调用查询父项接口
                const response = await axios.post('/api/PartBomLink/query-by-target-id', {
                    targetId: currentMasterId
                });
                
                if (response.data.code === '200') {
                    if (response.data.data && Array.isArray(response.data.data)) {
                        // 处理父项数据 - 从source对象中提取正确信息
                        const allParentPartsData = response.data.data.map((parentItem) => {
                            // 从source对象中提取父部件信息
                            const sourceData = parentItem.source || {};
                            const version = sourceData.version || '';
                            const iteration = sourceData.iteration || 0;
                            
                            return {
                                parentPartId: sourceData.id || '未知',
                                parentPartName: sourceData.name || '未知部件',
                                version: version && iteration ? `${version}-${iteration}` : (version || '-'),
                                iteration: iteration.toString(),
                                assemblyMode: sourceData.assemblyMode || '-',
                                versionNumber: version,
                                iterationNumber: parseInt(iteration),
                                // 保存原始数据
                                _originalData: parentItem
                            };
                        });

                        // 按父项名称分组并只保留最新版本
                        const latestVersionMap = new Map();
                        allParentPartsData.forEach(item => {
                            const existingItem = latestVersionMap.get(item.parentPartName);
                            if (!existingItem) {
                                latestVersionMap.set(item.parentPartName, item);
                            } else {
                                // 比较版本号
                                if (item.versionNumber > existingItem.versionNumber || 
                                    (item.versionNumber === existingItem.versionNumber && 
                                     item.iterationNumber > existingItem.iterationNumber)) {
                                    latestVersionMap.set(item.parentPartName, item);
                                }
                            }
                        });

                        // 转换为数组
                        parentParts.value = Array.from(latestVersionMap.values());
                        
                        // 更新统计数据
                        bomParentCount.value = parentParts.value.length;
                        
                        // 显示父项对话框
                        showParentItemsDialog.value = true;
                        
                    } else {
                        parentParts.value = [];
                        bomParentCount.value = 0;
                        ElMessage.info('当前部件没有父项');
                        // 仍然显示对话框，显示空列表
                        showParentItemsDialog.value = true;
                    }
                } else {
                    parentParts.value = [];
                    ElMessage.error(response.data.message || '获取父项失败');
                }
                
            } catch (error) {
                parentParts.value = [];
                ElMessage.error('操作失败');
            } finally {
                parentPartsLoading.value = false;
            }
        };
        
        // 查看BOM树
        const handleViewBomTree = async () => {
            if (!currentEditingPart.value) {
                ElMessage.error('请先选择要操作的部件');
                return;
            }
            
            try {
                bomTreeLoading.value = true;
                
                // 初始化根节点
                const rootNode = {
                    id: `root_${currentEditingPart.value.ComponentCode}`,
                    label: currentEditingPart.value.PartName,
                    sourceId: currentEditingPart.value.ComponentCode,
                    masterId: currentEditingPart.value.masterId || currentEditingPart.value._fullDetail?.master?.id,
                    isRoot: true,
                    isLeaf: false,
                    level: 0,
                    children: []
                };
                
                bomTreeData.value = [rootNode];
                
                // 显示BOM树对话框
                showBomTreeDialog.value = true;
                
                // 初始化统计信息
                bomTreeStats.value = {
                    totalNodes: 1,
                    maxLevel: 0,
                    leafNodes: 0
                };
            } catch (error) {
                ElMessage.error('操作失败');
            } finally {
                bomTreeLoading.value = false;
            }
        };
        
        // 懒加载BOM树节点
        const loadBomTreeNode = async (node, resolve) => {
            // 如果是根节点，直接返回已初始化的数据
            if (node.level === 0) {
                return resolve(bomTreeData.value);
            }
            
            // 如果节点已经加载过子节点，直接返回
            if (node.data.loaded) {
                return resolve(node.data.children || []);
            }
            
            try {
                const sourceId = node.data.sourceId || node.data.masterId;
                if (!sourceId) {
                    return resolve([]);
                }
                
                // 调用查询子部件接口
                const response = await axios.post('/api/PartBomLink/query-by-source-id', {
                    sourceId: sourceId
                });
                
                if (response.data.code === '200' && response.data.data && Array.isArray(response.data.data)) {
                    // 处理子部件数据
                    const childNodes = await Promise.all(
                        response.data.data.map(async (bomLink) => {
                            try {
                                // 获取子部件的masterId
                                let childMasterId = null;
                                if (bomLink.target && bomLink.target.id) {
                                    childMasterId = bomLink.target.id;
                                } else if (bomLink.masterId) {
                                    childMasterId = bomLink.masterId;
                                }
                                
                                if (!childMasterId) {
                                    return null;
                                }
                                
                                // 通过masterId获取子部件详细信息
                                const partResponse = await axios.post(`/api/part/queryByMasterId?masterId=${childMasterId}`);
                                
                                if (partResponse.data.code === '200' && partResponse.data.data) {
                                    let partDetail;
                                    if (Array.isArray(partResponse.data.data)) {
                                        partDetail = partResponse.data.data[0];
                                    } else {
                                        partDetail = partResponse.data.data;
                                    }
                                    
                                    if (partDetail) {
                                        return {
                                            id: `node_${partDetail.id}_${bomLink.id}`,
                                            label: partDetail.name || '未知部件',
                                            sourceId: partDetail.id,
                                            masterId: childMasterId,
                                            position: bomLink.position,
                                            quantity: bomLink.quantity,
                                            isRoot: false,
                                            isLeaf: false,
                                            level: node.level + 1,
                                            loaded: false,
                                            children: [],
                                            _bomLinkData: bomLink,
                                            _partDetail: partDetail
                                        };
                                    }
                                }
                                return null;
                            } catch (error) {
                                return null;
                            }
                        })
                    );
                    
                    // 过滤掉null值
                    const validChildNodes = childNodes.filter(child => child !== null);
                    
                    // 更新节点状态
                    node.data.children = validChildNodes;
                    node.data.loaded = true;
                    node.data.isLeaf = validChildNodes.length === 0;
                    
                    // 更新统计信息
                    updateBomTreeStats();
                    
                    return resolve(validChildNodes);
                } else {
                    node.data.loaded = true;
                    node.data.isLeaf = true;
                    node.data.children = [];
                    return resolve([]);
                }
            } catch (error) {
                node.data.loaded = true;
                node.data.isLeaf = true;
                return resolve([]);
            }
        };
        
        // 更新BOM树统计信息
        const updateBomTreeStats = () => {
            let totalNodes = 0;
            let maxLevel = 0;
            let leafNodes = 0;
            
            const traverseTree = (nodes, level = 0) => {
                nodes.forEach(node => {
                    totalNodes++;
                    maxLevel = Math.max(maxLevel, level);
                    
                    if (node.children && node.children.length > 0) {
                        traverseTree(node.children, level + 1);
                    } else if (node.isLeaf) {
                        leafNodes++;
                    }
                });
            };
            
            traverseTree(bomTreeData.value);
            
            bomTreeStats.value = {
                totalNodes,
                maxLevel,
                leafNodes
            };
        };
        
        // 刷新BOM树
        const refreshBomTree = async () => {
            if (!currentEditingPart.value) {
                ElMessage.error('请先选择要操作的部件');
                return;
            }
            try {
                bomTreeLoading.value = true;
                // 重新初始化BOM树
                const rootNode = {
                    id: `root_${currentEditingPart.value.ComponentCode}`,
                    label: currentEditingPart.value.PartName,
                    sourceId: currentEditingPart.value.ComponentCode,
                    masterId: currentEditingPart.value.masterId || currentEditingPart.value._fullDetail?.master?.id,
                    isRoot: true,
                    isLeaf: false,
                    level: 0,
                    loaded: false,
                    children: []
                };
                
                bomTreeData.value = [rootNode];
                
                // 重置统计信息
                bomTreeStats.value = {
                    totalNodes: 1,
                    maxLevel: 0,
                    leafNodes: 0
                };
                
                ElMessage.success('BOM树刷新成功');
                
            } catch (error) {
                  ElMessage.error('刷新BOM树失败，请稍后重试');
            } finally {
                bomTreeLoading.value = false;
            }
        };
        
        // 格式化版本属性
        const formatVersionAttributes = (clsAttrs) => {
            if (!clsAttrs || clsAttrs.length === 0) return [];
            
            const attributes = [];
            clsAttrs.forEach(attr => {
                if (attr.DPA) {
                    Object.keys(attr.DPA).forEach(key => {
                        let displayValue = attr.DPA[key] || '-';
                        
                        // 对特定属性进行特殊处理，提取cnName
                        if (key === 'ScreenType' || key === 'Color' || key === 'WaterproofingGrade') {
                            try {
                                // 如果值是对象且包含cnName，则使用cnName
                                if (typeof displayValue === 'object' && displayValue !== null && displayValue.cnName) {
                                    displayValue = displayValue.cnName;
                                }
                                // 如果值是字符串但看起来像JSON，尝试解析
                                else if (typeof displayValue === 'string' && displayValue.includes('"cnName"')) {
                                    const parsedValue = JSON.parse(displayValue);
                                    if (parsedValue.cnName) {
                                        displayValue = parsedValue.cnName;
                                    }
                                }
                            } catch (error) {
                                // 解析失败时保持原值
                                console.warn(`解析属性值失败: ${key}`, error);
                            }
                        }
                        
                        attributes.push({
                            name: key,
                            value: displayValue
                        });
                    });
                }
            });
            
            return attributes;
        };
        
        // 获取状态标签类型
        const getStateTagType = (state) => {
            switch (state) {
                case 'INWORK':
                case 'WORKING':
                    return 'warning';
                case 'RELEASED':
                case 'APPROVED':
                    return 'success';
                case 'OBSOLETE':
                case 'CANCELLED':
                    return 'danger';
                default:
                    return 'info';
            }
        };
        
        // 获取状态文本
        const getStateText = (state) => {
            switch (state) {
                case 'INWORK':
                case 'WORKING':
                    return '工作中';
                case 'RELEASED':
                    return '已发布';
                case 'APPROVED':
                    return '已批准';
                case 'OBSOLETE':
                    return '已废弃';
                case 'CANCELLED':
                    return '已取消';
                default:
                    return state || '未知';
            }
        };
        
        // 格式化日期时间
        const formatDateTime = (timestamp) => {
            if (!timestamp) return '-';
            
            try {
                const date = new Date(timestamp);
                return date.toLocaleString('zh-CN', {
                    year: 'numeric',
                    month: '2-digit',
                    day: '2-digit',
                    hour: '2-digit',
                    minute: '2-digit',
                    second: '2-digit'
                });
            } catch (error) {
                return timestamp.toString();
            }
        };

        // 数据映射处理函数
        const mapPartData = (data) => {
            // 构建版本号映射表
            const versionMap = {};
            data.forEach(item => {
                const masterId = item.master && item.master.id;
                const majorVersion = item.version; // 大版本号 (A, B, C, etc.)
                const minorVersion = getIterationNumber(item); // 使用统一的获取迭代号函数
                
                const key = `${masterId}_${majorVersion}`;
                versionMap[key] = {
                    maxMinor: minorVersion,
                    item: item
                };
            });
            
            return data.map((item) => {
                // 处理分类码 - 从extAttrs数组中提取
                let classificationCode = '';
                if (item.extAttrs && Array.isArray(item.extAttrs) && item.extAttrs.length > 0) {
                    const dpaAttr = item.extAttrs.find(attr => attr.name === 'DPA');
                    if (dpaAttr && dpaAttr.value && dpaAttr.value.businessCode) {
                        classificationCode = dpaAttr.value.businessCode;
                    }
                }
                
                // 构建版本号显示格式：大版本号-最大小版本号
                const masterId = item.master && item.master.id;
                const majorVersion = item.version;
                const key = `${masterId}_${majorVersion}`;
                const maxMinorVersion = versionMap[key] ? versionMap[key].maxMinor : 0;
                
                // 如果有小版本号且大于0，则显示为 "A-5" 格式；否则只显示大版本号 "A"
                const displayVersion = maxMinorVersion > 0 ? `${majorVersion}-${maxMinorVersion}` : majorVersion;
                
                return {
                    ComponentCode: item.id,           // 部件编码
                    PartName: item.name,              // 部件名称
                    VersionNumber: displayVersion,    // 版本号（格式：A-11, C-5）
                    AssemblyMode: item.assemblyMode,  // 装配模式
                    ClassificationCode: classificationCode, // 分类码
                    masterId: masterId,               // 保存masterId用于删除操作
                    // 保存原始数据用于筛选
                    _originalData: item
                };
            });
        };

                // 筛选原始数据中每个部件的最新版本和最新迭代
        const filterLatestVersionsForAll = (rawData) => {
            // 按 master.id 分组（每个唯一部件都有相同的 master.id）
            const groupedByMasterId = {};
            rawData.forEach(item => {
                const masterId = item.master && item.master.id;
                if (!masterId) {
                    return;
                }
            
                if (!groupedByMasterId[masterId]) {
                    groupedByMasterId[masterId] = [];
                }
                groupedByMasterId[masterId].push(item);
                          });
              
              // 对每个 master.id，找出最新版本的最新迭代
            const latestVersions = [];
            Object.keys(groupedByMasterId).forEach(masterId => {
                const parts = groupedByMasterId[masterId];
                
                // 找出最新版本的最新迭代
                let latestVersionPart = parts[0];
                parts.forEach(part => {
                    // 比较版本号 (A < B < C ...)
                    if (part.version > latestVersionPart.version) {
                        latestVersionPart = part;
                    } else if (part.version === latestVersionPart.version) {
                        // 版本号相同时，比较迭代次数
                        const currentIteration = getIterationNumber(part);
                        const latestIteration = getIterationNumber(latestVersionPart);
                        if (currentIteration > latestIteration) {
                            latestVersionPart = part;
                        }
                    }
                });
                
                latestVersions.push(latestVersionPart);
            });
            return latestVersions;
        };

        // 获取迭代号的辅助函数
        const getIterationNumber = (item) => {
            if (item.iteration !== undefined) {
                return parseInt(item.iteration) || 0;
            } else if (item.iterationNum !== undefined) {
                return parseInt(item.iterationNum) || 0;
            } else if (item.minorVersion !== undefined) {
                return parseInt(item.minorVersion) || 0;
            }
            return 0;
        };

        // 筛选已映射数据中的最新版本和最新迭代的函数（用于关键字搜索）
        const filterLatestVersions = (mappedData) => {
            // 按 master.id 分组
            const groupedByMasterId = {};
            mappedData.forEach(item => {
                const masterId = item._originalData.master && item._originalData.master.id;
                if (!masterId) {
                    return;
                }
                
                if (!groupedByMasterId[masterId]) {
                    groupedByMasterId[masterId] = [];
                }
                groupedByMasterId[masterId].push(item);
            });

            // 对每个 master.id，找出最新版本的最新迭代
            const latestVersions = [];
            Object.keys(groupedByMasterId).forEach(masterId => {
                const parts = groupedByMasterId[masterId];
                
                // 找出最新版本
                let latestVersionPart = parts[0];
                parts.forEach(part => {
                    const originalData = part._originalData;
                    const latestOriginalData = latestVersionPart._originalData;
                    
                    // 比较版本号 (A < B < C ...)
                    if (originalData.version > latestOriginalData.version) {
                        latestVersionPart = part;
                    } else if (originalData.version === latestOriginalData.version) {
                        // 版本号相同时，比较迭代次数
                        const currentIteration = getIterationNumber(originalData);
                        const latestIteration = getIterationNumber(latestOriginalData);
                        if (currentIteration > latestIteration) {
                            latestVersionPart = part;
                        }
                    }
                });
                
                // 移除临时数据
                delete latestVersionPart._originalData;
                latestVersions.push(latestVersionPart);
            });

            return latestVersions;
        };

        // 获取所有部件信息的方法
        const fetchPartData = async () => {
            loading.value = true;
            try {
                const response = await axios.get('/api/part/getAll');
                if (response.data.code === '200') {
                    // 检查data是否存在且为数组
                                          if (response.data.data && Array.isArray(response.data.data)) {
                          // 先筛选出每个部件的最新版本和最新迭代
                                                  const latestParts = filterLatestVersionsForAll(response.data.data);
                          
                          // 然后进行数据映射
                        let mappedData = mapPartData(latestParts);
                        // 移除临时数据
                        mappedData.forEach(item => delete item._originalData);
                        tableData1.value = mappedData;
                    } else {
                        tableData1.value = [];
                    }
                } else {
                    
                    tableData1.value = [];
                }
            } catch (error) {

                tableData1.value = [];
            } finally {
                loading.value = false;
            }
        };

        // 按部件编码精确查询
        const searchPartById = async (partId) => {
            loading.value = true;
            try {

                const response = await axios.post('/api/part/getById', { id: partId });
                
                if (response.data.code === '200') {
                    // 检查data是否存在且为数组
                                          if (response.data.data && Array.isArray(response.data.data)) {
                          let mappedData = mapPartData(response.data.data);
                        // 移除临时数据
                        mappedData.forEach(item => delete item._originalData);
                        tableData1.value = mappedData;
                    } else {

                        tableData1.value = [];
                    }
                } else {

                    tableData1.value = [];
                }
            } catch (error) {

                tableData1.value = [];
            } finally {
                loading.value = false;
            }
        };

        // 按关键字查询部件信息（用于部件名称查询和模糊搜索）
        const searchPartByKeyword = async (keyword, filterLatest = false) => {
            loading.value = true;
            try {
                const response = await axios.get(`/api/part/query?keyword=${encodeURIComponent(keyword)}`);
                if (response.data.code === '200') {
                    // 检查data是否存在且为数组
                    if (response.data.data && Array.isArray(response.data.data)) {
                        let mappedData = mapPartData(response.data.data);
                        
                        // 如果需要筛选最新版本，则应用筛选逻辑
                        if (filterLatest) {
                            mappedData = filterLatestVersions(mappedData);
                        } else {
                            // 不筛选时，移除临时数据
                            mappedData.forEach(item => delete item._originalData);
                        }
                        
                        tableData1.value = mappedData;
                    } else {
                        tableData1.value = [];
                    }
                } else {

                    tableData1.value = [];
                }
            } catch (error) {

                tableData1.value = [];
            } finally {
                loading.value = false;
            }
        };


        
        // 统一搜索处理函数
        const handleUnifiedSearch = () => {
            if (searchType.value === 'keyword') {
                // 关键字搜索
                const keyword = keywordInput.value.trim();
                                  if (!keyword) {
                      return;
                  }

                searchPartByKeyword(keyword, true); // 自动筛选最新版本
            } else if (searchType.value === 'precise') {
                // 精确查询
                const code = codeInput.value.trim();
                                  if (!code) {
                      return;
                  }

                searchPartById(code);
            }
        };
        
        // 统一重置函数
        const handleUnifiedReset = () => {
            searchType.value = 'keyword';
            keywordInput.value = '';
            codeInput.value = '';
            fetchPartData(); // 重新获取所有数据
        };
        
        // 删除部件函数
        const handleDeletePart = async (row) => {
            if (!row.masterId) {
                ElMessage.error('无法获取部件标识信息，删除失败');
                return;
            }
            
            try {
                // 先检查是否有父项引用
                const parentCheckResponse = await axios.post('/api/PartBomLink/query-by-target-id', {
                    targetId: row.masterId
                });

                if (parentCheckResponse.data.code === '200' && 
                    parentCheckResponse.data.data && 
                    Array.isArray(parentCheckResponse.data.data) && 
                    parentCheckResponse.data.data.length > 0) {
                    ElMessage.error('删除失败：该部件正在被其他部件引用，请先解除引用关系');
                    return;
                }
                
                await ElMessageBox.confirm(
                    `确定要删除部件 "${row.PartName}" (编码: ${row.ComponentCode}) 吗？此操作不可恢复。`,
                    '删除确认',
                    {
                        confirmButtonText: '确认删除',
                        cancelButtonText: '取消',
                        type: 'warning',
                        confirmButtonClass: 'el-button--danger'
                    }
                );
                
                // 显示加载状态
                loading.value = true;
                
                const response = await axios.post('/api/part/delete', {
                    masterId: row.masterId
                });
                
                if (response.data.code === '200') {
                    ElMessage.success(response.data.data || '部件删除成功');
                    // 删除成功后重新加载数据
                    await fetchPartData();
                } else {
                    ElMessage.error(response.data.message || '删除失败');
                }
                
            } catch (error) {
                if (error === 'cancel') {
                    // 用户取消删除
                } else {
                    ElMessage.error('删除操作失败，请稍后重试');
                }
            } finally {
                loading.value = false;
            }
        };
        
        // 页面加载时自动获取数据
        onMounted(() => {
            fetchPartData();
            loadClassificationTree();
        });

        let form1=reactive({
            PartName:'',
            DefaultUnit:'',
            Source:'1', // 默认选择零件区
            AssemblyMode:'',
            Classification:''
        })

        let isextend1=ref(false)
        let isextend2=ref(false)
        watch(
            ()=>form1.Classification,
            (newVal)=>{
            if(newVal!=''){
                // 分类选择后会通过 handleClassificationChange 来控制扩展属性的显示
                // isextend1.value=true 将在 loadClassificationAttributes 中设置
            }
            else{
                isextend1.value=false
                // 清空动态属性
                classificationAttributes.value = [];
                dynamicExtendedForm.value = {};
            }
        })

        // 分类树数据
        const data1 = ref([]);
        
        // 加载分类树数据
        const loadClassificationTree = async () => {
            try {
                const response = await axios.get('/api/category/getAll');
                const categories = response.data.data;
                allCategories.value = categories;
                data1.value = buildCompleteTree(categories);
            } catch (error) {
                data1.value = [];
            }
        };
        
        // 存储所有分类数据
        const allCategories = ref([]);
        
        // 构建完整的树结构
        const buildCompleteTree = (categories) => {
            const nodeMap = new Map();
            const rootNodes = [];
            
            categories.forEach(category => {
                const node = {
                    value: category.id,
                    label: category.name,
                    businessCode: category.businessCode,
                    children: []
                };
                nodeMap.set(category.id, node);
            });
            
            categories.forEach(category => {
                const currentNode = nodeMap.get(category.id);
                if (category.parentNode === null) {
                    rootNodes.push(currentNode);
                } else {
                    const parentNode = nodeMap.get(category.parentNode);
                    if (parentNode) {
                        parentNode.children.push(currentNode);
                    }
                }
            });
            
            const cleanEmptyChildren = (nodes) => {
                nodes.forEach(node => {
                    if (node.children && node.children.length === 0) {
                        delete node.children;
                    } else if (node.children) {
                        cleanEmptyChildren(node.children);
                    }
                });
            };
            
            cleanEmptyChildren(rootNodes);
            return rootNodes;
        };
        

        
        // 分类相关的扩展属性
        const classificationAttributes = ref([]);
        const dynamicExtendedForm = ref({});
        
        // 编辑部件相关变量
        const editForm = reactive({
            PartName: '',
            DefaultUnit: '',
            Source: '',
            AssemblyMode: '',
            Classification: ''
        });
        const editDynamicExtendedForm = ref({});
        const editClassificationAttributes = ref([]);
        const editSelectedClassificationName = ref('');
        const editSelectedClassificationInfo = ref(null);
        const currentEditingPart = ref(null);
        
        // 版本管理相关数据
        const partVersions = ref([]);
        const versionsLoading = ref(false);
        const deleteVersionLoading = ref(false);
        const showVersionDetailDialog = ref(false);
        const selectedVersionDetail = ref(null);
        
        // BOM管理相关数据
        const bomLoading = ref(false);
        const bomChildCount = ref(0);
        const bomParentCount = ref(0);
        const bomLevelCount = ref(1);
        
        // 新增子项对话框相关数据
        const showAddBomItemDialog = ref(false);
        const bomParts = ref([]);
        const bomPartsLoading = ref(false);
        const bomSearchKeyword = ref('');
        const filteredBomParts = ref([]);
        
        // BOM配置对话框相关数据
        const showBomConfigDialog = ref(false);
        const selectedChildPart = ref(null);
        const bomConfigLoading = ref(false);
        const bomConfigForm = reactive({
            position: '',
            quantity: 1
        });
        
        // BOM子部件列表相关数据
        const bomChildParts = ref([]);
        const bomChildPartsLoading = ref(false);
        
        // 修改BOM子项相关数据
        const showEditBomChildDialog = ref(false);
        const editingBomChild = ref(null);
        const editBomChildLoading = ref(false);
        const editBomChildForm = reactive({
            position: '',
            quantity: 1
        });
        
        // 查看父项相关数据
        const showParentItemsDialog = ref(false);
        const parentParts = ref([]);
        const parentPartsLoading = ref(false);
        
        // BOM树相关数据
        const showBomTreeDialog = ref(false);
        const bomTreeData = ref([]);
        const bomTreeLoading = ref(false);
        const bomTreeRef = ref(null);
        const bomTreeStats = ref({
            totalNodes: 0,
            maxLevel: 0,
            leafNodes: 0
        });
        
        // BOM树配置
        const bomTreeProps = ref({
            children: 'children',
            label: 'label',
            isLeaf: 'isLeaf'
        });
        
        // 版本管理计算属性
        const majorVersionCount = computed(() => {
            const uniqueMajorVersions = new Set(partVersions.value.map(v => v.version));
            return uniqueMajorVersions.size;
        });
        
        const latestMajorVersion = computed(() => {
            if (partVersions.value.length === 0) return '-';
            const versions = partVersions.value.map(v => v.version).sort();
            return versions[versions.length - 1];
        });
        
        const latestMinorVersion = computed(() => {
            if (partVersions.value.length === 0) return '-';
            const maxIteration = Math.max(...partVersions.value.map(v => v.iteration || 0));
            return maxIteration;
        });
        
        // 编辑分类选择对话框相关变量
        const showEditClassificationDialog = ref(false);
        const editTempSelectedClassification = ref(null);
        const editClassificationSearchKeyword = ref('');
        const editClassificationTreeRef = ref(null);
        const editClassificationTreeData = ref([]);
        
        // 分类选择改变时的处理
        const handleClassificationChange = async (value) => {
            if (value) {
                // 从所有分类数据中查找选中的分类
                const selectedCategory = allCategories.value.find(category => category.id === value);
                if (selectedCategory) {
        
                    
                    // 加载该分类的属性
                    await loadClassificationAttributes(value);
                }
            } else {
                // 清空扩展属性
                classificationAttributes.value = [];
                dynamicExtendedForm.value = {};
            }
        };
        
        // 查询属性的计量单位
        const getAttributeUnit = async (attributeName) => {
            try {
                const response = await axios.get(`/api/exa/querybyname?keyword=${encodeURIComponent(attributeName)}`);
                
                if (response.data.code === '200' && response.data.data && Array.isArray(response.data.data)) {
                    // 查找匹配的属性
                    const matchedAttribute = response.data.data.find(attr => 
                        attr.name === attributeName || attr.nameEn === attributeName
                    );
                    
                    if (matchedAttribute && matchedAttribute.unitType && 
                        matchedAttribute.unitType.measuringUnitList && 
                        Array.isArray(matchedAttribute.unitType.measuringUnitList) &&
                        matchedAttribute.unitType.measuringUnitList.length > 0) {
                        // 查找转换率为1的计量单位
                        const baseUnit = matchedAttribute.unitType.measuringUnitList.find(unit => 
                            unit.conversionRate === '1'
                        );
                        
                        // 如果找到了转换率为1的单位，返回其名称
                        if (baseUnit) {
                            return baseUnit.name;
                        }
                    }
                }
                return null;
            } catch (error) {
                console.warn(`获取属性"${attributeName}"的计量单位失败:`, error);
                return null;
            }
        };

        // 加载分类的属性
        const loadClassificationAttributes = async (classificationId) => {
            try {
                const parentResponse = await axios.get(`/api/EXADefinitionLink/getParentAttributesByClassification?classificationNodeId=${classificationId}`);
                let attributes = [];
                
                if (parentResponse.data.data) {
                    attributes = [...attributes, ...parentResponse.data.data.map(item => item.source)];
                }
                
                const directResponse = await axios.get(`/api/EXADefinitionLink/listByClassification?classificationNodeId=${classificationId}`);
                
                if (directResponse.data.data) {
                    attributes = [...attributes, ...directResponse.data.data.map(item => item.source)];
                }
                
                const uniqueAttributes = attributes.filter((attr, index, self) => 
                    index === self.findIndex(a => a.id === attr.id)
                );
                
                // 为每个属性查询计量单位
                const attributesWithUnits = await Promise.all(
                    uniqueAttributes.map(async (attr) => {
                        const unit = await getAttributeUnit(attr.name);
                        return {
                            ...attr,
                            unit: unit // 添加计量单位字段
                        };
                    })
                );
                
                classificationAttributes.value = attributesWithUnits;
                
                const formData = {};
                attributesWithUnits.forEach(attr => {
                    formData[attr.id] = '';
                });
                dynamicExtendedForm.value = formData;
                isextend1.value = true;
            } catch (error) {
                classificationAttributes.value = [];
                dynamicExtendedForm.value = {};
            }
        };
        
        // 分类选择对话框相关变量和函数
        const showClassificationDialog = ref(false);
        const selectedClassificationName = ref('');
        const selectedClassificationInfo = ref(null); // 存储完整的分类信息
        const tempSelectedClassification = ref(null);
        const classificationSearchKeyword = ref('');
        const classificationTreeRef = ref(null);
        const classificationTreeData = ref([]);
        
        // 分类树配置
        const classificationTreeProps = ref({
            children: 'children',
            label: 'name',
            isLeaf: 'isLeaf'
        });
        
        // 加载分类树根节点
        const loadClassificationRootNodes = async () => {
            try {
                const response = await axios.get('/api/category/getAll');
                
                // 存储所有分类数据
                allCategories.value = response.data.data;
                
                // 处理返回数据，构建根节点
                classificationTreeData.value = response.data.data.map(item => ({
                    id: item.id,
                    name: item.name,
                    businessCode: item.businessCode,
                    isLeaf: false,
                    loaded: false,
                    parentNode: item.parentNode,
                })).filter(item => item.parentNode === null);
            } catch (error) {
                classificationTreeData.value = [];
            }
        };
        
        // 懒加载分类树子节点
        const loadClassificationNode = async (node, resolve) => {
            // 如果是根节点，直接返回已加载的数据
            if (node.level === 0) {
                return resolve(classificationTreeData.value);
            }
            
            // 如果节点已经加载过子节点，直接返回
            if (node.data.loaded) {
                return resolve(node.data.children || []);
            }
            
            try {
                // 调用获取子分类的接口
                const response = await axios.get(`/api/category/children?parentId=${node.data.id}`);

                
                // 处理子节点数据
                const children = response.data.data.map(item => ({
                    id: item.id,
                    name: item.name,
                    businessCode: item.businessCode,
                    isLeaf: item.hasChildren === false,
                    loaded: false
                }));
                
                // 更新节点状态
                node.data.children = children;
                node.data.loaded = true;
                

                resolve(children);
            } catch (error) {

                resolve([]);
            }
        };
        
        // 分类树节点点击事件
                const handleClassificationNodeClick = (data) => {
            // 确保数据完整性，增强防御性检查
            if (data && (data.id || data.id === 0)) {
                const classificationData = {
                id: data.id,
                    name: data.name || data.label || '未知分类',
                    businessCode: data.businessCode || data.code || ''
                };
                
                                  tempSelectedClassification.value = classificationData;
                  
                  // 验证设置是否成功
                                  if (!tempSelectedClassification.value || !tempSelectedClassification.value.id) {
                      ElMessage.error('分类数据异常，请重新选择');
                  }
                          } else {
                  tempSelectedClassification.value = null;
                ElMessage.warning('请选择有效的分类节点');
            }
        };
        

        
        // 分类搜索处理
        const handleClassificationSearch = () => {
            if (classificationTreeRef.value) {
                classificationTreeRef.value.filter(classificationSearchKeyword.value);
            }
        };
        
        // 分类树过滤方法
        const filterClassificationNode = (value, data) => {
            if (!value) return true;
            const nameMatch = data.name && data.name.toLowerCase().indexOf(value.toLowerCase()) !== -1;
            const codeMatch = data.businessCode && data.businessCode.toLowerCase().indexOf(value.toLowerCase()) !== -1;
            return nameMatch || codeMatch;
        };
        
        // 确认分类选择
        const confirmClassificationSelection = async () => {
            if (!tempSelectedClassification.value || !tempSelectedClassification.value.id) {
                ElMessage.warning('请先选择一个分类');
                return;
            }
            
            try {
                // 设置分类ID和显示名称
                form1.Classification = tempSelectedClassification.value.id;
                selectedClassificationName.value = `${tempSelectedClassification.value.name} (${tempSelectedClassification.value.businessCode})`;
                
                                // 获取分类的详细信息（包括名称、类型、ID等）
                const classificationResponse = await axios.post('/api/category/getById', {
                    id: tempSelectedClassification.value.id
                });
                
                if (classificationResponse.data.code === '200' && classificationResponse.data.data) {
                    const classificationData = classificationResponse.data.data;
                    // 如果返回的是数组，取第一个元素
                    const classificationInfo = Array.isArray(classificationData) ? classificationData[0] : classificationData;

                    
                    // 存储分类的完整信息供后续使用
                    selectedClassificationInfo.value = {
                        id: classificationInfo.id,
                        name: classificationInfo.name,
                        businessCode: classificationInfo.businessCode,
                        type: classificationInfo.type || 'CATEGORY'
                    };
                    

                } else {

                    selectedClassificationInfo.value = {
                        id: tempSelectedClassification.value.id,
                        name: tempSelectedClassification.value.name,
                        businessCode: tempSelectedClassification.value.businessCode,
                        type: 'CATEGORY'
                    };
                }
                
                // 加载该分类的属性
                await loadClassificationAttributes(tempSelectedClassification.value.id);
                
                showClassificationDialog.value = false;
                tempSelectedClassification.value = null;
                classificationSearchKeyword.value = '';
                
            } catch (error) {

                
                // 检查tempSelectedClassification是否仍然存在
                if (tempSelectedClassification.value && tempSelectedClassification.value.id) {
                    // 即使获取详细信息失败，也可以继续使用基本信息

                    selectedClassificationInfo.value = {
                        id: tempSelectedClassification.value.id,
                        name: tempSelectedClassification.value.name,
                        businessCode: tempSelectedClassification.value.businessCode,
                        type: 'CATEGORY'
                    };
                    
                    // 尝试加载该分类的属性
                    try {
                        await loadClassificationAttributes(tempSelectedClassification.value.id);
                    } catch (attrError) {

                        ElMessage.warning('分类属性加载失败，但可以继续创建部件');
                    }
                    
                    ElMessage.warning('部分分类信息获取失败，但已选择该分类');
                } else {

                    ElMessage.error('分类选择失败，请重新选择');
                }
                
                showClassificationDialog.value = false;
                tempSelectedClassification.value = null;
                classificationSearchKeyword.value = '';
            }
        };
        
        // 取消分类选择
        const cancelClassificationSelection = () => {
            showClassificationDialog.value = false;
            tempSelectedClassification.value = null;
            classificationSearchKeyword.value = '';
            // 不清理已选择的分类信息，只清理临时状态
        };
        
        // 编辑分类选择相关函数
        const loadEditClassificationRootNodes = async () => {
            try {
                const response = await axios.get('/api/category/getAll');
                editClassificationTreeData.value = response.data.data.map(item => ({
                    id: item.id,
                    name: item.name,
                    businessCode: item.businessCode,
                    isLeaf: false,
                    loaded: false,
                    parentNode: item.parentNode,
                })).filter(item => item.parentNode === null);
                

            } catch (error) {

                editClassificationTreeData.value = [];
            }
        };
        
        const loadEditClassificationNode = async (node, resolve) => {
            if (node.level === 0) {
                return resolve(editClassificationTreeData.value);
            }
            
            if (node.data.loaded) {
                return resolve(node.data.children || []);
            }
            
            try {
                const response = await axios.get(`/api/category/children?parentId=${node.data.id}`);
                const children = response.data.data.map(item => ({
                    id: item.id,
                    name: item.name,
                    businessCode: item.businessCode,
                    isLeaf: item.hasChildren === false,
                    loaded: false
                }));
                
                node.data.children = children;
                node.data.loaded = true;
                
                resolve(children);
            } catch (error) {

                resolve([]);
            }
        };
        
        const handleEditClassificationNodeClick = (data) => {

            
            if (data && data.id) {
                editTempSelectedClassification.value = {
                    id: data.id,
                    name: data.name || '未知分类',
                    businessCode: data.businessCode || ''
                };
            } else {
                editTempSelectedClassification.value = null;
            }
        };
            
        const handleEditClassificationSearch = () => {
            if (editClassificationTreeRef.value) {
                editClassificationTreeRef.value.filter(editClassificationSearchKeyword.value);
            }
        };
        
        const filterEditClassificationNode = (value, data) => {
            if (!value) return true;
            const nameMatch = data.name && data.name.toLowerCase().indexOf(value.toLowerCase()) !== -1;
            const codeMatch = data.businessCode && data.businessCode.toLowerCase().indexOf(value.toLowerCase()) !== -1;
            return nameMatch || codeMatch;
        };
        
        const confirmEditClassificationSelection = async () => {
            if (!editTempSelectedClassification.value || !editTempSelectedClassification.value.id) {
                ElMessage.warning('请先选择一个分类');
                return;
            }
            
            try {
    
                editForm.Classification = editTempSelectedClassification.value.id;
                editSelectedClassificationName.value = `${editTempSelectedClassification.value.name} (${editTempSelectedClassification.value.businessCode})`;
                
                // 获取分类详细信息
                const classificationResponse = await axios.post('/api/category/getById', {
                    id: editTempSelectedClassification.value.id
                });
                
                if (classificationResponse.data.code === '200' && classificationResponse.data.data) {
                    const classificationData = classificationResponse.data.data;
                    const classificationInfo = Array.isArray(classificationData) ? classificationData[0] : classificationData;
                    
                    editSelectedClassificationInfo.value = {
                        id: classificationInfo.id,
                        name: classificationInfo.name,
                        businessCode: classificationInfo.businessCode,
                        type: classificationInfo.type || 'CATEGORY'
                    };
                } else {
                    editSelectedClassificationInfo.value = {
                        id: editTempSelectedClassification.value.id,
                        name: editTempSelectedClassification.value.name,
                        businessCode: editTempSelectedClassification.value.businessCode,
                        type: 'CATEGORY'
                    };
                }
                
                // 加载该分类的属性
                await loadEditClassificationAttributes(editTempSelectedClassification.value.id);
                
                showEditClassificationDialog.value = false;
                editTempSelectedClassification.value = null;
                editClassificationSearchKeyword.value = '';
                
            } catch (error) {

                ElMessage.warning('分类信息获取失败，但已选择该分类');
                
                showEditClassificationDialog.value = false;
                editTempSelectedClassification.value = null;
                editClassificationSearchKeyword.value = '';
            }
        };
        
        const cancelEditClassificationSelection = () => {

            showEditClassificationDialog.value = false;
            editTempSelectedClassification.value = null;
            editClassificationSearchKeyword.value = '';
        };
        
        // 监听编辑分类对话框显示
        watch(showEditClassificationDialog, (newVal) => {
            if (newVal) {
    
                editTempSelectedClassification.value = null;
                editClassificationSearchKeyword.value = '';
                loadEditClassificationRootNodes();
            }
        });
        
        // 监听分类对话框显示，加载根节点数据
        watch(showClassificationDialog, (newVal) => {
            if (newVal) {
    
                // 清理临时选择状态
                tempSelectedClassification.value = null;
                classificationSearchKeyword.value = '';
                // 加载分类树数据
                loadClassificationRootNodes();
            } else {

            }
        });
        
        return{
            // 统一搜索相关
            searchType,
            handleUnifiedSearch,
            handleUnifiedReset,
            handleDeletePart,
            keywordInput,
            codeInput,
            edit,
            tableData1,
            add,
            form1,
            isextend1,
            isextend2,
            data1,
            fetchPartData,
            loading,
            loadClassificationTree,
            allCategories,
            buildCompleteTree,
            handleClassificationChange,
            loadClassificationAttributes,
            classificationAttributes,
            dynamicExtendedForm,
            showClassificationDialog,
            selectedClassificationName,
            selectedClassificationInfo,
            tempSelectedClassification,
            classificationSearchKeyword,
            classificationTreeRef,
            classificationTreeData,
            classificationTreeProps,
            loadClassificationRootNodes,
            loadClassificationNode,
            handleClassificationNodeClick,
            handleClassificationSearch,
            filterClassificationNode,
            confirmClassificationSelection,
            cancelClassificationSelection,
            
            // 创建功能
            createPart,
            resetForm,
            handleCancelAdd,
            getWarehouseName,
            
            // 编辑功能
            handleEditPart,
            handleCancelEdit,
            updatePart,
            resetEditForm,
            loadEditClassificationAttributes,
            editForm,
            editDynamicExtendedForm,
            editClassificationAttributes,
            editSelectedClassificationName,
            editSelectedClassificationInfo,
            currentEditingPart,
            
            // 编辑分类选择
            showEditClassificationDialog,
            editTempSelectedClassification,
            editClassificationSearchKeyword,
            editClassificationTreeRef,
            editClassificationTreeData,
            
            // 版本管理相关方法
            loadPartVersions,
            deleteLatestVersion,
            viewVersionDetail,
            formatVersionAttributes,
            getStateTagType,
            getStateText,
            formatDateTime,
            
            // 版本管理响应式数据
            partVersions,
            versionsLoading,
            deleteVersionLoading,
            showVersionDetailDialog,
            selectedVersionDetail,
            majorVersionCount,
            latestMajorVersion,
            latestMinorVersion,
            
            // BOM管理相关
            bomLoading,
            bomChildCount,
            bomParentCount,
            bomLevelCount,
            handleAddBomItem,
            loadBomChildParts,
            handleViewParentItems,
            handleTabClick,
            
            // BOM子部件列表相关
            bomChildParts,
            bomChildPartsLoading,
            handleEditBomChild,
            handleDeleteBomChild,
            
            // 修改BOM子项相关
            showEditBomChildDialog,
            editingBomChild,
            editBomChildLoading,
            editBomChildForm,
            confirmUpdateBomChild,
            
            // 查看父项相关
            showParentItemsDialog,
            parentParts,
            parentPartsLoading,
            
            // BOM树相关
            showBomTreeDialog,
            bomTreeData,
            bomTreeLoading,
            bomTreeRef,
            bomTreeStats,
            bomTreeProps,
            handleViewBomTree,
            loadBomTreeNode,
            updateBomTreeStats,
            refreshBomTree,
            
            // 新增子项对话框相关
            showAddBomItemDialog,
            bomParts,
            bomPartsLoading,
            bomSearchKeyword,
            filteredBomParts,
            loadAllPartsForBom,
            handleBomSearch,
            handleAddBomRelation,
            
            // BOM配置对话框相关
            showBomConfigDialog,
            selectedChildPart,
            bomConfigLoading,
            bomConfigForm,
            confirmCreateBomLink,
            loadEditClassificationRootNodes,
            loadEditClassificationNode,
            handleEditClassificationNodeClick,
            handleEditClassificationSearch,
            filterEditClassificationNode,
            confirmEditClassificationSelection,
            cancelEditClassificationSelection,
            
            // 循环引用检测相关
            getAllAncestorMasterIds,
            checkForCircularReference
        }
    }
}
</script>

<style lang="less" scoped>
.partbom-container {
    padding: 15px;
    background: #fff;
    border-radius: 8px;
    box-shadow: 0 2px 12px rgba(0,0,0,0.1);

    .search-section {
        margin-bottom: 15px;
        padding: 15px;
        background: #f8f9fa;
        border-radius: 8px;

        .unified-search {
            padding: 15px;
            background: #fff;
            border-radius: 8px;
            border: 1px solid #e4e7ed;

            .title-with-logo {
                display: flex;
                align-items: center;
                gap: 10px;
                margin-bottom: 15px;

                .logo-img {
                    width: 24px;
                    height: 24px;
                }

                h4 {
                    color: #333;
                    margin: 0;
                }
            }

            .search-options {
                margin-bottom: 20px;

                .search-option {
                    margin-bottom: 20px;
                    padding: 16px;
                    border: 1px solid #e4e7ed;
                    border-radius: 8px;
                    transition: all 0.3s ease;

                    &:hover {
                        border-color: #409eff;
                        box-shadow: 0 2px 8px rgba(64, 158, 255, 0.1);
                    }

                    .option-header {
                        display: flex;
                        align-items: center;
                        margin-bottom: 12px;

                        .option-description {
                            font-size: 12px;
                            color: #6b7280;
                            margin-left: 8px;
                        }
                    }

                    .option-content {
                        .search-input {
                            max-width: 400px;
                        }
                    }
                }
            }

            .search-options-horizontal {
                margin-bottom: 15px;
                display: flex;
                gap: 15px;
                align-items: flex-start;

                .search-option-horizontal {
                    flex: 1;
                    padding: 12px;
                    border: 1px solid #e4e7ed;
                    border-radius: 8px;
                    transition: all 0.3s ease;

                    &:hover {
                        border-color: #409eff;
                        box-shadow: 0 2px 8px rgba(64, 158, 255, 0.1);
                    }

                    .option-header {
                        display: flex;
                        align-items: center;
                        margin-bottom: 8px;
                    }

                    .option-content {
                        .search-input-compact {
                            width: 100%;
                            max-width: 280px;
                        }
                    }
                }
            }

            .search-actions {
                display: flex;
                gap: 12px;
                margin-bottom: 10px;

                .search-btn {
                    background: linear-gradient(145deg, #4481eb, #04befe);
                    border: none;
                    padding: 10px 24px;
                    border-radius: 20px;
                    display: flex;
                    align-items: center;
                    gap: 8px;
                    font-weight: 500;

                    &:hover {
                        opacity: 0.9;
                        transform: translateY(-1px);
                    }
                }

                .reset-btn {
                    background: linear-gradient(145deg, #909399, #a6a9ad);
                    border: none;
                    padding: 10px 24px;
                    border-radius: 20px;
                    display: flex;
                    align-items: center;
                    gap: 8px;
                    font-weight: 500;

                    &:hover {
                        opacity: 0.9;
                        transform: translateY(-1px);
                    }
                }
            }

            .search-tip {
                padding: 12px 16px;
                background: #f0f9ff;
                border-radius: 6px;
                border: 1px solid #b3d8ff;
            }
        }


    }

    .content-section {
        .action-bar {
            margin-bottom: 15px;
            display: flex;
            justify-content: space-between;
            align-items: center;

            .add-btn {
                background: linear-gradient(145deg, #4481eb, #04befe);
                border: none;
                padding: 8px 20px;
                border-radius: 20px;
                display: flex;
                align-items: center;
                gap: 5px;

                &:hover {
                    opacity: 0.9;
                }
            }

            .data-info {
                padding: 8px 12px;
                background: #f0f9ff;
                border-radius: 6px;
                border: 1px solid #e0f2fe;
            }
        }
    }
}

.custom-table {
    border-radius: 8px;
    overflow: hidden;
    box-shadow: 0 2px 12px rgba(0,0,0,0.1);

    :deep(.el-table__header) {
        background-color: #f8f9fa;
        th {
            background-color: #f8f9fa;
            color: #333;
            font-weight: 600;
        }
    }

    :deep(.el-button--link) {
        padding: 4px 8px;
        
        &.el-button--danger {
            color: #ff4d4f;
        }
    }
}

.custom-dialog {
    :deep(.el-dialog__header) {
        margin: 0;
        padding: 20px;
        background: linear-gradient(145deg, #4481eb, #04befe);
        border-radius: 8px 8px 0 0;

        .el-dialog__title {
            color: white;
            font-size: 18px;
        }
    }

    .custom-form {
        padding: 20px;

        .form-section {
            background: #f8f9fa;
            padding: 20px;
            border-radius: 8px;
            
            .attributes-info {
                margin-bottom: 15px;
                padding: 8px 12px;
                border-radius: 6px;
                background: #e8f4fd;
                border: 1px solid #b3d8ff;
            }
            
            .attribute-description {
                margin-top: 4px;
                font-size: 12px;
                color: #666;
            }
            
            .no-attributes-info {
                display: flex;
                flex-direction: column;
                align-items: center;
                justify-content: center;
                padding: 40px 20px;
                text-align: center;
                background: #fafafa;
                border: 2px dashed #d9d9d9;
                border-radius: 8px;
                min-height: 120px;
                
                .no-attributes-icon {
                    margin-bottom: 16px;
                    opacity: 0.6;
                }
                
                .no-attributes-text {
                    h4 {
                        margin: 0 0 8px 0;
                        color: #666;
                        font-size: 16px;
                        font-weight: 500;
                    }
                }
            }
        }

        :deep(.el-form-item__label) {
            color: #666;
        }

        :deep(.el-input__inner) {
            border-radius: 8px;
        }

        :deep(.el-textarea__inner) {
            border-radius: 8px;
        }

        .custom-select {
            width: 100%;
            
            :deep(.el-tree-select__tags) {
                max-height: 60px;
                overflow-y: auto;
            }
            
            :deep(.el-tree-select__popper) {
                max-height: 300px;
            }
            
            :deep(.el-tree-node__content) {
                height: auto;
                padding: 8px 0;
                
                &:hover {
                    background-color: #f5f7fa;
                }
            }
            
            :deep(.el-tree-node__label) {
                font-size: 14px;
                line-height: 1.4;
            }
        }
    }

    .dialog-footer {
        display: flex;
        justify-content: flex-end;
        gap: 15px;
        padding: 20px;

        .cancel-btn {
            padding: 8px 20px;
            border-radius: 20px;
        }

        .confirm-btn {
            padding: 8px 20px;
            border-radius: 20px;
            background: linear-gradient(145deg, #4481eb, #04befe);
            border: none;

            &:hover {
                opacity: 0.9;
            }
        }
    }
}

.dialog-content {
    padding: 20px;

    .section-title {
        color: #333;
        margin: 20px 0;
        font-size: 16px;
        font-weight: 600;
    }
}

.custom-tabs {
    :deep(.el-tabs__header) {
        margin-bottom: 20px;
    }

    :deep(.el-tabs__nav) {
        border-radius: 8px 8px 0 0;
    }

    :deep(.el-tabs__item) {
        padding: 0 20px;
        height: 40px;
        line-height: 40px;
        
        &.is-active {
            background: linear-gradient(145deg, #4481eb, #04befe);
            color: white;
        }
    }
}

.bom-actions {
    margin-bottom: 20px;
    display: flex;
    gap: 10px;

    .action-btn {
        background: linear-gradient(145deg, #4481eb, #04befe);
        border: none;
        padding: 8px 20px;
        border-radius: 20px;
        display: flex;
        align-items: center;
        gap: 5px;

        &:hover {
            opacity: 0.9;
        }
    }
}

.custom-drawer {
    :deep(.el-drawer__header) {
        margin-bottom: 0;
        padding: 20px;
        background: linear-gradient(145deg, #4481eb, #04befe);
        margin: 0;

        .el-drawer__title {
            color: white;
            font-size: 18px;
        }
    }

    .custom-tree {
        padding: 20px;
    }
}

.search-filters {
    margin-bottom: 20px;
    display: flex;
    flex-wrap: wrap;
    gap: 20px;
    align-items: center;

    .filter-item {
        display: flex;
        align-items: center;
        gap: 10px;

        .custom-input {
            width: 240px;
        }
    }

    .search-btn {
        background: linear-gradient(145deg, #4481eb, #04befe);
        border: none;
        padding: 8px 20px;
        border-radius: 20px;
        display: flex;
        align-items: center;
        gap: 5px;

        &:hover {
            opacity: 0.9;
        }
    }
}

// 分类选择相关样式
.classification-input {
    cursor: pointer;
    
    :deep(.el-input__inner) {
        cursor: pointer;
    }
    
    .classification-icon {
        transition: transform 0.3s ease;
    }
    
    &:hover .classification-icon {
        transform: rotate(180deg);
    }
}

// 分类选择对话框样式
.classification-dialog-content {
    .search-section {
        margin-bottom: 20px;
        
        .search-input {
            :deep(.el-input__inner) {
                border-radius: 8px;
            }
        }
    }
    
    .tree-section {
        max-height: 400px;
        overflow-y: auto;
        border: 1px solid #dcdfe6;
        border-radius: 8px;
        padding: 10px;
        
        .classification-tree {
            :deep(.el-tree-node__content) {
                height: auto;
                padding: 8px 0;
                
                &:hover {
                    background-color: #f5f7fa;
                }
            }
            
            :deep(.el-tree-node__label) {
                font-size: 14px;
                line-height: 1.4;
            }
            
            :deep(.el-tree-node__expand-icon) {
                color: #409eff;
            }
            
            :deep(.el-tree-node__expand-icon.expanded) {
                transform: rotate(90deg);
            }
        }
    }
    
    .selected-info {
        margin-top: 15px;
        padding: 10px;
        background: #f0f9ff;
        border-radius: 6px;
        border: 1px solid #b3d8ff;
        text-align: center;
    }
}

// 创建部件对话框美化样式
.create-part-dialog {
    :deep(.el-dialog) {
        border-radius: 16px;
        overflow: hidden;
        box-shadow: 0 25px 50px -12px rgba(0, 0, 0, 0.25);
    }

    :deep(.el-dialog__header) {
        margin: 0;
        padding: 24px 32px;
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        border-radius: 16px 16px 0 0;
        position: relative;
        
        &::before {
            content: '';
            position: absolute;
            top: 0;
            left: 0;
            right: 0;
            bottom: 0;
            background: linear-gradient(135deg, rgba(255,255,255,0.1) 0%, rgba(255,255,255,0.05) 100%);
            pointer-events: none;
        }

        .el-dialog__title {
            color: white;
            font-size: 20px;
            font-weight: 600;
            text-shadow: 0 2px 4px rgba(0,0,0,0.1);
        }

        .el-dialog__headerbtn {
            .el-dialog__close {
                color: rgba(255,255,255,0.8);
                font-size: 18px;
                
                &:hover {
                    color: white;
                }
            }
        }
    }

    :deep(.el-dialog__body) {
        padding: 0;
        background: #f8fafc;
    }

    :deep(.el-dialog__footer) {
        padding: 0;
        background: transparent;
    }
}

.create-dialog-content {
    padding: 32px;
    background: #f8fafc;
    
    .info-card {
        background: white;
        border-radius: 16px;
        box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1), 0 2px 4px -1px rgba(0, 0, 0, 0.06);
        margin-bottom: 24px;
        overflow: hidden;
        transition: all 0.3s ease;
        
        &:hover {
            box-shadow: 0 10px 15px -3px rgba(0, 0, 0, 0.1), 0 4px 6px -2px rgba(0, 0, 0, 0.05);
            transform: translateY(-2px);
        }
        
        .card-header {
            padding: 24px 32px 16px;
            background: linear-gradient(135deg, #f8fafc 0%, #e2e8f0 100%);
            border-bottom: 1px solid #e2e8f0;
            display: flex;
            align-items: center;
            gap: 12px;
            
            .header-icon {
                width: 40px;
                height: 40px;
                border-radius: 10px;
                background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                display: flex;
                align-items: center;
                justify-content: center;
                color: white;
                box-shadow: 0 4px 6px -1px rgba(102, 126, 234, 0.3);
            }
            
            .card-title {
                margin: 0;
                font-size: 18px;
                font-weight: 600;
                color: #1e293b;
                flex: 1;
            }
            
            .card-subtitle {
                font-size: 14px;
                color: #64748b;
                margin-left: auto;
            }
        }
        
        .card-content {
            padding: 32px;
        }
    }
}

.enhanced-form {
    .form-grid {
        display: grid;
        grid-template-columns: 1fr 1fr;
        gap: 24px 32px;
        margin-bottom: 24px;
    }
    
    .classification-section {
        margin-top: 24px;
        padding-top: 24px;
        border-top: 1px solid #e2e8f0;
    }
    
    .form-item-enhanced {
        margin-bottom: 0;
        
        :deep(.el-form-item__label) {
            color: #374151;
            font-weight: 500;
            font-size: 14px;
            margin-bottom: 8px;
            line-height: 1.5;
        }
        
        .enhanced-input {
            :deep(.el-input__wrapper) {
                border-radius: 12px;
                border: 2px solid #e5e7eb;
                box-shadow: 0 1px 3px 0 rgba(0, 0, 0, 0.1);
                transition: all 0.3s ease;
                padding: 12px 16px;
                
                &:hover {
                    border-color: #d1d5db;
                }
                
                &.is-focus {
                    border-color: #667eea;
                    box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
                }
            }
            
            :deep(.el-input__inner) {
                font-size: 14px;
                color: #374151;
                
                &::placeholder {
                    color: #9ca3af;
                }
            }
        }
        
        .enhanced-select {
            width: 100%;
            
            :deep(.el-select__wrapper) {
                border-radius: 12px;
                border: 2px solid #e5e7eb;
                box-shadow: 0 1px 3px 0 rgba(0, 0, 0, 0.1);
                transition: all 0.3s ease;
                padding: 12px 16px;
                
                &:hover {
                    border-color: #d1d5db;
                }
                
                &.is-focus {
                    border-color: #667eea;
                    box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
                }
            }
        }
    }
    
    .classification-form-item {
        grid-column: 1 / -1;
        
        .classification-wrapper {
            .classification-input {
                cursor: pointer;
                
                :deep(.el-input__wrapper) {
                    cursor: pointer;
                    background: linear-gradient(135deg, #f8fafc 0%, #f1f5f9 100%);
                    
                    &:hover {
                        background: linear-gradient(135deg, #f1f5f9 0%, #e2e8f0 100%);
                        border-color: #667eea;
                    }
                }
                
                .classification-arrow {
                    transition: transform 0.3s ease;
                    color: #667eea;
                }
                
                &:hover .classification-arrow {
                    transform: rotate(180deg);
                }
            }
            
            .classification-hint {
                margin-top: 8px;
                padding: 8px 12px;
                background: #eff6ff;
                border-radius: 8px;
                border: 1px solid #bfdbfe;
            }
        }
    }
}

.option-item {
    display: flex;
    align-items: center;
    gap: 12px;
    
    .option-code {
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        color: white;
        padding: 2px 8px;
        border-radius: 6px;
        font-size: 12px;
        font-weight: 600;
        min-width: 28px;
        text-align: center;
    }
    
    .option-desc {
        color: #64748b;
        font-size: 14px;
    }
}

.attributes-header {
    margin-bottom: 24px;
    
    .attributes-badge {
        display: inline-flex;
        align-items: center;
        gap: 8px;
        background: linear-gradient(135deg, #eff6ff 0%, #dbeafe 100%);
        color: #1e40af;
        padding: 8px 16px;
        border-radius: 20px;
        font-size: 14px;
        font-weight: 500;
        border: 1px solid #bfdbfe;
    }
}

.attributes-grid {
    display: grid;
    grid-template-columns: 1fr 1fr;
    gap: 24px 32px;
    
    .attribute-item {
        .attribute-input-wrapper {
            width: 100%;
        }
        
        .attribute-meta {
            margin-top: 6px;
            padding: 4px 8px;
            background: #f8fafc;
            border-radius: 6px;
            font-size: 12px;
        }
    }
    
    .unit-suffix {
        color: #6b7280;
        font-size: 12px;
        font-weight: 500;
        padding: 0 8px;
        background: #f3f4f6;
        border-radius: 4px;
        border-left: 1px solid #e5e7eb;
        margin-left: 8px;
        white-space: nowrap;
    }
}

.empty-state {
    text-align: center;
    padding: 48px 24px;
    
    .empty-icon {
        margin-bottom: 16px;
        opacity: 0.6;
    }
    
    .empty-content {
        h4 {
            margin: 0 0 8px 0;
            color: #374151;
            font-size: 16px;
            font-weight: 500;
        }
        
        p {
            margin: 0;
            color: #6b7280;
            font-size: 14px;
            line-height: 1.5;
        }
    }
}

.no-attributes-state {
    background: linear-gradient(135deg, #fefce8 0%, #fef3c7 100%);
    border: 1px solid #fed7aa;
    border-radius: 12px;
}

.no-classification-state {
    background: linear-gradient(135deg, #f0f9ff 0%, #e0f2fe 100%);
    border: 1px solid #bae6fd;
    border-radius: 12px;
}

.enhanced-dialog-footer {
    padding: 24px 32px;
    background: white;
    border-top: 1px solid #e2e8f0;
    display: flex;
    justify-content: flex-end;
    gap: 16px;
    
    .enhanced-cancel-btn {
        padding: 12px 24px;
        border-radius: 10px;
        border: 2px solid #e5e7eb;
        background: white;
        color: #6b7280;
        font-weight: 500;
        transition: all 0.3s ease;
        display: flex;
        align-items: center;
        gap: 8px;
        
        &:hover {
            border-color: #d1d5db;
            background: #f9fafb;
            color: #374151;
            transform: translateY(-1px);
        }
    }
    
    .enhanced-confirm-btn {
        padding: 12px 24px;
        border-radius: 10px;
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        border: none;
        color: white;
        font-weight: 500;
        box-shadow: 0 4px 6px -1px rgba(102, 126, 234, 0.3);
        transition: all 0.3s ease;
        display: flex;
        align-items: center;
        gap: 8px;
        
        &:hover {
            opacity: 0.9;
            transform: translateY(-1px);
            box-shadow: 0 6px 8px -1px rgba(102, 126, 234, 0.4);
        }
        
        &.is-loading {
            cursor: not-allowed;
            opacity: 0.7;
        }
    }
}

// 版本管理样式
.version-management-content {
    .version-actions {
        display: flex;
        gap: 12px;
        align-items: center;
        
        .el-button {
            border-radius: 8px;
            padding: 8px 16px;
            font-weight: 500;
            transition: all 0.3s ease;
            
            &:hover {
                transform: translateY(-1px);
                box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
            }
            
            &.el-button--danger {
                background: linear-gradient(135deg, #ef4444 0%, #dc2626 100%);
                border: none;
                color: white;
                
                &:hover {
                    background: linear-gradient(135deg, #dc2626 0%, #b91c1c 100%);
                }
                
                &:disabled {
                    background: #d1d5db;
                    color: #9ca3af;
                    cursor: not-allowed;
                    transform: none;
                    box-shadow: none;
                }
            }
            
            &.el-button--primary {
                background: linear-gradient(135deg, #3b82f6 0%, #2563eb 100%);
                border: none;
                
                &:hover {
                    background: linear-gradient(135deg, #2563eb 0%, #1d4ed8 100%);
                }
            }
        }
    }
}

.version-management-content {
    .version-header {
        display: flex;
        justify-content: space-between;
        align-items: flex-start;
        margin-bottom: 20px;
        padding: 16px;
        background: #f8f9fa;
        border-radius: 8px;
        
        .version-info {
            h4 {
                margin: 0 0 8px 0;
                color: #409eff;
                font-size: 18px;
                font-weight: 600;
            }
            
            p {
                margin: 0;
                color: #666;
                font-size: 14px;
            }
        }
        
        .version-actions {
            display: flex;
            gap: 10px;
        }
    }
    
    .version-table-container {
        margin-bottom: 20px;
        
        .version-table {
            .el-table__row {
                cursor: pointer;
                
                &:hover {
                    background-color: #f5f7fa;
                }
            }
        }
    }
    
    .version-summary {
        padding: 16px;
        background: #f0f9ff;
        border-radius: 8px;
        border: 1px solid #b3d8ff;
        
        .el-statistic {
            text-align: center;
        }
    }
}

.version-detail-dialog {
    .version-detail-content {
        .version-attributes {
            margin-top: 20px;
            
            h4 {
                margin-bottom: 10px;
                color: #409eff;
            }
        }
        
        .version-json {
            margin-top: 20px;
            
            h4 {
                margin-bottom: 10px;
                color: #409eff;
            }
            
            .json-container {
                max-height: 400px;
                overflow-y: auto;
                border: 1px solid #dcdfe6;
                border-radius: 4px;
                
                .json-content {
                    margin: 0;
                    padding: 16px;
                    background: #f8f9fa;
                    font-family: 'Courier New', monospace;
                    font-size: 12px;
                    line-height: 1.5;
                    white-space: pre-wrap;
                    word-wrap: break-word;
                }
            }
        }
    }
    
    .dialog-footer {
        display: flex;
        justify-content: flex-end;
        gap: 10px;
    }
}

// 编辑部件对话框样式
.edit-part-dialog {
    :deep(.el-dialog) {
        border-radius: 16px;
        overflow: hidden;
        box-shadow: 0 25px 50px -12px rgba(0, 0, 0, 0.25);
    }

    :deep(.el-dialog__header) {
        margin: 0;
        padding: 24px 32px;
        background: linear-gradient(135deg, #f59e0b 0%, #d97706 100%);
        border-radius: 16px 16px 0 0;
        position: relative;
        
        &::before {
            content: '';
            position: absolute;
            top: 0;
            left: 0;
            right: 0;
            bottom: 0;
            background: linear-gradient(135deg, rgba(255,255,255,0.1) 0%, rgba(255,255,255,0.05) 100%);
            pointer-events: none;
        }

        .el-dialog__title {
            color: white;
            font-size: 20px;
            font-weight: 600;
            text-shadow: 0 2px 4px rgba(0,0,0,0.1);
        }

        .el-dialog__headerbtn {
            .el-dialog__close {
                color: rgba(255,255,255,0.8);
                font-size: 18px;
                
                &:hover {
                    color: white;
                }
            }
        }
    }

    :deep(.el-dialog__body) {
        padding: 0;
        background: #f8fafc;
    }

    :deep(.el-dialog__footer) {
        padding: 0;
        background: transparent;
    }
}

.edit-dialog-content {
    padding: 32px;
    background: #f8fafc;
    
    .info-card {
        background: white;
        border-radius: 16px;
        box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1), 0 2px 4px -1px rgba(0, 0, 0, 0.06);
        margin-bottom: 24px;
        overflow: hidden;
        transition: all 0.3s ease;
        
        &:hover {
            box-shadow: 0 10px 15px -3px rgba(0, 0, 0, 0.1), 0 4px 6px -2px rgba(0, 0, 0, 0.05);
            transform: translateY(-2px);
        }
        
        .card-header {
            padding: 24px 32px 16px;
            background: linear-gradient(135deg, #f8fafc 0%, #e2e8f0 100%);
            border-bottom: 1px solid #e2e8f0;
            display: flex;
            align-items: center;
            gap: 12px;
            
            .header-icon {
                width: 40px;
                height: 40px;
                border-radius: 10px;
                background: linear-gradient(135deg, #f59e0b 0%, #d97706 100%);
                display: flex;
                align-items: center;
                justify-content: center;
                color: white;
                box-shadow: 0 4px 6px -1px rgba(245, 158, 11, 0.3);
            }
            
            .card-title {
                margin: 0;
                font-size: 18px;
                font-weight: 600;
                color: #1e293b;
                flex: 1;
            }
            
            .card-subtitle {
                font-size: 14px;
                color: #64748b;
                margin-left: auto;
            }
        }
        
        .card-content {
            padding: 32px;
        }
    }
}

.enhanced-tabs {
    :deep(.el-tabs__header) {
        margin-bottom: 0;
        background: white;
        padding: 0 32px;
        border-bottom: 1px solid #e2e8f0;
    }

    :deep(.el-tabs__nav) {
        border-radius: 0;
        border: none;
    }

    :deep(.el-tabs__item) {
        padding: 16px 24px;
        height: auto;
        line-height: 1.5;
        font-weight: 500;
        border: none;
        color: #64748b;
        transition: all 0.3s ease;
        
        &.is-active {
            background: linear-gradient(135deg, #f59e0b 0%, #d97706 100%);
            color: white;
            border-radius: 8px 8px 0 0;
        }
        
        &:hover:not(.is-active) {
            background: #f1f5f9;
            color: #374151;
        }
    }
    
    :deep(.el-tabs__content) {
        padding: 0;
    }
    
    :deep(.el-tab-pane) {
        background: #f8fafc;
        min-height: 400px;
    }
}

// 确保编辑对话框中的表单样式正确应用
.edit-part-dialog {
    .enhanced-form {
        .form-grid {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 24px 32px;
            margin-bottom: 24px;
        }
        
        .classification-section {
            margin-top: 24px;
            padding-top: 24px;
            border-top: 1px solid #e2e8f0;
        }
        
        .form-item-enhanced {
            margin-bottom: 0;
            
            :deep(.el-form-item__label) {
                color: #374151;
                font-weight: 500;
                font-size: 14px;
                margin-bottom: 8px;
                line-height: 1.5;
            }
            
            .enhanced-input {
                :deep(.el-input__wrapper) {
                    border-radius: 12px;
                    border: 2px solid #e5e7eb;
                    box-shadow: 0 1px 3px 0 rgba(0, 0, 0, 0.1);
                    transition: all 0.3s ease;
                    padding: 12px 16px;
                    
                    &:hover {
                        border-color: #d1d5db;
                    }
                    
                    &.is-focus {
                        border-color: #f59e0b;
                        box-shadow: 0 0 0 3px rgba(245, 158, 11, 0.1);
                    }
                }
                
                :deep(.el-input__inner) {
                    font-size: 14px;
                    color: #374151;
                    
                    &::placeholder {
                        color: #9ca3af;
                    }
                }
            }
            
            .enhanced-select {
                width: 100%;
                
                :deep(.el-select__wrapper) {
                    border-radius: 12px;
                    border: 2px solid #e5e7eb;
                    box-shadow: 0 1px 3px 0 rgba(0, 0, 0, 0.1);
                    transition: all 0.3s ease;
                    padding: 12px 16px;
                    
                    &:hover {
                        border-color: #d1d5db;
                    }
                    
                    &.is-focus {
                        border-color: #f59e0b;
                        box-shadow: 0 0 0 3px rgba(245, 158, 11, 0.1);
                    }
                }
            }
        }
        
        .classification-form-item {
            grid-column: 1 / -1;
        }
        
        .classification-wrapper {
            .classification-input {
                cursor: pointer;
                
                :deep(.el-input__wrapper) {
                    cursor: pointer;
                    background: linear-gradient(135deg, #f8fafc 0%, #f1f5f9 100%);
                    
                    &:hover {
                        background: linear-gradient(135deg, #f1f5f9 0%, #e2e8f0 100%);
                        border-color: #f59e0b;
                    }
                }
                
                .classification-arrow {
                    transition: transform 0.3s ease;
                    color: #f59e0b;
                }
                
                &:hover .classification-arrow {
                    transform: rotate(180deg);
                }
            }
            
            .classification-hint {
                margin-top: 8px;
                padding: 8px 12px;
                background: #fef3c7;
                border-radius: 8px;
                border: 1px solid #fbbf24;
            }
        }
    }
    
    .attributes-header {
        margin-bottom: 24px;
        
        .attributes-badge {
            display: inline-flex;
            align-items: center;
            gap: 8px;
            background: linear-gradient(135deg, #fef3c7 0%, #fde68a 100%);
            color: #92400e;
            padding: 8px 16px;
            border-radius: 20px;
            font-size: 14px;
            font-weight: 500;
            border: 1px solid #fbbf24;
        }
    }
    
    .attributes-grid {
        display: grid;
        grid-template-columns: 1fr 1fr;
        gap: 24px 32px;
        
        .attribute-item {
            .attribute-meta {
                margin-top: 6px;
                padding: 4px 8px;
                background: #f8fafc;
                border-radius: 6px;
                font-size: 12px;
            }
        }
    }
    
    .empty-state {
        text-align: center;
        padding: 48px 24px;
        
        .empty-icon {
            margin-bottom: 16px;
            opacity: 0.6;
        }
        
        .empty-content {
            h4 {
                margin: 0 0 8px 0;
                color: #374151;
                font-size: 16px;
                font-weight: 500;
            }
            
            p {
                margin: 0;
                color: #6b7280;
                font-size: 14px;
                line-height: 1.5;
            }
        }
    }
    
    .no-attributes-state {
        background: linear-gradient(135deg, #fefce8 0%, #fef3c7 100%);
        border: 1px solid #fed7aa;
        border-radius: 12px;
    }
    
    .no-classification-state {
        background: linear-gradient(135deg, #fef3c7 0%, #fde68a 100%);
        border: 1px solid #fbbf24;
        border-radius: 12px;
    }
    
    .enhanced-dialog-footer {
        padding: 24px 32px;
        background: white;
        border-top: 1px solid #e2e8f0;
        display: flex;
        justify-content: flex-end;
        gap: 16px;
        
        .enhanced-cancel-btn {
            padding: 12px 24px;
            border-radius: 10px;
            border: 2px solid #e5e7eb;
            background: white;
            color: #6b7280;
            font-weight: 500;
            transition: all 0.3s ease;
            display: flex;
            align-items: center;
            gap: 8px;
            
            &:hover {
                border-color: #d1d5db;
                background: #f9fafb;
                color: #374151;
                transform: translateY(-1px);
            }
        }
        
        .enhanced-confirm-btn {
            padding: 12px 24px;
            border-radius: 10px;
            background: linear-gradient(135deg, #f59e0b 0%, #d97706 100%);
            border: none;
            color: white;
            font-weight: 500;
            box-shadow: 0 4px 6px -1px rgba(245, 158, 11, 0.3);
            transition: all 0.3s ease;
            display: flex;
            align-items: center;
            gap: 8px;
            
            &:hover {
                opacity: 0.9;
                transform: translateY(-1px);
                box-shadow: 0 6px 8px -1px rgba(245, 158, 11, 0.4);
            }
            
            &.is-loading {
                cursor: not-allowed;
                opacity: 0.7;
            }
        }
    }
}

// BOM管理样式
.bom-management-content {
    .bom-header {
        display: flex;
        justify-content: space-between;
        align-items: flex-start;
        margin-bottom: 20px;
        padding: 16px;
        background: #f8f9fa;
        border-radius: 8px;
        
        .bom-info {
            h4 {
                margin: 0 0 8px 0;
                color: #409eff;
                font-size: 18px;
                font-weight: 600;
            }
            
            p {
                margin: 0;
                color: #666;
                font-size: 14px;
            }
        }
        
        .bom-actions {
            display: flex;
            gap: 12px;
            align-items: center;
            
            .el-button {
                border-radius: 8px;
                padding: 8px 16px;
                font-weight: 500;
                transition: all 0.3s ease;
                display: flex;
                align-items: center;
                gap: 6px;
                
                &:hover {
                    transform: translateY(-1px);
                    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
                }
                
                &.el-button--primary {
                    background: linear-gradient(135deg, #3b82f6 0%, #2563eb 100%);
                    border: none;
                    
                    &:hover {
                        background: linear-gradient(135deg, #2563eb 0%, #1d4ed8 100%);
                    }
                }
                
                &.el-button--success {
                    background: linear-gradient(135deg, #10b981 0%, #059669 100%);
                    border: none;
                    
                    &:hover {
                        background: linear-gradient(135deg, #059669 0%, #047857 100%);
                    }
                }
                
                &.el-button--info {
                    background: linear-gradient(135deg, #6b7280 0%, #4b5563 100%);
                    border: none;
                    
                    &:hover {
                        background: linear-gradient(135deg, #4b5563 0%, #374151 100%);
                    }
                }
                
                &:disabled {
                    background: #d1d5db;
                    color: #9ca3af;
                    cursor: not-allowed;
                    transform: none;
                    box-shadow: none;
                }
            }
        }
    }
    
    .bom-content {
        .bom-child-parts {
            margin-bottom: 20px;
            
            h4 {
                margin: 0 0 16px 0;
                color: #409eff;
                font-size: 16px;
                font-weight: 600;
            }
            
            .child-parts-table-container {
                background: white;
                border-radius: 12px;
                overflow: hidden;
                box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
                
                .child-parts-table {
                    :deep(.el-table__header) {
                        background-color: #f8f9fa;
                        th {
                            background-color: #f8f9fa;
                            color: #333;
                            font-weight: 600;
                            border-bottom: 2px solid #e2e8f0;
                        }
                    }
                    
                    :deep(.el-table__row) {
                        transition: all 0.3s ease;
                        
                        &:hover {
                            background-color: #f0f9ff;
                        }
                    }
                    
                    :deep(.el-button) {
                        border-radius: 6px;
                        padding: 4px 8px;
                        font-size: 12px;
                        font-weight: 500;
                        transition: all 0.3s ease;
                        
                        &.el-button--primary {
                            &:hover {
                                transform: translateY(-1px);
                                box-shadow: 0 2px 8px rgba(64, 158, 255, 0.3);
                            }
                        }
                        
                        &.el-button--danger {
                            &:hover {
                                transform: translateY(-1px);
                                box-shadow: 0 2px 8px rgba(245, 101, 101, 0.3);
                            }
                        }
                    }
                }
            }
        }
        
        .bom-summary {
            padding: 16px;
            background: #f0f9ff;
            border-radius: 8px;
            border: 1px solid #b3d8ff;
            
            .el-statistic {
                text-align: center;
                
                :deep(.el-statistic__head) {
                    color: #64748b;
                    font-size: 14px;
                    font-weight: 500;
                    margin-bottom: 8px;
                }
                
                :deep(.el-statistic__content) {
                    color: #1e40af;
                    font-size: 24px;
                    font-weight: 600;
                }
            }
        }
    }
}

// 新增子项对话框样式
.add-bom-item-dialog {
    :deep(.el-dialog) {
        border-radius: 16px;
        overflow: hidden;
        box-shadow: 0 25px 50px -12px rgba(0, 0, 0, 0.25);
    }

    :deep(.el-dialog__header) {
        margin: 0;
        padding: 24px 32px;
        background: linear-gradient(135deg, #10b981 0%, #059669 100%);
        border-radius: 16px 16px 0 0;
        position: relative;
        
        &::before {
            content: '';
            position: absolute;
            top: 0;
            left: 0;
            right: 0;
            bottom: 0;
            background: linear-gradient(135deg, rgba(255,255,255,0.1) 0%, rgba(255,255,255,0.05) 100%);
            pointer-events: none;
        }

        .el-dialog__title {
            color: white;
            font-size: 20px;
            font-weight: 600;
            text-shadow: 0 2px 4px rgba(0,0,0,0.1);
        }

        .el-dialog__headerbtn {
            .el-dialog__close {
                color: rgba(255,255,255,0.8);
                font-size: 18px;
                
                &:hover {
                    color: white;
                }
            }
        }
    }

    :deep(.el-dialog__body) {
        padding: 0;
        background: #f8fafc;
    }

    :deep(.el-dialog__footer) {
        padding: 0;
        background: transparent;
    }
}

.add-bom-dialog-content {
    padding: 24px;
    background: #f8fafc;
    
    .dialog-header-info {
                display: flex;
                justify-content: space-between;
        align-items: flex-start;
        margin-bottom: 20px;
        padding: 16px;
        background: white;
        border-radius: 12px;
        box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
        
        .parent-part-info {
            h4 {
                margin: 0 0 8px 0;
                color: #10b981;
                font-size: 16px;
                font-weight: 600;
            }
            
            p {
                    margin: 0;
                color: #666;
                font-size: 14px;
            }
        }
        
        .search-section {
            display: flex;
            gap: 12px;
                align-items: center;
            
            .search-input {
                :deep(.el-input__wrapper) {
                    border-radius: 8px;
                    border: 2px solid #e5e7eb;
                    transition: all 0.3s ease;
                    
                    &:hover {
                        border-color: #10b981;
                    }
                    
                    &.is-focus {
                        border-color: #10b981;
                        box-shadow: 0 0 0 3px rgba(16, 185, 129, 0.1);
                    }
                }
            }
            
            .el-button {
                border-radius: 8px;
                padding: 8px 16px;
                font-weight: 500;
                transition: all 0.3s ease;
                
                &:hover {
                    transform: translateY(-1px);
                    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
                }
                
                &.el-button--primary {
                    background: linear-gradient(135deg, #10b981 0%, #059669 100%);
                    border: none;
                    
                    &:hover {
                        background: linear-gradient(135deg, #059669 0%, #047857 100%);
                    }
                }
            }
        }
    }
    
    .parts-table-container {
        background: white;
        border-radius: 12px;
        overflow: hidden;
        box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
                margin-bottom: 16px;
        
        .bom-parts-table {
            :deep(.el-table__header) {
                background-color: #f8f9fa;
                th {
                    background-color: #f8f9fa;
                    color: #333;
                    font-weight: 600;
                    border-bottom: 2px solid #e2e8f0;
                }
            }
            
            :deep(.el-table__row) {
                transition: all 0.3s ease;
                
                &:hover {
                    background-color: #f0fdf4;
                }
            }
            
            :deep(.el-button) {
                border-radius: 6px;
                padding: 4px 12px;
                font-size: 12px;
                font-weight: 500;
                transition: all 0.3s ease;
                
                &.el-button--primary {
                    background: linear-gradient(135deg, #10b981 0%, #059669 100%);
                    border: none;
                    
                    &:hover {
                        background: linear-gradient(135deg, #059669 0%, #047857 100%);
                        transform: translateY(-1px);
                        box-shadow: 0 2px 8px rgba(16, 185, 129, 0.3);
                    }
                    
                    &:disabled {
                        background: #d1d5db;
                        color: #9ca3af;
                        cursor: not-allowed;
                        transform: none;
                        box-shadow: none;
                    }
                }
            }
        }
    }
    
    .parts-summary {
        padding: 12px 16px;
        background: #f0fdf4;
        border-radius: 8px;
        border: 1px solid #bbf7d0;
        text-align: center;
    }
    
    .dialog-footer {
        padding: 20px 24px;
        background: white;
        border-top: 1px solid #e2e8f0;
        display: flex;
        justify-content: flex-end;
        
        .cancel-btn {
            padding: 10px 24px;
            border-radius: 8px;
            border: 2px solid #e5e7eb;
            background: white;
            color: #6b7280;
            font-weight: 500;
            transition: all 0.3s ease;
            
            &:hover {
                border-color: #d1d5db;
                background: #f9fafb;
                    color: #374151;
                transform: translateY(-1px);
            }
        }
    }
}

// 查看父项对话框样式
.parent-items-dialog {
    :deep(.el-dialog) {
        border-radius: 16px;
        overflow: hidden;
        box-shadow: 0 25px 50px -12px rgba(0, 0, 0, 0.25);
    }

    :deep(.el-dialog__header) {
        margin: 0;
        padding: 24px 32px;
        background: linear-gradient(135deg, #6b7280 0%, #4b5563 100%);
        border-radius: 16px 16px 0 0;
        position: relative;
        
        &::before {
            content: '';
            position: absolute;
            top: 0;
            left: 0;
            right: 0;
            bottom: 0;
            background: linear-gradient(135deg, rgba(255,255,255,0.1) 0%, rgba(255,255,255,0.05) 100%);
            pointer-events: none;
        }

        .el-dialog__title {
            color: white;
            font-size: 20px;
            font-weight: 600;
            text-shadow: 0 2px 4px rgba(0,0,0,0.1);
        }

        .el-dialog__headerbtn {
            .el-dialog__close {
                color: rgba(255,255,255,0.8);
                font-size: 18px;
                
                &:hover {
                    color: white;
                }
            }
        }
    }

    :deep(.el-dialog__body) {
        padding: 0;
        background: #f8fafc;
    }

    :deep(.el-dialog__footer) {
        padding: 0;
        background: transparent;
    }
}

.parent-items-content {
    padding: 24px;
    background: #f8fafc;
    
    .current-part-info {
        margin-bottom: 24px;
        padding: 16px;
        background: white;
        border-radius: 12px;
        box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
        
        h4 {
            margin: 0 0 8px 0;
            color: #6b7280;
            font-size: 16px;
            font-weight: 600;
        }
        
        p {
                    margin: 0;
            color: #666;
            font-size: 14px;
            line-height: 1.5;
        }
    }
    
    .parent-parts-section {
        h4 {
            margin: 0 0 16px 0;
            color: #409eff;
                    font-size: 16px;
                    font-weight: 600;
        }
        
        .parent-parts-table-container {
            background: white;
            border-radius: 12px;
            overflow: hidden;
            box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
            margin-bottom: 16px;
            
            .parent-parts-table {
                :deep(.el-table__header) {
                    background-color: #f8f9fa;
                    th {
                        background-color: #f8f9fa;
                        color: #333;
                        font-weight: 600;
                        border-bottom: 2px solid #e2e8f0;
                    }
                }
                
                :deep(.el-table__row) {
                    transition: all 0.3s ease;
                    
                    &:hover {
                        background-color: #f0f9ff;
                    }
                }
            }
        }
    }
    
    .parent-summary {
        padding: 12px 16px;
        background: #f0f9ff;
                border-radius: 8px;
        border: 1px solid #b3d8ff;
        text-align: center;
    }
    
    .dialog-footer {
        padding: 20px 24px;
        background: white;
        border-top: 1px solid #e2e8f0;
        display: flex;
        justify-content: flex-end;
        
        .confirm-btn {
            padding: 10px 24px;
            border-radius: 8px;
            background: linear-gradient(135deg, #6b7280 0%, #4b5563 100%);
            border: none;
            color: white;
            font-weight: 500;
            box-shadow: 0 4px 6px -1px rgba(107, 114, 128, 0.3);
            transition: all 0.3s ease;
            
            &:hover {
                background: linear-gradient(135deg, #4b5563 0%, #374151 100%);
                transform: translateY(-1px);
                box-shadow: 0 6px 8px -1px rgba(107, 114, 128, 0.4);
            }
        }
    }
}

// BOM树对话框样式
.bom-tree-dialog {
    :deep(.el-dialog) {
        border-radius: 16px;
        overflow: hidden;
        box-shadow: 0 25px 50px -12px rgba(0, 0, 0, 0.25);
    }

    :deep(.el-dialog__header) {
                    margin: 0;
        padding: 24px 32px;
        background: linear-gradient(135deg, #3b82f6 0%, #2563eb 100%);
        border-radius: 16px 16px 0 0;
        position: relative;
        
        &::before {
            content: '';
            position: absolute;
            top: 0;
            left: 0;
            right: 0;
            bottom: 0;
            background: linear-gradient(135deg, rgba(255,255,255,0.1) 0%, rgba(255,255,255,0.05) 100%);
            pointer-events: none;
        }

        .el-dialog__title {
            color: white;
            font-size: 20px;
            font-weight: 600;
            text-shadow: 0 2px 4px rgba(0,0,0,0.1);
        }

        .el-dialog__headerbtn {
            .el-dialog__close {
                color: rgba(255,255,255,0.8);
                font-size: 18px;
                
                &:hover {
                    color: white;
                }
            }
        }
    }

    :deep(.el-dialog__body) {
        padding: 0;
        background: #f8fafc;
    }

    :deep(.el-dialog__footer) {
        padding: 0;
        background: transparent;
    }
}

.bom-tree-content {
    padding: 24px;
    background: #f8fafc;
    
    .current-part-info {
        margin-bottom: 24px;
                    padding: 16px;
        background: white;
        border-radius: 12px;
        box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
        
        h4 {
            margin: 0 0 8px 0;
            color: #3b82f6;
            font-size: 16px;
            font-weight: 600;
        }
        
        p {
            margin: 0;
            color: #666;
            font-size: 14px;
                    line-height: 1.5;
        }
    }
    
    .bom-tree-section {
        margin-bottom: 24px;
        
        h4 {
            margin: 0 0 16px 0;
            color: #409eff;
            font-size: 16px;
            font-weight: 600;
        }
        
        .bom-tree-container {
            background: white;
            border-radius: 12px;
            padding: 16px;
            box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
            min-height: 400px;
            
            .bom-tree {
                :deep(.el-tree-node__content) {
                    padding: 8px 0;
                    border-radius: 6px;
                    margin: 2px 0;
                    transition: all 0.3s ease;
                    
                    &:hover {
                        background-color: #f0f9ff;
                    }
                    
                    .el-tree-node__expand-icon {
                        color: #3b82f6;
                        font-size: 14px;
                        
                        &.is-leaf {
                            color: #9ca3af;
                        }
                    }
                }
                
                :deep(.el-tree-node__label) {
                    font-size: 14px;
                    color: #374151;
                    font-weight: 500;
                }
                
                .bom-tree-node {
                    display: flex;
                    align-items: center;
                    flex: 1;
                    padding: 4px 8px;
                    
                    .node-icon {
                        margin-right: 8px;
                        font-size: 16px;
                        
                        &.root-icon {
                            color: #3b82f6;
                        }
                        
                        &.child-icon {
                            color: #10b981;
                        }
                    }
                    
                    .node-label {
                        flex: 1;
                        font-size: 14px;
                        color: #374151;
                        font-weight: 500;
                    }
                    
                    .node-info {
                        display: flex;
                        gap: 6px;
                        margin-left: 12px;
                        
                        .el-tag {
                            font-size: 11px;
                            padding: 2px 6px;
                            border-radius: 4px;
                        }
                    }
                }
            }
        }
    }
    
    .bom-tree-summary {
        padding: 16px;
        background: #f0f9ff;
        border-radius: 8px;
        border: 1px solid #b3d8ff;
        
        .el-statistic {
            text-align: center;
            
            :deep(.el-statistic__head) {
                color: #64748b;
                font-size: 14px;
                font-weight: 500;
                margin-bottom: 8px;
            }
            
            :deep(.el-statistic__content) {
                color: #1e40af;
                font-size: 24px;
                font-weight: 600;
            }
        }
    }
    
    .dialog-footer {
        padding: 20px 24px;
        background: white;
        border-top: 1px solid #e2e8f0;
        display: flex;
        justify-content: space-between;
        align-items: center;
        
        .refresh-btn {
            padding: 10px 20px;
            border-radius: 8px;
            background: #f3f4f6;
            border: 1px solid #d1d5db;
            color: #374151;
            font-weight: 500;
            transition: all 0.3s ease;
            
            &:hover {
                background: #e5e7eb;
                border-color: #9ca3af;
                transform: translateY(-1px);
            }
        }
        
        .confirm-btn {
            padding: 10px 24px;
            border-radius: 8px;
            background: linear-gradient(135deg, #3b82f6 0%, #2563eb 100%);
            border: none;
            color: white;
            font-weight: 500;
            box-shadow: 0 4px 6px -1px rgba(59, 130, 246, 0.3);
            transition: all 0.3s ease;
            
            &:hover {
                background: linear-gradient(135deg, #2563eb 0%, #1d4ed8 100%);
                transform: translateY(-1px);
                box-shadow: 0 6px 8px -1px rgba(59, 130, 246, 0.4);
            }
        }
    }
}

// 修改BOM子项对话框样式
.edit-bom-child-dialog {
    :deep(.el-dialog) {
        border-radius: 16px;
        overflow: hidden;
        box-shadow: 0 25px 50px -12px rgba(0, 0, 0, 0.25);
    }

    :deep(.el-dialog__header) {
        margin: 0;
        padding: 24px 32px;
        background: linear-gradient(135deg, #f59e0b 0%, #d97706 100%);
        border-radius: 16px 16px 0 0;
        position: relative;
        
        &::before {
            content: '';
            position: absolute;
            top: 0;
            left: 0;
            right: 0;
            bottom: 0;
            background: linear-gradient(135deg, rgba(255,255,255,0.1) 0%, rgba(255,255,255,0.05) 100%);
            pointer-events: none;
        }

        .el-dialog__title {
            color: white;
            font-size: 20px;
            font-weight: 600;
            text-shadow: 0 2px 4px rgba(0,0,0,0.1);
        }

        .el-dialog__headerbtn {
            .el-dialog__close {
                color: rgba(255,255,255,0.8);
                font-size: 18px;
                
                &:hover {
                    color: white;
                }
            }
        }
    }

    :deep(.el-dialog__body) {
        padding: 0;
        background: #f8fafc;
    }

    :deep(.el-dialog__footer) {
        padding: 0;
                    background: transparent;
    }
}

.edit-bom-child-content {
    padding: 24px;
    background: #f8fafc;
    
    .child-part-info {
        margin-bottom: 24px;
        padding: 16px;
        background: white;
        border-radius: 12px;
        box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
        
        h4 {
            margin: 0 0 8px 0;
            color: #f59e0b;
            font-size: 16px;
            font-weight: 600;
        }
        
        p {
            margin: 0;
            color: #666;
            font-size: 14px;
            line-height: 1.5;
        }
    }
    
    .edit-bom-child-form {
        background: white;
        padding: 24px;
        border-radius: 12px;
        box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
        
        .el-form-item {
            margin-bottom: 20px;
            
            :deep(.el-form-item__label) {
                color: #374151;
                font-weight: 500;
                font-size: 14px;
                margin-bottom: 8px;
            }
            
            .el-input {
                :deep(.el-input__wrapper) {
                    border-radius: 8px;
                    border: 2px solid #e5e7eb;
                    transition: all 0.3s ease;
                    
                    &:hover {
                        border-color: #f59e0b;
                    }
                    
                    &.is-focus {
                        border-color: #f59e0b;
                        box-shadow: 0 0 0 3px rgba(245, 158, 11, 0.1);
                    }
                }
            }
            
            .el-input-number {
                width: 100%;
                
                :deep(.el-input__wrapper) {
                    border-radius: 8px;
                    border: 2px solid #e5e7eb;
                    transition: all 0.3s ease;
                    
                    &:hover {
                        border-color: #f59e0b;
                    }
                    
                    &.is-focus {
                        border-color: #f59e0b;
                        box-shadow: 0 0 0 3px rgba(245, 158, 11, 0.1);
                    }
                }
            }
            
            .form-tip {
                margin-top: 6px;
                padding: 4px 8px;
                background: #fef3c7;
                border-radius: 4px;
                border: 1px solid #fbbf24;
            }
        }
    }
    
    .dialog-footer {
        padding: 20px 24px;
        background: white;
        border-top: 1px solid #e2e8f0;
        display: flex;
        justify-content: flex-end;
        gap: 12px;
        
        .cancel-btn {
            padding: 10px 24px;
            border-radius: 8px;
            border: 2px solid #e5e7eb;
            background: white;
            color: #6b7280;
            font-weight: 500;
            transition: all 0.3s ease;
            
            &:hover {
                border-color: #d1d5db;
                background: #f9fafb;
                color: #374151;
                transform: translateY(-1px);
            }
        }
        
        .confirm-btn {
            padding: 10px 24px;
            border-radius: 8px;
            background: linear-gradient(135deg, #f59e0b 0%, #d97706 100%);
    border: none;
            color: white;
            font-weight: 500;
            box-shadow: 0 4px 6px -1px rgba(245, 158, 11, 0.3);
            transition: all 0.3s ease;
            display: flex;
            align-items: center;
            gap: 8px;
            
            &:hover {
                background: linear-gradient(135deg, #d97706 0%, #b45309 100%);
                transform: translateY(-1px);
                box-shadow: 0 6px 8px -1px rgba(245, 158, 11, 0.4);
            }
            
            &.is-loading {
                cursor: not-allowed;
                opacity: 0.7;
            }
        }
    }
}

// BOM配置对话框样式
.bom-config-dialog {
    :deep(.el-dialog) {
        border-radius: 16px;
        overflow: hidden;
        box-shadow: 0 25px 50px -12px rgba(0, 0, 0, 0.25);
    }

    :deep(.el-dialog__header) {
        margin: 0;
        padding: 24px 32px;
        background: linear-gradient(135deg, #3b82f6 0%, #2563eb 100%);
        border-radius: 16px 16px 0 0;
        position: relative;
        
        &::before {
            content: '';
            position: absolute;
            top: 0;
            left: 0;
            right: 0;
            bottom: 0;
            background: linear-gradient(135deg, rgba(255,255,255,0.1) 0%, rgba(255,255,255,0.05) 100%);
            pointer-events: none;
        }

        .el-dialog__title {
            color: white;
            font-size: 20px;
                    font-weight: 600;
            text-shadow: 0 2px 4px rgba(0,0,0,0.1);
        }

        .el-dialog__headerbtn {
            .el-dialog__close {
                color: rgba(255,255,255,0.8);
                font-size: 18px;
                
                &:hover {
                    color: white;
                }
            }
        }
    }

    :deep(.el-dialog__body) {
        padding: 0;
                background: #f8fafc;
    }

    :deep(.el-dialog__footer) {
        padding: 0;
        background: transparent;
    }
}

.bom-config-content {
    padding: 24px;
    background: #f8fafc;
    
    .parts-info {
        display: flex;
        align-items: center;
        justify-content: space-between;
        margin-bottom: 24px;
        padding: 20px;
        background: white;
        border-radius: 12px;
        box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
        
        .part-item {
            flex: 1;
            text-align: center;
            
            h4 {
                margin: 0 0 8px 0;
                font-size: 16px;
                font-weight: 600;
                color: #374151;
            }
            
            p {
                    margin: 0;
                font-size: 14px;
                color: #6b7280;
                    line-height: 1.5;
            }
            
            &.parent-part {
                h4 {
                    color: #f59e0b;
                }
            }
            
            &.child-part {
                h4 {
                    color: #10b981;
                }
            }
        }
        
        .arrow-icon {
            margin: 0 20px;
            color: #3b82f6;
            display: flex;
            align-items: center;
            justify-content: center;
        }
    }
    
    .bom-config-form {
        background: white;
        padding: 24px;
        border-radius: 12px;
        box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
        
        .el-form-item {
            margin-bottom: 20px;
            
            :deep(.el-form-item__label) {
                color: #374151;
                font-weight: 500;
                font-size: 14px;
                margin-bottom: 8px;
            }
            
            .el-input {
                :deep(.el-input__wrapper) {
    border-radius: 8px;
                    border: 2px solid #e5e7eb;
                    transition: all 0.3s ease;
                    
                    &:hover {
                        border-color: #3b82f6;
                    }
                    
                    &.is-focus {
                        border-color: #3b82f6;
                        box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.1);
                    }
                }
            }
            
            .el-input-number {
                width: 100%;
                
                :deep(.el-input__wrapper) {
                    border-radius: 8px;
                    border: 2px solid #e5e7eb;
                    transition: all 0.3s ease;
                    
                    &:hover {
                        border-color: #3b82f6;
                    }
                    
                    &.is-focus {
                        border-color: #3b82f6;
                        box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.1);
                    }
                }
            }
            
            .form-tip {
                margin-top: 6px;
                padding: 4px 8px;
                background: #f0f9ff;
                border-radius: 4px;
                border: 1px solid #bfdbfe;
            }
        }
    }
    
    .dialog-footer {
        padding: 20px 24px;
        background: white;
        border-top: 1px solid #e2e8f0;
        display: flex;
        justify-content: flex-end;
        gap: 12px;
        
        .cancel-btn {
            padding: 10px 24px;
            border-radius: 8px;
            border: 2px solid #e5e7eb;
            background: white;
            color: #6b7280;
    font-weight: 500;
    transition: all 0.3s ease;
    
    &:hover {
                border-color: #d1d5db;
                background: #f9fafb;
                color: #374151;
        transform: translateY(-1px);
            }
        }
        
        .confirm-btn {
            padding: 10px 24px;
    border-radius: 8px;
            background: linear-gradient(135deg, #3b82f6 0%, #2563eb 100%);
            border: none;
            color: white;
    font-weight: 500;
            box-shadow: 0 4px 6px -1px rgba(59, 130, 246, 0.3);
    transition: all 0.3s ease;
            display: flex;
            align-items: center;
            gap: 8px;
    
    &:hover {
                background: linear-gradient(135deg, #2563eb 0%, #1d4ed8 100%);
        transform: translateY(-1px);
                box-shadow: 0 6px 8px -1px rgba(59, 130, 246, 0.4);
            }
            
            &.is-loading {
                cursor: not-allowed;
                opacity: 0.7;
            }
        }
    }
}
</style>