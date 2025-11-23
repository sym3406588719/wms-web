<template>
  <div>
  <div style="margin-bottom:10px;">
    <el-input v-model="name" placeholder="请输入名字" suffix-icon="el-icon-search" style="width:300px;"
    @keyup.enter.native="loadPost"></el-input>
    <el-select v-model="sex" filterable placeholder="请选择性别" style="margin-left:5px;">
    <el-option
      v-for="item in sexs"
      :key="item.value"
      :label="item.label"
      :value="item.value">
    </el-option>
  </el-select>
<el-button style="background-color:#996cd4; color:#d7bcff; margin-left:5px; " @click="loadPost">查询</el-button>
<el-button style="background-color:#f4cfe9; color:#f6f3ec; "
           hover-style="background-color: #e9ecef;" @click="resetParam">重置</el-button>
<el-button style="background-color:#3e439f; color:#a5a7fd;" @click="add">新增</el-button>
  </div>
    <div class="table-container">
      <el-table 
        :data="tableData"
        :header-cell-style="setTableHeaderStyle"
        :cell-style="setTableCellStyle"
        border
        class="custom-user-table"
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
              :color="scope.row.sex === 1 ? '#fadef4' : '#ad62bc'"  
              :style="scope.row.sex === 1 ? { color: '#ad62bc' } : { color: '#d1bdf8' }"
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
              :class="['role-tag', {
                'super-admin': scope.row.roleId === 1,
                'admin': scope.row.roleId === 0,
                'user': scope.row.roleId !== 1 && scope.row.roleId !== 0
              }]"
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
        <el-table-column prop="operate" label="操作" min-width="100">
          <template slot-scope="scope">
            <el-button size="mini"  style="margin-right: 6px;background-color:#f2d6fc; border-color:#f2d6fc; color:white;" @click="mod(scope.row)">编辑</el-button>
            <el-popconfirm title="确定要删除吗？" @confirm="del(scope.row.id)"  popper-class="custom-popconfirm">
            <el-button slot="reference" size="mini" style="background-color:#aac3f6; border-color:#aac3f6; color:white;">删除</el-button>
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
        :total="total">
      </el-pagination>
      <el-dialog
  title="提示"
  :visible.sync="centerDialogVisible"
  width="30%"
  center>
  <el-form ref="form" :rules="rules"   :model="form" label-width="80px">
  <el-form-item label="姓名" prop="name">
    <el-input v-model="form.name"></el-input>
  </el-form-item>
  <el-form-item label="账号" prop="no">
    <el-input v-model="form.no"></el-input>
  </el-form-item>
  <el-form-item label="年龄" prop="age">
    <el-input v-model="form.age"></el-input>
  </el-form-item>
  <el-form-item label="性别">
    <el-radio-group v-model="form.sex">
      <el-radio label="1">男</el-radio>
      <el-radio label="0">女</el-radio>
    </el-radio-group>
  </el-form-item>
  <el-form-item label="密码" prop="password">
    <el-input v-model="form.password"></el-input>
  </el-form-item>
  <el-form-item label="电话" prop="phone">
    <el-input v-model="form.phone"></el-input>
  </el-form-item>
  </el-form>
  <span slot="footer" class="dialog-footer">
    <el-button  style="background-color:#f6f2f3; border-color:#f6f2f3; color:#e8dae3;" @click="centerDialogVisible = false">取 消</el-button>
    <el-button style="background-color:#edcae6; border-color:#edcae6; color:#f4e2f0;" @click="save">确 定</el-button>
  </span>
</el-dialog>
    </div>
  </div>
</template>

