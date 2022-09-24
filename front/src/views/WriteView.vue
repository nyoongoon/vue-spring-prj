<script setup lang="ts">
import {ref} from "vue"
import axios from 'axios';
import HomeView from "@/views/HomeView.vue";
import {useRouter} from "vue-router";

const title = ref("");
const content = ref("");
const router = useRouter();


const write = () => {
  axios.post("/api/posts",{
    title: title.value,
    content: content.value
  }).then(()=>{
    //router.push({name : "home"}); //데이터 전송 후 라우터 이동 //router/index에 등록한 name값
    router.replace({name : "home"}); //데이터 전송 후 라우터 이동 //replace하면 뒤로가기 막기
  })

}
</script>

<template>

  <div>
    <el-input v-model="title" placeholder="제목을 입력해주세요"/>
  </div>
  <div class="mt-2">
    <el-input v-model="content" type="textarea" row="15"/>
  </div>

  <div class="mt-2">
    <div class="d-flex justify-content-end">
      <el-button type="primary" @click="write()">글 작성완료</el-button>
    </div>

  </div>
</template>

<style>

</style>