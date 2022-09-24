<script setup lang="ts">
import axios from "axios";
import {ref} from "vue";
import {useRouter} from "vue-router";

const router = useRouter()

const posts = ref([]); //반응형 데이터로 설정해줘야함(ref)

axios.get("/api/posts?page=1&size=5").then((response) => {
  response.data.forEach((r: any) => {
    posts.value.push(r);
  });
});
//console.log(response);
/*const moveToRead = () =>{
  router.push({name:"read"});
}*/

</script>

<template>
  <main>
    <ul>
<!--      <li v-for="post in posts" :key="post.id" @click="moveToRead()">-->
      <li v-for="post in posts" :key="post.id"> <!-- 제목 클릭시 이동으로 수정 -->
        <div class="title">
          <router-link :to="{name: 'read', params:{postId: post.id}}">{{ post.title }} </router-link> <!-- 클릭 시 이동-->
        </div>
        <div class="content">
          {{ post.content }}
        </div>

        <div class="sub d-flex">
          <div class="category">테스트</div>
          <div class="category">1999-01-01</div>
        </div>
      </li>
    </ul>
  </main>
</template>

<style scoped lang="scss">
ul{
  list-style: none;
  padding: 0;
  li {
    margin-bottom: 1.8rem;
    .title{
      a {
        font-size: 1.1rem;
        color:#383838;
        text-decoration: none;
      }
      &:hover{
        text-decoration: underline;
      }
    }
    .content{
      font-size: 0.85rem;
      margin-top: 8px;
      color:#7e7e7e;
    }
    &:last-child {
      margin-bottom: 0;
    }
    .sub{
      margin-top: 9px;
      font-size: 0.78rem;
      .regDate{
        margin-left: 10px;
        color: #6b6b6b;
      }
    }
  }
}


</style>

