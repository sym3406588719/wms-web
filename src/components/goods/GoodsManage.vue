<template>
    <div class="universe-container">
        <!-- 星空背景 -->
        <div class="stars"></div>
        <div class="twinkling"></div>
        
        <div class="cosmic-header">
            <div class="nebula-search-panel">
                <el-input 
                    v-model="name" 
                    placeholder="请输入物品名" 
                    suffix-icon="el-icon-search" 
                    class="cosmic-input"
                    @keyup.enter.native="loadPost">
                </el-input>
                <el-select 
                    v-model="storage" 
                    placeholder="请选择仓库" 
                    class="cosmic-select">
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
                    class="cosmic-select">
                    <el-option
                            v-for="item in goodstypeData"
                            :key="item.id"
                            :label="item.name"
                            :value="item.id">
                    </el-option>
                </el-select>

                <el-button type="primary" class="cosmic-btn starburst-query" @click="loadPost">查询</el-button>
                <el-button type="success" class="cosmic-btn nebula-reset" @click="resetParam">重置</el-button>

                <el-button type="primary" class="cosmic-btn galaxy-add" @click="add">新增</el-button>
                <el-button type="primary" class="cosmic-btn supernova-in" @click="inGoods">入库</el-button>
                <el-button type="primary" class="cosmic-btn blackhole-out" @click="outGoods">出库</el-button>
            </div>
        </div>
        
        <div class="cosmic-table-section">
            <div class="table-nebula-bg"></div>
            <el-table 
                :data="tableData"
                :header-cell-style="{ 
                    background: 'linear-gradient(135deg, rgba(102, 51, 153, 0.9) 0%, rgba(75, 0, 130, 0.9) 100%)', 
                    color: '#e6e6fa',
                    fontWeight: 'bold',
                    fontSize: '14px',
                    border: '1px solid rgba(147, 112, 219, 0.5)',
                    textShadow: '0 0 10px rgba(138, 43, 226, 0.8)'
                }"
                :cell-style="{ 
                    background: 'rgba(25, 25, 112, 0.7)', 
                    color: '#e6e6fa',
                    border: '1px solid rgba(138, 43, 226, 0.3)',
                    textShadow: '0 0 5px rgba(138, 43, 226, 0.5)'
                }"
                border
                highlight-current-row
                @current-change="selectCurrentChange"
                class="cosmic-table"
                style="width: 100%">
                <el-table-column prop="id" label="ID" width="80" align="center">
                </el-table-column>
                <el-table-column prop="name" label="物品名" width="180" align="center">
                </el-table-column>
                <el-table-column prop="storage" label="仓库" width="180" :formatter="formatStorage" align="center">
                </el-table-column>
                <el-table-column prop="goodsType" label="分类" width="180" :formatter="formatGoodstype" align="center">
                </el-table-column>
                <el-table-column prop="count" label="数量" width="120" align="center">
                </el-table-column>
                <el-table-column prop="remark" label="备注" align="center">
                </el-table-column>
                <el-table-column prop="operate" label="操作" v-if="user.roleId!=2" align="center" width="200">
                    <template slot-scope="scope">
                        <el-button size="small" type="success" class="cosmic-operate-btn pulsar-edit" @click="mod(scope.row)">编辑</el-button>
                        <el-popconfirm
                                title="确定删除吗？"
                                @confirm="del(scope.row.id)"
                                style="margin-left: 5px;"
                        >
                            <el-button slot="reference" size="small" type="danger" class="cosmic-operate-btn quasar-delete">删除</el-button>
                        </el-popconfirm>
                    </template>
                </el-table-column>
            </el-table>
        </div>
        
        <div class="cosmic-pagination-section">
            <el-pagination
                    @size-change="handleSizeChange"
                    @current-change="handleCurrentChange"
                    :current-page="pageNum"
                    :page-sizes="[5, 10, 20,30]"
                    :page-size="pageSize"
                    layout="total, sizes, prev, pager, next, jumper"
                    :total="total"
                    class="cosmic-pagination">
            </el-pagination>
        </div>

        <el-dialog
                title="物品新增"
                :visible.sync="centerDialogVisible"
                width="30%"
                center
                class="cosmic-dialog">

            <div class="dialog-nebula"></div>
            <el-form ref="form" :rules="rules" :model="form" label-width="80px">
                <el-form-item label="物品名" prop="name" class="cosmic-form-item">
                    <el-col :span="20">
                        <el-input v-model="form.name" class="cosmic-dialog-input"></el-input>
                    </el-col>
                </el-form-item>
                <el-form-item label="仓库" prop="storage" class="cosmic-form-item">
                    <el-col :span="20">
                        <el-select v-model="form.storage" placeholder="请选择仓库" class="cosmic-dialog-select">
                            <el-option
                                    v-for="item in storageData"
                                    :key="item.id"
                                    :label="item.name"
                                    :value="item.id">
                            </el-option>
                        </el-select>
                    </el-col>
                </el-form-item>
                <el-form-item label="分类" prop="goodstype" class="cosmic-form-item">
                    <el-col :span="20">
                        <el-select v-model="form.goodstype" placeholder="请选择分类" class="cosmic-dialog-select">
                            <el-option
                                    v-for="item in goodstypeData"
                                    :key="item.id"
                                    :label="item.name"
                                    :value="item.id">
                            </el-option>
                        </el-select>
                    </el-col>
                </el-form-item>
                <el-form-item label="数量" prop="count" class="cosmic-form-item">
                    <el-col :span="20">
                        <el-input v-model="form.count" class="cosmic-dialog-input"></el-input>
                    </el-col>
                </el-form-item>
                <el-form-item label="备注" prop="remark" class="cosmic-form-item">
                    <el-col :span="20">
                        <el-input type="textarea" v-model="form.remark" class="cosmic-dialog-textarea"></el-input>
                    </el-col>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
    <el-button @click="centerDialogVisible = false" class="cosmic-cancel-btn">取 消</el-button>
    <el-button type="primary" @click="save" class="cosmic-confirm-btn">确 定</el-button>
  </span>
        </el-dialog>

        <el-dialog
                title="出入库"
                :visible.sync="inDialogVisible"
                width="30%"
                center
                class="cosmic-dialog">

            <el-dialog
                    width="75%"
                    title="用户选择"
                    :visible.sync="innerVisible"
                    append-to-body
                    class="cosmic-dialog">
                <SelectUser @doSelectUser="doSelectUser"></SelectUser>
                <span slot="footer" class="dialog-footer">
                    <el-button @click="innerVisible = false" class="cosmic-cancel-btn">取 消</el-button>
                    <el-button type="primary" @click="confirmUser" class="cosmic-confirm-btn">确 定</el-button>
                  </span>
            </el-dialog>

            <div class="dialog-nebula"></div>
            <el-form ref="form1" :rules="rules1" :model="form1" label-width="80px">
                <el-form-item label="物品名" class="cosmic-form-item">
                    <el-col :span="20">
                        <el-input v-model="form1.goodsname" readonly class="cosmic-dialog-input"></el-input>
                    </el-col>
                </el-form-item>
                <el-form-item label="申请人" class="cosmic-form-item">
                    <el-col :span="20">
                        <el-input v-model="form1.username" readonly @click.native="selectUser" class="cosmic-dialog-input cosmic-user-select"></el-input>
                    </el-col>
                </el-form-item>
                <el-form-item label="数量" prop="count" class="cosmic-form-item">
                    <el-col :span="20">
                        <el-input v-model="form1.count" class="cosmic-dialog-input"></el-input>
                    </el-col>
                </el-form-item>
                <el-form-item label="备注" prop="remark" class="cosmic-form-item">
                    <el-col :span="20">
                        <el-input type="textarea" v-model="form1.remark" class="cosmic-dialog-textarea"></el-input>
                    </el-col>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
    <el-button @click="inDialogVisible = false" class="cosmic-cancel-btn">取 消</el-button>
    <el-button type="primary" @click="doInGoods" class="cosmic-confirm-btn">确 定</el-button>
  </span>
        </el-dialog>
    </div>
