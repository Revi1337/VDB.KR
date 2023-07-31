<template>
  <div class="card" :style="style">
    <div v-if="$slots.header">
      <slot name="header">CARD TITLE</slot>
    </div>

    <div v-if="$slots.default">
      <slot>CARD CONTENT</slot>
    </div>

    <div v-if="$slots.footer">
      <slot name="footer">CARD FOOTER</slot>
    </div>
  </div>
</template>

<script setup>
const props = defineProps({
  width: {
    type: String,
    required: false,
    default: '100%'
  },
  height: {
    type: String,
    required: false,
    default: '700px'
  },
  backgroundColor: {
    type: String,
    required: false,
    default: '#f6f4fa'
  },
  style: {
    type: Object,
    requried: false
  }
});
</script>

<style lang="scss" scoped>
.card {
  border-radius: 10px;
}
</style>
