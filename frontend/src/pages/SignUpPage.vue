<template>
  <q-page class="row">
    <div class="col-6 flex flex-center">
      <div class="col-4">
        <q-icon name="img:icons/SignUp.jpg" alt="SignLogo" size="38rem" />
      </div>
    </div>
    <div class="col-6 flex flex-center">
      <q-card class="q-pa-lg q-pt-xl">
        <q-form @submit.prevent="userJoinRequest">
          <div class="login-input q-gutter-y-sm">
            <p
              class="text-center text-darkpurple"
              :style="{ fontSize: '35px', fontWeight: 900 }"
            >
              SignUp
            </p>
            <Input
              autofocus
              outlined
              label="Username"
              :spellcheck="false"
              no-error-icon
              bottom-slots
              :error="v$.username.$errors.length != 0"
              v-model="userJoinData.username"
            >
              <template #before>
                <q-icon name="fa-solid fa-user" />
              </template>

              <template #append>
                <Button
                  type="button"
                  label="verify"
                  flat
                  @click="console.log('checkDuplicateUsername')"
                />
              </template>

              <template #error>
                <p v-for="error in v$.username.$errors" :key="error.$uid">
                  {{ error.$message }}
                </p>
              </template>
            </Input>

            <Input
              outlined
              label="Email"
              :spellcheck="false"
              no-error-icon
              bottom-slots
              :error="v$.email.$errors.length != 0"
              v-model="userJoinData.email"
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
                <p v-for="error in v$.email.$errors" :key="error.$uid">
                  {{ error.$message }}
                </p>
              </template>
            </Input>

            <Input
              :type="isPwd ? 'password' : 'text'"
              outlined
              label="Password"
              :spellcheck="false"
              no-error-icon
              bottom-slots
              :error="v$.password.$errors.length != 0"
              v-model="userJoinData.password"
            >
              <template #before>
                <q-icon name="fa-solid fa-lock" />
              </template>

              <template #append>
                <q-icon
                  :name="isPwd ? 'visibility_off' : 'visibility'"
                  class="cursor-pointer"
                  @click="isPwd = !isPwd"
                />
              </template>

              <template #error>
                <p v-for="error in v$.password.$errors" :key="error.$uid">
                  {{ error.$message }}
                </p>
              </template>
            </Input>

            <Input
              :type="isPwd ? 'password' : 'text'"
              outlined
              label="Password Confirm"
              :spellcheck="false"
              no-error-icon
              bottom-slots
              :error="v$.passwordConfirm.$errors.length != 0"
              v-model="userJoinData.passwordConfirm"
            >
              <template #before>
                <q-icon name="fa-solid fa-lock" />
              </template>

              <template #append>
                <q-icon
                  :name="isPwd ? 'visibility_off' : 'visibility'"
                  class="cursor-pointer"
                  @click="isPwd = !isPwd"
                />
              </template>

              <template #error>
                <p
                  v-for="error in v$.passwordConfirm.$errors"
                  :key="error.$uid"
                >
                  {{ error.$message }}
                </p>
              </template>
            </Input>

            <div class="text-right">
              <Button type="submit" label="Sign Up" outline />
            </div>
          </div>
        </q-form>
      </q-card>
    </div>
  </q-page>

  <q-dialog v-model="waitingModal" persistent>
    <q-spinner color="primary" size="3em" />
  </q-dialog>
</template>

<script setup>
import AppCard from 'src/components/AppCard.vue';
import Button from 'components/Button.vue';
import Input from 'components/Input.vue';
import { userJoin } from 'src/api/auth';
import { ref, computed, watch } from 'vue';
import { useVuelidate } from '@vuelidate/core';
import {
  email,
  required,
  minLength,
  maxLength,
  sameAs
} from '@vuelidate/validators';

// switch
const waitingModal = ref(false);
const isPwd = ref(true);

// Login
const userJoinData = ref({
  username: '',
  email: '',
  password: '',
  passwordConfirm: ''
});

// 1. Vuelidate Setup
const rules = computed(() => {
  return {
    username: {
      required,
      minLength: minLength(3),
      maxLength: maxLength(20)
    },
    email: { required, email },
    password: { required, minLength: minLength(3), maxLength: maxLength(20) },
    passwordConfirm: { required, sameAs: sameAs(userJoinData.value.password) }
  };
});

// 2. Create v$ via useVuelidate()
const v$ = useVuelidate(rules, userJoinData);

// 3. Start Validate
// Validate Username
const isUsernameValid = ref(true);
const usernameValidate = async () =>
  ({ validateResult: isUsernameValid.value } =
    await v$.value.username.$validate());
watch(
  () => userJoinData.value.username,
  () => usernameValidate()
);

// Validate Email
const isEmailValid = ref(true);
const emailValidate = async () =>
  ({ validateResult: isEmailValid.value } = await v$.value.email.$validate());
watch(
  () => userJoinData.value.email,
  () => emailValidate()
);

// Validate Password
const isPasswordValid = ref(true);
const passwordValidate = async () =>
  ({ validateResult: isPasswordValid.value } =
    await v$.value.password.$validate());
watch(
  () => userJoinData.value.password,
  () => passwordValidate()
);

// Validate PasswordConfirm
const isPasswordConfirmValid = ref(true);
const passwordConfirmValidate = async () =>
  ({ validateResult: isPasswordConfirmValid.value } =
    await v$.value.passwordConfirm.$validate());
watch(
  () => userJoinData.value.passwordConfirm,
  () => passwordConfirmValidate()
);

// Validate All()
const validateAll = async () => {
  return await v$.value.$validate();
};

const userJoinRequest = async () => {
  const result = await validateAll();
  if (!result) {
    alert('Validation Failed');
    return;
  }
  try {
    waitingModal.value = true;
    const { data } = await userJoin(userJoinData.value);
  } catch (error) {
    console.log(error);
  } finally {
    waitingModal.value = false;
  }
};
</script>

<style lang="scss" scoped>
* {
  color: #030328;
}
.login-container {
  width: 700px;
}
.login-input {
  width: 350px;
}
</style>
