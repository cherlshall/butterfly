export default [
  // user
  {
    path: '/user',
    component: '../layouts/UserLayout',
    routes: [
      { path: '/user', redirect: '/user/login' },
      { path: '/user/login', name: 'login', component: './User/Login' },
      { path: '/user/register', name: 'register', component: './User/Register' },
      {
        path: '/user/register-result',
        name: 'register.result',
        component: './User/RegisterResult',
      },
    ],
  },
  // app
  {
    path: '/',
    component: '../layouts/BasicLayout',
    Routes: ['src/pages/Authorized'],
    routes: [
      // dashboard
      // { path: '/', redirect: '/dashboard/analysis' },
      { path: '/', redirect: '/dashboard' },
      {
        path: '/dashboard',
        name: 'dashboard',
        icon: 'dashboard',
        component: './Dashboard/Topo',
        // routes: [
        //   {
        //     path: '/dashboard/analysis',
        //     name: 'analysis',
        //     component: './Dashboard/Analysis',
        //   },
        //   {
        //     path: '/dashboard/monitor',
        //     name: 'monitor',
        //     component: './Dashboard/Monitor',
        //   },
        // ],
      },
      {
        path: '/elasticsearch',
        name: 'elasticsearch',
        icon: 'search',
        routes: [
          {
            path: '/elasticsearch/esCluster',
            name: 'esCluster',
            component: './ElasticSearch/EsCluster/EsCluster',
          },
          {
            path: '/elasticsearch/esIndex',
            name: 'esIndex',
            component: './ElasticSearch/EsIndex/EsIndex',
          },
          {
            path: '/elasticsearch/esIndex/:indexName',
            hideInMenu: true,
            name: 'esIndex',
            component: './ElasticSearch/EsIndex/EsIndex',
          },
        ],
        authority: ['admin'],
      },
      {
        path: '/hbase',
        name: 'hbase',
        icon: 'database',
        routes: [
          {
            path: '/hbase/hbaseAdmin',
            name: 'hbaseAdmin',
            component: './HBase/hbaseAdmin/hbaseAdmin',
          },
          {
            path: '/hbase/hbaseTable',
            name: 'hbaseTable',
            component: './HBase/hbaseTable/hbaseTable',
          },
          {
            path: '/hbase/hbaseTable/:tableName',
            hideInMenu: true,
            name: 'hbaseTable',
            component: './HBase/hbaseTable/hbaseTable',
          },
        ],
        authority: ['admin'],
      },
      {
        path: '/hdfs',
        name: 'hdfs',
        icon: 'folder',
        component: './Hdfs/Hdfs',
        authority: ['admin'],
      },
      {
        path: '/m2',
        name: 'm2',
        icon: 'api',
        routes: [
          {
            path: '/m2/protocol',
            name: 'm2Protocol',
            component: './M2/protocol/protocol',
          },
          {
            path: '/m2/field/:protocolId',
            hideInMenu: true,
            name: 'm2Field',
            component: './M2/field/field',
          },
          {
            path: '/m2/type',
            name: 'm2Type',
            component: './M2/type/type',
          },
        ],
        authority: ['admin'],
      },
      {
        name: 'exception',
        icon: 'warning',
        path: '/exception',
        hideInMenu: true,
        routes: [
          // exception
          {
            path: '/exception/403',
            name: 'not-permission',
            component: './Exception/403',
          },
          {
            path: '/exception/404',
            name: 'not-find',
            component: './Exception/404',
          },
          {
            path: '/exception/500',
            name: 'server-error',
            component: './Exception/500',
          },
          {
            path: '/exception/trigger',
            name: 'trigger',
            component: './Exception/TriggerException',
          },
        ],
      },
      {
        name: 'account',
        icon: 'user',
        path: '/account',
        hideInMenu: true,
        routes: [
          {
            path: '/account/center',
            name: 'center',
            component: './Account/Center/Center',
            routes: [
              {
                path: '/account/center',
                redirect: '/account/center/articles',
              },
              {
                path: '/account/center/articles',
                component: './Account/Center/Articles',
              },
              {
                path: '/account/center/applications',
                component: './Account/Center/Applications',
              },
              {
                path: '/account/center/projects',
                component: './Account/Center/Projects',
              },
            ],
          },
          {
            path: '/account/settings',
            name: 'settings',
            component: './Account/Settings/Info',
            routes: [
              {
                path: '/account/settings',
                redirect: '/account/settings/base',
              },
              {
                path: '/account/settings/base',
                component: './Account/Settings/BaseView',
              },
              {
                path: '/account/settings/security',
                component: './Account/Settings/SecurityView',
              },
              {
                path: '/account/settings/binding',
                component: './Account/Settings/BindingView',
              },
              {
                path: '/account/settings/notification',
                component: './Account/Settings/NotificationView',
              },
            ],
          },
        ],
      },
      {
        component: '404',
      },
    ],
  },
];
