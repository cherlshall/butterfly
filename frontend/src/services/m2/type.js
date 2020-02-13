import { stringify } from 'qs';
import request from '@/utils/request';

export async function create(type) {
  return request(`/server/m2/type`, {
    method: 'POST',
    body: type,
  });
}

export async function changeActive({ id, active }) {
  return request(`/server/m2/type/active/${id}/${active}`, {
    method: 'PUT',
    body: {},
  });
}

export async function changeOrder({ id, order }) {
  return request(`/server/m2/type/order/${id}/${order}`, {
    method: 'PUT',
    body: {},
  });
}

export async function update(type) {
  return request(`/server/m2/type`, {
    method: 'PUT',
    body: type,
  });
}

export async function list() {
  return request(`/server/m2/type`);
}

export async function listActive() {
  return request(`/server/m2/type/active`);
}
