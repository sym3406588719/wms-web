<template>
  <div class="memory-container">
    <!-- 记忆碎片背景 -->
    <div class="memory-fragments">
      <div class="fragment f1"></div>
      <div class="fragment f2"></div>
      <div class="fragment f3"></div>
      <div class="fragment f4"></div>
      <div class="fragment f5"></div>
    </div>
    
    <div class="memory-header">
      <div class="search-panel">
        <el-input 
          v-model="name" 
          placeholder="请输入名字" 
          suffix-icon="el-icon-search" 
          class="memory-input"
          @keyup.enter.native="loadPost">
        </el-input>
        <el-select v-model="sex" filterable placeholder="请选择性别" class="memory-select">
          <el-option
            v-for="item in sexs"
            :key="item.value"
            :label="item.label"
            :value="item.value">
          </el-option>
        </el-select>
        <el-button class="memory-btn search-btn" @click="loadPost">查询</el-button>
        <el-button class="memory-btn reset-btn" @click="resetParam">重置</el-button>
        <el-button class="memory-btn add-btn" @click="add">新增</el-button>
      </div>
    </div>
    
    <div class="memory-table-section">
      <el-table 
        :data="tableData"
        :header-cell-style="setTableHeaderStyle"
        :cell-style="setTableCellStyle"
        border
        class="memory-table"
        :fit="true"  
        size="mini"  
      >
        <el-table-column prop="id" label="ID" min-width="60"></el-table-column>
        <el-table-column prop="no" label="账号" min-width="120"></el-table-column>
        <el-table-column prop="name" label="姓名" min-width="100"></el-table-column>
        <el-table-column prop="age" label="年龄" min-width="60"></el-table-column>
        <el-table-column prop="sex" label="性别" min-width="80">
          <template slot-scope="scope">
            <el-tag
              class="gender-tag"
              :class="scope.row.sex === 1 ? 'male' : 'female'"
              disable-transitions
              size="mini"
            >
              {{ scope.row.sex === 1 ? '男' : '女' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="phone" label="电话" min-width="110"></el-table-column>
        <el-table-column prop="roleId" label="角色" min-width="120">
          <template slot-scope="scope">
            <el-tag
              class="role-tag"
              :class="{
                'super-admin': scope.row.roleId === 1,
                'admin': scope.row.roleId === 0,
                'user': scope.row.roleId !== 1 && scope.row.roleId !== 0
              }"
              disable-transitions
              size="mini"
            >
              {{ 
                scope.row.roleId === 1 ? '超级管理员' : 
                scope.row.roleId === 0 ? '管理员' : '用户' 
              }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="operate" label="操作" min-width="120">
          <template slot-scope="scope">
            <el-button size="mini" class="memory-operate-btn edit-btn" @click="mod(scope.row)">编辑</el-button>
            <el-popconfirm title="确定要删除这段记忆吗？" @confirm="del(scope.row.id)" popper-class="memory-popconfirm">
              <el-button slot="reference" size="mini" class="memory-operate-btn delete-btn">删除</el-button>
            </el-popconfirm>
          </template>
        </el-table-column>
      </el-table>
      
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="pageNum"
        :page-sizes="[2, 5, 10, 20]"
        :page-size="pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
        class="memory-pagination">
      </el-pagination>
      
      <el-dialog
        title="用户信息"
        :visible.sync="centerDialogVisible"
        width="35%"
        center
        class="memory-dialog">
        <div class="dialog-memory-bg"></div>
        <el-form ref="form" :rules="rules" :model="form" label-width="80px">
          <el-form-item label="姓名" prop="name" class="memory-form-item">
            <el-input v-model="form.name" class="memory-dialog-input"></el-input>
          </el-form-item>
          <el-form-item label="账号" prop="no" class="memory-form-item">
            <el-input v-model="form.no" class="memory-dialog-input"></el-input>
          </el-form-item>
          <el-form-item label="年龄" prop="age" class="memory-form-item">
            <el-input v-model="form.age" class="memory-dialog-input"></el-input>
          </el-form-item>
          <el-form-item label="性别" class="memory-form-item">
            <el-radio-group v-model="form.sex" class="memory-radio-group">
              <el-radio label="1">男</el-radio>
              <el-radio label="0">女</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="密码" prop="password" class="memory-form-item">
            <el-input v-model="form.password" type="password" class="memory-dialog-input"></el-input>
          </el-form-item>
          <el-form-item label="电话" prop="phone" class="memory-form-item">
            <el-input v-model="form.phone" class="memory-dialog-input"></el-input>
          </el-form-item>
        </el-form>
        <span slot="footer" class="dialog-footer">
          <el-button class="memory-cancel-btn" @click="centerDialogVisible = false">取 消</el-button>
          <el-button class="memory-confirm-btn" @click="save">确 定</el-button>
        </span>
      </el-dialog>
    </div>
  </div>
</template>

<script>
export default {
  name: "UserManage",
  data() {
    const CheckAge = (_, value, callback) => {
      if (!value) {
        return callback();
      }
      const ageNum = Number(value);
      if (ageNum > 150) {
        callback(new Error("年龄输入过大"));
      } else {
        callback();
      }
    };

    return {
      tableData: [],
      pageSize: 2,
      pageNum: 1,
      total: 0,
      name: "",
      sex: "",
      sexs: [
        { value: "1", label: "男" },
        { value: "0", label: "女" }
      ],
      centerDialogVisible: false,
      form: {
        id: undefined,
        name: "",
        no: "",
        phone: "",
        password: "",
        age: "",
        sex: "0",
        roleId: "2"
      },
      rules: {
        no: [
          { required: true, message: "请输入账号", trigger: "blur" },
          { min: 3, max: 8, message: "长度在 3 到 8 个字符", trigger: "blur" },
          { validator: (_, value, callback) => this.CheckDuplicate(_, value, callback), trigger: "blur" }
        ],
        name: [
          { required: true, message: "请输入名字", trigger: "blur" }
        ],
        password: [
          { required: true, message: "请输入密码", trigger: "blur" },
          { min: 3, max: 8, message: "长度在 3 到 8 个字符", trigger: "blur" }
        ],
        age: [
          { required: true, message: "请输入年龄", trigger: "blur" },
          { min: 1, max: 3, message: "长度在1到3位", trigger: "blur" },
          { pattern: /^([1-9][0-9]*){1,3}$/, message: "年龄必须为正整数字", trigger: "blur" },
          { validator: CheckAge, trigger: "blur" }
        ],
        phone: [
          { required: true, message: "手机号不能为空", trigger: "blur" },
          { pattern: /^1[3-9]\d{9}$/, message: "请输入正确的手机号码", trigger: "blur" }
        ]
      }
    };
  },
  methods: {
    resetForm() {
      this.$refs.form?.resetFields();
    },

    del(id) {
      this.$axios.get(`${this.$httpUrl}/user/del?id=`+id)
        .then(res => res.data)
        .then(res => {
          if (res.code === 200) {
            this.$message({ message: "记忆已删除!", type: "success" });
            this.loadPost();
          } else {
            this.$message({ message: res.msg || "操作失败", type: "error" });
          }
        })
        .catch(err => {
          console.error("删除失败：", err);
          this.$message.error("删除失败，请稍后重试");
        });
    },

    mod(row) {
      if (!row) return;
      this.form = {
        id: row.id,
        no: row.no || "",
        password: "",
        age: row.age ? String(row.age) : "",
        phone: row.phone || "",
        sex: row.sex ? String(row.sex) : "0",
        name: row.name || "",
        roleId: row.roleId || "1"
      };
      this.centerDialogVisible = true;
    },

    add() {
      this.resetForm();
      this.$nextTick(() => {
        this.centerDialogVisible = true;
        this.form.id = undefined;
      });
    },

    CheckDuplicate(_, value, callback) {
      if (this.form?.id) {
        return callback();
      }
      if (!value) {
        return callback();
      }
      this.$axios.get(`${this.$httpUrl}/user/findByNo`, {
        params: { no: value }
      })
        .then(res => res.data)
        .then(res => {
          if (res.code != 200) {
            callback();
          } else if (res.code === 400) {
            callback(new Error("账号已存在"));
          } else {
            callback(new Error(res.msg || "验证失败，请重试"));
          }
        })
        .catch(err => {
          console.error("账号查重请求失败：", err);
          callback(new Error("账号验证失败，请稍后重试"));
        });
    },

    doSave() {
      this.$axios.post(`${this.$httpUrl}/user/save`, this.form)
        .then(res => res.data)
        .then(res => {
          if (res.code === 200) {
            this.$message({ message: "记忆已保存!", type: "success" });
            this.centerDialogVisible = false;
            this.loadPost();
          } else {
            this.$message({ message: res.msg || "操作失败", type: "error" });
          }
        })
        .catch(err => {
          console.error("保存失败：", err);
          this.$message.error("保存失败，请稍后重试");
        });
    },

    doMod() {
      this.$axios.post(`${this.$httpUrl}/user/update`, this.form)
        .then(res => res.data)
        .then(res => {
          if (res.code === 200) {
            this.$message({ message: "记忆已更新!", type: "success" });
            this.centerDialogVisible = false;
            this.loadPost();
          } else {
            this.$message({ message: res.msg || "操作失败", type: "error" });
          }
        })
        .catch(err => {
          console.error("更新失败：", err);
          this.$message.error("更新失败，请稍后重试");
        });
    },

    save() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          this.form.id ? this.doMod() : this.doSave();
        } else {
          this.$message.warning("请检查输入内容");
          return false;
        }
      });
    },

    handleSizeChange(val) {
      this.pageSize = val;
      this.loadPost();
    },

    handleCurrentChange(val) {
      this.pageNum = val;
      this.loadPost();
    },

    setTableHeaderStyle() {
      return {
        background: 'linear-gradient(135deg, rgba(168, 139, 217, 0.9) 0%, rgba(139, 117, 217, 0.9) 100%)',
        color: '#f8f4ff',
        border: '1px solid rgba(168, 139, 217, 0.5)',
        fontWeight: 'bold',
        textAlign: 'center',
        textShadow: '0 0 5px rgba(168, 139, 217, 0.5)'
      };
    },

    setTableCellStyle() {
      return {
        border: '1px solid rgba(168, 139, 217, 0.3)',
        textAlign: 'center',
        whiteSpace: 'nowrap',
        overflow: 'hidden',
        textOverflow: 'ellipsis',
        background: 'rgba(248, 244, 255, 0.7)',
        color: '#5d4b8c'
      };
    },

    loadGet() {
      this.$axios.get(`${this.$httpUrl}/user/list`)
        .then(res => res.data)
        .then(res => {
          this.tableData = res.data || res || [];
        })
        .catch(err => console.error("GET 请求失败：", err));
    },

    resetParam() {
      this.name = "";
      this.sex = "";
    },

    loadPost() {
      this.$axios.post(`${this.$httpUrl}/user/listPageC1`, {
        pageSize: this.pageSize,
        pageNum: this.pageNum,
        param: { name: this.name, sex: this.sex, roleId: '2' }
      })
        .then(res => res.data)
        .then(res => {
          if (res.code === 200) {
            this.tableData = res.data || [];
            this.total = res.total || 0;
            if (this.total === 0) {
              this.$message.info("未找到相关记忆");
            }
          } else {
            this.$message.error(res.msg || "获取数据失败");
            this.tableData = [];
            this.total = 0;
          }
        })
        .catch(err => {
          console.error("POST 请求失败：", err);
          this.$message.error("数据加载失败，请稍后重试");
          this.tableData = [];
          this.total = 0;
        });
    }
  },
  mounted() {
    this.loadPost();
  }
};
</script>

