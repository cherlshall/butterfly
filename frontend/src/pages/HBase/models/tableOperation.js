import * as service from '@/services/tableOperation';
import { list } from '@/services/adminOperation';
import { message } from 'antd';

export default {
  namespace: 'tableOperation',

  state: {
    columns: [],
    dataSource: [],
    tableNames: [],
  },

  effects: {

    *listTableName({ callback }, { call, put }) {
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
      } else {
        message.error(response.msg || "unknown error");
      }
    },

    *findByPage({ payload, callback }, { call, put }) {
      const response = yield call(service.findByPage, payload);
      if (response.success) {
        const data = response.data;
        yield put({
          type: 'save',
          payload: {
            columns: data.tableHead,
            dataSource: data.dataList,
          },
        });
        if (callback && data.dataList.length > 0) {
          callback(data.dataList[data.dataList.length - 1].rowkey);
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
        columns: [],
        dataSource: [],
      };
    },
  },
};
