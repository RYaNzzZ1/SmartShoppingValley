import { getUsersApi, saveUserStatusApi, getUserSearchedApi, getUserDetailApi } from '../api';

export default {
    namespaced: true,
    state: {
        users: [],
        pageData: {
            page: 1,
            limit: 8,
            total: 100
        },
        userDetail: {
            id: 0,
            customerId: 1,
            name: '李四',
            avatar: 'https://thirdqq.qlogo.cn/g?b=oidb&k=PRoZYDOElWrNPTDVsINSsQ&kti=ZO6skAAAAAA&s=100&t=1661708447',
            // 1 男 2 女
            sex: 1,
            age: 18,
            birthday: '2023-8-30 6.29',
            phone: 12345678902,
            description: '这个人很懒并没有描述'
        },
        drawer: false,
        loading: true,

    },
    actions: {
        getSearched(context, value) {
            const data = {
                username: value,
                page: context.state.pageData.page,
                limit: context.state.pageData.limit,
            }
            getUserSearchedApi(data)
                .then(({ data }) => {
                    const temp = {
                        page: data.data.current,
                        limit: data.data.size,
                        total: data.data.total
                    }
                    context.commit('CHANGE_PAGE', temp)
                    console.log('search');
                    console.log(context.state.pageData);
                    context.commit('GET_USERS', data.data.records);
                })
        },
        getUsers(context, value) {
            const data = {
                page: context.state.pageData.page,
                limit: context.state.pageData.limit
            }
            getUsersApi(data).then(({ data }) => {
                context.commit('GET_USERS', data.data.records);
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
        saveUserStatus(context, id) {
            return saveUserStatusApi({ id: id });
        },
        getUserDetail(context, id) {
            getUserDetailApi({ id: id })
                .then(({ data }) => {
                    context.commit('GET_USER_DETAIL', data.data);
                    context.commit('OPEN_DRAWER');
                }).catch(err => {
                    console.log(err);
                })
        },
        openDrawer(context) {
            context.commit('OPEN_DRAWER');
        },
        closeDrawer(context) {
            context.commit('CLOSE_DRAWER');
        }
    },
    mutations: {
        GET_USERS(state, value) {
            state.users = value;
        },
        CHANGE_PAGE(state, value) {
            state.pageData = value;
        },
        OPEN_DRAWER(state) {
            state.drawer = true;
        },
        CLOSE_DRAWER(state) {
            state.drawer = false;
        },
        GET_USER_DETAIL(state, value) {
            state.userDetail = value;
        }
    }
}