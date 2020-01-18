import { stringify } from 'qs';
import request from '@/utils/request';

export async function list({ indexName, ...params }) {
  return request(`/server/es/doc/${indexName}?${stringify(params)}`);
}

export async function deleteDoc({ indexName, id }) {
  return request(`/server/es/doc/${indexName}/${id}`, {
    method: 'DELETE',
  });
}

export async function insertDoc({ indexName, bean }) {
  return request(`/server/es/doc/${indexName}`, {
    method: 'POST',
    body: { ...bean },
  });
}
