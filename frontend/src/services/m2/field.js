import { stringify } from 'qs';
import request from '@/utils/request';

export async function insert(protocol) {
  return request(`/server/m2/field`, {
    method: 'POST',
    body: protocol,
  });
}

export async function deleteById({ id }) {
  return request(`/server/m2/field/${id}`, {
    method: 'DELETE',
    body: {},
  });
}

export async function update(protocol) {
  return request(`/server/m2/field`, {
    method: 'PUT',
    body: protocol,
  });
}

export async function listByPage({ ...params }) {
  return request(`/server/m2/field?${stringify(params)}`);
}

export async function changeActive({ active, id }) {
  return request(`/server/m2/field/active/${active}/${id}`, {
    method: 'PUT',
    body: {},
  });
}
