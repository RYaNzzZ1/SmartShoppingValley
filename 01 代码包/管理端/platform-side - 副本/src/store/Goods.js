import { addGoodsApi, editGoodsApi, getGoodsSearchedApi, getGoodsApi, deleteCommentApi, getCommentsApi, replyCommentApi, saveGoodsStatusApi } from '../api';

export default {
    namespaced: true,
    state: {
        goods: [
            //     {
            //     id: 0,
            //     categoryId: 1,
            //     name: '华为',
            //     price: 100,
            //     num: 10,
            //     description: "很好用",
            //     status: 1,
            //     createTime: '2020-8-12',
            //     isDelete: 0,
            //     commodityAttributeList: [
            //         {
            //             commodityId: 0,
            //             name: '大小',
            //             value: '大中小'
            //         }
            //     ],
            //     urls: [],
            //     fatherId: 1
            // }
        ],
        pageData: {
            page: 1,
            limit: 5,
            total: 10
        },
        loading: true,
        comments: [
            // {
            //     "id": 5,
            //     "customerId": 1,
            //     "commodityId": 2,
            //     "score": 1,
            //     "content": "好吃孩子很爱吃",
            //     "reply": null,
            //     "createTime": "2023-09-06T09:51:52",
            //     "urls": []
            // },
            // {
            //     "id": 4,
            //     "customerId": 1,
            //     "commodityId": 2,
            //     "score": 5,
            //     "content": "好吃孩子很爱吃",
            //     "reply": "非常感谢您的认可，我们一定用心做产品，期待您的惠顾",
            //     "createTime": "2023-09-06T09:51:48",
            //     "urls": []
            // }
        ],
        commentPageData: {
            page: 1,
            limit: 5,
            total: 10
        },
        commentLoading: true
    },
    actions: {
        getGoods(context) {
            const data = {
                page: context.state.pageData.page,
                limit: context.state.pageData.limit
            }
            return getGoodsApi(data)
                .then(({ data }) => {
                    context.commit('GET_GOODS', data.data.records);
                    const temp = {
                        page: data.data.current,
                        limit: data.data.size,
                        total: data.data.total
                    }
                    context.state.loading = false;
                    context.commit('CHANGE_PAGE', temp)
                });
        },
        getSearched(context, value) {
            const data = {
                ...value,
                page: context.state.pageData.page,
                limit: context.state.pageData.limit
            }
            getGoodsSearchedApi(data)
                .then(({ data }) => {
                    context.commit('CHANGE_PAGE', {
                        page: 1,
                        limit: 5,
                        total: 0
                    });
                    context.commit('GET_GOODS', data.data);
                })
        },
        saveGoodsStatus(context, value) {
            return saveGoodsStatusApi(value);
        },
        changePage(context, value) {
            context.commit('CHANGE_PAGE', value);
        },
        addGoods(context, value) {
            return addGoodsApi(value);
        },
        editGoods(context, value) {
            console.log('editGoods:');
            console.log(value);
            return editGoodsApi(value);
        },
        getComments(context, value) {
            const data = {
                commodityId: value,
                page: context.state.commentPageData.page,
                limit: context.state.commentPageData.limit,
            }
            console.log('请求comments');
            console.log(data);
            return getCommentsApi(data)
                .then(({ data }) => {
                    console.log('getComments');
                    console.log(data);

                    const temp = {
                        page: data.data.current,
                        limit: data.data.size,
                        total: data.data.total
                    }
                    context.state.commentLoading = false;
                    context.commit('GET_COMMENT_PAGEDATA', temp);
                    context.commit('GET_COMMENTS', data.data.records);
                })
        },
        deleteComment(context, value) {
            // 传入评论id
            console.log('删除评论');
            console.log(value);
            return deleteCommentApi({ id: value });
        },
        replyComment(context, value) {
            console.log('replyComment');
            console.log(value);
            return replyCommentApi(value);
        }
    },
    mutations: {
        CHANGE_PAGE(state, value) {
            state.pageData = value;
        },
        GET_GOODS(state, value) {
            state.goods = value;
        },
        GET_COMMENTS(state, value) {
            state.comments = value;
        },
        GET_COMMENT_PAGEDATA(state, value) {
            state.commentPageData = value;
        }
    }
}