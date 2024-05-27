<template>
  <div id="app">
    <router-view></router-view>
  </div>
</template>

<script>
import MainContainer from "./pages/MainContainer.vue";
import Login from "./pages/Login.vue";
import Cookies from "js-cookie";

export default {
  name: "App",
  components: {
    MainContainer,
    Login,
  },
};
</script>

<style lang="less">
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

#app {
  height: 100vh;
}
</style>
