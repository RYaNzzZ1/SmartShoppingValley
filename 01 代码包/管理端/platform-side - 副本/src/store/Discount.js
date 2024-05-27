import {
    getDiscountApi, saveDiscountStatusApi, addDiscountApi, deleteDiscountApi,
    editDiscountApi, getParticipatingApi, cancelDiscountApi, getNotParticipatingApi,
    setDiscountApi
} from '../api';

export default {
    namespaced: true,
    state: {
        discount: [
            // {
            //     id: 0,
            //     adminId: 1,
            //     name: '618折扣',
            //     value: '80%',
            //     status: 1,
            //     startTime: '2023-8-4',
            //     endTime: '2023-9-14'
            // },
            // {
            //     id: 1,
            //     adminId: 1,
            //     name: '11.11折扣',
            //     value: '80%',
            //     status: 1,
            //     startTime: '2023-8-4',
            //     endTime: '2023-9-14'
            // },
            // {
            //     id: 2,
            //     adminId: 1,
            //     name: '春节折扣',
            //     value: '80%',
            //     status: 1,
            //     startTime: '2023-8-4',
            //     endTime: '2023-9-14'
            // }
        ],
        participating: [
            // {
            //     "id": 2,
            //     "categoryId": 2,
            //     "name": "鸿星尔克",
            //     "price": 99.99,
            //     "num": 50,
            //     "description": null,
            //     "status": 1,
            //     "createTime": "2023-09-04T16:28:06",
            //     "isDeleted": 0
            // },
            // {
            //     "id": 1,
            //     "categoryId": 2,
            //     "name": "中国李宁",
            //     "price": 99.99,
            //     "num": 50,
            //     "description": null,
            //     "status": 1,
            //     "createTime": "2023-09-04T16:28:06",
            //     "isDeleted": 0
            // }, {
            //     "id": 3,
            //     "categoryId": 2,
            //     "name": "特步",
            //     "price": 99.99,
            //     "num": 50,
            //     "description": null,
            //     "status": 1,
            //     "createTime": "2023-09-04T16:28:06",
            //     "isDeleted": 0
            // },
        ],
        notParticipating: [
            {
                "id": 2,
                "categoryId": 2,
                "name": "鸿星尔克",
                "price": 99.99,
                "num": 50,
                "description": null,
                "status": 1,
                "createTime": "2023-09-04T16:28:06",
                "isDeleted": 0
            },
            {
                "id": 1,
                "categoryId": 2,
                "name": "中国李宁",
                "price": 99.99,
                "num": 50,
                "description": null,
                "status": 1,
                "createTime": "2023-09-04T16:28:06",
                "isDeleted": 0
            }, {
                "id": 3,
                "categoryId": 2,
                "name": "特步",
                "price": 99.99,
                "num": 50,
                "description": null,
                "status": 1,
                "createTime": "2023-09-04T16:28:06",
                "isDeleted": 0
            },

        ],
        pageData: {
            page: 1,
            limit: 5,
            total: 10
        },
        participatingPageData: {
            page: 1,
            limit: 5,
            total: 10
        },
        notParticipatingPageData: {
            page: 1,
            limit: 5,
            total: 10
        },
        loading: true,
        participatingLoading: true,
        notParticipatingLoading: true,
    },
    actions: {
        getDiscount(context) {
            const data = {
                page: context.state.pageData.page,
                limit: context.state.pageData.limit
            }
            return getDiscountApi(data).then(({ data }) => {
                context.commit('GET_DISCOUNT', data.data.records);
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
        saveDiscountStatus(context, value) {
            return saveDiscountStatusApi(value);
        },
        changePage(context, value) {
            context.commit('CHANGE_PAGE', value);
        },
        addDiscount(context, value) {
            return addDiscountApi(value);
        },
        editDiscount(context, value) {
            return editDiscountApi(value);
        },
        deleteDiscount(context, value) {
            return deleteDiscountApi(value);
        },
        getParticipating(context, value) {
            const data = {
                discountId: value,
                page: context.state.participatingPageData.page,
                limit: context.state.participatingPageData.limit,
            }
            getParticipatingApi(data)
                .then(({ data }) => {
                    context.commit('GET_PARTICIPATING', data.data.records);
                    const temp = {
                        page: data.data.current,
                        limit: data.data.size,
                        total: data.data.total
                    }
                    context.state.participatingLoading = false;
                    context.commit('CHANGE_PARTICIPATINGPAGE', temp)
                });
        },
        getNotParticipating(context, value) {
            const data = {
                page: context.state.notParticipatingPageData.page,
                limit: context.state.notParticipatingPageData.limit,
            }
            return getNotParticipatingApi(data)
                .then(({ data }) => {
                    // console.log('not data');
                    // console.log(data);
                    context.commit('GET_NOTPARTICIPATING', data.data.records);
                    const temp = {
                        page: data.data.current,
                        limit: data.data.size,
                        total: data.data.total
                    }
                    context.state.notParticipatingLoading = false;
                    context.commit('CHANGE_NOTPARTICIPATINGPAGE', temp);
                })
        },
        cancelDiscount(context, value) {
            return cancelDiscountApi(value);
        },
        setDiscount(context, value) {
            return setDiscountApi(value);
        }
    },
    mutations: {
        CHANGE_PAGE(state, value) {
            state.pageData = value;
        },
        GET_DISCOUNT(state, value) {
            state.discount = value;
        },
        GET_PARTICIPATING(state, value) {
            state.participating = value;
        },
        GET_NOTPARTICIPATING(state, value) {
            state.notParticipating = value;
        },
        CHANGE_PARTICIPATINGPAGE(state, value) {
            state.participatingPageData = value;
        },
        CHANGE_NOTPARTICIPATINGPAGE(state, value) {
            state.notParticipatingPageData = value;
        },
    }
}