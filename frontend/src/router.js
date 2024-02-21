
import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router);


import OrderOrderManager from "./components/listers/OrderOrderCards"
import OrderOrderDetail from "./components/listers/OrderOrderDetail"

import InstallInstallManager from "./components/listers/InstallInstallCards"
import InstallInstallDetail from "./components/listers/InstallInstallDetail"

import PaymentPaymentManager from "./components/listers/PaymentPaymentCards"
import PaymentPaymentDetail from "./components/listers/PaymentPaymentDetail"

import AirobotAirobotManager from "./components/listers/AirobotAirobotCards"
import AirobotAirobotDetail from "./components/listers/AirobotAirobotDetail"


export default new Router({
    // mode: 'history',
    base: process.env.BASE_URL,
    routes: [
            {
                path: '/orders/orders',
                name: 'OrderOrderManager',
                component: OrderOrderManager
            },
            {
                path: '/orders/orders/:id',
                name: 'OrderOrderDetail',
                component: OrderOrderDetail
            },

            {
                path: '/installs/installs',
                name: 'InstallInstallManager',
                component: InstallInstallManager
            },
            {
                path: '/installs/installs/:id',
                name: 'InstallInstallDetail',
                component: InstallInstallDetail
            },

            {
                path: '/payments/payments',
                name: 'PaymentPaymentManager',
                component: PaymentPaymentManager
            },
            {
                path: '/payments/payments/:id',
                name: 'PaymentPaymentDetail',
                component: PaymentPaymentDetail
            },

            {
                path: '/airobots/airobots',
                name: 'AirobotAirobotManager',
                component: AirobotAirobotManager
            },
            {
                path: '/airobots/airobots/:id',
                name: 'AirobotAirobotDetail',
                component: AirobotAirobotDetail
            },



    ]
})
