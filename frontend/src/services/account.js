import request from '@/utils/request';
import fetch from '@/utils/fetch';

export async function accountLogin(params) {
  return request('/server/account/login', {
    method: 'POST',
    body: params,
  });
}

export async function accountLogout() {
  return request(`/server/account/logout`, {
    method: 'POST',
  });
}

export async function upload(file) {
  return request(`/server/account/upload`, {
    method: 'POST',
    body: file,
  });
}
