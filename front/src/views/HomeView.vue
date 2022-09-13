<script setup lang="ts">
import axios from "axios";
import {ref} from "vue";

const posts = ref([]); //반응형 데이터로 설정해줘야함(ref)

axios.get("/api/posts?page=1&size=5").then((response) => {
  response.data.forEach((r: any) => {
    posts.value.push(r);
  });
});
//console.log(response);

</script>

<template>
  <main>
    <ul>
      <li v-for="post in posts" :key="post.id">
        <div>
          {{ post.title }}
        </div>
        <div>
          {{ post.content }}
        </div>
      </li>
    </ul>
  </main>
</template>

