import Vue from 'vue'
import Vuex from 'vuex'
import router, { resetRouter } from "../router";


Vue.use(Vuex)

function addNewRoute(menuList) {
    console.log('菜单列表:', menuList);
    if (!menuList || menuList.length === 0) return;

    resetRouter()

    const baseRoutes = [
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
                    path: '/Home',
                    name: 'home',
                    meta: { title: '首页' },
                    component: () => import('../components/Home.vue')
                }
            ]
        }
    ]

    baseRoutes.forEach(route => {
        router.addRoute(route)
    })

    // 动态添加路由：核心修复——强制兜底+路径校验
    menuList.forEach(menu => {
        console.log('菜单原始数据:', menu);
        // 1. 处理路由路径（避免空路径）
        const clickPath = menu.menuclick || menu.meauClick;
        const routePath = clickPath ? `/${clickPath}` : '/default'; // 空路径兜底为 /default

        // 2. 处理组件路径（关键：强制兜底，绝对不让路径为undefined）
        let componentPath = '';
        // 情况1：如果有menucomponent，直接用（但要校验格式）
        if (menu.menucomponent && menu.menucomponent.trim()) {
            componentPath = menu.menucomponent.trim();
            // 补全.vue后缀（防止后端漏传）
            if (!componentPath.endsWith('.vue')) {
                componentPath += '.vue';
            }
        } 
        // 情况2：没有menucomponent，按路径自动匹配（比如Admin对应admin/AdminManage.vue）
        else if (clickPath) {
            // 示例：clickPath为Admin → 组件路径默认为 admin/AdminManage.vue
            // 根据你的实际组件文件夹结构调整，这里是最常见的匹配逻辑
            componentPath = `${clickPath.toLowerCase()}/${clickPath}Manage.vue`;
        } 
        // 情况3：啥都没有，直接指向首页组件（终极兜底）
        else {
            componentPath = 'Home.vue';
        }

        // 3. 最终生成路由（用try-catch包裹，防止个别路由出错影响整体）
        try {
            const childRoute = {
                path: routePath,
                name: menu.menuname || menu.meauName || `route_${Date.now()}`, // 避免name重复
                meta: { title: menu.menuname || menu.meauName || '未命名菜单' },
                component: () => import(`../components/${componentPath}`) // 这里路径绝对有效
            };
            // 避免重复添加路由
            const isExist = router.getRoutes().some(r => r.path === routePath);
            if (!isExist) {
                router.addRoute('index', childRoute);
                console.log('成功添加路由:', routePath, '→', componentPath);
            }
        } catch (err) {
            console.error('添加路由失败（已自动跳过）:', err, '路由信息:', menu);
        }
    })

    console.log('最终路由配置:', router.getRoutes().map(r => r.path));
}

export default new Vuex.Store({
    state: {
        menu: []
    },
    mutations: {
        setMenu(state, menuList) {
            // 先过滤无效数据（只保留有clickPath的菜单）
            state.menu = (menuList || []).filter(menu => {
                const hasClickPath = !!menu.menuclick || !!menu.meauClick;
                if (!hasClickPath) {
                    console.warn('过滤无效菜单（无路径）:', menu);
                }
                return hasClickPath;
            });
            addNewRoute(state.menu);
        }
    },
    getters: {
        getMenu(state) {
            return state.menu;
        }
    }
})

    
