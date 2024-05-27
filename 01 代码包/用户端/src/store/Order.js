export default {
    namespaced: true,
    state: {
        // 0 表示立即购买   1 表示购物车购买
        flag: sessionStorage.getItem('flag'),
        order: {
            commodityId: sessionStorage.getItem('commodityId'),
            commodityAttribute: sessionStorage.getItem('commodityAttribute'),
            number: sessionStorage.getItem('number'),
            amount: sessionStorage.getItem('amount'),
            urls: sessionStorage.getItem('goodsImg'),
            description: sessionStorage.getItem('goodsDesc'),
        },
    },
    actions: {
        setFlag(context, value) {
            context.commit('SET_FLAG', value);
        },
        setOrder(context, value) {
            context.commit('SET_ORDER', value);
        }
    },
    mutations: {
        SET_FLAG(state, value) {
            state.flag = value;
        },
        SET_ORDER(state, value) {
            state.order = value;
            console.log(state.order);
        }
    }
}