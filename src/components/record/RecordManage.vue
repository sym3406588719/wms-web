<template>
    <div class="record-container">
        <!-- 背景装饰元素 -->
        <div class="bg-decoration blossom1"></div>
        <div class="bg-decoration blossom2"></div>
        <div class="bg-decoration blossom3"></div>
        
        <div class="search-panel">
            <div class="search-form">
                <el-input 
                    v-model="name" 
                    placeholder="请输入物品名" 
                    suffix-icon="el-icon-search" 
                    class="search-input"
                    @keyup.enter.native="loadPost">
                    <i slot="prefix" class="el-icon-search input-icon"></i>
                </el-input>
                
                <el-select 
                    v-model="storage" 
                    placeholder="请选择仓库" 
                    class="search-select">
                    <el-option
                        v-for="item in storageData"
                        :key="item.id"
                        :label="item.name"
                        :value="item.id">
                    </el-option>
                </el-select>
                
                <el-select 
                    v-model="goodstype" 
                    placeholder="请选择分类" 
                    class="search-select">
                    <el-option
                        v-for="item in goodstypeData"
                        :key="item.id"
                        :label="item.name"
                        :value="item.id">
                    </el-option>
                </el-select>

                <el-button 
                    type="primary" 
                    class="search-btn" 
                    @click="loadPost"
                    icon="el-icon-search">
                    查询
                </el-button>
                <el-button 
                    type="success" 
                    class="reset-btn" 
                    @click="resetParam"
                    icon="el-icon-refresh">
                    重置
                </el-button>
            </div>
        </div>

        <div class="table-panel">
            <el-table 
                :data="tableData"
                :header-cell-style="{
                    background: 'linear-gradient(135deg, #f9c1d9 0%, #d4a5f1 100%)',
                    color: '#7d3c98',
                    border: '1px solid #e8d3f0',
                    fontWeight: '600',
                    fontSize: '14px'
                }"
                :cell-style="{ border: '1px solid #f0e6f5' }"
                class="ancient-table"
                border>
                
                <el-table-column prop="id" label="ID" width="80" align="center">
                    <template #header>
                        <span class="column-title">📜 编号</span>
                    </template>
                </el-table-column>
                
                <el-table-column prop="goodsname" label="物品名" width="180" align="center">
                    <template #header>
                        <span class="column-title">🎁 物品名</span>
                    </template>
                    <template #default="scope">
                        <span class="cell-text">{{ scope.row.goodsname || '未知物品' }}</span>
                    </template>
                </el-table-column>
                
                <el-table-column prop="storagename" label="仓库" width="160" align="center">
                    <template #header>
                        <span class="column-title">🏯 仓库</span>
                    </template>
                    <template #default="scope">
                        <span class="cell-text">{{ scope.row.storagename || '未知仓库' }}</span>
                    </template>
                </el-table-column>
                
                <el-table-column prop="goodstypename" label="分类" width="150" align="center">
                    <template #header>
                        <span class="column-title">📦 分类</span>
                    </template>
                    <template #default="scope">
                        <span class="cell-text">{{ scope.row.goodstypename || '未知分类' }}</span>
                    </template>
                </el-table-column>
                
                <el-table-column prop="adminname" label="操作人" width="150" align="center">
                    <template #header>
                        <span class="column-title">👨‍💼 操作人</span>
                    </template>
                    <template #default="scope">
                        <span class="cell-text">{{ scope.row.adminname || '未知操作人' }}</span>
                    </template>
                </el-table-column>
                
                <el-table-column prop="username" label="申请人" width="150" align="center">
                    <template #header>
                        <span class="column-title">👤 申请人</span>
                    </template>
                    <template #default="scope">
                        <span class="cell-text">{{ scope.row.username || '未知申请人' }}</span>
                    </template>
                </el-table-column>
                
                <el-table-column prop="count" label="数量" width="120" align="center">
                    <template #header>
                        <span class="column-title">🔢 数量</span>
                    </template>
                    <template #default="scope">
                        <span 
                            :class="{
                                'count-positive': scope.row.count > 0,
                                'count-negative': scope.row.count < 0,
                                'count-zero': scope.row.count === 0
                            }"
                            class="count-text">
                            {{ scope.row.count }}
                        </span>
                    </template>
                </el-table-column>
                
                <el-table-column prop="createTime" label="操作时间" width="180" align="center">
                    <template #header>
                        <span class="column-title">⏰ 操作时间</span>
                    </template>
                    <template #default="scope">
                        <span class="time-text">{{ scope.row.createTime }}</span>
                    </template>
                </el-table-column>
                
                <el-table-column prop="remark" label="备注" min-width="200" align="center">
                    <template #header>
                        <span class="column-title">📝 备注</span>
                    </template>
                    <template #default="scope">
                        <span class="remark-text">{{ scope.row.remark || '无备注' }}</span>
                    </template>
                </el-table-column>
            </el-table>
        </div>

        <div class="pagination-panel">
            <el-pagination
                @size-change="handleSizeChange"
                @current-change="handleCurrentChange"
                :current-page="pageNum"
                :page-sizes="[5, 10, 20, 30]"
                :page-size="pageSize"
                layout="total, sizes, prev, pager, next, jumper"
                :total="total"
                class="ancient-pagination">
            </el-pagination>
        </div>
    </div>
