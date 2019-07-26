import { stringify } from 'qs';
import request from '@/utils/request';

export async function read(params) {
  return request(`/server/hdfs/content?${stringify(params)}`);
}

export async function readJson(params) {
  return request(`/server/hdfs/content/json?${stringify(params)}`);
}