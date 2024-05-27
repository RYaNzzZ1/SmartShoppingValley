import { getTypeListByIdApi, addTypeApi, editTypeApi, deleteTypeApi } from '../api';

export default {
    namespaced: true,
    state: {
        typeList: [
            // {
            //     id: 0,
            //     fatherId: -1,
            //     name: '衣服',
            //     createTime: '2023-8-31'
            // }
        ],
        loading: true
    },
    actions: {
        getTypeListById(context, fatherId) {
            const temp = {
                fatherId: fatherId,
            }
            return getTypeListByIdApi(temp)
                .then(({ data }) => {
                    context.state.loading = false;
                    context.commit('GET_TYPELIST', data.data);
                });
        },

        addType(context, value) {
            console.log("addType:");
            console.log(value);
            return addTypeApi(value).then(({ data }) => {
                return data;
            })
        },
        editType(context, value) {
            console.log('editType:');
            console.log(value);
            return editTypeApi();
        },
        deleteType(context, id) {
            return deleteTypeApi({ id: id }).then(({ data }) => {
                return data;
            })
        }
    },
    mutations: {
        GET_TYPELIST(state, value) {
            state.typeList = value;
        },
    }
}