<template>
  <div id="type">
    <el-card class="dataBar" shadow="never">
      <i class="el-icon-document"></i><span> 数据列表 </span>
      <el-button class="resetBtn" @click="handleReset">重置</el-button>
      <el-button @click="handleAdd" type="primary" plain>添加</el-button>
    </el-card>

    <el-drawer title="添加商品类型" :visible.sync="drawer" :with-header="true">
      <el-card shadow="never" class="addCard">
        <el-form
          :model="addForm"
          status-icon
          :rules="addRules"
          ref="addForm"
          label-width="0"
          class="demo-ruleForm"
        >
          <el-form-item prop="fatherName">
            <label>父级分类名称</label><br />
            <el-select
              v-model="addForm.fatherName"
              placeholder="请选择父级分类名称"
            >
              <el-option
                v-for="item in fatherList"
                :key="item.id"
                :label="item.name"
                :value="item.name"
              ></el-option>
            </el-select>
          </el-form-item>
          <el-form-item prop="name">
            <label>分类名称</label><br />
            <el-input
              v-model="addForm.name"
              autocomplete="off"
              placeholder="请输入分类名称"
            ></el-input>
          </el-form-item>
          <el-form-item>
            <div class="formBtnContainer">
              <el-button type="primary" @click="submitAddForm('addForm')"
                >添加</el-button
              >
              <el-button @click="cancelAdd('addForm')">取消</el-button>
            </div>
          </el-form-item>
        </el-form>
      </el-card>
    </el-drawer>
    <el-table
      class="table"
      :data="typeList"
      style="width: 100%"
      :border="true"
      :max-height="800"
      v-loading="loading"
    >
      <el-table-column prop="id" label="编号">
        <template slot-scope="scope" v-if="scope.row.id !== -1">
          {{ scope.row.id }}
        </template>
      </el-table-column>
      <el-table-column prop="fatherId" label="父级分类编号">
        <template slot-scope="scope" v-if="scope.row.id !== -1">
          {{ scope.row.fatherId !== -1 ? scope.row.fatherId : "无" }}
        </template>
      </el-table-column>
      <el-table-column prop="name" label="名称">
        <template slot-scope="scope" v-if="scope.row.id !== -1">
          {{ scope.row.name }}
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="创建时间" width="520">
        <template slot-scope="scope" v-if="scope.row.id !== -1">
          {{ scope.row.createTime }}
        </template>
      </el-table-column>
      <el-table-column label="设置" width="240">
        <template slot-scope="scope" v-if="scope.row.id !== -1">
          <el-button
            size="mini"
            type="primary"
            plain
            :disabled="scope.row.fatherId !== -1"
            @click="handleViewLowLevel(scope.row)"
            >查看下级</el-button
          >
        </template>
      </el-table-column>
      <el-table-column label="操作" width="280">
        <template slot-scope="scope" v-if="scope.row.id !== -1">
          <el-button size="mini" @click="handleEdit(scope.row)">编辑</el-button>
          <el-button size="mini" type="danger" @click="handleDelete(scope.row)"
            >删除</el-button
          >
        </template>
      </el-table-column>
    </el-table>

    <el-dialog title="修改分类" :visible.sync="dialogFormVisible" width="30%">
      <el-form
        :model="editForm"
        status-icon
        :rules="editRules"
        ref="editForm"
        label-width="0"
        class="demo-ruleForm"
      >
        <el-form-item prop="name">
          <label>分类名称</label><br />
          <el-input
            v-model="editForm.name"
            autocomplete="off"
            placeholder="请输入分类名称"
          ></el-input>
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
  </div>
</template>

<script>
import { mapActions, mapState } from "vuex";
import { Message } from "element-ui";
export default {
  name: "Type",
  data() {
    return {
      inputValue: "",
      drawer: false,
      dialogFormVisible: false,
      editForm: {
        name: "",
      },
      addForm: {
        fatherName: "",
        name: "",
      },
      addRules: {
        name: [
          { required: true, message: "请输入分类名称", trigger: "blur" },
          {
            min: 2,
            max: 8,
            message: "分类名称长度在 2 到 8 位之间",
            trigger: "blur",
          },
        ],
      },
      editRules: {
        name: [
          { required: true, message: "请输入分类名称", trigger: "blur" },
          {
            min: 2,
            max: 8,
            message: "分类名称长度在 2 到 8 位之间",
            trigger: "blur",
          },
        ],
      },
      fatherList: [],
      editId: "",
    };
  },
  methods: {
    ...mapActions("Type", [
      "getTypeListById",
      "addType",
      "editType",
      "deleteType",
    ]),
    resetInput() {
      this.inputValue = "";
    },
    handleAdd() {
      this.drawer = true;
      this.getTypeListById(-1);
      this.fatherList = this.typeList;
    },
    handleEdit(row) {
      this.dialogFormVisible = true;
      this.editId = row.id;
    },
    confirmEdit() {
      const data = {
        id: this.editId,
        name: this.editForm.name,
      };
      this.editType(data).then(() => this.getTypeListById(-1));
      this.dialogFormVisible = false;
      Message({
        type: "success",
        message: "修改成功",
      }).catch((err) => {
        console.log(err);
        Message({
          message: "网络连接超时，请稍后重试",
          type: "error",
        });
      });
    },
    cancelEdit() {
      console.log("cancel");
      this.dialogFormVisible = false;
      this.editForm = {
        name: "",
      };
    },
    handleDelete(row) {
      this.deleteType(row.id)
        .then((val) => {
          if (val.code === 200) {
            return this.getTypeListById(-1).then(() => {
              Message({
                type: "success",
                message: "删除成功",
              });
            });
          } else {
            Message({
              type: "error",
              message: val.msg,
            });
          }
        })
        .catch((err) => {
          console.log(err);
          Message({
            message: "网络连接超时，请稍后重试",
            type: "error",
          });
        });
    },
    submitAddForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          let fatherId;
          this.fatherList.forEach((item) => {
            if (item.name === this.addForm.fatherName) {
              fatherId = item.id;
            }
          });
          const data = {
            fatherId: fatherId,
            name: this.addForm.name,
          };
          this.addType(data)
            .then((val) => {
              if (val.code === 500) {
                Message({
                  type: "error",
                  message: `${this.addForm.name}分类已存在`,
                });
              }
            })
            .then(() => {
              this.getTypeListById(-1);
              this.drawer = false;
              this.addForm = {
                fatherName: "",
                name: "",
              };
            })
            .catch((err) => {
              console.log(err);
              Message({
                message: "网络连接超时，请稍后重试",
                type: "error",
              });
            });
        } else {
          console.log("表单验证失败");
          return false;
        }
      });
    },
    handleViewLowLevel(row) {
      this.getTypeListById(row.id).catch((err) => {
        console.log(err);
        Message({
          message: "网络连接超时，请稍后重试",
          type: "error",
        });
      });
    },
    handleReset() {
      this.getTypeListById(-1).catch((err) => {
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
        fatherName: "",
        name: "",
      };
    },
  },
  computed: {
    ...mapState("Type", ["typeList", "loading"]),
  },
  mounted() {
    this.getTypeListById(-1).catch((err) => {
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
#type {
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
    .resetBtn {
      right: 100px;
    }
  }
  .table {
    margin-bottom: @interval;
    max-height: 870px;
  }
  .addCard {
    margin: 0 30px;
    .formBtnContainer {
      display: flex;
      justify-content: space-around;
      .el-button {
        flex: 1;
      }
    }
  }
}
</style>