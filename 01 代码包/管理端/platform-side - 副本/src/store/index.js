/**
 * store 存放 vuex 管理的数据
 */
import Vue from 'vue';
import Vuex from 'vuex';

import ContainerAside from './ContainerAside';
import ContainerHeader from './ContainerHeader';
import User from './User';
import Type from './Type';
import Admin from './Admin';
import Shop from './Shop';
import Goods from './Goods';
import Coupon from './Coupon';
import Discount from './Discount';
import Order from './Order';
import Home from './Home';

Vue.use(Vuex);

export default new Vuex.Store({
    modules: {
        ContainerAside,
        ContainerHeader,
        User,
        Type,
        Admin,
        Shop,
        Goods,
        Coupon,
        Discount,
        Order,
        Home
    }
})