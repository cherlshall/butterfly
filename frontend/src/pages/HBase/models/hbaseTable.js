import * as service from '@/services/hbase/table';
import { list, listFamily } from '@/services/hbase/admin';
import { message } from 'antd';

export default {
  namespace: 'hbaseTable',

  state: {
    familyAndQualifiers: [],
    dataSource: [],
    dataSourceCol: [],
    tableNames: [],
    families: [],
  },

  effects: {
    *listTableName({ callback }, { call, put }) {
      const response = yield call(list);
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
        message.error(response.msg || 'unknown error');
      }
    },

    *findByPage({ payload, callback }, { call, put }) {
      const response = yield call(service.findByPage, payload);
      if (response.code === 200) {
        const { data } = response;
        const { familyAndQualifiers } = data;
        yield put({
          type: 'save',
          payload: {
            familyAndQualifiers,
            dataSource: data.dataList,
            dataSourceCol: data.dataColList,
          },
        });
        if (callback) {
          if (data.dataList.length > 0) {
            callback(data.dataList[data.dataList.length - 1].rowKey, familyAndQualifiers);
          } else {
            callback(undefined, familyAndQualifiers);
          }
        }
      } else {
        message.error(response.msg || 'unknown error');
      }
    },

    *listFamily({ payload, callback }, { call, put }) {
      const response = yield call(listFamily, payload);
      if (response.code === 200) {
        yield put({
          type: 'save',
          payload: {
            families: response.data,
          },
        });
      } else {
        message.error(response.msg || 'unknown error');
      }
      if (callback) {
        callback();
      }
    },

    *insertRow({ payload, callback }, { call }) {
      const response = yield call(service.insertRow, payload);
      if (response.code === 200) {
        message.success(
          `insert ${response.data} ${response.data.length === 1 ? 'field' : 'fields'} success`
        );
      } else {
        message.error(response.msg || 'unknown error');
      }
      if (callback) {
        callback();
      }
    },

    *recoverRow({ payload, callback }, { call, put, select }) {
      const response = yield call(service.insertRow, payload);
      if (response.code === 200) {
        const { family, qualifier, value } = payload.beans[0];
        const dataSource = yield select(state =>
          state.hbaseTable.dataSource.map(item => {
            const data = { ...item };
            if (data.rowKey === payload.rowKey) {
              data[`${family}.${qualifier}`] = value;
              data.deleted = undefined;
            }
            return data;
          })
        );
        const dataSourceCol = yield select(state =>
          state.hbaseTable.dataSourceCol.map(item => {
            const data = { ...item };

            if (
              data.rowKey === payload.rowKey &&
              data.family === family &&
              data.qualifier === qualifier
            ) {
              data.deleted = undefined;
            }
            return data;
          })
        );
        yield put({
          type: 'save',
          payload: {
            dataSource,
            dataSourceCol,
          },
        });
        message.success('recover success');
      } else {
        message.error(response.msg || 'unknown error');
      }
      if (callback) {
        callback();
      }
    },

    *deleteRow({ payload, callback }, { call, put, select }) {
      const response = yield call(service.deleteRow, payload);
      if (response.code === 200) {
        const dataSource = yield select(state =>
          state.hbaseTable.dataSource.map(item => {
            const data = { ...item };
            if (data.rowKey === payload.rowKey) {
              data.deleted = true;
            }
            return data;
          })
        );
        const dataSourceCol = yield select(state =>
          state.hbaseTable.dataSourceCol.map(item => {
            const data = { ...item };
            if (data.rowKey === payload.rowKey) {
              data.deleted = true;
            }
            return data;
          })
        );
        yield put({
          type: 'save',
          payload: {
            dataSource,
            dataSourceCol,
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

    *deleteCol({ payload, callback }, { call, put, select }) {
      const response = yield call(service.deleteCol, payload);
      if (response.code === 200) {
        const dataSource = yield select(state =>
          state.hbaseTable.dataSource.map(item => {
            const data = { ...item };
            if (data.rowKey === payload.rowKey) {
              data[`${payload.family}.${payload.qualifier}`] = null;
            }
            return data;
          })
        );
        const dataSourceCol = yield select(state =>
          state.hbaseTable.dataSourceCol.map(item => {
            const data = { ...item };
            if (
              data.rowKey === payload.rowKey &&
              data.family === payload.family &&
              data.qualifier === payload.qualifier
            ) {
              data.deleted = true;
            }
            return data;
          })
        );
        yield put({
          type: 'save',
          payload: {
            dataSource,
            dataSourceCol,
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
        columns: [],
        dataSource: [],
      };
    },
  },
};
