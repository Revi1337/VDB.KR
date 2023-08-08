<template>
  <q-btn
    :style="{ height: height, borderRadius: borderRadius }"
    :type="type"
    :ripple="false"
    :label="label"
    :outline="outline"
    :flat="flat"
    :v-close-popup="vClosePopup"
    :color="color"
    @click="movePage({ name: to })"
    :href="href"
  >
    <slot></slot>
  </q-btn>
</template>

<script setup>
import { useRouter } from 'vue-router';

const props = defineProps({
  label: {
    type: String,
    required: false
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
  },
  color: {
    type: String,
    required: false,
    default: 'primary'
  },
  href: {
    type: String,
    required: false
  },
  height: {
    type: String,
    required: false,
    default: '42px'
  },
  borderRadius: {
    type: String,
    required: false,
    default: '3px'
  }
});

const router = useRouter();
const movePage = obj => router.push(obj);
</script>

<style lang="scss" scoped>
.header-button {
  height: 42px;
  border-radius: 3px;
}
</style>
