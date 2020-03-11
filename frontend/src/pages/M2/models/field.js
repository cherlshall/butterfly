import * as service from '@/services/m2/field';
import * as protocolService from '@/services/m2/protocol';
import * as typeService from '@/services/m2/type';
import * as accountService from '@/services/account';
import { message } from 'antd';

export default {
  namespace: 'm2Field',

  state: {
    list: [],
    total: 0,
    protocol: {
      id: 0,
      type: 0,
      protocolId: 0,
      category: 0,
      cnName: '',
      enName: '',
      description: '',
      active: 1,
      protocolEnName: '',
    },
    typeList: [],
    baseTypeList: [], // not contains tlv and struct
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

    *upload({ payload, callback }, { call }) {
      console.log(111);
      const response = yield call(accountService.upload, payload);
      console.log(2222);
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
            total: response.data.total,
          },
        });
      } else {
        message.error(response.msg || 'unknown error');
      }
    },

    *getList({ payload, callback }, { call, put }) {
      const response = yield call(service.listByPage, payload);
      if (response.code === 200 && callback) {
        callback(response.data);
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

    *listType({ payload }, { call, put }) {
      const response = yield call(typeService.listActive, payload);
      if (response.code === 200) {
        const typeList = [];
        const baseTypeList = [];
        response.data.forEach(item => {
          const type = {
            id: item.id,
            text: item.cnName,
            value: item.enName,
          };
          typeList.push(type);
          if (type.text !== 'TLV' && type.text !== '结构体') {
            baseTypeList.push(type);
          }
        });
        yield put({
          type: 'save',
          payload: {
            typeList,
            baseTypeList,
          },
        });
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