</template>

<script>
    export default {
        name: "RecordManage",
        data() {
            return {
                user: JSON.parse(sessionStorage.getItem('CurUser')),
                storageData: [],
                goodstypeData: [],
                tableData: [],
                pageSize: 10,
                pageNum: 1,
                total: 0,
                name: '',
                storage: '',
                goodstype: '',
                centerDialogVisible: false,
                form: {
                    id: '',
                    name: '',
                    storage: '',
                    goodstype: '',
                    count: '',
                    remark: ''
                },
            }
        },
        methods: {
            // 所有原有方法保持不变
            formatStorage(row) {
                let temp = this.storageData.find(item => {
                    return item.id == row.storage
                })
                return temp && temp.name
            },
            formatGoodstype(row) {
                let temp = this.goodstypeData.find(item => {
                    return item.id == row.goodstype
                })
                return temp && temp.name
            },
            resetForm() {
                this.$refs.form.resetFields();
            },
            handleSizeChange(val) {
                console.log(`每页 ${val} 条`);
                this.pageNum = 1
                this.pageSize = val
                this.loadPost()
            },
            handleCurrentChange(val) {
                console.log(`当前页: ${val}`);
                this.pageNum = val
                this.loadPost()
            },
            resetParam() {
                this.name = ''
                this.storage = ''
                this.goodstype = ''
            },
            loadStorage() {
                this.$axios.get(this.$httpUrl + '/storage/list').then(res => res.data).then(res => {
                    console.log(res)
                    if (res.code == 200) {
                        this.storageData = res.data
                    } else {
                        alert('获取数据失败')
                    }
                })
            },
            loadGoodstype() {
                this.$axios.get(this.$httpUrl + '/goodstype/list').then(res => res.data).then(res => {
                    console.log(res)
                    if (res.code == 200) {
                        this.goodstypeData = res.data
                    } else {
                        alert('获取数据失败')
                    }
                })
            },
            loadPost() {
                console.log('=== 前端查询参数调试 ===');
                console.log('物品名 name:', this.name, '类型:', typeof this.name);
                console.log('仓库 storage:', this.storage, '类型:', typeof this.storage);
                console.log('分类 goodstype:', this.goodstype, '类型:', typeof this.goodstype);
                console.log('当前用户:', this.user);

                const requestData = {
                    pageSize: this.pageSize,
                    pageNum: this.pageNum,
                    param: {
                        name: this.name,
                        goodstype: this.goodstype + '',
                        storage: this.storage + '',
                        roleId: this.user.roleId + '',
                        userId: this.user.id + ''
                    }
                };

                console.log('发送的请求数据:', JSON.stringify(requestData, null, 2));

                this.$axios.post(this.$httpUrl + '/record/listPage', requestData)
                    .then(res => res.data)
                    .then(res => {
                        console.log('=== 后端返回数据 ===');
                        console.log('返回码:', res.code);
                        console.log('数据量:', res.data ? res.data.length : 0);
                        console.log('总记录数:', res.total);
                        console.log('详细数据:', res.data);

                        if (res.code == 200) {
                            this.tableData = res.data
                            this.total = res.total
                        } else {
                            alert('获取数据失败')
                        }
                    })
                    .catch(error => {
                        console.error('请求错误:', error);
                        alert('请求失败: ' + error.message);
                    })
            },
        },
        beforeMount() {
            this.loadStorage()
            this.loadGoodstype()
            this.loadPost()
        }
    }
</script>

