/*
  fetch
  HOW TO USE：
  Fetch('/users/list', {
    method: 'GET', //缺省为'GET'
    body: { limit: 20 },
    norest: true, //针对post请求，缺省:content-type为json，true:content-type为默认的form类型
  }).then((data) => {
    this.setState({
      data: data.result.list
    });
    //...
  }, (errMessage) => {
    //...
  })
*/
// import 'whatwg-fetch';
const toQueryString = function (obj) {
  const keys = obj && Object.keys(obj);
  let params;
  if (keys && keys.length > 0) {
    params = keys.map(key => {
      let value = obj[key];
      if (typeof value === 'object') {
        value = JSON.stringify(value);
      }
      return key + '=' + encodeURIComponent(value);
    }).join('&');
  }
  return params;
};
const Fetch = (url, options) => {
  options = options || {};
  options.method = (options.method || 'GET').toUpperCase();
  options.headers = options.headers || {};
  options.credentials = "same-origin";

  // 处理GET方法的提交数据
  if (!options.method || options.method === 'GET') {
    var str = toQueryString(options.body);
    if (str) {
      url += ~url.indexOf('?') ? '&' : '?';
      url += str;
    }
    delete options.body;
    options.headers['Accept'] = 'application/json';
    options.headers['Content-Type'] = 'application/json';
  }
  if (options.method && options.method.toUpperCase() === 'POST') {
    // 处理默认Content-Type
    if (options.norest) {
      options.headers['Content-Type'] = 'application/x-www-form-urlencoded;charset=utf-8';
    }
    // 处理默认Content-Type的提交数据
    if (options.headers['Content-Type'] && options.headers['Content-Type'] === 'application/x-www-form-urlencoded;charset=utf-8') {
      options.body = toQueryString(options.body);
    }
    // 添加Content-Type
    if (!options.headers['Content-Type'] && !options.formData) {
      options.headers['Accept'] = 'application/json';
      options.headers['Content-Type'] = 'application/json';
    }
    // 处理提交的json对象
    if (options.headers['Content-Type'] === 'application/json') {
      if (options.body) {
        options.body = JSON.stringify(options.body);
      }
    }
  }
  return new Promise(function (resolve, reject) {
    fetch(url, options)
      .then(function (response) {
        if (response.ok) {
          return response.json();
        } else {
          reject(response.statusText);
        }
      }).then(function (json) {
        if (!json) return;
        if (json.success) {
          resolve(json);
        } else {
          reject(json);
        }
      }).catch(function (ex) {
        throw (ex);
      });
  });
}

export default Fetch;