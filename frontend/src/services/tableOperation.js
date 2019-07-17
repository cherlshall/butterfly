import { stringify } from 'qs';
import request from '@/utils/request';

export async function findByPage(params) {
  return request(`/server/hbase/table/findAll?${stringify(params)}`);
}