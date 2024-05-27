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
        <el-form-item prop="userName">
          <el-input
            v-model="ruleForm.email"
            placeholder="请输入邮箱"
            autocomplete="off"
          ></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input
            type="password"
            v-model="ruleForm.password"
            autocomplete="off"
            placeholder="请输入密码"
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
          <el-button type="text" @click="goEmail" class="right"
            >验证码登录</el-button
          >
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
import { loginByPWApi } from "../api";
import Cookies from "js-cookie";
import { Message } from "element-ui";
export default {
  name: "PasswordLogin",
  data() {
    return {
      ruleForm: {
        // ---------------------------命名待定---------------------------
        email: "",
        password: "",
      },
      rules: {
        email: [{ required: true, message: "请输入邮箱", trigger: "blur" }],
        password: [{ required: true, message: "请输入密码", trigger: "blur" }],
      },
    };
  },
  methods: {
    goSignup() {
      this.$router.push("/signup");
    },
    goEmail() {
      this.$router.push("/emaillogin");
    },
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          localStorage.setItem("userEmail", this.ruleForm.email);
          // 验证完成发送登录请求, 接收并存储 token
          const data = {
            username: this.ruleForm.email,
            password: this.ruleForm.password,
          };
          loginByPWApi(data).then(({ data }) => {
            if (data.code == 200) {
              const token = data.data.token;
              Cookies.set("token", token);
              this.$router.push("/home");
            } else if (data.code === 500) {
              Message({
                message: "密码或用户名错误！",
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
  },
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