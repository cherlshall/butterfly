import * as service from '@/services/m2/field';
import * as protocolService from '@/services/m2/protocol';
import { message } from 'antd';

export default {
  namespace: 'm2Field',

  state: {
    list: [],
    total: 0,
    protocol: {},
  },

  effects: {
    *create({ payload, callback }, { call }) {
      const response = yield call(service.insert, payload);
      if (response.code === 200) {
        message.success('create success');
        if (callback) {
          callback();
        }
      } else {
        message.error(response.msg || 'unknown error');
      }
    },

    *deleteById({ payload, callback }, { call, put, select }) {
      const response = yield call(service.deleteById, payload);
      if (response.code === 200) {
        const list = yield select(state =>
          state.m2Field.list.map(item => {
            const data = { ...item };
            if (data.id === payload.id) {
              data.deleted = true;
            }
            return data;
          })
        );
        yield put({
          type: 'save',
          payload: {
            list,
          },
        });
        message.success('delete success');
      } else {
        message.error(response.msg || 'unknown error');
      }
      if (callback) {
        callback();
      }
    },

    *update({ payload, callback }, { call }) {
      const response = yield call(service.update, payload);
      if (response.code === 200) {
        message.success('update success');
        if (callback) {
          callback();
        }
      } else {
        message.error(response.msg || 'unknown error');
      }
    },

    *list({ payload, callback }, { call, put }) {
      const response = yield call(service.listByPage, payload);
      if (response.code === 200) {
        yield put({
          type: 'save',
          payload: {
            list: response.data.list,
            count: response.data.total,
          },
        });
      } else {
        message.error(response.msg || 'unknown error');
      }
    },

    *listProtocolName({ payload, callback }, { call, put }) {
      const response = yield call(protocolService.listProtocolName, payload);
      if (response.code === 200) {
        if (callback) {
          callback(response.data);
        }
      } else {
        message.error(response.msg || 'unknown error');
      }
    },

    *listChildProtocolName({ payload, callback }, { call, put }) {
      const response = yield call(protocolService.listChildProtocolName, payload);
      if (response.code === 200) {
        if (callback) {
          callback(response.data);
        }
      } else {
        message.error(response.msg || 'unknown error');
      }
    },

    *findProtocolById({ payload, callback }, { call, put }) {
      const response = yield call(protocolService.findById, payload);
      if (response.code === 200) {
        yield put({
          type: 'save',
          payload: {
            protocol: response.data,
          },
        });
      } else {
        message.error(response.msg || 'unknown error');
      }
    },

    *changeActive({ payload, callback }, { call, put, select }) {
      const response = yield call(service.changeActive, payload);
      if (response.code === 200) {
        const list = yield select(state =>
          state.m2Field.list.map(item => {
            const data = { ...item };
            if (data.id === payload.id) {
              data.active = payload.active;
            }
            return data;
          })
        );
        yield put({
          type: 'save',
          payload: {
            list,
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
        detailList: [],
      };
    },
  },
};
