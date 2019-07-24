import * as service from '@/services/esCluster';
import { queryTags } from '@/services/api';
import { message } from 'antd';

export default {
  namespace: 'esCluster',

  state: {
    clusterHealth: {},
    indexHealth: [],
    searchTags: [],
  },

  effects: {
    *health({ callback }, { call, put }) {
      const response = yield call(service.health);
      if (response.code === 200) {
        const { indexHealth, ...clusterHealth } = response.data;
        yield put({
          type: 'save',
          payload: {
            clusterHealth,
            indexHealth,
          },
        });
      } else {
        message.error(response.msg || "unknown error");
      }
      if (callback) {
        callback();
      }
    },

    *fetchTags(_, { call, put }) {
      const response = yield call(queryTags);
      yield put({
        type: 'save',
        payload: {
          searchTags: response.list
        },
      });
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
