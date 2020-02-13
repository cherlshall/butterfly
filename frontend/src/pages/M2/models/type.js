import * as service from '@/services/m2/type';
import { message } from 'antd';

export default {
  namespace: 'm2Type',

  state: {
    list: [],
    activeList: [],
  },

  effects: {
    *create({ payload, callback }, { call }) {
      const response = yield call(service.create, payload);
      if (response.code === 200) {
        message.success('create success');
        if (callback) {
          callback();
        }
      } else {
        message.error(response.msg || 'unknown error');
      }
    },

    *list({ payload }, { call, put }) {
      const response = yield call(service.list, payload);
      if (response.code === 200) {
        yield put({
          type: 'save',
          payload: {
            list: response.data,
          },
        });
      } else {
        message.error(response.msg || 'unknown error');
      }
    },

    *listActive({ payload }, { call, put }) {
      const response = yield call(service.listActive, payload);
      if (response.code === 200) {
        yield put({
          type: 'save',
          payload: {
            activeList: response.data,
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
          state.m2Type.list.map(item => {
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
