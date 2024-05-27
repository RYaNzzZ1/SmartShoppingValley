<template>
  <div id="goods">
    <el-card shadow="never" class="searchBar">
      <i class="el-icon-search"></i><span> 筛选搜索 </span>
      <el-button @click="resetInput" class="resetBtn">重置</el-button>
      <el-button @click="searchInput" class="searchBtn" type="primary"
        >查询搜索</el-button
      >
      <br />
      <br />
      <br />
      <el-form
        inline
        :model="searchForm"
        status-icon
        ref="searchForm"
        label-width="0"
        class="demo-ruleForm"
      >
        <el-form-item>
          <label>商品编号</label>
          <el-input v-model="searchForm.id"></el-input>
        </el-form-item>
        <el-form-item>
          <label>商品类别</label>
          <el-cascader
            v-model="selectedItems"
            :options="fatherTypeList"
            :props="{ label: 'name', value: 'id', children: 'chiledren' }"
            :show-all-levels="false"
            @change="handleSelectionChange"
          ></el-cascader>
        </el-form-item>
        <el-form-item>
          <label>商品名称</label>
          <el-input v-model="searchForm.name"></el-input>
        </el-form-item>
        <el-form-item>
          <label>商品状态</label>
          <el-select v-model="searchForm.status" placeholder="请选择">
            <el-option label="停售" :value="0"> </el-option>
            <el-option label="起售" :value="1"> </el-option>
          </el-select>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card class="dataBar" shadow="never">
      <i class="el-icon-document"></i><span> 商品列表 </span>
      <el-button @click="handleAdd">添加</el-button>
    </el-card>

    <el-table
      v-loading="loading"
      class="table"
      :data="goods"
      style="width: 100%"
      :border="true"
      :max-height="800"
    >
      <el-table-column prop="id" label="编号"> </el-table-column>
      <el-table-column label="商品图片">
        <template slot-scope="scope">
          <img
            class="goodsImg"
            :src="`${baseURL}/ImageFile/image/download?url=${scope.row.urls[0]}`"
            :alt="scope.name"
          />
        </template>
      </el-table-column>
      <el-table-column prop="name" label="名称"> </el-table-column>
      <el-table-column prop="price" label="价格"> </el-table-column>
      <el-table-column prop="num" label="库存量"> </el-table-column>
      <el-table-column prop="status" label="状态">
        <template slot-scope="scope">
          <el-tag :type="scope.row.status == 0 ? 'danger' : ''">{{
            scope.row.status == 0 ? "停售" : "起售"
          }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="创建时间"> </el-table-column>
      <el-table-column prop="description" label="商品描述">
        <template slot-scope="scope">
          {{ scope.row.description || "无" }}
        </template>
      </el-table-column>
      <el-table-column label="商品评论" width="220">
        <template slot-scope="scope">
          <div class="commentBtn">
            <el-button size="mini" x @click="handleComment(scope.row)"
              >查看评论</el-button
            >
          </div>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="300">
        <template slot-scope="scope">
          <el-button size="mini" @click="handleEnable(scope.row)"
            >起售</el-button
          >
          <el-button size="mini" @click="handleEdit(scope.row)">编辑</el-button>
          <el-button size="mini" type="danger" @click="handleDisable(scope.row)"
            >停售</el-button
          >
        </template>
      </el-table-column>
    </el-table>

    <div class="block">
      <el-pagination
        background
        layout="prev, pager, next"
        :page-size="pageData.limit"
        @current-change="handlePageChange"
        :total="pageData.total"
        :current-page.sync="pageData.page"
      >
      </el-pagination>
    </div>

    <el-dialog title="提示" :visible.sync="stopSaleDialogVisible" width="30%">
      <span>确认停售该商品？</span>
      <span slot="footer" class="dialog-footer">
        <el-button @click="stopSaleDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="confirmStopSale">确 定</el-button>
      </span>
    </el-dialog>

    <el-dialog
      title="编辑商品"
      :visible.sync="editDialogVisible"
      width="50%"
      top="10px"
    >
      <el-form
        :model="editForm"
        status-icon
        :rules="editRules"
        ref="editForm"
        label-width="0"
        class="demo-ruleForm"
      >
        <el-form-item prop="name">
          <label>商品</label><br />
          <el-input
            v-model="editForm.name"
            autocomplete="off"
            placeholder="请输入商品名"
          ></el-input>
        </el-form-item>
        <el-form-item prop="price">
          <label>定价</label><br />
          <el-input
            v-model="editForm.price"
            placeholder="请输入商品定价（￥）"
          ></el-input>
        </el-form-item>
        <el-form-item prop="num">
          <label>库存量</label><br />
          <el-input
            v-model="editForm.num"
            placeholder="请输入库存量"
          ></el-input>
        </el-form-item>

        <div>
          <label class="attributeLabel">商品属性</label><br />
          <el-form-item prop="commodityAttributeList" class="attributeBar">
            <el-input
              class="attributeNameInput"
              v-model="attributeName_1"
              placeholder="请输入属性名"
            ></el-input>
            <el-input
              class="attributeValueInput"
              v-model="attributeValue_1"
              placeholder="请输入属性(英文逗号分隔)"
            ></el-input>
          </el-form-item>
          <el-form-item prop="commodityAttributeList" class="attributeBar">
            <el-input
              class="attributeNameInput"
              v-model="attributeName_2"
              placeholder="请输入属性名"
            ></el-input>
            <el-input
              class="attributeValueInput"
              v-model="attributeValue_2"
              placeholder="请输入属性(英文逗号分隔)"
            ></el-input>
          </el-form-item>
          <el-form-item prop="commodityAttributeList" class="attributeBar">
            <el-input
              class="attributeNameInput"
              v-model="attributeName_3"
              placeholder="请输入属性名"
            ></el-input>
            <el-input
              class="attributeValueInput"
              v-model="attributeValue_3"
              placeholder="请输入属性(英文逗号分隔)"
            ></el-input>
          </el-form-item>
        </div>

        <el-form-item>
          <label>商品类别</label>
          <el-cascader
            v-model="selectedItems_3"
            :options="fatherTypeList"
            :props="{ label: 'name', value: 'id', children: 'chiledren' }"
            :show-all-levels="false"
            @change="handleSelectionChange_3"
          ></el-cascader>
        </el-form-item>
        <el-form-item>
          <label>商品描述</label>
          <el-input type="textarea" v-model="editForm.description"></el-input>
        </el-form-item>
        <el-upload
          class="upload-demo"
          drag
          name="files"
          :action="`${baseURL}/ImageFile/image/upload/`"
          :headers="{
            Authorization: `Bearer ${Cookies.get('token')}`,
          }"
          :on-success="handleSuccess_2"
          multiple
        >
          <i class="el-icon-upload"></i>
          <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
          <div class="el-upload__tip" slot="tip">
            只能上传jpg/png文件，且不超过500kb
          </div>
        </el-upload>
        <el-form-item>
          <div class="formBtnContainer">
            <el-button type="primary" @click="confirmEdit('addForm')"
              >确认</el-button
            >
            <el-button @click="cancelEdit('addForm')">取消</el-button>
          </div>
        </el-form-item>
      </el-form>
    </el-dialog>

    <el-dialog title="评论" :visible.sync="commentDialogVisible">
      <el-table :data="comments" v-loading="commentLoading">
        <el-table-column
          property="id"
          label="编号"
          width="80"
        ></el-table-column>
        <el-table-column
          property="customerId"
          label="用户编号"
          width="80"
        ></el-table-column>
        <el-table-column
          property="score"
          label="评分"
          width="80"
        ></el-table-column>
        <el-table-column
          property="content"
          label="内容"
          width="200"
        ></el-table-column>
        <el-table-column
          property="createTime"
          label="评论时间"
          width="200"
        ></el-table-column>
        <el-table-column property="reply" label="回复" width="200">
          <template slot-scope="scope">
            {{ scope.row.reply || "暂无" }}
          </template>
        </el-table-column>
        <el-table-column label="评论图片">
          <template slot-scope="scope">
            <img
              class="commentImg"
              :src="`${baseURL}/ImageFile/image/download?url=${item}`"
              v-for="item in scope.row.urls"
              :key="item"
            />
            <span v-if="scope.row.urls.length === 0">暂无</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" @click="handleReply(scope.row)"
              >回复</el-button
            >
            <el-button
              size="mini"
              type="danger"
              @click="handleCommentDelete(scope.row)"
              >删除</el-button
            >
          </template>
        </el-table-column>
      </el-table>

      <div class="paginationContainer">
        <el-pagination
          background
          layout="prev, pager, next"
          :page-size="commentPageData.limit"
          @current-change="handlePageChange"
          :total="commentPageData.total"
          :current-page.sync="commentPageData.page"
        >
        </el-pagination>
      </div>
    </el-dialog>

    <el-dialog
      title="提示"
      :visible.sync="deleteCommentDialogVisible"
      width="30%"
    >
      <span>确认删除该评论？</span>
      <span slot="footer" class="dialog-footer">
        <el-button @click="deleteCommentDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="confirmCommentDelete"
          >确 定</el-button
        >
      </span>
    </el-dialog>

    <el-dialog
      title="回复评论"
      :visible.sync="replyCommentDialogVisible"
      width="30%"
    >
      <el-input
        type="textarea"
        v-model="reply"
        placeholder="回复该评论"
      ></el-input>
      <span slot="footer" class="dialog-footer">
        <el-button @click="replyCommentDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="confirmCommentReply">确 定</el-button>
      </span>
    </el-dialog>

    <el-drawer
      class="addDrawer"
      title="添加商品"
      :visible.sync="drawer"
      :with-header="true"
      size="35%"
    >
      <el-card shadow="never">
        <el-form
          :model="addForm"
          status-icon
          :rules="addRules"
          ref="addForm"
          label-width="0"
          class="demo-ruleForm"
        >
          <el-form-item prop="name">
            <label>商品</label><br />
            <el-input
              v-model="addForm.name"
              autocomplete="off"
              placeholder="请输入商品名"
            ></el-input>
          </el-form-item>
          <el-form-item prop="price">
            <label>定价</label><br />
            <el-input
              v-model="addForm.price"
              placeholder="请输入商品定价（￥）"
            ></el-input>
          </el-form-item>
          <el-form-item prop="num">
            <label>库存量</label><br />
            <el-input
              v-model="addForm.num"
              placeholder="请输入库存量"
            ></el-input>
          </el-form-item>

          <div>
            <label class="attributeLabel">商品属性</label><br />
            <el-form-item prop="commodityAttributeList" class="attributeBar">
              <el-input
                class="attributeNameInput"
                v-model="attributeName_1"
                placeholder="请输入属性名"
              ></el-input>
              <el-input
                class="attributeValueInput"
                v-model="attributeValue_1"
                placeholder="请输入属性(英文逗号分隔)"
              ></el-input>
            </el-form-item>
            <el-form-item prop="commodityAttributeList" class="attributeBar">
              <el-input
                class="attributeNameInput"
                v-model="attributeName_2"
                placeholder="请输入属性名"
              ></el-input>
              <el-input
                class="attributeValueInput"
                v-model="attributeValue_2"
                placeholder="请输入属性(英文逗号分隔)"
              ></el-input>
            </el-form-item>
            <el-form-item prop="commodityAttributeList" class="attributeBar">
              <el-input
                class="attributeNameInput"
                v-model="attributeName_3"
                placeholder="请输入属性名"
              ></el-input>
              <el-input
                class="attributeValueInput"
                v-model="attributeValue_3"
                placeholder="请输入属性(英文逗号分隔)"
              ></el-input>
            </el-form-item>
          </div>

          <el-form-item>
            <label>商品类别</label>
            <el-cascader
              v-model="selectedItems_2"
              :options="fatherTypeList"
              :props="{ label: 'name', value: 'id', children: 'chiledren' }"
              :show-all-levels="false"
              @change="handleSelectionChange_2"
            ></el-cascader>
          </el-form-item>
          <el-form-item>
            <label>商品描述</label>
            <el-input type="textarea" v-model="addForm.description"></el-input>
          </el-form-item>
          <el-upload
            class="upload-demo"
            drag
            name="files"
            :action="`${baseURL}/ImageFile/image/upload/`"
            :on-success="handleSuccess"
            multiple
          >
            <i class="el-icon-upload"></i>
            <div class="el-upload__text">
              将文件拖到此处，或<em>点击上传</em>
            </div>
            <div class="el-upload__tip" slot="tip">
              只能上传jpg/png文件，且不超过500kb
            </div>
          </el-upload>
          <el-form-item>
            <div class="formBtnContainer">
              <el-button type="primary" @click="confirmAdd('addForm')"
                >确认</el-button
              >
              <el-button @click="cancelAdd('addForm')">取消</el-button>
            </div>
          </el-form-item>
        </el-form>
      </el-card>
    </el-drawer>
  </div>
