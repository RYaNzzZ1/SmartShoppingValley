<template>
  <div>
    <div class="CustomerData">
      <ul>
        <li>
          <span>用户头像</span>
          <img v-if="headimg == null" src="../assets/images/defaultUser.jpg" alt="headimg"
            @click="uploadVisible = true" />
          <img v-else :src="pictureGet(headimg)" alt="headimg" @click="uploadVisible = true" />
        </li>
        <li>
          <span>账号</span>
          <p>{{ email }}</p>
        </li>
        <li>
          <div class="form-floating">
            <input type="nickname" class="form-control" id="floatingInput" placeholder="aaa" v-model="nickname">
            <label for="floatingTextarea">昵称</label>
          </div>
        </li>
        <li>
          <div class="form-floating">
            <input type="phone" class="form-control" id="floatingInput" placeholder="aaa" v-model="phone">
            <label for="floatingTextarea">联系电话</label>
          </div>
        </li>
        <li>
          <div class="form-floating">
            <input type="disc" class="form-control" id="floatingInput" placeholder="aaa" v-model="description">
            <label for="floatingTextarea">个性签名</label>
          </div>
        </li>
        <li>
          <span>密码</span>
          <button @click="passwordVisible = true">修改密码</button>
        </li>
      </ul>
      <button @click="updateCustomerData" class="saveBtn">保存</button>
    </div>
    <el-dialog title="上传图片" :visible.sync="uploadVisible" width="30%" :before-close="handleClose">
      <el-upload class="upload-demo" drag :action="`${BaseURL}/ImageFile/image/upload/`" name="files" :limit="1"
        :headers="{ Authorization: `Bearer ${Cookies.get('token')}` }" :on-success="handle_success">
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <div class="el-upload__tip" slot="tip">只能上传jpg/png文件，且不超过500kb</div>
      </el-upload>
      <span slot="footer" class="dialog-footer">
        <el-button @click="uploadVisible = false">取 消</el-button>
        <el-button type="primary" @click="confirmHeadImg()">确 定</el-button>
      </span>
    </el-dialog>
    <el-dialog title="修改密码" :visible.sync="passwordVisible" width="30%" :before-close="handleClose">
      <el-form :model="ruleForm" status-icon :rules="rules" ref="ruleForm" label-width="120px" class="demo-ruleForm">
        <el-form-item prop="email" label="邮箱" style="position: relative;">
          <el-input type="email" class="email" v-model="ruleForm.email" autocomplete="off" placeholder="请输入邮箱"
            disabled></el-input>
          <el-button style="position: absolute;right: 0;top: 0;" @click="sendVerificationCode"
            :disabled="countDown > 0">{{ countDown > 0 ? `${countDown}s可再次发送` :
              '获取验证码' }}</el-button>
        </el-form-item>
        <el-form-item prop="confirm" label="输入验证码">
          <el-input type="confirm" v-model="ruleForm.confirm" autocomplete="off" placeholder="输入验证码"></el-input>
        </el-form-item>
        <el-form-item prop="password" label="密码">
          <el-input type="password" v-model="ruleForm.password" autocomplete="off" placeholder="请输入密码"></el-input>
        </el-form-item>
        <el-form-item prop="repassword" label="再次输入密码">
          <el-input type="password" v-model="ruleForm.repassword" autocomplete="off" placeholder="再次输入密码"></el-input>
        </el-form-item>
        <el-form-item slot="footer" class="dialog-footer">
          <el-button @click="resetForm('ruleForm')">取 消</el-button>
          <el-button type="primary" @click="submitForm('ruleForm')">确 定</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>
  </div>
</template>
  
<script>
import { getCustomerDataApi, updateCustomerDataApi, editPasswordApi, sendCodeApi } from '../api/index';
import Popup from '../components/Popup';
import { Message } from "element-ui";
import { BaseURL } from '../utils/request';
import Cookies from 'js-cookie';

