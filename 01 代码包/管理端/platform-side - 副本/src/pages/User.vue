<template>
  <div id="user">
    <el-card shadow="never" class="searchBar">
      <i class="el-icon-search"></i><span> 筛选搜索 </span>
      <el-button @click="resetInput" class="resetBtn">重置</el-button>
      <el-button @click="searchInput" class="searchBtn" type="primary"
        >查询搜索</el-button
      >
      <br />
      <el-input
        class="inputBar"
        placeholder="请输入用户名"
        prefix-icon="el-icon-search"
        v-model="inputValue"
      >
      </el-input>
    </el-card>
    <el-card class="dataBar" shadow="never">
      <i class="el-icon-document"></i><span> 用户列表 </span>
    </el-card>

    <el-table
      v-loading="loading"
      class="table"
      :data="users"
      style="width: 100%"
      :border="true"
      :max-height="800"
    >
      <el-table-column prop="id" label="编号"> </el-table-column>
      <el-table-column prop="username" label="用户名"> </el-table-column>
      <el-table-column prop="status" label="状态">
        <template slot-scope="scope">
          <el-tag :type="scope.row.status == 1 ? '' : 'danger'">{{
            scope.row.status == 1 ? "正常" : "禁用"
          }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="注册时间" width="520">
      </el-table-column>
      <el-table-column label="操作" width="300">
        <template slot-scope="scope">
          <el-button size="mini" @click="handleEnable(scope.row)"
            >启用</el-button
          >
          <el-button size="mini" @click="handleView(scope.row)"
            >查看详情</el-button
          >
          <el-button size="mini" type="danger" @click="handleDisable(scope.row)"
            >禁用</el-button
          >
        </template>
      </el-table-column>
    </el-table>

    <el-drawer
      title="用户详情"
      :visible="drawer"
      size="35%"
      direction="rtl"
      @open="openDrawer"
      @close="closeDrawer"
    >
      <el-card shadow="never" class="description">
        <el-descriptions
          border
          class="margin-top"
          :column="2"
          size="medium"
          direction="vertical"
        >
          <el-descriptions-item label="头像">
            <el-avatar
              shape="square"
              :size="50"
              :src="userDetail ? userDetail.avatar : ''"
            ></el-avatar>
          </el-descriptions-item>
          <el-descriptions-item
            label="id"
            label-class-name="descLabel"
            content-class-name="descContent"
            >{{
              userDetail ? userDetail.customerId : "无"
            }}</el-descriptions-item
          >
          <el-descriptions-item
            label="昵称"
            label-class-name="descLabel"
            content-class-name="descContent"
            >{{ userDetail ? userDetail.name : "无" }}</el-descriptions-item
          >
          <el-descriptions-item
            label="性别"
            label-class-name="descLabel"
            content-class-name="descContent"
            >{{
              userDetail ? (userDetail.sex === 1 ? "男" : "女") : "无"
            }}</el-descriptions-item
          >
          <el-descriptions-item
            label="年龄"
            label-class-name="descLabel"
            content-class-name="descContent"
            >{{ userDetail ? userDetail.age : "无" }}</el-descriptions-item
          >
          <el-descriptions-item
            label="生日"
            label-class-name="descLabel"
            content-class-name="descContent"
            >{{ userDetail ? userDetail.birthday : "无" }}</el-descriptions-item
          >
          <el-descriptions-item
            label="联系方式"
            label-class-name="descLabel"
            content-class-name="descContent"
            >{{ userDetail ? userDetail.phone : "无" }}</el-descriptions-item
          >
          <el-descriptions-item
            label="个人描述"
            label-class-name="descLabel"
            content-class-name="descContent"
            >{{
              userDetail ? userDetail.description : "无"
            }}</el-descriptions-item
          >
        </el-descriptions>
      </el-card>
    </el-drawer>

    <div class="block">
      <el-pagination
        background
        layout="prev, pager, next"
        :page-size="pageData.limit"
        @current-change="handlePageChange"
        :total="pageData.total"
        :current-page.sync="pageData.page"
      >
      </el-pagination>
    </div>
  </div>
</template>

<script>
import { mapState, mapActions } from "vuex";
import { Message } from "element-ui";

export default {
  name: "User",
  data() {
    return {
      inputValue: "",
    };
  },
  methods: {
    ...mapActions("User", [
      "getUsers",
      "saveUserStatus",
      "getSearched",
      "getUserDetail",
      "openDrawer",
      "closeDrawer",
    ]),
    resetInput() {
      this.inputValue = "";
      this.getUsers();
    },
    searchInput() {
      if (this.inputValue !== "") {
        this.getSearched(this.inputValue).catch((err) => {
          console.log(err);
          Message({
            message: "网络连接超时，请稍后重试",
            type: "error",
          });
        });
      }
    },
    handleEnable(row) {
      if (row.status === 2) {
        this.saveUserStatus(row.id)
          .then(() => {
            if (this.inputValue !== "") {
              this.getSearched(this.inputValue);
            } else {
              this.getUsers();
            }
          })
          .catch((err) => {
            console.log(err);
            Message({
              message: "网络连接超时，请稍后重试",
              type: "error",
            });
          });
        Message({
          type: "success",
          message: "启用成功",
        });
      }
    },
    handleDisable(row) {
      if (row.status === 1) {
        this.saveUserStatus(row.id)
          .then(() => {
            if (this.inputValue !== "") {
              this.getSearched(this.inputValue);
            } else {
              this.getUsers();
            }
          })
          .catch((err) => {
            console.log(err);
            Message({
              message: "网络连接超时，请稍后重试",
              type: "error",
            });
          });
        Message({
          type: "success",
          message: "禁用成功",
        });
      }
    },
    handleView(row) {
      this.getUserDetail(row.id).catch((err) => {
        console.log(err);
        Message({
          message: "网络连接超时，请稍后重试",
          type: "error",
        });
      });
    },
    handlePageChange() {
      this.getUsers().catch((err) => {
        console.log(err);
        Message({
          message: "网络连接超时，请稍后重试",
          type: "error",
        });
      });
    },
  },
  computed: {
    ...mapState("User", [
      "users",
      "pageData",
      "drawer",
      "userDetail",
      "loading",
    ]),
  },
  mounted() {
    this.getUsers().catch((err) => {
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
#user {
  position: relative;
  .searchBar {
    position: relative;
    margin-bottom: @interval;
    .el-button {
      position: absolute;
      top: 20%;
    }
    .resetBtn {
      right: 130px;
    }
    .searchBtn {
      right: 20px;
    }
    .inputBar {
      margin: 25px 0 8px 60px;
      width: 300px;
    }
  }

  .dataBar {
    position: relative;
    margin-bottom: @interval;
    .el-button {
      position: absolute;
      top: 20%;
      right: 20px;
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
  /deep/.descLabel {
    font-size: 16px;
    color: rgba(0, 0, 0, 0.4);
  }
  /deep/.descContent {
    font-size: 16px;

    color: #000;
  }
  .description {
    margin: 0 30px;
  }
}
</style>