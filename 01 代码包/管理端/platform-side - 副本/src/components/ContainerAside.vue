<template>
  <div id="ContainerAside">
    <el-menu
      :default-active="currentRouterId"
      class="el-menu-vertical-demo"
      text-color="#666"
      :collapse-transition="false"
    >
      <template v-if="showAside"> </template>
      <el-menu-item
        v-for="item in itemsHasNoChildren"
        :key="item.id"
        :index="item.id"
        :disabled="
          item.title == '管理其他管理员' && (privileges === 2 ? true : false)
        "
        @click="clickMenu(item)"
      >
        <i :class="`el-icon-${item.icon}`"></i>
        <span slot="title" v-show="collapse">{{ item.title }}</span>
      </el-menu-item>
      <el-submenu
        v-for="item in itemsHasChildren"
        :key="item.id"
        :index="item.id"
      >
        <template slot="title">
          <i :class="`el-icon-${item.icon}`"></i>
          <span v-show="collapse">{{ item.title }}</span>
        </template>
        <el-menu-item
          v-show="collapse"
          v-for="subItem in item.children"
          :key="subItem.id"
          :index="subItem.id"
          class="el-menu-item-option"
          @click="clickMenu(subItem)"
        >
          <i :class="`el-icon-${subItem.icon}`"></i>
          {{ subItem.title }}</el-menu-item
        >
      </el-submenu>
    </el-menu>
  </div>
</template>

<script>
import { mapActions, mapState } from "vuex";
import { nanoid } from "nanoid";
import Cookies from "js-cookie";
export default {
  name: "ContainerAside",
  data() {
    return {
      itemsHasNoChildren: [
        {
          id: nanoid(),
          title: "首页",
          icon: "s-home",
          path: "/home",
        },
        {
          id: nanoid(),
          title: "用户管理",
          icon: "s-custom",
          path: "/user",
        },
        {
          id: nanoid(),
          title: "门店信息管理",
          icon: "s-shop",
          path: "/shop",
        },
        {
          id: nanoid(),
          title: "商品管理",
          icon: "s-goods",
          path: "/goods",
        },
        {
          id: nanoid(),
          title: "种类管理",
          icon: "s-cooperation",
          path: "/type",
        },
        {
          id: nanoid(),
          title: "管理其他管理员",
          icon: "document",
          path: "/admin",
        },
        {
          id: nanoid(),
          title: "订单管理",
          icon: "s-order",
          path: "/order",
        },
      ],
      itemsHasChildren: [
        {
          id: nanoid(),
          title: "活动管理",
          icon: "message-solid",
          children: [
            {
              id: nanoid(),
              title: "优惠券管理",
              icon: "s-ticket",
              path: "/coupon",
            },
            {
              id: nanoid(),
              title: "折扣管理",
              icon: "s-marketing",
              path: "/discount",
            },
          ],
        },
      ],
      privileges: "",
      showAside: false,
    };
  },
  methods: {
    ...mapActions("ContainerHeader", ["selectMenu"]),
    clickMenu(item) {
      // 处理点击同一个菜单项两次会报错的问题
      if (
        this.$route.path !== item.path &&
        !(this.$route.path === "/home" && item.path === "/")
      ) {
        this.$router.push({
          path: item.path,
        });
      }
      // 变换面包屑
      const index = this.tableList.findIndex((key) => key.title === item.title);
      // 不存在，需要添加
      if (index === -1) {
        this.selectMenu(item);
      }
    },
  },
  computed: {
    ...mapState("ContainerAside", ["collapse"]),
    ...mapState("ContainerHeader", ["tableList"]),
    ...mapState("Admin", ["admin"]),
    // 用来设定当前激活的菜单
    currentRouterId: {
      get() {
        for (const item of this.itemsHasChildren) {
          for (const child of item.children) {
            if (this.$route.path == child.path) return child.id;
          }
          if (this.$route.path == item.path) return item.id;
        }
        for (const item of this.itemsHasNoChildren) {
          if (this.$route.path == item.path) return item.id;
        }
      },
    },
  },

  mounted() {
    this.privileges = Cookies.get("privileges") || this.admin.privileges || 2;
    this.showAside = true;
  },
};
</script>

<style scoped lang="less">
@backgroundColor_01: rgb(60, 60, 60);
@backgroundColor_02: rgb(3, 20, 40);

#ContainerAside {
  height: 100%;

  .el-menu-vertical-demo {
    height: 100vh;
    /deep/.el-submenu__title {
      &:hover {
        background-color: #e4e4e4 !important;
        color: #000000 !important;
      }
    }
    .el-menu-item {
      &:hover {
        background-color: #e4e4e4 !important;
        color: #000000 !important;
      }
    }
    .el-menu-item-option {
      &:hover {
        background-color: #e4e4e4 !important;
        color: #000000 !important;
      }
    }
  }
}
</style>