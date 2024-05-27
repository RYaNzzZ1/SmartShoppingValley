import { getAdminsApi, saveAdminStatusApi, changeAdminInfoApi, deleteAdminApi, addAdminApi } from '../api';

export default {
    namespaced: true,
    state: {
        admin: {
            // id: 0,
            // name: '张三',
            // password: '123456',
            // name: '李四',
            // // 1 正常 2 禁用
            // status: 1,
            // // 1 高级 2 普通
            // privileges: 2,
            // createTime: '2023-8-29',
        },
        admins: [
            // {
            //     id: 0,
            //     username: '张三',
            //     password: '123456',
            //     name: '李四',
            //     // 1 正常 2 禁用
            //     status: 1,
            //     // 1 高级 2 普通
            //     privileges: 1,
            //     createTime: '2023-8-29',
            // }
        ],
        loading: true,
    },
    actions: {
        // 获取当前登录的管理员信息
        setAdmin(context, value) {
            context.commit('SET_ADMIN', value);
        },
        // 获取管理员列表
        getAdmins(context) {
            return getAdminsApi().then(({ data }) => {
                context.state.loading = false;
                context.commit('GET_ADMINS', data.data);
            })
        },
        saveAdminStatus(context, id) {
            return saveAdminStatusApi({ id: id });
        },
        changeAdminInfo(context, value) {
            return changeAdminInfoApi(value);
        },
        deleteAdmin(context, id) {
            return deleteAdminApi({ id: id });
        },
        addAdmin(context, value) {
            console.log('addAdmin:');
            console.log(value);
            return addAdminApi(value);
        }
    },
    mutations: {
        SET_ADMIN(state, value) {
            state.admin = value;
        },
        GET_ADMINS(state, value) {
            state.admins = value;
        }
    }
}