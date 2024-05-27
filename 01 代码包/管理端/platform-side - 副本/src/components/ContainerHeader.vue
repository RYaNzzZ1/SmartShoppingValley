<template>
  <div id="ContainerHeader">
    <i
      class="collapseAsideBtn"
      @click="collapseAside"
      :class="`el-icon-${collapse ? 's-fold' : 's-unfold'}`"
    ></i>
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item
        v-for="item in tableList"
        :key="item.id"
        :to="clickBreadcrumb(item)"
        >{{ item.title }}</el-breadcrumb-item
      >
    </el-breadcrumb>
    <el-dropdown class="setting" trigger="click" @command="handleClickSetting">
      <el-button class="el-dropdown-link" icon="el-icon-setting"></el-button>
      <el-dropdown-menu slot="dropdown">
        <el-dropdown-item command="toHome" icon="el-icon-s-home"
          >首页</el-dropdown-item
        >
        <el-dropdown-item command="logout" icon="el-icon-error"
          >退出登录</el-dropdown-item
        >
      </el-dropdown-menu>
    </el-dropdown>
  </div>
</template>

<script>
import { mapState, mapActions } from "vuex";
import Cookies from "js-cookie";
export default {
  name: "ContainerHeader",
  methods: {
    ...mapActions("ContainerAside", ["changeCollapse"]),
    ...mapActions("ContainerHeader", ["selectMenu"]),
    collapseAside() {
      this.changeCollapse();
    },
    // 点击面包屑跳转路由
    clickBreadcrumb(item) {
      // 处理点击同一个菜单项两次会报错的问题
      if (
        this.$route.path !== item.path &&
        !(this.$route.path === "/home" && item.path === "/")
      ) {
        return {
          path: item.path,
        };
      }
    },
    // 点击右上角下拉菜单
    handleClickSetting(command) {
      if (command == "toHome") {
        if (this.$route.path !== "/home") {
          this.$router.push({
            name: "home",
          });
        }
      } else if (command == "logout") {
        Cookies.remove("token");
        Cookies.remove("privileges");
        this.$router.push({
          name: "login",
        });
      }
    },
  },
  computed: {
    ...mapState("ContainerAside", ["collapse"]),
    ...mapState("ContainerHeader", ["tableList"]),
  },
};
</script>

<style scoped lang="less">
#ContainerHeader {
  display: flex;
  position: relative;
  top: 20px;
  left: 20px;
  height: 50px;
  border-bottom: 1px solid rgb(230, 230, 230);
  .collapseAsideBtn {
    font-size: 30px;
  }
  .el-breadcrumb {
    position: relative;
    top: 7px;
    left: 20px;
  }
  .setting {
    position: absolute;
    top: -10%;
    right: 30px;
    font-size: 16px;
    background-color: #fff;
    color: #000;
  }
}
</style>