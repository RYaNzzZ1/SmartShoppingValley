<template>
  <div id="main-container">
    <el-container>
      <el-aside :width="collapse ? collapseWidth : notCollapseWidth">
        <ContainerAside></ContainerAside>
      </el-aside>
      <el-container>
        <el-header><ContainerHeader></ContainerHeader></el-header>
        <el-main><router-view></router-view></el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script>
import ContainerAside from "../components/ContainerAside.vue";
import ContainerHeader from "../components/ContainerHeader.vue";
import { mapState } from "vuex";
export default {
  name: "MainContainer",
  computed: {
    ...mapState("ContainerAside", [
      "collapseWidth",
      "notCollapseWidth",
      "collapse",
    ]),
  },
  components: {
    ContainerAside,
    ContainerHeader,
  },
};
</script>

<style scoped lang="less">
#main-container {
  width: 99vw;
  .el-header {
    padding: 0;
  }
  .el-aside {
    position: relative;
    transition: width 0.4s;
  }
}
</style>
