<template>
  <div class="GoodsDetail">
    <div class="content">
      <div class="goodsInfo">
        <div class="item-detail-left" style="display: inline-block;transform: translate(150px,0);">
          <div class="item-detail-big-img">
            <!-- class="infoLeft" -->
            <img :src="pictureGet(goodsImg[imgIndex])" alt="商品图片" style="transform: translate(0, 25px);"/>
          </div>

          <!-- <div  id="dplayer"></div> -->
          <div class="item-detail-img-row">
            <div class="item-detail-img-small" @mouseover="imgIndex = index" v-for="(item, index) in goodsImg"
              :key="index">
              <img :src="pictureGet(item)" />
            </div>
          </div>
        </div>
        <div class="infoRight" style="display: inline-block">
          <div class="infoBox">
            <h3 class="name">{{ goodsName }}</h3>
          </div>
          <div class="infoBox">
            <p>{{ goodsDesc }}</p>
          </div>
          <div class="infoBox">
            <h3 class="price">{{ "¥" + goodsPrice }}</h3>
          </div>
          <div class="infoBox">
            <span>规格：</span>
            <li v-for="(item, index) in specs" :key="index">
              <span>{{ item.name }}</span>
              <Radio v-for="(item1, index1) in item.values" :key="item1" :initVal="item.values[0]" :radioName="item.name"
                :radioVal="item1" @input="valueChange(item1, item.name)">
                <span class="tips" slot="tips">{{ item1 }}</span>
                <!-- + ' 还剩' + item.stockNum + '件' -->
              </Radio>
            </li>
          </div>
          <div class="infoBox">
            <span>数量：</span>
            <NumberInput v-model="num" :min="1" :max="Number(goodsNum)" />
          </div>
          <button class="buyBtn" @click="buy">立即购买</button>
          <button @click="addToCart">加入购物车</button>
        </div>
      </div>
      <section class="msgBox leftContainer">
        <ul class="tagList">
          <li :class="{ selected: curIndex === index }" v-for="(item, index) in tagList" :key="'tag' + index"
            @click="changeIndex(index)">
            {{ item }}
          </li>
        </ul>
        <div class="commentBody" v-if="curIndex === 0">
          <div v-if="pageData.total > 0" class="rateBox">
            <span>平均得分</span>
            <span class="rate">{{ rate }}</span>
          </div>
          <div v-if="commentList.length != 0">
            <ul class="commentList">
              <li v-for="(item, index) in commentList" :key="'comment' + index">
                <div class="userInfo">
                  <img :src="pictureGet(item.url)" alt="headimg" />
                  <span>{{ item.username }}</span>
                </div>
                <div class="commentInfo">
                  <el-rate v-model="item.score" disabled show-score text-color="#ff9900" score-template="{value}">
                  </el-rate>
                  <p class="comment">{{ item.content }}</p>
                  <p class="time">{{ item.createTime }}</p>
                  <div class="commentImages">
                    <div class="commentimg" v-for="(item1, index1) in item.urls" :key="'img' + index1">
                      <img :src="pictureGet(item1)" />
                    </div>
                  </div>
                  <button type="button" class="btn btn-light" @click="deleteComment(item.username, item.id)">删除评论</button>
                </div>
                <div v-if="item.reply != null">
                  <span>商家回复：</span>
                  <p class="comment">{{ item.reply }}</p>
                </div>
              </li>
            </ul>
          </div>
          <div class="noComment" v-else>暂时还没有评论~</div>
          <el-pagination background layout="prev, pager, next" @current-change="handlePageChange"
            :page-size="pageData.limit" :current-page.sync="pageData.page" :total="pageData.total" v-if="pageData.total !== 0">
          </el-pagination>
        </div>
      </section>
      <section class="typeGoods rightContainer">
        <div class="title">相似商品</div>
        <ul class="list" style="display: flex;">
          <GoodsItem  v-for="(item, index) in filterList.slice(0, 3)" :key="item.id" :id="item.id"
            :img="pictureGet(item.urls[0])" :name="item.name" :price="item.price" />
        </ul>
      </section>
    </div>
  </div>
</template>
  
