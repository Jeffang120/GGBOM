<template>
  <div class="modify-classify-container">
    <div class="page-header">
      <h2>修改分类</h2>
    </div>
    
    <div class="content-section">
      <el-form :model="form1" class="classify-form">
        <el-row :gutter="30">
          <el-col :span="12">
            <div class="form-card">
              <h3>基本信息</h3>
              <el-form-item label="分类码">
                <el-input v-model="form1.businessCode" disabled placeholder="分类码" />
              </el-form-item>
              <el-form-item label="中文名称">
                <el-input v-model="form1.name" placeholder="请输入中文名称" />
              </el-form-item>
              <el-form-item label="中文描述">
                <el-input v-model="form1.description" type="textarea" :rows="2" placeholder="请输入中文描述" />
              </el-form-item>
            </div>
          </el-col>

          <el-col :span="12">
            <div class="form-card">
              <h3>扩展信息</h3>
              <el-form-item label="属性状态">
                <el-radio-group v-model="form1.disableFlag">
                  <el-radio value="1" size="large">有效</el-radio>
                  <el-radio value="2" size="large">无效</el-radio>
                </el-radio-group>
              </el-form-item>
              <el-form-item label="英文名称">
                <el-input v-model="form1.nameEn" placeholder="请输入英文名称" />
              </el-form-item>
              <el-form-item label="英文描述">
                <el-input v-model="form1.descriptionEn" type="textarea" :rows="2" placeholder="请输入英文描述" />
              </el-form-item>
            </div>
          </el-col>
        </el-row>
      </el-form>

      <div class="table-section">
        <div class="table-header">
          <h3>属性信息</h3>
          <div class="table-actions">
            <el-button type="primary" class="action-btn" @click="add=true">新增</el-button>
            <div v-show="add" class="attribute-search">
                <el-select
                    v-model="selectedAttribute"
                    filterable
                    remote
                    placeholder="请输入属性名称"
                    :remote-method="handleSearch"
                    :loading="loading"
                    class="attribute-select"
                    @change="handleAttributeChange"
                    value-key="id"
                >
                    <el-option
                        v-for="item in attributeOptions"
                        :key="item.id"
                        :label="item.name"
                        :value="item"
                    >
                        <div class="attribute-option">
                            <span class="name">{{ item.name }}</span>
                            <span class="type">({{ item.type }})</span>
                        </div>
                    </el-option>
                </el-select>
            </div>
            <el-button class="action-btn" @click="attrSure" v-show="add">确认</el-button>
            <el-button class="action-btn delete-btn" @click="handleDelete">删除</el-button>
          </div>
        </div>
        
        <el-table 
          :data="tableData1" 
          class="custom-table"
          height="300" 
          style="width: 100%" 
          @selection-change="handleSelectionChange" 
          ref="tableRef"
        >
          <el-table-column type="selection" width="55" :selectable="isRowSelectable"/>
          <el-table-column prop="name" label="属性中文名称" width="280" />
          <el-table-column prop="nameEn" label="属性英文名称" width="280" />
          <el-table-column prop="description" label="属性中文描述" width="280" />
          <el-table-column prop="descriptionEn" label="属性英文描述" width="280"/>
          <el-table-column prop="type" label="数据类型" width="280"/>
          <el-table-column prop="explain" label="说明" />
        </el-table>
      </div>

      <div class="action-buttons">
        <el-button @click="cancel()" class="cancel-btn">取消</el-button>
        <el-button type="primary" @click="modifyclass()" class="confirm-btn">确认</el-button>
      </div>
    </div>
  </div>
</template>

<script>
import { reactive ,ref,watch, onMounted} from 'vue';
import { useRouter,useRoute } from 'vue-router';
import {useStore} from 'vuex';
import axios from 'axios';
import AttributeSearch from '@/components/AttributeSearch.vue';
import { ElMessage } from 'element-plus';

