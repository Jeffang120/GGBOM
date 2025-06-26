<template>
    <div class="attribute-container">
        <div class="search-section">
            <div class="search-box">
                <div class="title-with-logo">
                    <img src="@/assets/favicon.png" alt="GGBOM Logo" class="logo-img">
                    <h3>属性信息查询</h3>
                </div>
                <div class="search-input">
                    <el-input 
                        v-model="keyword" 
                        placeholder="请输入关键词搜索" 
                        class="custom-input"
                        prefix-icon="el-icon-search"
                    />
                    <el-button type="primary" @click="Search(keyword)" class="search-btn">查询</el-button>
                </div>
            </div>
            <div class="action-box">
                <el-button type="primary" @click="add=true" class="add-btn">
                    <el-icon><Plus /></el-icon>新增属性
                </el-button>
            </div>
        </div>

        <div class="content-section">
            <div class="table-container">
                <el-table 
                    :data="tableData1" 
                    class="custom-table"
                    height="400"
                >
                    <el-table-column prop="name" label="属性中文名称" width="240" />
                    <el-table-column prop="nameEn" label="属性英文名称" width="240" />
                    <el-table-column prop="description" label="属性中文描述" width="240" />
                    <el-table-column prop="descriptionEn" label="属性英文描述" width="240" />
                    <el-table-column prop="type" label="数据类型" width="240" />
                    <el-table-column label="操作" fixed="right" width="200">
                        <template #default="scope">
                            <el-button type="primary" @click="ViewClassification(scope.row)" link>查看分类</el-button>
                            <el-button type="primary" @click="handleModify(scope.row)" link>修改</el-button>
                            <el-button type="danger" @click="Delete(scope.row)" link>删除</el-button>
                        </template>
                    </el-table-column>
                </el-table>
            </div>

            <div class="classification-section">
                <div class="section-header">
                    <h3>属性所属分类 >> {{currentAttr}}</h3>
                </div>
                <el-table 
                    :data="tableData2" 
                    class="custom-table"
                    height="230"
                >
                    <el-table-column prop="businessCode" label="分类码" width="280" />
                    <el-table-column prop="name" label="分类中文名称" width="280" />
                    <el-table-column prop="nameEn" label="分类英文名称" width="280" />
                    <el-table-column prop="description" label="分类中文描述" width="280" />
                    <el-table-column prop="descriptionEn" label="分类英文描述" />
                </el-table>
            </div>
        </div>

        <!-- 新增属性对话框 -->
        <el-dialog v-model="add" title="新增属性" width="750" class="custom-dialog">
            <el-form :model="form1" class="custom-form">
                <div class="form-section">
                    <el-form-item label="中文名称">
                        <el-input v-model="form1.name" placeholder="请输入中文名称" />
                    </el-form-item>
                    <el-form-item label="中文描述">
                        <el-input v-model="form1.description" type="textarea" :rows="2" placeholder="请输入中文描述" />
                    </el-form-item>
                    <el-form-item label="英文名称">
                        <el-input v-model="form1.nameEn" placeholder="请输入英文名称" />
                    </el-form-item>
                    <el-form-item label="英文描述">
                        <el-input v-model="form1.descriptionEn" type="textarea" :rows="2" placeholder="请输入英文描述" />
                    </el-form-item>
                    <el-form-item label="类型">
                        <el-select v-model="form1.type" placeholder="请选择属性类型" class="custom-select">
                            <el-option label="STRING" value="STRING" />
                            <el-option label="INTEGER" value="INTEGER" />
                            <el-option label="DECIMAL" value="DECIMAL" />
                        </el-select>
                    </el-form-item>
                    <el-form-item label="属性状态">
                        <el-radio-group v-model="form1.disableFlag">
                            <el-radio value="1" size="large">有效</el-radio>
                            <el-radio value="2" size="large">无效</el-radio>
                        </el-radio-group>
                    </el-form-item>
                </div>
            </el-form>
            <template #footer>
                <div class="dialog-footer">
                    <el-button @click="add = false" class="cancel-btn">取消</el-button>
                    <el-button type="primary" @click="Add()" class="confirm-btn">确认</el-button>
                </div>
            </template>
        </el-dialog>

        <!-- 修改属性对话框 -->
        <el-dialog v-model="modify" title="修改属性" width="750" class="custom-dialog">
            <el-form :model="form2" class="custom-form">
                <div class="form-section">
                    <el-form-item label="中文名称">
                        <el-input v-model="form2.name" disabled />
                    </el-form-item>
                    <el-form-item label="中文描述">
                        <el-input v-model="form2.description" type="textarea" :rows="2" />
                    </el-form-item>
                    <el-form-item label="英文名称">
                        <el-input v-model="form2.nameEn" disabled />
                    </el-form-item>
                    <el-form-item label="英文描述">
                        <el-input v-model="form2.descriptionEn" type="textarea" :rows="2" />
                    </el-form-item>
                    <el-form-item label="类型">
                        <el-select v-model="form2.type" disabled class="custom-select">
                            <el-option label="STRING" value="STRING" />
                            <el-option label="INTEGER" value="INTEGER" />
                            <el-option label="DECIMAL" value="DECIMAL" />
                        </el-select>
                    </el-form-item>
                    <el-form-item label="属性状态">
                        <el-radio-group v-model="form2.disableFlag" disabled>
                            <el-radio value="1" size="large">有效</el-radio>
                            <el-radio value="2" size="large">无效</el-radio>
                        </el-radio-group>
                    </el-form-item>
                </div>
            </el-form>
            <template #footer>
                <div class="dialog-footer">
                    <el-button @click="modify = false" class="cancel-btn">取消</el-button>
                    <el-button type="primary" @click="Modify()" class="confirm-btn">确认</el-button>
                </div>
            </template>
        </el-dialog>
    </div>
