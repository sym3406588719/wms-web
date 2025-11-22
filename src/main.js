import Vue from 'vue'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import App from './App.vue'
// 注意：如果使用路由，需要先导入 router
import router from './router'  // 确保该文件存在且正确配置

Vue.config.productionTip = false
Vue.use(ElementUI)

// 只创建一个 Vue 实例，合并配置
new Vue({
  el: '#app',  // 挂载到 #app 元素
  router,      // 注入路由（如果项目使用路由）
  render: h => h(App)  // 渲染根组件 App
})