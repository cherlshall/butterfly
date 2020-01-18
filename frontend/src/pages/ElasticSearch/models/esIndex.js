import * as service from '@/services/elasticsearch/index';
import * as docService from '@/services/elasticsearch/doc';
import { message } from 'antd';

export default {
  namespace: 'esIndex',

  state: {
    properties: [],
    dataSource: [],
    total: 0,
    indices: [],
  },

  effects: {
    *createIndex({ payload, callback }, { call }) {
      const response = yield call(service.createIndex, payload);
      if (response.code === 200) {
        message.success('create success');
        if (callback) {
          callback();
        }
      } else {
        message.error(response.msg || 'unknown error');
      }
    },

    *deleteIndex({ payload, callback }, { call }) {
      const response = yield call(service.deleteIndex, payload);
      if (response.code === 200) {
        message.success('delete success');
        if (callback) {
          callback();
        }
      } else {
        message.error(response.msg || 'unknown error');
      }
    },

    *listIndex({ payload, callback }, { call, put }) {
      const response = yield call(service.listIndex, payload);
      if (response.code === 200) {
        yield put({
          type: 'save',
          payload: {
            indices: response.data,
          },
        });
        if (callback) {
          if (response.data.length > 0) {
            callback(response.data[0]);
          }
        }
      } else {
        message.error(response.msg || 'unknown error');
      }
    },

    *properties({ payload, callback }, { call, put }) {
      const response = yield call(service.properties, payload);
      if (response.code === 200) {
        yield put({
          type: 'save',
          payload: {
            properties: response.data,
          },
        });
        if (callback) {
          callback();
        }
      } else {
        message.error(response.msg || 'unknown error');
      }
    },

    *listData({ payload, callback }, { call, put }) {
      const response = yield call(docService.list, payload);
      if (response.code === 200) {
        const { dataSource } = response.data;
        dataSource.forEach(item => {
          for (const key in item) {
            const field = item[key];
            if (typeof field === 'object') {
              item[key] = JSON.stringify(field);
            } else {
              item[key] = field;
            }
          }
        });
        yield put({
          type: 'save',
          payload: {
            dataSource,
            total: response.data.total,
          },
        });
        if (callback) {
          callback();
        }
      } else {
        message.error(response.msg || 'unknown error');
      }
    },

    *createDoc({ payload, callback }, { call }) {
      const response = yield call(docService.insertDoc, payload);
      if (response.code === 200) {
        message.success('insert success');
        if (callback) {
          callback();
        }
      } else {
        message.error(response.msg || 'unknown error');
      }
    },

    *recoverDoc({ payload, callback }, { call, put, select }) {
      const response = yield call(docService.insertDoc, payload);
      if (response.code === 200) {
        const dataSource = yield select(state =>
          state.esIndex.dataSource.map(item => {
            const data = { ...item };
            if (data._id === payload.bean._id) {
              data._deleted = undefined;
            }
            return data;
          })
        );
        yield put({
          type: 'save',
          payload: {
            dataSource,
          },
        });
      } else {
        message.error(response.msg || 'unknown error');
      }
      if (callback) {
        callback();
      }
    },

    *deleteDoc({ payload, callback }, { call, put, select }) {
      const response = yield call(docService.deleteDoc, payload);
      if (response.code === 200) {
        const dataSource = yield select(state =>
          state.esIndex.dataSource.map(item => {
            const data = { ...item };
            if (data._id === payload.id) {
              data._deleted = true;
            }
            return data;
          })
        );
        yield put({
          type: 'save',
          payload: {
            dataSource,
          },
        });
      } else {
        message.error(response.msg || 'unknown error');
      }
      if (callback) {
        callback();
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
