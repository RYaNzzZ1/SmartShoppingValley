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
        <el-form-item prop="username">
          <el-input
            v-model="ruleForm.username"
            placeholder="请输入用户名"
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
      </el-form>
    </div>
  </div>
</template>

<script>
import { loginApi } from "../api";
import Cookies from "js-cookie";
import { Message } from "element-ui";
import { mapActions, mapState } from "vuex";
export default {
  name: "Login",
  data() {
    return {
      ruleForm: {
        username: "111",
        password: "12345678",
      },
      rules: {
        username: [
          { required: true, message: "请输入用户名", trigger: "blur" },
          {
            min: 3,
            max: 10,
            message: "用户名长度在 3 到 10 个字符之间",
            trigger: "blur",
          },
        ],
        password: [
          { required: true, message: "请输入密码", trigger: "blur" },
          {
            min: 8,
            max: 16,
            message: "密码长度在 8 到 16 位之间",
            trigger: "blur",
          },
        ],
      },
    };
  },
  methods: {
    ...mapActions("Admin", ["setAdmin"]),
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          loginApi(this.ruleForm)
            .then(({ data }) => {
              console.log("login");
              console.log(data);
              if (data.code == 200) {
                this.setAdmin(data.data.admin).then(() => {
                  Cookies.set("privileges", data.data.admin.privileges, {
                    expires: 1 / 24,
                  });
                });
                const token = data.data.token;
                Cookies.set("token", token, { expires: 1 / 24 });
                Message({
                  message: "登录成功!",
                  type: "success",
                });
                this.$router.push("/home");
              } else if (data.code === 500) {
                Message({
                  message: "密码或用户名错误！",
                  type: "error",
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
        } else {
          return false;
        }
      });
    },
  },
  computed: {
    ...mapState("Admin", ["admin"]),
  },
  mounted() {},
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
      width: 450px;
    }
    .el-form {
      width: 100%;
      .el-button {
        width: 100%;
      }
    }
  }
}
</style>