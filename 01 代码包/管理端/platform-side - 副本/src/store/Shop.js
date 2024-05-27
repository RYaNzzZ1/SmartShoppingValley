import { getShopApi, changeShopApi } from '../api';

export default {
    namespaced: true,
    state: {
        shop: {
            // id: 0,
            // name: '乐淘',
            // avatar: '',
            // description: '商品丰富，欢迎选购',
            // openingTime: '07:00',
            // closingTime: '24:00',
            // address: '江苏省南京市江宁区东南大学',
            // phone: '18456325410'
        }
    },
    actions: {
        getShop(context, data) {
            return getShopApi()
                .then(({ data }) => {
                    // console.log('shop');
                    // console.log(data);
                    context.commit('GET_SHOP', data.data);
                })
        },
        changeShop(context, data) {
            console.log(data);
            return changeShopApi(data);
        },
        change_Time_1(context, value) {
            context.commit('CHANGE_TIME_1', value);
        },
        change_Time_2(context, value) {
            context.commit('CHANGE_TIME_2', value);
        },
    },
    mutations: {
        GET_SHOP(state, value) {
            state.shop = value;
        },
        CHANGE_TIME_1(state, value) {
            state.shop.openingTime = value;
        },
        CHANGE_TIME_2(state, value) {
            state.shop.closingTime = value;
        }
    }
}