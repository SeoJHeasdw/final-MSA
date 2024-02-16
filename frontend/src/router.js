
import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router);


import ReserveCarManager from "./components/listers/ReserveCarCards"
import ReserveCarDetail from "./components/listers/ReserveCarDetail"





export default new Router({
    // mode: 'history',
    base: process.env.BASE_URL,
    routes: [
            {
                path: '/reserves/cars',
                name: 'ReserveCarManager',
                component: ReserveCarManager
            },
            {
                path: '/reserves/cars/:id',
                name: 'ReserveCarDetail',
                component: ReserveCarDetail
            },






    ]
})