export default{
    components:{
        AttributeSearch
    },
    setup(){
        const router = useRouter();
        const route = useRoute();
        const store=useStore();
        if(!store.state.user.userInfo){
            router.push('/login')
        }
        let selectedRows = ref([]);
        let tableRef = ref(null);
        let form1 = reactive({
            businessCode: '',
            name: '',
            nameEn: '',
            description: '',
            descriptionEn: '',
            disableFlag: ''
        })
        
        axios.post('/api/category/getById',{id:route.params.id})
        .then((res)=>{
            const data = res.data.data[0];
            form1.id=data.id
            form1.businessCode = data.businessCode;
            form1.name = data.name;
            form1.nameEn = data.nameEn;
            form1.description = data.description;
            form1.descriptionEn = data.descriptionEn;
            form1.disableFlag = data.disableFlag?'2':'1';
            console.log(form1)
        })
        .catch((err)=>{
            console.log(err)
        })

        let tableData1=ref([])
        const reloadAttributes = () =>{
            tableData1.value=[]
            axios.get(`/api/EXADefinitionLink/getParentAttributesByClassification?classificationNodeId=${route.params.id}`)
            .then((res)=>{
                if(res.data.data){
                const parentAttributes = res.data.data.map(item => ({
                    ...item.source,
                    isParentRelated: true,
                    explain: '继承自父节点',
                    ID:item.id
                }));
                tableData1.value = [...tableData1.value, ...parentAttributes];
                }
                console.log(res.data.data)
            })
            .catch((err)=>{
                console.log(err)
            })
            axios.get(`/api/EXADefinitionLink/listByClassification?classificationNodeId=${route.params.id}`)
            .then((res)=>{
                if(res.data.data){
                const currentAttributes = res.data.data.map(item => ({
                    ...item.source,
                    isParentRelated: false,
                    explain: '当前分类属性',
                    ID:item.id
                }));
                tableData1.value = [...tableData1.value, ...currentAttributes];
                }
                console.log(res.data.data)
            })
            .catch((err)=>{
                console.log(err)
            })
        }
        reloadAttributes()

        const modifyclass=()=>{
            let postData = { 
                ...form1,
                disableFlag:form1.disableFlag=='1'?false:true
            };
            axios.post('/api/category/update',postData)
            .then((res)=>{
                console.log(res.data)
                router.push('/classify')
            })
            .catch((err)=>{
                console.log(err)
            })
        }

        const handleSelectionChange = (rows) => {
            selectedRows.value = rows;
            console.log('选中的属性:', rows);
        };

        const handleNodeSelected = (node) => {
            console.log('接收到子组件的selectedNode:', node);
            form1.parentNode = { ...node };
        };

        let selectedAttribute = ref(null)
        let attributeOptions = ref([])
        let loading = ref(false)

        // 获取所有属性
        const getAllAttributes = async () => {
            try {
                const res = await axios.get('/api/exa/getAll')
                attributeOptions.value = res.data.data
            } catch (err) {
                console.error('获取属性列表失败:', err)
            }
        }

        // 搜索属性
        const handleSearch = async (query) => {
            if (query) {
                loading.value = true
                try {
                    const res = await axios.get(`/api/exa/querybyname?keyword=${query}`)
                    attributeOptions.value = res.data.data || []
                } catch (err) {
                    console.error('搜索属性失败:', err)
                    attributeOptions.value = []
                } finally {
                    loading.value = false
                }
            } else {
                await getAllAttributes()
            }
        }

        const attrSure = () => {
            if (!selectedAttribute.value || !selectedAttribute.value.id) {
                ElMessage.warning('请选择一个属性')
                return
            }

            // 检查属性是否已存在
            const isAttributeExists = tableData1.value.some(attr => 
                attr.id === selectedAttribute.value.id
            );

            if (isAttributeExists) {
                ElMessage.warning('该属性已存在，不能重复添加')
                selectedAttribute.value = null
                return
            }

            const params = [
                {
                    tenant: { id: "-1", clazz: "Tenant" },
                    source: { 
                        id: selectedAttribute.value.id,
                        clazz: "EXADefinition" 
                    },
                    target: { 
                        id: route.params.id,
                        clazz: "ClassificationNode" 
                    },
                    defaultValue: null
                }
            ];

            axios.post('/api/EXADefinitionLink/batchCreate', params)
            .then((res) => {
                console.log(res.data);
                const newAttr = {
                    ...selectedAttribute.value,
                    isParentRelated: false,
                    explain: '当前分类属性',
                    ID: res.data.data[0].id  // 保存返回的链接ID
                };
                tableData1.value = [...tableData1.value, newAttr];
                selectedAttribute.value = null;
                add.value = false;
                ElMessage.success('属性添加成功');
            })
            .catch((err) => {
                console.error('添加属性失败:', err);
                ElMessage.error('添加属性失败');
            });
        }

        const cancel=()=>{
            router.push('/classify')
        }
        let add=ref(false)

        const isRowSelectable = (row) => {
            return !row.isParentRelated; 
        };

        let isParentChangeQuery = ref(false);
        watch(() => form1.parentNode, (newVal) => {
            if (newVal && newVal.id) {
                isParentChangeQuery.value = true;
                getParentRelatedAttributes(newVal.id);
            } else {
                parentRelatedTableData.value = []; 
            }
        }, { deep: true }); 

        const getParentRelatedAttributes = async (parentId) => {
            isSearching.value = true;
            try {
                const response = await axios.get(`/api/EXADefinitionLink/listByClassification?classificationNodeId=${parentId}`); 
                const dataWithFlag = response.data.data.map(item => ({
                    ...item.source,
                    isParentRelated: true,
                    explain:'继承自父节点'
                }));
                tableData1.value = dataWithFlag;
                console.log('父节点相关属性查询结果:', response.data.data);
            } catch (error) {
                console.error('查询父节点相关属性失败:', error);
            } finally {
                isSearching.value = false;
            }
        };

        const handleDelete=()=>{
            console.log(selectedRows.value)
            const postData={
                ids:selectedRows.value.map(row => row.ID)
            }
            console.log(postData)
            axios.post('/api/EXADefinitionLink/batchDelete',postData)
            .then((res)=>{
                console.log(res.data.data)
                reloadAttributes()
            })
            .catch((err)=>{
                console.log(err)
            })
        }

        const handleAttributeChange = (value) => {
            console.log('选中的属性:', value)
            selectedAttribute.value = value
        }

        onMounted(() => {
            getAllAttributes()
        })

        return {
            form1,
            tableData1,
            cancel,
            add,
            modifyclass,
            handleNodeSelected,
            isRowSelectable,
            attrSure,
            selectedRows,
            tableRef,
            handleSelectionChange,
            handleDelete,
            selectedAttribute,
            attributeOptions,
            loading,
            handleSearch,
            handleAttributeChange
        }
    }
}
</script>

