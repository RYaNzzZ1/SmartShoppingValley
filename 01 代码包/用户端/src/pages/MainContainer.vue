<template>
  <div id="main-container">
        <el-main><router-view></router-view></el-main>
  </div>
</template>

<script>
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
