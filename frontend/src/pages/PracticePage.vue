<template>
  <InputFormComponent
    label="PRACTICE"
    outlined
    autofocus
    bottom-slots
    :error="formValue === 'asdf'"
    :model-value="formValue"
    @update:model-value="value => (formValue = value)"
  >
    <template #before>
      <q-icon name="email" />
    </template>

    <template #append>
      <ButtonComponent
        type="button"
        label="verify"
        flat
        @click="console.log('checkDuplicateEmail')"
      />
    </template>

    <template #error>
      <div>THIS IS ERROR</div>
    </template>
  </InputFormComponent>

  <!-- <InputFormComponent
    label="PRACTICE"
    outlined
    autofocus
    :model-value="formValue"
    @update:model-value="value => (formValue = value)"
    /> -->
</template>

<script setup>
import InputFormComponent from 'src/components/InputFormComponent.vue';
import { ref } from 'vue';

const formValue = ref('');
</script>
