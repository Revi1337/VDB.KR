<template>
  <q-dialog @update:model-value="layout">
    <q-card
      flat
      class="login-container row items-center justify-center q-px-sm q-pt-md"
    >
      <q-card-section>
        <LogoComponent />
      </q-card-section>

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
            <ButtonComponent outline type="submit" label="Sign In" />
          </div>

          <q-separator inset />

          <div class="q-mt-sm q-gutter-y-md q-pb-xl">
            <q-btn
              class="login-component"
              color="secondary"
              :ripple="false"
              outline
              href="/api/oauth2/google"
            >
              <div class="row items-center" style="width: 200px">
                <q-icon
                  class="col-4"
                  color="secondary"
                  size="16px"
                  name="fa-brands fa-google"
                />
                <span class="text-secondary">sign in google</span>
              </div>
            </q-btn>

            <q-btn
              class="login-component"
              color="secondary"
              :ripple="false"
              outline
              href="/api/oauth2/github"
            >
              <div class="row items-center" style="width: 200px">
                <q-icon
                  class="col-4"
                  color="secondary"
                  size="16px"
                  name="fa-brands fa-github"
                />
                <span class="text-secondary">sign in github</span>
              </div>
            </q-btn>
          </div>
        </div>
      </q-form>
    </q-card>
  </q-dialog>
</template>

<script setup>
import ButtonComponent from 'src/components/ButtonComponent.vue';
import LogoComponent from './LogoComponent.vue';
import { ref } from 'vue';
import { userLogin } from 'src/api/auth';

const props = defineProps({
  layout: {
    type: Boolean
  }
});

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
.login-container {
  width: 500px;
  border-radius: 8px;
}
.login-component {
  width: 350px;
}
</style>
