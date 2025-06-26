import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'

// 全局错误处理器 - 屏蔽ResizeObserver错误
const originalError = console.error;
console.error = (...args) => {
    if (args[0] && typeof args[0] === 'string' && args[0].includes('ResizeObserver loop completed with undelivered notifications')) {
        // 屏蔽ResizeObserver错误，这是Element Plus组件的已知问题，不影响功能
        return;
    }
    originalError.apply(console, args);
};

// 全局未捕获错误处理
window.addEventListener('error', (event) => {
    if (event.message && event.message.includes('ResizeObserver loop completed with undelivered notifications')) {
        event.preventDefault();
        return false;
    }
});

const app = createApp(App)

// 注册所有Element Plus图标
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component)
}

app.use(store).use(router).use(ElementPlus).mount('#app')
