<template>
  <div class="Cart">
    <div v-if="cartList.length > 0">
      <div class="cartTableHeader">
        <span>商品信息</span>
        <span>单价</span>
        <span>数量</span>
        <span>小计</span>
        <span>交易操作</span>
      </div>
      <ul class="cartList">
        <li v-for="(item, index) in cartList" :key="index">
          <div class="orderDetail">
            <img :src="pictureGet(item.urls[0])" alt="商品图片" />
            <div class="goodsName">
              <p @click="navTo('/home/goods/' + item.id)">{{ item.name }}</p>
              <span>{{ item.commodityAttribute }}</span>
            </div>
            <span class="unitPrice">{{ "￥" + item.price }}</span>
            <span class="num">
              <NumberInput
                @changeHandle="numberChange(item.id)"
                :initNum="item.number"
                v-model="item.number"
                :min="1"
                :max="item.num"
              />
            </span>
            <!-- <input @change="numberChange(item.id)" type="text" v-model="item.temGoodsNum" min="1" class="numInput" /> -->
            <span class="amount">{{ "￥" + item.amount }}</span>
            <button @click="deleteCart(item.id, item.commodityAttribute)">
              删除
            </button>
          </div>
        </li>
      </ul>
      <div class="cartFooter">
        <span>应付金额：</span>
        <span class="total">{{ "￥" + totalAmount }}</span>
        <button @click="settleAccounts">下单</button>
      </div>
    </div>
    <p class="emptyTips" v-else>购物车还是空滴~</p>
  </div>
</template>
  
<script>
import { mapActions, mapState } from "vuex";
import {
  getCartApi,
  deleteCartApi,
  settleAccountsApi,
  addCartApi,
} from "../api/index";
import NumberInput from "../components/NumberInput";
import Cookies from "js-cookie";
import { BaseURL } from "../utils/request";
import { Loading, Message } from "element-ui";

export default {
  name: "Cart",
  components: {
    NumberInput,
  },
  computed: {
    totalAmount() {
      let amount = 0;
      this.cartList.map((item, index) => {
        amount += item.amount;
      });
      return amount;
    },
  },
  data() {
    return {
      tokenjudge: false,
      cartList: [],
    };
  },

  methods: {
    ...mapActions("Order", ["setFlag"]),
    pictureGet(url) {
      return BaseURL + "/ImageFile/image/download" + "?url=" + url;
    },
    getCart() {
      getCartApi().then(({ data }) => {
        if (data.code == 200) {
          this.cartList = data.data;
        } else if (data.code === 500) {
          // Message({
          //   message: "获取失败！",
          //   type: "error",
          //   offset: "140"
          // });
          location.reload();
        }
      });
    },
    numberChange(orderId) {
      this.cartList.map((item, index) => {
        if (orderId === item.id) {
          item.amount = item.number * item.price;
          console.log(item.num, item.price);
          const data = {
            commodityId: item.id,
            commodityAttribute: item.commodityAttribute,
            number: item.number,
            amount: item.amount,
          };
          addCartApi(data).then(({ data }) => {
            if (data.code == 200) {
            } else if (data.code === 500) {
              Message({
                message: "修改失败！",
                type: "error",
                offset: "140",
              });
            }
          });
        }
      });
    },
    deleteCart(Id, spec) {
      const data = {
        commodityId: Id,
        commodityAttribute: spec,
      };
      deleteCartApi(data).then(({ data }) => {
        if (data.code == 200) {
          this.getCart();
          Message({
            message: "删除成功！",
            type: "success",
            offset: "140",
          });
        } else if (data.code === 500) {
          Message({
            message: "删除失败！",
            type: "error",
            offset: "140",
          });
        }
      });
      // const res = deleteOrderApi(orderId);
      // res
      //   .then(() => {
      //     alert('删除订单成功！');
      //     this.cartList.map((item, index) => {
      //       if (item.id === orderId) {
      //         this.cartList.splice(index, 1);
      //       }
      //     })
      //   })
      //   .catch((e) => {
      //     alert(e);
      //   })
    },
    navTo(route) {
      this.$router.push(route);
    },
    settleAccounts() {
      this.$router.push({
        name: "submitOrder",
      });
      this.setFlag(1);
      sessionStorage.setItem("flag", 1);
    },
  },

  mounted() {
    this.getCart();
    if (Cookies.get("token") != null) this.tokenjudge = true;
    else this.tokenjudge = false;
  },
};
</script>
  
<style scoped lang="less">
@import "../assets/css/var.less";

.Cart {
  width: 100%;

  .emptyTips {
    width: 200px;
    text-align: center;
    display: block;
    margin: 30px auto;
    color: @thirdColor;
    font-size: 20px;
  }

  .cartTableHeader {
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

  .cartList {
    width: 100%;

    li {
      border: 1px solid @borderColor;
      border-top: none;
      font-size: 13px;

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
      }

      .orderDetail {
        width: 100%;
        padding: 10px;
        position: relative;
        overflow: hidden;

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
            cursor: pointer;
            line-height: 20px;

            &:hover {
              text-decoration: underline;
            }
          }

          span {
            color: @fontDefaultColor;
            display: block;
            margin-top: 10px;
          }
        }

        .unitPrice,
        .num,
        .amount {
          display: inline-block;
          vertical-align: top;
          width: 15%;
          height: 85px;
          line-height: 85px;
          text-align: center;
        }

        .NumberInput {
          position: relative;
          top: 25px;
        }

        button {
          position: absolute;
          right: 90px;
          bottom: 40px;
          width: 70px;
          height: 30px;
          border-radius: 3px;
          background-color: @falseColor;
          color: white;
          border: none;
        }
      }
    }
  }

  .cartFooter {
    width: 100%;
    height: 60px;
    line-height: 60px;
    padding: 0 10px;
    border: 1px solid @borderColor;
    border-top: none;
    background-color: #f5f5f5;
    position: relative;

    span {
      color: @fontDefaultColor;
      display: inline-block;
      vertical-align: top;
    }

    .total {
      color: @falseColor;
      font-size: 25px;
      font-weight: 600;
    }

    button {
      position: absolute;
      right: 0;
      top: 0;
      width: 100px;
      height: 100%;
      background-color: @thirdColor;
      border: none;
      color: white;
      font-size: 20px;
    }
  }
}
</style>