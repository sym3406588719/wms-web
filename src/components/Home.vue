<template>
  <!-- 外层容器：限制最大宽度+水平居中，避免宽屏超出 -->
  <div class="user-center-wrapper">
    <!-- 页面标题 -->
    <div class="page-header">
      <el-breadcrumb separator=">" class="breadcrumb">
        <el-breadcrumb-item><i class="el-icon-home"></i> 首页</el-breadcrumb-item>
        <el-breadcrumb-item>个人中心</el-breadcrumb-item>
      </el-breadcrumb>
      <h2>我的个人中心</h2>
    </div>

    <!-- 主内容区：响应式布局，小屏自动换行 -->
    <div class="main-content">
      <!-- 左侧用户信息卡片：精简布局 -->
      <div class="user-info-card">
        <div class="avatar-box">
          <!-- 头像区域 -->
          <el-avatar :size="90" class="user-avatar">
            <img :src="user.avatar || defaultAvatar" alt="用户头像">
          </el-avatar>
          <el-button type="text" @click="showAvatarUpload = true" class="change-avatar">
            更换头像
          </el-button>
        </div>
        
        <!-- 基础信息：精简冗余样式 -->
        <div class="user-info">
          <h3 class="user-name">{{ user.name || '未设置昵称' }}</h3>
          <p class="user-role">{{ user.role || '普通用户' }}</p>
          <p class="join-time">
            <i class="el-icon-calendar"></i>
            注册时间: {{ user.joinDate || '未知' }}
          </p>
          <p class="last-login">
            <i class="el-icon-clock"></i>
            最后登录: {{ lastLoginTime || '从未登录' }}
          </p>
        </div>
      </div>

      
      <div class="edit-area">
        <!-- 基本资料：el-form 设置 label-width，el-form-item 移除 label-width -->
        <el-card class="edit-card">
          <div slot="header" class="card-header">基本资料</div>
          <el-form 
            :model="userForm" 
            ref="userForm"
            :rules="userFormRules"
            label-width="90px"
            class="form"
          >
            <el-form-item label="用户名" prop="name">
              <el-input v-model="userForm.name" placeholder="请输入昵称"></el-input>
            </el-form-item>
            <el-form-item label="邮箱" prop="email">
              <el-input v-model="userForm.email" placeholder="请输入邮箱"></el-input>
            </el-form-item>
            <el-form-item label="手机号" prop="phone">
              <el-input v-model="userForm.phone" placeholder="请输入手机号"></el-input>
            </el-form-item>
            <el-form-item label="性别">
              <el-radio-group v-model="userForm.gender">
                <el-radio label="男">男</el-radio>
                <el-radio label="女">女</el-radio>
                <el-radio label="保密">保密</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="简介">
              <el-input v-model="userForm.introduction" type="textarea" rows="3" placeholder="简单介绍自己吧~"></el-input>
            </el-form-item>
            <el-form-item class="form-btn">
              <el-button type="primary" @click="saveUserInfo">保存</el-button>
              <el-button @click="resetUserForm">重置</el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </div>
    </div>

    <!-- 头像上传弹窗：精简配置 -->
    <el-dialog 
      title="更换头像" 
      :visible.sync="showAvatarUpload" 
      :width="isSmallScreen ? '80%' : '30%'"
      :close-on-click-modal="false"
    >
      <el-upload
        class="avatar-upload"
        action="/api/upload/avatar"
        :show-file-list="false"
        :on-success="handleAvatarSuccess"
        :before-upload="beforeAvatarUpload"
      >
        <img v-if="tempAvatarUrl" :src="tempAvatarUrl" class="preview-avatar">
        <i v-else class="el-icon-plus upload-icon"></i>
      </el-upload>
      <div slot="footer">
        <el-button @click="showAvatarUpload = false">取消</el-button>
        <el-button type="primary" @click="confirmAvatarChange">确认</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'UserCenter',
  data() {
    return {
      defaultAvatar: 'https://cube.elemecdn.com/3/7c/3ea6beec643bed482f7661d1e6dd1jpeg.jpeg',
      user: {},
      lastLoginTime: '',
      userForm: { name: '', email: '', phone: '', gender: '保密', introduction: '' },
      userFormRules: {
        name: [{ required: true, message: '请输昵称', trigger: 'blur' }, { min: 2, max: 15, message: '昵称2-15字', trigger: 'blur' }],
        email: [{ type: 'email', message: '邮箱格式不对', trigger: 'blur' }],
        phone: [{ pattern: /^1[3-9]\d{9}$/, message: '手机号格式不对', trigger: 'blur' }]
      },
      showAvatarUpload: false,
      tempAvatarUrl: '',
      isSmallScreen: window.innerWidth < 768 // 小屏判断
    };
  },
  created() {
    this.getUserInfo();
    // 监听窗口变化，实时适配
    window.addEventListener('resize', () => { this.isSmallScreen = window.innerWidth < 768; });
  },
  beforeDestroy() { 
    // 清理监听，避免内存泄漏
    window.removeEventListener('resize', () => { this.isSmallScreen = window.innerWidth < 768; }); 
  },
  methods: {
    // 精简用户信息获取逻辑
    getUserInfo() {
      const userStr = sessionStorage.getItem('CurUser');
      if (userStr) {
        this.user = JSON.parse(userStr);
        this.userForm = { ...this.userForm, ...this.user }; // 简化赋值
        this.lastLoginTime = this.user.lastLoginTime ? this.formatDate(this.user.lastLoginTime) : '';
      } else {
        this.$router.push('/login');
        this.$message.warning('请先登录');
      }
    },
    // 保存资料：精简代码
    saveUserInfo() {
      this.$refs.userForm.validate(valid => {
        if (valid) {
          this.user = { ...this.user, ...this.userForm };
          sessionStorage.setItem('CurUser', JSON.stringify(this.user));
          this.$message.success('资料保存成功~');
        }
      });
    },
    // 头像上传相关：精简逻辑
    beforeAvatarUpload(file) {
      const isImg = file.type === 'image/jpeg' || file.type === 'image/png';
      const isLt2M = file.size / 1024 / 1024 < 2;
      if (!isImg) this.$message.error('仅支持JPG/PNG~');
      if (!isLt2M) this.$message.error('图片不超过2MB~');
      return isImg && isLt2M;
    },
    handleAvatarSuccess(res) { 
      this.tempAvatarUrl = URL.createObjectURL(res.raw); 
    },
    confirmAvatarChange() {
      this.user.avatar = this.tempAvatarUrl;
      sessionStorage.setItem('CurUser', JSON.stringify(this.user));
      this.$message.success('头像更换成功~');
      this.showAvatarUpload = false;
    },
    // 工具函数：精简
    formatDate(dateStr) {
      const d = new Date(dateStr);
      return `${d.getFullYear()}-${this.padZero(d.getMonth() + 1)}-${this.padZero(d.getDate())} ${this.padZero(d.getHours())}:${this.padZero(d.getMinutes())}`;
    },
    padZero(num) { 
      return num < 10 ? '0' + num : num; 
    },
    // 仅保留基本资料重置方法，删除密码重置方法
    resetUserForm() { this.$refs.userForm.resetFields(); }
  }
};
</script>

