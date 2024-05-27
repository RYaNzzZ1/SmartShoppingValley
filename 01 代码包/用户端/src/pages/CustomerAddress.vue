<template>
  <div>
    <el-form ref="form" :model="form" label-width="80px" :rules="rules" class="addressForm">
      <el-form-item label="具体省市" prop="cityCode">
        <el-select v-model="form.provinceCode" @change="provinceChange" placeholder="省" style="margin-right: 20px;">
          <el-option v-for="it in provinces" :key="it.id" :label="it.name" :value="it.id"></el-option>
        </el-select>
        <el-select v-model="form.cityCode" @change="cityChange" placeholder="市" class="address_select">
          <el-option v-for="it in cityList" :key="it.id" :label="it.name" :value="it.id"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="详细地址" prop="detailAddress">
        <el-input v-model="form.detailAddress" placeholder="请输入内容"></el-input>
      </el-form-item>
      <el-form-item label="收件人" prop="name">
        <el-input v-model="form.name" placeholder="请输入内容"></el-input>
      </el-form-item>
      <el-form-item label="性别" prop="sex">
        <el-radio-group v-model="form.sex">
          <el-radio label="男"></el-radio>
          <el-radio label="女"></el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="联系电话" prop="telephone">
        <el-input v-model="form.telephone" placeholder="请输入内容"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button class="createAddressBtn" @click="submitForm('form')">创建地址</el-button>
        <el-button @click="resetForm('form')">重置</el-button>
      </el-form-item>
    </el-form>

    <el-dialog title="编辑地址" :visible.sync="dialogFormVisible">
      <el-form ref="tempform" :model="tempform" :rules="rules" class="editAddressForm">
        <el-form-item label="具体省市" prop="cityCode">
          <el-select v-model="tempform.provinceCode" @change="tempprovinceChange" placeholder="省"
            style="margin-right: 20px;">
            <el-option v-for="it in provinces" :key="it.id" :label="it.name" :value="it.id"></el-option>
          </el-select>
          <el-select v-model="tempform.cityCode" @change="tempcityChange" placeholder="市" class="address_select">
            <el-option v-for="it in cityList" :key="it.id" :label="it.name" :value="it.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="详细地址" prop="detailAddress">
          <el-input v-model="tempform.detailAddress" placeholder="请输入内容"></el-input>
        </el-form-item>
        <el-form-item label="收件人" prop="name">
          <el-input v-model="tempform.name" placeholder="请输入内容"></el-input>
        </el-form-item>
        <el-form-item label="性别" prop="sex">
          <el-radio-group v-model="tempform.sex">
            <el-radio label="男"></el-radio>
            <el-radio label="女"></el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="联系电话" prop="telephone">
          <el-input v-model="tempform.telephone" placeholder="请输入内容"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button @click="modifyForm('tempform')" class="addressBtn">确 定</el-button>
      </div>
    </el-dialog>
    <br><br>
    <h5>地址列表</h5>
    <div v-if="address_list.length > 0">
      <div v-for="(item, index) in address_list" :key="index" class="addresslist">
        <el-descriptions :column="4" direction="horization">
          <el-descriptions-item label="姓名" label-class-name="my-label" content-class-name="my-content">{{
            item.consignee
          }}</el-descriptions-item>
          <el-descriptions-item label="电话" label-class-name="my-label" content-class-name="my-content">{{
            item.phone
          }}</el-descriptions-item>
          <el-descriptions-item label="收货地址" label-class-name="my-label" content-class-name="my-content">
            {{ item.address }}
          </el-descriptions-item>
          <br>
          <!-- <el-descriptions-item> -->

          <!-- </el-descriptions-item> -->
        </el-descriptions>
        <div style="transform: translate(35%,0);">
          <el-button class="addressBtn" @click="modify(index)">编辑</el-button>
          <el-button @click="del(index)">删除</el-button>
          <el-button>
            <em class="Default" v-if="item.isDefault">默认地址</em>
            <em v-else @click="setDefault(index)">设为默认</em>
          </el-button>
        </div>

      </div>
    </div>
    <p class="emptyTips" v-else>地址还是空滴~</p>
  </div>
