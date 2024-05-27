<template>
  <div class="CustomerOrder">
    <ul class="tagList">
      <li :class="{ selected: curIndex === index }" v-for="(item, index) in tagList" :key="'tag' + index"
        @click="changeIndex(index)">
        {{ item }}
      </li>
    </ul>
    <div class="orderBox">
      <div class="orderTableHeader">
        <span>商品</span>
        <span>单价</span>
        <span>数量</span>
        <span>实付款</span>
        <span>交易操作</span>
      </div>
      <ul class="orderList">
        <li v-for="(item, index) in TemporderList" :key="'order' + item.id">
          <div class="orderHeader">
            <span class="orderTime">{{ item.createTime }}</span>
            <span class="orderId">{{ "订单号：" + item.id }}</span>
            <span class="state">{{ tagList[item.status] }}</span>
            <!-- <button v-if="item.status == 1" @click="deleteOrder(item.id)">取消订单</button> -->
            <!-- <span class="deleteBtn" @click="deleteOrder(item.id)"><i class="iconfont icon-close" /></span> -->
          </div>
          <div class="orderDetail">
        <li v-for="(item1, index1) in item.commodityInOrdersList" :key="item1.id">
          <img :src="pictureGet(item1.urls[0])" alt="商品图片" />
          <div class="goodsName">
            <p @click="navTo('/home/goods/' + item1.id)">
              {{ item1.name }}
            </p>
            <span>{{ item1.commodityAttribute }}</span>
          </div>
          <span class="unitPrice">{{ "￥" + item1.price }}</span>
          <span class="num">{{ item1.number }}</span>
          <span class="amount">{{ "￥" + item1.amount }}</span>
          <span v-if="item.status == 1">待付款</span>
          <span v-else-if="item.status == 2">待收货</span>
          <span v-else-if="item.status == 3">已发货</span>
          <span v-else-if="item.status == 5">已取消</span>
          <span class="hasComment" v-else-if="item.status == 4 && item1.isCommented">已评价</span>
          <button v-show="commentJudge(item1.isCommented, item.status)" @click="showPopup(item.id, item1.id)">
            评价
          </button>
          <!-- v-if="item.status == 4 && !item1.isCommented" -->
        </li>
    </div>
    </li>
    </ul>
  </div>
  <el-dialog title="商品评价" :visible.sync="dialogVisible" width="30%" :before-close="handleClose" class="commentDialog">
    <div class="scoreBox">
      <span class="tips">评分：</span>
      <el-rate v-model="value1" :colors="colors" class="rating"> </el-rate>
    </div>
    <div class="form-floating">
      <textarea class="form-control" placeholder="Leave a comment here" id="floatingTextarea2" v-model="comment"
        style="height: 100px"></textarea>
      <label for="floatingTextarea2">评论</label>
    </div>
    <el-upload class="upload" :action="`${BaseURL}/ImageFile/image/upload/`" name="files" :on-success="handle_success"
      :headers="{ Authorization: `Bearer ${Cookies.get('token')}` }">
      <el-button size="small" type="primary" class="uploadButton">点击上传</el-button>
      <div slot="tip" class="el-upload__tip">
        只能上传jpg/png文件，且不超过500kb
      </div>
    </el-upload>
    <button @click="sendComment()">发表</button>
  </el-dialog>
  </div>
</template>
  
<script>
import {
  getAllOrderApi,
  sendCommentApi,
  getDetailOrderApi,
} from "../api/index";
import Popup from "../components/Popup";
import { BaseURL } from "../utils/request";
import { Message } from "element-ui";
import Cookies from "js-cookie";

