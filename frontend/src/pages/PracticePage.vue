<template>
  <Input
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
      <Button
        type="button"
        label="verify"
        flat
        @click="console.log('checkDuplicateEmail')"
      />
    </template>

    <template #error>
      <div>THIS IS ERROR</div>
    </template>
  </Input>

  <!-- <Input
    label="PRACTICE"
    outlined
    autofocus
    :model-value="formValue"
    @update:model-value="value => (formValue = value)"
    /> -->
</template>

<script setup>
import Button from 'src/components/Button.vue';
import Input from 'src/components/Input.vue';
import { ref } from 'vue';

const formValue = ref('');
</script>
