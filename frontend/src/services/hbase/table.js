import { stringify } from 'qs';
import request from '@/utils/request';

export async function findByPage({ tableName, ...params }) {
  return request(`/server/hbase/table/row/${tableName}?${stringify(params)}`);
}

export async function insertRow({ tableName, rowKey, beans }) {
  return request(`/server/hbase/table/row/${tableName}/${rowKey}`, {
    method: 'POST',
    body: beans,
  });
}

export async function deleteRow({ tableName, rowKey }) {
  return request(`/server/hbase/table/row/${tableName}/${rowKey}`, {
    method: 'DELETE',
    body: {},
  });
}

export async function deleteCol({ tableName, rowKey, ...params }) {
  return request(`/server/hbase/table/col/${tableName}/${rowKey}`, {
    method: 'DELETE',
    body: params,
  });
}
