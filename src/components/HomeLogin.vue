<template>
    <div class="loginBody">
        <div class="login-bg-decoration bg-circle circle1"></div>
        <div class="login-bg-decoration bg-circle circle2"></div>
        <div class="login-bg-decoration bg-circle circle3"></div>

        <!-- 修改为底部居中 -->
        <div class="bottom-center-image">
            <img src="/lihui.jpg" alt="立绘" class="lihui-img">
            <div class="image-caption">WMS 仓库管理系统</div>
        </div>

        <div class="loginDiv" :class="{ 'login-div-hover': isHover }" 
             @mouseenter="isHover = true" @mouseleave="isHover = false">
            <!-- 原有内容保持不变 -->
            <div class="login-content">
                <div class="login-header">
                    <i class="el-icon-user login-icon"></i>
                    <h1 class="login-title">用户登录</h1>
                </div>

                <el-form 
                    :model="loginForm" 
                    label-width="80px"
                    :rules="rules" 
                    ref="loginForm"
                    class="login-form"
                >
                    <!-- 表单内容保持不变 -->
                    <el-form-item label="账号" prop="no" class="form-item">
                        <el-input 
                            v-model="loginForm.no"
                            placeholder="请输入账号（3-8位）"
                            autocomplete="off" 
                            size="medium"
                            class="form-input"
                            prefix-icon="el-icon-user"
                        ></el-input>
                    </el-form-item>
                    <el-form-item label="密码" prop="password" class="form-item">
                        <el-input 
                            v-model="loginForm.password"
                            type="password"
                            placeholder="请输入密码（3-8位）"
                            show-password 
                            autocomplete="off" 
                            size="medium"
                            class="form-input"
                            prefix-icon="el-icon-lock" 
                            @keyup.enter.native="confirm"
                        ></el-input>
                    </el-form-item>

                    <el-form-item class="form-btn-group">
                        <el-button 
                            type="primary" 
                            @click="confirm" 
                            :disabled="confirm_disabled"
                            :loading="isLoading"
                            class="login-btn"
                        >
                            登录
                        </el-button>
                        <el-link type="info" class="forgot-link" @click="handleForgot">
                            忘记密码？
                        </el-link>
                    </el-form-item>
                </el-form>
            </div>
        </div>
    </div>
</template>

<script>
// 原有script内容保持不变
import router from '../router'

export default {
    name: "HomeLogin",
    data() {
        return {
            confirm_disabled: false,
            isHover: false,
            isLoading: false,
            loginForm: {
                no: '',
                password: ''
            },
            rules: {
                no: [
                    { required: true, message: '请输入账号', trigger: 'blur' },
                    { min: 3, max: 8, message: '账号长度需在3-8位之间', trigger: 'blur' }
                ],
                password: [
                    { required: true, message: '请输入密码', trigger: 'blur' },
                    { min: 3, max: 8, message: '密码长度需在3-8位之间', trigger: 'blur' }
                ]
            }
        };
    },
    methods: {
        // 原有方法保持不变
        confirm() {
            this.confirm_disabled = true;
            this.isLoading = true;

            this.$refs.loginForm.validate((valid) => {
                if (valid) {
                    this.$axios.post(`${this.$httpUrl}/user/login`, this.loginForm)
                        .then(res => {
                            console.log('登录接口完整响应:', res);
                            return res.data;
                        })
                        .then(res => {
                            console.log('登录接口返回data:', res);
                            if (res.code === 200) {
                                sessionStorage.setItem("CurUser", JSON.stringify(res.data.user));
                                
                                const menuData = this.formatMenuData(res.data.menu);
                                this.$store.commit("setMenu", menuData);

                                this.$message.success({
                                    message: '登录成功，正在跳转主页...',
                                    duration: 1500,
                                    onClose: () => {
                                        this.checkRoutesAndRedirect();
                                    }
                                });
                            } else {
                                this.$message.error(res.msg || '校验失败，用户名或密码错误！');
                                this.resetForm();
                            }
                        })
                        .catch(err => {
                            console.error('登录请求失败：', err);
                            this.$message.error('网络异常，请稍后重试');
                            this.resetForm();
                        });
                } else {
                    this.$message.warning('请完善表单信息后提交');
                    this.resetForm();
                    return false;
                }
            });
        },

        formatMenuData(menuFromBackend) {
            if (!menuFromBackend || menuFromBackend.length === 0) {
                console.warn('后端返回menu为空，使用默认菜单');
                return [
                    { 
                        menuclick: 'Admin', 
                        menuname: '管理员管理', 
                        menuIcon: 'el-icon-s-custom',
                        menucomponent: 'admin/AdminManage'
                    },
                    { 
                        menuclick: 'User', 
                        menuname: '用户管理', 
                        menuIcon: 'el-icon-user-solid',
                        menucomponent: 'user/UserManage'
                    },
                    { 
                        menuclick: 'Storage', 
                        menuname: '仓库管理', 
                        menuIcon: 'el-icon-s-grid',
                        menucomponent: 'storage/StorageManage'
                    },
                    {
                        menuclick: 'GoodsType', 
                        menuname:  '物品分类', 
                        menuIcon: 'el-icon-menu',
                        menucomponent: 'goodstype/GoodsTypeManage' 
                    },
                    {
                        menuclick: 'Goods', 
                        menuname:  '物品管理', 
                        menuIcon: 'el-icon-s-cooperation',
                        menucomponent: 'goods/GoodsManage' 
                    },
                    {
                        menuclick: 'Record', 
                        menuname:  '记录管理', 
                        menuIcon: 'el-icon-s-order',
                        menucomponent: 'record/RecordManage' 
                    }
                ];
            }

            return menuFromBackend.map(item => {
                const baseClick = item.menuclick || item.meauClick || 'default';
                
                return {
                    menuclick: baseClick,
                    menuname: item.menuname || item.meauName || '未命名菜单',
                    menuIcon: item.menuIcon || item.meauIcon || 'el-icon-menu',
                    menucomponent: item.menucomponent 
                        ? item.menucomponent 
                        : `${baseClick.toLowerCase()}/${baseClick}Manage`
                };
            }).filter(item => item.menuclick !== 'default');
        },

        checkRoutesAndRedirect() {
            const routes = router.getRoutes().map(r => r.path);
            console.log('当前所有路由:', routes);
            
            if (routes.includes('/index')) {
                this.$router.replace('/index');
            } else {
                const firstMenu = this.$store.getters.getMenu[0];
                if (firstMenu) {
                    this.$router.replace(`/${firstMenu.menuclick}`);
                } else {
                    this.$router.replace('/Home');
                }
            }
        },

        resetForm() {
            this.confirm_disabled = false;
            this.isLoading = false;
        },

        handleForgot() {
            this.$message.info('请联系管理员重置密码');
        }
    }
};
</script>

