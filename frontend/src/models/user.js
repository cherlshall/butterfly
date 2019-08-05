import { query as queryUsers, queryCurrent } from '@/services/user';
import { getUid } from '@/utils/authority';

export default {
  namespace: 'user',

  state: {
    list: [],
    currentUser: {},
  },

  effects: {

    *fetch(_, { call, put }) {
      const response = yield call(queryUsers);
      yield put({
        type: 'save',
        payload: response,
      });
    },

    *fetchCurrent(_, { call, put }) {
      const uid = getUid();
      if (uid) {
        const response = yield call(queryCurrent, {uid});
        if (response.code === 200) {
          yield put({
            type: 'saveCurrentUser',
            payload: response.data,
          });
        }
      }
    },

  },

  reducers: {
    save(state, action) {
      return {
        ...state,
        list: action.payload,
      };
    },
    saveCurrentUser(state, action) {
      return {
        ...state,
        currentUser: action.payload || {},
      };
    },
    changeNotifyCount(state, action) {
      return {
        ...state,
        currentUser: {
          ...state.currentUser,
          notifyCount: action.payload.totalCount,
          unreadCount: action.payload.unreadCount,
        },
      };
    },
  },
};
