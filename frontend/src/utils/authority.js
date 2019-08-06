// token和uid过期时间（小时）
const tokenTimeout = 24;

export function getAuthority() {
  const authorityString = getCookie("token") === "" ? "guest" : "admin";
  let authority;
  try {
    authority = JSON.parse(authorityString);
  } catch (e) {
    authority = authorityString;
  }
  if (typeof authority === 'string') {
    return [authority];
  }
  return authority || ['guest'];
}

export function getToken() {
  return getCookie("token");
}

export function setToken(token) {
  return setCookie('token', token, tokenTimeout);
}

export function removeToken() {
  return removeCookie('token');
}

export function getUid() {
  return getCookie("uid");
}

export function setUid(uid) {
  return setCookie('uid', uid, tokenTimeout);
}

export function removeUid() {
  return removeCookie('uid');
}

function setCookie(name, value, hours, path, domain, secure) {
  let cdata = name + "=" + value;
  if (hours) {
      const d = new Date();
      d.setHours(d.getHours() + hours);
      cdata += "; expires=" + d.toGMTString();
  }
  cdata += path ? ("; path=" + path) : "" ;
  cdata += domain ? ("; domain=" + domain) : "" ;
  cdata += secure ? ("; secure=" + secure) : "" ;
  document.cookie = cdata;
}

function getCookie(name) {
  const reg = eval("/(?:^|;\\s*)" + name + "=([^=]+)(?:;|$)/"); 
  return reg.test(document.cookie) ? RegExp.$1 : "";
}

function removeCookie(name, path, domain){
  setCookie(name, "", -1, path, domain);
}