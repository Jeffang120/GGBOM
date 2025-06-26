<template>
  <div class="category-parent-search" style="width: 750px;">
    <el-autocomplete
      v-model="keyword"
      :fetch-suggestions="querySearchAsync"
      placeholder="请输入父节点关键词搜索"
      @select="handleSelect"
      :trigger-on-focus="false"
      :value-key="'value'"
      popper-class="category-suggestion-box"
    >
      <template #suffix>
        <i class="el-icon-search" />
      </template>
      <template #item="scope">
        <div class="suggestion-item">
            <div class="item-code">{{ scope.row.code }}</div>
            <div class="item-name">{{ scope.row.name }}</div>
            <div class="item-path">{{ scope.row.path }}</div>
        </div>
      </template>
    </el-autocomplete>

  </div>
</template>

<script>
import axios from 'axios';
import { ref, watch } from 'vue';

export default {
  name: 'CategoryParentSearch',
  props: {
    // 当前已选择的父节点对象
    value: {
      type: Object,
      default: () => ({})
    },
    // 初始值（用于编辑场景）
    initialValue: {
      type: Object,
      default: () => ({})
    }
  },
  emits: ['update:value', 'select'],
  setup(props, { emit }) {
    // 搜索关键词
    const keyword = ref('');
    // 搜索结果列表
    const searchResults = ref([]);
    // 已选择的节点
    const selectedNode = ref({});
    // 加载状态
    const loading = ref(false);
    
    // 监听外部传入的value变化
    watch(() => props.value, (newVal) => {
      if (newVal && newVal.id) {
        // 更新选中节点
        selectedNode.value = { ...newVal };
      } else {
        // 清空选择
        clearSelection();
      }
    });
    
    // 初始化
    const init = () => {
      if (props.initialValue && props.initialValue.id) {
        selectedNode.value = { ...props.initialValue };
      }
    };
    
    // 异步搜索方法
    const querySearchAsync = (queryString, callback) => {
      if (!queryString.trim()) {
        callback([]);
        return;
      }
      
      loading.value = true;
      // 模拟API请求，实际项目中替换为真实API
      searchCategory(queryString).then(results => {
        searchResults.value = results;
        callback(results);
        loading.value = false;
      }).catch(() => {
        loading.value = false;
        callback([]);
      });
    };
            
            // 搜索分类API
    const searchCategory = async (keyword) => {
          try {
            // 实际项目中替换为真实API地址
            const response = await axios.get(`/api/category/query?keyword=${keyword}`);
            console.log(response.data.data)
            // 假设返回格式: { code: 0, data: [{ id, code, name, path }] }
            return (response.data.data || []).map(item => ({
                ...item,
                value: item.name // 使用name作为显示值
            }));
          } catch (error) {
            console.error('搜索分类失败:', error);
            return [];
          }
    };
    
    // 处理选择
    const handleSelect = (item) => {
      // 更新选中节点为完整对象
      selectedNode.value = { ...item };
      keyword.value = item.name;
      console.log(item)
      // 通知父组件（传递完整对象）
      emit('update:value', { ...item });
      emit('select', { ...item });
      emit('node-selected', { ...item });
    };
    
    // 清除选择
    const clearSelection = () => {
      selectedNode.value = { id: '', code: '', name: '', path: '' };
      keyword.value = '';
      emit('update:value', {});
    };
    
    init();
    
    return {
      keyword,
      searchResults,
      selectedNode,
      loading,
      querySearchAsync,
      handleSelect,
      clearSelection
    };
  }
}
</script>

<style scoped lang="less">
/* 样式保持不变 */
</style>    