export default {
  name: 'CustomerData',
  components: {
    Popup
  },
  computed: {

  },
  data() {
    return {
      countDown: 0,
      passwordVisible: false,
      Cookies: Cookies,
      BaseURL: BaseURL,
      uploadVisible: false,
      id: '',
      headimg: '',
      email: localStorage.getItem("userEmail"),
      nickname: '',
      description: '',
      phone: '',
      popupShow: false,
      oldPwd: '',
      newPwd: '',
      confirmPwd: '',
      tempimg: '',
      ruleForm: {
        email: localStorage.getItem("userEmail"),
        password: '',
        repassword: '',
        confirm: '',
      },
      rules: {
        email: [
          { required: true, message: "请输入邮箱", trigger: "blur" },
          {
            validator: this.validateEmail,
            trigger: ['blur', 'change'],
          },
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' },
          {
            validator: this.validatePassword,
            trigger: ['blur', 'change'],
          },
        ],
        repassword: [
          { required: true, message: '请重新输入密码', trigger: 'blur' },
          {
            validator: this.validateConfirmPassword,
            trigger: 'blur',
          },
        ],
      },
    }
  },

  methods: {
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          const data = {
            username: this.ruleForm.email,
            verificationCode: this.ruleForm.confirm,
            password: this.ruleForm.password,
            codeToken: this.codeToken,
          }
          editPasswordApi(data).then(({ data }) => {
            if (data.code == 200) {
              this.passwordVisible = false;
              Message({
                message: "修改成功!",
                type: "success",
              });
            } else if (data.code == 500) {
              Message({
                message: data.msg,
                type: "error",
              });
            }
          });
        } else {
          return false;
        }
      });
    },

    validateEmail(rule, value, callback) {
      const regex = /^[\w-]+(\.[\w-]+)*@([\w-]+\.)+[a-zA-Z]{2,7}$/;
      if (regex.test(value)) {
        callback(); // 验证通过
      } else {
        callback(new Error('请输入正确的邮箱')); // 验证失败，返回错误信息
      }
    },
    validatePassword(rule, value, callback) {
      // 使用正则表达式检查密码是否包含数字和大小写字母，并且长度大于8位
      const regex = /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{8,}$/;
      if (regex.test(value)) {
        callback(); // 验证通过
      } else {
        callback(new Error('密码必须包含数字和大小写字母，且长度大于8位')); // 验证失败，返回错误信息
      }
    },

    validateConfirmPassword(rule, value, callback) {
      // 获取原密码字段的值
      const password = this.ruleForm.password;
      // 如果确认密码不为空，并且与原密码不一致，则返回错误消息
      if (value !== '' && value !== password) {
        callback(new Error('确认密码与原密码不一致'));
      } else {
        callback();
      }
    },
    validatePassword(rule, value, callback) {
      // 使用正则表达式检查密码是否包含数字和大小写字母，并且长度大于8位
      const regex = /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{8,}$/;
      if (regex.test(value)) {
        callback(); // 验证通过
      } else {
        callback(new Error('密码必须包含数字和大小写字母，且长度大于8位')); // 验证失败，返回错误信息
      }
    },

    resetForm(formName) {
      this.$refs[formName].resetFields();
      this.passwordVisible = false;
    },
    sendVerificationCode() {
      this.$refs.ruleForm.validateField('email', valid => {
        if (!valid) {
          this.countDown = 60; // 设置倒计时为60s
          // 发送请求到后端获取验证码
          const data = {
            username: this.ruleForm.email,
          }
          sendCodeApi(data).then(({ data }) => {
            if (data.code == 200) {
              // Cookies.remove("codeToken");
              // const codeToken = data.codeToken;
              // Cookies.set("codeToken", codeToken);
              this.codeToken = data.codeToken;
              Message({
                message: "发送成功!",
                type: "success",
              });
            } else if (data.code === 500) {
              Message({
                message: "发送失败!",
                type: "error",
              });
            }
          });
          // 获取验证码成功后
          const timer = setInterval(() => {
            this.countDown--; // 每隔一秒减少countDown的值
            if (this.countDown === 0) {
              clearInterval(timer); // 清除定时器
            }
          }, 1000);
        }
      });
    },
    handle_success(response) {
      this.headimg = response.data;
      console.log(response.data);
      console.log(this.headimg);
    },
    confirmHeadImg() {
      this.uploadVisible = false;
    },
    handleClose(done) {
      this.$confirm('确认关闭？')
        .then(_ => {
          done();
        })
        .catch(_ => { });
    },
    pictureGet(url) {
      return BaseURL + "/ImageFile/image/download" + "?url=" + url;
    },
    getCustomerData() {
      getCustomerDataApi().then(({ data }) => {
        if (data.code == 200) {
          this.headimg = data.data.avatar;
          this.nickname = data.data.name;
          this.phone = data.data.phone;
          this.description = data.data.description;
        } else if (data.code === 500) {
          Message({
            message: "获取失败！",
            type: "error",
          });
        }
      });
    },
    updateCustomerData() {
      if (Array.isArray(this.headimg)) {
        this.tempimg = this.headimg[0];
      }
      const data = {
        avatar: this.tempimg,
        name: this.nickname,
        phone: this.phone,
        description: this.description,
      }
      updateCustomerDataApi(data).then(({ data }) => {
        if (data.code == 200) {
          Message({
            message: "修改成功！",
            type: "success",
          });
        } else if (data.code === 500) {
          Message({
            message: "修改失败！",
            type: "error",
          });
        }
      });
    },
    closePopup() {
      this.popupShow = false;
    },
    showPopup() {
      this.popupShow = true;
    },
  },

  mounted() {
    this.getCustomerData();
  }
}
</script>
  
<style scoped lang="less">
@import "../assets/css/var.less";

.CustomerData {
  ul {
    width: 100%;
    overflow: hidden;

    li {
      margin-bottom: 30px;

      &:first-child {
        height: 60px;
        line-height: 60px;

        span {
          position: relative;
          bottom: 18px;
        }
      }

      span {
        display: inline-block;
        width: 100px;
        height: 20px;
      }



      img {
        display: inline-block;
        width: 60px;
        height: 60px;
        border-radius: 50%;
      }

      p {
        display: inline-block;
      }

      input {
        width: 500px;
        border-bottom: 2px solid @thirdColor;
      }

      .long {}

      button {
        background-color: white;
        border: 1px solid @thirdColor;
        color: @thirdColor;
        width: 80px;
        height: 30px;
      }
    }
  }

  .demo-ruleForm {
    float: left;
    text-align: left;
    position: flex;
    width: 150px;
  }

  .saveBtn {
    background-color: @thirdColor;
    border: none;
    color: white;
    width: 110px;
    height: 35px;
    display: block;
    margin: 10px auto;
  }

  .popupContent {
    padding: 20px;

    input {
      display: block;
      border: none;
      border-bottom: 1px solid #313541;
      margin-bottom: 16px;
      font-size: 13px;
      padding: 5px;
      width: 200px;
    }

    button {
      background-color: #333333;
      border: none;
      color: white;
      width: 80px;
      height: 32px;
      display: block;
      margin: 20px auto 5px;
    }
  }

  /deep/.el-button {
    color: #fff;
    background-color: rgb(178, 161, 121);
    border-color: rgb(178, 161, 121);
  }
}</style>