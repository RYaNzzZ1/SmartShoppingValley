<template>
  <div id="app">
    <router-view></router-view>
  </div>
</template>

<script>
import PasswordLogin from "./pages/PasswordLogin.vue";
import EmailLogin from "./pages/EmailLogin.vue";
import Home from "./pages/Home.vue";

export default {
  name: "App",
  components: {
    PasswordLogin,
    EmailLogin,
    Home
  },
};
</script>

<style lang="less">
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
  list-style: none;
}

#app {
  height: 100vh;
}
</style>
