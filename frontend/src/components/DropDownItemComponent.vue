<template>
  <q-list>
    <template v-for="(value, index) in contents" :key="index">
      <q-item :clickable="value.clickable">
        <q-item-section>
          <q-item-label>{{ value.label }}</q-item-label>
        </q-item-section>
      </q-item>
    </template>
  </q-list>
</template>

<script setup>
const props = defineProps({
  contents: {
    type: Array[Object],
    required: false
  }
});
</script>

<style lang="scss" scoped></style>
