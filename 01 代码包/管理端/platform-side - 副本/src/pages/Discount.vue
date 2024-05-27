<template>
  <div id="discount">
    <el-card class="dataBar" shadow="never">
      <i class="el-icon-document"></i><span> 折扣列表 </span>
      <el-button @click="handleAdd">添加</el-button>
    </el-card>

    <el-table
      v-loading="loading"
      class="table"
      :data="discount"
      style="width: 100%"
      :border="true"
      :max-height="800"
    >
      <el-table-column prop="id" label="编号"> </el-table-column>
      <el-table-column prop="name" label="名称"> </el-table-column>
      <el-table-column prop="value" label="折扣值"> </el-table-column>
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
      <el-table-column label="商品设置">
        <template slot-scope="scope">
          <div class="discountBtn">
            <el-button size="mini" @click="handleParticipating(scope.row)"
              >查看参与该折扣商品</el-button
            ><br />
            <el-button size="mini" x @click="handleNotParticipating(scope.row)"
              >查看未参与该折扣商品</el-button
            >
          </div>
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
      <span>确认删除该折扣？</span>
      <span slot="footer" class="dialog-footer">
        <el-button @click="deleteDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="confirmDelete">确 定</el-button>
      </span>
    </el-dialog>

    <el-dialog title="编辑折扣" :visible.sync="editDialogVisible" width="30%">
      <el-form
        :model="editForm"
        status-icon
        :rules="editRules"
        ref="editForm"
        label-width="0"
        class="demo-ruleForm"
      >
        <el-form-item prop="name">
          <label>折扣名称</label><br />
          <el-input
            v-model="editForm.name"
            autocomplete="off"
            placeholder="请输入折扣名称"
          ></el-input>
        </el-form-item>
        <el-form-item prop="value">
          <label>折扣值</label><br />
          <el-input
            v-model="editForm.value"
            placeholder="请输入折扣值"
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
      title="添加折扣"
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
          <el-form-item prop="name">
            <label>折扣名称</label><br />
            <el-input
              v-model="addForm.name"
              autocomplete="off"
              placeholder="请输入名称"
            ></el-input>
          </el-form-item>
          <el-form-item prop="value">
            <label>折扣值</label><br />
            <el-input
              v-model="addForm.value"
              placeholder="请输入折扣值"
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

    <el-dialog
      title="参与该折扣商品"
      :visible.sync="participatingDialogVisible"
    >
      <el-table
        max-height="900"
        ref="multipleTable"
        :data="participating"
        tooltip-effect="dark"
        style="width: 100%; margin-bottom: 50px"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55"> </el-table-column>
        <el-table-column prop="id" label="编号"> </el-table-column>
        <el-table-column prop="name" label="名称"> </el-table-column>
        <el-table-column prop="price" label="价格"> </el-table-column>
        <el-table-column prop="num" label="库存量"> </el-table-column>
        <el-table-column prop="status" label="状态">
          <template slot-scope="scope">
            <el-tag :type="scope.row.status == 0 ? 'danger' : ''">{{
              scope.row.status == 0 ? "停售" : "起售"
            }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间"> </el-table-column>
        <el-table-column prop="description" label="商品描述">
          <template slot-scope="scope">
            {{ scope.row.description || "无" }}
          </template>
        </el-table-column>
      </el-table>
      <div class="block paginationContainer">
        <el-pagination
          background
          layout="prev, pager, next"
          :page-size="participatingPageData.limit"
          @current-change="handlePageChange_2"
          :total="participatingPageData.total"
          :current-page.sync="participatingPageData.page"
        >
        </el-pagination>
      </div>

      <div class="formBtnContainer">
        <el-button type="primary" @click="confirmParticipating"
          >取消折扣</el-button
        >
        <el-button @click="cancelParticipating">退出</el-button>
      </div>
    </el-dialog>

    <el-dialog
      title="未参与该折扣商品"
      :visible.sync="notParticipatingDialogVisible"
    >
      <el-table
        max-height="900"
        ref="multipleTable_not"
        :data="notParticipating"
        tooltip-effect="dark"
        style="width: 100%; margin-bottom: 50px"
        @selection-change="handleSelectionChange_not"
      >
        <el-table-column type="selection" width="55"> </el-table-column>
        <el-table-column prop="id" label="编号"> </el-table-column>
        <el-table-column prop="name" label="名称"> </el-table-column>
        <el-table-column prop="price" label="价格"> </el-table-column>
        <el-table-column prop="num" label="库存量"> </el-table-column>
        <el-table-column prop="status" label="状态">
          <template slot-scope="scope">
            <el-tag :type="scope.row.status == 0 ? 'danger' : ''">{{
              scope.row.status == 0 ? "停售" : "起售"
            }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间"> </el-table-column>
        <el-table-column prop="description" label="商品描述">
          <template slot-scope="scope">
            {{ scope.row.description || "无" }}
          </template>
        </el-table-column>
      </el-table>
      <div class="block paginationContainer">
        <el-pagination
          background
          layout="prev, pager, next"
          :page-size="notParticipatingPageData.limit"
          @current-change="handlePageChange_3"
          :total="notParticipatingPageData.total"
          :current-page.sync="notParticipatingPageData.page"
        >
        </el-pagination>
      </div>

      <div class="formBtnContainer">
        <el-button type="primary" @click="confirmNotParticipating"
          >添加折扣</el-button
        >
        <el-button @click="cancelNotParticipating">退出</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { mapState, mapActions } from "vuex";
import { Message } from "element-ui";

export default {
  name: "Discount",
  data() {
    return {
      deleteDialogVisible: false,
      deleteId: "",
      addForm: {
        name: "",
        value: "",
        status: "",
        startTime: "",
        endTime: "",
      },
      addRules: {
        name: [{ required: true, message: "请输入折扣名称", trigger: "blur" }],
        value: [{ required: true, message: "请输入折扣值", trigger: "blur" }],
        status: [{ required: true, message: "请选择状态", trigger: "blur" }],
        startTime: [
          { required: true, message: "请输入开始时间", trigger: "blur" },
        ],
        endTime: [
          { required: true, message: "请输入结束时间", trigger: "blur" },
        ],
      },
      editForm: {
        name: "",
        value: "",
        startTime: "",
        endTime: "",
      },
      editRules: {
        name: [{ required: true, message: "请输入折扣名称", trigger: "blur" }],
        value: [{ required: true, message: "请输入折扣值", trigger: "blur" }],
        startTime: [
          { required: true, message: "请输入开始时间", trigger: "blur" },
        ],
        endTime: [
          { required: true, message: "请输入结束时间", trigger: "blur" },
        ],
      },
      drawer: false,
      editDialogVisible: false,
      participatingDialogVisible: false,
      multipleSelection: [],
      discountId: "",
      multipleSelection_not: [],
      notParticipatingDialogVisible: false,
    };
  },
  methods: {
    ...mapActions("Type", ["getTypeListById"]),
    ...mapActions("Discount", [
      "getSearched",
      "getDiscount",
      "getDiscountImg",
      "deleteDiscount",
      "editDiscount",
      "saveDiscountStatus",
      "addDiscount",
      "deleteDiscount",
      "getParticipating",
      "getNotParticipating",
      "cancelDiscount",
      "setDiscount",
    ]),
    handlePageChange() {
      this.getDiscount().catch((err) => {
        console.log(err);
        Message({
          message: "网络连接超时，请稍后重试",
          type: "error",
        });
      });
    },
    handlePageChange_2() {
      this.getParticipating().catch((err) => {
        console.log(err);
        Message({
          message: "网络连接超时，请稍后重试",
          type: "error",
        });
      });
    },
    handlePageChange_3() {
      this.getNotParticipating().catch((err) => {
        console.log(err);
        Message({
          message: "网络连接超时，请稍后重试",
          type: "error",
        });
      });
    },
    handleEnable(row) {
      if (row.status === 0) {
        const temp = {
          status: 1,
          id: row.id,
        };
        this.saveDiscountStatus(temp)
          .then(() => {
            this.getDiscount();
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
        this.saveDiscountStatus(temp)
          .then(() => {
            this.getDiscount();
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
      this.deleteDiscount(this.deleteId)
        .then(() => {
          return this.getDiscount();
        })
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
    },
    handleAdd() {
      this.drawer = true;
    },
    confirmAdd() {
      this.addDiscount(this.addForm)
        .then(() => {
          return this.getDiscount();
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
        name: "",
        value: "",
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
      this.editDiscount(data)
        .then(() => {
          this.getDiscount();
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
        name: "",
        value: "",
        startTime: "",
        endTime: "",
      };
      this.editDialogVisible = false;
    },
    handleParticipating(row) {
      //TEMP
      // this.participatingDialogVisible = true;
      // this.discountId = row.id;
      this.getParticipating(row.id)
        .then(() => {
          this.discountId = row.id;
          this.participatingDialogVisible = true;
        })
        .catch((err) => {
          console.log(err);
          Message({
            message: "网络连接超时，请稍后重试",
            type: "error",
          });
        });
    },
    confirmParticipating() {
      const ids = [];
      this.multipleSelection.forEach((item) => {
        ids.push(item.id);
      });
      const data = {
        discountId: this.discountId,
        commodityIds: ids,
      };
      if (ids.length === 0) {
        this.participatingDialogVisible = false;
      } else {
        this.cancelDiscount(data)
          .then(() => {
            this.participatingDialogVisible = false;
            Message({
              type: "success",
              message: "操作成功",
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
    cancelParticipating() {
      this.participatingDialogVisible = false;
      this.$refs.multipleTable.clearSelection();
      this.multipleSelection = [];
    },
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    handleSelectionChange_not(val) {
      this.multipleSelection_not = val;
    },
    handleNotParticipating(row) {
      this.getNotParticipating()
        .then(() => {
          this.discountId = row.id;
          this.notParticipatingDialogVisible = true;
        })
        .catch((err) => {
          console.log(err);
          Message({
            message: "网络连接超时，请稍后重试",
            type: "error",
          });
        });
    },
    confirmNotParticipating() {
      const ids = [];
      this.multipleSelection_not.forEach((item) => {
        ids.push(item.id);
      });
      const data = {
        discountId: this.discountId,
        commodityIds: ids,
      };
      if (ids.length === 0) {
        this.notParticipatingDialogVisible = false;
      } else {
        this.setDiscount(data)
          .then(() => {
            this.notParticipatingDialogVisible = false;
            Message({
              type: "success",
              message: "操作成功",
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
    cancelNotParticipating() {
      this.notParticipatingDialogVisible = false;
      this.$refs.multipleTable_not.clearSelection();
      this.multipleSelection_not = [];
    },
  },
  computed: {
    ...mapState("Discount", [
      "discount",
      "pageData",
      "participatingPageData",
      "notParticipatingPageData",
      "loading",
      "participatingLoading",
      "notParticipatingLoading",
      "participating",
      "notParticipating",
    ]),
  },
  mounted() {
    this.getDiscount().catch((err) => {
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

#discount {
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
  .discountBtn {
    width: 100%;
    // padding-left: 13px;
    .el-button {
      width: 150px;
    }
  }
  .paginationContainer {
    position: relative;
    margin-bottom: 20px;
    margin-right: 20px;
    left: 55%;
  }
}
</style>