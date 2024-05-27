<template>
  <div id="coupon">
    <div class="title">
      <div>优</div>
      <div>惠</div>
      <div>券</div>
    </div>
    <div class="line"></div>
    <div class="info">
      <p>
        <span class="amount">￥{{ amount }}</span>
      </p>
      <p>满{{ conditions }}元可用</p>
    </div>
    <!-- <div class="btn">
      <button @click="handleClick">{{ usage }}</button>
    </div> -->
  </div>
</template>

<script>
import { mapActions } from "vuex";
import { Message } from "element-ui";

export default {
  name: "Coupon",
  props: ["conditions", "amount", "title", "chosen", "id"],
  data() {
    return {
      usage: this.title,
      info: {
        id: this.id,
        conditions: this.conditions,
        amount: this.amount,
      },
    };
  },
  methods: {
    ...mapActions("Coupon", [
      "receiveCoupon",
      "setCurrentCoupon",
      "getCoupons",
      "getReceivedCoupons",
    ]),
    // handleClick() {
    //   if (this.usage === "领取") {
    //     this.receiveCoupon(this.id)
    //       .then(() => {
    //         Message({
    //           message: "领取成功",
    //           type: "success",
    //           offset: "140",
    //         });
    //         return this.getCoupons();
    //       })
    //       .then(() => {
    //         return this.getReceivedCoupons();
    //       })
    //       .catch((err) => {
    //         console.log("error:coupon");
    //         console.log(err);
    //       });
    //   } else if (this.usage === "使用") {
    //     const data = {
    //       ...this.info,
    //       usageFlag: "使用",
    //     };
    //     this.$emit("myClick", data);
    //   } else if (this.usage === "已使用") {
    //     const data = {
    //       ...this.info,
    //       usageFlag: "已使用",
    //     };
    //     this.$emit("myClick", data);
    //   }
    // },
  },
};
</script>

<style scoped lang="less">
#coupon {
  display: flex;
  width: 320px;
  height: 110px;
  background-color: rgb(230, 78, 84);
  border-radius: 15px;
  .line {
    margin-left: 10px;
    margin-right: 10px;
    width: 1px;
    height: 100%;
    background-color: #fff;
  }
  .title {
    padding-left: 20px;
    width: 50px;
    font-size: 20px;
    font-weight: 900;
    color: #fff;
  }
  .info {
    padding-top: 20px;
    padding-left: 20px;
    color: #fff;
    .amount {
      font-size: 24px;
      color: #fff;
    }
    p {
      margin: 0;
      color: #ccc;
    }
  }
  // .btn {
  //   position: relative;
  //   width: 0;
  //   height: 0;
  //   padding: 0;
  //   button {
  //     position: absolute;
  //     right: -135px;
  //     top: 35px;
  //     padding: 0 10px;
  //     font-size: 16px;
  //     color: rgb(230, 78, 84);
  //     width: 70px;
  //     height: 30px;
  //     line-height: 30px;
  //     background-color: #fff;
  //     border: none;
  //     border-radius: 20px;
  //   }
  // }
}
</style>