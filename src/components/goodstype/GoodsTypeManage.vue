<template>
  <div class="page-container">
    <!-- 搜索区域：添加科幻风卡片 -->
    <div class="search-card">
      <el-input 
        v-model="name" 
        placeholder="请输入分类名" 
        suffix-icon="el-icon-search" 
        class="search-input"
        @keyup.enter.native="loadPost"
      ></el-input>
      <el-button class="btn-search" @click="loadPost">查询</el-button>
      <el-button class="btn-reset" @click="resetParam">重置</el-button>
      <el-button class="btn-add" @click="add">新增</el-button>
    </div>

    <!-- 表格区域：添加科幻风容器 -->
    <div class="table-card">
      <el-table 
        :data="tableData"
        :header-cell-style="setTableHeaderStyle"
        :cell-style="setTableCellStyle"
        border
        class="custom-user-table"
        :fit="true"  
        size="mini"  
        highlight-current-row
      >
        <el-table-column prop="id" label="ID" min-width="60"></el-table-column>
        <el-table-column prop="name" label="分类名" min-width="100"></el-table-column>
        <el-table-column prop="remark" label="备注" min-width="120"></el-table-column>
        <el-table-column prop="operate" label="操作" min-width="140">
          <template slot-scope="scope">
            <el-button size="mini" class="btn-edit" @click="mod(scope.row)">编辑</el-button>
            <el-popconfirm 
              title="确定要删除吗？" 
              @confirm="del(scope.row.id)"  
              popper-class="custom-popconfirm"
            >
              <el-button slot="reference" size="mini" class="btn-delete">删除</el-button>
            </el-popconfirm>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页器：添加科幻风样式 -->
      <div class="pagination-container">
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="pageNum"
          :page-sizes="[2, 5, 10, 20]"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
          class="custom-pagination"
        ></el-pagination>
      </div>
    </div>

    <!-- 弹窗：科幻风玻璃拟态 -->
    <el-dialog
      title="商品分类编辑"
      :visible.sync="centerDialogVisible"
      width="35%"
      center
      class="custom-dialog"
    >
      <el-form 
        ref="form" 
        :rules="rules"   
        :model="form" 
        label-width="80px"
        class="dialog-form"
      >
        <el-form-item label="分类名" prop="name">
          <el-input v-model="form.name" class="dialog-input"></el-input>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" class="dialog-input"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button class="btn-dialog-cancel" @click="centerDialogVisible = false">取 消</el-button>
        <el-button class="btn-dialog-confirm" @click="save">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "GoodsTypeManage",
  data() {
    return {
      tableData: [],
      pageSize: 2,
      pageNum: 1,
      total: 0,
      name: "",
      centerDialogVisible: false,
      form: {
        id: undefined,
        name: "",
        remark: ""
      },
      rules: {
        name: [
          { required: true, message: "请输入分类名", trigger: "blur" } // 修正提示文本，与业务匹配
        ]
      }
    };
  },
  methods: {
    resetForm() {
      this.$refs.form?.resetFields();
    },

    del(id) {
      this.$axios.get(`${this.$httpUrl}/goodstype/del?id=`+id)
        .then(res => res.data)
        .then(res => {
          console.log("POST 请求数据：", res);
          if (res.code === 200) {
            this.$message({ message: "操作成功!", type: "success" });
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

    mod(row) {
      if (!row) return;
      this.form = {
        id: row.id,
        remark: row.remark ? String(row.remark) : "",
        name: row.name || ""
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
      this.$axios.get(`${this.$httpUrl}/goodstype/findByNo`, {
        params: { no: value }
      })
        .then(res => res.data)
        .then(res => {
          if (res.code!==200) {
            callback();
          } else if (res.code === 400) {
            callback(new Error("分类名已存在")); // 修正提示文本，与业务匹配
          } else {
            callback(new Error(res.msg || "验证失败，请重试"));
          }
        })
        .catch(err => {
          console.error("分类名查重请求失败：", err);
          callback(new Error("分类名验证失败，请稍后重试")); // 修正提示文本
        });
    },

    doSave() {
      this.$axios.post(`${this.$httpUrl}/goodstype/save`, this.form)
        .then(res => res.data)
        .then(res => {
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

    doMod() {
      this.$axios.post(`${this.$httpUrl}/goodstype/update`, this.form)
        .then(res => res.data)
        .then(res => {
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

    save() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          this.form.id ? this.doMod() : this.doSave();
        } else {
          console.log("error submit!!");
          this.$message.warning("表单验证失败，请检查输入内容");
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
        background: 'linear-gradient(135deg, #1E293B, #334155)', // 深色科幻风表头
        color: '#E0F2FE', // 浅蓝色文字
        border: '1px solid rgba(168, 85, 247, 0.5)', // 霓虹边框
        fontWeight: 'bold',
        textAlign: 'center',
        boxShadow: 'inset 0 0 8px rgba(99, 102, 241, 0.3)' // 内发光效果
      };
    },

    setTableCellStyle() {
      return {
        background: 'rgba(30, 41, 59, 0.1)', // 半透明深色单元格
        color: '#F8FAFC', // 浅色文字
        border: '1px solid rgba(168, 85, 247, 0.2)', // 淡霓虹边框
        textAlign: 'center',
        whiteSpace: 'nowrap',
        overflow: 'hidden',
        textOverflow: 'ellipsis',
        transition: 'all 0.3s ease',
        boxShadow: 'inset 0 0 3px rgba(99, 102, 241, 0.1)'
      };
    },

    resetParam() {
      this.name = "";
      this.loadPost(); // 重置后自动刷新，提升体验
    },

    loadPost() {
      this.$axios.post(`${this.$httpUrl}/goodstype/listPage`, {
        pageSize: this.pageSize,
        pageNum: this.pageNum,
        param: { name: this.name }
      })
        .then(res => res.data)
        .then(res => {
          if (res.code === 200) {
            this.tableData = res.data || [];
            this.total = res.total || 0;
            if (this.total === 0) {
              this.$message.info("未查询到符合条件的分类"); // 修正提示文本
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
/* 页面容器：科幻风背景 */
.page-container {
  width: 100%;
  padding: 20px;
  box-sizing: border-box;
  background: linear-gradient(180deg, #0F172A, #1E293B);
  /* 科幻网格纹理 */
  background-image: 
    linear-gradient(rgba(99, 102, 241, 0.1) 1px, transparent 1px),
    linear-gradient(90deg, rgba(99, 102, 241, 0.1) 1px, transparent 1px);
  background-size: 20px 20px;
}

/* 搜索卡片：玻璃拟态+霓虹边框 */
.search-card {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 16px 20px;
  margin-bottom: 20px;
  background: rgba(30, 41, 59, 0.5);
  border-radius: 12px;
  box-shadow: 0 0 15px rgba(99, 102, 241, 0.2);
  border: 1px solid rgba(168, 85, 247, 0.3);
  backdrop-filter: blur(8px);
}

/* 搜索输入框：科幻风 */
.search-input {
  width: 320px !important;
  border-radius: 8px !important;
  background: rgba(30, 41, 59, 0.7) !important;
  border: 1px solid rgba(168, 85, 247, 0.4) !important;
  color: #F8FAFC !important;
  transition: all 0.3s ease;
}
.search-input::placeholder {
  color: rgba(248, 250, 252, 0.5) !important;
}
.search-input:focus {
  box-shadow: 0 0 0 2px rgba(168, 85, 247, 0.5), 0 0 15px rgba(99, 102, 241, 0.3) !important;
  border-color: #A855F7 !important;
}
.search-input .el-input__suffix {
  color: #A855F7;
}

/* 按钮样式：科幻渐变+发光效果 */
.btn-search {
  background: linear-gradient(135deg, #6366F1, #8B5CF6) !important;
  color: #FFFFFF !important;
  border: none !important;
  border-radius: 8px !important;
  padding: 8px 16px;
  transition: all 0.3s ease;
  box-shadow: 0 0 10px rgba(99, 102, 241, 0.3);
  position: relative;
  overflow: hidden;
}
.btn-search:hover {
  background: linear-gradient(135deg, #4F46E5, #7C3AED) !important;
  box-shadow: 0 0 20px rgba(99, 102, 241, 0.5);
  transform: translateY(-1px);
}
/* 按钮点击粒子效果 */
.btn-search::after {
  content: '';
  position: absolute;
  top: 50%;
  left: 50%;
  width: 140%;
  padding-bottom: 140%;
  background: rgba(255, 255, 255, 0.15);
  border-radius: 50%;
  transform: translate(-50%, -50%) scale(0);
  transition: transform 0.5s ease;
}
.btn-search:active::after {
  transform: translate(-50%, -50%) scale(1);
  opacity: 0;
}

.btn-reset {
  background: linear-gradient(135deg, #1E293B, #334155) !important;
  color: #94A3B8 !important;
  border: 1px solid rgba(168, 85, 247, 0.3) !important;
  border-radius: 8px !important;
  padding: 8px 16px;
  transition: all 0.3s ease;
}
.btn-reset:hover {
  background: linear-gradient(135deg, #334155, #475569) !important;
  color: #CBD5E1 !important;
  box-shadow: 0 0 15px rgba(168, 85, 247, 0.2);
}

.btn-add {
  background: linear-gradient(135deg, #A855F7, #D946EF) !important;
  color: #FFFFFF !important;
  border: none !important;
  border-radius: 8px !important;
  padding: 8px 16px;
  transition: all 0.3s ease;
  box-shadow: 0 0 10px rgba(168, 85, 247, 0.3);
  position: relative;
  overflow: hidden;
}
.btn-add:hover {
  background: linear-gradient(135deg, #9333EA, #C026D3) !important;
  box-shadow: 0 0 20px rgba(168, 85, 247, 0.5);
  transform: translateY(-1px);
}
.btn-add::after {
  content: '';
  position: absolute;
  top: 50%;
  left: 50%;
  width: 140%;
  padding-bottom: 140%;
  background: rgba(255, 255, 255, 0.15);
  border-radius: 50%;
  transform: translate(-50%, -50%) scale(0);
  transition: transform 0.5s ease;
}
.btn-add:active::after {
  transform: translate(-50%, -50%) scale(1);
  opacity: 0;
}

/* 表格卡片：科幻风容器 */
.table-card {
  background: rgba(30, 41, 59, 0.5);
  border-radius: 12px;
  box-shadow: 0 0 15px rgba(99, 102, 241, 0.2);
  border: 1px solid rgba(168, 85, 247, 0.3);
  padding: 20px;
  box-sizing: border-box;
  backdrop-filter: blur(8px);
}

/* 表格样式优化 */
::v-deep .custom-user-table {
  width: 100%;
  min-width: 800px;
  margin: 0 0 16px;
  border-collapse: separate;
  border-spacing: 0;
  border-radius: 8px;
  overflow: hidden;
}
::v-deep .custom-user-table .el-table__body tr:hover > td {
  background-color: rgba(168, 85, 247, 0.1) !important;
  border-color: rgba(168, 85, 247, 0.5) !important;
  box-shadow: 0 0 8px rgba(168, 85, 247, 0.2);
}
::v-deep .custom-user-table .el-table__row.current-row > td {
  background-color: rgba(99, 102, 241, 0.15) !important;
  border-color: rgba(99, 102, 241, 0.5) !important;
}
::v-deep .custom-user-table .el-table__empty-text {
  color: rgba(248, 250, 252, 0.5) !important;
}

/* 表格操作按钮 */
.btn-edit {
  background: linear-gradient(135deg, #3B82F6, #60A5FA) !important;
  color: #FFFFFF !important;
  border: none !important;
  border-radius: 6px !important;
  margin-right: 8px !important;
  transition: all 0.2s ease;
  box-shadow: 0 0 8px rgba(59, 130, 246, 0.2);
}
.btn-edit:hover {
  background: linear-gradient(135deg, #2563EB, #3B82F6) !important;
  box-shadow: 0 0 12px rgba(59, 130, 246, 0.3);
}

.btn-delete {
  background: linear-gradient(135deg, #EF4444, #F87171) !important;
  color: #FFFFFF !important;
  border: none !important;
  border-radius: 6px !important;
  transition: all 0.2s ease;
  box-shadow: 0 0 8px rgba(239, 68, 68, 0.2);
}
.btn-delete:hover {
  background: linear-gradient(135deg, #DC2626, #EF4444) !important;
  box-shadow: 0 0 12px rgba(239, 68, 68, 0.3);
}

/* 分页器容器 */
.pagination-container {
  display: flex;
  justify-content: flex-end;
  align-items: center;
  margin-top: 16px;
}
::v-deep .custom-pagination .el-pagination__sizes,
::v-deep .custom-pagination .el-pagination__total,
::v-deep .custom-pagination .el-pagination__jump {
  color: rgba(248, 250, 252, 0.7) !important;
}
::v-deep .custom-pagination .el-pagination__sizes .el-input .el-input__inner {
  background: rgba(30, 41, 59, 0.7) !important;
  border: 1px solid rgba(168, 85, 247, 0.4) !important;
  color: #F8FAFC !important;
}
::v-deep .custom-pagination .el-pager li {
  border-radius: 6px !important;
  margin: 0 4px !important;
  transition: all 0.2s ease;
  background: rgba(30, 41, 59, 0.5) !important;
  color: rgba(248, 250, 252, 0.7) !important;
  border: 1px solid rgba(168, 85, 247, 0.3) !important;
}
::v-deep .custom-pagination .el-pager li.active {
  background: linear-gradient(135deg, #6366F1, #A855F7) !important;
  color: #adc5f2 !important;
  border-color: transparent !important;
  box-shadow: 0 0 10px rgba(99, 102, 241, 0.3);
}
::v-deep .custom-pagination .el-pager li:hover:not(.active) {
  color: #A855F7 !important;
  border-color: rgba(168, 85, 247, 0.5) !important;
  transform: scale(1.05);
  box-shadow: 0 0 8px rgba(168, 85, 247, 0.2);
}

/* 弹窗样式：科幻风玻璃拟态 */
::v-deep .custom-dialog {
  border-radius: 12px !important;
  overflow: hidden;
  background: transparent !important;
}
::v-deep .custom-dialog .el-dialog__header {
  background: linear-gradient(135deg, #1E293B, #334155);
  padding: 16px 24px !important;
  border-bottom: 1px solid rgba(168, 85, 247, 0.3);
  position: relative;
}
::v-deep .custom-dialog .el-dialog__header::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 24px;
  width: calc(100% - 48px);
  height: 1px;
  background: linear-gradient(90deg, transparent, #A855F7, transparent);
}
::v-deep .custom-dialog .el-dialog__title {
  color: #E0F2FE !important;
  font-size: 16px !important;
  font-weight: bold !important;
}
::v-deep .custom-dialog .el-dialog__body {
  padding: 24px !important;
  background: rgba(30, 41, 59, 0.7);
  backdrop-filter: blur(8px);
  border-left: 1px solid rgba(168, 85, 247, 0.3);
  border-right: 1px solid rgba(168, 85, 247, 0.3);
}
::v-deep .custom-dialog .el-dialog__footer {
  padding: 16px 24px !important;
  background: rgba(30, 41, 59, 0.7);
  backdrop-filter: blur(8px);
  border-top: 1px solid rgba(168, 85, 247, 0.3);
  border-left: 1px solid rgba(168, 85, 247, 0.3);
  border-right: 1px solid rgba(168, 85, 247, 0.3);
  border-bottom-left-radius: 12px;
  border-bottom-right-radius: 12px;
}

/* 弹窗表单 */
.dialog-form {
  width: 100%;
  max-width: 400px;
  margin: 0 auto;
}
.dialog-input {
  border-radius: 8px !important;
  background: rgba(30, 41, 59, 0.8) !important;
  border: 1px solid rgba(168, 85, 247, 0.4) !important;
  color: #f8edbe !important;
  transition: all 0.2s ease;
}
.dialog-input::placeholder {
  color: rgba(248, 250, 252, 0.5) !important;
}
.dialog-input:focus {
  box-shadow: 0 0 0 2px rgba(168, 85, 247, 0.5) !important;
  border-color: #A855F7 !important;
}
::v-deep .dialog-form .el-form-item__label {
  color: rgba(250, 248, 252, 0.8) !important;
}
::v-deep .dialog-form .el-form-item__error {
  color: #FECDD3 !important;
  background: rgba(239, 68, 68, 0.1) !important;
  padding: 2px 4px;
  border-radius: 4px;
}

/* 弹窗按钮 */
.btn-dialog-cancel {
  background: linear-gradient(135deg, #1E293B, #334155) !important;
  color: #94A3B8 !important;
  border: 1px solid rgba(168, 85, 247, 0.3) !important;
  border-radius: 8px !important;
  margin-right: 12px !important;
  transition: all 0.3s ease;
}
.btn-dialog-cancel:hover {
  background: linear-gradient(135deg, #334155, #475569) !important;
  color: #CBD5E1 !important;
  box-shadow: 0 0 10px rgba(168, 85, 247, 0.2);
}

.btn-dialog-confirm {
  background: linear-gradient(135deg, #6366F1, #A855F7) !important;
  color: #FFFFFF !important;
  border: none !important;
  border-radius: 8px !important;
  transition: all 0.3s ease;
  box-shadow: 0 0 10px rgba(99, 102, 241, 0.3);
}
.btn-dialog-confirm:hover {
  background: linear-gradient(135deg, #4F46E5, #7C3AED) !important;
  box-shadow: 0 0 15px rgba(99, 102, 241, 0.5);
}

/* 确认弹窗样式优化 */
::v-deep .custom-popconfirm .el-popconfirm__content {
  border-radius: 8px !important;
  box-shadow: 0 0 15px rgba(99, 102, 241, 0.3) !important;
  padding: 16px !important;
  background: rgba(30, 41, 59, 0.8) !important;
  border: 1px solid rgba(168, 85, 247, 0.4) !important;
  backdrop-filter: blur(5px);
}
::v-deep .custom-popconfirm .el-popconfirm__title {
  color: #FECDD3 !important;
  font-weight: bold !important;
  margin-bottom: 12px !important;
}
::v-deep .custom-popconfirm .el-button--primary {
  background: linear-gradient(135deg, #EF4444, #F87171) !important;
  border-color: transparent !important;
  border-radius: 6px !important;
}
::v-deep .custom-popconfirm .el-button--primary:hover {
  background: linear-gradient(135deg, #DC2626, #EF4444) !important;
  box-shadow: 0 0 8px rgba(239, 68, 68, 0.2);
}
::v-deep .custom-popconfirm .el-button--default {
  background: linear-gradient(135deg, #1E293B, #334155) !important;
  border-color: rgba(168, 85, 247, 0.3) !important;
  border-radius: 6px !important;
  color: #94A3B8 !important;
}
::v-deep .custom-popconfirm .el-button--default:hover {
  background: linear-gradient(135deg, #334155, #475569) !important;
  color: #CBD5E1 !important;
}

/* 消息提示框样式优化 */
::v-deep .el-message {
  background: rgba(30, 41, 59, 0.8) !important;
  border: 1px solid rgba(168, 85, 247, 0.4) !important;
  color: #F8FAFC !important;
  border-radius: 8px !important;
  box-shadow: 0 0 15px rgba(99, 102, 241, 0.2) !important;
}
::v-deep .el-message--success .el-message__icon {
  color: #4ADE80 !important;
}
::v-deep .el-message--error .el-message__icon {
  color: #FECDD3 !important;
}
::v-deep .el-message--warning .el-message__icon {
  color: #FBBF24 !important;
}
::v-deep .el-message--info .el-message__icon {
  color: #60A5FA !important;
}

/* 响应式适配 */
@media screen and (max-width: 768px) {
  .search-card {
    flex-wrap: wrap;
  }
  .search-input {
    width: 100% !important;
    margin-bottom: 8px;
  }
  ::v-deep .custom-dialog {
    width: 90% !important;
  }
}
</style>