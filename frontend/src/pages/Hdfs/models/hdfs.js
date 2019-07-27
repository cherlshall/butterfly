import * as fileService from '@/services/hdfsFile';
import * as contentService from '@/services/hdfsContent';
import { message, Tooltip } from 'antd';
import { splitLongText } from '@/utils/utils';

export default {
  namespace: 'hdfs',

  state: {
    detailList: [],
    content: '',
    contentColumn: [],
    contentDataSource: [],
    contentTotal: 0,
  },

  effects: {
    *detail({ payload, callback }, { call, put }) {
      const response = yield call(fileService.detail, payload);
      if (response.code === 200) {
        yield put({
          type: 'save',
          payload: {
            detailList: response.data,
          },
        });
        if (callback) {
          callback();
        }
      } else {
        message.error(response.msg || "unknown error");
      }
    },

    *del({ payload, callback }, { call, put, select }) {
      const response = yield call(fileService.del, payload);
      if (response.code === 200) {
        const detailList = yield select(state =>
          state.hdfs.detailList.map(item => {
            const data = { ...item };
            if (data.path === payload.path) {
              data.deleted = true;
            }
            return data;
          })
        );
        yield put({
          type: 'save',
          payload: {
            detailList,
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

    *create({ payload, callback }, { call, put }) {
      const response = yield call(fileService.create, payload);
      if (response.code === 200) {
        message.success("create success");
        if (callback) {
          callback();
        }
      } else {
        message.error(response.msg || "unknown error");
      }
    },

    *mkdirs({ payload, callback }, { call, put }) {
      const response = yield call(fileService.mkdirs, payload);
      if (response.code === 200) {
        message.success("mkdir success");
        if (callback) {
          callback();
        }
      } else {
        message.error(response.msg || "unknown error");
      }
    },

    *read({ payload, callback }, { call, put }) {
      const response = yield call(contentService.read, payload);
      if (response.code === 200) {
        yield put({
          type: 'save',
          payload: {
            content: response.data,
          },
        });
        if (callback) {
          callback();
        }
      } else {
        message.error(response.msg || "unknown error");
      }
    },

    *readToTable({ payload, callback }, { call, put }) {
      const response = yield call(contentService.readToTable, payload);
      if (response.code === 200) {
        const contentColumn = [];
        response.data.columns.forEach(col => {
          contentColumn.push({
            title: col,
            dataIndex: col,
            render: text => splitLongText(text),
          })
        })
        yield put({
          type: 'save',
          payload: {
            contentColumn,
            contentDataSource: response.data.dataSource,
            contentTotal: response.data.total,
          },
        });
        if (callback) {
          callback();
        }
      } else {
        message.error(response.msg || "unknown error");
      }
    },

    *clearContent(_, { put }) {
      yield put({
        type: 'save',
        payload: {
          content: '',
          contentColumn: [],
          contentDataSource: [],
          contentTotal: 0,
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
        detailList: [],
      };
    },
  },
};
