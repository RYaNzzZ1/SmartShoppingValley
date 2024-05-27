<template>
  <div id="home">
    <!-- <div class="cloudContainer">
      <div class="cloud">
        <div class="before"></div>
        <div class="after"></div>
      </div>
    </div> -->

    <el-card shadow="never">
      <i class="el-icon-document"></i><span> 欢迎 </span>
    </el-card>

    <div class="carouselContainer">
      <el-carousel height="140px" arrow="never">
        <el-carousel-item v-for="item in carouselImgs" :key="item">
          <img class="carouselImg" :src="item" />
        </el-carousel-item>
      </el-carousel>
    </div>

    <el-card shadow="never" class="bar">
      <i class="el-icon-document"></i><span> 数据概览 </span>
    </el-card>

    <div class="dataBar">
      <el-descriptions direction="vertical" :column="4" border>
        <el-descriptions-item
          label="本日销售额"
          content-class-name="salesContent"
          label-class-name="salesLabel"
        >
          <img src="../assets/images/home01.png" class="salesImg" />
          <el-statistic
            group-separator=","
            :precision="2"
            :value="statistics.dailyAmount"
            ><template slot="suffix">
              <span>￥ &nbsp;&nbsp;&nbsp;</span>
              <i class="el-icon-s-flag" style="color: blue"></i> </template
          ></el-statistic>
        </el-descriptions-item>
      </el-descriptions>
      <el-descriptions direction="vertical" :column="4" border>
        <el-descriptions-item
          label="本周销售额"
          content-class-name="salesContent"
          label-class-name="salesLabel"
        >
          <img src="../assets/images/home02.png" class="salesImg" />

          <el-statistic
            group-separator=","
            :precision="2"
            :value="statistics.weeklyAmount"
            ><template slot="suffix">
              <span>￥ &nbsp;&nbsp;&nbsp;</span>
              <i class="el-icon-s-flag" style="color: blue"></i> </template
          ></el-statistic>
        </el-descriptions-item>
      </el-descriptions>
      <el-descriptions direction="vertical" :column="4" border>
        <el-descriptions-item
          label="本月销售额"
          content-class-name="salesContent"
          label-class-name="salesLabel"
        >
          <img src="../assets/images/home03.png" class="salesImg" />

          <el-statistic
            group-separator=","
            :precision="2"
            :value="statistics.monthlyAmount"
            ><template slot="suffix">
              <span>￥ &nbsp;&nbsp;&nbsp;</span>
              <i class="el-icon-s-flag" style="color: blue"></i> </template
          ></el-statistic>
        </el-descriptions-item>
      </el-descriptions>
    </div>

    <div class="amountBar">
      <el-descriptions direction="vertical" :column="4" border>
        <el-descriptions-item
          label="本日销量"
          content-class-name="salesContent"
          label-class-name="salesLabel"
        >
          <img src="../assets/images/home01.png" class="salesImg" />
          <el-statistic
            group-separator=","
            :precision="2"
            :value="statistics.dailyNum"
            ><template slot="suffix">
              <span>&nbsp;&nbsp;&nbsp;</span>
              <i class="el-icon-s-flag" style="color: red"></i> </template
          ></el-statistic>
        </el-descriptions-item>
      </el-descriptions>
      <el-descriptions direction="vertical" :column="4" border>
        <el-descriptions-item
          label="本周销量"
          content-class-name="salesContent"
          label-class-name="salesLabel"
        >
          <img src="../assets/images/home02.png" class="salesImg" />

          <el-statistic
            group-separator=","
            :precision="2"
            :value="statistics.weeklyNum"
            ><template slot="suffix">
              <span>&nbsp;&nbsp;&nbsp;</span>
              <i class="el-icon-s-flag" style="color: red"></i> </template
          ></el-statistic>
        </el-descriptions-item>
      </el-descriptions>
      <el-descriptions direction="vertical" :column="4" border>
        <el-descriptions-item
          label="本月销量"
          content-class-name="salesContent"
          label-class-name="salesLabel"
        >
          <img src="../assets/images/home03.png" class="salesImg" />

          <el-statistic
            group-separator=","
            :precision="2"
            :value="statistics.monthlyNum"
            ><template slot="suffix">
              <span>&nbsp;&nbsp;&nbsp;</span>
              <i class="el-icon-s-flag" style="color: red"></i> </template
          ></el-statistic>
        </el-descriptions-item>
      </el-descriptions>
    </div>

    <div class="chartBar">
      <el-descriptions direction="vertical" :column="4" border>
        <el-descriptions-item
          label="近一周内销售数据"
          content-class-name="salesContent"
          label-class-name="salesLabel"
        >
          <e-charts class="chart" :option="option" />
        </el-descriptions-item>
      </el-descriptions>
    </div>
  </div>
</template>

