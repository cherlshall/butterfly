import request from '@/utils/request';

export async function queryProvince() {
  return request('/server/geographic/province');
}

export async function queryCity(province) {
  return request(`/server/geographic/city/${province}`);
}
