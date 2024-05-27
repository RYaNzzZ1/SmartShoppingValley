import http from '../utils/request';
import { BaseURL } from '../utils/request';

/**
 * 先输出 data 确认形式是否正确再发送请求 
 * 
 */


// 发送验证码
export const sendCodeApi = (data) => {
    console.log('/Customer/customer/sendCode', data);
    return http.post('/Customer/customer/sendCode', data);
}

// 验证码登录
export const loginByCodeApi = (data) => {
    console.log('/Customer/customer/loginByCode', data);
    return http.post('/Customer/customer/loginByCode', data);
}

// 密码登录
export const loginByPWApi = (data) => {
    console.log('/Customer/customer/loginByPW', data);
    return http.post('/Customer/customer/loginByPW', data);
}

// 顾客注册
export const registerApi = (data) => {
    console.log('/Customer/customer/register', data);
    return http.post('/Customer/customer/register', data);
}

//获取商品列表
export const getGoodsListApi = (data) => {
    console.log('/Commodity/commodity/getCommodityByCategoryId', data);
    console.log(BaseURL + "/Commodity/commodity/getCommodityByCategoryId" + "?categoryId=" + parseInt(data), data);
    return http.post(BaseURL + "/Commodity/commodity/getCommodityByCategoryId" + "?categoryId=" + parseInt(data), data);
}

//获取分类
export const getTypesApi = (data) => {
    console.log('/Commodity/type/getTypeByFatherId', data);
    return http.post('/Commodity/type/getTypeByFatherId', data);
}

//搜索商品
export const searchGoodsApi = (data) => {
    console.log('/Commodity/commodity/searchGoods', data);
    return http.post('/Commodity/commodity/searchGoods', data);
}

//获取所有商品
export const getAllGoodsApi = (data) => {
    console.log('/Commodity/commodity/getGoods', data);
    return http.post('/Commodity/commodity/getGoods', data);
}

//获取城市列表
export const listByProvinceIdApi = (data) => {
    console.log('/Address/city/listByProvinceId', data);
    return http.get(BaseURL + "/Address/city/listByProvinceId" + "?provinceId=" + parseInt(data), data);
}

//获取省份列表
export const ProvincelistApi = (data) => {
    console.log('/Address/province/list', data);
    return http.get('/Address/province/list', data);
}

//设置默认地址
export const setDefaultApi = (data) => {
    console.log('/Customer/address-book/setDefault', data);
    return http.post('/Customer/address-book/setDefault', data);
}


//获取购物车
export const getCartApi = (data) => {
    return http.get('/Customer/shopping-cart/list');
}

//删除购物车
export const deleteCartApi = (data) => {
    console.log(data);
    return http.post('/Customer/shopping-cart/delete', data);
}



//删除订单
export const deleteOrderApi = (data) => {
    console.log(data);
    return http.put('/order/cancel', data);
}

//获取顾客信息
export const getCustomerDataApi = (data) => {
    console.log(data);
    return http.post('/Customer/customer-detail/getCustomerData', data);
}

//更新顾客信息
export const updateCustomerDataApi = (data) => {
    console.log(data);
    return http.post('/Customer/customer-detail/editCustomer', data);
}

//修改密码
export const editPasswordApi = (data) => {
    console.log(data);
    return http.post('/Customer/customer/editPassword', data);
}

//发送评论
export const sendCommentApi = (data) => {
    console.log(data);
    return http.post('/Comments/comments/add', data);
}

//获取商品信息
export const getGoodsInfoApi = (data) => {
    console.log('/Commodity/commodity/getGoodsDetail', data);
    return http.post("/Commodity/commodity/getGoodsDetail", data);
}

//加入购物车
export const addCartApi = (data) => {
    console.log(data);
    return http.post("/Customer/shopping-cart/update", data);
}

export const addCartFromDetailApi = (data) => {
    console.log(data);
    return http.post("/Customer/shopping-cart/add", data);
}

// 展示所有地址
export const showAddressApi = (data) => {
    console.log(data);
    return http.post("/Customer/address-book/getAll", data);
}

// 添加地址
export const addAddressApi = (data) => {
    console.log(data);
    return http.post("/Customer/address-book/add", data);
}

//删除地址
export const deleteAddressApi = (data) => {
    console.log(data);
    return http.post("/Customer/address-book/delete", data);
}

//修改地址
export const modifyAddressApi = (data) => {
    console.log(data);
    return http.post("/Customer/address-book/edit", data);
}

//获取所有订单
export const getAllOrderApi = (data) => {
    console.log(data);
    return http.get("/order/list", data);
}

//获取所有订单
export const getDetailOrderApi = (data) => {
    console.log(data);
    return http.post("/order/detail/show", data);
}

//删除评论
export const deleteCommentApi = (data) => {
    console.log(data);
    return http.post('/Comments/comments/delete', data);
}

//获取平均分
export const getAverageScoreApi = (data) => {
    console.log(data);
    return http.post('/Comments/comments/getAverageScore', data);
}

//获取评论
// 立即购买提交订单
export const submitOrderApi_0 = (data) => {
    return http.post('/order/detail/purchase', data);
}
// 购物车提交订单
export const submitOrderApi_1 = (data) => {
    return http.post('/order/submit', data);
}

//获取评论
export const getCommentApi = (data) => {
    console.log(data);
    return http.post('/Comments/comments/pageByCommodityId', data);
}

// 获取收货人的地址簿
export const getAddressApi = (data) => {
    return http.post('/Customer/address-book/getAll', data);
}

/**
 * 优惠券
 */

// 查看可领取的优惠券
export const getCouponsApi = () => {
    return http.get('/Discount/coupons/list');
}
// 查看已领取的优惠券
export const getReceivedCouponsApi = () => {
    return http.get('/Discount/coupons/get');
}
// 领取优惠券
export const receiveCouponApi = (data) => {
    return http.post('/Discount/coupons/receive', data);
}

