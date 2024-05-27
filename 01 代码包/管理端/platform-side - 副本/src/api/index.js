import http from '../utils/request';

/**
 * 
 * 首页 *
 */
// 获取统计数据 *
export const getStatisticsApi = () => {
    return http.get('/Admin/statistics/getStatistics');
}

/**
 * 
 * 用户 *
 */

// 用户名和密码登录 *
export const loginApi = (data) => {
    return http.post('/Admin/admin/login', data);
}

// 分页获取用户 *
export const getUsersApi = (data) => {
    return http.post('/Customer/customer/getUsers', data);
}

// 修改该用户 status  *
export const saveUserStatusApi = (data) => {
    return http.post('/Customer/customer/changeStatus', data);
}

// 用户名搜索结果   *
export const getUserSearchedApi = (data) => {
    return http.post('/Customer/customer/searchUsers', data);
}

// id 获取用户详情  *
export const getUserDetailApi = (data) => {
    return http.post('/Customer/customer-detail/getUserDetail', data);
}

/**
 * 
 * 商品种类 *
 */

// 通过父级 id 获取分类 *
export const getTypeListByIdApi = (data) => {
    return http.post('/Commodity/type/getTypeByFatherId', data);
}

// 添加分类 *
export const addTypeApi = (data) => {
    return http.post(`/Commodity/type/addType?name=${data.name}&fatherId=${data.fatherId}`);
}

// 编辑分类 *
export const editTypeApi = (data) => {
    return http.post(`/Commodity/type/editType?id=${data.id}&name=${data.name}`);
}

// 删除分类 *
export const deleteTypeApi = (data) => {
    return http.post(`/Commodity/type/deleteType?id=${data.id}`);
}

/**
 * 
 * 门店 *
 */

// 获取门店信息 *
export const getShopApi = () => {
    return http.get('/Admin/shop/getShop');
}

// 修改门店信息 *
export const changeShopApi = (data) => {
    return http.post('/Admin/shop/changeShop', data);
}

/**
 * 
 * 管理员   *
 */

// 获取管理员列表   *
export const getAdminsApi = () => {
    return http.get('/Admin/admin/getAdmins');
}

//  修改管理员状态  *
export const saveAdminStatusApi = (data) => {
    return http.post('/Admin/admin/changeAdminStatus', data);
}

//  修改管理员信息  *
export const changeAdminInfoApi = (data) => {
    return http.post('/Admin/admin/changeAdminInfo', data);
}

// 删除管理员   *
export const deleteAdminApi = (data) => {
    return http.post('/Admin/admin/deleteAdmin', data);
}

// 添加管理员   *
export const addAdminApi = (data) => {
    return http.post('/Admin/admin/addAdmin', data);
}

/**
 * 
 * 商品 *
 */

// 获取商品 *
export const getGoodsApi = (data) => {
    return http.post(`/Commodity/commodity/getGoods?page=${data.page}&limit=${data.limit}`);
}
// 搜索商品 *
export const getGoodsSearchedApi = (data) => {
    return http.post(`/Commodity/commodity/searchGoods`, data);
}
// 添加商品 *
export const addGoodsApi = (data) => {
    return http.post('/Commodity/commodity/addGoods', data);
}
// 编辑商品 *
export const editGoodsApi = (data) => {
    return http.post('/Commodity/commodity/editGoods', data);
}
// 修改商品状态 *
export const saveGoodsStatusApi = (data) => {
    return http.post(`/Commodity/commodity/delistGoods?id=${data}`);
}

/**
 * 
 * 优惠券   *
 */

//  获取优惠券  *
export const getCouponApi = (data) => {
    return http.post('/Discount/coupons/page', data);
}

// 修改优惠券状态   *
export const saveCouponStatusApi = (data) => {
    return http.post(`/Discount/coupons/updateStatus/${data.status}`, { ids: [data.id] });
}

// 添加优惠券   *
export const addCouponApi = (data) => {
    return http.post('/Discount/coupons/add', data);
}

// 删除优惠券   *
export const deleteCouponApi = (data) => {
    return http.delete(`/Discount/coupons/delete?ids=${data}`);
}

// 编辑优惠券   *
export const editCouponApi = (data) => {
    return http.put('/Discount/coupons/update', data);
}

/**
 * 
 * 折扣 *
 */

// 获取折扣 *
export const getDiscountApi = (data) => {
    return http.post('/Discount/discount/page', data);
}
// 修改状态 *
export const saveDiscountStatusApi = (data) => {
    return http.put(`/Discount/discount/updateStatus/${data.status}?ids=${data.id}`);
}
// 添加折扣 *
export const addDiscountApi = (data) => {
    return http.post('/Discount/discount/add', data);
}
// 删除折扣 *
export const deleteDiscountApi = (data) => {
    return http.delete(`/Discount/discount/delete?ids=${data}`);
}
// 编辑折扣 *
export const editDiscountApi = (data) => {
    return http.put('/Discount/discount/update', data);
}
// 获取参与折扣的商品   *
export const getParticipatingApi = (data) => {
    return http.post('/Discount/discount/pageByDiscountId', data);
}
//  取消折扣    *
export const cancelDiscountApi = (data) => {
    return http.post(`/Discount/discount/deleteCommodity/${data.discountId}`, {
        commodityIds: data.commodityIds
    });
}
// 获取未参与折扣的商品 *
export const getNotParticipatingApi = (data) => {
    return http.post('/Discount/discount/pageCommodityWithoutDiscount', data);
}
// 添加折扣商品 *
export const setDiscountApi = (data) => {
    // console.log(data);
    return http.post(`/Discount/discount/addCommodity/${data.discountId}`, {
        commodityIds: data.commodityIds
    });
}

/**
 * 
 * 评论 *
 */

// 删除评论 *
export const deleteCommentApi = (data) => {
    return http.post('/Comments/comments/delete', data);
}

// 获取评论 *
export const getCommentsApi = (data) => {
    return http.post('/Comments/comments/pageByCommodityId', data);
}
// 回复评论 *
export const replyCommentApi = (data) => {
    return http.put('/Comments/comments/update', data);
}

/**
 * 
 * 订单 *
 */
// 获取订单 *
export const getOrdersApi = (data) => {
    console.log(data);
    return http.post('/order/page', data);
}
// 派送订单 *
export const deliverOrderApi = (data) => {
    return http.put(`/order/deliver/`, { id: data });
}