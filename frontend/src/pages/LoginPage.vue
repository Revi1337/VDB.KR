<template>
  <q-page class="flex flex-center">
    <q-card class="login-container row items-center justify-center q-pa-xl">
      <q-form @submit.prevent="userLoginRequest">
        <div class="login-component q-gutter-y-sm">
          <q-input
            autofocus
            v-model="signInData.email"
            outlined
            label="Email"
            spellcheck="false"
          />
          <q-input
            v-model="signInData.password"
            outlined
            label="Password"
            spellcheck="false"
            type="password"
          />
          <div class="text-right q-my-md">
            <Button outline type="submit" label="Sign In" />
          </div>

          <q-separator inset />

          <div class="q-mt-sm q-gutter-y-md">
            <q-btn
              class="login-component"
              color="primary"
              :ripple="false"
              outline
              href="/api/oauth2/google"
            >
              <div class="row items-center" style="width: 200px">
                <q-icon
                  class="col-4"
                  color="primary"
                  size="16px"
                  name="fa-brands fa-google"
                />
                <span class="text-primary">sign in google</span>
              </div>
            </q-btn>

            <q-btn
              class="login-component"
              color="primary"
              :ripple="false"
              outline
              href="/api/oauth2/github"
            >
              <div class="row items-center" style="width: 200px">
                <q-icon
                  class="col-4"
                  color="primary"
                  size="16px"
                  name="fa-brands fa-github"
                />
                <span class="text-primary">sign in github</span>
              </div>
            </q-btn>
          </div>
        </div>
      </q-form>
    </q-card>
  </q-page>
</template>

<script setup>
import Button from 'components/Button.vue';
import { userLogin } from 'src/api/auth';
import { ref } from 'vue';

const layout = ref(true);

const signInData = ref({
  email: '',
  password: ''
});
const userLoginRequest = async () => {
  try {
    const { data } = await userLogin(signInData.value);
    console.log(data);
  } catch (error) {
    console.log(error);
  }
};
</script>

<style lang="scss" scoped>
* {
  color: #030328;
}
.login-container {
  width: 500px;
  border-radius: 8px;
}
.login-component {
  width: 350px;
}
</style>
