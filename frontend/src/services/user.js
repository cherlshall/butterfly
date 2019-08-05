import request from '@/utils/request';
import fetch from '@/utils/fetch';
import { stringify } from 'qs';

export async function query() {
  return request('/api/users');
}

export async function queryCurrent(params) {
  return request(`/server/user/currentUser?${stringify(params)}`);
}

export async function currentAuthority() {
  return request('/server/user/currentAuthority', {
    method: 'POST',
  });
}
