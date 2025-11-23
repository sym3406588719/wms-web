import Vue from 'vue'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import './assets/global.css'
import App from './App.vue'
import axios from 'axios'
import store from './store'

// 导入路由实例
import router from './router'  // 从router目录导入路由配置

// 使用Element UI
Vue.use(ElementUI)
// 关闭生产环境提示
Vue.config.productionTip = false
Vue.prototype.$axios=axios;
Vue.prototype.$httpUrl='http://localhost:8091'
// 创建并挂载Vue实例，注入路由
new Vue({
  el: '#app',
  router,  // 将路由实例注入Vue，使整个应用可用
  store,
  render: h => h(App)  // 渲染根组件
})
