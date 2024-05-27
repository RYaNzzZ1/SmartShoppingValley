<template>
  <div id="admin">
    <el-card class="dataBar" shadow="never">
      <i class="el-icon-document"></i><span> 管理员列表 </span>
      <!-- TODO -->
      <el-button @click="handleAdd" class="addBtn" type="primary" plain
        >添加</el-button
      >
    </el-card>

    <el-drawer
      class="addDrawer"
      title="添加管理员"
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
          <el-form-item prop="username">
            <label>用户名</label><br />
            <el-input
              v-model="addForm.username"
              autocomplete="off"
              placeholder="请输入用户名"
            ></el-input>
          </el-form-item>
          <el-form-item prop="name">
            <label>姓名</label><br />
            <el-input
              v-model="addForm.name"
              placeholder="请输入姓名"
            ></el-input>
          </el-form-item>
          <el-form-item prop="password">
            <label>密码</label><br />
            <el-input
              v-model="addForm.password"
              placeholder="请输入密码"
            ></el-input>
          </el-form-item>
          <el-form-item>
            <label>权限</label><br />
            <el-select
              v-model="addForm.privileges"
              placeholder="请选择管理员权限"
            >
              <el-option label="高级" value="1"></el-option>
              <el-option label="普通" value="2"></el-option>
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

    <el-table
      v-loading="loading"
      class="table"
      :data="admins"
      style="width: 100%"
      :border="true"
      :max-height="800"
    >
      <el-table-column prop="id" label="编号"> </el-table-column>
      <el-table-column prop="username" label="用户名"> </el-table-column>
      <el-table-column prop="name" label="姓名"> </el-table-column>
      <el-table-column prop="status" label="状态">
        <template slot-scope="scope">
          <el-tag :type="scope.row.status === 1 ? '' : 'danger'">{{
            scope.row.status === 1 ? "正常" : "禁用"
          }}</el-tag>
        </template>
      </el-table-column>

      <el-table-column prop="privileges" label="权限">
        <template slot-scope="scope">
          <el-tag :type="scope.row.privileges === 1 ? 'success' : ''">
            {{
              scope.row.privileges === 1 ? "高级管理员" : "普通管理员"
            }}</el-tag
          >
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="创建时间" width="420">
      </el-table-column>
      <el-table-column label="操作" width="360">
        <template slot-scope="scope">
          <el-button size="mini" @click="handleEnable(scope.row)"
            >启用</el-button
          >
          <el-button size="mini" @click="handleEdit(scope.row)">编辑</el-button>
          <el-button size="mini" type="danger" @click="handleDisable(scope.row)"
            >禁用</el-button
          >
          <el-button size="mini" type="danger" @click="handleDelete(scope.row)"
            >删除</el-button
          >
        </template>
      </el-table-column>
    </el-table>

    <el-dialog title="修改管理员" :visible.sync="editDialogVisible" width="30%">
      <el-form
        :model="editForm"
        status-icon
        :rules="editRules"
        ref="editForm"
        label-width="0"
        class="demo-ruleForm"
      >
        <el-form-item prop="username">
          <label>用户名</label><br />
          <el-input
            v-model="editForm.username"
            autocomplete="off"
            placeholder="请输入用户名"
          ></el-input>
        </el-form-item>
        <el-form-item prop="name">
          <label>姓名</label><br />
          <el-input v-model="editForm.name" placeholder="请输入姓名"></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <label>密码</label><br />
          <el-input
            v-model="editForm.password"
            placeholder="请输入密码"
          ></el-input>
        </el-form-item>
        <el-form-item>
          <label>是否设为高级管理员</label><br />
          <el-checkbox v-model="checked"></el-checkbox>
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

    <el-dialog
      title="提示"
      :visible.sync="deleteDialogVisible"
      width="30%"
      center
    >
      <span>确认删除？</span>
      <span slot="footer" class="dialog-footer">
        <el-button @click="cancelDelete">取 消</el-button>
        <el-button type="primary" @click="confirmDelete">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { mapActions, mapState } from "vuex";
import { Message } from "element-ui";
export default {
  name: "Admin",
  data() {
    return {
      editForm: {
        username: "",
        password: "",
        name: "",
      },
      editRules: {
        username: [
          { required: true, message: "请输入用户名", trigger: "blur" },
        ],
        password: [{ required: true, message: "请输入密码", trigger: "blur" }],
        name: [{ required: true, message: "请输入姓名", trigger: "blur" }],
      },
      editDialogVisible: false,
      deleteDialogVisible: false,
      checked: false,
      editId: "",
      deleteId: "",
      drawer: false,
      addForm: {
        username: "",
        password: "",
        name: "",
        privileges: "",
      },
      addRules: {
        username: [
          { required: true, message: "请输入用户名", trigger: "blur" },
        ],
        password: [{ required: true, message: "请输入密码", trigger: "blur" }],
        name: [{ required: true, message: "请输入姓名", trigger: "blur" }],
        privileges: [
          {
            required: true,
            message: "请选择权限",
            trigger: "blur",
          },
        ],
      },
    };
  },
  methods: {
    ...mapActions("Admin", [
      "getAdmins",
      "saveAdminStatus",
      "changeAdminInfo",
      "deleteAdmin",
      "addAdmin",
    ]),
    handleEnable(row) {
      if (row.status === 2) {
        this.saveAdminStatus(row.id)
          .then(() => {
            return this.getAdmins();
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
        this.saveAdminStatus(row.id)
          .then(() => {
            return this.getAdmins();
          })
          .then(() => {
            Message({
              type: "success",
              message: "禁用成功",
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
    handleEdit(row) {
      this.editDialogVisible = true;
      this.editId = row.id;
    },
    confirmEdit() {
      const data = {
        id: this.editId,
        ...this.editForm,
        privileges: this.checked ? 1 : 2,
      };

      this.changeAdminInfo(data)
        .then(() => {
          console.log("edit");
          console.log(data);
          this.getAdmins();
        })
        .then(() => {
          this.editForm = {
            username: "",
            password: "",
            name: "",
          };
          this.checked = false;
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
        username: "",
        password: "",
        name: "",
      };
      this.checked = false;
      this.editDialogVisible = false;
    },
    handleDelete(row) {
      this.deleteId = row.id;
      this.deleteDialogVisible = true;
    },
    confirmDelete() {
      this.deleteDialogVisible = false;
      this.deleteAdmin(this.deleteId)
        .then(() => {
          this.getAdmins();
        })
        .then(() => {
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
    cancelDelete() {
      this.deleteDialogVisible = false;
    },
    handleAdd() {
      this.drawer = true;
    },
    confirmAdd() {
      this.drawer = false;
      this.addAdmin(this.addForm)
        .then(() => {
          return this.getAdmins();
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
      this.drawer = false;
      this.addForm = {
        username: "",
        password: "",
        name: "",
        privileges: "",
      };
    },
  },
  computed: {
    ...mapState("Admin", ["admins", "loading"]),
  },
  mounted() {
    this.getAdmins()
      .then(() => {
        console.log(this.admins);
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
#admin {
  .dataBar {
    position: relative;
    margin-bottom: 30px;
    .addBtn {
      position: absolute;
      right: 40px;
      top: 20%;
    }
  }
  /deep/.el-dialog__body {
    padding-left: 50px;
    padding-right: 50px;
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
}
</style>