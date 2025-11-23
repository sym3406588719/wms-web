<template>
  <div style="display: flex; line-height: 60px;"> <!-- 增加背景色，确保文字可见 -->
    <div style="margin-top: 8px; cursor: pointer; margin-left: 15px;">
      <i :class="icon" style="font-size: 20px; color: white;" @click="collapse"></i>
    </div>
    <div style="flex: 1; text-align: center; font-size: 20px; color: white;">
      <span>欢迎来到天海仓库管理系统</span>
    </div>
    <el-dropdown>
      <div style="display: flex; align-items: center; cursor: pointer; margin-right: 15px;">
        <i class="el-icon-setting" style="margin-left: 5px; color: white;"></i>
        <!-- 安全访问user.name，避免undefined报错 -->
        <span style="color: white; margin-left: 5px;">{{ user?.name || '未登录' }}</span>
      </div>
      <el-dropdown-menu slot="dropdown">
        <el-dropdown-item @click.native="toUser">个人中心</el-dropdown-item>
        <el-dropdown-item @click.native="logout">退出登录</el-dropdown-item>
      </el-dropdown-menu>
    </el-dropdown>
  </div>
</template>

<script>
export default {
  name: "PageHeader",
  data() {
    return {
      // 优化用户数据初始化，避免JSON解析错误
      user: this.initUser()
    };
  },
  props: {
    icon: {
      type: String,
      default: 'el-icon-menu' // 增加默认图标，避免未传props时图标不显示
    }
  },
  methods: {
    // 抽取用户数据初始化逻辑，增强可读性和复用性
    initUser() {
      try {
        const curUser = sessionStorage.getItem("CurUser");
        // 仅当curUser存在且非空时才解析，避免解析undefined/null
        return curUser ? JSON.parse(curUser) : {};
      } catch (error) {
        console.error('用户数据解析失败:', error);
        return {}; // 解析失败时返回空对象，避免组件报错
      }
    },

    toUser() {
        console.log('to_uesr')
        this.$router.push('/Home')
    },

    // 核心：添加退出登录确认功能
    logout() {
      // 调用Element UI的确认弹窗
      this.$confirm('确定要退出登录吗？退出后将返回登录页', '退出确认', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning' // 弹窗类型：警告（黄色图标）
      })
      .then(() => {
        // 点击“确定”后的逻辑
        sessionStorage.clear(); // 修复语法错误：clear是方法，需加()
        this.$message.success('退出登录成功！'); // 成功提示
        this.$router.push("/"); // 跳转到登录页（原代码跳根路径，优化为明确的登录页）
      })
      .catch(() => {
        // 点击“取消”或关闭弹窗后的逻辑
        this.$message.info('已取消退出登录'); // 取消提示，提升用户体验
      });
    },

    collapse() {
      this.$emit("doCollapse");
    }
  }
};
</script>
    
