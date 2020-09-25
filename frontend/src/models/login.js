import { routerRedux } from 'dva/router';
import { stringify } from 'qs';
import { accountLogin, accountLogout } from '@/services/account';
import {
  setToken,
  removeToken,
  removeAuthority,
  setAuthority
} from '@/utils/authority';
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
      const { autoLogin } = payload;
      const response = yield call(accountLogin, payload);
      if (response.code === 200) {
        const { status, token, currentAuthority} = response.data;
        if (status === 'ok') {
          setToken(token, autoLogin);
          setAuthority(currentAuthority, autoLogin);
          reloadAuthorized();
          const query = getPageQuery();
          let { redirect } = query;
          if (redirect) {
            const url = new URL(window.location.href);
            const redirectUrl = new URL(redirect);
            if (redirectUrl.origin === url.origin) {
              redirect = redirect.substr(url.origin.length);
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
        message.error(response.msg || 'server error');
      }
    },

    *getCaptcha({ payload }, { call }) {

    },

    *logout({ redirect }, { call, put }) {
      removeToken();
      removeAuthority();
      reloadAuthorized();
      yield put(
        routerRedux.replace({
          pathName: '/user/login',
          search: stringify({
            redirect: redirect || window.location.href,
          })
        })
      )
    },

  },

};