<style scoped>
/* 浪漫色系：粉紫+浅蓝为主色调 */
.user-center-wrapper {
  width: 100%;
  max-width: 1200px; /* 宽屏限制最大宽度，避免超出 */
  margin: 0 auto;
  padding: 15px;
  box-sizing: border-box;
  background-color: #fdf7ff; /* 浅紫背景，浪漫柔和 */
 margin-top: 15px;
}

/* 标题区：浪漫色改造 */
.page-header { margin-bottom: 20px; }
.page-header h2 { 
  margin: 0; 
  color: #9370db; /* 中紫，浪漫主色 */
  font-weight: 500;
}
.breadcrumb { 
  margin-bottom: 10px; 
  color: #b19cd9; /* 浅紫，柔和配色 */
}

/* 主内容区：响应式布局 */
.main-content {
  display: flex;
  gap: 20px;
  flex-wrap: wrap; /* 小屏自动换行，避免超出 */
  align-items: flex-start;
}

/* 左侧信息卡：浪漫色+精简布局 */
.user-info-card {
  width: 280px;
  min-width: 250px; /* 小屏最小宽度，避免挤压 */
  background: #fff;
  border-radius: 12px; /* 圆角更柔和 */
  padding: 20px;
  box-sizing: border-box;
  box-shadow: 0 2px 10px rgba(177, 156, 217, 0.15); /* 浅紫阴影，浪漫感 */
}
.avatar-box {
  text-align: center;
  margin-bottom: 15px;
}
.user-avatar { 
  border: 3px solid #e6e0f8; /* 浅紫边框，呼应主色 */
  margin: 0 auto 8px;
}
.change-avatar { 
  color: #9370db; /* 中紫，按钮色 */
  font-size: 14px;
}
.user-info { text-align: center; }
.user-name { 
  margin: 0 0 8px; 
  color: #9370db; 
}
.user-role { 
  margin: 0 0 8px; 
  color: #b19cd9; 
  font-size: 14px;
}
.join-time, .last-login { 
  margin: 0 0 5px; 
  color: #c4b5e8; /* 浅紫灰，辅助色 */
  font-size: 13px;
}

