import { stringify } from 'qs';
import request from '@/utils/request';

export async function detail(params) {
  return request(`/server/hdfs/file/detail?${stringify(params)}`);
}

export async function create(params) {
  return request(`/server/hdfs/file/file?${stringify(params)}`, {
    method: 'POST',
  });
}

export async function del(params) {
  return request(`/server/hdfs/file?${stringify(params)}`, {
    method: 'DELETE',
  });
}

export async function mkdirs(params) {
  return request(`/server/hdfs/file/dir?${stringify(params)}`, {
    method: 'POST',
  });
}