</template>

<script>
import {ref,reactive} from 'vue'
import axios from 'axios'
import { ElMessage, ElMessageBox } from 'element-plus'
export default{ 
    setup(){
        let tableData1 = ref([])
        let tableData2 =ref([])
        let currentAttr=ref('')
        const ViewClassification=(row)=>{
            axios.get(`/api/EXADefinitionLink/listClassificationsByAttribute?businessCode=${row.businessCode}`)
            .then((res)=>{
                console.log(res.data)
                currentAttr.value=row.name
                tableData2.value=res.data.data
            })
            .catch((err)=>{
                console.log(err)
            })
        }
        const Modify=()=>{
            const putData={
                description:form2.description,
                descriptionEn:form2.descriptionEn
            }
            axios.put(`/api/exa/update?oldName=${form2.name}`,putData)
            .then((res)=>{
                console.log(res.data)
                modify.value = false
                Search(keyword.value)
            })
            .catch((err)=>{
                console.log(err)
            })
            console.log('修改')
        }
        const Delete=(row)=>{
            ElMessageBox.confirm(
                `确定要删除属性 "${row.name}" 吗？此操作不可恢复。`,
                '提示',
                {
                confirmButtonText: '确认',
                cancelButtonText: '取消',
                type: 'warning'
                }
            )
            .then(()=>{
                const postData={
                    id:row.id
                }
                return axios.post(`/api/exa/delete`,postData)
            })
            .then((res)=>{
                console.log(res.data)
                if(res.data.code!=200){
                    ElMessage.error(res.data.message)
                }
                Search(keyword.value)
            })
            .catch((err)=>{
                console.log(err)
            })
            console.log('删除')
        }
        const add=ref(false)
        const modify=ref(false)
        const form1 = reactive({
            name:'',
            description:'',
            nameEn:'',
            descriptionEn:'',
            type:'',
            disableFlag:'1'
        })
        const form2 = reactive({
            name:'',
            description:'',
            nameEn:'',
            descriptionEn:'',
            type:'',
            disableFlag:'1'
        })
        let handleModify=(row)=>{
            form2.name = row.name 
            form2.description = row.description 
            form2.nameEn = row.nameEn 
            form2.descriptionEn = row.descriptionEn 
            form2.type = row.type 
            form2.disableFlag = row.disableFlag?'2':'1' 
            modify.value=true
        }
        let Add=()=>{
            const postData={
                ...form1,
                disableFlag:form1.disableFlag==1?false:true
            }
            axios.post('/api/exa/create',postData)
            .then((res)=>{
                console.log(res.data)
                if(res.data.code!=200){
                    ElMessage.error(res.data.message)
                }
                add.value=false
                Search(keyword.value)
            })
            .catch((err)=>{
                console.log(err)
            })
        }
        const keyword=ref('')
        let Search=(keyword)=>{
            if(keyword==''){
                GetAll()
                return
            }
            axios.get(`/api/exa/querybyname?keyword=${keyword}`)
            .then((res)=>{
                tableData1.value=[...res.data.data]
                console.log(tableData1.value)
            })
            .catch((err)=>{
                console.log(err)
            })
        }

        let GetAll=()=>{
            axios.get('/api/exa/getAll')
            .then((res)=>{
                tableData1.value=[...res.data.data]
                console.log(tableData1.value)
            })
            .catch((err)=>{
                console.log(err)
            })
        }
        GetAll()
        return{
            tableData1,
            tableData2,
            ViewClassification,
            Modify,
            Delete,
            add,
            modify,
            form1,
            form2,
            Add,
            Search,
            keyword,
            handleModify,
            currentAttr
        }
    }
}
</script>

<style lang="less" scoped>
.attribute-container {
    padding: 20px;
    background: #fff;
    border-radius: 8px;
    box-shadow: 0 2px 12px rgba(0,0,0,0.1);

    .search-section {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 20px;

        .search-box {
            display: flex;
            align-items: center;
            gap: 20px;

            .title-with-logo {
                display: flex;
                align-items: center;
                gap: 10px;

                .logo-img {
                    width: 24px;
                    height: 24px;
                }

                h3 {
                    color: #333;
                    margin: 0;
                }
            }

            .search-input {
                display: flex;
                gap: 10px;

                .custom-input {
                    width: 240px;
                }

                .search-btn {
                    background: linear-gradient(145deg, #4481eb, #04befe);
                    border: none;
                    padding: 8px 20px;
                    border-radius: 20px;

                    &:hover {
                        opacity: 0.9;
                    }
                }
            }
        }

        .action-box {
            .add-btn {
                background: linear-gradient(145deg, #4481eb, #04befe);
                border: none;
                padding: 8px 20px;
                border-radius: 20px;

                &:hover {
                    opacity: 0.9;
                }
            }
        }
    }

    .content-section {
        .table-container {
            margin-bottom: 20px;
        }

        .classification-section {
            .section-header {
                margin-bottom: 15px;
                h3 {
                    color: #333;
                    margin: 0;
                }
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
</style>