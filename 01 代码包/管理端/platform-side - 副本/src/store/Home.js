import { getStatisticsApi } from '../api';

export default {
    namespaced: true,
    state: {
        statistics: {
            // id: 32,
            // dailyNum: 32,
            // dailyAmount: 32.00,
            // weeklyNum: 32,
            // weeklyAmount: 32.00,
            // monthlyNum: 32,
            // monthlyAmount: 32.00,
            // totalNum: 32,
            // totalAmount: 32.00,
            // createTime: "2023-09-07",
            // nums: [
            //     26,
            //     27,
            //     28,
            //     29,
            //     30,
            //     31,
            //     32
            // ],
            // "amounts": [
            //     26.00,
            //     27.00,
            //     28.00,
            //     29.00,
            //     30.00,
            //     31.00,
            //     32.00
            // ]
        }
    },
    actions: {
        getStatistics(context) {
            return getStatisticsApi().then(({ data }) => {
                context.commit('GET_STATISTICS', data.data);
            });
        },
    },
    mutations: {
        GET_STATISTICS(state, value) {
            state.statistics = value;
        }
    }
}