</template>

<script>
    import SelectUser from "../user/SelectUser";
    export default {
        name: "GoodsManage",
        components: {SelectUser},
        data() {
            let checkCount = (rule, value, callback) => {
                if(value>9999){
                    callback(new Error('数量输入过大'));
                }else{
                    callback();
                }
            };

            return {
                user : JSON.parse(sessionStorage.getItem('CurUser')),
                storageData:[],
                goodstypeData:[],
                tableData: [],
                pageSize:10,
                pageNum:1,
                total:0,
                name:'',
                storage:'',
                goodstype:'',
                centerDialogVisible:false,
                inDialogVisible:false,
                innerVisible:false,
                currentRow:{},
                tempUser:{},
                form:{
                    id:'',
                    name:'',
                    storage:'',
                    goodstype:'',
                    count:'',
                    remark:''
                },
                form1:{
                    goodsId:'',      // 统一字段名
                    goodsname:'',
                    count:'',
                    username:'',
                    userId:'',       // 统一字段名
                    adminId:'',
                    remark:'',
                    action:'1'
                },
                rules1: {
                    count: [
                        {required: true, message: '请输入数量', trigger: 'blur'},
                        {pattern: /^([1-9][0-9]*){1,4}$/,message: '数量必须为正整数字',trigger: "blur"},
                        {validator: checkCount, trigger: 'blur'}
                    ]
                },
                rules: {
                    name: [
                        {required: true, message: '请输入物品名', trigger: 'blur'}
                    ],
                    storage:[
                        {required: true, message: '请选择仓库', trigger: 'blur'}
                    ],
                    goodstype:[
                        {required: true, message: '请选择分类', trigger: 'blur'}
                    ],
                    count: [
                        {required: true, message: '请输入数量', trigger: 'blur'},
                        {pattern: /^([1-9][0-9]*){1,4}$/,message: '数量必须为正整数字',trigger: "blur"},
                        {validator:checkCount,trigger: 'blur'}
                    ],
                }
            }
        },
        methods:{
            doSelectUser(val){
                console.log(val)
                this.tempUser = val
            },
            confirmUser(){
                this.form1.username = this.tempUser.name
                this.form1.userId = this.tempUser.id  // 统一使用 userId
                this.innerVisible = false
            },
            selectCurrentChange(val) {
                this.currentRow = val;
            },
            formatStorage(row){
                let temp =  this.storageData.find(item=>{
                    return item.id == row.storage
                })
                return temp && temp.name
            },
            formatGoodstype(row) {
               if (!row.goodsType && row.goodsType !== 0) {
               return '未分类';
                 }
               const goodsTypeId = Number(row.goodsType);
               const foundType = this.goodstypeData.find(item => {
        // 同样将 item.id 转为数字，确保类型一致
               return Number(item.id) === goodsTypeId;
           });

    // 4. 如果找到了，返回名称；否则返回 '未知分类'
    return foundType ? foundType.name : '未知分类';
            },
            resetForm() {
                this.$refs.form.resetFields();
            },
            resetInForm(){
                this.$refs.form1.resetFields();
            },
            del(id){
                console.log(id)
                this.$axios.get(this.$httpUrl+'/goods/del?id='+id).then(res=>res.data).then(res=>{
                    console.log(res)
                    if(res.code==200){
                        this.$message({
                            message: '操作成功！',
                            type: 'success'
                        });
                        this.loadPost()
                    }else{
                        this.$message({
                            message: '操作失败！',
                            type: 'error'
                        });
                    }
                })
            },
            mod(row){
                this.centerDialogVisible = true
                this.$nextTick(()=>{
                    //赋值到表单
                    this.form.id = row.id
                    this.form.name = row.name
                    this.form.storage = row.storage
                    this.form.goodstype = row.goodstype
                    this.form.count = row.count
                    this.form.remark = row.remark
                })
            },
            add(){
                this.centerDialogVisible = true
                this.$nextTick(()=>{
                    this.resetForm()
                    this.form.id = ''
                })
            },
            inGoods(){
                if(!this.currentRow.id){
                    alert('请选择记录');
                    return;
                }
                this.inDialogVisible = true
                this.$nextTick(()=>{
                    this.resetInForm()
                })
                this.form1.goodsname = this.currentRow.name
                this.form1.goodsId = this.currentRow.id  // 统一使用 goodsId
                this.form1.adminId = this.user.id
                this.form1.action = '1'
            },
            outGoods(){
                if(!this.currentRow.id){
                    alert('请选择记录');
                    return;
                }
                this.inDialogVisible = true
                this.$nextTick(()=>{
                    this.resetInForm()
                })
                this.form1.goodsname = this.currentRow.name
                this.form1.goodsId = this.currentRow.id  // 统一使用 goodsId
                this.form1.adminId = this.user.id
                this.form1.action = '2'
            },
            selectUser(){
                this.innerVisible=true
            },
            doSave(){
                this.$axios.post(this.$httpUrl+'/goods/save',this.form).then(res=>res.data).then(res=>{
                    console.log(res)
                    if(res.code==200){
                        this.$message({
                            message: '操作成功！',
                            type: 'success'
                        });
                        this.centerDialogVisible = false
                        this.loadPost()
                        this.resetForm()
                    }else{
                        this.$message({
                            message: '操作失败！',
                            type: 'error'
                        });
                    }
                })
            },
            doMod(){
                this.$axios.post(this.$httpUrl+'/goods/update',this.form).then(res=>res.data).then(res=>{
                    console.log(res)
                    if(res.code==200){
                        this.$message({
                            message: '操作成功！',
                            type: 'success'
                        });
                        this.centerDialogVisible = false
                        this.loadPost()
                        this.resetForm()
                    }else{
                        this.$message({
                            message: '操作失败！',
                            type: 'error'
                        });
                    }
                })
            },
            save(){
                this.$refs.form.validate((valid) => {
                    if (valid) {
                        if(this.form.id){
                            this.doMod();
                        }else{
                            this.doSave();
                        }
                    } else {
                        console.log('error submit!!');
                        return false;
                    }
                });
            },
            doInGoods(){
                this.$refs.form1.validate((valid) => {
                    if (valid) {
                        // 检查必要字段
                        if (!this.form1.goodsId) {
                            this.$message.error('物品信息缺失');
                            return;
                        }
                        if (!this.form1.userId) {
                            this.$message.error('请选择申请人');
                            return;
                        }
                        
                        console.log('提交的出入库数据:', this.form1)
                        
                        this.$axios.post(this.$httpUrl+'/record/save',this.form1)
                        .then(res=>res.data)
                        .then(res=>{
                            console.log(res)
                            if(res.code==200){
                                this.$message({
                                    message: '操作成功！',
                                    type: 'success'
                                });
                                this.inDialogVisible = false
                                this.loadPost()
                                this.resetInForm()
                            }else{
                                this.$message({
                                    message: '操作失败！: ' + (res.msg || '未知错误'),
                                    type: 'error'
                                });
                            }
                        })
                        .catch(error => {
                            console.error('出入库操作失败:', error)
                            let errorMsg = '服务器错误'
                            if (error.response) {
                                errorMsg = `服务器错误 (${error.response.status})`
                                if (error.response.data) {
                                    console.error('错误详情:', error.response.data)
                                }
                            } else if (error.request) {
                                errorMsg = '网络错误，请检查后端服务'
                            }
                            this.$message({
                                message: '操作失败: ' + errorMsg,
                                type: 'error'
                            });
                        })
                    } else {
                        console.log('表单验证失败');
                        return false;
                    }
                });
            },
            handleSizeChange(val) {
                console.log(`每页 ${val} 条`);
                this.pageNum=1
                this.pageSize=val
                this.loadPost()
            },
            handleCurrentChange(val) {
                console.log(`当前页: ${val}`);
                this.pageNum=val
                this.loadPost()
            },
            resetParam(){
                this.name=''
                this.storage=''
                this.goodstype=''
            },
            loadPost(){
                this.$axios.post(this.$httpUrl+'/goods/listPage',{
                    pageSize:this.pageSize,
                    pageNum:this.pageNum,
                    param:{
                        name:this.name,
                        goodstype:this.goodstype+'',
                        storage:this.storage+''
                    }
                }).then(res=>res.data).then(res=>{
                    console.log(res)
                    if(res.code==200){
                        this.tableData=res.data
                        this.total=res.total
                    }else{
                        alert('获取数据失败')
                    }
                })
            },
            loadStorage(){
                this.$axios.get(this.$httpUrl+'/storage/list').then(res=>res.data).then(res=>{
                    console.log(res)
                    if(res.code==200){
                        this.storageData=res.data
                    }else{
                        alert('获取数据失败')
                    }
                })
            },
            loadGoodstype(){
                this.$axios.get(this.$httpUrl+'/goodstype/list').then(res=>res.data).then(res=>{
                    console.log(res)
                    if(res.code==200){
                        this.goodstypeData=res.data
                    }else{
                        alert('获取数据失败')
                    }
                })
            }
        },
        beforeMount() {
            this.loadStorage()
            this.loadGoodstype()
            this.loadPost()
        }
    }
