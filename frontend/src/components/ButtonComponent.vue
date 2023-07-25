<template>
  <q-btn
    :type="type"
    :ripple="false"
    :label="label"
    :outline="outline"
    :flat="flat"
    :v-close-popup="vClosePopup"
    color="primary"
    @click="movePage({ name: to })"
    class="header-button"
  />
</template>

<script setup>
import { useRouter } from 'vue-router';

const props = defineProps({
  label: {
    type: String,
    required: true
  },
  to: {
    type: String,
    required: false,
    default: ''
  },
  outline: {
    type: Boolean,
    required: false,
    default: false
  },
  flat: {
    type: Boolean,
    required: false,
    default: false
  },
  type: {
    type: String,
    required: false,
    default: ''
  },
  vClosePopup: {
    type: Boolean,
    required: false,
    default: false
  }
});

const router = useRouter();
const movePage = obj => router.push(obj);
</script>

<style lang="scss" scoped>
.header-button {
  height: 42px;
  border-radius: 5px;
}
</style>
