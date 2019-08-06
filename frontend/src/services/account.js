import request from '@/utils/request';
import fetch from '@/utils/fetch';

export async function accountLogin(params) {
  return request('/server/account/login', {
    method: 'POST',
    body: params,
  });
}

export async function accountLogout({ uid }) {
  return request(`/server/account/logout?uid=${uid}`, {
    method: 'POST',
  });
}