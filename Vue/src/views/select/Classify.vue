<template>
    <div class="classify-container">
        <div class="search-section">
            <div class="search-box">
                <h3>分类信息查询</h3>
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
                <el-button type="primary" @click="link=true" class="action-btn">
                    <el-icon><Link /></el-icon>查看属性关联
                </el-button>
                <el-button type="primary" @click="viewTree=true" class="action-btn">
                    <el-icon><Share /></el-icon>查看分类树
                </el-button>
                <el-button type="primary" @click="AddClassify()" class="action-btn">
                    <el-icon><Plus /></el-icon>新增分类
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
                    <el-table-column prop="businessCode" label="分类码" width="240" />
                    <el-table-column prop="name" label="分类中文名称" width="240" />
                    <el-table-column prop="nameEn" label="分类英文名称" width="240" />
                    <el-table-column prop="description" label="分类中文描述" width="240" />
                    <el-table-column prop="descriptionEn" label="分类英文描述" width="240" />
                    <el-table-column label="操作" fixed="right" width="200">
                        <template #default="scope">
                            <el-button type="primary" @click="ViewAttribute(scope.row)" link>查看属性</el-button>
                            <el-button type="primary" @click="Modify(scope.row)" link>修改</el-button>
                            <el-button type="danger" @click="Delete(scope.row)" link>删除</el-button>
                        </template>
                    </el-table-column>
                </el-table>
            </div>

            <div class="attribute-section">
                <div class="section-header">
                    <h3>分类属性信息 >> {{ currentClass }}</h3>
                </div>
                <el-table 
                    :data="tableData2" 
                    class="custom-table"
                    height="230"
                >
                    <el-table-column prop="name" label="属性中文名称" width="280" />
                    <el-table-column prop="nameEn" label="属性英文名称" width="280" />
                    <el-table-column prop="description" label="属性中文描述" width="280" />
                    <el-table-column prop="descriptionEn" label="属性英文描述" width="280"/>
                    <el-table-column prop="type" label="数据类型" />
                </el-table>
            </div>
        </div>

        <!-- 分类树对话框 -->
        <el-dialog 
            v-model="viewTree" 
            title="分类树" 
            width="750"
            class="custom-dialog"
        >
            <ClassTree />
        </el-dialog>

        <!-- 分类-属性关联对话框 -->
        <el-dialog 
            v-model="link" 
            title="分类-属性关联" 
            width="750"
            class="custom-dialog"
        >
            <LinkTable />
        </el-dialog>
    </div>
</template>

<script>
import {reactive, ref} from 'vue'
import { useRouter } from 'vue-router';
import axios from 'axios';
import { ElMessage, ElMessageBox } from 'element-plus'
import ClassTree from '@/components/ClassTree.vue'
import LinkTable from '@/components/LinkTable.vue';
export default{
    components:{
        ClassTree,
        LinkTable
    },
    setup(){
        const router = useRouter();
        const tableData1=ref([])
        const tableData2=ref([])
        let currentClass=ref('')
        let form1=reactive({
            ClassificationCode:'CN0000041',
            ChineseName: '显示器',
            EnglishName: 'Display',
            Chinesedescription: '显示器',
            Englishdescription: 'Display',
            attributeStatus: '1'
        })
        
        let viewTree=ref(false)
        let link=ref(false)
        const Modify=(row)=>{
            router.push(`./modifyclassify/${row.id}`)
        }
        const AddClassify=()=>{
            router.push('/addclassify')
        }

        const ViewAttribute=(row)=>{
            tableData2.value=[]
            currentClass.value=row.name
            axios.get(`/api/EXADefinitionLink/getParentAttributesByClassification?classificationNodeId=${row.id}`)
            .then((res)=>{
                if(res.data.data){
                tableData2.value=[...tableData2.value,...res.data.data.map(item=>item.source)]
                }
                console.log(res.data.data)
            })
            .catch((err)=>{
                console.log(err)
            })
            axios.get(`/api/EXADefinitionLink/listByClassification?classificationNodeId=${row.id}`)
            .then((res)=>{
                if(res.data.data){
                tableData2.value=[...tableData2.value,...res.data.data.map(item=>item.source)]
                }
                console.log(res.data.data)
            })
            .catch((err)=>{
                console.log(err)
            })

            console.log('查看属性')
        }

        let keyword=ref('')

        let Search=(keyword)=>{
            if(keyword==''){
                GetAll()
                return
            }
            axios.get(`/api/category/query?keyword=${keyword}`)
            .then((res)=>{
                tableData1.value=res.data.data
                console.log(res.data.data)
            })
            .catch((err)=>{
                console.log(err)
            })
        }

        const Delete=(row)=>{
            ElMessageBox.confirm(
                `确定要删除分类 "${row.name}" 吗？此操作不可恢复。`,
                '提示',
                {
                confirmButtonText: '确认',
                cancelButtonText: '取消',
                type: 'warning'
                }
            )
            .then(()=>{
                const postData={
                    id: row.id
                }
                return axios.post(`/api/category/delete`,postData)
            })
            .then((res)=>{
                console.log(res.data)
                if(res.data.code!=200){
                    ElMessage.error('删除失败:该分类含有子项')
                }
                Search(keyword.value)
            })
            .catch((err)=>{
                console.log(err)
            })
        }

        let GetAll=()=>{
            axios.get('/api/category/getAll')
            .then((res)=>{
                tableData1.value=res.data.data
                console.log(res.data)
            })
            .catch((err)=>{
                console.log(err)
            })
        }
        GetAll()
        return{
            tableData1,
            tableData2,
            viewTree,
            AddClassify,
            ViewAttribute,
            Modify,
            Delete,
            form1,
            link,
            Search,
            keyword,
            currentClass
        }
    }
}
</script>

<style lang="less" scoped>
.classify-container {
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

            h3 {
                color: #333;
                margin: 0;
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

                .el-icon {
                    margin-right: 4px;
                }
            }
        }
    }

    .content-section {
        .table-container {
            margin-bottom: 20px;
        }

        .attribute-section {
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

    :deep(.el-dialog__body) {
        padding: 20px;
    }
}
</style>