</script>

<style scoped>
.universe-container {
    padding: 20px;
    font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
    position: relative;
    overflow-x: hidden;
}

/* 星空背景效果 */
.stars, .twinkling {
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    width: 100%;
    height: 100%;
    display: block;
    z-index: -2;
}

.stars {
    background: #000 url('data:image/svg+xml,<svg xmlns="http://www.w3.org/2000/svg" width="100" height="100"><circle cx="50" cy="50" r="1" fill="white" opacity="0.6"/></svg>') repeat;
    z-index: -2;
}

.twinkling {
    background: transparent url('data:image/svg+xml,<svg xmlns="http://www.w3.org/2000/svg" width="100" height="100"><circle cx="20" cy="20" r="0.5" fill="white" opacity="0.8"/><circle cx="80" cy="60" r="0.7" fill="white" opacity="0.9"/><circle cx="40" cy="80" r="0.4" fill="white" opacity="0.7"/></svg>') repeat;
    z-index: -1;
    animation: twinkle 4s ease-in-out infinite alternate;
}

@keyframes twinkle {
    from { opacity: 0.2; }
    to { opacity: 0.8; }
}

.cosmic-header {
    background: linear-gradient(135deg, 
        rgba(75, 0, 130, 0.8) 0%, 
        rgba(138, 43, 226, 0.7) 50%, 
        rgba(147, 112, 219, 0.6) 100%);
    border-radius: 15px;
    padding: 25px;
    margin-bottom: 25px;
    box-shadow: 
        0 8px 32px rgba(138, 43, 226, 0.3),
        inset 0 1px 0 rgba(255, 255, 255, 0.2);
    border: 1px solid rgba(147, 112, 219, 0.3);
    backdrop-filter: blur(10px);
    position: relative;
    overflow: hidden;
}

