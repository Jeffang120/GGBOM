<template>
  <div class="add-classify-container">
    <div class="page-header">
      <h2>新增分类</h2>
    </div>
    
    <div class="content-section">
      <el-form 
        :model="form1" 
        :rules="rules"
        ref="formRef"
        class="classify-form"
      >
        <el-row :gutter="30">
          <el-col :span="12">
            <div class="form-card">
              <h3>基本信息</h3>
              <el-form-item label="分类码">
                <el-input v-model="form1.businessCode" placeholder="请输入分类码" />
              </el-form-item>
              <el-form-item 
                label="中文名称" 
                prop="name"
              >
                <el-input v-model="form1.name" placeholder="请输入中文名称" />
              </el-form-item>
              <el-form-item label="中文描述">
                <el-input v-model="form1.description" type="textarea" :rows="2" placeholder="请输入中文描述" />
              </el-form-item>
              <el-form-item label="属性状态">
                <el-radio-group v-model="form1.disableFlag">
                  <el-radio value="1" size="large">有效</el-radio>
                  <el-radio value="2" size="large">无效</el-radio>
                </el-radio-group>
              </el-form-item>
            </div>
          </el-col>

          <el-col :span="12">
            <div class="form-card">
              <h3>扩展信息</h3>
              <el-form-item label="分类父节点">
                <ParentSearch v-model="form1.parentNode" @node-selected="handleNodeSelected"/>
              </el-form-item>
              <el-form-item 
                label="英文名称"
                prop="nameEn"
              >
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
          <h3>继承属性信息</h3>
        </div>
        <el-table 
          :data="tableData1" 
          class="custom-table"
          height="300" 
          style="width: 100%"
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
        <el-button type="primary" @click="addclass()" class="confirm-btn">确认</el-button>
      </div>
    </div>
  </div>
</template>

<script>
import { reactive ,ref,watch} from 'vue';
import { useRouter } from 'vue-router';
import {useStore} from 'vuex';
import axios from 'axios';
import ParentSearch from '@/components/ParentSearch.vue';
import AttributeSearch from '@/components/AttributeSearch.vue';
import { ElMessage } from 'element-plus';

export default{
    components:{
        ParentSearch,
        AttributeSearch
    },
    setup(){
        const router = useRouter();
        const store=useStore();
        if(!store.state.user.userInfo){
            router.push('/login')
        }
        const formRef = ref(null);
        
        const rules = {
            name: [
                { required: true, message: '请输入中文名称', trigger: 'blur' }
            ],
            nameEn: [
                { required: true, message: '请输入英文名称', trigger: 'blur' }
            ]
        };

        let form1 = reactive({
            businessCode: '',
            parentNode: {},
            name: '',
            nameEn: '',
            description: '',
            descriptionEn: '',
            disableFlag: '1'
        })
        let tableData1=ref([])
        let addclass=()=>{
            formRef.value.validate((valid) => {
                if (valid) {
                    console.log(form1.parentNode)
                    let postData = { ...form1 };
                    postData.disableFlag = form1.disableFlag === '1' ? false : true;
                    if (form1.parentNode && Object.keys(form1.parentNode).length > 0) {
                        postData.parentNode = { 
                            id: form1.parentNode.id ,
                            clazz: 'ClassificationNode'
                        };
                    } else {
                        delete postData.parentNode;
                    }
                    console.log(postData)
                    axios.post('/api/category/create',postData)
                    .then((res)=>{
                        console.log(res.data)
                        ElMessage.success('分类创建成功');
                        router.push('/classify')
                    })
                    .catch((err)=>{
                        console.log(err)
                        ElMessage.error('分类创建失败');
                    })
                } else {
                    ElMessage.warning('请填写必填项');
                    return false;
                }
            });
        }

        const handleNodeSelected = (node) => {
            console.log('接收到子组件的selectedNode:', node);
            form1.parentNode = { ...node };
        };

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
        let isSearching=ref(false)
        const getParentRelatedAttributes = async (parentId) => {
            isSearching.value = true;
            try {
                const response = await axios.get(`/api/EXADefinitionLink/listByClassification?classificationNodeId=${parentId}`); 
                const response2 = await axios.get(`/api/EXADefinitionLink/getParentAttributesByClassification?classificationNodeId=${parentId}`);
                const combinedData = [
                  ...(response.data.data || []).map(item => ({
                    ...item.source,
                    isParentRelated: true,
                    explain: '继承自父节点'
                  })),
                  ...(response2.data.data || []).map(item => ({
                    ...item.source,
                    isParentRelated: true,
                    explain: '继承自父节点'
                  }))
                ];
                tableData1.value = combinedData;
                console.log('父节点相关属性查询结果:', response.data.data);
            } catch (error) {
                console.error('查询父节点相关属性失败:', error);
            } finally {
                isSearching.value = false;
            }
        };

        return {
            form1,
            formRef,
            rules,
            tableData1,
            cancel,
            add,
            addclass,
            handleNodeSelected,
            isRowSelectable,
        }
    }
}
</script>

<style lang="less" scoped>
.add-classify-container {
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
      height: 250px;

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
</style>