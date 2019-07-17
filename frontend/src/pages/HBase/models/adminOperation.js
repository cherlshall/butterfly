import { list } from '@/services/adminOperation';

export default {
  namespace: 'adminOperation',

  state: {
    tableNames: [],
  },

  effects: {
    *list({callback}, { call, put }) {
      const response = yield call(list);
      if (response.success) {
        yield put({
          type: 'save',
          payload: {
            tableNames: response.data,
          },
        });
        if (callback && response.data.length > 0) {
          callback(response.data[0]);
        }
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
        tableNames: [],
      };
    },
  },
};
