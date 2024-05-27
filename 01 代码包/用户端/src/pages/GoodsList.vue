<template>
  <div class="GoodsList">
    <div class="sortBox">
      <span class="title">排序：</span>
      <span
        :class="['normal', { selected: sortMode === 0 }]"
        @click="changeSortMode(0)"
        >默认</span
      >
      <span
        :class="['normal', { selected: sortMode !== 0 }]"
        @click="changeSortMode(3)"
      >
        价格
        <i
          :class="['iconfont', 'icon-arrows-4-7', { selected: sortMode === 1 }]"
        /><i
          :class="['iconfont', 'icon-arrows-4-7', { selected: sortMode === 2 }]"
        />
      </span>
    </div>
    <ul class="result">
      <GoodsItem
        v-for="(item, index) in sortedList"
        :style="{ marginRight: (index + 1) % 4 === 0 ? '0px' : '25px' }"
        :key="+item.id"
        :id="item.id"
        :img="pictureGet(item.urls[0])"
        :name="item.name"
        :price="item.price"
      />
      <p class="noGoods" v-if="goodsList.length <= 0">
        抱歉，没有找到相关商品~
      </p>
    </ul>
  </div>
</template>
  
<script>
import { getGoodsListApi, searchGoodsApi } from "../api/index";
import GoodsItem from "../components/GoodsItem";
import { BaseURL } from "../utils/request";

export default {
  name: "GoodsList",
  components: {
    GoodsItem,
  },
  computed: {
    isSearchPage() {
      return Number(this.typeId) === 0 ? true : false;
    },
    typeId() {
      return this.$route.params.typeId || 0;
    },
    keyword() {
      return this.$route.params.keyword || "";
    },
    sortedList() {
      let temList = this.goodsList.slice();
      if (this.sortMode === 0) {
        return temList;
      } else if (this.sortMode === 1) {
        return temList.sort((a, b) => {
          return a.price - b.price;
        });
      } else if (this.sortMode === 2) {
        return temList.sort((a, b) => {
          return b.price - a.price;
        });
      }
    },
  },
  data() {
    return {
      goodsList: [],
      sortMode: 0, //0默认，1价格升序，2价格降序
    };
  },

  methods: {
    getGoodsList(typeId) {
      getGoodsListApi(typeId).then(({ data }) => {
        if (data.code == 200) {
          this.goodsList = data.data;
        } else if (data.code === 500) {
          Message({
            message: "获取失败！",
            type: "error",
            offset: "140",
          });
        }
      });
    },
    searchGoods(keyword) {
      searchGoodsApi({ name: keyword })
        .then(({ data }) => {
          if (data.code == 200) {
            this.goodsList = data.data;
          } else if (data.code === 500) {
            Message({
              message: "获取失败！",
              offset: "140",
              type: "error",
            });
          }
        })
        .catch((err) => {
          console.log("error:searchGoods");
          console.log(err);
        });
    },
    changeSortMode(mode) {
      if (mode === 3) {
        this.sortMode = this.sortMode === 1 ? 2 : 1;
      } else {
        this.sortMode = 0;
      }
    },
    pictureGet(url) {
      return BaseURL + "/ImageFile/image/download" + "?url=" + url;
    },
  },

  mounted() {
    console.log(this.$route.params.typeId);
    //类别页
    if (!this.isSearchPage) {
      this.getGoodsList(this.$route.params.typeId);
    }
    //搜索结果页
    else {
      this.searchGoods(this.$route.params.keyword);
    }
  },

  watch: {
    $route(to, from) {
      this.sortMode = 0;
      if (!this.isSearchPage) {
        this.getGoodsList(this.$route.params.typeId);
      } else {
        this.searchGoods(to.params.keyword);
      }
    },
  },
};
</script>
  
<style scoped lang="less">
@import "../assets/css/var.less";

.GoodsList {
  .sortBox {
    width: 90%;
    border-top: 1px dotted @borderColor;
    border-bottom: 1px dotted @borderColor;
    margin: 30px auto;
    padding-bottom: 30px;
    padding-top: 30px;

    span {
      font-size: 13px;
    }

    .title {
      color: @fontDefaultColor;
      display: inline-block;
      margin-right: 10px;
    }

    .normal {
      cursor: pointer;
      color: @fontDefaultColor;
      display: inline-block;
      margin-right: 10px;
    }

    .iconfont {
      color: @fontDefaultColor;
      display: inline-block;
      position: relative;
      left: -3px;

      &:last-of-type {
        transform: rotate(180deg);
        left: -6px;
      }
    }

    .selected {
      color: @thirdColor;
    }
  }

  .noGoods {
    margin: 38px auto;
    text-align: center;
    color: @thirdColor;
    font-size: 18px;
  }
}
</style>