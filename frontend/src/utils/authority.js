// token和uid过期时间（小时）
const tokenTimeout = 24 * 7;

const identityConf = {
  visitor: [],
  nca_user: ['visitor'],
  nca_admin: ['nca_user'],
  ndds_user: ['visitor'],
  ndds_admin: ['ndds_user'],
  super: ['nca_admin', 'ndds_admin'],
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

export function permit(needAuths) {
  const currentAuths = getAuthority();
  if (typeof needAuths === 'string') {
    return permit0(currentAuths, needAuths);
  }
  for (let i = 0; i < needAuths.length; i++) {
    if (permit0(currentAuths, needAuths)) {
      return true;
    }
  }
  return false;
}

function permit0(currentAuths, needAuth) {
  for (let i = 0; i < currentAuths.length; i++) {
    const currentAuth = currentAuths[i];
    if (currentAuth === needAuth) {
      return true;
    }
    const currentIdentity = identityConf[currentAuth];
    if (currentIdentity !== undefined) {
      for (let j = 0; j < currentIdentity.length; j++) {
        if (permit0(currentIdentity, needAuth)) {
          return true;
        }
      }
    }
  }
  return false;
}

export function getToken() {
  return getCookie("token");
}

export function setToken(token, autoLogin) {
  setCookie('token', token, autoLogin ? tokenTimeout : null, '/');
}

export function removeToken() {
  removeCookie('token', '/');
}

export function getAuthority() {
  const authorityString = getCookie("authority") || "guest";
  return authorityString.split('&');
}

export function setAuthority(token, autoLogin) {
  setCookie('authority', token, autoLogin ? tokenTimeout : null, '/');
}

export function removeAuthority() {
  removeCookie('authority', '/');
}