<script>
import { mapState } from 'vuex';
import { getGoodsInfoApi, addCartFromDetailApi, getCommentApi, getGoodsListApi, deleteCommentApi, getAverageScoreApi } from '../api/index';
import NumberInput from '../components/NumberInput';
import Radio from '../components/Radio';
import GoodsItem from '../components/GoodsItem';
import { BaseURL } from '../utils/request';
import Cookies from 'js-cookie';
import { Message } from "element-ui";
import { mapActions } from "vuex";
export default {
  name: "GoodsDetail",
  components: {
    NumberInput,
    Radio,
    GoodsItem,
  },
  computed: {
    ...mapState("CustomerInfo", ["user"]),
    // id() {
    //   return this.$route.params.id;
    // },
    // goodsPrice() {
    //   let unitPrice = 0;
    //   this.specs.map((item, index) => {
    //     if (item.id === this.temSpecId) {
    //       unitPrice = Number(item.unitPrice);
    //     }
    //   })
    //   return (this.num * unitPrice);
    // },
    // temStockNum() {
    //   let stockNum = 0;
    //   this.specs.map((item, index) => {
    //     if (item.id === this.temSpecId) {
    //       stockNum = Number(item.stockNum);
    //     }
    //   })
    //   return stockNum;
    // },
    // filterList() {
    //   return this.goodsList.filter((item) => {
    //     return String(item.id) !== String(this.id);
    //   })
    // }
  },
  data() {
    return {
      pageData: {
        page: 1,
        limit: 5,
        total: 100,
      },
      tokenjudge: false,
      imgIndex: 0,
      goodsImg: [],
      goodsName: "",
      goodsDesc: "",
      goodsPrice: "",
      goodsId: "",
      goodsNum: "",
      specs: [],
      typeId: "",
      temSpec: [],
      num: 1,
      msgList: [],
      askContent: '',
      tagList: ['评价'],
      curIndex: 0,
      filterList: [],
      rate: "",
      commentList: [],
      goodsList: [],
      isCollected: false, // 是否收藏
    };
  },

  methods: {
    getAverageScore(id) {
      const data = {
        id: id,
      }
      getAverageScoreApi(data).then(({ data }) => {
        if (data.code == 200) {
          this.rate = data.data;
        } else if (data.code === 500) {
          Message({
            message: "获取失败！",
            type: "error",
          });
        }
      });
    },
    deleteComment(username, id) {
      if (username != localStorage.getItem("userEmail")) {
        Message({
          message: "无法删除非本人评论！",
          type: "error",
        });
        return;
      }
      const data = {
        id: id,
      }
      deleteCommentApi(data).then(({ data }) => {
        if (data.code == 200) {
          Message({
            message: "删除成功！",
            type: "success",
          });
          this.getComment(this.$route.params.id);
        } else if (data.code === 500) {
          Message({
            message: "删除失败！",
            type: "error",
          });
        }
      });
    },
    getComment(id) {
      const data = {
        commodityId: id,
        page: this.pageData.page,
        limit: this.pageData.limit,
      }
      getCommentApi(data).then(({ data }) => {
        if (data.code == 200) {
          this.commentList = data.data.records;
          this.pageData.page = data.data.current;
          this.pageData.limit = data.data.size;
          this.pageData.total = data.data.total;
          console.log(this.commentList);
        } else if (data.code === 500) {
          Message({
            message: "获取失败！",
            type: "error",
          });
        }
      });
    },
    handlePageChange(size) {
      this.pageData.page = size;
      this.getComment(this.$route.params.id);
    },
    ...mapActions("Order", ["setFlag", "setOrder"]),
    changeIndex(i) {
      this.curIndex = i;
    },

    valueChange(value, name) {
      for (let i = 0; i < this.temSpec.length; i++) {
        if (name == this.temSpec[i].name) {
          this.temSpec[i].value = value;
        }
      }
    },

    getGoodsInfo(id) {
      getGoodsInfoApi({ id: id }).then(({ data }) => {
        if (data.code == 200) {
          this.goodsId = data.data.id;
          this.goodsImg = data.data.urls;
          this.goodsName = data.data.name;
          this.goodsPrice = data.data.price;
          this.goodsDesc = data.data.description;
          this.specs = data.data.commodityAttributeList;
          this.typeId = data.data.categoryId;
          this.temSpecId = data.data.commodityAttributeList[0].id;
          this.goodsNum = data.data.num;
          this.getTypeGoodsList(data.data.categoryId);
          for (let i = 0; i < this.specs.length; i++) {
            this.temSpec.unshift({
              name: this.specs[i].name,
              value: this.specs[i].values[0],
            });
          }
        } else if (data.code === 500) {
          Message({
            message: "获取失败！",
            type: "error",
            offset: "140",
          });
        }
      });
    },

    getGoodsMsg(id) {
      getGoodsMsgApi(id).then(({ data }) => {
        if (data.code == 200) {
          this.msgList = data;
        } else if (data.code === 500) {
          Message({
            message: "获取失败！",
            offset: "140",

            type: "error",
          });
        }
      });
    },

    pictureGet(url) {
      return BaseURL + "/ImageFile/image/download" + "?url=" + url;
    },

    postAsk() {
      // if (this.askContent.trim().length <= 0) {
      //   return;
      // }
      const res = askGoodsMsgApi({
        token: this.tokenjudge,
        msg: this.askContent,
        goodsId: this.id,
      });
      res
        .then(() => {
          let time = new Date();
          this.msgList.unshift({
            id: "new",
            content: this.askContent,
            state: 0,
            asker: this.clientName,
            time: time.getMonth() + 1 + "-" + time.getDate(),
            reply: {},
          });
          this.askContent = "";
        })
        .catch((e) => {
          alert(e);
        });
    },

    addToCart() {
      if (!this.tokenjudge) {
        Message({
          message: "请先登录！",
          type: "error",
          offset: "140",
        });
        return;
      }
      let finalSpec = [];
      for (let i = 0; i < this.temSpec.length; i++) {
        finalSpec.push(this.temSpec[i].value);
      }
      const data = {
        commodityId: Number(this.goodsId),
        commodityAttribute: finalSpec.join(","),
        number: this.num,
        amount: this.num * Number(this.goodsPrice),
      };
      addCartFromDetailApi(data).then(({ data }) => {
        if (data.code == 200) {
          Message({
            message: "添加成功！",
            offset: "140",

            type: "success",
          });
        } else if (data.code === 500) {
          Message({
            message: "添加失败！",
            type: "error",
            offset: "140",
          });
        }
      });
    },

    buy() {
      this.$router.push({
        name: "submitOrder",
      });
      let finalSpec = [];
      for (let i = 0; i < this.temSpec.length; i++) {
        finalSpec.push(this.temSpec[i].value);
      }
      const data = {
        commodityId: Number(this.goodsId),
        commodityAttribute: finalSpec.join(","),
        number: this.num,
        amount: this.num * Number(this.goodsPrice),
        goodsImg: this.goodsImg,
        goodsDesc: this.goodsDesc,
      };
      sessionStorage.setItem("amount", data.amount);
      sessionStorage.setItem("commodityAttribute", data.commodityAttribute);
      sessionStorage.setItem("number", data.number);
      sessionStorage.setItem("commodityId", data.commodityId);
      sessionStorage.setItem("goodsImg", data.goodsImg);
      sessionStorage.setItem("goodsDesc", data.goodsDesc);
      this.setFlag(0);
      this.setOrder(data);
      sessionStorage.setItem("flag", 0);
    },

    getTypeGoodsList(typeId) {
      getGoodsListApi(typeId).then(({ data }) => {
        if (data.code == 200) {
          this.goodsList = data.data;
          this.filterList = this.goodsList.filter((item) => {
            return String(item.id) !== String(this.$route.params.id);
          });
          console.log(this.filterList);
        } else if (data.code === 500) {
          Message({
            message: "获取失败！",
            offset: "140",

            type: "error",
          });
        }
      });
    },
  },
  mounted() {
    this.getGoodsInfo(this.$route.params.id);
    this.getAverageScore(this.$route.params.id);
    if (Cookies.get('token') != null)
      this.tokenjudge = true;
    else this.tokenjudge = false;
    // this.getGoodsMsg(this.$route.params.id);
    this.getComment(this.$route.params.id);
  },

  watch: {
    $route(to, from) {
      this.getGoodsInfo(to.params.id);
      this.getGoodsMsg(to.params.id);
      this.getComment(to.params.id);
    },
  },
};
</script>
  