<style lang="less" scoped>
.modify-classify-container {
  padding: 20px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0,0,0,0.1);

  .page-header {
    margin-bottom: 30px;
    h2 {
      color: #333;
      font-size: 24px;
      margin: 0;
    }
  }

  .content-section {
    .classify-form {
      margin-bottom: 30px;
    }

    .form-card {
      background: #f8f9fa;
      padding: 20px;
      border-radius: 8px;
      height: 90%;

      h3 {
        color: #333;
        font-size: 18px;
        margin: 0 0 20px 0;
      }
    }

    .table-section {
      margin: 30px 0;
      
      .table-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 20px;

        h3 {
          color: #333;
          font-size: 18px;
          margin: 0;
        }

        .table-actions {
          display: flex;
          gap: 10px;
          align-items: center;

          .action-btn {
            padding: 8px 20px;
            border-radius: 20px;
            
            &.delete-btn {
              color: #ff4d4f;
              border-color: #ff4d4f;
              
              &:hover {
                color: #fff;
                background-color: #ff4d4f;
              }
            }
          }
        }
      }

      .custom-table {
        border-radius: 8px;
        overflow: hidden;
        box-shadow: 0 2px 12px rgba(0,0,0,0.1);
      }
    }
  }

  .action-buttons {
    display: flex;
    justify-content: flex-end;
    gap: 15px;
    margin-top: 30px;

    .cancel-btn {
      padding: 10px 30px;
      border-radius: 20px;
    }

    .confirm-btn {
      padding: 10px 30px;
      border-radius: 20px;
      background: linear-gradient(145deg, #4481eb, #04befe);
      border: none;

      &:hover {
        opacity: 0.9;
      }
    }
  }
}

:deep(.el-form-item) {
  margin-bottom: 20px;

  .el-form-item__label {
    color: #666;
  }

  .el-input__inner {
    border-radius: 8px;
  }

  .el-textarea__inner {
    border-radius: 8px;
  }
}

:deep(.el-radio-group) {
  .el-radio {
    margin-right: 20px;
  }
}

:deep(.el-table) {
  th {
    background-color: #f8f9fa;
    color: #333;
    font-weight: 600;
  }
}

.attribute-search {
    .attribute-select {
        width: 240px;
    }
}

.attribute-option {
    display: flex;
    align-items: center;
    justify-content: space-between;
    
    .name {
        font-weight: 500;
    }
    
    .type {
        color: #909399;
        font-size: 12px;
    }
}

:deep(.el-select-dropdown__item) {
    padding: 0 12px;
}
</style>