<style scoped>
/* 原有样式保持不变 */
.loginBody {
    position: absolute;
    width: 100%;
    height: 100%;
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    overflow: hidden;
}
.login-bg-decoration.bg-circle {
    position: absolute;
    border-radius: 50%;
    background: rgba(255, 255, 255, 0.1);
    animation: float 8s ease-in-out infinite;
}
.circle1 {
    width: 400px;
    height: 400px;
    top: -100px;
    left: -100px;
    animation-delay: 0s;
}
.circle2 {
    width: 300px;
    height: 300px;
    bottom: -80px;
    right: -80px;
    animation-delay: 2s;
}
.circle3 {
    width: 200px;
    height: 200px;
    top: 50%;
    right: 10%;
    transform: translateY(-50%);
    animation-delay: 4s;
}
@keyframes float {
    0% { transform: translate(0, 0) scale(1); }
    50% { transform: translate(20px, 20px) scale(1.05); }
    100% { transform: translate(0, 0) scale(1); }
}

/* 修改为底部居中样式 */
.bottom-center-image {
    position: absolute;
    bottom: 30px;
    left: 50%;
    transform: translateX(-50%);
    z-index: 10;
    text-align: center;
}

.lihui-img {
    width: 200px;
    height: auto;
    border-radius: 12px;
    box-shadow: 0 6px 20px rgba(0, 0, 0, 0.25);
    transition: all 0.4s ease;
    opacity: 0.9;
}

.lihui-img:hover {
    transform: scale(1.08) translateY(-5px);
    opacity: 1;
    box-shadow: 0 8px 25px rgba(0, 0, 0, 0.35);
}

.image-caption {
    margin-top: 10px;
    color: rgba(255, 255, 255, 0.85);
    font-size: 14px;
    font-weight: 400;
    text-shadow: 0 2px 4px rgba(0, 0, 0, 0.3);
    letter-spacing: 1px;
}

.loginDiv {
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    width: 420px;
    padding: 30px 20px;
    background: rgba(255, 255, 255, 0.95);
    border-radius: 12px;
    box-shadow: 0 8px 24px rgba(0, 0, 0, 0.15);
    transition: all 0.3s ease;
}
.login-div-hover {
    box-shadow: 0 12px 32px rgba(0, 0, 0, 0.2);
    transform: translate(-50%, -52%);
}
.login-header {
    display: flex;
    align-items: center;
    justify-content: center;
    margin-bottom: 30px;
}
.login-icon {
    font-size: 24px;
    color: #667eea;
    margin-right: 12px;
}
.login-title {
    font-size: 22px;
    font-weight: 600;
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    -webkit-background-clip: text;
    background-clip: text;
    color: transparent;
    margin: 0;
}
.login-form {
    width: 100%;
    max-width: 320px;
    margin: 0 auto;
}
.form-item {
    margin-bottom: 22px;
}
.form-input {
    border-radius: 6px;
    border: 1px solid #e5e7eb;
    transition: all 0.3s ease;
}
.form-input:focus {
    border-color: #667eea;
    box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.15);
}
.form-btn-group {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-top: 25px;
}
.login-btn {
    width: 160px;
    height: 42px;
    border-radius: 6px;
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    border: none;
    font-size: 15px;
    transition: all 0.3s ease;
}
.login-btn:hover, .login-btn:focus {
    opacity: 0.9;
    transform: translateY(-2px);
    box-shadow: 0 4px 12px rgba(102, 126, 234, 0.3);
}
.login-btn:disabled {
    background: linear-gradient(135deg, #a3bffa 0%, #c1a5d4 100%);
    cursor: not-allowed;
    transform: none;
    box-shadow: none;
}
.forgot-link {
    font-size: 13px;
    color: #667eea;
}
.forgot-link:hover {
    color: #764ba2;
    text-decoration: underline;
}
@media (max-width: 500px) {
    .loginDiv {
        width: 90%;
        padding: 25px 15px;
    }
    .login-header {
        margin-bottom: 25px;
    }
    .login-title {
        font-size: 20px;
    }
    .form-btn-group {
        flex-direction: column;
        align-items: flex-start;
    }
    .login-btn {
        width: 100%;
        margin-bottom: 15px;
    }
    .forgot-link {
        align-self: flex-end;
    }
    /* 移动端调整图片大小 */
    .bottom-center-image {
        bottom: 15px;
    }
    .lihui-img {
        width: 150px;
    }
    .image-caption {
        font-size: 12px;
        margin-top: 8px;
    }
}
</style>