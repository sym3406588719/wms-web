<template>
  <el-container style="height: 100%; border: 1px solid #eee">
    <el-aside :width="aside_width" style="background-color:rgb(238, 241, 246);height:100%;margin-left:-1px;">
      <SideAside :isCollapse="isCollapse"></SideAside>
      
    </el-aside>
    
    <el-container style="height: 100%;">
      <el-header style="text-align: right; font-size: 12px; height: 100%; border-bottom:#d8bbd7 4px solid;">
        <PageHeader @doCollapse="doCollapse" :icon="icon"></PageHeader>
        
      </el-header>
      
      <el-main style="height: 100%;">
       <!--<HomeMain></HomeMain>-->
       <router-view/>
      </el-main>
    </el-container>
  </el-container>
</template>


<style>
/* 自定义表格表头背景色为粉色 */
.el-table__header {
  background-color: pink !important;
}
/* 表头单元格文字颜色可根据需要调整，这里设为黑色 */
.el-table__header th {
  color: black;
}
/* 其他原有样式 */
.el-header {
  background:linear-gradient(to right, #b0bfb3,#a3daff,#c380ad);
  color: #333;
  line-height: 60px;
}
  
.el-aside {
  color: #333;
}
</style>
  

//src/components/index.vue
<script>
import SideAside from "./SideAside.vue";
import PageHeader from "./PageHeader.vue";


export default {
  // 关键：将 name 从 "index" 改为 "IndexLayout"（多个单词，PascalCase 命名）
  components:{SideAside,PageHeader},
  methods:{
       doCollapse(){
        console.log("切换前 icon:", this.icon);
        //console.log(1111)
        this.isCollapse=!this.isCollapse
        if(!this.isCollapse){
          this.aside_width='200px',
          this.icon='el-icon-s-fold'
        }else{
           this.aside_width='64px',
           this.icon='el-icon-s-unfold'
        }
        console.log("切换后 icon:", this.icon);
       },
  },
   data() {
    return {
      // 定义 isCollapse 并设置初始值（通常是布尔值，用于控制折叠状态）
      isCollapse: false,  // 例如：默认不折叠
      aside_width:'200px',
      icon:'el-icon-s-fold'
    };
  },
  name: "IndexLayout", 
  
};
</script>