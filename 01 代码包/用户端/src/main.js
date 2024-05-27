import Vue from 'vue'
import App from './App.vue'
import router from './router';
import VueRouter from 'vue-router';
import store from './store';
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import axios from 'axios';
// import bootstrap from 'bootstrap';
import 'bootstrap/dist/css/bootstrap.css';
import 'bootstrap';
import Cookies from 'js-cookie';


Vue.config.productionTip = false;
// axios.defaluts.withCredentials = true;
Vue.use(VueRouter);
Vue.use(ElementUI);
// Vue.use(bootstrap);

// Vue.component(Container.name, Container);
// Vue.component(Aside.name, Aside);
// Vue.component(Header.name, Header);
// Vue.component(Main.name, Main);
// Vue.component(Menu.name, Menu);
// Vue.component(Submenu.name, Submenu);
// Vue.component(MenuItem.name, MenuItem);
// Vue.component(MenuItemGroup.name, MenuItemGroup);
// Vue.component(Col.name, Col);
// Vue.component(Row.name, Row);
// Vue.component(Breadcrumb.name, Breadcrumb);
// Vue.component(BreadcrumbItem.name, BreadcrumbItem);
// Vue.component(Form.name, Form);
// Vue.component(FormItem.name, FormItem);
// Vue.component(Input.name, Input);
// Vue.component(Button.name, Button);
// Vue.component(Dropdown.name, Dropdown);
// Vue.component(DropdownMenu.name, DropdownMenu);
// Vue.component(DropdownItem.name, DropdownItem);
// Vue.component(Card.name, Card);
// Vue.component(Drawer.name, Drawer);
// Vue.component(Table.name, Table);
// Vue.component(TableColumn.name, TableColumn);
// Vue.component(Pagination.name, Pagination);

// router.beforeEach((to, from, next) => {
//   const token = Cookies.get('token');
//   if (!token && to.path !== '/login') {
//     next({ name: 'login' });
//   } else if (token && to.name === 'login') {
//     next({ name: 'home' });
//   } else {
//     next();
//   }
// })

new Vue({
  render: h => h(App),
  router,
  store
}).$mount('#app')


// 添加全局前置守卫
router.beforeEach((to, from, next) => {
  // 检查当前路由是否与目标路由相同
  if (to.path === from.path) {
    // 如果相同，不触发导航
    next(false);
  } else {
    // 如果不同，继续导航
    next();
  }
})