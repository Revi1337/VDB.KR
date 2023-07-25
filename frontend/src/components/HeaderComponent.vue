<template>
  <q-header :style="headerStyle" class="row items-center justify-center">
    <div :style="headerContainerStyle">
      <div class="row items-center justify-between">
        <div class="col-auto row items-center q-mr-xl">
          <div class="row items-center">
            <slot name="header-logo">
              <LogoComponent @click="$router.push({ name: 'Index' })" />
            </slot>
          </div>

          <slot name="header-content"></slot>
        </div>

        <div class="q-gutter-x-sm">
          <slot name="header-left"> </slot>
        </div>
      </div>
    </div>
  </q-header>
</template>

<script setup>
import { ref } from 'vue';
import LogoComponent from 'components/LogoComponent.vue';

const headerStyle = ref([
  'height: 82px',
  'background-color: rgba(255, 255, 255, 0.98)'
]);
const headerContainerStyle = ref(['width: 1350px']);
</script>