.cosmic-header::before {
    content: '';
    position: absolute;
    top: -50%;
    left: -50%;
    width: 200%;
    height: 200%;
    background: radial-gradient(circle, rgba(255,255,255,0.1) 1%, transparent 1%);
    background-size: 50px 50px;
    animation: float 20s linear infinite;
}

@keyframes float {
    from { transform: translateY(0) rotate(0deg); }
    to { transform: translateY(-50px) rotate(360deg); }
}

.nebula-search-panel {
    display: flex;
    align-items: center;
    flex-wrap: wrap;
    gap: 12px;
    position: relative;
    z-index: 1;
}

.cosmic-input, .cosmic-select {
    width: 200px;
}

.cosmic-input >>> .el-input__inner,
.cosmic-select >>> .el-input__inner {
    border-radius: 25px;
    border: 2px solid rgba(147, 112, 219, 0.5);
    background: rgba(25, 25, 112, 0.7);
    color: #e6e6fa;
    transition: all 0.4s ease;
    backdrop-filter: blur(5px);
    text-shadow: 0 0 5px rgba(138, 43, 226, 0.5);
}

.cosmic-input >>> .el-input__inner:focus,
.cosmic-select >>> .el-input__inner:focus {
    border-color: #9370db;
    box-shadow: 
        0 0 20px rgba(147, 112, 219, 0.6),
        inset 0 0 10px rgba(147, 112, 219, 0.3);
    background: rgba(75, 0, 130, 0.8);
}

