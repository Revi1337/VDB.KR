<template>
  <q-input
    :type="type"
    :autofocus="autofocus"
    :outlined="outlined"
    :label="label"
    :spellcheck="spellcheck"
    :no-error-icon="noErrorIcon"
    :bottom-slots="bottomSlots"
    :error="error"
    v-model="value"
  >
    <template v-if="$slots.before" #before>
      <slot name="before">
        <div>BEFORE</div>
      </slot>
    </template>

    <template v-if="$slots.append" #append>
      <slot name="append">
        <div>APPEND</div>
      </slot>
    </template>

    <template v-if="$slots.error && bottomSlots && error" #error>
      <slot name="error">
        <div>ERROR</div>
      </slot>
    </template>
  </q-input>
</template>

<script setup>
import { computed } from 'vue';

const value = computed({
  get() {
    return props.modelValue;
  },
  set(value) {
    emit('update:modelValue', value);
  }
});

const emit = defineEmits(['update:modelValue']);

const props = defineProps({
  type: {
    type: String,
    required: false,
    default: 'text'
  },
  modelValue: {
    type: String,
    required: true
  },
  autofocus: {
    type: Boolean,
    required: false
  },
  outlined: {
    type: Boolean,
    required: true
  },
  label: {
    type: String,
    required: false
  },
  spellcheck: {
    type: Boolean,
    required: false,
    default: false
  },
  noErrorIcon: {
    type: Boolean,
    required: false
  },
  bottomSlots: {
    type: Boolean,
    required: false,
    default: false
  },
  error: {
    type: Boolean,
    required: false,
    default: false
  },
});
</script>
<!-- SUCCESS -->
<!-- <q-input
    autofocus
    outlined
    label="PRACTICE"
    :spellcheck="false"
    no-error-icon
    v-model="value"
  />
  <div>
    {{ value }}
  </div> -->
