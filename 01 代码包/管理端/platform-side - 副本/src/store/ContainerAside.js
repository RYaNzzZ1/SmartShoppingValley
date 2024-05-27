export default {
    namespaced: true,
    state: {
        // 控制侧栏开关
        collapseWidth: '9%',
        notCollapseWidth: '4%',
        collapse: true,        
    },
    actions: {
        changeCollapse(context) {
            context.commit('CHANGE_COLLAPSE');
        }
    },
    mutations: {
        CHANGE_COLLAPSE(state) {
            state.collapse = !state.collapse;
        }
    }
}