import { routerRedux } from 'dva/router';
import { stringify } from 'qs';
import { accountLogin, accountLogout } from '@/services/account';
import { setToken, getToken, removeToken, setUid, getUid, removeUid } from '@/utils/authority';
import { getPageQuery } from '@/utils/utils';
import { reloadAuthorized } from '@/utils/Authorized';
import { message } from 'antd';

export default {
  namespace: 'login',

  state: {
    status: undefined,
  },

  effects: {
    *login({ payload }, { call, put }) {
      const response = yield call(accountLogin, payload);
      if (response.code === 200) {
        yield put({
          type: 'changeLoginStatus',
          payload: response.data,
        });
        // Login successfully
        if (response.data.status === 'ok') {
          setToken(response.data.token);
          setUid(response.data.id);
          reloadAuthorized();
          const urlParams = new URL(window.location.href);
          const params = getPageQuery();
          let { redirect } = params;
          if (redirect) {
            const redirectUrlParams = new URL(redirect);
            if (redirectUrlParams.origin === urlParams.origin) {
              redirect = redirect.substr(urlParams.origin.length);
              if (redirect.match(/^\/.*#/)) {
                redirect = redirect.substr(redirect.indexOf('#') + 1);
              }
            } else {
              window.location.href = redirect;
              return;
            }
          }
          yield put(routerRedux.replace(redirect || '/'));
        }
      } else {
        message.error(response.msg || "server error");
      }
    },

    *getCaptcha({ payload }, { call }) {
      
    },

    *logout(_, { call, put }) {
      const uid = getUid();
      if (uid !== '') {
        const response = yield call(accountLogout, {uid});
        if (response.code === 200) {
          yield put({
            type: 'changeLoginStatus',
            payload: {
              status: false,
              currentAuthority: 'guest',
            },
          });
          removeToken();
          removeUid();
          reloadAuthorized();
          yield put(
            routerRedux.replace({
              pathname: '/user/login',
              search: stringify({
                redirect: window.location.href,
              }),
            })
          );
        }
      } else {
        removeToken();
        removeUid();
        reloadAuthorized();
        yield put(
          routerRedux.replace({
            pathname: '/user/login',
            search: stringify({
              redirect: window.location.href,
            }),
          })
        );
      }
      
    },

  },

  reducers: {
    changeLoginStatus(state, { payload }) {
      return {
        ...state,
        status: payload.status,
        type: payload.type,
      };
    },
  },
};
