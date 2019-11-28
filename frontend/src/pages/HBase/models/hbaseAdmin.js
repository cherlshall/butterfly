import * as service from '@/services/hbaseAdmin';
import { message } from 'antd';

export default {
  namespace: 'hbaseAdmin',

  state: {
    tableNames: [],
    dataSource: [],
  },

  effects: {
    *list({ callback }, { call, put }) {
      const response = yield call(service.list);
      if (response.code === 200) {
        yield put({
          type: 'save',
          payload: {
            tableNames: response.data,
          },
        });
        if (callback && response.data.length > 0) {
          callback(response.data[0]);
        }
      } else {
        message.error(response.msg || "unknown error");
      }
    },

    *detail(_, { call, put }) {
      const response = yield call(service.detail);
      if (response.code === 200) {
        yield put({
          type: 'save',
          payload: {
            dataSource: response.data,
          },
        });
      } else {
        message.error(response.msg || "unknown error");
      }
    },

    *create({ payload, callback }, { call, put }) {
      const response = yield call(service.create, payload);
      if (response.code === 200) {
        message.success("create success")
        if (callback) {
          callback();
        }
      } else {
        message.error(response.msg || "unknown error");
      }
    },

    *remake({ payload, callback }, { call, put, select }) {
      const response = yield call(service.create, payload);
      if (response.code === 200) {
        const dataSource = yield select(state =>
          state.hbaseAdmin.dataSource.map(item => {
            const data = { ...item };
            if (data.tableName === payload.tableName) {
              data.deleted = undefined;
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
        message.success("remake success")
      } else {
        message.error(response.msg || "unknown error");
      }
      if (callback) {
        callback();
      }
    },

    *truncate({ payload, callback }, { call, put, select }) {
      const response = yield call(service.truncate, payload);
      if (response.code === 200) {
        const failNum = response.data.length;
        if (failNum === 0) {
          message.success("success all")
        } else {
          message.warn(`failure ${failNum} tables! table names are [${response.data.toString()}]`)
        }
      } else {
        message.error(response.msg || "unknown error");
      }
      if (callback) {
        callback(response.data);
      }
    },

    *del({ payload, callback }, { call, put, select }) {
      const response = yield call(service.del, payload);
      if (response.code === 200) {
        const dataSource = yield select(state =>
          state.hbaseAdmin.dataSource.map(item => {
            const data = { ...item };
            if (data.tableName === payload.tableName) {
              data.deleted = true;
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
        message.success("delete success")
      } else {
        message.error(response.msg || "unknown error");
      }
      if (callback) {
        callback();
      }
    },

    *disable({ payload, callback }, { call, put, select }) {
      const response = yield call(service.disable, payload);
      if (response.code === 200) {
        const dataSource = yield select(state =>
          state.hbaseAdmin.dataSource.map(item => {
            const data = { ...item };
            if (data.tableName === payload.tableName) {
              data.disable = true;
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
        message.success("disable success")
      } else {
        message.error(response.msg || "unknown error");
      }
      if (callback) {
        callback();
      }
    },

    *enable({ payload, callback }, { call, put, select }) {
      const response = yield call(service.enable, payload);
      if (response.code === 200) {
        const dataSource = yield select(state =>
          state.hbaseAdmin.dataSource.map(item => {
            const data = { ...item };
            if (data.tableName === payload.tableName) {
              data.disable = false;
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
        message.success("enable success")
      } else {
        message.error(response.msg || "unknown error");
      }
      if (callback) {
        callback();
      }
    },

    *addFamily({ payload, callback }, { call, put, select }) {
      const response = yield call(service.addFamily, payload);
      if (response.code === 200) {
        message.success("add family success")
      } else {
        message.error(response.msg || "unknown error");
      }
    },

    *deleteFamily({ payload, callback }, { call, put }) {
      const response = yield call(service.deleteFamily, payload);
      if (response.code === 200) {
        message.success("delete family success")
      } else {
        message.error(response.msg || "unknown error");
      }
    },

    *changeFamily({ payload, callback }, { call, put }) {
      const response = yield call(service.changeFamily, payload);
      if (response.code === 200) {
        if (callback) {
          callback();
        }
        message.success("change family success")
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
        tableNames: [],
      };
    },
  },
};
