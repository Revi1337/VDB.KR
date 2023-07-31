<template>
  <q-page-container class="row items-center justify-center">
    <div :style="pageStyle">
      <router-view />
    </div>
  </q-page-container>
</template>

<script setup>
import { ref } from 'vue';

const pageStyle = ref(['width: 1350px', 'height: 1500px']);
</script>
