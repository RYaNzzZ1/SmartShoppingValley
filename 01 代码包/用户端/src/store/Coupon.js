import {
    getCouponsApi,
    getReceivedCouponsApi,
    receiveCouponApi
}
    from '../api';

export default {
    namespaced: true,
    state: {
        coupons: [
            // {
            //     "id": 9,
            //     "conditions": 60,
            //     "amount": 20,
            //     "status": 1,
            //     "startTime": "2023-09-01T14:22:21",
            //     "endTime": "2023-09-14T14:22:21",
            //     "createTime": "2023-09-04T14:23:19"
            // },
            // {
            //     "id": 8,
            //     "conditions": 60,
            //     "amount": 10,
            //     "status": 1,
            //     "startTime": "2023-09-01T14:22:21",
            //     "endTime": "2023-09-14T14:22:21",
            //     "createTime": "2023-09-04T14:23:14"
            // },
            // {
            //     "id": 5,
            //     "conditions": 200,
            //     "amount": 10,
            //     "status": 1,
            //     "startTime": "2023-09-01T14:22:21",
            //     "endTime": "2023-09-14T14:22:21",
            //     "createTime": "2023-09-04T14:22:57"
            // }
        ],
        receivedCoupons: [
            // {
            //     "id": 7,
            //     "conditions": 60,
            //     "amount": 10,
            //     "status": 1,
            //     "startTime": "2023-09-01T14:22:21",
            //     "endTime": "2023-09-14T14:22:21",
            //     "createTime": "2023-09-04T14:23:14"
            // },
            // {
            //     "id": 3,
            //     "conditions": 200,
            //     "amount": 10,
            //     "status": 1,
            //     "startTime": "2023-09-01T14:22:21",
            //     "endTime": "2023-09-14T14:22:21",
            //     "createTime": "2023-09-04T14:22:57"
            // }
        ],

    },
    actions: {
        getCoupons(context) {
            return getCouponsApi().then(({ data }) => {
                context.commit('GET_COUPONS', data.data);
            });
        },
        getReceivedCoupons(context) {
            return getReceivedCouponsApi().then(({ data }) => {
                context.commit('GET_RECEIVED_COUPONS', data.data);
            });
        },
        receiveCoupon(context, id) {
            return receiveCouponApi({ id: id });
        },

    },
    mutations: {
        GET_COUPONS(state, value) {
            state.coupons = value;
            // console.log("state.coupons");
            // console.log(state.coupons);
        },
        GET_RECEIVED_COUPONS(state, value) {
            state.receivedCoupons = value;
        },
    }
}