</template>

<script>
import { mapState, mapActions } from "vuex";
import { Message } from "element-ui";
import { baseURL } from "../utils/request.js";
import Cookies from "js-cookie";
export default {
  name: "Goods",
  data() {
    return {
      baseURL: baseURL,
      Cookies: Cookies,
      searchForm: {
        id: "",
        categoryId: "",
        name: "",
        status: "",
      },
      fatherTypeList: [
        // {
        //   id: 0,
        //   fatherId: -1,
        //   name: "衣服",
        //   createTime: "2023-8-31",
        //   chiledren: [
        //     {
        //       id: 6,
        //       fatherId: 0,
        //       name: "衬衫",
        //       createTime: "2023-8-31",
        //     },
        //     {
        //       id: 7,
        //       fatherId: 0,
        //       name: "衬衫",
        //       createTime: "2023-8-31",
        //     },
        //     {
        //       id: 8,
        //       fatherId: 0,
        //       name: "衬衫",
        //       createTime: "2023-8-31",
        //     },
        //   ],
        // },
      ],
      selectedItems: "",
      selectedItems_2: "",
      selectedItems_3: "",
      deleteDialogVisible: false,
      stopSaleDialogVisible: false,
      deleteId: "",
      addForm: {
        categoryId: "",
        name: "",
        price: "",
        num: "",
        description: "",
        commodityAttributeList: [
          {
            // commodityId: 0,
            name: "",
            value: "",
          },
        ],
        urls: [],
      },
      attributeName_1: "",
      attributeName_2: "",
      attributeName_3: "",
      attributeValue_1: "",
      attributeValue_2: "",
      attributeValue_3: "",
      addRules: {
        name: [{ required: true, message: "请输入商品名", trigger: "blur" }],
        price: [{ required: true, message: "请输入定价", trigger: "blur" }],
        num: [{ required: true, message: "请输入库存量", trigger: "blur" }],
        categoryId: [
          { required: true, message: "请选择商品分类", trigger: "blur" },
        ],
        status: [
          {
            required: true,
            message: "请选择商品状态",
            trigger: "blur",
          },
        ],
      },
      editForm: {
        categoryId: "",
        name: "",
        price: "",
        num: "",
        description: "",
        commodityAttributeList: [
          {
            name: "",
            value: "",
          },
        ],
        urls: [],
      },
      editRules: {
        name: [{ required: true, message: "请输入商品名", trigger: "blur" }],
        price: [{ required: true, message: "请输入定价", trigger: "blur" }],
        num: [{ required: true, message: "请输入库存量", trigger: "blur" }],
        categoryId: [
          { required: true, message: "请选择商品分类", trigger: "blur" },
        ],
      },
      drawer: false,
      editDialogVisible: false,
      commentDialogVisible: false,
      deleteCommentDialogVisible: false,
      commentId: "",
      currentCommentGoodsId: "",
      replyCommentDialogVisible: false,
      reply: "",
      disableId: "",
    };
  },
  methods: {
    ...mapActions("Type", ["getTypeListById"]),
    ...mapActions("Goods", [
      "getSearched",
      "getGoods",
      "getGoodsImg",
      "deleteGoods",
      "editGoods",
      "saveGoodsStatus",
      "addGoods",
      "deleteGoods",
      "getComments",
      "deleteComment",
      "replyComment",
    ]),
    isObjectEmpty(obj) {
      for (let key in obj) {
        if (obj[key] === "") {
          return false;
        }
      }
      return true;
    },
    resetInput() {
      this.searchForm = {
        id: "",
        categoryId: "",
        name: "",
        status: "",
      };
      this.getGoods().catch((err) => {
        console.log(err);
        Message({
          message: "网络连接超时，请稍后重试",
          type: "error",
        });
      });
    },
    searchInput() {
      this.getSearched(this.searchForm);
    },
    handleSelectionChange() {
      this.searchForm.categoryId =
        this.selectedItems[this.selectedItems.length - 1];
    },
    handleSelectionChange_2() {
      this.addForm.categoryId =
        this.selectedItems_2[this.selectedItems_2.length - 1];
    },
    handleSelectionChange_3() {
      this.editForm.categoryId =
        this.selectedItems_3[this.selectedItems_3.length - 1];
    },
    handlePageChange() {
      this.getGoods().catch((err) => {
        Message({
          type: "error",
          message: "网络连接超时，请稍后重试",
        });
      });
    },
    handleEnable(row) {
      if (row.status === 0) {
        this.saveGoodsStatus(row.id)
          .then(() => {
            console.log("完成起售api");
            if (this.isObjectEmpty(this.searchForm)) {
              this.getSearched(this.searchForm);
            } else {
              this.getGoods();
            }
            Message({
              type: "success",
              message: "起售成功",
            });
          })
          .catch((err) => {
            console.log(err);
            Message({
              message: "网络连接超时，请稍后重试",
              type: "error",
            });
          });
      }
    },
    handleDisable(row) {
      if (row.status === 1) {
        this.stopSaleDialogVisible = true;
        this.disableId = row.id;
      }
    },
    confirmStopSale() {
      this.saveGoodsStatus(this.disableId)
        .then(() => {
          if (this.isObjectEmpty(this.searchForm)) {
            this.getSearched(this.searchForm);
          } else {
            this.getGoods();
          }
          this.deleteDialogVisible = false;
          Message({
            type: "success",
            message: "停售成功",
          });
          this.stopSaleDialogVisible = false;
        })
        .catch((err) => {
          console.log(err);
          Message({
            message: "网络连接超时，请稍后重试",
            type: "error",
          });
        });
    },
    handleAdd() {
      this.drawer = true;
    },
    confirmAdd() {
      if (this.attributeName_1 !== "" && this.attributeValue_1 !== "") {
        this.addForm.commodityAttributeList.push({
          name: this.attributeName_1,
          value: this.attributeValue_1,
        });
      }
      if (this.attributeName_2 !== "" && this.attributeValue_2 !== "") {
        console.log("属性2");
        this.addForm.commodityAttributeList.push({
          name: this.attributeName_2,
          value: this.attributeValue_2,
        });
      }
      if (this.attributeName_3 !== "" && this.attributeValue_3 !== "") {
        console.log("属性3");
        this.addForm.commodityAttributeList.push({
          name: this.attributeName_3,
          value: this.attributeValue_,
        });
      }
      console.log("confirmAdd");
      console.log(this.addForm);
      this.addGoods(this.addForm)
        .then(() => {
          return this.getGoods();
        })
        .then(() => {
          this.addForm = {
            categoryId: "",
            name: "",
            price: "",
            num: "",
            description: "",
            commodityAttributeList: [],
            urls: [],
          };
          this.drawer = false;
          Message({
            type: "success",
            message: "添加成功",
          });
        })
        .catch((err) => {
          console.log(err);
          Message({
            message: "网络连接超时，请稍后重试",
            type: "error",
          });
        });
    },
    cancelAdd() {
      this.addForm = {
        categoryId: "",
        name: "",
        price: "",
        num: "",
        description: "",
        commodityAttributeList: [],
        urls: [],
      };
      this.drawer = false;
    },
    handleEdit(row) {
      this.editDialogVisible = true;
      this.editId = row.id;
      this.editForm = {
        categoryId: row.categoryId,
        name: row.name,
        price: row.price,
        num: row.num,
        description: row.description,
        commodityAttributeList: row.commodityAttributeList,
        urls: row.urls,
      };
    },
    confirmEdit() {
      if (this.attributeName_1 !== "" && this.attributeValue_1 !== "") {
        this.editForm.commodityAttributeList.push({
          name: this.attributeName_1,
          value: this.attributeValue_1,
        });
      }
      if (this.attributeName_2 !== "" && this.attributeValue_2 !== "") {
        console.log("属性2");
        this.editForm.commodityAttributeList.push({
          name: this.attributeName_2,
          value: this.attributeValue_2,
        });
      }
      if (this.attributeName_3 !== "" && this.attributeValue_3 !== "") {
        console.log("属性3");
        this.editForm.commodityAttributeList.push({
          name: this.attributeName_3,
          value: this.attributeValue_,
        });
      }
      const data = {
        id: this.editId,
        ...this.editForm,
      };
      this.editGoods(data)
        .then(() => {
          this.getGoods();
        })
        .then(() => {
          this.editDialogVisible = false;
          this.editForm = {
            categoryId: "",
            name: "",
            price: "",
            num: "",
            description: "",
            commodityAttributeList: [
              {
                name: "",
                value: "",
              },
            ],
            urls: [],
          };
          Message({
            type: "success",
            message: "编辑成功",
          });
        })
        .catch((err) => {
          console.log(err);
          Message({
            message: "网络连接超时，请稍后重试",
            type: "error",
          });
        });
    },
    cancelEdit() {
      this.editDialogVisible = false;
      this.editForm = {
        categoryId: "",
        name: "",
        price: "",
        num: "",
        description: "",
        commodityAttributeList: [
          {
            name: "",
            value: "",
          },
        ],
        urls: [],
      };
    },
    handleComment(row) {
      this.getComments(row.id)
        .then(() => {
          this.commentDialogVisible = true;
          this.currentCommentGoodsId = row.id;
        })
        .catch((err) => {
          console.log(err);
          Message({
            message: "网络连接超时，请稍后重试",
            type: "error",
          });
        });
    },
    handleCommentDelete(row) {
      this.commentId = row.id;
      this.deleteCommentDialogVisible = true;
    },
    confirmCommentDelete() {
      this.deleteComment(this.commentId)
        .then(() => {
          return this.getComments(this.currentCommentGoodsId);
        })
        .then(() => {
          this.deleteCommentDialogVisible = false;
          Message({
            type: "success",
            message: "删除成功",
          });
        })
        .catch((err) => {
          console.log(err);
          Message({
            message: "网络连接超时，请稍后重试",
            type: "error",
          });
        });
    },
    handleReply(row) {
      this.replyCommentDialogVisible = true;
      this.replyCommentId = row.id;
    },
    confirmCommentReply() {
      const data = {
        id: this.replyCommentId,
        reply: this.reply,
      };
      return this.replyComment(data)
        .then(() => {
          return this.getComments(this.currentCommentGoodsId);
        })
        .then(() => {
          this.replyCommentDialogVisible = false;
          this.reply = "";
          Message({
            type: "success",
            message: "回复成功",
          });
        })
        .catch((err) => {
          console.log(err);
          Message({
            message: "网络连接超时，请稍后重试",
            type: "error",
          });
        });
    },
    handleSuccess(response, file, fileList) {
      this.addForm.urls = response.data;
    },
    handleSuccess_2(response, file, fileList) {
      this.editForm.urls = response.data;
    },
  },
  computed: {
    ...mapState("Type", ["typeList"]),
    ...mapState("Goods", [
      "goods",
      "pageData",
      "loading",
      "comments",
      "commentLoading",
      "commentPageData",
    ]),
  },
  mounted() {
    this.getGoods().catch((err) => {
      console.log(err);
      Message({
        message: "网络连接超时，请稍后重试",
        type: "error",
      });
    });

    let temp;
    // 先获取全部父级分类
    this.getTypeListById(-1)
      .then(() => {
        this.typeList.forEach((item) => {
          temp = {
            ...item,
            chiledren: [],
          };
          this.fatherTypeList.push(temp);
        });
      })
      .then(() => {
        // 获取父子分类的子分类
        return this.fatherTypeList.forEach((item) => {
          this.getTypeListById(item.id).then(() => {
            item.chiledren = this.typeList;
          });
        });
      })
      .catch((err) => {
        console.log(err);
        Message({
          message: "网络连接超时，请稍后重试",
          type: "error",
        });
      });
  },
};
</script>

