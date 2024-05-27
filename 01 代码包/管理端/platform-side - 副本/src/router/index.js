/**
 * router 存放路由
 */

import VueRouter from 'vue-router';
import Main from '../pages/MainContainer.vue';
import Home from '../pages/Home.vue';
import User from '../pages/User.vue';
import Shop from '../pages/Shop.vue';
import Goods from '../pages/Goods.vue';
import Type from '../pages/Type.vue';
import Admin from '../pages/Admin.vue';
import Login from '../pages/Login.vue';
import Coupon from '../pages/Coupon.vue';
import Discount from '../pages/Discount.vue';
import Order from '../pages/Order.vue';


export default new VueRouter({
    routes: [
        {
            name: 'login',
            path: '/login',
            component: Login
        },
        {
            name: 'main',
            path: '/',
            redirect: '/home',
            component: Main,
            children: [
                {
                    name: 'home',
                    path: '/home',
                    component: Home
                },
                {
                    name: 'user',
                    path: '/user',
                    component: User
                },
                {
                    name: 'shop',
                    path: '/shop',
                    component: Shop

                },
                {
                    name: 'goods',
                    path: '/goods',
                    component: Goods
                },
                {
                    name: 'type',
                    path: '/type',
                    component: Type
                },
                {
                    name: 'admin',
                    path: '/admin',
                    component: Admin
                },
                {
                    name: 'coupon',
                    path: '/coupon',
                    component: Coupon
                },
                {
                    name: 'discount',
                    path: '/discount',
                    component: Discount
                },
                {
                    name: 'order',
                    path: '/order',
                    component: Order
                },
            ]
        }
    ]
})