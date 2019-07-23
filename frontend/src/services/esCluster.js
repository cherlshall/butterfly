import { stringify } from 'qs';
import request from '@/utils/request';

export async function health() {
  return request('/server/es/cluster/health');
}
