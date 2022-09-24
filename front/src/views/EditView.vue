<script setup lang="ts">
import {ref} from "vue"
import {useRouter} from "vue-router";
import axios from "axios";

const title = ref("");
const content = ref("");
const router = useRouter();

const post = ref({//초기화 데이터 만들기
  id:0,
  title:"",
  content:"",
});

const props = defineProps({ //넘어온 데이터 받기
  postId: {
    type:[Number, String],
    require: true,
  },
});

axios.get(`/api/posts/${props.postId}`).then((response) => { //조회 req
  post.value = response.data;
});

const edit = ()=>{ //patch 메소드 사용 주의 // post.value 통째로 전달.
  axios.patch(`/api/posts/${props.postId}`, post.value).then((response) => {
   router.replace({name : "home"}) //완료시 홈으로
  });
}


</script>

<template>

  <div>
    <el-input v-model="post.title"/>
  </div>
  <div class="mt-2">
    <el-input v-model="post.content" type="textarea" row="15"/>
  </div>

  <div class="mt-2 d-flex justify-content-end">
    <el-button type="primary" @click="edit()">수정완료</el-button>
  </div>
</template>

<style>

</style>