<style scoped lang="less">
@interval: 30px;

#goods {
  .searchBar {
    position: relative;
    margin-bottom: @interval;
    .el-input,
    .el-cascader,
    .el-select {
      margin-left: 25px;
      margin-right: 50px;
      width: 221px;
    }
    .el-button {
      position: absolute;
      right: 30px;
    }
    .resetBtn {
      right: 150px;
    }
  }

  .table {
    margin-bottom: @interval;
    max-height: 870px;
  }

  .block {
    position: absolute;
    right: 20px;
  }
  .dataBar {
    position: relative;
    margin-bottom: @interval;
    .el-button {
      position: absolute;
      top: 20%;
      right: 20px;
    }
  }
  z .addBtn {
    position: absolute;
    right: 40px;
    top: 20%;
  }
  .formBtnContainer {
    display: flex;
    margin-top: 10px;
    .el-button {
      flex: 1;
    }
  }
  /deep/.el-drawer__body {
    padding-left: 40px;
    padding-right: 40px;
  }
  .el-cascader,
  .el-select {
    margin-left: 25px;
    margin-right: 50px;
  }
  .commentBtn {
    width: 100%;
    .el-button {
      width: 150px;
    }
  }
  .attributeBar {
    display: flex;
  }
  .attributeNameInput {
    width: 50%;
  }
  .attributeValueInput {
    position: relative;
    left: 50px;
    width: 50%;
  }
  .attributeLabel {
    display: block;
  }
  .goodsImg,
  .commentImg {
    width: 80px;
  }
  .paginationContainer {
    position: relative;
    margin: 30px 0 20px 0;
    left: 75%;
  }
}
</style>