</template>

<script>
import {
  ProvincelistApi,
  listByProvinceIdApi,
  showAddressApi,
  addAddressApi,
  deleteAddressApi,
  modifyAddressApi,
  setDefaultApi,
} from "../api/index";
import { Message } from "element-ui";

export default {
  data() {
    return {
      dialogFormVisible: false,
      address_list: [],
      form: {
        provinceCode: "",
        cityCode: "",
        sex: "",
        telephone: "",
        detailAddress: "",
        name: "",
      },
      tempform: {
        id: "",
        provinceCode: "",
        cityCode: "",
        sex: "",
        telephone: "",
        detailAddress: "",
        name: "",
      },
      rules: {
        name: [{ required: true, message: "请输入姓名", trigger: "blur" }],
        cityCode: [
          { required: true, message: "请选择省市", trigger: "change" },
        ],
        sex: [{ required: true, message: "请选择性别", trigger: "change" }],
        telephone: [{ required: true, message: "请填写电话", trigger: "blur" }],
        detailAddress: [
          { required: true, message: "请填写具体地址", trigger: "blur" },
        ],
      },
      provinces: [], // 初始化为一个空数组
      cityList: [], // 初始化为一个空数组
      provincesId: null,
      sexfinal: 1,
    };
  },
  methods: {
    modify(index) {
      this.tempform.id = this.address_list[index].id;
      this.dialogFormVisible = true;
    },
    getProvincelist() {
      ProvincelistApi().then(({ data }) => {
        if (data.code == 200) {
          this.provinces = data.data;
        } else if (data.code === 500) {
          Message({
            message: "获取失败！",
            type: "error",
            offset: "140",
          });
        }
      });
    },
    listByProvinceIdlist(id) {
      listByProvinceIdApi(id).then(({ data }) => {
        if (data.code == 200) {
          this.cityList = data.data;
        } else if (data.code === 500) {
          Message({
            message: "获取失败！",
            type: "error",
            offset: "140",
          });
        }
      });
    },
    // 操作：选择省，把省下的市的数据筛出来
    provinceChange(event) {
      this.form.cityCode = "";
      for (let i = 0; i < this.provinces.length; i++) {
        if (event === this.provinces[i].id) {
          this.provincesId = this.provinces[i].id;
          this.form.provinceCode = this.provinces[i].name;
        }
      }
      this.cityList = this.listByProvinceIdlist(this.provincesId);
    },

    cityChange(event) {
      for (let i = 0; i < this.cityList.length; i++) {
        if (event == this.cityList[i].id)
          this.form.cityCode = this.cityList[i].name;
      }
    },
    tempprovinceChange(event) {
      this.form.cityCode = "";
      for (let i = 0; i < this.provinces.length; i++) {
        if (event === this.provinces[i].id) {
          this.provincesId = this.provinces[i].id;
          this.tempform.provinceCode = this.provinces[i].name;
        }
      }
      this.cityList = this.listByProvinceIdlist(this.provincesId);
    },
    tempcityChange(event) {
      for (let i = 0; i < this.cityList.length; i++) {
        if (event == this.cityList[i].id)
          this.tempform.cityCode = this.cityList[i].name;
      }
    },
    submitForm(formName) {
      if (this.form.sex == "男") this.sexfinal = 1;
      else this.sexfinal = 2;
      this.$refs[formName].validate((valid) => {
        if (valid) {
          const data = {
            consignee: this.form.name,
            sex: this.sexfinal,
            phone: this.form.telephone,
            address:
              this.form.provinceCode +
              this.form.cityCode +
              this.form.detailAddress,
          };
          addAddressApi(data).then(({ data }) => {
            if (data.code == 200) {
              this.showAddress();
              Message({
                message: "添加成功!",
                type: "success",
                offset: "140",
              });
            } else if (data.code == 500) {
              Message({
                message: "添加失败！",
                type: "error",
                offset: "140",
              });
            }
          });
        } else {
          return false;
        }
      });
    },

    modifyForm(formName) {
      if (this.tempform.sex == "男") this.sexfinal = 1;
      else this.sexfinal = 2;
      this.$refs[formName].validate((valid) => {
        if (valid) {
          const data = {
            id: this.tempform.id,
            consignee: this.tempform.name,
            sex: this.sexfinal,
            phone: this.tempform.telephone,
            address:
              this.tempform.provinceCode +
              this.tempform.cityCode +
              this.tempform.detailAddress,
          };
          modifyAddressApi(data).then(({ data }) => {
            if (data.code == 200) {
              this.showAddress();
              this.dialogFormVisible = false;
              Message({
                message: "修改成功!",
                type: "success",
                offset: "140",
              });
            } else if (data.code == 500) {
              this.dialogFormVisible = false;
              Message({
                message: "修改失败！",
                type: "error",
                offset: "140",
              });
            }
          });
        } else {
          return false;
        }
      });
    },

    resetForm(formName) {
      this.$refs[formName].resetFields();
    },
    //设置默认地址
    setDefault(index) {
      const data = {
        id: this.address_list[index].id,
      };
      setDefaultApi(data).then(({ data }) => {
        if (data.code == 200) {
          this.showAddress();
          Message({
            message: "设置成功！",
            type: "success",
            offset: "140",
          });
        } else if (data.code === 500) {
          Message({
            message: "设置失败！",
            type: "error",
            offset: "140",
          });
        }
      });
    },

    showAddress() {
      showAddressApi().then(({ data }) => {
        if (data.code == 200) {
          this.address_list = data.data;
        } else if (data.code === 500) {
          Message({
            message: "获取失败！",
            type: "error",
            offset: "140",
          });
        }
      });
    },

    // 点击列表中每个地址的编辑时
    edit_address(index) {
      // 让渲染的add_address=此时选中的地址(index)
      let copy_obj = JSON.parse(JSON.stringify(this.address_list[index])); //复制列表里此地址obj
      copy_obj.title = "编辑地址";
      copy_obj.addr_id = index;
      this.add_address = JSON.parse(JSON.stringify(copy_obj));
      console.log(this.add_address);
      console.log("下方编辑按钮" + this.add_address.addr_id);
    },

    del(index) {
      const data = {
        addrId: this.address_list[index].id,
      };
      deleteAddressApi(data).then(({ data }) => {
        if (data.code == 200) {
          this.showAddress();
          Message({
            message: "删除成功！",
            type: "success",
            offset: "140",
          });
        } else if (data.code === 500) {
          Message({
            message: "删除失败！",
            type: "error",
            offset: "140",
          });
        }
      });
    },
  },
  mounted() {
    this.getProvincelist();
    this.showAddress();
  },
};
</script>

<style scoped lang="less">
@import "../assets/css/var.less";

.Default {
  color: orange;
}

.emptyTips {
  width: 200px;
  text-align: center;
  display: block;
  margin: 30px auto;
  color: @thirdColor;
  font-size: 20px;
}

.addresslist {
  border-top: 1px solid darkgray;
}

h5 {
  text-align: left;
  font-weight: bold;
}

.createAddressBtn {
  color: #fff;
  background-color: rgb(178, 161, 121);
  border-color: rgb(178, 161, 121);
}

.el-button,
.addressBtn {
  color: #fff;
  background-color: rgb(178, 161, 121);
  border-color: rgb(178, 161, 121);

}

/deep/.my-label {
  font-size: 18px;
  color: #989797;
}

/deep/.my-content {
  padding-left: 10px;
  font-size: 18px;
}

.addresslist {
  padding-top: 20px;
}

/deep/.addressForm {
  padding-right: 100px;
}

.editAddressForm {
  padding-left: 50px;
  padding-right: 50px;
}
</style>
