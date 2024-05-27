<template>
  <div class="MallShow">
    <FixedNav v-show="navShouldFixed">
      <div slot="navContent" class="container fixedNavContainer">
        <h3 class="fixedLeft" @click="navTo('/mall/show/index')">智能购物谷</h3>
        <ul class="fixedRight">
          <li
            v-for="item in typeList"
            :key="'type' + item.id"
            :class="{ selected: judgeCurPath(item.id) }"
            @click="selectType(item.id)"
          >
            {{ item.name }}
          </li>
        </ul>
      </div>
    </FixedNav>
    <div class="logo">
      <img src="../assets/images/index1.gif" />
      <div class="searchBox">
        <input
          type="text"
          placeholder="请输入商品关键字"
          v-model="searchText"
        />
        <svg
          xmlns="http://www.w3.org/2000/svg"
          width="16"
          height="16"
          fill="currentColor"
          class="bi bi-search"
          @click="searchConfirm"
          viewBox="0 0 16 16"
        >
          <path
            d="M11.742 10.344a6.5 6.5 0 1 0-1.397 1.398h-.001c.03.04.062.078.098.115l3.85 3.85a1 1 0 0 0 1.415-1.414l-3.85-3.85a1.007 1.007 0 0 0-.115-.1zM12 6.5a5.5 5.5 0 1 1-11 0 5.5 5.5 0 0 1 11 0z"
          />
        </svg>
      </div>
    </div>
    <ul ref="typeList" class="typeList">
      <li
        v-for="item in typeList"
        :key="'type' + item.id"
        :class="{ selected: judgeCurPath(item.id) }"
        @click="selectType(item.id)"
      >
        {{ item.name }}
      </li>
    </ul>
    <router-view></router-view>
  </div>
</template>
  
<script>
import { getTypesApi, getGoodsListApi } from "../api/index";
import TipsInput from "../components/TipsInput";
import FixedNav from "../components/FixedNav";
import { Message } from "element-ui";

export default {
  name: "MallShow",
  components: {
    TipsInput,
    FixedNav,
  },
  computed: {
    curPath() {
      return this.$route.path;
    },
  },
  data() {
    return {
      typeList: [
        {
          id: "-1",
          name: "首页",
        },
        {
          id: "1",
          name: "食品",
        },
        {
          id: "2",
          name: "家电",
        },
      ],
      searchText: "",
      tips: ["aa", "bb", "cc"],
      navShouldFixed: false,
    };
  },

  methods: {
    navTo(route) {
      this.$router.push(route);
    },
    judgeCurPath(typeId) {
      if (typeId === -1) {
        if (this.curPath.indexOf("/show/index") > -1) {
          return true;
        } else {
          return false;
        }
      } else {
        if (this.curPath.indexOf(`/show/goodsList/${typeId}/all`) > -1) {
          return true;
        } else {
          return false;
        }
      }
    },
    selectType(typeId) {
      //首页
      if (typeId === -1) {
        this.navTo("/home/show/index");
      } else {
        this.navTo("/home/show/goodsList/" + typeId + "/all");
      }
    },
    searchTip(tip) {
      alert(tip);
    },
    searchTextChange(text) {},
    searchConfirm() {
      if (this.searchText.trim().length <= 0) {
        Message({
          type: "error",
          message: "输入不能为空",
          offset: "140",
        });
      } else {
        this.navTo(`/home/show/goodsList/${this.searchText}`);
      }
    },
    scrollHandle() {
      const top = this.$refs.typeList.getBoundingClientRect().top;
      //还未到顶
      if (top > 0) {
        this.navShouldFixed = false;
      }
      //已经到顶
      else {
        this.navShouldFixed = true;
      }
    },
  },

  created() {
    const data = {
      fatherId: -1,
    };
    getTypesApi(data).then(({ data }) => {
      if (data.code == 200) {
        this.typeList = data.data;
        this.typeList = this.typeList.filter((item) => {
          return String(item.id) !== String(-1);
        });
        this.typeList.unshift({
          id: -1,
          fatherId: -1,
          name: "首页",
        });
      }
      // else if (data.code === 500) {
      //   Message({
      //     message: "获取失败！",
      //     type: "error",
      //            offset: "140",
      //   });
      // }
    });

    //监听滚动事件
    document.addEventListener("scroll", this.scrollHandle, false);
  },

  destroyed() {
    document.removeEventListener("scroll", this.scrollHandle, false);
  },
  watch: {
    searchText(newVal, oldVal) {
      this.searchTextChange(newVal);
    },
  },
};
</script>
  
<style scoped lang="less">
@import "../assets/css/var.less";

.MallShow {
  width: 100%;

  .logo {
    display: block;
    margin: -10px auto 30px;
    text-align: right;
    user-select: none;
    vertical-align: middle;

    img {
      display: inline-block;
      width: 33.3%;
      vertical-align: middle;
    }

    .searchBox {
      display: inline-block;
      width: 33.3%;
      vertical-align: middle;
      text-align: left;
      input {
        padding-left: 5px;
        margin-top: 20px;
        margin-left: 100px;
        width: 300px;
        border: none;
        border-bottom: 1px solid rgb(184, 168, 132);
        &:focus {
          outline: none !important;
        }
      }

      .bi-search{
        cursor: pointer;
      }

    }
  }

  .typeList {
    width: 100%;
    text-align: center;
    background-color: white;

    li {
      display: inline-block;
      width: 60px;
      margin: 0 50px;
      text-align: center;
      height: 30px;
      line-height: 20px;
      font-weight: 600;
      font-size: 14px;
      cursor: pointer;
    }

    .selected {
      color: @thirdColor;
      border-bottom: 3px solid @thirdColor;
    }
  }

  .fixedNavContainer {
    text-align: left;

    .fixedLeft {
      display: inline-block;
      vertical-align: middle;
      width: 15%;
      height: 100%;
      font-size: 30px;
      color: @thirdColor;
      user-select: none;
      line-height: 64px;
      text-align: center;
      cursor: pointer;
    }

    .fixedRight {
      width: 70%;
      height: 100%;
      display: inline-block;
      vertical-align: middle;

      li {
        display: inline-block;
        width: 60px;
        margin: 0 30px;
        text-align: center;
        height: 30px;
        line-height: 20px;
        font-weight: 600;
        font-size: 14px;
        cursor: pointer;
        position: relative;
        top: 4px;
      }

      .selected {
        color: @thirdColor;
        border-bottom: 3px solid @thirdColor;
      }
    }
  }
}
</style>