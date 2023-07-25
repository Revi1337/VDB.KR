const routes = [
  {
    path: '/',
    component: () => import('layouts/MainLayout.vue'),
    children: [
      {
        path: '',
        name: 'Index',
        component: () => import('pages/IndexPage.vue')
      },
      {
        path: '/statistic',
        name: 'Statistic',
        component: () => import('pages/StatisticsPage.vue')
      },
      {
        path: '/statistic2',
        name: 'Statistic2',
        component: () => import('pages/StatisticsPage2.vue')
      },
      {
        path: '/statistic/:year',
        name: 'YearStatistic',
        component: () => import('pages/YearStatisticsPage.vue')
      },
      {
        path: '/signup',
        name: 'SignUp',
        component: () => import('pages/SignUpPage.vue')
      },
      {
        path: '/practice',
        name: 'Practice',
        component: () => import('pages/PracticePage.vue')
      }
    ]
  },
  {
    path: '/:catchAll(.*)*',
    component: () => import('pages/ErrorNotFound.vue')
  }
];

export default routes;
