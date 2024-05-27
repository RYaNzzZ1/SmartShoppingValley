import Vue from 'vue'
import App from './App.vue'
import router from './router';
import VueRouter from 'vue-router';
import store from './store';
import {
  Container, Aside, Header, Main, Menu, Submenu, MenuItem,
  MenuItemGroup, Col, Row, Breadcrumb, BreadcrumbItem, Form, FormItem, Input, Button,
  Dropdown, DropdownMenu, DropdownItem, Card, Drawer, Table, TableColumn, Pagination,
  Dialog, Select, Option, Tag, Descriptions, DescriptionsItem, Avatar, Loading,
  TimeSelect, Upload, Checkbox, Cascader, DatePicker, TimePicker, ColorPicker, Statistic, Image,
  Carousel, CarouselItem, Divider
} from 'element-ui';
import Cookies from 'js-cookie';
import "echarts";
import ECharts from 'vue-echarts';

Vue.config.productionTip = false;
Vue.use(VueRouter);
Vue.use(Loading.directive);

Vue.component('ECharts', ECharts);
Vue.component(Container.name, Container);
Vue.component(Divider.name, Divider);
Vue.component(Aside.name, Aside);
Vue.component(Header.name, Header);
Vue.component(Main.name, Main);
Vue.component(Menu.name, Menu);
Vue.component(Submenu.name, Submenu);
Vue.component(MenuItem.name, MenuItem);
Vue.component(MenuItemGroup.name, MenuItemGroup);
Vue.component(Col.name, Col);
Vue.component(Row.name, Row);
Vue.component(Breadcrumb.name, Breadcrumb);
Vue.component(BreadcrumbItem.name, BreadcrumbItem);
Vue.component(Form.name, Form);
Vue.component(FormItem.name, FormItem);
Vue.component(Input.name, Input);
Vue.component(Button.name, Button);
Vue.component(Dropdown.name, Dropdown);
Vue.component(DropdownMenu.name, DropdownMenu);
Vue.component(DropdownItem.name, DropdownItem);
Vue.component(Card.name, Card);
Vue.component(Drawer.name, Drawer);
Vue.component(Table.name, Table);
Vue.component(TableColumn.name, TableColumn);
Vue.component(Pagination.name, Pagination);
Vue.component(Dialog.name, Dialog);
Vue.component(Select.name, Select);
Vue.component(Option.name, Option);
Vue.component(Tag.name, Tag);
Vue.component(Descriptions.name, Descriptions);
Vue.component(DescriptionsItem.name, DescriptionsItem);
Vue.component(Avatar.name, Avatar);
Vue.component(TimeSelect.name, TimeSelect);
Vue.component(Upload.name, Upload);
Vue.component(Checkbox.name, Checkbox);
Vue.component(Cascader.name, Cascader);
Vue.component(DatePicker.name, DatePicker);
Vue.component(TimePicker.name, TimePicker);
Vue.component(ColorPicker.name, ColorPicker);
Vue.component(Statistic.name, Statistic);
Vue.component(Image.name, Image);
Vue.component(Carousel.name, Carousel);
Vue.component(CarouselItem.name, CarouselItem);

router.beforeEach((to, from, next) => {
  const token = Cookies.get('token');
  if (!token && to.path !== '/login') {
    next({ name: 'login' });
  } else if (token && to.name === 'login') {
    next({ name: 'home' });
  } else {
    next();
  }
})


new Vue({
  render: h => h(App),
  router,
  store
}).$mount('#app')
