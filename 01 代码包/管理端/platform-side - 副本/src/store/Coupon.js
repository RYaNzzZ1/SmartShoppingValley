import { getCouponApi, saveCouponStatusApi, addCouponApi, deleteCouponApi, editCouponApi } from '../api';

export default {
    namespaced: true,
    state: {
        coupon: [
            // {
            //     id: 0,
            //     conditions: 200,
            //     amount: 20,
            //     // 0 禁用 1 启用
            //     status: 1,
            //     startTime: '2023-8-12',
            //     endTime: '2024-9-12',
            //     createTime: '2023-8-11'
            // }
        ],
        pageData: {
            page: 1,
            limit: 5,
            total: 10
        },
        loading: true,
    },
    actions: {
        getCoupon(context) {
            const data = {
                page: context.state.pageData.page,
                limit: context.state.pageData.limit
            }
            return getCouponApi(data)
                .then(({ data }) => {
                    console.log('coupon');
                    console.log(data);
                    context.commit('GET_COUPON', data.data.records);
                    const temp = {
                        page: data.data.current,
                        limit: data.data.size,
                        total: data.data.total
                    }
                    context.state.loading = false;
                    context.commit('CHANGE_PAGE', temp)
                }).catch(err => {
                    console.log(err);
                })
        },
        saveCouponStatus(context, value) {
            return saveCouponStatusApi(value);
        },
        changePage(context, value) {
            context.commit('CHANGE_PAGE', value);
        },
        addCoupon(context, value) {
            return addCouponApi(value);
        },
        editCoupon(context, value) {
            return editCouponApi(value);
        },
        deleteCoupon(context, value) {
            return deleteCouponApi(value);
        }
    },
    mutations: {
        CHANGE_PAGE(state, value) {
            state.pageData = value;
        },
        GET_COUPON(state, value) {
            state.coupon = value;
        }
    }
}