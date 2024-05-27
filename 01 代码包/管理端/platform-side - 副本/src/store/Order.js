import { getOrdersApi, deliverOrderApi } from '../api';

export default {
    namespaced: true,
    state: {
        orders: [
            // {
            //     "id": 2,
            //     "customerId": 1,
            //     "addressBookId": 1,
            //     "status": 2,
            //     "orderTime": "2023-09-06T15:42:56",
            //     "checkoutTime": "2023-09-06T15:48:27",
            //     // 1 微信 2 支付宝
            //     "payMethod": 2,
            //     "amount": 1.00,
            //     "remark": null,
            //     "createTime": "2023-09-06T15:43:05",
            //     "isDeleted": 0
            // },
            // {
            //     "id": 3,
            //     "customerId": 1,
            //     "addressBookId": 2,
            //     "status": 2,
            //     "orderTime": "2023-09-06T15:49:48",
            //     "checkoutTime": "2023-09-06T15:50:09",
            //     "payMethod": 2,
            //     "amount": 2.00,
            //     "remark": null,
            //     "createTime": "2023-09-06T15:49:52",
            //     "isDeleted": 0
            // },
            // {
            //     "id": 3,
            //     "customerId": 1,
            //     "addressBookId": 2,
            //     "status": 1,
            //     "orderTime": "2023-09-06T15:49:48",
            //     "checkoutTime": "2023-09-06T15:50:09",
            //     "payMethod": 2,
            //     "amount": 2.00,
            //     "remark": null,
            //     "createTime": "2023-09-06T15:49:52",
            //     "isDeleted": 0
            // }
        ],
        pageData: {
            page: 1,
            limit: 5,
            total: 10
        },
        loading: true
    },
    actions: {
        getOrders(context) {
            const data = {
                page: context.state.pageData.page,
                limit: context.state.pageData.limit
            }
            return getOrdersApi(data)
                .then(({ data }) => {
                    context.commit('GET_ORDERS', data.data.records);
                    const temp = {
                        page: data.data.current,
                        limit: data.data.size,
                        total: data.data.total
                    }
                    context.commit('CHANGE_PAGE', temp);
                    context.state.loading = false;
                })
        },
        deliverOrder(context, value) {
            console.log('deliverOrder');
            console.log(value);
            return deliverOrderApi(value);
        }
    },
    mutations: {
        CHANGE_PAGE(state, value) {
            state.pageData = value;
        },
        GET_ORDERS(state, value) {
            state.orders = value;
        }
    }
}