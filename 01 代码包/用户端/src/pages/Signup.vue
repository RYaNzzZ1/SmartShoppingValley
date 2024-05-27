<template>
  <div id="signup">
    <div class="signupBox">
      <img class="logo" src="../assets/images/logo.png" alt="logo" />
      <el-form
        :model="ruleForm"
        status-icon
        :rules="rules"
        ref="ruleForm"
        label-width="50%"
        class="demo-ruleForm"
      >
        <el-form-item prop="email" label="邮箱">
          <el-input
            type="email"
            class="email"
            v-model="ruleForm.email"
            autocomplete="off"
            placeholder="请输入邮箱"
          ></el-input>
          <el-button @click="sendVerificationCode" :disabled="countDown > 0">{{
            countDown > 0 ? `${countDown}s可再次发送` : "获取验证码"
          }}</el-button>
        </el-form-item>
        <el-form-item prop="confirm" label="输入验证码">
          <el-input
            type="confirm"
            v-model="ruleForm.confirm"
            autocomplete="off"
            placeholder="输入验证码"
          ></el-input>
        </el-form-item>
        <el-form-item prop="password" label="密码">
          <el-input
            type="password"
            v-model="ruleForm.password"
            autocomplete="off"
            placeholder="请输入密码"
          ></el-input>
        </el-form-item>
        <el-form-item prop="repassword" label="再次输入密码">
          <el-input
            type="password"
            v-model="ruleForm.repassword"
            autocomplete="off"
            placeholder="请再次输入密码"
          ></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="danger" @click="submitForm('ruleForm')" class="left"
            >立即创建</el-button
          >
          <el-button class="right" @click="resetForm('ruleForm')"
            >返回首页</el-button
          >
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
import { registerApi } from "../api";
import { sendCodeApi } from "../api";
import Cookies from "js-cookie";
import { Message } from "element-ui";
export default {
  name: "Signup",
  data() {
    return {
      countDown: 0,
      codeToken: "",
      ruleForm: {
        email: "",
        password: "",
        repassword: "",
        confirm: "",
      },
      rules: {
        email: [
          { required: true, message: "请输入邮箱", trigger: "blur" },
          {
            validator: this.validateEmail,
            trigger: ["blur", "change"],
          },
        ],
        password: [
          { required: true, message: "请输入密码", trigger: "blur" },
          {
            validator: this.validatePassword,
            trigger: ["blur", "change"],
          },
        ],
        repassword: [
          { required: true, message: "请重新输入密码", trigger: "blur" },
          {
            validator: this.validateConfirmPassword,
            trigger: "blur",
          },
        ],
      },
    };
  },
  methods: {
    sendVerificationCode() {
      this.$refs.ruleForm.validateField("email", (valid) => {
        if (!valid) {
          this.countDown = 60; // 设置倒计时为60s
          // 发送请求到后端获取验证码
          const data = {
            username: this.ruleForm.email,
          };
          sendCodeApi(data).then(({ data }) => {
            if (data.code == 200) {
              // Cookies.remove("codeToken");
              // const codeToken = data.codeToken;
              // Cookies.set("codeToken", codeToken);
              this.codeToken = data.codeToken;
              Message({
                message: "发送成功!",
                type: "success",
                offset: "140",
              });
            } else if (data.code === 500) {
              Message({
                message: "发送失败!",
                type: "error",
                offset: "140",
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

    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          const data = {
            username: this.ruleForm.email,
            verificationCode: this.ruleForm.confirm,
            password: this.ruleForm.password,
            codeToken: this.codeToken,
          };
          registerApi(data).then(({ data }) => {
            if (data.code == 200) {
              Message({
                message: "注册成功!",
                type: "success",
                offset: "140",
              });
              this.$router.push("/emaillogin");
            } else if (data.code == 500) {
              Message({
                message: data.msg,
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

    validateEmail(rule, value, callback) {
      const regex = /^[\w-]+(\.[\w-]+)*@([\w-]+\.)+[a-zA-Z]{2,7}$/;
      if (regex.test(value)) {
        callback(); // 验证通过
      } else {
        callback(new Error("请输入正确的邮箱")); // 验证失败，返回错误信息
      }
    },
    validatePassword(rule, value, callback) {
      // 使用正则表达式检查密码是否包含数字和大小写字母，并且长度大于8位
      const regex = /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{8,}$/;
      if (regex.test(value)) {
        callback(); // 验证通过
      } else {
        callback(new Error("密码必须包含数字和大小写字母，且长度大于8位")); // 验证失败，返回错误信息
      }
    },

    validateConfirmPassword(rule, value, callback) {
      // 获取原密码字段的值
      const password = this.ruleForm.password;
      // 如果确认密码不为空，并且与原密码不一致，则返回错误消息
      if (value !== "" && value !== password) {
        callback(new Error("确认密码与原密码不一致"));
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
        callback(new Error("密码必须包含数字和大小写字母，且长度大于8位")); // 验证失败，返回错误信息
      }
    },

    resetForm(formName) {
      this.$refs[formName].resetFields();
      this.$router.push("/home");
    },
  },
};
</script>
  
<style scoped lang="less">
#signup {
  position: relative;
  width: 100%;
  height: 100%;
  background: url("../assets/images/bg_login_1.png") no-repeat;
  background-color: #f0f2f5;
  background-size: cover;

  .signupBox {
    display: flex;
    flex-direction: column;
    justify-content: space-around;
    align-items: center;
    position: absolute;
    top: 20%;
    left: 40%;
    transform: translate(-50%, 0);
    width: 800px;
    height: 504px;

    .logo {
      transform: translateX(44%);
      width: 440px;
    }

    .el-form {
      width: 100%;

      .el-button {
        width: 34%;
      }

      .el-input {
        width: 100%;
      }

      .email {
        width: 66%;
      }

      .form-item {
        display: flex;
        flex-direction: row;
      }

      .left {
        float: left;
        width: 48%;
      }

      .right {
        float: right;
        width: 48%;
      }
    }
  }
}
</style>