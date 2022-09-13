<script setup lang="ts">
import {ref} from "vue"
import axios from 'axios';
import HomeView from "@/views/HomeView.vue";

const title = ref("");
const content = ref("");

const write = () => {
  axios.post("/api/posts",{
    title: title.value,
    content: content.value
  }); //요청 테스트

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
    <el-button type="primary" @click="write()">글 작성완료</el-button>
  </div>
</template>

<style>

</style>