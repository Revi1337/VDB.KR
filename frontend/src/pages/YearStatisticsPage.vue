<template>
  <div>Year Statitics</div>
  <div>
    {{ $route.params.year }}
  </div>
</template>

<script setup>
import { useRoute, useRouter } from 'vue-router';

const route = useRoute();

// Setup
console.log('setup');
console.log(`Fetching ${route.params.year} Data..`);
</script>

<style lang="scss" scoped></style>
