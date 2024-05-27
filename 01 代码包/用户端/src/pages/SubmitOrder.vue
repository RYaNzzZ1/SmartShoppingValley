<template>
  <div id="submitOrder">
    <header>
      <img src="../assets/images/logo.png" alt="" />
      <span>结算页</span>
    </header>
    <div class="submitBody">
      <span>收货人信息</span>
      <a href="#" @click="manageAddress">管理收货人地址</a>
      <hr />
      <div class="addressContainer">
        <div
          class="card"
          style="width: 18rem"
          v-for="(item, index) in addressBook"
          :key="index"
          @click="chooseAddress(item)"
        >
          <div class="card-body">
            <div class="consigneeLine">
              <span>{{ item.consignee }}</span>
              <el-tag :type="item.sex === 1 ? '' : 'warning'" size="small">{{
                item.sex === 1 ? "先生" : "女士"
              }}</el-tag>
              <el-tag v-if="item.isDefault" type="danger" size="small"
                >默认</el-tag
              >
              <i
                v-if="chosenAddress.id === item.id"
                class="el-icon-circle-check"
              ></i>
            </div>
            <div class="phoneLine">
              {{ item.phone }}
            </div>
            <div class="addressLine">
              {{ item.address }}
            </div>
          </div>
        </div>
        <div
          class="card addAddressCard"
          style="width: 18rem"
          @click="manageAddress"
        >
          <div class="card-body">添加地址 +</div>
        </div>
      </div>

      <div class="goodsDetail">
        <span>订单详情</span>
        <a href="#" @click="backToCart">返回购物车</a>
        <hr />

        <div class="goodsDetailContainer">
          <!-- 直接结算 -->
          <div v-if="flag == 0" class="card">
            <div class="card-body">
              <div class="goodsDetailBox">
                <img
                  :src="`${BaseURL}/ImageFile/image/download?url=${order.urls[0]}`"
                />
                <div class="infoBox">
                  <p>
                    {{ order.name }}
                  </p>
                  <p>
                    {{ order.description || "暂无商品描述" }}
                  </p>
                  <p>规格：{{ order.commodityAttribute }}</p>
                </div>
                <div class="numBox">
                  <span> {{ "数量： " + order.number }}</span>
                  <span>{{ "金额：  ￥" + order.amount }}</span>
                </div>
              </div>
            </div>
          </div>
          <!-- 购物车结算 -->
          <template v-if="flag == 1">
            <div v-for="(item, index) in cart" :key="index" class="card">
              <div class="card-body">
                <div class="goodsDetailBox">
                  <img
                    :src="`${BaseURL}/ImageFile/image/download?url=${item.urls[0]}`"
                  />
                  <div class="infoBox">
                    <p>
                      {{ item.name }}
                    </p>
                    <p>
                      {{ item.description || "暂无商品描述" }}
                    </p>
                    <p>规格：{{ item.commodityAttribute }}</p>
                  </div>
                  <div class="numBox">
                    <span> {{ "数量： " + item.number }}</span>
                    <span>{{ "金额：  ￥" + item.amount }}</span>
                  </div>
                </div>
              </div>
            </div>
          </template>
        </div>
      </div>

      <div class="paymentContainer">
        <span>支付方式</span>
        <hr />
        <div class="paymentBox">
          <div class="card" style="width: 7rem" @click="changePayment(1)">
            <div class="card-body">
              微信
              <i v-if="paymentFlag === 1" class="el-icon-circle-check"></i>
            </div>
          </div>
          <div class="card" style="width: 7rem" @click="changePayment(2)">
            <div class="card-body">
              支付宝
              <i v-if="paymentFlag === 2" class="el-icon-circle-check"></i>
            </div>
          </div>
        </div>
      </div>

      <div class="couponContainer">
        <span>优惠券</span>
        <hr />
        <el-collapse accordion>
          <el-collapse-item title="查看已领取优惠券" name="1">
            <template v-if="showCoupon">
              <div
                class="coupon"
                v-for="(item, index) in receivedCoupons"
                :key="index"
              >
                <Coupon
                  :conditions="item.conditions"
                  :amount="item.amount"
                  :id="item.id"
                >
                </Coupon>
                <div class="btn">
                  <button @click="handleClick(item)">{{ item.title }}</button>
                </div>
              </div>
            </template>
          </el-collapse-item>
          <el-collapse-item title="查看可领取优惠券" name="2">
            <template v-if="showCoupon">
              <div class="coupon" v-for="(item, index) in coupons" :key="index">
                <Coupon
                  :conditions="item.conditions"
                  :amount="item.amount"
                  :id="item.id"
                >
                </Coupon>
                <div class="btn">
                  <button @click="handleClick(item)">{{ item.title }}</button>
                </div>
              </div>
            </template>
          </el-collapse-item>
        </el-collapse>
      </div>

      <div class="submitOrderContainer">
        <span>提交</span>
        <hr />
        <div class="goodsDetailContainer">
          <div class="card">
            <div class="card-body">
              <div class="submitBox">
                <p>
                  应付金额：<span class="finalAmount"> ￥{{ sum }}</span>
                </p>
                <p>
                  寄送至：{{ chosenAddress.address }}&nbsp;&nbsp;&nbsp;
                  收货人：{{ chosenAddress.consignee }}&nbsp;&nbsp;&nbsp;{{
                    chosenAddress.phone
                  }}
                </p>
                <p></p>
              </div>
            </div>
          </div>
          <div class="submitBtnBar">
            <button class="submitBtn" @click="submitOrder">提交订单</button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import {
  getAddressApi,
  getCartApi,
  submitOrderApi_1,
  submitOrderApi_0,
} from "../api";
import { mapState, mapActions } from "vuex";
import { BaseURL } from "../utils/request.js";
import Coupon from "../components/Coupon.vue";
import { Message } from "element-ui";
export default {
  name: "submitOrder",
  components: {
    Coupon,
  },
  data() {
    return {
      addressBook: [],
      chosenAddress: "",
      // 1微信  2支付宝
      paymentFlag: "",
      BaseURL: BaseURL,
      cart: [],
      sum: 0,
      showCoupon: false,
      couponsIds: [],
    };
  },
  methods: {
    ...mapActions("Coupon", [
      "getCoupons",
      "getReceivedCoupons",
      "receiveCoupon",
    ]),
    getAddressBook() {
      return getAddressApi({ id: sessionStorage.getItem("userEmail") }).then(
        ({ data }) => {
          this.addressBook = data.data;
          this.addressBook.forEach((item) => {
            if (item.isDefault) {
              this.chosenAddress = item;
            }
          });
        }
      );
    },
    manageAddress() {
      this.$router.push("personal/personaladdress");
    },
    backToCart() {
      this.$router.push("personal/cart");
    },
    chooseAddress(item) {
      this.chosenAddress = item;
    },
    getCart() {
      getCartApi().then(({ data }) => {
        this.cart = data.data;
        if (this.flag == 1) {
          this.cart.forEach((item) => {
            this.sum += item.amount;
          });
        } else {
          console.log("this.order");
          console.log(this.order);
          this.sum = this.order.amount;
        }
      });
    },
    changePayment(value) {
      this.paymentFlag = value;
    },
    handleClick(item) {
      if (item.title === "使用") {
        if (item.conditions <= this.sum) {
          this.sum -= item.amount;
          this.couponsIds.push(item.id);
          this.receivedCoupons.forEach((i) => {
            {
              if (i.id === item.id) {
                this.$set(i, "title", "已使用");
              }
            }
          });
          Message({
            message: "使用成功",
            type: "success",
            offset: "140",
          });
        } else {
          Message({
            message: "无法使用当前优惠券",
            type: "error",
            offset: "140",
          });
        }
      } else if (item.title === "已使用") {
        this.sum += item.amount;
        this.couponsIds = this.couponsIds.filter((i) => {
          return i.id !== item.id;
        });
        this.receivedCoupons.forEach((i) => {
          {
            if (i.id === item.id) {
              this.$set(i, "title", "使用");
            }
          }
        });
        Message({
          message: "已取消使用",
          type: "success",
          offset: "140",
        });
      } else if (item.title === "领取") {
        this.receiveCoupon(item.id)
          .then(() => {
            Message({
              message: "领取成功",
              type: "success",
              offset: "140",
            });
            return this.getCoupons();
          })
          .then(() => {
            return this.getReceivedCoupons();
          })
          .then(() => {
            location.reload();
          })
          .catch((err) => {
            console.log("error:coupon");
            console.log(err);
          });
      }
    },
    submitOrder() {
      if (this.flag === 1) {
        const data = {
          addressBookId: this.chosenAddress.id,
          payMethod: this.paymentFlag,
          amount: this.sum,
          remark: "无",
          couponsIds: this.couponsIds,
        };
        // 购物车购买
        submitOrderApi_1(data).then(() => {
          Message({
            message: "订单提交成功",
            type: "success",
            offset: "140",
          });
          this.$router.push({ name: "cart" });
        });
      } else {
        const data = {
          addressBookId: this.chosenAddress.id,
          payMethod: this.paymentFlag,
          amount: this.sum,
          number: this.order.number,
          remark: "无",
          couponsIds: this.couponsIds,
          commodityId: this.order.commodityId,
          commodityAttribute: this.order.commodityAttribute,
        };
        // 立即购买
        submitOrderApi_0(data).then(() => {
          Message({
            message: "订单提交成功",
            type: "success",
            offset: "140",
          });
          this.$router.push({ name: "home" });
        });
      }
    },
  },
  computed: {
    ...mapState("Order", ["order", "flag"]),
    ...mapState("Coupon", ["coupons", "receivedCoupons"]),
  },
  created() {
    this.getAddressBook()
      .then(() => {
        return this.getCoupons();
      })
      .then(() => {
        return this.getReceivedCoupons();
      })
      .then(() => {
        return this.getCart();
      })
      .then(() => {
        this.coupons.forEach((item) => {
          this.$set(item, "title", "领取");
        });
        this.receivedCoupons.forEach((item) => {
          this.$set(item, "title", "使用");
        });
        this.showCoupon = true;
      });
  },
};
</script>

