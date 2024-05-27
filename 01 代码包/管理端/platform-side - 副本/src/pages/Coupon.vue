<template>
  <div id="coupon">
    <el-card class="dataBar" shadow="never">
      <i class="el-icon-document"></i><span> 优惠券列表 </span>
      <el-button @click="handleAdd">添加</el-button>
    </el-card>

    <el-table
      v-loading="loading"
      class="table"
      :data="coupon"
      style="width: 100%"
      :border="true"
      :max-height="800"
    >
      <el-table-column prop="id" label="编号"> </el-table-column>
      <el-table-column prop="conditions" label="满足额度"> </el-table-column>
      <el-table-column prop="amount" label="减少额度"> </el-table-column>
      <el-table-column prop="createTime" label="创建时间"> </el-table-column>
      <el-table-column prop="status" label="状态" width="140">
        <template slot-scope="scope">
          <el-tag :type="scope.row.status == 0 ? 'danger' : ''">{{
            scope.row.status == 0 ? "停用" : "启用"
          }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="有效期" width="320">
        <template slot-scope="scope">
          {{ scope.row.startTime }} —— {{ scope.row.endTime }}
        </template>
      </el-table-column>
      <el-table-column label="操作" width="300">
        <template slot-scope="scope">
          <el-button size="mini" @click="handleEnable(scope.row)"
            >启用</el-button
          >
          <el-button size="mini" @click="handleEdit(scope.row)">编辑</el-button>
          <el-button size="mini" type="danger" @click="handleDisable(scope.row)"
            >停用</el-button
          >
          <el-button
            size="mini"
            type="danger"
            @click="handleDelete(scope.row)"
            :disabled="scope.row.status === 1"
            >删除</el-button
          >
        </template>
      </el-table-column>
    </el-table>

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
    <el-dialog title="提示" :visible.sync="deleteDialogVisible" width="30%">
      <span>确认删除该优惠券？</span>
      <span slot="footer" class="dialog-footer">
        <el-button @click="deleteDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="confirmDelete">确 定</el-button>
      </span>
    </el-dialog>

    <el-dialog title="编辑优惠券" :visible.sync="editDialogVisible" width="30%">
      <el-form
        :model="editForm"
        status-icon
        :rules="editRules"
        ref="editForm"
        label-width="0"
        class="demo-ruleForm"
      >
        <el-form-item prop="conditions">
          <label>满足额度</label><br />
          <el-input
            v-model="editForm.conditions"
            autocomplete="off"
            placeholder="请输入满足额度"
          ></el-input>
        </el-form-item>
        <el-form-item prop="amount">
          <label>减少额度</label><br />
          <el-input
            v-model="editForm.amount"
            placeholder="请输入减少额度"
          ></el-input>
        </el-form-item>
        <el-form-item prop="startTime">
          <label>开始时间</label><br />
          <el-date-picker
            v-model="editForm.startTime"
            align="right"
            type="date"
            placeholder="选择开始日期"
            value-format="yyyy-MM-dd"
          >
          </el-date-picker>
        </el-form-item>
        <el-form-item prop="endTime">
          <label>结束时间</label><br />
          <el-date-picker
            v-model="editForm.endTime"
            align="right"
            type="date"
            placeholder="选择结束日期"
            value-format="yyyy-MM-dd"
          >
          </el-date-picker>
        </el-form-item>

        <el-form-item>
          <div class="formBtnContainer">
            <el-button type="primary" @click="confirmEdit('editForm')"
              >确认</el-button
            >
            <el-button @click="cancelEdit('editForm')">取消</el-button>
          </div>
        </el-form-item>
      </el-form>
    </el-dialog>

    <el-drawer
      class="addDrawer"
      title="添加优惠券"
      :visible.sync="drawer"
      :with-header="true"
    >
      <el-card shadow="never">
        <el-form
          :model="addForm"
          status-icon
          :rules="addRules"
          ref="addForm"
          label-width="0"
          class="demo-ruleForm"
        >
          <el-form-item prop="conditions">
            <label>满足额度</label><br />
            <el-input
              v-model="addForm.conditions"
              autocomplete="off"
              placeholder="请输入满足额度"
            ></el-input>
          </el-form-item>
          <el-form-item prop="amount">
            <label>减少额度</label><br />
            <el-input
              v-model="addForm.amount"
              placeholder="请输入减少额度"
            ></el-input>
          </el-form-item>
          <el-form-item prop="startTime">
            <label>开始时间</label><br />
            <el-date-picker
              v-model="addForm.startTime"
              align="right"
              type="date"
              placeholder="选择开始日期"
              value-format="yyyy-MM-dd"
            >
            </el-date-picker>
          </el-form-item>
          <el-form-item prop="endTime">
            <label>结束时间</label><br />
            <el-date-picker
              v-model="addForm.endTime"
              align="right"
              type="date"
              placeholder="选择结束日期"
              value-format="yyyy-MM-dd"
            >
            </el-date-picker>
          </el-form-item>
          <el-form-item>
            <label>状态</label><br />
            <el-select v-model="addForm.status" placeholder="请选择">
              <el-option label="停用" :value="0"> </el-option>
              <el-option label="启用" :value="1"> </el-option>
            </el-select>
          </el-form-item>
          <el-form-item>
            <div class="formBtnContainer">
              <el-button type="primary" @click="confirmAdd('addForm')"
                >确认</el-button
              >
              <el-button @click="cancelAdd('addForm')">取消</el-button>
            </div>
          </el-form-item>
        </el-form>
      </el-card>
    </el-drawer>
  </div>
</template>

<script>
import { mapState, mapActions } from "vuex";
import { Message } from "element-ui";

export default {
  name: "Coupon",
  data() {
    return {
      deleteDialogVisible: false,
      deleteId: "",
      addForm: {
        conditions: "",
        amount: "",
        status: "",
        startTime: "",
        endTime: "",
      },
      addRules: {
        conditions: [
          { required: true, message: "请输入满足额滴", trigger: "blur" },
        ],
        amount: [
          { required: true, message: "请输入减少额滴", trigger: "blur" },
        ],
        status: [{ required: true, message: "请选择状态", trigger: "blur" }],
        startTime: [
          { required: true, message: "请输入开始时间", trigger: "blur" },
        ],
        endTime: [
          { required: true, message: "请输入结束时间", trigger: "blur" },
        ],
      },
      editForm: {
        conditions: "",
        amount: "",
        startTime: "",
        endTime: "",
      },
      editRules: {
        conditions: [
          { required: true, message: "请输入满足额度", trigger: "blur" },
        ],
        amount: [
          { required: true, message: "请输入减少额度", trigger: "blur" },
        ],
        startTime: [
          { required: true, message: "请输入开始时间", trigger: "blur" },
        ],
        endTime: [
          { required: true, message: "请输入结束时间", trigger: "blur" },
        ],
      },
      drawer: false,
      editDialogVisible: false,
    };
  },
  methods: {
    ...mapActions("Type", ["getTypeListById"]),
    ...mapActions("Coupon", [
      "getSearched",
      "getCoupon",
      "getCouponImg",
      "deleteCoupon",
      "editCoupon",
      "saveCouponStatus",
      "addCoupon",
      "deleteCoupon",
    ]),
    handlePageChange() {
      this.getCoupon().catch((err) => {
        Message({
          type: "error",
          message: "网络连接超时，请稍后重试",
        });
      });
    },
    handleEnable(row) {
      if (row.status === 0) {
        const temp = {
          status: 1,
          id: row.id,
        };
        this.saveCouponStatus(temp)
          .then(() => {
            this.getCoupon();
          })
          .then(() => {
            Message({
              type: "success",
              message: "启用成功",
            });
          })
          .catch((err) => {
            console.log(err);
            Message({
              message: "网络连接超时，请稍后重试",
              type: "error",
            });
          });
      }
    },
    handleDisable(row) {
      if (row.status === 1) {
        const temp = {
          status: 0,
          id: row.id,
        };
        this.saveCouponStatus(temp)
          .then(() => {
            this.getCoupon();
          })
          .then(() => {
            Message({
              type: "success",
              message: "停用成功",
            });
          })
          .catch((err) => {
            console.log(err);
            Message({
              message: "网络连接超时，请稍后重试",
              type: "error",
            });
          });
      }
    },
    handleDelete(row) {
      this.deleteDialogVisible = true;
      this.deleteId = row.id;
    },
    confirmDelete() {
      this.deleteCoupon(this.deleteId).then(({ data }) => {
        if (data.code === 500) {
          Message({
            type: "error",
            message: `${data.msg}`,
          });
          return;
        } else {
          return this.getCoupon()
            .then(() => {
              this.deleteDialogVisible = false;
              Message({
                type: "success",
                message: "删除成功",
              });
            })
            .catch((err) => {
              console.log(err);
              Message({
                message: "网络连接超时，请稍后重试",
                type: "error",
              });
            });
        }
      });
    },
    handleAdd() {
      this.drawer = true;
    },
    confirmAdd() {
      const data = {
        adminId: this.admin.id,
        ...this.addForm,
      };
      this.addCoupon(data)
        .then(() => {
          return this.getCoupon();
        })
        .then(() => {
          this.drawer = false;
          Message({
            type: "success",
            message: "添加成功",
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
    cancelAdd() {
      this.addForm = {
        conditions: "",
        amount: "",
        status: "",
        startTime: "",
        endTime: "",
      };
      this.drawer = false;
    },
    handleEdit(row) {
      this.editDialogVisible = true;
      this.editId = row.id;
    },
    confirmEdit() {
      const data = {
        id: this.editId,
        ...this.editForm,
      };
      this.editCoupon(data)
        .then(() => {
          this.getCoupon();
        })
        .then(() => {
          this.editDialogVisible = false;
          Message({
            type: "success",
            message: "编辑成功",
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
    cancelEdit() {
      this.editForm = {
        conditions: "",
        amount: "",
        startTime: "",
        endTime: "",
      };
      this.editDialogVisible = false;
    },
  },
  computed: {
    ...mapState("Coupon", ["coupon", "pageData", "loading"]),
    ...mapState("Admin", ["admin"]),
  },
  mounted() {
    this.getCoupon().catch((err) => {
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

#coupon {
  .table {
    margin-bottom: @interval;
    max-height: 870px;
  }

  .block {
    position: absolute;
    right: 20px;
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
  .addBtn {
    position: absolute;
    right: 40px;
    top: 20%;
  }
  .formBtnContainer {
    display: flex;
    .el-button {
      flex: 1;
    }
  }
  /deep/.el-drawer__body {
    padding-left: 40px;
    padding-right: 40px;
  }
  //   .el-select {
  //     margin-left: 25px;
  //     margin-right: 50px;
  //   }
}
</style>