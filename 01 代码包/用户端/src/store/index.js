/**
 * store 存放 vuex 管理的数据
 */
import Vue from 'vue';
import Vuex from 'vuex';

import Order from './Order';
import Coupon from './Coupon';

Vue.use(Vuex);

export default new Vuex.Store({
    modules: {
        Order,
        Coupon
    }
})