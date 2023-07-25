<template>
  <q-footer :style="footerStyle" class="row items-center justify-center">
    <div :style="footerContainerStyle" class="row items-center bg-deep-purple">
      <div class="col">left</div>

      <div class="col">center</div>
    </div>
  </q-footer>
</template>

<script setup>
import { ref } from 'vue';

const footerStyle = ref(['height: 30px', 'background-color: #FFF']);
const footerContainerStyle = ref(['width: 1350px']);
</script>

<style lang="scss" scoped></style>
