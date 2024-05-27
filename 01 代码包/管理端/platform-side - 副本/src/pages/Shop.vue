<template>
  <div id="shop">
    <el-card class="dataBar" shadow="never">
      <i class="el-icon-document"></i><span> 门店信息管理 </span>
    </el-card>

    <el-divider content-position="left">欢迎进入线下店选购</el-divider>

    <el-card shadow="never" class="addCard">
      <el-form label-position="left" label-width="80px" :model="shop">
        <el-form-item label="门店名称">
          <el-input v-model="shop.name"></el-input>
        </el-form-item>
        <el-form-item label="联系方式">
          <el-input v-model="shop.phone"></el-input>
        </el-form-item>
        <el-form-item label="营业时间">
          <el-time-picker
            format="HH:mm:ss"
            arrow-control
            v-model="time_1"
            value-format="hh:mm:ss"
            :picker-options="{
              selectableRange: '07:00:00 - 12:00:00',
            }"
            @change="changeTime_1"
          >
          </el-time-picker>
          &nbsp;&nbsp;&nbsp;&nbsp;
          <el-time-picker
            format="HH:mm:ss"
            arrow-control
            v-model="time_2"
            value-format="hh:mm:ss"
            :picker-options="{
              selectableRange: '14:30:00 - 24:00:00',
            }"
            @change="changeTime_2"
          >
          </el-time-picker>
        </el-form-item>
        <el-form-item label="详细地址">
          <el-input v-model="shop.address"></el-input>
        </el-form-item>
        <el-form-item label="门店描述">
          <el-input type="textarea" v-model="shop.description"></el-input>
        </el-form-item>
        <el-form-item class="buttonBar">
          <el-button @click="confirm" type="primary" class="confirmBtn"
            >确认</el-button
          >
        </el-form-item>
      </el-form>
    </el-card>
    <el-image :src="require('../assets/images/shop01.png')" class="shopImg">
      <div slot="placeholder">图片加载中<i class="el-icon-loading"></i></div
    ></el-image>
  </div>
</template>

<script>
import { mapState, mapActions } from "vuex";
import { Message } from "element-ui";

export default {
  name: "Shop",
  data() {
    return {
      time_1: "",
      time_2: "",
    };
  },
  methods: {
    ...mapActions("Shop", [
      "getShop",
      "changeShop",
      "change_Time_1",
      "change_Time_2",
    ]),
    confirm() {
      this.changeShop(this.shop)
        .then(() => {
          return this.getShop();
        })
        .then(() => {
          Message({
            type: "success",
            message: "修改成功",
          });
        })
        .catch((err) => {
          console.log(err);
          Message({
            message: "网络连接超时，请稍后重试",
            type: "error",
          });
        });
    },
    changeTime_1() {
      console.log(111);
      console.log(this.time_1);
      this.change_Time_1(this.time_1);
    },
    changeTime_2() {
      console.log(222);
      console.log(this.time_2);
      this.change_Time_2(this.time_2);
    },
  },
  computed: {
    ...mapState("Shop", ["shop"]),
  },
  mounted() {
    this.getShop()
      .then(() => {
        this.time_1 = this.shop.openingTime;
        this.time_2 = this.shop.closingTime;
      })
      .catch((err) => {
        Message({
          type: "error",
          message: "网络连接超时，请稍后再试",
        });
      });
  },
};
</script>

<style scoped lang="less">
@interval: 30px;
#shop {
  position: relative;
  .dataBar {
    position: relative;
    margin-bottom: @interval;
  }
  .shopInfo {
    margin-bottom: @interval;
  }
  /deep/.descContent {
    font-size: 17px;
    color: rgba(0, 0, 0, 0.9);
  }
  .descLabel {
    color: rgba(0, 0, 0, 0.3);
  }
  .addCard {
    position: relative;
    padding: 0 50% 0 30px;
    .endTime {
      margin-left: 20px;
    }
    .el-input {
      margin-bottom: 30px;
    }
    .buttonBar {
      margin-bottom: 80px;
      .el-button {
        position: absolute;
        width: 300px;
        top: 25px;
      }
      .confirmBtn {
        left: 400px;
      }
      .cancelBtn {
        left: 800px;
      }
    }
  }
  .shopImg {
    position: absolute;
    top: 19%;
    right: 10%;
    width: 550px;
  }

  .el-divider {
    margin: 50px 0;
  }
}
</style>