/* 右侧编辑区：仅保留基本资料样式 */
.edit-area {
  flex: 1;
  min-width: 300px; /* 小屏最小宽度，避免超出 */
}
.edit-card {
  /* 替换纯色背景为：浅紫 → 淡粉紫 的线性渐变（从上到下） */
  background: linear-gradient(to bottom, #e6e0f8, #c2ace2); 
  border-radius: 12px;
  margin-bottom: 15px;
  box-shadow: 0 2px 10px rgba(177, 156, 217, 0.1);
  border: none;
}
.card-header { 
  color: #9370db; /* 中紫，标题色 */
  font-weight: 500;
  font-size: 15px;
}
.form { margin-top: 10px; }
.el-form-item { margin-bottom: 15px; }
/* 表单输入框：浪漫色边框 */
.el-input__inner, .el-textarea__inner {
  border-color: #e6e0f8; /* 浅紫边框 */
  border-radius: 8px;
}
.el-input__inner:focus, .el-textarea__inner:focus {
  border-color: #9370db; /* 聚焦时中紫边框 */
  box-shadow: 0 0 0 2px rgba(147, 112, 219, 0.1);
}
/* 单选框：浪漫色 */
.el-radio__input.is-checked .el-radio__inner {
  background-color: #9370db;
  border-color: #9370db;
}
.el-radio__inner:hover { border-color: #9370db; }
/* 按钮：浪漫色 */
.el-button--primary {
  background-color: #9370db;
  border-color: #9370db;
  border-radius: 8px;
}
.el-button--primary:hover {
  background-color: #805ad5; /* 深紫，hover色 */
  border-color: #805ad5;
}
.el-button { border-radius: 8px; }
.form-btn { margin-bottom: 5px; }

/* 头像上传弹窗：浪漫色 */
.avatar-upload {
  border: 1px dashed #e6e0f8; /* 浅紫虚线边框，呼应浪漫色系 */
  border-radius: 8px;
  text-align: center;
  padding: 20px;
  margin-bottom: 15px;
}
.upload-icon { 
  color: #b19cd9; /* 浅紫图标色，柔和不刺眼 */
  font-size: 24px;
  line-height: 1;
  margin-bottom: 8px;
}
.preview-avatar {
  width: 100%;
  max-width: 180px;
  height: auto;
  border-radius: 8px;
  margin: 0 auto;
  object-fit: cover; /* 保证头像不变形 */
  border: 2px solid #e6e0f8; /* 浅紫边框，提升精致感 */
}

/* 弹窗标题：浪漫色改造 */
.el-dialog__title { 
  color: #9370db; /* 中紫标题色，统一视觉风格 */
  font-weight: 500;
}
/* 弹窗按钮区：间距调整 */
.el-dialog__footer { 
  padding-top: 0; 
  text-align: right;
}
.el-dialog__footer .el-button { 
  margin-left: 10px; 
}

/* 小屏适配：768px以下（平板/手机横屏） */
@media (max-width: 768px) {
  .main-content { gap: 15px; }
  .user-info-card { 
    width: 100%; /* 小屏时信息卡占满宽度，避免挤压 */
    max-width: 400px; /* 限制最大宽度，避免过宽 */
    margin: 0 auto; /* 居中显示，提升美观度 */
  }
  .edit-area { 
    min-width: 100%; /* 编辑区占满宽度，避免超出屏幕 */
    max-width: 500px;
    margin: 0 auto;
  }
  .page-header h2 { font-size: 18px; }
  .card-header { font-size: 14px; }
  .form { margin-top: 8px; }
  .el-form-item { margin-bottom: 12px; }
  .el-form-item__label { font-size: 13px; }
  .el-input__inner, .el-textarea__inner { font-size: 13px; }
  /* 小屏按钮调整：缩小尺寸，节省空间 */
  .form-btn .el-button, .el-dialog__footer .el-button { 
    padding: 8px 16px; 
    font-size: 13px; 
  }
  /* 小屏头像上传区调整 */
  .avatar-upload { padding: 15px; }
  .upload-icon { font-size: 20px; }
  .preview-avatar { max-width: 150px; }
}

/* 超小屏适配：375px以下（手机窄屏） */
@media (max-width: 375px) {
  .user-center-wrapper { padding: 10px; }
  .page-header { margin-bottom: 15px; }
  .breadcrumb{ font-size: 12px; }
  .user-info-card { 
    padding: 15px; 
    max-width: 100%;
  }
  .user-avatar { size: 80px; } /* 缩小头像，节省窄屏空间 */
  .user-name { font-size: 16px; }
  .user-role, .join-time, .last-login { 
    font-size: 12px; 
    margin-bottom: 4px; /* 减少间距，避免内容溢出 */
  }
  .edit-card { padding: 12px; }
  .el-form-item__label { font-size: 12px; }
  /* 超小屏按钮纵向排列：避免横向挤压超出屏幕 */
  .form-btn { 
    display: flex; 
    flex-direction: column; 
    gap: 8px; 
  }
  .form-btn .el-button { 
    width: 100%; 
    padding: 6px 12px; 
    font-size: 12px; 
  }
  /* 超小屏弹窗优化：宽度占比提升，适配手机屏幕 */
  .el-dialog { width: 90% !important; }
  .preview-avatar { max-width: 120px; } /* 缩小预览头像，适配弹窗 */
}

/* 修复Element UI默认样式冲突 */
/* 1. 去除表单最后一项多余底部间距，避免卡片内留白过多 */
.el-form-item:last-child { margin-bottom: 0; }
/* 2. 优化单选框横向间距，避免选项挤在一起 */
.el-radio { margin-right: 15px; }
.el-radio:last-child { margin-right: 0; }
/* 3. 修复文本域默认最小高度，避免输入时高度突变 */
.el-textarea__inner { min-height: 80px; }
/* 4. 优化面包屑图标与文字对齐，提升视觉一致性 */
.el-breadcrumb__inner i { vertical-align: middle; margin-right: 4px; }
</style>