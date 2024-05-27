import axios from 'axios';
import { Message } from 'element-ui';
import Cookies from 'js-cookie';
// export const BaseURL = 'http://192.168.32.125:8081';
export const BaseURL = 'http://localhost:8081';
// export const BaseURL = 'http://10.203.189.59:8081';
const http = axios.create({
    baseURL: BaseURL,
    timeout: 5000,
    headers: {
        Authorization: `Bearer ${Cookies.get('token')}`
    }
});

http.interceptors.request.use(function (config) {
    // console.log(config);
    return config;
}, function (error) {
    // console.log(error);
    return Promise.reject(error);
});

http.interceptors.response.use(function (response) {
    if (response.data.code === 501) {
        Message({
            type: 'error',
            message: '登录过期，请重新登录'
        })
        console.log('token 过期');
    }
    return response;
}, function (error) {
    // console.log(error);
    return Promise.reject(error);
});

export default http;


export function backToTop() {
    let scrollTop = document.documentElement.scrollTop || document.body.scrollTop;
    if (scrollTop > 0) {
        window.requestAnimationFrame(backToTop);
        window.scrollTo(0, scrollTop - (scrollTop * 100));
    }
}

export function getClientSize() {
    let h = document.documentElement.clientHeight || document.body.clientHeight;
    let w = document.documentElement.clientWidth || document.body.clientWidth;
    return {
        width: w,
        height: h
    }
}

//获取滚动条宽度
export function getScrollWidth() {
    let noScroll, scroll, oDiv = document.createElement("DIV");
    oDiv.style.cssText = "position:absolute; top:-1000px; width:100px; height:100px; overflow:hidden;";
    noScroll = document.body.appendChild(oDiv).clientWidth;
    oDiv.style.overflowY = "scroll";
    scroll = oDiv.clientWidth;
    document.body.removeChild(oDiv);
    return noScroll - scroll;
}