import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router';

import AppVue from '../App.vue';

// 2. 定义路由
const routes: RouteRecordRaw[] = [
  {
    path: '/',
    component: AppVue,
    redirect: 'home',
    meta: { title: '跳转中...' },
    children: [
      {
        path: '/home',
        name: 'home',
        component: () => import('../pages/common/Home.vue'),
        meta: { title: 'Wei的博客' },
      },
      {
        path: '/login',
        name: 'login',
        component: () => import(''),
        meta: { title: '登录' },
      },
    ],
  },
];

// 3. 创建路由实例并传递 `routes` 配置

const router = createRouter({
  // 4. 内部提供了 history 模式的实现。为了简单起见，我们在这里使用 hash 模式。
  history: createWebHistory(),
  routes, // `routes: routes` 的缩写
});

router.beforeEach((to, from) => {
  to.meta.title && (document.title = to.meta.title as string);
  return true;
});

export default router;
