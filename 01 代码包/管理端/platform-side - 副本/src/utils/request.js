import axios from 'axios';
import Cookies from 'js-cookie';

export const baseURL = 'http://localhost:8081';

const http = axios.create({
    baseURL,
    timeout: 5000,
    headers: {
        'Authorization': `Bearer ${Cookies.get('token')}`
    }
});

http.interceptors.request.use(function (config) {
    return config;
}, function (error) {
    // console.log(error);
    return Promise.reject(error);
});

http.interceptors.response.use(function (response) {
    // console.log(response);
    return response;
}, function (error) {
    // console.log(error);
    return Promise.reject(error);
});

export default http;