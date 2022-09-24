<script setup lang="ts">
import {defineProps, onMounted, ref} from "vue";
import axios from "axios";
import {useRouter} from "vue-router";

const props = defineProps({ //넘어온 데이터 (프로퍼티) 정의하기
  postId: {
    type: [Number, String],
    require: true,
  },
});

const post = ref({//초기화 데이터 만들기
  id: 0,
  title: "",
  content: "",
});

const router = useRouter();

const moveToEdit = () => {
  router.push({name: "edit", params: {postId: props.postId}})
}

onMounted(() => { // 준비되면 메소드 실행
  axios.get(`/api/posts/${props.postId}`).then((response) => {
    post.value = response.data;
  });
});

</script>
<template>
  <el-row>
    <el-col>
      <h2>{{ post.title }}</h2>
      <div class="sub d-flex">
        <div class="category">테스트</div>
        <div class="category">1999-01-01</div>
      </div>

    </el-col>
  </el-row>

  <el-row class="mt-3">
    <el-col>
      <div class="content">{{ post.content }}</div>
    </el-col>
  </el-row>

  <el-row class="mt-3">
    <el-col>
      <div class="d-flex justify-content-end">
        <el-button type="warning" @click="moveToEdit()">수정</el-button>
      </div>
    </el-col>
  </el-row>
</template>

<style scoped lang="scss">
.title {
  font-size: 1.6rem;
  font-weight: 600;
  color: #383838;
  margin:0;
}

.sub{
  margin-top: 10px;
  font-size: 0.78rem;
  .regDate{
    margin-left: 10px;
    color: #6b6b6b;
  }
}

.content {
  font-size: 0.95rem;
  margin-top: 12px;
  color: #7e7e7e;
  white-space: break-spaces;
  line-height: 1.5;
}
</style>