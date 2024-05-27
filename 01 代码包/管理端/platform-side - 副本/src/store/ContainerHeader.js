import { nanoid } from "nanoid";

export default {
    namespaced: true,
    state: {
        // 用于面包屑控制的数组
        tableList: [
            {
                id: nanoid(),
                title: "首页",
                icon: "s-home",
                path: "/home",
            },
        ]
    },
    actions: {
        selectMenu(context, value) {
            context.commit("SELECT_MENU", value);
        }

    },
    mutations: {
        SELECT_MENU(state, value) {
            state.tableList.push(value);
        }
    }
}