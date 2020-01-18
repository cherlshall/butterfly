import { stringify } from 'qs';
import request from '@/utils/request';

export async function listIndex() {
  return request('/server/es/index');
}

export async function createIndex(params) {
  return request('/server/es/index', {
    method: 'POST',
    body: {
      ...params,
    },
  });
}

export async function deleteIndex({ indexName }) {
  return request(`/server/es/index/${indexName}`, {
    method: 'DELETE',
  });
}

export async function properties({ indexName }) {
  return request(`/server/es/index/${indexName}`);
}