.cosmic-btn {
    border-radius: 25px;
    border: none;
    font-weight: bold;
    transition: all 0.4s ease;
    padding: 12px 24px;
    color: white;
    text-shadow: 0 0 5px rgba(255,255,255,0.5);
    position: relative;
    overflow: hidden;
}

.cosmic-btn::before {
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

.cosmic-btn:hover::before {
    opacity: 0.6;
}

.starburst-query {
    background: linear-gradient(135deg, #ff6b6b 0%, #ee5a52 100%);
    box-shadow: 0 4px 15px rgba(255, 107, 107, 0.4);
}

.starburst-query:hover {
    transform: translateY(-3px) scale(1.05);
    box-shadow: 0 8px 25px rgba(255, 107, 107, 0.6);
}

.nebula-reset {
    background: linear-gradient(135deg, #4ecdc4 0%, #44a08d 100%);
    box-shadow: 0 4px 15px rgba(78, 205, 196, 0.4);
}

.nebula-reset:hover {
    transform: translateY(-3px) scale(1.05);
    box-shadow: 0 8px 25px rgba(78, 205, 196, 0.6);
}

.galaxy-add {
    background: linear-gradient(135deg, #ffd89b 0%, #19547b 100%);
    box-shadow: 0 4px 15px rgba(255, 216, 155, 0.4);
}

.galaxy-add:hover {
    transform: translateY(-3px) scale(1.05);
    box-shadow: 0 8px 25px rgba(255, 216, 155, 0.6);
}

.supernova-in {
    background: linear-gradient(135deg, #a8ff78 0%, #78ffd6 100%);
    box-shadow: 0 4px 15px rgba(168, 255, 120, 0.4);
}

.supernova-in:hover {
    transform: translateY(-3px) scale(1.05);
    box-shadow: 0 8px 25px rgba(168, 255, 120, 0.6);
}

.blackhole-out {
    background: linear-gradient(135deg, #fd746c 0%, #ff9068 100%);
    box-shadow: 0 4px 15px rgba(253, 116, 108, 0.4);
}

.blackhole-out:hover {
    transform: translateY(-3px) scale(1.05);
    box-shadow: 0 8px 25px rgba(253, 116, 108, 0.6);
}

.cosmic-table-section {
    background: rgba(25, 25, 112, 0.6);
    border-radius: 15px;
    padding: 20px;
    margin-bottom: 25px;
    box-shadow: 
        0 8px 32px rgba(75, 0, 130, 0.4),
        inset 0 1px 0 rgba(255, 255, 255, 0.1);
    border: 1px solid rgba(147, 112, 219, 0.3);
    backdrop-filter: blur(10px);
    position: relative;
    overflow: hidden;
}

.table-nebula-bg {
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: 
        radial-gradient(circle at 20% 80%, rgba(138, 43, 226, 0.2) 0%, transparent 50%),
        radial-gradient(circle at 80% 20%, rgba(147, 112, 219, 0.2) 0%, transparent 50%);
    z-index: 0;
}

.cosmic-table {
    border-radius: 10px;
    overflow: hidden;
    position: relative;
    z-index: 1;
}

.cosmic-table >>> .el-table__header-wrapper {
    border-radius: 10px 10px 0 0;
}

.cosmic-operate-btn {
    border-radius: 20px;
    border: none;
    font-weight: bold;
    transition: all 0.3s ease;
    color: white;
    text-shadow: 0 0 3px rgba(255,255,255,0.5);
}

.pulsar-edit {
    background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
    box-shadow: 0 3px 10px rgba(79, 172, 254, 0.4);
}

.pulsar-edit:hover {
    transform: translateY(-2px);
    box-shadow: 0 5px 15px rgba(79, 172, 254, 0.6);
}

.quasar-delete {
    background: linear-gradient(135deg, #fa709a 0%, #fee140 100%);
    box-shadow: 0 3px 10px rgba(250, 112, 154, 0.4);
}

.quasar-delete:hover {
    transform: translateY(-2px);
    box-shadow: 0 5px 15px rgba(250, 112, 154, 0.6);
}

.cosmic-pagination-section {
    display: flex;
    justify-content: center;
}

.cosmic-pagination >>> .el-pagination {
    padding: 15px;
    background: rgba(25, 25, 112, 0.6);
    border-radius: 25px;
    backdrop-filter: blur(5px);
}

.cosmic-pagination >>> .el-pager li {
    border-radius: 50%;
    margin: 0 5px;
    background: rgba(147, 112, 219, 0.3);
    border: 1px solid rgba(147, 112, 219, 0.5);
    color: #e6e6fa;
    transition: all 0.3s ease;
}

.cosmic-pagination >>> .el-pager li.active {
    background: linear-gradient(135deg, #9370db 0%, #8a2be2 100%);
    color: white;
    box-shadow: 0 0 10px rgba(147, 112, 219, 0.6);
}

.cosmic-pagination >>> .el-pager li:hover {
    transform: scale(1.1);
    background: rgba(147, 112, 219, 0.5);
}

.cosmic-dialog >>> .el-dialog {
    border-radius: 20px;
    background: linear-gradient(135deg, 
        rgba(25, 25, 112, 0.9) 0%, 
        rgba(75, 0, 130, 0.8) 100%);
    border: 2px solid rgba(147, 112, 219, 0.5);
    box-shadow: 
        0 20px 40px rgba(0, 0, 0, 0.5),
        inset 0 1px 0 rgba(255, 255, 255, 0.1);
    backdrop-filter: blur(10px);
}

.cosmic-dialog >>> .el-dialog__header {
    background: linear-gradient(135deg, 
        rgba(102, 51, 153, 0.9) 0%, 
        rgba(75, 0, 130, 0.9) 100%);
    border-radius: 20px 20px 0 0;
    padding: 20px;
    border-bottom: 1px solid rgba(147, 112, 219, 0.3);
}

.cosmic-dialog >>> .el-dialog__title {
    color: #e6e6fa;
    font-weight: bold;
    font-size: 18px;
    text-shadow: 0 0 10px rgba(138, 43, 226, 0.8);
}

.dialog-nebula {
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: 
        radial-gradient(circle at 30% 30%, rgba(138, 43, 226, 0.1) 0%, transparent 50%),
        radial-gradient(circle at 70% 70%, rgba(147, 112, 219, 0.1) 0%, transparent 50%);
    pointer-events: none;
    border-radius: 20px;
}

.cosmic-form-item >>> .el-form-item__label {
    color: #e6e6fa !important;
    text-shadow: 0 0 5px rgba(138, 43, 226, 0.5);
    font-weight: bold;
}

.cosmic-dialog-input >>> .el-input__inner,
.cosmic-dialog-select >>> .el-input__inner,
.cosmic-dialog-textarea >>> .el-textarea__inner {
    border-radius: 15px;
    border: 2px solid rgba(147, 112, 219, 0.5);
    background: rgba(25, 25, 112, 0.7);
    color: #e6e6fa;
    transition: all 0.3s ease;
    backdrop-filter: blur(5px);
}

.cosmic-dialog-input >>> .el-input__inner:focus,
.cosmic-dialog-select >>> .el-input__inner:focus,
.cosmic-dialog-textarea >>> .el-textarea__inner:focus {
    border-color: #9370db;
    box-shadow: 0 0 15px rgba(147, 112, 219, 0.5);
    background: rgba(75, 0, 130, 0.8);
}

.cosmic-user-select >>> .el-input__inner {
    cursor: pointer;
    background: rgba(147, 112, 219, 0.3);
}

.cosmic-cancel-btn {
    border-radius: 20px;
    border: 2px solid #9370db;
    color: #9370db;
    background: transparent;
    transition: all 0.3s ease;
    font-weight: bold;
}

.cosmic-cancel-btn:hover {
    background: #9370db;
    color: white;
    transform: translateY(-2px);
    box-shadow: 0 5px 15px rgba(147, 112, 219, 0.4);
}

.cosmic-confirm-btn {
    border-radius: 20px;
    border: none;
    background: linear-gradient(135deg, #9370db 0%, #8a2be2 100%);
    transition: all 0.3s ease;
    font-weight: bold;
    box-shadow: 0 4px 15px rgba(147, 112, 219, 0.4);
}

.cosmic-confirm-btn:hover {
    transform: translateY(-2px);
    box-shadow: 0 8px 25px rgba(147, 112, 219, 0.6);
}
</style>