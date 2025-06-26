<template>
  <div class="attribute-table-container">
    <el-table 
      :data="tableData" 
      style="width: 100%" 
      height="400"
      v-loading="loading"
    >
      <!-- 分类列（固定列） -->
      <el-table-column 
        fixed 
        prop="classInfo.name" 
        label="分类" 
        width="180" 
      />
      
      <!-- 动态生成属性列 -->
      <el-table-column 
        v-for="attr in attributes" 
        :key="attr.id" 
        :prop="`attr_${attr.id}`" 
        :label="attr.name" 
        width="120"
      />
    </el-table>
  </div>
</template>

<script>
import { ref, onMounted, watch } from 'vue';
import axios from 'axios';
export default {
  setup() {
    // 加载状态
    const loading = ref(true);
    
    // 分类数据
    const categories = ref([]);
    
    // 属性数据
    const attributes = ref([]);
    
    // 表格数据
    const tableData = ref([]);
    
    const getCategories = async () => { 
        const res = await axios.get('/api/category/getAll');
        return res.data;
    };

    const getAttributes = async () => { 
        const res = await axios.get('/api/exa/getAll');
        return res.data;
    };

    const getCategoryAttributes = async (id) => {
        // 并行发送两个API请求
        const [parentAttrsRes, listRes] = await Promise.all([
            axios.get(`/api/EXADefinitionLink/getParentAttributesByClassification?classificationNodeId=${id}`),
            axios.get(`/api/EXADefinitionLink/listByClassification?classificationNodeId=${id}`)
        ]);
        // 提取并合并数据
        const parentAttributes = parentAttrsRes.data.data || [];
        const listAttributes = listRes.data.data || [];
        const response = [
            ...parentAttributes.map(item => item.source),
            ...listAttributes.map(item => item.source)
        ];
        console.log('合并后的属性数据:', response);
        return response;
    };
    // 获取所有分类
    const fetchCategories = async () => {
      try {
        const response = await getCategories();
        categories.value = response.data;
        console.log('分类数据加载完成', categories.value);
      } catch (error) {
        console.error('加载分类数据失败', error);
      } finally {
        processData();
      }
    };
    
    // 获取所有属性
    const fetchAttributes = async () => {
      try {
        const response = await getAttributes();
        attributes.value = response.data;
        console.log('属性数据加载完成', attributes.value);
      } catch (error) {
        console.error('加载属性数据失败', error);
      } finally {
        processData();
      }
    };
    
    // 处理数据并生成表格
    const processData = async () => {
    // 确保分类和属性数据都已加载
    if (categories.value.length === 0 || attributes.value.length === 0) {
        return;
    }
    
    loading.value = true;
    tableData.value = [];
    
    try {
        // 为每个分类创建一个Promise，并行获取属性
        const categoryPromises = categories.value.map(async (category) => {
        try {
            // 获取分类对应的属性
            const categoryAttrs = await getCategoryAttributes(category.id);
            
            // 创建表格行数据
            const rowData = {
            classInfo: category,
            };
            
            // 初始化所有属性为未选中
            attributes.value.forEach(attr => {
            rowData[`attr_${attr.id}`] = '';
            });
            
            // 标记已有的属性
            categoryAttrs.forEach(attr => {
            // 找到全局属性列表中匹配的属性
            const globalAttr = attributes.value.find(a => a.id === attr.id);
            if (globalAttr) {
                rowData[`attr_${globalAttr.id}`] = '√';
            }
            });
            
            return rowData;
        } catch (error) {
            console.error(`加载分类 ${category.name} 的属性失败`, error);
            // 即使加载失败也返回空行
            const emptyRow = {
            classInfo: category,
            };
            attributes.value.forEach(attr => {
            emptyRow[`attr_${attr.id}`] = '';
            });
            return emptyRow;
        }
        });
        
        // 并行执行所有Promise
        const rows = await Promise.all(categoryPromises);
        
        // 一次性更新表格数据
        tableData.value = rows;
    } catch (error) {
        console.error('处理表格数据失败', error);
    } finally {
        loading.value = false;
        console.log('表格数据生成完成', tableData.value);
    }
    };
    
    // 监听分类或属性变化，重新处理数据
    watch([categories, attributes], processData);
    
    // 组件挂载后加载数据
    onMounted(() => {
      // 同时加载分类和属性数据
      Promise.all([fetchCategories(), fetchAttributes()])
        .catch(error => console.error('数据加载失败', error));
    });
    
    return {
      tableData,
      attributes,
      loading
    };
  }
};
</script>

<style lang="less" scoped>
.attribute-table-container {
  width: 100%;
  height: 100%;
}
</style>