export default {
  name: "CustomerOrder",
  components: {
    Popup,
  },
  computed: {
  },
  data() {
    return {
      value1: null,
      dialogVisible: false,
      Cookies: Cookies,
      fileList: [],
      colors: ["#99A9BF", "#F7BA2A", "#FF9900"],
      tagList: ["全部订单", "待付款", "待发货", "已发货", "已完成", "已取消"],
      curIndex: 0,
      orderList: [],
      orderDetailList: [],
      TemporderList: [],
      curOrderId: "",
      curCommentGoodsId: "",
      curCommentGoodsDetailId: "",
      curStar: 0,
      hasClickStar: false,
      comment: "",
      urlList: [],
      BaseURL: BaseURL,
    };
  },

  methods: {
    handle_success(response) {
      for(let i=0;i<response.data.length;i++){
        this.urlList.unshift(response.data[i]);
      }
      console.log(response.data);
      console.log(this.urlList);
    },
    handleClose(done) {
      this.$confirm("确认关闭？")
        .then((_) => {
          done();
        })
        .catch((_) => { });
    },

    pictureGet(url) {
      return BaseURL + "/ImageFile/image/download" + "?url=" + url;
    },
    commentJudge(isCommented, status) {
      if (status == 4 && isCommented == 0) {
        console.log(isCommented, status);
        return true;
      }
    },
    changeIndex(i) {
      if (i == 0) {
        this.TemporderList = this.orderDetailList;
        this.curIndex = i;
      } else {
        this.curIndex = i;
        this.getOrderByState(this.curIndex);
      }
    },
    navTo(route) {
      this.$router.push(route);
    },
    getAllOrder() {
      getAllOrderApi().then(({ data }) => {
        if (data.code == 200) {
          this.orderList = data.data;
          for (let i = 0; i < this.orderList.length; i++) {
            this.getDetailOrder(Number(this.orderList[i].id));
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

    getDetailOrder(id) {
      const data = {
        id: id,
      };
      getDetailOrderApi(data).then(({ data }) => {
        if (data.code == 200) {
          this.orderDetailList.unshift(data.data);
        } else if (data.code === 500) {
          Message({
            message: "获取失败！",
            type: "error",
            offset: "140",
          });
        }
      });
    },

    getOrderByState(state) {
      this.TemporderList = this.orderDetailList.filter((item) => {
        return String(item.status) == String(state);
      });
    },

    showPopup(orderId, goodsId) {
      console.log(this.value1, this.fileList);
      this.dialogVisible = true;
      this.curCommentGoodsId = goodsId;
      this.curOrderId = orderId;
    },
    sendComment() {
      if (this.value1 <= 0 || this.comment == "") {
        Message({
          message: "评分和评价不能为空！",
          type: "error",
        });
        return;
      }
      const data = {
        commodityId: this.curCommentGoodsId,
        orderId: this.curOrderId,
        score: this.value1,
        content: this.comment,
        urls: this.urlList,
      };
      sendCommentApi(data).then(({ data }) => {
        if (data.code == 200) {
          this.dialogVisible = false;
          this.getAllOrder();
          this.TemporderList = this.orderDetailList;
          location.reload();
          this.urlList = [];
          this.comment = '';
          this.value1 = null;
          Message({
            message: "评论成功！",
            type: "success",
          });
        } else if (data.code === 500) {
          Message({
            message: "评论失败！",
            type: "error",
          });
        }
      });
    },
    //   setCurStar(star) {
    //     if (this.hasClickStar) {
    //       return;
    //     }
    //     this.curStar = star;
    //   },
    //   confirmStar(star) {
    //     this.curStar = star;
    //     this.hasClickStar = true;
    //   }
  },

  mounted() {
    this.getAllOrder();
    this.TemporderList = this.orderDetailList;
  },
};
</script>
  
<style scoped lang="less">
@import "../assets/css/var.less";

.CustomerOrder {
  .tagList {
    li {
      text-align: center;
      display: inline-block;
      font-weight: 550;
      font-size: 18px;
      border-bottom: 2px solid @borderColor;
      cursor: pointer;
      padding: 10px 20px;
      position: relative;

      &:after {
        top: 12px;
        right: 0;
        position: absolute;
        content: "";
        width: 1px;
        height: 15px;
        background-color: @borderColor;
      }

      &:last-child {
        &:after {
          display: none;
        }
      }
    }

    .selected {
      color: @thirdColor;
      border-bottom: 2px solid @thirdColor;
    }
  }

  .orderBox {
    width: 100%;
    margin-top: 20px;

    .orderTableHeader {
      width: 100%;
      height: 40px;
      background-color: #f5f5f5;
      border: 1px solid @borderColor;
      color: @fontDefaultColor;
      font-size: 14px;
      line-height: 40px;

      span {
        display: inline-block;
        width: 14%;

        &:first-child {
          width: 40%;
          text-align: center;
        }
      }
    }

    .orderList {
      width: 100%;

      li {
        border: 1px solid @borderColor;
        font-size: 13px;
        margin-top: 10px;

        .orderHeader {
          background-color: #f1f1f1;
          height: 40px;
          line-height: 40px;
          padding: 0 5px;

          .orderTime {
            font-weight: 600;
          }

          .orderId,
          .state {
            margin-left: 10px;
          }

          .deleteBtn {
            float: right;
            cursor: pointer;

            i {}
          }
        }

        .orderDetail {
          width: 100%;
          padding: 10px;
          position: relative;
          overflow: hidden;

          span{
            display: inline-block;
            vertical-align: top;
            width: 15%;
            height: 65px;
            line-height: 65px;
            text-align: center;
          }


          img {
            width: 84px;
            height: 84px;
            display: inline-block;
          }

          .goodsName {
            display: inline-block;
            margin-left: 5px;
            width: 230px;
            vertical-align: top;

            p {
              transform: translateX(10px);
              cursor: pointer;
              line-height: 20px;

              &:hover {
                text-decoration: underline;
              }
            }

            span {
              width:100px;
              color: @fontDefaultColor;
              display: block;
              margin-top: 5px;
              transform: translateX(-10px);
            }
          }

          .unitPrice,
          .num,
          .amount,
          .hasComment {
            display: inline-block;
            vertical-align: top;
            width: 15%;
            height: 65px;
            line-height: 65px;
            text-align: center;
          }

          button {
            transform: translateX(33px);
            right: 80px;
            bottom: 40px;
            width: 70px;
            height: 30px;
            border-radius: 3px;
            background-color: @thirdColor;
            color: white;
            border: none;
          }
        }
      }
    }
  }

  .commentDialog {
    height: 1000px;
    padding: 10px;

    .scoreBox {
      width: 100%;
      height: 30px;
      position: relative;
      line-height: 50px;
      text-align: left;

      .tips {
        font-size: 12px;
        vertical-align: middle;
        display: inline-block;
      }

      .rating {
        vertical-align: middle;
        display: inline-block;
      }

      /deep/.upload {
        height: 50px !important;
        vertical-align: middle;
        line-height: 20px !important;

        display: inline-block;
      }

      /deep/.uploadButton {
        height: 20px !important;
        line-height: 0px !important;
      }

      i {
        cursor: pointer;
        vertical-align: middle;
        display: inline-block;
        font-size: 25px;
        margin-right: 5px;
        -webkit-text-stroke: 1px #f9bd4f;
      }
    }

    textarea {
      margin-top: 10px;
    }

    button {
      display: block;
      margin: 10px auto;
      width: 70px;
      height: 30px;
      border-radius: 3px;
      background-color: #313541;
      color: white;
      border: none;
    }
  }
}
</style>