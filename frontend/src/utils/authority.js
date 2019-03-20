import { currentAuthority } from '@/services/user';
import { async } from 'q';

// use localStorage to store the authority info, which might be sent from server in actual project.
export function getAuthority(str) {
  let responseAuthority;
  createRequest("/server/user/currentAuthority", (xmlhttp) => {
    if (xmlhttp.readyState==4 && xmlhttp.status==200) {
      const data = JSON.parse(xmlhttp.responseText);
      responseAuthority = data.authority;
    }
  });
  const authorityString =
    typeof str === 'undefined' ? responseAuthority : str;
  // authorityString could be admin, "admin", ["admin"]
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

function createXmlHttp() {
  //创建xmlhttp对象
  if (window.XMLHttpRequest) 
      return new XMLHttpRequest(); 
  else 
      return new ActiveXObject("Microsoft.XMLHTTP");
}
  
function createRequest(url, callback, data) {
  const xmlhttp = createXmlHttp();
  xmlhttp.onreadystatechange = () => {
    callback(xmlhttp);
  };
  xmlhttp.open('post', url, false);
  xmlhttp.setRequestHeader("Content-type","application/x-www-form-urlencoded");
  xmlhttp.send(data);
}
