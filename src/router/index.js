import Vue from 'vue'
import VueRouter from 'vue-router'

// 注册路由插件
Vue.use(VueRouter)

// 定义路由
const routes = [
    {
        path: '/',
        name: 'login',
        component: () => import('../components/HomeLogin.vue')
    },
    {
        path: '/index',
        name: 'index',
        component: () => import('../components/index.vue'),
        children: [
            {
                path: '/Home',  // 子路由以/开头视为根路径
                name: 'home',
                meta: {
                    title: '首页'
                },
                component: () => import('../components/Home.vue')
            }
            // 动态路由将通过store添加
        ]
    }
]

// 创建路由实例（Vue Router 3.x 写法）
const router = new VueRouter({
    mode: 'history',  // 3.x版本使用mode属性
    routes
})

// 重置路由函数（适配3.6.5版本）
export function resetRouter() {
    const newRouter = new VueRouter({
        mode: 'history',
        routes: []
    })
    router.matcher = newRouter.matcher  // 重置路由匹配器
}

// 重写push方法解决重复导航错误（3.x版本兼容写法）
const originalPush = VueRouter.prototype.push
VueRouter.prototype.push = function push(to) {
    return originalPush.call(this, to).catch(err => {
        if (err.name !== 'NavigationDuplicated') {
            throw err
        }
    })
}

export default router;
    
    
    
