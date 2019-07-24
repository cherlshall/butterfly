import * as service from '@/services/esIndex';
import { message } from 'antd';

export default {
  namespace: 'esIndex',

  state: {
  },

  effects: {
    *createIndex({ payload, callback }, { call, put }) {
      const response = yield call(service.createIndex, payload);
      if (response.code === 200) {
        message.success("create success");
        if (callback) {
          callback();
        }
      } else {
        message.error(response.msg || "unknown error");
      }
    },

    *deleteIndex({ payload, callback }, { call, put }) {
      const response = yield call(service.deleteIndex, payload);
      if (response.code === 200) {
        message.success("delete success");
        if (callback) {
          callback();
        }
      } else {
        message.error(response.msg || "unknown error");
      }
    },

  },

  reducers: {
    save(state, { payload }) {
      return {
        ...state,
        ...payload,
      };
    },
    clear() {
      return {
        clusterHealth: {},
        indexHealth: [],
      };
    },
  },
};
