<template>
  <q-btn-dropdown
    class="header-button"
    :ripple="false"
    flat
    auto-close
    color="primary"
    :label="label"
  >
    <slot></slot>
  </q-btn-dropdown>
</template>

<script setup>
const props = defineProps({
  label: {
    type: String,
    required: true
  }
});
</script>

<style lang="scss" scoped>
.header-button {
  height: 40px;
}
</style>