<style scoped>
/* 样式保持不变，与之前相同 */
.record-container {
    position: relative;
    background: linear-gradient(135deg, #fce4ec 0%, #f3e5f5 50%, #e8eaf6 100%);
    padding: 20px;
    overflow: hidden;
}

.bg-decoration {
    position: absolute;
    background-size: contain;
    background-repeat: no-repeat;
    opacity: 0.1;
    z-index: 0;
}

.blossom1 {
    width: 200px;
    height: 200px;
    top: 10%;
    left: 5%;
    background-image: url('data:image/svg+xml,<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 100 100"><path fill="%23e91e63" d="M50 20a30 30 0 1 1 0 60 30 30 0 0 1 0-60zm0 10a20 20 0 1 0 0 40 20 20 0 0 0 0-40z"/></svg>');
}

.blossom2 {
    width: 150px;
    height: 150px;
    top: 60%;
    right: 8%;
    background-image: url('data:image/svg+xml,<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 100 100"><path fill="%239c27b0" d="M50 10c20 0 35 15 35 35s-15 35-35 35-35-15-35-35 15-35 35-35z"/></svg>');
}

.blossom3 {
    width: 180px;
    height: 180px;
    bottom: 10%;
    left: 15%;
    background-image: url('data:image/svg+xml,<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 100 100"><path fill="%23673ab7" d="M50 5c25 0 45 20 45 45S75 95 50 95 5 75 5 50 25 5 50 5z"/></svg>');
}

.search-panel {
    background: rgba(255, 255, 255, 0.9);
    border-radius: 15px;
    padding: 20px;
    margin-bottom: 20px;
    box-shadow: 0 8px 25px rgba(156, 39, 176, 0.15);
    border: 1px solid rgba(233, 30, 99, 0.2);
    position: relative;
    z-index: 1;
}

.search-form {
    display: flex;
    align-items: center;
    gap: 12px;
    flex-wrap: wrap;
}

.search-input {
    width: 220px;
}

.search-input >>> .el-input__inner {
    border-radius: 25px;
    border: 2px solid #e1bee7;
    background: rgba(248, 231, 253, 0.8);
    color: #7b1fa2;
    transition: all 0.3s ease;
}

.search-input >>> .el-input__inner:focus {
    border-color: #e91e63;
    box-shadow: 0 0 0 2px rgba(233, 30, 99, 0.2);
}

.input-icon {
    color: #ab47bc;
    margin-left: 8px;
}

.search-select {
    width: 160px;
}

.search-select >>> .el-input__inner {
    border-radius: 20px;
    border: 2px solid #c5cae9;
    background: rgba(232, 234, 246, 0.8);
    color: #5c6bc0;
}

.search-btn {
    background: linear-gradient(135deg, #e91e63, #9c27b0);
    border: none;
    border-radius: 20px;
    color: white;
    padding: 10px 20px;
    transition: all 0.3s ease;
}

.search-btn:hover {
    transform: translateY(-2px);
    box-shadow: 0 5px 15px rgba(156, 39, 176, 0.4);
}

.reset-btn {
    background: linear-gradient(135deg, #7e57c2, #5c6bc0);
    border: none;
    border-radius: 20px;
    color: white;
    padding: 10px 20px;
    transition: all 0.3s ease;
}

.reset-btn:hover {
    transform: translateY(-2px);
    box-shadow: 0 5px 15px rgba(92, 107, 192, 0.4);
}

.table-panel {
    background: rgba(255, 255, 255, 0.95);
    border-radius: 15px;
    padding: 20px;
    margin-bottom: 20px;
    box-shadow: 0 8px 25px rgba(156, 39, 176, 0.1);
    border: 1px solid rgba(233, 30, 99, 0.15);
    position: relative;
    z-index: 1;
}

.ancient-table >>> .el-table {
    border-radius: 12px;
    overflow: hidden;
    background: transparent;
}

.ancient-table >>> .el-table__header {
    border-radius: 12px 12px 0 0;
}

.column-title {
    font-weight: 600;
    font-size: 14px;
}

.cell-text {
    color: #7d3c98;
    font-weight: 500;
}

.count-positive {
    color: #e91e63;
    font-weight: bold;
}

.count-negative {
    color: #5c6bc0;
    font-weight: bold;
}

.count-zero {
    color: #9e9e9e;
}

.time-text {
    color: #7e57c2;
    font-size: 12px;
}

.remark-text {
    color: #666;
    font-style: italic;
}

.pagination-panel {
    background: rgba(255, 255, 255, 0.9);
    border-radius: 15px;
    padding: 15px 20px;
    box-shadow: 0 5px 20px rgba(156, 39, 176, 0.1);
    border: 1px solid rgba(233, 30, 99, 0.1);
    position: relative;
    z-index: 1;
}

.ancient-pagination >>> .el-pagination {
    text-align: center;
}

.ancient-pagination >>> .el-pager li {
    border-radius: 50%;
    margin: 0 4px;
    background: rgba(233, 30, 99, 0.1);
    color: #7d3c98;
    border: 1px solid rgba(233, 30, 99, 0.2);
}

.ancient-pagination >>> .el-pager li.active {
    background: linear-gradient(135deg, #e91e63, #9c27b0);
    color: white;
}

.ancient-pagination >>> .el-pagination__jump {
    color: #7d3c98;
}

@media (max-width: 768px) {
    .search-form {
        flex-direction: column;
        align-items: stretch;
    }
    
    .search-input,
    .search-select {
        width: 100%;
        margin-bottom: 10px;
    }
    
    .search-btn,
    .reset-btn {
        width: 100%;
        margin: 5px 0;
    }
}
</style>