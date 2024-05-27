<template>
  <div id="login">
    <div class="loginBox">
      <img class="logo" src="../assets/images/logo.png" alt="logo" />
      <el-form
        :model="ruleForm"
        status-icon
        :rules="rules"
        ref="ruleForm"
        label-width="0"
        class="demo-ruleForm"
      >
        <el-form-item prop="email">
          <el-input
            type="email"
            class="email"
            v-model="ruleForm.email"
            autocomplete="off"
            placeholder="请输入邮箱"
          ></el-input>
          <!-- <el-button @click="confirmEmail" class="sendconfirm">发送验证码</el-button> -->
          <el-button
            @click="sendVerificationCode"
            class="sendconfirm"
            :disabled="countDown > 0"
            >{{
              countDown > 0 ? `${countDown}s可再次发送` : "获取验证码"
            }}</el-button
          >
        </el-form-item>
        <el-form-item prop="confirm">
          <el-input
            type="confirm"
            v-model="ruleForm.confirm"
            autocomplete="off"
            placeholder="请输入验证码"
          ></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="danger" @click="submitForm('ruleForm')"
            >登录</el-button
          >
        </el-form-item>
        <el-form-item>
          <el-button type="text" @click="goSignup" class="left"
            >用户注册</el-button
          >
          <el-button type="text" @click="goPassword" class="right"
            >密码登录</el-button
          >
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>
  
<script>
import { loginByCodeApi } from "../api";
import { sendCodeApi } from "../api";
import Cookies from "js-cookie";
import { Message } from "element-ui";
export default {
  name: "Login",
  data() {
    return {
      countDown: 0,
      codeToken: "",
      ruleForm: {
        // ---------------------------命名待定---------------------------
        email: "",
        confirm: "",
      },
      rules: {
        email: [
          { required: true, message: "请输入正确的邮箱", trigger: "blur" },
          {
            validator: this.validateEmail,
            trigger: ["blur", "change"],
          },
        ],
        confirm: [{ required: true, message: "请输入验证码", trigger: "blur" }],
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
                message: "发送失败！",
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
    validateEmail(rule, value, callback) {
      const regex = /^[\w-]+(\.[\w-]+)*@([\w-]+\.)+[a-zA-Z]{2,7}$/;
      if (regex.test(value)) {
        callback(); // 验证通过
      } else {
        callback(new Error("请输入正确的邮箱")); // 验证失败，返回错误信息
      }
    },
    goSignup() {
      this.$router.push("/signup");
    },
    goPassword() {
      this.$router.push("/passwordlogin");
    },
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          localStorage.setItem("userEmail", this.ruleForm.email);
          const data = {
            username: this.ruleForm.email,
            verificationCode: this.ruleForm.confirm,
            codeToken: this.codeToken,
          };
          loginByCodeApi(data).then(({ data }) => {
            if (data.code == 200) {
              const token = data.data.token;
              Cookies.set("token", token);
              this.$router.push("/home");
            } else if (data.code === 500) {
              Message({
                message: "验证码错误或用户名不存在！",
                type: "error",
                offset: "140",
              });
            }
          });
        } else {
          console.log("表单验证失败");
          return false;
        }
      });
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
    },
  },
  computed: {},
};
</script>
  
<style scoped lang="less">
#login {
  position: relative;
  width: 100%;
  height: 100%;
  background: url("../assets/images/bg_login_1.png") no-repeat;
  background-color: #f0f2f5;
  background-size: cover;

  .loginBox {
    display: flex;
    flex-direction: column;
    justify-content: space-around;
    align-items: center;
    position: absolute;
    top: 30%;
    left: 50%;
    transform: translate(-50%, 0);
    width: 386px;
    height: 404px;

    .logo {
      width: 440px;
    }

    .el-form {
      width: 100%;

      .el-button {
        width: 100%;
      }

      .email {
        width: 66%;
      }

      .sendconfirm {
        width: 34%;
      }

      .left {
        float: left;
        width: 20%;
      }

      .right {
        float: right;
        width: 20%;
      }
    }
  }
}
</style>