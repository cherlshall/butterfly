import { stringify } from 'qs';
import request from '@/utils/request';

export async function findByPage({tableName, ...params}) {
  return request(`/server/hbase/table/findByPage/${tableName}?${stringify(params)}`);
}