<style scoped lang="less">
#submitOrder {
  header {
    height: 80px;
    border-bottom: 2px solid #ff9500;
    line-height: 80px;
    img {
      width: 200px;
    }
    span {
      display: inline-block;
      transform: translate(15px, 8px);
      color: #797979;
    }
  }
  .submitBody {
    padding: 20px 30px;
    margin-top: 30px;
    // height: 100vh;
    box-shadow: 0 0 5px #ccc;
    span {
      font-size: 20px;
    }
    a {
      float: right;
      font-size: 14px;
      transform: translate(-10px, 10px);
      color: rgb(108, 137, 229);
      text-decoration: none;
      &:hover {
        color: rgb(239, 37, 73);
      }
    }
    .addressContainer {
      min-height: 150px;
      .card {
        display: inline-block;
        margin-right: 20px;
        margin-bottom: 20px;
      }

      .consigneeLine {
        margin-bottom: 5px;
        span:first-child {
          font-size: 16px;
        }
        .el-tag {
          margin-right: 20px;
          font-size: 14px;
          transform: translateX(20px);
        }
        i {
          position: absolute;
          right: 20px;
          font-size: 24px;
          color: rgb(102, 195, 59);
        }
      }
      .phoneLine,
      .addressLine {
        font-size: 14px;
        color: #797979;
      }
    }
    .goodsDetail {
      .goodsDetailContainer {
        .card {
          margin-bottom: 10px;
          width: 100%;
          background: rgba(224, 224, 224, 0.1);
        }
        .goodsDetailBox {
          display: flex;
          .infoBox {
            position: relative;
            left: 50px;
            p {
              color: #797979;
              &:nth-child(1) {
                color: #000;
                font-size: 20px;
                font-weight: 700;
              }
            }
          }

          .numBox {
            position: absolute;
            right: 20px;
            top: 60px;
            span {
              font-size: 18px;
              &:first-child {
                margin-right: 40px;
                width: 200px;
              }
              &:last-child {
                font-size: 20px;
                font-weight: 700;
                color: rgb(239, 37, 73);
              }
            }
          }
        }
      }
    }
    .paymentContainer {
      margin-top: 50px;
      .paymentBox {
        display: flex;
        div {
          width: 200px;
          margin-right: 20px;
        }
      }
      i {
        color: rgb(102, 195, 59);
      }
    }
    .couponContainer {
      margin-top: 50px;
      .coupon {
        position: relative;
        margin-right: 10px;
        margin-bottom: 10px;
        float: left;
      }
    }
    .submitOrderContainer {
      margin-top: 50px;
      .card {
        background-color: rgb(252, 252, 252);
      }
      .submitBox {
        position: relative;
        height: 70px;
        p {
          position: absolute;
          right: 0;
          &:nth-child(1) {
            top: -5px;
          }
          &:nth-child(2) {
            color: #797979;
            top: 40px;
          }
        }
        .finalAmount {
          margin-left: 50px;
          font-size: 22px;
          font-weight: 700;
          color: rgb(239, 37, 73);
        }
      }
      .submitBtnBar {
        margin-top: 10px;
        height: 40px;
        .submitBtn {
          float: right;
          font-size: 18px;
          width: 135px;
          height: 40px;
          color: #fff;
          background-color: rgb(227, 82, 86);
          border: none;
        }
      }
    }
  }
  .btn {
    position: absolute;
    top: 5px;
    left: 170px;

    width: 0;
    height: 0;
    padding: 0;
    button {
      position: absolute;
      right: -135px;
      top: 35px;
      padding: 0 10px;
      font-size: 16px;
      color: rgb(230, 78, 84);
      width: 70px;
      height: 30px;
      line-height: 30px;
      background-color: #fff;
      border: none;
      border-radius: 20px;
    }
  }
  img {
    width: 100px;
  }
}
</style>