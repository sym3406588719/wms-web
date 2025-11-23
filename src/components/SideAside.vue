<template>
  <el-menu 
    mode="vertical"
    text-color="#FFF143"
    active-text-color="#7c70d2"
    default-active="/Home"
    class="side-aside"
    :collapse="isCollapse"
    :collapse-transition="false"
    router
    style="height:100vh;"
  >
    <!-- 首页菜单（固定保留） -->
    <el-menu-item index="/Home">
      <i class="el-icon-s-home" style="color:#7960fc;"></i>
      <span slot="title">首页</span>
    </el-menu-item>

    <!-- 循环变量统一为menu，与定义保持一致 -->
    <template v-if="menu.length > 0">
      <el-menu-item 
        :index="'/'+(item.menuclick || item.meauClick)" 
        v-for="(item, i) in menu" 
        :key="i"
      >
        <i :class="item.menuIcon || 'el-icon-menu'" style="color:#7960fc;"></i>
        <span slot="title">{{ item.menuname || item.meauName || '未命名菜单' }}</span>
      </el-menu-item>
    </template>

    <!-- 无数据时显示提示 -->
    <div v-else class="empty-tip" style="color:#FFF143; padding:20px; text-align:center;">
      暂无菜单数据
    </div>
  </el-menu>
</template>

<script>
import { mapGetters } from 'vuex'

export default {
  name: "SideAside",
  data() {
    return {
      // 默认菜单数据（变量名不变，作为兜底用）
      defaultMeau: [
        {
          meauClick: 'Admin',
          meauName: '管理员管理',
          meauIcon: 'el-icon-s-custom'
        },
        {
          meauClick: 'User',
          meauName: '用户管理',
          meauIcon: 'el-icon-user-solid'
        }
      ]
    };
  },
  props: {
    isCollapse: {
      type: Boolean,
      default: false
    }
  },
  computed: {
    ...mapGetters(['getMenu']),
    // 核心变量统一命名为menu，替代原来的dynamicMenu
    menu() {
      console.log('Vuex 菜单数据:', this.getMenu);
      // 保留兜底逻辑：Vuex有数据用Vuex的，没有用默认的
      return (this.getMenu && this.getMenu.length > 0) ? this.getMenu : this.defaultMeau;
    }
  },
  mounted() {
    console.log('Vuex 是否有菜单数据:', this.getMenu?.length > 0);
    console.log('最终渲染的菜单数据:', this.menu); // 新增日志，确认menu数据
  }
};
</script>

<style scoped>
.side-aside {
  background: linear-gradient(180deg, #658bc4, #f9b2d4);
  border-right: none;
  overflow: hidden;
}

::v-deep .el-menu-item {
  transition: all 0.3s ease;
  height: 60px;
  line-height: 60px;
}

::v-deep .el-menu-item:hover {
  background-color: rgba(255, 255, 255, 0.1) !important;
}

::v-deep .el-menu-item.is-active {
  background-color: rgba(124, 112, 210, 0.2) !important;
}

::v-deep .el-menu-item i {
  margin-right: 8px;
  font-size: 18px;
}
</style>
    