<script>
import { Message } from "element-ui";
import { mapActions, mapState } from "vuex";
export default {
  name: "Home",
  data() {
    return {
      letters: "abcdefghijklmnopqrtuvwxyz1234567890",
      carouselImgs: [
        "http://ehall.seu.edu.cn/attachment/upload/2019-12-16_03-14-34/34bdfe19-f951-4f86-9efb-2cbed2663bd1/banner1.jpg",
        "http://ehall.seu.edu.cn/attachment/upload/2019-12-11_06-23-47/6a58fce9-5d7b-4d6e-80e0-fbcc033016b4/banner3.jpg",
        "http://ehall.seu.edu.cn/attachment/upload/2019-12-11_06-24-02/1cc29806-2bef-4d33-a798-6b9c55345d87/banner4.jpg",
        "http://ehall.seu.edu.cn/attachment/upload/2019-12-16_03-14-52/1e03a885-8fb4-45ee-bff4-a056f92b313e/%E4%B8%9C%E5%A4%A7banner1.jpg",
      ],
    };
  },
  methods: {
    ...mapActions("Home", ["getStatistics"]),
    getLetter() {
      return this.letters[Math.floor(Math.random() * this.letters.length)];
    },
  },
  computed: {
    ...mapState("Home", ["statistics"]),
    option() {
      return {
        tooltip: {
          trigger: "axis",
          axisPointer: {
            type: "cross",
            label: {
              backgroundColor: "#6a7985",
            },
          },
        },
        legend: {
          data: ["销量", "销售额"],
        },
        toolbox: {
          feature: {
            saveAsImage: {},
          },
        },
        grid: {
          left: "3%",
          right: "4%",
          bottom: "3%",
          containLabel: true,
        },
        xAxis: [
          {
            type: "category",
            boundaryGap: false,
            data: ["Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"],
          },
        ],
        yAxis: [
          {
            type: "value",
          },
        ],
        series: [
          {
            name: "销售额",
            type: "line",
            stack: "Total",
            areaStyle: {},
            emphasis: {
              focus: "series",
            },
            data: this.statistics.amounts,
          },
          {
            name: "销量",
            type: "line",
            stack: "Total",
            areaStyle: {},
            emphasis: {
              focus: "series",
            },
            data: this.statistics.nums,
          },
        ],
      };
    },
  },
  mounted() {
    this.getStatistics().catch((err) => {
      console.log(err);
      Message({
        type: "error",
        message: "网络连接超时，请稍后重试",
      });
    });
    // this.timer = setInterval(() => {
    //   // location.reload();
    // }, 0);
  },
  // beforeDestroy() {
  //   clearInterval(this.timer); // 清除定时器
  // },
};
</script>

<style scoped lang="less">
@cloudColor: rgb(188, 234, 167);

@keyframes animate {
  0% {
    transform: translateY(0) scale(1);
  }
  70% {
    transform: translateY(150px) scale(1.5);
  }
  100% {
    transform: translateY(200px) scale(0);
  }
}
@keyframes animate_move {
  0% {
    transform: translateX(-70px);
  }
  70% {
    transform: translateX(50px);
  }
  100% {
    transform: translateX(80px);
  }
}

#home {
  position: relative;
  .dataBar,
  .amountBar {
    margin-top: 30px;
    padding: 0 300px;
    display: flex;
    padding-right: 250px;
    margin-bottom: 30px;
    .el-descriptions {
      margin-right: 30px;
      /deep/.salesContent {
        display: flex;
        align-items: center;
        width: 350px;
      }
      /deep/.salesLabel {
        font-size: 14px;
        font-weight: 700;
        background-color: rgb(242, 246, 252);
      }
      /deep/.salesImg {
        width: 50px;
      }
    }
  }
  .chartBar {
    margin-top: 30px;
    padding-left: 220px;
    padding-right: 250px;
    .el-descriptions {
      margin-right: 30px;
      /deep/.salesLabel {
        font-size: 16px;
        font-weight: 700;
        background-color: rgb(242, 246, 252);
      }
    }
  }
  .cloudContainer {
    position: absolute;
    top: 70px;
    right: 60px;
    width: 250px;
    height: 250px;
    animation: animate_move 2s linear infinite alternate;
    &:hover {
      animation-play-state: paused;
    }
    .cloud {
      position: absolute;
      top: 80px;
      left: 0;
      height: 70px;
      width: 250px;
      border-radius: 50px;
      background-color: @cloudColor;
      box-shadow: 0 0 35px @cloudColor;
      z-index: 50;
      .before {
        position: absolute;
        top: -35px;
        left: 50px;
        content: "";
        height: 80px;
        width: 80px;
        background-color: @cloudColor;
        border-radius: 50%;
        box-shadow: 0 0 35px @cloudColor;
        z-index: -1;
      }
      .after {
        position: absolute;
        top: -55px;
        left: 100px;
        content: "";
        height: 100px;
        width: 100px;
        background-color: @cloudColor;
        border-radius: 50%;
        box-shadow: 0 0 35px @cloudColor;
        z-index: -1;
      }
      /deep/.text {
        position: absolute;
        top: 0;
        left: 0;
        font-size: 24px;
        color: @cloudColor;
        animation: animate 2s linear forwards;
      }
    }
  }
  .chart {
    width: 1300px;
    height: 320px;
  }
  .carouselContainer {
    position: relative;
    margin-top: 10px;
    width: 100%;
    height: 140px;
    .el-carousel {
      width: 1150px;
      position: absolute;
      top: 0;
      left: 50%;
      transform: translateX(-50%);
      .carouselImg {
        border-radius: 5px;
      }
    }
  }
  .bar {
    margin-top: 20px;
  }
}
</style>