```vue
<script>
export default {
  name: "AdminManage",
  data() {
    // 年龄验证：补充空值判断，避免非数字类型导致的逻辑偏差
    const CheckAge = (_, value, callback) => {
      // 空值处理：未输入时不触发“过大”错误，交由required规则处理
      if (!value) {
        return callback();
      }
      // 转为数字验证：防止字符串格式数字判断异常
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
        id: undefined, // 显式定义编辑标识，避免隐式类型问题
        name: "",
        no: "",
        phone: "",
        password: "",
        age: "",
        sex: "0",
        roleId: "1"
      },
      rules: {
        no: [
          { required: true, message: "请输入账号", trigger: "blur" },
          { min: 3, max: 8, message: "长度在 3 到 8 个字符", trigger: "blur" },
          // 修复this指向：通过箭头函数绑定组件实例this
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
          // 简化正则：去除冗余分隔符，匹配更严谨
          { pattern: /^1[3-9]\d{9}$/, message: "请输入正确的手机号码", trigger: "blur" }
        ]
      }
    };
  },
  methods: {
    // 重置表单：添加可选链，避免$refs.form未定义时报错
    resetForm() {
      this.$refs.form?.resetFields();
    },

    // 删除方法：保留空实现，避免调用时报错
    del(id) {
       this.$axios.get(`${this.$httpUrl}/user/del?id=`+id)
        .then(res => res.data)
        .then(res => {
          console.log("POST 请求数据：", res);
          if (res.code === 200) {
            this.$message({ message: "操作成功!", type: "success" });
            this.loadPost();
          } else {
            // 显示后端返回的错误信息（若有）
            this.$message({ message: res.msg || "操作失败", type: "error" });
          }
        })
        .catch(err => {
          console.error("保存失败：", err);
          this.$message.error("保存失败，请稍后重试");
        });
    },

    // 编辑用户：补充空值兜底，避免后端返回null导致的回显异常
    mod(row) {
      // 边界判断：防止未选中行时调用
      if (!row) return;

      this.form = {
        id: row.id,
        no: row.no || "", // 空值兜底
        password: "", // 编辑时清空密码，符合安全逻辑
        age: row.age ? String(row.age) : "", // 转为字符串，匹配输入框类型
        phone: row.phone || "",
        sex: row.sex ? String(row.sex) : "0", // 转为字符串，匹配下拉框value类型
        name: row.name || "",
        roleId: row.roleId || "1"
      };

      this.centerDialogVisible = true;
    },

    // 新增用户：调整重置顺序，先重置再打开弹窗（避免初始值残留）
    add() {
      this.resetForm();
      this.$nextTick(() => {
        this.centerDialogVisible = true;
        // 重置编辑标识，确保新增时无id
        this.form.id = undefined;
      });
    },

CheckDuplicate(_, value, callback) {
  // 编辑状态：有id则跳过重复验证（保持原逻辑）
  if (this.form?.id) {
    return callback();
  }

  // 空值处理：未输入账号时不发起请求（保持原逻辑）
  if (!value) {
    return callback();
  }

  //使用axios的params配置，自动处理特殊字符编码
  this.$axios.get(`${this.$httpUrl}/user/findByNo`, {
    params: {
      no: value  // 参数名与后端保持一致
    }
  })
    .then(res => res.data)
    .then(res => {
      if (res.code!=200) {
        callback();  // 验证通过，无错误
      } else if (res.code === 400) {  // 假设400表示账号已存在
        callback(new Error("账号已存在"));
      } else {
        // 其他状态码：统一返回后端消息或默认提示
        callback(new Error(res.msg || "验证失败，请重试"));
      }
    })
    .catch(err => {
      console.error("账号查重请求失败：", err);
      callback(new Error("账号验证失败，请稍后重试"));
    });
},

    // 新增提交：优化接口地址拼接，补充错误信息反馈
    doSave() {
      this.$axios.post(`${this.$httpUrl}/user/save`, this.form)
        .then(res => res.data)
        .then(res => {
          console.log("POST 请求数据：", res);
          if (res.code === 200) {
            this.$message({ message: "操作成功!", type: "success" });
            this.centerDialogVisible = false;
            this.loadPost();
          } else {
            // 显示后端返回的错误信息（若有）
            this.$message({ message: res.msg || "操作失败", type: "error" });
          }
        })
        .catch(err => {
          console.error("保存失败：", err);
          this.$message.error("保存失败，请稍后重试");
        });
    },

    // 编辑提交：同新增，优化地址拼接和错误反馈
    doMod() {
      this.$axios.post(`${this.$httpUrl}/user/update`, this.form)
        .then(res => res.data)
        .then(res => {
          console.log("POST 请求数据：", res);
          if (res.code === 200) {
            this.$message({ message: "操作成功!", type: "success" });
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

    // 表单提交：补充验证失败的用户提示
    save() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          this.form.id ? this.doMod() : this.doSave();
        } else {
          console.log("error submit!!");
          // 新增用户提示，明确失败原因
          this.$message.warning("表单验证失败，请检查输入内容");
          return false;
        }
      });
    },

    // 每页条数改变
    handleSizeChange(val) {
      this.pageSize = val;
      this.loadPost();
    },

    // 当前页改变
    handleCurrentChange(val) {
      this.pageNum = val;
      this.loadPost();
    },

    // 表头样式
    setTableHeaderStyle() {
      return {
        background: "#bbcbfe",
        color: "#fff7e1",
        border: "1px solid #99b4ff",
        borderRight: "1px solid #99b4ff",
        fontWeight: "bold",
        textAlign: "center"
      };
    },

    // 单元格样式
    setTableCellStyle() {
      return {
        border: "1px solid #99b4ff",
        textAlign: "center",
        whiteSpace: "nowrap",
        overflow: "hidden",
        textOverflow: "ellipsis"
      };
    },

    // 全量加载：补充空值兜底，避免表格显示异常
    loadGet() {
      this.$axios.get(`${this.$httpUrl}/user/list`)
        .then(res => res.data)
        .then(res => {
          console.log("GET 请求数据：", res);
          this.tableData = res.data || res || [];
        })
        .catch(err => console.error("GET 请求失败：", err));
    },

    // 重置查询参数：保持原逻辑
    resetParam() {
      this.name = "";
      this.sex = "";
    },

    // 分页加载：优化参数拼接，补充空数据提示
    loadPost() {
      this.$axios.post(`${this.$httpUrl}/user/listPageC1`, {
        pageSize: this.pageSize,
        pageNum: this.pageNum,
        param: { name: this.name, sex: this.sex ,roleId:'1'}
      })
        .then(res => res.data)
        .then(res => {
          console.log("POST 请求数据：", res);
          if (res.code === 200) {
            this.tableData = res.data || [];
            this.total = res.total || 0;
            // 新增空数据提示，提升用户体验
            if (this.total === 0) {
              this.$message.info("未查询到符合条件的数据");
            }
          } else {
            this.$message.error(res.msg || "获取数据失败");
            // 清空表格，避免旧数据残留
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
.table-container {
  width: 100%;        
  max-width: 100vw;    
  overflow-x: auto;    
  overflow-y: hidden;  
  padding: 0 10px;     
  box-sizing: border-box;
}

::v-deep .custom-user-table {
  width: 100%;         
  min-width: 800px;    
  margin: 10px 0;
  border-collapse: separate;
  border-spacing: 0;
}

::v-deep .custom-user-table .el-table__header-wrapper {
  border-bottom: 1px solid #99b4ff !important;
}

::v-deep .role-tag {
  font-weight: 500;
  padding: 2px 8px;
}
::v-deep .super-admin {
  background-color: #ad62bc !important;
  color: #fadef4 !important;
  border-color: #ad62bc !important;
}
::v-deep .admin {
  background-color: #d1bdf8 !important;
  color: #ad62bc !important;
  border-color: #d1bdf8 !important;
}
::v-deep .user {
  background-color: #d995c6 !important;
  color: #f6e4ea !important;
  border-color: #d995c6 !important;
}

::v-deep .custom-user-table .el-table__row:hover > td {
  border-color: #99b4ff !important;
}

@media screen and (max-width: 576px) {
  ::v-deep .custom-user-table th,
  ::v-deep .custom-user-table td {
    font-size: 12px !important;
  }
  ::v-deep .role-tag {
    padding: 1px 4px;
  }
}
::v-deep .el-radio__input.is-checked .el-radio__inner {
  /* 选中时的圆圈背景色 */
  background-color: #b0bafb;
  /* 选中时的圆圈边框色 */
  border-color: #b0bafb;
}

/* 选中时的文字颜色 */
::v-deep .el-radio__input.is-checked + .el-radio__label {
  color: #b0bafb;
}

/* 鼠标悬停时的边框颜色 */
::v-deep .el-radio__input:hover .el-radio__inner {
  border-color: #b0bafb;
}
::v-deep .el-message--custom {
  background-color: #c8b4e8; 
  color: #dadeff; 
}
/* 修改确认弹窗的标题颜色 */
::v-deep .custom-popconfirm .el-popconfirm__title {
  color: #E6A23C; 
}

/* 修改确认按钮的颜色 */
::v-deep .custom-popconfirm .el-button--primary {
  background-color:#8c35c6; 
  border-color:#8c35c6;
  color:#e75bbb;     
}

/* 修改确认按钮 hover 状态的颜色 */
::v-deep .custom-popconfirm .el-button--primary:hover {
  background-color: #F78989; 
  border-color: #F78989;     
}

/* 修改取消按钮的颜色 */
::v-deep .custom-popconfirm .el-button--default {
  background-color:#f7f9f8; 
  border-color:#f7f9f8;     
  color:#93c8f2;               
}

/* 修改取消按钮 hover 状态的颜色 */
::v-deep .custom-popconfirm .el-button--default:hover {
  background-color: #85CE61; 
  border-color: #85CE61;     
}

</style>
