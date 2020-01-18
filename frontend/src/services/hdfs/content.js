import { stringify } from 'qs';
import request from '@/utils/request';

export async function read(params) {
  return request(`/server/hdfs/content?${stringify(params)}`);
}

export async function readToTable(params) {
  return request(`/server/hdfs/content/table?${stringify(params)}`);
}
