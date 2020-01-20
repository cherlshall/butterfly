import { stringify } from 'qs';
import request from '@/utils/request';

export async function insert(protocol) {
  return request(`/server/m2/protocol`, {
    method: 'POST',
    body: protocol,
  });
}

export async function deleteById({ id }) {
  return request(`/server/m2/protocol/${id}`, {
    method: 'DELETE',
    body: {},
  });
}

export async function update(protocol) {
  return request(`/server/m2/protocol`, {
    method: 'PUT',
    body: protocol,
  });
}

export async function findById({ id }) {
  return request(`/server/m2/protocol/${id}`);
}

export async function listByPage({ ...params }) {
  return request(`/server/m2/protocol?${stringify(params)}`);
}

export async function listProtocolName({ category }) {
  return request(`/server/m2/protocol/names/${category}`);
}

export async function listChildProtocolName({ category, protocolId }) {
  return request(`/server/m2/protocol/names/${category}/${protocolId}`);
}

export async function changeActive({ active, id }) {
  return request(`/server/m2/protocol/active/${active}/${id}`, {
    method: 'PUT',
    body: {},
  });
}
