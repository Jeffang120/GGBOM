<template>
  <div class="tree-container">
    <el-tree
      :data="treeData"
      :props="treeProps"
      @node-click="handleNodeClick"
      @node-expand="handleNodeExpand"
      :load="loadNode"
      lazy
      class="custom-tree"
    >
      <template #default="{ node, data }">
        <div class="custom-tree-node">
          <span class="node-code">{{ data.businessCode }}</span>
          <span class="node-divider">-</span>
          <span class="node-name">{{ data.name }}</span>
        </div>
      </template>
    </el-tree>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue';
import axios from 'axios';
export default {
  setup() {
    // 树结构数据
    const treeData = ref([]);
    
    // 树配置
    const treeProps = ref({
      children: 'children',
      label: 'name',
      isLeaf: 'isLeaf'
    });
    
    const getCategories = async () => {
      // 替换为实际获取全部分类的接口
      const response = await axios.get('/api/category/getAll'); 
      return response;
    };

    const getSubCategories=async (parentId) => { 
        const response = await axios.get(`/api/category/children?parentId=${parentId}`);
        return response;
    };
    
    // 加载根节点
    const loadRootNode = async () => {
      try {
        // 调用获取全部分类的接口
        const response = await getCategories();
        console.log('获取全部分类数据成功', response.data.data);
        // 处理返回数据
        treeData.value = response.data.data.map(item => ({
          id: item.id,
          name: item.name,
          businessCode: item.businessCode || 'N/A', // 添加分类编码
          // 默认设置为非叶子节点，以便可以展开
          isLeaf: false,
          // 标记是否已加载子节点
          loaded: false,
          parentNode: item.parentNode,
        })).filter(item1 => item1.parentNode === null);
        
        console.log('根节点数据加载完成', treeData.value);
      } catch (error) {
        console.error('加载根节点失败', error);
        // 可以在这里添加错误提示
      }
    };
    
    // 加载子节点
    const loadNode = async (node, resolve) => {
      // 如果是根节点，直接返回已加载的数据
      if (node.level === 0) {
        return resolve(treeData.value);
      }
      
      // 如果节点已经加载过子节点，直接返回
      if (node.data.loaded) {
        return resolve(node.data.children || []);
      }
      
      try {
        // 调用获取子分类的接口
        const response = await getSubCategories(node.data.id);
        console.log(response.data.data);
        // 处理子节点数据
        const children = response.data.data.map(item => ({
          id: item.id,
          name: item.name,
          businessCode: item.businessCode || 'N/A', // 添加分类编码
          // 根据实际情况判断是否为叶子节点
          isLeaf: item.hasChildren === false,
          loaded: false
        }));
        
        // 更新节点状态
        node.data.children = children;
        node.data.loaded = true;
        
        console.log(`加载节点 ${node.data.name} 的子节点完成`, children);
        resolve(children);
      } catch (error) {
        console.error(`加载节点 ${node.data.name} 的子节点失败`, error);
        // 可以在这里添加错误提示
        resolve([]);
      }
    };
    
    // 节点点击事件
    const handleNodeClick = (data) => {
      console.log('点击节点', data);
      // 这里可以添加节点点击后的处理逻辑
    };
    
    // 节点展开事件
    const handleNodeExpand = (data) => {
      console.log('展开节点', data);
      // 可以在这里添加节点展开后的处理逻辑
    };
    
    // 组件挂载后加载根节点
    onMounted(() => {
      loadRootNode();
    });
    
    return {
      treeData,
      treeProps,
      handleNodeClick,
      handleNodeExpand,
      loadNode
    };
  }
};
</script>

<style lang="less" scoped>
.tree-container {
  padding: 10px;

  .custom-tree {
    background: transparent;

    :deep(.el-tree-node__content) {
      height: 36px;
      border-radius: 4px;
      margin: 2px 0;
      transition: all 0.3s ease;

      &:hover {
        background-color: #e6f1fc;
      }
    }

    :deep(.el-tree-node.is-current > .el-tree-node__content) {
      background-color: #e6f1fc;
      color: #409eff;
    }

    .custom-tree-node {
      display: flex;
      align-items: center;
      font-size: 14px;
      padding: 0 4px;

      .node-code {
        color: #666;
        font-family: monospace;
        margin-right: 4px;
      }

      .node-divider {
        color: #999;
        margin: 0 6px;
      }

      .node-name {
        color: #333;
        font-weight: 500;
      }
    }
  }
}
</style>