<style scoped lang="less">
@import "../assets/css/var.less";

.GoodsDetail {
  .content {
    width: 100%;
    padding-top: 20px;

    .goodsInfo {
      width: 100%;
      overflow: hidden;

      .item-detail-big-img img {
        width: 100%;
      }

      .item-detail-img-row {
        margin-top: 15px;
        display: flex;
      }

      .item-detail-img-small {
        width: 68px;
        height: 68px;
        box-shadow: 0px 0px 8px #ccc;
        cursor: pointer;
        margin-left: 5px;
      }

      .item-detail-img-small img {
        height: 100%;
        width: 100%;
      }

      .item-detail-left {
        width: 350px;

        margin-right: 30px;
      }

      .item-detail-big-img {
        height: 350px;
        width: 220px;
        box-shadow: 0px 0px 8px @border_color;
        cursor: pointer;
      }

      .infoLeft {
        display: inline-block;
        width: 400px;
        height: 400px;
        float: left;
      }

      .infoRight {
        display: inline-block;
        float: right;
        width: 700px;

        .infoBox {
          margin-bottom: 30px;

          .name {
            font-size: 20px;
          }

          p {
            color: @fontDefaultColor;
            font-size: 15px;
          }

          .price {
            font-size: 35px;
            color: @falseColor;
          }

          span {
            color: @fontDefaultColor;
            font-size: 13px;
            display: inline-block;
            width: 50px;
          }

          .tips {
            width: auto;
            margin: 0 20px 0 5px;
          }

          .NumberInput {
            display: inline-block;
            vertical-align: middle;
          }
        }

        button {
          background-color: #b4a078;
          width: 170px;
          height: 50px;
          color: white;
          border: none;
          font-size: 19px;
          margin-right: 20px;
          margin-top: 10px;

          &:hover {
            opacity: 0.8;
          }
        }

        .buyBtn {
          border: 1px solid #b4a078;
          color: #b4a078;
          background-color: #f5f3ef;
        }
      }
    }

    .msgBox {
      margin: 50px 0;
      border: 1px solid @borderColor;
      padding-top: 0;

      .tagList {
        width: 100%;
        height: 40px;
        border-bottom: 1px solid @borderColor;
        background-color: #f5f5f5;

        li {
          width: 170px;
          height: 42px;
          position: relative;
          top: -2px;
          display: inline-block;
          text-align: center;
          line-height: 40px;
          font-size: 13px;
          cursor: pointer;

          &:hover {
            color: @thirdColor;
          }
        }

        .selected {
          background-color: white;
          border-top: 4px solid @thirdColor;
        }
      }

      .commentBody {
        padding: 20px;
        min-height: 300px;

        .rateBox {
          margin-bottom: 10px;

          span {
            color: @fontDefaultColor;
            display: inline-block;
            margin-right: 10px;
          }

          .rate {
            color: @falseColor;
            font-weight: 600;
            font-size: 30px;
          }
        }

        .commentList {
          width: 100%;

          li {
            width: 100%;
            display: block;
            margin: 0 auto;
            border-bottom: 1px solid @borderColor;
            padding: 20px 0;

            .userInfo {
              transform: translateX(5%);
              width: 80px;
              height: 30px;
              display: flex;
              text-align: center;

              img {
                margin: auto;
                display: block;
                width: 30px;
                height: 30px;
                border-radius: 50%;
                margin-bottom: 6px;
              }

              span {
                font-size: 13px;
                color: @fontDefaultColor;
              }
            }

            .commentInfo {
              transform: translateX(5%);
              display: inline-block;
              vertical-align: top;
              width: 100%;


              .specName,
              .time {
                color: @fontDefaultColor;
                font-size: 13px;
                margin-top: 10px;
              }

              .comment {
                margin-top: 10px;
              }

              button {
                float: right;
                margin-right: 10%;
              }

              .commentImages {
                display: flex;
              }

              .commentimg {
                height: 120px;
                box-shadow: 0px 0px 8px #ccc;
                margin-left: 5px;
              }

              .commentimg img {
                height: 100%;
              }
            }
          }
        }

        .noComment {
          width: 100%;
          text-align: center;
          color: @thirdColor;
          padding-top: 30px;
        }
      }

      .msgBody {
        padding: 20px;
        min-height: 300px;

        .inputBox {
          margin-top: 20px;

          textarea {
            width: 88%;
            height: 50px;
            padding: 5px;
            border: 2px solid @borderColor;
            display: inline-block;
            overflow: hidden;
          }

          button,
          .banAsk {
            float: right;
            width: 10%;
            height: 50px;
            position: relative;
            display: inline-block;
            overflow: hidden;
            background-color: white;
            border: 2px solid @fontShallowColor;
            color: @fontDefaultColor;
          }

          .banAsk {
            background-color: @fontShallowColor;
            text-align: center;
            font-size: 12px;
            line-height: 50px;
            color: white;
            user-select: none;
          }
        }

        .msgList {
          margin-top: 20px;

          li {
            border-bottom: 1px solid @borderColor;
            padding: 10px 0;

            .ask,
            .answer {
              margin: 8px 0;
              width: 100%;

              .note {
                display: inline-block;
                color: white;
                text-align: center;
                width: 20px;
                height: 20px;
                border-radius: 50%;
                font-size: 10px;
                line-height: 20px;
              }

              .text {
                font-size: 14px;
              }

              .tipsInfo {
                float: right;
                font-size: 14px;
                color: @fontDefaultColor;
              }
            }

            .ask {
              .note {
                background-color: @falseColor;
              }
            }

            .answer {
              .note {
                background-color: @mainColor;
              }
            }
          }
        }
      }
    }

    .typeGoods {
      margin: 50px 0;
      border: 1px solid @borderColor;
      padding-top: 0;

      .title {
        text-align: center;
        width: 100%;
        height: 40px;
        line-height: 40px;
        background-color: #f5f5f5;
        font-weight: 600;
        border-bottom: 1px solid @borderColor;
      }

      .list {
        .GoodsItem {
          display: block;
          border-bottom: 1px dotted @borderColor;
          margin: 0 auto;
        }
      }
    }
  }

  .ban {
    user-select: none;
    cursor: not-allowed;
  }
}
</style>