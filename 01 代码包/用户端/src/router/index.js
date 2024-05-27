/**
 * router 存放路由
 */

import VueRouter from 'vue-router';
import Home from '../pages/Home.vue';
import Customer from '../pages/Customer.vue';
import PasswordLogin from '../pages/PasswordLogin.vue';
import EmailLogin from '../pages/EmailLogin.vue';
import Signup from '../pages/Signup.vue';
import HomeIndex from '../pages/HomeIndex.vue';
import HomeShow from '../pages/HomeShow.vue';
import GoodsList from '../pages/GoodsList.vue';
import Cart from '../pages/Cart.vue';
import GoodsDetail from '../pages/GoodsDetail.vue';
import CustomerData from '../pages/CustomerData.vue';
import CustomerOrder from '../pages/CustomerOrder.vue';
import CustomerAddress from '../pages/CustomerAddress.vue';
import SubmitOrder from '../pages/SubmitOrder.vue';

const router = new VueRouter({
  routes: [
    {
      name: 'main',
      path: '/',
      component: Home,
      redirect: '/home/show'
    },
    {
      name: 'passwordlogin',
      path: '/passwordlogin',
      component: PasswordLogin
    },
    {
      name: 'emaillogin',
      path: '/emaillogin',
      component: EmailLogin
    },
    {
      name: 'signup',
      path: '/signup',
      component: Signup
    },

    {
      name: 'home',
      path: '/home',
      component: Home,
      redirect: '/home/show',
      children: [
        {
          path: 'submitOrder',
          name: 'submitOrder',
          component: SubmitOrder
        },
        {
          path: 'show',
          name: 'homeshow',
          component: HomeShow,
          redirect: '/home/show/index',
          children: [
            {
              path: 'index',
              name: 'homeindex',
              component: HomeIndex
            },
            {
              path: 'goodsList/:typeId/:keyword',
              name: 'goodslist',
              component: GoodsList
            },
            {
              path: 'goodsList/:keyword',
              name: 'goodslist_2',
              component: GoodsList
            },

          ]
        },
        {
          path: '/home/goods/:id',
          name: 'goodsdetail',
          component: GoodsDetail
        },

        {
          path: 'personal',
          name: 'personal',
          component: Customer,
          redirect: 'personal/cart',
          children: [
            {
              path: 'cart',
              name: 'cart',
              component: Cart,
              meta: {
                requireLogin: true,
              },
            }, {
              path: 'personaldata',
              name: 'personaldata',
              component: CustomerData,
              meta: {
                requireLogin: true,
              },
            }, {
              path: 'personalorder',
              name: 'personalorder',
              component: CustomerOrder,
              meta: {
                requireLogin: true,
              },
            }, {
              path: 'personaladdress',
              name: 'personaladdress',
              component: CustomerAddress,
              meta: {
                requireLogin: true,
              },
            }, 
          ]
        }
      ]
    },

  ]
})

export default router;

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
