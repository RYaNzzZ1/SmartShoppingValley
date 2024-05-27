<template>
  <div id="order">
    <el-card class="dataBar" shadow="never">
      <i class="el-icon-s-order"></i><span> 订单列表 </span>
    </el-card>

    <el-table
      class="table"
      :data="orders"
      style="width: 100%"
      :border="true"
      :max-height="800"
      v-loading="loading"
    >
      <el-table-column prop="id" label="编号"> </el-table-column>
      <el-table-column prop="status" label="订单状态">
        <template slot-scope="scope">
          {{ getStatusInfo(scope.row) }}
        </template>
      </el-table-column>
      <el-table-column prop="customerId" label="顾客编号"> </el-table-column>
      <el-table-column prop="createTime" label="创建时间"></el-table-column>
      <el-table-column prop="addressBookId" label="地址编号"></el-table-column>
      <el-table-column prop="orderTime" label="下单时间"></el-table-column>
      <el-table-column prop="checkoutTime" label="结账时间"></el-table-column>
      <el-table-column prop="amount" label="实收金额">
        <template slot-scope="scope"> {{ scope.row.amount }}￥ </template>
      </el-table-column>
      <el-table-column prop="payMethod" label="支付方式">
        <template slot-scope="scope">
          {{ scope.row.payMethod === 1 ? "微信支付" : "支付宝支付" }}
        </template>
      </el-table-column>
      <el-table-column prop="remark" label="备注">
        <template slot-scope="scope">
          {{ scope.row.remark || "无" }}
        </template>
      </el-table-column>
      <el-table-column label="操作">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="primary"
            plain
            :disabled="scope.row.status !== 2"
            @click="handleDeliver(scope.row)"
            >发送订单</el-button
          >
        </template>
      </el-table-column>
    </el-table>

    <el-dialog title="提示" :visible.sync="dialogVisible" width="30%">
      <span>确认派送订单？</span>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="confirmDeliver">确 定</el-button>
      </span>
    </el-dialog>

    <div class="block">
      <el-pagination
        background
        layout="prev, pager, next"
        :page-size="pageData.limit"
        :total="pageData.total"
        :current-page.sync="pageData.page"
        @current-change="changePage"
      >
      </el-pagination>
    </div>
  </div>
</template>

<script>
import { mapActions, mapState } from "vuex";
import { Message } from "element-ui";

export default {
  name: "Order",
  data() {
    return {
      dialogVisible: false,
      orderId: "",
    };
  },
  methods: {
    ...mapActions("Order", ["getOrders", "deliverOrder"]),
    getStatusInfo(row) {
      switch (row.status) {
        case 1:
          return "待付款";
        case 2:
          return "待派送";
        case 3:
          return "已派送";
        case 4:
          return "已完成";
        case 5:
          return "已取消";
        case 6:
          return "售后";
        default:
          return "未知";
      }
    },
    handleDeliver(row) {
      this.orderId = row.id;
      this.dialogVisible = true;
    },
    confirmDeliver() {
      this.deliverOrder(this.orderId)
        .then(() => {
          return this.getOrders();
        })
        .then(() => {
          this.dialogVisible = false;
          Message({
            type: "success",
            message: "派送成功",
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
    changePage() {
      this.getOrders().catch((err) => {
        console.log(err);
        Message({
          message: "网络连接超时，请稍后重试",
          type: "error",
        });
      });
    },
  },
  computed: {
    ...mapState("Order", ["orders", "pageData", "loading"]),
  },
  mounted() {
    this.getOrders().catch((err) => {
      console.log(err);
      Message({
        message: "网络连接超时，请稍后重试",
        type: "error",
      });
    });
  },
};
</script>

<style scoped lang="less">
@interval: 30px;

.dataBar {
  position: relative;
  margin-bottom: @interval;
  .el-button {
    position: absolute;
    top: 20%;
    right: 20px;
  }
  .resetBtn {
    right: 100px;
  }
}
.table {
  margin-bottom: @interval;
  max-height: 870px;
}
.block {
  position: absolute;
  right: 20px;
}
</style>