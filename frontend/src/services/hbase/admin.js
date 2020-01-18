import { stringify } from 'qs';
import request from '@/utils/request';

export async function list() {
  return request('/server/hbase/admin/table');
}

export async function detail() {
  return request('/server/hbase/admin/detail');
}

export async function create(params) {
  return request('/server/hbase/admin/table', {
    method: 'POST',
    body: {
      ...params,
    },
  });
}

export async function truncate({ tableNames }) {
  return request(`/server/hbase/admin/truncate`, {
    method: 'DELETE',
    body: { tableNames },
  });
}

export async function del({ tableName }) {
  return request(`/server/hbase/admin/table/${tableName}`, {
    method: 'DELETE',
    body: {},
  });
}

export async function disable({ tableName }) {
  return request(`/server/hbase/admin/disable/${tableName}`, {
    method: 'PUT',
    body: {},
  });
}

export async function enable({ tableName }) {
  return request(`/server/hbase/admin/enable/${tableName}`, {
    method: 'PUT',
    body: {},
  });
}

export async function listFamily({ tableName }) {
  return request(`/server/hbase/admin/family/${tableName}`);
}

export async function addFamily({ tableName, family }) {
  return request(`/server/hbase/admin/family/${tableName}/${family}`, {
    method: 'POST',
    body: {},
  });
}

export async function deleteFamily({ tableName, family }) {
  return request(`/server/hbase/admin/family/${tableName}/${family}`, {
    method: 'DELETE',
    body: {},
  });
}

export async function changeFamily({ tableName, ...params }) {
  return request(`/server/hbase/admin/family/${tableName}`, {
    method: 'POST',
    body: params,
  });
}
