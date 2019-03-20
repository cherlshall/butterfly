import dva from 'dva';
import createLoading from 'dva-loading';

const runtimeDva = window.g_plugins.mergeConfig('dva');
let app = dva({
  history: window.g_history,
  
  ...(runtimeDva.config || {}),
});

window.g_app = app;
app.use(createLoading());
(runtimeDva.plugins || []).forEach(plugin => {
  app.use(plugin);
});

app.model({ namespace: 'global', ...(require('C:/work/source/butterfly/frontend/src/models/global.js').default) });
app.model({ namespace: 'list', ...(require('C:/work/source/butterfly/frontend/src/models/list.js').default) });
app.model({ namespace: 'login', ...(require('C:/work/source/butterfly/frontend/src/models/login.js').default) });
app.model({ namespace: 'menu', ...(require('C:/work/source/butterfly/frontend/src/models/menu.js').default) });
app.model({ namespace: 'project', ...(require('C:/work/source/butterfly/frontend/src/models/project.js').default) });
app.model({ namespace: 'setting', ...(require('C:/work/source/butterfly/frontend/src/models/setting.js').default) });
app.model({ namespace: 'user', ...(require('C:/work/source/butterfly/frontend/src/models/user.js').default) });