<style scoped>
.memory-container {
  padding: 20px;
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
  position: relative;
  overflow-x: hidden;
  background: linear-gradient(135deg, #f5f0ff 0%, #e6dcfd 50%, #dcd0ff 100%);
}

/* 记忆碎片背景 */
.memory-fragments {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  pointer-events: none;
  z-index: 0;
}

.fragment {
  position: absolute;
  background: rgba(255, 255, 255, 0.1);
  border: 1px solid rgba(168, 139, 217, 0.3);
  backdrop-filter: blur(5px);
  animation: float 6s ease-in-out infinite;
}

.f1 { top: 10%; left: 5%; width: 40px; height: 40px; transform: rotate(45deg); animation-delay: 0s; }
.f2 { top: 20%; right: 10%; width: 60px; height: 60px; transform: rotate(15deg); animation-delay: 1s; }
.f3 { bottom: 30%; left: 15%; width: 50px; height: 50px; transform: rotate(75deg); animation-delay: 2s; }
.f4 { bottom: 20%; right: 20%; width: 45px; height: 45px; transform: rotate(30deg); animation-delay: 3s; }
.f5 { top: 40%; left: 50%; width: 55px; height: 55px; transform: rotate(60deg); animation-delay: 4s; }

@keyframes float {
  0%, 100% { transform: translateY(0) rotate(var(--rotation, 0deg)); }
  50% { transform: translateY(-20px) rotate(var(--rotation, 0deg)); }
}

.memory-header {
  background: linear-gradient(135deg, 
    rgba(168, 139, 217, 0.8) 0%, 
    rgba(139, 117, 217, 0.7) 50%, 
    rgba(120, 100, 200, 0.6) 100%);
  border-radius: 20px;
  padding: 25px;
  margin-bottom: 25px;
  box-shadow: 
    0 8px 32px rgba(168, 139, 217, 0.3),
    inset 0 1px 0 rgba(255, 255, 255, 0.2);
  border: 1px solid rgba(168, 139, 217, 0.3);
  backdrop-filter: blur(10px);
  position: relative;
  z-index: 1;
}

.search-panel {
  display: flex;
  align-items: center;
  flex-wrap: wrap;
  gap: 12px;
}

.memory-input, .memory-select {
  width: 200px;
}

.memory-input >>> .el-input__inner,
.memory-select >>> .el-input__inner {
  border-radius: 25px;
  border: 2px solid rgba(168, 139, 217, 0.5);
  background: rgba(255, 255, 255, 0.9);
  color: #5d4b8c;
  transition: all 0.4s ease;
  backdrop-filter: blur(5px);
}

.memory-input >>> .el-input__inner:focus,
.memory-select >>> .el-input__inner:focus {
  border-color: #a88bd9;
  box-shadow: 
    0 0 20px rgba(168, 139, 217, 0.4),
    inset 0 0 10px rgba(168, 139, 217, 0.2);
}

.memory-btn {
  border-radius: 25px;
  border: none;
  font-weight: bold;
  transition: all 0.4s ease;
  padding: 12px 24px;
  color: white;
  text-shadow: 0 0 5px rgba(255, 255, 255, 0.5);
  position: relative;
  overflow: hidden;
}

.memory-btn::before {
  content: '';
  position: absolute;
  top: -2px;
  left: -2px;
  right: -2px;
  bottom: -2px;
  background: inherit;
  border-radius: inherit;
  z-index: -1;
  filter: blur(10px);
  opacity: 0;
  transition: opacity 0.4s ease;
}

.memory-btn:hover::before {
  opacity: 0.6;
}

.search-btn {
  background: linear-gradient(135deg, #ff9a9e 0%, #fad0c4 100%);
  box-shadow: 0 4px 15px rgba(255, 154, 158, 0.4);
}

.search-btn:hover {
  transform: translateY(-3px) scale(1.05);
  box-shadow: 0 8px 25px rgba(255, 154, 158, 0.6);
}

.reset-btn {
  background: linear-gradient(135deg, #a18cd1 0%, #fbc2eb 100%);
  box-shadow: 0 4px 15px rgba(161, 140, 209, 0.4);
}

.reset-btn:hover {
  transform: translateY(-3px) scale(1.05);
  box-shadow: 0 8px 25px rgba(161, 140, 209, 0.6);
}

.add-btn {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  box-shadow: 0 4px 15px rgba(102, 126, 234, 0.4);
}

.add-btn:hover {
  transform: translateY(-3px) scale(1.05);
  box-shadow: 0 8px 25px rgba(102, 126, 234, 0.6);
}

.memory-table-section {
  background: rgba(255, 255, 255, 0.8);
  border-radius: 20px;
  padding: 25px;
  margin-bottom: 25px;
  box-shadow: 
    0 8px 32px rgba(168, 139, 217, 0.2),
    inset 0 1px 0 rgba(255, 255, 255, 0.3);
  border: 1px solid rgba(168, 139, 217, 0.2);
  backdrop-filter: blur(10px);
  position: relative;
  z-index: 1;
}

.memory-table {
  border-radius: 15px;
  overflow: hidden;
}

.memory-table >>> .el-table__header-wrapper {
  border-radius: 15px 15px 0 0;
}

.gender-tag {
  border: none !important;
  font-weight: bold;
  border-radius: 15px !important;
  padding: 4px 12px !important;
}

.gender-tag.male {
  background: linear-gradient(135deg, #89f7fe 0%, #66a6ff 100%) !important;
  color: white !important;
}

.gender-tag.female {
  background: linear-gradient(135deg, #ff9a9e 0%, #fecfef 100%) !important;
  color: white !important;
}

.role-tag {
  border: none !important;
  font-weight: bold;
  border-radius: 15px !important;
  padding: 4px 12px !important;
}

.super-admin {
  background: linear-gradient(135deg, #ff6b6b 0%, #ee5a52 100%) !important;
  color: white !important;
}

.admin {
  background: linear-gradient(135deg, #4ecdc4 0%, #44a08d 100%) !important;
  color: white !important;
}

.user {
  background: linear-gradient(135deg, #a18cd1 0%, #fbc2eb 100%) !important;
  color: white !important;
}

.memory-operate-btn {
  border-radius: 15px;
  border: none;
  font-weight: bold;
  transition: all 0.3s ease;
  color: white;
  padding: 6px 12px;
  margin: 2px;
}

.edit-btn {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  box-shadow: 0 3px 10px rgba(102, 126, 234, 0.4);
}

.edit-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 5px 15px rgba(102, 126, 234, 0.6);
}

.delete-btn {
  background: linear-gradient(135deg, #ff9a9e 0%, #fad0c4 100%);
  box-shadow: 0 3px 10px rgba(255, 154, 158, 0.4);
}

.delete-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 5px 15px rgba(255, 154, 158, 0.6);
}

.memory-pagination {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}

.memory-pagination >>> .el-pagination {
  padding: 15px;
  background: rgba(255, 255, 255, 0.8);
  border-radius: 25px;
  backdrop-filter: blur(5px);
}

.memory-pagination >>> .el-pager li {
  border-radius: 50%;
  margin: 0 5px;
  background: rgba(168, 139, 217, 0.3);
  border: 1px solid rgba(168, 139, 217, 0.5);
  color: #5d4b8c;
  transition: all 0.3s ease;
}

.memory-pagination >>> .el-pager li.active {
  background: linear-gradient(135deg, #a88bd9 0%, #8b7fd9 100%);
  color: white;
  box-shadow: 0 0 10px rgba(168, 139, 217, 0.6);
}

.memory-pagination >>> .el-pager li:hover {
  transform: scale(1.1);
  background: rgba(168, 139, 217, 0.5);
}

.memory-dialog >>> .el-dialog {
  border-radius: 20px;
  background: linear-gradient(135deg, 
    rgba(255, 255, 255, 0.95) 0%, 
    rgba(248, 244, 255, 0.9) 100%);
  border: 2px solid rgba(168, 139, 217, 0.3);
  box-shadow: 
    0 20px 40px rgba(168, 139, 217, 0.3),
    inset 0 1px 0 rgba(255, 255, 255, 0.5);
  backdrop-filter: blur(10px);
}

.memory-dialog >>> .el-dialog__header {
  background: linear-gradient(135deg, 
    rgba(168, 139, 217, 0.9) 0%, 
    rgba(139, 117, 217, 0.9) 100%);
  border-radius: 20px 20px 0 0;
  padding: 20px;
  border-bottom: 1px solid rgba(168, 139, 217, 0.3);
}

.memory-dialog >>> .el-dialog__title {
  color: #f8f4ff;
  font-weight: bold;
  font-size: 18px;
  text-shadow: 0 0 10px rgba(168, 139, 217, 0.8);
}

.dialog-memory-bg {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: 
    radial-gradient(circle at 30% 30%, rgba(168, 139, 217, 0.1) 0%, transparent 50%),
    radial-gradient(circle at 70% 70%, rgba(139, 117, 217, 0.1) 0%, transparent 50%);
  pointer-events: none;
  border-radius: 20px;
}

.memory-form-item >>> .el-form-item__label {
  color: #5d4b8c !important;
  font-weight: bold;
  text-shadow: 0 0 5px rgba(168, 139, 217, 0.3);
}

.memory-dialog-input >>> .el-input__inner {
  border-radius: 15px;
  border: 2px solid rgba(168, 139, 217, 0.3);
  background: rgba(255, 255, 255, 0.9);
  color: #5d4b8c;
  transition: all 0.3s ease;
}

.memory-dialog-input >>> .el-input__inner:focus {
  border-color: #a88bd9;
  box-shadow: 0 0 15px rgba(168, 139, 217, 0.3);
}

.memory-radio-group >>> .el-radio__input.is-checked .el-radio__inner {
  background-color: #a88bd9;
  border-color: #a88bd9;
}

.memory-radio-group >>> .el-radio__input.is-checked + .el-radio__label {
  color: #a88bd9;
}

.memory-radio-group >>> .el-radio__input:hover .el-radio__inner {
  border-color: #a88bd9;
}

.memory-cancel-btn {
  border-radius: 20px;
  border: 2px solid #a88bd9;
  color: #a88bd9;
  background: transparent;
  transition: all 0.3s ease;
  font-weight: bold;
  padding: 10px 20px;
}

.memory-cancel-btn:hover {
  background: #a88bd9;
  color: white;
  transform: translateY(-2px);
  box-shadow: 0 5px 15px rgba(168, 139, 217, 0.4);
}

.memory-confirm-btn {
  border-radius: 20px;
  border: none;
  background: linear-gradient(135deg, #a88bd9 0%, #8b7fd9 100%);
  transition: all 0.3s ease;
  font-weight: bold;
  padding: 10px 20px;
  color: white;
  box-shadow: 0 4px 15px rgba(168, 139, 217, 0.4);
}

.memory-confirm-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(168, 139, 217, 0.6);
}

/* 弹窗确认框样式 */
::v-deep .memory-popconfirm .el-popconfirm__title {
  color: #5d4b8c;
  font-weight: bold;
}

::v-deep .memory-popconfirm .el-button--primary {
  background: linear-gradient(135deg, #a88bd9 0%, #8b7fd9 100%);
  border: none;
  border-radius: 15px;
  color: white;
}

::v-deep .memory-popconfirm .el-button--primary:hover {
  background: linear-gradient(135deg, #8b7fd9 0%, #7a6fcc 100%);
}

::v-deep .memory-popconfirm .el-button--default {
  background: rgba(168, 139, 217, 0.1);
  border: 1px solid #a88bd9;
  border-radius: 15px;
  color: #5d4b8c;
}

::v-deep .memory-popconfirm .el-button--default:hover {
  background: rgba(168, 139, 217, 0.2);
}
</style>