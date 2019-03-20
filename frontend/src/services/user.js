import request from '@/utils/request';
import fetch from '@/utils/fetch';

export async function query() {
  return request('/api/users');
}

export async function queryCurrent() {
  return request('/server/user/currentUser');
}

export async function accountLogin(params) {
  return request('/server/user/login/account', {
    method: 'POST',
    body: params,
  });
}

export async function accountLogout() {
  return request('/server/user/logout/account', {
    method: 'POST',
  });
}

export async function currentAuthority() {
  return request('/server/user/currentAuthority', {
    method: 'POST',
  });
}
